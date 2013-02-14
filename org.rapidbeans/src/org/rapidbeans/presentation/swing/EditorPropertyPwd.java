/*
 * Rapid Beans Framework: EditorPropertyPwd.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 12/22/2006
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.rapidbeans.core.basic.GenericBean;
import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyString;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.DialogPwdChange;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.config.ConfigPropEditorBean;
import org.rapidbeans.security.User;

/**
 * the bean editor GUI.
 * 
 * @author Martin Bluemel
 */
public class EditorPropertyPwd extends EditorPropertySwing {

	/**
	 * the state label
	 */
	private JLabel state = new JLabel();

	/**
	 * the text field.
	 */
	private JButton button = new JButton("+");

	/**
	 * the text field.
	 */
	private JButton buttonReset = new JButton("-");

	/**
	 * the text field.
	 */
	private JPanel panel = new JPanel();

	/**
	 * the layout manager.
	 */
	private LayoutManager layout = new GridBagLayout();

	/**
	 * @return the editor's widget
	 */
	public Object getWidget() {
		return this.panel;
	}

	/**
	 * constructor.
	 * 
	 * @param prop
	 *            the bean property to edit
	 * @param propBak
	 *            the bean property backup
	 * @param bizBeanEditor
	 *            the parent bean editor
	 * @param client
	 *            the client
	 */
	public EditorPropertyPwd(final Application client,
			final EditorBean bizBeanEditor,
			final Property prop, final Property propBak) {
		super(client, bizBeanEditor, prop, propBak);
		if (!(prop instanceof PropertyString)) {
			throw new RapidBeansRuntimeException("invalid property for pwd editor");
		}
		super.initColors();
		this.button.setText(client.getCurrentLocale().getStringGui(
				"editor.org.rapidbeans.security.user.pwd.button.set"));
		this.button.addActionListener(new ActionListener() {
			/**
			 * @param e
			 *            the event
			 */
			public void actionPerformed(final ActionEvent e) {
				pwdSet();
			}
		});
		this.buttonReset.setText(client.getCurrentLocale().getStringGui(
				"editor.org.rapidbeans.security.user.pwd.button.reset"));
		this.buttonReset.addActionListener(new ActionListener() {
			/**
			 * @param e
			 *            the event
			 */
			public void actionPerformed(final ActionEvent e) {
				pwdReset();
			}
		});
		this.panel.setLayout(this.layout);
		this.panel.add(this.state, new GridBagConstraints(
				0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		this.panel.add(this.button, new GridBagConstraints(
				1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE,
				new Insets(0, 5, 0, 0), 0, 0));
		this.panel.add(this.buttonReset, new GridBagConstraints(
				2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE,
				new Insets(0, 5, 0, 0), 0, 0));
		final ConfigPropEditorBean cfg = getConfig();
		if (prop.getReadonly()
				|| (cfg != null && !cfg.getEnabled())) {
			this.panel.setEnabled(false);
		}
		this.updateUI();
	}

	/**
	 * update the string presented in the editor.
	 */
	public void updateUI() {
		try {
			final Application client = ApplicationManager.getApplication();
			final RapidBeansLocale loc = client.getCurrentLocale();
			this.setUIEventLock();
			if (this.getProperty().getValue() == null) {
				this.state.setText(loc.getStringGui("editor.org.rapidbeans.security.user.pwd.state.unset"));
				this.button.setText(loc.getStringGui("editor.org.rapidbeans.security.user.pwd.button.set"));
				this.buttonReset.setEnabled(false);
			} else {
				this.state.setText(loc.getStringGui("editor.org.rapidbeans.security.user.pwd.state.set"));
				this.button.setText(loc.getStringGui("editor.org.rapidbeans.security.user.pwd.button.change"));
				this.buttonReset.setEnabled(true);
			}
		} finally {
			this.releaseUIEventLock();
		}
	}

	/**
	 * @return the Text field's content
	 */
	public Object getInputFieldValue() {
		return this.getProperty().getValue();
	}

	/**
	 * @return the input field value as string.
	 */
	public String getInputFieldValueString() {
		return (String) this.getProperty().getValue();
	}

	/**
	 * set the user's pwd.
	 */
	private void pwdSet() {
		if (DialogPwdChange.start(this)) {
			fireInputFieldChanged();
		}
	}

	/**
	 * reset the user's pwd.
	 */
	private void pwdReset() {
		try {
			getBeanEditor().setModifies(true);
			if (getProperty().getBean() instanceof User) {
				((User) getProperty().getBean()).resetPwd();
			} else {
				User.resetPwd((GenericBean) getProperty().getBean());
			}
		} finally {
			getBeanEditor().setModifies(false);
		}
		fireInputFieldChanged();
	}

	/**
	 * @return the buttonReset
	 */
	public JButton getButtonReset() {
		return buttonReset;
	}
}
