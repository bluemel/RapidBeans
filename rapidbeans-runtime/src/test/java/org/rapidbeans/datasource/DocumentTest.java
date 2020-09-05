/*
 * Rapid Beans Framework: DocumentTest.java
 * 
 * Copyright Martin Bluemel, 2006
 * 
 * 31.01.2006
 */
package org.rapidbeans.datasource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.rapidbeans.core.basic.GenericBean;
import org.rapidbeans.core.basic.IdGeneratorNumeric;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanImplParent;
import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.exception.BeanDuplicateException;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationInstanceAssocTwiceException;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TestHelperTypeLoader;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.FileHelper;
import org.rapidbeans.core.util.XmlNode;
import org.rapidbeans.presentation.config.ApplicationGuiType;
import org.rapidbeans.presentation.config.ConfigApplication;
import org.rapidbeans.presentation.config.ConfigLocale;
import org.rapidbeans.presentation.config.ConfigMainWindow;
import org.rapidbeans.presentation.config.ConfigMenuEntry;
import org.rapidbeans.presentation.config.ConfigMenuItem;
import org.rapidbeans.presentation.config.ConfigMenubar;
import org.rapidbeans.presentation.config.ConfigSubmenu;
import org.rapidbeans.presentation.config.swing.ConfigApplicationSwing;
import org.rapidbeans.service.Action;
import org.rapidbeans.test.BillingPeriod;
import org.rapidbeans.test.ClosingPeriod;
import org.rapidbeans.test.Location;
import org.rapidbeans.test.TestBean;
import org.rapidbeans.test.TestBeanSimple;
import org.rapidbeans.test.codegen.Address;
import org.rapidbeans.test.codegen.AddressBook;
import org.rapidbeans.test.codegen.Person;

/**
 * Unit Tests for class Document.
 * 
 * @author Martin Bluemel
 */
public class DocumentTest {

	public static final char UE = '\u00dc';

	public static final char ue = '\u00fc';

	public static final char SS = '\u00df';

