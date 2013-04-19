package org.rapidbeans.core.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import org.rapidbeans.test.TestHelper;

/**
 * Test generic beans.
 * 
 * @author Martin Bluemel
 */
public class GenericBeanTest {

	/**
	 * Test of createInstance(XmlNode).
	 * 
	 * This Method is intended to be used for Unit Tests. The bean's type is not
	 * registered at the bean type loader (RapidBeansTypeLoader).
	 */
	@Test
	public void testCreateInstanceDescr() {
		String descr = "<beantype name=\"TestBean1\"/>";
		GenericBean bean1 = TestHelper.createGenericBeanInstance(descr);
		assertEquals("TestBean1", bean1.getType().getName());
	}

	/**
	 * Test of createInstance(XmlNode).
	 * 
	 * This Method is intended to be used for Unit Tests. The bean's type is not
	 * registered at the bean type loader (RapidBeansTypeLoader).
	 */
	@Test
	public void testCreateInstanceDescrTypeLoader() {
		String descr = "<beantype name=\"TestBean\"/>";
		GenericBean bean1 = TestHelper.createGenericBeanInstance(descr);
		GenericBean bean2 = TestHelper.createGenericBeanInstance(descr);
		// approve that the type instances are not registered
		assertTrue(bean1.getType() != bean2.getType());
	}

	/**
	 * Test of createInstance(XmlNode).
	 */
	@Test
	public void testCreateInstanceDescrPropsList() {
		String descr = "<beantype name=\"TestBean\">" + "<property name=\"xxx\" key=\"true\"/>"
				+ "<property name=\"yyy\" type=\"integer\" default=\"85737\"/>"
				+ "<property name=\"zzz\" pattern=\"\\A[.\\-0-9A-Za-z]*@[.\\-0-9A-Za-z]*\\z\"/>" + "</beantype>";
		GenericBean bean = TestHelper.createGenericBeanInstance(descr);
		assertEquals(3, bean.getPropertyList().size());
	}

	/**
	 * Test of createInstance(XmlNode).
	 */
	@Test
	public void testCreateInstanceDescrPropsDefault() {
		String descr = "<beantype name=\"TestBean\">" + "<property name=\"xxx\" key=\"true\"/>"
				+ "<property name=\"yyy\" type=\"integer\" default=\"85737\"/>"
				+ "<property name=\"zzz\" pattern=\"\\A[.\\-0-9A-Za-z]*@[.\\-0-9A-Za-z]*\\z\"/>" + "</beantype>";
		GenericBean bean = TestHelper.createGenericBeanInstance(descr);
		assertEquals(85737, bean.getProperty("yyy").getValue());
	}

	/**
	 * Test of createInstance(XmlNode).
	 */
	@Test
	public void testCreateInstanceDescrPropsSetValueOk() {
		String descr = "<beantype name=\"TestBean\">" + "<property name=\"xxx\" key=\"true\"/>"
				+ "<property name=\"yyy\" type=\"integer\" default=\"85737\"/>"
				+ "<property name=\"zzz\" pattern=\"\\A[.\\-0-9A-Za-z]*@[.\\-0-9A-Za-z]*\\z\"/>" + "</beantype>";
		GenericBean bean = TestHelper.createGenericBeanInstance(descr);
		bean.setPropValue("zzz", "martin.bluemel@web.de");
		assertEquals("martin.bluemel@web.de", bean.getProperty("zzz").getValue());
	}

