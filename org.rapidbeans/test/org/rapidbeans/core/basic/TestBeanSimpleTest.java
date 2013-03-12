package org.rapidbeans.core.basic;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeRapidEnum;
import org.rapidbeans.domain.org.Sex;
import org.rapidbeans.test.Lang;
import org.rapidbeans.test.TestBeanSimple;

/**
 * Unit Test for domain class TestBeanSimple (simple implementation).
 * 
 * @author Martin Bluemel
 */
public class TestBeanSimpleTest {

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
	 * Constructor test:
	 * the constructor initializes all date attribute to empty (null).
	 */
	@Test
	public void testStringProperty() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals("Ismaning", bean.getCity());
		bean.setCity("Mannheim");
		Assert.assertEquals("Mannheim", bean.getCity());
	}

	@Test(expected = ValidationException.class)
	public void testStringPropertyInvalid() {
		TestBeanSimple bean = new TestBeanSimple();
		bean.setEmail("xyz");
	}

	@Test
	public void testDateProperty() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(DFDATE.parse("14.10.1964"), bean.getDateofbirth());
		bean.setDateofbirth(DFDATE.parse("15.10.1964"));
		Assert.assertEquals(DFDATE.parse("15.10.1964"), bean.getDateofbirth());
	}

	@Test(expected = ValidationException.class)
	public void testDatePropertyInvalid() throws ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		bean.setDateofbirth(DFDATE.parse("14.10.1700"));
	}

	@Test
	public void testBooleanProperty() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(true, bean.getMarried());
		bean.setMarried(false);
		Assert.assertEquals(false, bean.getMarried());
	}

	@Test(expected = ValidationException.class)
	public void testBooleanPropertyInvalid() throws ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		bean.setPropValue("married", null);
	}

	@Test
	public void testIntegerProperty() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(43, bean.getShoesize());
		bean.setShoesize(40);
		Assert.assertEquals(40, bean.getShoesize());
	}

	@Test(expected = ValidationException.class)
	public void testIntegerPropertyInvalid() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(43, bean.getShoesize());
		bean.setShoesize(5);
	}

	@Test
	public void testChoicePropertySingle()
	{
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(Sex.male, bean.getSex());
		bean.setSex(Sex.female);
		Assert.assertEquals(Sex.female, bean.getSex());
	}

	@Test
	public void testChoicePropertyMultiple()
	{
		TypeRapidEnum tlang = TypeRapidEnum.forName("org.rapidbeans.test.Lang");
		TestBeanSimple bean = new TestBeanSimple();
		assertListsEqual(Arrays.asList(new RapidEnum[] {
				tlang.elementOf("german"),
				tlang.elementOf("english"),
				tlang.elementOf("french"),
		}), bean.getLanguages());
		assertListsEqual(Arrays.asList(new RapidEnum[] {
				Lang.german, Lang.english, Lang.french
		}), bean.getLanguages());
		bean.setLanguages(Arrays.asList(new Lang[] { Lang.chinese, Lang.spanish }));
		assertListsEqual(Arrays.asList(new RapidEnum[] {
				Lang.chinese, Lang.spanish
		}), bean.getLanguages());
	}

	private void assertListsEqual(List<?> list1, List<?> list2)
	{
		int len = list1.size();
		if (len != list2.size())
		{
			Assert.fail("Lists have diferent sizes");
		}
		for (int i = 0; i < len; i++)
		{
			if (!list1.get(i).equals(list2.get(i)))
			{
				Assert.fail("List elements [" + Integer.toString(i)
						+ "] \"" + list1.get(i).toString()
						+ "\" and \"" + list2.get(i).toString()
						+ "\" differ");
			}
		}
	}
}
