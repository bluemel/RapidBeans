/*
 * Rapid Beans Framework: QueryExprConditionAttrval.java
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
import java.util.regex.Pattern;

import org.rapidbeans.core.basic.Container;
import org.rapidbeans.core.basic.Id;
import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyAssociationend;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.basic.ThreadLocalValidationSettings;

/**
 * Attribute value expression for bean queries.
 * 
 * @author Martin Bluemel
 */
class QueryExprConditionAttrval extends QueryExpression {

	/**
	 * undefined.
	 */
	public static final int OPERATOR_COMP_UNDEF = 0;

	/**
	 * '=', equals.
	 */
	public static final int OPERATOR_COMP_EQ = 1;

	/**
	 * '>' greater than.
	 */
	public static final int OPERATOR_COMP_GT = 2;

	/**
	 * '>=' greater than or equals.
	 */
	public static final int OPERATOR_COMP_GE = 3;

/**
     * '<', lower than.
     */
	public static final int OPERATOR_COMP_LT = 4;

	/**
	 * '<=', lower than or equals.
	 */
	public static final int OPERATOR_COMP_LE = 5;

	/**
	 * '!=', not equals.
	 */
	public static final int OPERATOR_COMP_NE = 6;

	/**
	 * '$', match.
	 */
	public static final int OPERATOR_COMP_MATCH = 7;

	/**
	 * the bean property name.
	 */
	private String attrName = null;

	/**
	 * the comparison operator.
	 */
	private int compOperator = OPERATOR_COMP_UNDEF;

	/**
	 * getter for the comparison operator. Needed to differ between a match and
	 * a normal condition during construction.
	 * 
	 * @return the comparison operator
	 */
	protected int getCompOperator() {
		return this.compOperator;
	}

	/**
	 * the value to compare against in string form.
	 */
	private String attrValue = null;

	/**
	 * the reqular expression to compare against.
	 */
	private Pattern attrRegExpPattern = null;

	/**
	 * constructor.
	 * 
	 * @param argAttrName
	 *            the property name
	 * @param argCompOperator
	 *            the comparison operator
	 * @param argParent
	 *            the parent regular expression
	 */
	public QueryExprConditionAttrval(final String argAttrName,
			final int argCompOperator, final QueryExpression argParent) {
		this.attrName = argAttrName;
		if (this.attrName.equals("id")) {
			this.isId = true;
		}
		this.compOperator = argCompOperator;
		this.becomeChildFrom(argParent);
	}

	/**
	 * @param expr
	 *            the child expression to add.
	 */
	protected void addChildExpression(final QueryExpression expr) {
		throw new QueryException(
				"tried to add child expression to attribute value condition expression");
	}

	/**
	 * @param expr
	 *            the child expession to remove.
	 */
	public void removeChildExpression(final QueryExpression expr) {
		throw new QueryException(
				"tried to remove child expression from attribute value condition expression");
	}

	/**
	 * flag for Id queries.
	 */
	private boolean isId = false;

	/**
	 * set the attribute's value as normal property value.
	 * 
	 * @param val
	 *            the attribute value to set.
	 */
	public void setAttrValue(final String val) {
		this.attrValue = val;
	}

	/**
	 * set the attribute's value as Java regular expression.
	 * 
	 * @param pattern
	 *            the Java regular expression
	 */
	public void setAttrRegExpPattern(final String pattern) {
		this.attrRegExpPattern = Pattern.compile(pattern);
	}

	/**
	 * evaluates the attribute value query expession.
	 * 
	 * @param db
	 *            the DB to search beans for.
	 * @param resultSetIn
	 *            the object found so far
	 * 
	 * @return the collection with beans
	 */
	public List<RapidBean> eval(final Container db,
			final List<RapidBean> resultSetIn) {
		ArrayList<RapidBean> resultSet = new ArrayList<RapidBean>();
		RapidBean bo;
		Property bop = null;
		Id id = null;
		Property curBop = null;
		Id curId = null;
		int curComp = 0;
		boolean curMatch;
		boolean isAssocEnd = false;
		for (RapidBean curBo : resultSetIn) {
			if (this.isId) {
				curId = curBo.getId();
				if (id == null) {
					id = Id.createInstance(curBo, this.attrValue);
				}
			} else {
				curBop = curBo.getProperty(this.attrName);
				if (bop == null) {
					bo = RapidBeanImplStrict.createInstance(curBo.getType()
							.getName());
					bop = bo.getProperty(this.attrName);
					if (bop == null) {
						throw new QueryException("Property \"" + this.attrName
								+ "\" not found for type \""
								+ bo.getType().getName() + "\"");
					}
					if (bop instanceof PropertyAssociationend) {
						isAssocEnd = true;
					}
					try {
						ThreadLocalValidationSettings.validationOff();
						bop.setValue(this.attrValue);
					} finally {
						ThreadLocalValidationSettings.remove();
					}
				}
			}

			curMatch = false;

			if (this.compOperator == OPERATOR_COMP_MATCH) {
				if (this.isId) {
					curMatch = this.attrRegExpPattern.matcher(curId.toString())
							.matches();
				} else {
					curMatch = this.attrRegExpPattern
							.matcher(curBop.toString()).matches();
				}
			} else {
				if (this.isId) {
					curComp = curId.toString().compareTo(id.toString());
				} else if (isAssocEnd) {
					curComp = curBop.toString().compareTo(bop.toString());
				} else {
					curComp = curBop.compareTo(bop);
				}

				switch (this.compOperator) {
				case OPERATOR_COMP_EQ:
					if (curComp == 0) {
						curMatch = true;
					}
					break;
				case OPERATOR_COMP_GT:
					if (curComp > 0) {
						curMatch = true;
					}
					break;
				case OPERATOR_COMP_GE:
					if (curComp >= 0) {
						curMatch = true;
					}
					break;
				case OPERATOR_COMP_LT:
					if (curComp < 0) {
						curMatch = true;
					}
					break;
				case OPERATOR_COMP_LE:
					if (curComp <= 0) {
						curMatch = true;
					}
					break;
				case OPERATOR_COMP_NE:
					if (curComp != 0) {
						curMatch = true;
					}
					break;
				default:
					throw new QueryException("undefined operator");
				}
			}
			if (curMatch) {
				resultSet.add(curBo);
			}
		}
		return resultSet;
	}

	/**
	 * @return the child expressions.
	 */
	protected List<QueryExpression> getChildExpressions() {
		throw new QueryException("attribute value condition expressions"
				+ " do not have a child expressions");
	}

	/**
	 * @return the string part to build a query dump
	 */
	protected String getDumpString() {
		String op = "??";
		switch (this.compOperator) {
		case OPERATOR_COMP_EQ:
			op = "==";
			break;
		case OPERATOR_COMP_GT:
			op = ">";
			break;
		case OPERATOR_COMP_GE:
			op = ">=";
			break;
		case OPERATOR_COMP_LT:
			op = "<";
			break;
		case OPERATOR_COMP_LE:
			op = "<=";
			break;
		case OPERATOR_COMP_NE:
			op = "!=";
			break;
		case OPERATOR_COMP_MATCH:
			op = "$";
			break;
		default:
			throw new QueryException("undefined operator " + this.compOperator);
		}
		return this.attrName + " " + op + " " + this.attrValue;
	}
}
