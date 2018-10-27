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
package net.sf.rej.java;

/**
 * Models the access flags of a class, method or field.
 * 
 * @author Sami Koivu
 */
public class AccessFlags {

	public AccessFlags() {
	}

	public AccessFlags(int flags) {
		this._public = isPublic(flags);
		this._private = isPrivate(flags);
		this._protected = isProtected(flags);
		this._static = isStatic(flags);
		this._final = isFinal(flags);
		this._synchronized = isSynchronized(flags);
		this._native = isNative(flags);
		this._abstract = isAbstract(flags);
		this._enum = isEnum(flags);
		this._annotation = isAnnotation(flags);
		this._varargs = isVarArgs(flags);
		this._bridge = isBridge(flags);
	}

	public static final int PUBLIC = 0x0001;
	public static final int PRIVATE = 0x0002;
	public static final int PROTECTED = 0x0004;
	public static final int STATIC = 0x0008;
	public static final int FINAL = 0x0010;
	public static final int SYNCHRONIZED = 0x0020;
	public static final int SUPER = 0x0020;
	public static final int BRIDGE = 0x0040;
	// A bridge method, generated by the compiler.
	public static final int VARARGS = 0x0080;
	//Declared with variable number of
	public static final int NATIVE = 0x0100;
	public static final int INTERFACE = 0x0200;
	public static final int ABSTRACT = 0x0400;
	public static final int ANNOTATION = 0x2000;
	// Declared as an annotation type.
	public static final int ENUM = 0x4000;
	// Declared as an enum type.
	
	private boolean _public = false;

	private boolean _private = false;

	private boolean _protected = false;

	private boolean _static = false;

	private boolean _final = false;

	private boolean _synchronized = false;

	private boolean _native = false;

	private boolean _abstract = false;

	private boolean _interface = false;

	private boolean _super = false;
	
	private boolean _enum = false;
	
	private boolean _annotation = false;
	
	private boolean _varargs = false;
	
	private boolean _bridge = false;

	public static boolean isPublic(int flag) {
		return (flag & PUBLIC) != 0;
	}

	public static boolean isPrivate(int flag) {
		return (flag & PRIVATE) != 0;
	}

	public static boolean isProtected(int flag) {
		return (flag & PROTECTED) != 0;
	}

	public static boolean isStatic(int flag) {
		return (flag & STATIC) != 0;
	}

	public static boolean isFinal(int flag) {
		return (flag & FINAL) != 0;
	}

	public static boolean isSynchronized(int flag) {
		return (flag & SYNCHRONIZED) != 0;
	}

	public static boolean isSuper(int flag) {
		return (flag & SUPER) != 0;
	}

	public static boolean isNative(int flag) {
		return (flag & NATIVE) != 0;
	}

	public static boolean isInterface(int flag) {
		return (flag & INTERFACE) != 0;
	}

	public static boolean isAbstract(int flag) {
		return (flag & ABSTRACT) != 0;
	}

	public static boolean isEnum(int flag) {
		return (flag & ENUM) != 0;
	}

	public static boolean isAnnotation(int flag) {
		return (flag & ANNOTATION) != 0;
	}

	public static boolean isVarArgs(int flag) {
		return (flag & VARARGS) != 0;
	}

	public static boolean isBridge(int flag) {
		return (flag & BRIDGE) != 0;
	}

	public void setAbstract(boolean value) {
		this._abstract = value;
	}

	public void setPublic(boolean value) {
		this._public = value;
	}

	public void setPrivate(boolean value) {
		this._private = value;
	}

	public void setNative(boolean value) {
		this._native = value;
	}

	public void setProtected(boolean value) {
		this._protected = value;
	}

	public void setSynchronized(boolean value) {
		this._synchronized = value;
	}

	public void setStatic(boolean value) {
		this._static = value;
	}

	public void setFinal(boolean value) {
		this._final = value;
	}

	public void setSuper(boolean value) {
		this._super = value;
	}

	public void setInterface(boolean value) {
		this._interface = value;
	}

	public void setEnum(boolean value) {
		this._enum = value;
	}

	public void setAnnotation(boolean value) {
		this._annotation = value;
	}

	public void setVarArgs(boolean value) {
		this._varargs = value;
	}

	public void setBridge(boolean value) {
		this._bridge = value;
	}

	public int getValue() {
		int value = 0;
		if (this._abstract)
			value |= AccessFlags.ABSTRACT;
		if (this._final)
			value |= AccessFlags.FINAL;
		if (this._native)
			value |= AccessFlags.NATIVE;
		if (this._private)
			value |= AccessFlags.PRIVATE;
		if (this._protected)
			value |= AccessFlags.PROTECTED;
		if (this._public)
			value |= AccessFlags.PUBLIC;
		if (this._static)
			value |= AccessFlags.STATIC;
		if (this._super)
			value |= AccessFlags.SUPER;
		if (this._synchronized)
			value |= AccessFlags.SYNCHRONIZED;
		if (this._interface)
			value |= AccessFlags.INTERFACE;
		if (this._enum)
			value |= AccessFlags.ENUM;
		if (this._annotation)
			value |= AccessFlags.ANNOTATION;
		if (this._varargs)
			value |= AccessFlags.VARARGS;
		if (this._bridge)
			value |= AccessFlags.BRIDGE;

		return value;
	}
	
	public static String toString(int f) {
		StringBuilder sb = new StringBuilder();
		
		if ((f & PUBLIC) != 0) sb.append("public ");
		if ((f & PRIVATE) != 0) sb.append("private ");
		if ((f & PROTECTED) != 0) sb.append("protected ");
		if ((f & STATIC) != 0) sb.append("static ");
		if ((f & ABSTRACT) != 0) sb.append("abstract ");
		if ((f & FINAL) != 0) sb.append("final ");
		if ((f & NATIVE) != 0) sb.append("native ");
		if ((f & SUPER) != 0) sb.append("super ");
		if ((f & SYNCHRONIZED) != 0) sb.append("synchronized ");
		if ((f & INTERFACE) != 0) sb.append("interface ");
		if ((f & ENUM) != 0) sb.append("enum ");
		if ((f & ANNOTATION) != 0) sb.append("annotation ");
		if ((f & VARARGS) != 0) sb.append("varargs ");
		if ((f & BRIDGE) != 0) sb.append("bridge ");
		
		return sb.toString().trim();
	}
}
