/*
 * Rapid Beans Framework: RapidBeanImplStrict.java
 * 
 * Copyright (C) 2012 Martin Bluemel
 * 
 * Creation Date: 06/20/2012
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.exception.ValidationInstanceAssocTwiceException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.ClassHelper;
import org.rapidbeans.core.util.StringHelper;

/**
 * The "classical" bean implementation of the RapidBeans framework.
 * 
 * * one extra property instance per attribute + quite save against illegal
 * modifications * properties are absolutely immutable * setting properties to
 * illegal values requires extra effort to switch of validation + potentially
 * higher dynamics possible (morphic objects not yet implemented) - not so
 * memory friendly - harder to debug
 * 
 * @author Martin Bluemel
 */
public abstract class RapidBeanImplStrict extends RapidBeanImplParent {

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
	 * @param typename the name of the bean's type
	 * 
	 * @return the new bean instance
	 */
	public static RapidBeanImplStrict createInstance(final String typename) {
		return createInstance(TypeRapidBean.forName(typename));
	}

	/**
	 * Creates a new bean instance from a special type.
	 * 
	 * @param type the bean's type
	 * 
	 * @return the new bean instance
	 */
	@SuppressWarnings("unchecked")
	public static RapidBeanImplStrict createInstance(final TypeRapidBean type) {
		RapidBeanImplStrict bean = null;
		Class<?> clazz = type.getImplementingClass();
		if (clazz == null) {
			bean = new GenericBean(type);
		} else {
			try {
				Constructor<RapidBeanImplStrict> constr = (Constructor<RapidBeanImplStrict>) clazz
						.getConstructor(CONSTR_PARAMTYPES_STRING_ARRAY);
				Object[] initargs = new Object[1];
				initargs[0] = EMPTY_STRING_ARRAY;
				bean = (RapidBeanImplStrict) constr.newInstance(initargs);
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
	public RapidBeanImplStrict() {
		this(EMPTY_STRING_ARRAY, null);
	}

	/**
	 * constructor for generic beans without initial values and type.
	 * 
	 * @param type the type
	 */
	protected RapidBeanImplStrict(final TypeRapidBean type) {
		this(EMPTY_STRING_ARRAY, type);
	}

	/**
	 * constructor with initial values as one String.
	 * 
	 * @param initvals String with initial values braced with quotes separated by a
	 *                 blank;
	 */
	public RapidBeanImplStrict(final String initvals) {
		this(initvals, null);
	}

	/**
	 * constructor with initial values as one String and type.
	 * 
	 * @param initvals String with initial values braced with quotes separated by a
	 *                 blank;
	 * @param type     the type
	 */
	protected RapidBeanImplStrict(final String initvals, final TypeRapidBean type) {
		this(StringHelper.splitQuoted(initvals), type);
	}

	/**
	 * constructor with initial values as String array.
	 * 
	 * @param initvals String array with initial values.
	 */
	public RapidBeanImplStrict(final String[] initvals) {
		this(initvals, null);
	}

	/**
	 * constructor with initial values as String array.
	 * 
	 * @param initvals String array with initial values.
	 * @param argType  possibility to give the type in advance
	 */
	protected RapidBeanImplStrict(final String[] initvals, final TypeRapidBean argType) {
		final RapidBeanState stateBefore = getBeanState();
		try {
			setBeanState(RapidBeanState.initializing);

			// initialize this bean's properties
			TypeRapidBean type;
			if (argType != null) {
				type = argType;
			} else {
				type = this.getType();
			}
			final Collection<TypeProperty> proptypes = type.getPropertyTypes();
			final int propcount = proptypes.size();
			Property prop;
			this.properties = new Property[propcount];
			int i = 0;
			for (TypeProperty proptype : proptypes) {
				prop = Property.createInstance(proptype, this);
				this.properties[i] = prop;
				i++;
			}
			this.initProperties();
			final int lenInitvals = initvals.length;
			for (int j = 0; j < this.properties.length; j++) {
				if (j < lenInitvals) {
					if (!this.properties[j].isDependent()) {
						try {
							this.properties[j].setValue(initvals[j]);
						} catch (ValidationException e) {
							throw e;
						}
					}
				}
			}

			// automatically create child instances for composite children bean
			// classes
			// with minimal multiplicity defined
			for (final PropertyCollection colProp : this.getColPropertiesComposition()) {
				final TypePropertyCollection colPropType = (TypePropertyCollection) colProp.getType();
				final int minmult = colPropType.getMinmult();
				if (minmult > 0) {
					final TypeRapidBean targetType = colPropType.getTargetType();
					for (int j = 0; j < minmult; j++) {
						colProp.addLink(RapidBeanImplStrict.createInstance(targetType));
					}
				}
			}
			setBeanState(RapidBeanState.initialized);
		} catch (RuntimeException e) {
			setBeanState(stateBefore);
			throw e;
		}
	}

	// private static final Logger log = Logger.getLogger(
	// RapidBeanImplStrict.class.getName());

	/**
	 * The property container. Implements the composition.
	 */
	private Property[] properties = null;

	/**
	 * The map just optimizes finding by name.
	 */
	private HashMap<String, Property> propmap = null;

	/**
	 * lazy initialization of the propmap.
	 */
	public void initPropmap() {
		this.propmap = new HashMap<String, Property>();
		for (int i = 0; i < this.properties.length; i++) {
			this.propmap.put(this.properties[i].getType().getPropName(), this.properties[i]);
		}
	}

	/**
	 * @return a List with all the bean's Properties
	 */
	public final List<Property> getPropertyList() {
		final List<Property> proplist = new ArrayList<Property>();
		for (int i = 0; i < this.properties.length; i++) {
			proplist.add(this.properties[i]);
		}
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
		for (int i = 0; i < this.properties.length; i++) {
			if (ClassHelper.classOf(TypePropertyCollection.class, this.properties[i].getType().getClass())) {
				colproplist.add((PropertyCollection) this.properties[i]);
			}
		}
		return colproplist;
	}

	/**
	 * @return a list with all the bean's composition collection properties
	 */
	public final List<PropertyCollection> getColPropertiesComposition() {
		final List<PropertyCollection> colproplist = new ArrayList<PropertyCollection>();
		for (int i = 0; i < this.properties.length; i++) {
			if (ClassHelper.classOf(TypePropertyCollection.class, this.properties[i].getType().getClass())
					&& ((TypePropertyCollection) this.properties[i].getType()).isComposition()) {
				colproplist.add((PropertyCollection) this.properties[i]);
			}
		}
		return colproplist;
	}

	/**
	 * @param name the Property's name.
	 * 
	 * @return the bean's Property with the specified name.
	 */
	public final Property getProperty(final String name) {
		if (this.propmap == null) {
			this.initPropmap();
		}
		return this.propmap.get(name);
	}

	/**
	 * Convenience getter for a property's value. Encourages not to hold the bean's
	 * property reference for a long time.
	 * 
	 * @param name the Property's name
	 * @return the Property's value
	 */
	public final Object getPropValue(final String name) {
		if (this.propmap == null) {
			this.initPropmap();
		}
		Property prop = this.propmap.get(name);
		if (prop == null) {
			throw new ValidationException("invalid.prop.name", this,
					"unknown property \"" + name + "\" for bean type \"" + this.getType().getName() + "\".");
		}
		return prop.getValue();
	}

	/**
	 * Convenience setter for a property's value. Encourages not to hold the bean's
	 * property reference for a long time.
	 * 
	 * @param name  the Property's name
	 * @param value the Property's value to set
	 */
	public final void setPropValue(final String name, final Object value) {
		if (this.propmap == null) {
			this.initPropmap();
		}
		Property prop = this.propmap.get(name);
		if (prop == null) {
			throw new ValidationException("invalid.prop.name", this,
					"unknown property \"" + name + "\" for bean type \"" + this.getType().getName() + "\".");
		}
		prop.setValue(value);
	}

	/**
	 * @return a clone of this bean.
	 */
	public RapidBean clone() {
		RapidBeanImplStrict bClone = RapidBeanImplStrict.createInstance(this.getType());
		for (int i = 0; i < this.properties.length; i++) {
			bClone.properties[i] = this.properties[i].clone(bClone);
		}
		bClone.initPropmap();
		bClone.initProperties();
		bClone.setContainer(this.getContainer());
		return bClone;
	}

	/**
	 * @param cloneContainer the container for the cloned bean
	 * 
	 * @return a clone of this bean including the whole hierarchy. The container in
	 *         set to null. Non compositions links are frozen.
	 */
	public RapidBean cloneExternal(final Container cloneContainer) {
		RapidBean bClone = RapidBeanImplStrict.createInstance(this.getType());
		bClone.setId(Id.createInstance(bClone, this.getIdString()));
		if (cloneContainer != null && (!cloneContainer.contains(bClone))) {
			((ContainerImpl) cloneContainer).insert(bClone, true);
		}
		bClone.setContainer(cloneContainer);
		for (int i = 0; i < this.properties.length; i++) {
			this.properties[i].cloneValue(bClone.getPropertyList().get(i), cloneContainer);
		}
		return bClone;
	}

	/**
	 * clones a property.
	 * 
	 * @param parentBean the parent bean.
	 * 
	 * @return the cloned property
	 */
	public static Property cloneProperty(final Property property, final RapidBean parentBean) {
		Property pClone = null;
		try {
			pClone = Property.createInstance(property.getType(), parentBean);
			ThreadLocalValidationSettings.validationOff();
			if (property instanceof PropertyCollection) {
				((PropertyCollection) pClone).setValue(property.getValue(), false, true);
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
		for (Property prop : this.properties) {
			ThreadLocalValidationSettings.readonlyOff();
			prop.validate(prop.getValue());
		}
	}
}
