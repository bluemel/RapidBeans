/*
 * Rapid Beans Framework: Property.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/22/2005
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

package org.rapidbeans.core.basic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.MissingResourceException;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.common.ThreadLocalProperties;
import org.rapidbeans.core.event.PropertyChangeEvent;
import org.rapidbeans.core.event.PropertyChangeEventType;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationInstanceAssocTwiceException;
import org.rapidbeans.core.exception.ValidationMandatoryException;
import org.rapidbeans.core.exception.ValidationReadonlyException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.ClassHelper;
import org.rapidbeans.core.util.StringHelper;

/**
 * The base class for every bean property.
 * Bean properties encapsulate value objects
 * and come with the following features:<br/>
 * - They can be accessed in a generic manner (Object getValue(), void setValue(Object))<br/>
 * - New property values are always validated before being set event if they're set internally<br/>
 * - Every property can be converted from (parse) or to (format) a normal string representation<br/>
 * - They ensure immutability in case the value objects are mutable (for example date)<br/>
 * <br/>
 * The following basic property types are supported: <li><b><code>association (end)</code></b> a collection of references to other beans (Links)</li> <li><b><code>boolean&nbsp&nbsp&nbsp&nbsp</code></b>a boolean value ('true' or 'false')</li> <li><b><code>choice&nbsp&nbsp&nbsp&nbsp&nbsp</code></b>(multiple) choice of enum entries (RapidEnum)</li> <li><b><code>date&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</code></b>a date</li> <li><b><code>decimal (not yet implemented)&nbsp&nbsp&nbsp&nbsp</code>
 * </b>numeric value stored in decimal format (BigDecimal)</li> <li><b><code>file&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</code></b>File or Directory</li> <li><b><code>float (not yet implemented)&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</code></b>numeric value stored in floating point format (double)</li> <li><b><code>integer&nbsp&nbsp&nbsp&nbsp</code></b>numeric value stored in integer format (long)</li> <li><b><code>quantity&nbsp&nbsp&nbsp</code></b>quantity consisting in magnitude (Number) and unit (RapidEnum)</li>
 * <li><b><code>string&nbsp&nbsp;&nbsp&nbsp&nbsp</code></b> a (Java) string consisting of Unicode characters (internal encoding: UTF-16)</li> <br/>
 * <br/>
 * Please note the this schema could be extended in the future and can
 * be also extended by yourself. That means you can invent, implement and
 * use your own property types deriving one of these basic property types. <br/>
 * <br/>
 * 
 * @see RapidBean
 * 
 * @author Martin Bluemel
 */
