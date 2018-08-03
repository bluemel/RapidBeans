/*
 * Rapid Beans Framework: XmlNode.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 10/21/2007
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copies of the GNU Lesser General Public License and the
 * GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

package org.rapidbeans.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.UtilException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Martin Bluemel
 * 
 *         Utility class for primitive XML parsing using good old JAXP DOM
 *         parser technology.
 */
public class XmlNode {

	/**
	 * the node encapsulated.
	 */
	private Node node = null;

	/**
	 * @return the XML node's name.
	 */
	public String getName() {
		return this.node.getNodeName();
	}

	/**
	 * @return the XML node's type.
	 */
	public short getType() {
		return this.node.getNodeType();
	}

	/**
	 * constructor.
	 * 
	 * @param argNode the node to encupsulate
	 */
	public XmlNode(final Node argNode) {
		this.node = argNode;
	}

	/**
	 * retrieve the first subnode of a node according to the given pattern.<br/>
	 * 
	 * @param pattern the pattern: &lt;entity 1&gt[&lt;entity 2&gt&lt;entity
	 *                3&gt...]
	 * 
	 * @return the found node or null if no node was found
	 */
	public XmlNode getFirstSubnode(final String pattern) {
		Collection<XmlNode> subnodes = this.getSubnodes(pattern);
		if (subnodes.size() == 0) {
			throw new UtilException("XML Util Error: could not find subnode \"" + pattern + "\" of node \""
					+ this.node.getNodeName() + "\"");
		}
		return subnodes.iterator().next();
	}

	/**
	 * determine all sunbnodes of a node.
	 * 
	 * @return a collection of all subnodes (empty collection if no subnode was
	 *         found).
	 */
	public Collection<XmlNode> getSubnodes() {
		final NodeList subnodes = this.node.getChildNodes();
		final int subnodesCount = subnodes.getLength();
		Collection<XmlNode> foundNodes = new ArrayList<XmlNode>();
		for (int i = 0; i < subnodesCount; i++) {
			foundNodes.add(new XmlNode(subnodes.item(i)));
		}
		return foundNodes;
	}

	/**
	 * determine all sub nodes of a node according to the given pattern.
	 * 
	 * @param pattern the pattern: &lt;entity 1&gt[&lt;entity 2&gt&lt;entity
	 *                3&gt...]
	 * @return a collection of found nodes (empty collection if no node was found).
	 */
	public List<XmlNode> getSubnodes(final String pattern) {
		String firstPatternToken = null;
		if (pattern != null) {
			firstPatternToken = new StringTokenizer(pattern, "/").nextToken();
		}
		final NodeList subnodes = this.node.getChildNodes();
		final int subnodesCount = subnodes.getLength();
		final List<XmlNode> foundNodes = new ArrayList<XmlNode>();
		for (int i = 0; i < subnodesCount; i++) {
			if (firstPatternToken == null || subnodes.item(i).getNodeName().equals(firstPatternToken)) {
				foundNodes.add(new XmlNode(subnodes.item(i)));
			}
		}
		return foundNodes;
	}

	/**
	 * find all attributes of the node.
	 * 
	 * @return collection with all attributes
	 */
	public Collection<XmlAttribute> getAttributes() {
		Collection<XmlAttribute> attrs = new ArrayList<XmlAttribute>();
		NamedNodeMap nodes = this.node.getAttributes();
		if (nodes != null) {
			int nodesLen = nodes.getLength();
			for (int i = 0; i < nodesLen; i++) {
				attrs.add(new XmlAttribute(nodes.item(i)));
			}
		}
		return attrs;
	}

	/**
	 * @return the node's namespace URI "xmlns:ns"
	 */
	public String getNamespaceURI() {
		return this.node.getNamespaceURI();
	}

	/**
	 * retrieve an attribute out of a node according to the given pattern and return
	 * it's value.
	 * 
	 * @param pattern the attribute pattern: [&lt;entity 1&gt&lt;entity
	 *                2&gt&lt;entity 3&gt.../]@&lt;attribute&gt
	 * @return the found attribute's value or null if not found
	 */
	public String getAttributeValue(final String pattern) {
		return getAttributeValue(pattern, null);
	}

