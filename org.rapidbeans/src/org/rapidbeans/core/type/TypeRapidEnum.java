/*
 * Rapid Beans Framework: TypeRapidEnum.java
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

import org.rapidbeans.core.basic.GenericEnum;
import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.EnumException;
import org.rapidbeans.core.exception.TypeNotFoundException;
import org.rapidbeans.core.exception.UtilException;
import org.rapidbeans.core.util.ClassHelper;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.core.util.XmlNode;


/**
 * Encapsulates the enum type's information.
 *
 * @author Martin Bluemel
 */
public final class TypeRapidEnum extends RapidBeansType {

    /**
     * array with the enum's element instances.
     */
    private List<RapidEnum> elements = null;

    /**
     * hash map with the enum's element instances for high performance
     * lookup by name.
     */
    private Map<String, RapidEnum> enumMap = null;

    /**
     * hash map with the enum's element descriptions
     * lookup by name.
     */
    private Map<String, String> descriptionMap = new HashMap<String, String>();

    public String getDescription(final String enumElementName) {
        return this.descriptionMap.get(enumElementName);
    }

    public String getDescription(final RapidEnum enumElement) {
        return this.descriptionMap.get(enumElement.name());
    }

    /**
     * Return an array with all enum elements of this type.
     *
     * @return the enum's element instances.
     */
    public List<RapidEnum> getElements() {
        int size = this.elements.size();
        List<RapidEnum> elemList = new ArrayList<RapidEnum>(size);
        for (int i = 0; i < size; i++) {
            elemList.add(this.elements.get(i));
        }
        return elemList;
    }

    /**
     * determine the enum element specified by it's name.
     *
     * @param enumElementName the name of the enum element
     *
     * @return the enum Element specified by it's name
     *
     * @throws org.rapidbeans.core.exception.EnumException
     *         if an element with the given name has not been found.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public RapidEnum elementOf(final String enumElementName) {
        RapidEnum e = null;
        if (this.getImplementingClass() != null) {
            try {
                e = (RapidEnum) java.lang.Enum.valueOf((Class) this.getImplementingClass(), enumElementName);
            } catch (final IllegalArgumentException ex) {
                if (this.enumMap != null) {
                    e = this.enumMap.get(enumElementName);
                }
            }
        } else {
            e = this.enumMap.get(enumElementName);
        }
        if (e == null) {
            if (!this.getName().equals("org.rapidbeans.core.basic.RapidEnum")) {
                throw new EnumException("element \"" + enumElementName
                        + "\" is not part of enum \"" + this.getName() + "\"");
            }
        }
        return e;
    }

    /**
     * determine the enum element with the given index.
     *
     * @param index the index of the enum element to retrieve.
     *
     * @return the enum element with the given index.
     */
    public RapidEnum elementOf(final int index) {
        return this.elements.get(index);
    }

    /**
     * determine the index of a certain enum element specified by it's name.
     *
     * @param enumElementName the name of the enum element
     *
     * @return the index of the enum Element specified by it's name
     */
    public int indexOf(final String enumElementName) {
        return this.elementOf(enumElementName).ordinal();
    }

    /**
     * Retrieve the localized name of the enum element
     *
     * @param locale the locale
     *
     * @return a string for the property's value for UI
     */
    public String getStringGui(final RapidEnum enumElement, final RapidBeansLocale locale) {
        try {
            return locale.getStringGui("enum."
                + this.getName().toLowerCase()
                + "." + enumElement.name().toLowerCase());
        } catch (MissingResourceException e) {
            return enumElement.name();
        }
    }


    /**
     * Retrieve the short form localized name of the enum element
     *
     * @param locale the locale
     *
     * @return a string for the property's value for UI
     */
    public String getStringGuiShort(final RapidEnum enumElement, final RapidBeansLocale locale) {
        return this.getStringGui(enumElement, locale).toUpperCase().substring(0, 2);
    }

