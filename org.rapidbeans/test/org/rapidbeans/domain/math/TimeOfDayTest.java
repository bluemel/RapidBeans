/*
 * Rapid Beans Framework: TimeTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Nov 14, 2005
 */
package org.rapidbeans.domain.math;

import java.math.BigDecimal;

import junit.framework.TestCase;

/**
 * Unit tests for class TimeOfDay.
 * 
 * @author Martin Bluemel
 */
public final class TimeOfDayTest extends TestCase {

	/**
	 * test constructor with string: simple integers with one or two digits will
	 * be interpreted as hours.
	 */
	public void testTimeOfDayHours() {
		TimeOfDay time = new TimeOfDay("2");
		assertEquals(new BigDecimal(2), time.getMagnitude());
		assertSame(UnitTime.h, time.getUnit());
		time = new TimeOfDay("11");
		assertEquals(new BigDecimal(11), time.getMagnitude());
		assertSame(UnitTime.h, time.getUnit());
	}

	/**
	 * test constructor with string: two integers (each one or two digits
	 * separated by ':'.
	 */
	public void testTimeOfDayMinutes() {
		TimeOfDay time = new TimeOfDay("1:59");
		assertEquals(new BigDecimal(60 + 59), time.getMagnitude());
		assertSame(UnitTime.min, time.getUnit());
		time = new TimeOfDay("23:00");
		assertEquals(new BigDecimal(23 * 60), time.getMagnitude());
		assertSame(UnitTime.min, time.getUnit());
	}

	/**
	 * test constructor with string: simple quntity coding coding with magnitude
	 * and unit.
	 */
	public void testTimeOfDayMinutesQuantity() {
		TimeOfDay time = new TimeOfDay("1140 min");
		assertEquals(1140, time.getMagnitudeLong());
		assertEquals(UnitTime.min, time.getUnit());
	}

	/**
	 * test constructor with string.
	 */
	public void testTimeOfDaySeconds() {
		TimeOfDay time = new TimeOfDay("23:02:09");
		assertEquals(new BigDecimal(23 * 3600 + 2 * 60 + 9), time.getMagnitude());
		assertSame(UnitTime.s, time.getUnit());
		time = new TimeOfDay("9:58:12");
		assertEquals(new BigDecimal(9 * 3600 + 58 * 60 + 12), time.getMagnitude());
		assertSame(UnitTime.s, time.getUnit());
	}

	/**
	 * test toString().
	 */
	public void testToStringHours() {
		TimeOfDay time = new TimeOfDay("0");
		assertEquals("0", time.toString());
		time = new TimeOfDay("02");
		assertEquals("2", time.toString());
		time = new TimeOfDay("23");
		assertEquals("23", time.toString());
	}

	/**
	 * test toString().
	 */
	public void testToStringMinutes() {
		TimeOfDay time = new TimeOfDay("23:00");
		assertEquals("23:00", time.toString());
		time = new TimeOfDay("8:51");
		assertEquals("8:51", time.toString());
		time = new TimeOfDay("08:55");
		assertEquals("8:55", time.toString());
	}

	/**
	 * test toString().
	 */
	public void testToStringSeconds() {
		TimeOfDay time = new TimeOfDay("23:02:09");
		assertEquals("23:02:09", time.toString());
		time = new TimeOfDay("9:58:12");
		assertEquals("9:58:12", time.toString());
	}
}
