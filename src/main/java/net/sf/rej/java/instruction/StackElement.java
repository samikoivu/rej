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
package net.sf.rej.java.instruction;


/**
 * A class that models a stack elements / operands that is pushed onto
 * or popped from the stack. 
 * 
 * @author Sami Koivu
 */
public class StackElement {
	
	public static final StackElement NULL = new StackElement("<null>", StackElementType.NULL);
	public static final StackElement INT = new StackElement("int", StackElementType.INT);
	public static final StackElement LONG = new StackElement("long", StackElementType.LONG);
	public static final StackElement FLOAT = new StackElement("float", StackElementType.FLOAT);
	public static final StackElement DOUBLE = new StackElement("double", StackElementType.DOUBLE);

	private String elementName;
	private StackElementType elementType;
	
	public StackElement(String name, StackElementType type) {
		this.elementName = name;
		this.elementType = type;
	}
	
	public String getName() {
		return this.elementName;
	}
	
	public StackElementType getType() {
		return this.elementType;
	}

	public static StackElement valueOf(String name, StackElementType type) {
		// just use new TODO: at some point think about resources (for the children)
		return new StackElement(name, type);
	}

	public static StackElement valueOf(StackElementType type) {
		if (type == StackElementType.NULL) {
			return NULL;
		} else if (type == StackElementType.INT) {
			return INT;
		} else if (type == StackElementType.LONG) {
			return LONG;
		} else if (type == StackElementType.FLOAT) {
			return FLOAT;
		} else if (type == StackElementType.DOUBLE) {
			return DOUBLE;
		}
		return new StackElement(type.name(), type);
	}
	
	@Override
	public String toString() {
		return (elementName == null ? "" : elementName) + " " + elementType;
	}
}
