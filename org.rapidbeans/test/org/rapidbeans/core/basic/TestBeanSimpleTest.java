package org.rapidbeans.core.basic;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeRapidEnum;
import org.rapidbeans.domain.math.Length;
import org.rapidbeans.domain.math.UnitLength;
import org.rapidbeans.domain.org.Sex;
import org.rapidbeans.test.Lang;
import org.rapidbeans.test.TestBean;
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

	@Test
	public void testFileProperty()
	{
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(new File("."), bean.getHomedir());
		bean.setHomedir(new File(System.getProperty("user.dir")));
		Assert.assertEquals(new File(System.getProperty("user.dir")), bean.getHomedir());
	}

	@Test(expected = ValidationException.class)
	public void testFilePropertyInvalid()
	{
		TestBean bean = new TestBean();
		Assert.assertEquals(new File("."), bean.getHomedir());
		bean.setHomedir(new File("pom.xml"));
	}

	@Test
	public void testQuantityProperty()
	{
		TestBean bean = new TestBean();
		Assert.assertEquals(new Length(new BigDecimal("1.73"), UnitLength.m), bean.getHeight());
		bean.setHeight(new Length(new BigDecimal("73"), UnitLength.cm));
		Assert.assertEquals(new Length(new BigDecimal("73"), UnitLength.cm), bean.getHeight());
	}

	@Test(expected = ValidationException.class)
	public void testQuantityInvalid()
	{
		TestBean bean = new TestBean();
		bean.setHeight(new Length(new BigDecimal("3.05"), UnitLength.m));
	}

	@Test
	public void testAssociationPropertySingle()
	{
		TestBeanSimple father = new TestBeanSimple();
		TestBeanSimple son = new TestBeanSimple();
		Assert.assertNull(son.getFather());
		Assert.assertNull(father.getChildren());
		son.setFather(father);
		Assert.assertSame(father, son.getFather());
		Assert.assertEquals(1, father.getChildren().size());
		Assert.assertSame(son, father.getChildren().get(0));
	}

	@Test(expected = ValidationException.class)
	public void testAssociationPropertySingleInvalid()
	{
		TestBeanSimple father = new TestBeanSimple();
		TestBeanSimple son = new TestBeanSimple();
		son.setFather(father);
		Assert.assertEquals(1, father.getChildren().size());
		son.setFather(null);
	}

	@Test
	public void testAssociationPropertyMultiple()
	{
		TestBeanSimple father = new TestBeanSimple();
		Assert.assertEquals(null, father.getChildren());
		TestBeanSimple son1 = new TestBeanSimple();
		Assert.assertNull(son1.getFather());
		TestBeanSimple son2 = new TestBeanSimple();
		Assert.assertNull(son2.getFather());
		TestBeanSimple son3 = new TestBeanSimple();
		Assert.assertNull(son3.getFather());
		TestBeanSimple son4 = new TestBeanSimple();
		Assert.assertNull(son4.getFather());
		father.setChildren(Arrays.asList(new TestBeanSimple[] {
				son1, son2, son3, son4
		}));
		Assert.assertEquals(4, father.getChildren().size());
		Assert.assertSame(son1, father.getChildren().get(0));
		Assert.assertSame(son2, father.getChildren().get(1));
		Assert.assertSame(son3, father.getChildren().get(2));
		Assert.assertSame(son4, father.getChildren().get(3));
	}

	@Test(expected = ValidationException.class)
	public void testAssociationPropertyMultipleInvalid()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.setChildren(Arrays.asList(new TestBeanSimple[] {
				new TestBeanSimple(),
				new TestBeanSimple(),
				new TestBeanSimple(),
				new TestBeanSimple(),
				new TestBeanSimple()
		}));
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
