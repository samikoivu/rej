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
package net.sf.rej.util;

import java.util.Arrays;

import org.junit.Assert;

import org.junit.Test;

/**
 * Class for testing ByteSerializer class
 * @author Sami Koivu
 * @version $Revision: 1.3 $
 */
public class ByteSerializerTest {

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#ByteSerializer(boolean)}.
	 */
	@Test
	public void testByteSerializer() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#getBytes()}.
	 */
	@Test
	public void testGetBytes() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#addBytes(byte[])}.
	 */
	@Test
	public void testAddBytesByteArray() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#addBytes(byte[], int)}.
	 */
	@Test
	public void testAddBytesByteArrayInt() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#addBytes(byte[], int, int)}.
	 */
	@Test
	public void testAddBytesByteArrayIntInt() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#addByte(int)}.
	 */
	@Test
	public void testAddByte() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#addShort(int)}.
	 */
	@Test
	public void testAddShort() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#addInt(int)}.
	 */
	@Test
	public void testAddInt() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#size()}.
	 */
	@Test
	public void testSize() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#addLong(long)}.
	 */
	@Test
	public void testAddLong() {
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addLong(0L);
			byte[] bytes = bs.getBytes();
			Assert.assertTrue("long value 0", Arrays.equals(new byte[] {0, 0, 0, 0, 0, 0, 0, 0}, bytes));
		}
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addLong(1);
			byte[] bytes = bs.getBytes();
			Assert.assertTrue("long value 1", Arrays.equals(new byte[] {0, 0, 0, 0, 0, 0, 0, 1}, bytes));
		}
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addLong(0xFF00FFL);
			byte[] bytes = bs.getBytes();
			Assert.assertTrue("long value FF00FF", Arrays.equals(new byte[] {0, 0, 0, 0, 0, (byte) 0xFF, 0, (byte) 0xFF}, bytes));
		}
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addLong(-1L);
			byte[] bytes = bs.getBytes();
			Assert.assertTrue("long value -1", Arrays.equals(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}, bytes));
		}
	}

}
