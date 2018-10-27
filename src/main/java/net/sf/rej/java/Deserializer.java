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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.rej.gui.serialized.EOF;
import net.sf.rej.java.serialized.Array;
import net.sf.rej.java.serialized.BlockData;
import net.sf.rej.java.serialized.ClassData;
import net.sf.rej.java.serialized.ClassDesc;
import net.sf.rej.java.serialized.ClassDescImpl;
import net.sf.rej.java.serialized.ClassDescInfo;
import net.sf.rej.java.serialized.Content;
import net.sf.rej.java.serialized.Enumeration;
import net.sf.rej.java.serialized.ExceptionContent;
import net.sf.rej.java.serialized.FieldDesc;
import net.sf.rej.java.serialized.Null;
import net.sf.rej.java.serialized.ProxyClassDesc;
import net.sf.rej.java.serialized.SerializedObject;
import net.sf.rej.java.serialized.SerializedStream;
import net.sf.rej.java.serialized.StringContent;
import net.sf.rej.java.serialized.Value;
import net.sf.rej.util.ByteArrayByteParser;
import net.sf.rej.util.ByteParser;
import net.sf.rej.util.ParsingException;
import net.sf.rej.util.StreamByteParser;

public class Deserializer {

    private static final Logger logger = Logger.getLogger(Deserializer.class.getName());

    public final static short STREAM_VERSION = 5;
    public final static byte TC_NULL = (byte)0x70;
    public final static byte TC_REFERENCE = (byte)0x71;
    public final static byte TC_CLASSDESC = (byte)0x72;
    public final static byte TC_OBJECT = (byte)0x73;
    public final static byte TC_STRING = (byte)0x74;
    public final static byte TC_ARRAY = (byte)0x75;
    public final static byte TC_CLASS = (byte)0x76;
    public final static byte TC_BLOCKDATA = (byte)0x77;
    public final static byte TC_ENDBLOCKDATA = (byte)0x78;
    public final static byte TC_RESET = (byte)0x79;
    public final static byte TC_BLOCKDATALONG = (byte)0x7A;
    public final static byte TC_EXCEPTION = (byte)0x7B;
    public final static byte TC_LONGSTRING = (byte) 0x7C;
    public final static byte TC_PROXYCLASSDESC = (byte) 0x7D;
    public final static byte TC_ENUM = (byte) 0x7E;
    public final static int baseWireHandle = 0x7E0000;

    public final static byte SC_WRITE_METHOD = 0x01; //if SC_SERIALIZABLE
    public final static byte SC_BLOCK_DATA = 0x08;    //if SC_EXTERNALIZABLE
    public final static byte SC_SERIALIZABLE = 0x02;
    public final static byte SC_EXTERNALIZABLE = 0x04;
    public final static byte SC_ENUM = 0x10;

    private List<Object> handles = new ArrayList<Object>();
    
    public static void main(String[] args) throws Exception {
		Deserializer des = new Deserializer();
		File f = new File("../zdipoc/exploit.ser");
		FileInputStream fis = new FileInputStream(f);
		byte[] data = new byte[(int)f.length()];
		fis.read(data);
		des.readSerialized(data);
	}

	public SerializedStream readSerialized(byte[] data) throws SerializedParsingException {
		ByteParser parser = new ByteArrayByteParser(data);
		parser.setBigEndian(true);

		return readSerialized(parser);
	}

	public SerializedStream readSerialized(InputStream data) throws SerializedParsingException {
		ByteParser parser = new StreamByteParser(data);
		parser.setBigEndian(true);

		return readSerialized(parser);
	}

