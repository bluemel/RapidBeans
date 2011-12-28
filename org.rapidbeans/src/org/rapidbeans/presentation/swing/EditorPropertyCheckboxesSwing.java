/*
 * Rapid Beans Framework: EditorPropertyCheckboxesSwing.java
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

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.rapidbeans.core.basic.GenericEnum;
import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.PropertyChoice;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeRapidEnum;
import org.rapidbeans.core.type.TypePropertyChoice;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.datasource.event.AddedEvent;
import org.rapidbeans.datasource.event.RemovedEvent;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.config.ConfigPropEditorBean;

/**
 * the bean editor GUI for
 * small multiple RapidEnum or Collection choices.
 *
 * @author Martin Bluemel
 */
public class EditorPropertyCheckboxesSwing extends EditorPropertySwing {

    /**
     * the checkbox panel.
     */
    private JPanel checkboxPanel = new JPanel();

    /**
     * @return the editor's widget
     */
    public Object getWidget() {
        return this.checkboxPanel;
    }

    /**
     * the checkboxes.
     */
    private Hashtable<String,JCheckBox> checkboxes = new Hashtable<String,JCheckBox>();

    /**
     * for testing reasons.
     * @return the checkboxes hashtable.
     */
    protected Hashtable<String,JCheckBox> getCheckboxes() {
        return this.checkboxes;
    }

    /**
     * constructor.
     *
     * @param prop the bean property to edit
     * @param propBak the bean property backup
     * @param bizBeanEditor the parent bean editor
     * @param client the client
     */
    public EditorPropertyCheckboxesSwing(final Application client,
            final EditorBean bizBeanEditor,
            final Property prop, final Property propBak) {
        super(client, bizBeanEditor, prop, propBak);
        super.initColors();
        // since it is impossible to enter a null value if the property
        // mandatory
        if (this.getProperty().getType().getMandatory()) {
            this.checkboxPanel.setBackground(COLOR_MANDATORY);
        }
        this.checkboxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        if (prop instanceof PropertyChoice) {
            final TypePropertyChoice choiceProptype = (TypePropertyChoice) prop.getType();
            JCheckBox curCheckBox;
            for (RapidEnum enumElem : choiceProptype.getEnumType().getElements()) {
                curCheckBox = new JCheckBox(enumElem.name());
                if (this.getProperty().getType().getMandatory()) {
                    curCheckBox.setBackground(COLOR_MANDATORY);
                }
                this.checkboxes.put(enumElem.name(), curCheckBox);
                curCheckBox.addItemListener(new ItemListener(){
                    public void itemStateChanged(final ItemEvent e) {
                        fireInputFieldChanged();
                    }
                });
            }
        } else if (prop instanceof PropertyCollection) {
            updateCheckboxes();
        } else {
            throw new RapidBeansRuntimeException("Class \"" + EditorPropertyCheckboxesSwing.class
                    + "\" does not support properties of class \""
                    + prop.getClass().getName() + "\".");
        }
        this.updateUI();
        final ConfigPropEditorBean cfg = getConfig();
        if (prop.getReadonly()
                || (cfg != null && !cfg.getEnabled())) {
            this.checkboxPanel.setEnabled(false);
        }
    }

    /**
     * update all checkboxes according to the collection.
     */
    private void updateCheckboxes() {
        final Property prop = this.getProperty();
        final EditorBean bizBeanEditor = this.getBeanEditor();
        final TypePropertyCollection colPropType = (TypePropertyCollection) prop.getType();
        final List<RapidBean> allTargetBeans = bizBeanEditor.getDocumentView().getDocument().findBeansByType(
                colPropType.getTargetType().getName());
        JCheckBox curCheckBox;
        for (Component comp : this.checkboxPanel.getComponents()) {
            this.checkboxPanel.remove(comp);
        }
        this.checkboxes.clear();
        for (RapidBean bean : allTargetBeans) {
            curCheckBox = new JCheckBox(bean.getIdString());
            if (this.getProperty().getType().getMandatory()) {
                curCheckBox.setBackground(COLOR_MANDATORY);
            }
            this.checkboxes.put(bean.getIdString(), curCheckBox);
            curCheckBox.addItemListener(new ItemListener(){
                public void itemStateChanged(final ItemEvent e) {
                    fireInputFieldChanged();
                }
            });
            this.checkboxPanel.add(curCheckBox);
        }
    }

    /**
     * updates the check box according to the boolean presented.
     */
    @SuppressWarnings("unchecked")
    public void updateUI() {
        try {
            this.setUIEventLock();
            if (this.getProperty().getValue() == null) {
                for (JCheckBox curCheckBox : this.checkboxes.values()) {
                    curCheckBox.setSelected(false);
                }
            } else {
                ArrayList<String> keys = new ArrayList<String>();
                if (this.getProperty() instanceof PropertyChoice) {
                    for (RapidEnum enumElem : (List<GenericEnum>) this.getProperty().getValue()) {
                        keys.add(enumElem.name());
                    }
                } else if (this.getProperty() instanceof PropertyCollection) {
                    for (Link link : (Collection<Link>) this.getProperty().getValue()) {
                        keys.add(link.getIdString());
                    }
                }
                JCheckBox curCheckBox;
                for (String checkboxKey : this.checkboxes.keySet()) {
                    curCheckBox = this.checkboxes.get(checkboxKey);
                    curCheckBox.setSelected(keys.contains(checkboxKey));
                }
            }
        } finally {
            this.releaseUIEventLock();
        }
    }

    /**
     * @return the selected CheckBoxe's names
     */
    public Object getInputFieldValue() {
        Object value = null;
        if (this.getProperty() instanceof PropertyChoice) {
            ArrayList<RapidEnum> enumList = new ArrayList<RapidEnum>();
            TypeRapidEnum enumtype = ((TypePropertyChoice) this.getProperty().getType()).getEnumType();
            for (JCheckBox curCheckBox : this.checkboxes.values()) {
                if (curCheckBox.isSelected()) {
                    enumList.add(enumtype.elementOf(curCheckBox.getText()));
                }
            }
            value = enumList;
        } else if (this.getProperty() instanceof PropertyCollection) {
            ArrayList<RapidBean> beanList = new ArrayList<RapidBean>();
            Document doc = this.getBeanEditor().getDocumentView().getDocument();
            String targetTypename = ((TypePropertyCollection) this.getProperty().getType()).getTargetType().getName();
            for (JCheckBox curCheckBox : this.checkboxes.values()) {
                if (curCheckBox.isSelected()) {
                    beanList.add(doc.findBean(targetTypename, curCheckBox.getText()));
                }
            }
            value = beanList;
        }

        return value;
    }

    /**
     * @return the input field value as string.
     */
    public String getInputFieldValueString() {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (JCheckBox curCheckBox : this.checkboxes.values()) {
            if (curCheckBox.isSelected()) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(curCheckBox.getText());
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
        if (this.getProperty() instanceof PropertyCollection) {
            this.updateCheckboxes();
            this.updateUI();
            this.checkboxPanel.repaint();
        }
    }

    /**
     * handler for added bean.
     * @param e the removed event
     */
    public void beanRemoved(final RemovedEvent e) {
        if (this.getProperty() instanceof PropertyCollection) {
            this.updateCheckboxes();
            this.updateUI();
            this.checkboxPanel.repaint();
        }
    }
}
