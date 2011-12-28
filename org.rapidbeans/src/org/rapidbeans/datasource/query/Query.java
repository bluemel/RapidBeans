/*
 * Rapid Beans Framework: Query.java
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
import java.util.logging.Logger;

import org.rapidbeans.core.basic.BeanSorter;
import org.rapidbeans.core.basic.Container;
import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.datasource.DatasourceException;
import org.rapidbeans.presentation.ApplicationManager;


/**
 * The base class for every bean query expression.
 *
 * @author Martin Bluemel
 */
public final class Query {

    private static final Logger log = Logger.getLogger(
            Query.class.getName()); 

    /**
     * constant for buffer length.
     */
    private static final int BUFFER_LENGTH = 1024;

    /**
     * the query expression tree root.
     */
    private QueryExpression queryExpressionTreeRoot = null;

    /**
     * the query expresion that was created before.
     */
    private QueryExpression lastCreatedExpression = null;

    /**
     * the query's sorter.
     */
    private BeanSorter sorter = null;

    /**
     * @param s the sorter
     */
    public void setSorter(final BeanSorter s) {
        this.sorter = s;
    }

    /**
     * the constructor for a bean query.
     * @param queryString the query string
     */
    public Query(final String queryString) {
        this.parse(queryString);
    }

    /**
     * the evaluation method.
     *
     * @param db the database to query beans from
     *
     * @return the collection of found beans.
     */
    public List<RapidBean> findBeans(final Container db) {
        if (this.queryExpressionTreeRoot == null) {
            throw new QueryException("QueryExpression.eval(): no query parsed.");
        }
        List<RapidBean> results = this.queryExpressionTreeRoot.eval(db, null);
        if (this.sorter != null) {
            results = this.sorter.sort(results);
        }
        return results;
    }

    /**
     * evaluates the query but anticipates that only one single object is found.
     *
     * @param db the database to query beans from
     *
     * @return a single bean found by that query
     *         or null if no bean has been found
     */
    public RapidBean findBean(final Container db) {
        List<RapidBean> resultSet = this.findBeans(db);
        switch (resultSet.size()) {
        case 0:
            return null;
        case 1:
            return resultSet.iterator().next();
        default:
            throw new DatasourceException(
                    "Unexpectedly found " + resultSet.size()
                    + " beans instead of one");
        }
    }

    private int depth1 = 0; // counts open '['

    private int depth2 = 0; // counts open ']'

    private StringBuffer buf = new StringBuffer(BUFFER_LENGTH);

    private StringBuffer buf1 = new StringBuffer(BUFFER_LENGTH);

    private int index;

    private int mult;

    private boolean sortAsc;

    /**
     * the bean query parser.
     *
     * @param queryString the query string
     */
    private void parse(final String queryString) {
        this.queryExpressionTreeRoot = null;
        this.lastCreatedExpression = null;

        int len = queryString.length();

        if (len == 0) {
            throw new QueryException("empty query!");
        }
        int state = 0;
        char c;
        this.buf = new StringBuffer(BUFFER_LENGTH);
        this.buf1 = new StringBuffer(BUFFER_LENGTH);
        this.depth1 = 0;
        this.depth2 = 0;

        for (this.index = 0; this.index < len; this.index++) {

            c = queryString.charAt(this.index);

            switch (state) {
            // parse a class name
            case 0:
                state = parseClassnameOrPath(state, c);
                break;

            // decide where to go after 0
            case 12:
                state = parseClassnameOrPathAfter(state, c);
                break;

            // parse an attribute or (link) set name
            case 1:
                state = parseAttributeOrLinkSetName(state, c, queryString);
                break;

            // parse an attribute value
            case 2:
                state = parseAttributeValue(state, c, queryString);
                break;

            // parse function parameters
            case 13:
                if (c != ')') {
                    throw new QueryException("Error in query \"" + queryString
                            + "\", unexpected character '" + c + "',"
                            + " expected ')' to close parameterless function.");
                }
                state = 2;
                break;

            // parse a quoted attribute value constant
            case 3:
                state = parseQuotedAttributeValue(state, c, queryString);
                break;

            // parse a quoted attribute type constant
            case 4:
                state = parseQuotedAttributeTypeConstant(state, c, queryString);
                break;

            // parse a sorting attribute path
            case 5:
                state = parseSortingAttributePath(state, c);
                break;

                // parse a sorting attribute path
            case 6:
                state = parseSortingAttributePathAfter(state, c);
                break;

            default:
                throw new QueryException("Error in query \"" + queryString
                        + "\", undefined state " + state);
            }
        }

        switch (state) {
        case 0:
            this.addNewBeanTypeOrPathCondition(buf.toString());
            break;
        case 5:
            this.addSortingCriteria(buf.toString(), this.sortAsc);
            break;
        default:
            break;
            //throw new QueryException("Error in query \"" + queryString
            //        + "\", undefined state " + state);
        }
    }

