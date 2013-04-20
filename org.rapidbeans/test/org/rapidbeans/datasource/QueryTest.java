/*
 * Rapid Beans Framework: QueryTest.java
 * 
 * Copyright Martin Bluemel, 2007
 * 
 * 22.12.2007
 */
package org.rapidbeans.datasource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.BeanSorter;
import org.rapidbeans.core.basic.GenericBean;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanImplParent;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TestHelperTypeLoader;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.XmlNode;
import org.rapidbeans.datasource.query.Query;
import org.rapidbeans.presentation.config.ConfigMainWindow;
import org.rapidbeans.presentation.config.ConfigMenuItem;
import org.rapidbeans.presentation.config.ConfigSubmenu;

/**
 * Unit tests for queries with a document.
 * 
 * @author Martin Bluemel
 */
public class QueryTest extends TestCase {

	private static Document testdoc01 = null;

	private static Document testdoc02 = null;

	public void setUp() {
		if (testdoc01 == null) {
			testdoc01 = new Document("testdoc01", new File("../org.rapidbeans/testdata/deserialization"
					+ "/AppGenericStyle.xml"));
		}
		if (testdoc02 == null) {
			TestHelperTypeLoader.clearBeanTypesGeneric();
			TypeRapidBean.forName("org.rapidbeans.test.addressbook5.AddressbookInDb");
			if (RapidBeansTypeLoader.getInstance().getXmlRootElementBinding("addressbookdb") == null) {
				RapidBeansTypeLoader.getInstance().addXmlRootElementBinding("addressbookdb",
						"org.rapidbeans.test.addressbook5.AddressbookDb");
			}
			testdoc02 = new Document("testdoc02",
					new File("../org.rapidbeans/testdata/addressbook5" + "/adrbookdb.xml"));
		}
	}

	/**
	 * simple type query.
	 */
	public void testFindByQueryStringTypeSimple() {
		Document doc = createTestDocument(false);
		Collection<RapidBean> resultSet = doc.findBeansByQuery("Trainer");
		assertEquals(4, resultSet.size());
		Iterator<RapidBean> iter = resultSet.iterator();
		assertEquals("Bl�mel_Martin", iter.next().getIdString());
		assertEquals("Bl�mel_Ulrike", iter.next().getIdString());
		assertEquals("Dahlheimer_Berit", iter.next().getIdString());
		assertEquals("Dautovic_Damir", iter.next().getIdString());
	}

	/**
	 * simple attribute query.
	 */
	public void testFindByQueryStringAttrSimple() {
		Document doc = createTestDocument(false);
		Collection<RapidBean> resultSet = doc.findBeansByQuery("Trainer[leader = false]");
		assertEquals(3, resultSet.size());
		Iterator<RapidBean> iter = resultSet.iterator();
		assertEquals("Bl�mel_Ulrike", iter.next().getIdString());
		assertEquals("Dahlheimer_Berit", iter.next().getIdString());
		assertEquals("Dautovic_Damir", iter.next().getIdString());
		resultSet = doc.findBeansByQuery("Trainer[leader = 'true']");
		assertEquals(1, resultSet.size());
		iter = resultSet.iterator();
		assertEquals("Bl�mel_Martin", iter.next().getIdString());
	}

	/**
	 * Querying an association end attribute directly leads to a string
	 * comparison on the "frozen" string value.
	 */
	public void testFindByQueryAssocEndSingle() {
		final Query query = new Query("org.rapidbeans.test.addressbook5.AddressbookInDb[" + "owner = 'user1']");
		assertEquals(1, testdoc02.findBeansByQuery(query).size());
	}

	/**
	 * simple attribute query.
	 */
	public void testFindByQueryStringAttrSimpleId() {
		Document doc = createTestDocument(false);
		Collection<RapidBean> resultSet = doc.findBeansByQuery("Trainer[id = 'Bl�mel_Ulrike']");
		assertEquals(1, resultSet.size());
		Iterator<RapidBean> iter = resultSet.iterator();
		assertEquals("Bl�mel_Ulrike", iter.next().getIdString());
	}

	// still unsure if we should support this shortcut
	// /**
	// * simple attribute query.
	// */
	// public void testFindByQueryStringAttrSimpleIdAlt() {
	// Document doc = createTestDocument(false);
	// Collection<RapidBean> resultSet =
	// doc.findBeansByQuery("Trainer['Bl�mel_Ulrike']");
	// assertEquals(1, resultSet.size());
	// Iterator<RapidBean> iter = resultSet.iterator();
	// assertEquals("Bl�mel_Ulrike", iter.next().getIdString());
	// }

