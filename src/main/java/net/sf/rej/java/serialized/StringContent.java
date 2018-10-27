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

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.rej.java.Deserializer;
import net.sf.rej.util.ByteSerializer;

public class StringContent implements Content {

	private String str;
	private long identity;

	public StringContent(String string) {
		this.str = string;
	}

	@Override
	public String toString() {
		return "(String " + this.str +")";
	}

	public String getString() {
		return this.str;
	}

	public void setIdentityHashcode(long identity) {
		this.identity = identity;
	}

	public long getIdentityHashcode() {
		return this.identity;
	}

	public void serialize(ByteSerializer serializer, List<Object> handles) {
		if (handles.contains(this)) {
			serializer.addByte(Deserializer.TC_REFERENCE);
			serializer.addInt(handles.indexOf(this) + Deserializer.baseWireHandle);
		} else {
			try {
				handles.add(this);
				byte[] bytes = this.str.getBytes("UTF-8");
				if (bytes.length > Short.MAX_VALUE) {
					serializer.addByte(Deserializer.TC_LONGSTRING);
					serializer.addLong(bytes.length);
					serializer.addBytes(bytes);
				} else {
					serializer.addByte(Deserializer.TC_STRING);
					serializer.addShort(bytes.length);
					serializer.addBytes(bytes);
				}
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}

	}

}
