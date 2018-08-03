/*
 * Rapid Beans Framework: QueryExprConditionType.java
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
import java.util.List;

import org.rapidbeans.core.basic.Container;
import org.rapidbeans.core.basic.RapidBean;

/**
 * Type expression for bean queries. The top level expression that mainly
 * focuses on bean types. &lt;RapidBean
 * Type&gt;<b>'['</b>&lt;Subexpression&gt;<b>']'</b><br/>
 * 
 * @author Martin Bluemel
 */
public class QueryExprConditionType extends QueryExpression {

	/**
	 * the name of the type of bean to search for.
	 */
	private String typename = null;

	/**
	 * the single child expression.
	 */
	private QueryExpression childExpression = null;

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
	 * @param expr the child expression to add.
	 */
	protected void addChildExpression(final QueryExpression expr) {
		this.childExpression = expr;
	}

	/**
	 * @param expr the child expession to remove.
	 */
	public void removeChildExpression(final QueryExpression expr) {
		if (expr != this.childExpression) {
			throw new QueryException("tried to remove non existing child expression");
		}
		this.childExpression = null;
	}

	// private boolean closed = false;
	//
	// public boolean getClosed() {
	// return this.closed;
	// }
	//
	// public void close() {
	// this.closed = true;
	// }

	/**
	 * constructor.
	 * 
	 * @param tname the name of the type of beans to look for.
	 */
	public QueryExprConditionType(final String tname) {
		this.typename = tname;
	}

	/**
	 * the query evaluation function.
	 * 
	 * @param db          the DB to look in
	 * @param resultSetIn should be null in the top level case
	 * 
	 * @return a Collection with all found beans (result set)
	 */
	public List<RapidBean> eval(final Container db, final List<RapidBean> resultSetIn) {
		List<RapidBean> bbAll = db.findBeansByType(this.typename);
		if (this.childExpression == null) {
			return bbAll;
		} else {
			return this.childExpression.eval(db, bbAll);
		}
	}

	/**
	 * @return a string to build a query dump.
	 */
	protected String getDumpString() {
		return this.typename;
	}

	/**
	 * @return the typename
	 */
	public String getTypename() {
		return typename;
	}
}
