package cipher;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class VigenereCipher extends AbstractCipher {
    /**
     * an array of CaesarCipher objects
     * with length equal to the key length
     */
    CaesarCipher[] ciphers;

    public VigenereCipher(String keyString) {
        List<Integer> key = new ArrayList<>();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        alphabet = alphabet.toLowerCase();
        keyString = keyString.toLowerCase();
        for (int i = 0; i < keyString.length(); i++) {
            int temp = alphabet.indexOf(keyString.substring(i, i + 1)) + 1;
            key.add(temp);
        }
        ciphers = new CaesarCipher[key.size()];
        for (int i = 0; i < key.size(); i++) {
            ciphers[i] = new CaesarCipher(key.get(i));
        }
    }

    @Override
    public String encrypt(String input) {
        StringBuilder answer = new StringBuilder();
        int i = 0;
        for (char c : input.toCharArray()) {
            int cipherIndex = i % ciphers.length; // cipherIndex as key
            CaesarCipher thisCipher = ciphers[cipherIndex];
            answer.append(thisCipher.encryptLetter(c));
            i++;
        }
        return answer.toString();
    }

    @Override
    public String decrypt(String input) {
        StringBuilder answer = new StringBuilder();
        int i = 0;
        for (char c : input.toCharArray()) {
            int cipherIndex = i % ciphers.length;
            CaesarCipher thisCipher = ciphers[cipherIndex];
            answer.append(thisCipher.decryptLetter(c));
            i++;
        }
        return answer.toString();
    }

    @Override
    public void save(OutputStream out) throws IOException {

    }

    @Override
    public String toString() {
        return Arrays.toString(ciphers);
    }
}
