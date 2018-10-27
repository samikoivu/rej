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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.sf.rej.gui.DefaultMatcher;
import net.sf.rej.gui.IterationContext;
import net.sf.rej.gui.Link;
import net.sf.rej.gui.SystemFacade;
import net.sf.rej.gui.editor.iteration.AnalyzeExternalRefs;
import net.sf.rej.gui.event.Event;
import net.sf.rej.gui.event.EventObserver;

/**
 * Tab that does analysis on the project resources and prints out reports
 *
 * @author Sami Koivu
 */
public class AnalysisTab extends JPanel implements Tabbable, EventObserver {
    JPanel freeTextPanel = new JPanel();
    JLabel jLabel1 = new JLabel();
    JButton freeTextButton = new JButton();
    JPanel resultPanel = new JPanel();
    JScrollPane jScrollPane1 = new JScrollPane();
    DefaultListModel model = new DefaultListModel();
    JList resultList = new JList(this.model);
    JRadioButton jRadioButton1 = new JRadioButton();
    ButtonGroup typeGroup = new ButtonGroup();

    public AnalysisTab() {
        try {
            this.setLayout(new GridBagLayout());
            this.freeTextPanel.setLayout(new GridBagLayout());
            this.setBackground(SystemColor.control);
            this.setBorder(BorderFactory.createEtchedBorder());
            this.jLabel1.setText("Analysis");
            this.freeTextButton.setText("Execute");
            this.freeTextButton.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
                    executeAnalysis();
            	}
            });
            this.resultPanel.setLayout(new BorderLayout());
            this.resultList.addMouseListener(new MouseAdapter() {
            	@Override
            	public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        Link link = (Link) resultList.getSelectedValue();
                        if (link != null) {
                            SystemFacade.getInstance().goTo(link);
                        }
                    }
            	}
            });
            this.jRadioButton1.setActionCommand("ext");
            this.jRadioButton1.setSelected(true);
            this.jRadioButton1.setText("External dependencies");
            this.add(this.freeTextPanel, new GridBagConstraints(0, 0, 1, 1,
                    1.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 30));
            this.freeTextPanel.add(this.freeTextButton, new GridBagConstraints(
                    1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            this.freeTextPanel.add(this.jLabel1, new GridBagConstraints(0, 0,
                    2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            this.freeTextPanel.add(this.jRadioButton1, new GridBagConstraints(
                    0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            this.add(this.resultPanel, new GridBagConstraints(0, 1, 1, 1, 1.0,
                    2.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            this.resultPanel.add(this.jScrollPane1, BorderLayout.CENTER);
            this.jScrollPane1.getViewport().add(this.resultList, null);
            this.typeGroup.add(this.jRadioButton1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void redo() {
    }

    public void undo() {
    }

    public void insert() {
    }

    public void remove() {
    }

    public void goTo(Link link) {
    }

    public void find() {
    }

    public void findNext() {
    }

    public void clear() {
        this.model.clear();
    }

    public void addResult(final Link link) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AnalysisTab.this.model.addElement(link);
            }
        });
    }

	private void showResults(Map<String, List<String>> refs) {
		System.out.println("refs size: " + refs.size());
		for (Entry<String, List<String>> entry : refs.entrySet()) {
			for (String occurance : entry.getValue()) {
				AnalysisTab.this.model.addElement(entry.getKey() + " : " + occurance);
			}
		}
	}

	private void executeAnalysis() {
        String cmd = this.typeGroup.getSelection().getActionCommand();
        this.model.clear();
        AnalyzeExternalRefs analyzer = new AnalyzeExternalRefs() {
        	@Override
        	public void finished(IterationContext ic, int totalCount) {
        		super.finished(ic, totalCount);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                    	showResults(refs);
                    }
                });
           	}
        };

        analyzer.setProgressMonitor(SystemFacade.getInstance()
                .getProgressMonitor());
        SystemFacade.getInstance().search(analyzer);
    }

	public void outline() {
	}

	public void leavingTab() {
	}

	public String getTabTitle() {
		return "Analysis";
	}

	public void enteringTab() {
	}

	public void processEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

}