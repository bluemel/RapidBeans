/*
 * Rapid Beans Framework: RapidBeanDeserializer.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/27/2005
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

package org.rapidbeans.core.common;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.rapidbeans.core.basic.Id;
import org.rapidbeans.core.basic.IdType;
import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.basic.ThreadLocalValidationSettings;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.core.util.XmlAttribute;
import org.rapidbeans.core.util.XmlNode;
import org.rapidbeans.core.util.XmlNodeTopLevel;
import org.w3c.dom.Node;

/**
 * Deserialize a Rapid Bean from file.
 * 
 * @author Martin Bluemel
 */
public final class RapidBeanDeserializer {

	/**
	 * Helper collection to collect beans with idtype keypropswithparenscope.
	 */
	private Collection<RapidBean> beansWithLateIdBinding = null;

	/**
	 * the URL deserialized
	 */
	private URL url = null;

	/**
	 * the document's character encoding.
	 */
	private String encoding = null;

	/**
	 * @return the document's character encoding.
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * Constructor.
	 * 
	 * @param argFile
	 *            the XML file
	 */
	public RapidBeanDeserializer() {
	}

	public static URL urlFromFile(final File file) {
		URL theUrl = null;
		try {
			theUrl = file.toURI().toURL();
			return theUrl;
		} catch (MalformedURLException e) {
			throw new RapidBeansRuntimeException(e);
		}
	}

	/**
	 * load a bean from an XML file.
	 * 
	 * @param rbType
	 *            the rapid bean type
	 * @param theUrl
	 *            the URL
	 * 
	 * @return the bean
	 */
	public RapidBean loadBean(final TypeRapidBean rbType, final File theFile) {
		return loadBean(rbType, urlFromFile(theFile));
	}

	/**
	 * load a document from an XML file.
	 * 
	 * @param rbType
	 *            the rapid bean type
	 * @param theUrl
	 *            the URL
	 * 
	 * @return the document's root bean
	 */
	public RapidBean loadBean(final TypeRapidBean rbType, final URL theUrl) {
		this.url = theUrl;
		final XmlNodeTopLevel bizBeanNode = XmlNode.getDocumentTopLevel(url);
		return loadBean(rbType, bizBeanNode);
	}

	/**
	 * load a document from a stream.
	 * 
	 * @param rbType
	 *            the type of the root bean
	 * @param url
	 *            the download URL
	 * @param is
	 *            the input stream to load from
	 * 
	 * @return the document's root bean
	 */
	public RapidBean loadBean(TypeRapidBean rbType, URL theUrl, InputStream is) {
		this.url = theUrl;
		final XmlNodeTopLevel bizBeanNode = XmlNode.getDocumentTopLevel(is);
		return loadBean(rbType, bizBeanNode);
	}

	/**
	 * load a bean from an input stream.
	 * 
	 * @param is
	 *            the input stream
	 * @param rbType
	 *            the rapid bean type
	 * 
	 * @return the bean loaded
	 */
	public RapidBean loadBean(final TypeRapidBean rbType, final InputStream is) {
		this.url = null;
		return loadBean(rbType, XmlNode.getDocumentTopLevel(is));
	}

	/**
	 * load a bean from an XML node.
	 * 
	 * @param is
	 *            the input stream
	 * @param rbType
	 *            the root bean type
	 * 
	 * @return the bean
	 */
	public RapidBean loadBean(final TypeRapidBean rbType, final XmlNodeTopLevel bizBeanNode) {
		this.encoding = bizBeanNode.getEncoding();
		TypeRapidBean rootBeanType = rbType;
		final String rootNodeName = bizBeanNode.getName();
		if (rootBeanType == null) {
			// if the type is not given as argument
			// try to find a type via an XML root element binding
			rootBeanType = RapidBeansTypeLoader.getInstance().getXmlRootElementBinding(rootNodeName);
		}
		if (rootBeanType == null) {
			// next try is to read the root element's rb:type attribute if set
			String typename = bizBeanNode.getAttributeValue("@rb:type");
			if (typename == null) {
				// next try is to interpret the XML name space as a package
				// and the rest as a class but I do not know if this is really
				// reasonable
				typename = extractTypenameFromNamespacedRootElement(bizBeanNode);
			}
			rootBeanType = TypeRapidBean.forName(typename);
		}
		final RapidBean bean = RapidBeanImplStrict.createInstance(rootBeanType);
		loadBeanNode(0, bean, bizBeanNode);
		if (this.beansWithLateIdBinding != null) {
			for (RapidBean pbean : this.beansWithLateIdBinding) {
				pbean.clearId();
			}
			this.beansWithLateIdBinding = null;
		}
		return bean;
	}

