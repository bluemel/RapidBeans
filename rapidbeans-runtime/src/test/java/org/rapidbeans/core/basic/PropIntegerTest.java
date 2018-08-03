/*
 * Rapid Beans Framework: PropIntegerTest
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Dec 20, 2005
 */
package org.rapidbeans.core.basic;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;

import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyInteger;
import org.rapidbeans.core.util.XmlNode;

import junit.framework.TestCase;

/**
 * Unit Tests for class PropertyInteger.
 * 
 * @author Martin Bluemel
 */
public class PropIntegerTest extends TestCase {

	/**
	 * Test method for default value setting and getValue().
	 */
	public void testDefaultAndGetValue() {
		PropertyInteger prop = this.createIntegerProperty("<property name=\"test\" default=\"2147483647\"/>");
		assertEquals(Integer.MAX_VALUE, prop.getValue());
		prop = this.createIntegerProperty("<property name=\"test\" size=\"byte01\" default=\"127\"/>");
		assertEquals(Byte.MAX_VALUE, prop.getValue());
	}

	/**
	 * Test method for default value setting and getValue().
	 */
	public void testDefaultAndGetValueNull() {
		PropertyInteger prop = this.createIntegerProperty("<property name=\"test\"/>");
		assertNull(prop.getValue());
	}

	/**
	 * Test method for default value setting with invalid value.
	 */
	public void testDefaultValueInvalidExceededMaxVal() {
		try {
			this.createIntegerProperty("<property name=\"test\" maxval=\"10\" default=\"20\"/>");
			fail("expected ValidationException");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	public void testDefaultValueExceededImplementationBoundaries() {
		try {
			this.createIntegerProperty("<property name=\"test\" size=\"byte01\" default=\"260\"/>");
			fail();
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for toString().
	 */
	public void testToString() {
		PropertyInteger prop = this.createIntegerProperty("<property name=\"test\" default=\"-123456789\"/>");
		assertEquals("-123456789", prop.toString());
	}

	/**
	 * Test method for toString() with undefined property value.
	 */
	public void testToStringNull() {
		PropertyInteger prop = this.createIntegerProperty("<property name=\"test\"/>");
		assertEquals(null, prop.toString());
	}

	/**
	 * Test method for setValue(Object).
	 */
	public void testSetValue() {
		PropertyInteger prop = this.createIntegerProperty("<property name=\"test\"/>");
		assertNull(prop.getValue());
		prop.setValue(new Integer(-2147483648));
		assertEquals(Integer.MIN_VALUE, prop.getValue());
	}

	/**
	 * Test method for setValue(Object).
	 */
	public void testSetValueInt() {
		PropertyInteger prop = this.createIntegerProperty("<property name=\"test\"/>");
		assertNull(prop.getValue());
		prop.setValue(2147483647);
		assertEquals(Integer.MAX_VALUE, prop.getValue());
	}

	/**
	 * Test method for setValue(Object).
	 */
	public void testSetValueIntBig() {
		PropertyInteger prop = this
				.createIntegerPropertyFlexible("<property name=\"test\" type=\"integer\" size=\"unlimited\"/>");
		assertNull(prop.getValue());
		prop.setValue("123456789012345678901234567890");
		assertEquals(new BigInteger("123456789012345678901234567890"), prop.getValue());
	}

	/**
	 * test of method convertValue(): happy day.
	 */
	public void testConvertInteger() {
		PropertyInteger prop = this.createIntegerProperty("<property name=\"test\"/>");
		assertEquals(123, prop.convertValue(new Integer("123")));
	}

	/**
	 * test of method convertValue() long happy.
	 */
	public void testConvertLongToIntOk() {
		PropertyInteger prop = this.createIntegerProperty("<property name=\"test\"/>");
		assertEquals(-1234567, prop.convertValue(new Long(-1234567)));
	}

	/**
	 * test of method convertValue() long happy.
	 */
	public void testConvertLongToIntTooLarge() {
		PropertyInteger prop = this.createIntegerProperty("<property name=\"test\"/>");
		try {
			prop.convertValue(new Long(new Long(Integer.MAX_VALUE) + 1));
			fail("expected ValidationException");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * test of method convertValue() long happy.
	 */
	public void testConvertLongToIntTooSmall() {
		PropertyInteger prop = this.createIntegerProperty();
		try {
			prop.convertValue(new Long(new Long(Integer.MIN_VALUE) - 1));
			fail("expected ValidationException");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	public void testConvertBigIntToIntOk() {
		PropertyInteger prop = this.createIntegerProperty();
		assertEquals(12345678, prop.convertValue(new BigInteger("12345678")));
	}

	public void testConvertBigIntToIntTooLarge() {
		PropertyInteger prop = this.createIntegerProperty();
		try {
			prop.convertValue(new BigInteger("12345678901"));
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * test of method convertValue(): happy day with String.
	 */
	public void testConvertString() {
		PropertyInteger prop = this.createIntegerProperty();
		assertEquals(1234599, prop.convertValue("1234599"));
	}

	/**
	 * test of method convertValue(): wrong class.
	 */
	public void testConvertInvalid() {
		PropertyInteger prop = this.createIntegerProperty("<property name=\"test\"/>");
		try {
			prop.convertValue(new Double(123.0));
			fail("expected validation exception");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * Happy day test for validation.
	 */
	public void testValidateOk() {
		PropertyInteger prop = this
				.createIntegerProperty("<property name=\"test\"" + " minval=\"-300000\" maxval=\"300000\"/>");
		prop.validate(0);
		prop.validate(1);
		prop.validate(100000);
		prop.validate(300000);
		prop.validate(-300000);
	}

	/**
	 * Test for validation of an Integer that undergoes minimal value.
	 */
	public void testValidateTooSmall() {
		PropertyInteger prop = this
				.createIntegerProperty("<property name=\"test\"" + " minval=\"-300000\" maxval=\"300000\"/>");
		try {
			prop.validate(300001);
			fail("expected validation exception");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test for validation of a String that exceeds maxlen.
	 */
	public void testValidateTooGreat() {
		PropertyInteger prop = this
				.createIntegerProperty("<property name=\"test\"" + " minval=\"0\" maxval=\"300000\"/>");
		try {
			prop.validate(1000000);
			fail("expected validation exception");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * Create a simple default implemented Integer property.
	 * 
	 * @return the new, simple and default implemented Integer property instance
	 */
	private PropertyInteger createIntegerProperty() {
		return createIntegerProperty("<property name=\"test\"/>");
	}

	/**
	 * set up an Integer Property.
	 * 
	 * @param descr the XML property type description
	 * @return a new Integer Property instance.
	 */
	private PropertyInteger createIntegerProperty(final String descr) {
		XmlNode propertyNode = XmlNode.getDocumentTopLevel(new ByteArrayInputStream(descr.getBytes()));
		TypePropertyInteger type = new TypePropertyInteger(new XmlNode[] { propertyNode }, null);
		return new PropertyInteger(type, null);
	}

	/**
	 * set up a Integer Property with BigInteger size.
	 * 
	 * @param descr the XML property type description
	 * @return a new Integer Property instance.
	 */
	private PropertyInteger createIntegerPropertyFlexible(final String descr) {
		XmlNode propertyNode = XmlNode.getDocumentTopLevel(new ByteArrayInputStream(descr.getBytes()));
		TypePropertyInteger type = (TypePropertyInteger) TypeProperty.createInstance(new XmlNode[] { propertyNode }[0],
				null, null);
		return new PropertyInteger(type, null);
	}
}
