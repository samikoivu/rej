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
package net.sf.rej.gui.serialized.rendering;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

import net.sf.rej.gui.EditorFacade;
import net.sf.rej.java.serialized.StringContent;
import net.sf.rej.java.serialized.Value;

public class CellEditor implements TreeCellEditor {

	private JTree tree;
	private JTextField text = new JTextField(20);
	private Object value;
	private Object orig;

	public CellEditor() {
		text.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					tree.stopEditing();
				}
			}

			public void keyTyped(KeyEvent e) {
			}

		});
	}

	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		this.tree = tree;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		this.orig = node.getUserObject();
		if (this.orig instanceof Value) {
			Value v = (Value) this.orig;
			if (v.getValue() instanceof StringContent) {
				StringContent sc = (StringContent) v.getValue();
				this.text.setText(sc.getString());
			} else {
				this.text.setText(value.toString());
			}
		} else {
			this.text.setText(value.toString());
		}

		return this.text;
	}

	public void addCellEditorListener(CellEditorListener l) {
	}

	public void cancelCellEditing() {
	}

	public Object getCellEditorValue() {
		return this.value;
	}

	public void removeCellEditorListener(CellEditorListener l) {
	}

	public boolean shouldSelectCell(EventObject anEvent) {
		return false;
	}

	public boolean stopCellEditing() {
		this.value = this.text.getText();
		if (this.orig instanceof Value) {
			Value val = (Value) this.orig;
			if (val.getValue() instanceof Integer) {
				try {
					this.value = this.orig;
					Integer intVal = Integer.valueOf(this.text.getText());
					EditorFacade.getInstance().setValueContained(val, intVal);
				} catch (NumberFormatException nfe) {
					return false;
				}
			} else if (val.getValue() instanceof StringContent) {
				this.value = this.orig;
				EditorFacade.getInstance().setValueContained(val, new StringContent(this.text.getText()));
			}
		}
		return true; // TODO: validation (return false)
	}

    public boolean isCellEditable(EventObject anEvent) {
	    if (anEvent instanceof MouseEvent) {
	    	MouseEvent me = (MouseEvent) anEvent;
	    	if (me.getSource() instanceof JTree && me.getClickCount() >= 2) {
	    		JTree tree = (JTree) me.getSource();
	    		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
	    		Object obj = node.getUserObject();
	    		if (obj instanceof Value) {
	    			Value value = (Value) obj;
	    			if (value.getValue() instanceof Integer) {
	    				return true;
	    			}
	    			if (value.getValue() instanceof StringContent) {
	    				return true;
	    			}
	    		}
	    	}
	    }

	    return false;
	}
}
