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
package net.sf.rej.gui.structure;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

public class StructureNode extends DefaultMutableTreeNode {

	private List<Action> actions = new ArrayList<Action>();
	
	public StructureNode() {
	}

	public JPopupMenu getContextMenu() {
		if (this.actions.size() > 0) {
			JPopupMenu contextMenu = new JPopupMenu();
			for (Action action : this.actions) {
				contextMenu.add(new JMenuItem(action));
			}
			return contextMenu;
		}
		return null;
	}

	public void addContextMenuItem(Action action) {
		this.actions.add(action);
	}

}
