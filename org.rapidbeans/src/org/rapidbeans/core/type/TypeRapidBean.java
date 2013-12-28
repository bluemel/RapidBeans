/*
 * Rapid Beans Framework: TypeRapidBean.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/04/2005
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

import org.rapidbeans.core.basic.IdGenerator;
import org.rapidbeans.core.basic.IdType;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.ModelValidationException;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.util.ClassHelper;
import org.rapidbeans.core.util.XmlNode;

/**
 * the type class with meta information of Rapid Beans.
 * 
 * @author Martin Bluemel
 */
public final class TypeRapidBean extends RapidBeansType {

	/**
	 * abstract type flag.
	 */
	private boolean isAbstract = false;

	/**
	 * @return if this type is abstract or not.
	 */
	public boolean getAbstract() {
		return this.isAbstract;
	}

	/**
	 * the final type flag.
	 */
	private boolean isFinal = false;

	/**
	 * @return if this type is final or not.
	 */
	public boolean getFinal() {
		return this.isFinal;
	}

	/**
	 * the collection of property types.
	 */
	private List<TypeProperty> propertyTypes = new ArrayList<TypeProperty>();

	/**
	 * @return the collection of the bean's property types.
	 */
	public List<TypeProperty> getPropertyTypes() {
		return this.propertyTypes;
	}

	/**
	 * @return a list with all the bean's collection properties
	 */
	public List<TypePropertyCollection> getColPropertyTypes() {
		final List<TypePropertyCollection> colproptypelist = new ArrayList<TypePropertyCollection>();
		for (final TypeProperty proptype : this.propertyTypes) {
			if (ClassHelper.classOf(TypePropertyCollection.class, proptype.getClass())) {
				colproptypelist.add((TypePropertyCollection) proptype);
			}
		}
		return colproptypelist;
	}

	/**
	 * the hash map for property types.
	 */
	private HashMap<String, TypeProperty> propTypeMap = new HashMap<String, TypeProperty>();

	/**
	 * retrieve a property type by a property name.
	 * 
	 * @param propname
	 *            the property name
	 * 
	 * @return the property type
	 */
	public TypeProperty getPropertyType(final String propname) {
		final TypeProperty proptype = this.propTypeMap.get(propname);
		if (proptype != null) {
			return this.propTypeMap.get(propname);
		} else if (this.getSupertype() != null) {
			return this.getSupertype().getPropertyType(propname);
		} else {
			return null;
		}
	}

	/**
	 * Find's a bean type instance out of it's name and loads it if it's not
	 * already loaded.
	 * 
	 * @param typename
	 *            the Bizbean's type name
	 * 
	 * @return the type instance if found
	 */
	public static TypeRapidBean forName(final String typename) {
		return (TypeRapidBean) RapidBeansTypeLoader.getInstance().loadType(TypeRapidBean.class, typename);
	}

	/**
	 * the type's super type.
	 */
	private TypeRapidBean supertype = null;

	/**
	 * @return the type's super type
	 */
	public TypeRapidBean getSupertype() {
		return this.supertype;
	}

	/**
	 * the type's sub types.
	 */
	private Collection<TypeRapidBean> subtypes = new ArrayList<TypeRapidBean>();

	/**
	 * @return the type's super type
	 */
	public Collection<TypeRapidBean> getSubtypes() {
		return this.subtypes;
	}

	/**
	 * @param subtype
	 *            the subtype to add
	 */
	public void addSubtype(final TypeRapidBean subtype) {
		this.subtypes.add(subtype);
	}

	/**
	 * the bean's id type.
	 */
	private IdType idtype = null;

	/**
	 * @return the bean's idtype
	 * @return
	 */
	public IdType getIdtype() {
		return this.idtype;
	}

	/**
	 * the depth of parents in the hierarchy.
	 */
	private int idtypeParentScopeDepth = 0;

	/**
	 * the depth of parents in the hierarchy is only valid for idtype =
	 * "keypropswithparentscope".
	 * 
	 * @return the depth of parents in the hierarchy.
	 */
	public int getIdtypeParentScopeDepth() {
		return this.idtypeParentScopeDepth;
	}

	/**
	 * the Id Generator.
	 */
	private IdGenerator idGenerator = null;

