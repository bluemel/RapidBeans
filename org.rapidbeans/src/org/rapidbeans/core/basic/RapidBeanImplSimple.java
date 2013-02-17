/*
 * Rapid Beans Framework: RapidBeanImplSimple.java
 * 
 * Copyright (C) 2012 Martin Bluemel
 * 
 * Creation Date: 08/08/2012
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.exception.ValidationInstanceAssocTwiceException;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.StringHelper;

/**
 * The "simple" bean implementation of the RapidBeans framework.
 * 
 * no extra property instance per attribute
 * + rather memory friendly
 * + easier to debug
 * - not perfectly save against illegal modifications
 * properties are partially not immutable
 * setting properties to illegal values is easier than with the strict beans
 * 
 * @author Martin Bluemel
 */
public abstract class RapidBeanImplSimple
		extends RapidBeanImplParent {

	/**
	 * empty string array.
	 */
	private static final String[] EMPTY_STRING_ARRAY = new String[0];

	/**
	 * parameter types for constructor RapidBean(String).
	 */
	private static final Class<?>[] CONSTR_PARAMTYPES_STRING_ARRAY = { String[].class };

	/**
	 * Creates a new bean instance from a special type.
	 * 
	 * @param typename
	 *            the name of the bean's type
	 * 
	 * @return the new bean instance
	 */
	public static RapidBeanImplSimple createInstance(final String typename) {
		return createInstance(TypeRapidBean.forName(typename));
	}

	/**
	 * Creates a new bean instance from a special type.
	 * 
	 * @param type
	 *            the bean's type
	 * 
	 * @return the new bean instance
	 */
	@SuppressWarnings("unchecked")
	public static RapidBeanImplSimple createInstance(final TypeRapidBean type) {
		RapidBeanImplSimple bean = null;
		Class<?> clazz = type.getImplementingClass();
		if (clazz == null) {
			throw new RapidBeansRuntimeException(
					"RapidBeans simple implementations need a conrete class.");
		} else {
			try {
				Constructor<RapidBeanImplSimple> constr = (Constructor<RapidBeanImplSimple>) clazz
						.getConstructor(CONSTR_PARAMTYPES_STRING_ARRAY);
				Object[] initargs = new Object[1];
				initargs[0] = EMPTY_STRING_ARRAY;
				bean = (RapidBeanImplSimple) constr.newInstance(initargs);
			} catch (NoSuchMethodException e) {
				throw new RapidBeansRuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RapidBeansRuntimeException(e);
			} catch (InstantiationException e) {
				throw new RapidBeansRuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RapidBeansRuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RapidBeansRuntimeException(e);
			}
		}
		return bean;
	}

	/**
	 * constructor without initial values.
	 */
	public RapidBeanImplSimple() {
		this(EMPTY_STRING_ARRAY, null);
	}

	/**
	 * constructor for generic beans without initial values and type.
	 * 
	 * @param type
	 *            the type
	 */
	protected RapidBeanImplSimple(final TypeRapidBean type) {
		this(EMPTY_STRING_ARRAY, type);
	}

	/**
	 * constructor with initial values as one String.
	 * 
	 * @param initvals
	 *            String with initial values braced with quotes separated by a
	 *            blank;
	 */
	public RapidBeanImplSimple(final String initvals) {
		this(initvals, null);
	}

	/**
	 * constructor with initial values as one String and type.
	 * 
	 * @param initvals
	 *            String with initial values braced with quotes separated by a
	 *            blank;
	 * @param type
	 *            the type
	 */
	protected RapidBeanImplSimple(final String initvals,
			final TypeRapidBean type) {
		this(StringHelper.splitQuoted(initvals), type);
	}

	/**
	 * constructor with initial values as String array.
	 * 
	 * @param initvals
	 *            String array with initial values.
	 */
	public RapidBeanImplSimple(final String[] initvals) {
		this(initvals, null);
	}

	/**
	 * constructor with initial values as String array.
	 * 
	 * @param initvals
	 *            String array with initial values.
	 * @param argType
	 *            possibility to give the type in advance
	 */
	protected RapidBeanImplSimple(final String[] initvals,
			final TypeRapidBean argType) {
		final RapidBeanState stateBefore = getBeanState();
		try {
			setBeanState(RapidBeanState.initializing);

			// automatically create child instances for composite children bean classes
			// with minimal multiplicity defined
			for (final PropertyCollection colProp : this.getColPropertiesComposition()) {
				final TypePropertyCollection colPropType = (TypePropertyCollection) colProp.getType();
				final int minmult = colPropType.getMinmult();
				if (minmult > 0) {
					final TypeRapidBean targetType = colPropType.getTargetType();
					for (int j = 0; j < minmult; j++) {
						colProp.addLink(RapidBeanImplSimple.createInstance(targetType));
					}
				}
			}
			initProperties();
			setBeanState(RapidBeanState.initialized);
		} catch (RuntimeException e) {
			setBeanState(stateBefore);
			throw e;
		}
	}

	//	private static final Logger log = Logger
	//			.getLogger(RapidBeanImplSimple.class.getName());

	/**
	 * lazy initialization of the propmap.
	 */
	public void initPropmap() {
		// nothing to do for simple beans
	}

	/**
	 * @return a List with all the bean's Properties
	 */
	public final List<Property> getPropertyList() {
		final List<Property> proplist = new ArrayList<Property>();
		// TODO
		//		for (int i = 0; i < this.properties.length; i++) {
		//			proplist.add(this.properties[i]);
		//		}
		return proplist;
	}

	/**
	 * empty key property array to avoid unnecessary instantiations.
	 */
	public static final Property[] EMPTY_KEYPROP_ARRAY = new Property[0];

	/**
	 * @return a list with all the bean's collection properties
	 */
	public final List<PropertyCollection> getColProperties() {
		final List<PropertyCollection> colproplist = new ArrayList<PropertyCollection>();
		// TODO
		//		for (int i = 0; i < this.properties.length; i++) {
		//			if (ClassHelper.classOf(TypePropertyCollection.class,
		//					this.properties[i].getType().getClass())) {
		//				colproplist.add((PropertyCollection) this.properties[i]);
		//			}
		//		}
		return colproplist;
	}

	/**
	 * @return a list with all the bean's composition collection properties
	 */
	public final List<PropertyCollection> getColPropertiesComposition() {
		final List<PropertyCollection> colproplist = new ArrayList<PropertyCollection>();
		// TODO
		//		for (int i = 0; i < this.properties.length; i++) {
		//			if (ClassHelper.classOf(TypePropertyCollection.class,
		//					this.properties[i].getType().getClass())
		//					&& ((TypePropertyCollection) this.properties[i].getType())
		//							.isComposition()) {
		//				colproplist.add((PropertyCollection) this.properties[i]);
		//			}
		//		}
		return colproplist;
	}

	/**
	 * @param name
	 *            the Property's name.
	 * 
	 * @return the bean's Property with the specified name.
	 */
	public final Property getProperty(final String name) {
		return Property.createInstance(getType().getPropertyType(name), this);
	}

	/**
	 * Convenience getter for a property's value. Encourages not to hold the
	 * bean's property reference for a long time.
	 * 
	 * @param name
	 *            the Property's name
	 * @return the Property's value
	 */
	public final Object getPropValue(final String name) {
		Object value = null;
		try {
			final Method getter = this.getClass().getMethod("get" + StringHelper.upperFirstCharacter(name));
			value = getter.invoke(this);
		} catch (NoSuchMethodException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RapidBeansRuntimeException(e);
		}
		return value;
	}

	/**
	 * Convenience setter for a property's value. Encourages not to hold the
	 * bean's property reference for a long time.
	 * 
	 * @param name
	 *            the Property's name
	 * @param value
	 *            the Property's value to set
	 */
	public final void setPropValue(final String name, final Object value) {
		try {
			final Method setter = this.getClass().getMethod(
					"set" + StringHelper.upperFirstCharacter(name),
					getType().getPropertyType(name).getValuetype());
			setter.invoke(this, value);
		} catch (NoSuchMethodException e) {
			throw new ValidationException("invalid.prop.name", this,
					"unknown property \"" + name + "\" for bean type \""
							+ this.getType().getName() + "\".");
		} catch (IllegalArgumentException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RapidBeansRuntimeException(e);
		}
	}

	/**
	 * @return a clone of this bean.
	 */
	public RapidBean clone() {
		RapidBeanImplSimple bClone = RapidBeanImplSimple.createInstance(this.getType());
		for (int i = 0; i < getPropertyList().size(); i++) {
			bClone.getPropertyList().get(i).setValue(getPropertyList().get(i).clone(bClone));
		}
		bClone.initPropmap();
		bClone.initProperties();
		bClone.setContainer(this.getContainer());
		return bClone;
	}

	/**
	 * @param cloneContainer
	 *            the container for the cloned bean
	 * 
	 * @return a clone of this bean including the whole hierarchy. The container
	 *         in set to null. Non compositions links are frozen.
	 */
	public RapidBean cloneExternal(final Container cloneContainer) {
		RapidBean bClone = RapidBeanImplSimple.createInstance(this.getType());
		bClone.setId(Id.createInstance(bClone, this.getIdString()));
		if (cloneContainer != null && (!cloneContainer.contains(bClone))) {
			((ContainerImpl) cloneContainer).insert(bClone, true);
		}
		bClone.setContainer(cloneContainer);
		for (int i = 0; i < getPropertyList().size(); i++) {
			getPropertyList().get(i).cloneValue(bClone.getPropertyList().get(i), cloneContainer);
		}
		return bClone;
	}

	/**
	 * clones a property.
	 * 
	 * @param parentBean
	 *            the parent bean.
	 * 
	 * @return the cloned property
	 */
	public static Property cloneProperty(final Property property,
			final RapidBean parentBean) {
		Property pClone = null;
		try {
			pClone = Property.createInstance(property.getType(), parentBean);
			ThreadLocalValidationSettings.validationOff();
			if (property instanceof PropertyCollection) {
				((PropertyCollection) pClone).setValue(property.getValue(),
						false, true);
			} else {
				if (!property.isDependent()) {
					pClone.setValue(property.getValue());
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
	 * validate the whole bean.
	 */
	public void validate() {
		for (Property prop : getPropertyList()) {
			ThreadLocalValidationSettings.readonlyOff();
			prop.validate(prop.getValue());
		}
	}
}
