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

import net.sf.rej.java.Deserializer;
import net.sf.rej.util.ByteSerializer;

public class Array implements Content {

	private ClassDesc classDesc;
	private List<Value> elements = new ArrayList<Value>();
	private long identity;

	public Array(ClassDesc classDesc) {
		this.classDesc = classDesc;
	}

	public void addElement(Value element) {
		this.elements.add(element);
	}

	public ClassDesc getClassDesc() {
		return this.classDesc;
	}

	public List<Value> getElements() {
		return this.elements;
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
			// TC_ARRAY classDesc newHandle (int)<size> values[size]
			serializer.addByte(Deserializer.TC_ARRAY);
			this.classDesc.serialize(serializer, handles);
			handles.add(this);
			serializer.addInt(this.elements.size());
			for (Value value : this.elements) {
				value.serialize(serializer, handles);
			}

		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		boolean first = true;
		for (Value element : this.elements) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(element.toString());
		}
		sb.append(" }");
		return sb.toString();
	}

}
