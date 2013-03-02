/*
 * Rapid Beans Framework: Filter.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/24/2007
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

import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.datasource.query.Query;
import org.rapidbeans.datasource.query.QueryExprConditionType;

/**
 * A Filter defines a set of filter rules applied to a set of beans. You apply
 * this filter to a document and then simply query if a bean is included.
 * 
 * @author Martin Bluemel
 */
public class Filter {

	/**
	 * The document to filter.
	 */
	private Document document = null;

	/**
	 * the include rules.
	 */
	private ArrayList<Query> includesRules = new ArrayList<Query>();

	/**
	 * the include rules.
	 */
	private ArrayList<Query> excludesRules = new ArrayList<Query>();

	/**
	 * contains references to all filtered beans.
	 */
	private IdMap idMap = new IdMap();

	/**
	 * contains references to all filtered types.
	 */
	private HashMap<String, TypeRapidBean> typeMap = new HashMap<String, TypeRapidBean>();

	/**
	 * test if a bean applies to the filter.
	 * 
	 * @param bean
	 *            the bean to test
	 * 
	 * @return if the bean applies
	 */
	public boolean applies(final RapidBean bean) {
		if (this.document == null) {
			throw new RapidBeansRuntimeException("Filter not correctly initialized");
		}
		return this.idMap.contains(bean.getType().getName(), bean.getIdString());
	}

	/**
	 * test if a bean type applies to the filter.
	 * 
	 * @param type
	 *            the type to test
	 * 
	 * @return if the bean applies
	 */
	public boolean applies(final TypeRapidBean type) {
		if (this.document == null) {
			throw new RapidBeansRuntimeException("Filter not correctly initialized");
		}
		return this.typeMap.get(type.getName()) != null;
	}

	/**
	 * Add an "includes" rule to the filter
	 * 
	 * @param includesRule
	 *            a query that describes the objects to include
	 */
	public void addIncludes(final String includesRule) {
		this.includesRules.add(new Query(includesRule));
	}

	/**
	 * Add an "excludes" rule to the filter
	 * 
	 * @param excludesRule
	 *            a query that describes the objects to exclude
	 */
	public void addExcludes(final String excludesRule) {
		this.excludesRules.add(new Query(excludesRule));
	}

	/**
	 * applies the given filter to the filter document.
	 * 
	 * @param doc
	 *            the document to filter
	 */
	public void setDocument(final Document doc) {
		this.document = doc;
		this.idMap = new IdMap();
		if (this.includesRules != null && this.includesRules.size() > 0) {
			for (final Query includesRule : this.includesRules) {
				if (includesRule.getQueryExpressionTreeRoot() instanceof QueryExprConditionType) {
					final String typename = ((QueryExprConditionType) includesRule.getQueryExpressionTreeRoot())
							.getTypename();
					if (this.typeMap.get(typename) == null) {
						this.typeMap.put(typename, TypeRapidBean.forName(typename));
					}
				}
				for (final RapidBean bean : doc.findBeansByQuery(includesRule)) {
					if (this.idMap.findBean(bean.getType().getName(), bean.getIdString()) == null) {
						this.idMap.insert(bean);
						RapidBean parentBean = bean.getParentBean();
						while (parentBean != null
								&& this.idMap.findBean(parentBean.getType().getName(), parentBean.getIdString()) == null) {
							this.idMap.insert(parentBean);
							parentBean = parentBean.getParentBean();
						}
					}
				}
			}
		} else {
			// take all beans of the document
			for (final String typename : doc.findAllTypenames()) {
				final TypeRapidBean rbtype = TypeRapidBean.forName(typename);
				this.typeMap.put(typename, rbtype);
				for (final RapidBean bean : doc.findBeansByType(typename)) {
					this.idMap.insert(bean);
				}
			}
		}
		for (final Query excludesRule : this.excludesRules) {
			for (final RapidBean bean : doc.findBeansByQuery(excludesRule)) {
				if (this.idMap.findBean(bean.getType().getName(), bean.getIdString()) != null) {
					deleteWithChildsFromIdMap(bean);
				}
			}
			if (excludesRule.getQueryExpressionTreeRoot() instanceof QueryExprConditionType) {
				final String typename = ((QueryExprConditionType) excludesRule.getQueryExpressionTreeRoot())
						.getTypename();
				if (this.typeMap.get(typename) != null && this.idMap.findAllIds(typename).size() == 0) {
					this.typeMap.remove(typename);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void deleteWithChildsFromIdMap(final RapidBean bean) {
		this.idMap.delete(bean);
		for (PropertyCollection colProp : bean.getColProperties()) {
			final Collection<RapidBean> col = (Collection<RapidBean>) colProp.getValue();
			if (col == null) {
				continue;
			}
			final TypePropertyCollection colPropType = (TypePropertyCollection) colProp.getType();
			if (colPropType.isComposition()) {
				for (RapidBean bean1 : col) {
					deleteWithChildsFromIdMap(bean1);
				}
			}
		}
	}
}
