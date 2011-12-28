/*
 * Rapid Beans Framework: MessageDialog.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 12/11/2005
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

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.presentation.config.ApplicationGuiType;
import org.rapidbeans.presentation.swing.MessageDialogSwing;


/**
 * Common interface for Message Dialogs.
 *
 * @author Martin Bluemel
 */
public abstract class MessageDialog {

    /**
     * factory method for Message Dialogs.
     * @param guitype { 'swing' , 'eclipsercp'}
     * @return the new instance
     */
    public static MessageDialog createInstance(final ApplicationGuiType guitype) {
        MessageDialog messageDialog = null;
        switch (guitype) {
        case swing:
            messageDialog = new MessageDialogSwing();
            break;
        default:
            throw new RapidBeansRuntimeException("no Message Dialog for GUI type \""
                    + guitype.name() + "\"");
        }
        return messageDialog;
    }

    /**
     * Shows a dialog with an informational message.
     * @param message the message to present on the GUI
     * @param title the window title
     */
    public abstract void messageInfo(final String message, final String title);

    /**
     * Shows a dialog with an informational message.
     * @param message the message to present on the GUI
     * @param title the window title
     */
    public abstract void messageError(final String message, final String title);

    /**
     * Shows a dialog with a yes no question message.
     * @param message the message to present on the GUI
     * @param title the window title
     * @return true if yes, false if no
     */
    public abstract boolean messageYesNo(final String message, final String title);

    /**
     * Shows a dialog with a yes no question message.
     * @param message the message to present on the GUI
     * @param title the window title
     * @return yes, no, cancel according to the user's response
     */
    public abstract MessageDialogResponse messageYesNoCancel(final String message, final String title);
}