    /**
     * parse a class name or path query part.
     *
     * @param curState the current state
     * @param c the character to parse
     *
     * @return the parser's next state
     */
    private int parseClassnameOrPath(final int curState, final char c) {
        int state = curState;
        switch (c) {
        default:
            this.buf.append(c);
            break;
        case '[':
            this.addNewBeanTypeOrPathCondition(this.buf.toString());
            this.buf.delete(0, BUFFER_LENGTH);
            this.depth1++;
            state = 1;
            break;
        case '>':
            state = 5;
            break;
        case '<':
            state = 5;
            break;
        case ']':
            throw new QueryException("']' not expexted while parsing class name");
        case '=':
            throw new QueryException("'=' not expexted while parsing class name");
        case ' ':
        case '\n':
        case '\t':
            this.addNewBeanTypeOrPathCondition(buf.toString());
            this.buf.delete(0, BUFFER_LENGTH);
            state = 12;
            break;
        }
        return state;
    }

    /**
     * parse a class name or path query part.
     *
     * @param curState the current state
     * @param c the character to parse
     *
     * @return the parser's next state
     */
    private int parseClassnameOrPathAfter(final int curState, final char c) {
        int state = curState;
        switch (c) {
        default:
            throw new QueryException("'" + c + "' not expexted while"
                    + " parsing blanks after classname or path");
        case '[':
            this.depth1++;
            state = 1;
            break;
        case '>':
            this.sortAsc = true;
            state = 5;
            break;
        case '<':
            this.sortAsc = false;
            state = 5;
            break;
        case ' ':
        case '\n':
        case '\t':
            break;
        }
        return state;
    }

