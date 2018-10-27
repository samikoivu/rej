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

public class ClassDescImpl implements ClassDesc {

	private String name;
	private long serialVersionUID;
	private ClassDescInfo cdi = null;

	public ClassDescImpl(String clsName) {
		this.name = clsName;
	}

	public void setSerialVersionUID(long serialVersionUID) {
		this.serialVersionUID = serialVersionUID;
	}

	public void setClassDescInfo(ClassDescInfo cdi) {
		this.cdi = cdi;
	}

	public ClassDescInfo getClassDescInfo() {
		return this.cdi;
	}

	public String getName() {
		return name;
	}

	public long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "(ClassDesc " + this.name + " " + this.serialVersionUID + ")";
	}

	public void serialize(ByteSerializer serializer, List<Object> handles) {
		// className serialVersionUID newHandle classDescInfo
		if (handles.contains(this)) {
			serializer.addByte(Deserializer.TC_REFERENCE);
			serializer.addInt(handles.indexOf(this) + Deserializer.baseWireHandle);
		} else {
			try {
				serializer.addByte(Deserializer.TC_CLASSDESC);
				byte[] nameBytes = this.name.getBytes("UTF-8");
				// this only works for short strings, but for practical means, the
				// length of a name of a type should be limited as well
				serializer.addShort(nameBytes.length);
				serializer.addBytes(nameBytes);
				serializer.addLong(this.serialVersionUID);
				handles.add(this);
				this.cdi.serialize(serializer, handles);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public List<ClassDesc> getClassHierarchy() {
		List<ClassDesc> hierarchy = this.cdi.getSuperClass().getClassHierarchy();
		hierarchy.add(this);
		return hierarchy;
	}
}
