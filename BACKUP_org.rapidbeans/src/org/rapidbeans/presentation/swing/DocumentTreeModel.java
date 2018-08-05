/*
 * Rapid Beans Framework: DocumentTreeModel.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.datasource.Filter;

/**
 * model part of the Swing implementation for a tree view for a bean document.
 * 
 * @author Martin Bluemel
 */
public final class DocumentTreeModel implements TreeModel {

	/**
	 * the document to show.
	 */
	private Document document = null;

	/**
	 * if bean links shall be shown or not.
	 */
	private boolean showBeanLinks = true;

	/**
	 * @return if bean links are presented to the tree view.
	 */
	protected boolean getShowBeanLinks() {
		return showBeanLinks;
	}

	/**
	 * setter.
	 * 
	 * @param show
	 *            determines if bean links are presented to the tree view.
	 */
	protected void setShowBeanLinks(final boolean show) {
		this.showBeanLinks = show;
	}

	/**
	 * if properties shall be shown or not.
	 */
	private boolean showProperties = true;

	/**
	 * @return if properties are presented to the tree view.
	 */
	protected boolean getShowProperties() {
		return showProperties;
	}

	/**
	 * setter.
	 * 
	 * @param show
	 *            determines if properties are presented to the tree view.
	 */
	protected void setShowProperties(final boolean show) {
		this.showProperties = show;
	}

	private Filter beanFilter = null;

	/**
	 * constructor.
	 * 
	 * @param doc
	 *            the document to show
	 * @param filter
	 *            the filter
	 */
	public DocumentTreeModel(final Document doc, final Filter filter) {
		this.document = doc;
		this.beanFilter = filter;
	}

	/**
	 * Returns the root of the tree. Returns <code>null</code> only if the tree
	 * has no nodes.
	 * 
	 * @return the root of the tree
	 */
	public Object getRoot() {
		return this.document.getRoot();
	}

	/**
	 * Returns the child of <code>parent</code> at index <code>index</code> in
	 * the parent's child array. <code>parent</code> must be a node previously
	 * obtained from this data source. This should not return <code>null</code> if <code>index</code> is a valid index for <code>parent</code> (that is <code>index >= 0 && index < getChildCount(parent</code>)). The parent can
	 * be of the following classes.<br/>
	 * <b>bean: </b>the childs are all Collection Properties with role type
	 * "composition"<br/>
	 * <b>PropertyCollection: </b>the child are the beans of this collection<br/>
	 * 
	 * @param parent
	 *            a node in the tree, obtained from this data source
	 * 
	 * @param index
	 *            the index of the child tree node
	 * 
	 * @return the child of <code>parent</code> at index <code>index</code>
	 */
	@SuppressWarnings("unchecked")
	public Object getChild(final Object parent, final int index) {
		Object foundObj = null;
		if (parent instanceof RapidBean) {
			if (this.showProperties) {
				if (this.showBeanLinks) {
					final List<PropertyCollection> colList = ((RapidBean) parent).getColProperties();
					PropertyCollection colProp = null;
					if (this.beanFilter == null) {
						colProp = colList.get(index);
					} else {
						int i = 0;
						for (final PropertyCollection colProp1 : colList) {
							if (this.beanFilter.applies(((TypePropertyCollection) colProp1.getType()).getTargetType())) {
								if (i < index) {
									i++;
								} else {
									colProp = colProp1;
									break;
								}
							}
						}
					}
					if (((TypePropertyCollection) colProp.getType()).isComposition()) {
						foundObj = new DocumentTreeNodePropColComp(colProp);
					} else {
						foundObj = new DocumentTreeNodePropColLink(colProp);
					}
				} else { // showBeanLinks == false
					final List<PropertyCollection> compColList = ((RapidBean) parent).getColPropertiesComposition();
					if (this.beanFilter == null) {
						foundObj = new DocumentTreeNodePropColComp(compColList.get(index));
					} else {
						int i = 0;
						for (final PropertyCollection colProp : compColList) {
							if (this.beanFilter.applies(((TypePropertyCollection) colProp.getType()).getTargetType())) {
								if (i < index) {
									i++;
								} else {
									foundObj = colProp;
									break;
								}
							}
						}
					}
				}
			} else { // showProperties == false
				List<PropertyCollection> colList;
				if (this.showBeanLinks) {
					colList = ((RapidBean) parent).getColProperties();
				} else {
					colList = ((RapidBean) parent).getColPropertiesComposition();
				}
				int beansCount = 0;
				boolean isComposition;
				for (PropertyCollection colProp : colList) {
					if (this.showBeanLinks) {
						isComposition = ((TypePropertyCollection) colProp.getType()).isComposition();
					} else {
						isComposition = true;
					}
					for (RapidBean bean : (List<RapidBean>) colProp.getValue()) {
						if (beansCount == index) {
							if (isComposition) {
								foundObj = bean;
								break;
							} else {
								foundObj = new DocumentTreeNodeBeanLink(bean);
								break;
							}
						}
						if (this.beanFilter == null || this.beanFilter.applies(bean)) {
							beansCount++;
						}
					}
					if (foundObj != null) {
						break;
					}
				}
			}
		} else if (parent instanceof DocumentTreeNodePropCol) {
			// a composition collection returns directly beans
			final PropertyCollection parentColProp = ((DocumentTreeNodePropCol) parent).getColProp();
			if (this.beanFilter == null) {
				final RapidBean bean = (RapidBean) ((ReadonlyListCollection<Link>) parentColProp.getValue()).get(index);
				if (parent instanceof DocumentTreeNodePropColLink) {
					foundObj = new DocumentTreeNodeBeanLink(bean);
				} else if (parent instanceof DocumentTreeNodePropColComp) {
					foundObj = bean;
				} else {
					throw new RapidBeansRuntimeException("Unexpected parent class \"" + parent.getClass().getName()
							+ "\" for bean tree model");
				}
			} else {
				int i = 0;
				for (final Object obj : (Collection<Link>) parentColProp.getValue()) {
					final RapidBean bean = (RapidBean) obj;
					if (this.beanFilter.applies(bean)) {
						if (i < index) {
							i++;
						} else {
							foundObj = bean;
							break;
						}
					}
				}
			}
		} else {
			// bean link nodes or other objects never should be asked for childs
			throw new RapidBeansRuntimeException("Unexpected parent class \"" + parent.getClass().getName()
					+ "\" for bean tree model");
		}
		if (foundObj == null) {
			throw new RapidBeansRuntimeException("No child object found for parent " + parent + " at index " + index);
		}
		return foundObj;
	}