	public SerializedStream readSerialized(ByteParser parser) throws SerializedParsingException {
		SerializedStream ss = new SerializedStream();
		short magic = (short) parser.getShortAsInt();
		if (magic != SerializedStream.STREAM_MAGIC) {
			throw new SerializedParsingException("Invalid magic: " + magic);
		}

		ss.setVersion(parser.getShortAsInt());
		try {
			while (parser.hasMore()) {
				Content content = getContent(parser);
				ss.addContent(content);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SerializedParsingException("Error parsing serializable: " + e.getMessage());
		}

		return ss;
	}

	public Content getContent(ByteParser parser) throws Exception {
		int type = 0;
		try {
			type = parser.getByteAsInt();
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			aioobe.printStackTrace();
			return new EOF();
		}
		switch (type) {
			case TC_BLOCKDATA: {
				logger.log(Level.FINEST, parser.getPosition() + " : TC_BLOCKDATA");
				int size = parser.getByteAsInt();
				byte[] block = parser.getBytes(size);
				return new BlockData(block);
			}
			case TC_BLOCKDATALONG: {
				logger.log(Level.FINEST, parser.getPosition() + " : TC_BLOCKDATALONG");
				long size = parser.getInt();
				byte[] block = parser.getBytes((int)size);
				return new BlockData(block);
			}
			case TC_OBJECT: {
				logger.log(Level.FINEST, parser.getPosition() + " : TC_OBJECT");
				//   TC_OBJECT classDesc newHandle classdata[]  // data for each class
				SerializedObject object = new SerializedObject();
				ClassDesc cd = getClassDesc(parser);
				object.setClassDesc(cd);
				object.setIdentityHashcode(baseWireHandle + this.handles.size());
				this.handles.add(object);
				List<ClassDesc> hierarchy = cd.getClassHierarchy();
				for (ClassDesc classes : hierarchy) {
					ClassData superData = getClassData(classes, parser);
					object.setClassData(classes, superData);
				}

				return object;
			}
			case TC_CLASS: {
				logger.log(Level.FINEST, parser.getPosition() + " : TC_CLASS");
				ClassDesc cd = getClassDesc(parser);
				this.handles.add(cd);
				return cd;
			}
			case TC_ARRAY: {
				logger.log(Level.FINEST, parser.getPosition() + " : TC_ARRAY");
				// TC_ARRAY classDesc newHandle (int)<size> values[size]
				ClassDesc cd = getClassDesc(parser);
				Array array = new Array(cd);
				array.setIdentityHashcode(baseWireHandle + this.handles.size());
				this.handles.add(array);
				int size = (int) parser.getInt();
				for (int i=0; i < size; i++) {
					array.addElement(getArrayElement(parser, cd.getName().charAt(1)));
				}
				return array;
			}
			case TC_STRING: {
				logger.log(Level.FINEST, parser.getPosition() + " : TC_STRING");
				int length = parser.getShortAsInt();
				String string = new String(parser.getBytes(length), "UTF-8");
				StringContent sc = new StringContent(string);
				sc.setIdentityHashcode(baseWireHandle + this.handles.size());
				this.handles.add(sc);
				return sc;
			}
			case TC_LONGSTRING: {
				logger.log(Level.FINEST, parser.getPosition() + " : TC_LONGSTRING");
				long length = parser.getLong();
				long maxInt = Integer.MAX_VALUE;
				if (length > maxInt) {
					throw new RuntimeException("Long string (length=" + length + ") not supported.");
				}
				String string = getName(parser);
				StringContent sc = new StringContent(string);
				sc.setIdentityHashcode(baseWireHandle + this.handles.size());
				this.handles.add(sc);
				return sc;
			}
			case TC_ENUM: {
				logger.log(Level.FINEST, parser.getPosition() + " : TC_ENUM");
				// TC_ENUM classDesc newHandle enumConstantName
				ClassDesc cd = getClassDesc(parser);
				Enumeration e = new Enumeration(cd);
				this.handles.add(e);
				StringContent sc = (StringContent) getContent(parser);
				e.setConstantName(sc.getString());
				return e;
			}
			case TC_CLASSDESC: {
				logger.log(Level.FINEST, parser.getPosition() + " : TC_CLASSDESC");
				ClassDesc cd = getClassDesc(parser);
				this.handles.add(cd);
				return cd;
			}
			case TC_REFERENCE: {
				logger.log(Level.FINEST, parser.getPosition() + " : TC_REFERENCE");
				int handle = (int)parser.getInt();
				Content content = (Content) this.handles.get(handle - baseWireHandle);
				return content;
			}
			case TC_NULL: {
				logger.log(Level.FINEST, parser.getPosition() + " : TC_NULL");
				return new Null();
			}
			case TC_EXCEPTION: {
				logger.log(Level.FINEST, parser.getPosition() + " : TC_EXCEPTION");
				// TC_EXCEPTION reset (Throwable)object reset
				this.handles.clear();
				Content content = getContent(parser);
				ExceptionContent ex = new ExceptionContent(content);
				this.handles.clear();
				return ex;
			}
			case TC_RESET: {
				logger.log(Level.FINEST, parser.getPosition() + " : TC_RESET");
				handles.clear();
				return null;
			}
		}

		throw new ParsingException("Invalid type: " + type + " @" + parser.getPosition());
	}

	private Value getArrayElement(ByteParser parser, char type) throws Exception {
		Value value = new Value();
		switch (type) {
		case 'L':
		case '[': {
			Content c = getContent(parser);
			value.setValue(c);
			break;
		}
		case 'B': {
			byte b = parser.getByte();
			value.setValue(b);
			break;
		}
		case 'C': {
			char c = (char) parser.getShortAsInt();
			value.setValue(c);
			break;
		}
		case 'D':
			double d = Double.longBitsToDouble(parser.getLong());
			value.setValue(d);
			break;
		case 'F':
			float f = Float.intBitsToFloat((int) parser.getInt());
			value.setValue(f);
			break;
		case 'I':
			int i = (int)parser.getInt();
			value.setValue(i);
			break;
		case 'J':
			long l = parser.getLong();
			value.setValue(l);
			break;
		case 'S':
			short s = (short) parser.getShortAsInt();
			value.setValue(s);
			break;
		case 'Z': {
			int z = parser.getByteAsInt();
			boolean b = (z != 0);
			value.setValue(b);
			break;
		}

		}

		return value;
	}

	private ClassData getClassData(ClassDesc cd, ByteParser parser) throws Exception {
		ClassData data = new ClassData();
		if ((cd.getClassDescInfo().getFlags() & SC_SERIALIZABLE) != 0) {
			if ((cd.getClassDescInfo().getFlags() & SC_WRITE_METHOD) != 0) {
				// wrclass objectAnnotation
				List<Value> values = getValues(parser,cd.getClassDescInfo().getFields());
				data.setValues(values);
				List<Content> annotation = new ArrayList<Content>();
				while (parser.peekByte() != TC_ENDBLOCKDATA) {
					annotation.add(getContent(parser));
				}

				// consume the TC_ENDBLOCKDATA that was peeked in the while
				parser.getByte();

				data.setAnnotation(annotation);
			} else {
				// nowrclass
				List<Value> values = getValues(parser,cd.getClassDescInfo().getFields());
				data.setValues(values);
			}
		} else if ((cd.getClassDescInfo().getFlags() & SC_EXTERNALIZABLE) != 0) {
			if ((cd.getClassDescInfo().getFlags() & SC_BLOCK_DATA) != 0) {
				// objectAnnotation
				List<Content> annotation = new ArrayList<Content>();
				while (parser.peekByte() != TC_ENDBLOCKDATA) {
					annotation.add(getContent(parser));
				}
				// consume the TC_ENDBLOCKDATA that was peeked in the while
				parser.getByte();

				data.setAnnotation(annotation);
			} else {
				// externalContents
				// unparseable?
				return null;
			}
		}

		return data;
	}

	private List<Value> getValues(ByteParser parser, List<FieldDesc> fields) throws Exception {
		List<Value> values = new ArrayList<Value>(fields.size());
		for (FieldDesc field : fields) {
			Value value = new Value();
			value.setType(field.getType());
			switch (field.getType()) {
			case 'L':
			case '[': {
				Content c = getContent(parser);
				value.setValue(c);
				break;
			}
			case 'B': {
				byte b = parser.getByte();
				value.setValue(b);
				break;
			}
			case 'C': {
				char c = (char) parser.getShortAsInt();
				value.setValue(c);
				break;
			}
			case 'D':
				double d = Double.longBitsToDouble(parser.getLong());
				value.setValue(d);
				break;
			case 'F':
				float f = Float.intBitsToFloat((int) parser.getInt());
				value.setValue(f);
				break;
			case 'I':
				int i = parser.getInt();
				value.setValue(i);
				break;
			case 'J':
				long l = parser.getLong();
				value.setValue(l);
				break;
			case 'S':
				short s = (short) parser.getShortAsInt();
				value.setValue(s);
				break;
			case 'Z': {
				int z = parser.getByteAsInt();
				boolean b = (z != 0);
				value.setValue(b);
				break;
			}

			}
			values.add(value);
		}
		return values;
	}

	public ClassDesc getClassDesc(ByteParser parser) throws Exception {
		int type = parser.getByteAsInt();
		switch (type) {
		case TC_CLASSDESC: {
			// className serialVersionUID newHandle classDescInfo
			String clsName = getName(parser);
			//System.out.println("Class name: " + clsName);
			ClassDescImpl classDesc = new ClassDescImpl(clsName);
			long serialVersionUID = parser.getLong();
			classDesc.setSerialVersionUID(serialVersionUID);
			this.handles.add(classDesc);
			ClassDescInfo cdi = getClassDescInfo(parser);
			classDesc.setClassDescInfo(cdi);
			return classDesc;
		}
		case TC_PROXYCLASSDESC: {
			// TC_PROXYCLASSDESC newHandle proxyClassDescInfo
			ProxyClassDesc proxy = new ProxyClassDesc();
			this.handles.add(proxy);
			//  (int)<count> proxyInterfaceName[count] classAnnotation superClassDesc
			int count = (int) parser.getInt();
			for (int i=0; i < count; i++) {
				String name = getName(parser);
				proxy.addName(name);
			}

			// classAnnotation
			while (parser.peekByte() != TC_ENDBLOCKDATA) {
				proxy.addAnnotation(getContent(parser));
			}

			// consume the TC_ENDBLOCKDATA that was peeked in the while
			parser.getByte();

			// superClassDesc
			proxy.setSuperClass(getClassDesc(parser));

			return proxy;
		}
		case TC_NULL: {
			return new Null();
		}
		case TC_REFERENCE: {
			// TC_REFERENCE (int)handle
			int handle = (int) parser.getInt();
			return (ClassDesc) this.handles.get(handle-baseWireHandle);
		}
		}

		throw new SerializedParsingException("Invalid ClassDesc: " + type);
	}

	public ClassDescInfo getClassDescInfo(ByteParser parser) throws Exception {
		ClassDescInfo cdi = new ClassDescInfo();
		// classDescFlags fields classAnnotation superClassDesc
		cdi.setFlags(parser.getByteAsInt());
		int fieldCount = parser.getShortAsInt();
		for (int i=0; i < fieldCount; i++) {
			cdi.addFieldDesc(getFieldDesc(parser));
		}

		// classAnnotation
		while (parser.peekByte() != TC_ENDBLOCKDATA) {
			cdi.addAnnotation(getContent(parser));
		}

		// consume the TC_ENDBLOCKDATA that was peeked in the while
		parser.getByte();

		// superClassDesc
		cdi.setSuperClass(getClassDesc(parser));

		return cdi;
	}

	public FieldDesc getFieldDesc(ByteParser parser) throws Exception {
		char type = (char) parser.getByteAsInt();
		FieldDesc fd = new FieldDesc();
		fd.setType(type);
		switch (type) {
		case 'L':
		case '[': {
			//  obj_typecode fieldName className1
			String name = getName(parser);
			fd.setName(name);
			StringContent sc = (StringContent) getContent(parser);
			fd.setTypeStr(sc.getString());

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
			// prim_typecode fieldName
			String name = getName(parser);
			fd.setName(name);
			fd.setTypeStr(String.valueOf(type));

			break;
		}
		default: {
			throw new RuntimeException("Error, invalid field type: " + type + " @" + parser.getPosition());
		}

		}

		return fd;
	}

	private String getName(ByteParser parser) throws Exception {
		int clNameLength = parser.getShortAsInt();
		String name = new String(parser.getBytes(clNameLength), "UTF-8");
		return name;
	}
}
