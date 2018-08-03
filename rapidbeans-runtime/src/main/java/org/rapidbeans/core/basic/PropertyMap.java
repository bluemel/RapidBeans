/*
 * Rapid Beans Framework: PropertyMap.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 12/13/2007
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypePropertyMap;

/**
 * A <b>Collection</b> bean property encapsulates a collection of beans.<br/>
 * Map is similar to collection. You can easily define associations between
 * beans that then can be accessed via a key.
 * 
 * @author Martin Bluemel
 */
public class PropertyMap extends PropertyAssociationend {

	/**
	 * the map of chosen value. !!! do not initialize here as this would overwrite
	 * the super classes default constructor's behavior.
	 */
	private Map<String, Link> value;

	/**
	 * constructor for a new Collection Property.
	 * 
	 * @param type       the Property's type
	 * @param parentBean the parent bean
	 */
	public PropertyMap(final TypeProperty type, final RapidBean parentBean) {
		super(type, parentBean);
		if (this.value == null && this.getType().getMandatory()) {
			this.value = createNewMap();
		}
	}

	/**
	 * just an empty object array.
	 */
	private static final Object[] MAP_CONSTR_ARGS = new Object[0];

	/**
	 * internal creation of new collections.
	 * 
	 * @return the collection
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Link> createNewMap() {
		final Class<?> mapClass = ((TypePropertyMap) this.getType()).getMapClass();

		// construct standard collection
		if (mapClass == HashMap.class) {
			return new HashMap<String, Link>();
		} else if (mapClass == LinkedHashMap.class) {
			return new LinkedHashMap<String, Link>();
		}

		// use reflective creation for special collection classes
		Map<String, Link> newMap = null;
		try {
			newMap = (Map<String, Link>) ((TypePropertyMap) this.getType()).getMapClassConstructor()
					.newInstance(MAP_CONSTR_ARGS);
		} catch (IllegalAccessException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		} catch (InstantiationException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		}
		return newMap;
	}

	/**
	 * generic value getter. since collections are n o t immutable and we don't want
	 * to clone a collection that could be very large we just give out the iterator.
	 * 
	 * @return a collection of beans
	 */
	public Map<String, Link> getValue() {
		if (this.value == null) {
			return null;
		} else {
			return this.value;
		}
	}

	/**
	 * generic value getter. since collections are n o t immutable and we don't want
	 * to clone a collection that could be very large we just give out the iterator.
	 * 
	 * @return a collection of beans
	 */
	public ReadonlyListCollection<Link> getValueCollection() {
		if (this.value == null) {
			return null;
		} else {
			return (ReadonlyListCollection<Link>) new ReadonlyListCollection<Link>(this.value.values(),
					(TypePropertyCollection) this.getType());
		}
	}

	/**
	 * generic value setter.
	 * 
	 * @param newValue the new value for this property
	 */
	public void setValue(final Object newValue) {
		this.setValue(newValue, true, true, true);
	}

	/**
	 * generic value setter absolute experts with fine tuning possibilities.
	 * 
	 * @param col                                  the new value for this property
	 * @param validate                             switch that allows turning off
	 *                                             validation Caution: handle with
	 *                                             very much care.
	 * @param touchInverseLinks                    if an inverse link will be added
	 *                                             or not
	 * @param checkContainerLinksToExternalObjects determines if links from an
	 *                                             object living inside the
	 *                                             container should be allowed. Be
	 *                                             very careful to set this argument
	 *                                             to false.
	 */
	public void setValue(final Object col, final boolean validate, final boolean touchInverseLinks,
			final boolean checkContainerLinksToExternalObjects) {
		final Map<String, Link> newValue = convertValue(col);
		if (validate) {
			validate(newValue);
		}
		super.setValueWithEvents(this.getValue(), newValue, new PropertyValueSetter() {
			@SuppressWarnings("unchecked")
			public void setValue(Object newValue) {
				value = (Map<String, Link>) newValue;
			}
		});
		// if (this.value != null) {
		// for (Link curLink : this.value.values()) {
		// if (touchInverseLinks && curLink instanceof RapidBean) {
		// removeInverseLink((RapidBean) curLink, true);
		// }
		// }
		// }
		// Collection oldCol = null;
		// if (this.value != null) {
		// oldCol = this.value.values();
		// }
		// this.value = newCol;
		// if (this.value != null) {
		// for (Link curLink : this.value.values()) {
		// if (touchInverseLinks && curLink instanceof RapidBean) {
		// addInverseLink((RapidBean) curLink, true,
		// checkContainerLinksToExternalObjects);
		// }
		// }
		// }
		//
		// if ((oldCol == null && newCol != null)
		// || (oldCol != null && newCol == null)
		// || ((oldCol != null && newCol != null)
		// && (!oldCol.equals(newCol)))) {
		// fireChanged(this, PropertyChangeEventType.set,
		// oldCol, newCol, null);
		// }
	}

