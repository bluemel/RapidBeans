/*
 * Rapid Beans Framework: QueryExprConditionPath.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 10/25/2007
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.rapidbeans.core.basic.Container;
import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.datasource.DatasourceException;
import org.rapidbeans.datasource.Document;


/**
 * Type expression for bean queries.
 * The top level expression that selects a set of beans specified by
 * a path.<br/>
 * "/" specifies the top level or root entity<br/>
 * "/&lt;prop 1>/&lt;prop 2>/..." specifies a set of entities while
 * prop n is the name of a collection property<br/>
 *
 * @author Martin Bluemel
 */
class QueryExprConditionPath extends QueryExpression {

    /**
     * the search path divided into components.
     */
    private List<String> pathComponents = null;

    /**
     * Convert the path to a string.
     *
     * @return the path as string
     */
    private String getPathQueryString() {
        final StringBuffer sb = new StringBuffer();
        if (this.absolutePath) {
            sb.append('/');
        }
        boolean first = true;
        for (String pathComponent : this.pathComponents) {
            if (!first) {
                sb.append('/');
            }
            sb.append(pathComponent);
            first = false;
        }
        return sb.toString();
    }

    /**
     * specifies if we have an absolute path.
     */
    private boolean absolutePath = false;

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
            throw new QueryException(
                    "tried to remove non existing child expression");
        }
        this.childExpression = null;
    }

    /**
     * constructor.
     *
     * @param pname describes the path.
     */
    public QueryExprConditionPath(final String pname) {
        this.absolutePath = pname.startsWith("/");
        this.pathComponents = StringHelper.split(pname, "/");
    }

    /**
     * the query evaluation function.
     *
     * @param db the DB to look in
     * @param resultSetIn should be null in the top level case
     *
     * @return a Collection with all found beans (result set)
     */
    @SuppressWarnings("unchecked")
    public List<RapidBean> eval(final Container db,
            final List<RapidBean> resultSetIn) {
        if (!this.absolutePath) {
            throw new DatasourceException("Path queries currently have to be absolute"
                    + " (to begin with '/')");
        }
        if (!(db instanceof Document)) {
            throw new DatasourceException(
                    "Path queries can only run against documents");
        }
        Property prop;
        ArrayList<RapidBean> curBeansParents = new ArrayList<RapidBean>();
        curBeansParents.add(((Document) db).getRoot());
        ArrayList<RapidBean> curBeans = new ArrayList<RapidBean>();
        Collection<RapidBean> col;
        boolean propFound;
        Set<TypeRapidBean> beanTypesSearched;
        for (final String pathComponent : this.pathComponents) {
            curBeans.clear();
            propFound = false;
            beanTypesSearched = new LinkedHashSet<TypeRapidBean>();
            for (final RapidBean bean : curBeansParents) {
                prop = bean.getProperty(pathComponent);
                beanTypesSearched.add(bean.getType());
                if (prop != null) {
                    propFound = true;
                    if (!(prop instanceof PropertyCollection)) {
                        throw new DatasourceException(
                                "Error in path query \"" + this.getPathQueryString() + "\":\n"
                                + "Property \"" + pathComponent + "\" of type \""
                                + bean.getType().getName() + "\" is no collection property");
                    }
                    col = (Collection<RapidBean>) bean.getProperty(pathComponent).getValue();
                    for (RapidBean nextBean : col) {
                        curBeans.add(nextBean);
                    }
                }
            }
            if (propFound == false) {
                int i = 0;
                StringBuffer sb = new StringBuffer();
                for (TypeRapidBean beanType : beanTypesSearched) {
                    if (i > 0) {
                        sb.append(',');
                    }
                    sb.append(beanType.getName());
                    i++;
                }
                switch (beanTypesSearched.size()) {
                case 0:
                    throw new DatasourceException(
                            "Unexpected error in path query \"" + this.getPathQueryString() + "\":\n"
                            + "No bean has beans searched at all.");
                case 1:
                    throw new DatasourceException(
                            "Error in path query \"" + this.getPathQueryString() + "\":\n"
                            + "Property \"" + pathComponent + "\" not found in beans of type: "
                            + sb.toString());
                default:
                    throw new DatasourceException(
                            "Error in path query \"" + this.getPathQueryString() + "\":\n"
                            + "Property \"" + pathComponent + "\" not found in beans of types: "
                            + sb.toString());
                }
            }
            curBeansParents.clear();
            for (RapidBean bean : curBeans) {
                curBeansParents.add(bean);
            }
        }
        if (this.childExpression == null) {
            return curBeans;
        } else {
            return this.childExpression.eval(db, curBeans);
        }
    }

    /**
     * @return a string to build a query dump.
     */
    protected String getDumpString() {
        return this.getPathQueryString();
    }
}
