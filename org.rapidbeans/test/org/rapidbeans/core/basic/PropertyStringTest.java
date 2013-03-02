/*
 * Rapid Beans Framework: PropertyStringTest
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Dec 20, 2005
 */
package org.rapidbeans.core.basic;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypePropertyString;
import org.rapidbeans.core.util.XmlNode;

/**
 * Unit Tests for class PropertyString.
 * 
 * @author Martin Bluemel
 */
public class PropertyStringTest extends TestCase {

	/**
	 * Test method for default and getValue().
	 */
	public void testDefaultAndGetValue() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " default=\"test1\"" + "/>");
		assertEquals("test1", prop.getValue());
	}

	/**
	 * Test method for default and getValue().
	 */
	public void testDefaultAndGetValueNull() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\"/>");
		assertNull(prop.getValue());
	}

	/**
	 * Test method for default and getValue().
	 */
	public void testDefaultValueInvalid() {
		try {
			this.createStringProperty("<property name=\"test\" type=\"string\""
					+ " maxlen=\"1\" default=\"xx\"" + "/>");
			fail("expected ValidationException");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for toString().
	 */
	public void testToString() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " default=\"test3\"" + "/>");
		assertEquals("test3", prop.toString());
	}

	/**
	 * Test method for toString().
	 */
	public void testToStringNull() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\"/>");
		assertNull(prop.toString());
	}

	/**
	 * Test method for setValue(Object).
	 */
	public void testSetValue() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ "/>");
		assertNull(prop.getValue());
		prop.setValue("test2");
		assertEquals("test2", prop.getValue());
	}

	/**
	 * Test method for setValue(Object).
	 */
	public void testSetValueNull() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " default=\"xxx\"/>");
		assertEquals("xxx", prop.getValue());
		prop.setValue(null);
		assertNull(prop.getValue());
	}

	/**
	 * test of method convertValue(): happy day.
	 */
	public void testConvertString() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ "/>");
		assertEquals("Asdf", prop.convertValue("Asdf"));
	}

	/**
	 * test of method convertValue(): invalid class.
	 */
	public void testConvertInvalid() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ "/>");
		try {
			prop.convertValue(new Integer("123"));
			fail("expected validation exception");
		} catch (ValidationException e) {
			assertTrue(true);
		}
		// insted of converting an Integer you could use the toString()
		// method of class Integer.
		assertEquals("123", prop.convertValue(new Integer("123").toString()));
	}

	/**
	 * Happy day test for validation.
	 */
	public void testValidateOk() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " minlen=\"6\" maxlen=\"30\" pattern=\".*@.*\""
						+ "/>");
		prop.validate("martin.bluemel@web.de");
	}

	/**
	 * Test for validation of a String that is empty.
	 */
	public void testValidateEmpty() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\"/>");
		try {
			prop.validate("");
			fail("expected validation exception");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test for validation of a String that is empty if this is explicitly
	 * allowed.
	 */
	public void testValidateEmptyOk() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\" emptyvalid=\"true\"/>");
		prop.validate("");
	}

	/**
	 * Test for validation of a String that undergoes minlen.
	 */
	public void testValidateTooShort() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " minlen=\"6\" maxlen=\"30\" pattern=\".*@.*\""
						+ "/>");
		try {
			prop.validate("m@x.d");
			fail("expected validation exception");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test for validation of a String that exceeds maxlen.
	 */
	public void testValidateTooLong() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " minlen=\"6\" maxlen=\"30\" pattern=\".*@.*\""
						+ "/>");
		try {
			prop.validate("0123456789@01234567890123456789");
			fail("expected validation exception");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test for validation of a String that exceeds maxlen.
	 */
	public void testValidateWrongPattern() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " minlen=\"6\" maxlen=\"30\" pattern=\".*@.*\""
						+ "/>");
		try {
			prop.validate("martin.bluemel%web.de");
			fail("expected validation exception");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for compareTo(Object).
	 */
	public void testCompareToEquals() {
		PropertyString prop1 = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " default=\"test3\"" + "/>");
		PropertyString prop2 = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " default=\"test3\"" + "/>");
		assertEquals(0, prop1.compareTo(prop2));
	}

	/**
	 * Test method for compareTo(Object).
	 */
	public void testCompareToGreater() {
		PropertyString prop1 = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " default=\"abc\"" + "/>");
		PropertyString prop2 = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " default=\"xyz\"" + "/>");
		assertEquals(-23, "abc".compareTo("xyz"));
		assertEquals(-23, prop1.compareTo(prop2));
	}

	/**
	 * Test method for compareTo(Object).
	 */
	public void testCompareToLess() {
		PropertyString prop1 = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " default=\"xyz\"" + "/>");
		PropertyString prop2 = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " default=\"abc\"" + "/>");
		assertEquals(23, "xyz".compareTo("abc"));
		assertEquals(23, prop1.compareTo(prop2));
	}

	/**
	 * Test writing a multi lined string.
	 */
	public void testMultiLine() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " multiline=\"true\"" + "/>");
		prop.setValue("aaa\nbbb");
		assertEquals("aaa\nbbb", prop.getValue());
	}

	/**
	 * Test writing a multi lined string to a single lined string property
	 */
	public void testMultiLineInvalid() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ "/>");
		try {
			prop.setValue("aaa\nbbb");
			fail("Expected a ValidationException");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test writing a multi lined string.
	 */
	public void testMultiLineEscaping() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " multiline=\"true\"" + "/>");
		prop.setValue("aaa\nbbb");
		assertEquals("aaa\\nbbb", prop.toString());
	}

	/**
	 * Test writing a string with tab character.
	 */
	public void testEscapingTab() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
				// multiline="true" activates the default escape map
						+ " multiline=\"true\"" + "/>");
		prop.setValue("aaa\tbbb");
		assertEquals("aaa\\tbbb", prop.toString());
	}

	/**
	 * set up a string property.
	 * 
	 * @param descr
	 *            the XML property type description
	 * @return a new string property.
	 */
	private PropertyString createStringProperty(final String descr) {
		XmlNode propertyNode = XmlNode
				.getDocumentTopLevel(new ByteArrayInputStream(descr.getBytes()));
		TypePropertyString type = new TypePropertyString(
				new XmlNode[] { propertyNode }, null);
		return new PropertyString(type, null);
	}
}
