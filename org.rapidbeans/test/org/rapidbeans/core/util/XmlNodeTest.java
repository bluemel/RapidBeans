/*
 * Rapid Beans Framework: BBXmNodeTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Nov 9, 2005
 */
package org.rapidbeans.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

/**
 * The Unit Tests for XmlNode.
 * 
 * @author Martin Bluemel
 */
public final class XmlNodeTest {

	/**
	 * test.
	 */
	@Test
	public void testGetFirstSubnodeSimple() {
		XmlNode topLevelNode = XmlNode.getDocumentTopLevel(new File(
				"../org.rapidbeans/testmodel/org/rapidbeans/test/codegen/Address.xml"));
		XmlNode subnode = topLevelNode.getFirstSubnode("property");
		Assert.assertEquals("country", subnode.getAttributeValue("@name"));
	}

	/**
	 * test.
	 */
	@Test
	public void testGetSubnodesSimple() {
		XmlNode topLevelNode = XmlNode.getDocumentTopLevel(new File(
				"../org.rapidbeans/testmodel/org/rapidbeans/test/codegen/Address.xml"));
		Collection<XmlNode> subnodes = topLevelNode.getSubnodes("property");
		Assert.assertEquals(5, subnodes.size());
	}

	/**
	 * Test method.
	 */
	@Test
	public void testGetAttributeValueExistent() {
		XmlNode topLevelNode = XmlNode.getDocumentTopLevel(new File(
				"../org.rapidbeans/testmodel/org/rapidbeans/test/codegen/Address.xml"));
		XmlNode subnode = topLevelNode.getFirstSubnode("property");
		Assert.assertEquals("country", subnode.getAttributeValue("@name"));
	}

	/**
	 * Test method.
	 */
	@Test
	public void testGetAttributeValueNotExistent() {
		XmlNode topLevelNode = XmlNode.getDocumentTopLevel(new File(
				"../org.rapidbeans/testmodel/org/rapidbeans/test/codegen/Address.xml"));
		XmlNode subnode = topLevelNode.getFirstSubnode("property");
		Assert.assertNull(subnode.getAttributeValue("@xxx"));
	}

	/**
	 * Test method.
	 */
	@Test
	public void testGetAttributeValueDefaultExistent() {
		XmlNode topLevelNode = XmlNode.getDocumentTopLevel(new File(
				"../org.rapidbeans/testmodel/org/rapidbeans/test/codegen/Address.xml"));
		XmlNode subnode = topLevelNode.getFirstSubnode("property");
		Assert.assertEquals("country", subnode.getAttributeValue("@name", "eugene"));
	}

	/**
	 * Test method.
	 */
	@Test
	public void testGetAttributeValueDefaultNotFound() {
		XmlNode topLevelNode = XmlNode.getDocumentTopLevel(new File(
				"../org.rapidbeans/testmodel/org/rapidbeans/test/codegen/Address.xml"));
		XmlNode subnode = topLevelNode.getFirstSubnode("property");
		Assert.assertEquals("eugene", subnode.getAttributeValue("@xxx", "eugene"));
	}

	/**
	 * Test method.
	 */
	@Test
	public void testGetAttributeValueDefaultEmpty() {
		XmlNode topLevelNode = XmlNode.getDocumentTopLevel(new File(
				"../org.rapidbeans/testmodel/org/rapidbeans/test/codegen/Address.xml"));
		XmlNode subnode = topLevelNode.getFirstSubnode("property");
		Assert.assertEquals("", subnode.getAttributeValue("@testempty", "xxx"));
	}

	// NEVER EVER USED!?!
	// /**
	// * test.
	// */
	// public void testGetDocumentTopLevelClassLoader() {
	// XmlNode topLevelNode = XmlNode.getDocumentTopLevelClassLoader(
	// "org/rapidbeans/test/Address.xml");
	// Assert.assertEquals("beantype", topLevelNode.getName());
	// }

	/**
	 * Test method.
	 */
	@Test
	public void testGetDocumentTopLevelFile() {
		XmlNode topLevelNode = XmlNode.getDocumentTopLevel(new File(
				"../org.rapidbeans/testmodel/org/rapidbeans/test/codegen/Address.xml"));
		Assert.assertNotNull(topLevelNode);
	}

	/**
	 * Test method.
	 * 
	 * @throws FileNotFoundException
	 *             if file not found
	 */
	@Test
	public void testGetDocumentTopLevelInputStream() throws FileNotFoundException {
		XmlNode topLevelNode = XmlNode.getDocumentTopLevel(new FileInputStream(new File(
				"../org.rapidbeans/testmodel/org/rapidbeans/test/codegen/Address.xml")));
		Assert.assertNotNull(topLevelNode);
	}

	/**
	 * test read in an XML string.
	 */
	@Test
	public void testGetDocumentTopLevelString() {
		final String descr = "<property name=\"test\" type=\"string\"" + " default=\"test1\"" + "/>";
		final XmlNode node = XmlNode.getDocumentTopLevel(new ByteArrayInputStream(descr.getBytes()));
		Assert.assertEquals("property", node.getName());
		Assert.assertEquals("test", node.getAttributeValue("@name"));
		Assert.assertEquals("string", node.getAttributeValue("@type"));
		Assert.assertEquals("test1", node.getAttributeValue("@default"));
	}
}
