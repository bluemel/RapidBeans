/*
 * Rapid Beans Framework: EditorPropertyTextSwingTest.java
 *
 * Copyright Martin Bluemel, 2008
 *
 * 12.09.2008
 */

package org.rapidbeans.presentation.swing;

import java.io.ByteArrayInputStream;

import javax.swing.JTextField;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.GenericBean;
import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.exception.ValidationMandatoryException;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TestHelperTypeLoader;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.XmlNode;
import org.rapidbeans.presentation.TestClient;
import org.rapidbeans.presentation.config.ConfigApplication;

/**
 * @author Bluemel Martin
 */
public class EditorPropertyTextSwingTest extends TestCase {

	/**
	 * The default behavior is that empty strings are invalid
	 */
    public void testStringGetInputFieldValueEmptyDefault() {
    	EditorPropertyTextSwing proped = createTestPropEdText(new String[0]);
        JTextField tf = (JTextField) proped.getWidget();

        // just to reassure the PropertyString behavior
        Property prop = proped.getProperty();
        assertNull(prop.getValue());
        prop.validate();
        // per default it is not mandatory (you may set it to null)
        prop.setValue(null);
        // but you are not allowed to set it to an empty string
        try {
            prop.setValue("");
            fail();
        } catch (ValidationException e) {
        	assertEquals("invalid.prop.string.empty", e.getSignature());
        }

        // initially the input field value is valid and null
        proped.validateInputField();
        assertNull(proped.getInputFieldValue());
        assertFalse(proped.isInputFieldChanged());

        // when you start writing characters the input field value is valid and defined
        tf.setText("a");
        proped.validateInputField();
        assertEquals("a", proped.getInputFieldValue());
        assertTrue(proped.isInputFieldChanged());

        // when you delete these characters again the input field value is valid and null
        // again since empty is invalid anyway
        tf.setText("");
        proped.validateInputField();
        assertNull(proped.getInputFieldValue());
        assertFalse(proped.isInputFieldChanged());
    }

	/**
	 * The default behavior is that empty strings are invalid
	 * Mandatory means that they may not be null.
	 */
    public void testStringGetInputFieldValueEmptyDefaultManadatory() {
    	EditorPropertyTextSwing proped = createTestPropEdText(
    			new String[]{"mandatory", "true"});
        JTextField tf = (JTextField) proped.getWidget();

        // just to reassure the PropertyString behavior
        Property prop = proped.getProperty();
        assertNull(prop.getValue());
        // since it is mandatory it initially has an invalid value
        try {
            prop.validate();
            fail();
        } catch (ValidationMandatoryException e) {
        	assertEquals("invalid.prop.mandatory", e.getSignature());
        }
        // since it is mandatory you must not set it to null
        try {
            prop.setValue(null);
            fail();
        } catch (ValidationMandatoryException e) {
        	assertEquals("invalid.prop.mandatory", e.getSignature());
        }
        // in addition are not allowed to set it to an empty string
        try {
            prop.setValue("");
            fail();
        } catch (ValidationException e) {
        	assertEquals("invalid.prop.string.empty", e.getSignature());
        }

        // initially the input field value is valid and null
        assertNull(proped.getInputFieldValue());
        // it is not really changed but the user should change it
        assertFalse(proped.isInputFieldChanged());

        // when you start writing characters the input field value is valid and defined
        tf.setText("a");
        proped.validateInputField();
        assertEquals("a", proped.getInputFieldValue());
        assertTrue(proped.isInputFieldChanged());

        // when you delete these characters again the input field value is valid and null
        // again since empty is invalid anyway
        tf.setText("");
        assertNull(proped.getInputFieldValue());
        // but this is invalid since the property is mandatory
        try {
            proped.validateInputField();
            fail();
        } catch (ValidationException e) {
        	assertEquals("invalid.prop.mandatory", e.getSignature());
        }
        assertFalse(proped.isInputFieldChanged());
    }