	/**
	 * returns the number of children of parent.
	 * 
	 * @param parent
	 *            the bean or PropertyCollection tree node. Must be a node
	 *            previously obtained from this data source.
	 * 
	 * @return 0 if the node is a leaf or if it has no children.<br/>
	 *         for beans: the number of Collection Properties with role type
	 *         "composition"<br/>
	 *         for Collection Properties: the number of collected beans<br/>
	 */
	@SuppressWarnings("unchecked")
	public int getChildCount(final Object parent) {
		int count = -1;
		if (parent instanceof RapidBean) {
			List<PropertyCollection> colPropList;
			if (this.showBeanLinks) {
				colPropList = ((RapidBean) parent).getColProperties();
			} else {
				colPropList = ((RapidBean) parent).getColPropertiesComposition();
			}
			if (this.showProperties) {
				if (this.beanFilter == null) {
					count = colPropList.size();
				} else {
					count = 0;
					for (final PropertyCollection colProp : colPropList) {
						if (beanFilter.applies(((TypePropertyCollection) colProp.getType()).getTargetType())) {
							count++;
						}
					}
				}
			} else { // show properties == false
				count = 0;
				for (PropertyCollection colProp : colPropList) {
					if (colProp.getValue() != null) {
						count += ((List<Link>) colProp.getValue()).size();
					}
				}
			}
		} else if (parent instanceof DocumentTreeNodeBeanLink) {
			count = 0;
		} else if (parent instanceof DocumentTreeNodePropCol) {
			final Collection<RapidBean> col = (Collection<RapidBean>) ((DocumentTreeNodePropCol) parent).getColProp()
					.getValue();
			if (col == null) {
				count = 0;
			} else {
				if (this.beanFilter == null) {
					count = col.size();
				} else {
					count = 0;
					for (final RapidBean linkedBean : col) {
						if (this.beanFilter.applies(linkedBean)) {
							count++;
						}
					}
				}
			}
		} else {
			throw new RapidBeansRuntimeException("Unexpected child class \"" + parent.getClass().getName()
					+ "\"for bean tree model");
		}
		return count;
	}

