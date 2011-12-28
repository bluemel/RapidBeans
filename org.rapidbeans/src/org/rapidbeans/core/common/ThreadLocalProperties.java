/*
 * Rapid Beans Framework: ThreadLocalProperties.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 02/25/2006
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

package org.rapidbeans.core.common;

import java.util.Hashtable;


/**
 * thread local hack to fine tune program behavior.
 *
 * @author Martin Bluemel
 */
public final class ThreadLocalProperties {

    /**
     * Thread hack.
     */
    private static ThreadLocal<Hashtable<String, Object>> props =
        new ThreadLocal<Hashtable<String, Object>>() {
        protected synchronized Hashtable<String, Object> initialValue() {
           return new Hashtable<String, Object>();
        }
    };

    /**
     * set the property.
     *
     * @param key the property's key
     * @param value the property's value
     *
     * @return if the property has been set successfully
     */
    public static boolean set(final String key,
            final Object value) {
        boolean accepted = false;
        // once a property is set it can not be set
        if (props.get().get(key) == null) {
            props.get().put(key, value);
            accepted = true;
        }
        return accepted;
    }

    /**
     * unset the thread local property.
     *
     * @param key the thread local property's key
     */
    public static void unset(final String key) {
        props.get().remove(key);
    }

    /**
     * get a thread local property value for a certain key.
     *
     * @param key the thread local property's key
     *
     * @return the property value
     */
    public static Object get(final String key) {
        return props.get().get(key);
    }

    /**
     * prevent from being constructed.
     */
    private ThreadLocalProperties() {
    }
}
