/*
 * Rapid Beans Framework: EnablerActiveViewClose.java
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

package org.rapidbeans.presentation.enabler;

/**
 * enables / disable the close document / view menu item.
 * 
 * @author Martin Bluemel
 */
public class EnablerActiveViewClose extends Enabler {

	public EnablerActiveViewClose() {
		super(EnablerActiveViewClose.class.getName());
	}

	/**
	 * the excecute method of every Action.
	 * 
	 * @return if the menu is enable or not.
	 */
	public boolean getEnabled() {
		if (this.getClient().getActiveView() == null) {
			return false;
		}
		return true;
	}
}
