/*
 * Rapid Beans Framework: SubmenuSwing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 12/07/2005
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

import javax.swing.JComponent;
import javax.swing.JMenu;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.MenuEntry;
import org.rapidbeans.presentation.MenuSeparator;
import org.rapidbeans.presentation.Submenu;
import org.rapidbeans.presentation.config.ConfigSubmenu;

/**
 * A submenu encapsulating a Swing JMenu.
 * 
 * @author Martin Bluemel
 */
public class SubmenuSwing extends Submenu {

	/**
	 * the Java Swing Menu.
	 */
	private JMenu menu = new JMenu();

	/**
	 * @return the Java Swing Menu.
	 */
	public final Object getWidget() {
		return this.menu;
	}

	/**
	 * constructor.
	 * 
	 * @param client
	 *            the client
	 * @param menuConfig
	 *            the menu configuration
	 * @param resourcePath
	 *            the resource path
	 */
	public SubmenuSwing(final ConfigSubmenu submenuConfig,
			final Application client, final String resourcePath) {
		super(client, submenuConfig, resourcePath);
		String menuText = null;
		final RapidBeansLocale locale = client.getCurrentLocale();
		if (locale != null) {
			try {
				menuText = locale.getStringGui(resourcePath + "."
						+ this.getName() + ".label");
			} catch (MissingResourceException e) {
				menuText = this.getName();
			}
		}
		if (menuText == null) {
			menuText = this.getName();
		}
		this.menu.setText(menuText);
		for (MenuEntry menuEntry : this.getMenuentrys()) {
			if (menuEntry instanceof MenuSeparator) {
				this.menu.addSeparator();
			} else if (menuEntry instanceof MenuHistoryOpenDocumentSwing) {
				if (menuEntry.getWidget() != null) {
					this.menu.add((JComponent) menuEntry.getWidget());
				}
			} else if (menuEntry instanceof MenuToolbarsSwing) {
				if (menuEntry.getWidget() != null) {
					this.menu.add((JComponent) menuEntry.getWidget());
				}
			} else {
				this.menu.add((JComponent) menuEntry.getWidget());
			}
		}
	}
}
