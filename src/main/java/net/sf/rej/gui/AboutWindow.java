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
package net.sf.rej.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.sf.rej.Version;

/**
 * An about Window displaying general information about reJ.
 *
 * @author Sami Koivu
 */
public class AboutWindow extends JDialog {

	private static AboutWindow instance = null;

	private static final long serialVersionUID = 1L;

	private JPanel imagePane = null;

	private JButton okButton = null;

	private Image image = null;

	public AboutWindow(Frame owner) {
		super(owner);
		initialize();
	}

	private void initialize() {
		this.setResizable(false);
		this.setTitle("About " + Version.VERSION_STRING);

		URL imageURL = getClass().getResource("/img/about_75.png");
		if (imageURL != null) {
			this.image = Toolkit.getDefaultToolkit().createImage(imageURL);
		} else {
			this.image = Toolkit.getDefaultToolkit().createImage("img/about_75.png");
		}

		this.getContentPane().setLayout(null);
		this.setSize(385, 245);
		this.getContentPane().add(getImagePane());
		getImagePane().setBounds(0, 0, 385, 245);
		this.getContentPane().add(getOkButton());
		getOkButton().setBounds(149, 170, 80, 30);
		addMouseListener(new MouseAdapter() {
			Rectangle projectLink = new Rectangle(20, 57, 164, 11);
			@Override
			public void mouseReleased(MouseEvent e) {
				if (projectLink.contains(e.getPoint()) ) {
					openLink("http://rejava.sourceforge.net/");
				}
			}
		});
	}

	private void openLink(String url) {
		if (System.getProperty("os.name").startsWith("Windows")) {
			try {
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private JPanel getImagePane() {
		if (imagePane == null) {
			imagePane = new JPanel() {
				@Override
				public void paint(Graphics g) {
					g.drawImage(image, 0, -45, new ImageObserver() {

						public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
							if ((infoflags & ImageObserver.ALLBITS) == 0) {
								return true;
							} else {
								imagePane.repaint();
								return false;
							}
						}

					});

					g.setFont(g.getFont().deriveFont(Font.BOLD));

					g.setColor(Color.LIGHT_GRAY);
					g.drawString(Version.VERSION_STRING, 20, 20);
					g.drawString("http://rejava.sourceforge.net/", 20, 40);
					g.setColor(Color.BLACK);
					g.drawString(Version.VERSION_STRING, 18, 18);
					g.drawString("http://rejava.sourceforge.net/", 18, 38);

					g.setClip(okButton.getBounds());
					okButton.paint(g);
				}
			};

		}

		return imagePane;
	}

	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("OK");
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					AboutWindow.this.setVisible(false);
				}
			});
		}
		return okButton;
	}

	public static void invoke() {
		getInstance().setVisible(true);
		getInstance().repaint();
	}

	public static AboutWindow getInstance() {
		if (instance == null) {
			instance = new AboutWindow(MainWindow.getInstance());
			instance.setLocationRelativeTo(MainWindow.getInstance());
		}

		return instance;
	}

}
