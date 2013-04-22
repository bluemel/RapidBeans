/*
 * Rapid Beans Framework: PropertyDate.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 12/24/2005
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

import java.text.DateFormat;
import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.rapidbeans.core.common.PrecisionDate;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.PropValueNullException;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyDate;
import org.rapidbeans.core.util.StringHelper;

/**
 * A <b>Date</b> bean property encapsulates Date values which express a certain
 * point of time in the precision of milliseconds.<br/>
 * The precision of the property is configurable from millisecond to year<br/>
 * In addition it optionally enforces validation of:<br/>
 * - minimal date<br/>
 * - maximal date<br/>
 * 
 * @author Martin Bluemel
 */
public class PropertyDate extends Property {

	/**
	 * the property's Date value. !!! do not initialize here because the
	 * superclass does it with the property type's default value
	 */
	private Date value;

	/**
	 * formatter for language independent format 1.
	 */
	private static final DateFormat LANG_INDEP_DATE_FORMAT_1 = DateFormat.getDateInstance(DateFormat.MEDIUM,
			Locale.GERMAN);

	/**
	 * formatter for language independent format 2.
	 */
	private static final DateFormat LANG_INDEP_DATE_FORMAT_2 = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
			DateFormat.MEDIUM, Locale.GERMAN);

	/**
	 * constructor for a new Date Property.
	 * 
	 * @param type
	 *            the Property's type
	 * @param parentBean
	 *            the parent bean
	 */
	public PropertyDate(final TypeProperty type, final RapidBean parentBean) {
		super(type, parentBean);
	}

	/**
	 * generic value getter.
	 * 
	 * @return the value of this Property as java.util.Date
	 */
	public Date getValue() {
		Date date = null;
		if (getBean() instanceof RapidBeanImplSimple) {
			date = (Date) Property.getValueFieldByReflection(getBean(), getName());
		} else {
			if (this.value != null) {
				// clone the date value object in order
				// to compensate the Java design error of a mutable Date object
				date = new Date(this.value.getTime());
			}
		}
		return date;
	}

	/**
	 * special value getter.
	 * 
	 * @return the value of this Property as long which represents the internal
	 *         time value of a java.util.Date
	 */
	public long getValueTime() {
		final Date date = getValue();
		if (date == null) {
			throw new PropValueNullException("value for property not defined");
		}
		return date.getTime();
	}

	/**
	 * String getter. Converts the Date Property into a String depending on the
	 * configured precision.<br/>
	 * Examples:<br/>
	 * precision = year, date = 2005 => <b>2005</b><br/>
	 * precision = day, date = March 15, 2005 => <b>20050315</b><br/>
	 * precision = millisecond, date = March 15, 2005, time = 11:23:45.678 pm =>
	 * <b>20050315112345678</b><br/>
	 * 
	 * @return the String representation of this Property's value.
	 */
	public String toString() {
		final Date date = getValue();
		if (date == null) {
			return null;
		} else {
			return format(date, ((TypePropertyDate) this.getType()).getPrecision());
		}
	}

	/**
	 * generic value setter. Accepts the following datatypes:<br/>
	 * <b>Date:</b> the Date<br/>
	 * <b>String:</b> the Date as string<br/>
	 * 
	 * @param newValue
	 *            the new value to set
	 */
	public void setValue(final Object newValue) {
		super.setValueWithEvents(getValue(), newValue, new PropertyValueSetter() {
			public void setValue(final Object newValue) {
				if (getBean() instanceof RapidBeanImplSimple) {
					Property.setValueByReflection(getBean(), getName(), newValue);
				} else {
					value = (Date) newValue;
				}
			}
		});
	}

	/**
	 * validate a value for a Date Property.
	 * 
	 * @param newValue
	 *            the value to convert and validate.
	 * 
	 * @return the converted value which is the internal representation or if a
	 *         primitive type the corresponding value object
	 */
	public Date validate(final Object newValue) {

		final Date date = (Date) super.validate(newValue);
		if (!ThreadLocalValidationSettings.getValidation()) {
			return date;
		}
		if (newValue == null) {
			return null;
		}

		final long time = date.getTime();
		final TypePropertyDate type = (TypePropertyDate) this.getType();

		// check against max boundary
		final long maxTime = type.getMaxVal();
		if (time > maxTime) {
			throw new ValidationException("invalid.prop.date.greater", this, "invalid date \""
					+ PropertyDate.format(new Date(time), type.getPrecision()) + "\" greater than maximal date \""
					+ PropertyDate.format(new Date(maxTime), type.getPrecision()) + ".", new String[] {});
		}

		// check against min boundary
		final long minTime = type.getMinVal();
		if (time < minTime) {
			throw new ValidationException("invalid.prop.date.lower", this, "invalid date \""
					+ PropertyDate.format(new Date(time), type.getPrecision()) + "\" lower than minimal date \""
					+ PropertyDate.format(new Date(minTime), type.getPrecision()) + ".");
		}

		return date;
	}

	/**
	 * converts the following classes into a time value for a Date.<br/>
	 * 
	 * - <b>Date:</b> cut to the appropriate precision<br/>
	 * - <b>String:</b> parsed from String representation<br/>
	 * - <b>Long:</b> seconds set as the time of the Date<br/>
	 * 
	 * @param argVal
	 *            the value object to convert
	 * 
	 * @return the converted Date
	 */
	public Date convertValue(final Object argVal) {
		if (argVal == null) {
			return null;
		}
		long time;

		if (argVal instanceof Date) {
			time = cutPrecisionLong(((Date) argVal).getTime());
		} else if (argVal instanceof String) {
			time = PropertyDate.parse(cutPrecisionString((String) argVal)).getTime();
		} else if (argVal instanceof Long) {
			time = cutPrecisionLong(((Long) argVal).longValue());
		} else {
			throw new ValidationException("invalid.prop.date.type", this, "invalid data type \""
					+ argVal.getClass().getName() + "\".\nOnly \"Date\", \"String\", and Long are valid types.");
		}

		return new Date(time);
	}

	/**
	 * Cut the given norm String to the appropriate precision.
	 * 
	 * @param newValue
	 *            the norm String
	 * @return the cut String
	 */
	private String cutPrecisionString(final String newValue) {
		final int len = newValue.length();
		int maxlen;
		switch (((TypePropertyDate) this.getType()).getPrecision()) {
		case year:
			maxlen = 4;
			break;
		case month:
			maxlen = 6;
			break;
		case day:
			maxlen = 8;
			break;
		case hour:
			maxlen = 10;
			break;
		case minute:
			maxlen = 12;
			break;
		case second:
			maxlen = 14;
			break;
		case millisecond:
			maxlen = 17;
			break;
		default:
			throw new RapidBeansRuntimeException("wrong precision");
		}
		String cutValue = newValue;
		if (len > maxlen) {
			cutValue = newValue.substring(0, maxlen);
		}
		return cutValue;
	}

	/**
	 * Cut the given norm String to the appropriate precision.
	 * 
	 * @param newValue
	 *            the norm String
	 * 
	 * @return the cut String
	 */
	public long cutPrecisionLong(final long newValue) {
		return cutPrecisionLong(newValue, ((TypePropertyDate) this.getType()).getPrecision());
	}

	/**
	 * Cut the given norm String to the appropriate precision.
	 * 
	 * @param newValue
	 *            the norm String
	 * @param precision
	 *            the precision to apply
	 * 
	 * @return the cut String
	 */
	public static long cutPrecisionLong(final long newValue, final PrecisionDate precision) {
		long cutValue = newValue;
		switch (precision) {
		case year:
			cutValue = parse(format(new Date(newValue), precision)).getTime();
			break;
		case month:
			cutValue = parse(format(new Date(newValue), precision)).getTime();
			break;
		case day:
			cutValue = parse(format(new Date(newValue), precision)).getTime();
			// cutValue = ((newValue + 3600000) / 86400000) * 86400000 -
			// 3600000;
			break;
		case hour:
			cutValue = parse(format(new Date(newValue), precision)).getTime();
			break;
		case minute:
			cutValue = parse(format(new Date(newValue), precision)).getTime();
			break;
		case second:
			cutValue = (newValue / 1000) * 1000;
			break;
		case millisecond:
			cutValue = newValue;
			break;
		default:
			throw new RapidBeansRuntimeException("wrong precision");
		}
		return cutValue;
	}

	/**
	 * convert a Date into a String.
	 * 
	 * @param d
	 *            the Date to convert
	 * @param precision
	 *            the precision given
	 * 
	 * @return the String representation of this property.
	 */
	public static String format(final Date d, final PrecisionDate precision) {
		String sDate = PropertyDate.LANG_INDEP_DATE_FORMAT_2.format(d);
		// get the year
		StringBuffer buf = new StringBuffer(sDate.substring(6, 10));
		if (precision == PrecisionDate.year) {
			return buf.toString();
		}
		buf.append(sDate.substring(3, 5));
		if (precision == PrecisionDate.month) {
			return buf.toString();
		}
		buf.append(sDate.substring(0, 2));
		if (precision == PrecisionDate.day) {
			return buf.toString();
		}
		buf.append(sDate.substring(11, 13));
		if (precision == PrecisionDate.hour) {
			return buf.toString();
		}
		buf.append(sDate.substring(14, 16));
		if (precision == PrecisionDate.minute) {
			return buf.toString();
		}
		buf.append(sDate.substring(17, 19));
		if (precision == PrecisionDate.second) {
			return buf.toString();
		}
		buf.append(Integer.toString((int) (d.getTime() % 1000)));
		return buf.toString();
	}

	/**
	 * parse a Date in norm format.
	 * 
	 * @param s
	 *            the String to parse
	 * 
	 * @return the parsed Date object.
	 */
	public static Date parse(final String s) {
		Date date = null;
		String sDate = null;

		int len = s.length();

		if (!StringHelper.isDigitsOnly(s)) {
			throw new ValidationException("invalid.prop.date.string.norm.number", null, "invalid date norm string \""
					+ s + "\", must have at least 4 and at maximum 17 digits." + "\nExample: 19641014235945999 means"
					+ " October 14, 1964 11:59 pm 45 seconds and 999 milliseconds.");
		}
		if (len < 4) {
			throw new ValidationException("invalid.prop.date.string.norm.short", null, "invalid " + len
					+ " digit date norm string \"" + s + "\", must have at least 4 digits."
					+ "\nExample: 1964 means the year 1964.");
		}
		if (len > 17) {
			throw new ValidationException("invalid.prop.date.string.norm.long", null, "invalid " + len
					+ " digit date (point of time) norm string \"" + s + "\", must have at maximum 17 characters."
					+ "\nExample: 19641014235945999 means"
					+ " October 14, 1964 11:59 pm 45 seconds and 999 milliseconds.");
		}

		int iDay = -1;
		int iMonth = -1;
		int iYear = -1;
		int iMillis = -1;
		GregorianCalendar cal = null;

		switch (len) {
		case 4:
			sDate = "01.01." + s.substring(0, 4);
			break;
		case 5:
			throw new ValidationException("invalid.prop.date.string.norm.5", null, "invalid " + len
					+ " digit date norm string \"" + s + "\", must have 4 or 6 digits."
					+ "\nExample: 196410 means October 14.");
		case 6:
			sDate = "01." + s.substring(4, 6) + "." + s.substring(0, 4);
			break;
		case 7:
			throw new ValidationException("invalid.prop.date.string.norm.7", null, "invalid " + len
					+ " digit date norm string \"" + s + "\", must have 6 or 8 digits."
					+ "\nExample: 19641014 means October 14, 1964.");
		case 8:
			iDay = Integer.parseInt(s.substring(6, 8));
			iMonth = Integer.parseInt(s.substring(4, 6)) - 1;
			iYear = Integer.parseInt(s.substring(0, 4));
			cal = new GregorianCalendar(iYear, iMonth, iDay);
			break;
		case 9:
			throw new ValidationException("invalid.prop.date.string.norm.9", null, "invalid " + len
					+ " digit date (point of time) norm string \"" + s + "\", must have 8 or 10 digits."
					+ "\nExample: 1964101423 means" + " October 14, 1964 11 pm seconds.");
		case 10:
			sDate = s.substring(6, 8) + "." + s.substring(4, 6) + "." + s.substring(0, 4) + " " + s.substring(8, 10)
					+ ":00:00";
			break;
		case 11:
			throw new ValidationException("invalid.prop.date.string.norm.11", null, "invalid " + len
					+ " digit date (point of time) norm string \"" + s + "\", must have 10 or 12 digits."
					+ "\nExample: 196410142359 means" + " October 14, 1964 11:59 pm.");
		case 12:
			sDate = s.substring(6, 8) + "." + s.substring(4, 6) + "." + s.substring(0, 4) + " " + s.substring(8, 10)
					+ ":" + s.substring(10, 12) + ":00";
			break;
		case 13:
			throw new ValidationException("invalid.prop.date.string.norm.13", null, "invalid " + len
					+ " digit date (point of time) norm string \"" + s + "\", must have 12 or 14 digits."
					+ "\nExample: 19641014235945 means" + " October 14, 1964 11:59 pm 45 seconds.");
		case 14:
			sDate = s.substring(6, 8) + "." + s.substring(4, 6) + "." + s.substring(0, 4) + " " + s.substring(8, 10)
					+ ":" + s.substring(10, 12) + ":" + s.substring(12, 14);
			break;
		case 15:
		case 16:
			throw new ValidationException("invalid.prop.date.string.norm.1516", null, "invalid " + len
					+ " digit date (point of time) norm string \"" + s + "\", must have 14 or 17 digits."
					+ "\nExample: 19641014235945999 means"
					+ " October 14, 1964 11:59 pm 45 seconds and 999 milliseconds.");
		case 17:
			sDate = s.substring(6, 8) + "." + s.substring(4, 6) + "." + s.substring(0, 4) + " " + s.substring(8, 10)
					+ ":" + s.substring(10, 12) + ":" + s.substring(12, 14);
			iMillis = Integer.parseInt(s.substring(14, 17));
			break;
		default:
			throw new RapidBeansRuntimeException("This should never never happen!!!");
		}

		if (cal != null) {
			if (iDay != cal.get(Calendar.DAY_OF_MONTH)) {
				throw new ValidationException("invalid.prop.date.dayofmonth", null, "invalid date \"" + s + "\"."
						+ "\nDay of month \"" + new Integer(iDay).toString() + "\" is incorrect.");
			}
			if (iMonth != cal.get(Calendar.MONTH)) {
				throw new ValidationException("invalid.prop.date.month", null, "invalid date \"" + s + "\"."
						+ "\"Month \"" + new Integer(iMonth).toString() + "\" is incorrect.");
			}
			if (iYear != cal.get(Calendar.YEAR)) {
				throw new ValidationException("invalid.prop.date.year", null, "invalid date \"" + s + "\"."
						+ "\nYear \"" + new Integer(iYear).toString() + "\" is incorrect.");
			}
			date = cal.getTime();
		} else {
			DateFormat df;
			if (len > 8) {
				df = PropertyDate.LANG_INDEP_DATE_FORMAT_2;
			} else {
				df = PropertyDate.LANG_INDEP_DATE_FORMAT_1;
			}
			try {
				date = df.parse(sDate);
				if (iMillis > -1) {
					date = new Date(date.getTime() + iMillis);
				}
			} catch (java.text.ParseException e) {
				throw new ValidationException("invalid.prop.date.parse", null, "invalid non parseable date \"" + sDate
						+ "\".");
			}
		}

		return date;
	}

	/**
	 * @param locale
	 *            the Locale
	 * @parame mode the mode
	 * @return a string for the property's value for UI
	 */
	public String toStringGui(final RapidBeansLocale locale) {
		return this.format(locale, DateFormat.MEDIUM, -1);
	}

	/**
	 * format a Date.
	 * 
	 * @param locale
	 *            the locale
	 * @param format
	 *            the format (see class java.text.DateFormat)
	 * @param field
	 *            the field (see class java.text.DateFormat). Set this argument
	 *            to -1 if all field shoud be shown
	 * @return the formatted string
	 */
	public String format(final RapidBeansLocale locale, final int format, final int field) {
		return formatDate(this.value, locale, format, field);
	}

	/**
	 * format a Date.
	 * 
	 * @param locale
	 *            the Locale
	 * @param sFormat
	 *            { DateFormat.SHORT | MEDIUM | LONG }
	 * @param sField
	 *            { DateFormat.DATE_FIELD, MONTH_FIELD, YEAR_FIELD, ...
	 * @return a string for the property's value for UI
	 */
	public String format(final RapidBeansLocale locale, final String sFormat, final String sField) {
		if (this.value == null) {
			return "-";
		}
		int format = -1;
		switch (sFormat.charAt(0)) {
		case 'S':
			if (!sFormat.equals("SHORT")) {
				throw new RapidBeansRuntimeException("Unknown Date Format \"" + sFormat + "\"");
			}
			format = DateFormat.SHORT;
			break;
		case 'M':
			if (!sFormat.equals("MEDIUM")) {
				throw new RapidBeansRuntimeException("Unknown Date Format \"" + sFormat + "\"");
			}
			format = DateFormat.MEDIUM;
			break;
		case 'L':
			if (!sFormat.equals("LONG")) {
				throw new RapidBeansRuntimeException("Unknown Date Format \"" + sFormat + "\"");
			}
			format = DateFormat.LONG;
			break;
		default:
			throw new RapidBeansRuntimeException("Unknown Date Format \"" + sFormat + "\"");
		}

		int field = -1;
		// DATE_FIELD
		// DAY_OF_WEEK_FIELD
		// DAY_OF_WEEK_IN_MONTH_FIELD
		// DAY_OF_YEAR_FIELD
		// DAY_OF_WEEK_IN_MONTH_FIELD
		// DAY_OF_WEEK_IN_MONTH
		// DATE_FIELD
		// DAY_OF_WEEK_FIELD
		// DAY_OF_YEAR_FIELD
		// ERA_FIELD
		// HOUR_OF_DAY0_FIELD
		// HOUR_OF_DAY1_FIELD
		// HOUR0_FIELD
		// HOUR1_FIELD
		// MILLISECOND_FIELD
		// MILLISECOND field
		// MINUTE_FIELD
		// MONTH_FIELD
		// SECOND_FIELD
		// TIMEZONE_FIELD
		// WEEK_OF_MONTH_FIELD
		// WEEK_OF_YEAR_FIELD
		// YEAR_FIELD

		String sfField = sField + "_FIELD";
		switch (sfField.charAt(0)) {
		case 'A':
			if (!sfField.equals("AM_PM_FIELD")) {
				throw new RapidBeansRuntimeException("Unknown date format \"" + sfField + "\"");
			}
			field = DateFormat.AM_PM_FIELD;
			break;
		case 'D':
			if (!sfField.equals("DATE_FIELD")) {
				throw new RapidBeansRuntimeException("Unknown date format \"" + sfField + "\"");
			}
			field = DateFormat.DATE_FIELD;
			break;
		case 'M':
			if (!sfField.equals("MONTH_FIELD")) {
				throw new RapidBeansRuntimeException("Unknown date format \"" + sfField + "\"");
			}
			field = DateFormat.MONTH_FIELD;
			break;
		case 'Y':
			if (!sfField.equals("YEAR_FIELD")) {
				throw new RapidBeansRuntimeException("Unknown date format \"" + sfField + "\"");
			}
			field = DateFormat.YEAR_FIELD;
			break;
		default:
			throw new RapidBeansRuntimeException("Unknown date field \"" + sField + "\"");
		}

		return formatDate(this.value, locale, format, field);
	}

	/**
	 * @param val
	 *            the date value as Date
	 * @param locale
	 *            the Locale
	 * @return a string for the property's value for UI
	 */
	public static String formatDate(final Date val, final RapidBeansLocale locale) {
		return formatDate(val, locale, DateFormat.MEDIUM, -1);
	}

	/**
	 * @param date
	 *            the date to format
	 * @param locale
	 *            the Locale
	 * @param format
	 *            { DateFormat.SHORT | MEDIUM | LONG }
	 * @param field
	 *            { DateFormat.DATE_FIELD, MONTH_FIELD, YEAR_FIELD, ...
	 * @return a string for the property's value for UI
	 */
	public static String formatDate(final Date date, final RapidBeansLocale locale, final int format, final int field) {
		if (date == null) {
			return "-";
		}
		String s = null;
		final DateFormat formatter = DateFormat.getDateInstance(format, locale.getLocale());
		final StringBuffer sb = new StringBuffer();
		if (field > -1) {
			final FieldPosition fp = new FieldPosition(field);
			formatter.format(date, sb, fp);
			s = sb.toString().substring(fp.getBeginIndex(), fp.getEndIndex());
		} else {
			s = formatter.format(date);
		}
		return s;
	}

	/**
	 * tests if two date intervals overlap.
	 * 
	 * @param i1From
	 *            start date of interval 1
	 * @param i1To
	 *            end date of interval 1
	 * @param i2From
	 *            start date of interval 2
	 * @param i2To
	 *            end date of interval 2
	 * 
	 * @return if the intervals overlap or not
	 */
	public static boolean dateIntervalsOverlap(final Date i1From, final Date i1To, final Date i2From, final Date i2To) {
		if (i1From == null) {
			throw new IllegalArgumentException("null value given for i1from.");
		}
		if (i1To == null) {
			throw new IllegalArgumentException("null value given for i1To.");
		}
		if (i2From == null) {
			throw new IllegalArgumentException("null value given for i2From.");
		}
		if (i2To == null) {
			throw new IllegalArgumentException("null value given for i2To.");
		}
		return (((i1From.compareTo(i2From) > 0) && (i1From.compareTo(i2To) < 0)) || (i1To.compareTo(i2From) > 0)
				&& (i1To.compareTo(i2To) < 0));
	}
}