	private String extractTypenameFromNamespacedRootElement(final XmlNode rootBeanNode) {
		final String rootNodeName = rootBeanNode.getName();
		String typename = null;
		if (!rootNodeName.contains(":")) {
			throw new RapidBeansRuntimeException("can't determine typename."
					+ " Neither root element attribute \"rb:type\"" + " nor a namespace scoped root element is defined");
		}
		final StringTokenizer st = new StringTokenizer(rootNodeName, ":");
		st.nextToken();
		final String rootNodePureName = st.nextToken();
		final String rootNodeNsVal = rootBeanNode.getNamespaceURI();
		if (rootNodeNsVal == null) {
			throw new RapidBeansRuntimeException("can't determine typename."
					+ " Neither root element attribute \"rb:type\"" + " nor a namespace URI is defined");
		}
		typename = mapNamespaceToPackage(rootNodeNsVal);
		typename += "." + StringHelper.upperFirstCharacter(rootNodePureName);
		return typename;
	}

	/**
	 * Map a name space of the form "http://rapidbeans.org/clubadmin/domain" to
	 * a fully qualified class or type name
	 * 
	 * @param namespace
	 *            the XML name space description
	 * 
	 * @return the mapped class or type name
	 */
	private String mapNamespaceToPackage(final String namespace) {
		final StringBuffer buf = new StringBuffer();
		try {
			final URI uri = new URI(namespace);
			final List<String> host = StringHelper.split(uri.getHost(), ".");
			final int hostsize = host.size();
			boolean first = true;
			for (int i = hostsize - 1; i >= 0; i--) {
				if (!first) {
					buf.append('.');
				}
				buf.append(host.get(i));
				if (first) {
					first = false;
				}
			}
			for (String pathElement : StringHelper.split(uri.getPath(), "/")) {
				if (!first) {
					buf.append('.');
				}
				buf.append(pathElement);
				if (first) {
					first = false;
				}
			}
		} catch (URISyntaxException e) {
			throw new RapidBeansRuntimeException(e);
		}
		return buf.toString();
	}

	/**
	 * load a bean from an XML node.
	 * 
	 * @param depth
	 *            the recursion depth
	 * @param bean
	 *            the bean to fill with values
	 * @param node
	 *            the XML DOM node
	 */
	private void loadBeanNode(final int depth, final RapidBean bean, final XmlNode node) {
		// load XML attribute into bean properties
		Property prop;
		for (XmlAttribute attr : node.getAttributes()) {
			// the type attribute has a special meaning
			// - top level: defines the top level bean's type
			// - else: defines a bean's fine type while the
			// property's type is a supertype
			// (e. g. for Composites)
			if (attr.getName().equals("rb:type")) {
				continue;
			} else if (attr.getName().equals("id")) {
				if (bean.getType().getIdtype() != IdType.keyprops
						&& bean.getType().getIdtype() != IdType.keypropswithparentscope
						&& !bean.getType().hasDependendKeyProp()) {
					bean.setId(Id.createInstance(bean, attr.getValue()));
				}
			} else {
				prop = bean.getProperty(attr.getName());
				if (prop != null) {
					try {
						ThreadLocalValidationSettings.validationOff();
						prop.setValue(attr.getValue());
					} finally {
						ThreadLocalValidationSettings.remove();
					}
				}
			}
		}

		// load XML sub entities
		final Collection<XmlNode> subnodes = node.getSubnodes();
		PropertyCollection colProp;
		String colPropName;
		String lastColPropName = null;
		for (XmlNode subnode : subnodes) {
			if (subnode.getType() == Node.ELEMENT_NODE) {
				// special handling of non empty collection properties
				// (= collections that have already got some default instances):
				// clean all default instances if there are any
				colProp = determineCollectionProperty(bean, subnode.getName());
				if (colProp == null) {
					prop = bean.getProperty(subnode.getName());
					if (prop != null) {
						try {
							ThreadLocalValidationSettings.validationOff();
							final String value = subnode.getValue();
							prop.setValue(value);
						} finally {
							ThreadLocalValidationSettings.remove();
						}
					}
				} else {
					colPropName = colProp.getType().getPropName();
					if ((lastColPropName == null) || (!lastColPropName.equals(colPropName))) {
						if (colProp.getValue() != null) {
							colProp.setValue(null);
						}
						lastColPropName = colPropName;
					}
					loadBeanSubnode(depth, bean, subnode, colProp);
				}
			}
		}
	}

