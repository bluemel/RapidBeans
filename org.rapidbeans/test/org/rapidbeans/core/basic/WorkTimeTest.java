package org.rapidbeans.core.basic;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

import junit.framework.TestCase;

import org.rapidbeans.domain.math.Time;
import org.rapidbeans.test.WorkTime;

/**
 * Unit Test for domain class WorkTime.
 * 
 * @author Martin Bluemel
 */
public class WorkTimeTest extends TestCase {

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
	 * Constructor test: the constructor initializes all date attribute to empty
	 * (null).
	 */
	public void testWorkTime() {
		WorkTime worktime = new WorkTime();
		assertEquals(null, worktime.getFrom());
		assertEquals(null, worktime.getTo());
		assertEquals(null, worktime.getTime());
	}

	/**
	 * setting "day" should be allowed if from is set to the same date.
	 * 
	 * @throws ParseException
	 *             in case of date parsing error
	 */
	public void testSetDay() throws ParseException {
		WorkTime worktime = new WorkTime();
		worktime.setFrom(DFTIME.parse("11.11.2005 04:30:00"));
		assertEquals(DFTIME.parse("11.11.2005 04:30:00"), worktime.getFrom());
	}

	/**
	 * setting from should always adapt day.
	 * 
	 * @throws ParseException
	 *             in case of date parsing error
	 */
	public void testSetFrom() throws ParseException {
		WorkTime worktime = new WorkTime();
		worktime.setFrom(DFTIME.parse("14.10.1964 04:30:00"));
		assertEquals(DFTIME.parse("14.10.1964 04:30:00"), worktime.getFrom());
	}

	/**
	 * setting "to" should recompute time if "from" is set before.
	 * 
	 * @throws ParseException
	 *             in case of date parsing error
	 */
	public void testSetToWithFromSetBefore() throws ParseException {
		WorkTime worktime = new WorkTime();
		assertNull(worktime.getTime());
		worktime.setFrom(DFTIME.parse("14.10.1964 04:30:00"));
		assertNull(worktime.getTime());
		worktime.setTo(DFTIME.parse("14.10.1964 05:31:00"));
		assertEquals(61, ((Time) worktime.getTime()).getMagnitudeLong());
	}

	/**
	 * setting "from" should also recompute time if "to" is set before.
	 * 
	 * @throws ParseException
	 *             in case of date parsing error
	 */
	public void testSetFromWithToSetBefore() throws ParseException {
		WorkTime worktime = new WorkTime();
		assertNull(worktime.getTime());
		worktime.setTo(DFTIME.parse("14.10.1964 05:31:00"));
		assertNull(worktime.getTime());
		worktime.setFrom(DFTIME.parse("14.10.1964 04:30:00"));
		assertEquals(61, ((Time) worktime.getTime()).getMagnitudeLong());
	}
}
