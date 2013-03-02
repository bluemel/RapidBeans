/*
 * Rapid Beans Framework: ToolbarButtonSwing.java
 * 
 * Copyright (C) 2010 Martin Bluemel
 * 
 * Creation Date: 02/12/2010
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

import java.util.MissingResourceException;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.MainWindow;
import org.rapidbeans.presentation.ToolbarButton;
import org.rapidbeans.presentation.config.ConfigToolbarButton;
import org.rapidbeans.service.Action;

/**
 * A ToolbarButton encapsulating a Swing JButton.
 * 
 * @author Martin Bluemel
 */
public class ToolbarButtonSwing extends ToolbarButton {

	/**
	 * the Java Swing widget.
	 */
	private JButton button = new JButton();

	/**
	 * @return the button
	 */
	protected final JButton getButton() {
		return this.button;
	}

	/**
	 * @return the Java Swing widget
	 */
	public final Object getWidget() {
		return this.button;
	}

	/**
	 * Update enabling of it's buttons
	 */
	public void update() {
		if (this.getEnabler() != null) {
			if (this.getEnabler().getEnabled()) {
				if (!this.button.isEnabled()) {
					this.button.setEnabled(true);
				}
			} else {
				if (this.button.isEnabled()) {
					this.button.setEnabled(false);
				}
			}
		} else {
			if (!this.button.isEnabled()) {
				this.button.setEnabled(true);
			}
		}
	}

	/**
	 * constructor.
	 * 
	 * @param client
	 *            the client
	 * @param mainWindow
	 *            the main window
	 * @param menuItemConfig
	 *            the menu item configuration
	 * @param resourcePath
	 *            the resource path
	 */
	public ToolbarButtonSwing(final ConfigToolbarButton cfg,
			final Application client, final MainWindow mainWindow,
			final String resourcePath) {
		super(client, cfg, resourcePath);
		this.button.setName(cfg.getName());
		final RapidBeansLocale locale = client.getCurrentLocale();
		final String key = resourcePath + "." + this.getName().toLowerCase()
				+ ".icon";

		// set the icon
		final ImageIcon icon = ((MainWindowSwing) mainWindow).getIconManager()
				.getIcon(key);
		if (icon != null) {
			this.button.setIcon(icon);
		}

		// set the tool tip text
		String ttext = null;
		if (locale != null) {
			try {
				ttext = locale.getStringGui(resourcePath + "." + this.getName()
						+ ".tooltip");
			} catch (MissingResourceException e) {
				ttext = null;
			}
		}
		if (ttext == null) {
			try {
				ttext = locale.getStringGui("commongui.text." + this.getName());
			} catch (MissingResourceException e) {
				ttext = null;
			}
		}
		if (ttext != null) {
			this.button.setToolTipText(ttext);
		}

		// set the label text only if explicitly defined
		// or if no icon defined
		String text = null;
		if (locale != null) {
			try {
				text = locale.getStringGui(resourcePath + "." + this.getName()
						+ ".label");
			} catch (MissingResourceException e) {
				text = null;
			}
		}
		if (text == null && icon == null) {
			try {
				text = locale.getStringGui("commongui.text." + this.getName());
			} catch (MissingResourceException e) {
				text = null;
			}
		}
		if (text == null && icon == null) {
			text = this.getName();
		}
		if (text != null) {
			this.button.setText(text);
		}

		Action actionConfig = this.getAction();
		if (actionConfig != null) {
			this.button.addActionListener(new ActionHandlerActionListener(
					actionConfig.clone()));
		}

		this.update();
		this.button.setVisible(true);
	}
}