	/**
	 * Test of createInstance(XmlNode).
	 */
	@Test
	public void testCreateInstanceDescrPropsSetValueWrong() {
		String descr = "<beantype name=\"TestBean\">" + "<property name=\"xxx\" key=\"true\"/>"
				+ "<property name=\"yyy\" type=\"integer\" default=\"85737\"/>"
				+ "<property name=\"zzz\" pattern=\"\\A[.\\-0-9A-Za-z]*@[.\\-0-9A-Za-z]*\\z\"/>" + "</beantype>";
		GenericBean bean = TestHelper.createGenericBeanInstance(descr);
		try {
			bean.setPropValue("zzz", "abc");
			fail("expected ValidationException");
		} catch (ValidationException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test createInstance(String typename).
	 * 
	 * This Method can be used for generic beans A generic bean has a pure
	 * declarative type without a Java class definition). If you use this method
	 * the bean's type is registered a the bean type loader.
	 */
	@Test
	public void testCreateInstanceFromModels() {
		GenericBean bean1 = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		assertEquals("org.rapidbeans.test.TestBeanGen", bean1.getType().getName());
		GenericBean bean2 = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		assertEquals("org.rapidbeans.test.TestBeanGen", bean2.getType().getName());
		// approve that the type instance is registered just once
		assertSame(bean1.getType(), bean2.getType());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testPropertyFile() {
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		assertSame(Sex.male, (Sex) ((List<RapidEnum>) bean.getPropValue("sex")).get(0));
		bean.setPropValue("sex", Sex.female);
		assertSame(Sex.female, (Sex) ((List<RapidEnum>) bean.getPropValue("sex")).get(0));
	}

	@Test
	public void testStringProperty() throws SecurityException, NoSuchFieldException, ParseException {
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertEquals("Ismaning", bean.getPropValue("city"));
		bean.setPropValue("city", "Mannheim");
		Assert.assertEquals("Mannheim", bean.getPropValue("city"));
	}

	@Test(expected = ValidationException.class)
	public void testStringPropertyInvalid() {
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		bean.setPropValue("email", "xyz");
	}

	@Test
	public void testDateProperty() throws SecurityException, NoSuchFieldException, ParseException {
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertEquals(DFDATE.parse("14.10.1964"), bean.getPropValue("dateofbirth"));
		bean.setPropValue("dateofbirth", DFDATE.parse("15.10.1964"));
		Assert.assertEquals(DFDATE.parse("15.10.1964"), bean.getPropValue("dateofbirth"));
	}

	@Test(expected = ValidationException.class)
	public void testDatePropertyInvalid() throws ParseException {
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		bean.setPropValue("dateofbirth", DFDATE.parse("14.10.1700"));
	}

	@Test
	public void testBooleanProperty() throws SecurityException, NoSuchFieldException, ParseException {
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertEquals(true, bean.getPropValue("married"));
		bean.setPropValue("married", false);
		Assert.assertEquals(false, bean.getPropValue("married"));
	}

	@Test(expected = ValidationException.class)
	public void testBooleanPropertyInvalid() throws ParseException {
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		bean.setPropValue("married", null);
	}

	@Test
	public void testIntegerProperty() throws SecurityException, NoSuchFieldException, ParseException {
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertEquals(43, bean.getPropValue("shoesize"));
		bean.setPropValue("shoesize", 40);
		Assert.assertEquals(40, bean.getPropValue("shoesize"));
	}

	@Test(expected = ValidationException.class)
	public void testIntegerPropertyInvalid() throws SecurityException, NoSuchFieldException, ParseException {
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		bean.setPropValue("shoesize", 5);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testChoicePropertySingle()
	{
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertEquals(Sex.male, ((ReadonlyListCollection<Sex>) bean.getPropValue("sex")).get(0));
		bean.setPropValue("sex", Sex.female);
		Assert.assertEquals(Sex.female, ((ReadonlyListCollection<Sex>) bean.getPropValue("sex")).get(0));
	}

	@Test(expected = ValidationException.class)
	public void testChoicePropertySingleInvalidEnumType()
	{
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		bean.setPropValue("sex", UnitLength.cm);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testChoicePropertyMultiple()
	{
		TypeRapidEnum tlang = TypeRapidEnum.forName("org.rapidbeans.test.LangGeneric");
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		assertListsEqual(Arrays.asList(new RapidEnum[] {
				tlang.elementOf("german"),
				tlang.elementOf("english"),
				tlang.elementOf("french")
		}), (List<RapidEnum>) bean.getPropValue("languages"));
		bean.setPropValue("languages", (Arrays.asList(new RapidEnum[] {
				tlang.elementOf("chinese"), tlang.elementOf("spanish") })));
		assertListsEqual(Arrays.asList(new RapidEnum[] {
				tlang.elementOf("chinese"),
				tlang.elementOf("spanish")
		}), (List<RapidEnum>) bean.getPropValue("languages"));
	}

	@Test(expected = ValidationException.class)
	public void testChoicePropertyMultipleInvalid()
	{
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		bean.setPropValue("languages", (Arrays.asList(new Lang[] { Lang.chinese, Lang.spanish })));
	}

	@Test
	public void testFileProperty()
	{
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertEquals(new File("."), bean.getPropValue("homedir"));
		bean.setPropValue("homedir", new File(System.getProperty("user.dir")));
		Assert.assertEquals(new File(System.getProperty("user.dir")), bean.getPropValue("homedir"));
	}

	@Test(expected = ValidationException.class)
	public void testFilePropertyInvalid()
	{
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		bean.setPropValue("homedir", new File("pom.xml"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testOverriding() {
		GenericBean bean = (GenericBean) RapidBeanImplStrict
				.createInstance("org.rapidbeans.test.TestBeanExtended1aGen");
		Sex defaultSex = (Sex) ((List<RapidEnum>) bean.getPropValue("sex")).get(0);
		assertSame(Sex.female, defaultSex);
	}

	@Test
	public void testQuantityProperty()
	{
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertEquals(new Length(new BigDecimal("1.73"), UnitLength.m), bean.getPropValue("height"));
		bean.setPropValue("height", new Length(new BigDecimal("73"), UnitLength.cm));
		Assert.assertEquals(new Length(new BigDecimal("73"), UnitLength.cm), bean.getPropValue("height"));
	}

	@Test(expected = ValidationException.class)
	public void testQuantityInvalid()
	{
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		bean.setPropValue("height", new Length(new BigDecimal("3.05"), UnitLength.m));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAssociationPropertySingle()
	{
		GenericBean father = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		GenericBean son = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertNull(son.getPropValue("father"));
		Assert.assertNull(father.getPropValue("children"));
		son.setPropValue("father", father);
		Assert.assertSame(father, ((List<GenericBean>) son.getPropValue("father")).get(0));
		Assert.assertEquals(1, ((List<GenericBean>) father.getPropValue("children")).size());
		Assert.assertSame(son, ((List<GenericBean>) father.getPropValue("children")).get(0));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAssociationPropertyMultiple()
	{
		GenericBean father = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertNull(father.getPropValue("children"));
		GenericBean son1 = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertNull((son1.getPropValue("father")));
		GenericBean son2 = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertNull((son2.getPropValue("father")));
		GenericBean son3 = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertNull((son3.getPropValue("father")));
		GenericBean son4 = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertNull((son4.getPropValue("father")));
		father.setPropValue("children", Arrays.asList(new GenericBean[] {
				son1, son2, son3, son4
		}));
		Assert.assertEquals(4, ((List<GenericBean>) father.getPropValue("children")).size());
		Assert.assertSame(son1, ((List<GenericBean>) father.getPropValue("children")).get(0));
		Assert.assertSame(son2, ((List<GenericBean>) father.getPropValue("children")).get(1));
		Assert.assertSame(son3, ((List<GenericBean>) father.getPropValue("children")).get(2));
		Assert.assertSame(son4, ((List<GenericBean>) father.getPropValue("children")).get(3));
	}

	@Test(expected = ValidationException.class)
	public void testAssociationPropertyMultipleInvalid()
	{
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		bean.setPropValue("children", (Arrays.asList(new TestBean[] {
				new TestBean(),
				new TestBean(),
				new TestBean(),
				new TestBean(),
				new TestBean()
		})));
	}

	@Test
	public void testVersionProperty()
	{
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertEquals(new Version("2.0"), bean.getPropValue("version"));
		bean.setPropValue("version", new Version("3.0.0"));
		Assert.assertEquals(new Version("3.0.0"), bean.getPropValue("version"));
	}

	@Test(expected = ValidationException.class)
	public void testVersionInvalid()
	{
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		bean.setPropValue("version", null);
	}

	@Test
	public void testUrlProperty() throws MalformedURLException
	{
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		Assert.assertEquals(new URL("http://www.rapidbeans.org"), bean.getPropValue("webaddress"));
		bean.setPropValue("webaddress", new URL("http://www.martin-bluemel.de"));
		Assert.assertEquals(new URL("http://www.martin-bluemel.de"), bean.getPropValue("webaddress"));
	}

	@Test(expected = ValidationException.class)
	public void testUrlInvalid()
	{
		GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
		bean.setPropValue("webaddress", null);
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
}