    /**
     * format a comma separated String out of a list
     * with enum elements.
     *
     * @param enumList the List with enum elements
     * @return the formatted String
     */
    public static String format(final List<RapidEnum> enumList) {
        StringBuffer s = new StringBuffer();
        final int size = enumList.size();
        if (size >= 1) {
            s.append(enumList.get(0).name());
        }
        for (int i = 1; i < size; i++) {
            s.append(',');
            s.append(enumList.get(i).name());
        }
        return s.toString();
    }

    /**
     * parse a comma separated String and convert into a list of enum elements.
     *
     * @param descr the String to parse
     *
     * @return the list of enum elements
     */
    public ArrayList<RapidEnum> parse(final String descr) {
        final ArrayList<RapidEnum> enumList = new ArrayList<RapidEnum>();
        for (final String currentName : StringHelper.split(descr, ",")) {
            enumList.add(this.elementOf(currentName));
        }
        return enumList;
    }

    /**
     * initialize an enum type out of it's (generated) class.
     *
     * @param clazz the real enum class
     *
     * @return the enum's type instance
     */
    public static TypeRapidEnum createInstance(final Class<?> clazz) {
        if (!ClassHelper.classOf(RapidEnum.class, clazz)) {
            throw new UtilException("tried to init an enum with a class that is not subclass of \"BBIEnum\"");
        }
        final TypeRapidEnum type = new TypeRapidEnum(clazz);
        RapidBeansTypeLoader.getInstance().registerType(type);
        return type;
    }


    /**
     * initialize a real generic enum type out of it's description.
     *
     * @param descr the typename
     * @param elements the elements
     *
     * @return the enum type instance
     */
    public static TypeRapidEnum createInstance(
            final String typename, final List<RapidEnum> elements) {
        final TypeRapidEnum type = new TypeRapidEnum(typename, elements);
        RapidBeansTypeLoader.getInstance().registerType(type);
        return type;
    }

    /**
     * initialize a real generic enum type out of it's description.
     *
     * @param descr the typename
     *
     * @return the enum type instance
     */
    public static TypeRapidEnum createInstance(final String typename) {
        final TypeRapidEnum type = new TypeRapidEnum(typename);
        RapidBeansTypeLoader.getInstance().registerType(type);
        return type;
    }

    /**
     * constructor for a type instance of a generic enum.
     *
     * @param typename the fully qualified enum type name
     */
    public TypeRapidEnum(final String typename) {
        XmlNode xmlDeclaration = null;
        try {
            xmlDeclaration = RapidBeansType.loadDescription(typename);
            setDescription(RapidBeansType.readDescription(xmlDeclaration));
        } catch (TypeNotFoundException e) {
            xmlDeclaration = null;
        }
        try {
            final Class<?> clazz = Class.forName(typename);
            this.initFromClass(clazz, xmlDeclaration);
        } catch (ClassNotFoundException e) {
            this.initFromTypeDescr(typename, xmlDeclaration);
        }
    }

    /**
     * constructor for a type instance of a generic enum with
     * elements already given.
     *
     * @param typename the fully qualified enum type name
     * @param enumElements the enum elements
     */
    public TypeRapidEnum(final String typename, final List<RapidEnum> enumElements) {
        XmlNode xmlDeclaration = null;
        try {
            xmlDeclaration = RapidBeansType.loadDescription(typename);
            setDescription(RapidBeansType.readDescription(xmlDeclaration));
        } catch (TypeNotFoundException e) {
            xmlDeclaration = null;
        }
        this.setName(typename);
        Class<?> clazz = null;
        try {
            clazz = Class.forName(typename);
        } catch (ClassNotFoundException e) {
            clazz = null;
        }
        this.setImplementingClass(clazz);
        this.elements = enumElements;
        this.enumMap = new Hashtable<String, RapidEnum>();
        for (RapidEnum elem : this.elements) {
            this.enumMap.put(elem.name(), elem);
        }
        this.checkElements();
    }

    /**
     * constructor.
     *
     * @param clazz the real enum class
     */
    public TypeRapidEnum(final Class<?> clazz) {
        XmlNode xmlDeclaration = null;
        try {
            xmlDeclaration = RapidBeansType.loadDescription(clazz.getName());
            setDescription(RapidBeansType.readDescription(xmlDeclaration));
        } catch (TypeNotFoundException e) {
            xmlDeclaration = null;
        }
        this.initFromClass(clazz, xmlDeclaration);
    }

