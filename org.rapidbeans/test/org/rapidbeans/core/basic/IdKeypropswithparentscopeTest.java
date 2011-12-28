package org.rapidbeans.core.basic;

import java.util.TreeSet;

import junit.framework.TestCase;

import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.type.RapidBeansType;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.test.TestHelper;

/**
 * UnitTests for IdKeypropswithparentscope.
 *
 * @author Martin Bluemel
 */
public class IdKeypropswithparentscopeTest extends TestCase {

    /**
     * Test method for toString().
     */
    public void testToString() {
        GenericBean kid = createTestBeanP0("Blümel", "Melanie", "20020831");
        assertEquals("Blümel_Melanie_20020831", kid.getIdString());
        GenericBean pa = createTestBeanP1("Blümel", "Martin", "19641014");
        assertEquals("Blümel_Martin_19641014", pa.getIdString());
        GenericBean grandma = createTestBeanP2("Blümel", "Gudrun", "19390511");
        assertSame(grandma.getId().getClass(), IdKeypropswithparentscope.class);
        assertEquals("Blümel_Gudrun_19390511", grandma.getIdString());
        grandma.setPropValue("children", pa);
        assertEquals("Blümel_Gudrun_19390511/Blümel_Martin_19641014", pa.getIdString());
        pa.setPropValue("children", kid);
        assertEquals("Blümel_Gudrun_19390511/Blümel_Martin_19641014/"
                + "Blümel_Melanie_20020831", kid.getIdString());
    }

    /**
     * Test method for equals().
     */
    public void testEqualsEquals0() {
        GenericBean kid1 = createTestBeanP0("Blümel", "Melanie", "20020831");
        GenericBean kid2 = createTestBeanP0("Blümel", "Melanie", "20020831");
        assertEquals(kid1.getId(), kid2.getId());
    }

    //    /**
    //     * Test method for equals().
    //     */
    //    public void testEqualsEquals1SameParent() {
    //        ArrayList<RapidBean> kids = new ArrayList<RapidBean>();
    //        GenericBean kid1 = createTestBeanP0("Blümel", "Melanie", "20020831");
    //        kids.add(kid1);
    //        GenericBean kid2 = createTestBeanP0("Blümel", "Melanie", "20020831");
    //        kids.add(kid2);
    //        GenericBean pa = createTestBeanP1("Blümel", "Martin", "19641014");
    //        pa.setPropValue("children", kids);
    //        assertEquals(kid1.getId(), kid2.getId());
    //    }

    //    /**
    //     * Test method for equals().
    //     */
    //    public void testEqualsDifferent() {
    //        GenericBean bean1 = createTestBean("Blümel", "Martin", "19641014");
    //        IdKeyprops id1 = new IdKeyprops(bean1, null);
    //        GenericBean bean2 = createTestBean("Blümel", "Johannes", "19641014");
    //        IdKeyprops id2 = new IdKeyprops(bean2, null);
    //        assertFalse(id1.equals(id2));
    //    }
    //
    //    /**
    //     * Test method for equals().
    //     */
    //    public void testEqualsWrongType() {
    //        TestBean bean = new TestBean("\"Blümel\" \"Martin\" \"19641014\"");
    //        bean.getType().setIdGenerator(new IdGeneratorUid());
    //        IdKeyprops id1 = new IdKeyprops(bean, null);
    //        IdUid id2 = new IdUid(bean);
    //        assertFalse(id1.equals(id2));
    //    }


    @SuppressWarnings("unchecked")
    public void testSortingSimple() {
        GenericBean p0a = createTestBeanP0("0", "a", "20100101");
        GenericBean p0b = createTestBeanP0("0", "b", "20100101");
        GenericBean p0c = createTestBeanP0("0", "c", "20100101");
        GenericBean p1 = createTestBeanP1("1", "1", "20100101");
        GenericBean p2 = createTestBeanP2("2", "2", "20100101");
        p2.setPropValue("children", p1);
        p1.setPropValue("children", new GenericBean[]{p0c, p0b, p0a});
        ReadonlyListCollection<GenericBean> list = (ReadonlyListCollection<GenericBean>)
            p1.getPropValue("children");
        assertSame(p0c, list.get(0));
        assertSame(p0b, list.get(1));
        assertSame(p0a, list.get(2));
    }

