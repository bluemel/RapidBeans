/*
 * Rapid Beans Framework: PresentationSwingTestHelper.java
 *
 * Copyright Martin Bluemel, 2006
 *
 * 26.05.2006
 */

package org.rapidbeans.presentation.swing;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.swing.JTree;

import junit.framework.Assert;

import org.rapidbeans.core.basic.GenericBean;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.XmlNode;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.DocumentView;

/**
 * A helper class for the Swing presentation tests.
 *
 * @author Martin Bluemel
 */
public final class PresentationSwingTestHelper {

    /**
     * the test client.
     */
    private static Application testClient = null;

    /**
     * @return the test client
     */
    public static Application getTestClient() {
        if (testClient == null) {
            ApplicationManager.start(null,
                    "../org.rapidbeans/testdata/rapidclubadmin/config/Application.xml",
                    new Application() {
                        @Override
                        public Properties getOptions() {
                            return new Properties();
                        }
                        public boolean getTestMode() {
                            return true;
                        }
            });
            testClient = ApplicationManager.getApplication();
            testClient.setSettingsDoc(new Document(new File("testdata/testsettings.xml")));
        }
        return testClient;
    }

    /**
     * release the TestClient.
     */
    public static void releaseTestClient() {
        if (testClient == null) {
            Assert.fail("test client aleady reset");
        }
        ApplicationManager.resetApplication();
        testClient = null;
        testDocview = null;
        testDocument = null;
    }

    /**
     * the test document.
     */
    private static Document testDocument = null;

    /**
     * the test document view.
     */
    private static DocumentViewSwing testDocview = null;

    /**
     * @return Returns the testDocument.
     */
    public static DocumentViewSwing getTestDocumentView() {
        return testDocview;
    }

    /**
     * creates a test tree view.
     *
     * @return the test tree view.
     */
    protected static DocumentTreeViewSwing createTestTreeView() {
        getTestClient();
        testDocument = createTestDocument(true);
        testClient.addDocument(testDocument);
        testDocview = (DocumentViewSwing) DocumentView.createInstance(
                testClient, testDocument, "test", "expert", null);
        testClient.addView(testDocview);
        return (DocumentTreeViewSwing) testDocview.getTreeView();
    }

    /**
     * creates a test tree view.
     *
     * @return the test tree view.
     */
    protected static DocumentTreeViewSwing createTestTreeViewWithEmptyColProps() {
        getTestClient();
        testDocument = createTestDocumentWithEmptyColProps();
        testClient.addDocument(testDocument);
        testDocview = (DocumentViewSwing) DocumentView.createInstance(testClient, testDocument,
                "test", "expert", null);
        testClient.addView(testDocview);
        return (DocumentTreeViewSwing) testDocview.getTreeView();
    }

    /**
     * deletes the test tree view.
     */
    protected static void deleteTestTreeView() {
        getTestClient();
        testClient.removeDocument(testDocument);
        testClient.removeView(testDocview);
        testDocview = null;
        testDocument = null;
    }

    /**
     * necessary for a Utility Class.
     */
    private PresentationSwingTestHelper() {
    }

    /**
     * @return Returns the testDocument.
     */
    public static Document getTestDocument() {
        return testDocument;
    }

    /**
     * @param tree the tree
     * @param index the horizontal index
     * @return the collection property name
     */
    public static String getColPropName(final JTree tree, final int index) {
        return ((DocumentTreeNodePropColComp) tree.getPathForRow(index)
                .getLastPathComponent()).getColProp().getType().getPropName();
    }

