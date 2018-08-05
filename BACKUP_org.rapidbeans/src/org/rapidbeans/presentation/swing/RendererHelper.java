/*
 * Rapid Beans Framework: RendererHelper.java
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

import javax.swing.JLabel;

/**
 * A cell renderer helper class.
 * 
 * @author Martin Bluemel
 */
public final class RendererHelper {

	public static JLabel createLabel(final String text, final boolean isSelected) {
		final JLabel label = new JLabel(text);
		if (isSelected) {
			label.setOpaque(true);
			label.setBackground(MainWindowSwing.COLOR_SELECTED_BACKGROUND);
		}
		return label;
	}
}
