/*
 * Rapid Beans Framework: ActionUserAuthn.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 11/07/2006
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

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.DocumentView;


/**
 * @author Martin Bluemel
 */
public class ActionUserAuthn extends Action {

    /**
     * implementation of the execute method.
     */
    public final void execute() {
        Application client = ApplicationManager.getApplication();
        try {
            DocumentView settingsView =
                client.openDocumentView(client.getAuthnDoc(),
                        "authn", "standard");
            settingsView.getTreeView().setShowProperties(true);
        } catch (ValidationException e) {
            RapidBeansLocale locale = ApplicationManager.getApplication().getCurrentLocale();
            if (!ApplicationManager.getApplication().getTestMode()) {
                ApplicationManager.getApplication().messageError(
                    e.getLocalizedMessage(locale),
                        locale.getStringGui("messagedialog.title.config.wrong"));
            }
        }
    }
}