    /**
     * create a test document. This document contains:
     * - 1 BillingPeriod object as top level object
     * - 3 Trainer objects: Blümel, Dahlheimer, Dautovic
     * - 3 TrainingDate objects:
     * @param certificatesMandatory if the certificate of a Trainer are mandatory
     *
     * @return the test document
     */
    private static Document createTestDocument(final boolean certificatesMandatory) {

        // set up certificates
        Collection<RapidBean> certs = new ArrayList<RapidBean>();
        GenericBean cert1 = createCertificate("Fachübungsleiter");
        certs.add(cert1);

        // set up the trainers
        Collection<RapidBean> trainers = new ArrayList<RapidBean>();
        GenericBean trainer1 = createTrainer("Blümel", "Martin", true, certificatesMandatory);
        trainers.add(trainer1);
        GenericBean trainer2 = createTrainer("Dahlheimer", "Berit", false, certificatesMandatory);
        trainers.add(trainer2);
        GenericBean trainer3 = createTrainer("Dautovic", "Damir", false, certificatesMandatory);
        trainers.add(trainer3);

        // set up the training dates
        Collection<RapidBean> trdates = new ArrayList<RapidBean>();
        GenericBean trdateMo = createTrainingDate("Aikido Adults I",
                "monday", "19:30", "21:30", trainer2);
        trdates.add(trdateMo);
        GenericBean trdateTu = createTrainingDate("Aikido Children",
                "tuesday", "18:00", "19:30", trainer3);
        trdates.add(trdateTu);
        GenericBean trdateTh = createTrainingDate("Aikido Adults II",
                "thursday", "19:00", "21:30", trainer3);
        trdates.add(trdateTh);

        // set up the billing period document
        GenericBean billingPeriod = createBillingPeriod("20060101", "20060331");
        billingPeriod.setPropValue("trainers", trainers);
        billingPeriod.setPropValue("trainingdates", trdates);
        billingPeriod.setPropValue("certificates", certs);

        Document doc = new Document("test", billingPeriod);
        return doc;
    }

    /**
     * create a test document. This document contains:
     * - 1 BillingPeriod object as top level object
     * - 3 Trainer objects: Blümel, Dahlheimer, Dautovic
     * - 3 TrainingDate objects:
     *
     * @return the test document
     */
    private static Document createTestDocumentWithEmptyColProps() {
        GenericBean billingPeriod = createBillingPeriod("20060101", "20060331");
        Document doc = new Document("test", billingPeriod);
        return doc;
    }

    /**
     * create a generic test BillingPeriod.
     * @param dateBegin the begin date
     * @param dateEnd the end date
     * @return the test bean
     */
    public static GenericBean createBillingPeriod(final String dateBegin,
            final String dateEnd) {
        if (RapidBeansTypeLoader.getInstance().lookupType("BillingPeriod") == null) {
            String descr = "<beantype name=\"BillingPeriod\" idtype=\"keyprops\">"
                + "<property name=\"from\" type=\"date\" key=\"true\"/>"
                + "<property name=\"to\" type=\"date\" key=\"true\"/>"
                + "<property name=\"trainers\" type=\"collection\""
                    + " composition=\"true\" targettype=\"Trainer\"/>"
                + "<property name=\"trainingdates\" type=\"collection\""
                    + " composition=\"true\" targettype=\"TrainingDate\"/>"
                + "/>"
                + "<property name=\"certificates\" type=\"collection\""
                    + " composition=\"true\" targettype=\"Certificate\"/>"
                + "/>"
                + "</beantype>";
            XmlNode xmlNode = XmlNode.getDocumentTopLevel(descr);
            new TypeRapidBean(null, xmlNode, null, true);
        }
        GenericBean bean = (GenericBean) RapidBean.createInstance("BillingPeriod");
        bean.setPropValue("from", dateBegin);
        bean.setPropValue("to", dateEnd);
        return bean;
    }

