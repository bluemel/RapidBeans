/*
 * Rapid Beans Framework: RendererListEnum.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/23/2006
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.MissingResourceException;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.presentation.EditorProperty;

/**
 * The cell renderer for enum lists.
 * Takes a string defined in the locale's resource bundle.<br/>
 * The key is <b>"enum.&lt;enum type short name&gt;.&lt;enum name&gt;"</b>
 * 
 * @author Martin Bluemel
 */
public class RendererListEnum implements ListCellRenderer {

	/**
	 * the parent property editor.
	 */
	private EditorProperty propEd = null;

	/**
	 * the locale to retrieve the appropriate text.
	 */
	private RapidBeansLocale locale = null;

	/**
	 * constructor.
	 * 
	 * @param loc
	 *            the locale
	 */
	public RendererListEnum(final RapidBeansLocale loc, final EditorProperty propertyEditor) {
		this.propEd = propertyEditor;
		this.locale = loc;
	}

	/**
	 * @param list
	 *            the list
	 * @param value
	 *            the value
	 * @param index
	 *            the index
	 * @param isSelected
	 *            is the cellis selected
	 * @param cellHasFocus
	 *            if the call has focus
	 * 
	 * @return the redered component
	 * 
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(final JList list, final Object value,
			final int index, final boolean isSelected, final boolean cellHasFocus) {
		JLabel label = new JLabel();
		if (value != null) {
			final RapidEnum bbEnum = (RapidEnum) value;
			String text = null;
			if (propEd.getConfig() != null && propEd.getConfig().getDetail() != null) {
				String key = null;
				try {
					final Method method = bbEnum.getClass().getMethod(
							"get" + StringHelper.upperFirstCharacter(propEd.getConfig().getDetail()),
							new Class[0]);
					key = (String) method.invoke(bbEnum, new Object[0]);
				} catch (SecurityException e) {
					throw new RapidBeansRuntimeException(e);
				} catch (NoSuchMethodException e) {
					throw new RapidBeansRuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw new RapidBeansRuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RapidBeansRuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RapidBeansRuntimeException(e);
				}
				if (key != null) {
					try {
						text = locale.getStringGui(key);
					} catch (MissingResourceException e) {
						text = key;
					}
				}
			}
			if (text == null) {
				text = bbEnum.toStringGui(this.locale);
			}
			label.setText(text);
		} else {
			label.setText("-");
		}
		if (isSelected) {
			label.setOpaque(true);
			label.setBackground(MainWindowSwing.COLOR_SELECTED_BACKGROUND);
		}
		return label;
	}

	/**
	 * @return the locale
	 */
	protected RapidBeansLocale getLocale() {
		return locale;
	}
}
