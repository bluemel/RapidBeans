/*
 * Rapid Beans Framework: ComponentDescr.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 01/01/2007
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

package org.rapidbeans.service;

/**
 * Helper Class
 *
 * @author Martin Bluemel
 */
public class ComponentDescr {

    /**
     * the component's name
     */
    private String name = null;

    /**
     * the component's root package
     */
    private String pkg = null;

    /**
     * the component's description or long name
     */
    private String description = null;

    /**
     * the component's version
     */
    private String version = null;

    /**
     * @return the pkg
     */
    public String getPkg() {
        return pkg;
    }

    /**
     * @param pkg the pkg to set
     */
    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
