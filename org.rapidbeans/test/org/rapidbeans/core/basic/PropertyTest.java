/*
 * Rapid Beans Framework: PropertyTest
 * 
 * Copyright Martin Bluemel, 2006
 * 
 * Mar 09, 2006
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
public class PropertyTest extends TestCase {

	/**
	 * Test method for default and getValue().
	 */
	public void testGetBean() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " default=\"test1\"" + "/>");
		assertEquals("test1", prop.getValue());
		assertNull(prop.getBean());
	}

	/**
	 * test validating a mandatory property. !!! a mandatory property must have
	 * a default value !!!
	 */
	public void testValidateMandatory() {
		PropertyString prop = this
				.createStringProperty("<property name=\"test\" type=\"string\""
						+ " mandatory=\"true\" emptyvalid=\"true\""
						+ " default=\"\"/>");
		assertEquals(true, prop.getType().getMandatory());
		try {
			prop.setValue(null);
			fail("expected a BBExValidationExceptoion");
		} catch (ValidationException e) {
			assertEquals("invalid.prop.mandatory", e.getSignature());
		}
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