public abstract class Property
		implements Cloneable, Comparable<Property> {

	/**
	 * the property's type.
	 */
	private TypeProperty type = null;

	/**
	 * @return the property's type.
	 */
	public final TypeProperty getType() {
		return this.type;
	}

	/**
	 * Simple and very convenient.
	 * 
	 * @return the property name.
	 */
	public final String getName() {
		return this.getType().getPropName();
	}

	/**
	 * the parent bean.
	 */
	private RapidBean bean = null;

	/**
	 * @return the parent bean.
	 */
	public RapidBean getBean() {
		return this.bean;
	}

	/**
	 * @return the Property's String value.
	 */
	public abstract Object getValue();

	/**
	 * set the Property's value.
	 * 
	 * @param value
	 *            the value object.
	 */
	public abstract void setValue(final Object value);

	/**
	 * every subclass must deliver it's specific implementation of toString().
	 * 
	 * @return the String representation of the property's value
	 */
	public abstract String toString();

	/**
	 * constructor for a new Property object.
	 * 
	 * @param propType
	 *            the Property type
	 * @param parentBean
	 *            the parent bean
	 */
	protected Property(final TypeProperty propType, final RapidBean parentBean) {
		this.bean = parentBean;
		this.type = propType;
		if (!(this.bean instanceof RapidBeanImplSimple)) {
			try {
				ThreadLocalValidationSettings.mandatoryOff();
				ThreadLocalValidationSettings.readonlyOff();
				if (!isDependent()) {
					setValue(propType.getDefaultValue());
				}
			} finally {
				ThreadLocalValidationSettings.remove();
			}
		}
	}

	/**
	 * constant.
	 */
	private static final Class<?>[] CONSTRUCTOR_PARAM_TYPES = { TypeProperty.class, RapidBean.class };

	/**
	 * factory method for a property with given typeinfo (metainfo).
	 * 
	 * @param type
	 *            the type info (constraints) for this property
	 * @param parentBean
	 *            the parent bean
	 * 
	 * @return the created Property instance
	 *         <p>
	 *         throws <b>RapidBeansRuntimeException:</b> thrown if creation fails
	 *         </p>
	 */
	public static Property createInstance(final TypeProperty type, final RapidBean parentBean) {

		Property prop = null;
		Class<?> propClass = type.getPropClass();

		// construct property via reflection
		try {
			Constructor<?> constructor = propClass.getConstructor(CONSTRUCTOR_PARAM_TYPES);
			Object[] oa = new Object[2];
			oa[0] = type;
			oa[1] = parentBean;
			prop = (Property) constructor.newInstance(oa);
		} catch (NoSuchMethodException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (InstantiationException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RapidBeansRuntimeException(e);
		}

		return prop;
	}

	/**
	 * generic value converter.
	 * 
	 * @param value
	 *            the value object to convert. Every property has to support at
	 *            least the internal representation or if a primitive type the
	 *            corresponding value object and String (for serialization).
	 * 
	 * @return the converted value which is the internal representation or if a
	 *         primitive type the corresponding value object
	 */
	public abstract Object convertValue(Object value);

	/**
	 * Check if the property's (initial) value is valid.
	 * Remember that properties never are allowed to get invalid values.
	 */
	public void validate() {
		validate(getValue());
	}

	/**
	 * The basic property validation. Should be called with super.validate
	 * by every overloaded validate method.<br>
	 * - checks a write lock (read only)<br>
	 * - check if an empty (null) value is to be written to a key or mandatory property<br>
	 * - delegate validation to all components.
	 * 
	 * @param newValue
	 *            the value object to validate
	 */
	public Object validate(final Object newValue) {

		final Object newValConverted = this.convertValue(newValue);

		if (!ThreadLocalValidationSettings.getValidation()) {
			return newValConverted;
		}

		// check if the property may be written
		//        System.out.println("@@@ readonly = " + this.getReadonly());
		//        System.out.println("@@@ check = " + ThreadLocalValidationSettings.getReadonlyCheck());
		if (this.getReadonly()
				&& ThreadLocalValidationSettings.getReadonlyCheck()) {
			throw new ValidationReadonlyException(
					"invalid.prop.readonly.proptype",
					this,
					"Property \"" + this.getName() + "\" must not be written at all.");
		}

		// check if a null (= not defined) value is
		// to be written to a mandatory property
		if (newValConverted == null && this.type.getMandatory()
				&& ThreadLocalValidationSettings.getMandatoryCheck()) {
			if (this.getBean() != null) {
				throw new ValidationMandatoryException(
						"invalid.prop.mandatory",
						this,
						"Bean \"" + this.getBean().getType().getName() + "::"
								+ this.getBean().toString() + ", "
								+ "Property \"" + this.getType().getPropName() + "\": "
								+ "an empty (null) value must not be written"
								+ " to a mandatory property");
			} else {
				throw new ValidationMandatoryException(
						"invalid.prop.mandatory",
						this,
						"Property \"" + this.getType().getPropName() + "\": "
								+ "an empty (null) value must not be written"
								+ " to a mandatory property");
			}
		}

		return newValConverted;
	}

	/**
	 * the comparison of two properties. Handles empty values and delegates
	 * comparison to the value objects if both are not empty.
	 * 
	 * @param prop
	 *            the other Property to compare this Property with
	 * 
	 * @return -1: the comparison result as integer, this.value &lt; o.value, 1:
	 *         this.value &gt; o.value, 0: this.value == o.value
	 */
	@SuppressWarnings("unchecked")
	public int compareTo(final Property prop) {
		int compResult = 0;

		Object v1 = this.getValue();
		Object v2 = prop.getValue();

		if ((v1 == null) && (v2 == null)) {
			return 0;
		} else if ((v1 == null) && (v2 != null)) {
			return -1;
		} else if ((v1 != null) && (v2 == null)) {
			return 1;
		} else {
			if (v1 instanceof Boolean) {
				boolean b1 = ((Boolean) v1).booleanValue();
				boolean b2 = ((Boolean) v2).booleanValue();
				if (!b1) {
					if (!b2) {
						return 0;
					} else {
						return -1;
					}
				} else {
					if (b2) {
						return 0;
					} else {
						return 1;
					}
				}
			}

			if ((v1 instanceof Comparable)) {
				compResult = ((Comparable<Object>) v1).compareTo(v2);
			} else {
				if (v1 instanceof Collection && v2 instanceof Collection) {
					Collection<?> c1 = (Collection<?>) v1;
					Collection<?> c2 = (Collection<?>) v2;
					Iterator<?> i2 = c2.iterator();
					Object o2;
					for (Object o1 : c1) {
						o2 = i2.next();
						if (o1 instanceof RapidBean) {
							try {
								compResult = ((RapidBean) o1).getId().compareTo(((RapidBean) o2).getId());
							} catch (ClassCastException e) {
								if (o1 instanceof LinkFrozen) {
									throw new RapidBeansRuntimeException("This bean property "
											+ this.getBean().getType().getName() + "::"
											+ this.getBean().getIdString() + "." + this.getName()
											+ ", link to " + ((LinkFrozen) o1).getIdString() + " is not resolved.");
								}
								if (o2 instanceof LinkFrozen) {
									throw new RapidBeansRuntimeException("Other bean property "
											+ prop.getBean().getType().getName() + "::"
											+ prop.getBean().getIdString() + "." + prop.getName()
											+ ", link to bean "
											+ ((TypePropertyCollection) prop.getType()).getTargetType().getName()
											+ "::" + o2.toString() + " is not resolved.");
								}
								throw e;
							}
						} else {
							compResult = ((Comparable<Object>) o1).compareTo(o2);
						}
						if (compResult != 0) {
							break;
						}
					}
				} else {
					throw new RapidBeansRuntimeException("Value objects v1 (class \""
							+ v1.getClass().getName() + "\") and v2 (class \""
							+ v2.getClass().getName() + "\") are not comparable.\n"
							+ "Prop 1: " + this.getBean().getIdString() + "." + this.getType().getPropName() + "\n"
							+ "Prop 2: " + prop.getBean().getIdString() + "." + prop.getType().getPropName() + "\n");
				}
			}
		}
		return compResult;
	}

	/**
	 * the test for equality of two properties.
	 * Handles empty values and delegates test to the value objects if both are not empty.
	 * 
	 * @param o
	 *            the property to compare this property with
	 * 
	 * @return a boolean flag that indicates equality or not
	 */
	public boolean equals(final Object o) {
		if (ClassHelper.classOf(this.getClass(), o.getClass())) {
			final Object v2 = ((Property) o).getValue();
			if (this == o) {
				return true;
			}
			if (!(o instanceof Property)) {
				return false;
			}
			if ((this.getValue() == null) && (v2 == null)) {
				// both values are empty => they're equal
				return true;
			} else if ((this.getValue() == null) ^ (v2 == null)) {
				// only one value is empty
				return false;
			} else {
				// no value is empty => test for equality
				return this.getValue().equals(v2);
			}
		} else {
			return false;
		}
	}

	/**
	 * @return hash code.
	 */
	public int hashCode() {
		if (this.toString() == null) {
			return "null#RapidBean".hashCode();
		} else {
			return this.toString().hashCode();
		}
	}

	/**
	 * Determines if this property may be written.
	 * 
	 * @return if this property may be written.
	 */
	public boolean getReadonly() {
		if (this.type.getReadonly()) {
			return true;
		}
		final RapidBean bean = this.getBean();
		if (bean != null) {
			final Container container = bean.getContainer();
			if (container != null && container.getReadonly()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * clones a Property's value.
	 * 
	 * @param pClone
	 *            the property that receives the cloned value
	 * @param cloneContainer
	 *            the container for the cloned property's sub beans
	 * 
	 * @return the cloned property
	 */
	@SuppressWarnings("unchecked")
	public Property cloneValue(final Property pClone,
			final Container cloneContainer) {
		boolean threadLocPropSet = ThreadLocalProperties.set(
				"bean.setparentbean.donotchangeids", true);
		try {
			if (this instanceof PropertyCollection) {
				Collection<Link> colBeans = (Collection<Link>) this.getValue();
				if (colBeans == null) {
					pClone.setValue(null);
				} else {
					Collection<Link> clonedBeans = new ArrayList<Link>();
					if (((TypePropertyCollection) this.getType()).isComposition()) {
						for (Object o : colBeans) {
							if (o instanceof RapidBean) {
								clonedBeans.add(((RapidBean) o).cloneExternal(cloneContainer));
							} else {
								LinkFrozen lf = (LinkFrozen) o;
								clonedBeans.add(lf.clone());
							}
						}
					} else {
						for (Object o : colBeans) {
							if (o instanceof RapidBean) {
								clonedBeans.add(new LinkFrozen((RapidBean) o));
							} else {
								clonedBeans.add(((LinkFrozen) o).clone());
							}
						}
					}
					try {
						ThreadLocalValidationSettings.validationOff();
						((PropertyCollection) pClone).setValue(clonedBeans, true, true);
					} finally {
						ThreadLocalValidationSettings.remove();
					}
				}
			} else {
				try {
					ThreadLocalValidationSettings.validationOff();
					pClone.setValue(this.getValue());
				} finally {
					ThreadLocalValidationSettings.remove();
				}
			}
		} catch (ValidationInstanceAssocTwiceException e) {
			// do not do anything else
			return pClone;
		} finally {
			if (threadLocPropSet) {
				ThreadLocalProperties.unset(
						"bean.setparentbean.donotchangeids");
			}
		}
		return pClone;
	}

	/**
	 * clones a property.
	 * 
	 * @param parentBean
	 *            the parent bean.
	 * 
	 * @return the cloned property
	 */
	public Property clone(final RapidBean parentBean) {
		Property pClone = null;
		try {
			pClone = Property.createInstance(this.type, parentBean);
			ThreadLocalValidationSettings.validationOff();
			if (this instanceof PropertyCollection) {
				((PropertyCollection) pClone).setValue(this.getValue(), false, true);
			} else {
				if (!isDependent()) {
					pClone.setValue(this.getValue());
				}
			}
		} catch (ValidationInstanceAssocTwiceException e) {
			// do not do anything else
		} finally {
			ThreadLocalValidationSettings.remove();
		}
		return pClone;
	}

	/**
	 * @param locale
	 *            the Locale
	 * @return a string for the property's value for UI
	 */
	public String toStringGui(final RapidBeansLocale locale) {
		if (this.getValue() == null) {
			return "";
		} else {
			return this.toString();
		}
	}

	/**
	 * @param locale
	 *            the Locale
	 * @return a string for the propertie's name UI
	 */
	public String getNameGui(final RapidBeansLocale locale) {
		return getNameGuiS(this.getType(), this.getBean().getType(), locale);
	}

	/**
	 * @param locale
	 *            the Locale
	 * @return a string for the propertie's name UI
	 */
	public static String getNameGuiS(final TypeProperty propType,
			final TypeRapidBean beanType,
			final RapidBeansLocale locale) {
		String text = null;
		if (text == null) {
			if (beanType != null) {
				try {
					final String key = "bean."
							+ beanType.getName().toLowerCase()
							+ ".prop." + propType.getPropName().toLowerCase();
					text = locale.getStringGui(key);
				} catch (MissingResourceException e) {
					text = null;
				}
			}
		}

		if (text == null && propType instanceof TypePropertyCollection) {
			try {
				TypePropertyCollection colPropType = (TypePropertyCollection) propType;
				String pattern = "bean."
						+ colPropType.getTargetType().getName().toLowerCase();
				if (colPropType.getMaxmult() != 1) {
					pattern += ".plural";
				}
				text = locale.getStringGui(pattern);
			} catch (MissingResourceException e) {
				text = null;
			}
		}

		if (text == null && beanType.getSupertype() != null
				&& beanType.getSupertype().getPropertyType(propType.getPropName()) != null) {
			final TypeRapidBean beanSupertype = beanType.getSupertype();
			final TypeProperty propSupertype =
					beanSupertype.getPropertyType(propType.getPropName());
			text = getNameGuiS(propSupertype, beanSupertype, locale);
		}

		if (text == null) {
			text = propType.getPropName();
		}
		return text;
	}

	/**
	 * fire the bean changed event
	 * 
	 * @param prop
	 *            the property
	 * @param changeType
	 *            change type
	 * @param oldVal
	 *            old value
	 * @param newVal
	 *            new value
	 * @param link
	 *            the link
	 */
	protected void fireChangePre(final Property prop,
			final PropertyChangeEventType changeType,
			final Object oldVal, final Object newVal,
			final Link link) {
		if (this.bean != null) {
			final PropertyChangeEvent event = new PropertyChangeEvent(
					prop, oldVal, newVal, changeType, link);
			this.bean.propertyChangePre(event);
		}
	}

	/**
	 * fire the bean changed event
	 * 
	 * @param prop
	 *            the property
	 * @param changeType
	 *            change type
	 * @param oldVal
	 *            old value
	 * @param newVal
	 *            new value
	 * @param link
	 *            the link
	 */
	protected void fireChanged(final Property prop,
			final PropertyChangeEventType changeType,
			final Object oldVal, final Object newVal,
			final Link link) {
		if (this.bean != null
				&& this.bean.getBeanState() != RapidBeanState.initializing) {
			final PropertyChangeEvent event = new PropertyChangeEvent(
					prop, oldVal, newVal, changeType, link);
			this.bean.propertyChanged(event);
		}
	}

	/**
	 * The template method for the pure value change and
	 * firing the events.
	 * 
	 * @param oldValue
	 * @param newValue
	 * @param valueSetter
	 */
	protected void setValueWithEvents(final Object oldValue, final Object newValue,
			final PropertyValueSetter valueSetter) {
		final Object validatedNewValue = validate(newValue);
		if ((validatedNewValue == null && oldValue != null)
				|| (validatedNewValue != null && oldValue == null)
				|| (validatedNewValue != null && oldValue != null
				&& (!oldValue.equals(validatedNewValue)))) {
			fireChangePre(this, PropertyChangeEventType.set,
					oldValue, validatedNewValue, null);
			valueSetter.setValue(validatedNewValue);
			fireChanged(this, PropertyChangeEventType.set,
					oldValue, validatedNewValue, null);
		}
	}

	/**
	 * @return if this is a dependent property
	 */
	public boolean isDependent() {
		return this.type.getDependentFromProps().size() > 0;
	}

	public static void setValueByReflection(final RapidBean bean, final String propname, final Object newValue) {
		try {
			final Field field = bean.getClass().getDeclaredField(propname);
			field.setAccessible(true);
			field.set(bean, newValue);
		} catch (SecurityException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RapidBeansRuntimeException(e);
		}
	}

	public static Object getValueByReflection(final RapidBean bean, final String propname) {
		Method getter;
		try {
			getter = bean.getClass().getMethod("get" + StringHelper.upperFirstCharacter(propname));
			return getter.invoke(bean);
		} catch (SecurityException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RapidBeansRuntimeException(e);
		}
	}
}
