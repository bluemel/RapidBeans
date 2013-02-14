/*
 * Rapid Beans Framework: NumberFormat.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/01/2000
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

package org.rapidbeans.core.util;

import java.math.BigDecimal;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.UtilException;

/**
 * Provides sophisticated localized formatting of numbers.
 * 
 * @author Martin Bl�mel
 */
public class NumberFormat {

	// Format pattern string ---------------------------

	/**
	 * A format pattern consists in following characters:
	 */
	public static final char PATTERN_FIX_DIGIT_CHAR = '0';

	public static final char PATTERN_NON_NULL_DIGIT_CHAR = '#';

	public static final char PATTERN_SEPARATOR_CHAR = ',';

	public static final char PATTERN_DECIMAL_CHAR = '.';

	public static final char PATTERN_EXPONENT_CHAR = 'E';

	public static final int CHECK_MODE_DECIMAL = 0;

	public static final int CHECK_MODE_FLOATING_POINT = 1;

	private static final int STATE_START = 0;

	private static final int STATE_AFTER_SIGN = 1;

	private static final int STATE_BEFORE_DECIMAL = 2;

	private static final int STATE_AFTER_DECIMAL_ONE = 3;

	private static final int STATE_AFTER_DECIMAL_N = 4;

	private static final int STATE_AFTER_DECIMAL_WSPACE = 5;

	/**
	 * normalize is supposed to return a normalized string representation of a
	 * decimal number with the following Syntax: checkMode:<br/>
	 * 
	 * 1) checkMode = decimal:<br/>
	 * <code>&lt;wspace&gt;* [&lt;sign char&gt;&lt;wspace&gt;*] &lt;digit&gt; {&lt;digit&gt;|&lt;separator&gt;}* &lt;decimal&gt; &lt;digit&gt;*
	 * &lt;wspace&gt;* checkMode</code><br/>
	 * 
	 * 2) maybe later extend for floating point strings<br/>
	 * - eliminate leading and trailing whitespace characters<br/>
	 * - eliminate positive sign '+'<br/>
	 * - eliminate whitespace characters after sign<br/>
	 * - eliminate separator characters<br/>
	 * - eliminate leading zeros up to the last position before decimal<br/>
	 * - eliminate trailing zeros up to the first position after decimal<br/>
	 * 
	 * The input separator and the decimal char can be given language dependent.
	 * The output is always language independent.
	 */
	public static String normalize(String sIn) throws UtilException {
		return normalize(sIn, CHECK_MODE_DECIMAL, PATTERN_SEPARATOR_CHAR, PATTERN_DECIMAL_CHAR);
	}

	public static String normalize(String sIn, int checkMode) throws UtilException {
		return normalize(sIn, checkMode, PATTERN_SEPARATOR_CHAR, PATTERN_DECIMAL_CHAR);
	}

