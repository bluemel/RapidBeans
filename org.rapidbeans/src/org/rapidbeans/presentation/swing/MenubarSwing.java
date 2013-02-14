/*
 * Rapid Beans Framework: MenubarSwing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/03/2005
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

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.Menubar;
import org.rapidbeans.presentation.Submenu;
import org.rapidbeans.presentation.config.ConfigMenubar;

/**
 * @author bluemel
 */
public class MenubarSwing extends Menubar {

	/**
	 * the Java Swing JMenuBar instance.
	 */
	private JMenuBar menuBar = new JMenuBar();

	/**
	 * @return the Java Swing JMenuBar instance
	 */
	public final Object getWidget() {
		return this.menuBar;
	}

	/**
	 * construct a MenubarSwing.
	 * 
	 * @param client
	 *            the parent client
	 * @param menubarConfig
	 *            the configuration
	 * @param resourcePath
	 *            the resource path
	 */
	public MenubarSwing(final Application client,
			final ConfigMenubar menubarConfig, final String resourcePath) {
		super(client, menubarConfig, resourcePath);
		for (Submenu submenu : this.getMenus()) {
			if (submenu.getMenuentrys().size() > 0) {
				this.menuBar.add((JMenu) ((SubmenuSwing) submenu).getWidget());
			}
		}
	}
}