    /**
     * same as default behavior.
     */
    public void testStringGetInputFieldValueEmptyInvalid() {
    	EditorPropertyTextSwing proped = createTestPropEdText(
    			new String[]{"emptyvalid", "false"});
        JTextField tf = (JTextField) proped.getWidget();

        // just to reassure the PropertyString behavior
        Property prop = proped.getProperty();
        // per default it is not mandatory (you may set it to null)
        prop.setValue(null);
        // but you are not allowed to set it to an empty string
        try {
            prop.setValue("");
            fail();
        } catch (ValidationException e) {
        	assertEquals("invalid.prop.string.empty", e.getSignature());
        }

        // initially the input field value is valid and null
        proped.validateInputField();
        assertNull(proped.getInputFieldValue());

        // when you start writing characters the input field value is valid and defined
        tf.setText("a");
        proped.validateInputField();
        assertEquals("a", proped.getInputFieldValue());

        // when you delete these characters again the input field value is valid and null
        // again since empty is invalid anyway
        tf.setText("");
        proped.validateInputField();
        assertNull(proped.getInputFieldValue());
    }

	/**
	 * same as default mandatory behavior.
	 */
    public void testStringGetInputFieldValueEmptyInvalidManadatory() {
    	EditorPropertyTextSwing proped = createTestPropEdText(
    			new String[]{"mandatory", "true", "emptyvalid", "false"});
        JTextField tf = (JTextField) proped.getWidget();

        // just to reassure the PropertyString behavior
        Property prop = proped.getProperty();
        // since it is mandatory you must not set it to null
        try {
            prop.setValue(null);
            fail();
        } catch (ValidationMandatoryException e) {
        	assertEquals("invalid.prop.mandatory", e.getSignature());
        }
        // in addition are not allowed to set it to an empty string
        try {
            prop.setValue("");
            fail();
        } catch (ValidationException e) {
        	assertEquals("invalid.prop.string.empty", e.getSignature());
        }

        // initially the input field value is valid and null
        assertNull(proped.getInputFieldValue());

        // when you start writing characters the input field value is valid and defined
        tf.setText("a");
        proped.validateInputField();
        assertEquals("a", proped.getInputFieldValue());

        // when you delete these characters again the input field value is valid and null
        // again since empty is invalid anyway
        tf.setText("");
        assertNull(proped.getInputFieldValue());
        // but this is invalid since the property is mandatory
        try {
            proped.validateInputField();
            fail();
        } catch (ValidationException e) {
        	assertEquals("invalid.prop.mandatory", e.getSignature());
        }
    }

    /**
     * If empty is valid the input field always returns an empty string.
     * That automatically results in the fact that the editor initially
     * has a changed input field. If you want to avoid that you simply
     * set the property's default value to an empty string (see next test).
     */
    public void testStringGetInputFieldValueEmptyValid() {
    	EditorPropertyTextSwing proped = createTestPropEdText(
    			new String[]{"emptyvalid", "true"});
        JTextField tf = (JTextField) proped.getWidget();

        // just to reassure the PropertyString behavior
        Property prop = proped.getProperty();
        // per default it is not mandatory (you may set it to null)
        prop.setValue(null);
        // but you are allowed to set it to an empty string
        prop.setValue("");

        // initially the input field value is valid and empty
        proped.validateInputField();
        assertEquals("", proped.getInputFieldValue());
        assertTrue(proped.isInputFieldChanged());

        // when you start writing characters the input field value is valid and defined
        tf.setText("a");
        proped.validateInputField();
        assertEquals("a", proped.getInputFieldValue());
        assertTrue(proped.isInputFieldChanged());

        // when you delete these characters again the input field value is valid
        // and empty again
        tf.setText("");
        proped.validateInputField();
        assertEquals("", proped.getInputFieldValue());
        assertTrue(proped.isInputFieldChanged());
    }

