/**
 * test class.
 */
package org.rapidbeans.presentation.swing;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;

import javax.swing.JTextField;

import org.junit.After;
import org.junit.Test;
import org.rapidbeans.core.basic.GenericBean;
import org.rapidbeans.core.basic.PropertyQuantity;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanImplParent;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TestHelperTypeLoader;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.XmlNode;
import org.rapidbeans.domain.math.Length;
import org.rapidbeans.domain.math.UnitLength;
import org.rapidbeans.presentation.TestClient;
import org.rapidbeans.presentation.config.ConfigApplication;

import junit.framework.Assert;

/**
 * @author Bluemel Martin
 */
public class EditorPropertyQuantitySwingTest {

	/**
	 * Test HasPotentiallyValidInputField() for GERMAN locale.
	 */
	@Test
	public void testTyping() {
		EditorPropertyQuantitySwing proped = createTestPropEdQuantity();
		JTextField tf = proped.getWidgetTextField();
		Assert.assertEquals(EditorPropertySwing.COLOR_NORMAL, tf.getBackground());
		assertValidTyping("", true, true, proped);
		assertValidTyping("1", true, true, proped);
		assertValidTyping("a", false, false, proped);
	}

	/**
	 * Test HasPotentiallyValidInputField() for GERMAN locale.
	 */
	@Test
	public void testHasPotentiallyValidInputFieldGerman() {
		EditorPropertyQuantitySwing proped = createTestPropEdQuantity();
		ValidationException ex = new ValidationException("invalid.prop.quantity.string.local.incomplete", proped,
				"yyy");
		assertPotentialOkQuantity("", true, proped, ex);
		assertPotentialOkQuantity("1", true, proped, ex);
		assertPotentialOkQuantity("a", false, proped, ex);
	}

	/**
	 * test helper.
	 * 
	 * @param input                    the input string to test
	 * @param shouldBePotentiallyValid if valid or invalid color is expected
	 * @param shouldBeValid            if a validation exception is expected
	 * @param proped                   the property date editor
	 */
	private void assertValidTyping(final String input, final boolean shouldBePotentiallyValid,
			final boolean shouldBeValid, final EditorPropertyQuantitySwing proped) {
		JTextField tf = proped.getWidgetTextField();
		tf.setText(input);
		boolean isValid = true;
		try {
			proped.validateInputField();
		} catch (ValidationException e) {
			isValid = false;
		}
		Color color;
		if (shouldBePotentiallyValid) {
			color = EditorPropertySwing.COLOR_NORMAL;
		} else {
			color = EditorPropertySwing.COLOR_INVALID;
		}
		Assert.assertEquals(shouldBeValid, isValid);
		Assert.assertEquals(color, tf.getBackground());
	}

	/**
	 * assertion.
	 * 
	 * @param s      the test string
	 * @param ok     if ok or not
	 * @param proped the editor
	 * @param ex     the exception
	 */
	private void assertPotentialOkQuantity(final String s, final boolean ok, final EditorPropertyQuantitySwing proped,
			final ValidationException ex) {
		proped.getWidgetTextField().setText(s);
		Assert.assertEquals(ok, proped.hasPotentiallyValidInputField(ex));
	}

	/**
	 * @return a test property editor for dates
	 */
	private static EditorPropertyQuantitySwing createTestPropEdQuantity() {
		TestClient client = new TestClient();
		ConfigApplication config = new ConfigApplication();
		client.setConfiguration(config);
		client.getConfiguration().setRootpackage("org.rapidbeans");
		RapidBeansLocale locale = new RapidBeansLocale("de");
		locale.init(client);
		client.setCurrentLocale(locale);
		RapidBean testBean = createTestBean1();
		PropertyQuantity prop = (PropertyQuantity) testBean.getProperty("quant");
		prop.setValue(new Length(BigDecimal.ZERO, UnitLength.m));
		EditorPropertyQuantitySwing proped = new EditorPropertyQuantitySwing(client, null, prop, prop.clone(testBean));
		return proped;
	}

	/**
	 * create a generic test bean with one date property.
	 * 
	 * @return the test bean
	 */
	private static GenericBean createTestBean1() {
		if (RapidBeansTypeLoader.getInstance().lookupType("TestBean1") == null) {
			String descr = "<beantype name=\"TestBean1\">" + "<property name=\"quant\" type=\"quantity\""
					+ " quantity=\"org.rapidbeans.domain.math.Length\">" + "</property>" + "</beantype>";
			XmlNode xmlNode = XmlNode.getDocumentTopLevel(new ByteArrayInputStream(descr.getBytes()));
			new TypeRapidBean(null, xmlNode, null, true);
		}
		GenericBean bean = (GenericBean) RapidBeanImplParent.createInstance("TestBean1");
		return bean;
	}

	/**
	 * tear down.
	 */
	@After
	public void tearDown() {
		TestHelperTypeLoader.clearBeanTypesGeneric();
	}
}
