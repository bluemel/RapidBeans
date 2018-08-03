/*
 * Rapid Beans Framework, SDK, Maven Plugin: CodeGenInfo.java
 *
 * Copyright (C) 2013 Martin Bluemel
 *
 * Creation Date: 01/21/2013
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

public class CodeGenInfo {

	/**
	 * the type of RapidBeans type.
	 */
	private TypeOfType typeOfType = TypeOfType.undefined;

	/**
	 * code generation mode.
	 */
	private CodeGenMode mode = CodeGenMode.simple;

	/**
	 * @return the mode
	 */
	public CodeGenMode getMode() {
		return mode;
	}

	/**
	 * @return the typeOfType
	 */
	public TypeOfType getTypeOfType() {
		return typeOfType;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(CodeGenMode mode) {
		this.mode = mode;
	}

	/**
	 * @param typeOfType the typeOfType to set
	 */
	public void setTypeOfType(TypeOfType typeOfType) {
		this.typeOfType = typeOfType;
	}

}
