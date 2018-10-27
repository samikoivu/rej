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
package net.sf.rej.gui.action.serialized;

import net.sf.rej.gui.Undoable;
import net.sf.rej.java.serialized.Value;

public class UpdateValueContentAction implements Undoable {

	private Value value;
	private Object newContent;
	private Object oldContent;

	public UpdateValueContentAction(Value value, Object content) {
		this.value = value;
		this.newContent = content;
		this.oldContent = value.getValue();
	}

	public void execute() {
		this.value.setValue(this.newContent);
	}

	public void undo() {
		this.value.setValue(this.oldContent);
	}

}
