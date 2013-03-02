/*
 * Rapid Beans Framework: Id.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/21/2005
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

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.StringHelper;

/**
 * the common parent class for identity defining objects.
 * 
 * @author Martin Bluemel
 */
public abstract class Id implements Comparable<Id> {

	/**
	 * constant used for reflective instantiation.
	 */
	private static final Class<?>[] CONSTR_PARAMTYPES_BIZBEAN_STRING = {
			RapidBean.class, String.class };

	/**
	 * factory method.
	 * 
	 * @param idString
	 *            the id in String form.
	 * @param bean
	 *            the parent bean for this id.
	 * @return the id instance
	 */
	public static Id createInstance(final RapidBean bean, final String idString) {
		Id id = null;
		final TypeRapidBean type = bean.getType();
		IdType idtype = type.getIdtype();
		String classname = null;
		try {
			classname = "org.rapidbeans.core.basic.Id"
					+ StringHelper.upperFirstCharacter(idtype.name());
			final Class<?> clazz = Class.forName(classname);
			final Constructor<?> constr = clazz
					.getConstructor(CONSTR_PARAMTYPES_BIZBEAN_STRING);
			final Object[] initargs = { bean, idString };
			id = (Id) constr.newInstance(initargs);
		} catch (ClassNotFoundException e) {
			throw new RapidBeansRuntimeException("Id class \"" + classname
					+ "\" not found");
		} catch (NoSuchMethodException e) {
			throw new RapidBeansRuntimeException("Id class \"" + classname
					+ "\" does not have a constructor with String");
		} catch (IllegalArgumentException e) {
			throw new RapidBeansRuntimeException(
					"IllegalArgumentException while trying"
							+ " to create instance for Id class \"" + classname,
					e);
		} catch (InstantiationException e) {
			throw new RapidBeansRuntimeException(
					"InstantiationException while trying"
							+ " to create instance for Id class \"" + classname,
					e);
		} catch (IllegalAccessException e) {
			throw new RapidBeansRuntimeException(
					"IllegalAccessException while trying"
							+ " to create instance for Id class \"" + classname,
					e);
		} catch (InvocationTargetException e) {
			throw new RapidBeansRuntimeException(
					"InvocationTargetException while trying"
							+ " to create instance for Id class \"" + classname,
					e);
		}
		return id;
	}

	/**
	 * every id must implement toString.
	 * 
	 * @return the id's string representation
	 */
	public abstract String toString();

	/**
	 * default implementation for hashCode.
	 * 
	 * @return the hash code
	 */
	public int hashCode() {
		return this.toString().hashCode();
	}

	/**
	 * default implementation to compare the string representations.
	 * 
	 * @param o
	 *            the object to compare
	 * 
	 * @return if equals or not
	 */
	public boolean equals(final Object o) {
		return this.toString().equals(o.toString());
	}

	/**
	 * comparison default implementation.
	 * 
	 * @param o
	 *            the Id to compare with
	 * 
	 * @return the comparison result -1, 0, 1
	 */
	public int compareTo(final Id id) {
		return this.toString().compareTo(id.toString());
	}
}
