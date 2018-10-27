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

public class ClassDescInfo {

	private int flags;
	private List<FieldDesc> fields = new ArrayList<FieldDesc>();
	private List<Content> annotations = new ArrayList<Content>();
	private ClassDesc superClass = null;

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public void addFieldDesc(FieldDesc fieldDesc) {
		this.fields.add(fieldDesc);
	}

	public void addAnnotation(Content content) {
		this.annotations.add(content);
	}

	public void setSuperClass(ClassDesc superClass) {
		this.superClass = superClass;
	}

	public int getFlags() {
		return this.flags;
	}

	public ClassDesc getSuperClass() {
		return superClass;
	}

	public List<FieldDesc> getFields() {
		return fields;
	}

	public List<Content> getAnnotation() {
		return this.annotations;
	}

	public void serialize(ByteSerializer serializer, List<Object> handles) {
		if (handles.contains(this)) {
			serializer.addByte(Deserializer.TC_REFERENCE);
			serializer.addInt(handles.indexOf(this) + Deserializer.baseWireHandle);
		} else {
			// classDescFlags fields classAnnotation superClassDesc
			serializer.addByte(this.flags);
			serializer.addShort(this.fields.size());
			for (FieldDesc field : this.fields) {
				field.serialize(serializer, handles);
			}

			for (Content annotation : this.annotations) {
				annotation.serialize(serializer, handles);
			}
			serializer.addByte(Deserializer.TC_ENDBLOCKDATA);

			this.superClass.serialize(serializer, handles);
		}

	}

}
