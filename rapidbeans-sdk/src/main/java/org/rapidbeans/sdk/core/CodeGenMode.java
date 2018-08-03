/*
 * Rapid Beans Framework, SDK: CodeGenMode.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/01/2008
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

package org.rapidbeans.sdk.core;

public enum CodeGenMode {

	/**
	 * Bean source file is purely generated into folder "destdirsimple".
	 */
	simple,

	/**
	 * Bean source file RapidBeanBase<class name> is purely generated into folder
	 * "destdirsimple".
	 */
	split,

	/**
	 * Bean source file is generated into folder "destdirjoint" (usually under
	 * version control and protected regions are merged.
	 */
	joint,

	/**
	 * No Bean source file is generated (Model file is interpreted).
	 */
	none,

	/**
	 * CodeGenMode is read from model file or "simple" if not set there (default =
	 * simple).
	 */
	flexible
}
