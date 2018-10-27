/* Copyright (C) 2004-2011 Sami Koivu
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
package net.sf.rej.android;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import net.sf.rej.gui.FileObject;
import net.sf.rej.util.ByteSerializer;

public class BinaryXMLFile implements FileObject {

	public static final int MAGIC = 524291;
	public static final int XXXMAGIC = 524672;
	
	private int muggle;
	private int xxxSectionOffset;
	private int muggle1;
	private int muggle2;
	private int muggle4;
	
	private ArrayList<String> strings = new ArrayList<String>();
	private int xxxSectionSize;
	private byte[] xxxSectionData;

	private XMLElement root;
	
	public int getMuggle() {
		return muggle;
	}

	public void setMuggle(int muggle) {
		this.muggle = muggle;
	}

	public int getxxxSectionOffset() {
		return xxxSectionOffset;
	}

	public void setxxxSectionOffset(int xxxSectionOffset) {
		this.xxxSectionOffset = xxxSectionOffset;
	}

	public int getMuggle1() {
		return muggle1;
	}

	public void setMuggle1(int muggle1) {
		this.muggle1 = muggle1;
	}

	public int getMuggle2() {
		return muggle2;
	}

	public void setMuggle2(int muggle2) {
		this.muggle2 = muggle2;
	}

	public int getMuggle4() {
		return muggle4;
	}

	public void setMuggle4(int muggle4) {
		this.muggle4 = muggle4;
	}

	public int getXxxSectionSize() {
		return xxxSectionSize;
	}

	public void setXxxSectionSize(int xxxSectionSize) {
		this.xxxSectionSize = xxxSectionSize;
	}

	public byte[] getXxxSectionData() {
		return xxxSectionData;
	}

	public void setXxxSectionData(byte[] xxxSectionData) {
		this.xxxSectionData = xxxSectionData;
	}

	public void setDocumentRoot(XMLElement tag) {
		this.root = tag;
	}

	public XMLElement getDocumentRoot() {
		return this.root;
	}

	public ArrayList<String> getStrings() {
		return strings;
	}

	public void setStringCount(int stringCount) {
		this.getStrings().ensureCapacity(stringCount);
	}

	public void addString(String str) {
		this.getStrings().add(str);
	}

	
	/*
	 * File layout:
	 * HEADER (24 bytes)
	 *  magic
	 *  filesize
	 *  ...
	 *  string count
	 *  
	 * STRING INDEX TABLE (string count *4 bytes)
	 *  string 1 offset
	 *  string 2 offset
	 *  ...
	 *  
	 * STRING TABLE (depends on string data)
	 * 
	 * UNKNOWN SECTION (section size bytes)
	 *  section magic?
	 *  section size
	 *  data ...
	 *  
	 * TAG SECTION
	 *  tag
	 *  tag
	 *  tag
	 *  ...
	 * 
	 * 
	 */
    public byte[] getData() {
        ByteSerializer ser = new ByteSerializer(false); // LE
        ser.addInt(MAGIC);
        byte[] data = getData0();
        ser.addInt(data.length + 8); // add magic size 
        ser.addBytes(data);
        return ser.getBytes();
    }

	private byte[] getData0() {
        ByteSerializer ser = new ByteSerializer(false); // LE
        ser.addInt(muggle);
        
        byte[] sitData = getSITData();
        byte[] stData = getSTData();
        byte[] xxxData = getXXXData();
        byte[] tagData = getTagData();
        
        int stOffset = this.strings.size()*4 + 28;
        int xxxOffset = stOffset+stData.length + stData.length % 4;
        
        ser.addInt(xxxOffset);
        ser.addInt(this.strings.size());
        ser.addInt(this.muggle1);
        ser.addInt(this.muggle2);
        ser.addInt(stOffset); // start of string table (this value + 8) 
        ser.addInt(this.muggle4);
        
        ser.addBytes(sitData);
        ser.addBytes(stData);
        ser.alignBy(4);
        ser.addBytes(xxxData);
        ser.addBytes(tagData);
        
        return ser.getBytes();
	}

	private byte[] getTagData() {
		return this.root.getData();
	}

	private byte[] getXXXData() {
		if (this.xxxSectionData == null) {
			return new byte[0];
		}
		
        ByteSerializer ser = new ByteSerializer(false); // LE
		ser.addInt(XXXMAGIC);
		ser.addInt(this.xxxSectionData.length + 8);
		ser.addBytes(this.xxxSectionData);
		return ser.getBytes();
	}

	private byte[] getSTData() {
        ByteSerializer ser = new ByteSerializer(false); // LE

        byte[] NULL = new byte[2];
        
        for (String s : this.strings) {
        	try {
        		byte[] data = s.getBytes("UTF-16LE");
        		ser.addShort(s.length());
        		ser.addBytes(data);
        		ser.addBytes(NULL);
        	} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Could not convert Binary XML to data", e);
			}
        }
		return ser.getBytes();
	}

	private byte[] getSITData() {
        ByteSerializer ser = new ByteSerializer(false); // LE
        int offset = 0;
        for (String s : this.strings) {
        	ser.addInt(offset);
        	try {
				offset += s.getBytes("UTF-16LE").length;
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Could not convert Binary XML to data", e);
			}
        	offset += 4; // one short for size, one short for a 00byte
        }
		return ser.getBytes();
	}

}
