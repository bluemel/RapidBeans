/*
 * Rapid Beans Framework: TypePropertyCollection.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/27/2005
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import org.rapidbeans.core.basic.AssociationendFlavor;
import org.rapidbeans.core.basic.SortingType;
import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.util.ClassHelper;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.core.util.XmlNode;

/**
 * The type class for association end properties.
 * 
 * @see org.rapidbeans.core.basic.PropertyAssociationEnd
 * 
 * @author Martin Bluemel
 */
public class TypePropertyCollection extends TypeProperty {

	@Override
	public Class<?> getValuetype() {
		return ReadonlyListCollection.class;
	}

	/**
	 * the bean target type for the collection property.
	 */
	private TypeRapidBean targetType = null;

	/**
	 * name of the property of the target type that defines the
	 * inverse link for this association. If you define that
	 * adding a link to an instance of targetType will also
	 * add an inverse link from the target instance to the proerties
	 * parent bean. Removing the link will also remove the inverse
	 * link.
	 */
	private String inverse = null;

	/**
	 * the composition flag.
	 */
	private boolean composition;

	/**
	 * the minimal multiplicity.
	 */
	private int minmult = 0;

	public static final int INFINITE = -1;

	/**
	 * the maximal multiplicity.
	 * Integer.MAX_VALUE means unlimited.
	 */
	private int maxmult = INFINITE;

	/**
	 * the flavor (set or bag).
	 */
	private AssociationendFlavor flavor = null;

	/**
	 * the sorting type.
	 */
	private SortingType sorting = null;

	/**
	 * defines the property types for sorting in ascending order.
	 */
	private TypeProperty[] sortingProptypes = null;

	/**
	 * the singular name for collection properties with maxmult > 1.
	 */
	private String singular = null;

	private static char defaultCharSeparator = ' ';

	/**
	 * @param defaultCharSeparator
	 *            the defaultCharSeparator to set
	 */
	public static void setDefaultCharSeparator(char defaultCharSeparator) {
		TypePropertyCollection.defaultCharSeparator = defaultCharSeparator;
	}

	private char charSeparator = defaultCharSeparator;

	/**
	 * @return the charSeparator
	 */
	public char getCharSeparator() {
		return charSeparator;
	}

	/**
	 * @param charSeparator
	 *            the charSeparator to set
	 */
	protected void setCharSeparator(char charSeparator) {
		this.charSeparator = charSeparator;
	}

	private static char defaultCharEscape = '\\';

	/**
	 * @param defaultCharEscape
	 *            the defaultCharEscape to set
	 */
	public static void setDefaultCharEscape(char defaultCharEscape) {
		TypePropertyCollection.defaultCharEscape = defaultCharEscape;
	}

	private char charEscape = defaultCharEscape;

	/**
	 * @return the defaultCharEscape
	 */
	public static char getDefaultCharEscape() {
		return defaultCharEscape;
	}

	/**
	 * @return the charEscape
	 */
	public char getCharEscape() {
		return charEscape;
	}

	/**
	 * @param charEscape
	 *            the charEscape to set
	 */
	protected void setCharEscape(char charEscape) {
		this.charEscape = charEscape;
	}

	/**
	 * RapidBean default Java collection implementation.
	 * It has a good performance also for big collections
	 * (LinkeHashSet doesn't).
	 * Elements are sorted as created and as serialized / persisted (XML
	 * not SQL).
	 * Please note that ArrayList does not prevent linking the same
	 * instance twice which complies to a sequence (ordered bag).
	 */
	public static final Class<?> DEFAULT_COLLECTION_CLASS_DEFAULT = ArrayList.class;

	/**
	 * defines the default collection class for all collections
	 * (= association roles or ends).
	 */
	private static Class<?> defaultCollectionClass = DEFAULT_COLLECTION_CLASS_DEFAULT;

	/**
	 * The collection class used for implementing this
	 * association role or end.
	 * The default is a LinkedHashSet which
	 * - prevents having the same object associated twice
	 * - preserves the order
	 */
	private Class<?> collectionClass = null;

	/**
	 * the default class to take for collection properties.
	 */
	private Constructor<?> collectionClassConstructor = null;

	/**
	 * just an empty class array.
	 */
	private static final Class<?>[] COLLECTION_CONSTR_PARAMTYPES = new Class[0];

	/**
	 * Constructor for TypePropertyCollection.
	 * 
	 * @param xmlNode
	 *            the XML DOM node with the property type description.
	 * @param parentBeanType
	 *            the parent bean type
	 */
	public TypePropertyCollection(final XmlNode[] xmlNodes,
			final TypeRapidBean parentBeanType) {
		this(xmlNodes, parentBeanType, "Collection", null, null);
	}

