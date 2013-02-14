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

package org.rapidbeans.presentation.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.rapidbeans.presentation.Footer;
import org.rapidbeans.presentation.FooterPresentationMode;
import org.rapidbeans.presentation.config.ConfigFooter;

/**
 * MainWindow.
 */
public class FooterSwing extends Footer {

	private JPanel panel = new JPanel();

	private JTextField messageField = new JTextField();

	public FooterSwing(final ConfigFooter footerConfig) {
		super(footerConfig);
		this.panel.setLayout(new BorderLayout());
		this.panel.add(this.messageField, BorderLayout.CENTER);
	}

	/**
	 * @return the panel widget
	 */
	@Override
	public Object getWidget() {
		return this.panel;
	}

	/**
	 * Show a message.
	 * 
	 * @param message
	 *            the message to show.
	 */
	@Override
	public void showMessage(final String message) {
		this.messageField.setText(message);
	}

	/**
	 * Clear the message window.
	 */
	@Override
	public void clearMessage() {
		if (this.getPresentationMode().ordinal() < FooterPresentationMode.always.ordinal()) {
			this.setVisible(false);
		}
		this.messageField.setText("");
	}

	@Override
	public void setVisible(final boolean visible) {
		this.panel.setVisible(visible);
	}
}
