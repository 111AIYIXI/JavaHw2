package cipher;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class RSAChunkReader implements ChunkReader {

	private final int cs;
	private final InputStream input;

	/**
	 * Creates a chunk reader that reads from a specific input stream for use with
	 * the RSA cipher.
	 * 
	 * @param i    The InputStream to read data from
	 * @param size The size of each chunk
	 */
	public RSAChunkReader(InputStream i, int size) {
		input = i;
		cs = size;
	}

	@Override
	public int chunkSize() {
		return cs;
	}

	@Override
	public boolean hasNext() {
		try {
			return input.available() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public int nextChunk(byte[] data) throws EOFException, IOException {
		int numBytesRead = input.read(data, 0, cs);

		if (numBytesRead == -1) {
			throw new EOFException();
		}

		return numBytesRead;
	}

}
