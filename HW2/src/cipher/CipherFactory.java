package cipher;

import java.math.BigInteger;

/**
 * Factory class for creating cipher objects.
 */
public class CipherFactory {
    /**
     * Returns: a monoalphabetic substitution cipher with the English alphabet mapped to the
     * provided alphabet.<br>
     * Requires: {@code encrAlph} contains exactly one occurrence of each English letter and nothing
     * more. No requirement is made on case.
     *
     * @param encrAlph the encrypted alphabet
     */
    public Cipher getMonoCipher(String encrAlph) {
        return new MonoCipher(encrAlph); // TODO implement
    }

    /**
     * Returns a new Caesar cipher with the given shift parameter.
     *
     * @param shift the cipher's shift parameter
     */
    public Cipher getCaesarCipher(int shift) {
        return new CaesarCipher(shift);// TODO implement
    }

    /**
     * Returns a Vigenere cipher (with multiple shifts).
     *
     * @param key the cipher's shift parameters. Note that A is a shift of 1.
     */
    public Cipher getVigenereCipher(String key) {
        return new VigenereCipher(key); // TODO implement
    }

    /**
     * Returns a new monoalphabetic substitution cipher with a randomly generated mapping.
     */
    public Cipher getRandomSubstitutionCipher() {
        return new RandomSubstitutionCipher(); // TODO implement
    }

    /**
     * Returns a new RSA cipher with a randomly generated keys.
     */
    public Cipher getRSACipher() {
        return null; // TODO implement
    }

    /**
     * Returns a new RSA cipher with given key.
     *
     * @param n modulus
     * @param d decryption key
     * @param e encryption key
     */
    public Cipher getRSACipher(BigInteger n, BigInteger e, BigInteger d) {
        return null; // TODO implement
    }
}
