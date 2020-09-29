package cipher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Command line interface to allow users to interact with your ciphers.
 *
 * <p>We have provided some infrastructure to parse most of the arguments. It is your responsibility
 * to implement the appropriate actions according to the assignment specifications. You may choose
 * to "fill in the blanks" or rewrite this class.
 *
 * <p>Regardless of which option you choose, remember to minimize repetitive code. You are welcome
 * to add additional methods or alter the provided code to achieve this.
 */
public class Main {
    public static final CipherFactory factory;
    private Cipher cipher;
    private InputStream in;
    private String processType;
    private List<OutputStream> outList;
    private List<OutputStream> saveList;

    static {
        factory = new CipherFactory();
    }

    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.parseOutputOptions(args, main.parseCipherFunction(args, main.parseCipherType(args, 0)));
        } catch (IllegalArgumentException e) {
            System.out.println("Usage: java -jar <YOUR_JAR> <CIPHER_TYPE> <CIPHER_FUNCTION> <OUTPUT_OPTIONS>");
            return;
        }

        System.out.println("Processing...");

        try {
            main.process();
            System.out.println("Done.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Aborted.");
        }
    }

    private void process() throws IOException {
        for (OutputStream outputStream : outList) {
            in.reset();
            switch (processType) {
                case "encrypt":
                    cipher.encrypt(in, outputStream);
                    break;
                case "decrypt":
                    cipher.decrypt(in, outputStream);
                default:
                    break;
            }
            if (outputStream == System.out) System.out.println();
        }
        if (saveList != null) {
            for (OutputStream outputStream : saveList) {
                cipher.save(outputStream);
            }
        }
    }

    private FileOutputStream getFileOutputStream(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    throw new IOException();
                }
            }
            if (!file.canWrite()) {
                throw new IOException();
            }
            return new FileOutputStream(file);
        } catch (IOException e) {
            System.err.printf("An I/O error occurred due to \"%s\".", filePath);
            thr();
        }
        return null;
    }

    private void thr() throws IllegalArgumentException {
        System.err.println();
        throw new IllegalArgumentException();
    }

    private void unknown(String cmdFlag, String flagType) {
        System.err.printf("Unknown flag \"%s\" of type \"%s\".", cmdFlag, flagType);
        thr();
    }

    private void pure(String encrAlph) {
        Pattern pattern = Pattern.compile("^[A-Za-z]*$");
        if (!pattern.matcher(encrAlph).matches()) {
            System.err.printf("The given encrypted alphabet \"%s\" is malformed.", encrAlph);
            thr();
        }
    }

    private void exhausted(int pos, int length, String flagType) {
        if (pos == length) {
            System.err.printf("A proper flag of type \"%s\" is missing.", flagType);
            thr();
        }
    }

    private void legit(int pos, int length, String flag) {
        if (pos == length) {
            System.err.printf("No argument follows the flag \"%s\".", flag);
            thr();
        }
    }

    /**
     * Set up the cipher type based on the options found in args starting at position pos, and
     * return the index into args just past any cipher type options.
     */
    private int parseCipherType(String[] args, int pos) throws IllegalArgumentException {
        String flagType = "<CIPHER_TYPE>";
        exhausted(pos, args.length, flagType);  // check if arguments are exhausted

        String cmdFlag = args[pos++];
        switch (cmdFlag) {
            case "--caesar":
                legit(pos, args.length, "--caesar");
                try {
                    cipher = factory.getCaesarCipher(Integer.parseInt(args[pos++]));
                    break;
                } catch (NumberFormatException e) {
                    System.err.print("The argument that follows \"--caesar\" is illegal. The characters in the string must all be decimal digits, except that the first character may be an ASCII plus sign '+' ('\\u002B') to indicate a positive value.");
                    thr();
                }
            case "--random":
                cipher = factory.getRandomSubstitutionCipher();
                break;
            case "--monoLoad":
                legit(pos, args.length, "--monoLoad");
                String encrAlph = args[pos++];
                pure(encrAlph);
                cipher = factory.getMonoCipher(encrAlph);
                break;
            case "--vigenere":
                legit(pos, args.length, "--vigenere");
                String key = args[pos++];
                pure(key);
                cipher = factory.getVigenereCipher(key);
                break;
            case "--vigenereLoad":
                legit(pos, args.length, "--vigenereLoad");
                String filePath = args[pos++];
                try {
                    InputStream in = new FileInputStream(filePath);
                    byte[] bytes = new byte[in.available()];
                    int len = in.read(bytes);
                    key = new String(bytes);
                    pure(key);
                    cipher = factory.getVigenereCipher(key);
                    break;
                } catch (IOException e) {
                    System.err.printf("No such file: \"%s\".", filePath);
                    thr();
                }
            case "--rsa":
                cipher = factory.getRSACipher();
                break;
            case "--rsaLoad":
                // TODO load an RSA key from the given file
                cipher = factory.getRSACipher();
                break;
            default:
                unknown(cmdFlag, flagType);
        }
        return pos;
    }

    /**
     * Parse the operations to be performed by the program from the command-line arguments in args
     * starting at position pos. Return the index into args just past the parsed arguments.
     */
    private int parseCipherFunction(String[] args, int pos) throws IllegalArgumentException {
        String flagType = "<CIPHER_FUNCTION>";
        exhausted(pos, args.length, flagType);  // check if arguments are exhausted

        String cmdFlag = args[pos++];
        switch (cmdFlag) {
            case "--em":
                legit(pos, args.length, "--em");
                processType = "encrypt";
                in = new ByteArrayInputStream(args[pos++].getBytes());
                break;
            case "--ef":
                legit(pos, args.length, "--em");
                processType = "encrypt";
                String filePath = args[pos++];
                try {
                    in = new FileInputStream(filePath);
                    break;
                } catch (FileNotFoundException e) {
                    System.err.printf("No such file: \"%s\".", filePath);
                    thr();
                }
            case "--dm":
                legit(pos, args.length, "--em");
                processType = "decrypt";
                in = new ByteArrayInputStream(args[pos++].getBytes());
                break;
            case "--df":
                legit(pos, args.length, "--df");
                processType = "decrypt";
                filePath = args[pos++];
                try {
                    in = new FileInputStream(filePath);
                    break;
                } catch (FileNotFoundException e) {
                    System.err.printf("No such file: \"%s\".", filePath);
                    thr();
                }
            default:
                unknown(cmdFlag, flagType);
        }
        return pos;
    }

    /**
     * Parse options for output, starting within {@code args} at index {@code argPos}. Return the
     * index in args just past such options.
     */
    private int parseOutputOptions(String[] args, int pos) throws IllegalArgumentException {
        String flagType = "<OUTPUT_OPTIONS>";
        exhausted(pos, args.length, flagType);  // check if arguments are exhausted

        outList = new ArrayList<>();
        String cmdFlag;
        while (pos < args.length) {
            switch (cmdFlag = args[pos++]) {
                case "--print":
                    outList.add(System.out);
                    break;
                case "--out":
                    legit(pos, args.length, "--out");
                    outList.add(getFileOutputStream(args[pos++]));
                    break;
                case "--save":
                    legit(pos, args.length, "--save");
                    if (saveList == null) {
                        saveList = new ArrayList<>();
                    }
                    saveList.add(getFileOutputStream(args[pos++]));
                    break;
                default:
                    unknown(cmdFlag, flagType);
            }
        }
        return pos;
    }
}
