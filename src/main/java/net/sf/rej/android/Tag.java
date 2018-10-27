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

public class Tag implements XMLElement {

	protected BinaryXMLFile xml;

	private int muggle10;
	private int srcLine;
	private int muggle11;

	private int e_muggle10; // same as the 3 above, but for the end tag
	private int e_srcLine;
	private int e_muggle11;

	private XMLElement parent;
	private List<XMLElement> children = new ArrayList<XMLElement>();

	private int muggle12;
	private int muggle13;
	private int ns;
	private int name;
	
	private List<Attribute> attrs = new ArrayList<Attribute>();


	public int getMuggle12() {
		return muggle12;
	}

	public void setMuggle12(int muggle12) {
		this.muggle12 = muggle12;
	}

	public int getMuggle13() {
		return muggle13;
	}

	public void setMuggle13(int muggle13) {
		this.muggle13 = muggle13;
	}

	public void add(Attribute attr) {
		attrs.add(attr);
	}

	public int getNs() {
		return ns;
	}

	public void setNs(int ns) {
		this.ns = ns;
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public String getTagString() {
		ArrayList<String> strs = this.xml.getStrings();

		if (this.ns == -1) {
			// default ns
			return strs.get(this.name);
		} else {
			return strs.get(this.ns) + ":" + strs.get(this.name);
		}
	}
	
	@Override
	public String toString() {
		return getTagString();
	}

	public List<Attribute> getAttributes() {
		return this.attrs;
	}

	public void setXML(BinaryXMLFile xml) {
		this.xml = xml;
	}

	public void setParent(XMLElement tag) {
		this.parent = tag;
	}
	
	public XMLElement getParent() {
		return this.parent;
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

	public List<XMLElement> getChildren() {
		return children;
	}

	public int aliasFor(int ns) {
		// tags always delegate this to their parents, namespaces either return alias, or delegate to parent
		return this.parent == null ? -1 : this.parent.aliasFor(ns);
	}

	public int getE_muggle10() {
		return e_muggle10;
	}

	public void setE_muggle10(int e_muggle10) {
		this.e_muggle10 = e_muggle10;
	}

	public int getE_srcLine() {
		return e_srcLine;
	}

	public void setE_srcLine(int e_srcLine) {
		this.e_srcLine = e_srcLine;
	}

	public int getE_muggle11() {
		return e_muggle11;
	}

	public void setE_muggle11(int e_muggle11) {
		this.e_muggle11 = e_muggle11;
	}

	public void addChild(XMLElement child) {
		this.children.add(child);
	}

	public byte[] getData() {
        ByteSerializer ser = new ByteSerializer(false); // LE
        
        ser.addInt(XMLElement.START_TAG);
        ser.addInt(muggle10);
        ser.addInt(srcLine);
        ser.addInt(muggle11);
        ser.addInt(ns);
        ser.addInt(name);

        ser.addInt(muggle12);
        ser.addInt(this.attrs.size());
        ser.addInt(muggle13);

        // attributes
		for (Attribute attr : this.attrs) {
			ser.addBytes(attr.getBytes());
		}

        
        // children
        for (XMLElement child : this.children) {
        	ser.addBytes(child.getData());
        }
        
        ser.addInt(XMLElement.END_TAG);
        ser.addInt(e_muggle10);
        ser.addInt(e_srcLine);
        ser.addInt(e_muggle11);
        ser.addInt(ns);
        ser.addInt(name);
        
		return ser.getBytes();
	}

	public void setChildren(List<XMLElement> children) {
		this.children = children;
	}

}
