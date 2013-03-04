/*
 * Rapid Beans Framework: ActionManager.java
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

import java.util.logging.Logger;

import org.rapidbeans.service.Action;
import org.rapidbeans.service.ActionState;
import org.rapidbeans.service.CursorStyle;

/**
 * Manage Action execution in a client or application.
 * 
 * @author Martin Bluemel
 */
public class ActionManager {

	private static final Logger log = Logger.getLogger(ActionManager.class.getName());

	/**
	 * default constructor.
	 */
	public ActionManager() {
	}

	/**
	 * Execute the given action.
	 * 
	 * @param action
	 *            the action to execute.
	 */
	public synchronized void execute(final Action action) {
		if (action.getBackground()) {
			executeInBackground(action);
		} else {
			executeControlled(action);
		}
	}

	/**
	 * Execute an action in its own thread.
	 * 
	 * @param action
	 *            the action to execute.
	 */
	private void executeInBackground(final Action action) {
		final ActionThread thread = new ActionThread(this, action);
		thread.start();
	}

	/**
	 * Template method for controlled action execution. Controls - the cursor -
	 * the logging - the message display
	 * 
	 * @param action
	 *            the action to execute.
	 */
	protected void executeControlled(final Action action) {
		final Application app = ApplicationManager.getApplication();
		if (app.isUsingAuthorization() && action.getRolesrequired() != null && action.getRolesrequired().size() > 0
				&& (!app.userIsAuthorized(action.getRolesrequired()))) {
			app.messageError(
					app.getCurrentLocale().getStringMessage("authorization.denied.action",
							app.getAuthenticatedUser().getProperty("accountname").toString()), app.getCurrentLocale()
							.getStringMessage("authorization.denied.title"));
			return;
		}
		final Footer footer = app.getMainwindow().getFooter();
		try {
			if (action.getWaitcursor()) {
				app.getMainwindow().setCursor(CursorStyle.wait);
			}
			log.info("ACTION START: " + action.getClassname());
			action.setState(ActionState.running);
			footer.showMessage(action);
			action.execute();
			action.setState(ActionState.success);
			log.info("ACTION END SUCCESS: " + action.getClassname());
			footer.showMessage(action);
		} catch (final Exception e) {
			action.setState(ActionState.failure);
			log.info("ACTION END FAILURE: " + action.getClassname());
			footer.showMessage(action);
			app.messageException(e, action.getMessage());
			e.printStackTrace();
		} finally {
			if (action.getWaitcursor()) {
				app.getMainwindow().setCursor(CursorStyle.defaultcursor);
			}
		}
	}
}
