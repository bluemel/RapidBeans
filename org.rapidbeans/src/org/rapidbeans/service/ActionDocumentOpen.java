/*
 * Rapid Beans Framework: ActionDocumentOpen.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/01/2007
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

package org.rapidbeans.service;

import org.rapidbeans.presentation.DocumentController;

/**
 * Action to drive loading a document.
 * 
 * @author Martin Bluemel
 */
public class ActionDocumentOpen extends Action {

	/**
	 * default constructor.
	 */
	public ActionDocumentOpen() {
		super(ActionDocumentOpen.class.getName());
	}

	/**
	 * load a bean document from file.
	 */
	public void execute() {
		DocumentController.open(this.getArgumentValue("docconfname", null),
				this.getArgumentValue("viewconfname", null));
	}
}
