/*
 * Rapid Beans Framework: EditorPropertyList2Swing.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/30/2006
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyChoice;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.event.PropertyChangeEvent;
import org.rapidbeans.core.event.PropertyChangeEventType;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypePropertyChoice;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.datasource.event.AddedEvent;
import org.rapidbeans.datasource.event.ChangedEvent;
import org.rapidbeans.datasource.event.RemovedEvent;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.EditorBeanListener;
import org.rapidbeans.presentation.ThreadLocalEventLock;

/**
 * the bean editor GUI for
 * big single or multiple RapidEnum or Collection choices.
 * 
 * @author Martin Bluemel
 */
public class EditorPropertyList2Swing extends EditorPropertySwing
		implements EditorBeanListener {

	/**
	 * Provide only valid association partners in the Out list.
	 */
	private boolean provideOnlyValidInOut = false;

	/**
	 * the list frame.
	 */
	private JDialog listWindow = new JDialog();

	/**
	 * the list panel.
	 */
	private JPanel listPanel = new JPanel();

	/**
	 * the list panel's layout.
	 */
	private LayoutManager listPanelLayout = new GridBagLayout();

	/**
	 * the tree view's scroll pane.
	 */
	private JScrollPane scrollPaneIn = new JScrollPane();

	/**
	 * the label for the chosen.
	 */
	private JLabel labelIn = new JLabel();

	/**
	 * the list.
	 */
	private JList listIn = new JList();

	/**
	 * the tree view's scroll pane.
	 */
	private JScrollPane scrollPaneOut = new JScrollPane();

	/**
	 * the label for the choice.
	 */
	private JLabel labelOut = new JLabel();

	/**
	 * the list.
	 */
	private JList listOut = new JList();

	/**
	 * the arrow buttons panel.
	 */
	private JPanel arrowButtonsPanel = new JPanel();

	/**
	 * the OK button.
	 */
	private JButton buttonOk = new JButton();

	/**
	 * the button panel.
	 */
	private JPanel panelButtons = new JPanel();

	/**
	 * the button panel's layout.
	 */
	private LayoutManager buttonsPanelLayout = new GridBagLayout();

	/**
	 * the arrow button panel's layout.
	 */
	private LayoutManager arrowButtonsPanelLayout = new GridBagLayout();

	/**
	 * the add button.
	 */
	private JButton buttonAdd = new JButton();

	/**
	 * the remove button.
	 */
	private JButton buttonRemove = new JButton();

	/**
	 * @return the editor's widget
	 */
	public Object getWidget() {
		return this.listWindow;
	}

	/**
	 * @return the editor's left list widget
	 */
	public JList getWidgetListIn() {
		return this.listIn;
	}

	/**
	 * @return the editor's right list widget
	 */
	public JList getWidgetListOut() {
		return this.listOut;
	}

	/**
	 * the parent editor.
	 */
	private EditorPropertyListSwing parentEditor = null;

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
	 * @param ed
	 *            the parent editor
	 * @param provideOnlyValidInOut
	 *            switch to provide only
	 *            valid values in the "out" list
	 */
	public EditorPropertyList2Swing(final Application client,
			final EditorBean bizBeanEditor,
			final Property prop, final Property propBak,
			final EditorPropertyListSwing ed,
			final boolean provideOnlyValidInOut) {
		super(client, bizBeanEditor, prop, propBak);
		super.initColors();
		if (this.getProperty().getType().getMandatory()) {
			this.listPanel.setBackground(COLOR_NORMAL);
		}
		this.provideOnlyValidInOut = provideOnlyValidInOut;
		this.parentEditor = ed;
		final RapidBeansLocale locale = client.getCurrentLocale();
		this.labelIn.setText(locale.getStringGui("commongui.text.chosen"));
		this.labelIn.setHorizontalAlignment(JLabel.CENTER);
		this.labelOut.setText(locale.getStringGui("commongui.text.choice"));
		this.labelOut.setHorizontalAlignment(JLabel.CENTER);
		this.listWindow.setTitle(this.getProperty().getBean().toStringGuiType(
				bizBeanEditor.getLocale()) + ": "
				+ this.getProperty().getBean().toStringGui(bizBeanEditor.getLocale())
				+ ", " + this.getProperty().getNameGui(bizBeanEditor.getLocale()));
		this.listWindow.setSize(600, 300);
		this.listWindow.addWindowListener(new WindowAdapter() {
			public void windowClosed(final WindowEvent e) {
				handleActionWindowClosed();
			}
		});
		this.listPanel.setLayout(this.listPanelLayout);
		if (prop instanceof PropertyChoice) {
			this.listIn.setModel(new ModelListChoice((PropertyChoice) prop));
			this.listIn.setCellRenderer(new RendererListEnum(
					client.getCurrentLocale(), this));
		} else if (prop instanceof PropertyCollection) {
			this.listIn.setModel(new ModelListCollection((PropertyCollection) prop,
					this.getBeanEditor().getDocumentView().getDocument()));
			this.listIn.setCellRenderer(new RendererListCollection(
					bizBeanEditor.getDocumentView().getDocument(),
					this.getLocale()));
		} else {
			throw new RapidBeansRuntimeException("Class \"" + EditorPropertyList2Swing.class
					+ "\" does not support properties of class \""
					+ prop.getClass().getName() + "\".");
		}
		if (prop instanceof PropertyChoice) {
			this.listOut.setModel(new ModelListChoiceWithout((PropertyChoice) prop,
					(TypePropertyChoice) (this.getProperty().getType())));
			this.listOut.setCellRenderer(new RendererListEnum(
					client.getCurrentLocale(), this));
		} else if (prop instanceof PropertyCollection) {
			this.listOut.setModel(new ModelListCollectionAllWithout(
					(PropertyCollection) this.getProperty(),
					((TypePropertyCollection) (this.getProperty().getType())).getTargetType(),
					this.getBeanEditor().getDocumentView().getDocument(), this.provideOnlyValidInOut));
			this.listOut.setCellRenderer(new RendererListCollection(
					bizBeanEditor.getDocumentView().getDocument(),
					this.getLocale()));
		} else {
			throw new RapidBeansRuntimeException("Class \"" + EditorPropertyList2Swing.class
					+ "\" does not support properties of class \""
					+ prop.getClass().getName() + "\".");
		}
		this.buttonAdd.setIcon(new ImageIcon(Application.class.getResource(
				"pictures/arrowFullRight.gif")));

		this.buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				addSelectedBeans();
			}
		});
		this.buttonRemove.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				removeSelectedBeans();
			}
		});
		this.buttonRemove.setIcon(new ImageIcon(Application.class.getResource(
				"pictures/arrowFullLeft.gif")));
		this.arrowButtonsPanel.setLayout(this.arrowButtonsPanelLayout);
		this.panelButtons.setLayout(buttonsPanelLayout);
		this.buttonOk.setText(client.getCurrentLocale().getStringGui(
				"commongui.text.ok"));
		this.buttonOk.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				handleActionOk();
			}
		});
		this.buttonOk.setEnabled(true);
		this.panelButtons.add(this.buttonOk,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(5, 5, 5, 5), 0, 0));
		this.arrowButtonsPanel.add(this.buttonAdd, new GridBagConstraints(
				0, 0, 1, 1, 0.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.arrowButtonsPanel.add(this.buttonRemove, new GridBagConstraints(
				0, 1, 1, 1, 0.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.scrollPaneIn.getViewport().add(this.listIn);
		this.scrollPaneOut.getViewport().add(this.listOut);
		this.listPanel.add(this.labelOut, new GridBagConstraints(
				0, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.listPanel.add(this.scrollPaneOut, new GridBagConstraints(
				0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.listPanel.add(this.arrowButtonsPanel, new GridBagConstraints(
				1, 0, 1, 2, 0.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
				new Insets(10, 10, 10, 10), 0, 0));
		this.listPanel.add(this.labelIn, new GridBagConstraints(
				2, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.listPanel.add(this.scrollPaneIn, new GridBagConstraints(
				2, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.listWindow.getContentPane().add(this.listPanel, BorderLayout.CENTER);
		this.listWindow.getContentPane().add(this.panelButtons, BorderLayout.SOUTH);
		this.updateUI();
		if (!this.getBeanEditor().getDocumentView().getClient().getTestMode()) {
			this.listWindow.setVisible(true);
			//does not make the dialog modal
			//this.listWindow.setModal(true);
		}
	}

	/**
	 * add selected beans.
	 */
	public void addSelectedBeans() {
		boolean addFailed = false;
		EditorBean ed = null;
		final Collection<?> oldValue = (Collection<?>) this.getProperty().getValue();
		try {
			ed = this.getBeanEditor();
			ThreadLocalEventLock.set(this);
			ed.setModifies(true);
			if (this.getProperty() instanceof PropertyCollection) {
				final PropertyCollection colProp = (PropertyCollection) this.getProperty();
				if (this.listOut.getSelectedIndex() > -1) {
					for (Object o : this.listOut.getSelectedValues()) {
						try {
							colProp.addLink((RapidBean) o, true, false, true);
						} catch (ValidationException e) {
							this.indicateError(e);
							break;
						}
					}
				}
			} else if (this.getProperty() instanceof PropertyChoice) {
				final PropertyChoice chcProp = (PropertyChoice) this.getProperty();
				if (this.listOut.getSelectedIndex() > -1) {
					try {
						final ArrayList<RapidEnum> newChoice = new ArrayList<RapidEnum>();
						if (chcProp.getValue() != null) {
							for (Object o1 : chcProp.getValue()) {
								newChoice.add((RapidEnum) o1);
							}
						}
						for (final Object o : this.listOut.getSelectedValues()) {
							if (newChoice.contains(o)) {
								throw new RapidBeansRuntimeException("Assertion failed:"
										+ " new choice unexpectedly already contains the enum to add.");
							}
							newChoice.add((RapidEnum) o);
						}
						chcProp.setValue(newChoice);
					} catch (ValidationException e) {
						this.indicateError(e);
					}
				}
			} else {
				throw new RapidBeansRuntimeException("Unexpected property class \""
						+ this.getProperty().getClass().getName() + "\".");
			}
			try {
				ed.addBeanIfNew(true);
			} catch (ValidationException e) {
				assert (ed.getBean().getContainer() == null);
				addFailed = true;
				this.indicateError(e);
				if (this.getProperty() instanceof PropertyCollection) {
					final PropertyCollection colProp = (PropertyCollection) this.getProperty();
					for (Object o : this.listOut.getSelectedValues()) {
						if (((Collection<?>) colProp.getValue()).contains((RapidBean) o)) {
							colProp.removeLink((RapidBean) o, false, false, true);
						}
					}
					switch (this.parentEditor.getNullBehaviour()) {
					case always_null:
						if (((Collection<?>) colProp.getValue()).size() == 0) {
							colProp.setValue(null);
						}
						break;
					default:
						break;
					}
				} else if (this.getProperty() instanceof PropertyChoice) {
					final PropertyChoice chcProp = (PropertyChoice) this.getProperty();
					chcProp.setValue(oldValue);
					switch (this.parentEditor.getNullBehaviour()) {
					case always_null:
						if (chcProp.getValue().size() == 0) {
							chcProp.setValue(null);
						}
						break;
					default:
						break;
					}
				}
			}
			if (!addFailed) {
				final PropertyChangeEvent[] proparray = {
						new PropertyChangeEvent(this.getProperty(),
								oldValue, this.getProperty().getValue(),
								PropertyChangeEventType.set, null)
				};
				this.parentEditor.beanChanged(new ChangedEvent(ed.getBean(), proparray));
				if (!ed.isInNewMode()) {
					this.parentEditor.fireInputFieldChanged();
				}
			}
		} finally {
			ed.removeBeanIfNew(false);
			ed.setModifies(false);
			ThreadLocalEventLock.release();
		}
	}

	/**
	 * remove selected beans.
	 */
	public void removeSelectedBeans() {
		EditorBean ed = null;
		final boolean docChangedBefore = this.parentEditor.getBeanEditor().getDocumentView().getDocument().getChanged();
		try {
			ed = this.getBeanEditor();
			ed.setModifies(true);
			ThreadLocalEventLock.set(this);
			ed.addBeanIfNew(true);
			final Collection<?> oldValue = (Collection<?>) this.getProperty().getValue();
			if (this.getProperty() instanceof PropertyCollection) {
				if (this.listIn.getSelectedIndex() > -1) {
					for (Object o : this.listIn.getSelectedValues()) {
						try {
							((PropertyCollection) this.getProperty()).removeLink((RapidBean) o);
						} catch (ValidationException e) {
							this.indicateError(e);
							break;
						}
					}
				}
			} else if (this.getProperty() instanceof PropertyChoice) {
				if (this.listIn.getSelectedIndex() > -1) {
					final ArrayList<RapidEnum> newValue = new ArrayList<RapidEnum>();
					for (Object o : oldValue) {
						newValue.add((RapidEnum) o);
					}
					boolean changed = false;
					for (Object o : this.listIn.getSelectedValues()) {
						if (newValue.remove((RapidEnum) o)) {
							changed = true;
						}
					}
					if (changed) {
						this.getProperty().setValue(newValue);
					}
				}
			} else {
				throw new RapidBeansRuntimeException("Unexpected property class \""
						+ this.getProperty().getClass().getName() + "\".");
			}
			final PropertyChangeEvent[] proparray = {
					new PropertyChangeEvent(this.getProperty(),
							oldValue, this.getProperty().getValue(),
							PropertyChangeEventType.set, null)
			};
			this.parentEditor.beanChanged(new ChangedEvent(ed.getBean(), proparray));
			if (!ed.isInNewMode()) {
				this.parentEditor.fireInputFieldChanged();
			}
		} finally {
			ed.removeBeanIfNew(docChangedBefore);
			ThreadLocalEventLock.release();
			ed.setModifies(false);
		}
	}

	/**
	 * indicate an error during associating beans
	 * through this dialog.
	 * 
	 * @param e
	 *            the validation exception to indicate
	 */
	private void indicateError(final ValidationException e) {
		final Application client = ApplicationManager.getApplication();
		if (!client.getTestMode()) {
			client.playSoundError();
			final RapidBeansLocale locale = this.getLocale();
			final String locMessagePre = locale.getStringGui("messagedialog.input.field");
			final String locPropname = this.getProperty().getNameGui(locale);
			final String locTitle = locale.getStringGui("messagedialog.title.input.wrong");
			final String locMessage = e.getLocalizedMessage(locale);
			client.messageError(locMessagePre + " \"" + locPropname + "\":\n"
					+ locMessage, locTitle);
			this.parentEditor.setFocus();
			this.setFocus();
		}
	}

	/**
	 * updates the check box according to the boolean presented.
	 */
	public void updateUI() {
		if (this.listIn.getModel() instanceof ModelListCollection) {
			((ModelListCollection) this.listIn.getModel()).fireColPropChanged(
					(PropertyCollection) this.getProperty());
		} else if (this.listIn.getModel() instanceof ModelListChoice) {
			((ModelListChoice) this.listIn.getModel()).fireChoicePropChanged(
					(PropertyChoice) this.getProperty());
		} else {
			throw new RapidBeansRuntimeException("Unknown list model class \""
					+ this.listIn.getModel().getClass().getName() + "\"");
		}
		this.listIn.repaint();

		if (this.listOut.getModel() instanceof ModelListCollectionAllWithout) {
			((ModelListCollectionAllWithout) this.listOut.getModel()).fireColPropChanged(
					(PropertyCollection) this.getProperty());
		} else if (this.listOut.getModel() instanceof ModelListChoiceWithout) {
			((ModelListChoiceWithout) this.listOut.getModel()).fireChoicePropChanged(
					(PropertyChoice) this.getProperty());
		} else {
			throw new RapidBeansRuntimeException("Unknown list model class \""
					+ this.listOut.getModel().getClass().getName() + "\"");
		}
		this.listOut.repaint();
	}

	/**
	 * Dummy implementation (not used).
	 * Data binding with collections should to the job.
	 * 
	 * @return the selected CheckBoxe's names
	 */
	public Object getInputFieldValue() {
		throw new RapidBeansRuntimeException("This method must not be called!");
	}

	/**
	 * Dummy implementation (not used)
	 * 
	 * @return the input field value as string.
	 */
	public String getInputFieldValueString() {
		throw new RapidBeansRuntimeException("This method must not be called!");
	}

	/**
	 * handler for added bean.
	 * 
	 * @param e
	 *            the added event
	 */
	public void beanAdded(final AddedEvent e) {
		if (this.getProperty().getValue() != null) {
			((ModelListCollectionAllWithout) this.listOut.getModel()).fireBeanAdded(e.getBean());
		}
	}

	/**
	 * handler for removed bean.
	 * 
	 * @param e
	 *            the removed event
	 */
	public void beanRemoved(final RemovedEvent e) {
		if (this.getProperty().getValue() != null) {
			((ModelListCollection) this.listIn.getModel()).fireBeanRemoved(e.getBean());
			((ModelListCollectionAllWithout) this.listOut.getModel()).fireBeanRemoved(e.getBean());
			//((PropertyCollection) this.getPropertyBak()).removeLink(e.getBean(), false, true, false);
		}
	}

	/**
	 * ovrerrides the EditorProperty method and adds a repaint
	 * of the lists.
	 * 
	 * bean changed event.
	 * 
	 * @param e
	 *            changed event
	 */
	public void beanChanged(final ChangedEvent e) {
		boolean interestedForEvent = false;
		for (PropertyChangeEvent propEv : e.getPropertyEvents()) {
			final Property prop = propEv.getProperty();
			if (prop == this.getProperty()) {
				interestedForEvent = true;
				break;
			}
		}
		if (!interestedForEvent) {
			return;
		}
		this.updateUI();
	}

	/**
	 * release the model if the bean editor is closed.
	 * 
	 * @param editor
	 *            the bean editor
	 */
	public void editorClosed(final EditorBean editor) {
		//        final Object model = this.listLeft.getModel();
		//        if (model instanceof ModelListCollection) {
		//                ((ModelListCollection) this.listLeft.getModel()).release();
		//        }
	}

	/**
	 * action handler for the OK button.
	 */
	public void handleActionOk() {
		listWindow.dispose();
	}

	/**
	 * action handler for the window closed event.
	 * Equivalent to OK.
	 */
	public void handleActionWindowClosed() {
		try {
			closeInternal();
		} catch (ValidationException e) {
			// intentionally do nothing
		}
	}

	/**
	 * close the editor
	 */
	private void closeInternal() {
		try {
			ThreadLocalEventLock.set(this);
			//            this.parentEditor.getBeanEditor().validateAndUpdateButtons(this.parentEditor);
			this.parentEditor.fireInputFieldChanged();
		} finally {
			try {
				parentEditor.resetListEditor();
				//                parentEditor.updateUI();
			} finally {
				ThreadLocalEventLock.release();
			}
		}
	}
}
