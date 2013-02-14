/*
 * Rapid Beans Framework: TypePropertyInteger.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/27/2005
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

import java.math.BigInteger;

import org.rapidbeans.core.basic.IntegerSize;
import org.rapidbeans.core.basic.PropertyInteger;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.util.ClassHelper;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.core.util.XmlNode;

/**
 * The bean property type class for Integer properties.
 * 
 * @author Martin Bluemel
 */
public final class TypePropertyInteger extends TypePropertyNumber {

	/**
	 * Bit size of the current integer implementation.
	 * Default: byte04 is the size of Java Integer (int).
	 */
	private IntegerSize size = IntegerSize.byte04;

	/**
	 * Constructor for TypePropertyInteger.
	 * 
	 * @param xmlNode
	 *            the XML DOM node describing the property type
	 * @param parentBeanType
	 *            the parent bean type
	 */
	public TypePropertyInteger(final XmlNode[] xmlNodes,
			final TypeRapidBean parentBeanType) {
		super("Integer", xmlNodes, parentBeanType);

		final String sizeString = xmlNodes[0].getAttributeValue("@size");
		if (sizeString != null) {
			this.size = IntegerSize.valueOf(sizeString);
		}

		if (this.getNumberClass() == null) {
			this.setNumberClass(sizeToNumberClass(this.size));
		}

		final String maxValueString = xmlNodes[0].getAttributeValue("@maxval");
		if (maxValueString != null) {
			this.setMaxValue(constructNumber(maxValueString));
		}

		final String minValueString = xmlNodes[0].getAttributeValue("@minval");
		if (minValueString != null) {
			this.setMinValue(constructNumber(minValueString));
		}

		final String defaultValueString = xmlNodes[0].getAttributeValue("@default");
		if (defaultValueString != null) {
			final Number defaultValue = convertValue(null, defaultValueString);
			this.setDefaultValue(defaultValue);
		}
	}

	/**
	 * Tells us which Java number class implements the given size.
	 * 
	 * @param size
	 *            the size to convert to a Java number class.
	 * @return the Java number class implementing the given integer size.
	 */
	private Class<?> sizeToNumberClass(final IntegerSize size) {
		switch (size) {
		case byte01:
			return Byte.class;
		case byte02:
			return Short.class;
		case byte04:
			return Integer.class;
		case byte08:
			return Long.class;
		case unlimited:
			return BigInteger.class;
		default:
			throw new RapidBeansRuntimeException("Unknown integer size \""
					+ size.toString() + "\"");
		}
	}

	/**
	 * @return the property type enumeration
	 */
	public PropertyType getProptype() {
		return PropertyType.integer;
	}

	private enum ConvertErrorType {
		none,
		tooLargeByte,
		tooSmallByte,
		tooLargeShort,
		tooSmallShort,
		tooLargeInteger,
		tooSmallInteger,
		tooLargeLong,
		tooSmallLong,
	}

	private static final BigInteger bigIntLongMaxValue = new BigInteger(new Long(Long.MAX_VALUE).toString());

	private static final BigInteger bigIntLongMinValue = new BigInteger(new Long(Long.MIN_VALUE).toString());

	private static final BigInteger bigIntIntegerMaxValue = new BigInteger(new Integer(Integer.MAX_VALUE).toString());

	private static final BigInteger bigIntIntegerMinValue = new BigInteger(new Integer(Integer.MIN_VALUE).toString());

	private static final BigInteger bigIntShortMaxValue = new BigInteger(new Short(Short.MAX_VALUE).toString());

	private static final BigInteger bigIntShortMinValue = new BigInteger(new Short(Short.MIN_VALUE).toString());

	private static final BigInteger bigIntByteMaxValue = new BigInteger(new Byte(Byte.MAX_VALUE).toString());

	private static final BigInteger bigIntByteMinValue = new BigInteger(new Byte(Byte.MIN_VALUE).toString());

