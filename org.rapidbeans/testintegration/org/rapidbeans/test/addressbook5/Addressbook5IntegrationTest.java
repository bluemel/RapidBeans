/*
 * Rapid Beans Framework: Addressbook5IntegrationTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * October 08, 2008
 */

package org.rapidbeans.test.addressbook5;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JTree;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rapidbeans.core.basic.GenericBean;
import org.rapidbeans.core.basic.PropertyChoice;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.util.PlatformHelper;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.DocumentView;
import org.rapidbeans.presentation.EditorBean;
import org.rapidbeans.presentation.EditorProperty;
import org.rapidbeans.presentation.MenuItem;
import org.rapidbeans.presentation.Submenu;
import org.rapidbeans.presentation.View;
import org.rapidbeans.presentation.swing.DocumentTreeViewSwing;
import org.rapidbeans.presentation.swing.EditorPropertyComboboxSwing;
import org.rapidbeans.presentation.swing.EditorPropertyDateSwing;
import org.rapidbeans.presentation.swing.EditorPropertyList2Swing;
import org.rapidbeans.presentation.swing.EditorPropertyListSwing;
import org.rapidbeans.presentation.swing.EditorPropertySwing;

public class Addressbook5IntegrationTest {

	// Set to false if you want the GUI to be presented
	public static boolean TEST_MODE = true;

	private static File testDocument = null;

	private static TestApplication1 application1 = null;

	private static TestApplication2 application2 = null;

	private static TestApplication3 application3 = null;

	private static TestApplication4 application4 = null;

	private static DocumentView view1 = null;

	private static DocumentTreeViewSwing treeView1 = null;

	private static Document doc1 = null;

	private static JTree tree1 = null;

	private static DocumentView view3 = null;

	private static DocumentTreeViewSwing treeView3 = null;

	// private static Document doc3 = null;

	private static JTree tree3 = null;

	// JUnit 3 stuff
	// private static int testMethodCount = -1;
	//
	// private static int testMethodIndex = 0;
	//
	// private int countTestMethods() {
	// int count = 0;
	// for (Method method : this.getClass().getMethods()) {
	// if (method.getName().startsWith("test")) {
	// count++;
	// }
	// }
	// return count;
	// }

	@BeforeClass
	public static void setUpClass() throws IOException {
		testDocument = new File(
				"../org.rapidbeans/testdata/addressbook5/myaddressbook.xml");
		initApp(application1, "en", Locale.ENGLISH);
		initApp(application2, "en", Locale.ENGLISH);
		initApp(application3, "de", Locale.GERMAN);
		initApp(application4, "fr", Locale.FRENCH);
	}

	@Before
	public void setUp() throws IOException {
		String viewname = "file:";
		switch (PlatformHelper.getOsfamily()) {
		case windows:
			viewname += "/"
					+ testDocument.getCanonicalPath().replace(
							File.separatorChar, '/');
			break;
		default:
			viewname += testDocument.getCanonicalPath();
		}
		viewname += ".standard";
		view1 = (DocumentView) application1.getView(viewname);
		doc1 = view1.getDocument();
		treeView1 = (DocumentTreeViewSwing) view1.getTreeView();
		tree1 = (JTree) treeView1.getTree();
		view3 = (DocumentView) application3.getView(viewname);
		// doc3 = view3.getDocument();
		treeView3 = (DocumentTreeViewSwing) view3.getTreeView();
		tree3 = (JTree) treeView3.getTree();
	}

	private static void initApp(Application app, final String lang,
			final Locale loc) throws IOException {
		if (app == null) {
			System.setProperty("user.language", lang);
			System.setProperty("user.country", "");
			System.setProperty("user.variant", "");
			Locale.setDefault(loc);
			if (app == application1) {
				app = application1 = new TestApplication1(testDocument);
			} else if (app == application2) {
				app = application2 = new TestApplication2(testDocument);
			} else if (app == application3) {
				app = application3 = new TestApplication3(testDocument);
			} else if (app == application4) {
				app = application4 = new TestApplication4(testDocument);
			}
			app.start();
		}
	}

	@After
	public void tearDown() {
		// JUnit 3 stuff
		// testMethodIndex++;
		// if (testMethodIndex < testMethodCount) {
		resetApplication(application1, view1, doc1);
		// }
	}

	@AfterClass
	public static void tearDownClass() {
		ApplicationManager.resetApplication();
	}

	private void resetApplication(final TestApplication app, final View view,
			Document doc) {
		app.setTestMode(true);
		view.close();
		app.setTestMode(TEST_MODE);
		app.removeDocument(doc);
		if (app == application1) {
			doc = doc1 = new Document(testDocument);
		}
		app.addDocument(doc);
		if (app == application1) {
			view1 = app.openDocumentView(doc);
		}
	}

	/**
	 * More than one -docroottype leads to one "New" Submenu with one specific
	 * menu entry per type. Per default the specific menu entry shows the root
	 * element bean's type name.
	 */
	@Test
	public void testNewMenuDocrootclassSingle() {
		Submenu fileMenu = application1.getMainwindow().getMenubar().getMenus()
				.iterator().next();
		MenuItem newMenuItem = (MenuItem) fileMenu.getMenuentrys().iterator()
				.next();
		Assert.assertEquals("New...",
				((JMenuItem) newMenuItem.getWidget()).getText());
	}

	/**
	 * I
	 */
	@Test
	public void testNewMenuDocrootclassMulti() {
		Submenu fileMenu = application2.getMainwindow().getMenubar().getMenus()
				.iterator().next();
		Submenu newMenu = (Submenu) fileMenu.getMenuentrys().iterator().next();
		Assert.assertEquals("New", ((JMenu) newMenu.getWidget()).getText());
		Assert.assertEquals(2, newMenu.getMenuentrys().size());
		Iterator<?> iter = newMenu.getMenuentrys().iterator();
		Assert.assertEquals("Addressbook",
				((JMenuItem) ((MenuItem) iter.next()).getWidget()).getText());
		Assert.assertEquals("Person",
				((JMenuItem) ((MenuItem) iter.next()).getWidget()).getText());
	}

