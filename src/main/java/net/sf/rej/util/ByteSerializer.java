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

/**
 * A class that wraps a byte array for stream-type writing.
 *
 * @author Sami Koivu
 */

public class ByteSerializer {

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private boolean bigEndian = false;

    public ByteSerializer(boolean bigEndian) {
        this.bigEndian = bigEndian;
    }

    public byte[] getBytes() {
        return this.baos.toByteArray();
    }

    public void addBytes(byte[] data) {
        addBytes(data, 0, data.length);
    }

    public void addBytes(byte[] data, int length) {
        addBytes(data, 0, length);
    }

    public void addBytes(byte[] data, int offset, int length) {
        this.baos.write(data, offset, length);
    }

    public void addByte(int b) {
        this.baos.write(b);
    }

    public void addShort(int l) {
        if (l > 0xffff)
            throw new RuntimeException(
                    "ByteSerializer.addShort(long) does not support integers this big: "
                            + l);

//        byte[] data = ByteToolkit.longToTwoBytes(i, this.bigEndian);
//        addBytes(data);
		if (this.bigEndian) {
	        baos.write((byte)(l >>>  8));
	        baos.write((byte)(l >>>  0));
		} else {
	        baos.write((byte)(l >>>  0));
	        baos.write((byte)(l >>>  8));
		}
    }

    public void addInt(int l) {
//        byte[] data = ByteToolkit.longToFourBytes(l, this.bigEndian);
//        addBytes(data);
		if (this.bigEndian) {
	        baos.write((byte)(l >>> 24));
	        baos.write((byte)(l >>> 16));
	        baos.write((byte)(l >>>  8));
	        baos.write((byte)(l >>>  0));
		} else {
	        baos.write((byte)(l >>>  0));
	        baos.write((byte)(l >>>  8));
	        baos.write((byte)(l >>> 16));
	        baos.write((byte)(l >>> 24));
		}
    }

    public int size() {
        return this.baos.size();
    }


	public void addLong(long l) {
		if (this.bigEndian) {
	        baos.write((byte)(l >>> 56));
	        baos.write((byte)(l >>> 48));
	        baos.write((byte)(l >>> 40));
	        baos.write((byte)(l >>> 32));
	        baos.write((byte)(l >>> 24));
	        baos.write((byte)(l >>> 16));
	        baos.write((byte)(l >>>  8));
	        baos.write((byte)(l >>>  0));
		} else {
	        baos.write((byte)(l >>>  0));
	        baos.write((byte)(l >>>  8));
	        baos.write((byte)(l >>> 16));
	        baos.write((byte)(l >>> 24));
	        baos.write((byte)(l >>> 32));
	        baos.write((byte)(l >>> 40));
	        baos.write((byte)(l >>> 48));
	        baos.write((byte)(l >>> 56));
		}
	}

	public void addChar(int c) {
        baos.write((c >>>  8) & 0xFF);
        baos.write((c >>>  0) & 0xFF);
	}

	public void alignBy(int i) {
		int alignment = this.baos.size() % 4;
		addBytes(new byte[alignment]);
	}

}
