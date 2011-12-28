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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.MissingResourceException;
import java.util.logging.Logger;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.common.ThreadLocalProperties;
import org.rapidbeans.core.event.PropertyChangeEvent;
import org.rapidbeans.core.event.PropertyChangeListener;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.UtilException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.ClassHelper;
import org.rapidbeans.core.util.StringHelper;


/**
 * The very basic class of the Easybiz framework. Defines a domain object
 * superclass for all business entites.
 *
 * @author Martin Bluemel
 */
//TODO Framework 10) map association property
//TODO Framework 11) (b/c) lob and picture property
//TODO Framework 21) XML Serialization: consistent null semantics for especially collections
//TODO Framework 22) XML Serialization: alternatively with JAXB? at least SAX (JAXP)
//TODO Framework 23) make quantity editing more robust (no exceptions
//                 if the unit is null while editing)
//TODO Framework 24) support maps
//TODO Framework 25) try changing setValue of a collection by
//                 adding and deleting successively.
//TODO Framework 25) allow paging for queries
//TODO Framework 26) Move towards DBs: document partitioning
//                 incl. partial views, and document merging

//TODO Framework 30) icons for buttons and standard menu entries
//TODO Framework 31) sandwatch, progress bar, etc for actions
//TODO Framework 32) integrate a toolbox
//TODO Framework 33) integrate tooltips
public abstract class RapidBean
    implements Cloneable, Comparable<Link>, Link {

    private static final Logger log = Logger.getLogger(
            RapidBean.class.getName()); 

    /**
     * @return the bean's type instance
     */
    public abstract TypeRapidBean getType();

    /**
     * the identity object.
     */
    private Id id = null;

    /**
     * @return the bean's ID.
     */
    public Id getId() {
        if (this.id == null) {
            this.id = Id.createInstance(this, null);
        }
        return this.id;
    }

    /**
     * reset the ID to null.
     * Do not use in general.
     */
    public final void clearId() {
        this.id = null;
    }

    /**
     * @return the ID string
     */
    public String getIdString() {
        if (this.id == null) {
            return this.getId().toString();
        } else {
            return this.id.toString();
        }
    }

    /**
     * @return the ID string
     */
    public final String toString() {
        return this.getIdString();
    }


    /**
     * CAUTION: exclusively use this setter for deserialization.
     * @param newId the new id.
     */
    public void setId(final Id newId) {
        this.id = newId;
    }

//    /**
//     * CAUTION: exclusively use this setter for deserialization.
//     * @param idString the serialized id.
//     */
//    public void setId(final String idString) {
//        this.id = Id.createInstance(this, idString);
//    }

    /**
     * The property container.
     * Implements the composition.
     */
    private Property[] properties = null;

    /**
     * The map just optimizes finding by name.
     */
    private HashMap<String,Property> propmap = null;

    /**
     * lazy initialization of the propmap.
     */
    private void initPropmap() {
        this.propmap = new HashMap<String,Property>();
        for (int i = 0; i < this.properties.length; i++) {
            this.propmap.put(this.properties[i].getType().getPropName(),
                    this.properties[i]);
        }
    }

    /**
     * Store the bean's state
     */
    private RapidBeanState beanState = null;

    /**
     * @return the bean's state
     */
    public RapidBeanState getBeanState() {
        return this.beanState;
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
            if (ClassHelper.classOf(TypePropertyCollection.class,
                    this.properties[i].getType().getClass())
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
     * Convenience getter for a property's value.
     * Encourages not to hold the bean's property reference for a long time.
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
            throw new ValidationException("invalid.prop.name",
            		this,
                    "unknown property \"" + name
                    + "\" for bean type \"" + this.getType().getName() + "\".");
        }
        return prop.getValue();
    }

    /**
     * Convenience setter for a property's value.
     * Encourages not to hold the bean's property reference for a long time.
     *
     * @param name the Property's name
     * @param value the Property's value to set
     */
    public final void setPropValue(final String name, final Object value) {
        if (this.propmap == null) {
            this.initPropmap();
        }
        Property prop = this.propmap.get(name);
        if (prop == null) {
            throw new ValidationException("invalid.prop.name",
            		this,
                    "unknown property \"" + name
                    + "\" for bean type \"" + this.getType().getName() + "\".");
        }
        prop.setValue(value);
    }

    /**
     *  the parent (composite) bean of this bean.
     */
    private RapidBean parent = null;

    /**
     * navigate to the parent (composite) bean of this bean.
     *
     * @return the parent (composite) bean of this bean if any or null
     */
    public RapidBean getParentBean() {
        return this.parent;
    }

    /**
     * retrieve all parent beans in the composite hierarcy.
     *
     * @return an array of parent beans starting
     * with the (document) root and ending with the direct parent
     */
    public RapidBean[] getParentBeans() {
        final ArrayList<RapidBean> beans = new ArrayList<RapidBean>();
        RapidBean parentBean = this.getParentBean();
        while (parentBean != null) {
            beans.add(parentBean);
            parentBean = parentBean.getParentBean();
        }
        final RapidBean[] parentBeans = new RapidBean[beans.size()];
        int j = 0;
        for (int i = parentBeans.length - 1; i >= 0; i--) {
            parentBeans[i] = beans.get(j++);
        }
        return parentBeans;
    }

    /**
     * get the parent collection property.
     * @return the parent collection property.
     */
    public PropertyCollection getParentProperty() {
        PropertyCollection parentCompColProp = null;
        if (this.parent != null) {
            for (PropertyCollection compColProp :
                this.parent.getColPropertiesComposition()) {
                final Collection<?> col = (Collection<?>) compColProp.getValue();
                if (col != null && col.contains(this)) {
                    parentCompColProp = compColProp;
                    break;
                }
            }
        }
        return parentCompColProp;
    }

    /**
     * setter for the parent (composite) bean used internally
     * when adding or removing a bean reference to / from a
     * collection property of type composition.
     * Also used when validating properties in the editor
     *
     * @param newParent the new parent bean
     */
    public void setParentBean(final RapidBean newParent) {
        // reset the id if the parent changes
        if (this.parent != null && newParent != null) {
            // change a component's parent
            final PropertyCollection parentColProp = this.getParentProperty();
            parentColProp.removeLink(this, false, true, false);
            if (ClassHelper.classOf(this.parent.getClass(),
                    newParent.getClass())) {
                PropertyCollection newParentColProp = (PropertyCollection) newParent.getProperty(
                        parentColProp.getName());
                newParentColProp.addLink(this, false, false, false);
            }
        }
        resetId(newParent);
        this.parent = newParent;
    }

    /**
     * reset the ID.
     *
     * @param newParentBean the new parent bean
     */
    private void resetId(final RapidBean newParentBean) {
        if (this.id instanceof IdKeypropswithparentscope) {
            boolean changeids = true;
            Boolean doNotChange = (Boolean) ThreadLocalProperties.get("bean.setparentbean.donotchangeids");
            if (doNotChange != null) {
                changeids = !doNotChange.booleanValue();
            }
            if (changeids) {
                if (this.container != null
                        && (this.container.contains(this))) {
                    this.container.delete(this);
                    this.id = null;
                    if (newParentBean != null && this.container != null) {
                        ((ContainerImpl) this.container).insert(this, true);
                    }
                } else {
                    this.id = null;
                }
            }
        }
        for (PropertyCollection colProp : this.getColPropertiesComposition()) {
            if (colProp.getValue() == null) {
                continue;
            }
            for (Object o : (Collection<?>) colProp.getValue()) {
                final RapidBean bean = (RapidBean) o;
                bean.resetId(this);
            }
        }
    }

    /**
     * the container where the bean lives in.
     */
    private Container container = null;

    /**
     * @return Returns the container.
     */
    public Container getContainer() {
        return this.container;
    }

    /**
     * @param cont The container to set
     */
    public void setContainer(final Container cont) {
        this.container = cont;
    }

    /**
     * Creates a new bean instance from a special type.
     *
     * @param typename the name of the bean's type
     *
     * @return the new bean instance
     */
    public static RapidBean createInstance(final String typename) {
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
    public static RapidBean createInstance(final TypeRapidBean type) {
        RapidBean bean = null;
        Class<?> clazz = type.getImplementingClass();
        if (clazz == null) {
            bean = new GenericBean(type);
        } else {
            try {
                Constructor<RapidBean> constr = (Constructor<RapidBean>) clazz.getConstructor(CONSTR_PARAMTYPES_STRING_ARRAY);
                Object[] initargs = new Object[1];
                initargs[0] = EMPTY_STRING_ARRAY;
                bean = (RapidBean) constr.newInstance(initargs);
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
     * remove all references to and from other beans.
     * notify the container about deletion.
     */
    @SuppressWarnings("unchecked")
    public void delete() {
        final RapidBeanState stateBefore = this.beanState;
        this.beanState = RapidBeanState.deleting;
        Container containerFired = null;
        try {
            if (this.container != null) {
                this.container.fireBeanRemovePre(this);
                containerFired = this.container;
            }

            // delete the component relationship to the parent
            if (this.parent != null) {
                TypePropertyCollection inverseColPropType;
                Collection<RapidBean> inverseCol;
                boolean removed = false;
                for (PropertyCollection inverseColProp : this.parent.getColPropertiesComposition()) {
                    inverseColPropType = (TypePropertyCollection) inverseColProp.getType();
                    inverseCol = (Collection<RapidBean>) inverseColProp.getValue();
                    if (TypeRapidBean.isSameOrSubtype(inverseColPropType.getTargetType(), this.getType())
                            && inverseCol.contains(this)) {
                        inverseColProp.removeLink(this);
                        removed = true;
                        break;
                    }
                }
                if (!removed) {
                    throw new RapidBeansRuntimeException("Removing a bean out of a container by"
                            + " breaking the collection link from it's parent bean failed.");
                }
            }

            // delete normal association instances and start a
            // delete cascade for components
            Collection<RapidBean> col;
            TypePropertyCollection colPropType;
            for (PropertyCollection colProp : this.getColProperties()) {
                col = (Collection<RapidBean>) colProp.getValue();
                if (col == null) {
                    continue;
                }
                colPropType = (TypePropertyCollection) colProp.getType();
                if (colPropType.isComposition()) {
                    // we have to store the component beans to delete in a different
                    // collection before we delete them otherwise we get a
                    // ConcurrentModification exception because col is changed
                    // implicitly while the component bean is deleted and at the
                    // same time we're iterating over it.
                    Collection<RapidBean> componentBeansToDelete = new ArrayList<RapidBean>();
                    for (RapidBean bean : col) {
                        componentBeansToDelete.add(bean);
                    }
                    for (RapidBean bean : componentBeansToDelete) {
                        bean.delete();
                    }
                    componentBeansToDelete.clear();
                } else {
                    Collection<RapidBean> beansToUnlink = new ArrayList<RapidBean>();
                    for (RapidBean bean : col) {
                        beansToUnlink.add(bean);
                    }
                    for (RapidBean bean : beansToUnlink) {
                        colProp.removeLink(bean);
                    }                    
                }
            }

            if (containerFired != null) {
                containerFired.fireBeanRemoved(this);
            }
        } finally {
            this.beanState = stateBefore;
        }
    }

    /**
     * empty string array.
     */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * initializes all properties.
     */
    protected abstract void initProperties();

    /**
     * constructor without initial values.
     */
    public RapidBean() {
        this(EMPTY_STRING_ARRAY, null);
    }

    /**
     * constructor for generic beans without initial values and type.
     *
     * @param type the type
     */
    protected RapidBean(final TypeRapidBean type) {
        this(EMPTY_STRING_ARRAY, type);
    }

    /**
     * constructor with initial values as one String.
     *
     * @param initvals String with initial values braced with quotes
     *                 separated by a blank;
     */
    public RapidBean(final String initvals) {
        this(initvals, null);
    }

    /**
     * constructor with initial values as one String and type.
     *
     * @param initvals String with initial values braced with quotes
     *                 separated by a blank;
     * @param type the type
     */
    protected RapidBean(final String initvals, final TypeRapidBean type) {
        this(StringHelper.splitQuoted(initvals), type);
    }

    /**
     * constructor with initial values as String array.
     *
     * @param initvals String array with initial values.
     */
    public RapidBean(final String[] initvals) {
        this(initvals, null);
    }

    /**
     * constructor with initial values as String array.
     *
     * @param initvals String array with initial values.
     * @param argType possibility to give the type in advance
     */
    protected RapidBean(final String[] initvals, final TypeRapidBean argType) {
        final RapidBeanState stateBefore = this.beanState;
        try {
            this.beanState = RapidBeanState.initializing;
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
                        this.properties[j].setValue(initvals[j]);
                    }
                }
            }
            for (final PropertyCollection colProp : this.getColPropertiesComposition()) {
                final TypePropertyCollection colPropType = (TypePropertyCollection) colProp.getType();
                final int minmult = colPropType.getMinmult();
                if (minmult > 0) {
                    final TypeRapidBean targetType = colPropType.getTargetType();
                    for (int j = 0; j < minmult; j++) {
                        colProp.addLink(RapidBean.createInstance(targetType));
                    }
                }
            }
            this.beanState = RapidBeanState.initialized;
        } catch (RuntimeException e) {
            this.beanState = stateBefore;
            throw e;
        }
    }

    /**
     * parameter types for constructor RapidBean(String).
     */
    private static final Class<?>[] CONSTR_PARAMTYPES_STRING_ARRAY = {
        String[].class
    };

    /**
     * compare method.
     *
     * @param o the bean to compare with
     *
     * @return the compare value
     */
    public int compareTo(final Link link) {
        int compare = 0;

        if (link instanceof RapidBean) {
            final RapidBean bean = (RapidBean) link;

            // Assert id's not null
            if (this.id == null) {
                this.getId();
                if (this.id == null) {
                    throw new RapidBeansRuntimeException("could not determine id");
                }
            }
            if (bean.id == null) {
                bean.getId();
                if (bean.id == null) {
                    throw new RapidBeansRuntimeException("could not determine id");
                }
            }

            // get the property types to sort after from the thread local sorter
            final TypeProperty[] propTypes = BeanSorter.get();
            if (propTypes == null) {
                // if sorting is not required simply sort by ID (natural order)
                compare = compareIds(bean);
            } else {
                // if sorting by properties is required
                if (sameSortingProptypes(bean, propTypes)) {
                    // important note:
                    // equality of IDs must overrule sorting by properties
                    if (this.id.equals(bean.id)) {
                        compare = 0;
                    } else {
                        for (int i = 0; i < propTypes.length; i++) {
                            final String propname = propTypes[i].getPropName();
                            compare = this.getProperty(propname).compareTo(
                                    bean.getProperty(propname));
                            if (compare != 0) {
                                break;
                            }
                        }
                        if (compare == 0) {
                            compare = this.compareIds(bean);
                        }
                    }
                } else {
                    compare = this.compareIds(bean);
                }
            }
        } else if (link instanceof LinkFrozen) {
            compare = this.getIdString().compareTo(link.getIdString());
        } else {
            if (link != null) {
                throw new RapidBeansRuntimeException("cannot compare a bean with "
                    + link.getClass().getName());
            } else {
                throw new RapidBeansRuntimeException("cannot compare with null");
            }
        }

        if (compare > 1) {
            compare = 1;
        } else if (compare < -1) {
            compare = -1;
        }
        return compare;
    }

    /**
     * compare method.
     *
     * @param o the bean to compare with
     *
     * @return the compare value
     */
    public int compareToOld(final Object o) {
        int compare = 0;

        if (o instanceof RapidBean) {
            final RapidBean bean = (RapidBean) o;

            // do not compare apples with peaches
            if (this.getType() != bean.getType()) {
                return -2;
            }

            // get the property types to sort after from the thread local sorter
            final TypeProperty[] propTypes = BeanSorter.get();
            if (propTypes == null) {
                // if sorting is not required simply sort by ID (natural order)
                if (this.id != null && bean.id != null) {
                    compare = this.id.compareTo(bean.id);
                } else {
                    compare = this.getId().compareTo(bean.getId());
                }
            } else {
                // if sorting by properties is required
                
                // important note:
                // equality of IDs must overrule sorting by properties
                if (this.getId().equals(bean.getId())) {
                    compare = 0;
                } else {
                    for (int i = 0; i < propTypes.length; i++) {
                        final String propname = propTypes[i].getPropName();
                        compare = this.getProperty(propname).compareTo(
                                bean.getProperty(propname));
                        if (compare != 0) {
                            break;
                        }
                    }
                    if (compare == 0){
                        if (this.id != null && bean.id != null) {
                            compare = this.id.compareTo(bean.id);
                            if (compare == 0) {
                                compare = this.id.compareTo(bean.id);
                            }
                        } else {
                            compare = this.getId().compareTo(bean.getId());
                        }
                    }
                }
            }
        } else if (o instanceof LinkFrozen) {
            LinkFrozen link = (LinkFrozen) o;
            compare = this.getIdString().compareTo(link.getIdString());
        } else {
            throw new RapidBeansRuntimeException("cannot compare a bean with "
                    + o.getClass().getName());
        }

        if (compare > 1) {
            compare = 1;
        } else if (compare < -1) {
            compare = -1;
        }
        return compare;
    }

    /**
     * @param bean the bean to compare with
     *
     * @return the compare value
     */
    private int compareIds(final RapidBean bean) {
        int compare;
        if (idtypesComparable(bean)) {
            compare = this.id.compareTo(bean.id);
        } else {
            compare = (this.getIdString()).compareTo(bean.getIdString());
        }
        return compare;
    }

    /**
     * Helper to determine if id types are comparable
     *
     * @param bean the bean to compare with
     *
     * @return true if id types are comparable, false otherwise
     */
    final boolean idtypesComparable(final RapidBean bean) {
        if (this.getType() == bean.getType()) {
            return true;
        }
        if (this.id.getClass() == bean.id.getClass()) {
            if (this.id instanceof IdKeyprops) {
                final Property[] thisIdKeyprops = ((IdKeyprops) this.id).getKeyprops();
                final Property[] beanIdKeyprops = ((IdKeyprops) bean.id).getKeyprops();
                if (thisIdKeyprops.length != beanIdKeyprops.length) {
                    return false;
                }
                final int len = thisIdKeyprops.length;
                for (int i = 0; i < len; i++) {
                    if (thisIdKeyprops[i] != beanIdKeyprops[i]) {
                        return false;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper to determine if both bean types have all properties
     * required for sorting with same name and property type.
     *
     * @param bean the bean to compare with
     * @param propTypes the property types required for sorting
     *
     * @return if sorting can be accomplished according to the
     *         given property types
     */
    private boolean sameSortingProptypes(RapidBean bean,
            TypeProperty[] propTypes) {
        final TypeRapidBean thisType = this.getType();
        final TypeRapidBean beanType = bean.getType();
        for (final TypeProperty propType : propTypes) {
            final String propname = propType.getPropName();
            final TypeProperty thisProptype = thisType.getPropertyType(propname);
            if (thisProptype == null) {
                return false;
            }
        }
        if (thisType == beanType) {
            return true;
        }
        for (final TypeProperty propType : propTypes) {
            final String propname = propType.getPropName();
            final TypeProperty thisProptype = thisType.getPropertyType(propname);
            if (thisProptype == null) {
                return false;
            }
            final String thisPropClass = thisProptype.getClass().getName();
            final TypeProperty beanProptype = beanType.getPropertyType(propname);
            if (beanProptype == null) {
                return false;
            }
            final String beanPropClass = beanProptype.getClass().getName();
            if (!thisPropClass.equals(beanPropClass)) {
                return false;
            }
        }
        return true;
    }

    /**
     * A bean equals another bean it has the same type
     * and an equal identity.
     *
     * @param o the object to compare
     * @return if the object equals or not
     */
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        try {
            RapidBean bean = (RapidBean) o;
            if (!(this.getType() == bean.getType())) {
                return false;
            }
            return this.getId().equals(bean.getId());
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * the hashcode of a bean is the hashcode of it's id.
     * @return the hashcode
     */
    public int hashCode() {
        if (this.getId() instanceof IdTransientid) {
            return super.hashCode();
        }
        return this.getId().hashCode();
    }

    /**
     * PropertyChangeListenr collection.
     */
    private Collection<PropertyChangeListener> changeListeners = new ArrayList<PropertyChangeListener>();

    /**
     * adds a new PropertyChangeListener.
     * @param l the new listener to add
     */
    public void addPropertyChangeListener(final PropertyChangeListener l) {
        this.changeListeners.add(l);
    }

    /**
     * adds a new PropertyChangeListener.
     * @param l the new listener to add
     */
    public void removePropertyChangeListener(final PropertyChangeListener l) {
        this.changeListeners.remove(l);
    }

    /**
     * Fires a property pre change event for that bean.
     * For specific processing simply override and call super().
     *
     * @param event the PropertyChangeEvent to fire
     */
    protected void propertyChangePre(
            final PropertyChangeEvent event) {
        if (this.changeListeners != null && this.changeListeners.size() > 0) {
            for (PropertyChangeListener l : this.changeListeners) {
                l.propertyChangePre(event);
            }
        }
        final Container container = this.getContainer();
        if (container != null) {
            container.fireBeanChangePre(event);
        }
    }

    /**
     * Fires a property post change event for that bean.
     * For specific processing simply override and call super().
     *
     * @param event the PropertyChangeEvent to fire
     */
    protected void propertyChanged(final PropertyChangeEvent event) {
        if (this.changeListeners != null && this.changeListeners.size() > 0) {
            for (PropertyChangeListener l : this.changeListeners) {
                l.propertyChanged(event);
            }
        }
        final Container container = getContainer();
        if (container != null) {
            container.fireBeanChanged(event);
        }
    }

    /**
     * computes a localized String to present this Bean in a UI.
     *
     * @param locale the Locale
     *
     * @return the localized String for this Bean
     */
    public String toStringGui(final RapidBeansLocale locale) {
        final String sGuiType = toStringGuiType(locale);
        final String sGuiId = toStringGuiId(locale);
        if (sGuiType.equals(sGuiId)) {
            return sGuiId;
        } else {
            if (this.getType().getIdtype() == IdType.transientid
                    && sGuiId.equals(this.getIdString())) {
                return sGuiType;
            } else {
                return sGuiType + ": " + sGuiId;
            }
        }
    }

    /**
     * computes a localized String to present the Bean's type name in a UI.
     * @param locale the Locale
     * @return the localized String for this Bean
     */
    public String toStringGuiType(final RapidBeansLocale locale) {
        return this.getType().toStringGui(locale, false, null);
    }

    /**
     * computes a localized String to present this Bean's instance name in a UI.
     * @param locale the Locale
     * @return the localized String for this Bean
     */
    public String toStringGuiId(final RapidBeansLocale locale) {
        String uistring = null;
        if (uistring == null) {
            try {
                final String key = "bean."
                    + this.getType().getName().toLowerCase() + ".id";
                uistring = locale.getStringGui(key);
                uistring = expandPropertyValues(uistring, locale);
            } catch (MissingResourceException e) {
                uistring = null;
            }
        }
        if (uistring == null) {
            if (this.getType().getIdtype() == IdType.transientid) {
                uistring = toStringGuiType(locale);
            } else {
                uistring = this.getIdString();
            }
        }
        return uistring;
    }

    /**
     * expand the curly braced property names with the bean's property values.
     * @param pattern the pattern with curly braces e. g. "{firstname}, {lastname}"
     * @param locale the locale
     * @return the expanded string
     */
    public String expandPropertyValues(
            final String pattern, final RapidBeansLocale locale) {
        char c;
        int len = pattern.length();
        int state = 0;
        StringBuffer bufExpanded = new StringBuffer();
        StringBuffer bufPropname = new StringBuffer();
        StringBuffer bufParam = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        String propname = null;
        String methodname = null;
        List<Object> parameters = new ArrayList<Object>();
        Class<?>[] parameterTypes = null;
        int j, ptsize;
        Property prop;
        Method method;
        List<String> sa;
        int sasize;
        String s;
        for (int i = 0; i < len; i++) {
            c = pattern.charAt(i);
            switch (state) {
            case 0: // out of curley brace
                switch (c) {
                case '{':
                    state = 1;
                    break;
                default:
                    bufExpanded.append(c);
                break;
                }
                break;
            case 1: // within curley brace
                switch (c) {
                case '(':
                    sa = StringHelper.split(bufPropname.toString(), ".");
                    sasize = sa.size();
                    for (j = 0; j < sasize; j++) {
                        if (j < (sasize - 1)) {
                            if (j > 0) {
                                sb.append('.');
                            }
                            sb.append(sa.get(j));
                        } else {
                            propname = sb.toString();
                            sb.setLength(0);
                            methodname = sa.get(j);
                        }
                    }
                    bufPropname.setLength(0);
                    parameters.clear();
                    parameters.add(locale);
                    state = 2;
                    break;
                case '}':
                    if (bufPropname.length() > 0) {
                        prop = dereferencePropname(bufPropname.toString());
                        if (prop != null) {
                            bufExpanded.append(prop.toStringGui(locale));
                            prop = null;
                        } else {
                            bufExpanded.append(bufPropname.toString());
                        }
                        bufPropname.setLength(0);
                    }
                    state = 0;
                    break;
                default:
                    bufPropname.append(c);
                break;
                }
                break;
            case 2: // within method parameter brace
                switch (c) {
                case ')':
                    prop = null;
                    if (propname != null) {
                        prop = dereferencePropname(propname);
                    }
                    if (bufParam.length() > 0) {
                        parameters.add(bufParam.toString().trim());
                        bufParam.setLength(0);
                    }
                    ptsize = parameters.size();
                    parameterTypes = new Class[ptsize];
                    parameterTypes[0] = RapidBeansLocale.class;
                    for (j = 1;  j < ptsize; j++) {
                        parameterTypes[j] = String.class;
                    }
                    method = null;
                    try {
                        if (prop != null) {
                            method = prop.getClass().getMethod(methodname, parameterTypes);
                        }
                    } catch (NoSuchMethodException e) {
                        throw new RapidBeansRuntimeException(e);
                    }
                    if (prop != null && method != null) {
                        try {
                            s = (String) method.invoke(prop, parameters.toArray());
                        } catch (IllegalArgumentException e) {
                            throw new RapidBeansRuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RapidBeansRuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RapidBeansRuntimeException(e);
                        }
                        bufExpanded.append(s);
                    } else {
                        bufExpanded.append(bufPropname.toString());
                    }
                    parameters.clear();
                    state = 1;
                    break;
                case ',':
                    if (bufParam.length() > 0) {
                        parameters.add(bufParam.toString());
                        bufParam.setLength(0);
                    }
                    break;
                default:
                    bufParam.append(c);
                break;
                }
                break;
            default:
                throw new UtilException("expandBeanPropertyValues(\""
                        + pattern + "\") failed\n"
                        + "wrong state " + state);
            }
        }
        if (state == 1) {
            throw new UtilException("expandBeanPropertyValues(\""
                    + pattern + "\") failed\n"
                    + "missing closing }");
        }
        return bufExpanded.toString();
    }

    /**
     * find a property out of the given property name.
     * The property name can be ether a simple name or a
     * chain of reference (collection, map) properties.
     * The most right property can be an arbitrary one.
     *
     * @param propname property name (chain)
     *
     * @return the referenced property
     */
    private Property dereferencePropname(final String propname) {
        Property prop = null;
        if (propname.indexOf(".") == -1) {
            prop = this.getProperty(propname);
        } else {
            RapidBean linkedBean = this;
            final List<String> propnames = StringHelper.split(propname, ".");
            final int propnamessize = propnames.size();
            int j = 0;
            for (final String pname : propnames) {
                j++;
                if (pname.equals("parentBean")) {
                    linkedBean = linkedBean.getParentBean();
                    if (linkedBean == null) {
                        prop = null;
                        break;
                    } else {
                        continue;
                    }
                } else {
                    prop = linkedBean.getProperty(pname);
                }
                if (j < propnamessize) {
                    if (prop instanceof PropertyCollection) {
                        Collection<?> col = (Collection<?>) prop.getValue();
                        if (col != null) {
                            linkedBean = (RapidBean) col.iterator().next();
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return prop;
    }

    /**
     * @return a clone of this bean.
     */
    public RapidBean clone() {
        RapidBean bClone = RapidBean.createInstance(this.getType());
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
     * @return a clone of this bean including
     * the whole hierarchy. The container in set to
     * null. Non compositions links are frozen.
     */
    public RapidBean cloneExternal(final Container cloneContainer) {
        RapidBean bClone = RapidBean.createInstance(this.getType());
        bClone.setId(Id.createInstance(bClone, this.getIdString()));
        if (cloneContainer != null && (!cloneContainer.contains(bClone))) {
            ((ContainerImpl) cloneContainer).insert(bClone, true);
        }
        bClone.setContainer(cloneContainer);
        for (int i = 0; i < this.properties.length; i++) {
            this.properties[i].cloneValue(bClone.properties[i], cloneContainer);
        }
        return bClone;
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

    /**
     * trace that bean.
     */
    @SuppressWarnings("unchecked")
    public void trace() {
        log.info("BIZ BEAN: " + this.getType().getNameShort()
                + "::" + this.getIdString());
        for (PropertyCollection prop : this.getColProperties()) {
            if (prop.getValue() != null) {
                for (final Link link : (Collection<Link>) prop.getValue()) {
                    if (link instanceof RapidBean) {
                        if (((TypePropertyCollection) prop.getType()).isComposition()) {
                            ((RapidBean) link).trace();
                        } else {
                            log.info("BEAN LINK: "
                                    + ((RapidBean) link).getIdString());
                        }
                    } else {
                        log.info("FROZEN LINK: "
                                + ((LinkFrozen) link).getIdString());
                    }
                }
            }
        }
    }
}
