/*
 * Rapid Beans Framework: ActionHandlerActionListener.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 12/09/2005
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.service.Action;


/**
 * The central handler entry point for actions in a Swing client / application.
 *
 * @author Martin Bluemel
 */
public class ActionHandlerActionListener implements ActionListener {

    /**
     * the action handler command instance.
     */
    private Action action = null;

    /**
     * constructor.
     * @param argActionPerformedCommand the action handler command instance
     */
    public ActionHandlerActionListener(final Action argActionPerformedCommand) {
        this.action = argActionPerformedCommand;
    }

    /**
     * implementation of actionPerformed().
     * @param e the action event
     * @actionEvent the Java AWT Action Event
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public final void actionPerformed(final ActionEvent e) {
        ApplicationManager.getApplication().getActionManager().execute(this.action);
    }
}
