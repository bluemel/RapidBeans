package org.rapidbeans.core.basic;

import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TypeRapidEnum;

import junit.framework.TestCase;

/**
 * Test generic enums.
 * 
 * @author Martin Bluemel
 */
public class GenericEnumTest extends TestCase {

	/**
	 * Test of createInstance(XmlNode).
	 * 
	 * This Method is intended to be used for Unit Tests. The bean's type is not
	 * registered at the bean type loader (RapidBeansTypeLoader).
	 */
	public void testCreateInstanceDescr() {
		try {
			String descr = "<enumtype name=\"Season\">" + "<enum name=\"spring\"/>" + "<enum name=\"summer\"/>"
					+ "<enum name=\"autumn\"/>" + "<enum name=\"winter\"/>" + "</enumtype>";
			TypeRapidEnum enumtype = TypeRapidEnum.createInstance(descr);
			assertEquals("Season", enumtype.getName());
			assertEquals("spring", enumtype.elementOf("spring").name());
		} finally {
			RapidBeansTypeLoader.getInstance().unregisterType("Season");
		}
	}
}
