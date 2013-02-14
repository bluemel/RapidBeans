/*
 * Rapid Beans Framework: EnablerDocumentSave.java
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

package org.rapidbeans.presentation.enabler;

import org.rapidbeans.datasource.Document;

/**
 * enables / disable the save document menu item.
 * 
 * @author Martin Bluemel
 */
public class EnablerDocumentSave extends Enabler {

	public EnablerDocumentSave() {
		super(EnablerDocumentSave.class.getName());
	}

	/**
	 * the execute method of every Action.
	 * 
	 * @return if the menu is enable or not.
	 */
	public boolean getEnabled() {
		Document doc = this.getClient().getActiveDocument();
		if (doc == null) {
			return false;
		} else if (doc.getUrl() == null) {
			return false;
		} else {
			return doc.getChanged();
		}
	}
}
