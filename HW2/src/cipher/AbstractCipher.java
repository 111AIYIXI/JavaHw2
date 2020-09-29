package cipher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A place to put some inherited code?
 */
public abstract class AbstractCipher implements Cipher {
    @Override
    public void encrypt(InputStream in, OutputStream out) throws IOException {
        byte[] bytes = new byte[in.available()];
        int len = in.read(bytes);
        String text = new String(bytes);
        String cipher = encrypt(text);
        out.write(cipher.getBytes());
        out.flush();
    }

    @Override
    public void decrypt(InputStream in, OutputStream out) throws IOException {
        byte[] bytes = new byte[in.available()];
        int len = in.read(bytes);
        String text = new String(bytes);
        String plain = decrypt(text);
        out.write(plain.getBytes());
        out.flush();
    }
}