	/**
	 * load a bean from an XML subnode.
	 * 
	 * @param depth
	 *            the recursion depth
	 * @param bean
	 *            the bean to fill with values
	 * @param subnode
	 *            the XML node
	 * @param colProp
	 *            the collection property for this sub node
	 */
	private void loadBeanSubnode(final int depth, final RapidBean bean, final XmlNode subnode,
			final PropertyCollection colProp) {
		final TypeRapidBean colPropTargetType = determineColPropTargetType(subnode, colProp);
		try {
			final RapidBean subnodeBean = RapidBeanImplStrict.createInstance(colPropTargetType.getName());
			loadBeanNode(depth + 1, subnodeBean, subnode);
			colProp.addLink(subnodeBean);
			if (subnodeBean.getType().getIdtype() == IdType.keypropswithparentscope
					|| subnodeBean.getType().hasDependendKeyProp()) {
				if (this.beansWithLateIdBinding == null) {
					this.beansWithLateIdBinding = new ArrayList<RapidBean>();
				}
				this.beansWithLateIdBinding.add(subnodeBean);
			}
		} catch (RapidBeansRuntimeException e) {
			if (e.getCause() instanceof InstantiationException) {
				throw new RapidBeansRuntimeException("Cannot instantiate bean type \"" + colPropTargetType.getName()
						+ "\"", e);
			} else {
				throw e;
			}
		}
	}

	/**
	 * determines the collection property for a certain subnode name. 1st try)
	 * the collection property is the one with the subnode's name 2nd try) the
	 * collection property is the one with the subnode's name + "s"
	 * 
	 * @param bean
	 *            the bean with the property
	 * @param subnodeName
	 *            the subnode's name
	 * 
	 * @return the collection property
	 */
	private PropertyCollection determineCollectionProperty(final RapidBean bean, final String subnodeName) {

		String subnodeNameAlt = null;
		PropertyCollection colProp = null;

		try {
			colProp = (PropertyCollection) bean.getProperty(subnodeName);
		} catch (ClassCastException e) {
			colProp = null;
		}

		if (colProp == null) {
			subnodeNameAlt = subnodeName + "s";
			try {
				colProp = (PropertyCollection) bean.getProperty(subnodeNameAlt);
			} catch (ClassCastException e) {
				colProp = null;
			}
		}

		if (colProp == null) {
			try {
				colProp = (PropertyCollection) bean.getProperty(bean.getType().mapXmlElementToPropName(subnodeName));
			} catch (ClassCastException e) {
				colProp = null;
			}
		}
		return colProp;
	}

	/**
	 * Determine the target type for the given collection property.
	 * 
	 * Ether the directly target type of the given collection property or the
	 * type specified by the type attribute if the subnode. If a type is
	 * specified by the subnode this must be a subtype of the collection
	 * property's target type.
	 * 
	 * @param subnode
	 *            the subnode
	 * @param colProp
	 *            the collection property
	 * 
	 * @return the determined target type.
	 */
	private TypeRapidBean determineColPropTargetType(final XmlNode subnode, final PropertyCollection colProp) {
		TypeRapidBean subnodeType = null;
		final TypePropertyCollection colPropType = (TypePropertyCollection) colProp.getType();

		// take the 'rb:type' attribute as target type name if given
		final String subnodeTypeName = subnode.getAttributeValue("@rb:type");
		if (subnodeTypeName != null) {
			subnodeType = TypeRapidBean.forName(subnodeTypeName);
		}

		// otherwise take XML Binding
		if (subnodeType == null) {
			subnodeType = colPropType.mapXmlElementToType(subnode.getName());
		}

		// otherwise take directly the collection property's target type
		if (subnodeType == null) {
			// if not given take the collection property's target type
			subnodeType = colPropType.getTargetType();
		}

		if (subnodeType != null) {
			if (!TypeRapidBean.isSameOrSubtype(colPropType.getTargetType(), subnodeType)) {
				throw new RapidBeansRuntimeException("Error while deserializing file " + this.url.toString() + ":\n"
						+ "Subnode type \"" + subnodeType.getName() + "\" is not a subtype of target type \""
						+ colPropType.getTargetType().getName() + "\" of property \"" + subnode.getName() + "\"");
			}
		}

		return subnodeType;
	}
}
