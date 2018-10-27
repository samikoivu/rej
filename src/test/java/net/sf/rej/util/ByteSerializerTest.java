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
import java.util.Random;

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
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addLong(0x1122334455667788L);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, (byte)0x88}, bytes);
		}
		{
			ByteSerializer bs = new ByteSerializer(false);
			bs.addLong(0x1122334455667788L);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {(byte)0x88, 0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11}, bytes);
		}
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#getBytes()}.
	 */
	@Test
	public void testGetBytes() {
		// generate random test data
		byte[] data = new byte[128];
		Random r = new Random();
		r.nextBytes(data); // fill array with random data

		
		ByteSerializer bs = new ByteSerializer(true);
		bs.addBytes(data);
		byte[] bytes = bs.getBytes();
		Assert.assertArrayEquals(data, bytes);
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#addBytes(byte[])}.
	 */
	@Test
	public void testAddBytesByteArray() {		
		ByteSerializer bs = new ByteSerializer(true);
		bs.addBytes("hello".getBytes());
		byte[] bytes = bs.getBytes();
		Assert.assertArrayEquals(new byte[] {'h', 'e', 'l', 'l', 'o'}, bytes);
		bs.addBytes(" world".getBytes());
		bytes = bs.getBytes();
		Assert.assertArrayEquals(new byte[] {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'}, bytes);
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#addBytes(byte[], int)}.
	 */
	@Test
	public void testAddBytesByteArrayInt() {
		ByteSerializer bs = new ByteSerializer(true);
		bs.addBytes("hello".getBytes(), 3);
		byte[] bytes = bs.getBytes();
		Assert.assertArrayEquals(new byte[] {'h', 'e', 'l'}, bytes);
		bs.addBytes(" world".getBytes(), 2);
		bytes = bs.getBytes();
		Assert.assertArrayEquals(new byte[] {'h', 'e', 'l', ' ', 'w'}, bytes);
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#addBytes(byte[], int, int)}.
	 */
	@Test
	public void testAddBytesByteArrayIntInt() {
		ByteSerializer bs = new ByteSerializer(true);
		bs.addBytes("hello".getBytes(), 2, 2);
		byte[] bytes = bs.getBytes();
		Assert.assertArrayEquals(new byte[] {'l', 'l'}, bytes);
		bs.addBytes(" world".getBytes(), 1, 3);
		bytes = bs.getBytes();
		Assert.assertArrayEquals(new byte[] {'l', 'l', 'w', 'o', 'r'}, bytes);
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#addByte(int)}.
	 */
	@Test
	public void testAddByte() {
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addByte(0);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {0}, bytes);
		}
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addByte(1);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {1}, bytes);
		}
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addByte(0xFF);
			bs.addByte(55);
			bs.addByte(91);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {(byte)0xFF, 55, 91}, bytes);
		}
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#addShort(int)}.
	 */
	@Test
	public void testAddShort() {
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addShort(0);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {0, 0}, bytes);
		}
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addShort(1);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {0, 1}, bytes);
		}
		{
			ByteSerializer bs = new ByteSerializer(false); // small endian
			bs.addShort(1);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {1, 0}, bytes);
		}
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addShort(-1);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {(byte)0xff, (byte)0xff}, bytes);
		}
		{
			ByteSerializer bs = new ByteSerializer(true); // big endian
			bs.addShort(0x1234);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {0x12, 0x34}, bytes);
		}
		{
			ByteSerializer bs = new ByteSerializer(false); // small endian
			bs.addShort(0x1234);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {0x34, 0x12}, bytes);
		}
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#addInt(int)}.
	 */
	@Test
	public void testAddInt() {
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addInt(0);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {0, 0, 0, 0}, bytes);
		}
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addInt(1);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {0, 0, 0, 1}, bytes);
		}
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addInt(0xFF00FF);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {0, (byte) 0xFF, 0, (byte) 0xFF}, bytes);
		}
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addInt(-1);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}, bytes);
		}
	}

	/**
	 * Test method for {@link net.sf.rej.util.ByteSerializer#size()}.
	 */
	@Test
	public void testSize() {
		ByteSerializer bs = new ByteSerializer(true);
		byte[] bytes = bs.getBytes();
		Assert.assertEquals("array size 0", 0, bytes.length);
		bs.addBytes(new byte[91]);
		bytes = bs.getBytes();
		Assert.assertEquals("array size 91", 91, bytes.length);
		bs.addByte(66);
		bytes = bs.getBytes();
		Assert.assertEquals("array size 92", 92, bytes.length);
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
			Assert.assertArrayEquals(new byte[] {0, 0, 0, 0, 0, 0, 0, 0}, bytes);
		}
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addLong(1);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {0, 0, 0, 0, 0, 0, 0, 1}, bytes);
		}
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addLong(0xFF00FFL);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {0, 0, 0, 0, 0, (byte) 0xFF, 0, (byte) 0xFF}, bytes);
		}
		{
			ByteSerializer bs = new ByteSerializer(true);
			bs.addLong(-1L);
			byte[] bytes = bs.getBytes();
			Assert.assertArrayEquals(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}, bytes);
		}
	}

}
