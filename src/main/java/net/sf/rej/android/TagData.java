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

import java.util.ArrayList;
import java.util.List;

import net.sf.rej.util.ByteSerializer;

public class TagData implements XMLElement {

	private XMLElement parent;
	private int ns;
	private int muggle10;
	private int srcLine;
	private int muggle11;
	private int value;
	private int muggle12;
	private BinaryXMLFile xml;

	public byte[] getData() {
        ByteSerializer ser = new ByteSerializer(false); // LE
        
        ser.addInt(XMLElement.TAG_DATA);
        ser.addInt(muggle10);
        ser.addInt(srcLine);
        ser.addInt(muggle11);
        ser.addInt(ns);
        ser.addInt(value);
        ser.addInt(muggle12);
	        
		return ser.getBytes();
	}

	public void addChild(XMLElement child) {
		throw new RuntimeException("Data is not allowed to have children.");
	}

	public XMLElement getParent() {
		return this.parent;
	}

	public int getNs() {
		return ns;
	}

	public int aliasFor(int ns) {
		return this.parent.aliasFor(ns);
	}

	public String getTagString() {
		return this.xml.getStrings().get(this.value);
	}

	private static List<XMLElement> EMPTY = new ArrayList<XMLElement>();
	public List<XMLElement> getChildren() {
		return EMPTY;
	}

	public void setChildren(List<XMLElement> children) {
		throw new RuntimeException("Data is not allowed to have children.");
	}

	public void setParent(XMLElement tag) {
		this.parent = tag;
	}

	public int getMuggle10() {
		return muggle10;
	}

	public void setMuggle10(int muggle10) {
		this.muggle10 = muggle10;
	}

	public int getSrcLine() {
		return srcLine;
	}

	public void setSrcLine(int srcLine) {
		this.srcLine = srcLine;
	}

	public int getMuggle11() {
		return muggle11;
	}

	public void setMuggle11(int muggle11) {
		this.muggle11 = muggle11;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getMuggle12() {
		return muggle12;
	}

	public void setMuggle12(int muggle12) {
		this.muggle12 = muggle12;
	}

	public void setNs(int ns) {
		this.ns = ns;
	}

	public void setXML(BinaryXMLFile xml) {
		this.xml = xml;
	}

}
