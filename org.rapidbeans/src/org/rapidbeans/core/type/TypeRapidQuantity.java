/*
 * Rapid Beans Framework: TypeRapidQuantity.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/15/2005
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

import java.util.Collection;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.core.util.XmlNode;

/**
 * the type class for quantities.
 * 
 * @author Martin Bluemel
 */
public class TypeRapidQuantity extends RapidBeansType {

	/**
	 * the type of the quantity type's unit.
	 */
	private TypeRapidEnum unittype = null;

	/**
	 * the conversion table.
	 */
	private TypeRapidQuantityConversionTable conversionTable = null;

	/**
	 * @return the conversion table
	 */
	public final TypeRapidQuantityConversionTable getConversionTable() {
		return this.conversionTable;
	}

	/**
	 * Constructor for TypeRapidQuantity.
	 * 
	 * @param typename
	 *            the quantity type name
	 */
	public TypeRapidQuantity(final String typename) {
		this(null, loadDescription(typename));
	}

	/**
	 * Constructor for TypeRapidQuantity.
	 * 
	 * @param clazz
	 *            the quantity type class
	 * @param typeDescrRootNode
	 *            the quantity type description
	 */
	public TypeRapidQuantity(final Class<?> clazz, final XmlNode typeDescrRootNode) {
		final String quantityTypeName = typeDescrRootNode.getAttributeValue("@name");
		validateString(quantityTypeName, "quantitytype", "name");
		String unitEnumTypename = typeDescrRootNode.getAttributeValue("@unitenum");
		validateString(unitEnumTypename, "quantitytype", "unitenum");
		TypeRapidEnum unitEnumType = null;
		if (!unitEnumTypename.contains(".") && quantityTypeName.contains(".")) {
			unitEnumTypename = StringHelper.splitBeforeLast(quantityTypeName, ".")
					+ '.' + unitEnumTypename;
		}
		unitEnumType = TypeRapidEnum.forName(unitEnumTypename);

		// transform the units into the short conversion table
		// description e. g.
		// "pm/1E12,nm/1E9,um/1E6,mm/1E3,cm/1E2,dm/10,m*1,km*1E3";
		final Collection<XmlNode> units =
				typeDescrRootNode.getSubnodes("unit");
		StringBuffer conversionTableDescr = new StringBuffer();
		int i = 0;
		for (XmlNode unit : units) {
			if (i > 0) {
				conversionTableDescr.append(',');
			}
			i++;
			conversionTableDescr.append(unit.getAttributeValue("@name"));
			final String sFactor = unit.getAttributeValue("@factor");
			if ((!sFactor.startsWith("*")) && (!sFactor.startsWith("/"))) {
				conversionTableDescr.append("*");
			}
			conversionTableDescr.append(sFactor);
		}
		this.setName(quantityTypeName);
		this.setImplementingClass(clazz);
		this.unittype = unitEnumType;
		this.conversionTable = new TypeRapidQuantityConversionTable(
				unitEnumType, conversionTableDescr.toString());
	}

	/**
	 * see Object.toString().
	 * 
	 * @return the string
	 */
	public final String toString() {
		return this.getName();
	}

	/**
	 * @return the quantity type's units' type
	 */
	public final TypeRapidEnum getUnitInfo() {
		return this.unittype;
	}

	/**
	 * initialize a quantity type out of a concrete (generated) class.
	 * 
	 * @param quantityClass
	 *            the quantity's class.
	 * 
	 * @return the quantity type
	 */
	public static final TypeRapidQuantity createInstance(final Class<?> quantityClass) {
		final TypeRapidQuantity qtype = new TypeRapidQuantity(quantityClass, loadDescription(quantityClass.getName()));
		RapidBeansTypeLoader.getInstance().registerType(qtype);
		return qtype;
	}

	/**
	 * factory method for the type instance.
	 * 
	 * @param descr
	 *            the XML type description
	 * 
	 * @return the new type instance
	 */
	public static TypeRapidQuantity createInstance(
			final String descr) {
		return createInstance(XmlNode.getDocumentTopLevel(descr));
	}

	/**
	 * factory method for the type instance.
	 * 
	 * @param descr
	 *            the XML type description
	 * 
	 * @return the new type instance
	 */
	public static TypeRapidQuantity createInstance(
			final XmlNode descrTopLevel) {
		TypeRapidQuantity type = new TypeRapidQuantity(null, descrTopLevel);
		RapidBeansTypeLoader.getInstance().registerType(type);
		return type;
	}

	/**
	 * find a quantity type out of a name.
	 * 
	 * @param typename
	 *            the type's name
	 * @return null if the quantity with the specified type name could not be
	 *         found and a concrete quantity class could not be loaded
	 */
	public static TypeRapidQuantity forName(final String typename) {
		return (TypeRapidQuantity) RapidBeansTypeLoader.getInstance().loadType(TypeRapidQuantity.class, typename);
	}

	/**
	 * validate.
	 * 
	 * @param s
	 *            the string to validate
	 * @param entityname
	 *            entity name
	 * @param attrname
	 *            attribute name
	 */
	private static void validateString(final String s,
			final String entityname, final String attrname) {
		if (s == null) {
			throw new RapidBeansRuntimeException("Error parsing XML quantity description."
					+ " entity <" + entityname + ">"
					+ " attribute \"" + attrname + "\" not found");
		}
		if (s.equals("")) {
			throw new RapidBeansRuntimeException("Error parsing XML quantity description."
					+ "  Entity <quantitytype>: attribute \"name\" is empty");
		}
	}
}