	/**
	 * If you have specified a localized class name as GUI text
	 * bean.org.rapidbeans.test.addressbook5.<lowercased class name> and no
	 * special menu text mainwindow.menubar.file.newsubmenu.<lowercased class
	 * name>.label the localized class name will be taken as menu text
	 */
	@Test
	public void testNewMenuDocrootclassMultiLocalizedClassnames() {
		Submenu fileMenu = application3.getMainwindow().getMenubar().getMenus()
				.iterator().next();
		Submenu newMenu = (Submenu) fileMenu.getMenuentrys().iterator().next();
		Assert.assertEquals("Neu", ((JMenu) newMenu.getWidget()).getText());
		Assert.assertEquals(2, newMenu.getMenuentrys().size());
		Iterator<?> iter = newMenu.getMenuentrys().iterator();
		Assert.assertEquals("Adressbuch",
				((JMenuItem) ((MenuItem) iter.next()).getWidget()).getText());
		Assert.assertEquals("Person",
				((JMenuItem) ((MenuItem) iter.next()).getWidget()).getText());
	}

	/**
	 * If you have specified a special menu GUI text
	 * mainwindow.menubar.file.newsubmenu.<lowercased class name>.label this
	 * text will be taken as menu text
	 */
	@Test
	public void testNewMenuDocrootclassMultiLocalizedGuiTexts() {
		Submenu fileMenu = application4.getMainwindow().getMenubar().getMenus()
				.iterator().next();
		Submenu newMenu = (Submenu) fileMenu.getMenuentrys().iterator().next();
		Assert.assertEquals("Nouveau", ((JMenu) newMenu.getWidget()).getText());
		Assert.assertEquals(2, newMenu.getMenuentrys().size());
		Iterator<?> iter = newMenu.getMenuentrys().iterator();
		Assert.assertEquals("Petit livre d'adresses",
				((JMenuItem) ((MenuItem) iter.next()).getWidget()).getText());
		Assert.assertEquals("Jolie person",
				((JMenuItem) ((MenuItem) iter.next()).getWidget()).getText());
	}

	/**
	 * Create a new person named "Gustav Gans".
	 */
	@Test
	public void testAssociation1to1() throws InterruptedException, IOException {

		Person alf = (Person) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Person[firstname = 'Alfred']");
		Person martin = (Person) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Person[firstname = 'Martin']");
		GenericBean umartin = (GenericBean) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.User[accountname = 'martin']");

		// SETUP: assert user martin is neither linked to alf nor to martin
		Assert.assertEquals("Alfred", alf.getPropValue("firstname"));
		Assert.assertNull(alf.getPropValue("user"));
		Assert.assertEquals("Martin", martin.getPropValue("firstname"));
		Assert.assertNull(martin.getPropValue("user"));
		Assert.assertEquals("martin", umartin.getPropValue("accountname"));
		Assert.assertNull(umartin.getPropValue("person"));

		// SETUP: open editor for beans Person:alf, Person:martin,
		// AdrbookUser:martin
		tree1.expandPath(tree1.getPathForRow(1));
		tree1.expandPath(tree1.getPathForRow(10));

		tree1.setSelectionPath(tree1.getPathForRow(2));
		EditorBean edAnton = treeView1.editBeans();
		Assert.assertSame(alf, edAnton.getBean());
		EditorPropertyComboboxSwing pedAlfUser = (EditorPropertyComboboxSwing) edAnton
				.getPropEditor("user");
		Assert.assertNull(pedAlfUser.getInputFieldValue());

		tree1.setSelectionPath(tree1.getPathForRow(5));
		EditorBean edMartin = treeView1.editBeans();
		EditorPropertyComboboxSwing pedMartinUser = (EditorPropertyComboboxSwing) edMartin
				.getPropEditor("user");
		Assert.assertSame(martin, edMartin.getBean());

		tree1.setSelectionPath(tree1.getPathForRow(15));
		EditorBean edUmartin = treeView1.editBeans();
		EditorPropertyComboboxSwing pedUmartinPerson = (EditorPropertyComboboxSwing) edUmartin
				.getPropEditor("person");
		Assert.assertSame(umartin, edUmartin.getBean());

		// assert all 3 editors have no user / person selected
		Assert.assertNull(pedMartinUser.getInputFieldValue());
		Assert.assertNull(pedUmartinPerson.getInputFieldValue());
		Assert.assertNull(pedMartinUser.getInputFieldValue());
		JComboBox cbUmartinPersons = (JComboBox) pedUmartinPerson.getWidget();
		Assert.assertEquals(8, cbUmartinPersons.getItemCount());

		// TEST: associate AdrbookUser:martin with Person:alf
		cbUmartinPersons.setSelectedIndex(findIndex(cbUmartinPersons,
				"firstname", "Alfred"));

		Assert.assertSame(alf, ((Collection<?>) umartin.getPropValue("person"))
				.iterator().next());
		Assert.assertSame(umartin, ((Collection<?>) alf.getPropValue("user"))
				.iterator().next());
		Assert.assertNull(martin.getPropValue("user"));

		Assert.assertSame(alf, pedUmartinPerson.getInputFieldValue());
		Assert.assertSame(umartin, pedAlfUser.getInputFieldValue());
		Assert.assertNull(pedMartinUser.getInputFieldValue());

		// TEST: associate AdrbookUser:martin with Person:martin
		cbUmartinPersons.setSelectedIndex(findIndex(cbUmartinPersons,
				"firstname", "Martin"));

		Assert.assertSame(martin, ((Collection<?>) umartin
				.getPropValue("person")).iterator().next());
		Assert.assertSame(umartin,
				((Collection<?>) martin.getPropValue("user")).iterator().next());
		Assert.assertEquals(0,
				((Collection<?>) alf.getPropValue("user")).size());

		Assert.assertSame(martin, pedUmartinPerson.getInputFieldValue());
		Assert.assertSame(umartin, pedMartinUser.getInputFieldValue());
		Assert.assertNull(pedAlfUser.getInputFieldValue());

		// TEST: associate AdrbookUser:martin with no person
		cbUmartinPersons.setSelectedIndex(0);

		Assert.assertEquals(0,
				((Collection<?>) martin.getPropValue("user")).size());
		Assert.assertEquals(0,
				((Collection<?>) alf.getPropValue("user")).size());
		Assert.assertNull(umartin.getPropValue("person"));
	}

	public static String getBeanName(final RapidBean bean) {
		if (bean == null) {
			return "null";
		}
		if (bean.getType().getNameShort().equals("Person")) {
			if (bean.getPropValue("firstname") != null) {
				return (String) bean.getPropValue("firstname");
			}
		}
		return bean.getIdString();
	}

