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
package net.sf.rej.java.instruction;

import net.sf.rej.util.ByteSerializer;
import net.sf.rej.util.ByteToolkit;

/**
 * Undefined instruction 0xBA.
 * 
 * @author Sami Koivu
 */
public class _invokedynamic extends Instruction {

	public static final int OPCODE = 0xba;

	public static final String MNEMONIC = "invokedynamic";

	private static final int SIZE = 5;

	private int index = 0;

	public _invokedynamic() {
	}

	@Override
	public int getOpcode() {
		return OPCODE;
	}

	@Override
	public String getMnemonic() {
		return MNEMONIC;
	}

	@Override
	public int getSize() {
		return SIZE;
	}

	@Override
	public void execute(ExecutionContext ec) {
	}

	@Override
	public void setData(byte[] data, DecompilationContext dc) {
		this.index = (int) ByteToolkit.getLong(data, 1, 2, true);
		ByteToolkit.getLong(data, 3, 2, true); // zeros, might as well not read them, just a sanity check
	}

	@Override
	public byte[] getData(DecompilationContext dc) {
		ByteSerializer ser = new ByteSerializer(true);
		ser.addByte(OPCODE);
		ser.addShort(this.index);
		ser.addByte(0);
		ser.addByte(0);
		return ser.getBytes();
	}

	@Override
	public Parameters getParameters() {
		Parameters params = getParameterTypes();
		params.addValue(this.index);
		return params;
	}

	@Override
	public Parameters getParameterTypes() {
		return new Parameters(new ParameterType[] {
				ParameterType.TYPE_INVOKEDYNAMIC,
				});
	}

	@Override
	public void setParameters(Parameters params) {
		this.index = params.getInt(0);
	}

}