	/**
	 * converts a variety of types to the neccessary map.
	 * 
	 * @param collectionValue the value to convert
	 * 
	 * @return a map of bean links
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Link> convertValue(final Object newValue) {

		Map<String, Link> newMapValue = null;

		if (newValue != null) {
			// if (collectionValue instanceof Collection) {
			// final Map argValList = (Collection) collectionValue;
			// collection = createNewCollection();
			// for (Object obj : argValList) {
			// if (obj == null) {
			// throw new
			// ValidationException("invalid.prop.collection.contentnull",
			// "invalid null object");
			// } else if (obj instanceof RapidBean || obj instanceof LinkFrozen)
			// {
			// collection.add((Link) obj);
			// } else if (obj instanceof String) {
			// collection.add(new LinkFrozen((String) obj));
			// } else {
			// throw new
			// ValidationException("invalid.prop.collection.contenttype",
			// "Collection property \"" + this.getType().getPropName() + "\": "
			// + " invalid content data type " + obj.getClass().getName()
			// +
			// ".\nOnly \"RapidBean\", \"LinkFrozen\", and \"String\" are valid types.");
			// }
			// }
			if (newValue instanceof Map) {
				newMapValue = (Map<String, Link>) newValue;
			} else if (newValue instanceof LinkWithKey) {
				newMapValue = createNewMap();
				newMapValue.put(((LinkWithKey) newValue).getKey(), ((LinkWithKey) newValue).getLink());
			} else if (newValue instanceof String) {
				newMapValue = createNewMap();
				final String newValueString = (String) newValue;
				if (newValueString.length() > 0) {
					throw new RuntimeException("not yet implemented");
				}
				// final String[] frozenLinkDescriptions =
				// UtilsString.tokenize((String) collectionValue, ",");
				// for (int i = 0; i < frozenLinkDescriptions.length; i++) {
				// collection.add(new LinkFrozen(frozenLinkDescriptions[i]));
				// }
			}
			// } else if (collectionValue instanceof String[]) {
			// collection = createNewCollection();
			// String[] sa = (String[]) collectionValue;
			// for (int i = 0; i < sa.length; i++) {
			// collection.add(new LinkFrozen(sa[i]));
			// }
			// } else {
			// throw new ValidationException("invalid.prop.collection.type",
			// "Collection property \"" + this.getType().getPropName() + "\": "
			// + " invalid data type " + collectionValue.getClass().getName()
			// + ".\nOnly \"RapidBean\", \"Collection<RapidBean>\","
			// + " \"String[]\", and \"String\" are valid types.");
			// }
		}

		return newMapValue;
	}

	public void putLink(final String key, final Link link) {
		if (this.value == null) {
			validate(null);
			this.value = new HashMap<String, Link>();
		}
		this.value.put(key, link);
	}

	// /**
	// * adds the inverse link.
	// *
	// * @param linkedBean the bean linked
	// * @param addInverse the switch to prevent from endless recursion
	// * @param checkContainerLinksToExternalObjects
	// * determines if links from an object living inside the
	// * container should be allowed.
	// * Be very careful to set this argument to false.
	// */
	// private void addInverseLink(final RapidBean linkedBean, final boolean
	// addInverse,
	// final boolean checkContainerLinksToExternalObjects) {
	// TypePropertyCollection colType = (TypePropertyCollection) this.getType();
	// RapidBean bean = this.getBean();
	// ContainerImpl container = null;
	// if (bean != null) {
	// container = (ContainerImpl) bean.getContainer();
	// }
	// Container linkedContainer = linkedBean.getContainer();
	// if (colType.isComposition()) {
	// if (linkedBean.getParentBean() != null
	// && linkedBean.getParentBean() != this.getBean()) {
	// throw new RapidBeansRuntimeException("tried to add a bean as component"
	// + " that already is component of another bean (has a parent)");
	// }
	// linkedBean.setParentBean(this.getBean());
	// if (container == null) {
	// if (linkedContainer != null) {
	// throw new RapidBeansRuntimeException("tried to compose a bean"
	// + " living outside a container"
	// + " with one inside a container");
	// }
	// } else {
	// if (linkedContainer == null) {
	// // insert a component implicitely into the container
	// linkedBean.setContainer(container);
	// container.insert(linkedBean, true);
	// } else if (container != linkedContainer) {
	// if (container == null) {
	// throw new
	// RapidBeansRuntimeException("tried to associate a bean living outside a
	// container"
	// + " with one inside a container");
	// } else {
	// throw new
	// RapidBeansRuntimeException("tried to compose a bean living outside a
	// container"
	// + " with one inside a different container");
	// }
	// }
	// }
	// } else if (colType.getInverse() != null && addInverse) {
	// PropertyMap inverseColProp = (PropertyMap)
	// linkedBean.getProperty(colType.getInverse());
	// if (inverseColProp != null) {
	// // if we have a 1:1 association and the bean to link (linked bean) is
	// // already linked to another instance refuse linkage
	// if (colType.getMaxmult() == 1) {
	// final TypePropertyCollection inverseColPropType =
	// (TypePropertyCollection) inverseColProp.getType();
	// final Collection<RapidBean> inverseColPropValue = (Collection<RapidBean>)
	// inverseColProp.getValue();
	// if (inverseColPropType.getMaxmult() == 1
	// && inverseColPropValue != null
	// && inverseColPropValue.size() == 1
	// && inverseColPropValue.iterator().next() instanceof RapidBean) {
	// final Object[] oa = {
	// this.getBean().getType() + ": " + this.getBean().getIdString(),
	// inverseColPropValue.iterator().next().getType().getName() + ": "
	// + inverseColPropValue.iterator().next().getIdString()
	// };
	// throw new
	// ValidationException("invalid.prop.collection.add.target.alreadey.added",
	// "Bean \"" + this.getBean().getType().getName() + "::"
	// + this.getBean().getIdString() + "\":"
	// + "Collection property \"" + this.getType().getPropName() + "\": "
	// + " failed to add bean "
	// + inverseColPropValue.iterator().next().getIdString()
	// + ".\n1:1 associated bean already linked.", oa);
	// }
	// }
	// // inverseColProp.addLink(this.getBean(), false,
	// checkContainerLinksToExternalObjects, true);
	// if (checkContainerLinksToExternalObjects) {
	// if (container == null) {
	// if (linkedContainer != null) {
	// throw new RapidBeansRuntimeException("tried to associate"
	// + " a bean living outside a container"
	// + " with one inside a container");
	// }
	// } else {
	// if (linkedContainer == null) {
	// throw new RapidBeansRuntimeException("tried to associate a"
	// + " bean living inside a container"
	// + " with one outside a container");
	// } else if (container != linkedContainer) {
	// throw new RapidBeansRuntimeException("tried to associate a"
	// + " bean living outside a container"
	// + " with one inside a different container");
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// /**
	// * remove a bean reference from the Collection Property.
	// *
	// * @param key the bean reference to remove
	// * @param removeInverse if the inverse link has to be removed
	// * @param throwNotFound throw an exception if the link to remove is not in
	// * @param validate validate
	// */
	// public void removeLink(final Object key, final boolean removeInverse,
	// final boolean throwNotFound, final boolean validate) {
	// if (!removeInverse && (this.value == null || this.value.size() == 0)) {
	// return;
	// }
	// if (this.value != null && validate && this.value.size()
	// <= ((TypePropertyCollection) this.getType()).getMinmult()
	// && (this.getBean().getBeanState() != RapidBeanState.deleting)) {
	// throw new ValidationException("invalid.prop.collection.remove.minmult",
	// "Collection property \"" + this.getType().getPropName() + "\": "
	// + " failed to remove bean with key from map property " + key
	// + ".\nMinimal multiplicity "
	// + ((TypePropertyCollection) this.getType()).getMinmult()
	// + " undergone.");
	// }
	// final Link link = (RapidBean) this.value.get(key);
	// if (validate) {
	// this.validate(link);
	// }
	// if (link == null) {
	// if (throwNotFound) {
	// throw new BeanNotFoundException(
	// "Collection property \"" + this.getType().getPropName() + "\": "
	// + " failed to remove null bean " + key.toString()
	// + ".\nBean is not in this collection.");
	// }
	// }
	// this.value.remove(key);
	// if (link instanceof RapidBean) {
	// try {
	// removeInverseLink((RapidBean) link, removeInverse);
	// } catch (BeanNotFoundException e) {
	// return;
	// }
	// }
	// fireChanged(this, PropertyChangeEventType.removelink,
	// null, null, link);
	// }
	//
	// /**
	// * adds the inverse link.
	// *
	// * @param linkedBean the bean linked
	// * @param removeInverse the switch to prevent from endless recursion
	// */
	// private void removeInverseLink(final RapidBean linkedBean, final boolean
	// removeInverse) {
	// TypePropertyCollection colType =
	// (TypePropertyCollection) this.getType();
	// if (colType.isComposition()) {
	// linkedBean.setParentBean(null);
	// if (linkedBean.getContainer() != null) {
	// linkedBean.getContainer().delete(linkedBean);
	// linkedBean.setContainer(null);
	// }
	// } else if (colType.getInverse() != null && removeInverse) {
	// PropertyMap inverseColProp = (PropertyMap)
	// linkedBean.getProperty(colType.getInverse());
	// if (inverseColProp != null) {
	// inverseColProp.removeLink(this.getBean(), false, false, false);
	// }
	// }
	// }
	//
	// /**
	// * initialize default value.
	// *
	// * @param propType the Property type
	// * @param parentBean the parent bean
	// */
	// protected void initDefaultValue(final TypeProperty propType,
	// final RapidBean parentBean) {
	// try {
	// this.setValue(propType.getDefaultValue());
	// } catch (ValidationMandatoryException e) {
	// this.value = createNewMap();
	// }
	// }
	//
	/**
	 * implementation of toString().
	 * 
	 * @return the String representation of this collection
	 */
	public String toString() {
		if (this.value == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (Link link : this.value.values()) {
			if (i > 0) {
				sb.append(',');
			}
			sb.append(link.getIdString());
			i++;
		}
		return sb.toString();
	}

	// /**
	// * validation for Choice Properties.
	// *
	// * @param newCollection the new value
	// *
	// * @return the converted value which is the internal representation or if
	// a
	// * primitive type the corresponding value object
	// */
	// public Map<Object, Link> validateMap(final Object newCollection) {
	// final Map<Object, Link> map = (Map<Object, Link>)
	// super.validate(newCollection);
	// if (newCollection == null) {
	// return map;
	// }
	// for (Link link : map.values()) {
	// if (link instanceof RapidBean) {
	// this.validate((RapidBean) link);
	// }
	// }
	// return map;
	// }
	//
	// /**
	// * validate a single bean.
	// *
	// * @param bean the bean to validate
	// *
	// * @return the converted value which is the internal representation or if
	// a
	// * primitive type the corresponding value object
	// */
	// protected Map<String, Link> validateMap(final RapidBean bean) {
	// TypePropertyCollection collectionType = (TypePropertyCollection)
	// this.getType();
	// if (bean != null) {
	// if (!TypeRapidBean.isSameOrSubtype(collectionType.getTargetType(),
	// bean.getType())) {
	// throw new ValidationException("invalid.prop.collection.targettype",
	// "Collection property \"" + this.getType().getPropName() + "\": "
	// + " invalid bean type \"" + bean.getType().getName()
	// + "\".\nType \"" + collectionType.getTargetType().getName()
	// + "\" is required.");
	// }
	// }
	// Map<String, Link> map = createNewMap();
	// // map.add(bean);
	// return map;
	// }

	// /**
	// * Helper function.
	// *
	// * @param idstr the id string to search for
	// *
	// * @return if the collection already contains a link with the given
	// idstring.
	// */
	// private boolean containsFrozenLinkWithIdstring(final String idstr) {
	// boolean contains = false;
	// for (Link link : this.value.values()) {
	// if (link instanceof LinkFrozen
	// && link.getIdString().equals(idstr)) {
	// contains = true;
	// break;
	// }
	// }
	// return contains;
	// }

	public void removeLink(final String key) {
		throw new RuntimeException("not yet implemented");
	}
}
