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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.sf.rej.java.RandomAccessArray;

/**
 * Push int constant -1.
 * 
 * @author Sami Koivu
 */
public class _iconst_m1 extends Instruction {

	public static final int OPCODE = 0x2;

	public static final String MNEMONIC = "iconst_m1";

	public static final int SIZE = 1;

	private static final byte[] DATA = { OPCODE };

	public _iconst_m1() {
		// do-nothing constructor
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
		// for future considerations
	}

	@Override
	public byte[] getData(DecompilationContext dc) {
		return DATA;
	}

	@Override
	public Parameters getParameterTypes() {
		return Parameters.EMPTY_PARAMS;
	}

	@Override
	public Parameters getParameters() {
		Parameters params = new Parameters();
		params.addParam(ParameterType.TYPE_CONSTANT_READONLY);
		params.addValue(-1);
		return params;
	}

	@Override
	public void setData(byte[] data, DecompilationContext dc) {
		// instruction has no data
	}

	@Override
	public void setParameters(Parameters params) {
		// instruction has no modifiable parameters
	}

	@Override
	public List<StackElement> getPushedElements(DecompilationContext dc) {
		List<StackElement> elements = new ArrayList<StackElement>();
		elements.add(new StackElement("-1", StackElementType.INT));
		return elements;
	}

	@Override
	public void stackFlow(DecompilationContext dc) {
		Stack<StackElement> stack = dc.getStack();
		RandomAccessArray lvs = dc.getLocalVariables();
		stack.push(StackElement.valueOf(StackElementType.INT));
	}

}
