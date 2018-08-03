package org.rapidbeans.core.basic;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.exception.ImmutableCollectionException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypePropertyChoice;
import org.rapidbeans.core.util.XmlNode;
import org.rapidbeans.domain.org.Country;
import org.rapidbeans.domain.org.Sex;

import junit.framework.TestCase;

/**
 * @author Martin Bluemel
 */
public class PropertyChoiceTest extends TestCase {

	/**
	 * Test method for default value setting and getValue().
	 */
	public void testDefaultAndGetValue() {
		PropertyChoice prop = this.createChoiceProperty(
				"<property name=\"test\" enum=\"org.rapidbeans.domain.org.Sex\"" + " default=\"female\"/>");
		assertEquals(1, prop.getValue().size());
		assertSame(Sex.female, prop.getValue().get(0));
	}

	/**
	 * Test method for default value setting and getValue().
	 */
	public void testDefaultAndGetValueMultipleOne() {
		PropertyChoice prop = this
				.createChoiceProperty("<property name=\"test\" enum=\"org.rapidbeans.domain.org.Country\""
						+ " multiple=\"true\" default=\"germany,austria\"/>");
		assertEquals(2, prop.getValue().size());
		assertSame(Country.germany, prop.getValue().get(0));
		assertSame(Country.austria, prop.getValue().get(1));
	}

	/**
	 * Test method for default value setting and getValue().
	 */
	public void testDefaultAndGetValueMultipleMore() {
		PropertyChoice prop = this
				.createChoiceProperty("<property name=\"test\" enum=\"org.rapidbeans.domain.org.Country\""
						+ " multiple=\"true\" default=\"germany,austria,finnland\"/>");
		assertEquals(3, prop.getValue().size());
		assertSame(Country.germany, prop.getValue().get(0));
		assertSame(Country.austria, prop.getValue().get(1));
		assertSame(Country.finnland, prop.getValue().get(2));
	}

	/**
	 * Test method for no default value.
	 */
	public void testDefaultAndGetValueNull() {
		PropertyChoice prop = this
				.createChoiceProperty("<property name=\"test\" enum=\"org.rapidbeans.domain.org.Sex\"/>");
		assertNull(prop.getValue());
	}

