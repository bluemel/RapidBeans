/*
 * Rapid Beans Framework: RapidBeanTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * 22.11.2005
 */

package org.rapidbeans.core.basic;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.TypeNotFoundException;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TestHelperTypeLoader;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.XmlNode;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.config.ConfigApplication;
import org.rapidbeans.presentation.config.ConfigMainWindow;
import org.rapidbeans.presentation.config.ConfigMenubar;
import org.rapidbeans.presentation.config.ConfigSubmenu;
import org.rapidbeans.presentation.config.swing.ConfigApplicationSwing;
import org.rapidbeans.presentation.settings.swing.ApplicationLnfTypeSwing;
import org.rapidbeans.test.TestHelper;

import junit.framework.TestCase;

/**
 * Unit Test for RapidBean.
 * 
 * @author Martin Bluemel
 */
public final class RapidBeanTest extends TestCase {

	/**
	 * test method createInstance().
	 */
	public void testCreateInstance() {
		ConfigApplicationSwing bean = (ConfigApplicationSwing) RapidBeanImplParent
				.createInstance("org.rapidbeans.presentation.config.swing.ConfigApplicationSwing");
		assertSame(ConfigApplicationSwing.class, bean.getClass());
		assertSame(TypeRapidBean.forName("org.rapidbeans.presentation.config.swing.ConfigApplicationSwing"),
				bean.getType());
		PropertyString name = (PropertyString) bean.getProperty("name");
		assertNotNull(name);
		PropertyChoice guitype = (PropertyChoice) bean.getProperty("guitype");
		assertNotNull(guitype);
		PropertyChoice lnftype = (PropertyChoice) bean.getProperty("lookandfeel");
		assertNotNull(lnftype);
		List<ApplicationLnfTypeSwing> choice = new ArrayList<ApplicationLnfTypeSwing>();
		lnftype.setValue(choice);
		ApplicationLnfTypeSwing lnfType = bean.getLookandfeel();
		assertNull(lnfType);
	}

	/**
	 * test method createInstance().
	 */
	public void testCreateInstanceUnknownClass() {
		try {
			RapidBeanImplParent.createInstance("org.rapidbeans.test.Xyz");
		} catch (TypeNotFoundException e) {
			// O. K.
			assertTrue(true);
		}
	}

	/**
	 * test get the key properties.
	 */
	public void testGetKeyProperties() {
		String descr = "<beantype name=\"TestBean1\" idtype=\"keyprops\">" + "<property name=\"surname\" key=\"true\"/>"
				+ "<property name=\"prename\" key=\"true\"/>" + "<property name=\"dateofbirth\" type=\"date\"/>"
				+ "<property name=\"city\" key=\"true\"/>" + "</beantype>";
		GenericBean bean = TestHelper.createGenericBeanInstance(descr);
		assertEquals(3, ((IdKeyprops) bean.getId()).getKeyprops().length);
	}

	/**
	 * test method createInstance().
	 */
	public void testNavigateFromProperty() {
		ConfigApplication bean = (ConfigApplication) RapidBeanImplParent
				.createInstance("org.rapidbeans.presentation.config.ConfigApplication");
		assertSame(bean, bean.getProperty("name").getBean());
	}

	/**
	 * test the parent beans.
	 */
	public void testGetParentBeans() {
		Document doc = new Document("testdoc",
				TypeRapidBean.forName("org.rapidbeans.presentation.config.ConfigApplication"),
				new File("../org.rapidbeans/testdata/rapidclubadmin/config/Application.xml"));
		assertNotNull(
				(ConfigSubmenu) doc.findBeanByQuery("org.rapidbeans.presentation.config.ConfigSubmenu[name = 'test']"));
		Collection<RapidBean> menuItems = doc
				.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuItem[name = 'test222']");
		assertEquals(1, menuItems.size());
		RapidBean menuItem = menuItems.iterator().next();
		RapidBean[] parentBeans = menuItem.getParentBeans();
		assertEquals(6, parentBeans.length);

		assertEquals("org.rapidbeans.presentation.config.ConfigApplication", parentBeans[0].getType().getName());
		assertEquals("Test", ((ConfigApplication) parentBeans[0]).getName());
		assertEquals("org.rapidbeans.presentation.config.ConfigMainWindow", parentBeans[1].getType().getName());
		assertEquals("mainwindow", ((ConfigMainWindow) parentBeans[1]).getName());
		assertEquals("org.rapidbeans.presentation.config.ConfigMenubar", parentBeans[2].getType().getName());
		assertEquals("menubar", ((ConfigMenubar) parentBeans[2]).getName());
		assertEquals("org.rapidbeans.presentation.config.ConfigSubmenu", parentBeans[3].getType().getName());
		assertEquals("test", ((ConfigSubmenu) parentBeans[3]).getName());
		assertEquals("org.rapidbeans.presentation.config.ConfigSubmenu", parentBeans[4].getType().getName());
		assertEquals("test2", ((ConfigSubmenu) parentBeans[4]).getName());
		assertEquals("org.rapidbeans.presentation.config.ConfigSubmenu", parentBeans[5].getType().getName());
		assertEquals("test22", ((ConfigSubmenu) parentBeans[5]).getName());
	}

	/**
	 * test the right behavior.
	 */
	public void testGetParentCompColProperty() {
		Document testdoc = createTestDocument();
		GenericBean cert = (GenericBean) testdoc.findBean("Certificate", "Fach�bungsleiter");
		assertNotNull(cert);
		PropertyCollection parentColProp = cert.getParentProperty();
		assertEquals("certificates", parentColProp.getType().getPropName());
	}

