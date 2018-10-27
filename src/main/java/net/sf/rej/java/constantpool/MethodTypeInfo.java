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

public class MethodTypeInfo extends ConstantPoolInfo {

	private int descriptorIndex;

	public MethodTypeInfo(int descriptorIndex, ConstantPool pool) {
		super(METHOD_TYPE, pool);
		this.descriptorIndex = descriptorIndex;
	}

	@Override
	public String toString() {
		Descriptor desc = getDescriptor();
		return desc.getReturn() + " " + "(" + desc.getParams()
				+ ")";
	}

	public String getDescriptorString() {
		return this.pool.get(this.descriptorIndex).getValue();
	}

	@Override
	public byte[] getData() {
		ByteSerializer ser = new ByteSerializer(true);
		ser.addByte(getType());
		ser.addShort(this.descriptorIndex);

		return ser.getBytes();
	}

	@Override
	public int hashCode() {
		return getDescriptorString().hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;

		try {
			MethodTypeInfo info = (MethodTypeInfo) other;

			if (!getDescriptorString().equals(info.getDescriptorString()))
				return false;

			return true;
		} catch (ClassCastException cce) {
			return false;
		}
	}

	public Descriptor getDescriptor() {
		return new Descriptor(getDescriptorString());
	}

	public void setDescriptorIndex(int index) {
		this.descriptorIndex = index;
	}

	@Override
	public String getTypeString() {
		return "MethodType";
	}

	public int getDescriptorIndex() {
		return this.descriptorIndex;
	}

}