    /**
     * parse a class name or path query part.
     *
     * @param curState the current state
     * @param c the character to parse
     * @param queryString the query string to parse
     *
     * @return the parser's next state
     */
    private int parseAttributeOrLinkSetName(final int curState, final char c,
            final String queryString) {
        int state = curState;
        switch (c) {
        default:
            this.buf.append(c);
            break;
        case '\'':
            if (this.buf.length() > 0) {
                throw new QueryException("Buffer not empty before scanning quoted constant");
            }
            this.buf.append("'");
            state = 4;
            break;
        case '(':
            this.buf.delete(0, BUFFER_LENGTH);
            this.addNewOpenBrace();
            this.depth2++;
            break;
        case ')':
            if (this.buf.length() > 0) {
                if (this.buf.toString().startsWith("'")) {
                    this.addNewAttrValueCondition(this.buf.toString(), QueryExprConditionAttrval.OPERATOR_COMP_EQ);
                } else {
                    this.addNewLinkSetCondition(this.buf.toString(), -1);
                }
            }
            this.buf.delete(0, BUFFER_LENGTH);
            this.lastCreatedExpression = this.closeBrace2(this.depth2);
            this.depth2--;
            break;
        case '[':
            if (this.buf.length() > 0) {
                this.mult = -1;
                char c1 = queryString.charAt(this.index + 1);
                if (c1 > '0' && c1 < '9') {
                    while (c1 > '0' && c1 < '9') {
                        this.index++;
                        this.buf1.append(c1);
                        c1 = queryString.charAt(this.index + 1);
                    }
                    this.mult = Integer.parseInt(this.buf1.toString());
                    this.buf1.delete(0, BUFFER_LENGTH);
                    if (c1 == ',') {
                        this.index++;
                    } else {
                        if (c1 != ']') {
                            throw new QueryException("expected ',' or ']' after multiplicity " + mult
                                    + " for link set condition \"" + buf.toString() + "\"");
                        }
                    }
                }
                this.addNewLinkSetCondition(buf.toString(), mult);
                this.buf.delete(0, BUFFER_LENGTH);
            }
            this.depth1++;
            state = 1;
            break;
        case ']':
            if (this.buf.length() > 0) {
                this.addNewLinkSetCondition(this.buf.toString(), -1);
            }
            this.buf.delete(0, BUFFER_LENGTH);
            this.lastCreatedExpression = this.closeBrace1(this.depth1);
            this.depth1--;
            if (this.depth1 == 0) {
                if (this.depth2 > 0) {
                    throw new DatasourceException(
                            "Closing ')' missing before last closing ']'");
                }
                state = 6;
            }
            break;
        case '=':
            if (queryString.charAt(this.index + 1) == '=') {
                this.index++;
            }
            this.addNewAttrValueCondition(this.buf.toString(), QueryExprConditionAttrval.OPERATOR_COMP_EQ);
            this.buf.delete(0, BUFFER_LENGTH);
            state = 2;
            break;
        case '$':
            this.addNewAttrValueCondition(this.buf.toString(), QueryExprConditionAttrval.OPERATOR_COMP_MATCH);
            this.buf.delete(0, BUFFER_LENGTH);
            state = 2;
            break;
        case '!':
            if (queryString.charAt(this.index + 1) == '=') {
                this.index++;
            }
            this.addNewAttrValueCondition(this.buf.toString(), QueryExprConditionAttrval.OPERATOR_COMP_NE);
            this.buf.delete(0, BUFFER_LENGTH);
            state = 2;
            break;
        case '>':
            if (queryString.charAt(this.index + 1) == '=') {
                this.addNewAttrValueCondition(this.buf.toString(), QueryExprConditionAttrval.OPERATOR_COMP_GE);
                this.index++;
            } else {
                this.addNewAttrValueCondition(this.buf.toString(), QueryExprConditionAttrval.OPERATOR_COMP_GT);
            }
            this.buf.delete(0, BUFFER_LENGTH);
            state = 2;
            break;
        case '<':
            if (queryString.charAt(this.index + 1) == '=') {
                this.addNewAttrValueCondition(this.buf.toString(), QueryExprConditionAttrval.OPERATOR_COMP_LE);
                this.index++;
            } else {
                this.addNewAttrValueCondition(this.buf.toString(), QueryExprConditionAttrval.OPERATOR_COMP_LT);
            }
            this.buf.delete(0, BUFFER_LENGTH);
            state = 2;
            break;
        case ' ':
        case '\n':
        case '\t':
            break;
        }
        return state;
    }

