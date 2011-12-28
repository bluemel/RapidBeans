/*
 * Rapid Beans Framework: RapidBeansTypeLoader.java
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.TypeNotFoundException;
import org.rapidbeans.core.util.StringHelper;


/**
 * the type loader.
 *
 * @author Martin Bluemel
 */
public final class RapidBeansTypeLoader {

    /**
     * the single type loader instance.
     */
    private static RapidBeansTypeLoader singleInstance = null;

    /**
     * the factory method for the singleton.
     *
     * @return the one and only TypeLoader instance.
     */
    public static RapidBeansTypeLoader getInstance() {
        if (singleInstance == null) {
            singleInstance = new RapidBeansTypeLoader();
        }
        return singleInstance;
    }

    /**
     * the type map: maps a type name to a RapidBeans type.
     */
    private HashMap<String,RapidBeansType> typeMap =
        new HashMap<String,RapidBeansType>();

    /**
     * the XML root element binding map: maps an XML root element name
     * to a RapidBeans type.
     */
    private HashMap<String,TypeRapidBean> xmlRootElementMap =
        new HashMap<String,TypeRapidBean>();

    /**
     * Find an XML root element binding.
     *
     * @param elementName the XML root element name.
     *
     * @return the binding or null if no binding is registered
     */
    public TypeRapidBean getXmlRootElementBinding(final String elementName) {
        return this.xmlRootElementMap.get(elementName);
    }

    /**
     * Add a new XML root element binding.
     *
     * @param elementName the XML element name
     * @param typeName the RapidBean type name
     */
    public void addXmlRootElementBinding(final String elementName,
            final String typeName) {
        this.addXmlRootElementBinding(elementName, typeName, false);
    }


    /**
     * Add a new XML root element binding.
     *
     * @param elementName the XML element name
     * @param typeName the RapidBean type name
     * @param override if overriding the bind is allowed or not
     *                 Handle overriding with care
     */
    public void addXmlRootElementBinding(final String elementName,
            final String typeName,
            final boolean override) {
        if ((!override) && this.xmlRootElementMap.get(elementName) != null) {
            throw new RapidBeansRuntimeException("An XML root element binding"
                    + " for XML element name \"" + elementName + "\" has already been defined.");
        }
        final TypeRapidBean type = TypeRapidBean.forName(typeName);
        this.xmlRootElementMap.put(elementName, type);
    }

    /**
     * default constructor only to be used privately.
     */
    private RapidBeansTypeLoader() {
    }

    /**
     * @param typename the type name.
     * @param suffix the suffix of the declaration file
     *
     * @return the stream
     */
    protected static InputStream findDeclaration(final String typename,
            final String suffix) {
        InputStream is = null;
        String typeInfoDescrFileName = null;
        try {
            // this looks a bit awkward but seems to be a way to load the description
            // also in an Applet environment
            final Class<?> typeclass = Class.forName(typename);
            if (suffix == null) {
                typeInfoDescrFileName = StringHelper.splitLast(typename, ".") + ".xml";
            } else {
                typeInfoDescrFileName = StringHelper.splitLast(typename, ".") + suffix + ".xml";
            }
            is = typeclass.getResourceAsStream(typeInfoDescrFileName);
        } catch (ClassNotFoundException e) {
            // for generic types we take the straight forward method which unfortunately
            // won't run with Applets
            if (suffix == null) {
                typeInfoDescrFileName = typename.replace('.', '/') + ".xml";
            } else {
                typeInfoDescrFileName = typename.replace('.', '/') + suffix + ".xml";                
            }
            is = ClassLoader.getSystemResourceAsStream(typeInfoDescrFileName);
        }
        return is;
    }

    /**
     * registers a Rapid Beans type.
     *
     * @param typename the type's name
     * @param type the type to register
     */
    public void registerType(final RapidBeansType type) {
    	if (this.typeMap.get(type.getName()) != null) {
    		throw new RapidBeansRuntimeException("bean type \""
    				+ type.getName() + "\" already registered");
    	}
    	this.typeMap.put(type.getName(), type);
    }

    /**
     * registers an Easybiz type if the same instance is not already registered.
     * @param typename the type's name
     * @param type the type to register
     */
    public void registerTypeIfNotRegistered(final String typename, final RapidBeansType type) {
    	if (this.typeMap.get(typename) != null) {
    		return;
    	}
    	this.typeMap.put(typename, type);
    }

