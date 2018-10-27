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

import java.util.ArrayList;
import java.util.List;

import net.sf.rej.android.XMLElement;
import net.sf.rej.gui.Undoable;

public class RemoveTagAction implements Undoable {

	private XMLElement tag;
	private XMLElement parent;
	private List<XMLElement> children;

    public RemoveTagAction(XMLElement tag) {
    	this.tag = tag;
    	this.parent = tag.getParent();
	}

	public void execute() {
		// create a copy of siblings (with order) before modification
		this.children = new ArrayList<XMLElement>();
		this.children.addAll(this.parent.getChildren());
		
		this.parent.getChildren().remove(this.tag);
    }

    public void undo() {
        this.parent.setChildren(children);
    }

}
