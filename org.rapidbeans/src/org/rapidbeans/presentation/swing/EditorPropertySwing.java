/*
 * Rapid Beans Framework: EditorPropertySwing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/17/2006
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
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.EditorProperty;

/**
 * the bean editor GUI.
 * 
 * @author Martin Bluemel
 */
public abstract class EditorPropertySwing extends EditorProperty {

	/**
	 * the label.
	 */
	private JLabel label = new JLabel();

	/**
	 * @return the editor's label widget
	 */
	public final Object getLabelWidget() {
		return this.label;
	}

	/**
	 * set the focus to the input field's widget.
	 */
	public final void setFocus() {
		((Component) this.getWidget()).requestFocus();
	}

	/**
	 * constructor.
	 * 
	 * @param prop
	 *            the bean property to edit
	 * @param propBak
	 *            the bean property to edit
	 * @param bizBeanEditor
	 *            the parent bean editor
	 * @param client
	 *            the client
	 */
	public EditorPropertySwing(final Application client, final EditorBean bizBeanEditor, final Property prop,
			final Property propBak) {
		super(client, bizBeanEditor, prop, propBak);
		this.label.setText(this.getLabelText(bizBeanEditor, prop));
	}

	/**
	 * initialize the background color.
	 */
	protected void initColors() {
		if (this.getProperty().getType().isKeyCandidate()) {
			((Component) this.getWidget()).setBackground(COLOR_KEY);
		} else if (this.getProperty().getType().getMandatory()) {
			((Component) this.getWidget()).setBackground(COLOR_MANDATORY);
		}
	}

	/**
	 * the normal background color.
	 */
	private Color background = null;

	/**
	 * validate the input field and mark wrong fields.
	 * 
	 * @return if the input field is valid.
	 */
	public Object validateInputField() {
		Object value = null;
		Component widget = (Component) this.getWidget();
		try {
			value = super.validateInputFieldInternal();
		} catch (ValidationException e) {
			boolean infieldValueNull = false;
			try {
				infieldValueNull = this.getInputFieldValue() == null;
			} catch (ValidationException ev) {
				infieldValueNull = false;
			}
			if ((this.getProperty().getType().isKeyCandidate() || this.getProperty().getType().getMandatory())
					&& infieldValueNull) {
				if (this.background != null) {
					restoreNormalBackground();
				}
			} else {
				if (this.background == null) {
					if (!this.hasPotentiallyValidInputField(e)) {
						this.background = widget.getBackground();
						final Application client = ApplicationManager.getApplication();
						// this makes property editors testable without having
						// a client (application).
						if (client != null) {
							client.playSoundError();
						}
						widget.setBackground(COLOR_INVALID);
					}
				} else if (this.hasPotentiallyValidInputField(e)) {
					restoreNormalBackground();
				}
			}
			throw e;
		}
		if (this.background != null) {
			restoreNormalBackground();
		}
		return value;
	}

	/**
	 * restore normal background.
	 */
	private void restoreNormalBackground() {
		((Component) this.getWidget()).setBackground(this.background);
		this.background = null;
	}

	/**
	 * updates the property value presented in the editor gui.
	 */
	public abstract void updateUI();

	/**
	 * Backgound color for input fields presenting normal properties.
	 */
	public static final Color COLOR_NORMAL = new JTextField().getBackground();

	/**
	 * Backgound color for input fields presenting key properties.
	 */
	public static final Color COLOR_KEY = new Color(0xFFF0B0);

	/**
	 * Backgound color for input fields presenting key properties.
	 */
	public static final Color COLOR_MANDATORY = new Color(0xFFF0D0);

	/**
	 * Backgound color for input fields with invalid values.
	 */
	public static final Color COLOR_INVALID = new Color(0xFFE0E0);

	/**
	 * retrieves the label text from resourecs.
	 * 
	 * @param prop
	 *            the bean property to edit
	 * @param bizBeanEditor
	 *            the parent bean editor
	 * 
	 * @return the label text
	 */
	public String getLabelText(final EditorBean bizBeanEditor, final Property prop) {
		String text = null;
		if (text == null) {
			text = prop.getNameGui(this.getLocale());
		}
		return text;
	}
}
