package org.rapidbeans.datasource;

import java.io.File;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.datasource.query.Query;

public class FilterTest extends TestCase {

    protected void setUp() throws Exception {
        if (RapidBeansTypeLoader.getInstance().getXmlRootElementBinding(
                "addressbookdb") == null) {
            RapidBeansTypeLoader.getInstance().addXmlRootElementBinding(
                    "addressbookdb",
                    "org.rapidbeans.test.addressbook5.AddressbookDb");
        }
    }

    public void testExcludes01() {
        final Document doc = new Document(new File(
                "../org.rapidbeans/testdata/addressbook5/adrbookdb.xml"));
        final Query query = new Query(
                "org.rapidbeans.test.addressbook5.User");
        assertEquals(5, doc.findBeansByQuery(query).size());
        final Filter filter = new Filter();
        filter.addExcludes("org.rapidbeans.test.addressbook5.User");
        filter.setDocument(doc);
        RapidBean root = doc.getRoot();
        assertTrue(filter.applies(root));
        for (RapidBean bean : doc.findBeansByType(
                "org.rapidbeans.test.addressbook5.User")) {
            assertFalse(filter.applies(bean));
        }
    }
}
