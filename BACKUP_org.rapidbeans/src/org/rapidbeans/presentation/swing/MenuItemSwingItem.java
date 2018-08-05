/*
 * Rapid Beans Framework: MenuItemSwingItem.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 12/27/2006
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

import java.awt.Graphics;

import javax.swing.JMenuItem;

import org.rapidbeans.presentation.enabler.Enabler;

/**
 * A MenuItem extending a Swing JMenuItem.
 * 
 * @author Martin Bluemel
 */
public class MenuItemSwingItem extends JMenuItem {

	/**
	 * serial id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the hook for the enabling controller.
	 */
	private Enabler enabler = null;

	/**
	 * paint.
	 * 
	 * @param g
	 *            the Graphics context
	 */
	public void paint(final Graphics g) {
		if (this.enabler != null) {
			this.setEnabled(this.enabler.getEnabled());
		}
		super.paint(g);
	}

	/**
	 * setter.
	 * 
	 * @param en
	 *            the enabler to set
	 */
	protected void setEnabler(final Enabler en) {
		this.enabler = en;
	}
}
