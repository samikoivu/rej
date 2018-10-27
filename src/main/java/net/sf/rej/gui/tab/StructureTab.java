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
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import net.sf.rej.gui.EditorFacade;
import net.sf.rej.gui.Link;
import net.sf.rej.gui.MainWindow;
import net.sf.rej.gui.SystemFacade;
import net.sf.rej.gui.action.ModifyAttributeDataAction;
import net.sf.rej.gui.action.ModifyClassVersionAction;
import net.sf.rej.gui.dialog.ClassVersionDialog;
import net.sf.rej.gui.event.Event;
import net.sf.rej.gui.event.EventObserver;
import net.sf.rej.gui.event.EventType;
import net.sf.rej.gui.hexeditor.ByteArrayDataProvider;
import net.sf.rej.gui.hexeditor.HexEditorDialog;
import net.sf.rej.gui.split.StructureSplitSynchronizer;
import net.sf.rej.gui.structure.AccessFlagsNode;
import net.sf.rej.gui.structure.AttributeNode;
import net.sf.rej.gui.structure.AttributesNode;
import net.sf.rej.gui.structure.ClassFileNode;
import net.sf.rej.gui.structure.ConstantPoolNode;
import net.sf.rej.gui.structure.FieldNode;
import net.sf.rej.gui.structure.FieldsNode;
import net.sf.rej.gui.structure.InterfaceNode;
import net.sf.rej.gui.structure.InterfacesNode;
import net.sf.rej.gui.structure.MagicNode;
import net.sf.rej.gui.structure.MethodAccessFlagsNode;
import net.sf.rej.gui.structure.MethodDescriptorNode;
import net.sf.rej.gui.structure.MethodNameNode;
import net.sf.rej.gui.structure.MethodNode;
import net.sf.rej.gui.structure.MethodsNode;
import net.sf.rej.gui.structure.StructureNode;
import net.sf.rej.gui.structure.SuperClassNode;
import net.sf.rej.gui.structure.ThisClassNode;
import net.sf.rej.gui.structure.VersionNode;
import net.sf.rej.java.ClassFile;
import net.sf.rej.java.Field;
import net.sf.rej.java.Interface;
import net.sf.rej.java.Method;
import net.sf.rej.java.attribute.Attribute;
import net.sf.rej.java.attribute.Attributes;
import net.sf.rej.java.attribute.CodeAttribute;
import net.sf.rej.util.Range;

public class StructureTab extends JPanel implements Tabbable, EventObserver {
    private JTree tree = new JTree();
    private ClassFile cf = null;
    private boolean isOpen = false;
    private boolean upToDate = false;
	private StructureSplitSynchronizer sync;
	private Map<Object, Range> offsets;
	private DefaultTreeModel model;

    public StructureTab() {
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
		if (this.cf != null) {
			this.removeAll();
			this.add(new JScrollPane(this.tree), BorderLayout.CENTER);
			StructureNode root = new StructureNode();
			root.setUserObject(new ClassFileNode(this.cf));
			this.model = new DefaultTreeModel(root);
			this.tree.setModel(this.model);
			addChildren(root);
			tree.expandRow(0);
			
			this.offsets = this.cf.getOffsetMap();
			if (this.sync != null) {
				this.sync.setOffsets(this.offsets);
				splitSynchronize();
			}
		} else {
			this.removeAll();
			this.add(new JPanel());
		}
		this.upToDate = true;
    }

