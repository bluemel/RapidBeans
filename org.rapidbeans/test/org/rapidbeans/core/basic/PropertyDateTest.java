/*
 * Rapid Beans Framework: PropertyDateTest
 *
 * Copyright Martin Bluemel, 2008
 *
 * Dec 10, 2005
 */
package org.rapidbeans.core.basic;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

import org.rapidbeans.core.exception.PropValueNullException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypePropertyDate;
import org.rapidbeans.core.util.XmlNode;

/**
 * Unit Tests for class PropertyDate.
 *
 * @author Martin Bluemel
 */
public class PropertyDateTest extends TestCase {

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
     * test construction of a Date property.
     */
    public void testBBPropDate() {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\"/>");
        assertNotNull(prop);
        assertNull(prop.getValue());
    }

    /**
     * test init a default Date value and get it.
     * @throws java.text.ParseException if parsing fails
     */
    public void testDeafultAndGetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"20051111\"/>");
        assertEquals(DFDATE.parse("11.11.2005"), prop.getValue());
    }

    /**
     * test init a default Date value and get it.
     * @throws java.text.ParseException if parsing fails
     */
    public void testDeafultAndGetValueNull() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\"/>");
        assertNull(prop.getValue());
    }

    /**
     * test init a default Date value and get it.
     * @throws java.text.ParseException if parsing fails
     */
    public void testDeafultValueInvalid() throws java.text.ParseException {
        try {
            this.createDateProperty(
                "<property name=\"test\" maxval=\"20051110\" default=\"20051111\"/>");
        } catch (ValidationException e) {
            assertTrue(true);
        }
    }

    /**
     * test immutability.
     * proove that Date is  n o t  immutable
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testImmutabilityDateMutable() throws java.text.ParseException {

        DumbClass dumb = new DumbClass();
        // originally dumb has the default value
        assertEquals(DFTIME.parse("14.10.1964 04:30:00"), dumb.getDate());
        // then I mute the private field date by just using the getter
        dumb.getDate().setTime(DFTIME.parse("01.01.1980 00:00:00").getTime());
        assertEquals(DFTIME.parse("01.01.1980 00:00:00"), dumb.getDate());

    }

    /**
     * test immutability.
     * proove that our PropertyDate is immutable after getValue.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testImmutabilityGet() throws java.text.ParseException {

        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " default=\"19700101\"/>");
        // originally prop has the default value
        assertEquals(DFTIME.parse("01.01.1970 00:00:00"), prop.getValue());
        // then I try to mute the private field date by just using the getter
        ((Date) prop.getValue()).setTime(DFTIME.parse("01.01.1980 00:00:00").getTime());
        // but our prop stays the same
        assertEquals(DFTIME.parse("01.01.1970 00:00:00"), prop.getValue());
    }

    /**
     * test immutability.
     * proove that our PropertyDate is immutable after setValue
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testImmutability() throws java.text.ParseException {
        PropertyDate prop2 = this.createDateProperty(
                "<property name=\"test\"/>");
        Date date2 = DFTIME.parse("04.01.2006 00:00:00");
        prop2.setValue(date2);
        date2.setTime(0);
        assertEquals(DFTIME.parse("04.01.2006 00:00:00"), prop2.getValue());
    }

    /**
     * Prooves misdesign of mutable Java value class Date.
     *
     * @author Martin Bluemel
     */
    private class DumbClass {

        /**
         * the date attribute.
         */
        private Date date;

        /**
         * @return the date attribute
         */
        public Date getDate() {
            return this.date;
        }

        /**
         * constructor.
         * @throws java.text.ParseException if parsing fails
         */
        public DumbClass() throws java.text.ParseException {
            this.date = DFTIME.parse("14.10.1964 04:30:00");
        }
    }
    /**
     * test get a long value.
     * @throws java.text.ParseException if parsing fails
     */
    public void testGetValueLong() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"20051111\"/>");
        assertEquals(DFTIME.parse("11.11.2005 00:00:00").getTime(),
                prop.getValueTime());
    }

    /**
     * test get a long value of a Date Property with undefined value (null).
     */
    public void testGetValueLongNull() {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\"/>");
        try {
            prop.getValueTime();
            fail("expected PropValueNullException");
        } catch (PropValueNullException e) {
            assertTrue(true);
        }
    }

    /**
     * test get a String value.
     */
    public void testGetValueString() {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"20051111\"/>");
        assertEquals("20051111", prop.toString());
    }

    /**
     * test get a null String from a property with an undefined value (null).
     */
    public void testGetValueStringNull() {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\"/>");
        assertNull(prop.toString());
    }

    /**
     * test get a null String from a property with an undefined value (null).
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testGetValueDefault() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"19700101\"/>");
        assertEquals(DFDATE.parse("01.01.1970"), prop.getValue());
    }

    /**
     * successful simple set value of a Date Property.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testSetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " maxval=\"20101231\" minval=\"19000101\""
                + "/>");
        prop.setValue(DFDATE.parse("12.12.1999"));
        assertEquals(DFDATE.parse("12.12.1999"), prop.getValue());
    }

    /**
     * successful set value of a Date Property with a String.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testSetValueString() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " maxval=\"20101231\" minval=\"19000101\""
                + " precision=\"second\"/>");
        prop.setValue("19991212");
        assertEquals(DFDATE.parse("12.12.1999"), prop.getValue());
    }


    /**
     * successful set value of a Date Property with a String.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testSetValueLong() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " maxval=\"20101231\" minval=\"19000101\""
                + " precision=\"second\"/>");
        prop.setValue(DFDATE.parse("12.12.1999").getTime());
        assertEquals(DFDATE.parse("12.12.1999"), prop.getValue());
    }

    /**
     * failure scenario: set value of a Date Property greater than max value.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testValidateTooGreat() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " maxval=\"20101231\" minval=\"19000101\""
                + " precision=\"second\"/>");
        try {
            prop.validate(DFTIME.parse("31.12.2010 00:00:01"));
            fail("expected ValidationException");
        } catch (ValidationException e) {
            assertTrue(true);
        }
    }

    /**
     * set value of a Date Property exactly max value.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testValidateMaxBoundaryExactly() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " maxval=\"20101231\" minval=\"19000101\""
                + " precision=\"second\"/>");
        prop.validate(DFTIME.parse("31.12.2010 00:00:00"));
        // if we get no validation exception it's O. K.
    }

    /**
     * failure scenario: set value of a Date Property smaller than min value.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testValidateTooSmall() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " maxval=\"20101231\" minval=\"19000101\""
                + " precision=\"second\"/>");
        try {
            prop.validate(DFTIME.parse("31.12.1899 23:59:59"));
            fail("expected ValidationException");
        } catch (ValidationException e) {
            assertTrue(true);
        }
    }

    /**
     * set value of a Date Property exactly min value.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testValidateMinBoundaryExactly() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " maxval=\"20101231\" minval=\"19000101\""
                + " precision=\"second\""
                + "/>");
        Date val = DFTIME.parse("01.01.1900 00:00:00");
        prop.validate(val);
        // if we get no validation exception it's O. K.
    }

    /**
     * test convertValue with a Date object.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testConvertValueDate() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + "/>");
        assertEquals(DFDATE.parse("22.12.2005"), prop.convertValue(DFDATE.parse("22.12.2005")));
    }

    /**
     * test convertValue with a primitive long value.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testConvertValueLongPrimitive() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + "/>");
        assertEquals(DFDATE.parse("22.12.2005"), prop.convertValue(DFDATE.parse("22.12.2005").getTime()));
    }

    /**
     * test convertValue with a Long object.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testConvertValueLong() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + "/>");
        assertEquals(DFDATE.parse("22.12.2005"), prop.convertValue(
                new Long(DFDATE.parse("22.12.2005").getTime())));
    }

    /**
     * test convertValue with a String object.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testConvertValueString() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + "/>");
        assertEquals(DFDATE.parse("22.12.2005"), prop.convertValue("20051222"));
    }

    /**
     * test convertValue with an invalid object.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testConvertValueInvalid() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + "/>");
        try {
            prop.convertValue(new Integer(0));
        } catch (ValidationException e) {
            assertTrue(true);
        }
    }

    /**
     * test precision "year" get value.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionYearGetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"2005\""
                + " precision=\"year\""
                + "/>");
        assertEquals(DFTIME.parse("01.01.2005 00:00:00"), prop.getValue());
    }

    /**
     * test precision "year" get value.
     */
    public void testPrecisionYearGetString() {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"2005\""
                + " precision=\"year\""
                + "/>");
        assertEquals("2005", prop.toString());
    }

    /**
     * test precision "year" set value happy day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionYearSetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"year\""
                + "/>");
        prop.setValue(DFTIME.parse("01.01.2005 23:59:59"));
        assertEquals(DFTIME.parse("01.01.2005 00:00:00"), prop.getValue());
    }

    /**
     * test precision "year" set value happy day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionYearSetValueStringExact() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"year\""
                + "/>");
        prop.setValue("2005");
        assertEquals(DFTIME.parse("01.01.2005 00:00:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1.2005 00:00:00 of the year.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionYearSetValueStringMorePrecise() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"year\""
                + "/>");
        prop.setValue("20050202102030");
        assertEquals(DFTIME.parse("01.01.2005 00:00:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1. of the year.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionYearSetValueTooPreciseDate() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"year\""
                + "/>");
        prop.setValue(DFTIME.parse("01.01.2005 12:24:11"));
        assertEquals(DFTIME.parse("01.01.2005 00:00:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1. of the year.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionYearSetValueTooPreciseLong() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"year\""
                + "/>");
        prop.setValue(DFTIME.parse("01.01.2005 12:24:11").getTime());
        assertEquals(DFTIME.parse("01.01.2005 00:00:00"), prop.getValue());
    }

    /**
     * test precision "month" get value.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMonthGetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"200406\""
                + " precision=\"month\""
                + "/>");
        assertEquals(DFTIME.parse("01.06.2004 00:00:00"), prop.getValue());
    }

    /**
     * test precision "month" get value.
     */
    public void testPrecisionMonthGetString() {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"200511\""
                + " precision=\"month\""
                + "/>");
        assertEquals("200511", prop.toString());
    }

    /**
     * test precision "month" set value happy day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMonthSetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"month\""
                + "/>");
        prop.setValue(DFTIME.parse("01.08.2222 23:59:59"));
        assertEquals(DFTIME.parse("01.08.2222 00:00:00"), prop.getValue());
    }

    /**
     * test precision "month" set value happy day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMonthSetValueStringExact() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"month\""
                + "/>");
        prop.setValue("178907");
        assertEquals(DFTIME.parse("01.07.1789 00:00:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1.2005 00:00:00 of the month.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMonthSetValueStringMorePrecise() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"month\""
                + "/>");
        prop.setValue("20050202102030");
        assertEquals(DFTIME.parse("01.02.2005 00:00:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1. of the month.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMonthSetValueTooPreciseDate() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"month\""
                + "/>");
        prop.setValue(DFTIME.parse("01.07.2010 12:24:11"));
        assertEquals(DFTIME.parse("01.07.2010 00:00:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1. of the month.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMonthSetValueTooPreciseLong() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"month\""
                + "/>");
        prop.setValue(DFTIME.parse("11.11.1111 12:24:11").getTime());
        assertEquals(DFTIME.parse("01.11.1111 00:00:00"), prop.getValue());
    }

    /**
     * test precision "day" get value default.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionDayGetValueDefault() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"20050303\""
                + "/>");
        assertEquals(DFTIME.parse("03.03.2005 00:00:00"), prop.getValue());
    }

    /**
     * test precision "day" get value.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionDayGetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"20050102\""
                + " precision=\"day\""
                + "/>");
        assertEquals(DFTIME.parse("02.01.2005 00:00:00"), prop.getValue());
    }

    /**
     * test precision "day" get value.
     */
    public void testPrecisionDayGetString() {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"20050303\""
                + " precision=\"day\""
                + "/>");
        assertEquals("20050303", prop.toString());
    }

    /**
     * test precision "day" set value happy day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionDaySetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"day\""
                + "/>");
        prop.setValue(DFTIME.parse("04.03.2005 23:59:59"));
        assertEquals(DFTIME.parse("04.03.2005 00:00:00"), prop.getValue());
    }

    /**
     * test precision "day" set value happy day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionDaySetValueStringExact() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"day\""
                + "/>");
        prop.setValue("20050721");
        assertEquals(DFTIME.parse("21.07.2005 00:00:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1.2005 00:00:00 of the day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionDaySetValueStringMorePrecise() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"day\""
                + "/>");
        prop.setValue("20050202102030");
        assertEquals(DFTIME.parse("02.02.2005 00:00:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1. of the day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionDaySetValueTooPreciseDate1() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"day\""
                + "/>");
        prop.setValue(DFTIME.parse("01.03.2005 12:24:11"));
        assertEquals(DFTIME.parse("01.03.2005 00:00:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1. of the day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionDaySetValueTooPreciseDate() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"day\""
                + "/>");
        prop.setValue(DFTIME.parse("31.03.2005 12:24:11"));
        assertEquals(DFTIME.parse("31.03.2005 00:00:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1. of the day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionDaySetValueTooPreciseLong() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"day\""
                + "/>");
        prop.setValue(DFTIME.parse("31.03.2005 12:24:11").getTime());
        assertEquals(DFTIME.parse("31.03.2005 00:00:00"), prop.getValue());
    }

    /**
     * test precision "hour" get value.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionHourGetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"2005123123\""
                + " precision=\"hour\""
                + "/>");
        assertEquals(DFTIME.parse("31.12.2005 23:00:00"), prop.getValue());
    }

    /**
     * test precision "hour" get value.
     */
    public void testPrecisionHourGetString() {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"2000110104\""
                + " precision=\"hour\""
                + "/>");
        assertEquals("2000110104", prop.toString());
    }

    /**
     * test precision "hour" set value happy day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionHourSetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"hour\""
                + "/>");
        prop.setValue(DFTIME.parse("01.01.2000 23:59:59"));
        assertEquals(DFTIME.parse("01.01.2000 23:00:00"), prop.getValue());
        prop.setValue(DFTIME.parse("01.01.1900 23:59:59"));
        assertEquals(DFTIME.parse("01.01.1900 23:00:00"), prop.getValue());
        prop.setValue(DFTIME.parse("01.01.00001 23:59:59"));
        assertEquals(DFTIME.parse("01.01.00001 23:00:00"), prop.getValue());
    }

    /**
     * test precision "hour" set value happy day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionHourSetValueStringExact() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"hour\""
                + "/>");
        prop.setValue("2080080808");
        assertEquals(DFTIME.parse("08.08.2080 08:00:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1.2005 00:00:00 of the hour.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionHourSetValueStringMorePrecise() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"hour\""
                + "/>");
        prop.setValue("20050202102030");
        assertEquals(DFTIME.parse("02.02.2005 10:00:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1. of the hour.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionHourSetValueTooPreciseDate() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"hour\""
                + "/>");
        prop.setValue(DFTIME.parse("01.01.2005 12:24:11"));
        assertEquals(DFTIME.parse("01.01.2005 12:00:00"), prop.getValue());
    }

    /**
     * test precision "minute" get value.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMinuteGetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"200512312311\""
                + " precision=\"minute\""
                + "/>");
        assertEquals(DFTIME.parse("31.12.2005 23:11:00"), prop.getValue());
    }

    /**
     * test precision "minute" get value.
     */
    public void testPrecisionMinuteGetString() {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"200011010459\""
                + " precision=\"minute\""
                + "/>");
        assertEquals("200011010459", prop.toString());
    }

    /**
     * test precision "minute" set value happy day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMinuteSetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"minute\""
                + "/>");
        prop.setValue(DFTIME.parse("01.01.2000 23:59:59"));
        assertEquals(DFTIME.parse("01.01.2000 23:59:00"), prop.getValue());
        prop.setValue(DFTIME.parse("01.01.1900 23:59:59"));
        assertEquals(DFTIME.parse("01.01.1900 23:59:00"), prop.getValue());
        prop.setValue(DFTIME.parse("01.01.00001 23:59:59"));
        assertEquals(DFTIME.parse("01.01.00001 23:59:00"), prop.getValue());
    }

    /**
     * test precision "minute" set value happy day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMinuteSetValueStringExact() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"minute\""
                + "/>");
        prop.setValue("208008080831");
        assertEquals(DFTIME.parse("08.08.2080 08:31:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1.2005 00:00:00 of the minute.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMinuteSetValueStringMorePrecise() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"minute\""
                + "/>");
        prop.setValue("200502021012301");
        assertEquals(DFTIME.parse("02.02.2005 10:12:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1. of the minute.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMinuteSetValueTooPreciseDate() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"minute\""
                + "/>");
        prop.setValue(DFTIME.parse("01.01.2005 12:24:11"));
        assertEquals(DFTIME.parse("01.01.2005 12:24:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1. of the minute.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMinuteSetValueTooPreciseLong() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"minute\""
                + "/>");
        prop.setValue(DFTIME.parse("01.01.2005 12:24:11").getTime());
        assertEquals(DFTIME.parse("01.01.2005 12:24:00"), prop.getValue());
    }

    /**
     * test precision "second" get value.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionSecondGetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"20051231231123\""
                + " precision=\"second\""
                + "/>");
        assertEquals(DFTIME.parse("31.12.2005 23:11:23"), prop.getValue());
    }

    /**
     * test precision "second" get value.
     */
    public void testPrecisionSecondGetString() {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"200011010459\""
                + " precision=\"second\""
                + "/>");
        assertEquals("20001101045900", prop.toString());
    }

    /**
     * test precision "second" set value happy day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionSecondSetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"second\""
                + "/>");
        prop.setValue(DFTIME.parse("01.01.2000 23:59:59"));
        assertEquals(DFTIME.parse("01.01.2000 23:59:59"), prop.getValue());
        prop.setValue(DFTIME.parse("01.01.1900 23:59:59"));
        assertEquals(DFTIME.parse("01.01.1900 23:59:59"), prop.getValue());
        prop.setValue(DFTIME.parse("01.01.00001 23:59:59"));
        assertEquals(DFTIME.parse("01.01.00001 23:59:59"), prop.getValue());
    }

    /**
     * test precision "second" set value happy day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionSecondSetValueStringExact() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"second\""
                + "/>");
        prop.setValue("208008080831");
        assertEquals(DFTIME.parse("08.08.2080 08:31:00"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1.2005 00:00:00 of the second.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionSecondSetValueStringMorePrecise() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"second\""
                + "/>");
        prop.setValue("20050202101230123");
        assertEquals(DFTIME.parse("02.02.2005 10:12:30"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1. of the second.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionSecondSetValueTooPreciseDate() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"second\""
                + "/>");
        prop.setValue(DFTIME.parse("01.01.2005 12:24:11"));
        assertEquals(DFTIME.parse("01.01.2005 12:24:11"), prop.getValue());
    }

    /**
     * verifies cutting of the value to 1.1. of the second.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionSecondSetValueTooPreciseLong() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"second\""
                + "/>");
        prop.setValue(DFTIME.parse("01.01.2005 12:24:11").getTime());
        assertEquals(DFTIME.parse("01.01.2005 12:24:11"), prop.getValue());
    }

    /**
     * test precision "millisecond" get value.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMillisGetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"20050101121314155\""
                + " precision=\"millisecond\""
                + "/>");
        assertEquals(new Date(DFTIME.parse("01.01.2005 12:13:14").getTime() + 155), prop.getValue());
    }

    /**
     * test precision "millis" to String.
     */
    public void testPrecisionMilisGetString() {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\" default=\"20050101121314999\""
                + " precision=\"millisecond\""
                + "/>");
        assertEquals("20050101121314999", prop.toString());
    }

    /**
     * test precision "millisecond" set value happy day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMillisSetValue() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"millisecond\""
                + "/>");
        prop.setValue(DFTIME.parse("01.01.2005 23:01:12").getTime() + 815);
        assertEquals(DFTIME.parse("01.01.2005 23:01:12").getTime() + 815, prop.getValueTime());
    }

    /**
     * test precision "milliseconds" set value String happy day.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testPrecisionMillisSetValueStringExact() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"millisecond\""
                + "/>");
        prop.setValue("20051129215900123");
        assertEquals(DFTIME.parse("29.11.2005 21:59:00").getTime() + 123, prop.getValueTime());
    }

    /**
     * show that value 0 means 1.1.1970 1 a.m.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testDateArithmetics() throws java.text.ParseException {

        // Date(0) is the 01 January 1970 1:00:00 p.m. (0 milliseconds)
        assertEquals(DFTIME.parse("01.01.1970 01:00:00"), new Date(0));

        // adding 1000 milliseconds makes one second more
        assertEquals(DFTIME.parse("01.01.1970 01:00:01"), new Date(1000));

        // adding 60 seconds makes one minute more
        assertEquals(DFTIME.parse("01.01.1970 01:01:00"), new Date(60000));

        // adding 3600 seconds makes one our more
        assertEquals(DFTIME.parse("01.01.1970 02:00:00"), new Date(3600000));

        // adding 23 * 3600 (= 82800) is the next day exactly at 0:00 p.m.
        assertEquals(DFTIME.parse("02.01.1970 00:00:00"), new Date(82800000));

        // substracting 1000 milliseconds makes one second less
        assertEquals(DFTIME.parse("01.01.1970 00:59:59"), new Date(-1000));

        // substracting 60 seconds makes one minute less
        assertEquals(DFTIME.parse("01.01.1970 00:59:00"), new Date(-60000));

        // substracting 3600 seconds makes one our less, exactly 0:00 p.m.
        assertEquals(DFTIME.parse("01.01.1970 00:00:00"), new Date(-3600000));
    }

    /**
     * test cut the long value according to the specified precision.
     *
     * @throws java.text.ParseException if parsing fails
     */
    public void testCutPrecisionLong() throws java.text.ParseException {
        PropertyDate prop = this.createDateProperty(
                "<property name=\"test\""
                + " precision=\"day\""
                + "/>");
        // 1.1.1970 00:00:00 == - 3 600 000
        assertEquals(-3600000, DFDATE.parse("01.01.1970").getTime());
        // 1.1.1970 00:00:00.000 --> 1.1.1970 00:00:00.000
        assertEquals(-3600000, prop.cutPrecisionLong(-3600000));
        // 1.1.1970 01:00:00.000 --> 1.1.1970 00:00:00.000
        assertEquals(-3600000, prop.cutPrecisionLong(0));
        // 1.1.1970 23:59:59.999 --> 1.1.1970 00:00:00.000
        assertEquals(-3600000, prop.cutPrecisionLong((23 * 3600000) - 1));
        // 1.1.1970 24:00:00.000 --> 2.1.1970 00:00:00.000
        assertEquals(23 * 3600000, prop.cutPrecisionLong(23 * 3600000));
        // 1.1.1970 24:00:00.000 --> 2.1.1970 00:00:00.000
        assertEquals(23 * 3600000, prop.cutPrecisionLong((23 * 3600000) + 1));
        // 31.03.2005 00:00:00.000
        assertEquals(1112220000000L, DFDATE.parse("31.03.2005").getTime());
        // 1112220000000 + 3600000 = 1112223600000
        assertEquals(1112223600000L, DFTIME.parse("31.03.2005 01:00:00").getTime());
    }

    /**
     * set up a date property.
     * @param descr the XML property type description
     * @return a new date property.
     */
    private PropertyDate createDateProperty(final String descr) {
        XmlNode propertyNode = XmlNode.getDocumentTopLevel(
                new ByteArrayInputStream(descr.getBytes()));
        TypePropertyDate type = new TypePropertyDate(new XmlNode[]{propertyNode}, null);
        return new PropertyDate(type, null);
    }
}
