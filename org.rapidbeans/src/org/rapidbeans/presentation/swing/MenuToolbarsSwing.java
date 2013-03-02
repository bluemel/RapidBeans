/*
 * Rapid Beans Framework: MenuToolbars.java
 * 
 * Copyright (C) 2010 Martin Bluemel
 * 
 * Creation Date: 02/18/2010
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JToolBar;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.MainWindow;
import org.rapidbeans.presentation.MenuToolbars;
import org.rapidbeans.presentation.Toolbar;
import org.rapidbeans.presentation.config.ConfigMenuToolbars;

/**
 * The toolbar(s) menu.
 * 
 * @author Martin Bluemel
 */
public class MenuToolbarsSwing extends MenuToolbars {

	private MainWindow mainWindow = null;

	/**
	 * The tool bars sub menu.
	 */
	private JMenu toolbarsSubmenu = null;

	/**
	 * The single tool bar menu item
	 */
	private JCheckBoxMenuItem singleToolbarMenuItem = null;

	private Map<JCheckBoxMenuItem, Toolbar> toolbarMap = new HashMap<JCheckBoxMenuItem, Toolbar>();

	/**
	 * @return the Java Swing widget
	 */
	public final Object getWidget() {
		switch (this.mainWindow.getToolbars().size()) {
		case 0:
			throw new AssertionError("no toolbars cofigured");
		case 1:
			return this.singleToolbarMenuItem;
		default:
			return this.toolbarsSubmenu;
		}
	}

	/**
	 * constructor.
	 * 
	 * @param client
	 *            the client
	 * @param config
	 *            the menu item configuration
	 * @param resourcePath
	 *            the resource path
	 */
	public MenuToolbarsSwing(final ConfigMenuToolbars config,
			final Application client, final String resourcePath) {
		super(client, config, resourcePath);
		this.mainWindow = client.getMainwindow();
		switch (this.mainWindow.getToolbars().size()) {
		case 0:
			throw new AssertionError("no toolbars cofigured");
		case 1:
			this.singleToolbarMenuItem = new JCheckBoxMenuItem();
			this.toolbarMap.put(this.singleToolbarMenuItem, this.mainWindow
					.getToolbars().get(0));
			this.singleToolbarMenuItem.setSelected(this.mainWindow
					.getToolbars().get(0).getOn());
			this.singleToolbarMenuItem.setVisible(this.mainWindow.getToolbars()
					.get(0).getOn());
			this.singleToolbarMenuItem.setText(this.mainWindow.getToolbars()
					.get(0).getTextLocalized(client, resourcePath));
			this.singleToolbarMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					menuItemSelectionToggled(e);
				}
			});
			break;
		default:
			initToolbarsSubmenu(client, resourcePath);
			break;
		}
	}

	private void menuItemSelectionToggled(final ActionEvent e) {
		final JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();
		final Toolbar toolbar = this.toolbarMap.get(menuItem);
		toolbar.setOn(menuItem.isSelected());
		((JToolBar) toolbar.getWidget()).setVisible(menuItem.isSelected());
	}

	/**
	 * Initialize a new history sub menu.
	 * 
	 * @param app
	 *            the application
	 * @param resourcePath
	 */
	private void initToolbarsSubmenu(final Application client,
			final String resourcePath) {
		if (this.getName() == null || this.getName().length() == 0) {
			this.setName("toolbars");
		}
		this.toolbarsSubmenu = new JMenu();
		String menuText = null;
		final RapidBeansLocale locale = client.getCurrentLocale();
		if (locale != null) {
			try {
				final String key = resourcePath + "." + this.getName()
						+ ".label";
				menuText = locale.getStringGui(key);
			} catch (MissingResourceException e) {
				menuText = null;
			}
			if (menuText == null) {
				try {
					final String key = "commongui.text.toolbars";
					menuText = locale.getStringGui(key);
				} catch (MissingResourceException e) {
					menuText = null;
				}
			}
		}
		if (menuText == null) {
			menuText = this.getName();
		}
		if (menuText == null) {
			menuText = "toolbarsSubmenu";
		}
		this.toolbarsSubmenu.setText(menuText);
		for (final Toolbar toolbar : this.mainWindow.getToolbars()) {
			final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem();
			this.toolbarMap.put(menuItem, toolbar);
			menuItem.setSelected(toolbar.getOn());
			menuItem.setVisible(toolbar.getOn());
			final String toolbarResourcePath = resourcePath + ".toolbars."
					+ toolbar.getName();
			menuItem.setText(toolbar.getTextLocalized(client,
					toolbarResourcePath));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					menuItemSelectionToggled(e);
				}
			});
			this.toolbarsSubmenu.add(menuItem);
		}
	}
}