	private int findIndex(final JComboBox cb, final String attrname,
			final String s) {
		final int count = cb.getItemCount();
		for (int i = 0; i < count; i++) {
			final RapidBean bean = (RapidBean) cb.getItemAt(i);
			if (bean != null && bean.getProperty(attrname) != null
					&& ((String) bean.getPropValue(attrname)).equals(s)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Create a new group "gr" and afterwards a second group "gr1".
	 * 
	 * While typing the second group we are temporarily in a state where the
	 * second group's name is equal to the one we have already created formerly.
	 * At this stage the editor changes to "error presentation mode". However
	 * when we continue typing the mode should be normal again.
	 */
	@Test
	public void testCreateGroupTemporarilyDuplicate()
			throws InterruptedException, IOException {
		Assert.assertEquals(2,
				doc1.findBeansByType("org.rapidbeans.test.addressbook5.Group")
						.size());
		GenericBean adrbook = (GenericBean) doc1.getRoot();
		Assert.assertNotNull(adrbook.getProperty("groups").getValue());
		tree1.setSelectionPath(tree1.getPathForRow(2));
		EditorBean ed = treeView1.createBean();

		// create the third group "testgroup"
		((JTextField) ed.getPropEditor("name").getWidget())
				.setText("testgroup");
		ed.getPropEditor("name").fireInputFieldChanged();
		Assert.assertEquals("Apply",
				((JButton) ed.getButtonWidgets().get("apply")).getText());
		ed.handleActionApply();
		Assert.assertEquals(3, ((Collection<?>) adrbook.getProperty("groups")
				.getValue()).size());
		Assert.assertEquals(3,
				doc1.findBeansByType("org.rapidbeans.test.addressbook5.Group")
						.size());
		Assert.assertEquals(
				1,
				doc1.findBeansByQuery(
						"org.rapidbeans.test.addressbook5.Group[name = 'testgroup']")
						.size());
		Assert.assertTrue(doc1.getChanged());
		Assert.assertEquals("*Addressbook",
				((JInternalFrame) view1.getWidget()).getTitle());

		// try to create a fourth group "testgroup"
		((JTextField) ed.getPropEditor("name").getWidget())
				.setText("testgroup");
		ed.getPropEditor("name").fireInputFieldChanged();
		Assert.assertEquals("Check",
				((JButton) ed.getButtonWidgets().get("apply")).getText());

		((JTextField) ed.getPropEditor("name").getWidget())
				.setText("testgroup1");
		ed.getPropEditor("name").fireInputFieldChanged();
		Assert.assertEquals("Apply",
				((JButton) ed.getButtonWidgets().get("apply")).getText());

		ed.handleActionApply();
		Assert.assertEquals(4, ((Collection<?>) adrbook.getProperty("groups")
				.getValue()).size());
		Assert.assertEquals(4,
				doc1.findBeansByType("org.rapidbeans.test.addressbook5.Group")
						.size());
		Assert.assertEquals(
				1,
				doc1.findBeansByQuery(
						"org.rapidbeans.test.addressbook5.Group[name = 'testgroup1']")
						.size());
		Assert.assertTrue(doc1.getChanged());
		Assert.assertEquals("*Addressbook",
				((JInternalFrame) view1.getWidget()).getTitle());

	}

	@Test
	public void testDeletePerson4() throws InterruptedException, IOException {
		tree1.expandPath(tree1.getPathForRow(1));
		tree1.setSelectionPath(tree1.getPathForRow(4));
		RapidBean bean = treeView1.getSelectedBeans()[0];
		Assert.assertNotNull(doc1.findBean(bean.getType().getName(),
				bean.getIdString()));
		bean.delete();
		Assert.assertNull(doc1.findBean(bean.getType().getName(),
				bean.getIdString()));
		Assert.assertEquals("*Addressbook",
				((JInternalFrame) view1.getWidget()).getTitle());
	}

	@Test
	public void testDeletePerson5() throws InterruptedException, IOException {
		tree1.expandPath(tree1.getPathForRow(1));
		tree1.setSelectionPath(tree1.getPathForRow(5));
		RapidBean bean = treeView1.getSelectedBeans()[0];
		Assert.assertNotNull(doc1.findBean(bean.getType().getName(),
				bean.getIdString()));
		bean.delete();
		Assert.assertNull(doc1.findBean(bean.getType().getName(),
				bean.getIdString()));
	}

	@Test
	public void testDeletePersonFromGroupEditorsRemoveLinkOpenNo() {
		GenericBean adrbook = (GenericBean) doc1.getRoot();
		PropertyCollection adrbookPersons = (PropertyCollection) adrbook
				.getProperty("persons");
		Person alfred = (Person) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Person[lastname='Abalbert']");
		Assert.assertEquals("Alfred", alfred.getFirstname());
		Person martin = (Person) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Person[lastname='Bluemel']");
		Assert.assertEquals("Martin", martin.getFirstname());
		GenericBean group1 = (GenericBean) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Group[name='group1']");
		GenericBean group2 = (GenericBean) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Group[name='group2']");

		try {
			Assert.assertEquals(2,
					((List<?>) group1.getPropValue("persons")).size());
			Assert.assertSame(alfred,
					((List<?>) group1.getPropValue("persons")).get(0));
			Assert.assertSame(martin,
					((List<?>) group1.getPropValue("persons")).get(1));
			Assert.assertEquals(2,
					((List<?>) group2.getPropValue("persons")).size());
			Assert.assertSame(alfred,
					((List<?>) group2.getPropValue("persons")).get(0));
			Assert.assertSame(martin,
					((List<?>) group2.getPropValue("persons")).get(1));
			Assert.assertEquals(2, alfred.getGroups().size());
			Assert.assertSame(group1, alfred.getGroups().get(0));
			Assert.assertSame(group2, alfred.getGroups().get(1));
			Assert.assertEquals(2, martin.getGroups().size());
			Assert.assertSame(group1, martin.getGroups().get(0));
			Assert.assertSame(group2, martin.getGroups().get(1));
			Assert.assertEquals(
					7,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Person").size());
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(alfred));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(martin));
			Assert.assertEquals(
					2,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Group").size());
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group1));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group2));

			adrbookPersons.removeLink(alfred);

			Assert.assertEquals(1,
					((List<?>) group1.getPropValue("persons")).size());
			Assert.assertSame(martin,
					((List<?>) group1.getPropValue("persons")).get(0));
			Assert.assertEquals(1,
					((List<?>) group2.getPropValue("persons")).size());
			Assert.assertSame(martin,
					((List<?>) group2.getPropValue("persons")).get(0));
			Assert.assertEquals(0, alfred.getGroups().size());
			Assert.assertEquals(2, martin.getGroups().size());
			Assert.assertSame(group1, martin.getGroups().get(0));
			Assert.assertSame(group2, martin.getGroups().get(1));
			Assert.assertEquals(
					6,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Person").size());
			Assert.assertFalse(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(alfred));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(martin));
			Assert.assertEquals(
					2,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Group").size());
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group1));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group2));
		} finally {
			if (!adrbookPersons.getValue().contains(alfred)) {
				adrbookPersons.removeLink(martin);
				adrbookPersons.addLink(alfred);
				adrbookPersons.addLink(martin);
				PropertyCollection alfredGroups = (PropertyCollection) alfred
						.getProperty("groups");
				alfredGroups.addLink(group1);
				alfredGroups.addLink(group2);
				PropertyCollection martinGroups = (PropertyCollection) martin
						.getProperty("groups");
				martinGroups.addLink(group1);
				martinGroups.addLink(group2);

				Assert.assertEquals(2,
						((List<?>) group1.getPropValue("persons")).size());
				Assert.assertSame(alfred,
						((List<?>) group1.getPropValue("persons")).get(0));
				Assert.assertSame(martin,
						((List<?>) group1.getPropValue("persons")).get(1));
				Assert.assertEquals(2,
						((List<?>) group2.getPropValue("persons")).size());
				Assert.assertSame(alfred,
						((List<?>) group2.getPropValue("persons")).get(0));
				Assert.assertSame(martin,
						((List<?>) group2.getPropValue("persons")).get(1));
				Assert.assertEquals(2, alfred.getGroups().size());
				Assert.assertSame(group1, alfred.getGroups().get(0));
				Assert.assertSame(group2, alfred.getGroups().get(1));
				Assert.assertEquals(2, martin.getGroups().size());
				Assert.assertSame(group1, martin.getGroups().get(0));
				Assert.assertSame(group2, martin.getGroups().get(1));
				Assert.assertEquals(
						7,
						doc1.findBeansByType(
								"org.rapidbeans.test.addressbook5.Person")
								.size());
				Assert.assertTrue(doc1.findBeansByType(
						"org.rapidbeans.test.addressbook5.Person").contains(
						alfred));
				Assert.assertTrue(doc1.findBeansByType(
						"org.rapidbeans.test.addressbook5.Person").contains(
						martin));
				Assert.assertEquals(
						2,
						doc1.findBeansByType(
								"org.rapidbeans.test.addressbook5.Group")
								.size());
				Assert.assertTrue(doc1.findBeansByType(
						"org.rapidbeans.test.addressbook5.Group").contains(
						group1));
				Assert.assertTrue(doc1.findBeansByType(
						"org.rapidbeans.test.addressbook5.Group").contains(
						group2));
			}
		}
	}

	@Test
	public void testDeletePersonFromGroupEditorsRemoveLinkOpenPerson() {
		GenericBean adrbook = (GenericBean) doc1.getRoot();
		PropertyCollection adrbookPersons = (PropertyCollection) adrbook
				.getProperty("persons");
		Person alfred = (Person) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Person[lastname='Abalbert']");
		Assert.assertEquals("Alfred", alfred.getFirstname());
		Person martin = (Person) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Person[lastname='Bluemel']");
		Assert.assertEquals("Martin", martin.getFirstname());
		GenericBean group1 = (GenericBean) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Group[name='group1']");
		GenericBean group2 = (GenericBean) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Group[name='group2']");

		try {
			Assert.assertEquals(2,
					((List<?>) group1.getPropValue("persons")).size());
			Assert.assertSame(alfred,
					((List<?>) group1.getPropValue("persons")).get(0));
			Assert.assertSame(martin,
					((List<?>) group1.getPropValue("persons")).get(1));
			Assert.assertEquals(2,
					((List<?>) group2.getPropValue("persons")).size());
			Assert.assertSame(alfred,
					((List<?>) group2.getPropValue("persons")).get(0));
			Assert.assertSame(martin,
					((List<?>) group2.getPropValue("persons")).get(1));
			Assert.assertEquals(2, alfred.getGroups().size());
			Assert.assertSame(group1, alfred.getGroups().get(0));
			Assert.assertSame(group2, alfred.getGroups().get(1));
			Assert.assertEquals(2, martin.getGroups().size());
			Assert.assertSame(group1, martin.getGroups().get(0));
			Assert.assertSame(group2, martin.getGroups().get(1));
			Assert.assertEquals(
					7,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Person").size());
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(alfred));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(martin));
			Assert.assertEquals(
					2,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Group").size());
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group1));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group2));
			tree1.expandPath(tree1.getPathForRow(1));
			tree1.setSelectionPath(tree1.getPathForRow(2));
			RapidBean bean = treeView1.getSelectedBeans()[0];
			Assert.assertNotNull(doc1.findBean(bean.getType().getName(),
					bean.getIdString()));
			treeView1.editBeans();

			adrbookPersons.removeLink(alfred);

			Assert.assertEquals(1,
					((List<?>) group1.getPropValue("persons")).size());
			Assert.assertSame(martin,
					((List<?>) group1.getPropValue("persons")).get(0));
			Assert.assertEquals(1,
					((List<?>) group2.getPropValue("persons")).size());
			Assert.assertSame(martin,
					((List<?>) group2.getPropValue("persons")).get(0));
			Assert.assertEquals(0, alfred.getGroups().size());
			Assert.assertEquals(2, martin.getGroups().size());
			Assert.assertSame(group1, martin.getGroups().get(0));
			Assert.assertSame(group2, martin.getGroups().get(1));
			Assert.assertEquals(
					6,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Person").size());
			Assert.assertFalse(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(alfred));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(martin));
			Assert.assertEquals(
					2,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Group").size());
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group1));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group2));
		} finally {
			if (!adrbookPersons.getValue().contains(alfred)) {
				adrbookPersons.removeLink(martin);
				adrbookPersons.addLink(alfred);
				adrbookPersons.addLink(martin);
				PropertyCollection alfredGroups = (PropertyCollection) alfred
						.getProperty("groups");
				alfredGroups.addLink(group1);
				alfredGroups.addLink(group2);
				PropertyCollection martinGroups = (PropertyCollection) martin
						.getProperty("groups");
				martinGroups.addLink(group1);
				martinGroups.addLink(group2);

				Assert.assertEquals(2,
						((List<?>) group1.getPropValue("persons")).size());
				Assert.assertSame(alfred,
						((List<?>) group1.getPropValue("persons")).get(0));
				Assert.assertSame(martin,
						((List<?>) group1.getPropValue("persons")).get(1));
				Assert.assertEquals(2,
						((List<?>) group2.getPropValue("persons")).size());
				Assert.assertSame(alfred,
						((List<?>) group2.getPropValue("persons")).get(0));
				Assert.assertSame(martin,
						((List<?>) group2.getPropValue("persons")).get(1));
				Assert.assertEquals(2, alfred.getGroups().size());
				Assert.assertSame(group1, alfred.getGroups().get(0));
				Assert.assertSame(group2, alfred.getGroups().get(1));
				Assert.assertEquals(2, martin.getGroups().size());
				Assert.assertSame(group1, martin.getGroups().get(0));
				Assert.assertSame(group2, martin.getGroups().get(1));
				Assert.assertEquals(
						7,
						doc1.findBeansByType(
								"org.rapidbeans.test.addressbook5.Person")
								.size());
				Assert.assertTrue(doc1.findBeansByType(
						"org.rapidbeans.test.addressbook5.Person").contains(
						alfred));
				Assert.assertTrue(doc1.findBeansByType(
						"org.rapidbeans.test.addressbook5.Person").contains(
						martin));
				Assert.assertEquals(
						2,
						doc1.findBeansByType(
								"org.rapidbeans.test.addressbook5.Group")
								.size());
				Assert.assertTrue(doc1.findBeansByType(
						"org.rapidbeans.test.addressbook5.Group").contains(
						group1));
				Assert.assertTrue(doc1.findBeansByType(
						"org.rapidbeans.test.addressbook5.Group").contains(
						group2));
			}
		}
	}

	@Test
	public void testDeletePersonFromGroupEditorsOpenNo() {
		GenericBean adrbook = (GenericBean) doc1.getRoot();
		PropertyCollection adrbookPersons = (PropertyCollection) adrbook
				.getProperty("persons");
		Person alfred = (Person) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Person[lastname='Abalbert']");
		Assert.assertEquals("Alfred", alfred.getFirstname());
		Person martin = (Person) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Person[lastname='Bluemel']");
		Assert.assertEquals("Martin", martin.getFirstname());
		GenericBean group1 = (GenericBean) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Group[name='group1']");
		GenericBean group2 = (GenericBean) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Group[name='group2']");

		try {
			Assert.assertEquals(2,
					((List<?>) group1.getPropValue("persons")).size());
			Assert.assertSame(alfred,
					((List<?>) group1.getPropValue("persons")).get(0));
			Assert.assertSame(martin,
					((List<?>) group1.getPropValue("persons")).get(1));
			Assert.assertEquals(2,
					((List<?>) group2.getPropValue("persons")).size());
			Assert.assertSame(alfred,
					((List<?>) group2.getPropValue("persons")).get(0));
			Assert.assertSame(martin,
					((List<?>) group2.getPropValue("persons")).get(1));
			Assert.assertEquals(2, alfred.getGroups().size());
			Assert.assertSame(group1, alfred.getGroups().get(0));
			Assert.assertSame(group2, alfred.getGroups().get(1));
			Assert.assertEquals(2, martin.getGroups().size());
			Assert.assertSame(group1, martin.getGroups().get(0));
			Assert.assertSame(group2, martin.getGroups().get(1));
			Assert.assertEquals(
					7,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Person").size());
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(alfred));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(martin));
			Assert.assertEquals(
					2,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Group").size());
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group1));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group2));
			tree1.expandPath(tree1.getPathForRow(1));
			tree1.setSelectionPath(tree1.getPathForRow(2));

			treeView1.getSelectedBeans()[0].delete();

			Assert.assertEquals(1,
					((List<?>) group1.getPropValue("persons")).size());
			Assert.assertSame(martin,
					((List<?>) group1.getPropValue("persons")).get(0));
			Assert.assertEquals(1,
					((List<?>) group2.getPropValue("persons")).size());
			Assert.assertSame(martin,
					((List<?>) group2.getPropValue("persons")).get(0));
			Assert.assertEquals(0, alfred.getGroups().size());
			Assert.assertEquals(2, martin.getGroups().size());
			Assert.assertSame(group1, martin.getGroups().get(0));
			Assert.assertSame(group2, martin.getGroups().get(1));
			Assert.assertEquals(
					6,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Person").size());
			Assert.assertFalse(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(alfred));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(martin));
			Assert.assertEquals(
					2,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Group").size());
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group1));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group2));
		} finally {
			if (!adrbookPersons.getValue().contains(alfred)) {
				adrbookPersons.removeLink(martin);
				adrbookPersons.addLink(alfred);
				adrbookPersons.addLink(martin);
				PropertyCollection alfredGroups = (PropertyCollection) alfred
						.getProperty("groups");
				alfredGroups.addLink(group1);
				alfredGroups.addLink(group2);
				PropertyCollection martinGroups = (PropertyCollection) martin
						.getProperty("groups");
				martinGroups.addLink(group1);
				martinGroups.addLink(group2);

				Assert.assertEquals(2,
						((List<?>) group1.getPropValue("persons")).size());
				Assert.assertSame(alfred,
						((List<?>) group1.getPropValue("persons")).get(0));
				Assert.assertSame(martin,
						((List<?>) group1.getPropValue("persons")).get(1));
				Assert.assertEquals(2,
						((List<?>) group2.getPropValue("persons")).size());
				Assert.assertSame(alfred,
						((List<?>) group2.getPropValue("persons")).get(0));
				Assert.assertSame(martin,
						((List<?>) group2.getPropValue("persons")).get(1));
				Assert.assertEquals(2, alfred.getGroups().size());
				Assert.assertSame(group1, alfred.getGroups().get(0));
				Assert.assertSame(group2, alfred.getGroups().get(1));
				Assert.assertEquals(2, martin.getGroups().size());
				Assert.assertSame(group1, martin.getGroups().get(0));
				Assert.assertSame(group2, martin.getGroups().get(1));
				Assert.assertEquals(
						7,
						doc1.findBeansByType(
								"org.rapidbeans.test.addressbook5.Person")
								.size());
				Assert.assertTrue(doc1.findBeansByType(
						"org.rapidbeans.test.addressbook5.Person").contains(
						alfred));
				Assert.assertTrue(doc1.findBeansByType(
						"org.rapidbeans.test.addressbook5.Person").contains(
						martin));
				Assert.assertEquals(
						2,
						doc1.findBeansByType(
								"org.rapidbeans.test.addressbook5.Group")
								.size());
				Assert.assertTrue(doc1.findBeansByType(
						"org.rapidbeans.test.addressbook5.Group").contains(
						group1));
				Assert.assertTrue(doc1.findBeansByType(
						"org.rapidbeans.test.addressbook5.Group").contains(
						group2));
			}
		}
	}

	@Test
	public void testDeletePersonFromGroupEditorsOpenPerson() {
		GenericBean adrbook = (GenericBean) doc1.getRoot();
		PropertyCollection adrbookPersons = (PropertyCollection) adrbook
				.getProperty("persons");
		Person alfred = (Person) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Person[lastname='Abalbert']");
		Assert.assertEquals("Alfred", alfred.getFirstname());
		Person martin = (Person) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Person[lastname='Bluemel']");
		Assert.assertEquals("Martin", martin.getFirstname());
		GenericBean group1 = (GenericBean) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Group[name='group1']");
		GenericBean group2 = (GenericBean) doc1
				.findBeanByQuery("org.rapidbeans.test.addressbook5.Group[name='group2']");
		RuntimeException re = null;

		try {
			Assert.assertEquals(2,
					((List<?>) group1.getPropValue("persons")).size());
			Assert.assertSame(alfred,
					((List<?>) group1.getPropValue("persons")).get(0));
			Assert.assertSame(martin,
					((List<?>) group1.getPropValue("persons")).get(1));
			Assert.assertEquals(2,
					((List<?>) group2.getPropValue("persons")).size());
			Assert.assertSame(alfred,
					((List<?>) group2.getPropValue("persons")).get(0));
			Assert.assertSame(martin,
					((List<?>) group2.getPropValue("persons")).get(1));
			Assert.assertEquals(2, alfred.getGroups().size());
			Assert.assertSame(group1, alfred.getGroups().get(0));
			Assert.assertSame(group2, alfred.getGroups().get(1));
			Assert.assertEquals(2, martin.getGroups().size());
			Assert.assertSame(group1, martin.getGroups().get(0));
			Assert.assertSame(group2, martin.getGroups().get(1));
			Assert.assertEquals(
					7,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Person").size());
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(alfred));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(martin));
			Assert.assertEquals(
					2,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Group").size());
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group1));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group2));
			tree1.expandPath(tree1.getPathForRow(1));
			tree1.setSelectionPath(tree1.getPathForRow(2));
			RapidBean bean = treeView1.getSelectedBeans()[0];
			Assert.assertNotNull(doc1.findBean(bean.getType().getName(),
					bean.getIdString()));
			treeView1.editBeans();

			treeView1.getSelectedBeans()[0].delete();

			Assert.assertEquals(1,
					((List<?>) group1.getPropValue("persons")).size());
			Assert.assertSame(martin,
					((List<?>) group1.getPropValue("persons")).get(0));
			Assert.assertEquals(1,
					((List<?>) group2.getPropValue("persons")).size());
			Assert.assertSame(martin,
					((List<?>) group2.getPropValue("persons")).get(0));
			Assert.assertEquals(0, alfred.getGroups().size());
			Assert.assertEquals(2, martin.getGroups().size());
			Assert.assertSame(group1, martin.getGroups().get(0));
			Assert.assertSame(group2, martin.getGroups().get(1));
			Assert.assertEquals(
					6,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Person").size());
			Assert.assertFalse(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(alfred));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Person").contains(martin));
			Assert.assertEquals(
					2,
					doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Group").size());
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group1));
			Assert.assertTrue(doc1.findBeansByType(
					"org.rapidbeans.test.addressbook5.Group").contains(group2));
		} catch (RuntimeException re1) {
			re = re1;
		} finally {
			try {
				if (!adrbookPersons.getValue().contains(alfred)) {
					adrbookPersons.removeLink(martin);
					adrbookPersons.addLink(alfred);
					adrbookPersons.addLink(martin);
					PropertyCollection alfredGroups = (PropertyCollection) alfred
							.getProperty("groups");
					alfredGroups.addLink(group1);
					alfredGroups.addLink(group2);
					PropertyCollection martinGroups = (PropertyCollection) martin
							.getProperty("groups");
					martinGroups.addLink(group1);
					martinGroups.addLink(group2);
					Assert.assertEquals(2,
							((List<?>) group1.getPropValue("persons")).size());
					Assert.assertSame(alfred,
							((List<?>) group1.getPropValue("persons")).get(0));
					Assert.assertSame(martin,
							((List<?>) group1.getPropValue("persons")).get(1));
					Assert.assertEquals(2,
							((List<?>) group2.getPropValue("persons")).size());
					Assert.assertSame(alfred,
							((List<?>) group2.getPropValue("persons")).get(0));
					Assert.assertSame(martin,
							((List<?>) group2.getPropValue("persons")).get(1));
					Assert.assertEquals(2, alfred.getGroups().size());
					Assert.assertSame(group1, alfred.getGroups().get(0));
					Assert.assertSame(group2, alfred.getGroups().get(1));
					Assert.assertEquals(2, martin.getGroups().size());
					Assert.assertSame(group1, martin.getGroups().get(0));
					Assert.assertSame(group2, martin.getGroups().get(1));
					Assert.assertEquals(
							7,
							doc1.findBeansByType(
									"org.rapidbeans.test.addressbook5.Person")
									.size());
					Assert.assertTrue(doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Person")
							.contains(alfred));
					Assert.assertTrue(doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Person")
							.contains(martin));
					Assert.assertEquals(
							2,
							doc1.findBeansByType(
									"org.rapidbeans.test.addressbook5.Group")
									.size());
					Assert.assertTrue(doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Group").contains(
							group1));
					Assert.assertTrue(doc1.findBeansByType(
							"org.rapidbeans.test.addressbook5.Group").contains(
							group2));
				}
			} catch (RuntimeException re2) {
				if (re == null) {
					throw re2;
				} else {
					System.err.println("WARNIG: teardown also failed:");
					re2.printStackTrace();
					throw re;
				}
			}
		}
	}

	@Test
	public void testUpdatePersonChangeLastnameProgrammatically()
			throws InterruptedException, IOException {

		tree1.expandPath(tree1.getPathForRow(1));

		RapidBean b1 = (RapidBean) tree1.getPathForRow(2)
				.getLastPathComponent();
		Assert.assertEquals("Abalbert", b1.getProperty("lastname").getValue());
		RapidBean b2 = (RapidBean) tree1.getPathForRow(3)
				.getLastPathComponent();
		Assert.assertEquals("Bluemel", b2.getProperty("lastname").getValue());
		RapidBean b3 = (RapidBean) tree1.getPathForRow(4)
				.getLastPathComponent();
		Assert.assertEquals("Citrone", b3.getProperty("lastname").getValue());
		RapidBean b4 = (RapidBean) tree1.getPathForRow(5)
				.getLastPathComponent();
		Assert.assertEquals("Doof", b4.getProperty("lastname").getValue());
		RapidBean b5 = (RapidBean) tree1.getPathForRow(6)
				.getLastPathComponent();
		Assert.assertEquals("Esel", b5.getProperty("lastname").getValue());
		RapidBean b6 = (RapidBean) tree1.getPathForRow(7)
				.getLastPathComponent();
		Assert.assertEquals("Fendrich", b6.getProperty("lastname").getValue());
		RapidBean b7 = (RapidBean) tree1.getPathForRow(8)
				.getLastPathComponent();
		Assert.assertEquals("Xenophon", b7.getProperty("lastname").getValue());
		Assert.assertNull(tree1.getPathForRow(11));

		tree1.setSelectionPath(tree1.getPathForRow(5));
		Person bean = (Person) treeView1.getSelectedBeans()[0];
		Assert.assertEquals("Doof", bean.getProperty("lastname").getValue());

		try {
			bean.setPropValue("lastname", "Xyz");

			// after the change the persons are automatically re sorted
			// in the tree1 view1
			b1 = (RapidBean) tree1.getPathForRow(2).getLastPathComponent();
			Assert.assertEquals("Abalbert", b1.getProperty("lastname")
					.getValue());
			b2 = (RapidBean) tree1.getPathForRow(3).getLastPathComponent();
			Assert.assertEquals("Bluemel", b2.getProperty("lastname")
					.getValue());
			b3 = (RapidBean) tree1.getPathForRow(4).getLastPathComponent();
			Assert.assertEquals("Citrone", b3.getProperty("lastname")
					.getValue());
			b4 = (RapidBean) tree1.getPathForRow(5).getLastPathComponent();
			Assert.assertEquals("Esel", b4.getProperty("lastname").getValue());
			b5 = (RapidBean) tree1.getPathForRow(6).getLastPathComponent();
			Assert.assertEquals("Fendrich", b5.getProperty("lastname")
					.getValue());
			b6 = (RapidBean) tree1.getPathForRow(7).getLastPathComponent();
			Assert.assertEquals("Xenophon", b6.getProperty("lastname")
					.getValue());
			b7 = (RapidBean) tree1.getPathForRow(8).getLastPathComponent();
			Assert.assertEquals("Xyz", b7.getProperty("lastname").getValue());
			Assert.assertNull(tree1.getPathForRow(11));
		} finally {
			bean.setPropValue("lastname", "Doof");
		}
	}

	@Test
	public void testUpdatePersonChangeLastnameViaEditor()
			throws InterruptedException, IOException {
		tree1.expandPath(tree1.getPathForRow(1));

		RapidBean b1 = (RapidBean) tree1.getPathForRow(2)
				.getLastPathComponent();
		Assert.assertEquals("Abalbert", b1.getProperty("lastname").getValue());
		RapidBean b2 = (RapidBean) tree1.getPathForRow(3)
				.getLastPathComponent();
		Assert.assertEquals("Bluemel", b2.getProperty("lastname").getValue());
		RapidBean b3 = (RapidBean) tree1.getPathForRow(4)
				.getLastPathComponent();
		Assert.assertEquals("Citrone", b3.getProperty("lastname").getValue());
		RapidBean b4 = (RapidBean) tree1.getPathForRow(5)
				.getLastPathComponent();
		Assert.assertEquals("Doof", b4.getProperty("lastname").getValue());
		RapidBean b5 = (RapidBean) tree1.getPathForRow(6)
				.getLastPathComponent();
		Assert.assertEquals("Esel", b5.getProperty("lastname").getValue());
		RapidBean b6 = (RapidBean) tree1.getPathForRow(7)
				.getLastPathComponent();
		Assert.assertEquals("Fendrich", b6.getProperty("lastname").getValue());
		RapidBean b7 = (RapidBean) tree1.getPathForRow(8)
				.getLastPathComponent();
		Assert.assertEquals("Xenophon", b7.getProperty("lastname").getValue());
		Assert.assertNull(tree1.getPathForRow(11));

		tree1.setSelectionPath(tree1.getPathForRow(5));
		EditorBean ed = treeView1.editBeans();
		EditorProperty ped = ed.getPropEditor("lastname");
		JTextField tf = (JTextField) ped.getWidget();
		Assert.assertEquals("Doof", tf.getText());

		try {
			tf.setText("Xyz");
			ped.fireInputFieldChanged();
			ed.handleActionOk();

			b1 = (RapidBean) tree1.getPathForRow(2).getLastPathComponent();
			Assert.assertEquals("Abalbert", b1.getProperty("lastname")
					.getValue());
			b2 = (RapidBean) tree1.getPathForRow(3).getLastPathComponent();
			Assert.assertEquals("Bluemel", b2.getProperty("lastname")
					.getValue());
			b3 = (RapidBean) tree1.getPathForRow(4).getLastPathComponent();
			Assert.assertEquals("Citrone", b3.getProperty("lastname")
					.getValue());
			b4 = (RapidBean) tree1.getPathForRow(5).getLastPathComponent();
			Assert.assertEquals("Esel", b4.getProperty("lastname").getValue());
			b5 = (RapidBean) tree1.getPathForRow(6).getLastPathComponent();
			Assert.assertEquals("Fendrich", b5.getProperty("lastname")
					.getValue());
			b6 = (RapidBean) tree1.getPathForRow(7).getLastPathComponent();
			Assert.assertEquals("Xenophon", b6.getProperty("lastname")
					.getValue());
			b7 = (RapidBean) tree1.getPathForRow(8).getLastPathComponent();
			Assert.assertEquals("Xyz", b7.getProperty("lastname").getValue());
			Assert.assertNull(tree1.getPathForRow(13));
		} finally {
			tf.setText("Doof");
			ped.fireInputFieldChanged();
			ed.handleActionOk();
		}
	}

	@Test
	public void testUpdatePersonDate() throws InterruptedException, IOException {
		tree1.expandPath(tree1.getPathForRow(1));
		tree1.setSelectionPath(tree1.getPathForRow(3));
		EditorBean ed = treeView1.editBeans();
		EditorPropertyDateSwing ped = (EditorPropertyDateSwing) ed
				.getPropEditor("dateofbirth");
		JTextField tf = (JTextField) ped.getWidget();
		Assert.assertEquals("Oct 14, 1964", tf.getText());
		tf.setText("Oct 14, 196");
		ped.fireInputFieldChanged();
		Assert.assertEquals("Oct 14, 0196", tf.getText());
	}

	@Test
	public void testEditMulipleChoice() throws IOException {
		tree1.expandPath(tree1.getPathForRow(1));

		RapidBean b1 = (RapidBean) tree1.getPathForRow(2)
				.getLastPathComponent();
		Assert.assertEquals("Abalbert", b1.getProperty("lastname").getValue());
		Assert.assertNull(((PropertyChoice) b1.getProperty("sex")).getValue());

		tree1.setSelectionPath(tree1.getPathForRow(2));
		EditorBean ed = treeView1.editBeans();
		EditorPropertyListSwing ped = (EditorPropertyListSwing) ed
				.getPropEditor("sex");
		JList list = ped.getWidgetList();
		Assert.assertEquals(0, list.getModel().getSize());
		EditorPropertyList2Swing ped2 = ped.openListEditor();
		Assert.assertEquals(0, ped2.getWidgetListIn().getModel().getSize());
		Assert.assertEquals(2, ped2.getWidgetListOut().getModel().getSize());

		ped2.getWidgetListOut().setSelectedIndices(new int[] { 0, 1 });
		ped2.addSelectedBeans();
		Assert.assertEquals(2, list.getModel().getSize());
		Assert.assertEquals(2, ((PropertyChoice) b1.getProperty("sex"))
				.getValue().size());
		Assert.assertEquals(2, ped2.getWidgetListIn().getModel().getSize());
		Assert.assertEquals(0, ped2.getWidgetListOut().getModel().getSize());

		ped2.close();
		ed.handleActionApply();
	}

	@Test
	public void testEditDate() throws IOException {
		tree3.expandPath(tree3.getPathForRow(1));
		tree3.setSelectionPath(tree3.getPathForRow(2));
		EditorBean ed = treeView3.editBeans();
		EditorPropertyDateSwing ped = (EditorPropertyDateSwing) ed
				.getPropEditor("dateofbirth");
		JTextField tf = (JTextField) ped.getWidget();
		JButton okButton = ((JButton) ed.getButtonWidgets().get("ok"));
		Assert.assertFalse(okButton.isEnabled());

		tf.setText("1");
		ped.fireInputFieldChanged();
		Assert.assertEquals("1", tf.getText());
		Assert.assertSame(EditorPropertySwing.COLOR_NORMAL, tf.getBackground());
		Assert.assertFalse(okButton.isEnabled());

		tf.setText("1a");
		ped.fireInputFieldChanged();
		Assert.assertEquals("1a", tf.getText());
		Assert.assertSame(EditorPropertySwing.COLOR_INVALID, tf.getBackground());
		Assert.assertFalse(okButton.isEnabled());

		tf.setText("1.1.1");
		KeyEvent keyEvent = new KeyEvent(tf, 0, System.currentTimeMillis(), 0,
				KeyEvent.VK_1, '1');
		ped.handleKeyReleased(keyEvent);
		Assert.assertEquals("1.1.1", tf.getText());
		Assert.assertSame(EditorPropertySwing.COLOR_NORMAL, tf.getBackground());
		Assert.assertTrue(okButton.isEnabled());

		keyEvent = new KeyEvent(tf, 0, System.currentTimeMillis(), 0,
				KeyEvent.VK_ENTER, '\n');
		ped.handleKeyReleased(keyEvent);
		Assert.assertEquals("01.01.2001", tf.getText());
		Assert.assertSame(EditorPropertySwing.COLOR_NORMAL, tf.getBackground());
		Assert.assertTrue(okButton.isEnabled());
	}

	private static class TestApplication extends Application {

		private boolean testMode = TEST_MODE;

		public TestApplication(final String[] docroottypes) throws IOException {
			super("MyAddressbook", docroottypes,
					"org.rapidbeans.test.addressbook5", testDocument
							.getCanonicalPath(), null, null);
		}

		public void setTestMode(final boolean mode) {
			this.testMode = mode;
		}

		public boolean getTestMode() {
			return testMode;
		}
	}

	/**
	 * One doc1 root type.
	 */
	private static class TestApplication1 extends TestApplication {

		public TestApplication1(final File testDocument) throws IOException {
			super(
					new String[] { "org.rapidbeans.test.addressbook5.Addressbook" });
		}
	}

	/**
	 * Two doc1 root types.
	 */
	private static class TestApplication2 extends TestApplication {

		public TestApplication2(final File testDocument) throws IOException {
			super(new String[] {
					"org.rapidbeans.test.addressbook5.Addressbook",
					"org.rapidbeans.test.addressbook5.Person" });
		}
	}

	/**
	 * Two doc1 root types.
	 */
	private static class TestApplication3 extends TestApplication {
		public TestApplication3(final File testDocument) throws IOException {
			super(new String[] {
					"org.rapidbeans.test.addressbook5.Addressbook",
					"org.rapidbeans.test.addressbook5.Person" });
		}
	}

	/**
	 * Two doc1 root types.
	 */
	private static class TestApplication4 extends TestApplication {
		public TestApplication4(final File testDocument) throws IOException {
			super(new String[] {
					"org.rapidbeans.test.addressbook5.Addressbook",
					"org.rapidbeans.test.addressbook5.Person" });
		}
	}

	private void setUpSimple() throws IOException {
		if (testDocument == null) {
			testDocument = new File(
					"../org.rapidbeans/testdata/addressbook5/myaddressbook.xml");
		}
		initApp(application1, "en", Locale.ENGLISH);
		// JUnit 3 Stuff
		// if (testMethodCount == -1) {
		// testMethodCount = this.countTestMethods();
		// }
		view1 = (DocumentView) application1.getView("file:/"
				+ testDocument.getCanonicalPath().replace(File.separatorChar,
						'/') + ".standard");
		doc1 = view1.getDocument();
		treeView1 = (DocumentTreeViewSwing) view1.getTreeView();
		tree1 = (JTree) treeView1.getTree();

		view3 = (DocumentView) application3.getView("file:/"
				+ testDocument.getCanonicalPath().replace(File.separatorChar,
						'/') + ".standard");
		// doc3 = view3.getDocument();
		treeView3 = (DocumentTreeViewSwing) view3.getTreeView();
		tree3 = (JTree) treeView3.getTree();
	}

	public static void main(final String[] args) {
		final Addressbook5IntegrationTest test = new Addressbook5IntegrationTest();
		TEST_MODE = false;
		try {
			test.setUpSimple();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