	/**
	 * attribute query with association (join).
	 */
	public void testFindByQueryStringAttrAssoc() {
		Document doc = createTestDocument(false);
		Collection<RapidBean> resultSetAll = doc.findBeansByQuery("TrainingDate");
		assertEquals(3, resultSetAll.size());
		Collection<RapidBean> resultSet = doc.findBeansByQuery("TrainingDate[defaulttrainer[lastname = 'Dahlheimer']]");
		assertEquals(2, resultSet.size());
		Iterator<RapidBean> iter = resultSet.iterator();
		GenericBean date = (GenericBean) iter.next();
		assertEquals("Aikido Adults II", date.getIdString());
		date = (GenericBean) iter.next();
		assertEquals("Aikido Children", date.getIdString());
		GenericBean trainer = (GenericBean) ((Collection<?>) date.getProperty("defaulttrainer").getValue()).iterator()
				.next();
		assertEquals("Dahlheimer", trainer.getProperty("lastname").getValue());
	}

	/**
	 * id (attribute) query with association (join).
	 */
	public void testFindByQueryStringAttrAssocEndNoCompId() {
		Document doc = createTestDocument(false);
		Collection<RapidBean> resultSet = doc.findBeansByQuery("TrainingDate[defaulttrainer[id = 'Dahlheimer_Berit']]");
		assertEquals(2, resultSet.size());
		Iterator<RapidBean> iter = resultSet.iterator();
		GenericBean date = (GenericBean) iter.next();
		assertEquals("Aikido Adults II", date.getIdString());
		date = (GenericBean) iter.next();
		assertEquals("Aikido Children", date.getIdString());
		GenericBean trainer = (GenericBean) ((Collection<?>) date.getProperty("defaulttrainer").getValue()).iterator()
				.next();
		assertEquals("Dahlheimer", trainer.getProperty("lastname").getValue());
	}

	public void testFindByQueryStringAttrAssocEndNoCompOtherProp() {
		final Query query = new Query("org.rapidbeans.test.addressbook5.AddressbookInDb["
				+ "owner[accountname = 'user1']]");
		assertEquals(1, testdoc02.findBeansByQuery(query).size());
	}

	public void testFindByQueryStringAttrAssocEndCompId() {
		final Query query = new Query("org.rapidbeans.test.addressbook5.AddressbookInDb[" + "name = 'Address Book 1'"
				+ " & persons[id='41bb6f77-d599-4368-b2d6-b385ff342625']]");
		assertEquals(1, testdoc02.findBeansByQuery(query).size());
	}

	public void testFindByQueryStringAttrAssocEndCompOtherProp() {
		final Query query = new Query("org.rapidbeans.test.addressbook5.AddressbookInDb[" + "name = 'Address Book 1'"
				+ " & persons[lastname='Ehlert']]");
		assertEquals(1, testdoc02.findBeansByQuery(query).size());
	}

	// still unsure if we should support this shortcut
	// /**
	// * simple attribute query.
	// */
	// public void testFindByQueryStringAttrSimpleIdAlt() {
	// Document doc = createTestDocument(false);
	// Collection<RapidBean> resultSet =
	// doc.findBeansByQuery("Trainer['Bl�mel_Ulrike']");
	// assertEquals(1, resultSet.size());
	// Iterator<RapidBean> iter = resultSet.iterator();
	// assertEquals("Bl�mel_Ulrike", iter.next().getIdString());
	// }

	/**
	 * attribute query with brace and and or.
	 */
	public void testFindByQueryStringAttrComplex() {
		Document doc = createTestDocument(false);
		Collection<RapidBean> resultSet = doc
				.findBeansByQuery("Trainer[(firstname = 'Martin' | firstname = 'Berit') & lastname $ 'D.*']");
		assertEquals(1, resultSet.size());
		Iterator<RapidBean> iter = resultSet.iterator();
		assertEquals("Dahlheimer_Berit", iter.next().getIdString());
	}

	/**
	 * test query with default association "parentBean"
	 */
	public void testFindByQueryStringAttrAssocParentBean() {
		Document doc = createTestDocument(false);
		Collection<RapidBean> resultSet = doc
				.findBeansByQuery("TrainingDate[parentBean[from = '20060101' & to = '20060331']]");
		assertEquals(3, resultSet.size());
	}