    /**
     * parse attribute value.
     *
     * @param curState the current state
     * @param c the character to parse
     * @param queryString the query string to parse
     *
     * @return the parser's next state
     */
    private int parseAttributeValue(final int curState,
            final char c, final String queryString) {
        int state = curState;
        switch (c) {
        case '\'':
            if (this.buf.length() > 0) {
                throw new QueryException("Buffer not empty before scanning quoted constant");
            }
            state = 3;
            break;
        case ']':
        case ')':
            if (this.buf.length() > 0) {
                this.setValueOfAttrValueCondition(this.buf.toString());
                this.buf.delete(0, BUFFER_LENGTH);
            }

            switch (c) {
            case ']':
                this.lastCreatedExpression = this.closeBrace1(this.depth1);
                this.depth1--;
                break;
            case ')':
                this.lastCreatedExpression = this.closeBrace2(this.depth2);
                this.depth2--;
                break;
            default:
                throw new QueryException("Unexpected character '" + c
                        + "\'. Expected ']' or ')'.");
            }
            break;

        case '&':
            if (queryString.charAt(this.index + 1) == '&') {
                this.index++;
            }
            if (this.buf.length() > 0) {
                this.setValueOfAttrValueCondition(buf.toString());
                this.buf.delete(0, BUFFER_LENGTH);
            }
            this.addNewBoolAndExpression();
            state = 1;
            break;
        case '|':
            if (c == '|' && queryString.charAt(this.index + 1) == '|') {
                this.index++;
            }
            if (this.buf.length() > 0) {
                this.setValueOfAttrValueCondition(this.buf.toString());
                this.buf.delete(0, BUFFER_LENGTH);
            }
            this.addNewBoolOrExpression();
            state = 1;
            break;
        case '(':
            // quick hack for primitive functions
            if (this.buf.length() > 0
                    && this.buf.toString().equals("authenticatedUser")) {
                this.setValueOfAttrValueCondition(
                        ApplicationManager.getApplication().getAuthenticatedUser().getProperty("accountname").toString());
                this.buf.delete(0, BUFFER_LENGTH);
            } else {
                throw new DatasourceException(
                        "Unexpected fuction '" + this.buf.toString() + "' in query");
            }
            state = 13;
            break;
        case '[':
        case '=':
        case '>':
        case '<':
            throw new DatasourceException(
                    "Unexpected character '" + c + "' in query");
        case ' ':
        case '\n':
        case '\t':
            break;
        default:
            this.buf.append(c);
            break;
        }
        return state;
    }

    /**
     * parse a quoted attribute value.
     *
     * @param curState the current state
     * @param c the character to parse
     * @param queryString the query string to parse
     *
     * @return the parser's next state
     */
    private int parseQuotedAttributeValue(final int curState,
            final char c, final String queryString) {
        int state = curState;
        switch (c) {
        case '\'':
            state = 2;
            break;
        case '\\':
            this.index++;
            final char c1 = queryString.charAt(this.index);
            switch (c1) {
            case '\\':
            case '\'':
                this.buf.append(c1);
                break;
            case 'n':
                this.buf.append('\n');
                break;
            case 't':
                this.buf.append('\t');
                break;
            case '0':
                this.buf.append('\0');
                break;
            default:
                throw new QueryException("Error in query \"" + queryString
                        + "\", invalid escape sequence \\" + c + ".\n"
                        + "Valid escape sequences \\\\, \\n, \\t, \\0");
            }
            break;
        default:
            this.buf.append(c);
            break;
        }
        return state;
    }

    /**
     * parse a quoted attribute type constant.
     *
     * @param curState the current state
     * @param c the character to parse
     *
     * @return the parser's next state
     */
    private int parseQuotedAttributeTypeConstant(final int curState,
            final char c, final String queryString) {
        int state = curState;
        switch (c) {
        case '\'':
            state = 1;
            break;
        case '\\':
            this.index++;
            final char c1 = queryString.charAt(this.index);
            switch (c1) {
            case '\\':
            case '\'':
                this.buf.append(c1);
                break;
            case 'n':
                this.buf.append('\n');
                break;
            case 't':
                this.buf.append('\t');
                break;
            case '0':
                this.buf.append('\0');
                break;
            default:
                throw new QueryException("Error in query \"" + queryString
                        + "\", invalid escape sequence \\" + c1 + ".\n"
                        + "Valid escape sequences \\\\, \\n, \\t, \\0");
            }
            break;
        default:
            this.buf.append(c);
        break;
        }
        return state;
    }

    /**
     * parse a sorting criteria.
     *
     * @param curState the current state
     * @param c the character to parse
     *
     * @return the parser's next state
     */
    private int parseSortingAttributePath(final int curState, final char c) {
        int state = curState;
        switch (c) {
        default:
            this.buf.append(c);
            break;
        case ',':
        case ' ':
        case '\n':
        case '\t':
            this.addSortingCriteria(buf.toString(), this.sortAsc);
            this.buf.delete(0, BUFFER_LENGTH);
            state = 6;
            break;
        }
        return state;
    }

