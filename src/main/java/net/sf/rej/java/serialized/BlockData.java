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
package net.sf.rej.java.serialized;

import java.util.List;

import net.sf.rej.java.Deserializer;
import net.sf.rej.util.ByteSerializer;
import net.sf.rej.util.ByteToolkit;

public class BlockData implements Content {

	private byte[] data;

	public BlockData(byte[] block) {
		this.data = block;
	}

	public byte[] getData() {
		return this.data;
	}

	@Override
	public String toString() {
		if (this.data.length > 32) {
			return ByteToolkit.getHexString(this.data, 32) + " ... (" + data.length + " bytes total)";
		} else {
			if (this.data.length == 1 && (this.data[0]==0 || this.data[0] ==1)) {
				return  ByteToolkit.getHexString(this.data) + (this.data[0] == 0 ? " (false)" : " (true)");
			} else if (this.data.length == 2) {
				// TODO: short, char
				return ByteToolkit.getHexString(this.data);
			} else if (this.data.length == 4) {
				return ByteToolkit.getHexString(this.data) + " (" + ByteToolkit.getInt(data) + ")";
			} else if (this.data.length == 8) {
				return ByteToolkit.getHexString(this.data) + " (" + ByteToolkit.getLong(data) + "L)";
			} else {
				return ByteToolkit.getHexString(this.data);
			}
		}
	}

	public void serialize(ByteSerializer serializer, List<Object> handles) {
		if (this.data.length < 256) {
			serializer.addByte(Deserializer.TC_BLOCKDATA);
			serializer.addByte((byte) this.data.length);
			serializer.addBytes(this.data);
		} else {
			serializer.addByte(Deserializer.TC_BLOCKDATALONG);
			serializer.addInt(this.data.length);
			serializer.addBytes(this.data);
		}
	}

}
