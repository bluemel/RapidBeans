/*
 * Rapid Beans Framework: TypeProperty.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/26/2005
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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rapidbeans.core.exception.ModelValidationException;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.TypeNotFoundException;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.core.util.XmlNode;

/**
 * the parent class for every property type.
 * 
 * @author Martin Bluemel
 */
public abstract class TypeProperty {

	/**
	 * @return the property type enumeration
	 */
	public abstract PropertyType getProptype();

	/**
	 * @return the property's value type
	 */
	public abstract Class<?> getValuetype();

	/**
	 * the property's name.
	 */
	private String propName = null;

	/**
	 * @return the property's name.
	 */
	public final String getPropName() {
		return this.propName;
	}

	/**
	 * the property's implementing class.
	 */
	private Class<?> propClass = null;

	/**
	 * @return the property's class.
	 */
	public final Class<?> getPropClass() {
		return this.propClass;
	}

	/**
	 * the parent bean type.
	 */
	private TypeRapidBean parentBeanType;

	/**
	 * @return the parent bean type
	 */
	protected TypeRapidBean getParentBeanType() {
		return parentBeanType;
	}

	/**
	 * the property's default value.
	 */
	private Object defaultValue = null;

	/**
	 * @return the property's default value.
	 */
	public final Object getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * internal setter for the default value.
	 * 
	 * @param argValue
	 *            the default value instance
	 */
	protected final void setDefaultValue(final Object argValue) {
		this.defaultValue = argValue;
	}

	/**
	 * flag for key (candidate, component) attribute.
	 */
	private boolean key = false;

	/**
	 * @return if the property is a key candidate
	 */
	public boolean isKeyCandidate() {
		return this.key;
	}

	/**
	 * Indicates a read only property.
	 * Use for constants or derived properties (properties that
	 * may be only set by the program.
	 */
	private boolean readonly = false;

	/**
	 * @return if this property may be written or not
	 */
	public boolean getReadonly() {
		return this.readonly;
	}

	/**
	 * flag for mandatory attribute.
	 */
	private boolean mandatory = false;

	/**
	 * @return if the property must be defined
	 *         (= may not be null) or not
	 */
	public boolean getMandatory() {
		return this.mandatory;
	}

	/**
	 * the final property flag.
	 */
	private boolean isFinal = false;

	/**
	 * @return if this property is final or not.
	 */
	public boolean getFinal() {
		return this.isFinal;
	}

	/**
	 * the transient flag.
	 */
	private boolean transientProp = false;

	/**
	 * @return the transient flag.
	 */
	public final boolean isTransient() {
		return this.transientProp;
	}

	/**
	 * The dependencies
	 */
	private List<TypeProperty> dependentFromProps =
			new ArrayList<TypeProperty>();

	/**
	 * @return the dependentFromProps
	 */
	public List<TypeProperty> getDependentFromProps() {
		return dependentFromProps;
	}

	public boolean equals(final Object other) {
		if (!(other instanceof TypeProperty)) {
			return false;
		}
		final TypeProperty otherPropType = (TypeProperty) other;
		final String thisSignature = this.parentBeanType.getName()
				+ "." + this.propName;
		final String otherSignature = otherPropType.parentBeanType.getName()
				+ "." + otherPropType.propName;
		return thisSignature.equals(otherSignature);
	}

	public int hashCode() {
		return (this.parentBeanType.getName()
				+ "." + this.propName).hashCode();
	}

	private Map<String, TypeRapidBean> xmlBindXmlToType = null;

	public TypeRapidBean mapXmlElementToType(final String xmlElement) {
		if (this.xmlBindXmlToType != null) {
			return this.xmlBindXmlToType.get(xmlElement);
		}
		return null;
	}

	private Map<TypeRapidBean, String> xmlBindTypeToXml = null;

	public String mapTypeToXmlElement(final TypeRapidBean type) {
		if (this.xmlBindTypeToXml != null) {
			return this.xmlBindTypeToXml.get(type);
		}
		return null;
	}

	/**
	 * Define attribute or element binding.
	 */
	private PropertyXmlBindingType xmlBindingType = PropertyXmlBindingType.attribute;

	/**
	 * @return the xmlBindingType
	 */
	public PropertyXmlBindingType getXmlBindingType() {
		return this.xmlBindingType;
	}

	private String description = null;

