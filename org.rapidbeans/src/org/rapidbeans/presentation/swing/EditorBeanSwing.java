/*
 * Rapid Beans Framework: EditorBeanSwing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/13/2006
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
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.ThreadLocalValidationSettings;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.exception.ValidationReadonlyException;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.DocumentView;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.EditorProperty;

/**
 * the bean editor GUI.
 * 
 * @author Martin Bluemel
 */
// TODO Framework 21) introduce null selection for enum choices and
// 1 : 0..1 collections
public class EditorBeanSwing extends EditorBean {

	/**
	 * the tree view's scroll pane.
	 */
	private JScrollPane scrollPane = new JScrollPane();

	/**
	 * the panel.
	 */
	private JPanel panel = new JPanel();

	/**
	 * the north panel. The construct to stick a further panel into the "NORTH"
	 * section of the main panel with BIrderLayout causes the editor to be
	 * aligned to the upper window boundary.
	 */
	private JPanel panelProps = new JPanel();

	/**
	 * the button panel.
	 */
	private JPanel panelButtons = new JPanel();

	/**
	 * the OK button.
	 */
	private JButton buttonOk = new JButton();

	/**
	 * getter for testing reasons.
	 * 
	 * @return the apply button.
	 */
	protected JButton getButtonOk() {
		return this.buttonOk;
	}

	/**
	 * the OK button.
	 */
	private JButton buttonApply = new JButton();

	/**
	 * getter for testing reasons.
	 * 
	 * @return the apply button.
	 */
	protected JButton getButtonApply() {
		return this.buttonApply;
	}

	/**
	 * the OK button.
	 */
	private JButton buttonClose = new JButton();

	/**
	 * getter for testing reasons.
	 * 
	 * @return the close button.
	 */
	protected JButton getButtonClose() {
		return this.buttonClose;
	}

	/**
	 * @return the view's widget
	 */
	public Object getWidget() {
		return this.panel;
	}

