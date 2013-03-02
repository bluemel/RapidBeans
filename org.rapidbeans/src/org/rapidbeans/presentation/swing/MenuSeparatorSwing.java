/*
 * Rapid Beans Framework: MenuSeparatorSwing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 08/11/2009
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

import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.MenuSeparator;
import org.rapidbeans.presentation.config.ConfigMenuSeparator;

/**
 * A MenuSeparaotr encapsulating a Swing JMenuItem.
 * 
 * @author Martin Bluemel
 */
public class MenuSeparatorSwing extends MenuSeparator {

	/**
	 * @return the Java Swing widget
	 */
	public final Object getWidget() {
		return null;
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
	public MenuSeparatorSwing(final ConfigMenuSeparator config,
			final Application client, final String resourcePath) {
		super(client, config, resourcePath);
	}
}
