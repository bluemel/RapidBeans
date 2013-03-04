package org.rapidbeans.core.basic;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.rapidbeans.test.TestBeanSimple;

/**
 * Unit Test for domain class TestBeanSimple.
 * 
 * @author Martin Bluemel
 */
public class TestBeanSimpleTest {

	/**
	 * Date formatter.
	 */
	static final DateFormat DFDATE = DateFormat.getDateInstance(
			DateFormat.MEDIUM, Locale.GERMAN);

	/**
	 * Date formatter.
	 */
	static final DateFormat DFTIME = DateFormat.getDateTimeInstance(
			DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMAN);

	/**
	 * Date formatter.
	 */
	static final DateFormat DFTIMELONG = DateFormat.getDateTimeInstance(
			DateFormat.MEDIUM, DateFormat.LONG, Locale.GERMAN);

	/**
	 * Constructor test:
	 * the constructor initializes all date attribute to empty (null).
	 */
	@Test
	public void testStringProperty() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals("Ismaning", bean.getCity());
		bean.setCity("Mannheim");
		Assert.assertEquals("Mannheim", bean.getCity());
	}

	@Test
	public void testDateProperty() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(DFDATE.parse("14.10.1964"), bean.getDateofbirth());
		bean.setDateofbirth(DFDATE.parse("15.10.1964"));
		Assert.assertEquals(DFDATE.parse("15.10.1964"), bean.getDateofbirth());
	}

	@Test
	public void testBooleanProperty() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(true, bean.getMarried());
		bean.setMarried(false);
		Assert.assertEquals(false, bean.getMarried());
	}
}
