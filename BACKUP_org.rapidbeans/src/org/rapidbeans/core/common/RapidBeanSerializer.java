/*
 * Rapid Beans Framework: RapidBeanSerializer.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 04/02/2006
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.List;

import org.rapidbeans.core.basic.IdType;
import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.util.FileHelper;
import org.rapidbeans.core.util.PlatformHelper;

/**
 * Serialize a RapidBeansDocument to a stream / file.
 * 
 * @author Martin Bluemel
 */
public final class RapidBeanSerializer {

	/**
	 * Constructor.
	 */
	public RapidBeanSerializer() {
	}

	/**
	 * save a bean or a composite of beans to an XML file.
	 * 
	 * @param rootBean
	 *            the bean or root bean of the composite
	 */
	public void saveBean(final RapidBean rootBean, final URL url, final String encoding) {
		if (url == null) {
			throw new IllegalArgumentException("no URL given");
		}
		OutputStreamWriter osw = null;
		String enc = encoding;
		try {
			if (enc == null || enc.length() == 0) {
				enc = "UTF-8";
			}
			if (url.getProtocol().equals("file")) {
				final File file = new File(url.getFile().replaceAll("%20", " "));
				if (!(file.getParentFile().exists())) {
					FileHelper.mkdirs(file.getParentFile());
				}
				if (!(file.exists())) {
					if (!file.createNewFile()) {
						throw new RapidBeansRuntimeException("Could not create file \"" + file.getAbsolutePath() + "\"");
					}
				}
				osw = new OutputStreamWriter(new FileOutputStream(file), enc);
			} else if (url.getProtocol().equals("ftp") || url.getProtocol().equals("http")) {
				final URLConnection urlc = url.openConnection();
				if (url.getProtocol().equals("http")) {
					urlc.setDoOutput(true);
				}
				final OutputStream os = urlc.getOutputStream();
				osw = new OutputStreamWriter(os, enc);
			} else {
				throw new RapidBeansRuntimeException("Unsupported protocol \"" + url.getProtocol() + "\"");
			}
			writeToWriter(rootBean, osw, enc);
		} catch (IOException e) {
			throw new RapidBeansRuntimeException("writing document file \"" + url.toString() + "\"failed", e);
		} finally {
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
					throw new RapidBeansRuntimeException("writing document file \"" + url.toString() + "\"failed", e);
				}
			}
		}
	}

	/**
	 * save a bean or a composite of beans to an XML file.
	 * 
	 * @param rootBean
	 *            the bean or root bean of the composite
	 */
	public void writeToWriter(final RapidBean rootBean, Writer osw, String encoding) throws IOException {
		osw.write("<?xml version=\"1.0\" encoding=\"" + encoding + "\" standalone=\"yes\"?>");
		osw.write(LF);
		this.write(osw, 0, null, rootBean);
	}

	/**
	 * save a bean or a composite of beans to an XML string.
	 * 
	 * @param rootBean
	 *            the bean or root bean of the composite
	 */
	public String toString(final RapidBean rootBean, String encoding) {
		StringWriter sw = new StringWriter();
		try {
			writeToWriter(rootBean, sw, encoding);
		} catch (IOException e) {
			// This should not happen as we are writing into memory
			throw new RapidBeansRuntimeException("Should not happen!", e);
		}
		return sw.toString();
	}

	/**
	 * the line feed character sequence.
	 */
	private static final String LF = PlatformHelper.getLineFeed();

	/**
	 * the bean processor.
	 * 
	 * @param osw
	 *            the OutputStreamWriter
	 * @param depth
	 *            the depth
	 * @param colProp
	 *            the parent collection property
	 * @param bean
	 *            the bean
	 */
	private void write(final Writer osw, final int depth, final PropertyCollection colProp, final RapidBean bean) {
		try {
			PlatformHelper.getLineFeed();
			String propname = null;
			String xmlelement = null;
			if (colProp != null) {
				propname = colProp.getType().getPropName();
				int maxmult = ((TypePropertyCollection) colProp.getType()).getMaxmult();
				if ((maxmult > 1 || maxmult < 0) && propname.endsWith("s")) {
					propname = propname.substring(0, propname.length() - 1);
				}
				xmlelement = colProp.getType().mapTypeToXmlElement(bean.getType());
			}
			if (depth == 0) {
				osw.write("<");
				if (bean.getType().getXmlRootElement() != null) {
					osw.write(bean.getType().getXmlRootElement());
				} else {
					osw.write("rb:bean xmlns:rb=\"http://rapidbeans.org/core/basic\"");
					osw.write(LF);
					osw.write("\trb:type=\"");
					osw.write(bean.getType().getName());
					osw.write("\"");
				}
				osw.write(LF);
			} else {
				for (int i = 0; i < depth; i++) {
					osw.write("\t");
				}
				osw.write("<");
				if (xmlelement != null) {
					osw.write(xmlelement);
				} else {
					osw.write(propname);
				}
				osw.write(LF);
			}

			if (bean.getType().getIdtype() != IdType.transientid) {
				for (int i = -1; i < depth; i++) {
					osw.write("\t");
				}
				osw.write("id=\"");
				osw.write(bean.getIdString());
				osw.write("\"");
				osw.write(LF);
			}

			final List<PropertyCollection> compColProperties = bean.getColPropertiesComposition();
			boolean closeImmediately = true;

			TypeProperty proptype;
			RapidBean colBean;
			Collection<?> col;
			if (colProp != null && ((TypePropertyCollection) colProp.getType()).getTargetType() != bean.getType()
					&& xmlelement == null) {
				for (int i = -1; i < depth; i++) {
					osw.write("\t");
				}
				osw.write("rb:type=\"");
				osw.write(bean.getType().getName());
				osw.write("\"");
				osw.write(LF);
			}
			for (Property prop : bean.getPropertyList()) {
				proptype = prop.getType();
				if (proptype.isTransient()) {
					continue;
				}
				if (prop.getValue() != null
						&& (!(proptype instanceof TypePropertyCollection) || (!((TypePropertyCollection) proptype)
								.isComposition()))) {
					switch (proptype.getXmlBindingType()) {
					case attribute:
						for (int i = -1; i < depth; i++) {
							osw.write("\t");
						}
						osw.write(prop.getType().getPropName());
						osw.write("=\"");
						osw.write(prop.toString());
						osw.write("\"");
						osw.write(LF);
						break;
					case element:
						closeImmediately = false;
						break;
					}
				}
			}
			for (int i = -1; i < depth; i++) {
				osw.write("\t");
			}
			if (closeImmediately) {
				for (PropertyCollection prop : compColProperties) {
					if (prop.getValue() != null && ((Collection<?>) prop.getValue()).size() > 0) {
						closeImmediately = false;
						break;
					}
				}
			}
			if (closeImmediately && depth > 0) {
				osw.write("/>");
				osw.write(LF);
			} else {
				osw.write(">");
				osw.write(LF);
				for (PropertyCollection prop : compColProperties) {
					col = (Collection<?>) prop.getValue();
					if (col == null) {
						continue;
					}
					for (Object o : (Collection<?>) prop.getValue()) {
						colBean = (RapidBean) o;
						this.write(osw, depth + 1, prop, colBean);
					}
				}
			}

			for (Property prop : bean.getPropertyList()) {
				proptype = prop.getType();
				if (proptype.isTransient()) {
					continue;
				}
				if (prop.getValue() != null
						&& (!(proptype instanceof TypePropertyCollection) || (!((TypePropertyCollection) proptype)
								.isComposition()))) {
					switch (proptype.getXmlBindingType()) {
					case element:
						for (int i = -1; i < depth; i++) {
							osw.write("\t");
						}
						osw.write("<");
						osw.write(prop.getType().getPropName());
						osw.write(">");
						osw.write(prop.toString());
						osw.write("</");
						osw.write(prop.getType().getPropName());
						osw.write(">");
						osw.write(LF);
						break;
					case attribute:
						break;
					}
				}
			}

			if (depth == 0) {
				osw.write("</");
				if (bean.getType().getXmlRootElement() != null) {
					osw.write(bean.getType().getXmlRootElement());
				} else {
					osw.write("rb:bean");
				}
				osw.write(">");
				osw.write(LF);
			} else {
				if (!closeImmediately) {
					for (int i = 0; i < depth; i++) {
						osw.write("\t");
					}
					osw.write("</");
					if (xmlelement != null) {
						osw.write(xmlelement);
					} else {
						osw.write(propname);
					}
					osw.write(">");
					osw.write(LF);
				}
			}

		} catch (IOException e) {
			throw new RapidBeansRuntimeException("writing document file failed", e);
		}
	}
}
