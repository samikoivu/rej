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

import net.sf.rej.java.Descriptor;
import net.sf.rej.java.JavaType;
import net.sf.rej.util.ByteSerializer;

public class FieldDesc {

	private char type;
	private String name = null;
	private JavaType jtType = null;

	public void setType(char type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTypeStr(String typeStr) {
		Descriptor.DescriptorParser parser = new Descriptor.DescriptorParser(typeStr);
		this.jtType = new JavaType(parser.getType());
	}

	public char getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}

	public JavaType getJavaType() {
		return this.jtType;
	}

	public String getTypeDescription() {
		switch (this.type) {
		case 'L':
		case '[': {
			return this.jtType.getType() + this.jtType.getDimensions();
		}
		case 'B': {
			return "byte";
		}
		case 'C': {
			return "char";
		}
		case 'D':
			return "double";
		case 'F':
			return "float";
		case 'I':
			return "int";
		case 'J':
			return "long";
		case 'S':
			return "short";
		case 'Z': {
			return "boolean";
		}

		}

		return null;
	}

	public void serialize(ByteSerializer serializer, List<Object> handles) {
		serializer.addByte(this.type);
		switch (this.type) {
		case 'L':
		case '[': {
			//  obj_typecode fieldName className1
			byte[] bytes;
			try {
				bytes = this.name.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			serializer.addShort(bytes.length);
			serializer.addBytes(bytes);
			StringContent sc = new StringContent(String.valueOf(this.jtType.getRaw()));
			sc.serialize(serializer, handles);
			break;
		}
		case 'B':
		case 'C':
		case 'D':
		case 'F':
		case 'I':
		case 'J':
		case 'S':
		case 'Z': {
			byte[] bytes;
			try {
				bytes = this.name.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			serializer.addShort(bytes.length);
			serializer.addBytes(bytes);
			break;
		}
		}
	}

}
