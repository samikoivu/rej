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

public class Enumeration implements Content {

	private ClassDesc classDesc;
	private String constantName = null;

	public Enumeration(ClassDesc classDesc) {
		this.classDesc = classDesc;
	}

	public void setConstantName(String enumConstantName) {
		this.constantName = enumConstantName;
	}

	public ClassDesc getClassDesc() {
		return this.classDesc;
	}

	public String getConstantName() {
		return this.constantName;
	}

	public void serialize(ByteSerializer serializer, List<Object> handles) {
		if (handles.contains(this)) {
			serializer.addByte(Deserializer.TC_REFERENCE);
			serializer.addInt(handles.indexOf(this) + Deserializer.baseWireHandle);
		} else {
			// TC_ENUM classDesc newHandle enumConstantName
			serializer.addByte(Deserializer.TC_ENUM);
			this.classDesc.serialize(serializer, handles);
			handles.add(this);
			StringContent sc = new StringContent(this.constantName);
			sc.serialize(serializer, handles);
		}
	}

}
