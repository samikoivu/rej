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
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import net.sf.rej.gui.editor.rendering.GraphicsSyntaxDrawer;
import net.sf.rej.gui.editor.rendering.WidthCalculatorDrawer;

public class SerializedTreeRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;

	private Object value;

	private SerializedRenderer renderer = new SerializedRenderer();

	private int width = 0;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		if (tree.getGraphics() == null) {
			// early return
			return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}

		this.selected = sel;

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		this.value = node.getUserObject();

		WidthCalculatorDrawer wcd = new WidthCalculatorDrawer(tree.getGraphics());
		wcd.setOffset(20); // icon
		this.renderer.render(wcd, this.value);
		this.width = wcd.getMaxWidth();

		return this;
	}

	@Override
	public void paint(Graphics g) {
		setIcon(this.renderer.getIcon(this.value));
		super.paint(g);

		Dimension d = this.getSize();
		GraphicsSyntaxDrawer sd = new GraphicsSyntaxDrawer(g, d);
		sd.setOffset(20); // icon
		this.renderer.render(sd, this.value);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.width, 18);
	}

}