    /**
     * create a generic test Trainer.
     *
     * @param lastname last name
     * @param firstname first name
     * @param leader if the trainer is certified exercise leader
     * @param mandatory if the certificates property is mandatory
     *
     * @return the test bean
     */
    public static GenericBean createTrainer(final String lastname,
            final String firstname, final boolean leader, final boolean mandatory) {
        if (RapidBeansTypeLoader.getInstance().lookupType("Trainer") == null) {
            String descr = "<beantype name=\"Trainer\" idtype=\"keyprops\">"
                + "<property name=\"lastname\" type=\"string\" key=\"true\"/>"
                + "<property name=\"firstname\" type=\"string\" key=\"true\"/>"
                + "<property name=\"leader\" type=\"boolean\""
                +    " mandatory=\"true\" default=\"false\""
                + "/>"
                + "<property name=\"email\" pattern=\"\\A[.\\-0-9A-Za-z]*@[.\\-0-9A-Za-z]*\\z\"/>"
                + "<property name=\"certificates\" type=\"collection\"";
            if (mandatory) {
                descr += "    mandatory=\"true\" default=\"\"";
            }
            descr += "    targettype=\"Certificate\""
                + "/>"
                + "</beantype>";
            XmlNode xmlNode = XmlNode.getDocumentTopLevel(
                    new ByteArrayInputStream(descr.getBytes()));
            new TypeRapidBean(null, xmlNode, null, true);
        }
        GenericBean bean = (GenericBean) RapidBean.createInstance("Trainer");
        bean.setPropValue("lastname", lastname);
        bean.setPropValue("firstname", firstname);
        bean.setPropValue("leader", new Boolean(leader));
        return bean;
    }

    /**
     * create a generic test Certificate.
     *
     * @param name name
     *
     * @return the test bean
     */
    public static GenericBean createCertificate(final String name) {
        if (RapidBeansTypeLoader.getInstance().lookupType("Certificate") == null) {
            String descr = "<beantype name=\"Certificate\" idtype=\"keyprops\">"
                + "<property name=\"name\" type=\"string\" key=\"true\"/>"
                + "/>"
                + "</beantype>";
            XmlNode xmlNode = XmlNode.getDocumentTopLevel(
                    new ByteArrayInputStream(descr.getBytes()));
            new TypeRapidBean(null, xmlNode, null, true);
        }
        GenericBean bean = (GenericBean) RapidBean.createInstance("Certificate");
        bean.setPropValue("name", name);
        return bean;
    }

    /**
     * create a generic test TrainingDate.
     * @param name name
     * @param dayofweek { monday, ..., sunday }
     * @param tstart starting time
     * @param tend end time
     * @param defaulttrainer the default trainer
     * @return the generic training date bean
     */
    public static GenericBean createTrainingDate(final String name,
            final String dayofweek, final String tstart, final String tend,
            final GenericBean defaulttrainer) {
        if (RapidBeansTypeLoader.getInstance().lookupType("TrainingDate") == null) {
            String descr = "<beantype name=\"TrainingDate\" idtype=\"keyprops\">"
                + "<property name=\"name\" type=\"string\" key=\"true\"/>"
                + "<property name=\"dayofweek\" type=\"choice\""
                    + " enum=\"org.rapidbeans.domain.math.DayOfWeek\"/>"
                + "<property name=\"timestart\" type=\"quantity\""
                    + " quantity=\"org.rapidbeans.domain.math.TimeOfDay\"/>"
                + "<property name=\"timeend\" type=\"quantity\""
                    + " quantity=\"org.rapidbeans.domain.math.TimeOfDay\"/>"
                + "<property name=\"defaulttrainer\" type=\"collection\""
                    + " targettype=\"Trainer\""
                    + " minmult=\"1\" maxmult=\"1\"/>"
                + "</beantype>";
            XmlNode xmlNode = XmlNode.getDocumentTopLevel(
                    new ByteArrayInputStream(descr.getBytes()));
            new TypeRapidBean(null, xmlNode, null, true);
        }
        GenericBean bean = (GenericBean) RapidBean.createInstance("TrainingDate");
        bean.setPropValue("name", name);
        bean.setPropValue("dayofweek", dayofweek);
        bean.setPropValue("timestart", tstart);
        bean.setPropValue("timeend", tend);
        bean.setPropValue("defaulttrainer", defaulttrainer);
        return bean;
    }
}
