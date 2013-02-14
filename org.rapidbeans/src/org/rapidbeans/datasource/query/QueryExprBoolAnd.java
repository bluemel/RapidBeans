/*
 * Rapid Beans Framework: QueryExprBoolAnd.java
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
 * AND expression for bean queries.
 * 
 * @author Martin Bluemel
 */
class QueryExprBoolAnd extends QueryExpression {

	/**
	 * the child expessions.
	 */
	private List<QueryExpression> childExpressions = new ArrayList<QueryExpression>();

	/**
	 * adds a child expression to the query expression.
	 * 
	 * @param childExpression
	 *            the child expression to add.
	 */
	public void addChildExpression(final QueryExpression childExpression) {
		this.childExpressions.add(childExpression);
	}

	/**
	 * removes a child expression from the query expression.
	 * 
	 * @param childExpression
	 *            the child expression to add.
	 */
	public void removeChildExpression(final QueryExpression childExpression) {
		this.childExpressions.remove(childExpression);
	}

	/**
	 * constructor.
	 * 
	 * @param argLastCreatedExpression
	 *            the expression created last
	 */
	public QueryExprBoolAnd(final QueryExpression argLastCreatedExpression) {
		// get the parent of last created expression
		QueryExpression lastCreatedParent = argLastCreatedExpression.getParentExpression();

		// precedence OR before closed Braces
		QueryExpression lastCreatedExpression = argLastCreatedExpression;
		while (lastCreatedParent instanceof QueryExprBrace
				&& ((QueryExprBrace) lastCreatedParent).isClosed()) {
			lastCreatedParent = lastCreatedParent.getParentExpression();
			lastCreatedExpression = lastCreatedExpression.getParentExpression();
		}

		// hang in between parent and child
		lastCreatedParent.removeChildExpression(lastCreatedExpression);
		this.becomeChildFrom(lastCreatedParent);
		argLastCreatedExpression.becomeChildFrom(this);
	}

	/**
	 * evaluates the AND query expession.
	 * 
	 * @param db
	 *            the DB to search beans for.
	 * @param resultSetIn
	 *            the object found so far
	 * 
	 * @return the collection with beans
	 */
	public List<RapidBean> eval(final Container db, final List<RapidBean> resultSetIn) {
		List<RapidBean> resultSet = resultSetIn;
		for (QueryExpression childExpression : this.childExpressions) {
			resultSet = childExpression.eval(db, resultSet);
		}
		return resultSet;
	}

	/**
	 * @return the child expressions
	 */
	protected List<QueryExpression> getChildExpressions() {
		return this.childExpressions;
	}

	/**
	 * @return the string part to build a query dump
	 */
	protected String getDumpString() {
		return "AND";
	}
}
