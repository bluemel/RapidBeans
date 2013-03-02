/*
 * Rapid Beans Framework: EditorPropertyRadiobuttonSwing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 03/10/2006
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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JRadioButton;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyBoolean;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.config.ConfigPropEditorBean;

/**
 * the bean editor GUI.
 * 
 * @author Martin Bluemel
 */
public class EditorPropertyRadiobuttonSwing extends EditorPropertySwing {

	/**
	 * the text field.
	 */
	private JRadioButton radioButton = new JRadioButton();

	/**
	 * @return the editor's widget
	 */
	public Object getWidget() {
		return this.radioButton;
	}

	/**
	 * constructor.
	 * 
	 * @param prop
	 *            the bean property to edit
	 * @param propBak
	 *            the bean property backup
	 * @param bizBeanEditor
	 *            the parent bean editor
	 * @param client
	 *            the client
	 */
	public EditorPropertyRadiobuttonSwing(final Application client, final EditorBean bizBeanEditor,
			final Property prop, final Property propBak) {
		super(client, bizBeanEditor, prop, propBak);
		super.initColors();
		this.radioButton.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				fireInputFieldChanged();
			}
		});
		this.updateUI();
		final ConfigPropEditorBean cfg = getConfig();
		if (prop.getReadonly() || (cfg != null && !cfg.getEnabled())) {
			this.radioButton.setEnabled(false);
		}
	}

	/**
	 * updates the check box according to the boolean presented.
	 */
	public void updateUI() {
		try {
			this.setUIEventLock();
			if (this.getProperty() instanceof PropertyBoolean) {
				this.radioButton.setSelected(((Boolean) this.getProperty().getValue()).booleanValue());
			} else {
				this.radioButton.setText(this.getProperty().toString());
			}
		} finally {
			this.releaseUIEventLock();
		}
	}

	/**
	 * @return the CheckBox's content
	 */
	public Boolean getInputFieldValue() {
		return new Boolean(this.radioButton.isSelected());
	}

	/**
	 * @return the input field value as string.
	 */
	public String getInputFieldValueString() {
		return new Boolean(this.radioButton.isSelected()).toString();
	}
}
