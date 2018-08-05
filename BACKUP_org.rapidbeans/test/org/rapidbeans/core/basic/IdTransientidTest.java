package org.rapidbeans.core.basic;

import junit.framework.TestCase;

import org.rapidbeans.test.TestHelper;

/**
 * UnitTests f�r IdTransientid.
 * 
 * @author Martin Bluemel
 */
public class IdTransientidTest extends TestCase {

	/**
	 * Test method for toString().
	 */
	public void testToString() {
		GenericBean bean1 = this.createTestBean("Meyer", "Michael", "19651020");
		GenericBean bean2 = this.createTestBean("Veith", "Uwe", "19640810");
		IdTransientid id1 = new IdTransientid(bean1, null);
		IdTransientid id2 = new IdTransientid(bean2, null);
		assertTrue(!id1.equals(id2));
		// the string conversion for references relies on the object
		// method hashcode() for that object.
		assertTrue(!id1.toString().equals(id2.toString()));
	}

	/**
	 * Test method for IdUid.BBIdUid(String).
	 */
	public void testBBIdUidString() {
		IdUuid idUid = new IdUuid(null, "353f0fe0-04e8-11da-9131-0002a5d5c51b");
		assertEquals(null, "353f0fe0-04e8-11da-9131-0002a5d5c51b", idUid.toString());
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
	private GenericBean createTestBean(final String name, final String prename, final String dateofbirth) {
		String descr = "<beantype name=\"TestBean\">" + "<property name=\"name\" key=\"true\"/>"
				+ "<property name=\"prename\" key=\"true\"/>"
				+ "<property name=\"dateofbirth\" type=\"date\" key=\"true\"/>" + "</beantype>";
		GenericBean bean = TestHelper.createGenericBeanInstance(descr);
		bean.setPropValue("name", name);
		bean.setPropValue("prename", prename);
		bean.setPropValue("dateofbirth", dateofbirth);
		return bean;
	}
}