	/**
	 * @param node
	 *            the tree node
	 * 
	 * @return if the node is a leaf node
	 */
	public boolean isLeaf(final Object node) {
		return (this.getChildCount(node) == 0);
	}

	/**
	 * Returns the index of child in parent. If either <code>parent</code> or <code>child</code> is <code>null</code>, returns -1. If either <code>parent</code> or <code>child</code> don't belong to this tree
	 * model, returns -1.
	 * 
	 * @param parent
	 *            a node in the tree, obtained from this data source
	 * 
	 * @param child
	 *            the node we are interested in
	 * 
	 * @return the index of the child in the parent, or -1 if either <code>child</code> or <code>parent</code> are <code>null</code> or don't belong to this tree model
	 */
	@SuppressWarnings("unchecked")
	public int getIndexOfChild(final Object parent, final Object child) {
		if (parent == null || child == null) {
			throw new IllegalArgumentException("Unexpected null parent or child");
		}
		int index = 0;
		if (parent instanceof RapidBean) {
			final RapidBean parentBean = (RapidBean) parent;
			List<PropertyCollection> colPropList;
			if (this.showBeanLinks) {
				colPropList = parentBean.getColProperties();
			} else {
				colPropList = parentBean.getColPropertiesComposition();
			}
			if (this.showProperties) {
				final DocumentTreeNodePropCol childPropTreeNode = (DocumentTreeNodePropCol) child;
				int i = 0;
				for (PropertyCollection colProp : colPropList) {
					if (childPropTreeNode.getColProp() == colProp) {
						index = i;
						break;
					}
					i++;
				}
			} else { // show properties == false
				RapidBean childBean;
				if (child instanceof RapidBean) {
					childBean = (RapidBean) child;
				} else if (child instanceof DocumentTreeNodeBeanLink) {
					childBean = ((DocumentTreeNodeBeanLink) child).getLinkedBean();
				} else {
					throw new RapidBeansRuntimeException("Unexpected child class \"" + child.getClass().getName()
							+ "\" for bean tree model");
				}
				boolean found = false;
				for (PropertyCollection colProp : colPropList) {
					final Collection<RapidBean> colBeans = (Collection<RapidBean>) colProp.getValue();
					for (RapidBean bean : colBeans) {
						if (bean == childBean) {
							found = true;
							break;
						}
					}
					if (found) {
						break;
					}
				}
			}
		} else if (parent instanceof DocumentTreeNodePropCol) {
			final DocumentTreeNodePropCol parentCollection = (DocumentTreeNodePropCol) parent;
			RapidBean childBean;
			if (child instanceof RapidBean) {
				childBean = (RapidBean) child;
			} else if (child instanceof DocumentTreeNodeBeanLink) {
				childBean = ((DocumentTreeNodeBeanLink) child).getLinkedBean();
			} else {
				throw new RapidBeansRuntimeException("Unexpected child class \"" + child.getClass().getName()
						+ "\" for bean tree model");
			}
			index = ((ReadonlyListCollection<Link>) parentCollection.getColProp().getValue()).indexOf(childBean);
		} else {
			throw new RapidBeansRuntimeException("Unexpected parent class \"" + parent.getClass().getName()
					+ "\" for bean tree model");
		}
		return index;
	}

	/**
	 * the TreeModelListeners.
	 */
	private Collection<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	/**
	 * add a TreeModelListener.
	 * 
	 * @param l
	 *            the TreeModelListener to add
	 */
	public void addTreeModelListener(final TreeModelListener l) {
		this.listeners.add(l);
	}

	/**
	 * remove a TreeModelListener.
	 * 
	 * @param l
	 *            the TreeModelListener to remove
	 */
	public void removeTreeModelListener(final TreeModelListener l) {
		this.listeners.remove(l);
	}

	/**
	 * @param path
	 *            the tree path
	 * @param newValue
	 *            the new value
	 */
	public void valueForPathChanged(final TreePath path, final Object newValue) {
		TreeModelEvent e = new TreeModelEvent(this, path, new int[] { this.getIndexOfChild(path.getParentPath()
				.getLastPathComponent(), newValue) }, new Object[] { newValue });
		for (TreeModelListener listener : this.listeners) {
			listener.treeNodesChanged(e);
		}
	}

