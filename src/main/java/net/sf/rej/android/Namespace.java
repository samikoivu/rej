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

public class Namespace implements XMLElement {

	private BinaryXMLFile xml;
	private XMLElement parent;
	private List<XMLElement> children = new ArrayList<XMLElement>();

	private int nsAlias;
	private int ns;

	private int muggle10;
	private int line;
	private int muggle11;

	private int e_muggle10; // same as the 3 above, but for the end tag
	private int e_srcLine;
	private int e_muggle11;

	public int getNsAlias() {
		return nsAlias;
	}
	
	public void setNsAlias(int nsAlias) {
		this.nsAlias = nsAlias;
	}
	
	public int getNs() {
		return ns;
	}
	
	public void setNs(int ns) {
		this.ns = ns;
	}
	
	public String getNSString() {
		ArrayList<String> strs = this.xml.getStrings();

		return "xmlns:" + strs.get(this.nsAlias) + "=" + strs.get(this.ns);
	}

	public XMLElement getParent() {
		return this.parent;
	}

	public void setXML(BinaryXMLFile xml) {
		this.xml = xml;
	}

	public void setParent(XMLElement namespace) {
		this.parent = namespace;
	}

	public void setMuggle10(int i) {
		this.muggle10 = i;
	}

	public void setSrcLine(int line) {
		this.line = line;
	}

	public void setMuggle11(int i) {
		this.muggle11 = i;
	}

	public int aliasFor(int ns) {
		if (ns == this.ns) {
			return this.nsAlias;
		} else {
			// otherwise ask parent, if one exists
			if (this.parent == null) {
				return -1;
			} else {
				return this.parent.aliasFor(ns);
			}
		}
	}

	public void addChild(XMLElement child) {
		this.children.add(child);
	}

	public void setE_muggle10(int e_muggle10) {
		this.e_muggle10 = e_muggle10;
	}

	public void setE_srcLine(int e_srcLine) {
		this.e_srcLine = e_srcLine;
	}

	public void setE_muggle11(int e_muggle11) {
		this.e_muggle11 = e_muggle11;
	}

	public String getTagString() {
		ArrayList<String> strs = this.xml.getStrings();

		return "xmlns:" + strs.get(getNsAlias()) + "=\"" + strs.get(getNs()) + "\"";
	}
	
	@Override
	public String toString() {
		return getTagString();
	}

	public List<XMLElement> getChildren() {
		return this.children;
	}

	public byte[] getData() {
        ByteSerializer ser = new ByteSerializer(false); // LE
        
        ser.addInt(XMLElement.START_NAMESPACE);
        ser.addInt(muggle10);
        ser.addInt(line);
        ser.addInt(muggle11);
        ser.addInt(nsAlias);
        ser.addInt(ns);
        
        // children
        for (XMLElement child : this.children) {
        	ser.addBytes(child.getData());
        }
        
        ser.addInt(XMLElement.END_NAMESPACE);
        ser.addInt(e_muggle10);
        ser.addInt(e_srcLine);
        ser.addInt(e_muggle11);
        ser.addInt(nsAlias);
        ser.addInt(ns);
        
		return ser.getBytes();
	}

	public void setChildren(List<XMLElement> children) {
		this.children = children;
	}

}