	/**
	 * test complex query that uses two conditions on different associated
	 * objects
	 */
	public void testFindByQueryStringAttrTwoAssocsAnd() {
		Document doc = createTestDocument(false);
		Collection<RapidBean> resultSet = doc
				.findBeansByQuery("TrainingDate[parentBean[from = '20060101'] & defaulttrainer[lastname = 'Dahlheimer']]");
		assertEquals(2, resultSet.size());
	}

	/**
	 * simple query that uses natural sorting provided by the document's id map.
	 */
	public void testFindByQueryStringTypeSortedIdmap() {
		Document doc = createTestDocument(false);
		Collection<RapidBean> resultSet = doc.findBeansByQuery("Trainer");
		assertEquals(4, resultSet.size());
		Iterator<RapidBean> iter = resultSet.iterator();
		assertEquals("Bl�mel_Martin", iter.next().getIdString());
		assertEquals("Bl�mel_Ulrike", iter.next().getIdString());
		assertEquals("Dahlheimer_Berit", iter.next().getIdString());
		assertEquals("Dautovic_Damir", iter.next().getIdString());
	}

	/**
	 * simple query that uses natural sorting.
	 */
	public void testFindByQueryStringTypeSortedNatural() {
		Document doc = createTestDocument(false);
		Query query = new Query("Trainer");

		query.setSorter(new BeanSorter(null));
		Collection<RapidBean> resultSet = doc.findBeansByQuery(query);
		assertEquals(4, resultSet.size());
		Iterator<RapidBean> iter = resultSet.iterator();
		assertEquals("Bl�mel_Martin", iter.next().getIdString());
		assertEquals("Bl�mel_Ulrike", iter.next().getIdString());
		assertEquals("Dahlheimer_Berit", iter.next().getIdString());
		assertEquals("Dautovic_Damir", iter.next().getIdString());
	}

	/**
	 * test ascending sorting according to one criteria.
	 */
	public void testFindByQueryStringTypeSortOneAsc() {
		Document doc = createTestDocument(false);
		Query query = new Query("Trainer >lastname");
		Collection<RapidBean> resultSet = doc.findBeansByQuery(query);
		assertEquals(4, resultSet.size());
		Iterator<RapidBean> iter = resultSet.iterator();
		assertEquals("Bl�mel_Martin", iter.next().getIdString());
		assertEquals("Bl�mel_Ulrike", iter.next().getIdString());
		assertEquals("Dahlheimer_Berit", iter.next().getIdString());
		assertEquals("Dautovic_Damir", iter.next().getIdString());
	}

	/**
	 * test sorting according to two criteria.
	 */
	public void testFindByQueryStringTypeSortedFirstnameLastname() {
		Document doc = createTestDocument(false);
		Query query = new Query("Trainer[] >firstname, >lastname");
		Collection<RapidBean> resultSet = doc.findBeansByQuery(query);
		assertEquals(4, resultSet.size());
		Iterator<RapidBean> iter = resultSet.iterator();
		assertEquals("Dahlheimer_Berit", iter.next().getIdString());
		assertEquals("Dautovic_Damir", iter.next().getIdString());
		assertEquals("Bl�mel_Martin", iter.next().getIdString());
		assertEquals("Bl�mel_Ulrike", iter.next().getIdString());
	}

	// still unsure if we should support this shortcut
	// /**
	// * id (attribute) query with association (join).
	// */
	// public void testFindByQueryStringAttrAssocIdAlt() {
	// Document doc = createTestDocument(false);
	// Collection<RapidBean> resultSet = doc.findBeansByQuery(
	// "TrainingDate[defaulttrainer['Dahlheimer_Berit']]");
	// assertEquals(2, resultSet.size());
	// Iterator<RapidBean> iter = resultSet.iterator();
	// GenericBean date = (GenericBean) iter.next();
	// assertEquals("Aikido Adults II", date.getIdString());
	// date = (GenericBean) iter.next();
	// assertEquals("Aikido Children", date.getIdString());
	// GenericBean trainer = (GenericBean) ((Collection)
	// date.getProperty("defaulttrainer").getValue()).iterator().next();
	// assertEquals("Dahlheimer", trainer.getProperty("lastname").getValue());
	// }

