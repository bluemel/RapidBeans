package org.rapidbeans.core.basic;

import org.rapidbeans.core.basic.GenericBean;
import org.rapidbeans.core.basic.IdGeneratorUuid;
import org.rapidbeans.core.basic.IdUuid;
import org.rapidbeans.test.TestHelper;

import junit.framework.TestCase;

/**
 * UnitTests für IdUid.
 *
 * @author Martin Bluemel
 */
public class IdUidTest extends TestCase {

    /**
     * Test method for toString().
     */
    public void testToString() {
        GenericBean bean = this.createTestBean("Meyer", "Michael", "19651020");
        bean.getType().setIdGenerator(new IdGeneratorUuid());
        String sUid1 = new IdUuid(bean, null).toString();
        String sUid2 = new IdUuid(bean, null).toString();
        assertTrue(!(sUid1.equals(sUid2)));
    }

    /**
     * Test method for IdUid.BBIdUid(String).
     */
    public void testBBIdUidString() {
        IdUuid idUid = new IdUuid(null, "353f0fe0-04e8-11da-9131-0002a5d5c51b");
        assertEquals("353f0fe0-04e8-11da-9131-0002a5d5c51b", idUid.toString());
    }


    /**
     * create a generic TestBean.
     * @param name the last name
     * @param prename the first name
     * @param dateofbirth the date of birth
     * @return the test bean
     */
    private GenericBean createTestBean(final String name,
            final String prename, final String dateofbirth) {
        String descr = "<beantype name=\"TestBean\">"
                + "<property name=\"name\" key=\"true\"/>"
                + "<property name=\"prename\" key=\"true\"/>"
                + "<property name=\"dateofbirth\" type=\"date\" key=\"true\"/>"
            + "</beantype>";
        GenericBean bean = TestHelper.createGenericBeanInstance(descr);
        bean.setPropValue("name", name);
        bean.setPropValue("prename", prename);
        bean.setPropValue("dateofbirth", dateofbirth);
        return bean;
    }
}
