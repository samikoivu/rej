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

import net.sf.rej.util.ByteSerializer;

public class Value {

	private char type;
	private Object value;

	public Value() {
	}

	public void setType(char type) {
		this.type = type;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public char getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

	public void serialize(ByteSerializer serializer, List<Object> handles) {
		switch (this.type) {
		case 'L':
		case '[': {

			Content c = (Content) this.value;
			c.serialize(serializer, handles);
			break;
		}
		case 'B': {
			serializer.addByte((Byte) this.value);
			break;
		}
		case 'C': {
			serializer.addChar((Character) this.value);
			break;
		}
		case 'D':
			serializer.addLong(Double.doubleToLongBits((Double) this.value));
			break;
		case 'F':
			serializer.addInt(Float.floatToIntBits((Float) this.value));
			break;
		case 'I':
			serializer.addInt((Integer) this.value);
			break;
		case 'J':
			serializer.addLong((Long) this.value);
			break;
		case 'S':
			serializer.addShort((Short) this.value);
			break;
		case 'Z': {
			serializer.addByte((Boolean) this.value ? 0 : 1);
			break;
		}

		}
	}

}
