/**
 * test class.
 */
package org.rapidbeans.presentation.swing;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.util.Locale;

import javax.swing.JTextField;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.GenericBean;
import org.rapidbeans.core.basic.PropertyDate;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanImplParent;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TestHelperTypeLoader;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.XmlNode;
import org.rapidbeans.presentation.TestClient;
import org.rapidbeans.presentation.config.ConfigApplication;

/**
 * @author Bluemel Martin
 */
public class EditorPropertyDateSwingTest extends TestCase {

	/**
	 * test the completion of german date input.
	 */
	public void testInputfieldCompletionGerman() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);
		EditorPropertyDateSwing proped = createTestPropEdDateGerman();
		assertCompletion("02.02.2002", "02.02.2002", proped, df);
		assertCompletion("2.2.2", "02.02.2002", proped, df);
		assertCompletion("2.2.200", "02.02.0200", proped, df);
		assertCompletion("31.1.19", "31.01.2019", proped, df);
		assertCompletion("28.2.20", "28.02.1920", proped, df);
	}

	/**
	 * Test HasPotentiallyValidInputField() for GERMAN locale.
	 */
	public void testTypingGerman() {
		EditorPropertyDateSwing proped = createTestPropEdDateGerman();
		JTextField tf = (JTextField) proped.getWidget();
		assertEquals(EditorPropertySwing.COLOR_NORMAL, tf.getBackground());
		assertValidTyping("", true, true, proped);
		assertValidTyping("1", true, false, proped);
		assertValidTyping("x", false, false, proped);
		assertValidTyping("11", true, false, proped);
		assertValidTyping("1x", false, false, proped);
		assertValidTyping("11.", true, false, proped);
		assertValidTyping("11x", false, false, proped);
		assertValidTyping("11.0", true, false, proped);
		assertValidTyping("11.x", false, false, proped);
		assertValidTyping("11.02", true, false, proped);
		assertValidTyping("11.02.", true, false, proped);
		assertValidTyping("11.02.0", true, true, proped);
		assertValidTyping("11.02.00", true, true, proped);
		assertValidTyping("11.02.000", true, true, proped);
		assertValidTyping("11.02.0000", true, true, proped);
		assertValidTyping("11.02.0001", true, true, proped);
		assertValidTyping("19.11.2010", true, true, proped);
		assertValidTyping("32.10.2006", false, false, proped);
		assertValidTyping("0.10.2006", false, false, proped);
		assertValidTyping("1.13.2006", false, false, proped);
		assertValidTyping("31.04.06", false, false, proped);
		assertValidTyping("1.1.06", true, true, proped);
		assertValidTyping("30.04.19999", false, false, proped);
		assertValidTyping("28.02.2000", true, true, proped);
		assertValidTyping("29.02.2000", true, true, proped);
		assertValidTyping("28.02.1999", true, true, proped);
		assertValidTyping("29.02.1999", false, false, proped);
		assertValidTyping("30.02.1999", false, false, proped);
		assertValidTyping("2", true, false, proped);
		assertValidTyping("29", true, false, proped);
		assertValidTyping("29.", true, false, proped);
		assertValidTyping("29.02", true, false, proped);
		assertValidTyping("29.02.2002", false, false, proped);
		assertValidTyping("29.02.2", false, false, proped);
		assertValidTyping("29.02.2020", true, true, proped);
		assertValidTyping("29.02.20", true, true, proped);
		assertValidTyping("29.02.0200", true, true, proped);
		assertValidTyping("29.02.200", true, true, proped);
		assertValidTyping("29.02.2000", true, true, proped);
	}

	/**
	 * Test HasPotentiallyValidInputField() for GERMAN locale.
	 */
	public void testHasPotentiallyValidInputFieldGerman() {
		EditorPropertyDateSwing proped = createTestPropEdDateGerman();
		ValidationException ex = new ValidationException("invalid.prop.date.string.local.incomplete", proped, "yyy");
		assertPotentialOkDate("", true, proped, ex);
		assertPotentialOkDate("1", true, proped, ex);
		assertPotentialOkDate("31", true, proped, ex);
		assertPotentialOkDate("0", true, proped, ex);
		assertPotentialOkDate("32", false, proped, ex);
		assertPotentialOkDate("-1", false, proped, ex);
		assertPotentialOkDate("A", false, proped, ex);
		assertPotentialOkDate("tt.ee.ss", false, proped, ex);

		assertPotentialOkDate("28.", true, proped, ex);
		assertPotentialOkDate("28,", false, proped, ex);
		assertPotentialOkDate("32.", false, proped, ex);

		assertPotentialOkDate("28.3", true, proped, ex);
		assertPotentialOkDate("28.03", true, proped, ex);
		assertPotentialOkDate("28.12", true, proped, ex);
		assertPotentialOkDate("28.0", true, proped, ex);
		assertPotentialOkDate("28.13", false, proped, ex);

		assertPotentialOkDate("28.1.", true, proped, ex);
		assertPotentialOkDate("28.02.", true, proped, ex);
		assertPotentialOkDate("28.1,", false, proped, ex);
		assertPotentialOkDate("32.12.", false, proped, ex);

		assertPotentialOkDate("28.1.1", true, proped, ex);
		assertPotentialOkDate("28.02.12", true, proped, ex);
		assertPotentialOkDate("28.02.200", true, proped, ex);
		assertPotentialOkDate("28.02.2006", true, proped, ex);
		assertPotentialOkDate("28.02.20067", false, proped, ex);
		assertPotentialOkDate("28.02.2006.", false, proped, ex);
		assertPotentialOkDate("28.1.,", false, proped, ex);
		assertPotentialOkDate("32.12.", false, proped, ex);
		assertPotentialOkDate("31.8", true, proped, ex);
		assertPotentialOkDate("30.2", false, proped, ex);
		assertPotentialOkDate("31.11.20", false, proped, ex);
	}

	/**
	 * test helper.
	 * 
	 * @param uncompleteDate
	 *            the uncomplete date
	 * @param completedDate
	 *            the completed date
	 * @param proped
	 *            the property editor
	 * @param df
	 *            the date formatter
	 */
	private void assertCompletion(final String uncompleteDate, final String completedDate,
			final EditorPropertyDateSwing proped, final DateFormat df) {
		JTextField tf = (JTextField) proped.getWidget();
		tf.setText(uncompleteDate);
		assertEquals(completedDate, df.format(proped.getInputFieldValue()));
	}

	/**
	 * test helper.
	 * 
	 * @param input
	 *            the input string to test
	 * @param shouldBePotentiallyValid
	 *            if valid or invalid color is expected
	 * @param shouldBeValid
	 *            if a validation exception is expected
	 * @param proped
	 *            the property date editor
	 */
	private void assertValidTyping(final String input, final boolean shouldBePotentiallyValid,
			final boolean shouldBeValid, final EditorPropertyDateSwing proped) {
		JTextField tf = (JTextField) proped.getWidget();
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
		assertEquals(shouldBeValid, isValid);
		assertEquals(color, tf.getBackground());
	}

	/**
	 * assertion.
	 * 
	 * @param s
	 *            the test string
	 * @param ok
	 *            if ok or not
	 * @param proped
	 *            the editor
	 * @param ex
	 *            the exception
	 */
	private void assertPotentialOkDate(final String s, final boolean ok, final EditorPropertyDateSwing proped,
			final ValidationException ex) {
		((JTextField) proped.getWidget()).setText(s);
		assertEquals(ok, proped.hasPotentiallyValidInputField(ex));
	}

	/**
	 * @return a test property editor for dates
	 */
	private static EditorPropertyDateSwing createTestPropEdDateGerman() {
		TestClient client = new TestClient();
		ConfigApplication config = new ConfigApplication();
		client.setConfiguration(config);
		client.getConfiguration().setRootpackage("org.rapidbeans");
		RapidBeansLocale locale = new RapidBeansLocale("de");
		locale.init(client);
		client.setCurrentLocale(locale);
		RapidBean testBean = createTestBean1();
		PropertyDate prop = (PropertyDate) testBean.getProperty("date");
		EditorPropertyDateSwing proped = new EditorPropertyDateSwing(client, null, prop, prop.clone(testBean));
		return proped;
	}

	/**
	 * create a generic test bean with one date property.
	 * 
	 * @return the test bean
	 */
	private static GenericBean createTestBean1() {
		if (RapidBeansTypeLoader.getInstance().lookupType("TestBean1") == null) {
			String descr = "<beantype name=\"TestBean1\">" + "<property name=\"date\" type=\"date\"/>" + "</beantype>";
			XmlNode xmlNode = XmlNode.getDocumentTopLevel(new ByteArrayInputStream(descr.getBytes()));
			new TypeRapidBean(null, xmlNode, null, true);
		}
		GenericBean bean = (GenericBean) RapidBeanImplParent.createInstance("TestBean1");
		return bean;
	}

	/**
	 * tear down.
	 */
	public void tearDown() {
		// TestHelper.tearDownTypeDef("TestBean1");
		TestHelperTypeLoader.clearBeanTypesGeneric();
	}
}
