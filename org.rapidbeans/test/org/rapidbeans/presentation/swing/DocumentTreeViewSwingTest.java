package org.rapidbeans.presentation.swing;

import java.util.Collection;

import javax.swing.JTree;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.GenericBean;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.type.TestHelperTypeLoader;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.ApplicationManager;

/**
 * White Box GUI test.
 *
 * @author Martin Bluemel
 */
public class DocumentTreeViewSwingTest extends TestCase {

    /**
     * Select Trainer bean "Blümel" and "Dautovic" in the tree view
     * and delete them.
     * => - the beans should be deleted
     *    - the tree view should be correctly updated
     *
     * @throws InterruptedException test
     */
    public void testDeleteBeansFromTree() throws InterruptedException {
        DocumentTreeViewSwing docTreeView = PresentationSwingTestHelper.createTestTreeView();
        JTree tree = docTreeView.getTree();
        try {
            // expand "trainers" branch in the tree
            tree.expandPath(tree.getPathForRow(2));
            tree.expandPath(tree.getPathForRow(1));
            assertEquals("trainers", PresentationSwingTestHelper.getColPropName(tree, 1));
            assertEquals("Blümel", ((RapidBean) tree.getPathForRow(2)
                .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("Dahlheimer", ((RapidBean) tree.getPathForRow(3)
                .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("Dautovic", ((RapidBean) tree.getPathForRow(4)
                .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("trainingdates", PresentationSwingTestHelper.getColPropName(tree, 5));
            assertEquals("Aikido Adults I", ((RapidBean) tree.getPathForRow(6)
                    .getLastPathComponent()).getIdString());
            assertEquals(3, ((Collection<?>) ((DocumentTreeNodePropColComp)
                    tree.getPathForRow(6).getPath()[tree.getPathForRow(6).getPath().length - 2]).
                    getColProp().getValue()).size());
            assertEquals("Aikido Children", ((RapidBean) tree.getPathForRow(7)
                    .getLastPathComponent()).getIdString());
            assertEquals("Aikido Adults II", ((RapidBean) tree.getPathForRow(8)
                    .getLastPathComponent()).getIdString());
            assertNull(tree.getPathForRow(10));
            Document testDoc = PresentationSwingTestHelper.getTestDocument();
            assertNotNull(testDoc.findBean("Trainer", "Blümel_Martin"));
            assertNotNull(testDoc.findBean("Trainer", "Dautovic_Damir"));
            assertNotNull(testDoc.findBean("Trainer", "Dahlheimer_Berit"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Adults I"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Children"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Adults II"));

        } finally {
            PresentationSwingTestHelper.deleteTestTreeView();
        }
    }

    /**
     * Just present a tree with one bean with empty collection properties.
     * There have been difficulties with that.
     *
     * @throws InterruptedException test
     */
    public void testShowDocumentWithEmptyColProps() throws InterruptedException {
        DocumentTreeViewSwing docTreeView = PresentationSwingTestHelper.createTestTreeViewWithEmptyColProps();
        try {
            Document testDoc = PresentationSwingTestHelper.getTestDocument();
            GenericBean root = (GenericBean) testDoc.getRoot();
            assertEquals("BillingPeriod", root.getType().getName());
            assertNull(root.getProperty("trainers").getValue());
            assertNull(root.getProperty("trainingdates").getValue());
            JTree tree = docTreeView.getTree();
            tree.expandPath(tree.getPathForRow(1));
            tree.expandPath(tree.getPathForRow(2));
            assertEquals("trainers", PresentationSwingTestHelper.getColPropName(tree, 1));
            assertEquals("trainingdates", PresentationSwingTestHelper.getColPropName(tree, 2));
        } finally {
            PresentationSwingTestHelper.deleteTestTreeView();
        }
    }

    /**
     * Simply delete Trainer beans "Blümel" and "Dautovic" in the
     * document.
     *
     * @throws InterruptedException test
     */
    public void testDeleteBeansViaDeleteAction() throws InterruptedException {
        DocumentTreeViewSwing docTreeView = PresentationSwingTestHelper.createTestTreeView();
        JTree tree = docTreeView.getTree();
        try {
            // expand "trainers" branch in the tree
            tree.expandPath(tree.getPathForRow(2));
            tree.expandPath(tree.getPathForRow(1));
            assertEquals("trainers", PresentationSwingTestHelper.getColPropName(tree, 1));
            assertEquals("Blümel", ((RapidBean) tree.getPathForRow(2)
                .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("Dahlheimer", ((RapidBean) tree.getPathForRow(3)
                .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("Dautovic", ((RapidBean) tree.getPathForRow(4)
                .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("trainingdates", PresentationSwingTestHelper.getColPropName(tree, 5));
            assertEquals("Aikido Adults I", ((RapidBean) tree.getPathForRow(6)
                    .getLastPathComponent()).getIdString());
            assertEquals("Aikido Children", ((RapidBean) tree.getPathForRow(7)
                    .getLastPathComponent()).getIdString());
            assertEquals("Aikido Adults II", ((RapidBean) tree.getPathForRow(8)
                    .getLastPathComponent()).getIdString());
            assertNull(tree.getPathForRow(10));
            final Document testDoc = PresentationSwingTestHelper.getTestDocument();
            assertNotNull(testDoc.findBean("Trainer", "Blümel_Martin"));
            assertNotNull(testDoc.findBean("Trainer", "Dautovic_Damir"));
            assertNotNull(testDoc.findBean("Trainer", "Dahlheimer_Berit"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Adults I"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Children"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Adults II"));

            testDoc.findBean("TrainingDate", "Aikido Adults II").delete();
            testDoc.findBean("Trainer", "Dautovic_Damir").delete();
            testDoc.findBean("TrainingDate", "Aikido Adults I").delete();
            testDoc.findBean("Trainer", "Blümel_Martin").delete();
            // assert the beans correctly deleted from the document
            assertNull(PresentationSwingTestHelper.getTestDocument().findBean("Trainer", "Blümel_Martin"));
            assertNull(PresentationSwingTestHelper.getTestDocument().findBean("Trainer", "Dautovic_Damir"));
            assertNotNull(PresentationSwingTestHelper.getTestDocument().findBean("Trainer", "Dahlheimer_Berit"));
            assertNull(testDoc.findBean("TrainingDate", "Aikido Adults I"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Children"));
            assertNull(testDoc.findBean("TrainingDate", "Aikido Adults II"));

            // assert the tree view is correctly updated
            assertEquals("trainers", PresentationSwingTestHelper.getColPropName(tree, 1));
            assertEquals("Dahlheimer", ((RapidBean) tree.getPathForRow(2)
                .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("trainingdates", PresentationSwingTestHelper.getColPropName(tree, 3));
            assertEquals("Aikido Children", ((RapidBean) tree.getPathForRow(4)
                    .getLastPathComponent()).getIdString());
            assertNull(tree.getPathForRow(6));
        } finally {
            PresentationSwingTestHelper.deleteTestTreeView();
        }
    }

    /**
     * Simply create a new Trainer "Blümel".
     *
     * @throws InterruptedException test
     */
    public void testCreateSngleBeanViaCreateAction() throws InterruptedException {
        DocumentTreeViewSwing docTreeView = PresentationSwingTestHelper.createTestTreeView();
        JTree tree = docTreeView.getTree();
        try {
            // expand "trainers" branch in the tree
            tree.expandPath(tree.getPathForRow(2));
            tree.expandPath(tree.getPathForRow(1));
            assertEquals("trainers", ((DocumentTreeNodePropColComp) tree.getPathForRow(1)
                .getLastPathComponent()).getColProp().getType().getPropName());
            assertEquals("Blümel", ((RapidBean) tree.getPathForRow(2)
                .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("Dahlheimer", ((RapidBean) tree.getPathForRow(3)
                .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("Dautovic", ((RapidBean) tree.getPathForRow(4)
                .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("trainingdates", ((DocumentTreeNodePropColComp) tree.getPathForRow(5)
                    .getLastPathComponent()).getColProp().getType().getPropName());
            assertEquals("Aikido Adults I", ((RapidBean) tree.getPathForRow(6)
                    .getLastPathComponent()).getIdString());
            assertEquals("Aikido Children", ((RapidBean) tree.getPathForRow(7)
                    .getLastPathComponent()).getIdString());
            assertEquals("Aikido Adults II", ((RapidBean) tree.getPathForRow(8)
                    .getLastPathComponent()).getIdString());
            assertNull(tree.getPathForRow(10));

            Document testDoc = PresentationSwingTestHelper.getTestDocument();
            assertNotNull(testDoc.findBean("Trainer", "Blümel_Martin"));
            assertNotNull(testDoc.findBean("Trainer", "Dautovic_Damir"));
            assertNotNull(testDoc.findBean("Trainer", "Dahlheimer_Berit"));
            assertNull(testDoc.findBean("Trainer", "Meyer_Michael"));
            assertNull(testDoc.findBean("Trainer", "Meyer_Herbert"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Adults I"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Children"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Adults II"));

            GenericBean newTrainer = PresentationSwingTestHelper.createTrainer(
                    "Meyer" , "Michael", true, false);
            ((PropertyCollection) testDoc.getRoot().getProperty("trainers")).addLink(newTrainer);

            // assert the bean correctly created in the document
            assertNotNull(testDoc.findBean("Trainer", "Blümel_Martin"));
            assertNotNull(testDoc.findBean("Trainer", "Dautovic_Damir"));
            assertNotNull(testDoc.findBean("Trainer", "Dahlheimer_Berit"));
            assertNotNull(testDoc.findBean("Trainer", "Meyer_Michael"));
            assertNull(testDoc.findBean("Trainer", "Meyer_Herbert"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Adults I"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Children"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Adults II"));

            // assert the tree view is correctly updated
            assertEquals("trainers", ((DocumentTreeNodePropColComp) tree.getPathForRow(1)
                    .getLastPathComponent()).getColProp().getType().getPropName());
                assertEquals("Blümel", ((RapidBean) tree.getPathForRow(2)
                    .getLastPathComponent()).getProperty("lastname").getValue());
                assertEquals("Dahlheimer", ((RapidBean) tree.getPathForRow(3)
                    .getLastPathComponent()).getProperty("lastname").getValue());
                assertEquals("Dautovic", ((RapidBean) tree.getPathForRow(4)
                    .getLastPathComponent()).getProperty("lastname").getValue());
                assertEquals("Meyer", ((RapidBean) tree.getPathForRow(5)
                        .getLastPathComponent()).getProperty("lastname").getValue());
                assertEquals("trainingdates", ((DocumentTreeNodePropColComp) tree.getPathForRow(6)
                        .getLastPathComponent()).getColProp().getType().getPropName());
                assertEquals("Aikido Adults I", ((RapidBean) tree.getPathForRow(7)
                        .getLastPathComponent()).getIdString());
                assertEquals("Aikido Children", ((RapidBean) tree.getPathForRow(8)
                        .getLastPathComponent()).getIdString());
                assertEquals("Aikido Adults II", ((RapidBean) tree.getPathForRow(9)
                        .getLastPathComponent()).getIdString());
                assertNull(tree.getPathForRow(11));

                newTrainer = PresentationSwingTestHelper.createTrainer(
                        "Mayer" , "Herbert", true, false);
                ((PropertyCollection) testDoc.getRoot().getProperty("trainers")).addLink(newTrainer);

                // assert the bean correctly created in the document
                assertNotNull(testDoc.findBean("Trainer", "Blümel_Martin"));
                assertNotNull(testDoc.findBean("Trainer", "Dautovic_Damir"));
                assertNotNull(testDoc.findBean("Trainer", "Dahlheimer_Berit"));
                assertNotNull(testDoc.findBean("Trainer", "Meyer_Michael"));
                assertNotNull(testDoc.findBean("Trainer", "Mayer_Herbert"));
                assertNotNull(testDoc.findBean("TrainingDate", "Aikido Adults I"));
                assertNotNull(testDoc.findBean("TrainingDate", "Aikido Children"));
                assertNotNull(testDoc.findBean("TrainingDate", "Aikido Adults II"));

                // assert the tree view is correctly updated
                assertEquals("trainers", ((DocumentTreeNodePropColComp) tree.getPathForRow(1)
                        .getLastPathComponent()).getColProp().getType().getPropName());
                    assertEquals("Blümel", ((RapidBean) tree.getPathForRow(2)
                        .getLastPathComponent()).getProperty("lastname").getValue());
                    assertEquals("Dahlheimer", ((RapidBean) tree.getPathForRow(3)
                        .getLastPathComponent()).getProperty("lastname").getValue());
                    assertEquals("Dautovic", ((RapidBean) tree.getPathForRow(4)
                        .getLastPathComponent()).getProperty("lastname").getValue());
                    assertEquals("Meyer", ((RapidBean) tree.getPathForRow(5)
                            .getLastPathComponent()).getProperty("lastname").getValue());
                    assertEquals("Mayer", ((RapidBean) tree.getPathForRow(6)
                            .getLastPathComponent()).getProperty("lastname").getValue());
                    assertEquals("trainingdates", ((DocumentTreeNodePropColComp) tree.getPathForRow(7)
                            .getLastPathComponent()).getColProp().getType().getPropName());
                    assertEquals("Aikido Adults I", ((RapidBean) tree.getPathForRow(8)
                            .getLastPathComponent()).getIdString());
                    assertEquals("Aikido Children", ((RapidBean) tree.getPathForRow(9)
                            .getLastPathComponent()).getIdString());
                    assertEquals("Aikido Adults II", ((RapidBean) tree.getPathForRow(10)
                            .getLastPathComponent()).getIdString());
                    assertNull(tree.getPathForRow(12));
        } finally {
            PresentationSwingTestHelper.deleteTestTreeView();
        }
    }

    /**
     * Simply create a Tnew Trainer "Blümel".
     *
     * @throws InterruptedException test
     */
    public void testCreateMultipleBeansViaCreateAction() throws InterruptedException {
        DocumentTreeViewSwing docTreeView = PresentationSwingTestHelper.createTestTreeView();
        JTree tree = docTreeView.getTree();
        try {
            // expand "trainers" branch in the tree
            tree.expandPath(tree.getPathForRow(2));
            tree.expandPath(tree.getPathForRow(1));
            assertEquals("trainers", ((DocumentTreeNodePropColComp) tree.getPathForRow(1)
                    .getLastPathComponent()).getColProp().getType().getPropName());
            assertEquals("Blümel", ((RapidBean) tree.getPathForRow(2)
                    .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("Dahlheimer", ((RapidBean) tree.getPathForRow(3)
                    .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("Dautovic", ((RapidBean) tree.getPathForRow(4)
                    .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("trainingdates", ((DocumentTreeNodePropColComp) tree.getPathForRow(5)
                    .getLastPathComponent()).getColProp().getType().getPropName());
            assertEquals("Aikido Adults I", ((RapidBean) tree.getPathForRow(6)
                    .getLastPathComponent()).getIdString());
           assertEquals("Aikido Children", ((RapidBean) tree.getPathForRow(7)
                    .getLastPathComponent()).getIdString());
           assertEquals("Aikido Adults II", ((RapidBean) tree.getPathForRow(8)
                   .getLastPathComponent()).getIdString());
            assertNull(tree.getPathForRow(10));

            Document testDoc = PresentationSwingTestHelper.getTestDocument();
            assertNotNull(testDoc.findBean("Trainer", "Blümel_Martin"));
            assertNotNull(testDoc.findBean("Trainer", "Dautovic_Damir"));
            assertNotNull(testDoc.findBean("Trainer", "Dahlheimer_Berit"));
            assertNull(testDoc.findBean("Trainer", "Meyer_Michael"));
            assertNull(testDoc.findBean("Trainer", "Meyer_Herbert"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Adults I"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Children"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Adults II"));

            GenericBean newTrainer = PresentationSwingTestHelper.createTrainer(
                    "Meyer" , "Michael", true, true);
            ((PropertyCollection) testDoc.getRoot().getProperty("trainers")).addLink(newTrainer);
            newTrainer = PresentationSwingTestHelper.createTrainer(
                    "Mayer" , "Herbert", true, true);
            ((PropertyCollection) testDoc.getRoot().getProperty("trainers")).addLink(newTrainer);

            // assert the bean correctly created in the document
            assertNotNull(testDoc.findBean("Trainer", "Blümel_Martin"));
            assertNotNull(testDoc.findBean("Trainer", "Dautovic_Damir"));
            assertNotNull(testDoc.findBean("Trainer", "Dahlheimer_Berit"));
            assertNotNull(testDoc.findBean("Trainer", "Meyer_Michael"));
            assertNotNull(testDoc.findBean("Trainer", "Mayer_Herbert"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Adults I"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Children"));
            assertNotNull(testDoc.findBean("TrainingDate", "Aikido Adults II"));

            // assert the tree view is correctly updated
            assertEquals("trainers", ((DocumentTreeNodePropColComp) tree.getPathForRow(1)
                    .getLastPathComponent()).getColProp().getType().getPropName());
            assertEquals("Blümel", ((RapidBean) tree.getPathForRow(2)
                    .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("Dahlheimer", ((RapidBean) tree.getPathForRow(3)
                    .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("Dautovic", ((RapidBean) tree.getPathForRow(4)
                    .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("Meyer", ((RapidBean) tree.getPathForRow(5)
                    .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("Mayer", ((RapidBean) tree.getPathForRow(6)
                    .getLastPathComponent()).getProperty("lastname").getValue());
            assertEquals("trainingdates", ((DocumentTreeNodePropColComp) tree.getPathForRow(7)
                    .getLastPathComponent()).getColProp().getType().getPropName());
            assertEquals("Aikido Adults I", ((RapidBean) tree.getPathForRow(8)
                    .getLastPathComponent()).getIdString());
            assertEquals("Aikido Children", ((RapidBean) tree.getPathForRow(9)
                    .getLastPathComponent()).getIdString());
            assertEquals("Aikido Adults II", ((RapidBean) tree.getPathForRow(10)
                    .getLastPathComponent()).getIdString());
            assertNull(tree.getPathForRow(12));
        } finally {
            PresentationSwingTestHelper.deleteTestTreeView();
        }
    }

//    /**
//     * create a generic test Trainer.
//     *
//     * @param lastname last name
//     * @param firstname first name
//     * @param leader if the trainer is certified exercise leader
//     * @param mandatory if the certificates property is mandatory
//     *
//     * @return the test bean
//     */
//    private static GenericBean createTrainer(final String lastname,
//            final String firstname, final boolean leader, final boolean mandatory) {
//        if (RapidBeansTypeLoader.getInstance().lookupType("Trainer") == null) {
//            String descr = "<beantype name=\"Trainer\" idtype=\"keyprops\">"
//                + "<property name=\"lastname\" type=\"string\" key=\"true\"/>"
//                + "<property name=\"firstname\" type=\"string\" key=\"true\"/>"
//                + "<property name=\"leader\" type=\"boolean\""
//                +    " mandatory=\"true\" default=\"false\""
//                + "/>"
//                + "<property name=\"certificates\" type=\"collection\"";
//            if (mandatory) {
//                descr += "    mandatory=\"true\" default=\"\"";
//            }
//            descr += "    targettype=\"Certificate\""
//                + "/>"
//                + "</beantype>";
//            XmlNode xmlNode = XmlNode.getDocumentTopLevel(
//                    new ByteArrayInputStream(descr.getBytes()));
//            new TypeRapidBean(null, xmlNode, true);
//        }
//        GenericBean bean = (GenericBean) RapidBeanImplStrict.createInstance("Trainer");
//        bean.setPropValue("lastname", lastname);
//        bean.setPropValue("firstname", firstname);
//        bean.setPropValue("leader", new Boolean(leader));
//        return bean;
//    }

    public void setUp() {
        PresentationSwingTestHelper.createCertificate("XXX");
        PresentationSwingTestHelper.createTrainer("Uga", "Aga", true, true);
        PresentationSwingTestHelper.createTrainingDate("xxx", "monday", "12:30", "13:30", null);
    }

    /**
     * unregister type "Trainer".
     */
    public void tearDown() {
        TestHelperTypeLoader.clearBeanTypesGeneric();
        if (ApplicationManager.getApplication() != null) {
            ApplicationManager.resetApplication();
        }        
    }
}
