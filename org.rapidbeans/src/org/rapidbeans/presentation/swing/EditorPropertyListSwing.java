/*
 * Rapid Beans Framework: EditorPropertyListSwing.java
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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyChoice;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.event.PropertyChangeEvent;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.datasource.event.AddedEvent;
import org.rapidbeans.datasource.event.ChangedEvent;
import org.rapidbeans.datasource.event.RemovedEvent;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.EditorBeanListener;
import org.rapidbeans.presentation.config.ConfigPropEditorBean;

/**
 * the bean editor GUI for
 * big single or multiple RapidEnum or Collection choices.
 *
 * @author Martin Bluemel
 */
public class EditorPropertyListSwing extends EditorPropertySwing
        implements EditorBeanListener {

    /**
     * Provide only valid association partners in the Out list.
     */
    private boolean provideOnlyValidInOut = false;

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
    private JScrollPane scrollPane = new JScrollPane();

    /**
     * the list.
     */
    private JList list = new JList();

    /**
     * the edit list button.
     */
    private JButton editButton = new JButton("...");

    /**
     * @return the editor's list panel widget
     */
    public Object getWidget() {
        return this.listPanel;
    }

    /**
     * @return the editor's list widget
     */
    public JList getWidgetList() {
        return this.list;
    }


    /**
     * Currently reserved for GUI test purposes
     *
     * @return the editor's edit button widget
     */
    protected JButton getWidgetEditButton() {
        return this.editButton;
    }

    /**
     * the real editor to change the collections.
     */
    private EditorPropertyList2Swing listEditor = null;

    /**
     * throw away the list editor instance.
     */
    protected void resetListEditor() {
        this.listEditor = null;
    }

    /**
     * constructor.
     *
     * @param prop the bean property to edit
     * @param propBak the bean property backup
     * @param bizBeanEditor the parent bean editor
     * @param client the client
     */
    public EditorPropertyListSwing(final Application client,
            final EditorBean bizBeanEditor,
            final Property prop, final Property propBak) {
        super(client, bizBeanEditor, prop, propBak);
        if (this.getConfig() != null) {
            final String sProvideOnlyValid = this.getConfig().getArgumentValue("restrictchoicetovalid");
            if (sProvideOnlyValid != null) {
                if (Boolean.parseBoolean(sProvideOnlyValid)) {
                    this.provideOnlyValidInOut = true;
                }
            }
        }
        bizBeanEditor.addEditorListener(this);
        super.initColors();
        if (this.getProperty().getType().getMandatory()) {
            this.listPanel.setBackground(COLOR_MANDATORY);
        }
        this.listPanel.setLayout(this.listPanelLayout);
        if (prop instanceof PropertyChoice) {
            this.list.setModel(new ModelListChoice((PropertyChoice) prop));
            this.list.setCellRenderer(new RendererListEnum(
                client.getCurrentLocale(), this));
        } else if (prop instanceof PropertyCollection) {
            this.list.setModel(new ModelListCollection(
                    (PropertyCollection) this.getProperty(),
                    this.getBeanEditor().getDocumentView().getDocument()));
            this.list.setCellRenderer(new RendererListCollection(
                    bizBeanEditor.getDocumentView().getDocument(),
                    this.getLocale()));
        } else {
            throw new RapidBeansRuntimeException("Class \"" + EditorPropertyListSwing.class
                    + "\" does not support properties of class \""
                    + prop.getClass().getName() + "\".");
        }

        final int elCount = this.list.getModel().getSize();
        int yDim;
        switch (elCount) {
        case 0:
        case 1: yDim = 10;
            break;
        case 2:
            yDim = 40;
            break;
        default:
            yDim = 60;
        }
        this.scrollPane.setPreferredSize(new Dimension(0, yDim));
        this.editButton.addActionListener(new ActionListener() {
            /**
             * pop up the editor.
             */
            public void actionPerformed(final ActionEvent e) {
                openListEditor();
            }
        });

        this.scrollPane.getViewport().add(this.list);
        this.listPanel.add(this.scrollPane, new GridBagConstraints(0, 0, 1, 1,
                1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        this.listPanel.add(this.editButton, new GridBagConstraints(1, 0, 1, 1,
                0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 5, 0, 0), 0, 0));
        this.updateUI();
        final ConfigPropEditorBean cfg = getConfig();
        if (prop.getReadonly()
                || (cfg != null && !cfg.getEnabled())
                || (prop.getType().isKeyCandidate()
                    && (!this.getBeanEditor().isInNewMode()))) {
            this.listPanel.setEnabled(false);
            this.editButton.setEnabled(false);
        }
    }

    /**
     * open a list editor.
     *
     * @return the list editor
     */
    public EditorPropertyList2Swing openListEditor() {
        if (this.listEditor == null) {
            this.listEditor = new EditorPropertyList2Swing(
                this.getBeanEditor().getDocumentView().getClient(),
                this.getBeanEditor(), this.getProperty(),
                this.getPropertyBak(), this,
                this.provideOnlyValidInOut);
        } else {
            JDialog f = (JDialog) this.listEditor.getWidget();
            f.setVisible(true);
        }
        return this.listEditor;
    }

    /**
     * updates the check box according to the boolean presented.
     */
    public void updateUI() {
        try {
            this.setUIEventLock();
            if (this.list.getModel() instanceof ModelListCollection) {
                ((ModelListCollection) this.list.getModel()).fireColPropChanged(
                        (PropertyCollection) this.getProperty());
            } else if (this.list.getModel() instanceof ModelListChoice) {
                ((ModelListChoice) this.list.getModel()).fireChoicePropChanged(
                        (PropertyChoice) this.getProperty());
            } else {
                throw new RapidBeansRuntimeException("Unknown list model class \""
                        + this.list.getModel().getClass().getName() + "\"");
            }
            this.list.repaint();
            if (this.listEditor != null) {
                this.listEditor.updateUI();
            }
        } finally {
            this.releaseUIEventLock();
        }
    }

    /**
     * @return the selected CheckBoxe's names
     */
    public Object getInputFieldValue() {
//        Object value = null;
//        if (this.getProperty() instanceof PropertyChoice) {
//        } else if (this.getProperty() instanceof PropertyCollection) {
//        }
        // Data binding with collections should to the job
        return this.getProperty().getValue();
//        switch (this.getNullBehavour()) {
//        case always_empty:
//            // Data binding with collections should to the job
////            return this.getProperty().getValue();
////            ArrayList<Link> list = new ArrayList<Link>();
////            for (Object o : this.list.getSelectedValues()) {
////                list.add((Link) o);
////            }
////            return list;
////            return new ReadonlyListArray(this.list.getSelectedValues());
//        case always_null:
//            if (this.list.getSelectedValues().length == 0) {
//                return null;
//            } else {
//                return new ReadonlyListArray(this.list.getSelectedValues());
//            }
//        default:
//            return new ReadonlyListArray(this.list.getSelectedValues());
//        }
    }

    /**
     * @return the input field value as string.
     */
    public String getInputFieldValueString() {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (Object selobj : this.list.getSelectedValues()) {
            if (i < 0) {
                sb.append(',');
            }
            if (this.getProperty() instanceof PropertyChoice) {
                sb.append(((RapidEnum) selobj).name());
            } else if (this.getProperty() instanceof PropertyCollection) {
                sb.append(((RapidBean) selobj).getIdString());
            }
            i++;
        }

        return sb.toString();
    }

    /**
     * handler for added bean.
     * @param e the added event
     */
    public void beanAdded(final AddedEvent e) {
        if (this.listEditor != null) {
            this.listEditor.beanAdded(e);
        }
    }

    /**
     * handler for added bean.
     * @param e the removed event
     */
    public void beanRemoved(final RemovedEvent e) {
        super.beanRemoved(e);
        if (this.getProperty().getValue() != null) {
            if (this.list.getModel() instanceof ModelListCollection) {
                ((ModelListCollection) this.list.getModel()).fireBeanRemoved(e.getBean());
            } else if (this.list.getModel() instanceof ModelListChoice) {
                ((ModelListChoice) this.list.getModel()).fireBeanRemoved(e.getBean());
            }
        }
        if (this.listEditor != null) {
            this.listEditor.beanRemoved(e);
        }
    }

    /**
     * ovrerrides the EditorProperty method and adds a repaint
     * of the list.
     *
     * bean changed event.
     * @param e changed event
     */
    public void beanChanged(final ChangedEvent e)  {
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
     * @param editor the bean editor
     */
    public void editorClosed(final EditorBean editor) {
//        if (this.list.getModel() instanceof ModelListCollection) {
//                ((ModelListCollection) this.list.getModel()).release();
//        }
          if (this.listEditor != null) {
              ((JDialog) this.listEditor.getWidget()).dispose();
              this.listEditor = null;
        }
    }
}