    /**
     * parse blanks until next sorting attribute path.
     *
     * @param curState the current state
     * @param c the character to parse
     *
     * @return the parser's next state
     */
    private int parseSortingAttributePathAfter(final int curState, final char c) {
        int state = curState;
        switch (c) {
        default:
            throw new QueryException("missing '>' or '<' for additional sorting criteria");
        case '>':
            this.sortAsc = true;
            state = 5;
            break;
        case '<':
            this.sortAsc = false;
            state = 5;
            break;
        case ' ':
        case '\n':
        case '\t':
            break;
        }
        return state;
    }

    /**
     * create a bean type condition expression.
     *
     * @param typenameOrPath the bean type name or a path
     */
    private void addNewBeanTypeOrPathCondition(final String typenameOrPath) {
        if (typenameOrPath.indexOf('/') != -1) {
            this.lastCreatedExpression = new QueryExprConditionPath(typenameOrPath);
        } else {
            this.lastCreatedExpression = new QueryExprConditionType(typenameOrPath);
        }
        this.queryExpressionTreeRoot = this.lastCreatedExpression;
    }

    /**
     * create an attribute value condition expression.
     *
     * @param attrName the bean type name
     * @param operator the operator
     */
    private void addNewAttrValueCondition(final String attrName, final int operator) {
        if (this.lastCreatedExpression instanceof QueryExprConditionAttrval) {
            throw new QueryException("can't create an AttrValueCondition directliy after another");
        }
        this.lastCreatedExpression = new QueryExprConditionAttrval(attrName, operator, this.lastCreatedExpression);
    }

    /**
     * set the value for an attribute value condition expression.
     *
     * @param value the value string
     */
    private void setValueOfAttrValueCondition(final String value) {
        final QueryExprConditionAttrval attrExpr = (QueryExprConditionAttrval) this.lastCreatedExpression;
        if (attrExpr.getCompOperator() == QueryExprConditionAttrval.OPERATOR_COMP_MATCH) {
            attrExpr.setAttrRegExpPattern(value);
        } else {
            attrExpr.setAttrValue(value);
        }
        log.fine(" ---------------------------------------");
        log.fine(this.queryExpressionTreeRoot.dump("", 0, false, false, ""));
        log.fine(" ---------------------------------------");
        log.fine(" set value \"" + value + "\" of attr value condition");
    }

    /**
     * create a new association condition expression.
     *
     * @param colPropName the name of the collection attribute
     * @param multiplicity ???
     */
    private void addNewLinkSetCondition(final String colPropName, final int multiplicity) {
        this.lastCreatedExpression = new QueryExprConditionLinkSet(colPropName, multiplicity,
                this.lastCreatedExpression);
        log.fine(" adding LinkSetExpression");
        log.fine(" ---------------------------------------");
        log.fine(this.queryExpressionTreeRoot.dump("", 0, false, false, ""));
        log.fine(" ---------------------------------------");
    }

    /**
     * create and AND expression.
     */
    private void addNewBoolAndExpression() {
//        if (!(this.lastCreatedExpression instanceof QueryExprConditionAttrval
//                || this.lastCreatedExpression instanceof QueryExprConditionLinkSet
//                || this.lastCreatedExpression instanceof QueryExprBrace)) {
//            throw new QueryException("can't create a BoolAndExpression after a \""
//                    + this.lastCreatedExpression.getClass().getName() + "\"\n"
//                    + "only directly after an "
//                    + "AttributeValueCondition\n" + "or LinkSetCondition or a BraceExpression");
//        }
        this.lastCreatedExpression = new QueryExprBoolAnd(this.lastCreatedExpression);
        log.fine(" adding new BoolAndExpression");
        log.fine(" ---------------------------------------");
        log.fine(this.queryExpressionTreeRoot.dump("", 0, false, false, ""));
        log.fine(" ---------------------------------------");
    }

