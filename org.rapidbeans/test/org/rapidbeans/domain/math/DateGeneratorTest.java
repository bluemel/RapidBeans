/*
 * Rapid Beans Framework: DateGeneratorTest.java
 *
 * created on 14.01.2007
 *
 * (c) Martin Bluemel, 2007
 */
package org.rapidbeans.domain.math;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

/**
 *
 * @author Martin Bluemel
 *
 */
public class DateGeneratorTest extends TestCase {

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

//    /**
//     * Date formatter.
//     */
//    static final DateFormat DFTIMELONG = DateFormat.getDateTimeInstance(
//            DateFormat.MEDIUM, DateFormat.LONG, Locale.GERMAN);

    /**
     * normal case.
     * @throws ParseException in case of dateparsing problems
     */
    public void testGenerateNormal() throws ParseException {
        DateGenerator testGenerator = new DateGenerator();
        ArrayList<Date> dates = testGenerator.generateDays(
                DFDATE.parse("11.11.2005"), DFDATE.parse("15.11.2005"));
        assertNotNull(dates);
        assertEquals(5, dates.size());
        assertEquals(DFDATE.parse("11.11.2005"), dates.get(0));
        assertEquals(DFDATE.parse("12.11.2005"), dates.get(1));
        assertEquals(DFDATE.parse("13.11.2005"), dates.get(2));
        assertEquals(DFDATE.parse("14.11.2005"), dates.get(3));
        assertEquals(DFDATE.parse("15.11.2005"), dates.get(4));
    }

    /**
     * boundary condition.
     *
     * @throws ParseException in case of dateparsing problems
     */
    public void testGenerateFromEqualsTo() throws ParseException {
        DateGenerator testGenerator = new DateGenerator();
        ArrayList<Date> dates = testGenerator.generateDays(
                DFDATE.parse("11.11.2005"), DFDATE.parse("11.11.2005"));
        assertNotNull(dates);
        assertEquals(1, dates.size());
        assertEquals(DFDATE.parse("11.11.2005"), dates.get(0));
    }

    /**
     * robustness test.
     * @throws ParseException in case of dateparsing problems
     */
    public void testGenerateInputDatesNotExact() throws ParseException {
        DateGenerator testGenerator = new DateGenerator();
        ArrayList<Date> dates = testGenerator.generateDays(
                DFTIME.parse("11.11.2005 23:59:59"),
                DFTIME.parse("15.11.2005 00:00:00"));
        assertNotNull(dates);
        assertEquals(5, dates.size());
        assertEquals(DFDATE.parse("11.11.2005"), dates.get(0));
        assertEquals(DFDATE.parse("12.11.2005"), dates.get(1));
        assertEquals(DFDATE.parse("13.11.2005"), dates.get(2));
        assertEquals(DFDATE.parse("14.11.2005"), dates.get(3));
        assertEquals(DFDATE.parse("15.11.2005"), dates.get(4));
    }

    /**
     * failure condition test: from date smaller that to date.
     *
     * @throws ParseException in case of dateparsing problems
     */
    public void testGenerateFromSmallerTo() throws ParseException {
        DateGenerator testGenerator = new DateGenerator();
        try {
            testGenerator.generateDays(
                DFDATE.parse("15.11.2005"), DFDATE.parse("11.11.2005"));
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}

