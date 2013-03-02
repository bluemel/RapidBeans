/*
 * Rapid Beans Framework: EditorPropertyCheckboxSwing.java
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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.config.ConfigPropEditorBean;

/**
 * the bean editor GUI.
 * 
 * @author Martin Bluemel
 */
public class EditorPropertyCheckboxSwing extends EditorPropertySwing {

	/**
	 * the text field.
	 */
	private JCheckBox checkbox = new JCheckBox();

	/**
	 * @return the editor's widget
	 */
	public Object getWidget() {
		return this.checkbox;
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
	public EditorPropertyCheckboxSwing(final Application client,
			final EditorBean bizBeanEditor, final Property prop,
			final Property propBak) {
		super(client, bizBeanEditor, prop, propBak);
		super.initColors();
		this.checkbox.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				fireInputFieldChanged();
			}
		});
		this.updateUI();
		final ConfigPropEditorBean cfg = getConfig();
		if (prop.getReadonly() || (cfg != null && !cfg.getEnabled())) {
			this.checkbox.setEnabled(false);
		}
	}

	/**
	 * updates the check box according to the boolean presented.
	 */
	public void updateUI() {
		try {
			this.setUIEventLock();
			Boolean b = (Boolean) this.getProperty().getValue();
			if (b == null) {
				this.checkbox.setSelected(false);
			} else {
				this.checkbox.setSelected(b.booleanValue());
			}
		} finally {
			this.releaseUIEventLock();
		}
	}

	/**
	 * @return the CheckBox's content
	 */
	public Boolean getInputFieldValue() {
		return new Boolean(this.checkbox.isSelected());
	}

	/**
	 * @return the CheckBox's content as text
	 */
	public String getInputFieldValueString() {
		return new Boolean(this.checkbox.isSelected()).toString();
	}
}
