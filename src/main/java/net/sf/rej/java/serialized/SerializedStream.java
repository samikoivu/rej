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

import java.util.ArrayList;
import java.util.List;

import net.sf.rej.gui.FileObject;
import net.sf.rej.util.ByteSerializer;

public class SerializedStream implements FileObject {
	public final static short STREAM_MAGIC = (short)0xaced;
	private int version;

	private List<Content> contents = new ArrayList<Content>();

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void addContent(Content content) {
		this.contents.add(content);
	}

	public List<Content> getContents() {
		return this.contents;
	}

	/**
	 * Method that serializes the object hierarchy contained within this object as
	 * a byte array.
	 * @return the serialized data
	 */
	public byte[] getData() {
		ByteSerializer serializer = new ByteSerializer(true);
		serializer.addShort(SerializedStream.STREAM_MAGIC);
		serializer.addShort(this.version);

		List<Object> handles = new ArrayList<Object>();

		for (Content content : this.contents) {
			content.serialize(serializer, handles);
		}

		return serializer.getBytes();
	}
}