	/**
	 * test reading a document from file.
	 */
	@Test
	public void testReadFromFile() {
		Document testdoc = new Document(TypeRapidBean.forName(ConfigApplication.class.getName()),
				new File("src/test/resources/rapidclubadmin/config/Application.xml"));
		ConfigApplication config = (ConfigApplication) testdoc.getRoot();
		Assert.assertSame(ApplicationGuiType.swing, config.getGuitype());
		ConfigMainWindow mainWindow = config.getMainwindow();
		Assert.assertEquals("mainwindow", mainWindow.getName());
		ConfigMenubar menubar = mainWindow.getMenubar();
		Assert.assertEquals("menubar", menubar.getName());
		Collection<ConfigSubmenu> menus = menubar.getMenus();
		Assert.assertEquals(3, menus.size());
		Action action;
		for (ConfigSubmenu menu : menus) {
			if (menu.getName().equals("file")) {
				for (ConfigMenuEntry entry : menu.getMenuentrys()) {
					if (entry.getName().equals("quit")) {
						action = ((ConfigMenuItem) entry).getAction();
						Assert.assertEquals("org.rapidbeans.service.ActionQuit", action.getClassname());
						Assert.assertEquals("org.rapidbeans.service.Action", action.getClass().getName());
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReadFromFileGeneric() {
		Document testdoc = new Document(new File("src/test/resources/testBeanGenericExample.xml"));
		GenericBean bean = (GenericBean) testdoc.getRoot();
		Assert.assertEquals("Bluemel", bean.getPropValue("surname"));
		Assert.assertEquals("Martin", bean.getPropValue("prename"));
		Assert.assertEquals(1, ((List<GenericBean>) bean.getPropValue("children")).size());
		Assert.assertEquals("Bluemel",
				((List<GenericBean>) bean.getPropValue("children")).get(0).getPropValue("surname"));
		Assert.assertEquals("Melanie",
				((List<GenericBean>) bean.getPropValue("children")).get(0).getPropValue("prename"));
		Assert.assertSame(bean, ((List<GenericBean>) bean.getPropValue("children")).get(0).getParentBean());
	}

	@Test
	public void testReadFromFileStrict() {
		Document testdoc = new Document(new File("src/test/resources/testBeanStrictExample.xml"));
		TestBean bean = (TestBean) testdoc.getRoot();
		Assert.assertEquals("Bluemel", bean.getSurname());
		Assert.assertEquals("Martin", bean.getPrename());
		Assert.assertEquals(1, bean.getChildren().size());
		Assert.assertEquals("Bluemel", bean.getChildren().get(0).getSurname());
		Assert.assertEquals("Melanie", bean.getChildren().get(0).getPrename());
		Assert.assertSame(bean, bean.getChildren().get(0).getParentBean());
	}

	@Test
	public void testReadFromFileSimple() {
		Document testdoc = new Document(new File("src/test/resources/testBeanSimpleExample.xml"));
		TestBeanSimple bean = (TestBeanSimple) testdoc.getRoot();
		Assert.assertEquals("Bluemel", bean.getSurname());
		Assert.assertEquals("Martin", bean.getPrename());
		Assert.assertEquals(1, bean.getChildren().size());
		Assert.assertEquals("Bluemel", bean.getChildren().get(0).getSurname());
		Assert.assertEquals("Melanie", bean.getChildren().get(0).getPrename());
		Assert.assertSame(bean, bean.getChildren().get(0).getParentBean());
	}

	/**
	 * test reading a document from file.
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void testReadFromURL() throws MalformedURLException {
		Document testdoc = new Document(TypeRapidBean.forName(ConfigApplication.class.getName()),
				new File("src/test/resources/rapidclubadmin" + "/config/Application.xml").toURI().toURL());
		ConfigApplication config = (ConfigApplication) testdoc.getRoot();
		Assert.assertSame(ApplicationGuiType.swing, config.getGuitype());
		ConfigMainWindow mainWindow = config.getMainwindow();
		Assert.assertEquals("mainwindow", mainWindow.getName());
		ConfigMenubar menubar = mainWindow.getMenubar();
		Assert.assertEquals("menubar", menubar.getName());
		Collection<ConfigSubmenu> menus = menubar.getMenus();
		Assert.assertEquals(3, menus.size());
		Action action;
		for (ConfigSubmenu menu : menus) {
			if (menu.getName().equals("file")) {
				for (ConfigMenuEntry entry : menu.getMenuentrys()) {
					if (entry.getName().equals("quit")) {
						action = ((ConfigMenuItem) entry).getAction();
						Assert.assertEquals("org.rapidbeans.service.ActionQuit", action.getClassname());
						Assert.assertEquals("org.rapidbeans.service.Action", action.getClass().getName());
					}
				}
			}
		}
	}

	/**
	 * test reading a document from file.
	 * 
	 * @throws MalformedURLException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testReadFromInputStream() throws MalformedURLException, FileNotFoundException {
		File file = new File("src/test/resources/rapidclubadmin/config/Application.xml");
		FileInputStream is = new FileInputStream(file);
		Document testdoc = new Document("test", TypeRapidBean.forName(ConfigApplication.class.getName()),
				file.toURI().toURL(), is);
		ConfigApplication config = (ConfigApplication) testdoc.getRoot();
		Assert.assertSame(ApplicationGuiType.swing, config.getGuitype());
		ConfigMainWindow mainWindow = config.getMainwindow();
		Assert.assertEquals("mainwindow", mainWindow.getName());
		ConfigMenubar menubar = mainWindow.getMenubar();
		Assert.assertEquals("menubar", menubar.getName());
		Collection<ConfigSubmenu> menus = menubar.getMenus();
		Assert.assertEquals(3, menus.size());
		Action action;
		for (ConfigSubmenu menu : menus) {
			if (menu.getName().equals("file")) {
				for (ConfigMenuEntry entry : menu.getMenuentrys()) {
					if (entry.getName().equals("quit")) {
						action = ((ConfigMenuItem) entry).getAction();
						Assert.assertEquals("org.rapidbeans.service.ActionQuit", action.getClassname());
						Assert.assertEquals("org.rapidbeans.service.Action", action.getClass().getName());
					}
				}
			}
		}
	}

	/**
	 * test reading a document from ISO encoded file.
	 * 
	 * @throws IOException if something goes wrong
	 */
	@Test
	public void testReadEncodingISO() throws IOException {

		// the test XML file (ISO-8859-1 encoded)
		File file = new File("src/test/resources/EncodingTestISO.xml");

		// test readability of German "umlauts" with ISO-8859-1 configured
		// reader
		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
		for (int i = 0; i < 5; i++) {
			lnr.readLine();
		}
		String line = lnr.readLine();
		Assert.assertEquals("\t\tid=\"" + UE + "berseestra" + SS + "e\"", line);
		lnr.close();

		// prove that an UTF-8 configured reader can't read the "umlauts"
		// correctly
		lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		for (int i = 0; i < 5; i++) {
			lnr.readLine();
		}
		Assert.assertFalse(lnr.readLine().equals("\t\tid=\"" + UE + "berseestr" + SS + "e\""));
		lnr.close();

		// read the "umlauts" with a RapidBeans document
		Document testdoc = new Document(file);
		Location loc = (Location) testdoc.findBean("org.rapidbeans.test.Location", UE + "berseestra" + SS + "e");
		Assert.assertEquals(UE + "berseestra" + SS + "e", loc.getName());
		Assert.assertEquals("ISO-8859-1", testdoc.getEncoding());
	}

	/**
	 * test reading a document from UTF-8 encoded file.
	 * 
	 * @throws IOException if something goes wrong
	 */
	@Test
	public void testReadEncodingUTF() throws IOException {
		// the test XML file (ISO-8859-1 encoded)
		File file = new File("src/test/resources/EncodingTestUTF.xml");

		// test readability of German "umlauts" with ISO-8859-1 configured
		// reader
		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		for (int i = 0; i < 5; i++) {
			lnr.readLine();
		}
		String line = lnr.readLine();
		Assert.assertEquals("\t\tid=\"" + UE + "berseestra" + SS + "e\"", line);
		lnr.close();

		// prove that an UTF-8 configured reader can't read the "umlauts"
		// correctly
		lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
		for (int i = 0; i < 6; i++) {
			lnr.readLine();
		}
		line = lnr.readLine();
		Assert.assertFalse(line.equals("\t\tid=\"" + UE + "berseestra" + SS + "e\""));
		lnr.close();

		// read the "umlauts" with a RapidBeans document
		Document testdoc = new Document(file);
		Location loc = (Location) testdoc.findBean("org.rapidbeans.test.Location", UE + "berseestra" + SS + "e");
		Assert.assertEquals(UE + "berseestra" + SS + "e", loc.getName());
		Assert.assertEquals("UTF-8", testdoc.getEncoding());
	}

	/**
	 * test reading a document from ISO encoded file.
	 * 
	 * @throws IOException if something goes wrong
	 */
	@Test
	public void testWriteEncodingSame() throws IOException {
		TypePropertyCollection loctype = (TypePropertyCollection) TypeRapidBean
				.forName("org.rapidbeans.test.BillingPeriod").getPropertyType("locations");
		Assert.assertSame(ArrayList.class, loctype.getCollectionClass());
		TypePropertyCollection cptype = (TypePropertyCollection) TypeRapidBean
				.forName("org.rapidbeans.test.BillingPeriod").getPropertyType("closingperiods");
		Assert.assertSame(ArrayList.class, cptype.getCollectionClass());
		TypePropertyCollection loctype1 = (TypePropertyCollection) TypeRapidBean
				.forName("org.rapidbeans.test.ClosingPeriod").getPropertyType("locations");
		loctype1.setCollectionClass(ArrayList.class);
		TypePropertyCollection cptype1 = (TypePropertyCollection) TypeRapidBean.forName("org.rapidbeans.test.Location")
				.getPropertyType("closedons");
		cptype1.setCollectionClass(ArrayList.class);
		// the test XML file (ISO-8859-1 encoded)
		File file = new File("src/test/resources/EncodingTestISO.xml");
		File testfile = new File("src/test/resources/TestEncodingISO.xml");
		if (testfile.exists()) {
			testfile.delete();
		}

		// read the "umlauts" with a RapidBeans document
		Document testdoc = new Document(file);
		Assert.assertEquals("ISO-8859-1", testdoc.getEncoding());
		testdoc.setUrl(testfile.toURI().toURL());
		testdoc.save();
		Assert.assertTrue(FileHelper.filesEqual(file, testfile, true, true));
		testfile.delete();
	}

	/**
	 * test reading a document from ISO encoded file.
	 * 
	 * @throws IOException if something goes wrong
	 */
	@Test
	public void testWriteEncodingDifferent() throws IOException {
		// the test XML file (ISO-8859-1 encoded)
		File file = new File("src/test/resources/EncodingTestISO.xml");
		File testfileExpected = new File("src/test/resources/EncodingTestUTF.xml");
		File testfile = new File("src/test/resources/TestEncodingChange.xml");
		if (testfile.exists()) {
			testfile.delete();
		}

		// read the "umlauts" with a RapidBeans document
		Document testdoc = new Document("testdoc", file);
		Assert.assertEquals("ISO-8859-1", testdoc.getEncoding());
		testdoc.setUrl(testfile.toURI().toURL());
		testdoc.setEncoding("UTF-8");
		testdoc.save();
		Assert.assertTrue(FileHelper.filesEqual(testfileExpected, testfile, true, true));
		testfile.delete();
	}

	/**
	 * test write.
	 * 
	 * @throws IOException if IO fails
	 */
	@Test
	public void testWrite() throws IOException {
		File file1 = new File("src/test/resources/rapidclubadmin/config/Application.xml");
		File file2 = new File("src/test/resources/rapidclubadmin/config/ApplicationTest.xml");
		Document testdoc = new Document(TypeRapidBean.forName(ConfigApplicationSwing.class.getName()), file1);
		testdoc.setUrl(file2.toURI().toURL());
		testdoc.save();
		Assert.assertTrue(FileHelper.filesEqual(file1, file2, true, true));
		file2.delete();
	}

	@Test
	public void testInsertComponentExplicitly() {
		// set up a test document out of file Application.xml
		File file = new File("src/test/resources/rapidclubadmin/config/Application.xml");
		Document testdoc = new Document(TypeRapidBean.forName(ConfigApplication.class.getName()), file);
		ConfigApplication clientCfg = (ConfigApplication) testdoc.getRoot();
		Collection<ConfigLocale> locales = clientCfg.getLocales();
		Assert.assertEquals(1, locales.size());

		// create a new RapidBeansLocale bean and assert that it is not
		// living in the document
		ConfigLocale locale = new ConfigLocale("at");
		Assert.assertNull(locale.getContainer());
		Assert.assertNull(testdoc.findBean("org.rapidbeans.core.common.RapidBeansLocale", locale.getIdString()));

		// add the new bean as component to the Application bean that
		// is already living in the document
		Assert.assertFalse(testdoc.contains(locale));
		clientCfg.addLocale(locale);
	}

	/**
	 * test inserting a simple bean as component to a bean that is already living in
	 * a document. This should implicitly insert this Bean into the document.
	 */
	@Test
	public void testInsertComponentImplicitly() {

		// set up a test document out of file Application.xml
		File file = new File("src/test/resources/rapidclubadmin/config/Application.xml");
		Document testdoc = new Document(TypeRapidBean.forName(ConfigApplication.class.getName()), file);
		ConfigApplication clientCfg = (ConfigApplication) testdoc.getRoot();
		Collection<ConfigLocale> locales = clientCfg.getLocales();
		Assert.assertEquals(1, locales.size());

		// create a new RapidBeansLocale bean and assert that it is not
		// living in the document
		ConfigLocale locale = new ConfigLocale("at");
		Assert.assertNull(locale.getContainer());
		Assert.assertNull(testdoc.findBean("org.rapidbeans.core.common.RapidBeansLocale", locale.getIdString()));

		// add the new bean as component to the Application bean that
		// is already living in the document
		Assert.assertFalse(testdoc.contains(locale));
		clientCfg.addLocale(locale);

		Assert.assertSame(testdoc, locale.getContainer());
		Assert.assertTrue(testdoc.contains(locale));
		Assert.assertSame(locale, testdoc.findBean(ConfigLocale.class.getName(), locale.getIdString()));
		locales = clientCfg.getLocales();
		Assert.assertEquals(2, locales.size());
		Assert.assertSame(clientCfg, locale.getParentBean());
	}

	/**
	 * test inserting a simple bean as component to a bean that is already living in
	 * a document twice.
	 */
	@Test(expected = BeanDuplicateException.class)
	public void testInsertComponentImplicitlyDuplicate() {
		// set up a test document out of file Application.xml
		Document testdoc = createTestDocument(false);
		GenericBean bpRoot = (GenericBean) testdoc.getRoot();
		PropertyCollection bpRootTrainers = (PropertyCollection) bpRoot.getProperty("trainers");

		// create a new Trainer bean and assert
		// that it is not living in the document
		GenericBean trainer1 = createTrainer("A", "B", true, false);
		bpRootTrainers.addLink(trainer1);

		GenericBean trainer2 = createTrainer("A", "B", true, false);
		try {
			bpRootTrainers.addLink(trainer2);
			Assert.fail("expected a BeanDuplicateException");
		} catch (BeanDuplicateException e) {
			Assert.assertTrue(true);
		}

		GenericBean trainer3 = createTrainer("A", "B", true, false);
		bpRootTrainers.addLink(trainer3);
	}

	/**
	 * test a tree set.
	 */
	@Test
	public void testNMAssociationInverseArrayList() {
		// configure collection properties of Location and ClosingPeriod
		// to use ArrayList as collection implementing class
		((TypePropertyCollection) (new BillingPeriod()).getProperty("locations").getType())
				.setCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new BillingPeriod()).getProperty("closingperiods").getType())
				.setCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new Location()).getProperty("closedons").getType())
				.setCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations").getType())
				.setCollectionClass(ArrayList.class);
		tstNMAssociationInverse();
	}

	/**
	 * test a tree set.
	 */
	@Test
	public void testNMAssociationInverseLinkedHashSet() {
		// configure collection properties of Location and ClosingPeriod
		// to use LinkedHashSet as collection implementing class
		((TypePropertyCollection) (new BillingPeriod()).getProperty("locations").getType())
				.setCollectionClass(LinkedHashSet.class);
		((TypePropertyCollection) (new BillingPeriod()).getProperty("closingperiods").getType())
				.setCollectionClass(LinkedHashSet.class);
		((TypePropertyCollection) (new Location()).getProperty("closedons").getType())
				.setCollectionClass(LinkedHashSet.class);
		((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations").getType())
				.setCollectionClass(LinkedHashSet.class);
		tstNMAssociationInverse();
		((TypePropertyCollection) (new BillingPeriod()).getProperty("locations").getType())
				.setCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new BillingPeriod()).getProperty("closingperiods").getType())
				.setCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new Location()).getProperty("closedons").getType())
				.setCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations").getType())
				.setCollectionClass(ArrayList.class);
	}

	/**
	 * test a tree set.
	 */
	@Test
	public void testNMAssociationInverseTreeSet() {
		// configure collection properties of Location and ClosingPeriod
		// to use TreeSet as collection implementing class
		((TypePropertyCollection) (new BillingPeriod()).getProperty("locations").getType())
				.setCollectionClass(TreeSet.class);
		((TypePropertyCollection) (new BillingPeriod()).getProperty("closingperiods").getType())
				.setCollectionClass(TreeSet.class);
		((TypePropertyCollection) (new Location()).getProperty("closedons").getType())
				.setCollectionClass(TreeSet.class);
		((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations").getType())
				.setCollectionClass(TreeSet.class);
		tstNMAssociationInverse();
		((TypePropertyCollection) (new BillingPeriod()).getProperty("locations").getType())
				.setCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new BillingPeriod()).getProperty("closingperiods").getType())
				.setCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new Location()).getProperty("closedons").getType())
				.setCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations").getType())
				.setCollectionClass(ArrayList.class);
	}

	/**
	 * test collection properties used in conjunction with an N:M association.
	 */
	private void tstNMAssociationInverse() {
		// configure collection properties of Location and ClosingPeriod
		// to use TreeSet as collection implementing class
		((TypePropertyCollection) (new Location()).getProperty("closedons").getType())
				.setCollectionClass(TreeSet.class);
		((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations").getType())
				.setCollectionClass(TreeSet.class);
		// set up two locations and closing periods
		ClosingPeriod cp1 = new ClosingPeriod("\"20050101\" \"1\"");
		ClosingPeriod cp2 = new ClosingPeriod("\"20050201\" \"2\"");
		Location locA = new Location("\"A\"");
		Location locB = new Location("\"B\"");
		BillingPeriod bp = new BillingPeriod();
		Document doc = new Document("test", bp);
		Assert.assertNotNull(doc);
		bp.addClosingperiod(cp1);
		bp.addClosingperiod(cp2);
		bp.addLocation(locA);
		bp.addLocation(locB);

		// at the beginning all four collection properties are
		// undefined (null)
		Assert.assertNull(locA.getClosedons());
		Assert.assertNull(locB.getClosedons());
		Assert.assertNull(cp1.getLocations());
		Assert.assertNull(cp2.getLocations());

		// link locA with cp1
		locA.addClosedon(cp1);
		Assert.assertEquals(1, locA.getClosedons().size());
		ReadonlyListCollection<ClosingPeriod> cps = (ReadonlyListCollection<ClosingPeriod>) locA.getClosedons();
		Assert.assertSame(cp1, cps.get(0));
		Assert.assertNull(locB.getClosedons());
		ReadonlyListCollection<Location> locs = (ReadonlyListCollection<Location>) cp1.getLocations();
		Assert.assertSame(locA, locs.get(0));
		Assert.assertEquals(1, cp1.getLocations().size());
		Assert.assertNull(cp2.getLocations());

		// link locA with cp2
		locA.addClosedon(cp2);
		Assert.assertEquals(2, locA.getClosedons().size());
		cps = (ReadonlyListCollection<ClosingPeriod>) locA.getClosedons();
		Assert.assertSame(cp1, cps.get(0));
		Assert.assertSame(cp2, cps.get(1));
		Assert.assertNull(locB.getClosedons());
		Assert.assertEquals(1, cp1.getLocations().size());
		locs = (ReadonlyListCollection<Location>) cp1.getLocations();
		Assert.assertSame(locA, locs.get(0));
		Assert.assertEquals(1, cp2.getLocations().size());
		locs = (ReadonlyListCollection<Location>) cp2.getLocations();
		Assert.assertSame(locA, locs.get(0));

		// link locB with cp1
		locB.addClosedon(cp1);
		Assert.assertEquals(2, locA.getClosedons().size());
		cps = (ReadonlyListCollection<ClosingPeriod>) locA.getClosedons();
		Assert.assertSame(cp1, cps.get(0));
		Assert.assertSame(cp2, cps.get(1));
		Assert.assertEquals(1, locB.getClosedons().size());
		cps = (ReadonlyListCollection<ClosingPeriod>) locB.getClosedons();
		Assert.assertSame(cp1, cps.get(0));
		Assert.assertEquals(2, cp1.getLocations().size());
		locs = (ReadonlyListCollection<Location>) cp1.getLocations();
		Assert.assertSame(locA, locs.get(0));
		Assert.assertSame(locB, locs.get(1));
		Assert.assertEquals(1, cp2.getLocations().size());
		locs = (ReadonlyListCollection<Location>) cp2.getLocations();
		Assert.assertSame(locA, locs.get(0));

		// link locB with cp2
		locB.addClosedon(cp2);
		Assert.assertEquals(2, locA.getClosedons().size());
		cps = (ReadonlyListCollection<ClosingPeriod>) locA.getClosedons();
		Assert.assertSame(cp1, cps.get(0));
		Assert.assertSame(cp2, cps.get(1));
		Assert.assertEquals(2, locB.getClosedons().size());
		cps = (ReadonlyListCollection<ClosingPeriod>) locB.getClosedons();
		Assert.assertSame(cp1, cps.get(0));
		Assert.assertSame(cp2, cps.get(1));
		Assert.assertEquals(2, cp1.getLocations().size());
		locs = (ReadonlyListCollection<Location>) cp1.getLocations();
		Assert.assertSame(locA, locs.get(0));
		Assert.assertSame(locB, locs.get(1));
		Assert.assertEquals(2, cp2.getLocations().size());
		locs = (ReadonlyListCollection<Location>) cp2.getLocations();
		Assert.assertSame(locA, locs.get(0));
		Assert.assertSame(locB, locs.get(1));

		Assert.assertEquals(2, cp1.getLocations().size());
		try {
			cp1.addLocation(locA);
			Assert.fail("expected ValidationInstanceAssocTwiceException");
		} catch (ValidationInstanceAssocTwiceException e) {
			Assert.assertTrue(true);
		}
		Assert.assertEquals(2, cp1.getLocations().size());
		try {
			cp1.addLocation(locB);
			Assert.fail("expected ValidationInstanceAssocTwiceException");
		} catch (ValidationInstanceAssocTwiceException e) {
			Assert.assertTrue(true);
		}
		Assert.assertEquals(2, cp1.getLocations().size());
		Assert.assertEquals(2, locB.getClosedons().size());
		try {
			locB.addClosedon(cp1);
			Assert.fail("expected ValidationInstanceAssocTwiceException");
		} catch (ValidationInstanceAssocTwiceException e) {
			Assert.assertTrue(true);
		}
		Assert.assertEquals(2, locB.getClosedons().size());
		try {
			locB.addClosedon(cp2);
			Assert.fail("expected ValidationInstanceAssocTwiceException");
		} catch (ValidationInstanceAssocTwiceException e) {
			Assert.assertTrue(true);
		}
		Assert.assertEquals(2, locB.getClosedons().size());

		((TypePropertyCollection) (new Location()).getProperty("closedons").getType())
				.setCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations").getType())
				.setCollectionClass(ArrayList.class);
	}

	/**
	 * Test loading the following document. 4 instances of 2 class.es There is an n
	 * : m association betwee the two classes. This association can be navigated
	 * bidirectionally.
	 */
	@Test
	public void testLoadDocNMInverseArrayList() {
		TypePropertyCollection.setDefaultCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new BillingPeriod()).getProperty("locations").getType())
				.setCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new BillingPeriod()).getProperty("closingperiods").getType())
				.setCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new Location()).getProperty("closedons").getType())
				.setCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations").getType())
				.setCollectionClass(ArrayList.class);

		Document doc = new Document(new File("src/test/resources/NMAssocTest01.xml"));
		Assert.assertEquals(2, doc.findBeansByType("org.rapidbeans.test.ClosingPeriod").size());
		Assert.assertEquals(2, doc.findBeansByType("org.rapidbeans.test.Location").size());
		BillingPeriod bp = (BillingPeriod) doc.findBeansByType("org.rapidbeans.test.BillingPeriod").iterator().next();
		Location locA = (Location) doc.findBean("org.rapidbeans.test.Location", "A");
		Location locB = (Location) doc.findBean("org.rapidbeans.test.Location", "B");
		ClosingPeriod cp1 = (ClosingPeriod) doc.findBean("org.rapidbeans.test.ClosingPeriod", "20050101_1");
		ClosingPeriod cp2 = (ClosingPeriod) doc.findBean("org.rapidbeans.test.ClosingPeriod", "20050201_2");

		List<RapidBean> beans = (List<RapidBean>) doc.findBeansByType("org.rapidbeans.test.Location");
		Assert.assertEquals(2, beans.size());
		Assert.assertSame(locA, beans.get(0));
		Assert.assertSame(locB, beans.get(1));

		Collection<?> col = bp.getLocations();
		Assert.assertEquals(2, col.size());
		Iterator<?> it = col.iterator();
		Assert.assertSame(locB, it.next());
		Assert.assertSame(locA, it.next());

		col = cp1.getLocations();
		Assert.assertEquals(2, col.size());
		it = col.iterator();
		Assert.assertSame(locB, it.next());
		Assert.assertSame(locA, it.next());

		col = cp2.getLocations();
		Assert.assertEquals(2, col.size());
		it = col.iterator();
		Assert.assertSame(locB, it.next());
		Assert.assertSame(locA, it.next());

		col = bp.getClosingperiods();
		Assert.assertEquals(2, col.size());
		it = col.iterator();
		Assert.assertSame(cp2, it.next());
		Assert.assertSame(cp1, it.next());

		col = locA.getClosedons();
		Assert.assertEquals(2, col.size());
		it = col.iterator();
		Assert.assertSame(cp2, it.next());
		Assert.assertSame(cp1, it.next());

		col = locB.getClosedons();
		Assert.assertEquals(2, col.size());
		it = col.iterator();
		Assert.assertSame(cp2, it.next());
		Assert.assertSame(cp1, it.next());

		cp2.delete();
		Assert.assertEquals(1, doc.findBeansByType("org.rapidbeans.test.ClosingPeriod").size());
		Assert.assertNull(doc.findBean("org.rapidbeans.test.ClosingPeriod", "20050201_2"));

		col = cp1.getLocations();
		Assert.assertEquals(2, col.size());
		it = col.iterator();
		Assert.assertSame(locB, it.next());
		Assert.assertSame(locA, it.next());

		// cp2 is deleted from the container
		// but not released by garbage collection
		// since we still hold the reference here
		// At least all references to location should be removed
		Assert.assertEquals(0, cp2.getLocations().size());

		col = bp.getClosingperiods();
		Assert.assertEquals(1, col.size());
		it = col.iterator();
		Assert.assertSame(cp1, it.next());

		col = locA.getClosedons();
		Assert.assertEquals(1, col.size());
		it = col.iterator();
		Assert.assertSame(cp1, it.next());

		col = locB.getClosedons();
		Assert.assertEquals(1, col.size());
		it = col.iterator();
		Assert.assertSame(cp1, it.next());

		locB.delete();
		Assert.assertEquals(1, doc.findBeansByType("org.rapidbeans.test.Location").size());
		Assert.assertNull(doc.findBean("org.rapidbeans.test.Location", "B"));

		Assert.assertEquals(0, locB.getClosedons().size());

		col = cp1.getLocations();
		Assert.assertEquals(1, col.size());
		it = col.iterator();
		Assert.assertSame(locA, it.next());

		col = locA.getClosedons();
		Assert.assertEquals(1, col.size());
		it = col.iterator();
		Assert.assertSame(cp1, it.next());
	}

	/**
	 * Test loading the following document. 4 instances of 2 class.es There is an n
	 * : m association betwee the two classes. This association can be navigated
	 * bidirectionally.
	 */
	@Test
	public void testLoadDocNMInverseTreeSet() {
		((TypePropertyCollection) (new BillingPeriod()).getProperty("locations").getType())
				.setCollectionClass(TreeSet.class);
		((TypePropertyCollection) (new BillingPeriod()).getProperty("closingperiods").getType())
				.setCollectionClass(TreeSet.class);
		((TypePropertyCollection) (new Location()).getProperty("closedons").getType())
				.setCollectionClass(TreeSet.class);
		((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations").getType())
				.setCollectionClass(TreeSet.class);
		Document doc = new Document(new File("src/test/resources/NMAssocTest01.xml"));
		Assert.assertEquals(2, doc.findBeansByType("org.rapidbeans.test.ClosingPeriod").size());
		Assert.assertEquals(2, doc.findBeansByType("org.rapidbeans.test.Location").size());
		BillingPeriod bp = (BillingPeriod) doc.findBeansByType("org.rapidbeans.test.BillingPeriod").iterator().next();
		Location locA = (Location) doc.findBean("org.rapidbeans.test.Location", "A");
		Location locB = (Location) doc.findBean("org.rapidbeans.test.Location", "B");
		ClosingPeriod cp1 = (ClosingPeriod) doc.findBean("org.rapidbeans.test.ClosingPeriod", "20050101_1");
		ClosingPeriod cp2 = (ClosingPeriod) doc.findBean("org.rapidbeans.test.ClosingPeriod", "20050201_2");

		List<RapidBean> beans = (List<RapidBean>) doc.findBeansByType("org.rapidbeans.test.Location");
		Assert.assertEquals(2, beans.size());
		Assert.assertSame(locA, beans.get(0));
		Assert.assertSame(locB, beans.get(1));

		Collection<?> col = bp.getLocations();
		Assert.assertEquals(2, col.size());
		Iterator<?> it = col.iterator();
		Assert.assertSame(locA, it.next());
		Assert.assertSame(locB, it.next());

		col = cp1.getLocations();
		Assert.assertEquals(2, col.size());
		it = col.iterator();
		Assert.assertSame(locA, it.next());
		Assert.assertSame(locB, it.next());

		col = cp2.getLocations();
		Assert.assertEquals(2, col.size());
		it = col.iterator();
		Assert.assertSame(locA, it.next());
		Assert.assertSame(locB, it.next());

		col = bp.getClosingperiods();
		Assert.assertEquals(2, col.size());
		it = col.iterator();
		Assert.assertSame(cp1, it.next());
		Assert.assertSame(cp2, it.next());

		col = locA.getClosedons();
		Assert.assertEquals(2, col.size());
		it = col.iterator();
		Assert.assertSame(cp1, it.next());
		Assert.assertSame(cp2, it.next());

		col = locB.getClosedons();
		Assert.assertEquals(2, col.size());
		it = col.iterator();
		Assert.assertSame(cp1, it.next());
		Assert.assertSame(cp2, it.next());

		cp2.delete();
		Assert.assertEquals(1, doc.findBeansByType("org.rapidbeans.test.ClosingPeriod").size());
		Assert.assertNull(doc.findBean("org.rapidbeans.test.ClosingPeriod", "20050201_2"));

		col = cp1.getLocations();
		Assert.assertEquals(2, col.size());
		it = col.iterator();
		Assert.assertSame(locA, it.next());
		Assert.assertSame(locB, it.next());

		// cp2 is deleted from the container
		// but not released by garbage collection
		// since we still hold the reference here
		// At least all references to location should be removed
		Assert.assertEquals(0, cp2.getLocations().size());

		col = bp.getClosingperiods();
		Assert.assertEquals(1, col.size());
		it = col.iterator();
		Assert.assertSame(cp1, it.next());

		col = locA.getClosedons();
		Assert.assertEquals(1, col.size());
		it = col.iterator();
		Assert.assertSame(cp1, it.next());

		col = locB.getClosedons();
		Assert.assertEquals(1, col.size());
		it = col.iterator();
		Assert.assertSame(cp1, it.next());

		locB.delete();
		Assert.assertEquals(1, doc.findBeansByType("org.rapidbeans.test.Location").size());
		Assert.assertNull(doc.findBean("org.rapidbeans.test.Location", "B"));

		Assert.assertEquals(0, locB.getClosedons().size());

		col = cp1.getLocations();
		Assert.assertEquals(1, col.size());
		it = col.iterator();
		Assert.assertSame(locA, it.next());

		col = locA.getClosedons();
		Assert.assertEquals(1, col.size());
		it = col.iterator();
		Assert.assertSame(cp1, it.next());

		Document doc1 = new Document(new File("src/test/resources/treesettest01.xml"));
		col = doc1.findBeansByType("org.rapidbeans.test.Location");
		it = col.iterator();
		Location loc1 = (Location) it.next();
		Assert.assertEquals("Eurythmiesaal 1 Waldorfschule", loc1.getIdString());
		Location loc2 = (Location) it.next();
		Assert.assertEquals("Turnhalle Grundschule S" + ue + "d", loc2.getIdString());
		col = loc1.getClosedons();
		it = col.iterator();
		Assert.assertEquals("20051222_Weihnachtsferien", ((RapidBean) it.next()).getIdString());
		Assert.assertEquals("20060116_Schulputztag", ((RapidBean) it.next()).getIdString());
	}

	/**
	 * test creating an association instance between beans within a document.
	 */
	@Test
	public void testAddLinkFromBeanIndocToBeanIndoc() {

		AddressBook book = new AddressBook();
		Document doc = new Document("test", book);
		Address adr = new Address();
		Person martin = new Person("\"Martin\" \"Bl�mel\" \"19641014\"");
		Person jojo = new Person("\"Johannes\" \"Bl�mel\" \"19641014\"");
		TypeRapidBean.forName("org.rapidbeans.test.codegen.Address").setIdGenerator(new IdGeneratorNumeric());
		book.addAddress(adr);
		book.addPerson(martin);
		book.addPerson(jojo);
		Assert.assertSame(doc, jojo.getContainer());
		Assert.assertNull(adr.getInhabitants());
		Assert.assertNull(martin.getAddress());
		Assert.assertNull(jojo.getAddress());

		// add the 2 Persons to the Address as inhabitants
		adr.addInhabitant(martin);
		adr.addInhabitant(jojo);

		// assert the Persons being linked to the Address
		Assert.assertEquals(2, adr.getInhabitants().size());
		Iterator<?> iter = adr.getInhabitants().iterator();
		Assert.assertSame(martin, iter.next());
		Assert.assertSame(jojo, iter.next());
		// assert the Address being linked to both Persons implicitely
		Assert.assertSame(adr, martin.getAddress());
		Assert.assertSame(adr, jojo.getAddress());
	}

	/**
	 * test creating an association instance between a bean within a document and
	 * one outside a document should be forbidden.
	 */
	@Test(expected = RapidBeansRuntimeException.class)
	public void testAddLinkFromBeanIndocToBeanOutdoc() {

		AddressBook book = new AddressBook();
		new Document("test", book);

		Address adr = new Address();
		Person martin = new Person("\"Martin\" \"Bl�mel\" \"19641014\"");
		book.addAddress(adr);

		adr.addInhabitant(martin);
	}

	/**
	 * test changing the parent of a component within a document.
	 * 
	 * @throws IOException if IO fails
	 */
	@Test
	public void testChangeParentComponent() throws IOException {
		File file = new File("src/test/resources/rapidclubadmin/config/Application.xml");
		Document testdoc = new Document(TypeRapidBean.forName(ConfigApplicationSwing.class.getName()), file);
		ConfigApplication client = (ConfigApplication) testdoc.getRoot();
		ConfigMainWindow mainwin = client.getMainwindow();
		ConfigMenubar menubar = mainwin.getMenubar();
		ConfigSubmenu submenuEdit = null;
		ConfigSubmenu submenuTest = null;
		ConfigMenuItem menuItemTest1 = null;
		ConfigSubmenu submenuTest2 = null;
		Collection<ConfigSubmenu> submenus1 = menubar.getMenus();
		Assert.assertEquals(3, submenus1.size());
		int i = 1;
		for (ConfigSubmenu submenu : submenus1) {
			switch (i) {
			case 1:
				break;
			case 2:
				submenuEdit = submenu;
				break;
			case 3:
				submenuTest = submenu;
				break;
			default:
				Assert.fail("unexpected submenu: " + submenu.getName());
			}
			i++;
		}
		Collection<ConfigMenuEntry> menuentries = submenuTest.getMenuentrys();
		i = 1;
		for (ConfigMenuEntry menuentry : menuentries) {
			switch (i) {
			case 1:
				menuItemTest1 = (ConfigMenuItem) menuentry;
				break;
			case 2:
				submenuTest2 = (ConfigSubmenu) menuentry;
				break;
			default:
				Assert.fail("unexpected submenu: " + menuentry.getName());
			}
			i++;
		}
		Assert.assertEquals("edit", submenuEdit.getName());
		Assert.assertSame(submenuEdit,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigSubmenu", submenuEdit.getIdString()));
		Assert.assertEquals("test", submenuTest.getName());
		Assert.assertSame(submenuTest,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigSubmenu", submenuTest.getIdString()));
		Assert.assertSame(menuItemTest1,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigMenuItem", menuItemTest1.getIdString()));
		Assert.assertEquals("test1", menuItemTest1.getName());
		Assert.assertSame(submenuTest2,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigSubmenu", submenuTest2.getIdString()));
		Assert.assertEquals("test2", submenuTest2.getName());
		Assert.assertEquals(11, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuItem").size());
		Assert.assertEquals(6, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigSubmenu").size());
		Assert.assertEquals(17, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuEntry").size());
		Assert.assertEquals(2, submenuEdit.getMenuentrys().size());
		Assert.assertEquals(2, submenuTest.getMenuentrys().size());
		Assert.assertSame(submenuTest, menuItemTest1.getParentBean());
		Assert.assertSame(submenuTest, submenuTest2.getParentBean());

		// move submenu "test2" from the "test" menu to the "edit" menu
		submenuTest2.setParentBean(submenuEdit);

		Assert.assertSame(menuItemTest1,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigMenuItem", menuItemTest1.getIdString()));
		Assert.assertEquals("test1", menuItemTest1.getName());
		Assert.assertSame(submenuTest2,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigSubmenu", submenuTest2.getIdString()));
		Assert.assertEquals("test2", submenuTest2.getName());
		Assert.assertEquals(11, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuItem").size());
		Assert.assertEquals(6, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigSubmenu").size());
		Assert.assertEquals(17, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuEntry").size());

		Assert.assertEquals(3, submenuEdit.getMenuentrys().size());
		Assert.assertEquals(1, submenuTest.getMenuentrys().size());
		Assert.assertSame(submenuTest, menuItemTest1.getParentBean());
		Assert.assertSame(submenuEdit, submenuTest2.getParentBean());

		File file1 = new File("src/test/resources/rapidclubadmin/config/Test.xml");
		testdoc.setUrl(file1.toURI().toURL());
		testdoc.save();
		Assert.assertTrue(FileHelper.filesEqual(file1,
				new File("src/test/resources/rapidclubadmin/config/ApplicationAfterMoveComposite.xml"), true, true));
		file1.delete();
	}

	/**
	 * test changing the parent of a component within a document. This can not to be
	 * accomplished in two steps 1) remove the component from the current parent 2)
	 * add it to the desired parent because the deleted hierarchy will be destroyed
	 * completely
	 * 
	 * @throws IOException if IO fails
	 */
	@Test
	public void testChangeParentComponentTheWrongWay1() throws IOException {
		File file = new File("src/test/resources/rapidclubadmin/config/Application.xml");
		Document testdoc = new Document(TypeRapidBean.forName(ConfigApplicationSwing.class.getName()), file);
		ConfigApplication client = (ConfigApplication) testdoc.getRoot();
		ConfigMainWindow mainwin = client.getMainwindow();
		ConfigMenubar menubar = mainwin.getMenubar();
		ConfigSubmenu submenuEdit = null;
		ConfigSubmenu submenuTest = null;
		ConfigMenuItem menuItemTest1 = null;
		ConfigSubmenu submenuTest2 = null;
		Collection<ConfigSubmenu> submenus1 = menubar.getMenus();
		Assert.assertEquals(3, submenus1.size());
		int i = 1;
		for (ConfigSubmenu submenu : submenus1) {
			switch (i) {
			case 1:
				break;
			case 2:
				submenuEdit = submenu;
				break;
			case 3:
				submenuTest = submenu;
				break;
			default:
				Assert.fail("unexpected submenu: " + submenu.getName());
			}
			i++;
		}
		Collection<ConfigMenuEntry> menuentries = submenuTest.getMenuentrys();
		i = 1;
		for (ConfigMenuEntry menuentry : menuentries) {
			switch (i) {
			case 1:
				menuItemTest1 = (ConfigMenuItem) menuentry;
				break;
			case 2:
				submenuTest2 = (ConfigSubmenu) menuentry;
				break;
			default:
				Assert.fail("unexpected submenu: " + menuentry.getName());
			}
			i++;
		}
		Assert.assertEquals("edit", submenuEdit.getName());
		Assert.assertSame(submenuEdit,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigSubmenu", submenuEdit.getIdString()));
		Assert.assertEquals("test", submenuTest.getName());
		Assert.assertSame(submenuTest,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigSubmenu", submenuTest.getIdString()));
		Assert.assertSame(menuItemTest1,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigMenuItem", menuItemTest1.getIdString()));
		Assert.assertEquals("test1", menuItemTest1.getName());
		Assert.assertSame(submenuTest2,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigSubmenu", submenuTest2.getIdString()));
		Assert.assertEquals("test2", submenuTest2.getName());
		Assert.assertEquals(11, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuItem").size());
		Assert.assertEquals(6, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigSubmenu").size());
		Assert.assertEquals(17, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuEntry").size());

		Assert.assertEquals(2, submenuEdit.getMenuentrys().size());
		Assert.assertEquals(2, submenuTest.getMenuentrys().size());
		Assert.assertSame(submenuTest, menuItemTest1.getParentBean());
		Assert.assertSame(submenuTest, submenuTest2.getParentBean());

		// move submenu "test2" from the "test" menu to the "edit" menu
		submenuTest.removeMenuentry(submenuTest2);
		Assert.assertEquals(7, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuItem").size());
		Assert.assertEquals(4, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigSubmenu").size());
		Assert.assertEquals(11, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuEntry").size());
		submenuEdit.addMenuentry(submenuTest2);

		Assert.assertSame(menuItemTest1,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigMenuItem", menuItemTest1.getIdString()));
		Assert.assertEquals("test1", menuItemTest1.getName());
		Assert.assertSame(submenuTest2,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigSubmenu", submenuTest2.getIdString()));
		Assert.assertEquals("test2", submenuTest2.getName());
		Assert.assertEquals(7, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuItem").size());
		Assert.assertEquals(5, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigSubmenu").size());
		Assert.assertEquals(12, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuEntry").size());

		Assert.assertEquals(3, submenuEdit.getMenuentrys().size());
		Assert.assertEquals(1, submenuTest.getMenuentrys().size());
		Assert.assertSame(submenuTest, menuItemTest1.getParentBean());
		Assert.assertSame(submenuEdit, submenuTest2.getParentBean());

		File file1 = new File("src/test/resources/rapidclubadmin/config/Test.xml");
		testdoc.setUrl(file1.toURI().toURL());
		testdoc.save();
		Assert.assertTrue(FileHelper.filesEqual(file1,
				new File("src/test/resources/rapidclubadmin/config/ApplicationAfterRemoveAndAddComposite.xml"), true,
				true));
		file1.delete();
	}

	/**
	 * test changing the parent of a component without removing it from the current
	 * parent should fail.
	 */
	@Test(expected = RapidBeansRuntimeException.class)
	public void testChangeParentComponentTheWrongWay2() {
		File file = new File("src/test/resources/rapidclubadmin/config/Application.xml");
		Document testdoc = new Document(TypeRapidBean.forName(ConfigApplication.class.getName()), file);
		ConfigApplication client = (ConfigApplication) testdoc.getRoot();
		ConfigMainWindow mainwin = client.getMainwindow();
		ConfigMenubar menubar = mainwin.getMenubar();
		ConfigSubmenu submenuEdit = null;
		ConfigSubmenu submenuTest = null;
		ConfigSubmenu submenuTest2 = null;
		Collection<ConfigSubmenu> submenus1 = menubar.getMenus();
		Assert.assertEquals(3, submenus1.size());
		int i = 1;
		for (ConfigSubmenu submenu : submenus1) {
			switch (i) {
			case 1:
				break;
			case 2:
				submenuEdit = submenu;
				break;
			case 3:
				submenuTest = submenu;
				break;
			default:
				Assert.fail("unexpected submenu: " + submenu.getName());
			}
			i++;
		}
		Collection<ConfigMenuEntry> menuentries = submenuTest.getMenuentrys();
		i = 1;
		for (ConfigMenuEntry menuentry : menuentries) {
			switch (i) {
			case 1:
				break;
			case 2:
				submenuTest2 = (ConfigSubmenu) menuentry;
				break;
			default:
				Assert.fail("unexpected submenu: " + menuentry.getName());
			}
			i++;
		}

		// try move with addMenuentry() leads to an exception
		submenuEdit.addMenuentry(submenuTest2);
	}

	/**
	 * test creating an association instance between a bean outside a document and
	 * one within a document should be also forbidden.
	 */
	@Test(expected = RapidBeansRuntimeException.class)
	public void testAddLinkFromBeanOutdocToBeanIndoc() {

		AddressBook book = new AddressBook();
		new Document("test", book);

		Address adr = new Address();
		Person martin = new Person("\"Martin\" \"Bl�mel\" \"19641014\"");
		book.addPerson(martin);

		adr.addInhabitant(martin);
	}

	/**
	 * test deleting a simple object.
	 */
	@Test
	public void testDelete() {

		// set up a test document out of file Application.xml
		File file = new File("src/test/resources/rapidclubadmin/config/Application.xml");
		Document testdoc = new Document(TypeRapidBean.forName(ConfigApplication.class.getName()), file);
		ConfigApplication client = (ConfigApplication) testdoc.getRoot();
		ConfigLocale localeconfig = (ConfigLocale) testdoc.findBean(ConfigLocale.class.getName(), "en");
		String id = localeconfig.getIdString();
		Assert.assertEquals("en", localeconfig.getName());
		Assert.assertSame(localeconfig, testdoc.findBean(ConfigLocale.class.getName(), id));
		Assert.assertSame(client, localeconfig.getParentBean());
		Assert.assertSame(testdoc, localeconfig.getContainer());

		localeconfig.delete();
		Assert.assertNull(testdoc.findBean(ConfigLocale.class.getName(), id));
		Assert.assertNull(localeconfig.getParentBean());
		Assert.assertNull(localeconfig.getContainer());
	}

	/**
	 * test deleting a composite object.
	 * 
	 * @throws IOException in case of file IO problems
	 */
	@Test
	public void testDeleteComposite() throws IOException {
		File file = new File("src/test/resources/rapidclubadmin/config/Application.xml");
		Document testdoc = new Document(TypeRapidBean.forName(ConfigApplicationSwing.class.getName()), file);
		ConfigApplication client = (ConfigApplication) testdoc.getRoot();
		ConfigMainWindow mainwin = client.getMainwindow();
		ConfigMenubar menubar = mainwin.getMenubar();
		ConfigSubmenu submenuFile = null;
		ConfigSubmenu submenuEdit = null;
		ConfigSubmenu submenuTest = null;
		ConfigMenuItem menuItemLoad = null;
		ConfigMenuItem menuItemSave = null;
		ConfigMenuItem menuItemQuit = null;
		Collection<ConfigSubmenu> submenus1 = menubar.getMenus();
		int i = 1;
		for (ConfigSubmenu submenu : submenus1) {
			switch (i) {
			case 1:
				submenuFile = submenu;
				break;
			case 2:
				submenuEdit = submenu;
				break;
			case 3:
				submenuTest = submenu;
				break;
			default:
				Assert.fail("unexpected submenu: " + submenu.getName());
			}
			i++;
		}
		Collection<ConfigMenuEntry> menuentries = submenuFile.getMenuentrys();
		i = 1;
		for (ConfigMenuEntry menuentry : menuentries) {
			switch (i) {
			case 1:
				menuItemLoad = (ConfigMenuItem) menuentry;
				break;
			case 2:
				menuItemSave = (ConfigMenuItem) menuentry;
				break;
			case 3:
				menuItemQuit = (ConfigMenuItem) menuentry;
				break;
			default:
				Assert.fail("unexpected submenu: " + menuentry.getName());
			}
			i++;
		}
		Assert.assertSame(menubar,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigMenubar", menubar.getIdString()));
		Assert.assertSame(testdoc, menubar.getContainer());
		Assert.assertSame(mainwin, menubar.getParentBean());
		Assert.assertEquals(3, submenus1.size());
		Assert.assertEquals("file", submenuFile.getName());
		Assert.assertSame(submenuFile,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigSubmenu", submenuFile.getIdString()));
		Assert.assertEquals("edit", submenuEdit.getName());
		Assert.assertSame(submenuEdit,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigSubmenu", submenuEdit.getIdString()));
		Assert.assertEquals("test", submenuTest.getName());
		Assert.assertSame(submenuTest,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigSubmenu", submenuTest.getIdString()));
		Assert.assertEquals("load", menuItemLoad.getName());
		Assert.assertSame(menuItemLoad,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigMenuItem", menuItemLoad.getIdString()));
		Assert.assertEquals("save", menuItemSave.getName());
		Assert.assertSame(menuItemSave,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigMenuItem", menuItemSave.getIdString()));
		Assert.assertEquals("quit", menuItemQuit.getName());
		Assert.assertSame(testdoc, menuItemQuit.getContainer());
		Assert.assertSame(menuItemQuit,
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigMenuItem", menuItemQuit.getIdString()));
		Assert.assertEquals(11, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuItem").size());
		Assert.assertEquals(6, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigSubmenu").size());
		Assert.assertEquals(17, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuEntry").size());

		menubar.delete();

		Assert.assertNull(testdoc.findBeanByQuery("org.rapidbeans.presentation.config.ConfigMenubar"));
		Assert.assertNull(testdoc.findBean("org.rapidbeans.presentation.config.ConfigMenubar", menubar.getIdString()));
		Assert.assertNull(menubar.getParentBean());
		Assert.assertEquals(0, menubar.getMenus().size());
		Assert.assertNull(menubar.getContainer());
		Assert.assertNull(
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigSubmenu", submenuFile.getIdString()));
		Assert.assertNull(submenuFile.getParentBean());
		Assert.assertEquals(0, submenuFile.getMenuentrys().size());
		Assert.assertNull(
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigSubmenu", submenuEdit.getIdString()));
		Assert.assertNull(
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigSubmenu", submenuTest.getIdString()));
		Assert.assertNull(
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigMenuItem", menuItemLoad.getIdString()));
		Assert.assertNull(
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigMenuItem", menuItemSave.getIdString()));
		Assert.assertNull(
				testdoc.findBean("org.rapidbeans.presentation.config.ConfigMenuItem", menuItemQuit.getIdString()));
		Assert.assertNull(menuItemQuit.getContainer());
		Assert.assertEquals(0, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuItem").size());
		Assert.assertEquals(0, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigSubmenu").size());
		Assert.assertEquals(0, testdoc.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuEntry").size());

		File file1 = new File("src/test/resources/rapidclubadmin/config/Test.xml");
		testdoc.setUrl(file1.toURI().toURL());
		testdoc.save();
		Assert.assertTrue(FileHelper.filesEqual(file1,
				new File("src/test/resources/rapidclubadmin/config/ApplicationAfterDelComposite.xml"), true, true));
		file1.delete();
	}

	/**
	 * cloning a composite object.
	 */
	@Test
	public void testCloneLinkedBeanComposite() {

		// set up a test document out of file Application.xml
		File file = new File("src/test/resources/rapidclubadmin/config/Application.xml");
		Document doc = new Document(TypeRapidBean.forName(ConfigApplication.class.getName()), file);
		ConfigMainWindow mainwin = (ConfigMainWindow) doc
				.findBeansByType("org.rapidbeans.presentation.config.ConfigMainWindow").iterator().next();
		Assert.assertNotNull(mainwin);
		Assert.assertSame(mainwin, mainwin.getMenubar().getParentBean());
		ConfigMainWindow cMainwin = (ConfigMainWindow) mainwin.clone();
		Assert.assertEquals(mainwin.getWidth(), cMainwin.getWidth());
		Assert.assertEquals(mainwin.getHeight(), cMainwin.getHeight());
		Assert.assertSame(mainwin.getMenubar(), cMainwin.getMenubar());
		Assert.assertSame(mainwin, mainwin.getMenubar().getParentBean());
	}

	/**
	 * test cloning a bean linked normally.
	 */
	@Test
	public void testCloneLinkedBean() {
		Address adr = createAddress("Germany", "Ismaning", "Camerloher Strasse", 20);
		Person pers = createPerson("Bluemel", "Martin", "19641014");
		AddressBook book = createAddressBook();
		book.addAddress(adr);
		book.addPerson(pers);
		Document doc = new Document("testdoc", book);
		Assert.assertEquals(1, doc.findBeansByQuery("org.rapidbeans.test.codegen.Address").size());
		Assert.assertEquals(1, doc.findBeansByQuery("org.rapidbeans.test.codegen.Person").size());
		adr.addInhabitant(pers);
		Assert.assertEquals(1, adr.getInhabitants().size());
		Assert.assertSame(pers, adr.getInhabitants().iterator().next());
		Assert.assertSame(adr, pers.getAddress());

		Person bClone = (Person) pers.clone();
		Assert.assertEquals(1, doc.findBeansByQuery("org.rapidbeans.test.codegen.Address").size());
		Assert.assertEquals(1, doc.findBeansByQuery("org.rapidbeans.test.codegen.Person").size());
		Assert.assertEquals(1, adr.getInhabitants().size());
		Assert.assertSame(pers, adr.getInhabitants().iterator().next());
		Assert.assertSame(adr, pers.getAddress());
		Assert.assertSame(adr, bClone.getAddress());
	}

	/**
	 * test getting the document path of an arbitrary entity.
	 */
	@Test
	public void testGetPath() {
		Document doc = createTestDocument(false);

		GenericBean berit = (GenericBean) doc.findBeansByQuery("Trainer[firstname = 'Berit']").get(0);
		Assert.assertEquals("test/trainers", doc.getPath(berit, '/'));
		GenericBean deftr = (GenericBean) doc.findBeansByQuery("TrainingDate[defaulttrainer[lastname = 'Dahlheimer']]")
				.get(0);
		Assert.assertEquals("test/trainingdates", doc.getPath(deftr, '/'));
	}

	/**
	 * test the document change flag with inserting a bean.
	 */
	@Test
	public void testGetChangedInsertBean() {
		Document doc = createTestDocument(false);
		Assert.assertFalse(doc.getChanged());

		GenericBean trainer = createTrainer("Meier", "Michael", false, false);
		((PropertyCollection) doc.getRoot().getProperty("trainers")).addLink(trainer);
		Assert.assertTrue(doc.getChanged());
	}

	/**
	 * test the document change flag with removing a bean.
	 */
	@Test
	public void testGetChangedRemoveBean() {
		Document doc = createTestDocument(false);
		Assert.assertFalse(doc.getChanged());

		GenericBean berit = (GenericBean) doc.findBeansByQuery("Trainer[firstname = 'Berit']").get(0);
		((PropertyCollection) doc.getRoot().getProperty("trainers")).removeLink(berit);
		Assert.assertTrue(doc.getChanged());
	}

	/**
	 * test the document change flag with a property change.
	 */
	@Test
	public void testGetChangedChangeProperty() {
		Document doc = createTestDocument(false);
		Assert.assertFalse(doc.getChanged());

		GenericBean berit = (GenericBean) doc.findBeansByQuery("Trainer[firstname = 'Berit']").get(0);
		Assert.assertFalse(((Boolean) berit.getProperty("leader").getValue()).booleanValue());
		// writing the same value does not change the document
		berit.getProperty("leader").setValue(Boolean.valueOf(false));
		Assert.assertFalse(doc.getChanged());
		// writing another value causes the document to be changed
		berit.getProperty("leader").setValue(Boolean.valueOf(true));
		Assert.assertTrue(doc.getChanged());
		// writing back the old value does n o t cause the
		// document to be unchanged
		berit.getProperty("leader").setValue(Boolean.valueOf(false));
		Assert.assertTrue(doc.getChanged());
	}

	/**
	 * common tear down method.
	 */
	public void tearDown() {
		TestHelperTypeLoader.clearBeanTypesGeneric();
	}

	/**
	 * create a test document. This document contains: - 1 BillingPeriod object as
	 * top level object - 3 Trainer objects: Bl�mel, Dahlheimer, Dautovic - 3
	 * TrainingDate objects:
	 * 
	 * @param certificatesMandatory if the certificate of a Trainer are mandatory
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

	// /**
	// * create a test document. This document contains:
	// * - 1 BillingPeriod object as top level object
	// * - 3 Trainer objects: Bl�mel, Dahlheimer, Dautovic
	// * - 3 TrainingDate objects:
	// *
	// * @return the test document
	// */
	// private static Document createTestDocumentWithEmptyColProps() {
	// GenericBean billingPeriod = createBillingPeriod("20060101", "20060331");
	// Document doc = new Document("test", billingPeriod);
	// return doc;
	// }

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
	 * @param lastname  last name
	 * @param firstname first name
	 * @param leader    if the trainer is certified exercise leader
	 * @param mandatory if the certificates property is mandatory
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
		bean.setPropValue("leader", Boolean.valueOf(leader));
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
	 * create a test adress book.
	 * 
	 * @return the test address book
	 */
	private AddressBook createAddressBook() {
		AddressBook book = new AddressBook();
		return book;
	}

	/**
	 * createn a test address.
	 * 
	 * @param country  country
	 * @param city     city
	 * @param street   street
	 * @param streetno street number
	 * @return the address biz bean
	 */
	private Address createAddress(final String country, final String city, final String street, final int streetno) {
		Address adr = new Address();
		return adr;
	}

	/**
	 * create a test person.
	 * 
	 * @param surname     the last name
	 * @param forename    the first name
	 * @param dateofbirth the date of birth as string
	 * 
	 * @return the test person
	 */
	private Person createPerson(final String surname, final String forename, final String dateofbirth) {
		final String[] sa = { surname, forename, dateofbirth };
		Person person = new Person(sa);
		return person;
	}
}
