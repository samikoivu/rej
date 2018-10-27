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

import net.sf.rej.util.ByteSerializer;

public class MethodHandleInfo extends ConstantPoolInfo {

	private int referenceKind;
	private int referenceIndex;

	public MethodHandleInfo(int referenceKind, int referenceIndex,
			ConstantPool pool) {
		super(METHOD_HANDLE, pool);
		this.referenceKind = referenceKind;
		this.referenceIndex = referenceIndex;
	}

	@Override
	public String toString() {
		return this.referenceKind + ", " + this.referenceIndex;
	}

	@Override
	public byte[] getData() {
		ByteSerializer ser = new ByteSerializer(true);
		ser.addByte(getType());
		ser.addByte(this.referenceKind);
		ser.addShort(this.referenceIndex);

		return ser.getBytes();
	}

	@Override
	public int hashCode() {
		return this.referenceKind*31 + this.referenceIndex;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;

		try {
			MethodHandleInfo info = (MethodHandleInfo) other;

			ConstantPoolInfo thisRef = this.pool.get(this.referenceIndex);
			ConstantPoolInfo otherRef = this.pool.get(info.referenceIndex);
			return info.referenceKind == this.referenceKind && thisRef.equals(otherRef);
		} catch (ClassCastException cce) {
			return false;
		}
	}

	@Override
	public String getTypeString() {
		return "MethodHandle";
	}

}
