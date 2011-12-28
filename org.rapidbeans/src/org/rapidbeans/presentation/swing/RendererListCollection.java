/*
 * Rapid Beans Framework: RendererListCollection.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 02/24/2006
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
import java.util.MissingResourceException;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.rapidbeans.core.basic.Container;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.RapidBeansLocale;


/**
 * The cell renderer for bean collection.
 * Takes a string defined in the locale's resource bundle.<br/>
 * The key is <b>"enum.&lt;enum type short name&gt;.&lt;enum name&gt;"</b>
 *
 * @author Martin Bluemel
 */
public class RendererListCollection implements ListCellRenderer {

    /**
     * the locale to retrieve the appropriate text.
     */
    private RapidBeansLocale locale = null;

    /**
     * this document.
     */
    private Container document = null;

    /**
     * constructor.
     * @param doc the document
     * @param loc the locale
     */
    public RendererListCollection(final Container doc, final RapidBeansLocale loc) {
        this.document = doc;
        this.locale = loc;
    }

    /**
     * @param list the list
     * @param value the value
     * @param index the index
     * @param isSelected is the cellis selected
     * @param cellHasFocus if the call has focus
     *
     * @return the redered component
     *
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(
     * javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    public Component getListCellRendererComponent(final JList list, final Object value,
            final int index, final boolean isSelected, final boolean cellHasFocus) {
        JLabel label = new JLabel();
        if (value == null) {
            label.setText("-");
        } else {
            final RapidBean bean = (RapidBean) value;
            label.setText(this.getIdString(bean));
        }
        if (isSelected) {
            label.setOpaque(true);
            label.setBackground(MainWindowSwing.COLOR_SELECTED_BACKGROUND);
        }
        return label;
    }

    /**
     * @param bean the bean to persent in the tree view
     * @return the id string to present in the tree view
     */
    protected String getIdString(final RapidBean bean) {
        if (bean == null) {
            return "null";
        }
        String idstring = null;
        String pattern = null;
        try {
            pattern = this.locale.getStringGui("document."
                + this.document.getConfigNameOrName() + ".treeview."
                + bean.getType().getNameShort().toLowerCase()
                + ".id");
            idstring = bean.expandPropertyValues(pattern, this.locale);
        } catch (MissingResourceException e) {
            idstring = null;
        }
        if (idstring == null) {
            idstring = bean.toStringGuiId(this.locale);
        }
        return idstring;
    }
}
