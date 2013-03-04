package org.rapidbeans.core.basic;

import junit.framework.TestCase;

import org.rapidbeans.test.TestHelper;

/**
 * UnitTests f�r IdUid.
 * 
 * @author Martin Bluemel
 */
public class IdNumericTest extends TestCase {

	/**
	 * Test method for toString().
	 */
	public void testToString() {
		GenericBean bean = this.createTestBeanNumeric("Meyer", "Michael", "19651020");
		bean.getType().setIdGenerator(new IdGeneratorNumeric());
		String sNum1 = new IdNumeric(bean, null).toString();
		assertEquals("1", sNum1);
		String sNum2 = new IdNumeric(bean, null).toString();
		assertEquals("2", sNum2);
	}

	/**
	 * Test method for IdNumeric.BBIdNumeric(String).
	 */
	public void testBBIdNumericString() {
		GenericBean bean = this.createTestBeanNumeric("Meyer", "Michael", "19651020");
		bean.getType().setIdGenerator(new IdGeneratorNumeric());
		IdNumeric idNumeric = new IdNumeric(bean, "1234");
		assertEquals("1234", idNumeric.toString());
	}

	/**
	 * create a generic TestBean.
	 * 
	 * @param name
	 *            the last name
	 * @param prename
	 *            the first name
	 * @param dateofbirth
	 *            the date of birth
	 * @return the test bean
	 */
	private GenericBean createTestBeanNumeric(final String name, final String prename, final String dateofbirth) {
		String descr = "<beantype name=\"TestBean\" idtype=\"numeric\">" + "<property name=\"name\" key=\"true\"/>"
				+ "<property name=\"prename\" key=\"true\"/>"
				+ "<property name=\"dateofbirth\" type=\"date\" key=\"true\"/>" + "</beantype>";
		GenericBean bean = TestHelper.createGenericBeanInstance(descr);
		bean.setPropValue("name", name);
		bean.setPropValue("prename", prename);
		bean.setPropValue("dateofbirth", dateofbirth);
		return bean;
	}
}
