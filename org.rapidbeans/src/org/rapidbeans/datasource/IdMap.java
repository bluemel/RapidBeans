/*
 * Rapid Beans Framework: IdMap.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/01/2000
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

package org.rapidbeans.datasource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.type.TypeRapidBean;

/**
 * A straight forward implementation of an identity map. It consists in a hash
 * map (by bean typename) that contains itself hash maps (by bean identity.
 * 
 * @author Martin Bluemel
 */
public final class IdMap {

	/**
	 * the identity map.
	 */
	private HashMap<String, TreeMap<String, RapidBean>> idmap;

	/**
	 * the default constructor.
	 */
	public IdMap() {
		this.idmap = new HashMap<String, TreeMap<String, RapidBean>>();
	}

	/**
	 * the read method.
	 * 
	 * @param bean
	 *            the bean
	 */
	public void read(final RapidBean bean) {
		throw new DatasourceException("\"read\" forbidden for pooled beans");
	}

	/**
	 * the read method.
	 * 
	 * @param bean
	 *            the bean
	 */
	public void update(final RapidBean bean) {
		throw new DatasourceException("\"update\" forbidden for pooled beans");
	}

	/**
	 * register a bean to the map.
	 * 
	 * @param bean
	 *            the bean to register
	 */
	public void insert(final RapidBean bean) {
		String typename = bean.getType().getName();
		TreeMap<String, RapidBean> submap = this.idmap.get(typename);
		if (submap == null) {
			submap = new TreeMap<String, RapidBean>();
			this.idmap.put(typename, submap);
		}
		String id = bean.getId().toString();
		if (this.findBean(typename, id) != null) {
			throw new DatasourceException("Failed to insert bean \"" + typename
					+ "::" + id + "\" into pool.\n" + "It is already in there.");
		}
		submap.put(id, bean);
	}

	/**
	 * unregister a bean from the HashMap.
	 * 
	 * @param bean
	 *            the bean to unregister
	 */
	public void delete(final RapidBean bean) {
		String typename = bean.getType().getName();
		TreeMap<String, RapidBean> map = this.idmap.get(bean.getType()
				.getName());
		if (map == null) {
			throw new DatasourceException("Failed to delete bean of \""
					+ typename + "\" out of pool.\n"
					+ "No bean of this type is in there.");
		}
		String id = bean.getId().toString();
		RapidBean pooledBean = (RapidBean) map.get(id);
		if (pooledBean == null) {
			throw new DatasourceException("Failed to insert bean \"" + id
					+ "\" out of pool.\n" + "No bean of this type is in there.");
		}
		map.remove(id);
		if (map.size() == 0) {
			this.idmap.remove(map);
		}
	}

	/**
	 * prooves if a BiBean is alread in this Map or not.
	 * 
	 * @param typename
	 *            the bean type name
	 * @param id
	 *            the bean's id as string
	 * 
	 * @return if the idmap contains a certain bean or not
	 */
	public boolean contains(final String typename, final String id) {
		TreeMap<String, RapidBean> map = this.idmap.get(typename);
		if (map == null) {
			return false;
		}
		return (map.get(id.toString()) != null);
	}

	/**
	 * retrieves a bean out of this Map.
	 * 
	 * @param typename
	 *            the bean type name
	 * @param id
	 *            the bean's id as string
	 * 
	 * @return if the idmap contains a certain bean or not
	 */
	public RapidBean findBean(final String typename, final String id) {
		RapidBean foundBean = null;
		TreeMap<String, RapidBean> map = this.idmap.get(typename);
		if (map != null) {
			foundBean = map.get(id.toString());
		}
		if (foundBean == null) {
			final Collection<TypeRapidBean> subtypes = TypeRapidBean.forName(
					typename).getSubtypes();
			for (TypeRapidBean subtype : subtypes) {
				map = this.idmap.get(subtype.getName());
				if (map != null) {
					foundBean = map.get(id.toString());
				}
				if (foundBean != null) {
					break;
				}
			}
		}
		return foundBean;
	}

	/**
	 * find types of all beans stored in this DB.
	 * 
	 * @return a list of strings with the typenames
	 */
	public Set<String> findAllTypenames() {
		return this.idmap.keySet();
	}

	/**
	 * query for all beans of a type.
	 * 
	 * @param typename
	 *            the name of the bean type for which you want to find
	 *            instances.
	 * 
	 * @return a list with all found beans
	 */
	public Set<String> findAllIds(final String typename) {
		return this.idmap.get(typename).keySet();
	}

	// /**
	// * query for all beans of a type.
	// *
	// * @param typename the name of the bean type for which you
	// * want to find instances.
	// *
	// * @return a collection with all found beans
	// */
	// public Collection<RapidBean> findAll(final String typename) {
	// HashMap<String, RapidBean> map = this.idmap.get(typename);
	// if (map == null) {
	// return new ArrayList<RapidBean>();
	// }
	// return map.values();
	// }

	/**
	 * query for all beans of a type.
	 * 
	 * @param typename
	 *            the name of the bean type for which you want to find
	 *            instances.
	 * 
	 * @return a list with all found beans
	 */
	public List<RapidBean> findBeansByType(final String typename) {
		final List<RapidBean> all = new ArrayList<RapidBean>();
		findAll(TypeRapidBean.forName(typename), all);
		return all;
	}

	/**
	 * implements a recursive traversal through the whole inheritance tree to
	 * fill the result set with all objects of this type and all subtypes.
	 * 
	 * @param type
	 *            the type of beans to search for
	 * @param all
	 *            the collection to fill with all objects
	 */
	private void findAll(final TypeRapidBean type, final List<RapidBean> all) {
		TreeMap<String, RapidBean> map = this.idmap.get(type.getName());
		if (map != null) {
			Collection<RapidBean> values = map.values();
			for (RapidBean bean : values) {
				all.add(bean);
			}
		}
		for (TypeRapidBean subtype : type.getSubtypes()) {
			findAll(subtype, all);
		}
	}

	// /**
	// * find a set of beans by query.
	// *
	// * @param squery the query string.
	// *
	// * @return a list with all found beans
	// */
	// public List<RapidBean> findBeansByQuery(final String squery) {
	// return this.findBeansByQuery(new Query(squery));
	// }
	//
	// /**
	// * find a set of beans by query.
	// *
	// * @param query the query.
	// *
	// * @return a list with all found beans
	// */
	// public List<RapidBean> findBeansByQuery(final Query query) {
	// return query.findBeans(this);
	// }
	//
	// /**
	// * find a single bean by query.
	// *
	// * @param squery the query string.
	// *
	// * @return the bean found or null
	// */
	// public RapidBean findBeanByQuery(final String squery) {
	// return this.findBeanByQuery(new Query(squery));
	// }
	//
	// /**
	// * find a single bean by query.
	// *
	// * @param query the query.
	// *
	// * @return the bean found or null
	// */
	// public RapidBean findBeanByQuery(final Query query) {
	// return query.findBean(this);
	// }
}
