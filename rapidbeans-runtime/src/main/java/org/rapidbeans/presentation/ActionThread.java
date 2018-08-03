/*
 * Rapid Beans Framework: ActionThread.java
 * 
 * Copyright (C) 2010 Martin Bluemel
 * 
 * Creation Date: 05/09/2010
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

import org.rapidbeans.service.Action;

/**
 * Thread for executing Action.
 * 
 * @author Martin Bluemel
 */
public class ActionThread extends Thread {

	private ActionManager manager = null;

	private Action action = null;

	/**
	 * default constructor.
	 * 
	 * @param manager the action manager
	 * @param action  the action to execute
	 */
	public ActionThread(final ActionManager manager, final Action action) {
		this.manager = manager;
		this.action = action;
	}

	/**
	 * The thread's run method.
	 */
	public void run() {
		this.manager.executeControlled(this.action);
	}
}