    /**
     * If empty is valid the input field always returns an empty string.
     * Setting the property's default value to an empty string helps
     * avoiding undefined values and having a changed input field
     * value initially.
     */
    public void testStringGetInputFieldValueEmptyValidDefaultvalueEmpty() {
    	EditorPropertyTextSwing proped = createTestPropEdText(
    			new String[]{"default", "", "emptyvalid", "true"});
        JTextField tf = (JTextField) proped.getWidget();

        // just to reassure the PropertyString behavior
        Property prop = proped.getProperty();
        assertEquals("", prop.getValue());
        prop.validate();
        // per default it is not mandatory (you may set it to null)
        prop.setValue(null);
        // but you are also allowed to set it to an empty string again
        prop.setValue("");

        // initially the input field value is valid and empty
        proped.validateInputField();
        assertEquals("", proped.getInputFieldValue());
        assertFalse(proped.isInputFieldChanged());

        // when you start writing characters the input field value is valid and defined
        tf.setText("a");
        proped.validateInputField();
        assertEquals("a", proped.getInputFieldValue());
        assertTrue(proped.isInputFieldChanged());

        // when you delete these characters again the input field value is valid
        // and empty again
        tf.setText("");
        proped.validateInputField();
        assertEquals("", proped.getInputFieldValue());
        assertFalse(proped.isInputFieldChanged());
    }

    /**
     * If empty is valid the input field always returns an empty string.
     */
    public void testStringGetInputFieldValueEmptyValidMandatory() {
    	EditorPropertyTextSwing proped = createTestPropEdText(
    			new String[]{"mandatory", "true", "emptyvalid", "true"});
        JTextField tf = (JTextField) proped.getWidget();

        // just to reassure the PropertyString behavior
        Property prop = proped.getProperty();
        assertNull(prop.getValue());
        try {
        	prop.validate();
        	fail();
        } catch (ValidationMandatoryException e) {
            assertEquals("invalid.prop.mandatory", e.getSignature());
        }
        // since it is mandatory you must not set it to null)
        try {
            prop.setValue(null);
        	fail();
        } catch (ValidationMandatoryException e) {
            assertEquals("invalid.prop.mandatory", e.getSignature());
        }
        // but you are allowed to set it to an empty string
        prop.setValue("");

        // initially the input field value is valid and empty
        // please note that in this case the input field is initially
        // changed if you want to avoid that it would make sense
        // to set the property's default value to an empty string.
        assertTrue(proped.isInputFieldChanged());
        proped.validateInputField();
        assertEquals("", proped.getInputFieldValue());

        // when you start writing characters the input field value is valid and defined
        assertTrue(proped.isInputFieldChanged());
        tf.setText("a");
        proped.validateInputField();
        assertEquals("a", proped.getInputFieldValue());

        // when you delete these characters again the input field value is valid
        // and empty again
        assertTrue(proped.isInputFieldChanged());
        tf.setText("");
        proped.validateInputField();
        assertEquals("", proped.getInputFieldValue());
    }

    /**
     * These configurations go together well.
     * You have valid values from the start.
     */
    public void testStringGetInputFieldValueEmptyValidManadtoryDefaultvalueEmpty() {
    	EditorPropertyTextSwing proped = createTestPropEdText(
    			new String[]{"mandatory", "true", "default", "", "emptyvalid", "true"});
        JTextField tf = (JTextField) proped.getWidget();

        // just to reassure the PropertyString behavior
        Property prop = proped.getProperty();
        assertEquals("", prop.getValue());
        prop.validate();
        // since it is mandatory you must not set it to null)
        try {
            prop.setValue(null);
        	fail();
        } catch (ValidationMandatoryException e) {
            assertEquals("invalid.prop.mandatory", e.getSignature());
        }
        // but you are allowed to set it to an empty string again
        prop.setValue("");

        // initially the input field value is valid and empty
        proped.validateInputField();
        assertEquals("", proped.getInputFieldValue());
        assertFalse(proped.isInputFieldChanged());

        // when you start writing characters the input field value is valid and defined
        tf.setText("a");
        proped.validateInputField();
        assertEquals("a", proped.getInputFieldValue());
        assertTrue(proped.isInputFieldChanged());

        // when you delete these characters again the input field value is valid
        // and empty again
        tf.setText("");
        proped.validateInputField();
        assertEquals("", proped.getInputFieldValue());
        assertFalse(proped.isInputFieldChanged());
    }

