package cipher;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Random;
import java.util.StringTokenizer;

public class RSACipher extends AbstractCipher {
    /**
     * Bit length of each prime number.
     */
    int primeSize;

    /**
     * Two distinct large prime numbers p and q.
     */
    BigInteger p, q;

    /**
     * Modulus N.
     */
    BigInteger N;

    /**
     * r = ( p-1 )*( q-1 )
     */
    BigInteger r;

    /**
     * Public exponent E and Private exponent D
     */
    BigInteger E, D;

    String nt, dt, et;

    String publicKey;
    String privateKey;
    String randomNumber;

    BigInteger[] ciphertext;
    int m[] = new int[1000];
    String st[] = new String[1000];
    String str = "";
    String sarray1[] = new String[100000];

    StringBuffer sb1 = new StringBuffer();

    /**
     * Constructor.
     *
     * @param primeSize Bit length of each prime number.
     */
    public RSACipher(int primeSize) {
        this.primeSize = primeSize;
        generatePrimeNumbers();  // Generate two distinct large prime numbers p and q.
        generatePublicPrivateKeys();  // Generate Public and Private Keys.

        BigInteger publicKeyB = getE();
        BigInteger privateKeyB = getD();
        BigInteger randomNumberB = getN();
        publicKey = publicKeyB.toString();
        privateKey = privateKeyB.toString();
        randomNumber = randomNumberB.toString();
    }

    /**
     * Generate two distinct large prime numbers p and q.
     */
    public void generatePrimeNumbers() {
        p = new BigInteger(primeSize, 10, new Random());

        do {
            q = new BigInteger(primeSize, 10, new Random());
        } while (q.compareTo(p) == 0);
    }

    /**
     * Generate Public and Private Keys.
     */
    public void generatePublicPrivateKeys() {
        N = p.multiply(q);  // N = p * q

        r = p.subtract(BigInteger.valueOf(1));
        r = r.multiply(q.subtract(BigInteger.valueOf(1)));

        do {
            E = new BigInteger(2 * primeSize, new Random());  // Choose E, coprime to and less than r
        } while ((E.compareTo(r) > -1) || (E.gcd(r).compareTo(BigInteger.valueOf(1)) != 0));

        D = E.modInverse(r);  // Compute D, the inverse of E mod r
    }

    /**
     * Get prime number p.
     *
     * @return Prime number p.
     */
    public BigInteger getp() {
        return (p);
    }

    /**
     * Get prime number q.
     *
     * @return Prime number q.
     */
    public BigInteger getq() {
        return (q);
    }

    /**
     * Get r.
     *
     * @return r.
     */
    public BigInteger getr() {
        return (r);
    }

    /**
     * Get modulus N.
     *
     * @return Modulus N.
     */
    public BigInteger getN() {
        return (N);
    }

    /**
     * Get Public exponent E.
     *
     * @return Public exponent E.
     */
    public BigInteger getE() {
        return (E);
    }

    /**
     * Get Private exponent D.
     *
     * @return Private exponent D.
     */
    public BigInteger getD() {
        return (D);
    }

    public String rsaDecrypt(BigInteger[] encrypted, BigInteger D, BigInteger N, int size) {
        int i;
        String rs = "";
        BigInteger[] decrypted = new BigInteger[size];
        for (i = 0; i < decrypted.length; i++) {
            decrypted[i] = encrypted[i].modPow(D, N);
        }
        char[] charArray = new char[decrypted.length];
        byte[] byteArray = new byte[decrypted.length];
        for (i = 0; i < charArray.length; i++) {
            charArray[i] = (char) (decrypted[i].intValue());
            int iv;
            iv = decrypted[i].intValue();
            byteArray[i] = (byte) iv;
        }
        try {
            rs = new String(byteArray);
        } catch (Exception e) {
            System.out.println(e);
        }
        return (rs);
    }

    public BigInteger[] rsaEncrypt(String message) {
        int i;
        byte[] temp = new byte[1]; //
        byte[] digits = new byte[8];
        try {
            digits = message.getBytes();
            String ds = new String(digits);

            System.out.println("ds=" + ds);

        } catch (Exception e) {
            System.out.println(e);
        }
        BigInteger[] bigdigits = new BigInteger[digits.length];
        for (i = 0; i < bigdigits.length; i++) {
            temp[0] = digits[i];
            bigdigits[i] = new BigInteger(temp);
        }
        BigInteger[] encrypted = new BigInteger[bigdigits.length];
        for (i = 0; i < bigdigits.length; i++)
            encrypted[i] = bigdigits[i].modPow(E, N);

        return (encrypted);
    }

    @Override
    public String encrypt(String info) {
        E = new BigInteger(publicKey);
        N = new BigInteger(randomNumber);
        try {
            ciphertext = rsaEncrypt(info);
            for (int i = 0; i < ciphertext.length; i++) {
                m[i] = ciphertext[i].intValue();
                st[i] = String.valueOf(m[i]);
                sb1.append(st[i]);
                sb1.append(" ");
                str = sb1.toString();

            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return str;
    }

    @Override
    public String decrypt(String encryptedData) {
        D = new BigInteger(privateKey);
        N = new BigInteger(randomNumber);

        System.out.println("D = " + D);
        System.out.println("N = " + N);

        int k1 = 0;
        StringTokenizer st = new StringTokenizer(encryptedData);
        while (st.hasMoreTokens()) {
            sarray1[k1] = st.nextToken(" ");
            k1++;
        }

        BigInteger[] ciphertext1 = new BigInteger[100000];
        for (int i = 0; i < ciphertext1.length; i++) {
            ciphertext1[i] = new BigInteger(sarray1[i]);
        }
        String recoveredPlaintext = rsaDecrypt(ciphertext1, D, N, k1);
        System.out.println(recoveredPlaintext);
        return recoveredPlaintext;
    }

    @Override
    public void save(OutputStream out) throws IOException {

    }
}