	/**
	 * @return the ID genrator instance
	 */
	public IdGenerator getIdGenerator() {
		return this.idGenerator;
	}

	/**
	 * inject the id generator.
	 * 
	 * @param generator
	 *            the generator instance
	 */
	public void setIdGenerator(final IdGenerator generator) {
		this.idGenerator = generator;
	}

	/**
	 * Mapping of this RapidBean type to an XML element. Specifies the name of
	 * the root element of an XML document if a bean of this type is root of a
	 * document.
	 */
	private String xmlRootElement = null;

	/**
	 * Maps this RapidBean type to an XML element. This mapping specifies the
	 * name of the root element of an XML document if this bean is root of a
	 * document.
	 * 
	 * @return the XML element name of this RapidBean type
	 */
	public String getXmlRootElement() {
		if (this.xmlRootElement != null) {
			return this.xmlRootElement;
		}
		TypeRapidBean type = this;
		while (type != null) {
			if (type.xmlRootElement != null) {
				return type.xmlRootElement;
			}
			type = type.getSupertype();
		}
		return null;
	}

	/**
	 * Mapping of XML element to a collection property type
	 */
	private Map<String, TypeProperty> xmlElements = null;

	/**
	 * @return the xmlElements
	 */
	public Map<String, TypeProperty> getXmlElements() {
		return this.xmlElements;
	}

	/**
	 * Mapping of XML element to a bean type
	 */
	private Map<String, TypeRapidBean> xmlElementsTypeMap = null;

	/**
	 * @return the xmlElements type map
	 */
	public Map<String, TypeRapidBean> getXmlElementsTypeMap() {
		return this.xmlElementsTypeMap;
	}

	/**
	 * Maps a given XML element name to a name of a property of this type if the
	 * appropriate XML binding is defined.
	 * 
	 * @param xmlRootElement
	 *            the XML element name
	 * 
	 * @return the mapped property name if defined, otherwise "null"
	 */
	public String mapXmlElementToPropName(final String xmlElement) {
		String propName = null;
		if (this.xmlElements != null) {
			final TypeProperty propType = this.xmlElements.get(xmlElement);
			if (propType != null) {
				propName = propType.getPropName();
			}
		}
		return propName;
	}

	/**
	 * Constructor.
	 * 
	 * @param clazz
	 *            the bean's class
	 */
	protected TypeRapidBean(final Class<?> clazz) {
		this(clazz, loadDescription(clazz.getName()), loadDescriptionXmlBinding(clazz.getName()), false);
	}

