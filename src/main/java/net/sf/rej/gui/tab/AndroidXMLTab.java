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
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import net.sf.rej.android.Attribute;
import net.sf.rej.android.BinaryXMLFile;
import net.sf.rej.android.Tag;
import net.sf.rej.android.XMLElement;
import net.sf.rej.gui.EditorFacade;
import net.sf.rej.gui.Link;
import net.sf.rej.gui.MainWindow;
import net.sf.rej.gui.SystemFacade;
import net.sf.rej.gui.action.android.xml.EditIntegerAttributeAction;
import net.sf.rej.gui.action.android.xml.EditStringAttributeAction;
import net.sf.rej.gui.action.android.xml.RemoveTagAction;
import net.sf.rej.gui.event.Event;
import net.sf.rej.gui.event.EventObserver;
import net.sf.rej.gui.event.EventType;
import net.sf.rej.gui.structure.StructureNode;

public class AndroidXMLTab extends JPanel implements Tabbable, EventObserver {
    private JTree tree = new JTree();
    private boolean isOpen = false;
    private boolean upToDate = false;
    BinaryXMLFile xml = null;
	private DefaultTreeModel model;

	private Action editAttrAction = new AbstractAction("Edit") {
		public void actionPerformed(ActionEvent e) {
			StructureNode sn = (StructureNode) tree.getSelectionPath().getLastPathComponent();
			Attribute attr = (Attribute) sn.getUserObject();
			Object currentValue = attr.getAttributeValue();
			String ret = JOptionPane.showInputDialog(MainWindow.getInstance(), "Enter new value: ", currentValue);
			if (ret != null) {
				if (currentValue instanceof Integer) {
					// integer
					if (!Integer.valueOf(ret).equals(currentValue)) {
						SystemFacade.getInstance().performAction(new EditIntegerAttributeAction(attr, Integer.valueOf(ret)));
					}
				} else {
					// string
					if (!ret.equals(currentValue)) {
						SystemFacade.getInstance().performAction(new EditStringAttributeAction(attr, ret));
					}
				}
			}
		}
	};

	
	
    public AndroidXMLTab() {
        this.setLayout(new BorderLayout());
        this.add(new JPanel(), BorderLayout.CENTER);

        this.tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				splitSynchronize();
			}
        });

        this.tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				if (me.getButton() == MouseEvent.BUTTON3) {
					// right click
					TreePath path = tree.getPathForLocation(me.getX(), me.getY());
					if (path == null) {
						return; // early return
					}

					Object obj = path.getLastPathComponent();
					if (obj instanceof StructureNode) {
						StructureNode sn = (StructureNode) obj;
						JPopupMenu menu = sn.getContextMenu();
						if (menu != null) {
							menu.show(tree, me.getX(), me.getY());
						}
					}
				}
			}
		});

    }

    public void refresh() {
		if (this.xml != null) {
			this.removeAll();
			this.add(new JScrollPane(this.tree), BorderLayout.CENTER);
			StructureNode root = new StructureNode();
			root.setUserObject(this.xml.getDocumentRoot());
			this.model = new DefaultTreeModel(root);
			this.tree.setModel(model);
			addChildren(root);
			for (int i=0; i < this.tree.getRowCount(); i++) {
				tree.expandRow(i);
			}
		} else {
			this.removeAll();
			this.add(new JPanel());
		}
		this.upToDate = true;
    }

    private void addChildren(StructureNode root) {
    	XMLElement el = (XMLElement) root.getUserObject();
        if (el instanceof Tag) {
        	Tag tag = (Tag) el;
	        for (Attribute attr : tag.getAttributes()) {
	        	StructureNode sn = new StructureNode();
	        	sn.setUserObject(attr);
	        	sn.addContextMenuItem(this.editAttrAction);
	        	this.model.insertNodeInto(sn, root, root.getChildCount());
	        }
        }
        
        for (XMLElement child : el.getChildren()) {
        	StructureNode sn = new StructureNode();
        	sn.setUserObject(child);
        	this.model.insertNodeInto(sn, root, root.getChildCount());
        	addChildren(sn);
        }
	}

	public void redo() {
        EditorFacade.getInstance().performRedo();
    }

    public void undo() {
        EditorFacade.getInstance().performUndo();
    }

    public void insert() {
    }

    public void remove() {
    	TreePath path = this.tree.getSelectionPath();
    	if (path != null) {
    		StructureNode node = (StructureNode) path.getLastPathComponent();
    		XMLElement el = (XMLElement) node.getUserObject();
    		if (el instanceof Tag) {
    			Tag tag = (Tag) el;
	    		if (tag.getParent() != null) {
	    			SystemFacade.getInstance().performAction(new RemoveTagAction(tag));
	    			this.model.removeNodeFromParent(node);
	    		}
	    	} else if (el instanceof Attribute) {
	    		Attribute attr = (Attribute) el;
	    		// TODO: remove attribute
	    	}
    	}
    }

    public void goTo(Link link) {
    }

    public void find() {
    }

    public void findNext() {
    }

	public void processEvent(Event event) {
        try {
        	if (event.getType() == EventType.BINARY_XML_OPEN) {
        		this.xml = event.getBinaryXML();
        		this.upToDate = false;
        		if (this.isOpen) {
        			refresh();
        		}
        	}

        	if (event.getType() == EventType.SERIALIZED_OPEN || event.getType() == EventType.RAW_OPEN || event.getType() == EventType.CLASS_OPEN) {
        		this.xml = null;
        		this.upToDate = false;
        		if (this.isOpen) {
        			refresh();
        		}
        	}
        } catch(Exception e) {
            SystemFacade.getInstance().handleException(e);
        }
    }

	public void outline() {
	}

	public void leavingTab() {
		this.isOpen = false;
	}

	public String getTabTitle() {
		return "APK Binary XML";
	}

	public void enteringTab() {
		this.isOpen = true;
		if (!this.upToDate) {
			refresh();
		}
		splitSynchronize();
	}

	private void splitSynchronize() {
	}

}
