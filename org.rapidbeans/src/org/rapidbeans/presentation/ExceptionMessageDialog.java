/*
 * Rapid Beans Framework: ExceptionMessageDialog.java
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

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.presentation.swing.ExceptionMessageDialogSwing;


/**
 * MainWindow.
 */
public abstract class ExceptionMessageDialog {

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the throwable
     */
    public Throwable getThrowable() {
        return throwable;
    }

    private String title = null;

    private Throwable throwable = null;

    /**
     * Present an unforeseen exception.
     *
     * @param throwable the Throwable instance
     * @param title the dialog title
     */
    public ExceptionMessageDialog(final Throwable throwable, final String title) {
        this.throwable = throwable;
        this.title = title;
    }

    /**
     * create a Dialog of a special type out of a configuration.
     *
     * @param app the application or client
     * @param throwable the Throwable instance to present
     * @param title the dialog title
     *
     * @return the instance
     */
    public static ExceptionMessageDialog createInstance(
            final Application app,
            final Throwable throwable,
            final String title) {
        ExceptionMessageDialog dialog = null;
        switch (app.getConfiguration().getGuitype()) {
        case swing:
            dialog = new ExceptionMessageDialogSwing(throwable, title);
            break;
        default:
            throw new RapidBeansRuntimeException("Unknown GUI type \""
                + app.getConfiguration().getGuitype().name() + "\"");
        }
        return dialog;
    }

    /**
     * @return the widget.
     */
    public abstract Object getWidget();

    /**
     * Show the dialog.
     */
    public abstract void show();
}
