package cipher;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

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

    static {
        factory = new CipherFactory();
    }

    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.parseOutputOptions(args, main.parseCipherFunction(args, main.parseCipherType(args, 0)));
        } catch (IllegalArgumentException e) {
            System.out.println("Usage: java -jar <YOUR_JAR> <CIPHER_TYPE> <CIPHER_FUNCTION> <OUTPUT_OPTIONS>");
        }
    }

    private void thr() throws IllegalArgumentException {
        System.err.println();
        throw new IllegalArgumentException();
    }

    private void unknown(String cmdFlag, String flagType) {
        System.err.printf("Unknown flag \"%s\" of type \"%s\".", cmdFlag, flagType);
        thr();
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
                // TODO check the above variant encrAlph
                // TODO Better literally add some check codes here
                // TODO same as "if (encrAlph == xxxxx)"
                cipher = factory.getMonoCipher(encrAlph);
                break;
            case "--vigenere":
                legit(pos, args.length, "--vigenere");
                String key = args[pos++];
                // TODO check the key same like mono
                cipher = factory.getVigenereCipher(key);
                break;
            case "--vigenereLoad":
                legit(pos, args.length, "--vigenereLoad");
                String filePath = args[pos++];
                try {
                    InputStream in = new FileInputStream(filePath);
                    byte[] bytes = new byte[in.available()];
                    int len = in.read(bytes);
                    key = Arrays.toString(bytes);
                    break;
                } catch (IOException e) {
                    System.err.printf("No such file: \"%s\".", filePath);
                    thr();
                }
            case "--rsa":
                // TODO create new RSA cipher
                break;
            case "--rsaLoad":
                // TODO load an RSA key from the given file
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
                // TODO encrypt the given string
                break;
            case "--ef":
                // TODO encrypt the contents of the given file
                break;
            case "--dm":
                // TODO decrypt the given string -- substitution ciphers only
                break;
            case "--df":
                // TODO decrypt the contents of the given file
                break;
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

        String cmdFlag;
        while (pos < args.length) {
            switch (cmdFlag = args[pos++]) {
                case "--print":
                    // TODO print result of applying the cipher to the console -- substitution
                    // ciphers only
                    break;
                case "--out":
                    // TODO output result of applying the cipher to a file
                    break;
                case "--save":
                    // TODO save the cipher key to a file
                    break;
                default:
                    unknown(cmdFlag, flagType);
            }
        }
        return pos;
    }
}