	/**
	 * simple path query that uses the sorting of the underlying collection
	 * class In this case a simple ArrayList is used for property trainers. So
	 * we expect to get the objects in the sequence we created them.
	 */
	public void testFindByQueryStringTypeSortedCollectionClass() {
		Document doc = createTestDocument(false);
		GenericBean bean = (GenericBean) doc.getRoot();
		PropertyCollection colProp = (PropertyCollection) bean.getProperty("trainers");
		TypePropertyCollection colPropType = (TypePropertyCollection) colProp.getType();
		assertEquals(LinkedHashSet.class, colPropType.getCollectionClass());
		Collection<RapidBean> resultSet = doc.findBeansByQuery("/trainers");
		assertEquals(4, resultSet.size());
		Iterator<RapidBean> iter = resultSet.iterator();
		assertEquals("Dahlheimer_Berit", iter.next().getIdString());
		assertEquals("Dautovic_Damir", iter.next().getIdString());
		assertEquals("Bl�mel_Martin", iter.next().getIdString());
		assertEquals("Bl�mel_Ulrike", iter.next().getIdString());
	}

	/**
	 * create a test document. This document contains: - 1 BillingPeriod object
	 * as top level object - 3 Trainer objects: Bl�mel, Dahlheimer, Dautovic - 3
	 * TrainingDate objects:
	 * 
	 * @param certificatesMandatory
	 *            if the certificate of a Trainer are mandatory
	 * 
	 * @return the test document
	 */
	private static Document createTestDocument(final boolean certificatesMandatory) {

		// set up certificates
		Collection<RapidBean> certs = new ArrayList<RapidBean>();
		GenericBean cert1 = createCertificate("Fach�bungsleiter");
		certs.add(cert1);

		// set up the trainers
		// don't do this in perfect alphabetical order
		Collection<RapidBean> trainers = new ArrayList<RapidBean>();
		GenericBean trainer3 = createTrainer("Dahlheimer", "Berit", false, certificatesMandatory);
		trainers.add(trainer3);
		GenericBean trainer4 = createTrainer("Dautovic", "Damir", false, certificatesMandatory);
		trainers.add(trainer4);
		GenericBean trainer1 = createTrainer("Bl�mel", "Martin", true, certificatesMandatory);
		trainers.add(trainer1);
		GenericBean trainer2 = createTrainer("Bl�mel", "Ulrike", false, certificatesMandatory);
		trainers.add(trainer2);

		// set up the training dates
		Collection<RapidBean> trdates = new ArrayList<RapidBean>();
		GenericBean trdateMo = createTrainingDate("Aikido Adults I", "monday", "19:30", "21:30", trainer2);
		trdates.add(trdateMo);
		GenericBean trdateTu = createTrainingDate("Aikido Children", "tuesday", "18:00", "19:30", trainer3);
		trdates.add(trdateTu);
		GenericBean trdateTh = createTrainingDate("Aikido Adults II", "thursday", "19:00", "21:30", trainer3);
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
	 * create a generic test BillingPeriod.
	 * 
	 * @param dateBegin
	 *            the begin date
	 * @param dateEnd
	 *            the end date
	 * @return the test bean
	 */
	private static GenericBean createBillingPeriod(final String dateBegin, final String dateEnd) {
		if (RapidBeansTypeLoader.getInstance().lookupType("BillingPeriod") == null) {
			String descr = "<beantype name=\"BillingPeriod\" idtype=\"keyprops\">"
					+ "<property name=\"from\" type=\"date\" key=\"true\"/>"
					+ "<property name=\"to\" type=\"date\" key=\"true\"/>"
					+ "<property name=\"trainers\" type=\"collection\""
					+ " composition=\"true\" targettype=\"Trainer\"/>"
					+ "<property name=\"trainingdates\" type=\"collection\""
					+ " composition=\"true\" targettype=\"TrainingDate\"/>" + "/>"
					+ "<property name=\"certificates\" type=\"collection\""
					+ " composition=\"true\" targettype=\"Certificate\"/>" + "/>" + "</beantype>";
			XmlNode xmlNode = XmlNode.getDocumentTopLevel(descr);
			new TypeRapidBean(null, xmlNode, null, true);
		}
		GenericBean bean = (GenericBean) RapidBeanImplParent.createInstance("BillingPeriod");
		bean.setPropValue("from", dateBegin);
		bean.setPropValue("to", dateEnd);
		return bean;
	}

	/**
	 * create a generic test Trainer.
	 * 
	 * @param lastname
	 *            last name
	 * @param firstname
	 *            first name
	 * @param leader
	 *            if the trainer is certified exercise leader
	 * @param mandatory
	 *            if the certificates property is mandatory
	 * 
	 * @return the test bean
	 */
	private static GenericBean createTrainer(final String lastname, final String firstname, final boolean leader,
			final boolean mandatory) {
		if (RapidBeansTypeLoader.getInstance().lookupType("Trainer") == null) {
			String descr = "<beantype name=\"Trainer\" idtype=\"keyprops\">"
					+ "<property name=\"lastname\" type=\"string\" key=\"true\"/>"
					+ "<property name=\"firstname\" type=\"string\" key=\"true\"/>"
					+ "<property name=\"leader\" type=\"boolean\"" + " mandatory=\"true\" default=\"false\"" + "/>"
					+ "<property name=\"certificates\" type=\"collection\"";
			if (mandatory) {
				descr += "    mandatory=\"true\" default=\"\"";
			}
			descr += "    targettype=\"Certificate\"" + "/>" + "</beantype>";
			XmlNode xmlNode = XmlNode.getDocumentTopLevel(new ByteArrayInputStream(descr.getBytes()));
			new TypeRapidBean(null, xmlNode, null, true);
		}
		GenericBean bean = (GenericBean) RapidBeanImplParent.createInstance("Trainer");
		bean.setPropValue("lastname", lastname);
		bean.setPropValue("firstname", firstname);
		bean.setPropValue("leader", new Boolean(leader));
		return bean;
	}

	/**
	 * create a generic test Certificate.
	 * 
	 * @param name
	 *            name
	 * 
	 * @return the test bean
	 */
	private static GenericBean createCertificate(final String name) {
		if (RapidBeansTypeLoader.getInstance().lookupType("Certificate") == null) {
			String descr = "<beantype name=\"Certificate\" idtype=\"keyprops\">"
					+ "<property name=\"name\" type=\"string\" key=\"true\"/>" + "/>" + "</beantype>";
			XmlNode xmlNode = XmlNode.getDocumentTopLevel(new ByteArrayInputStream(descr.getBytes()));
			new TypeRapidBean(null, xmlNode, null, true);
		}
		GenericBean bean = (GenericBean) RapidBeanImplParent.createInstance("Certificate");
		bean.setPropValue("name", name);
		return bean;
	}

	/**
	 * create a generic test TrainingDate.
	 * 
	 * @param name
	 *            name
	 * @param dayofweek
	 *            { monday, ..., sunday }
	 * @param tstart
	 *            starting time
	 * @param tend
	 *            end time
	 * @param defaulttrainer
	 *            the default trainer
	 * @return the generic training date bean
	 */
	private static GenericBean createTrainingDate(final String name, final String dayofweek, final String tstart,
			final String tend, final GenericBean defaulttrainer) {
		if (RapidBeansTypeLoader.getInstance().lookupType("TrainingDate") == null) {
			String descr = "<beantype name=\"TrainingDate\" idtype=\"keyprops\">"
					+ "<property name=\"name\" type=\"string\" key=\"true\"/>"
					+ "<property name=\"dayofweek\" type=\"choice\""
					+ " enum=\"org.rapidbeans.domain.math.DayOfWeek\"/>"
					+ "<property name=\"timestart\" type=\"quantity\""
					+ " quantity=\"org.rapidbeans.domain.math.TimeOfDay\"/>"
					+ "<property name=\"timeend\" type=\"quantity\""
					+ " quantity=\"org.rapidbeans.domain.math.TimeOfDay\"/>"
					+ "<property name=\"defaulttrainer\" type=\"collection\"" + " targettype=\"Trainer\""
					+ " minmult=\"1\" maxmult=\"1\"/>" + "</beantype>";
			XmlNode xmlNode = XmlNode.getDocumentTopLevel(new ByteArrayInputStream(descr.getBytes()));
			new TypeRapidBean(null, xmlNode, null, true);
		}
		GenericBean bean = (GenericBean) RapidBeanImplParent.createInstance("TrainingDate");
		bean.setPropValue("name", name);
		bean.setPropValue("dayofweek", dayofweek);
		bean.setPropValue("timestart", tstart);
		bean.setPropValue("timeend", tend);
		bean.setPropValue("defaulttrainer", defaulttrainer);
		return bean;
	}

	// TODO Framework 21 fix query boundary condition
	// /**
	// * Test path query for the root object
	// *
	// * @throws IOException
	// */
	// public void testPathQueryRoot() throws IOException {
	// File file1 = new
	// File("../org.rapidbeans/testdata/deserialization/AppGenericRapidBeansStyle.xml");
	// Document doc = new Document("testdoc", file1);
	// ConfigApplication cfg = (ConfigApplication) doc.getRoot();
	// assertEquals("org.rapidbeans.presentation.TestClient",
	// cfg.getApplicationclass());
	// assertSame(cfg, doc.findBeanByQuery("/"));
	// }

	/**
	 * Test path query for a single first level object
	 * 
	 * @throws IOException
	 */
	public void testPathQuery1stLevelSingle() throws IOException {
		ConfigMainWindow cfgMw = (ConfigMainWindow) testdoc01.findBeanByQuery("/mainwindow");
		assertEquals("mainwindow", cfgMw.getName());
	}

	/**
	 * Test path query for a single 2nd level object with true condition
	 * 
	 * @throws IOException
	 */
	public void testPathQuery2ndLevelWithConditionTrue() throws IOException {
		ConfigMainWindow cfgMw = (ConfigMainWindow) testdoc01.findBeanByQuery("/mainwindow[name = 'mainwindow']");
		assertEquals("mainwindow", cfgMw.getName());
	}

	/**
	 * Test path query for a single 2nd level object with false condition
	 */
	public void testPathQuery1ndLevelWithConditionFalse() throws IOException {
		ConfigMainWindow cfgMw = (ConfigMainWindow) testdoc01.findBeanByQuery("/mainwindow[name = 'xxx']");
		assertNull(cfgMw);
	}

	/**
	 * Test
	 */
	public void testPathQuery3rdLevelWithCondition() throws IOException {
		ConfigSubmenu sm = (ConfigSubmenu) testdoc01.findBeanByQuery("/mainwindow/menubar/menus[name = 'file']");
		assertEquals("file", sm.getName());
	}

	/**
	 * Test
	 */
	public void testPathQuery3rdLevelComplexConditions() throws IOException {
		ConfigSubmenu sm = (ConfigSubmenu) testdoc01
				.findBeanByQuery("/mainwindow/menubar/menus"
						+ "[name = 'test' && menuentrys[name = 'test2' && menuentrys[name = 'test22' && menuentrys[name = 'test222']]]]");
		assertEquals("test", sm.getName());
	}

	/**
	 * Test
	 */
	public void testPathQueryVeryDeep() throws IOException {
		// Klaus fragen
		// List<ConfigSubmenu> sms = (List<ConfigSubmenu>) doc.findBeansByQuery(
		// "/mainwindow/menubar/menus/menuentrys/menuentrys");
		List<RapidBean> mes = testdoc01.findBeansByQuery("/mainwindow/menubar/menus/menuentrys/menuentrys/menuentrys");
		assertEquals(2, mes.size());
		assertEquals("test221", ((ConfigMenuItem) mes.get(0)).getName());
		assertEquals("test222", ((ConfigMenuItem) mes.get(1)).getName());
	}

	/**
	 * Test
	 */
	public void testPathQueryVeryDeepWithCondition() throws IOException {
		ConfigMenuItem mi = (ConfigMenuItem) testdoc01
				.findBeanByQuery("/mainwindow/menubar/menus/menuentrys/menuentrys/menuentrys[name = 'test222']");
		assertEquals("test222", mi.getName());
	}

	// /**
	// * Test
	// */
	// public void testPathQueryVeryDeepWithConditionsParsing() throws
	// IOException {
	// String expected =
	// "org.rapidbeans.datasource.query.QueryExprConditionPath:"
	// + " /mainwindow/menubar/menus\n"
	// + "|__org.rapidbeans.datasource.query.QueryExprConditionAttrval:"
	// + " name == test"
	// + "   |__org.rapidbeans.datasource.query.QueryExprConditionPath:"
	// + " menuentrys"
	// + "      |__org.rapidbeans.datasource.query.QueryExprConditionAttrval:"
	// + " name == test2";
	// Query query = new Query(
	// "/mainwindow/menubar/menus[name = 'test']"
	// + "/menuentrys[name = 'test2']\n");
	// assertEquals(expected, query.toString());
	// }
	//
	// /**
	// * Test
	// */
	// public void testPathQueryVeryDeepWithConditions() throws IOException {
	// ConfigMenuItem mi = (ConfigMenuItem) testdoc01.findBeanByQuery(
	// "/mainwindow/menubar/menus[name = 'test']"
	// + "/menuentrys[name = 'test2']");
	// assertEquals("test2", mi.getName());
	// }
}
