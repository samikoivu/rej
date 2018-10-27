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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import net.sf.rej.gui.editor.rendering.JavaBytecodeSyntaxDrawer;
import net.sf.rej.java.JavaType;
import net.sf.rej.java.serialized.BlockData;
import net.sf.rej.java.serialized.ClassDesc;
import net.sf.rej.java.serialized.ClassDescInfo;
import net.sf.rej.java.serialized.FieldDesc;
import net.sf.rej.java.serialized.Null;
import net.sf.rej.java.serialized.SerializedObject;
import net.sf.rej.java.serialized.StringContent;
import net.sf.rej.java.serialized.Value;
import net.sf.rej.util.ByteToolkit;

public class SerializedRenderer {
	private Icon fieldIcon;
	private Icon classIcon;
	private Icon annotation;
	private Icon variable;
	private Icon binary;
	private Icon defaultIcon = UIManager.getIcon("Tree.leafIcon");

	public SerializedRenderer() {
		loadIcons();
	}

	public Icon getIcon(Object value) {
		if (value instanceof SerializedObject) {
			return this.classIcon;
		} else if (value instanceof StringContent) {
			return this.variable;
		} else if (value instanceof Value) {
			Value v = (Value) value;
			if (v.getValue() instanceof SerializedObject) {
				return this.classIcon;
			} else {
				return this.variable;
			}
		} else if (value instanceof FieldDesc) {
			return this.fieldIcon;
		} else if (value instanceof BlockData) {
			return this.binary;
		} else if (value instanceof String) {
			if ("Contents".equals(value)) {
				return this.defaultIcon;
			} else if ("Annotations".equals(value)) {
				return this.annotation;
			}
		}

		return this.defaultIcon;
	}

	public void render(JavaBytecodeSyntaxDrawer sd, Object value) {
		if (value == null) return; // early return

		// Do the drawing
		if (value instanceof SerializedObject) {
			drawObjectDesc(sd, (SerializedObject) value);
		} else if (value instanceof StringContent) {
			StringContent sc = (StringContent) value;
			sd.drawString("\"" + sc.getString() + "\"");
			sd.drawAnnotation(" @" + sc.getIdentityHashcode());
		} else if (value instanceof Value) {
			Value v = (Value) value;
			if (v.getValue() instanceof SerializedObject) {
				drawObjectDesc(sd, (SerializedObject) v.getValue());
			} else if (v.getValue() instanceof Null) {
				sd.drawKeyword("null");
			} else if (v.getValue() instanceof StringContent) {
				StringContent sc = (StringContent) v.getValue();
				sd.drawString("\"" + sc.getString() + "\"");
				sd.drawAnnotation(" @" + Long.toHexString(sc.getIdentityHashcode()));
			} else if (v.getValue() instanceof Boolean) {
				sd.drawKeyword(v.getValue().toString());
			} else {
				// TODO: show type? such as: (int) 91
				sd.drawDefault(v.getValue().toString());
			}
		} else if (value instanceof FieldDesc) {
			FieldDesc field = (FieldDesc) value;
			JavaType jt = field.getJavaType();
			if (jt.isPrimitive()) {
				sd.drawKeyword(jt.getType());
			} else {
				sd.drawDefault(jt.getType());
			}
			sd.drawDefault(jt.getDimensions());
			sd.drawDefault(" ");
			sd.drawDefault(field.getName());
		} else if (value instanceof String && "Annotations".equals(value)) {
			sd.drawInstruction("Annotations");
		} else if (value instanceof String && "Contents".equals(value)) {
			sd.drawInstruction("Contents");
		} else if (value instanceof BlockData) {
			BlockData bd = (BlockData) value;
			if (bd.getData().length > 32) {
				sd.drawDefault(ByteToolkit.getHexString(bd.getData(), 32));
				sd.drawComment(" ... (" + bd.getData().length + " bytes total)");
			} else {
				if (bd.getData().length == 1 && (bd.getData()[0]==0 || bd.getData()[0] ==1)) {
					sd.drawDefault(ByteToolkit.getHexString(bd.getData()));
					sd.drawComment(" (");
					sd.drawKeyword((bd.getData()[0] == 0 ? "false" : "true"));
					sd.drawComment(")");
				} else if (bd.getData().length == 2) {
					// TODO: short, char
					sd.drawDefault(ByteToolkit.getHexString(bd.getData()));
				} else if (bd.getData().length == 4) {
					sd.drawDefault(ByteToolkit.getHexString(bd.getData()));
					sd.drawComment(" (" + ByteToolkit.getInt(bd.getData()) + ")");
				} else if (bd.getData().length == 8) {
					sd.drawDefault(ByteToolkit.getHexString(bd.getData()));
					sd.drawComment(" (" + ByteToolkit.getLong(bd.getData()) + "L)");
				} else {
					sd.drawDefault(ByteToolkit.getHexString(bd.getData()));
				}
			}
		} else {
			sd.drawDefault(value.toString());
			new RuntimeException("No handler at SerializedRenderer for " + value.getClass().getName()).printStackTrace();
			if (value instanceof String) {
				System.out.println(">>" + value + "<<");
			}
		}
	}

	private void drawObjectDesc(JavaBytecodeSyntaxDrawer sd, SerializedObject so) {
		ClassDesc desc = so.getClassDesc();
		sd.drawDefault(desc.getName());
		sd.drawDefault(" ");
		sd.drawAnnotation("@");
		sd.drawAnnotation(Long.toHexString(so.getIdentityHashcode()));
		ClassDescInfo cdi = desc.getClassDescInfo();
		ClassDesc superClass = cdi.getSuperClass();
		while (superClass != null && !(superClass instanceof Null)) {
			sd.drawDefault(" ");
			sd.drawKeyword("extends");
			sd.drawDefault(" ");
			sd.drawDefault(superClass.getName());
			superClass = superClass.getClassDescInfo().getSuperClass();
		}
	}

	private void loadIcons() {
    	try {
    		this.fieldIcon = new ImageIcon(getClass().getResource("/img/field_private_obj.gif"));
    		this.fieldIcon = new ImageIcon(getClass().getResource("/img/field_private_obj.gif"));
    		this.annotation = new ImageIcon(getClass().getResource("/img/att_class_obj.gif"));
    		this.variable = new ImageIcon(getClass().getResource("/img/variable_tab.gif"));
    		this.binary = new ImageIcon(getClass().getResource("/img/binary.gif"));
    	} catch(NullPointerException npe) {
    		this.fieldIcon = new ImageIcon("img/field_private_obj.gif");
    		this.classIcon = new ImageIcon("img/class_obj.gif");
    		this.annotation = new ImageIcon("img/att_class_obj.gif");
    		this.variable = new ImageIcon("img/variable_tab.gif");
    		this.binary = new ImageIcon("img/binary.gif");
    	}

	}

}
