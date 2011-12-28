/*
 * Rapid Beans Framework: GenericEnum.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 01/25/2008
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

import java.util.List;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.UtilException;
import org.rapidbeans.core.type.TypeRapidEnum;

/**
 * a Rapid enum without class.
 *
 * @author Martin Bluemel
 */
public class GenericEnum implements Cloneable, Comparable<RapidEnum>, RapidEnum {

    /**
     * the enum element's type.
     */
    private TypeRapidEnum type = null;

    /**
     * @return the enum element's type
     * @see org.rapidbeans.core.basic.RapidEnum#getType()
     */
    public TypeRapidEnum getType() {
        return this.type;
    }


    /**
     * @param type the type to set
     */
    public void setType(TypeRapidEnum type) {
        this.type = type;
    }

    /**
     * the enum element's name.
     */
    private String name = null;

    /**
     * @return the enum element's name
     * @see org.rapidbeans.core.basic.RapidEnum#getName()
     */
    public final String name() {
        return this.name;
    }

    /**
     * the enum element's order.
     */
    private int ordinal = Integer.MIN_VALUE;

    /**
     * @see org.rapidbeans.core.basic.RapidEnum#getOrder()
     */
    public final int ordinal() {
        return this.ordinal;
    }

    /**
     * this enumeration element's description.
     */
    private String description = null;

    /**
     * Get the description from the model (meta information - not UI).
     *
     * @return this enumeration element's description.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Generic instance finder for enums.
     * Provides a generic way to retrieve certain enum elements.<br/>
     * Instead of<br/>
     * <code>RapidEnum enum = Length.m</code> you can also code<br/>
     * <code>RapidEnum enum = RapidEnum.getInstance("org.rapidbeans.domain.math.Length", "m")</code><br/>
     * <p><b>throws TypeNotFoundException:</b><br/>
     * if the specified RapidEnum type does not exist<br/></p>
     * <p><b>throws EnumException:</b><br/>
     * if the specified RapidEnum instance does not exist<br/></p>
     *
     * @param enumTypeName specifies the RapidEnum's type
     * @param enumName     specifies the RapidEnum element
     *
     * @return             the RapidEnum element instance.
     */
    public static RapidEnum valueOf(final String enumTypeName,
            final String enumName) {
        return TypeRapidEnum.forName(enumTypeName).elementOf(enumName);
    }

    /**
     * one and only common constructor for RapidBeans RapidEnum elements.
     * Must only be used by subclasses.
     *
     * @param argType the generic enum's type
     * @param argName the enum element's name.
     * @param argOrdinal the enum element's ordinal.
     */
    public GenericEnum(final TypeRapidEnum argType, final String argName, final int argOrdinal) {
        this.type = argType;
        this.name = argName;
        this.ordinal = argOrdinal;
    }

    /**
     * @see org.rapidbeans.core.basic.RapidEnum#toString()
     */
    public final String toString() {
        return this.name;
    }

    /**
     * @see org.rapidbeans.core.basic.RapidEnum#toStringGui(org.rapidbeans.core.common.RapidBeansLocale)
     */
    public String toStringGui(final RapidBeansLocale locale) {
        return this.getType().getStringGui(this, locale);
    }

    /**
     * @see org.rapidbeans.core.basic.RapidEnum#toStringGuiShort(org.rapidbeans.core.common.RapidBeansLocale)
     */
    public String toStringGuiShort(final RapidBeansLocale locale) {
        return this.getType().getStringGuiShort(this, locale);
    }

    /**
     * @see org.rapidbeans.core.basic.RapidEnum#equals(java.lang.Object)
     */
    public boolean equals(final Object other) {
        if (!(other instanceof GenericEnum)) {
            return false;
        }
        GenericEnum otherEnum = (GenericEnum) other;
        if (otherEnum.type != this.type) {
            return false;
        }
        return this.name.equals(((GenericEnum) other).name);
    }

    /**
     * implementation of Object.compareTo().
     *
     * @param object the ibject instance to compare
     *
     * @return -1 if smaller, 0 is equals, 1 if greater
     */
    public final int compareTo(final RapidEnum object) {
        int comp = 0;
        if (!(object instanceof GenericEnum)) {
            throw new UtilException("Cannot compare a GenericEnum against a \""
                    + object.getClass().getName() + "\".\n"
                    + "Only enums of same type can be compared against each other.");
        }
        GenericEnum genenum = (GenericEnum) object;
        if (!(this.getType() == genenum.getType())) {
            throw new UtilException(
                    "Cannot compare a GenericEnum against a \""
                    + object.getClass().getName() + "\".\n"
                    + "Only enums of same type can be compared"
                    + " against each other.");
        }
        if (this.ordinal > genenum.ordinal) {
            comp = 1;
        } else if (this.ordinal < genenum.ordinal) {
            comp = -1;
        } else {
            comp = 0;
        }
        return comp;
    }


    /**
     * Initialize a RapidEnum generic type with elements
     * dynamically generated a initializion time.
     *
     * @param enumClass the enum class
     * @param elements the elements ((must be subclass of generic enum) 
     */
    protected static TypeRapidEnum initType(Class<?> enumClass,
            List<RapidEnum> elements) {
        TypeRapidEnum type = TypeRapidEnum.createInstance(enumClass.getName(), elements);
        initType(type, elements);
        return type;
    }

    /**
     * Initialize a RapidEnum generic type with elements
     * dynamically generated a initializion time.
     *
     * @param enumClass the enum class
     * @param elements the elements ((must be subclass of generic enum) 
     */
    protected static void initType(final TypeRapidEnum type,
            final List<RapidEnum> elements) {
        for (final RapidEnum enumElement : elements) {
            final GenericEnum genEnumElement = (GenericEnum) enumElement;
            genEnumElement.setType(type);
        }
    }
}