	/**
	 * Constructor.
	 * 
	 * @param clazz
	 *            the bean's class
	 * @param typeDescr
	 *            the RapidBean type description's top level node
	 * @param xmlBindingDescr
	 *            the XML binding description's top level node
	 * @param register
	 *            specifies if the type should be registered
	 */
	public TypeRapidBean(final Class<?> clazz, final XmlNode typeDescr, final XmlNode xmlBindingDescr,
			final boolean register) {

		try {
			ThreadLocalBeanInitDepth.increment(this);
			// System.out.println("@@@ INIT["
			// + ThreadLocalBeanInitDepth.getDepth()
			// + "]: " + typeDescr.getAttributeValue("@name"));
			if (clazz == null) {
				// implementingClass stays null
				this.setName(typeDescr.getAttributeValue("@name"));
			} else {
				this.setName(clazz.getName());
				this.setImplementingClass(clazz);
			}

			if (register) {
				if (clazz == null) {
					RapidBeansTypeLoader.getInstance().registerTypeIfNotRegistered(this.getName(), this);
				} else {
					RapidBeansTypeLoader.getInstance().registerTypeIfNotRegistered(clazz.getName(), this);
				}
			}

			this.isAbstract = Boolean.parseBoolean(typeDescr.getAttributeValue("@abstract", "false"));
			this.isFinal = Boolean.parseBoolean(typeDescr.getAttributeValue("@final", "false"));

			final String superTypeName = typeDescr.getAttributeValue("@extends");
			if (superTypeName != null) {
				this.supertype = TypeRapidBean.forName(superTypeName);
			}
			if ((this.supertype != null) && (typeDescr.getAttributeValue("@idtype") == null)) {
				this.idtype = this.supertype.getIdtype();
			} else {
				this.idtype = IdType.valueOf(typeDescr.getAttributeValue("@idtype", "transientid"));
			}
			if (this.idtype == IdType.keypropswithparentscope) {
				final String s = typeDescr.getAttributeValue("@idtypeparentscopedepth", "0");
				this.idtypeParentScopeDepth = Integer.parseInt(s);
			}

			final HashMap<String, TypeProperty> propMap = new HashMap<String, TypeProperty>();
			if (this.supertype != null) {
				this.supertype = TypeRapidBean.forName(superTypeName);
				for (TypeProperty superPropType : this.supertype.getPropertyTypes()) {
					this.propertyTypes.add(superPropType);
					this.propTypeMap.put(superTypeName, superPropType);
					propMap.put(superPropType.getPropName(), superPropType);
				}
				this.supertype.addSubtype(this);
			}
			for (XmlNode propertyNode : typeDescr.getSubnodes("property")) {
				XmlNode propertyNodeXmlBinding = null;
				if (xmlBindingDescr != null) {
					propertyNodeXmlBinding = retrievePropertyNodeXmlBinding(propertyNode, xmlBindingDescr);
				} else {
					for (XmlNode xmlBindingNode : typeDescr.getSubnodes("xmlbinding")) {
						propertyNodeXmlBinding = retrievePropertyNodeXmlBinding(propertyNode, xmlBindingNode);
					}
				}
				final TypeProperty propertyType = TypeProperty.createInstance(propertyNode, propertyNodeXmlBinding,
						this);
				final TypeProperty superPropType = propMap.get(propertyType.getPropName());
				if (superPropType != null) {
					if (this.isFinal || propertyType.getFinal()) {
						throw new RapidBeansRuntimeException("Error while initializing" + " bean type \""
								+ this.getName() + "\": final property \"" + propertyType.getPropName()
								+ "\" may not be overriden.");
					}
					this.propertyTypes.set(getIndexInPropertyTypes(superPropType), propertyType);
				} else {
					this.propertyTypes.add(propertyType);
				}
				this.propTypeMap.put(propertyType.getPropName(), propertyType);
			}

			if (ThreadLocalBeanInitDepth.getDepth() == 1) {
				for (final TypeRapidBean typeInited : ThreadLocalBeanInitDepth.getTypesInitialilized()) {
					for (final TypePropertyCollection colPropType : typeInited.getColPropertyTypes()) {
						if (colPropType.getInverse() != null
								&& colPropType.getTargetType().getPropertyType(colPropType.getInverse()) == null) {
							final String msg = "Problem with property \"" + typeInited.getName() + "::"
									+ colPropType.getPropName() + "\":\n" + "inverse property \""
									+ colPropType.getTargetType().getName() + "::" + colPropType.getInverse()
									+ "\" is not defined.";
							throw new ModelValidationException(msg);
						}
					}
				}
			}

			if (this.supertype != null
					&& (this.idtype == IdType.keyprops || this.idtype == IdType.keypropswithparentscope)) {
				checkKeypropsSorting();
			}

			setDescription(RapidBeansType.readDescription(typeDescr));

			// evaluate XML binding description
			if (xmlBindingDescr != null) {
				evalXmlBinding(xmlBindingDescr);
			} else {
				for (XmlNode xmlBindingNode : typeDescr.getSubnodes("xmlbinding")) {
					evalXmlBinding(xmlBindingNode);
				}
			}
		} finally {
			ThreadLocalBeanInitDepth.decrement();
		}
	}

	private int getIndexInPropertyTypes(final TypeProperty propType) {
		int i = 0;
		for (final TypeProperty currentPropType : this.propertyTypes) {
			if (propType.getPropName().equals(currentPropType.getPropName())) {
				return i;
			}
			i++;
		}
		return -1;
	}

	private XmlNode retrievePropertyNodeXmlBinding(final XmlNode propertyNode, final XmlNode xmlBindingDescr) {
		final String propName = propertyNode.getAttributeValue("@name");
		XmlNode propertyNodeXmlBinding = null;
		for (XmlNode currentNode : xmlBindingDescr.getSubnodes("property")) {
			if (currentNode.getAttributeValue("@name").equals(propName)) {
				propertyNodeXmlBinding = currentNode;
				break;
			}
		}
		return propertyNodeXmlBinding;
	}

