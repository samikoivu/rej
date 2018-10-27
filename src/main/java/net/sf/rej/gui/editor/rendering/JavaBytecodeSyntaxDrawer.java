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


public interface JavaBytecodeSyntaxDrawer {

	/**
	 * Indents current line by one block (4 spaces by default).
	 *
	 */
    public void drawIndent();

    /**
     * Draws the argument text with keyword formatting.
     * @param text a String to draw.
     */
    public void drawKeyword(String text);

    /**
     * Draws the argument text with comment formatting.
     * @param text a String to draw.
     */
    public void drawComment(String text);

    /**
     * Draws the argument text with annotation formatting.
     * @param text a String to draw.
     */
    public void drawAnnotation(String text);

    /**
     * Draws the argument text with String formatting.
     * @param text a String to draw.
     */
    public void drawString(String text);

    /**
     * Draws the argument text with field formatting.
     * @param text a String to draw.
     */
    public void drawField(String text);

    /**
     * Draws the argument text with default (no) formatting.
     * @param text a String to draw.
     */
    public void drawDefault(String text);

    /**
     * Draws the argument text with overstrike formatting.
     * @param text a String to draw.
     */
    public void drawDefaultOverstrike(String text);

    /**
     * Draws the argument text with instruction formatting.
     * @param text a String to draw.
     */
    public void drawInstruction(String text);

    /**
     * Draws the argument text with small formatting.
     * @param text a String to draw.
     * @param offset an offset (in pixels) where to draw.
     */
    public void drawSmall(String text, int offset);

    /**
     * Sets the drawing offset to the argument value.
     * @param offset an offset (in pixels) where to draw the next item.
     */
	public void setOffset(int offset);

	/**
	 * Draws the symbol signalling an active breakpoint on the line.
	 *
	 */
	public void drawBreakpoint();

	/**
	 * Sets the background of the current line to signal that it is the
	 * current execution line.
	 *
	 */
	public void setExecutionBackground();

	/**
	 * Draws the overridden clue on the current line, signalling that the
	 * method defined on the current line is overriding a method in one of
	 * the superclasses.
	 *
	 */
	public void drawOverriddenClue();

	/**
	 * Draws the implemented clue on the current line, signalling that the
	 * method defined on the current line is implementing a method in one
	 * of the interfaces implemented by this class or one of the superclasses.
	 *
	 */
	public void drawImplementedClue();

}
