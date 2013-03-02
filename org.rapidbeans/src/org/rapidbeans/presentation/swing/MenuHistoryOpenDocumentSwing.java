/*
 * Rapid Beans Framework: MenuHistoryOpenDocumentSwing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 08/12/2009
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
import java.util.List;
import java.util.MissingResourceException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.MenuEntry;
import org.rapidbeans.presentation.MenuHistoryOpenDocument;
import org.rapidbeans.presentation.MenuHistoryOpenDocumentPresentationMode;
import org.rapidbeans.presentation.MenuSeparator;
import org.rapidbeans.presentation.Submenu;
import org.rapidbeans.presentation.config.ConfigMenuHistoryOpenDocument;

/**
 * A History Menu encapsulating a History (sub) menu.
 * 
 * @author Martin Bluemel
 */
public class MenuHistoryOpenDocumentSwing extends MenuHistoryOpenDocument {

	/**
	 * The history sub menu.
	 */
	private JMenu historySubmenu = null;

	/**
	 * For lazy initialization of the history sub menu.
	 */
	private String resourcePath;

	/**
	 * @return the Java Swing widget
	 */
	public final Object getWidget() {
		return this.historySubmenu;
	}

	/**
	 * Helper flag to eliminate two consecutive separators while the history
	 * menu is switched off.
	 */
	private boolean separatorRemoved = false;

	/**
	 * Helper flag to eliminate two consecutive separators while the history
	 * menu is switched off.
	 */
	private boolean lastSwitchedOn = true;

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
	public MenuHistoryOpenDocumentSwing(
			final ConfigMenuHistoryOpenDocument config,
			final Application client, final String resourcePath) {
		super(client, config, resourcePath);
		switch (getPresentation()) {
		case inline:
			break;
		case submenu:
			initHistorySubmenu(client, resourcePath);
			break;
		}
		this.resourcePath = resourcePath;
	}

	/**
	 * Initialize a new history sub menu.
	 * 
	 * @param app
	 *            the application
	 * @param resourcePath
	 */
	private void initHistorySubmenu(final Application client,
			final String resourcePath) {
		if (this.getName() == null || this.getName().length() == 0) {
			this.setName("recentdocumentsopened");
		}
		this.historySubmenu = new JMenu();
		String menuText = null;
		final RapidBeansLocale locale = client.getCurrentLocale();
		if (locale != null) {
			try {
				final String key = resourcePath + "." + this.getName()
						+ ".label";
				menuText = locale.getStringGui(key);
			} catch (MissingResourceException e) {
				menuText = this.getName();
			}
		}
		if (menuText == null) {
			menuText = "historySubmenu";
		}
		this.historySubmenu.setText(menuText);
	}

