/*
 * Rapid Beans Framework: EditorPropertyMoneySwing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/18/2008
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

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.domain.finance.Currency;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.config.ConfigPropEditorBean;

/**
 * a special property editor for Money properties.
 * 
 * @author Martin Bluemel
 */
public class EditorPropertyMoneySwing extends EditorPropertyQuantitySwing {

	/**
	 * constructor.
	 * 
	 * @param prop          the bean property to edit
	 * @param propBak       the bean property backup
	 * @param bizBeanEditor the parent bean editor
	 * @param client        the client
	 */
	public EditorPropertyMoneySwing(final Application client, final EditorBean bizBeanEditor, final Property prop,
			final Property propBak) {
		super(client, bizBeanEditor, prop, propBak);
		final ConfigPropEditorBean cfg = getConfig();
		if (prop.getReadonly() || (cfg != null && !cfg.getEnabled())) {
			this.setEnabled(false);
		} else if (client.getConfiguration().getCurrency().getRestricttoone()) {
			this.getWidgetComboBox().setEnabled(false);
		}
	}

	/**
	 * pre select an appropriate unit
	 */
	protected void preSelectUnit() {
		if (this.getWidgetComboBox().getSelectedItem() == null) {
			final Application client = ApplicationManager.getApplication();
			if (client.getConfiguration().getCurrency() != null) {
				final Currency defaultCurrency = client.getConfiguration().getCurrency().getDefaultcurrency();
				if (defaultCurrency != null) {
					this.getWidgetComboBox().setSelectedItem(defaultCurrency);
				}
			} else {
				super.preSelectUnit();
			}
		}
	}

	/**
	 * Set enabled.
	 * 
	 * @param enabled if enabled or not
	 */
	protected void setEnabled(final boolean enabled) {
		final Application client = ApplicationManager.getApplication();
		if (client.getConfiguration().getCurrency().getRestricttoone()) {
			this.getWidgetTextField().setEnabled(enabled);
			if (this.getWidgetComboBox().isEnabled()) {
				this.getWidgetComboBox().setEnabled(false);
			}
		} else {
			super.setEnabled(enabled);
		}
	}
}
