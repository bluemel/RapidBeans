package org.rapidbeans.core.util;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XmlHelperTest {

	@Test
	public void testRetrieveNodeTopLevel() {
		Node tlNode = XmlHelper.getDocumentTopLevel(new File("testdata/xml/textXmlComplex01.xml")).getParentNode();
		Assert.assertEquals("workbench", XmlHelper.getNode(tlNode, "//workbench").getNodeName());
	}

	@Test
	public void testRetrieveSubnode() {
		Node tlNode = XmlHelper.getDocumentTopLevel(new File("testdata/xml/textXmlComplex01.xml")).getParentNode();
		Assert.assertEquals("view", XmlHelper.getNode(tlNode, "//workbench/window/page/views/view").getNodeName());
	}

	@Test
	public void testRetrieveSubnodeWithSpecNodeText() {
		Node tlNode = XmlHelper.getDocumentTopLevel(new File("testdata/xml/web.xml")).getParentNode();
		Assert.assertEquals("param-name", XmlHelper.getNode(tlNode,
				"//web-app/context-param/param-name[.='config_home']").getNodeName());
		Assert.assertEquals("config_home", XmlHelper.getNode(tlNode,
				"//web-app/context-param/param-name[.='config_home']").getFirstChild().getNodeValue());
	}

	@Test
	public void testRetrieveSubnodeWithSpecAttribute() {
		Node tlNode = XmlHelper.getDocumentTopLevel(new File("testdata/xml/textXmlComplex01.xml")).getParentNode();
		Assert.assertEquals("view", XmlHelper.getNode(tlNode,
				"//workbench/window/page/views/view[@id='org.eclipse.jdt.ui.PackageExplorer']").getNodeName());
	}

	@Test
	public void testRetrieveSubnodeWithSpecAttributeComplex() {
		Node tlNode = XmlHelper.getDocumentTopLevel(new File("testdata/xml/textXmlComplex01.xml")).getParentNode();
		Assert.assertEquals("false",
				XmlHelper.getNode(
						tlNode,
						"//workbench/window/page/views"
								+ "/view[@id='org.eclipse.jdt.ui.PackageExplorer']"
								+ "/viewState/customFilters/xmlDefinedFilters"
								+ "/child[@filterId='org.eclipse.jdt.ui.PackageExplorer_patternFilterId_.*']"
								+ "/@isEnabled").getNodeValue());
	}

	@Test
	public void testRetrieveSubnodeWithSpecSubnode() {
		Document doc = XmlHelper.getDocument(new File("testdata/xml/web.xml"));
		Assert.assertEquals("web-app", XmlHelper.getNode(doc, "//web-app").getNodeName());
		Assert.assertEquals("context-param", XmlHelper.getNode(doc, "//web-app/context-param").getNodeName());
		Assert.assertEquals("param-name",
				XmlHelper.getNode(doc, "//web-app/context-param/param-name[.='config_home']").getNodeName());
		Assert.assertEquals("param-name", XmlHelper.getNodes(doc,
				"//web-app/context-param/param-name[text()='config_home']").item(0).getNodeName());
		Assert.assertEquals("context-param", XmlHelper.getNodes(doc,
				"//web-app/context-param[param-name/.='config_home']").item(0).getNodeName());
		Assert.assertEquals("context-param", XmlHelper.getNodes(doc,
				"//web-app/context-param[param-name/text()='config_home']").item(0).getNodeName());
	}

	@Test
	public void testParseIdAttrs() {
		XmlHelper.parseIdAttrs("//xxx/yyy[@id='org.eclipse.wst.server.ui.editor'"
				+ " and @name='JBoss 6.0 Runtime Server']");
	}
}