    private void addChildren(StructureNode parent) {
    	Object userObject = parent.getUserObject();
        if (userObject instanceof ClassFileNode) {
        	{
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new MagicNode(this.cf));
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
        	}
        	{
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new VersionNode(this.cf));
		        sn.addContextMenuItem(new AbstractAction("Change Version..") {
		        	public void actionPerformed(ActionEvent e) {
		        		ClassVersionDialog dlg = new ClassVersionDialog(MainWindow.getInstance());
		        		dlg.invoke(cf.getMajorVersion(), cf.getMinorVersion());
						if (!dlg.wasCancelled()) {
							SystemFacade.getInstance().performAction(new ModifyClassVersionAction(cf, dlg.getVersion()));
						}
		        	}
		        });
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
        	}
        	{
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new ConstantPoolNode(this.cf));
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
        	}
        	{
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new AccessFlagsNode(this.cf));
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
        	}
        	{
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new ThisClassNode(this.cf));
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
        	}
        	{
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new SuperClassNode(this.cf));
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
        	}
        	{
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new InterfacesNode(this.cf));
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
		        addChildren(sn);
        	}
        	{
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new FieldsNode(this.cf));
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
		        addChildren(sn);
        	}
        	{
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new MethodsNode(this.cf));
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
		        addChildren(sn);
        	}
        	{
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new AttributesNode(this.cf.getAttributes()));
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
		        addChildren(sn);
        	}
        	
        } else if (userObject instanceof InterfacesNode) {
            List list = this.cf.getInterfaces();
            for (int i = 0; i < list.size(); i++) {
                Interface interface0 = (Interface) list.get(i);
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new InterfaceNode(this.cf, interface0));
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
            }
        } else if (userObject instanceof FieldsNode) {
    		List list = this.cf.getFields();
    		for (int i = 0; i < list.size(); i++) {
    			Field field = (Field) list.get(i);
    			FieldNode fieldNode = new FieldNode(this.cf, field);
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(fieldNode);
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
		        addChildren(sn);
    		}
        } else if (userObject instanceof MethodsNode) {
    		List list = this.cf.getMethods();
    		for (int i = 0; i < list.size(); i++) {
    			Method method = (Method) list.get(i);
    			MethodNode methodNode = new MethodNode(this.cf, method);
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(methodNode);
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
		        addChildren(sn);
    		}
        } else if (userObject instanceof AttributesNode) {
        	AttributesNode attrsNode = (AttributesNode) userObject;
    		List list = attrsNode.getAttrs().getAttributes();
    		for (int i = 0; i < list.size(); i++) {
    			Attribute attr = (Attribute) list.get(i);
    			AttributeNode attrNode = new AttributeNode(attr);
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(attrNode);
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
		        addChildren(sn);
    		}
        } else if (userObject instanceof MethodNode) {
        	MethodNode mn = (MethodNode) userObject;
        	{
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new MethodAccessFlagsNode(mn.getMethod()));
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
		        addChildren(sn);
        	}
        	{
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new MethodNameNode(mn.getMethod()));
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
		        addChildren(sn);
        	}
        	{
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new MethodDescriptorNode(mn.getMethod()));
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
		        addChildren(sn);
        	}
        	{
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(new AttributesNode(mn.getMethod().getAttributes()));
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
		        addChildren(sn);
        	}
        } else if (userObject instanceof AttributeNode) {
        	final AttributeNode an = (AttributeNode) userObject;
    		if (an.getAttributeObject() instanceof CodeAttribute) {
    			CodeAttribute ca = (CodeAttribute) an.getAttributeObject();
    			Attributes attrs = ca.getAttributes();
    			AttributesNode attrsNode = new AttributesNode(attrs);
	        	StructureNode sn = new StructureNode();
		        sn.setUserObject(attrsNode);
		        this.model.insertNodeInto(sn, parent, parent.getChildCount());
		        addChildren(sn);
    		}

        	Action hexEditAction = new AbstractAction("Edit attribute data..") {
        		byte[] data;
        		public void actionPerformed(ActionEvent e) {
        			data = an.getAttributeObject().getPayload();
        			ByteArrayDataProvider badp = new ByteArrayDataProvider(data);
        			HexEditorDialog dlg = new HexEditorDialog(MainWindow.getInstance(), badp, 8, false);
        			boolean cancelled = dlg.invoke();
        			if (!cancelled) {
        				SystemFacade.getInstance().performAction(new ModifyAttributeDataAction(an.getAttributeObject(), data));
        			}
        		}
        	};
        	parent.addContextMenuItem(hexEditAction);
        } else if (userObject instanceof FieldNode) {
        	FieldNode fn = (FieldNode) userObject;
        	AttributesNode attrNode = new AttributesNode(fn.getField().getAttributes());
        	StructureNode sn = new StructureNode();
	        sn.setUserObject(attrNode);
	        this.model.insertNodeInto(sn, parent, parent.getChildCount());
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
    }

    public void goTo(Link link) {
    }

    public void find() {
    }

    public void findNext() {
    }

	public void processEvent(Event event) {
        try {
        	if (event.getType() == EventType.CLASS_OPEN || event.getType() == EventType.CLASS_REPARSE) {
        		this.cf = event.getClassFile();
        	}

        	if (event.getType() == EventType.CLASS_PARSE_ERROR) {
    			this.tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Class parse error.")));
        	}

        	if (event.getType() == EventType.CLASS_OPEN || event.getType() == EventType.CLASS_UPDATE || event.getType() == EventType.CLASS_REPARSE) {
        		this.upToDate = false;
        		if (this.isOpen) {
        			refresh();
        		}
            }

        	if (event.getType() == EventType.SERIALIZED_OPEN || event.getType() == EventType.RAW_OPEN) {
        		this.cf = null;
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
		return "Structure";
	}

	public void enteringTab() {
		this.isOpen = true;
		if (!this.upToDate) {
			refresh();
		}
		splitSynchronize();
	}

	public void setSplitSynchronizer(StructureSplitSynchronizer sync) {
		this.sync = sync;
		this.sync.setOffsets(this.offsets);
		splitSynchronize();
	}

	private void splitSynchronize() {
		if (this.sync != null && isOpen) {
			TreePath path = this.tree.getSelectionPath();
			if (path != null) {
				StructureNode node = (StructureNode) path.getLastPathComponent();
				this.sync.sync(node);
			}
		}
	}

}
