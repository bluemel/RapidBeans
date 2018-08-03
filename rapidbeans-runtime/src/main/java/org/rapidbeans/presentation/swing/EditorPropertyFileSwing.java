/*
 * Rapid Beans Framework: EditorPropertyFileSwing.java
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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyFile;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypePropertyFile;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.config.ConfigPropEditorBean;

/**
 * the bean editor GUI.
 * 
 * @author Martin Bluemel
 */
public class EditorPropertyFileSwing extends EditorPropertySwing {

	/**
	 * the text field.
	 */
	private JTextField text = new JTextField();

	/**
	 * @return the editor's text widget
	 */
	public final Object getTextWidget() {
		return this.text;
	}

	/**
	 * the text field.
	 */
	private JButton button = new JButton("...");

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
	 * @param prop          the bean property to edit
	 * @param propBak       the bean property backup
	 * @param bizBeanEditor the parent bean editor
	 * @param client        the client
	 */
	public EditorPropertyFileSwing(final Application client, final EditorBean bizBeanEditor, final Property prop,
			final Property propBak) {
		super(client, bizBeanEditor, prop, propBak);
		if (!(prop instanceof PropertyFile)) {
			throw new RapidBeansRuntimeException("invalid propperty for a file editor");
		}
		super.initColors();
		if (prop.getType().isKeyCandidate()) {
			// if the editor is in new mode
			if (this.getBeanEditor().getParentBean() == null) {
				this.text.setEditable(false);
			}
		}

		this.text.addKeyListener(new KeyListener() {
			public void keyTyped(final KeyEvent e) {
			}

			public void keyPressed(final KeyEvent e) {
			}

			public void keyReleased(final KeyEvent e) {
				fireInputFieldChanged();
			}
		});
		this.button.addActionListener(new ActionListener() {

			/**
			 * @param e the event
			 */
			public void actionPerformed(final ActionEvent e) {
				chooseFile();
			}
		});
		this.panel.setLayout(this.layout);
		this.panel.add(this.text, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.panel.add(this.button, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.updateUI();
		final ConfigPropEditorBean cfg = getConfig();
		if (prop.getReadonly() || (cfg != null && !cfg.getEnabled())) {
			this.panel.setEnabled(false);
		}
	}

	/**
	 * update the string presented in the editor.
	 */
	public void updateUI() {
		try {
			this.setUIEventLock();
			this.text.setText((String) this.getProperty().toString());
		} finally {
			this.releaseUIEventLock();
		}
	}

	/**
	 * @return the Text field's content
	 */
	public Object getInputFieldValue() {
		String ifValue = this.text.getText();
		if (ifValue.equals("")) {
			ifValue = null;
		}
		return ifValue;
	}

	/**
	 * @return the input field value as string.
	 */
	public String getInputFieldValueString() {
		return this.text.getText();
	}

	/**
	 * validate an input field.
	 * 
	 * @return if the string in the input field is valid or at least could at least
	 *         get after appending additional characters.
	 * 
	 * @param ex the validation exception
	 */
	protected boolean hasPotentiallyValidInputField(final ValidationException ex) {
		if (ex.getSignature().startsWith("invalid.prop.integer")) {
			if (ex.getSignature().endsWith("lower")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * open a file chooser dialog and choose the file.
	 */
	private void chooseFile() {
		final JFileChooser chooser = new JFileChooser();
		final Application client = ApplicationManager.getApplication();
		File chooserDir = null;
		if (this.getProperty().getValue() == null) {
			// take the directory of you latest choice
			if (client != null) {
				chooserDir = client.getSettings().getBasic().getFolderfiles();
			}
		} else {
			// take the parent directory of the file chosen lately for this
			// property
			chooserDir = (File) this.getProperty().getValue();
			chooserDir = chooserDir.getParentFile();
		}

		if (chooserDir != null) {
			chooser.setCurrentDirectory(chooserDir);
			if (client != null) {
				client.getSettings().getBasic().setFolderfiles(chooserDir);
			}
		}

		final TypePropertyFile type = (TypePropertyFile) this.getProperty().getType();
		switch (type.getFiletype()) {
		case directory:
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			break;
		case file:
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			break;
		default:
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			break;
		}

		if (type.getSuffix() != null) {
			ExampleFileFilter filter = new ExampleFileFilter();
			filter.addExtension(type.getSuffix());
			filter.setDescription(this.getProperty().getNameGui(this.getLocale()));
			chooser.setFileFilter(filter);
		}

		chooser.setDialogTitle(this.getLocale().getStringGui("commongui.text.choose") + ": "
				+ this.getProperty().getNameGui(this.getLocale()));
		int returnVal = chooser.showDialog(
				(Component) this.getBeanEditor().getDocumentView().getClient().getMainwindow().getWidget(),
				this.getLocale().getStringGui("commongui.text.choose"));
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			final File file = chooser.getSelectedFile();
			if (file != null && file.exists()) {
				chooserDir = file.getParentFile();
				chooser.setCurrentDirectory(chooserDir);
				if (client != null) {
					client.getSettings().getBasic().setFolderfiles(chooserDir);
				}
			}
			this.text.setText(file.getAbsolutePath());
			this.fireInputFieldChanged();
		}
	}
}
