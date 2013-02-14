/*
 * Rapid Beans Framework: TypePropertyMap.java
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

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.util.ClassHelper;
import org.rapidbeans.core.util.XmlNode;

/**
 * the type class for Map properties.
 * 
 * @author Martin Bluemel
 */
public final class TypePropertyMap extends TypePropertyCollection {

	/**
	 * the bean key class for the map property.
	 */
	private Class<?> keyClass = null;

	/**
	 * @return the bean key class for the collection property
	 */
	public Class<?> getKeyClass() {
		return this.keyClass;
	}

	/**
	 * the default for the default.
	 * HashMap is suitable in most cases.
	 */
	public static final Class<?> DEFAULT_MAP_CLASS_DEFAULT = HashMap.class;

	/**
	 * defines the standard default map class.
	 */
	private static Class<?> defaultMapClass = DEFAULT_MAP_CLASS_DEFAULT;

	/**
	 * set the default map class.
	 * !!! caution: handle with care.
	 * 
	 * @param defMapClass
	 *            the default map class
	 */
	public static void setDefaultMapClass(final Class<?> defMapClass) {
		defaultMapClass = defMapClass;
	}

	/**
	 * @return the default map class.
	 */
	public static Class<?> getDefaultMapClass() {
		return defaultMapClass;
	}

	/**
	 * the map class used for implementing this association role.
	 * The default is a HashMap
	 */
	private Class<?> mapClass = defaultMapClass;

	/**
	 * for constructing maps.
	 */
	private Constructor<?> mapClassConstructor = null;

	/**
	 * just an empty class array.
	 */
	private static final Class<?>[] COLLECTION_CONSTR_PARAMTYPES = new Class[0];

	/**
	 * @return the map class used for implementing this association role
	 */
	public Class<?> getMapClass() {
		return this.mapClass;
	}

	/**
	 * for test reasons. Not for production use.
	 * 
	 * @param clazz
	 *            the new map class
	 */
	public void setMapClass(final Class<?> clazz) {
		this.mapClass = clazz;
	}

	/**
	 * @return the map class constructor
	 */
	public Constructor<?> getMapClassConstructor() {
		return mapClassConstructor;
	}

	/**
	 * Constructor for TypePropertyMap.
	 * 
	 * @param xmlNodes
	 *            the XML DOM nodes with the property type description.
	 * @param parentBeanType
	 *            the parent bean type
	 */
	public TypePropertyMap(final XmlNode[] xmlNodes,
			final TypeRapidBean parentBeanType) {
		super(xmlNodes, parentBeanType, "Map");
		super.parseDefaultValue(xmlNodes[0]);

		final String mapClassname = xmlNodes[0].getAttributeValue("@mapclass");
		if (mapClassname != null && !mapClassname.equals("")) {
			try {
				this.mapClass = Class.forName(mapClassname);
				if (!ClassHelper.classOf(Collection.class, this.mapClass)) {
					throw new RapidBeansRuntimeException("Invalid collectionclass: Class \""
							+ mapClassname + " is not a Collection.");
				}
				this.mapClassConstructor =
						this.mapClass.getConstructor(COLLECTION_CONSTR_PARAMTYPES);
			} catch (ClassNotFoundException e) {
				throw new RapidBeansRuntimeException("Collection class \""
						+ mapClassname + " not found.");
			} catch (NoSuchMethodException e) {
				throw new RapidBeansRuntimeException("invalid collection class \""
						+ mapClassname + "\" configured for collection"
						+ " properties.\n"
						+ "No empty default constructor found", e);
			}
		}
	}

	/**
	 * @return the property type collection
	 */
	public PropertyType getProptype() {
		return PropertyType.map;
	}
}
