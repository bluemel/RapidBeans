/*
 * Rapid Beans Framework: PropertyFile.java
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

package org.rapidbeans.core.basic;

import java.io.File;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyFile;

/**
 * A <b>File</b> bean property stores handles to files.<br/>
 * In addition it optionally enforces validation of:<br/>
 * - fileType Attributes<br/>
 * <b>fileType: { file, directory }<br/>
 * </b> specifies the maximal length of the String<br/>
 * <b>default: default = null</b> specifies the default value<br/>
 * 
 * @author Martin Bluemel
 */
public class PropertyFile extends Property {

	/**
	 * the property's File value. !!! do not initialize here because the
	 * superclass does it with the property type's default value
	 */
	private File value;

	/**
	 * constructor for a new String Property.
	 * 
	 * @param type
	 *            the Property's type
	 * @param parentBean
	 *            the parent bean
	 */
	public PropertyFile(final TypeProperty type, final RapidBean parentBean) {
		super(type, parentBean);
	}

	/**
	 * generic value getter.
	 * 
	 * @return the String value of this Property
	 */
	public File getValue() {
		return this.value;
	}

	/**
	 * String value getter.
	 * 
	 * @return the String representation of the Property's value.<br/>
	 *         For a File this is the absolute path.
	 */
	public String toString() {
		if (this.value == null) {
			return null;
		} else {
			return this.value.getAbsolutePath();
		}
	}

	/**
	 * generic value setter.
	 * 
	 * @param fileValue
	 *            the new value for this property.<br/>
	 *            Must be an instance of the following class:<br/>
	 *            <b>File:</b> a File<br/>
	 *            <b>String:</b> a String describing a absolute or relative path
	 *            to the file<br/>
	 */
	public void setValue(final Object newValue) {
		super.setValueWithEvents(this.value, newValue,
				new PropertyValueSetter() {
					public void setValue(final Object newValue) {
						value = (File) newValue;
					}
				});
	}

	/**
	 * converts different classes to the Property's internal value class.<br/>
	 * For a File property String or File are accepted.
	 * 
	 * @param argValue
	 *            the value to convert<br/>
	 *            Must be an instance of the following classes:<br/>
	 *            <b>String:</b> the absolute or relative path of the file<br/>
	 *            <b>File:</b> the File value<br/>
	 * 
	 * @return a String
	 */
	public File convertValue(final Object argValue) {
		if (argValue == null) {
			return null;
		}
		File f = null;
		if (argValue instanceof File) {
			f = (File) argValue;
		} else if (argValue instanceof String) {
			f = new File((String) argValue);
		} else {
			throw new ValidationException("invalid.prop.string.type", this,
					"Tried to convert value from a data type \""
							+ argValue.getClass().getName()
							+ "\" different to String.");
		}
		return f;
	}

	/**
	 * generic validation for the Property's value.
	 * 
	 * @param newValue
	 *            the value to validate<br/>
	 *            Must be an instance of the following classes:<br/>
	 *            <b>String:</b> the String to validate<br/>
	 * 
	 * @return the converted value which is the internal representation or if a
	 *         primitive type the corresponding value object
	 */
	public File validate(final Object newValue) {

		final File file = (File) super.validate(newValue);

		if (!ThreadLocalValidationSettings.getValidation()) {
			return file;
		}

		if (newValue == null) {
			return null;
		}

		final TypePropertyFile type = (TypePropertyFile) this.getType();

		// check existence
		if (type.getMustExist() && !file.exists()) {
			final Object[] messageArgs = { file.getAbsolutePath() };
			switch (type.getFiletype()) {
			case file:
				throw new ValidationException("invalid.prop.dir.notexists",
						this, "File \"" + file.getAbsolutePath()
								+ "\" does not exist.", messageArgs);
			case link:
				throw new ValidationException("invalid.prop.link.notexists",
						this, "File \"" + file.getAbsolutePath()
								+ "\" does not exist.", messageArgs);
			default:
				throw new ValidationException("invalid.prop.file.notexists",
						this, "File \"" + file.getAbsolutePath()
								+ "\" does not exist.", messageArgs);
			}
		}

		// check file type
		if (type.getFiletype() != null && file.exists()) {
			switch (type.getFiletype()) {
			case file:
				if (!file.isFile()) {
					throw new ValidationException("invalid.prop.file.nofile",
							this, "File \"" + file.getAbsolutePath()
									+ "\" is not a plain file.");
				}
				break;
			case directory:
				if (!file.isDirectory()) {
					throw new ValidationException("invalid.prop.file.nodir",
							this, "File \"" + file.getAbsolutePath()
									+ "\" is not a directory.");
				}
				break;
			case fileordir:
				if ((!file.isDirectory()) && (!file.isFile())) {
					throw new ValidationException(
							"invalid.prop.file.nofileordir", this, "File \""
									+ file.getAbsolutePath()
									+ "\" is neither a file nor a directory.");
				}
				break;
			default:
				throw new RapidBeansRuntimeException("usupported file type \""
						+ type.getFiletype().name() + "\"");
			}
		}

		return file;
	}
}
