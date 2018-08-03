/*
 * Rapid Beans Framework: View.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/11/2006
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

package org.rapidbeans.presentation;

/**
 * The bean view interface.
 * 
 * @author Martin Bluemel
 */
public interface View {

	/**
	 * @return the view's widget
	 */
	Object getWidget();

	/**
	 * @return the view's title
	 */
	String getTitle();

	/**
	 * @return the view's name
	 */
	String getName();

	/**
	 * close the view.
	 * 
	 * @return if cancelling is desired
	 */
	boolean close();
}
