/*
 * Rapid Beans Framework: EditorPropertyComboboxSwing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/13/2006
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

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JComboBox;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyChoice;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyChoice;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.datasource.event.AddedEvent;
import org.rapidbeans.datasource.event.ChangedEvent;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.config.ConfigPropEditorBean;

/**
 * the bean editor GUI.
 * 
 * @author Martin Bluemel
 */
public class EditorPropertyComboboxSwing extends EditorPropertySwing {

	/**
	 * the combo box.
	 */
	private ComboBoxWrapper comboBox = new ComboBoxWrapper();

	/**
	 * @return the editor's widget
	 */
	public Object getWidget() {
		return comboBox.get();
	}

	/**
	 * constructor.
	 * 
	 * @param prop          the bean property to edit
	 * @param propBak       the bean property backup
	 * @param bizBeanEditor the parent bean editor
	 * @param client        the client
	 */
	public EditorPropertyComboboxSwing(final Application client, final EditorBean bizBeanEditor, final Property prop,
			final Property propBak) {
		super(client, bizBeanEditor, prop, propBak);
		super.initColors();
		if (prop instanceof PropertyChoice) {
			final TypePropertyChoice propType = (TypePropertyChoice) prop.getType();
			final ModelComboBoxEnum<RapidEnum> model = new ModelComboBoxEnum<>(propType);
			this.comboBox.setModel(model);
			this.comboBox.setRenderer(new RendererListEnum(client.getCurrentLocale(), this));
		} else if (prop instanceof PropertyCollection) {
			final PropertyCollection colPropType = (PropertyCollection) this.getProperty();
			final Document doc = this.getBeanEditor().getDocumentView().getDocument();
			final ModelComboBoxCollection<RapidBean> model = new ModelComboBoxCollection<>(colPropType, doc);
			this.comboBox.setModel(model);
			this.comboBox.setRenderer(new RendererListCollection(doc, client.getCurrentLocale()));
		} else {
			throw new RapidBeansRuntimeException("EditorPropertyComboboxSwing does not support properties of class \""
					+ prop.getType().getProptype().name() + "\"\n" + "");
		}
		if (prop.getType().isKeyCandidate() && (!this.getBeanEditor().isInNewMode())) {
			// unfortunately a combo box still can be edited
			// although editable is set to false.
			// this.comboBox.setEditable(false);
			// TODO Framework 4) how to set a combo box uneditable or to change
			// the text foreground while it is disabled?
			this.comboBox.setEnabled(false);
			this.comboBox.setBackground(COLOR_KEY);
		} else if (prop.getType().getMandatory()) {
			this.comboBox.setBackground(COLOR_MANDATORY);
		}
		this.comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				fireInputFieldChanged();
			}
		});
		this.updateUI();
		final ConfigPropEditorBean cfg = getConfig();
		if (prop.getReadonly() || (cfg != null && !cfg.getEnabled())) {
			this.comboBox.setEnabled(false);
		}
	}

	/**
	 * update the string presented in the editor.
	 */
	public void updateUI() {
		try {
			setUIEventLock();
			updateComboBox();
			final List<?> list = (List<?>) this.getProperty().getValue();
			if (list != null && list.size() > 0) {
				this.comboBox.setSelectedItem(list.get(0));
			} else {
				final TypeProperty proptype = this.getProperty().getType();
				if (!proptype.getMandatory()) {
					this.comboBox.setSelectedIndex(0);
				}
			}
		} finally {
			releaseUIEventLock();
		}
	}

	/**
	 * update the combo box.
	 */
	private void updateComboBox() {
		final Object selectedItem = this.comboBox.getSelectedItem();
		final TypeProperty proptype = this.getProperty().getType();
		if (proptype instanceof TypePropertyChoice) {
			this.comboBox.setModel(new ModelComboBoxEnum<>((TypePropertyChoice) this.getProperty().getType()));
		} else if (proptype instanceof TypePropertyCollection) {
			this.comboBox.setModel(new ModelComboBoxCollection<>((PropertyCollection) this.getProperty(),
					this.getBeanEditor().getDocumentView().getDocument()));
		}
		if (selectedItem != null) {
			this.comboBox.setSelectedItem(selectedItem);
		}
	}

	/**
	 * @return the combo boxes selected item.
	 */
	public Object getInputFieldValue() {
		return this.comboBox.getSelectedItem();
	}

	/**
	 * @return the combo boxes selected item as string.
	 */
	public Object getInputFieldValueString() {
		return this.comboBox.getSelectedItem().toString();
	}

	/**
	 * handler for added bean.
	 * 
	 * @param e the added event
	 */
	public void beanAdded(final AddedEvent e) {
		if (this.getProperty() instanceof PropertyCollection) {
			this.updateUI();
		}
	}

	/**
	 * handler for added bean.
	 * 
	 * @param e the added event
	 */
	public void bizBeanRemoved(final AddedEvent e) {
		if (this.getProperty() instanceof PropertyCollection) {
			this.updateUI();
		}
	}

	/**
	 * handler for changed bean.
	 * 
	 * @param e the changed event
	 */
	public void beanChanged(final ChangedEvent e) {
		super.beanChanged(e);
		// TypePropertyCollection colPropType = null;
		// TypePropertyChoice choicePropType = null;
		// final TypeProperty proptype = this.getProperty().getType();
		// if (proptype instanceof TypePropertyCollection) {
		// colPropType = (TypePropertyCollection) proptype;
		// }
		// if (proptype instanceof TypePropertyChoice) {
		// choicePropType = (TypePropertyChoice) proptype;
		// }
		// if (colPropType != null || choicePropType != null) {
		// this.updateUI();
		// }
	}

	class ComboBoxWrapper {
		private JComboBox<RapidEnum> enumComboBox = null;
		private JComboBox<RapidBean> beanComboBox = null;

		public Object get() {
			if (this.enumComboBox != null) {
				return this.enumComboBox;
			}
			return this.beanComboBox;
		}

		public void setModel(final ModelComboBoxEnum<RapidEnum> model) {
			this.enumComboBox = new JComboBox<>();
		}

		public void setRenderer(final RendererListEnum rendererListEnum) {
			this.enumComboBox.setRenderer(rendererListEnum);
		}

		public void setModel(final ModelComboBoxCollection<RapidBean> model) {
			this.beanComboBox = new JComboBox<>();
		}

		public void setRenderer(final RendererListCollection rendererListCollection) {
			this.beanComboBox.setRenderer(rendererListCollection);
		}

		public Object getSelectedItem() {
			if (this.enumComboBox != null) {
				return this.enumComboBox.getSelectedItem();
			}
			return this.beanComboBox.getSelectedItem();
		}

		public void setSelectedItem(final Object selectedItem) {
			if (this.enumComboBox != null) {
				this.enumComboBox.setSelectedItem(selectedItem);
			} else {
				this.beanComboBox.setSelectedItem(selectedItem);
			}
		}

		public void setSelectedIndex(final int i) {
			if (this.enumComboBox != null) {
				this.enumComboBox.setSelectedIndex(i);
			} else {
				this.beanComboBox.setSelectedIndex(i);
			}
		}

		public void setEnabled(final boolean b) {
			if (this.enumComboBox != null) {
				this.enumComboBox.setEnabled(b);
			} else {
				this.beanComboBox.setEnabled(b);
			}
		}

		public void setBackground(final Color colorKey) {
			if (this.enumComboBox != null) {
				this.enumComboBox.setBackground(colorKey);
			} else {
				this.beanComboBox.setBackground(colorKey);
			}
		}

		public void addItemListener(final ItemListener itemListener) {
			if (this.enumComboBox != null) {
				this.enumComboBox.addItemListener(itemListener);
			} else {
				this.beanComboBox.addItemListener(itemListener);
			}
		}
	}
}