	public DocumentTreeNodePropCol findColPropNode(final Property property) {
		final RapidBean bean = property.getBean();
		final int len = this.getChildCount(bean);
		for (int i = 0; i < len; i++) {
			final DocumentTreeNodePropCol currentColPropNode = (DocumentTreeNodePropCol) this.getChild(bean, i);
			if (currentColPropNode.getColProp() == property) {
				return currentColPropNode;
			}
		}
		return null;
	}

	/**
	 * Example Array for toArray.
	 */
	static final Object[] OA = new Object[0];

	/**
	 * retrieve an Object Array containing the path of all parent beans and
	 * Properties.
	 * 
	 * @param argObject
	 *            the object: ether a bean or a BBPropCollctionTreeNode
	 * @param includeObjectSelf
	 *            if the object given should also be included in the path
	 * 
	 * @return the Object array which is empty if there isn't any parent
	 */
	protected Object[] getParentObjects(final Object argObject, final boolean includeObjectSelf) {
		ArrayList<Object> al = new ArrayList<Object>();
		if (includeObjectSelf) {
			al.add(argObject);
		}
		Object o = argObject;
		while (o != null) {
			if (o instanceof RapidBean) {
				if (this.showProperties) {
					PropertyCollection parentProp = ((RapidBean) o).getParentProperty();
					if (parentProp == null) {
						o = null;
					} else {
						o = new DocumentTreeNodePropColComp(parentProp);
					}
				} else { // showProperties == false
					o = ((RapidBean) o).getParentBean();
				}
			} else if (o instanceof DocumentTreeNodeBeanLink) {
				if (o instanceof RapidBean) {
					PropertyCollection parentProp = ((DocumentTreeNodeBeanLink) o).getLinkedBean().getParentProperty();
					if (parentProp == null) {
						o = null;
					} else {
						o = new DocumentTreeNodePropColLink(parentProp);
					}
				} else { // showProperties == false
					o = ((DocumentTreeNodeBeanLink) o).getLinkedBean().getParentBean();
				}
			} else if (o instanceof DocumentTreeNodePropCol) {
				if (((DocumentTreeNodePropCol) o).getColProp() != null) {
					o = ((DocumentTreeNodePropCol) o).getColProp().getBean();
				} else {
					o = null;
				}
			} else {
				throw new RapidBeansRuntimeException("Unexpected parent class \"" + o.getClass().getName()
						+ "\" for bean tree model");
			}
			if (o != null) {
				al.add(o);
			}
		}

		// stick the objects found into an array in reverse order.
		final int alSize = al.size();
		Object[] oa = new Object[alSize];
		int j = 0;
		for (int i = alSize - 1; i >= 0; i--) {
			oa[j++] = al.get(i);
		}
		return oa;
	}

	/**
	 * Fires a change event for a certain node (bean or Property) that indicates
	 * that the tree structure has changed below this root.
	 * 
	 * @param root
	 *            the root bean or property with changes below
	 * @param tree
	 *            the tree widget
	 */
	protected void fireTreeStructureChanged(final Object root, final JTree tree) {

		if (!(root instanceof RapidBean || root instanceof DocumentTreeNodePropCol)) {
			throw new RapidBeansRuntimeException("unexpected class of given root object: " + root.getClass().getName());
		}

		final MyTreeNode treeNode = findPresentedTreeNodes(tree, this.getRoot());

		TreeModelEvent e = new TreeModelEvent(this, new Object[] { root });
		for (TreeModelListener listener : this.listeners) {
			listener.treeStructureChanged(e);
		}

		if (treeNode != null) {
			expand(tree, treeNode);
		}
	}

