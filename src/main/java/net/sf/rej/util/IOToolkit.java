/* Copyright (C) 2004-2007 Sami Koivu
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sf.rej.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOToolkit {

	public static byte[] readStream(int length, InputStream is)
			throws IOException {
		int read = 0; // count of already read bytes
		byte[] buf = new byte[length]; // byte array to read into

		while (read < length) { // read until length bytes have been read
			int i = is.read(buf, read, length - read); // try to read as many
			// bytes as there are
			// left
			if (i == -1)
				throw new IOException("No more bytes left.");
			read += i; // increase count with the actual # of bytes read
		}

		return buf;
	}

	private static final byte[] buf = new byte[4096];

	/**
	 * Fully reads the contents of the given stream.
	 * @param is Stream to read
	 * @return Contents of the stream.
	 * @throws IOException Error reading from the stream.
	 */
	public static byte[] readStream(InputStream is)
			throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		while (true) {
			int count = is.read(buf);

			if (count == -1) { // end of stream
				break;
			}

			baos.write(buf, 0, count);
		}

		return baos.toByteArray();
	}

	public static void writeStream(InputStream is, OutputStream os)
			throws IOException {
		byte[] buf = new byte[4096]; // byte array to read into

		while (true) { // read until length bytes have been read
			int i = is.read(buf); // try to read as many bytes as there are
			// left
			if (i == -1)
				break;
			os.write(buf, 0, i);
		}
	}

	/**
	 * Compares two <code>InputStream</code>s for equality. Both streams are
	 * read and the content is compared.
	 *
	 * @param isA
	 *            first stream to compare
	 * @param isB
	 *            second stream to compare
	 * @return true if the streams have equal data, false otherwise.
	 * @throws IOException
	 *             a problem in the I/O processing.
	 */
	public static boolean areEqual(InputStream isA, InputStream isB)
			throws IOException {
		byte[] bufA = new byte[256]; // byte array to read into
		byte[] bufB = new byte[256]; // byte array to read into

		while (true) {
			int countA = isA.read(bufA);
			int countB = isB.read(bufB);

			if (countA != countB) {
				return false; // early return
			}

			if (countA == -1)
				break;
			// countB is automatically -1 too, because of the previous if

			for (int i = 0; i < countA; i++) {
				if (bufA[i] != bufB[i])
					return false; // early return
			}
		}

		return true;
	}

}