    /**
     * create and OR expression.
     */
    private void addNewBoolOrExpression() {
        if (!(this.lastCreatedExpression instanceof QueryExprConditionAttrval)) {
            throw new QueryException("can create a BoolOrExpression only"
                    + " directly after an AttributeValueCondition");
        }
        this.lastCreatedExpression = new QueryExprBoolOr(this.lastCreatedExpression);
        log.fine(" adding new BoolOrExpression");
        log.fine(" ---------------------------------------");
        log.fine(this.queryExpressionTreeRoot.dump("", 0, false, false, ""));
        log.fine(" ---------------------------------------");
    }

    private void addSortingCriteria(final String propPath, final boolean asc) {
        if (this.sorter == null) {
            this.sorter = new BeanSorter();
        }
        if (this.queryExpressionTreeRoot instanceof QueryExprConditionType) {
            final QueryExprConditionType rootExpr = (QueryExprConditionType) this.queryExpressionTreeRoot;
            final TypeRapidBean type = TypeRapidBean.forName(rootExpr.getTypename());
            RapidBean bbExample;
            if (type.getAbstract()) {
                final List<TypeRapidBean> concreteSubtypes = type.getConcreteSubtypes();
                if (concreteSubtypes.size() == 0) {
                    throw new QueryException("no concrete subtype found for type \""
                            + type.getName() + "\"");
                }
                final TypeRapidBean firstConcreteSubtype = concreteSubtypes.get(0);
                bbExample = RapidBean.createInstance(firstConcreteSubtype);
            } else {
                bbExample = RapidBean.createInstance(rootExpr.getTypename());
            }
            final Property prop = bbExample.getProperty(propPath);
            if (prop == null) {
                throw new DatasourceException("invalid sort criteria \"" + propPath + "\""
                        + " for bean type \"" + bbExample.getType().getName() + "\"");
            }
            this.sorter.addSortCriteria(prop.getType());
        } else {
            throw new DatasourceException("queryExpressionTreeRoot has unexpected class");
        }
    }

    /**
     * opens a brace expression.
     */
    private void addNewOpenBrace() {
        log.fine(" adding new Brace Expression");
        if (this.lastCreatedExpression instanceof QueryExprConditionAttrval) {
            throw new QueryException("can't create a BraceExpression directly after an AttributeValue Condition");
        }
        this.lastCreatedExpression = new QueryExprBrace(this.lastCreatedExpression);
    }

    /**
     * finishes a [] brace expression.
     *
     * @param depth the depth
     *
     * @return the [] brace expression
     */
    private QueryExpression closeBrace1(final int depth) {
        QueryExpression lastCreatedBraceExpression = this.lastCreatedExpression;
        log.fine(" closing Brace 1a ']': lastCreatedExpression:"
                + lastCreatedBraceExpression.getClass().getName());

        while (!(lastCreatedBraceExpression instanceof QueryExprConditionLinkSet
                        || lastCreatedBraceExpression instanceof QueryExprConditionType
                        || (lastCreatedBraceExpression instanceof QueryExprConditionAttrval
                                && depth <= 1))) {
            lastCreatedBraceExpression = lastCreatedBraceExpression.getParentExpression();
            log.fine(" closing Brace 1b ']': lastCreatedExpression:"
                    + lastCreatedBraceExpression.getClass().getName());
        }

        return lastCreatedBraceExpression;
    }

    /**
     * finishes a () brace expression.
     *
     * @param depth the depth
     *
     * @return the () brace expression
     */
    private QueryExpression closeBrace2(final int depth) {
        QueryExpression lastCreatedBraceExpression = this.lastCreatedExpression;
        log.fine(" closing Brace 2a ')': lastCreatedExpression:"
                + lastCreatedBraceExpression.getClass().getName());
        while (!(lastCreatedBraceExpression instanceof QueryExprBrace)) {
            lastCreatedBraceExpression = lastCreatedBraceExpression.getParentExpression();
            log.fine(" closing Brace 2b ')': lastCreatedExpression:"
                    + lastCreatedBraceExpression.getClass().getName());
        }
        ((QueryExprBrace) lastCreatedBraceExpression).close();
        return lastCreatedBraceExpression;
    }

    /**
     * @return the queryExpressionTreeRoot
     */
    public QueryExpression getQueryExpressionTreeRoot() {
        return queryExpressionTreeRoot;
    }
}
