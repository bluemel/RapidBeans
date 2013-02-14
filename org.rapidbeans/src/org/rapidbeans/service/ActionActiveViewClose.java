/*
 * Rapid Beans Framework: ActionActiveViewClose.java
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

package org.rapidbeans.service;

import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.View;

/**
 * Action to drive closing a document.
 * 
 * @author Martin Bluemel
 */
public class ActionActiveViewClose extends Action {

	/**
	 * default constructor.
	 */
	public ActionActiveViewClose() {
		super(ActionActiveViewClose.class.getName());
	}

	/**
	 * implementation of the execute method.
	 */
	public final void execute() {
		final Application client = ApplicationManager.getApplication();
		final View activeView = client.getActiveView();
		if (activeView != null) {
			activeView.close();
		}
	}
}