    @SuppressWarnings("unchecked")
    public void testSortingReal() {
        GenericBean p0a = createTestBeanP0("0", "a", "20100101");
        GenericBean p0b = createTestBeanP0("0", "b", "20100101");
        GenericBean p0c = createTestBeanP0("0", "c", "20100101");
        GenericBean p1 = createTestBeanP1("1", "1", "20100101");
        TypePropertyCollection colproptype = (TypePropertyCollection) p1.getProperty("children").getType();
        colproptype.setCollectionClass(TreeSet.class);
        GenericBean p2 = createTestBeanP2("2", "2", "20100101");
        p2.setPropValue("children", p1);
        p1.setPropValue("children", new GenericBean[]{p0b, p0c, p0a});
        ReadonlyListCollection<GenericBean> list = (ReadonlyListCollection<GenericBean>)
            p1.getPropValue("children");
        assertSame(p0a, list.get(0));
        assertSame(p0b, list.get(1));
        assertSame(p0c, list.get(2));
    }

    /**
     * create a generic TestBean.
     * @param name the last name
     * @param prename the first name
     * @param dateofbirth the date of birth
     * @return the test bean
     */
    private GenericBean createTestBeanP0(final String name,
            final String prename, final String dateofbirth) {
        GenericBean bean = null;
        RapidBeansType type = RapidBeansTypeLoader.getInstance().lookupType("TestBeanP0");
        if (type == null) {
            String descr = "<beantype name=\"TestBeanP0\" idtype=\"keypropswithparentscope\">"
                + "<property name=\"name\" key=\"true\"/>"
                + "<property name=\"prename\" key=\"true\"/>"
                + "<property name=\"dateofbirth\" type=\"date\" key=\"true\"/>"
                + "</beantype>";
            bean = TestHelper.createGenericBeanInstance(descr);
            type = bean.getType();
            RapidBeansTypeLoader.getInstance().registerType(type);
        } else {
            bean = new GenericBean((TypeRapidBean) type);
        }
        bean.setPropValue("name", name);
        bean.setPropValue("prename", prename);
        bean.setPropValue("dateofbirth", dateofbirth);
        return bean;
    }

    /**
     * create a generic TestBean.
     * @param name the last name
     * @param prename the first name
     * @param dateofbirth the date of birth
     * @return the test bean
     */
    private GenericBean createTestBeanP1(final String name,
            final String prename, final String dateofbirth) {
        String descr = "<beantype name=\"TestBeanP1\" idtype=\"keypropswithparentscope\">"
            + "<property name=\"name\" key=\"true\"/>"
            + "<property name=\"prename\" key=\"true\"/>"
            + "<property name=\"dateofbirth\" type=\"date\" key=\"true\"/>"
            + "<property name=\"children\" type=\"collection\" composition=\"true\" targettype=\"TestBeanP0\"/>"
            + "</beantype>";
        GenericBean bean = TestHelper.createGenericBeanInstance(descr);
        RapidBeansTypeLoader.getInstance().registerTypeIfNotRegistered("TestBeanP1", bean.getType());
        bean.setPropValue("name", name);
        bean.setPropValue("prename", prename);
        bean.setPropValue("dateofbirth", dateofbirth);
        return bean;
    }

    /**
     * create a generic TestBean.
     * @param name the last name
     * @param prename the first name
     * @param dateofbirth the date of birth
     * @return the test bean
     */
    private GenericBean createTestBeanP2(final String name,
            final String prename, final String dateofbirth) {
        String descr = "<beantype name=\"TestBeanP2\" idtype=\"keypropswithparentscope\">"
            + "<property name=\"name\" key=\"true\"/>"
            + "<property name=\"prename\" key=\"true\"/>"
            + "<property name=\"dateofbirth\" type=\"date\" key=\"true\"/>"
            + "<property name=\"children\" type=\"collection\" composition=\"true\" targettype=\"TestBeanP1\"/>"
            + "</beantype>";
        GenericBean bean = TestHelper.createGenericBeanInstance(descr);
        bean.setPropValue("name", name);
        bean.setPropValue("prename", prename);
        bean.setPropValue("dateofbirth", dateofbirth);
        return bean;
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() {
        if (RapidBeansTypeLoader.getInstance().lookupType("TestBeanP0") != null) {
            RapidBeansTypeLoader.getInstance().unregisterType("TestBeanP0");
        }
        if (RapidBeansTypeLoader.getInstance().lookupType("TestBeanP1") != null) {
            RapidBeansTypeLoader.getInstance().unregisterType("TestBeanP1");
        }
    }
}
