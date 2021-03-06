/*
 * Rapid Beans Framework: XmlHelper.java
 * 
 * Copyright (C) 2010 Martin Bluemel
 * 
 * Creation Date: 09/12/2010
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
 *         Utility class for easy XML parsing
 */
public class XmlHelper {

	/**
	 * get the first XML sub node.
	 * 
	 * @param node    - parent node
	 * @param pattern - node pattern
	 * 
	 * @return - sub node
	 */
	public static Node getFirstSubnode(final Node node, final String pattern) {
		Node[] subnodes = getSubnodes(node, pattern);
		if (subnodes.length == 0) {
			throw new RapidBeansRuntimeException("XML Util Error: could not find subnode \"" + pattern + "\" of node \""
					+ node.getNodeName() + "\"");
		}
		return subnodes[0];
	}

	public static Node[] getSubnodes(final Node node, final String pattern) {
		String firstPatternToken = null;
		if (pattern != null) {
			firstPatternToken = new StringTokenizer(pattern, "/").nextToken();
		}
		final NodeList subnodes = node.getChildNodes();
		final int subnodesCount = subnodes.getLength();
		final List<Node> foundNodes = new ArrayList<Node>();
		for (int i = 0; i < subnodesCount; i++) {
			if (firstPatternToken == null || subnodes.item(i).getNodeName().equals(firstPatternToken)) {
				foundNodes.add(subnodes.item(i));
			}
		}
		Node[] ret = new Node[0];
		ret = (Node[]) foundNodes.toArray(ret);
		return ret;
	}

	public static String getNodeValue(final Node node, final String pattern) {
		return getNodeValue(node, pattern, null);
	}

	public static String getNodeValue(final Node startNode, final String pattern, final String defaultValue) {
		String ret = defaultValue;
		Node node = getNode(startNode, pattern);
		if (node != null) {
			final Node firstChild = node.getFirstChild();
			if (firstChild != null) {
				ret = firstChild.getNodeValue();
			} else {
				ret = node.getNodeValue();
			}
		}
		return ret;
	}

	/**
	 * Helper function using Java XPath support.
	 * 
	 * @param node            an arbitrary XML element node to start the search with
	 * @param nodePathPattern An XPath expression - e. g.:
	 *                        //Server/Service/Connector/@port
	 *                        Service/Connector[2]/@port
	 * 
	 * @return a list with found nodes (might be empty)
	 */
	public static NodeList getNodes(final Document doc, final String xPathExpression) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList nodes;
		try {
			nodes = (NodeList) xPath.evaluate(xPathExpression, doc.getDocumentElement(), XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new UtilException("Error while parsing XPath expression \"" + xPathExpression + "\"", e);
		}
		return nodes;
	}