	/**
	 * Constructor for TypePropertyCollection.
	 * 
	 * @param xmlNode
	 *            the XML DOM node with the property type description.
	 * @param parentBeanType
	 *            the parent bean type
	 */
	public TypePropertyCollection(final XmlNode[] xmlNodes,
			final TypeRapidBean parentBeanType,
			final String specificTypeNamePart) {
		this(xmlNodes, parentBeanType, specificTypeNamePart, null, null);
	}

	/**
	 * Constructor for TypePropertyCollection.
	 * 
	 * @param xmlNode
	 *            the XML DOM node with the property type description.
	 * @param parentBeanType
	 *            the parent bean type
	 * @param specificTypeNamePart
	 *            "Collection"
	 * @param separator
	 *            sep
	 * @param escape
	 */
	public TypePropertyCollection(
			final XmlNode[] xmlNodes,
			final TypeRapidBean parentBeanType,
			final String specificTypeNamePart,
			final String separator,
			final String escape) {
		super(specificTypeNamePart, xmlNodes, parentBeanType);
		super.parseDefaultValue(xmlNodes[0]);

		String targetTypeName = xmlNodes[0].getAttributeValue("@targettype");
		if (targetTypeName == null || targetTypeName.equals("")) {
			throwModelValidationException("no targettype specified for Collection Property "
					+ this.getPropName());
		}
		if (!targetTypeName.contains(".") && this.getParentBeanType() != null) {
			final String packageName = this.getParentBeanType().getPackageName();
			if (packageName != null) {
				targetTypeName = packageName + "." + targetTypeName;
			}
		}
		this.targetType = (TypeRapidBean) RapidBeansTypeLoader.getInstance().loadType(TypeRapidBean.class,
				targetTypeName);

		this.inverse = xmlNodes[0].getAttributeValue("@inverse");

		this.composition = Boolean.parseBoolean(
				xmlNodes[0].getAttributeValue("@composition", "false"));

		String sMult = xmlNodes[0].getAttributeValue("@minmult");
		if (sMult != null) {
			this.minmult = Integer.parseInt(sMult);
		}

		if (this.minmult < 0) {
			throwModelValidationException("Invalid minimal multiplicity < 0 specified");
		}

		sMult = xmlNodes[0].getAttributeValue("@maxmult");
		if (sMult != null) {
			if (sMult.equals("*")) {
				this.maxmult = INFINITE;
			} else {
				this.maxmult = Integer.parseInt(sMult);
			}
			if (this.maxmult < 1 && this.maxmult != INFINITE) {
				throwModelValidationException("Invalid maximal multiplicity < 1 specified");
			}
		}

		final String sFlavor = xmlNodes[0].getAttributeValue("@flavor", "set");
		if (sFlavor != null) {
			this.flavor = AssociationendFlavor.valueOf(sFlavor);
		}

		final String sSorting = xmlNodes[0].getAttributeValue("@sorting");
		if (sSorting != null) {
			final int iColon = sSorting.indexOf(':');
			if (iColon != -1) {
				this.sorting = SortingType.valueOf(
						sSorting.substring(0, iColon));
				final ArrayList<TypeProperty> sortingPtypes = new ArrayList<TypeProperty>();
				for (String propname : StringHelper.split(sSorting.substring(iColon + 1), ",")) {
					propname = propname.trim();
					final TypeProperty proptype = this.targetType.getPropertyType(propname);
					if (proptype == null) {
						throwModelValidationException("Invalid ordering property \"" + propname
								+ "\" specified for target type "
								+ "\"" + this.targetType.getName() + "\"\n");
					}
					sortingPtypes.add(proptype);
				}
				if (sortingPtypes.size() == 0) {
					throwModelValidationException("Specify at least one ordering property.");
				}
				this.sortingProptypes = new TypeProperty[sortingPtypes.size()];
				int i = 0;
				for (TypeProperty proptype : sortingPtypes) {
					this.sortingProptypes[i++] = proptype;
				}
			} else {
				this.sorting = SortingType.valueOf(sSorting);
			}
		}

		if (this.maxmult == 1 && this.sorting != null) {
			throwModelValidationException("Defining sorting for maximal"
					+ " multiplicity 1 is useless");
		}

		// the default collection class if
		// - flavor is undefined and
		// - ordering is undefined
		this.collectionClass = defaultCollectionClass;

		if (this.flavor == null) {
			this.throwModelValidationException("unexpected null flavor");
		}

		switch (this.flavor) {
		case set:
			if (this.sorting == null) {
				// default for an unsorted set
				this.collectionClass = LinkedHashSet.class;
			} else {
				// default for a sorted set
				this.collectionClass = TreeSet.class;
			}
			break;
		case bag:
			this.flavor = AssociationendFlavor.bag;
			if (this.sorting == null) {
				// default for an unsorted bag
				this.collectionClass = ArrayList.class;
			} else {
				// default for an sorted bag
				throwModelValidationException("no idea how to implement a sorted bag");
			}
		}

		final String collectionClassname = xmlNodes[0].getAttributeValue("@collectionclass");
		if (collectionClassname != null && !collectionClassname.equals("")) {
			try {
				this.collectionClass = Class.forName(collectionClassname);
				if (!ClassHelper.classOf(Collection.class, this.collectionClass)) {
					throwModelValidationException("Invalid collectionclass: Class \""
							+ collectionClassname + " is not a Collection.");
				}
				this.collectionClassConstructor =
						this.collectionClass.getConstructor(COLLECTION_CONSTR_PARAMTYPES);
			} catch (ClassNotFoundException e) {
				throwModelValidationException("Collection class \""
						+ collectionClassname + " not found.");
			} catch (NoSuchMethodException e) {
				throwModelValidationException("invalid collection class \""
						+ collectionClassname + "\" configured for collection"
						+ " properties.\n"
						+ "No empty default constructor found");
			}
		}

		final String sing = xmlNodes[0].getAttributeValue("@singular");
		if (sing != null && !sing.equals("")) {
			this.singular = sing;
		}

		if (separator != null) {
			if (separator.length() != 1) {
				throw new IllegalArgumentException(
						"Illegal separator XML binding option \""
								+ separator + "\"");
			}
			this.charSeparator = separator.charAt(0);
		}
		if (escape != null) {
			if (escape.length() != 1) {
				throw new IllegalArgumentException(
						"Illegal escape XML binding option \""
								+ escape + "\"");
			}
			this.charEscape = escape.charAt(0);
		}
	}