    /**
     * removes a Rapid Beans type registration.
     * Caution: for test purposes only!!!
     *
     * @param typename the type's name
     */
    public void unregisterType(final String typename) {
        if (this.typeMap.get(typename) == null) {
            throw new RapidBeansRuntimeException("bean type \""
                    + typename + "\" not registered");
        }
        this.typeMap.remove(typename);
    }

    /**
     * looks up an Rapid Beans type.
     *
     * @param typename the type's name
     *
     * @return the registered type
     */
    public RapidBeansType lookupType(final String typename) {
        return this.typeMap.get(typename);
    }

    /**
     * load a type specified by it's name.
     * <p><b>throws TypeNotFoundException:</b><br/>
     * if the specified RapidEnum type does not exist<br/></p>
     *
     * @param typeclass the type's class { TypeRapidEnum, TypeRapidQuantity, TypeRapidBean }
     * @param typename the types full qualified name
     *
     * @return the loaded type
     */
    protected RapidBeansType loadType(final Class<?> typeclass, final String typename) {

        if (typeclass != TypeRapidEnum.class
                && typeclass != TypeRapidQuantity.class
                && typeclass != TypeRapidBean.class) {
            throw new RapidBeansRuntimeException("RapidBeansType class \""
                    + typeclass.getName() + "\" not supported.");
        }

        RapidBeansType type = this.typeMap.get(typename);
        Class<?> clazz = null;
        if (type == null) {
            try {
                // the typeinfo implicitly is registered while the class is loaded
                // (static initializer of all concrete enum classes)
                clazz = Class.forName(typename);

                // if we got no ClassNotFoundException
                // we can get the typeinfo now
                type = this.typeMap.get(typename);

            } catch (ClassNotFoundException e) {
                // do nothing (type stays null)
                type = null;
            }

            if (type == null) {
                if (typeclass == TypeRapidBean.class) {
                    type = new TypeRapidBean(clazz,
                            RapidBeansType.loadDescription(typename),
                            RapidBeansType.loadDescriptionXmlBinding(typename),
                            true);
                } else if (typeclass == TypeRapidEnum.class) {
                    type = new TypeRapidEnum(typename);
                    this.registerType(type);
                } else if (typeclass == TypeRapidQuantity.class) {
                    type = TypeRapidQuantity.createInstance(RapidBeansType.loadDescription(typename));
                }
            }

            if (type == null) {
                if (typeclass == TypeRapidEnum.class) {
                    throw new TypeNotFoundException("Enum type \"" + typename + "\"");
                } else if (typeclass == TypeRapidQuantity.class) {
                    throw new TypeNotFoundException("Quantity type \"" + typename + "\"");
                } else if (typeclass == TypeRapidBean.class) {
                    throw new TypeNotFoundException("Bean type \"" + typename + "\"");
                }
            }
        } // if (type == null)

        return type;
    }

    /**
     * CAUTION: this methods clears the single typeMap.
     * Just use this for unit tests or if you exactly know what you're doing.
     */
    protected static void typeMapClear(){
        singleInstance.typeMap.clear();
    }

    /**
     * CAUTION: this methods clears all generic bean types
     * out of the single type map.
     * Just use this for unit tests
     * or if you exactly know what you're doing.
     */
    protected static void typeMapClearBeanGeneric() {
        RapidBeansType bbType = null;
        ArrayList<String> sa = new ArrayList<String>();
        if (singleInstance != null) {
            for (Object o : singleInstance.typeMap.values()) {
                bbType = (RapidBeansType) o;
                if (bbType.getImplementingClass() == null) {
                    sa.add(bbType.getName());
                }
            }
        }
        for (String s : sa) {
            singleInstance.unregisterType(s);
        }
    }

    /**
     * CAUTION: this methods clears all root element bindings.
     * Just use this for unit tests
     * or if you exactly know what you're doing.
     */
    protected static void typeMapClearRootElementBindingsGeneric() {
        final ArrayList<String> elementNames = new ArrayList<String>();
        if (singleInstance != null) {
            for (final Object o : singleInstance.xmlRootElementMap.keySet()) {
                final String elementName = (String) o;
                final TypeRapidBean beantype = singleInstance.xmlRootElementMap.get(elementName);
                if (beantype.getImplementingClass() == null) {
                    elementNames.add(elementName);
                }
            }
            for (final String elementName : elementNames) {
                singleInstance.xmlRootElementMap.remove(elementName);
            }
        }
    }
}
