/*
 * Rapid Beans Framework: QueryExprBrace.java
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
 * Brace expression for bean queries.
 * 
 * @author Martin Bluemel
 */
class QueryExprBrace extends QueryExpression {

	/**
	 * the child expression.
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

	/**
	 * the closed flag.
	 */
	private boolean closed = false;

	/**
	 * @return if the closing brace has been parsed or not
	 */
	public boolean isClosed() {
		return this.closed;
	}

	/**
	 * notify that the closing brace is parsed.
	 */
	public void close() {
		this.closed = true;
	}

	/**
	 * constructor.
	 * 
	 * @param argLastCreatedExpression the expression created last
	 */
	public QueryExprBrace(final QueryExpression argLastCreatedExpression) {
		this.becomeChildFrom(argLastCreatedExpression);
	}

	/**
	 * evaluates the query expession.
	 * 
	 * @param db          the DB to search beans for.
	 * @param resultSetIn the object found so far
	 * 
	 * @return the collection with beans
	 */
	public List<RapidBean> eval(final Container db, final List<RapidBean> resultSetIn) {
		List<RapidBean> resultSet = new ArrayList<RapidBean>();
		if (this.childExpression != null) {
			resultSet = this.childExpression.eval(db, resultSetIn);
		}
		return resultSet;
	}

	/**
	 * @return the string part to build a query dump
	 */
	protected String getDumpString() {
		return "BRACE";
	}
}
