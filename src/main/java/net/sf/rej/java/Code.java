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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.sf.rej.gui.InstructionHints;
import net.sf.rej.java.attribute.LineNumberTableAttribute;
import net.sf.rej.java.attribute.LocalVariableTableAttribute;
import net.sf.rej.java.constantpool.ConstantPool;
import net.sf.rej.java.constantpool.UTF8Info;
import net.sf.rej.java.instruction.Branchable;
import net.sf.rej.java.instruction.DecompilationContext;
import net.sf.rej.java.instruction.Instruction;
import net.sf.rej.java.instruction.Jumpable;
import net.sf.rej.java.instruction.Label;
import net.sf.rej.java.instruction.Returnable;
import net.sf.rej.java.instruction.StackElement;
import net.sf.rej.java.instruction.StackElementType;
import net.sf.rej.java.instruction._athrow;
import net.sf.rej.java.instruction._ret;
import net.sf.rej.util.ByteParser;
import net.sf.rej.util.ByteSerializer;

/**
 * <code>Code</code> class models a block of code.
 * 
 * @author Sami Koivu
 */
public class Code {

    private List<Instruction> code = new ArrayList<Instruction>();
    private List<String> errors = new ArrayList<String>();
    private DecompilationContext context;
    /**
     * Used for informing the line number table attribute about changes to
     * the instructions - if an instructions is removed, the pc positions
     * of the remaining instructions change, and these changes must be
     * propagated into the line number table.
     */
    private LineNumberTableAttribute lineNumbers = null;