    /**
     * constructor for a type instance of a generic enumeration.
     *
     * @param typename the fully qualified enumeration type name
     * @param decl the enumeration type declaration
     */
    private void initFromClass(final Class<?> clazz,
            final XmlNode decl) {
        this.setName(clazz.getName());
        this.setImplementingClass(clazz);

        // explore the enum type via reflection
        Field[] fields = clazz.getDeclaredFields();
        Object obj;
        RapidEnum enumelem = null;
        this.elements = new ArrayList<RapidEnum>();
        for (int i = 0; i < fields.length; i++) {
            try {
                obj = fields[i].get(enumelem);
                if (obj.getClass() == clazz) {
                    enumelem = (RapidEnum) obj;
                    final XmlNode enumNode = findEnumNode(decl, enumelem.name());
                    this.descriptionMap.put(enumelem.name(), RapidBeansType.readDescription(enumNode));
                    this.elements.add(enumelem);
                }
            } catch (IllegalAccessException e) {
                // just do nothing in case of non accessible members
                obj = null;
            }
        }
        this.checkElements();
    }

    private XmlNode findEnumNode(final XmlNode decl, final String name) {
        for (final XmlNode node : decl.getSubnodes("enum")) {
            if (node.getAttributeValue("@name").equals(name)) {
                return node;
            }
        }
        return null;
    }

    /**
     * constructor for a type instance of a generic enum.
     *
     * @param typenameOrDescr the fully qualified enum type name or the
     *        whole description
     */
    private void initFromTypeDescr(final String typenameOrDescr,
            final XmlNode decl) {
        this.setImplementingClass(null);

        this.elements = new ArrayList<RapidEnum>();
        this.enumMap = new Hashtable<String, RapidEnum>();
        XmlNode tlNode = null;
        if (typenameOrDescr.startsWith("<enumtype")) {
            tlNode = XmlNode.getDocumentTopLevel(typenameOrDescr);
            this.setName(tlNode.getAttributeValue("@name"));
        } else {
            tlNode = RapidBeansType.loadDescription(typenameOrDescr);
            this.setName(typenameOrDescr);
        }
        int i = 0;
        for (XmlNode node : tlNode.getSubnodes("enum")) {
            this.descriptionMap.put(node.getAttributeValue("@name"),
                    RapidBeansType.readDescription(node));
            final GenericEnum genericEnum = new GenericEnum(this, node.getAttributeValue("@name"), i++);
            this.elements.add(genericEnum);
            this.enumMap.put(genericEnum.name(), genericEnum);
        }
        this.checkElements();
    }

    private void checkElements() {
        if (this.elements.size() == 0) {
            if (!this.getName().equals("org.rapidbeans.core.basic.RapidEnum")) {
                throw new EnumException("Problems with enumeration type \""
                        + getName() + "\". An enum must at least have one member");
            }
        }
        int j;
        final int elemSize = this.elements.size();
        for (int i = 0; i < elemSize; i++) {
            final RapidEnum enumElement = this.elements.get(i);
            for (j = i + 1; j < elemSize; j++) {
                if (enumElement == this.elements.get(j)) {
                    throw new EnumException(
                            "Invalid enum for TypeRapidEnum (enum class name \""
                            + this.getName() + "\"): enum element \""
                            + this.elements.get(i).name()
                            + "\" enumerated more than once\n");
                }
            }
        }        
    }

    /**
     * find an enum type out of a name.
     * <p><b>throws TypeNotFoundException:</b><br/>
     * if the specified enum type does not exist<br/></p>
     *
     * @param     typename    the type's name
     *
     * @return    null if the enum with the specified type name could not be found
     *            and a concrete enum class could not be loaded
     */
    public static TypeRapidEnum forName(final String typename) {
        return (TypeRapidEnum) RapidBeansTypeLoader.getInstance().loadType(TypeRapidEnum.class, typename);
    }
}
