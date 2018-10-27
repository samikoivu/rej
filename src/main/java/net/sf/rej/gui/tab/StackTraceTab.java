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
package net.sf.rej.gui.tab;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;

import net.sf.rej.files.ClassLocator;
import net.sf.rej.gui.Link;
import net.sf.rej.gui.SystemFacade;

public class StackTraceTab extends JPanel implements Tabbable {

	private JEditorPane editor;

	public StackTraceTab() {
		super();

		this.editor = new JEditorPane();
		this.editor.setContentType("text/html");

		this.setLayout(new BorderLayout());
		this.add(new JLabel("Paste stack traces here and follow the hyperlinks to the referenced bytecode."), BorderLayout.NORTH);
		this.add(new JScrollPane(this.editor), BorderLayout.CENTER);

		this.editor.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					String[] components = e.getDescription().split(":");
					ClassLocator locator = SystemFacade.getInstance().getClassIndex().getLocator(components[0]);
					if (locator == null) {
						SystemFacade.getInstance().setStatus("Class " + components[0] + " not found in classpath.");
					} else {
						Link link = new Link();
						link.setAnchor(Link.ANCHOR_SOURCE_LINE_NUMBER);
						link.setTab(Tab.EDITOR);
						link.setFile(locator.getFile());
						link.setClassLocator(locator);
						link.setPosition(Integer.parseInt(components[1]));
						SystemFacade.getInstance().goTo(link);
					}
				}
			}
		});

		this.editor.setEditable(false);

		this.editor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_V &&
					e.isControlDown()) {
					Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
					StringBuilder sb = new StringBuilder();
					try {
						//InputStream is = (InputStream) board.getData(DataFlavor.getTextPlainUnicodeFlavor());
						BufferedReader br = new BufferedReader(DataFlavor.getTextPlainUnicodeFlavor().getReaderForText(board.getContents(null)));
						while (true) {
							String line = br.readLine();
							if (line == null) break;

							sb.append(processLine(line));
						}
						br.close();

						editor.setText(sb.toString());
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					try {
						editor.getDocument().remove(editor.getSelectionStart(), editor.getSelectionEnd() - editor.getSelectionStart());
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

	}

	private String processLine(String line) {
		String htmlLine = line;
		if (line.trim().startsWith("at ")){
			int atIndex = line.indexOf("at ");
			int parenthesisIndex = line.indexOf("(");
			if (parenthesisIndex != -1) {
				String classMethodString = line.substring(atIndex + 3, parenthesisIndex);
				String className = classMethodString.substring(0, classMethodString.lastIndexOf("."));
				int colonIndex = line.indexOf(":", parenthesisIndex);
				int parenthesisCloseIndex = line.indexOf(")", parenthesisIndex);
				if (colonIndex != -1 && parenthesisCloseIndex != -1) {
					String lineNumber = line.substring(colonIndex + 1, parenthesisCloseIndex);
					htmlLine = line.substring(0, atIndex + 3).replace(" ", "&nbsp;").replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;")
					        + "<a href=\"" + className + ":" + lineNumber + "\">"
					        + line.substring(atIndex + 3)
					        + "</a>";
				}
			}
		}

		return htmlLine + "<BR>";
	}

	public void enteringTab() {
	}

	public void find() {
	}

	public void findNext() {
	}

	public String getTabTitle() {
		return "Stack Trace";
	}

	public void goTo(Link link) {
	}

	public void insert() {
	}

	public void leavingTab() {
	}

	public void outline() {
	}

	public void redo() {
	}

	public void remove() {
	}

	public void undo() {
	}

}