	public static String normalize(String sIn, int checkMode, char separatorChar, char decimalChar)
			throws UtilException {
		int len = sIn.length();
		char c;
		StringBuffer buf = new StringBuffer();
		int i = 0;
		int state = STATE_START;
		if (checkMode == CHECK_MODE_DECIMAL) {
			for (i = 0; i < len; i++) {
				c = sIn.charAt(i);
				switch (state) {
				// ---------------------------------------------------------------------
				case STATE_START:
					// ---------------------------------------------------------------------
					// recognize the begin of the pre decimal part
					if (c >= '1' && c <= '9') {
						buf.append(c);
						state = STATE_BEFORE_DECIMAL;
					}
					// eliminate leading zeros except on the last position
					// before decimal
					else if (c == '0') {
						if (sIn.charAt(i + 1) == decimalChar) {
							buf.append(c);
							state = STATE_BEFORE_DECIMAL;
						}
					}
					// accept a leading decimal char but prepend a zero
					else if (c == decimalChar) {
						buf.append('0');
						state = STATE_AFTER_DECIMAL_ONE;
					}
					// recognize a positive sign and filter it
					else if (c == '+') {
						state = STATE_AFTER_SIGN;
					}
					// recognize a negative sign
					else if (c == '-') {
						buf.append(c);
						state = STATE_AFTER_SIGN;
					}
					// eliminate leading whitespaces
					else if (c == ' ' || c == '\t' || c == '\n') {
						; // filter leading whitespace
					} else {
						throw new UtilException("wrong char '" + c + "'");
					}
					break;

				// ---------------------------------------------------------------------
				case STATE_AFTER_SIGN:
					// ---------------------------------------------------------------------
					// recognize the begin of the pre decimal part
					if (c >= '1' && c <= '9') {
						buf.append(c);
						state = STATE_BEFORE_DECIMAL;
					}
					// eliminate leading zeros except on the last position
					// before decimal
					else if (c == '0') {
						if (sIn.charAt(i + 1) == decimalChar) {
							buf.append(c);
							state = STATE_BEFORE_DECIMAL;
						}
					}
					// accept a leading decimal char but prepend a zero
					else if (c == decimalChar) {
						buf.append('0');
						state = STATE_AFTER_DECIMAL_ONE;
					}
					// eliminate leading whitespaces
					else if (c == ' ' || c == '\t' || c == '\n') {
						; // filter leading whitespace
					} else {
						throw new UtilException("wrong char '" + c + "'");
					}
					break;

				// ---------------------------------------------------------------------
				case STATE_BEFORE_DECIMAL:
					// ---------------------------------------------------------------------
					if (c >= '0' && c <= '9') {
						buf.append(c);
					} else if (c == separatorChar) {
						; // filter separators
					} else if (c == decimalChar) {
						buf.append(PATTERN_DECIMAL_CHAR);
						state = STATE_AFTER_DECIMAL_ONE;
					} else {
						throw new UtilException("wrong char '" + c + "'");
					}
					break;

				// ---------------------------------------------------------------------
				case STATE_AFTER_DECIMAL_ONE:
					// ---------------------------------------------------------------------
					// only accept a digit here
					if (c >= '0' && c <= '9') {
						buf.append(c);
						state = STATE_AFTER_DECIMAL_N;
					} else {
						throw new UtilException("wrong char '" + c + "'");
					}
					break;

				// ---------------------------------------------------------------------
				case STATE_AFTER_DECIMAL_N:
					// ---------------------------------------------------------------------
					if (c >= '0' && c <= '9') {
						buf.append(c);
					}
					// recognize the first trailing whitespace (filter it)
					else if (c == ' ' || c == '\t' || c == '\n') {
						state = STATE_AFTER_DECIMAL_WSPACE;
					} else {
						throw new UtilException("wrong char '" + c + "'");
					}
					break;

				// ---------------------------------------------------------------------
				case STATE_AFTER_DECIMAL_WSPACE:
					// ---------------------------------------------------------------------
					// filter trailing whitespaces
					if (c == ' ' || c == '\t' || c == '\n') {
						;
					} else {
						throw new UtilException("wrong char '" + c + "'");
					}
					break;
				}
			}
		}
		String ret = buf.toString();
		return ret;
	}

	public static String format(double d, String numberFormatString, char separatorChar, char decimalChar) {
		return format(new Double(d).toString(), numberFormatString, separatorChar, decimalChar);
	}

	/**
	 * Formats a BigDecimal number according to the given number format string.
	 * 
	 * @param d
	 *            the number a BigDecimal
	 * @param locale
	 *            the locale
	 * @param numberFormatString
	 *            the number format string
	 * 
	 * @return the formatted number string
	 */
	public static String format(final BigDecimal d,
			final RapidBeansLocale locale,
			final String numberFormatString) {
		return format(d.toString(), numberFormatString,
				locale.getStringGui("number.format.char.separator").charAt(0),
				locale.getStringGui("number.format.char.decimal").charAt(0));
	}

	/**
	 * Formats an integer number according to the given number format string.
	 * 
	 * @param i
	 *            the number
	 * @param locale
	 *            the locale
	 * @param numberFormatString
	 *            the number format string
	 * 
	 * @return the formatted number string
	 */
	public static String format(final int i,
			final RapidBeansLocale locale,
			final String numberFormatString) {
		return format(Integer.toString(i), numberFormatString,
				locale.getStringGui("number.format.char.separator").charAt(0),
				locale.getStringGui("number.format.char.decimal").charAt(0));
	}

	/**
	 * Formats a long integer number according to the given number format string.
	 * 
	 * @param l
	 *            the long integer number
	 * @param locale
	 *            the locale
	 * @param numberFormatString
	 *            the number format string
	 * 
	 * @return the formatted number string
	 */
	public static String format(final long l,
			final RapidBeansLocale locale,
			final String numberFormatString) {
		return format(Long.toString(l), numberFormatString,
				locale.getStringGui("number.format.char.separator").charAt(0),
				locale.getStringGui("number.format.char.decimal").charAt(0));
	}

