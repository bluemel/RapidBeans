/*
 * Rapid Beans Framework: RapidBean.java
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

import java.util.List;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.event.PropertyChangeEvent;
import org.rapidbeans.core.event.PropertyChangeListener;
import org.rapidbeans.core.type.TypeRapidBean;

/**
 * The basic bean interface of the RapidBeans framework.
 * 
 * @author Martin Bluemel
 */
// TODO Framework 10) map association property
// TODO Framework 11) (b/c) lob and picture property
// TODO Framework 21) XML Serialization: consistent null semantics for
// especially collections
// TODO Framework 22) XML Serialization: alternatively with JAXB? at least SAX
// (JAXP)
// TODO Framework 23) make quantity editing more robust (no exceptions
// if the unit is null while editing)
// TODO Framework 24) support maps
// TODO Framework 25) try changing setValue of a collection by
// adding and deleting successively.
// TODO Framework 25) allow paging for queries
// TODO Framework 26) Move towards DBs: document partitioning
// incl. partial views, and document merging

// TODO Framework 30) icons for buttons and standard menu entries
// TODO Framework 31) sandwatch, progress bar, etc for actions
// TODO Framework 32) integrate a toolbox
// TODO Framework 33) integrate tooltips

public interface RapidBean extends Cloneable, Comparable<Link>, Link {

	// Factory methods

	// public static RapidBean createInstance(final String typename)
	// public static RapidBean createInstance(final TypeRapidBean type);

	// Constructors

	// public RapidBean();
	// public RapidBean(final String initvals);
	// public RapidBean(final String[] initvals);
	// protected RapidBean(final TypeRapidBean type);
	// protected RapidBean(final String initvals, final TypeRapidBean type);
	// protected RapidBean(final String[] initvals, final TypeRapidBean argType)

	// Java standard Object operations to implement

	public String toString();

	/**
	 * A bean equals another bean it has the same type and an equal identity.
	 * 
	 * @param o
	 *            the object to compare
	 * @return if the object equals or not
	 */
	boolean equals(final Object o);

	/**
	 * the hash code of a bean is the hash code of it's id.
	 * 
	 * @return the hash code
	 */
	int hashCode();

	/**
	 * compare method.
	 * 
	 * @param o
	 *            the bean to compare with
	 * 
	 * @return the compare value
	 */
	public int compareTo(final Link link);

	// --- RapidBean instance operations ---------------------------

	/**
	 * @return the bean's type instance
	 */
	TypeRapidBean getType();

	/**
	 * Convenience getter for a property's value. Encourages not to hold the
	 * bean's property reference for a long time.
	 * 
	 * @param name
	 *            the Property's name
	 * @return the Property's value
	 */
	Object getPropValue(final String name);

	/**
	 * Convenience setter for a property's value. Encourages not to hold the
	 * bean's property reference for a long time.
	 * 
	 * @param name
	 *            the Property's name
	 * @param value
	 *            the Property's value to set
	 */
	void setPropValue(final String name, final Object value);

	/**
	 * @param name
	 *            the Property's name.
	 * 
	 * @return the bean's Property with the specified name.
	 */
	Property getProperty(final String name);

	/**
	 * Find an association property with the given singular name.
	 * 
	 * @param subnodeName
	 *            the assocation property's name.
	 * @return
	 */
	PropertyCollection findAssociationPropertyWithSingularName(String subnodeName);

	/**
	 * @return a List with all the bean's Properties
	 */
	List<Property> getPropertyList();

	/**
	 * @return a list with all the bean's collection properties
	 */
	List<PropertyCollection> getColProperties();

	/**
	 * @return a list with all the bean's composition collection properties
	 */
	List<PropertyCollection> getColPropertiesComposition();

	/**
	 * navigate to the parent (composite) bean of this bean.
	 * 
	 * @return the parent (composite) bean of this bean if any or null
	 */
	RapidBean getParentBean();

