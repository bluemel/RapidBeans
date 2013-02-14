/*
 * Rapid Beans Framework: QueryExprConditionLinkSet.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/09/2006
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

package org.rapidbeans.datasource.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.rapidbeans.core.basic.Container;
import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.type.TypePropertyCollection;

/**
 * The expression that allows to express a condition for a linked
 * object.
 * 
 * @author Martin Bluemel
 */
final class QueryExprConditionLinkSet extends QueryExpression {

	/**
	 * the name of the Collection Property.
	 */
	private String linkname = null;

	/**
	 * the child expression in [] parantheses.
	 */
	private QueryExpression childExpression = null;

	/**
	 * ???.
	 */
	private int multiplicity = -1;

	/**
	 * @return the child expressions.
	 */
	protected List<QueryExpression> getChildExpressions() {
		List<QueryExpression> childs = new ArrayList<QueryExpression>();
		if (this.childExpression != null) {
			childs.add(this.childExpression);
		}
		return childs;
	}

	/**
	 * @param expr
	 *            the child expression to add.
	 */
	protected void addChildExpression(final QueryExpression expr) {
		this.childExpression = expr;
	}

	/**
	 * @param expr
	 *            the child expession to remove.
	 */
	public void removeChildExpression(final QueryExpression expr) {
		if (expr != this.childExpression) {
			throw new QueryException(
					"tried to remove non existing child expression");
		}
		this.childExpression = null;
	}

	/**
	 * @return dump string.
	 */
	protected String getDumpString() {
		return this.linkname;
	}

	/**
	 * constructor.
	 * 
	 * @param colPropName
	 *            the Collection Property's name
	 * @param mult
	 *            theCollection Property's (min? max?) multiplicity
	 * @param parent
	 *            the parent expression
	 */
	protected QueryExprConditionLinkSet(final String colPropName,
			final int mult, final QueryExpression parent) {
		this.linkname = colPropName;
		this.multiplicity = mult;
		this.becomeChildFrom(parent);
	}

	/**
	 * the expression evaluation method.
	 * 
	 * @param db
	 *            the data container
	 * @param resultSetIn
	 *            the collection with found beans
	 * 
	 * @return the set with found objects
	 */
	@SuppressWarnings("unchecked")
	public List<RapidBean> eval(final Container db, final List<RapidBean> resultSetIn) {
		ArrayList<RapidBean> resultSet = new ArrayList<RapidBean>();
		List<RapidBean> resultSetLinkTargets = null;
		String linkTypename = null;
		PropertyCollection curColProp;
		Collection<RapidBean> curLinkSet;
		RapidBean curLinkTargetBean;
		List<RapidBean> parentBeanList;
		boolean match;

		// filter all objects that have linke beans with
		// the appropriate multiplicity
		for (RapidBean curBean : resultSetIn) {

			if (this.linkname.equals("parentBean")) {
				// get the target bean
				curLinkTargetBean = curBean.getParentBean();
				parentBeanList = new ArrayList<RapidBean>();
				parentBeanList.add(curLinkTargetBean);
				// check the target bean against the child expressions
				if (this.childExpression != null) {
					resultSetLinkTargets = this.childExpression.eval(db, parentBeanList);
					if (resultSetLinkTargets.contains(curLinkTargetBean)) {
						resultSet.add(curBean);
					}
				}
			} else {
				curColProp = (PropertyCollection) curBean.getProperty(this.linkname);
				if (curColProp == null) {
					throw new QueryException("No collection property \"" + this.linkname
							+ "\" found for type \"" + curBean.getType().getName() + "\"");
				}

				if (linkTypename == null) {
					linkTypename = ((TypePropertyCollection)
							curColProp.getType()).getTargetType().getName();
				}

				// check if there are linked beans for the specified
				// collection property with the correct multiplicity.
				curLinkSet = (Collection<RapidBean>) curColProp.getValue();

				if ((curLinkSet != null)
						&& (((this.multiplicity == -1) && (curLinkSet.size() > 0))
						|| ((this.multiplicity != -1) && (curLinkSet.size() > 0))))
				{
					match = false;

					for (Link curLink : curLinkSet) {
						// get the target bean
						if (curLink instanceof RapidBean) {
							curLinkTargetBean = (RapidBean) curLink;
						} else {
							curLinkTargetBean = db.findBean(linkTypename, curLink.getIdString());
						}
						// check the target bean against the child expressions
						if (this.childExpression != null) {
							resultSetLinkTargets = this.childExpression.eval(db, db.findBeansByType(linkTypename));
							if (resultSetLinkTargets.contains(curLinkTargetBean)) {
								match = true;
								break;
							}
						}
					}
					if (match) {
						resultSet.add(curBean);
					}
				}
			}
		}

		//
		//            for (RapidBean curBean : resultSet) {
		//                curColProp = (PropertyCollection) curBean.getProperty(this.linkname);
		//                curLinkSet = (Collection<RapidBean>) curColProp.getValue();
		//
		//                // remove all RapidBeans that don't have at least one
		//                // link target that matches the link condition
		//            }
		//        }

		return resultSet;
	}
}