	/**
	 * @return the type description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * creates a property type instance out of it XML description.
	 * 
	 * @param propertyNode
	 *            the XML DOM node with the property's type description
	 * @param propertyNode
	 *            the XML DOM node with the property's XML binding
	 * @param bizBeanType
	 *            the parent bizBeanType
	 * 
	 * @return the new property type instance
	 */
	@SuppressWarnings("unchecked")
	public static TypeProperty createInstance(
			final XmlNode propertyNode,
			final XmlNode propertyNodeXmlBinding,
			final TypeRapidBean bizBeanType) {
		TypeProperty type = null;

		String propTypename = propertyNode.getAttributeValue("@type", "string");
		switch (propTypename.charAt(0)) {
		case 'a':
			if (propTypename.equals("association")
					|| propTypename.equals("associationend")) {
				propTypename = "collection";
			}
			break;
		case 'b':
			// we need "bool" here because of enum PropertyType.
			// boolean is a keyword.
			if (propTypename.equals("boolean")) {
				propTypename = "bool";
			}
			break;
		default:
			break;
		}
		final PropertyType proptype = PropertyType.valueOf(propTypename);
		String classname = null;
		if (proptype == PropertyType.bool) {
			classname = "org.rapidbeans.core.type.TypePropertyBoolean";
		} else {
			classname = "org.rapidbeans.core.type.TypeProperty"
					+ StringHelper.upperFirstCharacter(proptype.name());
		}
		Class<?> clazz;
		try {
			clazz = Class.forName(classname);
		} catch (ClassNotFoundException e) {
			throw new RapidBeansRuntimeException("class " + classname + " not found", e);
		}
		try {
			Constructor<TypeProperty> constr = (Constructor<TypeProperty>)
					clazz.getConstructor(BBPROPTYPE_CONSTRUCTOR_TYPES);
			Object[] oa = { new XmlNode[] { propertyNode, propertyNodeXmlBinding }, bizBeanType };
			type = (TypeProperty) constr.newInstance(oa);
		} catch (NoSuchMethodException e) {
			throw new RapidBeansRuntimeException("failed to initialize BBProp of Class \""
					+ clazz.getName() + "\".\n" + "Constructor (XmlNode) not found.");
		} catch (IllegalAccessException e) {
			throw new RapidBeansRuntimeException("failed to initialize BBProp of Class \"" + clazz.getName() + "\".\n"
					+ "IllegalAccessException while calling the constructor.");
		} catch (InstantiationException e) {
			throw new RapidBeansRuntimeException("failed to initialize BBProp of Class \"" + clazz.getName() + "\".\n"
					+ "InstatiationException while calling the constructor.");
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			if (t instanceof ExceptionInInitializerError) {
				t = ((ExceptionInInitializerError) t).getException();
			}
			if (t instanceof RuntimeException) {
				throw ((RuntimeException) t);
			}
			throw new RapidBeansRuntimeException("failed to initialize BBProp of Class \"" + clazz.getName() + "\".\n"
					+ "InvocationTargetException caused by " + t.getClass().getName() + " \"" + t.getMessage()
					+ "\"" + " while calling the constructor.");
		}
		return type;
	}

	/**
	 * constant for constructor arguments of class TypeProperty.
	 */
	private static final Class<?>[] BBPROPTYPE_CONSTRUCTOR_TYPES = {
			XmlNode[].class,
			TypeRapidBean.class
	};

