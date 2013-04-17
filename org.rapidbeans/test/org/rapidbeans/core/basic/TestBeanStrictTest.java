package org.rapidbeans.core.basic;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeRapidEnum;
import org.rapidbeans.core.util.Version;
import org.rapidbeans.domain.math.Length;
import org.rapidbeans.domain.math.UnitLength;
import org.rapidbeans.domain.org.Sex;
import org.rapidbeans.test.Lang;
import org.rapidbeans.test.TestBean;

/**
 * Unit Test for domain class TestBean (strict implementation).
 * 
 * @author Martin Bluemel
 */
public class TestBeanStrictTest {

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

	@Test
	public void testStringProperty() throws SecurityException, NoSuchFieldException, ParseException {
		TestBean bean = new TestBean();
		Assert.assertEquals("Ismaning", bean.getCity());
		bean.setCity("Mannheim");
		Assert.assertEquals("Mannheim", bean.getCity());
	}

	@Test(expected = ValidationException.class)
	public void testStringPropertyInvalid() {
		TestBean bean = new TestBean();
		bean.setEmail("xyz");
	}

	@Test
	public void testDateProperty() throws SecurityException, NoSuchFieldException, ParseException {
		TestBean bean = new TestBean();
		Assert.assertEquals(DFDATE.parse("14.10.1964"), bean.getDateofbirth());
		bean.setDateofbirth(DFDATE.parse("15.10.1964"));
		Assert.assertEquals(DFDATE.parse("15.10.1964"), bean.getDateofbirth());
	}

	@Test(expected = ValidationException.class)
	public void testDatePropertyInvalid() throws ParseException {
		TestBean bean = new TestBean();
		bean.setDateofbirth(DFDATE.parse("14.10.1700"));
	}

	@Test
	public void testBooleanProperty() throws SecurityException, NoSuchFieldException, ParseException {
		TestBean bean = new TestBean();
		Assert.assertEquals(true, bean.getMarried());
		bean.setMarried(false);
		Assert.assertEquals(false, bean.getMarried());
	}

	@Test(expected = ValidationException.class)
	public void testBooleanPropertyInvalid() throws ParseException {
		TestBean bean = new TestBean();
		bean.setPropValue("married", null);
	}

	@Test
	public void testIntegerProperty() throws SecurityException, NoSuchFieldException, ParseException {
		TestBean bean = new TestBean();
		Assert.assertEquals(43, bean.getShoesize());
		bean.setShoesize(40);
		Assert.assertEquals(40, bean.getShoesize());
	}

	@Test(expected = ValidationException.class)
	public void testIntegerPropertyInvalid() throws SecurityException, NoSuchFieldException, ParseException {
		TestBean bean = new TestBean();
		Assert.assertEquals(43, bean.getShoesize());
		bean.setShoesize(5);
	}

	@Test
	public void testChoicePropertySingle()
	{
		TestBean bean = new TestBean();
		Assert.assertEquals(Sex.male, bean.getSex());
		bean.setSex(Sex.female);
		Assert.assertEquals(Sex.female, bean.getSex());
	}

