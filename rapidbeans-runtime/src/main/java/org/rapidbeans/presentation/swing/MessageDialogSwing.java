/*
 * Rapid Beans Framework: MessageDialogSwing.java
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

package org.rapidbeans.presentation.swing;

import javax.swing.JOptionPane;

import org.rapidbeans.presentation.MessageDialog;
import org.rapidbeans.presentation.MessageDialogResponse;

/**
 * Message Dialogs for Java Swing (JOptionPane).
 * 
 * @author Martin Bluemel
 */
public class MessageDialogSwing extends MessageDialog {

	/**
	 * @param message the message
	 * @param title   the title
	 * @see org.rapidbeans.presentation.MessageDialog#messageError(java.lang.String)
	 */
	public final void messageError(final String message, final String title) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * @param message the message
	 * @param title   the title
	 * @see org.rapidbeans.presentation.MessageDialog#messageInfo(java.lang.String)
	 */
	public final void messageInfo(final String message, final String title) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @param message the message
	 * @param title   the title
	 * @see org.rapidbeans.presentation.MessageDialog#messageInfo(java.lang.String)
	 * @return true if yes, fals if no
	 */
	public final boolean messageYesNo(final String message, final String title) {
		final int confirm = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
		switch (confirm) {
		case JOptionPane.YES_OPTION:
			return true;
		default:
			return false;
		}
	}

	/**
	 * @param message the message
	 * @param title   the title
	 * @see org.rapidbeans.presentation.MessageDialog#messageInfo(java.lang.String)
	 * @return true if yes, fals if no
	 */
	public final MessageDialogResponse messageYesNoCancel(final String message, final String title) {
		final int confirm = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_CANCEL_OPTION);
		switch (confirm) {
		case JOptionPane.YES_OPTION:
			return MessageDialogResponse.yes;
		case JOptionPane.NO_OPTION:
			return MessageDialogResponse.no;
		default:
			return MessageDialogResponse.cancel;
		}
	}

	/**
	 * @param message the message
	 * @see org.rapidbeans.presentation.MessageDialog#messageError(java.lang.String)
	 */
	public final void messageError(final String message) {
		messageError(message, null);
	}

	/**
	 * @param message the message
	 * @see org.rapidbeans.presentation.MessageDialog#messageInfo(java.lang.String)
	 */
	public final void messageInfo(final String message) {
		messageInfo(message, null);
	}
}
