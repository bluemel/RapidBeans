package org.rapidbeans.core.basic;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;

import junit.framework.TestCase;

import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypePropertyQuantity;
import org.rapidbeans.core.util.XmlNode;
import org.rapidbeans.domain.finance.Currency;
import org.rapidbeans.domain.finance.Money;
import org.rapidbeans.domain.math.Time;
import org.rapidbeans.domain.math.TimeOfDay;
import org.rapidbeans.domain.math.UnitTime;

/**
 * @author Martin Bluemel
 */
public class PropertyQuantityTest extends TestCase {

    /**
     * Test method for default value setting and getValue().
     */
    public void testDefaultAndGetValue() {
        PropertyQuantity prop = this.createQuantityProperty(
                "<property name=\"test\" quantity=\"org.rapidbeans.domain.math.Time\""
                + " default=\"2 h\"/>");
        assertEquals(new Time(new BigDecimal(2), UnitTime.h), (RapidQuantity) prop.getValue());
    }

    /**
     * Test method for no default value.
     */
    public void testDefaultAndGetValueNull() {
        PropertyQuantity prop = this.createQuantityProperty(
                "<property name=\"test\" quantity=\"org.rapidbeans.domain.math.Time\"/>");
        assertNull(prop.getValue());
    }

    /**
     * Test method for default value invalied.
     */
    public void testDefaultValueInvalid() {
        try {
            this.createQuantityProperty(
                "<property name=\"test\" quantity=\"org.rapidbeans.domain.math.Time\""
                + " maxval=\"23 min\" default=\"30 min\"/>");
            fail("expected ValidationException");
        } catch (ValidationException e) {
            assertTrue(true);
        }
    }

    /**
     * Test method for setValue(Object).
     */
    public void testSetValue() {
        PropertyQuantity prop = this.createQuantityProperty(
                "<property name=\"test\" quantity=\"org.rapidbeans.domain.math.Time\"/>");
        prop.setValue("123 s");
        assertEquals(new Time("123 s"), prop.getValue());
    }

    /**
     * Test method for toString().
     */
    public void testToString() {
        PropertyQuantity prop = this.createQuantityProperty(
                "<property name=\"test\" quantity=\"org.rapidbeans.domain.math.Time\""
                + " default=\"3600123 ms\"/>");
        assertEquals("3600123 ms", prop.toString());
    }

    /**
     * Happy day test for validation.
     */
    public void testValidateOk() {
        PropertyQuantity prop = this.createQuantityProperty(
                "<property name=\"test\" quantity=\"org.rapidbeans.domain.math.TimeOfDay\""
                + " maxval=\"01:57\"/>");
        prop.validate(new TimeOfDay("01:57"));
    }

    /**
     * test for validation of quantity type.
     */
    public void testValidateQuantityType() {
        PropertyQuantity prop = this.createQuantityProperty(
                "<property name=\"test\" quantity=\"org.rapidbeans.domain.math.Time\"/>");
        try {
            prop.validate(new Money("10 euro"));
            fail("expected ValidationException");
        } catch (ValidationException e) {
            assertTrue(true);
        }
    }


    /**
     * test for validation of min value.
     */
    public void testValidateMaxValue() {
        PropertyQuantity prop = this.createQuantityProperty(
                "<property name=\"test\" quantity=\"org.rapidbeans.domain.math.TimeOfDay\""
                + " maxval=\"01:57\"/>");
        try {
            prop.validate(new TimeOfDay("01:58"));
            fail("expected ValidationException");
        } catch (ValidationException e) {
            assertTrue(true);
        }
    }


    /**
     * test for validation of min value.
     */
    public void testValidateMinValue() {
        PropertyQuantity prop = this.createQuantityProperty(
                "<property name=\"test\" quantity=\"org.rapidbeans.domain.math.TimeOfDay\""
                + " minval=\"23:57:11\"/>");
        try {
            prop.validate(new TimeOfDay("23:50"));
            fail("expected ValidationException");
        } catch (ValidationException e) {
            assertTrue(true);
        }
    }

    /**
     * test of method convertValue(): happy day.
     */
    public void testConvertQuantity() {
        PropertyQuantity prop = this.createQuantityProperty(
                "<property name=\"test\" quantity=\"org.rapidbeans.domain.finance.Money\"/>");
        assertEquals(new Money(new BigDecimal("1222333444.555666"), Currency.euro),
                prop.convertValue(new Money("1222333444.555666 euro")));
    }

    /**
     * test of method convertValue(): happy day.
     */
    public void testConvertString() {
        PropertyQuantity prop = this.createQuantityProperty(
                "<property name=\"test\" quantity=\"org.rapidbeans.domain.finance.Money\"/>");
        assertEquals(new Money(new BigDecimal("7771222333444.555666"), Currency.euro),
                prop.convertValue("7771222333444.555666 euro"));
    }

    /**
     * set up a RapidQuantity Property.
     * @param descr the XML property type description
     * @return a new Choice property.
     */
    private PropertyQuantity createQuantityProperty(final String descr) {
        XmlNode propertyNode = XmlNode.getDocumentTopLevel(
                new ByteArrayInputStream(descr.getBytes()));
        TypePropertyQuantity type = new TypePropertyQuantity(new XmlNode[]{propertyNode}, null);
        return new PropertyQuantity(type, null);
    }
}
