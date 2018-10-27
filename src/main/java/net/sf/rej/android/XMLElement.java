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

import java.util.List;

public interface XMLElement {
	public final int START_NAMESPACE = 0x00100100;
	public final int END_NAMESPACE = 0x00100101;
	public final int START_TAG =  0x00100102;
	public final int END_TAG =    0x00100103;
	public final int TAG_DATA =    0x00100104;

	public byte[] getData();

	public void addChild(XMLElement child);

	public XMLElement getParent();

	public int getNs();

	public int aliasFor(int ns);

	public String getTagString();

	public List<XMLElement> getChildren();

	public void setChildren(List<XMLElement> children);
	
}