	/**
	 * constructor.
	 * 
	 * @param client
	 *            the client
	 * @param docView
	 *            the parent document view
	 * @param bizBean
	 *            the bean to edit
	 * @param newBeanParentColProp
	 *            a new Bean's parent collection property. Is not null if a new
	 *            Bean is to be created Is null if an existing bean is simply
	 *            edited
	 */
	public EditorBeanSwing(final Application client, final DocumentView docView, final RapidBean bizBean,
			final PropertyCollection newBeanParentColProp) {
		super(client, docView, bizBean, newBeanParentColProp);
		int i = 0;
		this.panel.setLayout(new BorderLayout());
		this.panelProps.setLayout(new GridBagLayout());
		boolean editorWithYExtension = false;
		for (EditorProperty propEditor : this.getPropEditors()) {
			this.panelProps.add((Component) propEditor.getLabelWidget(), new GridBagConstraints(0, i, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			if (propEditor instanceof EditorPropertyListSwing) {
				editorWithYExtension = true;
				this.panelProps.add((Component) propEditor.getWidget(), new GridBagConstraints(1, i++, 1, 1, 1.0, 1.0,
						GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
			} else {
				this.panelProps.add((Component) propEditor.getWidget(), new GridBagConstraints(1, i++, 1, 1, 1.0, 0.0,
						GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			}
		}
		this.panelButtons.setLayout(new GridBagLayout());
		this.buttonOk.setText(client.getCurrentLocale().getStringGui("commongui.text.ok"));
		this.buttonOk.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				handleActionOk();
			}
		});
		this.buttonOk.setEnabled(false);
		this.buttonApply.setText(client.getCurrentLocale().getStringGui("commongui.text.apply"));
		this.buttonApply.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				handleActionApply();
			}
		});
		this.buttonApply.setEnabled(false);
		this.buttonClose.setText(client.getCurrentLocale().getStringGui("commongui.text.close"));
		this.buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				handleActionClose();
			}
		});
		this.panelButtons.add(this.buttonOk, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.panelButtons.add(this.buttonApply, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.panelButtons.add(this.buttonClose, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		final JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		if (editorWithYExtension) {
			p.add(this.panelProps, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		} else {
			p.add(this.panelProps, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		this.scrollPane.getViewport().add(p);
		this.panel.add(this.scrollPane, BorderLayout.CENTER);
		this.panel.add(this.panelButtons, BorderLayout.SOUTH);
		validateAndUpdateButtons();
	}

	/**
	 * validate and update the GUI. - the buttons enabled - the text of the
	 * close button
	 * 
	 * @param propEditor
	 *            the propEditor where the input field has been changed. Give a
	 *            null if an OK, Apply or Close button has been pressed instead.
	 */
	private void validateAndUpdateButtons() {
		try {
			for (EditorProperty ped : this.getPropEditors()) {
				ped.validateInputField();
			}
			updateButtons(true);
		} catch (ValidationReadonlyException e) {
			try {
				ThreadLocalValidationSettings.readonlyOff();
				for (EditorProperty ped : this.getPropEditors()) {
					ped.validateInputField();
				}
				updateButtons(true);
			} catch (ValidationException e1) {
				updateButtons(false);
			} finally {
				ThreadLocalValidationSettings.remove();
			}
		} catch (ValidationException e) {
			updateButtons(false);
		}

	}

	/**
	 * validate and update the GUI. - the buttons enabled - the text of the
	 * close button
	 * 
	 * @param propEditor
	 *            the propEditor where the input field has been changed. Give a
	 *            null if an OK, Apply or Close button has been pressed instead.
	 */
	public void validateAndUpdateButtons(final EditorProperty propEditor) {
		try {
			ThreadLocalValidationSettings.readonlyOff();
			super.validateInputAndUpdateBean(false, false, false, true, propEditor);
			updateButtons(true);
		} catch (ValidationException e) {
			updateButtons(false);
		} finally {
			ThreadLocalValidationSettings.remove();
		}
	}

	/**
	 * update the GUI. - the buttons enabled - the text of the close button
	 * 
	 * @param valid
	 *            if all input values are valid or not
	 */
	private void updateButtons(final boolean valid) {
		if (valid) {
			if (super.isAnyInputFieldChanged()) {
				this.buttonOk.setEnabled(true);
				this.buttonApply.setEnabled(true);
				this.buttonApply.setText(this.getLocale().getStringGui("commongui.text.apply"));
				this.buttonClose.setText(this.getLocale().getStringGui("commongui.text.cancel"));
			} else {
				this.buttonOk.setEnabled(false);
				this.buttonApply.setEnabled(false);
				this.buttonApply.setText(this.getLocale().getStringGui("commongui.text.apply"));
				this.buttonClose.setText(this.getLocale().getStringGui("commongui.text.close"));
			}
		} else { // invalid
			this.buttonOk.setEnabled(false);
			this.buttonApply.setEnabled(true);
			this.buttonApply.setText(this.getLocale().getStringGui("commongui.text.check"));
			if (super.isAnyInputFieldChanged()) {
				this.buttonClose.setText(this.getLocale().getStringGui("commongui.text.cancel"));
			} else {
				this.buttonClose.setText(this.getLocale().getStringGui("commongui.text.close"));
			}
		}
		// } else {
		// this.buttonOk.setEnabled(false);
		// this.buttonApply.setEnabled(false);
		// this.buttonApply.setText(this.getLocale().getStringGui(
		// "commongui.text.apply"));
		// this.buttonClose.setText(this.getLocale().getStringGui(
		// "commongui.text.close"));
		// }
	}

	/**
	 * action handler for OK button.
	 */
	public void handleActionOk() {
		super.handleActionOk();
	}

	/**
	 * action handler for Appply button.
	 */
	public void handleActionApply() {
		super.handleActionApply();
	}

	/**
	 * for white box testing.
	 * 
	 * @return a HashMap with button wigets. The keys are the button names ok,
	 *         apply and close
	 */
	public HashMap<String, Object> getButtonWidgets() {
		HashMap<String, Object> buttons = new HashMap<String, Object>();
		buttons.put("ok", this.buttonOk);
		buttons.put("apply", this.buttonApply);
		buttons.put("close", this.buttonClose);
		return buttons;
	}

	/**
	 * @return the panelButtons
	 */
	public JPanel getPanelButtons() {
		return panelButtons;
	}

	/**
	 * @return the panelProps
	 */
	public JPanel getPanelProps() {
		return panelProps;
	}

	@Override
	public void rotateFocus(final Property property, final int direction) {
		EditorProperty nextEditor = getNextEditor(property);
		if (direction == EditorBean.DIRECTION_UP) {
			nextEditor = getPreviousEditor(property);
		} else {
			nextEditor = getNextEditor(property);
		}
		((JComponent) nextEditor.getWidget()).requestFocus();
	}
}
