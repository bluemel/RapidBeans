/*
 * Rapid Beans Framework: QueryExpression.java
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

import java.util.List;

import org.rapidbeans.core.basic.Container;
import org.rapidbeans.core.basic.RapidBean;

/**
 * the base class for every query expression.
 * 
 * @author Martin Bluemel
 */
abstract class QueryExpression {

	/**
	 * the parent expression.
	 */
	private QueryExpression parentExpression = null;

	/**
	 * @return the parent expression
	 */
	public QueryExpression getParentExpression() {
		return this.parentExpression;
	}

	/**
	 * break up the parent expsession link.
	 * Used to avoid cyclic dependencies after parsing.
	 */
	public void releaseParentExpression() {
		this.parentExpression = null;
		for (QueryExpression expr : this.getChildExpressions()) {
			expr.releaseParentExpression();
		}
	}

	/**
	 * used for reorganization of the expression tree during parsing.
	 * 
	 * @param argParentExpression
	 *            the new parent expression
	 */
	protected void becomeChildFrom(final QueryExpression argParentExpression) {
		this.parentExpression = argParentExpression;
		if (argParentExpression != null) {
			argParentExpression.addChildExpression(this);
		}
	}

	/**
	 * evaluates the query expression.
	 * 
	 * @param db
	 *            the DB to search beans for.
	 * @param selectionIn
	 *            the object found so far
	 * 
	 * @return the collection with beans
	 */
	public abstract List<RapidBean> eval(Container db, List<RapidBean> selectionIn);

	/**
	 * adds a child expression to the query expression.
	 * 
	 * @param childExpression
	 *            the child expression to add.
	 */
	protected abstract void addChildExpression(final QueryExpression childExpression);

	/**
	 * remove a child expression to the query expression.
	 * 
	 * @param childExpression
	 *            the child expression to remove.
	 */
	protected abstract void removeChildExpression(QueryExpression childExpression);

	/**
	 * @return the child expressions
	 */
	protected abstract List<QueryExpression> getChildExpressions();

	/**
	 * @return the string part to build a query dump
	 */
	protected abstract String getDumpString();

	/**
	 * prints a query dump for testing reasons.
	 * 
	 * @param sIn
	 *            the string dumped so far
	 * @param ipDepth
	 *            the depth within the expression tree
	 * @param bpParentIsLast
	 *            the flag if the parent is last
	 * @param bpIsLast
	 *            the flag if the expression itself is the last
	 * @param spIndentIn
	 *            the indentation string
	 * 
	 * @return the dump string
	 */
	protected String dump(final String sIn, final int ipDepth,
			final boolean bpParentIsLast, final boolean bpIsLast,
			final String spIndentIn) {

		final StringBuffer s = new StringBuffer(sIn);
		final String sAdd = this.getClass().getName() + ": " + this.getDumpString();
		String spIndent = spIndentIn;
		switch (ipDepth) {
		case 0:
			s.append(sAdd);
			s.append("\n");
			break;
		case 1:
			s.append("|__");
			s.append(sAdd);
			s.append("\n");
			break;
		default:
			if (bpParentIsLast) {
				spIndent += "   ";
			} else {
				spIndent += "|  ";
			}
			s.append(spIndent);
			s.append("|__");
			s.append(sAdd);
			s.append("\n");
			break;
		}

		String sRet = s.toString();

		try {
			List<QueryExpression> childs = this.getChildExpressions();
			int size = childs.size();
			QueryExpression child;

			for (int i = 0; i < size; i++) {
				child = (QueryExpression) childs.get(i);
				if (i < size - 1) {
					sRet = child.dump(sRet, ipDepth + 1, bpIsLast, false, spIndent);
				} else {
					sRet = child.dump(sRet, ipDepth + 1, bpIsLast, true, spIndent);
				}
			}
		} catch (QueryException e) {
			if (!e.getMessage().startsWith("attribute value condition"
					+ " expressions do not have a child expressions")) {
				throw e;
			}
		}

		return sRet;
	}
}
