/*
 * Rapid Beans Framework: PropIntegerTest
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Dec 20, 2005
 */
package org.rapidbeans.core.basic;

import java.io.ByteArrayInputStream;

import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypePropertyBoolean;
import org.rapidbeans.core.util.XmlNode;

import junit.framework.TestCase;

/**
 * Unit Tests for class PropertyBoolean.
 * 
 * @author Martin Bluemel
 */
public class PropertyBooleanTest extends TestCase {

	/**
	 * Test method for default value setting and getValue().
	 */
	public void testDefaultAndGetValue() {
		PropertyBoolean prop = this.createBooleanProperty("<property name=\"test\" default=\"true\"/>");
		assertEquals(Boolean.TRUE, prop.getValue());
	}

	/**
	 * Test method for default value setting and getValue().
	 */
	public void testDefaultAndGetValueNull() {
		PropertyBoolean prop = this.createBooleanProperty("<property name=\"test\"/>");
		assertNull(prop.getValue());
	}

	/**
	 * Test method for default value setting and getValue().
	 */
	public void testDefaultValueInvalid() {
		try {
			this.createBooleanProperty("<property name=\"test\" default=\"xxx\"/>");
			fail("expected ValidationException");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for toString().
	 */
	public void testToStringTrue() {
		PropertyBoolean prop = this.createBooleanProperty("<property name=\"test\"" + " default=\"TrUe\"/>");
		assertEquals("true", prop.toString());
	}

	/**
	 * Test method for toString().
	 */
	public void testToStringFalse() {
		PropertyBoolean prop = this.createBooleanProperty("<property name=\"test\"" + " default=\"FaLSE\"/>");
		assertEquals("false", prop.toString());
	}

	/**
	 * Test method for toString() with undefined property value.
	 */
	public void testToStringNull() {
		PropertyBoolean prop = this.createBooleanProperty("<property name=\"test\"/>");
		assertEquals(null, prop.toString());
	}

	/**
	 * Test method for setValue(Object).
	 */
	public void testSetValueTrue() {
		PropertyBoolean prop = this.createBooleanProperty("<property name=\"test\"/>");
		assertNull(prop.getValue());
		prop.setValue(Boolean.TRUE);
		assertEquals(Boolean.TRUE, prop.getValue());
	}

	/**
	 * Test method for setValue(Object).
	 */
	public void testSetValueFalse() {
		PropertyBoolean prop = this.createBooleanProperty("<property name=\"test\"/>");
		assertNull(prop.getValue());
		prop.setValue(Boolean.FALSE);
		assertEquals(Boolean.FALSE, prop.getValue());
	}

	/**
	 * Test method for setValue(Object).
	 */
	public void testSetValueBoolean() {
		PropertyBoolean prop = this.createBooleanProperty("<property name=\"test\"/>");
		assertNull(prop.getValue());
		prop.setValue(true);
		assertEquals(Boolean.TRUE, prop.getValue());
	}

	/**
	 * test of method convertValue(): happy day.
	 */
	public void testConvertBoolean() {
		PropertyBoolean prop = this.createBooleanProperty("<property name=\"test\"/>");
		assertEquals(Boolean.FALSE, prop.convertValue(Boolean.FALSE));
		assertEquals(Boolean.TRUE, prop.convertValue(Boolean.TRUE));
	}

	/**
	 * test of method convertValue(): happy day with String.
	 */
	public void testConvertString() {
		PropertyBoolean prop = this.createBooleanProperty("<property name=\"test\"/>");
		assertEquals(Boolean.FALSE, prop.convertValue("false"));
		assertEquals(Boolean.TRUE, prop.convertValue("True"));
	}

	/**
	 * test of method convertValue(): invalid String.
	 */
	public void testConvertStringInvalid() {
		PropertyBoolean prop = this.createBooleanProperty("<property name=\"test\"/>");
		try {
			prop.convertValue("falxe");
			fail("expected ValidationException");
		} catch (ValidationException e) {
			assertEquals("invalid.prop.boolean.string", e.getSignature());
		}
	}

	/**
	 * test of method convertValue(): wrong class.
	 */
	public void testConvertInvalid() {
		PropertyBoolean prop = this.createBooleanProperty("<property name=\"test\"/>");
		try {
			prop.convertValue(Integer.valueOf(123));
			fail("expected validation exception");
		} catch (ValidationException e) {
			assertEquals("invalid.prop.boolean.type", e.getSignature());
			assertTrue(e.getMessage().startsWith(
					"Property \"test\": invalid data type" + " \"java.lang.Integer\" for a boolean property"));
			// assertTrue(e.getLocalizedMessage().startsWith("Property \"test\": invalid
			// data type"
			// + " \"java.lang.Integer\" for a boolean property"));
			// RapidBeansLocale locale = (RapidBeansLocale)
			// RapidBeanImplStrict.createInstance("org.rapidbeans.core.common.RapidBeansLocale");
			// assertTrue(e.getLocalizedMessage(locale,
			// Integer.class.getName()).startsWith(
			// "Invalid data type \"java.lang.Integer\" for a boolean property"));
		}
	}

	/**
	 * Happy day test for validation.
	 */
	public void testValidateOk() {
		PropertyBoolean prop = this.createBooleanProperty("<property name=\"test\"/>");
		prop.validate(true);
		prop.validate(false);
		prop.validate(Boolean.TRUE);
		prop.validate(Boolean.FALSE);
		prop.validate("true");
		prop.validate("True");
		prop.validate("TRUE");
		prop.validate("TrUe");
		prop.validate("false");
		prop.validate("False");
		prop.validate("FALSE");
		prop.validate("FaLsE");
	}

	/**
	 * Test for validation for a String that is (without checking upper or lower
	 * case) not 'true' or 'false'.
	 */
	public void testValidateWrongString() {
		PropertyBoolean prop = this.createBooleanProperty("<property name=\"test\"/>");
		try {
			prop.validate("trux");
			fail("expected validation exception");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * set up an Integer Property.
	 * 
	 * @param descr the XML property type description
	 * @return a new Integer Property instance.
	 */
	private PropertyBoolean createBooleanProperty(final String descr) {
		XmlNode propertyNode = XmlNode.getDocumentTopLevel(new ByteArrayInputStream(descr.getBytes()));
		TypePropertyBoolean type = new TypePropertyBoolean(new XmlNode[] { propertyNode }, null);
		return new PropertyBoolean(type, null);
	}
}
