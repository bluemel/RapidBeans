/*
 * Rapid Beans Framework: RapidQuantity.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/21/2005
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.EnumException;
import org.rapidbeans.core.exception.QuantityConversionNotSupportedException;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.UtilException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeRapidEnum;
import org.rapidbeans.core.type.TypeRapidQuantity;
import org.rapidbeans.core.util.StringHelper;

/**
 * the abstract base class for quantity value objects.
 * 
 * @author Martin Bluemel
 */
public abstract class RapidQuantity implements Cloneable,
		Comparable<RapidQuantity> {

	/**
	 * the magnitude.
	 */
	private BigDecimal magnitude;

	/**
	 * returns null if the enumTypeName could not be found in the table and a
	 * concrete enum class could not be loaded.
	 * 
	 * @return the magnitude
	 */
	public final BigDecimal getMagnitude() {
		return this.magnitude;
	}

	/**
	 * the getter for the magnitude as long.
	 * 
	 * @return the magnitude
	 */
	public final long getMagnitudeLong() {
		return this.magnitude.longValue();
	}

	/**
	 * the getter for the magnitude as double.
	 * 
	 * @return the magnitude
	 */
	public final double getMagnitudeDouble() {
		return this.magnitude.doubleValue();
	}

	/**
	 * the unit.
	 */
	private RapidEnum unit;

	/**
	 * the getter for the unit.
	 * 
	 * @return the unit
	 */
	public final RapidEnum getUnit() {
		return this.unit;
	}

	/**
	 * the type accesor method.
	 * 
	 * @return the type instance
	 */
	public abstract TypeRapidQuantity getType();

	/**
	 * parameter types for constructor RapidQuantity(String).
	 */
	private static final Class<?>[] CONSTR_PARAMTYPES_STRING = { String.class };

	/**
	 * Factory method for a RapidQuantity.
	 * 
	 * @param typename
	 *            determines the quantity's type
	 * @param val
	 *            determines the quantity's value e. g. "12 km"
	 * 
	 * @return the new quantity instance
	 */
	@SuppressWarnings("unchecked")
	public static final RapidQuantity createInstance(final String typename,
			final String val) {
		RapidQuantity quantity = null;
		final TypeRapidQuantity type = TypeRapidQuantity.forName(typename);
		final Class<?> clazz = type.getImplementingClass();
		if (clazz != null) {
			try {
				final Constructor<RapidQuantity> constr = (Constructor<RapidQuantity>) clazz
						.getConstructor(CONSTR_PARAMTYPES_STRING);
				final Object[] initargs = new Object[1];
				initargs[0] = val;
				quantity = (RapidQuantity) constr.newInstance(initargs);
			} catch (NoSuchMethodException e) {
				throw new RapidBeansRuntimeException("RapidQuantity class \""
						+ clazz.getName()
						+ "\" does not have a constructor with String", e);
			} catch (IllegalArgumentException e) {
				throw new RapidBeansRuntimeException(
						"IllegalArgumentException while trying"
								+ "to create instance for quantity class \""
								+ clazz.getName() + "\", quantity type \""
								+ typename + "\"", e);
			} catch (InstantiationException e) {
				throw new RapidBeansRuntimeException(
						"InstantiationException while trying"
								+ "to create instance for quantity class \""
								+ clazz.getName() + "\", quantity type \""
								+ typename + "\"", e);
			} catch (IllegalAccessException e) {
				throw new RapidBeansRuntimeException(
						"IllegalAccessException while trying"
								+ "to create instance for quantity class \""
								+ clazz.getName() + "\", quantity type \""
								+ typename + "\"", e);
			} catch (InvocationTargetException e) {
				throw new RapidBeansRuntimeException(
						"InvocationTargetException while trying"
								+ " to create instance for quantity class \""
								+ clazz.getName() + "\", quantity type \""
								+ typename + "\"", e);
			}
		} else {
			quantity = new GenericQuantity(type, val);
		}
		return quantity;
	}

	/**
	 * parameter types for constructor RapidQuantity(String).
	 */
	private static final Class<?>[] CONSTR_PARAMTYPES_MAG_UNIT = {
			BigDecimal.class, RapidEnum.class };

	/**
	 * Factory method for a RapidQuantity.
	 * 
	 * @param typename
	 *            determines the quantity's type
	 * @param val
	 *            determines the quantity's value e. g. "12 km"
	 * 
	 * @return the new quantity instance
	 */
	@SuppressWarnings("unchecked")
	public static final RapidQuantity createInstance(
			final TypeRapidQuantity type, final BigDecimal magnitude,
			final RapidEnum unit) {
		RapidQuantity quantity = null;
		final Class<?> clazz = type.getImplementingClass();
		if (clazz != null) {
			try {
				final Constructor<RapidQuantity> constr = (Constructor<RapidQuantity>) clazz
						.getConstructor(CONSTR_PARAMTYPES_MAG_UNIT);
				final Object[] initargs = new Object[2];
				initargs[0] = magnitude;
				initargs[1] = unit;
				quantity = (RapidQuantity) constr.newInstance(initargs);
			} catch (NoSuchMethodException e) {
				throw new RapidBeansRuntimeException(
						"RapidQuantity class \""
								+ clazz.getName()
								+ "\" does not have a constructor with BigDecimal and RapidEnum",
						e);
			} catch (IllegalArgumentException e) {
				throw new RapidBeansRuntimeException(
						"IllegalArgumentException while trying"
								+ "to create instance for quantity class \""
								+ clazz.getName() + "\", quantity type \""
								+ type.getName() + "\"", e);
			} catch (InstantiationException e) {
				throw new RapidBeansRuntimeException(
						"InstantiationException while trying"
								+ "to create instance for quantity class \""
								+ clazz.getName() + "\", quantity type \""
								+ type.getName() + "\"", e);
			} catch (IllegalAccessException e) {
				throw new RapidBeansRuntimeException(
						"IllegalAccessException while trying"
								+ "to create instance for quantity class \""
								+ clazz.getName() + "\", quantity type \""
								+ type.getName() + "\"", e);
			} catch (InvocationTargetException e) {
				throw new RapidBeansRuntimeException(
						"InvocationTargetException while trying"
								+ " to create instance for quantity class \""
								+ clazz.getName() + "\", quantity type \""
								+ type.getName() + "\"", e);
			}
		} else {
			quantity = new GenericQuantity(type, magnitude, unit);
		}
		return quantity;
	}

	/**
	 * simple constructor.
	 * 
	 * @param description
	 *            the enum's string representation.
	 */
	protected RapidQuantity() {
		this.magnitude = null;
		this.unit = null;
	}

	/**
	 * constructor.
	 * 
	 * @param description
	 *            the enum's string representation.
	 */
	protected RapidQuantity(final String description) {
		this(null, description);
	}

	/**
	 * constructor.
	 * 
	 * @param argType
	 *            the RapidQuantity's type
	 * @param description
	 *            the enum's string representation.
	 */
	protected RapidQuantity(final TypeRapidQuantity type,
			final String description) {
		TypeRapidQuantity tp = type;
		if (tp == null) {
			tp = this.getType();
		}
		List<String> tokenizedDescr = StringHelper.split(description, " ");
		switch (tokenizedDescr.size()) {
		case 0:
			Object[] oa1 = { description };
			throw new ValidationException("invalid.quantity.empty", this,
					"Invalid empty quantity description \"" + description
							+ "\"", oa1);
		case 1:
			this.magnitude = parseMagnitudeOneToken(description);
			this.unit = parseUnitOneToken(description);
			break;
		case 2:
			try {
				this.magnitude = new BigDecimal(tokenizedDescr.get(0));
			} catch (NumberFormatException e) {
				throw new ValidationException("invalid.quantity.magnitude",
						this, "RapidQuantity with invalid magnitude \""
								+ tokenizedDescr.get(0) + "\"", new Object[] {
								description, tokenizedDescr.get(0) });
			}
			try {
				this.unit = tp.getUnitInfo().elementOf(tokenizedDescr.get(1));
			} catch (EnumException e) {
				String validEnums = TypeRapidEnum.format(tp.getUnitInfo()
						.getElements());
				throw new ValidationException("invalid.quantity.unit", this,
						"RapidQuantity with invalid unit \""
								+ tokenizedDescr.get(1) + "\"",
						new Object[] { description, tokenizedDescr.get(1),
								validEnums });
			}
			break;
		default:
			Object[] oa3 = { description };
			throw new ValidationException(
					"invalid.quantity.format.threeormore", this,
					"Invalid quantity description \"" + description
							+ "\" with more than two tokens.\n"
							+ "Need two tokens: <magnitude> <unit>", oa3);
		}
	}

	/**
	 * constructor.
	 * 
	 * @param argMagnitude
	 *            the magnitude
	 * @param argUnit
	 *            the unit
	 */
	protected RapidQuantity(final BigDecimal argMagnitude,
			final RapidEnum argUnit) {
		this.magnitude = argMagnitude;
		this.unit = argUnit;
	}

	/**
	 * to conversion to a string.
	 * 
	 * @return the string
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (this.magnitude == null) {
			buf.append("null");
		} else {
			buf.append(this.magnitude.toString());
		}
		buf.append(' ');
		if (this.magnitude == null) {
			buf.append("null");
		} else {
			buf.append(this.unit.toString());
		}
		return buf.toString();
	}

	/**
	 * Convert to a localized string
	 * 
	 * @param locale
	 *            the locale
	 * @param minimumFractionDigits
	 *            the minimal fraction digits
	 * @param maximumFractionDigits
	 *            the maximal fraction digits
	 * 
	 * @return the localized string
	 */
	public String toStringGui(final RapidBeansLocale locale,
			final int minimumFractionDigits, final int maximumFractionDigits) {
		// later on we can construct here with a locale
		NumberFormat ft = NumberFormat.getInstance();
		ft.setMinimumFractionDigits(minimumFractionDigits);
		ft.setMaximumFractionDigits(minimumFractionDigits);
		return ft.format(this.getMagnitude()) + " "
				+ this.getUnit().toStringGui(locale);
	}

	/**
	 * equals.
	 * 
	 * @param o
	 *            the object to compare with
	 * 
	 * @return if equals or not
	 */
	public final boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof RapidQuantity)) {
			return false;
		}
		RapidQuantity quant = (RapidQuantity) o;

		return (this.getUnit() == quant.getUnit() && ((this.getMagnitude() == null && quant
				.getMagnitude() == null) || (this.getMagnitude() != null
				&& quant.getMagnitude() != null && this.getMagnitude().equals(
				quant.getMagnitude()))));
	}

	/**
	 * @return the quantity's hash code.
	 */
	public final int hashCode() {
		return this.toString().hashCode();
	}

	/**
	 * the comparator.
	 * 
	 * @param o
	 *            the other quantity
	 * @return -1, 0, 1 if less, equal, greater than
	 */
	public int compareTo(final RapidQuantity quant) {
		int comp = 0;
		if (this.getType() != quant.getType()) {
			throw new UtilException(
					"Cannot compare a RapidQuantity against a \""
							+ quant.getClass().getName()
							+ "\".\n"
							+ "only quantities of same type can be compared against each other.");
		}
		if (this.getUnit() == quant.getUnit()) {
			comp = this.getMagnitude().compareTo(quant.getMagnitude());
		} else {
			try {
				RapidQuantity cQuant = quant.convert(this.getUnit());
				comp = this.getMagnitude().compareTo(cQuant.getMagnitude());
			} catch (QuantityConversionNotSupportedException e) {
				RapidQuantity cThis = this.convert(quant.getUnit());
				comp = cThis.getMagnitude().compareTo(quant.getMagnitude());
			}
		}

		return comp;
	}

	/**
	 * the conversion routine.
	 * 
	 * @param conversionUnit
	 *            the unit to convert into
	 * 
	 * @return a new quantity containing the converted value
	 */
	public final RapidQuantity convert(final RapidEnum conversionUnit) {
		RapidQuantity converted = null;
		try {
			converted = (RapidQuantity) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new UtilException(e.getMessage());
		}
		if (this.getUnit() != conversionUnit) {
			final BigDecimal conversionFactor = this.getType()
					.getConversionTable()
					.getConversionFactor(this.unit, conversionUnit);
			if (conversionFactor == null) {
				throw new QuantityConversionNotSupportedException(
						"conversion of quantity class \""
								+ this.getClass().getName() + "\" from Unit \""
								+ this.unit + "\" to Unit \"" + conversionUnit
								+ "\" is not supported.");
			}
			final boolean conversionFactorReciprocal = this
					.getType()
					.getConversionTable()
					.getConversionFactorReciprocalFlag(this.unit,
							conversionUnit);
			if (conversionFactorReciprocal) {
				try {
					converted.magnitude = (BigDecimal) this.magnitude
							.divide(conversionFactor);
				} catch (ArithmeticException e) {
					double dMagnitude = this.magnitude.doubleValue();
					double dConversionFactor = conversionFactor.doubleValue();
					converted.magnitude = new BigDecimal(dMagnitude
							/ dConversionFactor);
				}
			} else {
				converted.magnitude = (BigDecimal) this.magnitude
						.multiply(conversionFactor);
			}
			converted.unit = conversionUnit;
		}
		return converted;
	}

	/**
	 * Parser function for quantity magnitudes. To be overwritten for specific
	 * quantity classes.
	 * 
	 * @param token
	 *            the token
	 * 
	 * @return throws an exception
	 */
	public BigDecimal parseMagnitudeOneToken(final String token) {
		Object[] oa = { token };
		throw new ValidationException("invalid.quantity.format.one", this,
				"Invalid quantity description \"" + token
						+ "\" with only one token.\n"
						+ "Need two tokens: <magnitude> <unit>", oa);
	}

	/**
	 * Parser function for quantity units. To be overwritten for specific
	 * quantity classes.
	 * 
	 * @param token
	 *            the token
	 * 
	 * @return throws an exception
	 */
	public RapidEnum parseUnitOneToken(final String token) {
		Object[] oa = { token };
		throw new ValidationException("invalid.quantity.format.one", this,
				"Invalid quantity description \"" + token
						+ "\" with only one token.\n"
						+ "Need two tokens: <magnitude> <unit>", oa);
	}

	/**
	 * Round the quantity with rounding mode "half up".
	 * 
	 * @param fraction
	 *            specifies the number of digits behind the decimal sign
	 */
	public RapidQuantity round(final int fraction) {
		return round(fraction, RoundingMode.HALF_UP);
	}

	/**
	 * Add the given summand to this. The given summand is converted to this
	 * unit.
	 * 
	 * @param summand
	 *            the summand
	 * 
	 * @return the addition result.
	 */
	public RapidQuantity add(final RapidQuantity summand) {
		RapidQuantity sd = summand;
		if (this.unit != sd.getUnit()) {
			sd = sd.convert(this.getUnit());
		}
		return RapidQuantity.createInstance(this.getType(), this.getMagnitude()
				.add(sd.getMagnitude()), this.unit);
	}

	/**
	 * Divide this value by the given dividend. The given dividend is converted
	 * to this unit.
	 * 
	 * @param dividend
	 *            the dividend
	 * 
	 * @return the division result.
	 */
	public RapidQuantity divide(final RapidQuantity dividend) {
		RapidQuantity div = dividend;
		if (this.unit != div.getUnit()) {
			div = dividend.convert(this.getUnit());
		}
		RapidQuantity result = null;
		try {
			result = RapidQuantity.createInstance(this.getType(), this
					.getMagnitude().divide(div.getMagnitude()), this.unit);
		} catch (ArithmeticException e) {
			double d = this.getMagnitude().doubleValue()
					/ div.getMagnitude().doubleValue();
			result = RapidQuantity.createInstance(this.getType(),
					new BigDecimal(d), this.unit);
		}
		return result;
	}

	/**
	 * Round the quantity.
	 * 
	 * @param friction
	 *            specifies the number of digits behind the decimal sign
	 * @param roundingMode
	 *            rounding mode
	 */
	public RapidQuantity round(final int fraction, RoundingMode roundingMode) {
		if (this.magnitude == null) {
			throw new IllegalArgumentException(
					"can not round a quantity with null magnitude");
		}
		int precision = 3;
		double mag = this.magnitude.doubleValue();
		while (mag > 10.0) {
			mag /= 10.0;
			precision++;
		}
		RapidQuantity rounded = RapidQuantity.createInstance(this.getType(),
				this.magnitude.round(new MathContext(precision, roundingMode)),
				this.getUnit());
		return rounded;
	}
}
