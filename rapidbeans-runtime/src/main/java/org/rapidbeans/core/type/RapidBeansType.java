/*
 * Rapid Beans Framework: RapidBeansType.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/04/2005
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

package org.rapidbeans.core.type;

import java.io.InputStream;
import java.util.List;

import org.rapidbeans.core.exception.TypeNotFoundException;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.core.util.XmlNode;

/**
 * The base class for all RapidBeans type (meta) information.
 * 
 * @author Martin Bluemel
 */
public abstract class RapidBeansType {

	/**
	 * the RapidBeans type's name.
	 */
	private String name = null;

	/**
	 * @return the RapidBeans type's name.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * the shortname.
	 */
	private String nameShort = null;

	/**
	 * @return the short name (class or type name without package)
	 */
	public String getNameShort() {
		if (this.nameShort == null && this.name != null) {
			this.nameShort = StringHelper.splitLast(this.name, ".");
		}
		return this.nameShort;
	}

	/**
	 * the package name.
	 */
	private String packageName = null;

	/**
	 * @return the package name
	 */
	public String getPackageName() {
		if (this.packageName == null && this.name != null && this.name.contains(".")) {
			this.packageName = StringHelper.splitBeforeLast(this.name, ".");
		}
		return this.packageName;
	}

	/**
	 * sets the rapid bean type's name.
	 * 
	 * @param argName the rapid bean type's name
	 */
	protected final void setName(final String argName) {
		this.name = argName;
		this.nameShort = null;
		this.packageName = null;
	}

	private String description = null;

	/**
	 * @param description the description to set
	 */
	protected void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the type description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * the equals implementation
	 * 
	 * @param other the other Object
	 * 
	 * @return if the type names are equal
	 */
	public boolean equals(final Object other) {
		return this.name.equals(((RapidBeansType) other).name);
	}

	/**
	 * the (generated) class implementing the type. Is null in case of generic
	 * types.
	 */
	private Class<?> implementingClass = null;

	/**
	 * @return the (generated) class implementing the type.
	 */
	public final Class<?> getImplementingClass() {
		return this.implementingClass;
	}

	/**
	 * sets the (generated) class implementing this type.
	 * 
	 * @param argClass the implementing class
	 */
	protected final void setImplementingClass(final Class<?> argClass) {
		this.implementingClass = argClass;
	}

	/**
	 * read the XML declaration.
	 * 
	 * @param typename the type's name
	 * 
	 * @return the description
	 */
	protected static XmlNode loadDescription(final String typename) {
		final InputStream typeDescrStream = RapidBeansTypeLoader.findDeclaration(typename, null);
		if (typeDescrStream == null) {
			throw new TypeNotFoundException("description for type \"" + typename + "\"");
		}
		final XmlNode typeDescrBaseNode = XmlNode.getDocumentTopLevel(typeDescrStream);
		return typeDescrBaseNode;
	}

	/**
	 * read the XML declaration.
	 * 
	 * @param typename the type's name
	 * 
	 * @return the description
	 */
	protected static XmlNode loadDescriptionXmlBinding(final String typename) {
		XmlNode typeDescrBaseNode = null;
		final InputStream typeDescrStream = RapidBeansTypeLoader.findDeclaration(typename, "_xmlbind");
		if (typeDescrStream != null) {
			typeDescrBaseNode = XmlNode.getDocumentTopLevel(typeDescrStream);
		}
		return typeDescrBaseNode;
	}

	/**
	 * Read an XML nodes "description" subnode.
	 * 
	 * @param xmlNode the XML node to analyze
	 * 
	 * @return the description as string or null if nothing found
	 */
	public static String readDescription(final XmlNode xmlNode) {
		String descr = null;
		final List<XmlNode> descrSubnodes = xmlNode.getSubnodes("description");
		if (descrSubnodes != null && descrSubnodes.size() > 0) {
			descr = descrSubnodes.get(0).getValue();
		}
		return descr;
	}
}
