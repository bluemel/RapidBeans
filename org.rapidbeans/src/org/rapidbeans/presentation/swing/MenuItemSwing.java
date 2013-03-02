/*
 * Rapid Beans Framework: MenuItemSwing.java
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

import javax.swing.ImageIcon;

import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.MenuItem;
import org.rapidbeans.presentation.config.ConfigMenuItem;
import org.rapidbeans.presentation.enabler.Enabler;
import org.rapidbeans.service.Action;

/**
 * A MenuItem encapsulating a Swing JMenuItem.
 * 
 * @author Martin Bluemel
 */
public class MenuItemSwing extends MenuItem {

	/**
	 * the Java Swing widget.
	 */
	private MenuItemSwingItem menuitem = new MenuItemSwingItem();

	/**
	 * @return the Java Swing widget
	 */
	public final Object getWidget() {
		return this.menuitem;
	}

	/**
	 * constructor.
	 * 
	 * @param client
	 *            the client
	 * @param menuItemConfig
	 *            the menu item configuration
	 * @param resourcePath
	 *            the resource path
	 */
	public MenuItemSwing(final ConfigMenuItem menuItemConfig, final Application client, final String resourcePath) {
		super(client, menuItemConfig, resourcePath);
		this.menuitem.setName(menuItemConfig.getName());
		this.menuitem.setText(getMenuText(client, resourcePath));
		Action actionConfig = this.getAction();
		if (actionConfig != null) {
			this.menuitem.addActionListener(new ActionHandlerActionListener(actionConfig.clone()));
		}
		Enabler enablerConfig = this.getEnabler();
		if (enablerConfig != null) {
			this.menuitem.setEnabler(enablerConfig.createInstance(client));
		}
		final ImageIcon icon = ((MainWindowSwing) client.getMainwindow()).getIconManager().getIcon(
				resourcePath + "." + this.getName() + ".icon");
		if (icon != null) {
			this.menuitem.setIcon(icon);
		}
	}
}
