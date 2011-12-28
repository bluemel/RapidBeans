/*
 * Rapid Beans Framework: Footer.java
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
import org.rapidbeans.presentation.config.ConfigFooter;
import org.rapidbeans.presentation.config.ConfigMainWindow;
import org.rapidbeans.presentation.swing.FooterSwing;
import org.rapidbeans.service.Action;


/**
 * MainWindow.
 */
public abstract class Footer {

    private FooterPresentationMode presentationMode = FooterPresentationMode.onmessagespecific;

    public abstract void setVisible(boolean visible);

    /**
     * @return the presentationMode
     */
    public FooterPresentationMode getPresentationMode() {
        return presentationMode;
    }

    /**
     * create a MainWindow of a special type out of a configuration.
     *
     * @param client the parent client
     * @param mainWindowConfig the configuration
     *
     * @return the instance
     */
    public static Footer createInstance(final Application client,
            final ConfigMainWindow mainWindowConfig) {
        Footer footer = null;
        switch (client.getConfiguration().getGuitype()) {
        case swing:
            footer = new FooterSwing(mainWindowConfig.getFooter());
            break;
        default:
            throw new RapidBeansRuntimeException("Unknown GUI type \""
                + client.getConfiguration().getGuitype().name() + "\"");
        }
        return footer;
    }

    public Footer(final ConfigFooter footerConfig) {
        if (footerConfig != null) {
            this.presentationMode = footerConfig.getPresentationmode();
        }
    }

    /**
     * @return the widget.
     */
    public abstract Object getWidget();

    /**
     * Clear the message window.
     */
    public abstract void clearMessage();

    /**
     * Show a message.
     *
     * @param message the message to show.
     */
    public abstract void showMessage(String message);

    /**
     * Show a message for the given action.
     *
     * @param action the action for which the message is shown
     */    
    public void showMessage(final Action action) {
        final String message = action.getMessage();
        if (message != null) {
            if (getPresentationMode().ordinal() >
                    FooterPresentationMode.onmessagespecific.ordinal()) {
                setVisible(true);
            }
            showMessage(message);
        }
    }
}