	/**
	 * retrieve an attribute out of a node according to the given pattern and return
	 * it's value (with default value).
	 * 
	 * @param pattern      the attribute pattern: [&lt;entity 1&gt&lt;entity
	 *                     2&gt&lt;entity 3&gt.../]@&lt;attribute&gt
	 * @param defaultValue the default value in case the attribute is not found
	 * @return the found attribute's value or a default value if not found
	 */
	public String getAttributeValue(final String pattern, final String defaultValue) {
		String ret = defaultValue;
		final String firstPatternToken = new StringTokenizer(pattern, "/").nextToken();
		if (firstPatternToken.startsWith("@")) {
			String attrName = firstPatternToken.substring(1);
			final NamedNodeMap attrs = this.node.getAttributes();
			if (attrs != null) {
				final Node attrNode = attrs.getNamedItem(attrName);
				if (attrNode != null) {
					ret = attrNode.getNodeValue();
				}
			}
		}
		return ret;
	}

	/**
	 * Parse an XML document from a URL using a DOM parser and get the top level
	 * node.
	 * 
	 * @param url the URL
	 * 
	 * @return the top level node
	 */
	public static XmlNodeTopLevel getDocumentTopLevel(final URL url) {
		if (url.getProtocol().equals("file")) {
			return getDocumentTopLevel(new File(url.getFile().replaceAll("%20", " ")));
		} else if (url.getProtocol().equals("ftp") || url.getProtocol().equals("http")
				|| url.toString().startsWith("jar:http:")) {
			return getDocumentTopLevelConnection(url);
		} else {
			throw new RapidBeansRuntimeException("Unsupported protocol \"" + url.getProtocol());
		}
	}

	/**
	 * load a DOM document from a file and get the top level node.
	 * 
	 * @param xmlResourceFile the resource file
	 * 
	 * @return the top level node
	 */
	public static XmlNodeTopLevel getDocumentTopLevel(final File xmlResourceFile) {
		InputStream is = null;
		try {
			is = new FileInputStream(xmlResourceFile);
			return getDocumentTopLevel(is);
		} catch (FileNotFoundException e) {
			throw new UtilException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * load a DOM document via HTTP or FTP and get the top level node.
	 * 
	 * @param url the URL
	 * 
	 * @return the top level node
	 */
	private static XmlNodeTopLevel getDocumentTopLevelConnection(final URL url) {
		InputStream is = null;
		try {
			URLConnection urlc = url.openConnection();
			is = urlc.getInputStream();
			if (is == null) {
				throw new UtilException("URL \"" + url.toString() + "\" not found");
			}
			return getDocumentTopLevel(is);
		} catch (IOException e) {
			throw new UtilException("Problems opening a connection for URL \"" + url.toString() + "\"", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * load a DOM document from a string and get the top level node.
	 * 
	 * @param descr the XML description string
	 * 
	 * @return the top level node
	 */
	public static XmlNode getDocumentTopLevel(final String descr) {
		return getDocumentTopLevel(new ByteArrayInputStream(descr.getBytes()));
	}

	/**
	 * load a DOM document and get the top level node.
	 * 
	 * @param inputStream the stream.
	 * 
	 * @return the top level node
	 */
	public static XmlNodeTopLevel getDocumentTopLevel(final InputStream inputStream) {
		try {
			final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			// dbf.setValidating(true);
			dbf.setValidating(false);
			final DocumentBuilder db = dbf.newDocumentBuilder();
			// db.setErrorHandler(new ErrorHandler() {
			// @Override
			// public void error(SAXParseException e) throws SAXException {
			// System.out.println("PARSER ERROR: " + e.getMessage());
			// }
			// @Override
			// public void fatalError(SAXParseException e) throws SAXException {
			// System.out.println("PARSER FATAL ERROR: " + e.getMessage());
			// }
			// @Override
			// public void warning(SAXParseException e) throws SAXException {
			// System.out.println("PARSER WARNING: " + e.getMessage());
			// }
			// });
			final Document doc = db.parse(inputStream);
			final NodeList topLevelNodes = doc.getChildNodes();
			int i = 0;
			Node topLevelNode = null;
			Node node;
			while ((node = topLevelNodes.item(i++)) != null) {
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					topLevelNode = node;
				}
			}
			if (topLevelNode == null) {
				throw new RapidBeansRuntimeException("No top level element found.");
			}
			return new XmlNodeTopLevel(topLevelNode, doc.getXmlEncoding());
		} catch (ParserConfigurationException e) {
			throw new UtilException(e);
		} catch (SAXException e) {
			throw new UtilException(e);
		} catch (IOException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * @return the XML node value.
	 */
	public String getValue() {
		if (this.node.getFirstChild() != null) {
			return this.node.getFirstChild().getNodeValue();
		} else {
			return "";
		}
	}
}