	@SuppressWarnings("unchecked")
	private MyTreeNode findPresentedTreeNodes(final JTree tree, final Object presentedObject) {
		MyTreeNode node = null;
		final TreePath path = this.findTreePath(tree, presentedObject);
		if (path != null) {
			node = new MyTreeNode(path);
			if (presentedObject instanceof RapidBean) {
				for (final Property prop : (((RapidBean) presentedObject)).getPropertyList()) {
					if (prop instanceof PropertyCollection) {
						final MyTreeNode subnode = findPresentedTreeNodes(tree, new DocumentTreeNodePropColComp(
								((PropertyCollection) prop)));
						if (subnode != null) {
							node.addPresentedChildNode(subnode);
						}
					}
				}
			} else if (presentedObject instanceof DocumentTreeNodePropCol) {
				PropertyCollection prop = ((DocumentTreeNodePropCol) presentedObject).getColProp();
				if (prop.getValue() != null) {
					if (((TypePropertyCollection) prop.getType()).isComposition()) {
						for (final Link link : (Collection<Link>) prop.getValue()) {
							if (link instanceof RapidBean) {
								final MyTreeNode subnode = findPresentedTreeNodes(tree, link);
								if (subnode != null) {
									node.addPresentedChildNode(subnode);
								}
							}
						}
					} else {
						for (final Link link : (Collection<Link>) prop.getValue()) {
							if (link instanceof RapidBean) {
								final TreePath path1 = this.findTreePath(tree, presentedObject);
								if (path1 != null) {
									node.addPresentedChildNode(new MyTreeNode(path1));
								}
							}
						}
					}
				}
			} else {
				throw new RuntimeException("unexpected instance");
			}
		}
		return node;
	}

	private void expand(final JTree tree, final MyTreeNode node) {
		if (node.isExpanded()) {
			tree.expandPath(node.getTreePath());
			for (final MyTreeNode subnode : node.getPresentedChildNode()) {
				expand(tree, subnode);
			}
		}
	}

	private TreePath findTreePath(final JTree tree, final Object presentedObject) {
		TreePath treePath = null;
		int row = 0;
		while (true) {
			treePath = tree.getPathForRow(row++);
			if (treePath == null) {
				break;
			} else if (presentedObject instanceof RapidBean) {
				if (treePath.getLastPathComponent() == presentedObject) {
					break;
				}
			} else if (presentedObject instanceof DocumentTreeNodePropCol) {
				if (treePath.getLastPathComponent() instanceof DocumentTreeNodePropCol
						&& ((DocumentTreeNodePropCol) presentedObject).getColProp() == ((DocumentTreeNodePropCol) treePath
								.getLastPathComponent()).getColProp()) {
					break;
				}
			} else {
				throw new RuntimeException("unexpected instance");
			}
		}
		return treePath;
	}

	private class MyTreeNode {
		private ArrayList<MyTreeNode> presentedChildNodes = new ArrayList<MyTreeNode>();

		private TreePath treePath = null;

		public MyTreeNode(TreePath path) {
			this.treePath = path;
		}

		public void addPresentedChildNode(final MyTreeNode node) {
			this.presentedChildNodes.add(node);
		}

		public ArrayList<MyTreeNode> getPresentedChildNode() {
			return this.presentedChildNodes;
		}

		public TreePath getTreePath() {
			return treePath;
		}

		public boolean isExpanded() {
			return (this.presentedChildNodes.size() > 0);
		}
	}

	/**
	 * Fires a change event for created beans.
	 * 
	 * @param path
	 *            the parent path for these changes
	 * @param childIndices
	 *            the child indices
	 * @param beans
	 *            the child objects
	 */
	protected void fireBeansInserted(final TreePath path, final int[] childIndices, final RapidBean[] beans) {
		TreeModelEvent e = new TreeModelEvent(this, path, childIndices, beans);
		for (TreeModelListener listener : this.listeners) {
			listener.treeNodesInserted(e);
		}
	}

	/**
	 * Fires a change event for deleted beans.
	 * 
	 * @param path
	 *            the parent path for these changes
	 * @param childIndices
	 *            the child indices
	 * @param beans
	 *            the child objects
	 */
	protected void fireBeansDeleted(final TreePath path, final int[] childIndices, final RapidBean[] beans) {
		TreeModelEvent e = new TreeModelEvent(this, path, childIndices, beans);
		for (TreeModelListener listener : this.listeners) {
			listener.treeNodesRemoved(e);
		}
	}

	/**
	 * Fires a change event for modified beans.
	 * 
	 * @param path
	 *            the parent path for these changes
	 */
	protected void fireBeanChanged(final TreePath path) {
		TreeModelEvent e = new TreeModelEvent(this, path);
		for (TreeModelListener listener : this.listeners) {
			listener.treeNodesChanged(e);
		}
	}
}
