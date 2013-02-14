package org.rapidbeans.core.basic;

import junit.framework.TestCase;

import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TestHelperTypeLoader;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.type.TypeRapidEnum;
import org.rapidbeans.core.type.TypeRapidQuantity;
import org.rapidbeans.core.util.XmlNode;

/**
 * Test generic quantities.
 * 
 * @author Martin Bluemel
 */
public class GenericQuantityTest extends TestCase {

	/**
	 * Test creating a type instance of a generic quantity (with a
	 * generic enum type).
	 */
	public void testCreateInstanceDescrGeneric() {
		try {
			String descr = "<enumtype name=\"UnitVolt\">"
					+ "<enum name=\"kV\"/>"
					+ "<enum name=\"V\"/>"
					+ "<enum name=\"mV\"/>"
					+ "</enumtype>";
			TypeRapidEnum enumtype = TypeRapidEnum.createInstance(descr);
			assertEquals("UnitVolt", enumtype.getName());
			assertEquals("V", enumtype.elementOf("V").name());

			descr = "<quantitytype name=\"Voltage\""
					+ " unitenum=\"UnitVolt\">"
					+ "<unit name=\"kV\" factor=\"1E3\"/>"
					+ "<unit name=\"V\" factor=\"1\"/>"
					+ "<unit name=\"mV\" factor=\"1E-3\"/>"
					+ "</quantitytype>";
			TypeRapidQuantity.createInstance(descr);
			RapidQuantity.createInstance("Voltage", "1.5 V");
		} finally {
			TestHelperTypeLoader.clearBeanTypesGeneric();
			assertNull(RapidBeansTypeLoader.getInstance().lookupType("Voltage"));
			assertNull(RapidBeansTypeLoader.getInstance().lookupType("UnitVolt"));
		}
	}

	/**
	 * Test creating a type instance of a generic bean with a
	 * generic quantity attribute (with a generic enum type).
	 */
	public void testCreateInstanceDescrGenericBeanWithQuant() {
		try {
			String descr = "<enumtype name=\"UnitVolt\">"
					+ "<enum name=\"kV\"/>"
					+ "<enum name=\"V\"/>"
					+ "<enum name=\"mV\"/>"
					+ "</enumtype>";
			TypeRapidEnum enumtype = TypeRapidEnum.createInstance(descr);
			assertEquals("UnitVolt", enumtype.getName());
			assertEquals("V", enumtype.elementOf("V").name());

			descr = "<quantitytype name=\"Voltage\""
					+ " unitenum=\"UnitVolt\">"
					+ "<unit name=\"kV\" factor=\"1E3\"/>"
					+ "<unit name=\"V\" factor=\"1\"/>"
					+ "<unit name=\"mV\" factor=\"1E-3\"/>"
					+ "</quantitytype>";
			TypeRapidQuantity quantitytype = TypeRapidQuantity.createInstance(descr);
			assertEquals("Voltage", quantitytype.getName());

			descr = "<beantype name=\"TestBean\">"
					+ "<property name=\"name\" default=\"xxx\"/>"
					+ "<property name=\"tension\" type=\"quantity\" quantity=\"Voltage\" default=\"100000 V\"/>"
					+ "</beantype>";
			TypeRapidBean beantype = new TypeRapidBean(null,
					XmlNode.getDocumentTopLevel(descr), null, true);
			assertEquals("TestBean", beantype.getName());

			RapidBean bean = RapidBeanImplStrict.createInstance(beantype);
			PropertyString propName = (PropertyString) bean.getProperty("name");
			assertEquals("xxx", propName.getValue());
			PropertyQuantity propTension = (PropertyQuantity) bean.getProperty("tension");
			assertEquals(GenericQuantity.createInstance("Voltage", "100000 V"), propTension.getValue());
		} finally {
			TestHelperTypeLoader.clearBeanTypesGeneric();
			assertNull(RapidBeansTypeLoader.getInstance().lookupType("Voltage"));
			assertNull(RapidBeansTypeLoader.getInstance().lookupType("UnitVolt"));
		}
	}
}