	/**
	 * Formats a number string according to the given number format string.
	 * 
	 * @param sIn
	 *            the string containing the number
	 * 
	 * @param numberFormatString
	 * @param separatorChar
	 * @param decimalChar
	 * 
	 * @return the formatted number string
	 */
	public static String format(String sIn, String numberFormatString, char separatorChar, char decimalChar) {
		if (numberFormatString == null || numberFormatString.equals("")) {
			throw new UtilException("Illegal argument numberFormatString is empty");
		}

		// create a buffer for the resulting String an initially fill it
		// with the format string
		StringBuffer buf = new StringBuffer(numberFormatString);

		// position one before the decimal sign or at the last digit
		int lenIn = sIn.length();
		int posInStart, posFormatStart, posIn, posFormat;
		int posDecimalCharIn = sIn.indexOf(PATTERN_DECIMAL_CHAR);
		if (posDecimalCharIn == -1) {
			posInStart = lenIn - 1;
		} else {
			posInStart = posDecimalCharIn - 1;
		}
		final int lenBuf = numberFormatString.length();
		boolean decimalFoundFormat = false;
		int posDecimalCharFormat = numberFormatString.indexOf(PATTERN_DECIMAL_CHAR);
		if (posDecimalCharFormat == -1) {
			posFormatStart = lenBuf - 1;
		} else {
			posFormatStart = posDecimalCharFormat - 1;
			decimalFoundFormat = true;
		}

		if (decimalFoundFormat) {
			buf.setCharAt(posDecimalCharFormat, decimalChar);
		}

		// fill the buffer before the decimal sign
		char c, cf;
		posIn = posInStart;
		posFormat = posFormatStart;
		while (posFormat >= 0) {
			if (posIn >= 0) {
				c = sIn.charAt(posIn);
			} else {
				c = ' ';
			}
			cf = numberFormatString.charAt(posFormat);
			if (cf == PATTERN_FIX_DIGIT_CHAR) {
				if (c == ' ') {
					c = '0';
				}
				buf.setCharAt(posFormat, c);
				posIn--;
			} else if (cf == PATTERN_NON_NULL_DIGIT_CHAR) {
				buf.setCharAt(posFormat, c);
				posIn--;
			} else if (cf == PATTERN_SEPARATOR_CHAR) {
				buf.setCharAt(posFormat, separatorChar);
			} else {
				throw new UtilException("unexpected char'" + cf + "' at pos " + posFormat);
			}
			posFormat--;
		}

		// fill the buffer after the decimal sign
		if (decimalFoundFormat) {
			posIn = posInStart + 2;
			posFormat = posFormatStart + 2;
			while (posFormat < lenBuf) {
				if (posIn < lenIn) {
					c = sIn.charAt(posIn);
				} else {
					c = ' ';
				}
				cf = numberFormatString.charAt(posFormat);
				if (cf == PATTERN_FIX_DIGIT_CHAR) {
					if (c == ' ') {
						c = '0';
					}
					buf.setCharAt(posFormat, c);
					posIn++;
				} else if (cf == PATTERN_NON_NULL_DIGIT_CHAR) {
					buf.setCharAt(posFormat, c);
					posIn++;
				} else if (cf == PATTERN_SEPARATOR_CHAR) {
					buf.setCharAt(posFormat, separatorChar);
				} else {
					throw new UtilException("unexpected char'" + cf + "' at pos "
							+ posFormat);
				}
				posFormat++;
			}
		}
		// round
		String ret = buf.toString();
		if (posIn < lenIn) {
			c = sIn.charAt(posIn);
			if (c >= '5' && c <= '9') {
				// round up
				char[] ca = ret.toCharArray();
				boolean round = true;
				for (int pos = ca.length - 1; round && pos >= 0; pos--) {
					c = ca[pos];
					if (c == '9') {
						ca[pos] = '0';
						round = true;
					} else if (c >= '0' && c <= '8') {
						ca[pos] = (char) (ca[pos] + 1);
						round = false;
					}
				}
				ret = new String(ca);
			}
		}
		return ret;
	}
}
