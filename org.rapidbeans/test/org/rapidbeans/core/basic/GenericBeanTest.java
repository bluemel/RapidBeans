package org.rapidbeans.core.basic;

import java.util.List;

import junit.framework.TestCase;

import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.domain.org.Sex;
import org.rapidbeans.test.TestHelper;

/**
 * Test generic beans.
 *
 * @author Martin Bluemel
 */
public class GenericBeanTest extends TestCase {

    /**
     * Test of createInstance(XmlNode).
     *
     * This Method is intended to be used for Unit Tests.
     * The bean's type is not registered
     * at the bean type loader (RapidBeansTypeLoader).
     */
    public void testCreateInstanceDescr() {
        String descr = "<beantype name=\"TestBean1\"/>";
        GenericBean bean1 = TestHelper.createGenericBeanInstance(descr);
        assertEquals("TestBean1",  bean1.getType().getName());
    }

    /**
     * Test of createInstance(XmlNode).
     *
     * This Method is intended to be used for Unit Tests.
     * The bean's type is not registered
     * at the bean type loader (RapidBeansTypeLoader).
     */
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
    public void testCreateInstanceDescrPropsList() {
        String descr = "<beantype name=\"TestBean\">"
                        + "<property name=\"xxx\" key=\"true\"/>"
                        + "<property name=\"yyy\" type=\"integer\" default=\"85737\"/>"
                        + "<property name=\"zzz\" pattern=\"\\A[.\\-0-9A-Za-z]*@[.\\-0-9A-Za-z]*\\z\"/>"
                    + "</beantype>";
        GenericBean bean = TestHelper.createGenericBeanInstance(descr);
        assertEquals(3, bean.getPropertyList().size());
    }

    /**
     * Test of createInstance(XmlNode).
     */
    public void testCreateInstanceDescrPropsDefault() {
        String descr = "<beantype name=\"TestBean\">"
                        + "<property name=\"xxx\" key=\"true\"/>"
                        + "<property name=\"yyy\" type=\"integer\" default=\"85737\"/>"
                        + "<property name=\"zzz\" pattern=\"\\A[.\\-0-9A-Za-z]*@[.\\-0-9A-Za-z]*\\z\"/>"
                    + "</beantype>";
        GenericBean bean = TestHelper.createGenericBeanInstance(descr);
        assertEquals(85737, bean.getProperty("yyy").getValue());
    }

    /**
     * Test of createInstance(XmlNode).
     */
    public void testCreateInstanceDescrPropsSetValueOk() {
        String descr = "<beantype name=\"TestBean\">"
                        + "<property name=\"xxx\" key=\"true\"/>"
                        + "<property name=\"yyy\" type=\"integer\" default=\"85737\"/>"
                        + "<property name=\"zzz\" pattern=\"\\A[.\\-0-9A-Za-z]*@[.\\-0-9A-Za-z]*\\z\"/>"
                    + "</beantype>";
        GenericBean bean = TestHelper.createGenericBeanInstance(descr);
        bean.setPropValue("zzz", "martin.bluemel@web.de");
        assertEquals("martin.bluemel@web.de", bean.getProperty("zzz").getValue());
    }

    /**
     * Test of createInstance(XmlNode).
     */
    public void testCreateInstanceDescrPropsSetValueWrong() {
        String descr = "<beantype name=\"TestBean\">"
                        + "<property name=\"xxx\" key=\"true\"/>"
                        + "<property name=\"yyy\" type=\"integer\" default=\"85737\"/>"
                        + "<property name=\"zzz\" pattern=\"\\A[.\\-0-9A-Za-z]*@[.\\-0-9A-Za-z]*\\z\"/>"
                    + "</beantype>";
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
     * This Method can be used for generic beans
     * A generic bean has a pure declarative type without
     * a Java class definition).
     * If you use this method the bean's type is registered a the
     * bean type loader.
     */
    public void testCreateInstanceFromModels() {
        GenericBean bean1 =
            (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
        assertEquals("org.rapidbeans.test.TestBeanGen", bean1.getType().getName());
        GenericBean bean2 =
            (GenericBean) RapidBeanImplStrict.createInstance("org.rapidbeans.test.TestBeanGen");
        assertEquals("org.rapidbeans.test.TestBeanGen", bean2.getType().getName());
        // approve that the type instance is registered just once
        assertSame(bean1.getType(), bean2.getType());
    }

    @SuppressWarnings("unchecked")
	public void testDefaultValue() {
        GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance(
                "org.rapidbeans.test.TestBeanGen");
        Sex defaultSex = (Sex) ((List<RapidEnum>) bean.getPropValue("sex")).get(0);
        assertSame(Sex.male, defaultSex);
    }

    @SuppressWarnings("unchecked")
	public void testOverriding() {
        GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance(
                "org.rapidbeans.test.TestBeanExtended1aGen");
        Sex defaultSex = (Sex) ((List<RapidEnum>) bean.getPropValue("sex")).get(0);
        assertSame(Sex.female, defaultSex);
    }
}
