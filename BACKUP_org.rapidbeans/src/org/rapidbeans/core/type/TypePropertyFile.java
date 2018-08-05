/*
 * Rapid Beans Framework: TypePropertyFile.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 12/14/2006
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

import java.io.File;

import org.rapidbeans.core.util.XmlNode;

/**
 * the type class for File properties.
 * 
 * @author Martin Bluemel
 */
public class TypePropertyFile extends TypeProperty {

	@Override
	public Class<?> getValuetype() {
		return File.class;
	}

	/**
	 * marks if the existence of the file is required.
	 */
	private boolean mustExist = true;

	/**
	 * @return if if the existence of the file is required
	 */
	public boolean getMustExist() {
		return this.mustExist;
	}

	/**
	 * the file type.
	 */
	private FileType filetype = null;

	/**
	 * @return the file type
	 */
	public FileType getFiletype() {
		return this.filetype;
	}

	/**
	 * the file's suffix.
	 */
	private String suffix = null;

	/**
	 * @return the file's suffix
	 */
	public String getSuffix() {
		return this.suffix;
	}

	/**
	 * Constructor.
	 * 
	 * @param propertyNode
	 *            XML DOM node with the property type description
	 * @param parentBeanType
	 *            the parent bean type
	 */
	public TypePropertyFile(final XmlNode[] propertyNodes, final TypeRapidBean parentBeanType) {
		super("File", propertyNodes, parentBeanType);

		String s = propertyNodes[0].getAttributeValue("@default");
		if (s != null) {
			setDefaultValue(new File(s));
		}

		s = propertyNodes[0].getAttributeValue("@mustExist");
		if (s != null) {
			this.mustExist = Boolean.parseBoolean(s);
		}

		s = propertyNodes[0].getAttributeValue("@filetype");
		if (s == null) {
			this.filetype = FileType.file;
		} else {
			this.filetype = FileType.valueOf(s);
		}

		s = propertyNodes[0].getAttributeValue("@suffix");
		if (s != null) {
			this.suffix = s;
		}

	}

	/**
	 * @return the property type enumeration
	 */
	public PropertyType getProptype() {
		return PropertyType.file;
	}
}
