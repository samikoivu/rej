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
package net.sf.rej.gui.editor.rendering;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;


/**
 * Class used to draw syntax highlighted text into a graphics
 * object
 *
 * @author Sami Koivu
 */

public class WidthCalculatorDrawer implements JavaBytecodeSyntaxDrawer {
    private static final Font BOLD = new Font("Monospaced", Font.BOLD, 14);

    private int offset = 0;
    private int maxOffset = 0;
    private FontMetrics fm;

    public WidthCalculatorDrawer(Graphics g) {
    	if (g != null) {
        	this.fm = g.getFontMetrics(BOLD);
    	}
    }

    public void drawIndent() {
    	drawDefault("    ");
    }

    public void drawKeyword(String text) {
        this.offset += this.fm.stringWidth(text);
        updateMaxOffset();
    }

    public void drawComment(String text) {
        this.offset += this.fm.stringWidth(text);
        updateMaxOffset();
    }

    public void drawAnnotation(String text) {
        this.offset += this.fm.stringWidth(text);
        updateMaxOffset();
    }

    public void drawString(String text) {
        this.offset += this.fm.stringWidth(text);
        updateMaxOffset();
    }

    public void drawField(String text) {
        this.offset += this.fm.stringWidth(text);
        updateMaxOffset();
    }

    public void drawDefault(String text) {
        this.offset += this.fm.stringWidth(text);
        updateMaxOffset();
    }

    public void drawDefaultOverstrike(String text) {
        this.offset += this.fm.stringWidth(text);
        updateMaxOffset();
    }

    public void drawInstruction(String text) {
        this.offset += this.fm.stringWidth(text);
        updateMaxOffset();
    }

    public void drawSmall(String text, int offset) {
    }

	public int getMaxWidth() {
		return this.maxOffset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
        updateMaxOffset();
	}

	public void reset() {
		this.offset = 0;
		this.maxOffset = 0;
	}

	public void drawBreakpoint() {
		// do nothing
	}

	public void setExecutionBackground() {
		// do nothing
	}

	public void drawImplementedClue() {
		// do nothing
	}

	public void drawOverriddenClue() {
		// do nothing
	}

	private void updateMaxOffset() {
		this.maxOffset = Math.max(this.maxOffset, this.offset);
	}

}
