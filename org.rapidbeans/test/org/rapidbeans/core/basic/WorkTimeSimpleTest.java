package org.rapidbeans.core.basic;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.rapidbeans.domain.math.Time;
import org.rapidbeans.test.WorkTimeSimple;

/**
 * Unit Test for domain class WorkTimeSimple.
 * 
 * @author Martin Bluemel
 */
public class WorkTimeSimpleTest {

	/**
	 * Date formatter.
	 */
	static final DateFormat DFDATE = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);

	/**
	 * Date formatter.
	 */
	static final DateFormat DFTIME = DateFormat
			.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMAN);

	/**
	 * Date formatter.
	 */
	static final DateFormat DFTIMELONG = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG,
			Locale.GERMAN);

	/**
	 * Constructor test: the constructor initializes all date attribute to empty
	 * (null).
	 */
	@Test
	public void testWorkTime() throws SecurityException, NoSuchFieldException, ParseException {
		WorkTimeSimple worktime = new WorkTimeSimple();
		Assert.assertEquals(DFTIME.parse("14.10.1964 00:01:00"), worktime.getFrom());
		Assert.assertEquals(null, worktime.getTo());
		Assert.assertEquals(null, worktime.getTime());
	}

	/**
	 * setting from should always adapt day.
	 */
	@Test
	public void testSetFrom() throws ParseException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		WorkTimeSimple worktime = new WorkTimeSimple();
		worktime.setFrom(DFTIME.parse("14.10.1964 04:30:00"));
		Assert.assertEquals(DFTIME.parse("14.10.1964 04:30:00"), worktime.getFrom());
		Assert.assertEquals(DFTIME.parse("14.10.1964 04:30:00"), worktime.getPropValue("from"));
		Assert.assertEquals(DFTIME.parse("14.10.1964 04:30:00"), worktime.getProperty("from").getValue());
	}

	/**
	 * setting "to" should recompute time if "from" is set before.
	 */
	@Test
	public void testSetToWithFromSetBefore() throws ParseException {
		WorkTimeSimple worktime = new WorkTimeSimple();
		Assert.assertNull(worktime.getTime());
		worktime.setFrom(DFTIME.parse("14.10.1964 04:30:00"));
		Assert.assertEquals(DFTIME.parse("14.10.1964 04:30:00"), worktime.getFrom());
		Assert.assertNull(worktime.getTime());
		worktime.setTo(DFTIME.parse("14.10.1964 05:31:00"));
		Assert.assertEquals(DFTIME.parse("14.10.1964 05:31:00"), worktime.getTo());
		Assert.assertEquals(61, ((Time) worktime.getTime()).getMagnitudeLong());
	}

	/**
	 * setting "from" should also recompute time if "to" is set before.
	 */
	@Test
	public void testSetFromWithToSetBefore() throws ParseException {
		WorkTimeSimple worktime = new WorkTimeSimple();
		worktime.setPropValue("from", null);
		Assert.assertNull(worktime.getTime());
		worktime.setTo(DFTIME.parse("14.10.1964 05:31:00"));
		Assert.assertNull(worktime.getTime());
		worktime.setFrom(DFTIME.parse("14.10.1964 04:30:00"));
		Assert.assertEquals(61, ((Time) worktime.getTime()).getMagnitudeLong());
	}
}
