/*
 * Rapid Beans Framework: TestHelper.java
 * 
 * Copyright Martin Bluemel, 2006
 * 
 * 11.10.2006
 */

package org.rapidbeans.test;

import org.rapidbeans.core.basic.GenericBean;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.XmlNode;

/**
 * @author Martin Bluemel
 */
public final class TestHelper {

	/**
	 * prevent construction.
	 */
	private TestHelper() {
	}

	/**
	 * create a generic bean.
	 * 
	 * @param xmlDescription
	 *            the XML type description
	 * @return the TestBean instance
	 */
	public static GenericBean createGenericBeanInstance(
			final String xmlDescription) {
		final XmlNode xmlNode = XmlNode.getDocumentTopLevel(xmlDescription);
		TypeRapidBean type = new TypeRapidBean(null, xmlNode, null, false);
		GenericBean newBean = new GenericBean(type);
		return newBean;
	}
}
