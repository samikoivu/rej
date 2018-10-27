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

import net.sf.rej.util.ByteSerializer;

public class Attribute {

	private int ns;
	private int name;
	private int value;
	private int muggle14;
	private int resourceId;
	
	private BinaryXMLFile xml;
	private Tag parent;

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
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getMuggle14() {
		return muggle14;
	}
	
	public void setMuggle14(int muggle14) {
		this.muggle14 = muggle14;
	}
	
	public int getResourceId() {
		return resourceId;
	}
	
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public void setXML(BinaryXMLFile xml) {
		this.xml = xml;
	}

	public String getAttrString() {
		ArrayList<String> strs = this.xml.getStrings();

		if (this.ns == -1) {
			// default ns
			return strs.get(this.name);
		} else {
			// ask for an alias up the ns def hierarchy
			int alias = this.parent.aliasFor(this.ns);
			if (alias != -1) {
				return strs.get(alias) + ":" + strs.get(this.name);
			} else {
				return strs.get(this.ns) + ":" + strs.get(this.name);
			}
		}
	}
	
	public String getValueString() {
		ArrayList<String> strs = this.xml.getStrings();

		if (this.value == -1) {
			// default ns
			return "@" + this.resourceId;
		} else {
			return strs.get(this.value);
		}
	}
	
	@Override
	public String toString() {
		return getAttrString() + "=" + getValueString();
	}

	public void setParent(Tag tag) {
		this.parent = tag;
	}

	public byte[] getBytes() {
        ByteSerializer ser = new ByteSerializer(false); // LE

        ser.addInt(this.ns);
        ser.addInt(this.name);
        ser.addInt(this.value);
        ser.addInt(this.muggle14);
        ser.addInt(this.resourceId);
        
        return ser.getBytes();
	}

	public Object getAttributeValue() {
		if (this.value == -1) {
			return this.resourceId;
		} else {
			return this.xml.getStrings().get(this.value);
		}
	}
}
