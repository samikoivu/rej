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
import java.util.Map;

import net.sf.rej.java.Deserializer;
import net.sf.rej.util.ByteSerializer;

public class ClassData {

	private List<Value> values = null;
	private List<Content> annotations = null;

	public void setValues(List<Value> values) {
		this.values = values;
	}

	public void setAnnotation(List<Content> annotation) {
		this.annotations = annotation;
	}

	public List<Content> getAnnotations() {
		return annotations;
	}

	public List<Value> getValues() {
		return values;
	}

	public void serialize(ByteSerializer serializer, List<Object> handles) {
		if (this.values != null) {
			//System.out.println("values: " + this.values);
			for (Value value : this.values) {
				value.serialize(serializer, handles);
			}
		}

		if (this.annotations != null) {
			//System.out.println("annotations: " + this.annotations);
			for (Content annotation : this.annotations) {
			/*	System.out.println(annotation + " " + annotation.getClass());
				if (annotation instanceof SerializedObject) {
					SerializedObject obj = (SerializedObject) annotation;
					Map<ClassDesc, ClassData> map = obj.getSuperClassData();
					ClassData cd = map.get(obj.getClassDesc());
					List<Value> values = cd.getValues();
					Value lm = values.get(0);
					SerializedObject so = (SerializedObject) lm.getValue();
					so.serialize(serializer, handles);
				} else*/ {
					annotation.serialize(serializer, handles);
				}
			}
			serializer.addByte(Deserializer.TC_ENDBLOCKDATA);
		}
	}


}
