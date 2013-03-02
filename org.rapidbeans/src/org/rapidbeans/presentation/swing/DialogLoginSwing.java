/*
 * Rapid Beans Framework: DialogLoginSwing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/08/2007
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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.DialogLogin;
import org.rapidbeans.presentation.ThreadLocalEventLock;

/**
 * The super class that abstracts from gui implementation.
 * 
 * @author Martin Bluemel
 */
public class DialogLoginSwing extends DialogLogin {

	/**
	 * the dialog widget.
	 */
	private JDialog dialogWidget = new JDialog();

	/**
	 * text field to enter the logname.
	 */
	private JTextField log = new JTextField();

	/**
	 * @return the login name entered
	 */
	protected String getLoginname() {
		return log.getText();
	}

	/**
	 * @param l
	 *            the login name
	 */
	protected void setLoginname(final String l) {
		this.log.setText(l);
	}

	/**
	 * text field to enter the password.
	 */
	private JPasswordField pwd = new JPasswordField();

	/**
	 * Converts the pwd entered into a string and erases the pwd input filed.
	 * 
	 * @return the pwd entered
	 */
	protected String getPwd() {
		final char[] ca = pwd.getPassword();
		final String s = new String(ca);
		return s;
	}

	/**
	 * @param p
	 *            the pwd
	 */
	protected void setPwd(final String p) {
		this.pwd.setText(p);
	}

	/**
	 * check box to indicate that the credential once given should be reused for
	 * the next login.
	 */
	private JCheckBox savecred = new JCheckBox();

	/**
	 * @return if saving the credentials is desired or not.
	 */
	protected boolean getSavecred() {
		return this.savecred.isSelected();
	}

	/**
	 * check box to indicate that the credentials to be saved should be
	 * encrypted.
	 */
	private JCheckBox encryptcred = new JCheckBox();

	/**
	 * @return if encrypting the credentials is desired or not.
	 */
	protected boolean getEncryptcred() {
		return this.encryptcred.isSelected();
	}

	/**
	 * OK button.
	 */
	private JButton buttonOk = new JButton();

	/**
	 * Close button.
	 */
	private JButton buttonCancel = new JButton();

