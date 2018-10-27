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

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public class Checksum {

	private CRC32 crc = new CRC32();
	private byte[] buf = new byte[8192*2];

	public Checksum() {
	}

	/**
	 * Calculate a checksum for the stream.
	 * @param is InputStream the stream for which to calculate the checksum.
	 * @throws IOException problem in the I/O processing.
	 */
	public void updateCRCWithStream(InputStream is) throws IOException {
		while (true) {
			int i = is.read(buf);

			if (i == -1) {
				break;
			}

			this.crc.update(buf, 0, i);
		}
	}

	public long getChecksum() {
		return this.crc.getValue();
	}

}