	/**
	 * Update the history.
	 */
	public void update() {
		super.update();
		if (getHistList() == null) {
			return;
		}
		final Submenu parentMenu = (Submenu) this.getParentBean();
		final JMenu parentMenuWidget = (JMenu) parentMenu.getWidget();
		final List<MenuEntry> menuEntries = (List<MenuEntry>) parentMenu
				.getMenuentrys();
		final int entryCount = menuEntries.size();
		int itemCount = parentMenuWidget.getItemCount();
		int indexEntries = 0;
		int indexItems = 0;
		boolean updated = false;
		while (!updated && indexEntries < entryCount) {
			final MenuEntry entry = menuEntries.get(indexEntries);
			MenuEntry nextEntry = null;
			if (indexEntries < (entryCount - 1)) {
				nextEntry = menuEntries.get(indexEntries + 1);
			}
			JMenuItem menuItem = null;
			if (indexItems < itemCount) {
				menuItem = parentMenuWidget.getItem(indexItems);
			}
			if (entry instanceof MenuHistoryOpenDocumentSwing) {
				if (entry == this) {
					// remove old history menu items
					if (this.getPresentation() == MenuHistoryOpenDocumentPresentationMode.inline
							|| (this.getPresentation() == MenuHistoryOpenDocumentPresentationMode.submenu && (this.historySubmenu == null || !this
									.getOn()))) {
						while ((nextEntry == null && indexItems < itemCount)
								|| ((!this.separatorRemoved)
										&& nextEntry != null && menuItem != nextEntry
										.getWidget())) {
							parentMenuWidget.remove(indexItems);
							itemCount--;
							if (indexItems < itemCount) {
								menuItem = parentMenuWidget.getItem(indexItems);
							}
						}
					}

					if (this.getOn()) {
						// insert new history menu items
						final int histListCount = getHistList().size();
						switch (this.getPresentation()) {
						case inline:
							for (int i = 0; i < histListCount; i++) {
								final JMenuItem histMenuItem = new JMenuItem();
								histMenuItem.setText(super.getMenuText(i));
								histMenuItem
										.addActionListener(new ActionListener() {
											public void actionPerformed(
													ActionEvent e) {
												histMenuItemSelected((JMenuItem) e
														.getSource());
											}
										});
								parentMenuWidget.insert(histMenuItem,
										indexItems++);
								itemCount++;
							}
							break;

						case submenu:
							if (this.historySubmenu == null) {
								initHistorySubmenu(
										ApplicationManager.getApplication(),
										this.resourcePath);
								parentMenuWidget.insert(this.historySubmenu,
										indexItems++);
								itemCount++;
							}
							this.historySubmenu.removeAll();
							for (int i = 0; i < histListCount; i++) {
								final JMenuItem histMenuItem = new JMenuItem();
								histMenuItem.setText(super.getMenuText(i));
								histMenuItem
										.addActionListener(new ActionListener() {
											public void actionPerformed(
													ActionEvent e) {
												histMenuItemSelected((JMenuItem) e
														.getSource());
											}
										});
								this.historySubmenu.add(histMenuItem);
							}
							break;
						}
						if (this.separatorRemoved) {
							parentMenuWidget.insertSeparator(indexItems++);
							itemCount++;
							this.separatorRemoved = false;
						}
						this.lastSwitchedOn = true;
					} else {
						// if there are two separators remove the second
						if (menuEntries.get(indexEntries - 1) instanceof MenuSeparator
								&& nextEntry instanceof MenuSeparator
								&& this.lastSwitchedOn
								&& parentMenuWidget.getItem(indexItems) == null) {
							parentMenuWidget.remove(indexItems);
							itemCount--;
							this.separatorRemoved = true;
						}

						if (this.getPresentation() == MenuHistoryOpenDocumentPresentationMode.submenu) {
							this.historySubmenu = null;
						}
						this.lastSwitchedOn = false;
					}
					updated = true;
				} else {
					// skip foreign history in same parent menu
					switch (this.getPresentation()) {
					case inline:
						// skip foreign in lined history menu items
						while ((nextEntry == null && indexItems < itemCount)
								|| (nextEntry != null && menuItem != nextEntry
										.getWidget())) {
							indexItems++;
							if (indexItems < itemCount) {
								menuItem = parentMenuWidget.getItem(indexItems);
							}
						}
						break;
					case submenu:
						// skip submenu
						indexItems++;
						break;
					}
				}
			} else {
				if (menuItem != entry.getWidget()) {
					throw new RapidBeansRuntimeException(
							"Assertion failed: menuItem != entry.getWidget");
				}
				indexItems++;
			}
			indexEntries++;
		}
	}

	/**
	 * Action handler for selected history menu item
	 * 
	 * @param selectedMenuItem
	 *            the menu item that has been selected
	 */
	private void histMenuItemSelected(final JMenuItem selectedMenuItem) {
		final int index = Integer.parseInt(StringHelper
				.splitFirst(selectedMenuItem.getText()));
		super.histMenuItemSelected(index - 1);
	}
}