	/**
	 * constructor.
	 * 
	 * @param typeId
	 *            the property's type id { string, choice, ... }
	 * @param propertyNodes
	 *            the XML nodes describing the property.
	 * @param parentType
	 *            the parent bean type
	 */
	protected TypeProperty(final String typeId, final XmlNode[] propertyNodes,
			final TypeRapidBean parentType) {

		final XmlNode propertyNode = propertyNodes[0];

		this.propName = propertyNode.getAttributeValue("@name");
		if (this.propName == null) {
			throw new RapidBeansRuntimeException("Property defined without name");
		}
		this.parentBeanType = parentType;

		String s = propertyNode.getAttributeValue("@depends");
		if (s != null) {
			for (final String sdep : StringHelper.split(s, ",")) {
				this.dependentFromProps.add(
						this.getParentBeanType().getPropertyType(sdep));
			}
			if (this.dependentFromProps.size() > 0) {
				this.transientProp = true;
				this.readonly = true;
			}
		}

		// make the first character upper case
		String classname;
		s = propertyNode.getAttributeValue("@propclass");
		if (getDependentFromProps().size() > 0) {
			classname = "org.rapidbeans.core.basic.PropertyReflectImpl";
		} else if (s == null) {
			classname = "org.rapidbeans.core.basic.Property" + typeId;
		} else {
			classname = s;
			if (!s.contains(".") && this.getParentBeanType() != null) {
				final String packageName = this.getParentBeanType().getPackageName();
				if (packageName != null) {
					classname = packageName + "." + s;
				}
			}
		}
		try {
			this.propClass = Class.forName(classname);
		} catch (ClassNotFoundException e) {
			throw new RapidBeansRuntimeException("class \"" + classname + "\"not found");
		}

		s = propertyNode.getAttributeValue("@key");
		if (s != null) {
			this.key = Boolean.parseBoolean(s);
			if (this.key) {
				this.mandatory = true;
			}
		}

		s = propertyNode.getAttributeValue("@readonly");
		if (s != null) {
			this.readonly = Boolean.parseBoolean(s);
		}

		s = propertyNode.getAttributeValue("@mandatory");
		if (s != null) {
			this.mandatory = Boolean.parseBoolean(s);
		}

		s = propertyNode.getAttributeValue("@final");
		if (s != null) {
			this.isFinal = Boolean.parseBoolean(s);
		}

		s = propertyNode.getAttributeValue("@transient");
		if (s != null) {
			this.transientProp = Boolean.parseBoolean(s);
		}

		this.description = RapidBeansType.readDescription(propertyNode);

		if (propertyNodes.length > 1 && propertyNodes[1] != null) {
			this.evalXmlBinding(propertyNodes[1]);
		}
	}

	/**
	 * Evaluate the property type's XML binding description.
	 * 
	 * @param xmlBindingDescr
	 *            the XML Node with the binding description
	 */
	private void evalXmlBinding(final XmlNode propertyNodeXmlBinding) {
		final String s = propertyNodeXmlBinding.getAttributeValue("@bindingtype");
		if (s != null) {
			this.xmlBindingType = PropertyXmlBindingType.valueOf(s);
		}
		final Collection<XmlNode> subnodes = propertyNodeXmlBinding.getSubnodes("beantype");
		if (subnodes == null) {
			return;
		}
		for (final XmlNode currentNode : subnodes) {
			final String typename = currentNode.getAttributeValue("@name");
			if (typename == null || typename.length() == 0) {
				throw new RapidBeansRuntimeException("wrong XML binding, beantype name missing");
			}
			TypeRapidBean type = null;
			try {
				type = TypeRapidBean.forName(typename);
			} catch (TypeNotFoundException e) {
				throw new RapidBeansRuntimeException("Invalid XML binding, beantype name \""
						+ typename + "\" unknown", e);
			}
			final String xmlelement = currentNode.getAttributeValue("@xmlelement");
			if (this.xmlBindTypeToXml == null) {
				this.xmlBindTypeToXml = new HashMap<TypeRapidBean, String>();
				this.xmlBindXmlToType = new HashMap<String, TypeRapidBean>();
			}
			this.xmlBindTypeToXml.put(type, xmlelement);
			this.xmlBindXmlToType.put(xmlelement, type);
		}
	}

	protected void evalXmlBinding(
			final TypeRapidBean beantype, final XmlNode propNode) {
	}

	/**
	 * default parsing method for default values.
	 * 
	 * @param xmlNode
	 *            the XML DOM node with the property type description.
	 */
	protected void parseDefaultValue(final XmlNode xmlNode) {
		final String defaultValueString = xmlNode.getAttributeValue("@default");
		if (defaultValueString != null) {
			this.setDefaultValue(defaultValueString);
		}
	}

	protected void throwModelValidationException(final String message) {
		String completeMessage;
		if (this.getParentBeanType() != null) {
			completeMessage = "Error in specification of bean type"
					+ "\"" + this.getParentBeanType().getName() + "\", property "
					+ "\"" + this.getPropName() + ":\n" + message;
		} else {
			completeMessage = "Error in specification of property "
					+ "\"" + this.getPropName() + ":\n" + message;
		}
		throw new ModelValidationException(completeMessage);
	}
}
