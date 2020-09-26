package cipher;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class RandomSubstitutionCipher extends AbstractCipher {
	private String alphabet;
	private String shiftedAlphabet;

	public RandomSubstitutionCipher() {
		alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		alphabet = alphabet.toLowerCase();
		int[] randomInt = getSequence(0,25);
		char[] shifted = new char[26];
		char[] tempAlphabet = alphabet.toCharArray();
		for(int i = 1; i < randomInt.length; i++) {
			int temp = randomInt[i];
			shifted[i] = tempAlphabet[temp];
		}
		shiftedAlphabet = String.valueOf(shifted);
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
		return "" + shiftedAlphabet;
	}
	
	// randomly give a sequence of integers
	public static int[] getSequence(int minnum, int maxnum) {
		int num = maxnum - minnum + 1;
		int[] sequence = new int[num];
		for (int i = 0; i < num; i++) {
			sequence[i] = i + minnum;
		}
		Random random = new Random();
		for (int i = 0; i < num; i++) {
			int p = random.nextInt(num);
			int tmp = sequence[i];
			sequence[i] = sequence[p];
			sequence[p] = tmp;
		}
		random = null;
		return sequence;
	}

}