	/**
	 * Self written search function with XPath like expression syntax. Not
	 * recommended if you have the document node. Then please prefer the getNode()
	 * and getNodes() functions above.
	 * 
	 * @param node            an arbitrary XML element node to start the search with
	 * @param nodePathPattern An XPath like expression with strong restrictions
	 *                        against XPath - e. g.:
	 *                        //Server/Service/Connector/@port
	 *                        Service/Connector[2]/@port
	 * 
	 * @return found element or attribute node / subnode
	 */
	public static Node getNode(final Node startNode, final String xPathExpression) {
		if (StringHelper.trim(xPathExpression).equals("")) {
			return startNode;
		}
		Node foundNode = null;
		if (startNode instanceof Document) {
			final XPath xPath = XPathFactory.newInstance().newXPath();
			try {
				foundNode = (Node) xPath.evaluate(xPathExpression, ((Document) startNode).getDocumentElement(),
						XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				throw new UtilException("Error while parsing XPath expression \"" + xPathExpression + "\"", e);
			}
		} else {
			final StringTokenizer st = new StringTokenizer(xPathExpression, "/");
			final String firstPatternToken = st.nextToken();
			if (firstPatternToken.startsWith("@")) {
				final String attrName = firstPatternToken.substring(1);
				foundNode = startNode.getAttributes().getNamedItem(attrName);
			} else {
				final String subnodePathPattern = getSubnodePathPattern(xPathExpression, firstPatternToken);
				final String subnodeName = getSubnodePathToken(firstPatternToken);
				final SubnodeFilter subnodeFilter = createSubnodeFilter(new XmlHelper(), firstPatternToken);
				final Node[] subnodes = getSubnodes(startNode, subnodeName);
				if (subnodes != null && subnodes.length > 0) {
					foundNode = subnodeFilter.filter(subnodes, subnodePathPattern);
				}
			}
		}
		return foundNode;
	}

	/**
	 * extracts the name.
	 * 
	 * @param pattern           - xxx
	 * @param firstPatternToken - xxx
	 * @return node name without order e. g. xxx[2] -> xxx
	 */
	private static String getSubnodePathPattern(final String pattern, final String firstPatternToken) {
		switch (StringHelper.split(pattern, "/").size()) {
		case 0:
		case 1:
			return "";
		default:
			if (pattern.startsWith("//")) {
				return pattern.substring(firstPatternToken.length() + 3);
			} else if (pattern.startsWith("/")) {
				return pattern.substring(firstPatternToken.length() + 2);
			} else {
				if (pattern.length() > firstPatternToken.length()) {
					return pattern.substring(firstPatternToken.length() + 1);
				} else {
					return "";
				}
			}
		}
	}

	/**
	 * extracts the parent.
	 * 
	 * @param nodePattern nodepattern with parent
	 * @return the parent
	 */
	public static String getParentNodePattern(final String nodePattern) {
		return StringHelper.splitBeforeLast(nodePattern, "/");
	}

	/**
	 * extracts the attribute name.
	 * 
	 * @param nodePattern - node pattern with attribute
	 * @return attribute name
	 */
	public static String getAttributeName(final String nodePattern) {
		StringTokenizer st = new StringTokenizer(nodePattern, "/");
		String token = null;
		while (st.hasMoreTokens()) {
			token = st.nextToken();
		}
		if (!token.startsWith("@")) {
			throw new RapidBeansRuntimeException("pattern \"" + nodePattern + "\" does not define an attribute");
		}
		return token.substring(1);
	}

	/**
	 * extracts the name.
	 * 
	 * @param patternToken - XML path component
	 * @return node name without order e. g. xxx[2] -> xxx
	 */
	private static String getSubnodePathToken(final String patternToken) {
		if (patternToken.indexOf('[') > -1) {
			return patternToken.substring(0, patternToken.indexOf('['));
		} else {
			return patternToken;
		}
	}

	/**
	 * search for the top level document.
	 * 
	 * @param xmlResourceFileName file name
	 * @return top level document
	 */
	public static Document getDocument(final String xmlResourceFileName) {
		final InputStream is = ClassLoader.getSystemResourceAsStream(xmlResourceFileName);
		if (is == null) {
			throw new RapidBeansRuntimeException("System resource file \"" + xmlResourceFileName + "\" not found");
		}
		return (Document) getDocumentTopLevel(is, false);
	}

	/**
	 * search for the top level document.
	 * 
	 * @param xmlResourceFile the file to load.
	 * @return top level document
	 */
	public static Document getDocument(final File xmlResourceFile) {
		InputStream is;
		try {
			is = new FileInputStream(xmlResourceFile);
		} catch (FileNotFoundException e) {
			throw new RapidBeansRuntimeException(e);
		}
		return (Document) getDocumentTopLevel(is, false);
	}

	/**
	 * search for the top level document.
	 * 
	 * @param xmlResourceFileName file name
	 * @return top level document
	 */
	public static Node getDocumentTopLevel(final String xmlResourceFileName) {
		final InputStream is = ClassLoader.getSystemResourceAsStream(xmlResourceFileName);
		if (is == null) {
			throw new RapidBeansRuntimeException("System resource file \"" + xmlResourceFileName + "\" not found");
		}
		return getDocumentTopLevel(is, true);
	}

	/**
	 * search for the top level document.
	 * 
	 * @param xmlResourceFile the file to load.
	 * @return top level document
	 */
	public static Node getDocumentTopLevel(final File xmlResourceFile) {
		InputStream is;
		try {
			is = new FileInputStream(xmlResourceFile);
		} catch (FileNotFoundException e) {
			throw new RapidBeansRuntimeException(e);
		}
		return getDocumentTopLevel(is, true);
	}

	/**
	 * Load the top level document.
	 * 
	 * @param InputStream is
	 * @return top level document
	 */
	public static Node getDocumentTopLevel(final InputStream is, final boolean firstChild) {
		try {
			final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(false);
			dbf.setValidating(false);
			final DocumentBuilder db = dbf.newDocumentBuilder();
			final Document doc = db.parse(is);
			if (firstChild) {
				return doc.getFirstChild();
			}
			return doc;
		} catch (ParserConfigurationException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (SAXException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (IOException e) {
			throw new RapidBeansRuntimeException(e);
		}
	}

	public static final SubnodeFilter createSubnodeFilter(final XmlHelper encInstance, final String s) {
		if (s.matches(".*\\[[0-9]*]")) {
			return encInstance.new SubnodeFilterOrder(s);
		} else if (s.matches(".*\\[@.* *= *'.*'].*")) {
			return encInstance.new SubnodeFilterAttvals(s);
		} else if (s.matches(".*\\[\\. *= *'.*'].*")) {
			return encInstance.new SubnodeFilterNodeval(s);
		} else {
			return encInstance.new SubnodeFilterFirst(s);
		}
	}

	private abstract class SubnodeFilter {
		public abstract Node filter(Node[] subnodes, String subnodePattern);
	}

	private class SubnodeFilterFirst extends SubnodeFilter {

		public Node filter(Node[] subnodes, final String subnodePathPattern) {
			Node ret;
			for (int i = 0; i < subnodes.length; i++) {
				ret = getNode(subnodes[i], subnodePathPattern);
				if (ret != null) {
					return ret;
				}
			}
			return null;
		}

		public SubnodeFilterFirst(String s) {
			if (s.indexOf('[') != -1) {
				throw new RapidBeansRuntimeException("'[' found in string \"" + s + "\"");
			}
		}
	}

	private class SubnodeFilterOrder extends SubnodeFilter {

		private int order = -1;

		public Node filter(Node[] subnodes, final String subnodePathPattern) {
			if (this.order >= subnodes.length) {
				return null;
			}
			return getNode(subnodes[this.order], subnodePathPattern);
		}

		public SubnodeFilterOrder(String s) {
			if (s.indexOf('[') > -1) {
				this.order = Integer.parseInt(s.substring(s.indexOf('[') + 1, s.length() - 1));
			} else {
				throw new RapidBeansRuntimeException("'[' not found in string \"" + s + "\"");
			}
		}
	}

	private class SubnodeFilterNodeval extends SubnodeFilter {

		private String nodeval = null;

		public Node filter(Node[] subnodes, final String subnodePathPattern) {
			Node ret = null;
			for (int i = 0; i < subnodes.length; i++) {
				if (subnodes[i].getFirstChild().getNodeValue().equals(this.nodeval)) {
					ret = getNode(subnodes[i], subnodePathPattern);
					break;
				}
			}
			return ret;
		}

		public SubnodeFilterNodeval(final String s) {
			if (s.indexOf('[') > -1) {
				this.nodeval = parseNodeval(s);
			} else {
				throw new RapidBeansRuntimeException("'[' not found in string \"" + s + "\"");
			}
		}
	}

	private class SubnodeFilterAttvals extends SubnodeFilter {

		private Map<String, String> attrmap = null;

		public Node filter(Node[] subnodes, final String subnodePathPattern) {
			Node ret = null;
			for (int i = 0; i < subnodes.length; i++) {
				final NamedNodeMap attrs = subnodes[i].getAttributes();
				if (attrs != null) {
					boolean match = true;
					for (final Entry<String, String> entry : this.attrmap.entrySet()) {
						final Node attr = attrs.getNamedItem(entry.getKey());
						if ((attr == null) || (!attr.getNodeValue().equals(entry.getValue()))) {
							match = false;
							break;
						}
					}
					if (match) {
						ret = getNode(subnodes[i], subnodePathPattern);
						break;
					}
				}
			}
			return ret;
		}

		public SubnodeFilterAttvals(final String s) {
			if (s.indexOf('[') > -1) {
				this.attrmap = parseIdAttrs(s);
			} else {
				throw new RapidBeansRuntimeException("'[' not found in string \"" + s + "\"");
			}
		}
	}

	public static Map<String, String> parseIdAttrs(final String nodePath) {
		final Map<String, String> idAttrs = new HashMap<String, String>();
		if (nodePath.contains("[")) {
			if (nodePath.matches(".*\\[.*\\]\\z")) {
				final String idAttrString = nodePath.substring(nodePath.indexOf('[') + 1, nodePath.length() - 1);
				final int len = idAttrString.length();
				int state = 0;
				String idAttrName = null;
				String idAttrValue = null;
				StringBuffer buf = new StringBuffer();
				for (int i = 0; i < len && state >= 0; i++) {
					final char c = idAttrString.charAt(i);
					switch (state) {
					case 0:
						switch (c) {
						case ' ':
						case '\t':
						case '\n':
							break;
						case '@':
							state = 1;
							break;
						default:
							state = -1;
							break;
						}
						break;
					case 1:
						switch (c) {
						case ' ':
						case '\t':
						case '\n':
							idAttrName = buf.toString();
							buf.setLength(0);
							state = 2;
							break;
						case '=':
							idAttrName = buf.toString();
							buf.setLength(0);
							state = 3;
							break;
						default:
							buf.append(c);
							break;
						}
						break;
					case 2:
						switch (c) {
						case ' ':
						case '\t':
						case '\n':
							break;
						case '=':
							state = 3;
							break;
						default:
							state = -1;
							break;
						}
						break;
					case 3:
						switch (c) {
						case ' ':
						case '\t':
						case '\n':
							break;
						case '\'':
							state = 4;
							break;
						default:
							state = -1;
							break;
						}
						break;
					case 4:
						switch (c) {
						case '\'':
							idAttrValue = buf.toString();
							idAttrs.put(idAttrName, idAttrValue);
							state = 5;
							break;
						default:
							buf.append(c);
							break;
						}
						break;
					case 5:
						switch (c) {
						case ' ':
						case '\t':
						case '\n':
							break;
						case 'a':
							state = 6;
							break;
						default:
							state = -1;
							break;
						}
						break;
					case 6:
						switch (c) {
						case 'n':
							state = 7;
							break;
						default:
							state = -1;
							break;
						}
						break;
					case 7:
						switch (c) {
						case 'd':
							state = 0;
							break;
						default:
							state = -1;
							break;
						}
						break;
					}
				}
				if (state == -1) {
					idAttrs.clear();
				}
			}
		}
		return idAttrs;
	}

	public static String parseNodeval(final String nodePath) {
		final StringBuilder sb = new StringBuilder();
		if (nodePath.contains("[")) {
			if (nodePath.matches(".*\\[.*\\]\\z")) {
				final String idAttrString = nodePath.substring(nodePath.indexOf('[') + 1, nodePath.length() - 1);
				final int len = idAttrString.length();
				int state = 0;
				for (int i = 0; i < len && state >= 0; i++) {
					final char c = idAttrString.charAt(i);
					switch (state) {
					case 0:
						switch (c) {
						case '.':
						case ' ':
						case '\t':
						case '\n':
							break;
						case '=':
							state = 1;
							break;
						default:
							state = -1;
							break;
						}
						break;
					case 1:
						switch (c) {
						case ' ':
						case '\t':
						case '\n':
							break;
						case '\'':
							state = 2;
							break;
						default:
							state = -1;
							break;
						}
						break;
					case 2:
						switch (c) {
						case '\'':
							state = 5;
							break;
						default:
							sb.append(c);
							break;
						}
						break;
					case 5:
						switch (c) {
						case ' ':
						case '\t':
						case '\n':
							break;
						default:
							state = -1;
							break;
						}
						break;
					}
					if (state == -1) {
						throw new UtilException("parseNodeval(\"" + nodePath + "\")  failed with invalid parser state");
					}
				}
			}
		}
		return sb.toString();
	}
}
