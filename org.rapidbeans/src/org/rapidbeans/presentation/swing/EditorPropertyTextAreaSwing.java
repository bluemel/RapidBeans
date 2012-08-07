/*
 * Rapid Beans Framework: EditorPropertyTextAreaSwing.java
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

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;

import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypePropertyString;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.config.ConfigPropEditorBean;

/**
 * the bean editor GUI.
 *
 * @author Martin Bluemel
 */
public class EditorPropertyTextAreaSwing extends EditorPropertySwing {

    /**
     * the text field.
     */
    private JTextArea text = new JTextArea();

    /**
     * @return the editor's widget
     */
    public Object getWidget() {
        return this.text;
    }

    /**
     * constructor.
     *
     * @param prop the bean property to edit
     * @param propBak the bean property backup
     * @param bizBeanEditor the parent bean editor
     * @param client the client
     */
    public EditorPropertyTextAreaSwing(final Application client,
            final EditorBean bizBeanEditor,
            final Property prop, final Property propBak) {
        super(client, bizBeanEditor, prop, propBak);
        super.initColors();
        this.text.setBorder(new LineBorder(new Color(0x888899)));
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
        this.updateUI();
        final ConfigPropEditorBean cfg = getConfig();
        if (prop.getReadonly()
                || (cfg != null && !cfg.getEnabled())) {
            this.text.setEnabled(false);
        }
    }

    /**
     * update the string presented in the editor.
     */
    public void updateUI() {
        try {
            this.setUIEventLock();
            if (this.getProperty() instanceof PropertyCollection) {
                final Collection<?> col =
                    (Collection<?>) this.getProperty().getValue();
                if (col == null) {
                    this.text.setText("");
                } else {
                    final TypePropertyCollection colType =
                        (TypePropertyCollection) this.getProperty().getType();
                    if (colType.getMaxmult() == 1) {
                        RapidBean bean = (RapidBean) col.iterator().next();
                        if (bean == null) {
                            this.text.setText("");
                        } else {
                            this.text.setText(bean.getIdString());
                        }
                    }
                }
            } else {
                this.text.setText((String) this.getProperty().toString());
            }
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
            TypeProperty type = this.getProperty().getType();
            if ((type instanceof TypePropertyString) && ((TypePropertyString) type).getEmptyValid()) {
                ifValue = this.text.getText();
            } else {
                ifValue = null;
            }
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
     * @return if the string in the input field is valid
     * or at least could at least get after appending additional
     * characters.
     *
     * @param ex the validation exception
     */
    protected boolean hasPotentiallyValidInputField(
            final ValidationException ex) {
        if (ex.getSignature().startsWith("invalid.prop.integer")) {
            if (ex.getSignature().endsWith("lower")) {
                return true;
            }
        }
        return false;
    }
}
