/*
 * Rapid Beans Framework: ToolbarSwing.java
 * 
 * Copyright (C) 2010 Martin Bluemel
 * 
 * Creation Date: 02/12/2005
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

import javax.swing.JButton;
import javax.swing.JToolBar;

import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.MainWindow;
import org.rapidbeans.presentation.Toolbar;
import org.rapidbeans.presentation.ToolbarButton;
import org.rapidbeans.presentation.config.ConfigToolbar;

/**
 * @author Martin Bluemel
 */
public class ToolbarSwing extends Toolbar {

	/**
	 * the Java Swing JToolBar instance.
	 */
	private JToolBar toolBar = new JToolBar();

	/**
	 * @return the Java Swing JMenuBar instance
	 */
	public final Object getWidget() {
		return this.toolBar;
	}

	/**
	 * construct a ToolbarSwing.
	 * 
	 * @param client
	 *            the parent client
	 * @param toolbarConfig
	 *            the configuration
	 * @param mainWindow
	 *            the main window
	 * @param resourcePath
	 *            the resource path
	 */
	public ToolbarSwing(final Application client, final MainWindow mainWindow, final ConfigToolbar toolbarConfig,
			final String resourcePath) {
		super(client, mainWindow, toolbarConfig, resourcePath);
		if (this.getButtons() != null) {
			for (final ToolbarButton button : this.getButtons()) {
				this.toolBar.add((JButton) button.getWidget());
			}
		}

		// set the tool tip text
		final String ttext = getTextLocalized(client, resourcePath);
		if (ttext != null) {
			this.toolBar.setToolTipText(ttext);
		}
		this.toolBar.setVisible(true);
	}

	/**
	 * Update visibility of the tool bar and enabling of it's buttons
	 */
	public void update() {
		if (this.getOn()) {
			if (!this.toolBar.isVisible()) {
				this.toolBar.setVisible(true);
			}
		} else {
			if (this.toolBar.isVisible()) {
				this.toolBar.setVisible(false);
			}
		}
		if (this.getButtons() != null) {
			for (final ToolbarButton button : this.getButtons()) {
				button.update();
			}
		}
	}
}
