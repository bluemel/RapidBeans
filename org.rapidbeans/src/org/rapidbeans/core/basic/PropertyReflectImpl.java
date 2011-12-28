/*
 * Rapid Beans Framework: PropertyReflectImpl.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 10/08/2010
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.util.StringHelper;


/**
 * Reflect over the properties.<br/>
 *
 * @author Martin Bluemel
 */
public class PropertyReflectImpl extends Property {

    /**
     * constructor for a new reflection Property.
     *
     * @param type the Property's type
     * @param parentBean the parent bean
     */
    public PropertyReflectImpl(final TypeProperty type, final RapidBean parentBean) {
        super(type, parentBean);
    }

    /**
     * generic value getter.
     *
     * @return the String value of this Property
     */
    public Object getValue() {
        Object ret = null;
        final Class<?> clazz = getBean().getType().getImplementingClass();
        final String getterMethodName = "get"
            + StringHelper.upperFirstCharacter(getName());
        try {
            final Method method = clazz.getMethod(getterMethodName);
            ret = method.invoke(getBean());
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {            
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return ret;
    }

    /**
     * String value getter.
     *
     * @return the String representation of the Property's value.<br/>
     * For a String this is the value itself.
     */
    public String toString() {
        if (getValue() == null) {
            return null;
        }
        return this.getValue().toString();
    }

    /**
     * generic value setter.
     *
     * @param newValue the new value for this property.<br/>
     * Must be an instance of the following class:<br/>
     * <b>String:</b> the String<br/>
     */
    public void setValue(final Object newValue) {
        throw new RapidBeansRuntimeException("Dependent property: " + getName());
    }

    /**
     * converts different classes to the Property's internal
     * value class.<br/>
     * For a String property this means just verifying that the given
     * object is a String.
     * @param argValue the value to convert<br/>
     * Must be an instance of the following classes:<br/>
     * <b>String:</b> the String value<br/>
     *
     * @return a String
     */
    public String convertValue(final Object argValue) {
        return (String) argValue;
    }

    /**
     * generic validation for the Property's value.
     *
     * @param newValue the value to validate<br/>
     * Must be an instance of the following classes:<br/>
     * <b>String:</b> the String to validate<br/>
     *
     * @return the converted value which is the internal representation or if a
     *         primitive type the corresponding value object
     */
    public String validate(final Object newValue) {
        return (String) newValue;
    }
}
