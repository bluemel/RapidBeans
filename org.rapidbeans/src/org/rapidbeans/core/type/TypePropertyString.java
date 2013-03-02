/*
 * Rapid Beans Framework: TypePropertyString.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/26/2005
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

import java.util.List;
import java.util.regex.Pattern;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.util.EscapeMap;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.core.util.XmlNode;

/**
 * the type class for String properties.
 * 
 * @author Martin Bluemel
 */
public class TypePropertyString extends TypeProperty {

	@Override
	public Class<?> getValuetype() {
		return String.class;
	}

	/**
	 * constant to code an unlimeted string length.
	 */
	public static final int LENGTH_UNLIMITED = -1;

	/**
	 * marks if an empty string is valid.
	 */
	private boolean emptyValid = false;

	/**
	 * @return if an empty string is valid.
	 */
	public boolean getEmptyValid() {
		return this.emptyValid;
	}

	/**
	 * maximal string length.
	 */
	private int maxLength = LENGTH_UNLIMITED;

	/**
	 * @return maximal string length
	 */
	public final int getMaxLength() {
		return this.maxLength;
	}

	/**
	 * minimal string length.
	 */
	private int minLength = 0;

	/**
	 * @return minimal string length
	 */
	public final int getMinLength() {
		return this.minLength;
	}

	/**
	 * Java regular expression that the string must match.
	 */
	private Pattern pattern = null;

	/**
	 * @return Java regular expression that the string must match.
	 */
	public final Pattern getPattern() {
		return this.pattern;
	}

	private boolean multiline = false;

	/**
	 * @return if the string might have more than one line or not
	 */
	public boolean getMultiline() {
		return multiline;
	}

	/**
	 * Constructor.
	 * 
	 * @param propertyNodes
	 *            XML DOM nodes with the property type description
	 * @param parentBeanType
	 *            the parent bean type
	 */
	public TypePropertyString(final XmlNode[] propertyNodes,
			final TypeRapidBean parentBeanType) {
		super("String", propertyNodes, parentBeanType);

		String s = propertyNodes[0].getAttributeValue("@default");
		if (s != null) {
			setDefaultValue(s);
		}

		s = propertyNodes[0].getAttributeValue("@emptyvalid");
		if (s != null) {
			this.emptyValid = Boolean.parseBoolean(s);
		}

		s = propertyNodes[0].getAttributeValue("@maxlen");
		if (s != null) {
			this.maxLength = Integer.parseInt(s);
		}

		s = propertyNodes[0].getAttributeValue("@minlen");
		if (s != null) {
			this.minLength = Integer.parseInt(s);
		}

		s = propertyNodes[0].getAttributeValue("@pattern");
		if (s != null) {
			this.pattern = Pattern.compile(s);
		}

		s = propertyNodes[0].getAttributeValue("@multiline");
		if (s != null) {
			this.multiline = Boolean.parseBoolean(s);
		}

		s = propertyNodes[0].getAttributeValue("@escape");
		if (s == null) {
			if (this.multiline) {
				this.escapeMap = DEFAULT_ESCAPE_MAP;
			}
		} else {
			if (s.length() > 0 && (!s.equals("none"))) {
				final List<String> l = StringHelper.split(s, ",");
				final int len = l.size();
				if (len < 2) {
					throw new RapidBeansRuntimeException(
							"invalid escape definition \"" + s + "\"");
				}
				if (len % 2 != 0) {
					throw new RapidBeansRuntimeException(
							"invalid escape definition: "
									+ "odd number of mappings \"" + s + "\"");
				}
				for (int i = 0; i < len; i++) {
					l.set(i, StringHelper.unescape(l.get(i)));
				}
				this.escapeMap = new EscapeMap(l);
			}
		}
	}

	/**
	 * Evaluate XML binding description (XML)
	 * 
	 * @param beantype
	 *            the parent bean type
	 * @param propNode
	 *            the XML property description node
	 */
	protected void evalXmlBinding(final TypeRapidBean beantype,
			final XmlNode propNode) {
		if (this.multiline && this.escapeMap == DEFAULT_ESCAPE_MAP) {
			if (this.getXmlBindingType() == PropertyXmlBindingType.element) {
				this.escapeMap = null;
			}
		}
	}

	/**
	 * @return the property type enumeration
	 */
	public PropertyType getProptype() {
		return PropertyType.string;
	}

	private EscapeMap escapeMap = null;

	private static final EscapeMap DEFAULT_ESCAPE_MAP = new EscapeMap(
			new String[] { "\n", "\\n", "\t", "\\t", "\r", "\\r", "\b", "\\b",
					"\\", "\\\\" });

	/**
	 * @return the escape map
	 */
	public EscapeMap getEscapeMap() {
		return this.escapeMap;
	}
}
