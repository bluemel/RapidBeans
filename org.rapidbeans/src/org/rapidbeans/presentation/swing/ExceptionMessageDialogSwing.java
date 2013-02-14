/*
 * Rapid Beans Framework: ExceptionMessageDialogSwing.java
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

package org.rapidbeans.presentation.swing;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.ExceptionMessageDialog;

/**
 * MainWindow.
 */
public class ExceptionMessageDialogSwing extends ExceptionMessageDialog {

	private JDialog dialog = new JDialog((JFrame) ApplicationManager.getApplication().getMainwindow().getWidget());

	private JScrollPane scrollPane = new JScrollPane();

	final JTextArea textArea = new JTextArea();

	/**
	 * Present an unforeseen exception.
	 * 
	 * @param throwable
	 *            the Throwable instance
	 * @param title
	 *            the dialog title
	 */
	public ExceptionMessageDialogSwing(final Throwable throwable, final String title) {
		super(throwable, title);
		this.dialog.setTitle(title);
		this.dialog.setSize(600, 400);
		final Dimension screenSize = this.dialog.getToolkit().getScreenSize();
		this.dialog.setLocation((screenSize.width - 600) / 2,
				(screenSize.height - 400) / 2);
		this.scrollPane.getViewport().add(this.textArea);
		this.dialog.getContentPane().add(this.scrollPane);
		this.textArea.setText(StringHelper.toStackTraceString(throwable));
	}

	/**
	 * @return the widget.
	 */
	public Object getWidget() {
		return this.dialog;
	}

	/**
	 * Show the dialog.
	 */
	public void show() {
		this.dialog.setVisible(true);
	}
}
