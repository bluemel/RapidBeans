/*
 * Rapid Beans Framework: DocumentTreeViewSwing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/11/2006
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copies of the GNU Lesser General Public License and the
 * GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

package org.rapidbeans.presentation.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.event.PropertyChangeEvent;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.datasource.Filter;
import org.rapidbeans.datasource.event.AddedEvent;
import org.rapidbeans.datasource.event.ChangedEvent;
import org.rapidbeans.datasource.event.RemovedEvent;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.DocumentTreeView;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.service.ActionDocumentSave;

/**
 * Swing implementation for a tree view for a bean document.
 * 
 * @author Martin Bluemel
 */
public final class DocumentTreeViewSwing extends DocumentTreeView {

	/**
	 * the tree view's scroll pane.
	 */
	private JScrollPane scrollPane = null;

	/**
	 * the Swing tree model.
	 */
	private DocumentTreeModel treeModel = null;

	/**
	 * the Swing tree view.
	 */
	private JTree tree = null;

	/**
	 * @return the Swing tree.
	 */
	public JTree getTree() {
		return this.tree;
	}

	/**
	 * the editor's popup menu.
	 */
	private JPopupMenu popupMenu = new JPopupMenu();

	/**
	 * the editor's popup delete menu.
	 */
	private JMenuItem popupMenuItemNew = new JMenuItem();

	/**
	 * the editor's popup delete menu.
	 */
	private JMenuItem popupMenuItemEdit = new JMenuItem();

	/**
	 * the editor's popup delete menu.
	 */
	private JMenuItem popupMenuItemDelete = new JMenuItem();

	/**
	 * @return the JTreeView.
	 */
	public Object getWidget() {
		return this.scrollPane;
	}

	/**
	 * change the tree view selection.
	 * 
	 * @param treePathObject
	 *            the tree path identifying the tree object to select
	 */
	protected void setSelectedBean(final Object treePathObject) {
		suppressSelectionChangeHandling(true);
		TreePath treePath = (TreePath) treePathObject;
		try {
			if (treePath == null) {
				this.tree.clearSelection();
			} else {
				this.tree.setSelectionPath(treePath);
				this.tree.expandPath(treePath);
			}
		} finally {
			suppressSelectionChangeHandling(false);
		}
	}

	/**
	 * Change the show bean links behaviour.
	 */
	protected void changeShowBeanLinks() {
		this.treeModel.setShowBeanLinks(this.getShowBeanLinks());
		this.treeModel.fireTreeStructureChanged(this.treeModel.getRoot(), this.tree);
	}

	/**
	 * Change the show properties behaviour.
	 */
	protected void changeShowProperties() {
		this.treeModel.setShowProperties(this.getShowProperties());
		this.treeModel.fireTreeStructureChanged(this.treeModel.getRoot(), this.tree);
	}