	/**
	 * Evaluate the type's XML binding description.
	 * 
	 * @param xmlBindingDescr
	 *            the XML Node with the binding description
	 */
	private void evalXmlBinding(final XmlNode xmlBindingDescr) {
		final List<TypeRapidBean> supertypes = new ArrayList<TypeRapidBean>();
		TypeRapidBean supertype = this.getSupertype();
		while (supertype != null) {
			supertypes.add(supertype);
			supertype = supertype.getSupertype();
		}
		final int size = supertypes.size();
		for (int i = size - 1; i > 0; i--) {
			final TypeRapidBean st = supertypes.get(i);
			if (st.getXmlRootElement() != null) {
				this.xmlRootElement = st.getXmlRootElement();
			}
			if (st.getXmlElements() != null) {
				for (final String typename : st.getXmlElements().keySet()) {
					// lazy initialization
					if (this.xmlElements == null) {
						this.xmlElements = new HashMap<String, TypeProperty>();
					}
					this.xmlElements.put(typename, st.getXmlElements().get(typename));
				}
			}
		}
		if (xmlBindingDescr.getAttributeValue("@xmlrootelement") != null) {
			this.xmlRootElement = xmlBindingDescr.getAttributeValue("@xmlrootelement");
		}
		if (this.xmlRootElement != null && this.xmlRootElement.length() > 0) {
			final RapidBeansTypeLoader typeLoader = RapidBeansTypeLoader.getInstance();
			if (typeLoader.getXmlRootElementBinding(this.xmlRootElement) == null) {
				typeLoader.addXmlRootElementBinding(this.xmlRootElement, this.getName());
			} else {
				if (!typeLoader.getXmlRootElementBinding(this.xmlRootElement).getName().equals(this.getName())) {
					throw new RapidBeansRuntimeException("An XML root element binding" + " for XML element name "
							+ this.xmlRootElement + " has already been defined differently\n" + "Current definition: "
							+ typeLoader.getXmlRootElementBinding(this.xmlRootElement).getName());
				}
			}
		}
		for (final XmlNode propNode : xmlBindingDescr.getSubnodes("property")) {
			final String propName = propNode.getAttributeValue("@name");
			if (propName == null || propName.length() == 0) {
				throw new RapidBeansRuntimeException("Invalid XML Binding for type \"" + this.getName() + "\":\n"
						+ "\"property\" element without or with empty attribute \"name\"");
			}
			TypeProperty proptype = this.propTypeMap.get(propName);
			if (proptype == null) {
				TypeRapidBean st = this.getSupertype();
				while (st != null && proptype == null) {
					proptype = st.propTypeMap.get(propName);
					st = st.getSupertype();
				}
			}
			if (proptype == null) {
				throw new RapidBeansRuntimeException("Invalid XML Binding for type \"" + this.getName() + "\":\n"
						+ "property \"" + proptype + "\" does not exist.");
			}
			proptype.evalXmlBinding(this, propNode);
			for (final XmlNode beantypeNode : propNode.getSubnodes("beantype")) {
				final String mappedTypeName = beantypeNode.getAttributeValue("@name");
				if (mappedTypeName == null || mappedTypeName.length() == 0) {
					throw new RapidBeansRuntimeException("Invalid XML Binding for type \"" + this.getName() + "\":\n"
							+ "\"beanype\" element without or with empty attribute \"xmlelement\"");
				}
				final String xmlPropElement = beantypeNode.getAttributeValue("@xmlelement");
				if (xmlPropElement == null || xmlPropElement.length() == 0) {
					throw new RapidBeansRuntimeException("Invalid XML Binding for type \"" + this.getName() + "\":\n"
							+ "\"beanype\" element without or with empty attribute \"xmlelement\"");
				}
				// lazy initialization
				if (this.xmlElements == null) {
					this.xmlElements = new HashMap<String, TypeProperty>();
				}
				this.xmlElements.put(xmlPropElement, proptype);
				if (this.xmlElementsTypeMap == null) {
					this.xmlElementsTypeMap = new HashMap<String, TypeRapidBean>();
				}
				this.xmlElementsTypeMap.put(xmlPropElement, TypeRapidBean.forName(mappedTypeName));
			}
		}
	}