    public Code(ByteParser parser, ConstantPool pool, ClassContext cc, Exceptions exceptions, int accessFlags, int descriptorIndex, int maxStack, int maxLocals) {
        this.context = new DecompilationContext();
        this.context.setConstantPool(pool);
        setExceptions(exceptions);
        InstructionSet set = InstructionSet.getInstance();
        this.context.setParser(parser);
        this.context.setPosition(0);
        DecompilationContext dc = createDecompilationContext();
        List<Label> labels = new ArrayList<Label>();
        
        // params to the rest of the local variables
        UTF8Info info = (UTF8Info) pool.get(descriptorIndex);
		Descriptor desc = new Descriptor(info.getValue());

        while (parser.hasMore()) {
            int opcode = parser.peekByte();

            try {
                Instruction instruction = set.getInstruction(opcode);
                if (instruction == null) {
                    this.errors.add("Unknown opcode at position " + dc.getPosition() + " trying to recover.");
                    continue;
                }
                int size = instruction.getSize(dc);
                byte[] data = parser.getBytes(size);
                instruction.setData(data, dc);
                
                List<Label> al = instruction.getLabels();
                if (al != null && al.size() > 0) {
                    labels.addAll(al);
                }

                this.code.add(instruction);

                dc.incrementPosition(size);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Simulate stack flow: TODO: it should be possible to disable this as it's a tad slow and not always very necessary
        if (false ) {
	        // reset dc
	        dc = createDecompilationContext();
	
	        
	        // TODO: enforce/validate max stack size
	        Stack<StackElement> stack = new Stack<StackElement>();
	        // TODO: enforce/validate max locals
	        RandomAccessArray lvs = new RandomAccessArray(maxLocals);
	
	        dc.setStack(stack);
	        dc.setLocalVariables(lvs);
	
	        // "this" to local variable 0 for non-static methods
	        int lvIndex = 0;
	        if (!AccessFlags.isStatic(accessFlags)) {
	            lvs.put(lvIndex++, StackElement.valueOf(cc.getClassName(), StackElementType.REF));
	        }
	
			for (JavaType jt : desc.getParamList()) {
				if (jt.getDimensionCount() > 0 || (!jt.isPrimitive())) {
					// array or primitive are both refs
		            lvs.put(lvIndex, StackElement.valueOf(jt.toString(),StackElementType.REF));
		            lvIndex++;
				} else {
					// primitive non-array
					if (jt.getType().equals("long")) {
						// CAT2, two slots
			            lvs.put(lvIndex, StackElement.valueOf(StackElementType.LONG));
			            lvIndex += 2;
					} else if (jt.getType().equals("float")) {
			            lvs.put(lvIndex, StackElement.valueOf(StackElementType.FLOAT));
			            lvIndex++;
					} else if (jt.getType().equals("double")) {
						// CAT2, two slots
			            lvs.put(lvIndex, StackElement.valueOf(StackElementType.DOUBLE));
			            lvIndex += 2;
					} else {
						// boolean, byte, short, char and int are all of type int
			            lvs.put(lvIndex, StackElement.valueOf(jt.getType(), StackElementType.INT));
			            lvIndex++;
					}
				}
			}
	
	        
			// iterate through all execution paths, storing the stack state for each pc, on reprocessing any pc, don't do it twice
			// if the stack is the same
			// do once from the beginning and do each of the exception handlers

			
			
			
			
			
			
            // verify if an exception handlers starts at this PC and put the exception on stack
            // TODO: might be worthwhile to optimize (ordered list and keep removing old exceptioninfos
            exceptions:
            for (ExceptionInfo ei : dc.getExceptions().getExceptionInfos()) {
            	if (ei.getHandlerPc() == dc.getPosition()) {
            		dc.getStack().push(StackElement.valueOf(ei.getType(), StackElementType.EXCEPTION));
            		break exceptions;
            	}
            }

			
			
			
			
			
			
			// TODO: figure out how the jsrs work
			
			// for each position we need to store 0 to n states (stack, lvs...)
			State[] state = new State[this.code.size()]; // to save states for each instruction
			
			dc.setPosition(0);
			int index = 0;
			stackanalysis:
			while (true) {
		        try {
		        	Instruction instruction = this.code.get(index);
		        	if (instruction instanceof _athrow) {
		        		
		        	} else if (instruction instanceof _ret) {
		        		
		        	} else if (instruction instanceof Branchable) {
		        		
		        	} else if (instruction instanceof Returnable) {
		        		
		        	} else if (instruction instanceof Jumpable) {
		        		
		        	} else {
		        		instruction.stackFlow(dc);
		        		dc.incrementPosition(instruction);
		        		index++;
		        	}
		        } catch(Exception e) {
		        	e.printStackTrace();
		        	break stackanalysis;
		        }
				
			}
        }

		// process labels of jumps, scopes, exceptions, etc
        insertLabels(labels);
    }

    public Code(ConstantPool pool) {
        this.context = new DecompilationContext();
        this.context.setConstantPool(pool);
    }

    /**
     * Inserts Labels into correct positions based on the pc information of the
     * label.
     * @param labels List A list object containing labels
     */
    public void insertLabels(List<Label> labels) {
        if(labels == null) return; // early return

        labelloop:
        for (Label label : labels ) {
            if (this.code.contains(label)) continue;
            
            DecompilationContext dc = createDecompilationContext();
            for (int j = 0; j < this.code.size(); j++) {
                Instruction instruction = this.code.get(j);
                if (dc.getPosition() == label.getPosition()) {
                    this.code.add(j, label);
                    continue labelloop;
                }
                dc.incrementPosition(instruction);
            }
            // add to end
            if (dc.getPosition() == label.getPosition()) {
                this.code.add(label);
            }
        }
    }

    /**
     * Debug type dump of instructions to System.out
     *
     * @param out the stream in which to dump the code.
     * @param lnAttr LineNumberTableAttribute
     * @param lvAttr LocalVariableTableAttribute
     */
    public void printCode(PrintStream out, LineNumberTableAttribute lnAttr, LocalVariableTableAttribute lvAttr) {
    	InstructionHints hints = new InstructionHints();
        out.println("---- code starts ----");
        DecompilationContext dc = createDecompilationContext();
        for (Instruction instruction : this.code) {
            String posStr = String.valueOf(dc.getPosition());
            while (posStr.length() < 5)
                posStr = " " + posStr;

            String hint = hints.getHint(instruction);
            if (hint.length() > 0)
                hint = "  (" + hint + ")";
            String lineNumberStr = "     ";
            if (lnAttr != null) {
                int lineNumber = lnAttr.getLineNumber(dc.getPosition());
                if (lineNumber != -1) {
                    lineNumberStr = String.valueOf(lineNumber);
                    while (lineNumberStr.length() < 5)
                        lineNumberStr = " " + lineNumberStr;
                }
            }


            out.println(lineNumberStr + "  " + posStr + " " + " "
                    + instruction.getMnemonic() + " "
                    + instruction.getParameters() + hint);
            dc.incrementPosition(instruction);
        }

        out.println("---- code ends ----");
    }

    public byte[] getData() {
        updateLabelPositions();
        ByteSerializer ser = new ByteSerializer(true);
        DecompilationContext dc = createDecompilationContext();
        for (Instruction instruction : this.code) {
            ser.addBytes(instruction.getData(dc));
            dc.incrementPosition(instruction);
        }

        return ser.getBytes();
    }

    public List<Instruction> getInstructions() {
        return this.code;
    }

    public DecompilationContext createDecompilationContext() {
        return this.context.createNew();
    }

    public void addInstructionAtPC(int pc, Instruction instruction) {
    	DecompilationContext dc = createDecompilationContext();
        int pos = -1;
        for (int i = 0; i < this.code.size(); i++) {
            Instruction otherInst = this.code.get(i);
            if (dc.getPosition() == pc) {
                pos = i; // get the last match, so insert will be after labels
            } else if (dc.getPosition() > pc) {
                break;
            }
            dc.incrementPosition(otherInst);
        }

        if (pc == 0) {
        	pos = 0;
        }

        if (pos == -1) {
        	if (dc.getPosition() == pc) {
        		pos = this.code.size();
        	} else {
        		throw new RuntimeException("Could not find given pc " + pc);
        	}
        }

        add(pos, instruction);
    }
    
    public void appendInstruction(Instruction instruction) {
    	add(this.code.size(), instruction);
    }

    public void add(int index, Instruction instruction) {
        this.code.add(index, instruction);
        this.insertLabels(instruction.getLabels());
        invalidateLineNumbers();
    }
    
    public void add(int index, List<Instruction> instructions) {
    	this.code.addAll(index, instructions);
    	// make sure any labels that might not have been included in the List are added, too
    	for (Instruction inst : instructions) {
            this.insertLabels(inst.getLabels());    		
    	}

        invalidateLineNumbers();
    }

    public int getPosition(Instruction instruction) {
        return this.code.indexOf(instruction);
    }

    public void remove(Instruction instruction) {
        List labels = instruction.getLabels();
        this.code.remove(instruction);
        if (labels != null) {
            this.code.removeAll(labels);
        }

        invalidateLineNumbers();
    }

    public void updateLabelPositions() {
        DecompilationContext dc = createDecompilationContext();
        for (Instruction instruction : this.code) {
            if (instruction instanceof Label) {
                ((Label) instruction).setPosition(dc.getPosition());
            }

            dc.incrementPosition(instruction);
        }
    }

    public int getMaxPC() {
        DecompilationContext dc = createDecompilationContext();
        for (Instruction instruction : this.code) {
            dc.incrementPosition(instruction);
        }
        return dc.getPosition();
    }

    public void moveUp(Instruction instruction) {
        int pos = getPosition(instruction);
        // TODO: validation, throw exception
        if (pos == 0)
            return;
        this.code.remove(pos);
        this.code.add(pos - 1, instruction);
        invalidateLineNumbers();
    }

    public void moveDown(Instruction instruction) {
        int pos = getPosition(instruction);
        // TODO: validation, throw exception
        if (pos == this.code.size() - 1)
            return;
        this.code.remove(pos);
        this.code.add(pos + 1, instruction);
        invalidateLineNumbers();
    }
    
    private void invalidateLineNumbers() {
    	if (this.lineNumbers != null) {
    		this.lineNumbers.invalidate();
    	}
    }
    
    public void setLineNumberTable(LineNumberTableAttribute lineNumbers) {
    	this.lineNumbers = lineNumbers;
    }

	public void setLocalVariableTable(LocalVariableTableAttribute lvta) {
		this.context.setLocalVariableTable(lvta);
	}

	public void setExceptions(Exceptions exceptions) {
		this.context.setExceptions(exceptions);
	}
}