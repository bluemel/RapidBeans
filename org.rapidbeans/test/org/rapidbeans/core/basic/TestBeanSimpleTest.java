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

	@Test
	public void testStringProperty() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals("Ismaning", bean.getCity());
		bean.setCity("Mannheim");
		Assert.assertEquals("Mannheim", bean.getCity());
	}

	@Test
	public void testStringPropertyGenericAccess() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals("Ismaning", bean.getCity());
		bean.setPropValue("city", "Mannheim");
		Assert.assertEquals("Mannheim", bean.getPropValue("city"));
		bean.getProperty("city").setValue("Kaiserslautern");
		Assert.assertEquals("Kaiserslautern", bean.getProperty("city").getValue());
	}

	@Test(expected = ValidationException.class)
	public void testStringPropertyInvalid() {
		TestBeanSimple bean = new TestBeanSimple();
		bean.setEmail("xyz");
	}

	@Test(expected = ValidationException.class)
	public void testStringPropertyInvalidGenericAccess() {
		TestBeanSimple bean = new TestBeanSimple();
		bean.setPropValue("email", "xyz");
	}

	@Test
	public void testDateProperty() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(DFDATE.parse("14.10.1964"), bean.getDateofbirth());
		bean.setDateofbirth(DFDATE.parse("15.10.1964"));
		Assert.assertEquals(DFDATE.parse("15.10.1964"), bean.getDateofbirth());
	}

	@Test
	public void testDatePropertyGenericAccess() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(DFDATE.parse("14.10.1964"), bean.getPropValue("dateofbirth"));
		bean.setPropValue("dateofbirth", DFDATE.parse("15.10.1964"));
		Assert.assertEquals(DFDATE.parse("15.10.1964"), bean.getPropValue("dateofbirth"));
		bean.getProperty("dateofbirth").setValue(DFDATE.parse("16.10.1964"));
		Assert.assertEquals(DFDATE.parse("16.10.1964"), bean.getProperty("dateofbirth").getValue());
	}

	@Test(expected = ValidationException.class)
	public void testDatePropertyInvalid() throws ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		bean.setDateofbirth(DFDATE.parse("14.10.1700"));
	}

	@Test(expected = ValidationException.class)
	public void testDatePropertyInvalidGenericAccess() throws ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		bean.setPropValue("dateofbirth", DFDATE.parse("14.10.1700"));
	}

	@Test
	public void testBooleanProperty() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(true, bean.getMarried());
		bean.setMarried(false);
		Assert.assertEquals(false, bean.getMarried());
	}

	@Test
	public void testBooleanPropertyGenericAccess() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(true, bean.getPropValue("married"));
		bean.setPropValue("married", false);
		Assert.assertEquals(false, bean.getPropValue("married"));
		bean.getProperty("married").setValue(false);
		Assert.assertEquals(false, bean.getProperty("married").getValue());
	}

	@Test(expected = ValidationException.class)
	public void testBooleanPropertyInvalidGenericAccess() throws ParseException {
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

	@Test
	public void testIntegerPropertyGenericAccess() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(43, bean.getPropValue("shoesize"));
		bean.setPropValue("shoesize", 40);
		Assert.assertEquals(40, bean.getPropValue("shoesize"));
		bean.getProperty("shoesize").setValue(41);
		Assert.assertEquals(41, bean.getProperty("shoesize").getValue());
	}

	@Test(expected = ValidationException.class)
	public void testIntegerPropertyInvalid() throws SecurityException, NoSuchFieldException, ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(43, bean.getShoesize());
		bean.setShoesize(5);
	}

	@Test(expected = ValidationException.class)
	public void testIntegerPropertyInvalidGenericAccess() throws SecurityException, NoSuchFieldException,
			ParseException {
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(43, bean.getPropValue("shoesize"));
		bean.setPropValue("shoesize", 5);
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
	@SuppressWarnings("unchecked")
	public void testChoicePropertySingleGenericAccess()
	{
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(Sex.male, ((ReadonlyListCollection<RapidEnum>) bean.getPropValue("sex")).get(0));
		bean.setPropValue("sex", Sex.female);
		Assert.assertEquals(Sex.female, ((ReadonlyListCollection<RapidEnum>) bean.getProperty("sex").getValue()).get(0));
	}

	@Test(expected = ValidationException.class)
	public void testChoicePropertySingleInvalid()
	{
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(Sex.male, bean.getSex());
		bean.setSex(null);
	}

	@Test(expected = ValidationException.class)
	public void testChoicePropertySingleInvalidGenericAccess()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.setPropValue("sex", null);
	}

	@Test(expected = ValidationException.class)
	public void testChoicePropertySingleInvalidGenericAccess2()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.getProperty("sex").setValue(null);
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

	@SuppressWarnings("unchecked")
	@Test
	public void testChoicePropertyMultipleGenericAccess()
	{
		TypeRapidEnum tlang = TypeRapidEnum.forName("org.rapidbeans.test.Lang");
		TestBeanSimple bean = new TestBeanSimple();
		assertListsEqual(Arrays.asList(new RapidEnum[] {
				tlang.elementOf("german"),
				tlang.elementOf("english"),
				tlang.elementOf("french"),
		}), (List<RapidEnum>) bean.getPropValue("languages"));
		assertListsEqual(Arrays.asList(new RapidEnum[] {
				Lang.german, Lang.english, Lang.french
		}), (List<RapidEnum>) bean.getPropValue("languages"));
		bean.setPropValue("languages", Arrays.asList(new Lang[] { Lang.chinese, Lang.spanish }));
		assertListsEqual(Arrays.asList(new RapidEnum[] {
				Lang.chinese, Lang.spanish
		}), (List<RapidEnum>) bean.getPropValue("languages"));
		bean.getProperty("languages").setValue(Arrays.asList(new Lang[] { Lang.chinese }));
		assertListsEqual(Arrays.asList(new RapidEnum[] { Lang.chinese }),
				(List<RapidEnum>) bean.getProperty("languages").getValue());
	}

	@Test(expected = ValidationException.class)
	public void testChoicePropertyMultipleInvalid()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.setLanguages(null);
	}

	@Test(expected = ValidationException.class)
	public void testChoicePropertyMultipleInvalidGenericAccess()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.setPropValue("languages", null);
	}

	@Test(expected = ValidationException.class)
	public void testChoicePropertyMultipleInvalidGenericAccess2()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.getProperty("languages").setValue(null);
	}

	@Test
	public void testFileProperty()
	{
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(new File("."), bean.getHomedir());
		bean.setHomedir(new File(System.getProperty("user.dir")));
		Assert.assertEquals(new File(System.getProperty("user.dir")), bean.getHomedir());
	}

	@Test
	public void testFilePropertyGenericAccess()
	{
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(new File("."), bean.getPropValue("homedir"));
		bean.setPropValue("homedir", new File(System.getProperty("user.dir")));
		Assert.assertEquals(new File(System.getProperty("user.dir")), bean.getPropValue("homedir"));
		bean.getProperty("homedir").setValue(new File(System.getProperty("user.dir") + "/src"));
		Assert.assertEquals(new File(System.getProperty("user.dir") + "/src"), bean.getProperty("homedir").getValue());
	}

	@Test(expected = ValidationException.class)
	public void testFilePropertyInvalid()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.setHomedir(new File("pom.xml"));
	}

	@Test(expected = ValidationException.class)
	public void testFilePropertyInvalidGenericAccess()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.setPropValue("homedir", new File("pom.xml"));
	}

	@Test(expected = ValidationException.class)
	public void testFilePropertyInvalidGenericAccess2()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.getProperty("homedir").setValue(new File("pom.xml"));
	}

	@Test
	public void testQuantityProperty()
	{
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(new Length(new BigDecimal("1.73"), UnitLength.m), bean.getHeight());
		bean.setHeight(new Length(new BigDecimal("73"), UnitLength.cm));
		Assert.assertEquals(new Length(new BigDecimal("73"), UnitLength.cm), bean.getHeight());
	}

	@Test
	public void testQuantityPropertyGenericAccess()
	{
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(new Length(new BigDecimal("1.73"), UnitLength.m), bean.getPropValue("height"));
		bean.setPropValue("height", new Length(new BigDecimal("73"), UnitLength.cm));
		Assert.assertEquals(new Length(new BigDecimal("73"), UnitLength.cm), bean.getPropValue("height"));
		bean.getProperty("height").setValue(new Length(new BigDecimal("74"), UnitLength.cm));
		Assert.assertEquals(new Length(new BigDecimal("74"), UnitLength.cm), bean.getProperty("height").getValue());
	}

	@Test(expected = ValidationException.class)
	public void testQuantityPropertyInvalid()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.setHeight(new Length(new BigDecimal("3.05"), UnitLength.m));
	}

	@Test(expected = ValidationException.class)
	public void testQuantityPropertyInvalidGenericAccess()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.setPropValue("height", new Length(new BigDecimal("3.05"), UnitLength.m));
	}

	@Test(expected = ValidationException.class)
	public void testQuantityPropertyInvalidGenericAccess2()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.getProperty("height").setValue(new Length(new BigDecimal("3.05"), UnitLength.m));
	}

	@Test
	public void testVersionProperty()
	{
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(new Version("2.0"), bean.getVersion());
		bean.setVersion(new Version("3.0.0"));
		Assert.assertEquals(new Version("3.0.0"), bean.getVersion());
	}

	@Test
	public void testVersionPropertyGenericAccess()
	{
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(new Version("2.0"), bean.getPropValue("version"));
		bean.setPropValue("version", new Version("3.0.0"));
		Assert.assertEquals(new Version("3.0.0"), bean.getPropValue("version"));
		bean.getProperty("version").setValue(new Version("3.1.0"));
		Assert.assertEquals(new Version("3.1.0"), bean.getProperty("version").getValue());
	}

	@Test(expected = ValidationException.class)
	public void testVersionInvalid()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.setVersion(null);
	}

	@Test(expected = ValidationException.class)
	public void testVersionInvalidGenericAccess()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.setPropValue("version", null);
	}

	@Test(expected = ValidationException.class)
	public void testVersionInvalidGenericAccess2()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.getProperty("version").setValue(null);
	}

	@Test
	public void testUrlProperty() throws MalformedURLException
	{
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(new URL("http://www.rapidbeans.org"), bean.getWebaddress());
		bean.setWebaddress(new URL("http://www.martin-bluemel.de"));
		Assert.assertEquals(new URL("http://www.martin-bluemel.de"), bean.getWebaddress());
	}

	@Test
	public void testUrlPropertyGenericAccess() throws MalformedURLException
	{
		TestBeanSimple bean = new TestBeanSimple();
		Assert.assertEquals(new URL("http://www.rapidbeans.org"), bean.getPropValue("webaddress"));
		bean.setPropValue("webaddress", new URL("http://www.martin-bluemel.de"));
		Assert.assertEquals(new URL("http://www.martin-bluemel.de"), bean.getPropValue("webaddress"));
		bean.getProperty("webaddress").setValue(new URL("http://www.martin-bluemel.de"));
		Assert.assertEquals(new URL("http://www.martin-bluemel.de"), bean.getProperty("webaddress").getValue());
	}

	@Test(expected = ValidationException.class)
	public void testUrlInvalid()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.setWebaddress(null);
	}

	@Test(expected = ValidationException.class)
	public void testUrlInvalidGenericAccess()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.setPropValue("webaddress", null);
	}

	@Test(expected = ValidationException.class)
	public void testUrlInvalidGenericAccess2()
	{
		TestBeanSimple bean = new TestBeanSimple();
		bean.getProperty("webaddress").setValue(null);
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

	@Test
	@SuppressWarnings("unchecked")
	public void testAssociationPropertySingleGenericAccess()
	{
		TestBeanSimple father = new TestBeanSimple();
		TestBeanSimple son = new TestBeanSimple();
		Assert.assertNull(son.getPropValue("father"));
		Assert.assertNull(father.getPropValue("children"));
		son.setPropValue("father", father);
		Assert.assertSame(father, son.getFather());
		Assert.assertEquals(1, ((List<TestBean>) father.getPropValue("children")).size());
		Assert.assertSame(son, ((List<TestBean>) father.getPropValue("children")).get(0));
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

	@SuppressWarnings("unchecked")
	@Test
	public void testAssociationPropertyMultipleGenericAcces()
	{
		TestBeanSimple father = new TestBeanSimple();
		Assert.assertEquals(null, father.getPropValue("children"));
		TestBeanSimple son1 = new TestBeanSimple();
		Assert.assertNull(son1.getPropValue("father"));
		TestBeanSimple son2 = new TestBeanSimple();
		Assert.assertNull(son2.getPropValue("father"));
		TestBeanSimple son3 = new TestBeanSimple();
		Assert.assertNull(son3.getPropValue("father"));
		TestBeanSimple son4 = new TestBeanSimple();
		Assert.assertNull(son4.getPropValue("father"));
		father.setPropValue("children", Arrays.asList(new TestBeanSimple[] {
				son1, son2, son3, son4
		}));
		Assert.assertEquals(4, ((ReadonlyListCollection<TestBeanSimple>) father.getPropValue("children")).size());
		Assert.assertSame(son1, ((ReadonlyListCollection<TestBeanSimple>) father.getPropValue("children")).get(0));
		Assert.assertSame(son2, ((ReadonlyListCollection<TestBeanSimple>) father.getPropValue("children")).get(1));
		Assert.assertSame(son3, ((ReadonlyListCollection<TestBeanSimple>) father.getPropValue("children")).get(2));
		Assert.assertSame(son4, ((ReadonlyListCollection<TestBeanSimple>) father.getPropValue("children")).get(3));
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

	@Test(expected = ValidationException.class)
	public void testAssociationPropertyMultipleInvalidGenericAcces()
	{
		TestBeanSimple bean = new TestBeanSimple();
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
