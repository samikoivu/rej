/* Copyright (C) 2004-2011 Sami Koivu
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
package net.sf.rej.gui.action.android.xml;

import net.sf.rej.android.Attribute;
import net.sf.rej.gui.Undoable;

public class EditIntegerAttributeAction implements Undoable {

	private int oldValue;
	private Integer newValue;
	private Attribute attr;
	
	public EditIntegerAttributeAction(Attribute attr, Integer value) {
		this.attr = attr;
		this.newValue = value;
		this.oldValue = attr.getResourceId();
	}

	public void execute() {
		this.attr.setResourceId(this.newValue);
	}

	public void undo() {
		this.attr.setResourceId(this.oldValue);
	}

}