	/**
	 * factory method for the type instance.
	 * 
	 * @param clazz
	 *            the bean's class
	 * 
	 * @return the new type instance
	 */
	public static TypeRapidBean createInstance(final Class<?> clazz) {
		TypeRapidBean type = new TypeRapidBean(clazz, loadDescription(clazz.getName()),
				loadDescriptionXmlBinding(clazz.getName()), true);
		return type;
	}

	/**
	 * behaves like "instance of" but with bean types.
	 * 
	 * @param supertype
	 *            the same or super type.
	 * @param comptype
	 *            the type to test if same or sub type
	 * 
	 * @return if the type to test is same or sub type of the super type
	 */
	public static boolean isSameOrSubtype(final TypeRapidBean supertype, final TypeRapidBean comptype) {
		boolean b = false;
		TypeRapidBean typeToCompare = comptype;
		while (!b && typeToCompare != null) {
			if (typeToCompare == supertype) {
				b = true;
			}
			typeToCompare = typeToCompare.getSupertype();
		}
		return b;
	}

	/**
	 * check and sort key properties together if necessary.
	 */
	private void checkKeypropsSorting() {
		int size = this.propertyTypes.size();
		int firstNonKeypropIndex = -1;
		for (int i = 0; i < size; i++) {
			if (firstNonKeypropIndex == -1) {
				if (!this.propertyTypes.get(i).isKeyCandidate()) {
					firstNonKeypropIndex = i;
				}
			} else {
				if (this.propertyTypes.get(i).isKeyCandidate()) {
					final TypeProperty swaptype = this.propertyTypes.get(i);
					for (int j = i; j > firstNonKeypropIndex; j--) {
						this.propertyTypes.set(j, this.propertyTypes.get(j - 1));
					}
					this.propertyTypes.set(firstNonKeypropIndex, swaptype);
					firstNonKeypropIndex++;
				}
			}
		}
	}

	/**
	 * Retrieve all concrete sub beantypes from the inheritance tree.
	 * 
	 * @return a list of bean types
	 */
	public List<TypeRapidBean> getConcreteSubtypes() {
		final List<TypeRapidBean> concreteSubtpyes = new ArrayList<TypeRapidBean>();
		for (final TypeRapidBean subtype : this.getSubtypes()) {
			subtype.collectConcreteSubtypes(concreteSubtpyes);
		}
		return concreteSubtpyes;
	}

	/**
	 * computes a localized String to present this type in a UI.
	 * 
	 * @param locale
	 *            the Locale
	 * @param plural
	 *            determines if the plural is required
	 * @param defaultTypename
	 *            if null the type's short name is returned
	 * 
	 * @return the localized String for this Bean
	 */
	public String toStringGui(final RapidBeansLocale locale, final boolean plural, final String defaultTypename) {
		String uistring = null;

		if (locale != null) {
			try {
				String pattern = "bean." + getName().toLowerCase();
				if (plural) {
					pattern += ".plural";
				}
				uistring = locale.getStringGui(pattern);
			} catch (MissingResourceException e) {
				uistring = null;
			}
		}

		if (uistring == null) {
			if (defaultTypename == null) {
				if (plural) {
					uistring = getNameShort() + 's';
				} else {
					uistring = getNameShort();
				}
			} else {
				uistring = defaultTypename;
			}
		}

		return uistring;
	}

	/**
	 * Recursive collection of sub beantypes.
	 * 
	 * @param concreteSubtpyes
	 *            the list into which we collect
	 */
	private void collectConcreteSubtypes(List<TypeRapidBean> concreteSubtpyes) {
		if (!this.getAbstract()) {
			concreteSubtpyes.add(this);
		}
		for (final TypeRapidBean subtype : this.getSubtypes()) {
			subtype.collectConcreteSubtypes(concreteSubtpyes);
		}
	}

	/**
	 * @return if the bean type has a dependend key property
	 */
	public boolean hasDependendKeyProp() {
		boolean hasDependendKeyProp = false;
		if (getIdtype() == IdType.keyprops || getIdtype() == IdType.keypropswithparentscope) {
			for (final TypeProperty proptype : this.getPropertyTypes()) {
				if (proptype.getDependentFromProps().size() > 0) {
					hasDependendKeyProp = true;
					break;
				}
			}
		}
		return hasDependendKeyProp;
	}
}