	/**
	 * constructor.
	 * 
	 * @param client
	 *            the client
	 * @param doc
	 *            the document to show
	 * @param filter
	 *            the filter
	 */
	public DocumentTreeViewSwing(final Application client, final Document doc,
			final Filter filter) {
		super(client, doc, filter);
		this.treeModel = new DocumentTreeModel(doc, filter);
		this.treeModel.setShowBeanLinks(this.getShowBeanLinks());
		this.tree = new JTree(this.treeModel);
		this.tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		this.scrollPane = new JScrollPane(this.tree);
		final RapidBeansLocale loc = client.getCurrentLocale();
		this.tree.setCellRenderer(new DocumentTreeCellRenderer(doc, loc));
		this.tree.addMouseListener(new MouseListener() {

			/**
			 * event handler for mouse clicks.
			 */
			@SuppressWarnings({ "synthetic-access", "unqualified-field-access" })
			public void mouseClicked(final MouseEvent e) {
				switch (e.getButton()) {
				case MouseEvent.BUTTON1:
					mouseClickedLeft(e);
					break;
				default:
					break;
				}
			}

			public void mouseEntered(final MouseEvent e) {
			}

			public void mouseExited(final MouseEvent e) {
			}

			public void mousePressed(final MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					mousePressedRight(e);
				}
			}

			public void mouseReleased(final MouseEvent e) {
			}
		});
		this.tree.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_DELETE:
					deleteBeans();
					break;
				case KeyEvent.VK_S:
					if (e.getModifiers() == 2) {
						(new ActionDocumentSave()).execute();
					}
					break;
				default:
					break;
				}
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		this.popupMenuItemNew.setText(client.getCurrentLocale().getStringGui(
				"commongui.text.new"));
		this.popupMenuItemNew.isFocusable();
		this.popupMenuItemEdit.setText(client.getCurrentLocale().getStringGui(
				"commongui.text.edit"));
		this.popupMenuItemDelete.setText(client.getCurrentLocale().getStringGui(
				"commongui.text.delete"));
		this.popupMenuItemNew.addActionListener(new ActionListener() {
			@SuppressWarnings({ "synthetic-access", "unqualified-field-access" })
			public void actionPerformed(final ActionEvent e) {
				popupMenu.setVisible(false);
				createBean();
			}
		});
		this.popupMenuItemEdit.addActionListener(new ActionListener() {
			@SuppressWarnings({ "synthetic-access", "unqualified-field-access" })
			public void actionPerformed(final ActionEvent e) {
				popupMenu.setVisible(false);
				editBeans();
			}
		});
		this.popupMenuItemDelete.addActionListener(new ActionListener() {
			@SuppressWarnings({ "synthetic-access", "unqualified-field-access" })
			public void actionPerformed(final ActionEvent e) {
				popupMenu.setVisible(false);
				deleteBeans();
			}
		});
		this.popupMenu.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuCanceled(final PopupMenuEvent e) {
			}

			public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
			}

			public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
				updatePopupMenu();
			}
		});
		this.popupMenu.add(this.popupMenuItemNew);
		this.popupMenu.add(this.popupMenuItemEdit);
		this.popupMenu.add(this.popupMenuItemDelete);
		//this.popupMenu.isFocusable();
		// the component popup menu eats up the right click mouse event
		this.tree.setComponentPopupMenu(this.popupMenu);
	}

	/**
	 * Delete a set of selected beans.
	 */
	//Is protected to enable Unit Tests.
	protected void deleteBeans() {

		// retrieve the paths selected in the tree
		final TreePath[] paths = this.tree.getSelectionPaths();
		final Object[] selObjs = new Object[paths.length];
		for (int i = 0; i < paths.length; i++) {
			selObjs[i] = paths[i].getLastPathComponent();
		}

		suppressSelectionChangeHandling(true);
		try {
			super.deleteBeans(selObjs);
		} finally {
			suppressSelectionChangeHandling(false);
		}
	}

	/**
	 * initiates creating of a new bean.
	 * 
	 * @return the bean editor used for creating the new bean
	 */
	public EditorBean createBean() {
		EditorBean editor = null;
		if (this.tree != null && this.tree.getSelectionPaths() != null
				&& this.tree.getSelectionPaths().length > 0) {
			final TreePath path = this.tree.getSelectionPaths()[0];
			final Object selObj = path.getLastPathComponent();
			if (selObj instanceof DocumentTreeNodePropColComp) {
				DocumentTreeNodePropColComp treenode = (DocumentTreeNodePropColComp) selObj;
				editor = createBean(path, treenode.getColProp());
			} else if (selObj instanceof RapidBean) {
				RapidBean selBean = (RapidBean) selObj;
				if (selBean.getParentBean() != null) {
					editor = createBean(path, selBean.getParentProperty());
				}
			}
		}
		return editor;
	}

	/**
	 * notifies all registered listeners that a bean has bee selected.
	 * 
	 * @return the bean editor of the last bean edited
	 */
	public EditorBean editBeans() {
		return super.editBeans(this.tree.getSelectionPaths(), getSelectedBeans());
	}

	/**
	 * determines all selected beans.
	 */
	public RapidBean[] getSelectedBeans() {
		final TreePath[] paths = this.tree.getSelectionPaths();
		final RapidBean[] selBeans = new RapidBean[paths.length];
		for (int i = 0; i < paths.length; i++) {
			selBeans[i] = (RapidBean) paths[i].getLastPathComponent();
		}
		return selBeans;
	}

	/**
	 * the left mouse button has been clicked.
	 * 
	 * @param e
	 *            the mouse event.
	 */
	private void mouseClickedLeft(final MouseEvent e) {
		//this.popupMenu.setVisible(false);
		if (e.getClickCount() == 2) {
			final TreePath path = this.tree.getPathForLocation(e.getX(), e.getY());
			if (path != null) {
				final Object[] keys = new Object[1];
				keys[0] = path;
				final Object[] selObjs = new Object[1];
				selObjs[0] = path.getLastPathComponent();
				editBeans(keys, selObjs);
			}
		}
	}

	/**
	 * retrieve the original object for the link object.
	 * 
	 * @param link
	 *            the link object
	 * 
	 * @return the original object
	 */
	protected TreePath getOriginalForLink(final Object link) {
		TreePath linkPath = (TreePath) link;
		RapidBean linkedBean = ((DocumentTreeNodeBeanLink)
				linkPath.getLastPathComponent()).getLinkedBean();
		TreePath path = new TreePath(this.treeModel.getParentObjects(linkedBean, true));
		this.tree.expandPath(path);
		this.tree.setSelectionPath(path);
		TreePath pathFromTree = this.tree.getSelectionPath();
		return pathFromTree;
	}

	/**
	 * Retrieve an object representing the bean in the TreeView.
	 * 
	 * @param bean
	 *            the bean
	 * 
	 * @return the path
	 */
	protected Object getTreeKey(final RapidBean bean) {
		return new TreePath(this.treeModel.getParentObjects(bean, true));
	}

	/**
	 * the right mouse button has been clicked.
	 * 
	 * @param e
	 *            the mouse event.
	 */
	private void mousePressedRight(final MouseEvent e) {
		// select the node right "clicked" if the pointer's
		// location is not already on a selected node
		final TreePath[] selPaths = tree.getSelectionPaths();
		final TreePath rightClickedPath = tree.getPathForLocation(e.getX(), e.getY());
		boolean pointerIsOnSelected = false;
		if (selPaths != null && selPaths.length > 0) {
			for (int i = 0; i < selPaths.length; i++) {
				if (selPaths[i] == rightClickedPath) {
					pointerIsOnSelected = true;
					break;
				}
			}
		}
		if (!pointerIsOnSelected) {
			suppressSelectionChangeHandling(true);
			try {
				tree.setSelectionPath(rightClickedPath);
			} finally {
				suppressSelectionChangeHandling(false);
			}
		}
	}

	/**
	 * update the texts and the enabeling of the popup menu's entries.
	 */
	private void updatePopupMenu() {
		final TreePath[] selPaths = this.tree.getSelectionPaths();
		if (selPaths == null) {
			this.popupMenuItemNew.setEnabled(false);
			this.popupMenuItemEdit.setEnabled(false);
			this.popupMenuItemDelete.setEnabled(false);
			return;
		}
		switch (selPaths.length) {
		case 0:
			this.popupMenuItemNew.setEnabled(false);
			this.popupMenuItemEdit.setEnabled(false);
			this.popupMenuItemDelete.setEnabled(false);
			break;
		case 1:
			if (selPaths[0].getLastPathComponent() instanceof RapidBean) {
				final RapidBean selBean = (RapidBean) selPaths[0].getLastPathComponent();
				if (selBean.getParentBean() == null) {
					this.popupMenuItemNew.setEnabled(false);
					this.popupMenuItemDelete.setEnabled(false);
				} else {
					this.popupMenuItemNew.setEnabled(true);
					this.popupMenuItemDelete.setEnabled(true);
				}
				this.popupMenuItemEdit.setEnabled(true);
			} else {
				this.popupMenuItemNew.setEnabled(true);
				this.popupMenuItemEdit.setEnabled(false);
				this.popupMenuItemDelete.setEnabled(false);
			}
			break;
		default:
			this.popupMenuItemNew.setEnabled(false);
			boolean atLeastOneBeanSelected = false;
			for (int i = 0; i < selPaths.length; i++) {
				if (selPaths[i].getLastPathComponent() instanceof RapidBean) {
					atLeastOneBeanSelected = true;
					break;
				}
			}
			if (atLeastOneBeanSelected) {
				this.popupMenuItemEdit.setEnabled(true);
				this.popupMenuItemDelete.setEnabled(true);
			} else {
				this.popupMenuItemEdit.setEnabled(false);
				this.popupMenuItemDelete.setEnabled(false);
			}
			break;
		}
	}

	/**
	 * event handler for bean added event.
	 * 
	 * @param e
	 *            the added event
	 */
	public void beanAddPre(final AddedEvent e) {
	}

	/**
	 * event handler for bean added event.
	 * 
	 * @param e
	 *            the added event
	 */
	public void beanAdded(final AddedEvent e) {
	}

	/**
	 * event handler for bean changed event.
	 * 
	 * @param e
	 *            the changed event
	 */
	public void beanChangePre(final ChangedEvent e) {
	}

	/**
	 * event handler for bean changed event.
	 * 
	 * @param e
	 *            the changed event
	 */
	public void beanChanged(final ChangedEvent e) {
		if (!(this.getDocument() == e.getBean().getContainer())) {
			return;
		}
		if (e.getPropertyEvents().length == 0) {
			throw new RapidBeansRuntimeException("unexpectedly got no property changed");
		}
		for (final PropertyChangeEvent pe : e.getPropertyEvents()) {
			if (pe.getProperty() instanceof PropertyCollection) {
				switch (pe.getType()) {
				case addlink:
				case removelink:
				case set:
					final Property prop = pe.getProperty();
					final DocumentTreeNodePropCol colPropNode = treeModel.findColPropNode(prop);
					if (colPropNode != null) {
						this.treeModel.fireTreeStructureChanged(colPropNode, this.tree);
					}
					break;
				}
			}
		}
		final TreePath beanPath = new TreePath(this.treeModel.getParentObjects(e.getBean(), true));
		this.treeModel.fireBeanChanged(beanPath);
	}

	/**
	 * handler for the pre remove event.
	 * 
	 * @param e
	 *            the removed event
	 */
	public void beanRemovePre(final RemovedEvent e) {
	}

	/**
	 * event handler for bean removed event.
	 * 
	 * @param e
	 *            the removed event
	 */
	public void beanRemoved(final RemovedEvent e) {
	}
}
