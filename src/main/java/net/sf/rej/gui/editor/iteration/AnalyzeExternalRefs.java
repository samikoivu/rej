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
package net.sf.rej.gui.editor.iteration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.rej.gui.IterationContext;
import net.sf.rej.gui.IteratorAgentAdapter;
import net.sf.rej.gui.Link;
import net.sf.rej.gui.MainWindow;
import net.sf.rej.gui.tab.Tab;
import net.sf.rej.java.ClassFile;
import net.sf.rej.java.constantpool.ClassInfo;
import net.sf.rej.java.constantpool.ConstantPoolInfo;
import net.sf.rej.java.constantpool.RefInfo;
import net.sf.rej.java.instruction.Instruction;
import net.sf.rej.java.instruction.ParameterType;
import net.sf.rej.java.instruction.Parameters;

/**
 * <code>IteratorAgent</code> which analyzes references to external types
 * fields and methods. External means stuff that isn't part of the
 * project resources.
 * 
 * @author Sami Koivu
 */
public class AnalyzeExternalRefs extends IteratorAgentAdapter {
	protected int resultCount = 0;

	protected List<String> localTypes = new ArrayList<String>();
	
	protected Map<String, List<String>> refs = new HashMap<String, List<String>>();
	
	public AnalyzeExternalRefs() {
	}

	@Override
	public void processClass(IterationContext sc, ClassFile cf) {
		localTypes.add(cf.getFullClassName());
		
		// drop any already stored refs for this type, necessary because we're doing a single sweep
		refs.remove(cf.getFullClassName());
	}

	@Override
	public void processInstruction(IterationContext sc, Instruction instruction) {
		Parameters params = instruction.getParameters();
		for (int i = 0; i < params.getCount(); i++) {
			if (params.getType(i) == ParameterType.TYPE_CONSTANT_POOL_FIELD_REF) {
				ConstantPoolInfo cpi = sc.getDc().getConstantPool().get(
						params.getInt(i));
				RefInfo ri = (RefInfo) cpi;
				String extName = ri.getClassName().replace("[]", "");

				if (!localTypes.contains(extName)) {
					String instructionLine = instruction.getMnemonic() + " "
					+ instruction.getParameters().getString(sc.getDc());
					addRef(extName, instructionLine);
				}
			} else if (params.getType(i) == ParameterType.TYPE_CONSTANT_POOL_METHOD_REF) {
				ConstantPoolInfo cpi = sc.getDc().getConstantPool().get(
						params.getInt(i));
				RefInfo ri = (RefInfo) cpi;
				String extName = ri.getClassName().replace("[]", "");
				if (!localTypes.contains(extName)) {
					String instructionLine = instruction.getMnemonic() + " "
					+ instruction.getParameters().getString(sc.getDc());
					addRef(extName, instructionLine);
				}
			} else if (params.getType(i) == ParameterType.TYPE_CONSTANT_POOL_CLASS) {
				ConstantPoolInfo cpi = sc.getDc().getConstantPool().get(
						params.getInt(i));
				ClassInfo ci = (ClassInfo) cpi;
				String extName = ci.getName().replace("[]", "");
				if (!localTypes.contains(extName)) {
					String instructionLine = instruction.getMnemonic() + " "
					+ instruction.getParameters().getString(sc.getDc());
					addRef(extName, instructionLine);
				}
			}
		}
	}

	private void addRef(String extName, String instructionLine) {
		List<String> results = this.refs.get(extName);
		if (results == null) {
			results = new ArrayList<String>();
			this.refs.put(extName, results);
		}
		if (!results.contains(instructionLine)) {
			results.add(instructionLine);
		}
	}

}
