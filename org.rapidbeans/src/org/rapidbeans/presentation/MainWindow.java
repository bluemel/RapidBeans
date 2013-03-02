/*
 * Rapid Beans Framework: MainWindow.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/01/2005
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

package org.rapidbeans.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.config.ConfigMainWindow;
import org.rapidbeans.presentation.config.ConfigToolbar;
import org.rapidbeans.presentation.guistate.MainWindowState;
import org.rapidbeans.presentation.guistate.UiState;
import org.rapidbeans.presentation.swing.MainWindowSwing;
import org.rapidbeans.service.CursorStyle;

/**
 * MainWindow.
 */
public abstract class MainWindow {

	/**
	 * Save the UI state.
	 * 
	 * @param uiState
	 *            the parent UI sate object
	 */
	public abstract MainWindowState saveUiState(final UiState uiState);

	/**
	 * Restore the UI state.
	 * 
	 * @param uiState
	 *            the parent UI sate object
	 */
	public abstract void restoreUiState(final UiState uiState);

	/**
	 * @return value of Property 'width'
	 */
	public abstract int getLocationX();

	/**
	 * @return value of Property 'height'
	 */
	public abstract int getLocationY();

	/**
	 * @return value of Property 'width'
	 */
	public abstract int getWidth();

	/**
	 * @return value of Property 'height'
	 */
	public abstract int getHeight();

	/**
	 * show the main window.
	 */
	public abstract void show();

	/**
	 * destroy the main window.
	 */
	public abstract void close();

	/**
	 * @return the widget.
	 */
	public abstract Object getWidget();

	/**
	 * add a view (internal frame) to the main window.
	 * 
	 * @param view
	 *            the view to add
	 */
	public abstract void addView(final View view);

	/**
	 * add a view (internal frame) to the main window.
	 * 
	 * @param view
	 *            the view to add
	 */
	public abstract void addView(final Application client, final View view);

	/**
	 * Puts the given view to the front so that it can be seen and edited.
	 * 
	 * @param view
	 *            the view to put to front
	 */
	public abstract void putToFront(final View view);

	/**
	 * remove a view (internal frame) from the main window.
	 * 
	 * @param view
	 *            the view to add
	 */
	public abstract void removeView(final View view);

	/**
	 * retrieve the document currently on top and active.
	 * 
	 * @return the active document
	 */
	public abstract Document getActiveDocument();

	/**
	 * retrieve the document view currently on top and active.
	 * 
	 * @return the active document ciew
	 */
	public abstract DocumentView getActiveDocumentView();

	/**
	 * @return the Z order if the view is a top level view. -1 otherwise.
	 */
	public abstract int getViewZOrder(final View view);

	/**
	 * create a MainWindow of a special type out of a configuration.
	 * 
	 * @param client
	 *            the parent client
	 * @param mainWindowConfig
	 *            the configuration
	 * 
	 * @return the instance
	 */
	public static MainWindow createInstance(final Application client,
			final ConfigMainWindow mainWindowConfig) {
		MainWindow mainWindow = null;
		switch (client.getConfiguration().getGuitype()) {
		case swing:
			mainWindow = new MainWindowSwing(client, mainWindowConfig);
			break;
		case eclipsercp:
			// mainWindow = new BBMainWindowEclispercp();
			break;
		default:
			throw new RapidBeansRuntimeException("Unknown GUI type \""
					+ client.getConfiguration().getGuitype().name() + "\"");
		}
		return mainWindow;
	}

	/**
	 * construct a MainWindow.
	 * 
	 * @param client
	 *            the parent client
	 * @param mainWindowConfig
	 *            the configuration
	 */
	public MainWindow(final Application client,
			final ConfigMainWindow mainWindowConfig) {
		client.setMainwindow(this);
		if (mainWindowConfig == null) {
			this.setName("mainwindow");
		} else {
			this.setName(mainWindowConfig.getName());
			final String resourcePathToolbar = this.getName() + ".toolbar";
			if (mainWindowConfig.getToolbars() != null) {
				for (final ConfigToolbar conf : mainWindowConfig.getToolbars()) {
					this.addToolbar(Toolbar.createInstance(client, this, conf,
							resourcePathToolbar));
				}
			}
			if (mainWindowConfig.getMenubar() != null) {
				this.setMenubar(Menubar.createInstance(client,
						mainWindowConfig.getMenubar(), this.getName()));
			}
		}
		this.footer = Footer.createInstance(client, mainWindowConfig);
	}

	protected String getWindowTitle(final Application client) {
		String windowTitle = null;
		final RapidBeansLocale locale = client.getCurrentLocale();
		if (locale != null) {
			try {
				windowTitle = locale.getStringGui(this.getName() + ".title");
			} catch (MissingResourceException e) {
				windowTitle = null;
			}
			if (windowTitle == null) {
				try {
					windowTitle = locale.getStringGui("mainwindow.title");
				} catch (MissingResourceException e) {
					windowTitle = null;
				}
			}
		}
		if (windowTitle == null) {
			if (locale != null) {
				try {
					windowTitle = locale.getStringGui("mainwidow.application");
				} catch (MissingResourceException e) {
					windowTitle = "Rapid Beans Application";
				}
			}
			windowTitle += ": " + client.getConfiguration().getName();
		}
		return windowTitle;
	}

	/**
	 * property "name".
	 */
	private String name = null;

	/**
	 * property "menubar".
	 */
	private Menubar menubar;

	/**
	 * property "toolbars".
	 */
	private List<Toolbar> toolbars = new ArrayList<Toolbar>();

	/**
	 * @return value of Property 'name'
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * setter for Property 'name'.
	 * 
	 * @param argValue
	 *            value of Property 'name' to set
	 */
	public final void setName(final String argValue) {
		this.name = argValue;
	}

	/**
	 * @return value of Property 'menubar'
	 */
	public final Menubar getMenubar() {
		return this.menubar;
	}

	/**
	 * setter for Property 'menubar'.
	 * 
	 * @param argValue
	 *            value of Property 'menubar' to set
	 */
	public final void setMenubar(Menubar argValue) {
		this.menubar = argValue;
	}

	/**
	 * @return value of Property 'toolbar'
	 */
	public final List<Toolbar> getToolbars() {
		return this.toolbars;
	}

	/**
	 * add a tool bar.
	 * 
	 * @param argValue
	 *            value of Property 'toolbar' to set
	 */
	public final void addToolbar(final Toolbar toolbar) {
		this.toolbars.add(toolbar);
	}

	/**
	 * Update any history views for the history given.
	 */
	public void updateHistoryViews() {
		this.menubar.updateHistoryViews();
	}

	/**
	 * Update all tool bars.
	 */
	public void updateToolbars() {
		for (final Toolbar toolbar : this.getToolbars()) {
			toolbar.update();
		}
	}

	private Footer footer;

	/**
	 * @return the footer
	 */
	public Footer getFooter() {
		return footer;
	}

	/**
	 * Set the cursor to a defined style.
	 * 
	 * @param style
	 *            the cursor style to set.
	 */
	public abstract void setCursor(CursorStyle style);
}
