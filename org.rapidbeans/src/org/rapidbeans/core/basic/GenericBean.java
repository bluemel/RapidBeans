/*
 * Rapid Beans Framework: GenericBean.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 01/26/2006
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

package org.rapidbeans.core.basic;

import org.rapidbeans.core.type.TypeRapidBean;


/**
 * a Rapid Bean without class.
 *
 * @author Martin Bluemel
 */
public class GenericBean extends RapidBeanImplStrict {

    /**
     * The Rapid Bean type instance.
     * Since we have no class we can't make it a static
     * member of the class. So every instance has to have
     * a reference to it's type.
     */
    private TypeRapidBean type = null;

    /**
     * @return the Rapid Bean type
     */
    public TypeRapidBean getType() {
        return this.type;
    }

    /**
     * A generic Bean has nothing to initialize here since it
     * you can access properties only over the generic ways
     * not the concrete ones like in non generic Beans.
     */
    public void initProperties() {
        // do absolutely nothing
    }

    /**
     * constructor.
     *
     * @param argType the Rapid Bean type
     */
    public GenericBean(final TypeRapidBean argType) {
        super(argType);
        this.type = argType;
    }

    /**
     * constructor with initial values as one String and type.
     *
     * @param argType the Rapid Bean type
     * @param initvals String with initial values braced with quotes
     *                 separated by a blank;
     */
    public GenericBean(final TypeRapidBean argType, final String initvals) {
        super(initvals, argType);
        this.type = argType;
    }

    /**
     * constructor with initial values as String array.
     *
     * @param argType the Rapid Bean type
     * @param initvals String arry with initial values.
     */
    public GenericBean(final TypeRapidBean argType, final String[] initvals) {
        super(initvals, argType);
        this.type = argType;
    }        
}