	/**
	 * constructor.
	 * 
	 * @param saveCredent
	 *            if credentials should be saved or not
	 * @param encryptCredent
	 *            if credentials should be encrypted or not
	 */
	public DialogLoginSwing(final boolean saveCredent,
			final boolean encryptCredent) {
		ThreadLocalEventLock.set(null);
		final Application client = ApplicationManager.getApplication();
		try {
			final Image image = new IconManagerSwing()
					.getImage("mainwindow.icon");
			if (image != null) {
				this.dialogWidget.setIconImage(image);
			}
			final RapidBeansLocale loc = client.getCurrentLocale();
			final KeyListener kl = new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					keyTypedOnDialog(e);
				}
			};
			this.log.addKeyListener(kl);
			this.pwd.addKeyListener(kl);
			this.savecred.setSelected(saveCredent);
			this.savecred.addItemListener(new ItemListener() {
				public void itemStateChanged(final ItemEvent e) {
					savecredChanged();
				}
			});
			this.encryptcred.setSelected(encryptCredent);
			this.encryptcred.addItemListener(new ItemListener() {
				public void itemStateChanged(final ItemEvent e) {
					encryptcredChanged();
				}
			});
			this.buttonOk.setText(loc.getStringGui("commongui.text.ok"));
			this.dialogWidget.getRootPane().setDefaultButton(this.buttonOk);
			this.buttonOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					buttonPressedOk();
				}
			});
			this.buttonCancel
					.setText(loc.getStringGui("commongui.text.cancel"));
			this.buttonCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					buttonPressedCancel();
				}
			});
			dialogWidget.getContentPane().setLayout(new BorderLayout());
			dialogWidget.addWindowListener(new WindowAdapter() {
				public void windowClosed(final WindowEvent e) {
					buttonPressedCancel();
				}
			});
			final JPanel tfPanel = new JPanel(new GridBagLayout());
			tfPanel.add(new JLabel(loc.getStringGui("dialog.login.logname")),
					new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 0, 0));
			tfPanel.add(this.log, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
					new Insets(5, 5, 5, 5), 0, 0));
			tfPanel.add(new JLabel(loc.getStringGui("dialog.login.password")),
					new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 0, 0));
			tfPanel.add(this.pwd, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
					new Insets(5, 5, 5, 5), 0, 0));
			tfPanel.add(new JLabel(loc.getStringGui("dialog.login.savecred")),
					new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 0, 0));
			tfPanel.add(this.savecred, new GridBagConstraints(1, 2, 1, 1, 1.0,
					0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
					new Insets(5, 5, 5, 5), 0, 0));
			tfPanel.add(
					new JLabel(loc.getStringGui("dialog.login.encryptcred")),
					new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(5, 5, 5, 5), 0, 0));
			tfPanel.add(this.encryptcred, new GridBagConstraints(1, 3, 1, 1,
					1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
					new Insets(5, 5, 5, 5), 0, 0));
			dialogWidget.getContentPane().add(tfPanel, BorderLayout.CENTER);
			final JPanel btPanel = new JPanel(new GridBagLayout());
			btPanel.add(this.buttonOk, new GridBagConstraints(0, 0, 1, 1, 1.0,
					1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
					new Insets(5, 5, 5, 5), 0, 0));
			btPanel.add(this.buttonCancel, new GridBagConstraints(1, 0, 1, 1,
					1.0, 1.0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			dialogWidget.getContentPane().add(btPanel, BorderLayout.SOUTH);
			this.setTitle(loc.getStringGui("dialog.login.title") + ": "
					+ loc.getStringGui("mainwindow.title"));
			dialogWidget.setTitle(this.getTitle());
			final Dimension screenSize = Toolkit.getDefaultToolkit()
					.getScreenSize();
			dialogWidget.setSize(450, 200);
			dialogWidget.setLocation(
					(screenSize.width - dialogWidget.getWidth()) / 2,
					(screenSize.height - dialogWidget.getHeight()) / 2);
		} finally {
			ThreadLocalEventLock.release();
		}
	}

	/**
	 * Dispose the dialog widget.
	 */
	protected void dispose() {
		this.dialogWidget.dispose();
	}

	/**
	 * this GUI toolkit specific method pops up a login dialog.
	 * 
	 * @return if the dialog has been finished with OK (true) or Cancel (false),
	 *         Closing the dialog is interpreted as Cancel.
	 */
	protected boolean showLogin() {
		this.ok = false;
		dialogWidget.setModal(true);
		dialogWidget.setVisible(true);
		return this.ok;
	}

	/**
	 * OK button pressed flag.
	 */
	private boolean ok = false;

	/**
	 * action handler for the OK button pressed event.
	 */
	private void buttonPressedOk() {
		this.ok = true;
		this.dialogWidget.setVisible(false);
	}

	/**
	 * action handler for the Cancel button pressed event.
	 */
	private void buttonPressedCancel() {
		this.dialogWidget.setVisible(false);
	}

	/**
	 * Action handler for key typed events
	 * 
	 * @param e
	 *            the key event
	 */
	private void keyTypedOnDialog(final KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			this.dialogWidget.setVisible(false);
			break;
		default:
			break;
		}
	}

	/**
	 * Asks the user for confirmation if he / she really wants to save the
	 * creds.
	 */
	private void savecredChanged() {
		if (ThreadLocalEventLock.get()) {
			return;
		}
		if (this.getSavecred() && !this.getEncryptcred()) {
			try {
				ThreadLocalEventLock.set(null);
				this.encryptcred.setSelected(true);
			} finally {
				ThreadLocalEventLock.release();
			}
		}
	}

	/**
	 * Action routine for the encryptcred changed event.
	 */
	private void encryptcredChanged() {
		if (ThreadLocalEventLock.get()) {
			return;
		}
		try {
			ThreadLocalEventLock.set(null);
			if (!getEncryptcred()) {
				final Application client = ApplicationManager.getApplication();
				final RapidBeansLocale loc = client.getCurrentLocale();
				if (!client
						.messageYesNo(
								loc.getStringMessage("login.confirm.savecredwoencryption"),
								loc.getStringMessage("login.confirm.savecredwoencryption.title"))) {
					this.encryptcred.setSelected(true);
				}
			}
		} finally {
			ThreadLocalEventLock.release();
		}
	}
}
