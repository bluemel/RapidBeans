/*
 * Rapid Beans Framework: DialogPwdChangeSwing.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 11/20/2007
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
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.DialogPwdChange;
import org.rapidbeans.presentation.ThreadLocalEventLock;

/**
 * The CONCRETE class that abstracts from Swing implementation.
 *
 * @author Martin Bluemel
 */
public class DialogPwdChangeSwing extends DialogPwdChange {

    /**
     * the dialog widget.
     */
    private JDialog dialogWidget = new JDialog();

    /**
     * text field to enter the new password first.
     */
    private JPasswordField pwdOld = new JPasswordField();

    /**
     * text field to enter the new password first.
     */
    private JPasswordField pwdNew1 = new JPasswordField();

    /**
     * text field to enter the new password first.
     */
    private JPasswordField pwdNew2 = new JPasswordField();

    /**
     * Converts the pwd entered into a string and erases the
     * pwd input field.
     *
     * @return the pwd entered
     */
    protected String getPwdOld() {
        final char[] ca = pwdOld.getPassword();
        pwdOld.setText("");
        final String s = new String(ca);
        return s;
    }

    /**
     * Converts the pwd entered into a string and erases the
     * pwd input field.
     *
     * @return the pwd entered
     */
    protected String getPwdNew1() {
        final char[] ca = pwdNew1.getPassword();
        pwdNew1.setText("");
        final String s = new String(ca);
        return s;
    }

    /**
     * Converts the pwd entered into a string and erases the
     * pwd input field.
     *
     * @return the pwd entered
     */
    protected String getPwdNew2() {
        final char[] ca = pwdNew2.getPassword();
        pwdNew2.setText("");
        final String s = new String(ca);
        return s;
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
     */
    public DialogPwdChangeSwing(final RapidBean usr) {
        this.setUser(usr);
        try {
            ThreadLocalEventLock.set(null);
            final Application client = ApplicationManager.getApplication();
            final RapidBeansLocale loc = client.getCurrentLocale();
            if (this.getUser().getPropValue("pwd") == null) {
                this.pwdOld.setVisible(false);
            }
            this.buttonOk.setText(loc.getStringGui("commongui.text.ok"));
            this.dialogWidget.getRootPane().setDefaultButton(this.buttonOk);
            this.buttonOk.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    buttonPressedOk();
                }
            });
            this.buttonCancel.setText(loc.getStringGui("commongui.text.cancel"));
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
            tfPanel.add(new JLabel(loc.getStringGui("dialog.pwdchange.pwdold")),
                    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            tfPanel.add(this.pwdOld, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 5, 5), 0, 0));
            tfPanel.add(new JLabel(loc.getStringGui("dialog.pwdchange.pwdnew1")),
                    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            tfPanel.add(this.pwdNew1, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 5, 5), 0, 0));
            tfPanel.add(new JLabel(loc.getStringGui("dialog.pwdchange.pwdnew2")),
                    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            tfPanel.add(this.pwdNew2, new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 5, 5), 0, 0));
            dialogWidget.getContentPane().add(tfPanel, BorderLayout.CENTER);
            final JPanel btPanel = new JPanel(new GridBagLayout());
            btPanel.add(this.buttonOk,
                    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            btPanel.add(this.buttonCancel,
                    new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(5, 5, 5, 5), 0, 0));
            dialogWidget.getContentPane().add(btPanel, BorderLayout.SOUTH);
            this.setTitle(loc.getStringGui("dialog.login.title")
                    + ": " + loc.getStringGui("mainwindow.title"));
            dialogWidget.setTitle(this.getTitle());
            final Dimension screenSize =
                Toolkit.getDefaultToolkit().getScreenSize();
            dialogWidget.setSize(450, 170);
            dialogWidget.setLocation((screenSize.width - dialogWidget.getWidth()) / 2,
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
     * @return if the dialog has been finished with OK (true)
     *         or Cancel (false), Closing the dialog is interpreted
     *         as Cancel.
     */
    protected boolean show() {
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
}
