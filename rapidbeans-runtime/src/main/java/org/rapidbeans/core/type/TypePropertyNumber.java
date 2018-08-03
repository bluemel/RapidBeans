/*
 * Rapid Beans Framework: TypePropertyNumber.java
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
import java.lang.reflect.InvocationTargetException;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.util.ClassHelper;
import org.rapidbeans.core.util.XmlNode;

/**
 * the common type class for pure number properties.
 * 
 * @author Martin Bluemel
 */
public abstract class TypePropertyNumber extends TypeProperty {

	/**
	 * the maximal value.
	 */
	private Number maxValue = null;

	/**
	 * @return max value
	 */
	public Number getMaxValue() {
		return this.maxValue;
	}

	/**
	 * @param maxValue the maxValue to set
	 */
	protected void setMaxValue(Number maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * the minimal value.
	 */
	private Number minValue = null;

	/**
	 * @return min value
	 */
	public Number getMinValue() {
		return this.minValue;
	}

	/**
	 * @param minValue the minValue to set
	 */
	protected void setMinValue(Number minValue) {
		this.minValue = minValue;
	}

	/**
	 * The number implementing class.
	 */
	private Class<?> numberClass = null;

	/**
	 * @return the numberClass
	 */
	public Class<?> getNumberClass() {
		return numberClass;
	}

	/**
	 * @param numberClass the numberClass to set
	 */
	protected void setNumberClass(Class<?> numberClass) {
		this.numberClass = numberClass;
	}

	/**
	 * Constructor for TypePropertyNumber.
	 * 
	 * @param typeNameExtension { 'Integer', 'Float', 'Decimal' }
	 * @param xmlNodes          the XML DOM nodes describing this type
	 * @param parentBeanType    the parent bean type
	 */
	public TypePropertyNumber(final String typeNameExtension, final XmlNode[] xmlNodes,
			final TypeRapidBean parentBeanType) {
		super(typeNameExtension, xmlNodes, parentBeanType);

		final String numberClassName = xmlNodes[0].getAttributeValue("@implementation");
		if (numberClassName != null) {
			try {
				this.numberClass = Class.forName(numberClassName);
			} catch (ClassNotFoundException e) {
				throw new RapidBeansRuntimeException(e);
			}
			if (!ClassHelper.classOf(Number.class, this.numberClass)) {
				throw new RapidBeansRuntimeException("Class \"" + this.numberClass + "\" is no Number class.");
			}
		}
	}

	private final static Class<?>[] NUMBER_CONSTRUCTOR_PARAM_TYPES = { String.class };

	/**
	 * Generically construct a Number out of the given string.
	 * 
	 * @param numberString the string encoding the number to construct
	 * @return the new Number instance
	 */
	@SuppressWarnings("unchecked")
	protected Number constructNumber(final String numberString) {
		Number number = null;
		try {
			final Constructor<Number> numberConstructor = (Constructor<Number>) this.numberClass
					.getConstructor(NUMBER_CONSTRUCTOR_PARAM_TYPES);
			number = (Number) numberConstructor.newInstance(new Object[] { numberString });
		} catch (IllegalArgumentException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (InstantiationException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RapidBeansRuntimeException(e);
		}
		return number;
	}
}
