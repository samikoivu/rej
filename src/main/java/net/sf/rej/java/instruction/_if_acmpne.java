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

import net.sf.rej.util.ByteSerializer;
import net.sf.rej.util.ByteToolkit;

/**
 * Branch if reference comparison(not equals) succeeds.
 * 
 * @author Sami Koivu
 */
public class _if_acmpne extends Instruction implements Branchable {

	public static final int OPCODE = 0xa6;

	public static final String MNEMONIC = "if_acmpne";

	private static final int SIZE = 3;

	private Label label = null;

	public _if_acmpne() {
	}

	public _if_acmpne(Label label) {
		this.label = label;
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
		int addr = (int) ByteToolkit.getSignedLong(data, 1, 2, true)
				+ dc.getPosition();
		this.label = new Label(addr);
	}

	@Override
	public byte[] getData(DecompilationContext dc) {
		ByteSerializer ser = new ByteSerializer(true);
		ser.addByte(OPCODE);
		ser.addShort(this.label.getPosition() - dc.getPosition());
		return ser.getBytes();
	}

	@Override
	public List<Label> getLabels() {
		List<Label> al = new ArrayList<Label>();
		al.add(this.label);
		return al;
	}

	@Override
	public Parameters getParameters() {
		Parameters params = getParameterTypes();
		params.addValue(this.label);
		return params;
	}

	@Override
	public Parameters getParameterTypes() {
		return new Parameters(new ParameterType[] { ParameterType.TYPE_LABEL });
	}

	@Override
	public void setParameters(Parameters params) {
		this.label = (Label) params.getObject(0);
	}

	@Override
	public List<StackElement> getPoppedElements(DecompilationContext dc) {
		List<StackElement> elements = new ArrayList<StackElement>();
		elements.add(new StackElement("value1", StackElementType.REF));
		elements.add(new StackElement("value2", StackElementType.REF));
		return elements;
	}

	@Override
	public void stackFlow(DecompilationContext dc) {
		Stack<StackElement> stack = dc.getStack();
		assertType(stack.pop(), StackElementType.REF);
		assertType(stack.pop(), StackElementType.REF);
	}

}
