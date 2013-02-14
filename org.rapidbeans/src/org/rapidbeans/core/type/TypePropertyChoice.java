/*
 * Rapid Beans Framework: TypePropertyChoice.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 12/01/2005
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

package org.rapidbeans.core.type;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.util.XmlNode;

/**
 * the type class for Choice properties.
 * 
 * @author Martin Bluemel
 */
public final class TypePropertyChoice extends TypeProperty {

	/**
	 * the EasyBiz RapidEnum type for the choice property.
	 */
	private TypeRapidEnum enumType = null;

	/**
	 * Morphic change of meta information.
	 * WARNING do not use this method besides you exactly know the consequences.
	 * 
	 * @param enumType
	 */
	public void setEnumType(TypeRapidEnum enumType) {
		this.enumType = enumType;
	}

	/**
	 * @return the EasyBiz RapidEnum type for the choice property
	 */
	public TypeRapidEnum getEnumType() {
		return this.enumType;
	}

	/**
	 * the single / multiple configuration for the choice property.
	 */
	private boolean multiple = false;

	/**
	 * @return the single / multiple configuration for the choice property.<br/>
	 *         <b>false</b>: single choice<br/>
	 *         <b>true</b>: multiple choice<br/>
	 */
	public boolean getMultiple() {
		return this.multiple;
	}

	/**
	 * Constructor for TypePropertyChoice.
	 * 
	 * @param xmlNode
	 *            the XML DOM node with the property type description.
	 * @param parentBeanType
	 *            the parent bean type
	 */
	public TypePropertyChoice(final XmlNode[] xmlNodes,
			final TypeRapidBean parentBeanType) {
		super("Choice", xmlNodes, parentBeanType);

		String enumTypeName = xmlNodes[0].getAttributeValue("@enum");
		if (enumTypeName == null || enumTypeName.equals("")) {
			throw new RapidBeansRuntimeException("no enum specified for Choice Property "
					+ this.getPropName());
		}
		if (!enumTypeName.contains(".") && this.getParentBeanType() != null) {
			final String packageName = this.getParentBeanType().getPackageName();
			if (packageName != null) {
				enumTypeName = packageName + "." + enumTypeName;
			}
		}
		this.enumType = TypeRapidEnum.forName(enumTypeName);
		final String defaultDescr = xmlNodes[0].getAttributeValue("@default");
		if (defaultDescr != null) {
			this.setDefaultValue(this.getEnumType().parse(defaultDescr));
		}
		final String sMultiple = xmlNodes[0].getAttributeValue("@multiple");
		if (sMultiple != null) {
			this.multiple = Boolean.parseBoolean(sMultiple);
		}
	}

	/**
	 * @return the property type enumeration
	 */
	public PropertyType getProptype() {
		return PropertyType.choice;
	}
}