	/**
	 * converter.
	 * 
	 * @param prop
	 *            the property to validate
	 * @param integerValue
	 *            the object to convert
	 *            Must be an instance of the following classes:<br/>
	 *            <b>Integer:</b> the integer value itself<br/>
	 *            <b>String:</b> the integer as decimal string<br/>
	 * 
	 * @return the converted value
	 */
	public Number convertValue(final PropertyInteger prop, final Object integerValue) {
		Number i = null;
		final Class<?> numberClass = this.getNumberClass();
		ConvertErrorType error = ConvertErrorType.none;
		if (integerValue != null) {
			if (integerValue instanceof BigInteger) {
				final BigInteger intValBigInt = (BigInteger) integerValue;
				if (ClassHelper.classOf(BigInteger.class, numberClass)) {
					i = intValBigInt;
				} else if (ClassHelper.classOf(Long.class, numberClass)) {
					if (intValBigInt.compareTo(bigIntLongMaxValue) > 0) {
						error = ConvertErrorType.tooLargeLong;
					} else if (intValBigInt.compareTo(bigIntLongMinValue) < 0) {
						error = ConvertErrorType.tooSmallLong;
					} else {
						i = new Long(intValBigInt.longValue());
					}
				} else if (ClassHelper.classOf(Integer.class, numberClass)) {
					if (intValBigInt.compareTo(bigIntIntegerMaxValue) > 0) {
						error = ConvertErrorType.tooLargeInteger;
					} else if (intValBigInt.compareTo(bigIntIntegerMinValue) < 0) {
						error = ConvertErrorType.tooSmallInteger;
					} else {
						i = new Integer(intValBigInt.intValue());
					}
				} else if (ClassHelper.classOf(Short.class, numberClass)) {
					if (intValBigInt.compareTo(bigIntShortMaxValue) > 0) {
						error = ConvertErrorType.tooLargeShort;
					} else if (intValBigInt.compareTo(bigIntShortMinValue) < 0) {
						error = ConvertErrorType.tooSmallShort;
					} else {
						i = new Short(intValBigInt.shortValue());
					}
				} else if (ClassHelper.classOf(Byte.class, numberClass)) {
					if (intValBigInt.compareTo(bigIntByteMaxValue) > 0) {
						error = ConvertErrorType.tooLargeByte;
					} else if (intValBigInt.compareTo(bigIntByteMinValue) < 0) {
						error = ConvertErrorType.tooSmallByte;
					} else {
						i = new Byte(intValBigInt.byteValue());
					}
				}
			} else if (integerValue instanceof Long) {
				final Long intValLong = (Long) integerValue;
				if (ClassHelper.classOf(BigInteger.class, numberClass)) {
					i = new BigInteger(intValLong.toString());
				} else if (ClassHelper.classOf(Long.class, numberClass)) {
					i = intValLong;
				} else if (ClassHelper.classOf(Integer.class, numberClass)) {
					if (intValLong > Integer.MAX_VALUE) {
						error = ConvertErrorType.tooLargeInteger;
					} else if (intValLong < Integer.MIN_VALUE) {
						error = ConvertErrorType.tooSmallInteger;
					} else {
						i = new Integer(intValLong.intValue());
					}
				} else if (ClassHelper.classOf(Short.class, numberClass)) {
					if (intValLong > Short.MAX_VALUE) {
						error = ConvertErrorType.tooLargeShort;
					} else if (intValLong < Short.MIN_VALUE) {
						error = ConvertErrorType.tooSmallShort;
					} else {
						i = new Short(intValLong.shortValue());
					}
				} else if (ClassHelper.classOf(Byte.class, numberClass)) {
					if (intValLong > Byte.MAX_VALUE) {
						error = ConvertErrorType.tooLargeByte;
					} else if (intValLong < Byte.MIN_VALUE) {
						error = ConvertErrorType.tooSmallByte;
					} else {
						i = new Byte(intValLong.byteValue());
					}
				}
			} else if (integerValue instanceof Integer) {
				final Integer intValInteger = (Integer) integerValue;
				if (ClassHelper.classOf(BigInteger.class, numberClass)) {
					i = new BigInteger(intValInteger.toString());
				} else if (ClassHelper.classOf(Long.class, numberClass)) {
					i = new Long(intValInteger.longValue());
				} else if (ClassHelper.classOf(Integer.class, numberClass)) {
					i = intValInteger;
				} else if (ClassHelper.classOf(Short.class, numberClass)) {
					if (intValInteger > Short.MAX_VALUE) {
						error = ConvertErrorType.tooLargeShort;
					} else if (intValInteger < Short.MIN_VALUE) {
						error = ConvertErrorType.tooSmallShort;
					} else {
						i = new Short(intValInteger.shortValue());
					}
				} else if (ClassHelper.classOf(Byte.class, numberClass)) {
					if (intValInteger > Byte.MAX_VALUE) {
						error = ConvertErrorType.tooLargeByte;
					} else if (intValInteger < Byte.MIN_VALUE) {
						error = ConvertErrorType.tooSmallByte;
					} else {
						i = new Byte(intValInteger.byteValue());
					}
				}
			} else if (integerValue instanceof Short) {
				final Short intValShort = (Short) integerValue;
				if (ClassHelper.classOf(BigInteger.class, numberClass)) {
					i = new BigInteger(intValShort.toString());
				} else if (ClassHelper.classOf(Long.class, numberClass)) {
					i = new Long(intValShort.longValue());
				} else if (ClassHelper.classOf(Integer.class, numberClass)) {
					i = new Integer(intValShort.intValue());
				} else if (ClassHelper.classOf(Short.class, numberClass)) {
					i = intValShort;
				} else if (ClassHelper.classOf(Byte.class, numberClass)) {
					if (intValShort > Byte.MAX_VALUE) {
						error = ConvertErrorType.tooLargeByte;
					} else if (intValShort < Byte.MIN_VALUE) {
						error = ConvertErrorType.tooSmallByte;
					} else {
						i = new Byte(intValShort.byteValue());
					}
				}
			} else if (integerValue instanceof Byte) {
				if (ClassHelper.classOf(BigInteger.class, numberClass)) {
					i = new BigInteger(((Byte) integerValue).toString());
				} else if (ClassHelper.classOf(Long.class, numberClass)) {
					i = new Long(((Byte) integerValue).longValue());
				} else if (ClassHelper.classOf(Integer.class, numberClass)) {
					i = new Integer(((Byte) integerValue).intValue());
				} else if (ClassHelper.classOf(Short.class, numberClass)) {
					i = new Short(((Byte) integerValue).shortValue());
				} else if (ClassHelper.classOf(Byte.class, numberClass)) {
					i = (Byte) integerValue;
				}
			} else if (integerValue instanceof String) {
				String sIntegerValue = (String) integerValue;
				try {
					if (ClassHelper.classOf(BigInteger.class, numberClass)) {
						i = new BigInteger(sIntegerValue);
					} else if (ClassHelper.classOf(Long.class, numberClass)) {
						i = new Long(sIntegerValue);
					} else if (ClassHelper.classOf(Integer.class, numberClass)) {
						i = new Integer(sIntegerValue);
					} else if (ClassHelper.classOf(Short.class, numberClass)) {
						i = new Short(sIntegerValue);
					} else if (ClassHelper.classOf(Byte.class, numberClass)) {
						i = new Byte(sIntegerValue);
					}
				} catch (NumberFormatException e) {
					boolean negative = false;
					sIntegerValue = sIntegerValue.trim();
					if (sIntegerValue.startsWith("-")) {
						sIntegerValue = sIntegerValue.substring(1);
						negative = true;
					}
					if (sIntegerValue.length() > 0 && StringHelper.isDigitsOnly(sIntegerValue)
							&& (!ClassHelper.classOf(BigInteger.class, numberClass))) {
						if (negative) {
							if (ClassHelper.classOf(Long.class, numberClass)) {
								error = ConvertErrorType.tooSmallLong;
							} else if (ClassHelper.classOf(Integer.class, numberClass)) {
								error = ConvertErrorType.tooSmallInteger;
							} else if (ClassHelper.classOf(Short.class, numberClass)) {
								error = ConvertErrorType.tooSmallShort;
							} else if (ClassHelper.classOf(Byte.class, numberClass)) {
								error = ConvertErrorType.tooSmallByte;
							}
						} else {
							if (ClassHelper.classOf(Long.class, numberClass)) {
								error = ConvertErrorType.tooLargeLong;
							} else if (ClassHelper.classOf(Integer.class, numberClass)) {
								error = ConvertErrorType.tooLargeInteger;
							} else if (ClassHelper.classOf(Short.class, numberClass)) {
								error = ConvertErrorType.tooLargeShort;
							} else if (ClassHelper.classOf(Byte.class, numberClass)) {
								error = ConvertErrorType.tooLargeByte;
							}
						}
					} else {
						throw new ValidationException("invalid.prop.integer.nonumber",
								prop,
								"\"" + (String) integerValue + "\" is no valid number.",
								new Object[] { integerValue });
					}
				}
			} else {
				throw new ValidationException("invalid.prop.integer.type",
						prop,
						"invalid data type \"" + integerValue.getClass().getName()
								+ "\".\nOnly \"Integer\" and \"String\" are valid types.");
			}
		}

		switch (error) {
		case tooLargeLong:
			throw new ValidationException("invalid.prop.integer.int.large",
					prop,
					integerValue.toString() + " is too large for a Java Long.\n"
							+ "The largest possible Long value is "
							+ new Long(Long.MAX_VALUE).toString() + ".",
					new Object[] { integerValue.toString(), Long.MAX_VALUE });
		case tooSmallLong:
			throw new ValidationException("invalid.prop.integer.int.small",
					prop,
					integerValue.toString() + " is too small for a Java Long.\n"
							+ "The smallest possible Long value is "
							+ new Long(Long.MIN_VALUE).toString() + ".",
					new Object[] { integerValue.toString(), Long.MIN_VALUE });
		case tooLargeInteger:
			throw new ValidationException("invalid.prop.integer.int.large",
					prop,
					integerValue.toString() + " is too large for a Java Integer.\n"
							+ "The largest possible Java Integer value is "
							+ new Integer(Integer.MAX_VALUE).toString() + ".",
					new Object[] { integerValue.toString(), Integer.MAX_VALUE });
		case tooSmallInteger:
			throw new ValidationException("invalid.prop.integer.int.small",
					prop,
					integerValue.toString() + " is too small for a Java Integer.\n"
							+ "The smallest possible Java Integer value is "
							+ new Integer(Integer.MIN_VALUE).toString() + ".",
					new Object[] { integerValue.toString(), Integer.MIN_VALUE });
		case tooLargeShort:
			throw new ValidationException("invalid.prop.integer.int.large",
					prop,
					integerValue.toString() + " is too large for a Java Short.\n"
							+ "The largest possible Java Short value is "
							+ new Short(Short.MAX_VALUE).toString() + ".",
					new Object[] { integerValue.toString(), Short.MAX_VALUE });
		case tooSmallShort:
			throw new ValidationException("invalid.prop.integer.int.small",
					prop,
					integerValue.toString() + " is too small for a Java Short.\n"
							+ "The smallest possible Java Short value is "
							+ new Short(Short.MIN_VALUE).toString() + ".",
					new Object[] { integerValue.toString(), Short.MIN_VALUE });
		case tooLargeByte:
			throw new ValidationException("invalid.prop.integer.int.large",
					prop,
					integerValue.toString() + " is too large for a Java Byte\n"
							+ "The largest possible Java Byte value is "
							+ new Byte(Byte.MAX_VALUE).toString() + ".",
					new Object[] { integerValue.toString(), Byte.MAX_VALUE });
		case tooSmallByte:
			throw new ValidationException("invalid.prop.integer.int.small",
					prop,
					integerValue.toString() + " is too small for a Java Byte\n"
							+ "The smallest possible Java Byte value is "
							+ new Byte(Byte.MIN_VALUE).toString() + ".",
					new Object[] { integerValue.toString(), Byte.MIN_VALUE });
		case none:
			break;
		default:
			throw new RapidBeansRuntimeException(
					"Unexpected errorType \"" + error.name() + "\"");
		}
		return i;
	}
}