	/**
	 * By default (no resources specified) the UI (localized) type name is the
	 * (unqualified or short) beantype name
	 */
	public void testToStringGuiTypeDefault() {
		Document doc = createTestDocument();
		RapidBeansLocale locale = new RapidBeansLocale("en");
		locale.init("org.rapidbeans");
		RapidBean billingPeriod = doc.getRoot();
		assertEquals("BillingPeriod", billingPeriod.toStringGuiType(locale));
		RapidBean bluemel = doc.findBean("Trainer", "Bl�mel_Martin");
		assertEquals("Trainer", bluemel.toStringGuiType(locale));
	}

	/**
	 * 
	 */
	public void testToStringGuiTypeWithOwnType() {
		Document doc = createTestDocument();
		RapidBeansLocale locale = new RapidBeansLocale("en");
		locale.init("org.rapidbeans");
		RapidBean billingPeriod = doc.getRoot();
		assertEquals("BillingPeriod", billingPeriod.getType().toStringGui(locale, false, null));
		RapidBean bluemel = doc.findBean("Trainer", "Bl�mel_Martin");
		assertEquals("Trainer", bluemel.getType().toStringGui(locale, false, null));
	}

	/**
	 * 
	 */
	public void testToStringGuiType() {
		Document doc = createTestDocument();
		RapidBeansLocale locale = new RapidBeansLocale("en");
		locale.init("org.rapidbeans");
		RapidBean bluemel = doc.findBean("Trainer", "Bl�mel_Martin");
		assertEquals("Trainer", bluemel.getType().toStringGui(locale, false, null));
		assertEquals("Person", bluemel.getType().getSupertype().toStringGui(locale, false, null));
	}

	/**
	 * create a test document. This document contains: - 1 BillingPeriod object as
	 * top level object - 3 Trainer objects: Bl�mel, Dahlheimer, Dautovic - 3
	 * TrainingDate objects:
	 * 
	 * @return the test document
	 */
	private static Document createTestDocument() {

		final boolean certificatesMandatory = false;

		// set up certificates
		Collection<RapidBean> certs = new ArrayList<RapidBean>();
		GenericBean cert1 = createCertificate("Fach�bungsleiter");
		certs.add(cert1);

		// set up the trainers
		Collection<RapidBean> trainers = new ArrayList<RapidBean>();
		GenericBean trainer1 = createTrainer("Bl�mel", "Martin", true, certificatesMandatory);
		trainers.add(trainer1);
		GenericBean trainer2 = createTrainer("Dahlheimer", "Berit", false, certificatesMandatory);
		trainers.add(trainer2);
		GenericBean trainer3 = createTrainer("Dautovic", "Damir", false, certificatesMandatory);
		trainers.add(trainer3);

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
	 * @param dateBegin the begin date
	 * @param dateEnd   the end date
	 * @return the test bean
	 */
	private static GenericBean createBillingPeriod(final String dateBegin, final String dateEnd) {
		if (RapidBeansTypeLoader.getInstance().lookupType("BillingPeriod") == null) {
			String descr = "<beantype name=\"BillingPeriod\" idtype=\"keyprops\">"
					+ "<property name=\"from\" type=\"date\" key=\"true\"/>"
					+ "<property name=\"to\" type=\"date\" key=\"true\"/>"
					+ "<property name=\"trainers\" type=\"collection\""
					+ " composition=\"true\" targettype=\"Trainer\" collectionclass=\"java.util.TreeSet\"/>"
					+ "<property name=\"trainingdates\" type=\"collection\""
					+ " composition=\"true\" targettype=\"TrainingDate\" collectionclass=\"java.util.TreeSet\"/>" + "/>"
					+ "<property name=\"certificates\" type=\"collection\""
					+ " composition=\"true\" targettype=\"Certificate\" collectionclass=\"java.util.TreeSet\"/>" + "/>"
					+ "</beantype>";
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
	 * @param lastname  last name
	 * @param firstname first name
	 * @param leader    if the trainer is certified exercise leader
	 * @param mandatory if the certificates property is mandatory
	 * 
	 * @return the test bean
	 */
	private static GenericBean createTrainer(final String lastname, final String firstname, final boolean leader,
			final boolean mandatory) {
		if (RapidBeansTypeLoader.getInstance().lookupType("Person") == null) {
			String descr = "<beantype name=\"Person\" idtype=\"keyprops\">"
					+ "<property name=\"lastname\" type=\"string\" key=\"true\"/>"
					+ "<property name=\"firstname\" type=\"string\" key=\"true\"/>" + "</beantype>";
			XmlNode xmlNode = XmlNode.getDocumentTopLevel(new ByteArrayInputStream(descr.getBytes()));
			new TypeRapidBean(null, xmlNode, null, true);
		}
		if (RapidBeansTypeLoader.getInstance().lookupType("Trainer") == null) {
			String descr = "<beantype name=\"Trainer\" idtype=\"keyprops\" extends=\"Person\">"
					// +
					// "<property name=\"lastname\" type=\"string\" key=\"true\"/>"
					// +
					// "<property name=\"firstname\" type=\"string\" key=\"true\"/>"
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
	 * @param name name
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
	 * @param name           name
	 * @param dayofweek      { monday, ..., sunday }
	 * @param tstart         starting time
	 * @param tend           end time
	 * @param defaulttrainer the default trainer
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

	/**
	 * common tear down method.
	 */
	public void tearDown() {
		TestHelperTypeLoader.clearBeanTypesGeneric();
	}
}
