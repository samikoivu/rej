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
package net.sf.rej.java;

import java.util.Arrays;

import net.sf.rej.java.instruction.StackElement;

public final class RandomAccessArray {

	private StackElement[] array;
	
	public RandomAccessArray() {
		this(10); // default size
	}

	public RandomAccessArray(int size) {
		this.array = new StackElement[size];
	}
	
	public void put(int index, StackElement obj) {
		if (index >= array.length) {
			// grow
			this.array = Arrays.copyOf(this.array, index+1);
		}
		
		array[index] = obj;
	}
	
	public StackElement get(int index) {
		if (index >= array.length) {
			return null; // out of bounds, return null but don't bother growing
		} else {
			return array[index];
		}
	}
	
}