    public void testStringInvalidPattern() {
        EditorPropertyTextSwing proped = createTestPropEdText(
        		new String[]{"pattern", "[0-9]*"});
        JTextField tf = (JTextField) proped.getWidget();
        tf.setText("1234x");
        try {
            proped.validateInputField();
            fail();
        } catch (ValidationException e) {
        	assertEquals("invalid.prop.string.pattern", e.getSignature());
        	assertEquals(2, e.getMessageArgs().length);
        	assertEquals("1234x", e.getMessageArgs()[0]);
        	assertEquals("[0-9]*", e.getMessageArgs()[1]);
        }
    }

    public void testStringInvalidMaxlen() {
        EditorPropertyTextSwing proped = createTestPropEdText(
        		new String[]{"maxlen", "10"});
        JTextField tf = (JTextField) proped.getWidget();
        tf.setText("12345678901");
        try {
            proped.validateInputField();
            fail();
        } catch (ValidationException e) {
        	assertEquals("invalid.prop.string.maxlen", e.getSignature());
        	assertEquals(2, e.getMessageArgs().length);
        	assertEquals("12345678901", e.getMessageArgs()[0]);
        	assertEquals("10", e.getMessageArgs()[1]);
        }
    }

    public void testStringInvalidMinlen() {
        EditorPropertyTextSwing proped = createTestPropEdText(
        		new String[]{"minlen", "2"});
        JTextField tf = (JTextField) proped.getWidget();
        tf.setText("1");
        try {
            proped.validateInputField();
            fail();
        } catch (ValidationException e) {
        	assertEquals("invalid.prop.string.minlen", e.getSignature());
        	assertEquals(2, e.getMessageArgs().length);
        	assertEquals("1", e.getMessageArgs()[0]);
        	assertEquals("2", e.getMessageArgs()[1]);
        }
    }

    /**
     * @return a test property editor with a simple text field
     */
    private static EditorPropertyTextSwing createTestPropEdText(
    		final String[] testBeanConstraints) {
        TestClient client = new TestClient();
        ConfigApplication config = new ConfigApplication();
        client.setConfiguration(config);
        client.getConfiguration().setRootpackage("org.rapidbeans");
        RapidBeansLocale locale = new RapidBeansLocale("de");
        locale.init(client);
        client.setCurrentLocale(locale);
        RapidBean testBean = createTestBean1(testBeanConstraints);
        Property prop = testBean.getProperty("textnumber");
        EditorPropertyTextSwing proped =
            new EditorPropertyTextSwing(client, null, prop, prop.clone(testBean));
        return proped;
    }

    /**
     * create a generic test bean with one date property.
     *
     * @return the test bean
     */
    private static GenericBean createTestBean1(final String[] constraints) {
        if (RapidBeansTypeLoader.getInstance().lookupType("TestBean1") == null) {
            final StringBuffer descr = new StringBuffer();
            descr.append("<beantype name=\"TestBean1\">");
            descr.append("<property name=\"textnumber\" type=\"string\"");
            for (int i = 0; i < constraints.length; i++) {
            	descr.append(' ');
            	descr.append(constraints[i++]);
            	descr.append("=\"");
            	descr.append(constraints[i]);
            	descr.append('"');
            }
            descr.append("/>");
            descr.append("</beantype>");
            final XmlNode xmlNode = XmlNode.getDocumentTopLevel(
                    new ByteArrayInputStream(descr.toString().getBytes()));
            new TypeRapidBean(null, xmlNode, null, true);
        }
        GenericBean bean =
            (GenericBean) RapidBean.createInstance("TestBean1");
        return bean;
    }

    /**
     * tear down.
     */
    public void tearDown() {
        TestHelperTypeLoader.clearBeanTypesGeneric();
    }
}