	@Test
	public void testChoicePropertyMultiple()
	{
		TypeRapidEnum tlang = TypeRapidEnum.forName("org.rapidbeans.test.Lang");
		TestBean bean = new TestBean();
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
		TestBean bean = new TestBean();
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
	public void testVersionProperty()
	{
		TestBean bean = new TestBean();
		Assert.assertEquals(new Version("2.0"), bean.getVersion());
		bean.setVersion(new Version("3.0.0"));
		Assert.assertEquals(new Version("3.0.0"), bean.getVersion());
	}

	@Test(expected = ValidationException.class)
	public void testVersionInvalid()
	{
		TestBean bean = new TestBean();
		bean.setVersion(null);
	}

	@Test
	public void testUrlProperty() throws MalformedURLException
	{
		TestBean bean = new TestBean();
		Assert.assertEquals(new URL("http://www.rapidbeans.org"), bean.getWebaddress());
		bean.setWebaddress(new URL("http://www.martin-bluemel.de"));
		Assert.assertEquals(new URL("http://www.martin-bluemel.de"), bean.getWebaddress());
	}

	@Test(expected = ValidationException.class)
	public void testUrlInvalid()
	{
		TestBean bean = new TestBean();
		bean.setWebaddress(null);
	}

	@Test
	public void testAssociationPropertySingle()
	{
		TestBean father = new TestBean();
		TestBean son = new TestBean();
		Assert.assertNull(son.getFather());
		Assert.assertNull(father.getChildren());
		son.setFather(father);
		Assert.assertSame(father, son.getFather());
		Assert.assertEquals(1, father.getChildren().size());
		Assert.assertSame(son, father.getChildren().get(0));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testAssociationPropertySingleGenericAccess()
	{
		TestBean father = new TestBean();
		TestBean son = new TestBean();
		Assert.assertNull(son.getPropValue("father"));
		Assert.assertNull(father.getPropValue("children"));
		son.setPropValue("father", father);
		Assert.assertSame(father, son.getFather());
		Assert.assertEquals(1, ((List<TestBean>) father.getPropValue("children")).size());
		Assert.assertSame(son, ((List<TestBean>) father.getPropValue("children")).get(0));
	}

	@Test(expected = ValidationException.class)
	public void testAssociationPropertySingleInvalid()
	{
		TestBean father = new TestBean();
		TestBean son = new TestBean();
		son.setFather(father);
		Assert.assertEquals(1, father.getChildren().size());
		son.setFather(null);
	}

	@Test(expected = ValidationException.class)
	public void testAssociationPropertySingleInvalidGenericAccess()
	{
		TestBean father = new TestBean();
		TestBean son = new TestBean();
		son.setFather(father);
		Assert.assertEquals(1, father.getChildren().size());
		son.setPropValue("father", null);
	}

	@Test
	public void testAssociationPropertyMultiple()
	{
		TestBean father = new TestBean();
		Assert.assertNull(father.getChildren());
		TestBean son1 = new TestBean();
		Assert.assertNull(son1.getFather());
		TestBean son2 = new TestBean();
		Assert.assertNull(son2.getFather());
		TestBean son3 = new TestBean();
		Assert.assertNull(son3.getFather());
		TestBean son4 = new TestBean();
		Assert.assertNull(son4.getFather());
		father.setChildren(Arrays.asList(new TestBean[] {
				son1, son2, son3, son4
		}));
		Assert.assertEquals(4, father.getChildren().size());
		Assert.assertSame(son1, father.getChildren().get(0));
		Assert.assertSame(son2, father.getChildren().get(1));
		Assert.assertSame(son3, father.getChildren().get(2));
		Assert.assertSame(son4, father.getChildren().get(3));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAssociationPropertyMultipleGenericAcces()
	{
		TestBean father = new TestBean();
		Assert.assertEquals(null, father.getPropValue("children"));
		TestBean son1 = new TestBean();
		Assert.assertNull(son1.getPropValue("father"));
		TestBean son2 = new TestBean();
		Assert.assertNull(son2.getPropValue("father"));
		TestBean son3 = new TestBean();
		Assert.assertNull(son3.getPropValue("father"));
		TestBean son4 = new TestBean();
		Assert.assertNull(son4.getPropValue("father"));
		father.setPropValue("children", Arrays.asList(new TestBean[] {
				son1, son2, son3, son4
		}));
		Assert.assertEquals(4, ((ReadonlyListCollection<TestBean>) father.getPropValue("children")).size());
		Assert.assertSame(son1, ((ReadonlyListCollection<TestBean>) father.getPropValue("children")).get(0));
		Assert.assertSame(son2, ((ReadonlyListCollection<TestBean>) father.getPropValue("children")).get(1));
		Assert.assertSame(son3, ((ReadonlyListCollection<TestBean>) father.getPropValue("children")).get(2));
		Assert.assertSame(son4, ((ReadonlyListCollection<TestBean>) father.getPropValue("children")).get(3));
	}

	@Test(expected = ValidationException.class)
	public void testAssociationPropertyMultipleInvalid()
	{
		TestBean bean = new TestBean();
		bean.setChildren(Arrays.asList(new TestBean[] {
				new TestBean(),
				new TestBean(),
				new TestBean(),
				new TestBean(),
				new TestBean()
		}));
	}

	@Test(expected = ValidationException.class)
	public void testAssociationPropertyMultipleInvalidGenericAcces()
	{
		TestBean bean = new TestBean();
		bean.setPropValue("children", Arrays.asList(new TestBean[] {
				new TestBean(),
				new TestBean(),
				new TestBean(),
				new TestBean(),
				new TestBean()
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