	/**
	 * Test method for default value invalied.
	 */
	public void testDefaultValueInvalid() {
		try {
			this.createChoiceProperty("<property name=\"test\" enum=\"org.rapidbeans.domain.org.Country\""
					+ " multiple=\"false\" default=\"germany,austria\"/>");
			fail("expected ValidationException");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for setValue(Object).
	 */
	public void testSetValue() {
		PropertyChoice prop = this
				.createChoiceProperty("<property name=\"test\" enum=\"org.rapidbeans.domain.org.Sex\"/>");
		prop.setValue(Sex.female);
		assertSame(Sex.female, prop.getValue().get(0));
	}

	/**
	 * Test method for no default value and setValue(Object) with a multiple choice
	 * and one single enum.
	 */
	public void testSetValueMultipleOne() {
		PropertyChoice prop = this.createChoiceProperty(
				"<property name=\"test\" enum=\"org.rapidbeans.domain.org.Country\"" + " multiple=\"true\"/>");
		assertNull(prop.getValue());
		prop.setValue(Country.austria);
		assertSame(Country.austria, prop.getValue().get(0));
	}

	/**
	 * Test method for no default value and setValue(Object) with a multple choice
	 * and one single enum.
	 */
	public void testSetValueMultipleMore() {
		PropertyChoice prop = this.createChoiceProperty(
				"<property name=\"test\" enum=\"org.rapidbeans.domain.org.Country\"" + " multiple=\"true\"/>");
		assertNull(prop.getValue());
		List<Country> list = new ArrayList<Country>();
		list.add(Country.england);
		list.add(Country.austria);
		list.add(Country.finnland);
		list.add(Country.belgium);
		prop.setValue(list);
		assertEquals(4, prop.getValue().size());
		assertSame(Country.england, prop.getValue().get(0));
		assertSame(Country.austria, prop.getValue().get(1));
		assertSame(Country.finnland, prop.getValue().get(2));
		assertSame(Country.belgium, prop.getValue().get(3));
	}

	/**
	 * Test method for toString().
	 */
	public void testToString() {
		PropertyChoice prop = this.createChoiceProperty(
				"<property name=\"test\" enum=\"org.rapidbeans.domain.org.Sex\"" + " default=\"female\"/>");
		assertNotNull(prop.getValue());
		assertEquals("female", prop.toString());
	}

	/**
	 * Test method for toString with multple choice and one item chosen.
	 */
	public void testToStringMultipleOne() {
		PropertyChoice prop = this
				.createChoiceProperty("<property name=\"test\" enum=\"org.rapidbeans.domain.org.Country\""
						+ " multiple=\"true\" default=\"germany\"/>");
		assertEquals(1, prop.getValue().size());
		assertSame("germany", prop.toString());
	}

	/**
	 * Test method for toString with multple choice and more than one item chosen.
	 */
	public void testToStringMultipleMore() {
		PropertyChoice prop = this
				.createChoiceProperty("<property name=\"test\" enum=\"org.rapidbeans.domain.org.Country\""
						+ " multiple=\"true\" default=\"germany,austria,finnland\"/>");
		assertEquals("germany,austria,finnland", prop.toString());
	}

	/**
	 * Happy day test for validation.
	 */
	public void testValidateOk() {
		PropertyChoice prop = this.createChoiceProperty(
				"<property name=\"test\" enum=\"org.rapidbeans.domain.org.Country\"" + " multiple=\"true\"/>");
		List<Country> list = new ArrayList<Country>();
		list.add(Country.england);
		list.add(Country.austria);
		list.add(Country.finnland);
		list.add(Country.belgium);
		prop.validate(list);
	}

	/**
	 * Test for validation more than one entry if multiple == false.
	 */
	public void testValidateMultipleChoiceInSingleChoice() {
		// multiple defaults to "false"
		PropertyChoice prop = this
				.createChoiceProperty("<property name=\"test\" enum=\"org.rapidbeans.domain.org.Country\"/>");
		List<Country> list = new ArrayList<Country>();
		list.add(Country.england);
		list.add(Country.austria);
		try {
			prop.validate(list);
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test for validation one entry more than once.
	 */
	public void testValidateOneEntryMoreThanOnce() {
		PropertyChoice prop = this.createChoiceProperty(
				"<property name=\"test\" enum=\"org.rapidbeans.domain.org.Country\"" + " multiple=\"true\"/>");
		List<Country> list = new ArrayList<Country>();
		list.add(Country.england);
		list.add(Country.austria);
		list.add(Country.finnland);
		list.add(Country.finnland);
		list.add(Country.belgium);
		try {
			prop.validate(list);
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * test of method convertValue(): happy day.
	 */
	public void testConvertListOfBBEnum() {
		PropertyChoice prop = this.createChoiceProperty(
				"<property name=\"test\" enum=\"org.rapidbeans.domain.org.Country\"" + " multiple=\"true\"/>");
		List<Country> list1 = new ArrayList<Country>();
		list1.add(Country.england);
		list1.add(Country.austria);
		list1.add(Country.finnland);
		list1.add(Country.belgium);
		List<Country> list2 = new ArrayList<Country>();
		list2.add(Country.england);
		list2.add(Country.austria);
		list2.add(Country.finnland);
		list2.add(Country.belgium);
		assertEquals(list2, prop.convertValue(list1));
	}

	/**
	 * test of method convertValue(): happy day.
	 */
	public void testConvertSingleBBEnum() {
		PropertyChoice prop = this.createChoiceProperty(
				"<property name=\"test\" enum=\"org.rapidbeans.domain.org.Country\"" + " multiple=\"true\"/>");
		List<Country> list = new ArrayList<Country>();
		list.add(Country.finnland);
		assertEquals(list, prop.convertValue(Country.finnland));
	}

	/**
	 * test of method convertValue(): happy day with String.
	 */
	public void testConvertString() {
		PropertyChoice prop = this.createChoiceProperty(
				"<property name=\"test\" enum=\"org.rapidbeans.domain.org.Country\"" + " multiple=\"true\"/>");
		List<Country> list = new ArrayList<Country>();
		list.add(Country.finnland);
		list.add(Country.austria);
		list.add(Country.belgium);
		assertEquals(list, prop.convertValue("finnland,austria,belgium"));
	}

	/**
	 * test of method convertValue(): happy day with String.
	 */
	public void testConvertStringArray() {
		PropertyChoice prop = this.createChoiceProperty(
				"<property name=\"test\" enum=\"org.rapidbeans.domain.org.Country\"" + " multiple=\"true\"/>");
		List<Country> list = new ArrayList<Country>();
		list.add(Country.finnland);
		list.add(Country.austria);
		list.add(Country.belgium);
		final String[] sa = { "finnland", "austria", "belgium" };
		assertEquals(list, prop.convertValue(sa));
	}

	/**
	 * test immutability. proove that our PropertyChoice is immutable after getValue
	 * 
	 * @throws java.text.ParseException if parsing fails
	 */
	public void testImmutabilityGet() throws java.text.ParseException {

		PropertyChoice prop = this
				.createChoiceProperty("<property name=\"test\"" + " enum=\"org.rapidbeans.domain.org.Country\""
						+ " multiple=\"true\"" + " default=\"germany,austria\"/>");

		// originally prop1 has the default value
		assertEquals(2, prop.getValue().size());
		assertSame(Country.germany, prop.getValue().get(0));
		assertSame(Country.austria, prop.getValue().get(1));

		// then I try to mute the private field date by using the object
		// returned by the getter
		ReadonlyListCollection<?> choice1 = prop.getValue();
		try {
			choice1.clear();
		} catch (ImmutableCollectionException e) {
			assertTrue(true);
		}

		// but our prop stays the same
		assertEquals(2, prop.getValue().size());
		assertSame(Country.germany, prop.getValue().get(0));
		assertSame(Country.austria, prop.getValue().get(1));
	}

	/**
	 * test immutability. proove that our PropertyDate is immutable after setValue.
	 * 
	 * @throws java.text.ParseException if parsing fails
	 */
	public void testImmutabilitySet() throws java.text.ParseException {

		PropertyChoice prop = this.createChoiceProperty(
				"<property name=\"test\"" + " enum=\"org.rapidbeans.domain.org.Country\"" + " multiple=\"true\"/>");

		List<Country> choice2 = new ArrayList<Country>();
		choice2.add(Country.austria);
		choice2.add(Country.finnland);
		prop.setValue(choice2);
		assertEquals(2, prop.getValue().size());
		assertSame(Country.austria, prop.getValue().get(0));
		assertSame(Country.finnland, prop.getValue().get(1));

		// then I try to mute the private field date by using the object
		// injected by the setter.
		choice2.clear();

		// but our prop stays the same
		assertEquals(2, prop.getValue().size());
		assertSame(Country.austria, prop.getValue().get(0));
		assertSame(Country.finnland, prop.getValue().get(1));
	}

	/**
	 * set up a Choice Property.
	 * 
	 * @param descr the XML property type description
	 * @return a new Choice property.
	 */
	private PropertyChoice createChoiceProperty(final String descr) {
		XmlNode propertyNode = XmlNode.getDocumentTopLevel(new ByteArrayInputStream(descr.getBytes()));
		TypePropertyChoice type = new TypePropertyChoice(new XmlNode[] { propertyNode }, null);
		return new PropertyChoice(type, null);
	}
}
