package org.rapidbeans.core.util;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.config.ConfigApplication;

public class NumberFormatTest extends TestCase {

	private Application app = new Application();

	private ConfigApplication config = new ConfigApplication();

	private RapidBeansLocale locEn = new RapidBeansLocale("en");

	private RapidBeansLocale locDe = new RapidBeansLocale("de");

	public void setUp() {
		app.setConfiguration(this.config);
		app.getConfiguration().setRootpackage("org.rapidbeans");
		locEn.init(app);
		locDe.init(app);
	}

	public void testFormatSimple() {
		assertEquals("20.00", NumberFormat.format(
				new BigDecimal("20"), locEn, "##.00"));
		assertEquals("20,00", NumberFormat.format(
				new BigDecimal("20"), locDe, "##.00"));
		assertEquals("20.00", NumberFormat.format(
				new Integer(20), locEn, "##.00"));
		assertEquals("20,00", NumberFormat.format(
				new Integer(20), locDe, "##.00"));
		assertEquals("20.00", NumberFormat.format(
				new Long(20), locEn, "##.00"));
		assertEquals("20,00", NumberFormat.format(
				new Long(20), locDe, "##.00"));
	}

	public void testFormatUpperBoundaries() {
		assertEquals("20.00", NumberFormat.format(
				new BigDecimal("20"), locEn, "##.00"));
		assertEquals("20,00", NumberFormat.format(
				new BigDecimal("20"), locDe, "##.00"));
		assertEquals("2111222333.00", NumberFormat.format(
				new Integer(2111222333), locEn, "##########.00"));
		assertEquals("2111222333,00", NumberFormat.format(
				new Integer(2111222333), locDe, "##########.00"));
		assertEquals("9111222333444555666.00", NumberFormat.format(
				new Long(9111222333444555666L), locEn, "###################.00"));
		assertEquals("9111222333444555666,00", NumberFormat.format(
				new Long(9111222333444555666L), locDe, "###################.00"));
	}

	public void testFormatBDAfterDecimalDigitsAndRounding() {
		assertEquals("20.00", NumberFormat.format(new BigDecimal("19.995000000"), locEn, "##.00"));
	}

	public void testFormatBDGerman() {
		assertEquals("20,00", NumberFormat.format(new BigDecimal("20"), locDe, "##.00"));
	}

	/**
	 * Old tests.
	 */
	public void testFormatDouble() {
		// normal (default Locale: German)
		testFormat("00.00", 12.34, "12,34", true);
		testFormat("#0.00", 12.34, "12,34", true);
		testFormat("000.00", 12.34, "012,34", true);
		testFormat("#00.00", 12.344, " 12,34", true);
		testFormat("000.00", 12.344, "012,34", true);
		testFormat("##0.00##", 12.344, " 12,344 ", true);
		//        testFormat("#,###,###,##0.00 EUR", 1000000000.1111, "1.000.000.000,11 EUR", true);
		//        testFormat("##,##,##,##,#0.00 EUR", 1000000000.1111, "10.00.00.00.00,11 EUR", true);
		//        testFormat("##,###,##0", 70050020, "70.050.020", true);
		//        testFormat("##,##,##,#0", 70050020, "70.05.00.20", true);
		//        testFormat("##,###,##0", 70050020, "70.050.020", true);
		//        testFormat("###,###,#0", 70050020, "700.500.20", false);
		//        testFormat("#,###,###,##0.00 EUR", 1.1111, "1,11 EUR", true);
		//        testFormat("0.000E00", 1000000000.1111, "1,000E09", true);

		// runden
		//        testFormat("##0.00", 12.346, "12,35", true);
		testFormat("#0.00", 12.344, "12,34", true);
		//        testFormat("##0.00", 12.345, " 12,34", true);
		//        testFormat("#0.00", 12.34500000000001, "12,35", true);
	}

	/**
	 * test helper for old format tests.
	 * 
	 * @param pattern
	 *            the format pattern
	 * @param d
	 *            the double to format
	 * @param resExp
	 *            the expected result
	 * @param testOk
	 *            the expected result
	 */
	private void testFormat(String pattern, double d, String resExp, boolean testOk) {
		String s = NumberFormat.format(d, pattern, '.', ',');
		assertTrue("\npattern: \"" + pattern + "\", " + "double: " + d + ", " + "expected: \"" + resExp + "\", "
				+ "got: \"" + s + "\"", (testOk && s.equals(resExp)) || (!testOk && !s.equals(resExp)));
	}
}
