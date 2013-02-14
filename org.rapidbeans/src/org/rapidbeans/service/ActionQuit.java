/*
 * Rapid Beans Framework: ActionQuit.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/31/2005
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

import org.rapidbeans.presentation.ApplicationManager;

/**
 * @author Martin Bluemel
 */
public class ActionQuit extends Action {

	/**
	 * cancel flag.
	 */
	private boolean cancelled = false;

	/**
	 * @return cancel flag
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * default constructor.
	 */
	public ActionQuit() {
		super(ActionQuit.class.getName());
	}

	/**
	 * implementation of the execute method.
	 */
	public final void execute() {
		this.cancelled = ApplicationManager.getApplication().end();
	}
}
