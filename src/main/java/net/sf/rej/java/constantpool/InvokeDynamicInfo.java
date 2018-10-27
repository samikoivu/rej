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
package net.sf.rej.java.constantpool;

import net.sf.rej.java.Descriptor;
import net.sf.rej.util.ByteSerializer;

public class InvokeDynamicInfo extends ConstantPoolInfo {

	private int bootstrapMethodAttrIndex;
	private int nameAndTypeIndex;

	public InvokeDynamicInfo(int bootstrapMethodAttrIndex,
			int nameAndTypeIndex, ConstantPool pool) {
		super(INVOKE_DYNAMIC, pool);
		this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	@Override
	public String toString() {
		Descriptor desc = getDescriptor();
		return desc.getReturn() + " " + "." + getTargetName()
				+ "(" + desc.getParams() + ")";
	}

	public String getTargetName() {
		NameAndTypeInfo info = (NameAndTypeInfo) this.pool
				.get(this.nameAndTypeIndex);
		return info.getName();
	}

	public Descriptor getDescriptor() {
		NameAndTypeInfo info = (NameAndTypeInfo) this.pool
				.get(this.nameAndTypeIndex);
		return info.getDescriptor();
	}

	@Override
	public byte[] getData() {
		ByteSerializer ser = new ByteSerializer(true);
		ser.addByte(getType());
		ser.addShort(this.bootstrapMethodAttrIndex);
		ser.addShort(this.nameAndTypeIndex);

		return ser.getBytes();
	}

	public NameAndTypeInfo getNameAndTypeInfo() {
		NameAndTypeInfo info = (NameAndTypeInfo) this.pool
				.get(this.nameAndTypeIndex);
		return info;
	}

	@Override
	public int hashCode() {
		int i = this.bootstrapMethodAttrIndex;
		i *=31;
		i += getNameAndTypeInfo().hashCode();

		return i;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;

		try {
			InvokeDynamicInfo ref = (InvokeDynamicInfo) other;
			if (this.bootstrapMethodAttrIndex != ref.bootstrapMethodAttrIndex)
				return false;

			if (!getTargetName().equals(ref.getTargetName()))
				return false;

			return true;
		} catch (ClassCastException cce) {
			return false;
		}
	}

	@Override
	public String getTypeString() {
		return "InvokeDynamic";
	}

}