	/**
	 * retrieve all parent beans in the composite hierarcy.
	 * 
	 * @return an array of parent beans starting with the (document) root and
	 *         ending with the direct parent
	 */
	RapidBean[] getParentBeans();

	/**
	 * get the parent collection property.
	 * 
	 * @return the parent collection property.
	 */
	PropertyCollection getParentProperty();

	/**
	 * setter for the parent (composite) bean used internally when adding or
	 * removing a bean reference to / from a collection property of type
	 * composition. Also used when validating properties in the editor
	 * 
	 * @param newParent
	 *            the new parent bean
	 */
	void setParentBean(final RapidBean newParent);

	/**
	 * @return the bean's ID.
	 */
	Id getId();

	/**
	 * @return the ID string
	 */
	String getIdString();

	/**
	 * CAUTION: exclusively use this setter for deserialization.
	 * 
	 * @param newId
	 *            the new id.
	 */
	void setId(final Id newId);

	/**
	 * CAUTION:
	 */
	void clearId();

	/**
	 * @return the bean's state
	 */
	RapidBeanState getBeanState();

	/**
	 * @return Returns the container.
	 */
	Container getContainer();

	/**
	 * @param cont
	 *            The container to set
	 */
	void setContainer(final Container container);

	/**
	 * remove all references to and from other beans. notify the container about
	 * deletion.
	 */
	void delete();

	/**
	 * initializes all properties.
	 */
	void initProperties();

	/**
	 * adds a new PropertyChangeListener.
	 * 
	 * @param l
	 *            the new listener to add
	 */
	void addPropertyChangeListener(final PropertyChangeListener l);

	/**
	 * adds a new PropertyChangeListener.
	 * 
	 * @param l
	 *            the new listener to add
	 */
	void removePropertyChangeListener(final PropertyChangeListener l);

	/**
	 * Fires a property pre change event for that bean. For specific processing
	 * simply override and call super().
	 * 
	 * @param event
	 *            the PropertyChangeEvent to fire
	 */
	void propertyChangePre(final PropertyChangeEvent event);

	/**
	 * Fires a property post change event for that bean. For specific processing
	 * simply override and call super().
	 * 
	 * @param event
	 *            the PropertyChangeEvent to fire
	 */
	void propertyChanged(final PropertyChangeEvent event);

	/**
	 * validate the whole bean.
	 */
	void validate();

	/**
	 * @param cloneContainer
	 *            the container for the cloned bean
	 * 
	 * @return a clone of this bean including the whole hierarchy. The container
	 *         in set to null. Non compositions links are frozen.
	 */
	RapidBean cloneExternal(final Container cloneContainer);

	/**
	 * @return a clone of this bean.
	 */
	RapidBean clone();

	/**
	 * expand the curly braced property names with the bean's property values.
	 * 
	 * @param pattern
	 *            the pattern with curly braces e. g. "{firstname}, {lastname}"
	 * @param locale
	 *            the locale
	 * @return the expanded string
	 */
	String expandPropertyValues(final String pattern, final RapidBeansLocale locale);

	/**
	 * lazy initialization of the propmap.
	 */
	void initPropmap();

	/**
	 * computes a localized String to present this Bean in a UI.
	 * 
	 * @param locale
	 *            the Locale
	 * 
	 * @return the localized String for this Bean
	 */
	String toStringGui(final RapidBeansLocale locale);

	/**
	 * computes a localized String to present the Bean's type name in a UI.
	 * 
	 * @param locale
	 *            the Locale
	 * @return the localized String for this Bean
	 */
	String toStringGuiType(final RapidBeansLocale locale);

	/**
	 * computes a localized String to present this Bean's instance name in a UI.
	 * 
	 * @param locale
	 *            the Locale
	 * @return the localized String for this Bean
	 */
	String toStringGuiId(final RapidBeansLocale locale);

	/**
	 * trace that bean.
	 */
	public void trace();
}
