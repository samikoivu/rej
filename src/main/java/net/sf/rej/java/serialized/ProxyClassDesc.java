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

import java.util.ArrayList;
import java.util.List;

import net.sf.rej.util.ByteSerializer;

public class ProxyClassDesc implements ClassDesc {

	private List<String> names = new ArrayList<String>();
	private List<Content> annotations = new ArrayList<Content>();
	private ClassDesc superClass = null;

	public ClassDescInfo getClassDescInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addName(String name) {
		this.names.add(name);
	}

	public void addAnnotation(Content content) {
		this.annotations.add(content);
	}

	public void setSuperClass(ClassDesc superClass) {
		this.superClass = superClass;
	}

	public ClassDesc getSuperClass() {
		return this.superClass;
	}

	public String getName() {
		return this.names.toString();
	}

	public void serialize(ByteSerializer serializer, List<Object> handles) {
		// TODO: impelement!
		throw new RuntimeException("ProxyClassDesc serialization not implemented.");
	}

	public List<ClassDesc> getClassHierarchy() {
		List<ClassDesc> hierarchy = this.superClass.getClassHierarchy();
		hierarchy.add(this);
		return hierarchy;
	}

}
