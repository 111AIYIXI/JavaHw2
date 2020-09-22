package cipher;

import java.io.IOException;
import java.io.OutputStream;

public class CaesarCipher extends AbstractCipher {
    private String alphabet;
    private String shiftedAlphabet;
    private int theKey;

    public CaesarCipher(int key) {
        theKey = key;
        alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        shiftedAlphabet = alphabet.substring(key) + alphabet.substring(0, key);
        alphabet = alphabet + alphabet.toLowerCase();
        shiftedAlphabet = shiftedAlphabet + shiftedAlphabet.toLowerCase();
    }

    private char transformLetter(char c, String from, String to) {
        int idx = from.indexOf(c);
        if (idx != -1) {
            return to.charAt(idx);
        }
        return c;
    }

    public char encryptLetter(char c) {
        return transformLetter(c, alphabet, shiftedAlphabet);
    }

    public char decryptLetter(char c) {
        return transformLetter(c, shiftedAlphabet, alphabet);
    }

    private String transform(String input, String from, String to) {
        StringBuilder sb = new StringBuilder(input);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            c = transformLetter(c, from, to);
            sb.setCharAt(i, c);
        }
        return sb.toString();
    }

    @Override
    public String encrypt(String plaintext) {
        return transform(plaintext, alphabet, shiftedAlphabet);
    }

    @Override
    public String decrypt(String ciphertext) {
        return transform(ciphertext, shiftedAlphabet, alphabet);
    }

    @Override
    public void save(OutputStream out) throws IOException {

    }

    @Override
    public String toString() {
        return "" + theKey;
    }
}
