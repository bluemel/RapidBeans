/*
 * Rapid Beans Framework: EditorPropertyQuantitySwing.java
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyQuantity;
import org.rapidbeans.core.basic.RapidQuantity;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypePropertyQuantity;
import org.rapidbeans.core.type.TypeRapidEnum;
import org.rapidbeans.core.type.TypeRapidQuantityConversionTable;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.config.ConfigPropEditorBean;

/**
 * a special property editor for RapidQuantity properties. Combines a text field
 * for editing the magnitude with a combo box for selecting the unit.
 * 
 * @author Martin Bluemel
 */
public class EditorPropertyQuantitySwing extends EditorPropertySwing {

	/**
	 * the text field.
	 */
	private JTextField text = new JTextField();

	/**
	 * the combo box.
	 */
	private JComboBox combobox = new JComboBox();

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
	 * @return the editor's widget
	 */
	public JTextField getWidgetTextField() {
		return this.text;
	}

	/**
	 * @return the editor's widget
	 */
	public JComboBox getWidgetComboBox() {
		return this.combobox;
	}

	/**
	 * setter.
	 * 
	 * @param prop
	 *            the property to set.
	 */
	@Override
	protected void setProperty(final Property prop) {
		super.setProperty(prop);
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
	public EditorPropertyQuantitySwing(final Application client,
			final EditorBean bizBeanEditor, final Property prop,
			final Property propBak) {
		super(client, bizBeanEditor, prop, propBak);
		this.panel = new BBEditorPropertyQuantitySwingPanel(this);
		super.initColors();

		if (!(prop instanceof PropertyQuantity)) {
			throw new RapidBeansRuntimeException(
					"invalid propperty for a quantity editor");
		}
		if (prop.getType().isKeyCandidate()) {
			if (this.getBeanEditor().getParentBean() == null) {
				this.text.setEditable(false);
				this.combobox.setEnabled(false);
				this.combobox.setBackground(COLOR_KEY);
			}
		}
		this.text.addKeyListener(new KeyListener() {
			public void keyTyped(final KeyEvent e) {
			}

			public void keyPressed(final KeyEvent e) {
			}

			public void keyReleased(final KeyEvent e) {
				fireInputFieldChanged();
			}
		});

		this.combobox.setModel(new ModelComboBoxEnum(
				(TypePropertyQuantity) prop.getType()));
		this.combobox.setRenderer(new RendererListEnum(client
				.getCurrentLocale(), this));
		this.combobox.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				convertToNewUnit();
				fireInputFieldChanged();
			}
		});

		this.panel.setLayout(this.layout);
		this.panel.add(this.text, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 0, 5, 5), 0, 0));
		this.panel.add(this.combobox, new GridBagConstraints(1, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		this.updateUI();
		final ConfigPropEditorBean cfg = getConfig();
		if (prop.getReadonly() || (cfg != null && !cfg.getEnabled())) {
			this.panel.setEnabled(false);
		}
	}

	private RapidQuantity lastQuant = null;

	/**
	 * convert the value shown to the new unit
	 */
	private void convertToNewUnit() {
		try {
			this.setUIEventLock();
			RapidQuantity value = getInputFieldValue();
			if (value != null && this.lastQuant != null) {
				value = lastQuant.convert(value.getUnit());
				this.text.setText(value.getMagnitude().toString());
			}
			this.lastQuant = value;
		} finally {
			this.releaseUIEventLock();
		}
	}

	/**
	 * update the string presented in the editor.
	 */
	public void updateUI() {
		try {
			this.setUIEventLock();
			final RapidQuantity value = (RapidQuantity) this.getProperty()
					.getValue();
			if (value == null) {
				this.text.setText("");
				this.combobox.setSelectedItem(((TypePropertyQuantity) (this
						.getProperty().getType())).getDefaultUnit());
			} else {
				if (value.getMagnitude() == null) {
					this.text.setText("");
				} else {
					this.text.setText(value.getMagnitude().toString());
				}
				if (value.getUnit() == null) {
					this.combobox.setSelectedItem(((TypePropertyQuantity) (this
							.getProperty().getType())).getDefaultUnit());
				} else {
					this.combobox.setSelectedItem(value.getUnit());
				}
			}
			if (this.combobox.getSelectedItem() == null) {
				preSelectUnit();
			}
		} finally {
			this.releaseUIEventLock();
		}
	}

	/**
	 * @return the Text field's content
	 */
	public RapidQuantity getInputFieldValue() {
		RapidQuantity ifValue = null;
		if (this.text.getText().trim().length() > 0) {
			if (this.combobox.getSelectedItem() == null) {
				throw new ValidationException("invalid.quantity.unit.missing",
						ifValue, "no unit specified for quantity");
			}
			try {
				final String qtypename = ((TypePropertyQuantity) this
						.getProperty().getType()).getQuantitytype().getName();
				final String ifValueString = getInputFieldValueString();
				ifValue = RapidQuantity
						.createInstance(qtypename, ifValueString);
			} catch (RapidBeansRuntimeException e) {
				final Throwable c1 = e.getCause();
				if (c1 == null) {
					throw e;
				} else if (c1 instanceof ValidationException) {
					throw (ValidationException) c1;
				} else if (c1 instanceof InvocationTargetException) {
					final Throwable c2 = c1.getCause();
					if (c2 == null) {
						throw e;
					} else if (c2 instanceof ValidationException) {
						throw (ValidationException) c2;
					} else {
						throw e;
					}
				} else {
					throw e;
				}
			}
		}
		return ifValue;
	}

	/**
	 * validate an input field.
	 * 
	 * @return if the string in the input field is valid or at least could at
	 *         least get after appending additional characters.
	 * 
	 * @param ex
	 *            the validation exception
	 */
	protected boolean hasPotentiallyValidInputField(final ValidationException ex) {
		boolean potentiallyValid = false;
		if (ex.getSignature().endsWith("incomplete")) {
			potentiallyValid = this.checkLocalNumber(false);
		} else {
			final String s = this.text.getText().trim();
			final int slen = s.length();
			char c;
			potentiallyValid = true;
			for (int i = 0; potentiallyValid == true && i < slen; i++) {
				c = s.charAt(i);
				if ((c < '0' || c > '9') && c != 'E' && c != 'e') {
					potentiallyValid = false;
				}
			}
		}
		return potentiallyValid;
	}

	/**
	 * check the localized number.
	 * 
	 * @param completenessRequired
	 *            if completeness is required
	 * 
	 * @return if the local number is ok
	 */
	protected boolean checkLocalNumber(final boolean completenessRequired) {
		boolean ok = true;
		try {
			parseLocalNumber(completenessRequired);
		} catch (ValidationException e) {
			ok = false;
		}
		return ok;
	}

	/**
	 * pre select an appropriate unit
	 */
	protected void preSelectUnit() {
		final TypePropertyQuantity proptype = (TypePropertyQuantity) this
				.getProperty().getType();
		if (proptype.getDefaultUnit() != null) {
			// select the property's default unit
			this.combobox.setSelectedItem(proptype.getDefaultUnit());
		} else {
			final TypeRapidQuantityConversionTable ct = proptype
					.getQuantitytype().getConversionTable();
			if (ct.getNormUnit() != null) {
				// select the quantity's norm unit
				this.combobox.setSelectedItem(ct.getNormUnit());
			} else {
				// select the quantity's first unit
				final TypeRapidEnum et = proptype.getQuantitytype()
						.getUnitInfo();
				this.combobox.setSelectedItem(et.elementOf(0));
			}
		}
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
		 * @param s
		 *            the number string
		 * @param l
		 *            the locale
		 */
		ParsedNumber(final String s, final Locale l) {
			if (s != null && s.length() > 0) {
				try {
					new BigDecimal(s);
				} catch (NumberFormatException e) {
					throw new ValidationException(
							"invalid.quantity.magnitude.only", s, "\"" + s
									+ "\" is not a valid number.",
							new Object[] { s });
				}
			}
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
		// * @return if the property is O.K.
		// */
		// public boolean isOk() {
		// return ok;
		// }

	}

	/**
	 * validate the date field.
	 * 
	 * @return if the string in the date field is valid or at least could at
	 *         least get after appending additional characters.
	 * 
	 * @param completenessRequired
	 *            if the input fields must be completeS
	 */
	private ParsedNumber parseLocalNumber(final boolean completenessRequired) {
		ParsedNumber number = new ParsedNumber(this.text.getText(), this
				.getLocale().getLocale());
		return number;
	}

	/**
	 * @return the input field value as string.
	 */
	public String getInputFieldValueString() {
		return this.text.getText().trim() + ' '
				+ this.combobox.getSelectedItem();
	}

	/**
	 * Set the background.
	 * 
	 * @param bg
	 *            the new background color
	 */
	protected void setBackground(final Color bg) {
		this.text.setBackground(bg);
		this.combobox.setBackground(bg);
	}

	/**
	 * Set enabled.
	 * 
	 * @param enabled
	 *            if enabled or not
	 */
	protected void setEnabled(final boolean enabled) {
		this.text.setEnabled(enabled);
		this.combobox.setEnabled(enabled);
	}

	/**
	 * special panel that forwards set background call to its components.
	 * 
	 * @author Martin Bluemel
	 */
	private final class BBEditorPropertyQuantitySwingPanel extends JPanel {

		/**
		 * serialization id.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * the parent property editor for the panel.
		 */
		private final EditorPropertyQuantitySwing parentEditor;

		/**
		 * constructor with components.
		 * 
		 * @param parent
		 *            the parent editor
		 */
		public BBEditorPropertyQuantitySwingPanel(
				final EditorPropertyQuantitySwing parent) {
			super();
			this.parentEditor = parent;
		}

		/**
		 * forwards set background call to its component widgets.
		 * 
		 * @param bg
		 *            the new background color
		 */
		public void setBackground(final Color bg) {
			if (this.parentEditor != null) {
				this.parentEditor.setBackground(bg);
			} else {
				super.setBackground(bg);
			}
		}

		/**
		 * forwards set enabled call to its component widgets.
		 * 
		 * @param enabled
		 *            if enabled or not
		 */
		public void setEnabled(final boolean enabled) {
			if (this.parentEditor != null) {
				this.parentEditor.setEnabled(enabled);
			} else {
				super.setEnabled(enabled);
			}
		}
	}
}
