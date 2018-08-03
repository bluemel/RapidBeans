/*
 * Rapid Beans Framework: EditorPropertyTimeOfDaySwing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 12/12/2006
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyQuantity;
import org.rapidbeans.core.basic.RapidQuantity;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypePropertyQuantity;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.domain.math.TimeOfDay;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.config.ConfigPropEditorBean;

/**
 * a special property editor for RapidQuantity properties. Combines a textHours
 * field for editing the magnitude with a combo box for selecting the unit.
 * 
 * @author Martin Bluemel
 */
public class EditorPropertyTimeOfDaySwing extends EditorPropertySwing {

	/**
	 * the textHours field.
	 */
	private JTextField textHours = new JTextField();

	/**
	 * the combo box.
	 */
	private JTextField textMinutes = new JTextField();

	/**
	 * the combo box.
	 */
	private JLabel labelColon = new JLabel(":");

	/**
	 * the panel.
	 */
	private BBEditorPropertyQuantitySwingPanel panel = null;

	/**
	 * the panel's layout manager.
	 */
	private LayoutManager layout = new GridBagLayout();

	/**
	 * @return the editor's widget
	 */
	public Object getWidget() {
		return this.panel;
	}

	/**
	 * constructor.
	 * 
	 * @param prop          the bean property to edit
	 * @param propBak       the bean property backup
	 * @param bizBeanEditor the parent bean editor
	 * @param client        the client
	 */
	public EditorPropertyTimeOfDaySwing(final Application client, final EditorBean bizBeanEditor, final Property prop,
			final Property propBak) {
		super(client, bizBeanEditor, prop, propBak);
		Component[] comps = { this.textHours, this.textMinutes };
		this.panel = new BBEditorPropertyQuantitySwingPanel(comps);
		super.initColors();

		if (!(prop instanceof PropertyQuantity)) {
			throw new RapidBeansRuntimeException("invalid propperty for a quantity editor");
		}
		if (!(prop.getValue() instanceof TimeOfDay)) {
			throw new RapidBeansRuntimeException("invalid propperty value for a TimeOfDay editor");
		}

		if (prop.getType().isKeyCandidate()) {
			if (this.getBeanEditor().getParentBean() == null) {
				this.textHours.setEditable(false);
				this.textMinutes.setEnabled(false);
				this.textMinutes.setBackground(COLOR_KEY);
			}
		}

		this.textHours.addKeyListener(new KeyListener() {
			public void keyTyped(final KeyEvent e) {
			}

			public void keyPressed(final KeyEvent e) {
			}

			public void keyReleased(final KeyEvent e) {
				fireInputFieldChanged();
			}
		});
		this.textMinutes.addKeyListener(new KeyListener() {
			public void keyTyped(final KeyEvent e) {
			}

			public void keyPressed(final KeyEvent e) {
			}

			public void keyReleased(final KeyEvent e) {
				fireInputFieldChanged();
			}
		});

		this.panel.setLayout(this.layout);
		this.panel.add(this.textHours, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.panel.add(this.labelColon, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.panel.add(this.textMinutes, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.updateUI();
		final ConfigPropEditorBean cfg = getConfig();
		if (prop.getReadonly() || (cfg != null && !cfg.getEnabled())) {
			this.panel.setEnabled(false);
		}
	}

	/**
	 * update the string presented in the editor.
	 */
	public void updateUI() {
		try {
			this.inputFieldValueCompleted = false;
			this.setUIEventLock();
			final TimeOfDay value = (TimeOfDay) this.getProperty().getValue();
			if (value == null) {
				this.textHours.setText("");
			} else {
				this.textHours.setText(Integer.toString(value.getHours()));
				this.textMinutes.setText(normalizeMinutesString(Integer.toString(value.getMinutes())));
			}
		} finally {
			this.releaseUIEventLock();
		}
	}

	/**
	 * @return the Text field's content
	 */
	public Object getInputFieldValue() {
		RapidQuantity ifValue = null;
		String s = this.textHours.getText();
		if (s.trim().length() > 0) {
			ifValue = RapidQuantity.createInstance(
					((TypePropertyQuantity) this.getProperty().getType()).getQuantitytype().getName(),
					getInputFieldValueString());
		}
		return ifValue;
	}

	/**
	 * validate an input field.
	 * 
	 * @return if the string in the input field is valid or at least could at least
	 *         get after appending additional characters.
	 * 
	 * @param ex the validation exception
	 */
	protected boolean hasPotentiallyValidInputField(final ValidationException ex) {
		if (ex.getSignature().endsWith("incomplete")) {
			return this.checkLocalNumber(false);
		} else {
			return this.inputFieldValueCompleted;
		}
	}

	/**
	 * show if the input field valuehas been expanded.
	 */
	private boolean inputFieldValueCompleted = false;

	/**
	 * check the localized number.
	 * 
	 * @param completenessRequired if completeness is required
	 * 
	 * @return if the local number is ok
	 */
	protected boolean checkLocalNumber(final boolean completenessRequired) {
		boolean ok = true;
		try {
			parseLocalNumberHours(completenessRequired);
			parseLocalNumberMinutes(completenessRequired);
		} catch (ValidationException e) {
			ok = false;
		}
		return ok;
	}

	/**
	 * A helper class for number parsing.
	 * 
	 * @author Martin Bluemel
	 */
	private class ParsedNumber {

		// /**
		// * the number.
		// */
		// private BigDecimal number = null;

		// /**
		// * the ok flag.
		// */
		// private boolean ok = false;

		/**
		 * constructor.
		 * 
		 * @param s the date string
		 * @param l the locale
		 */
		ParsedNumber(final String s, final Locale l) {
			new BigDecimal(s);
		}

		// /**
		// * @param s the day String to set
		// */
		// public void setNumber(final String s) {
		// this.number = new BigDecimal(s);
		// }

		// /**
		// * @param o the ok to set
		// */
		// public void setOk(final boolean o) {
		// this.ok = o;
		// }

		// /**
		// * @return the number
		// */
		// public BigDecimal getNumber() {
		// return number;
		// }

		// /**
		// * @return the ok
		// */
		// public boolean isOk() {
		// return ok;
		// }
	}

	/**
	 * validate the hour field.
	 * 
	 * @return if the string in the date field is valid or at least could at least
	 *         get after appending additional characters.
	 * 
	 * @param completenessRequired if the inpput fiels must be completeS
	 */
	private ParsedNumber parseLocalNumberHours(final boolean completenessRequired) {
		ParsedNumber number = new ParsedNumber(this.textHours.getText(), this.getLocale().getLocale());
		return number;
	}

	/**
	 * validate the minute field.
	 * 
	 * @return if the string in the date field is valid or at least could at least
	 *         get after appending additional characters.
	 * 
	 * @param completenessRequired if the inpput fiels must be completeS
	 */
	private ParsedNumber parseLocalNumberMinutes(final boolean completenessRequired) {
		ParsedNumber number = new ParsedNumber(this.textMinutes.getText(), this.getLocale().getLocale());
		return number;
	}

	/**
	 * @return the input field value as string.
	 */
	public String getInputFieldValueString() {
		return new TimeOfDay(this.textHours.getText() + ':' + normalizeMinutesString(this.textMinutes.getText()))
				.toString();
	}

	/**
	 * fill the minute String with leading zeros.
	 * 
	 * @param min the minutes
	 * 
	 * @return the filled string
	 */
	private static String normalizeMinutesString(final String min) {
		return StringHelper.fillUp(min, 2, '0', StringHelper.FillMode.left);
	}

	/**
	 * special panel that forwards set backgound call to its components.
	 * 
	 * @author Martin Bluemel
	 */
	private final class BBEditorPropertyQuantitySwingPanel extends JPanel {

		/**
		 * serial id.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * the parent property editor for the panel.
		 */
		private Component[] components = null;

		/**
		 * constructor with components.
		 * 
		 * @param comps the component widgets
		 */
		public BBEditorPropertyQuantitySwingPanel(final Component[] comps) {
			super();
			this.components = comps;
		}

		/**
		 * forwards set backgound call to its component widgets.
		 * 
		 * @param bg the new background color
		 */
		public void setBackground(final Color bg) {
			if (components != null) {
				for (Component comp : this.components) {
					comp.setBackground(bg);
				}
			} else {
				super.setBackground(bg);
			}
		}

		/**
		 * forwards set enabled call to its component widgets.
		 * 
		 * @param en if enabled or not
		 */
		public void setEnabled(final boolean en) {
			if (components != null) {
				for (Component comp : this.components) {
					comp.setEnabled(en);
				}
			} else {
				super.setEnabled(en);
			}
		}
	}
}