	/**
	 * @return the property type collection
	 */
	public PropertyType getProptype() {
		return PropertyType.collection;
	}

	/**
	 * @return the singular
	 */
	public String getSingular() {
		return singular;
	}

	/**
	 * @return the bean target type for the collection property
	 */
	public TypeRapidBean getTargetType() {
		return this.targetType;
	}

	/**
	 * @return the name of the property of the target type that defines the
	 *         inverse link for this association.
	 */
	public String getInverse() {
		return this.inverse;
	}

	/**
	 * @return if this is a composition
	 */
	public boolean isComposition() {
		return this.composition;
	}

	/**
	 * @return how this collection is sorted
	 */
	public SortingType getSorting() {
		return this.sorting;
	}

	/**
	 * @return Returns the minmult.
	 */
	public int getMinmult() {
		return this.minmult;
	}

	/**
	 * @return Returns the maxmult.
	 */
	public int getMaxmult() {
		return this.maxmult;
	}

	/**
	 * !!! For test purposes only. Not for production use.
	 * Set the default collection class for all for all collections
	 * (= association roles or ends).
	 * 
	 * @param defColClass
	 *            the default collection class
	 */
	public static void setDefaultCollectionClass(final Class<?> defColClass) {
		defaultCollectionClass = defColClass;
	}

	/**
	 * @return the default collection class.
	 */
	public static Class<?> getDefaultCollectionClass() {
		return defaultCollectionClass;
	}

	/**
	 * @return the collection class used for implementing this association role
	 */
	public Class<?> getCollectionClass() {
		return this.collectionClass;
	}

	/**
	 * !!! For test purposes only. Not for production use.
	 * 
	 * @param clazz
	 *            the new default collection class
	 */
	public void setCollectionClass(final Class<?> clazz) {
		this.collectionClass = clazz;
	}

	/**
	 * @return the collectionClassConstructor
	 */
	public Constructor<?> getCollectionClassConstructor() {
		return collectionClassConstructor;
	}

	/**
	 * @return the sortingProptypes
	 */
	public TypeProperty[] getSortingProptypes() {
		return sortingProptypes;
	}

	/**
	 * Evaluate XML binding description (XML)
	 * 
	 * @param beantype
	 *            the parent bean type
	 * @param propNode
	 *            the XML property description node
	 */
	protected void evalXmlBinding(final TypeRapidBean beantype,
			final XmlNode propNode) {
		final String separator = propNode.getAttributeValue("@separator");
		if (separator != null) {
			if (separator.length() != 1) {
				throw new RapidBeansRuntimeException(
						"Invalid XML Binding for type \"" + beantype.getName() + "\":\n"
								+ "Property \"" + getPropName() + ", illegal separator character \""
								+ separator + "\"");
			}
			setCharSeparator(separator.charAt(0));
		}
		final String escape = propNode.getAttributeValue("@escape");
		if (escape != null) {
			if (escape.length() != 1) {
				throw new RapidBeansRuntimeException(
						"Invalid XML Binding for type \"" + beantype.getName() + "\":\n"
								+ "Property \"" + getPropName() + ", illegal escape character \""
								+ escape + "\"");
			}
			setCharEscape(escape.charAt(0));
		}
	}
}
