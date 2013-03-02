/*
 * Rapid Beans Framework: DocumentTreeCellRenderer.java
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

import java.awt.Component;
import java.util.List;
import java.util.MissingResourceException;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;

/**
 * cell renderer for the Swing implementation for a tree view for a bean
 * document.
 * 
 * @author Martin Bluemel
 */
public final class DocumentTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the bean document presented as tree.
	 */
	private Document document = null;

	/**
	 * the locale.
	 */
	private RapidBeansLocale locale;

	/**
	 * constructor.
	 * 
	 * @param doc
	 *            the document
	 * @param loc
	 *            the locale
	 */
	public DocumentTreeCellRenderer(final Document doc,
			final RapidBeansLocale loc) {
		this.document = doc;
		this.locale = loc;
		if (this.locale == null) {
			throw new RapidBeansRuntimeException("no locale specified");
		}
	}

	/**
	 * Sets the value of the current tree cell to value. If selected is true,
	 * the cell will be drawn as if selected. If expanded is true the node is
	 * currently expanded and if leaf is true the node represets a leaf and if
	 * hasFocus is true the node currently has focus. tree is the JTree the
	 * receiver is being configured for. Returns the Component that the renderer
	 * uses to draw the value.
	 * 
	 * @param tree
	 *            the tree
	 * @param value
	 *            the bean or Collection Property to render
	 * @param isSelected
	 *            if the tree node is selected
	 * @param expanded
	 *            if the tree node is expanded
	 * @param leaf
	 *            if the tree node is a leaf
	 * @param row
	 *            the row
	 * @param hasGotFocus
	 *            if the tree node has the focus
	 * 
	 * @return the tree cell renderer component
	 */
	public Component getTreeCellRendererComponent(final JTree tree,
			final Object value, final boolean isSelected,
			final boolean expanded, final boolean leaf, final int row,
			final boolean hasGotFocus) {
		String text = null;
		ImageIcon icon = null;
		DocumentTreeCellRenderer comp = (DocumentTreeCellRenderer) super
				.getTreeCellRendererComponent(tree, value, isSelected,
						expanded, leaf, row, hasGotFocus);
		if (value instanceof RapidBean) {
			text = findText((RapidBean) value);
			icon = findIcon((RapidBean) value, false);
		} else if (value instanceof DocumentTreeNodeBeanLink) {
			text = findText(((DocumentTreeNodeBeanLink) value).getLinkedBean());
			icon = findIcon(((DocumentTreeNodeBeanLink) value).getLinkedBean(),
					true);
		} else if (value instanceof DocumentTreeNodePropCol) {
			text = findText((DocumentTreeNodePropCol) value);
			icon = findIcon((DocumentTreeNodePropCol) value);
		}
		comp.setText(text);
		comp.setIcon(icon);
		return comp;
	}

	/**
	 * Finds a GUI text for a bean in a tree view.
	 * 
	 * @param bean
	 *            the bean for which we need a text
	 * 
	 * @return the found text or null if none found
	 */
	private String findText(final RapidBean bean) {
		String text = null;

		// 1) take resource definition: treeview.beanlabel.<path>
		// e. g.
		// treeview.beanlabel.masterdata.clubs.trainingdates.trainerplannings
		try {
			if (bean.getContainer() != null
					&& bean.getContainer() instanceof Document) {
				final String path = ((Document) bean.getContainer()).getPath(
						bean, '.');
				final String pattern = "treeview.beanlabel." + path;
				final String s = this.locale.getStringGui(pattern);
				text = bean.expandPropertyValues(s, this.locale);
			}
		} catch (MissingResourceException e) {
			text = null;
		}

		// 1b) take resource definition:
		// treeview.beanlabel.<documentprefix>*.<path>
		// e. g.
		// treeview.beanlabel.masterdata.clubs.trainingdates.trainerplannings
		if (text == null) {
			try {
				if (bean.getContainer() != null
						&& bean.getContainer() instanceof Document) {
					final List<String> sa = StringHelper.split(
							"treeview.beanlabel."
									+ ((Document) bean.getContainer()).getPath(
											bean, '.'), ".");
					if (sa.size() > 3) {
						final String docname = sa.get(2);
						final int docnamepos = docname.indexOf('_');
						if (docnamepos != -1) {
							StringBuffer sb = new StringBuffer();
							for (int i = 0; i < sa.size(); i++) {
								if (i > 0) {
									sb.append('.');
								}
								if (i == 2) {
									sb.append(docname.substring(0,
											docnamepos + 1));
									sb.append('*');
								} else {
									sb.append(sa.get(i));
								}
							}
							final String pattern = sb.toString();
							text = bean.expandPropertyValues(
									this.locale.getStringGui(pattern),
									this.locale);
						}
					}
				}
			} catch (MissingResourceException e) {
				text = null;
			}
		}

		// 2) default: take the internationalized stringified identity
		if (text == null) {
			text = bean.toStringGuiId(this.locale);
		}
		return text;
	}

	/**
	 * Finds a GUI text for a collection property node in a tree view.
	 * 
	 * @param colNode
	 *            the bean for which we need a text
	 * 
	 * @return the found text or null if none found
	 */
	private String findText(final DocumentTreeNodePropCol colNode) {
		String text = null;

		// daisy chain of tries to find an appropriate text for
		// compositions collection (or container) properties

		// try tree view resource string specifically defined for this kind of
		// document
		// "document.<document config name>.treeview.<property name>.label"
		try {
			text = this.locale.getStringGui("document."
					+ this.document.getConfigNameOrName() + ".treeview."
					+ colNode.getColProp().getType().getPropName() + ".label");
		} catch (MissingResourceException e) {
			text = null;
		}

		// try resource string defined for this property
		// "bean.<lowercased typename>.prop.<property name>.label"
		// defined for the bean type or one of it's parent types
		if (text == null) {
			TypeRapidBean type = colNode.getColProp().getBean().getType();
			while (text == null && type != null) {
				try {
					final String key = "bean." + type.getName().toLowerCase()
							+ ".prop."
							+ colNode.getColProp().getType().getPropName();
					text = this.locale.getStringGui(key);
				} catch (MissingResourceException e) {
					text = null;
					type = type.getSupertype();
				}
			}
		}

		// in case of an association property
		// try resource string defined for the
		// target type or one of it's parent types
		// "bean.<lowercased target typename>(.plural)" or
		// "bean.<lowercased target typename>" in case of maxmult == 1
		if (text == null
				&& colNode.getColProp().getType() instanceof TypePropertyCollection) {
			final TypePropertyCollection colPropType = (TypePropertyCollection) colNode
					.getColProp().getType();
			TypeRapidBean type = colPropType.getTargetType();
			while (text == null && type != null) {
				try {
					text = type.toStringGui(this.locale,
							(colPropType.getMaxmult() != 1),
							colPropType.getPropName());
					String key = "bean." + type.getName().toLowerCase();
					if (colPropType.getMaxmult() != 1) {
						key += ".plural";
					}
					text = locale.getStringGui(key);
				} catch (MissingResourceException e) {
					if (colPropType.getMaxmult() != 1) {
						try {
							final String key1 = "bean."
									+ type.getName().toLowerCase();
							text = locale.getStringGui(key1);
							text += "s";
						} catch (MissingResourceException e1) {
							text = null;
							type = type.getSupertype();
						}
					} else {
						text = null;
						type = type.getSupertype();
					}
				}
			}
		}

		// fallback 2: property name (not localized)
		if (text == null) {
			text = colNode.getColProp().getType().getPropName();
		}
		return text;
	}

	/**
	 * Finds an Icon for a bean in a tree view.
	 * 
	 * @param bean
	 *            the bean for which we need a text
	 * @param link
	 *            determines if it is a bean or a link to a bean.
	 * 
	 * @return the found text or null if none found
	 */
	private static ImageIcon findIcon(final RapidBean bean, final boolean link) {

		ImageIcon icon = null;

		if (ApplicationManager.getApplication() != null
				&& ApplicationManager.getApplication().getMainwindow() != null
				&& ((MainWindowSwing) ApplicationManager.getApplication()
						.getMainwindow()).getIconManager() != null) {
			icon = ((MainWindowSwing) ApplicationManager.getApplication()
					.getMainwindow()).getIconManager().getIcon(bean.getType());
		}

		if (icon == null) {
			if (bean.getContainer() != null
					&& bean == ((Document) bean.getContainer()).getRoot()) {
				icon = getIconBeanRoot();
			} else if (link) {
				icon = getIconBeanLink();
			}
		}

		if (icon == null) {
			icon = getIconBean();
		}

		return icon;
	}

	/**
	 * Finds an Icon for a bean Collection Property in a tree view.
	 * 
	 * @param colNode
	 *            the collection property node for which we query an icon
	 * 
	 * @return the found icon or null if none found
	 */
	private ImageIcon findIcon(final DocumentTreeNodePropCol colNode) {
		ImageIcon icon = null;
		if (((TypePropertyCollection) colNode.getColProp().getType())
				.isComposition()) {
			icon = getIconColPropComposition();
		} else {
			icon = getIconColPropLink();
		}
		return icon;
	}

	private static ImageIcon iconBeanRoot = null;

	private static ImageIcon getIconBeanRoot() {
		if (iconBeanRoot == null) {
			iconBeanRoot = new ImageIcon(
					Application.class.getResource("pictures/root.gif"));
		}
		return iconBeanRoot;
	}

	private static ImageIcon iconBeanLink = null;

	private static ImageIcon getIconBeanLink() {
		if (iconBeanLink == null) {
			iconBeanLink = new ImageIcon(
					Application.class.getResource("pictures/beanlink.gif"));
		}
		return iconBeanLink;
	}

	private static ImageIcon iconBean = null;

	private static ImageIcon getIconBean() {
		if (iconBean == null) {
			iconBean = new ImageIcon(
					Application.class.getResource("pictures/bean.gif"));
		}
		return iconBean;
	}

	private static ImageIcon iconColPropComposition = null;

	private static ImageIcon getIconColPropComposition() {
		if (iconColPropComposition == null) {
			iconColPropComposition = new ImageIcon(
					Application.class.getResource("pictures/property.gif"));
		}
		return iconColPropComposition;
	}

	private static ImageIcon iconColPropLink = null;

	private static ImageIcon getIconColPropLink() {
		if (iconColPropLink == null) {
			iconColPropLink = new ImageIcon(
					Application.class.getResource("pictures/propertyLink.gif"));
		}
		return iconColPropLink;
	}
}
