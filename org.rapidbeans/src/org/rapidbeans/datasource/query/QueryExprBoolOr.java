/*
 * Rapid Beans Framework: QueryExprBoolOr.java
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
 * OR expression for bean queries.
 *
 * @author Martin Bluemel
 */
class QueryExprBoolOr extends QueryExpression {

    /**
     * the child expressions.
     */
    private ArrayList<QueryExpression> childExpressions = new ArrayList<QueryExpression>();

    /**
     * @return the child expressions.
     */
    protected List<QueryExpression> getChildExpressions() {
        return this.childExpressions;
    }

    /**
     * @param expr the child expression to add.
     */
    protected void addChildExpression(final QueryExpression expr) {
        this.childExpressions.add(expr);
    }

    /**
     * @param expr the child expession to remove.
     */
    public void removeChildExpression(final QueryExpression expr) {
        this.childExpressions.remove(expr);
    }

    /**
     * constructor.
     *
     * @param argLastCreatedExpression the expression created last
     */
    public QueryExprBoolOr(final QueryExpression argLastCreatedExpression) {
        // get the parent of last created expression
        QueryExpression lastCreatedExpression = argLastCreatedExpression;
        QueryExpression lastCreatedParent = argLastCreatedExpression.getParentExpression();
        // precedence AND before OR and before closed Braces
        while (lastCreatedParent instanceof QueryExprBoolAnd
                || (lastCreatedParent instanceof QueryExprBrace
                && ((QueryExprBrace) lastCreatedParent).isClosed())) {
            lastCreatedParent = lastCreatedParent.getParentExpression();
            lastCreatedExpression = lastCreatedExpression.getParentExpression();
        }
        // hang in between parent and child
        lastCreatedParent.removeChildExpression(lastCreatedExpression);
        this.becomeChildFrom(lastCreatedParent);
        lastCreatedExpression.becomeChildFrom(this);
    }

    /**
     * evaluates the query expession.
     *
     * @param db the DB to search beans for.
     * @param resultSetIn the object found so far
     *
     * @return the collection with beans
     */
    public List<RapidBean> eval(final Container db, final List<RapidBean> resultSetIn) {
        ArrayList<RapidBean> resultSet = new ArrayList<RapidBean>();
        List<RapidBean> resultSetChild;
        List<RapidBean> resultSetInNotMatched = new ArrayList<RapidBean>();
        for (RapidBean bean : resultSetIn) {
            resultSetInNotMatched.add(bean);
        }
        for (QueryExpression childExpression : this.childExpressions) {
            resultSetChild = childExpression.eval(db, resultSetInNotMatched);
            for (RapidBean bean : resultSetChild) {
                if (resultSet.indexOf(bean) == -1) {
                    resultSet.add(bean);
                    resultSetInNotMatched.remove(bean);
                }
            }
        }
        return resultSet;
    }

    /**
     * @return the string part to build a query dump
     */
    protected String getDumpString() {
        return "OR";
    }
}
