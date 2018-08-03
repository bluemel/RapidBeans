/*
 * Rapid Beans Framework: RapidBeanDeserializerTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * created 25.02.2008
 */
package org.rapidbeans.core.common;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.config.ConfigApplication;
import org.rapidbeans.presentation.config.ConfigMainWindow;
import org.rapidbeans.presentation.config.ConfigMenubar;
import org.rapidbeans.presentation.config.ConfigSubmenu;
import org.rapidbeans.test.codegen.Person;

import junit.framework.TestCase;

/**
 * Unit test for class RapidBeanDeserializer.
 * 
 * @author Martin Bluemel
 */
public class RapidBeanDeserializerTest extends TestCase {

	/**
	 * Test method for
	 * {@link org.rapidbeans.core.common.RapidBeanDeserializer#loadBean(org.rapidbeans.core.type.TypeRapidBean, java.net.URL)}
	 * . (type = null).
	 * 
	 * @throws IOException
	 */
	public void testLoadBeanGenericStyle() throws IOException {
		File file1 = new File("src/test/resources/deserialization/AppGenericStyle.xml");
		Document doc = new Document("testdoc", file1);
		ConfigApplication cfg = (ConfigApplication) doc.getRoot();
		assertEquals("org.rapidbeans.presentation.TestClient", cfg.getApplicationclass());
		assertNotNull((ConfigMainWindow) doc.findBeanByQuery("/mainwindow"));
		assertNotNull((ConfigMenubar) doc.findBeanByQuery("/mainwindow/menubar"));
		assertEquals("test",
				((ConfigSubmenu) doc.findBeanByQuery("/mainwindow/menubar/menus[name = 'test']")).getName());
		Collection<RapidBean> menuItems = doc
				.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuItem[name = 'test222']");
		assertEquals(1, menuItems.size());
	}

	/**
	 * Test method for
	 * {@link org.rapidbeans.core.common.RapidBeanDeserializer#loadBean(org.rapidbeans.core.type.TypeRapidBean, java.net.URL)}
	 * . (type = null).
	 * 
	 * @throws IOException
	 */
	public void testLoadBeanConcreteStyle() throws IOException {
		Document doc = new Document("testdoc", TypeRapidBean.forName(ConfigApplication.class.getName()),
				new File("src/test/resources/deserialization/AppConcreteStyle.xml"));
		ConfigApplication cfg = (ConfigApplication) doc.getRoot();
		assertEquals("org.rapidbeans.presentation.TestClient", cfg.getApplicationclass());
		assertNotNull((ConfigMainWindow) doc.findBeanByQuery("/mainwindow"));
		assertNotNull((ConfigMenubar) doc.findBeanByQuery("/mainwindow/menubar"));
		assertEquals("test",
				((ConfigSubmenu) doc.findBeanByQuery("/mainwindow/menubar/menus[name = 'test']")).getName());
		Collection<RapidBean> menuItems = doc
				.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuItem[name = 'test222']");
		assertEquals(1, menuItems.size());
	}

	/**
	 * Test method for
	 * {@link org.rapidbeans.core.common.RapidBeanDeserializer#loadBean(org.rapidbeans.core.type.TypeRapidBean, java.net.URL)}
	 * . (type = null).
	 * 
	 * @throws IOException
	 */
	public void testLoadAppNamespaceStyle() throws IOException {
		File file1 = new File("src/test/resources/deserialization/AppNamespaceStyle.xml");
		Document doc = new Document("testdoc", file1);
		ConfigApplication cfg = (ConfigApplication) doc.getRoot();
		assertEquals("org.rapidbeans.presentation.TestClient", cfg.getApplicationclass());
		assertNotNull((ConfigMainWindow) doc.findBeanByQuery("/mainwindow"));
		assertNotNull((ConfigMenubar) doc.findBeanByQuery("/mainwindow/menubar"));
		assertEquals("test",
				((ConfigSubmenu) doc.findBeanByQuery("/mainwindow/menubar/menus[name = 'test']")).getName());
		Collection<RapidBean> menuItems = doc
				.findBeansByQuery("org.rapidbeans.presentation.config.ConfigMenuItem[name = 'test222']");
		assertEquals(1, menuItems.size());
	}

	/**
	 * Test the default serialization for multiline strings as attributes.
	 */
	public void testDeserializeStringMultilineAttribute() throws MalformedURLException {
		File testfile = new File("src/test/resources/serialization/testSerMultiAttr.xml");
		Document doc = new Document(testfile);
		Person p1 = (Person) doc.findBean("org.rapidbeans.test.codegen.Person", "Bluemel_Martin_19641014");
		assertEquals("xxx\nyyy", p1.getComment());
	}

	/**
	 * Test the default serialization for multiline strings as attributes.
	 */
	public void testSerializeStringMultilineElement() throws MalformedURLException {
		File testfile = new File("src/test/resources/serialization/testSerMultiElem.xml");
		Document doc = new Document(testfile);
		Person p1 = (Person) doc.findBean("org.rapidbeans.test.codegen.Person", "Bluemel_Martin_19641014");
		assertEquals("xxx\nyyy", p1.getComment2());
	}

	/**
	 * Test the default serialization for multiline strings as attributes.
	 */
	public void testSerializeStringMultilineElementWithDefinedMapping() throws MalformedURLException {
		File testfile = new File("src/test/resources/serialization/testSerMultiElemDefinedMapping.xml");
		Document doc = new Document(testfile);
		Person p1 = (Person) doc.findBean("org.rapidbeans.test.codegen.Person", "Bluemel_Martin_19641014");
		assertEquals("xxx\nyyy", p1.getComment3());
	}
}
