/*
 * Partially generated code file: RapidBeansLocale.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.core.common.RapidBeansLocale
 * 
 * model:    model/org/rapidbeans/core/common/RapidBeansLocale.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.core.common;



// BEGIN manual code section
// RapidBeansLocale.import
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.Application;

// END manual code section

/**
 * Rapid Bean class: RapidBeansLocale.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class RapidBeansLocale extends RapidBeanImplStrict {
	// BEGIN manual code section
	// RapidBeansLocale.classBody
	private static final Logger log = Logger.getLogger(
			RapidBeansLocale.class.getName());

	/**
	 * the associated Java Locale instance.
	 */
	private Locale locale = null;

	/**
	 * the localized gui texts of the framework.
	 */
	private ResourceBundle textsGuiFW = null;

	/**
	 * the localized gui texts of the application.
	 */
	private ResourceBundle textsGui = null;

	/**
	 * the localized message texts of the framework.
	 */
	private ResourceBundle textsMessagesFW = null;

	/**
	 * the localized message texts of the application.
	 */
	private ResourceBundle textsMessages = null;

	/**
	 * @return the associated Java Locale instance
	 */
	public final Locale getLocale() {
		return this.locale;
	}

	/**
	 * setter.
	 * 
	 * @param loc
	 *            the Locale to set
	 */
	public void setLocale(final Locale loc) {
		this.locale = loc;
	}

	/**
	 * return the localized string for a given resource key.
	 * 
	 * @param key
	 *            the resource key
	 * 
	 * @return the localized string
	 */
	public String getStringGui(final String key) {
		String s = null;
		log.fine("getStringGui: key = \"" + key + "\"");
		try {
			s = this.textsGui.getString(key);
		} catch (MissingResourceException e) {
			s = this.textsGuiFW.getString(key);
		}
		return s;
	}

	/**
	 * return the localized string for a given resource key.
	 * 
	 * @param key
	 *            the resource key
	 * 
	 * @return the localized string
	 */
	public String getStringMessage(final String key) {
		String s = null;
		try {
			s = this.textsMessages.getString(key);
		} catch (MissingResourceException e) {
			s = this.textsMessagesFW.getString(key);
		}
		return s;
	}

	/**
	 * return the localized string for a given resource key
	 * while inserting one string.
	 * 
	 * @param key
	 *            the resource key
	 * @param arg1
	 *            the string to insert
	 * 
	 * @return the localized string
	 */
	public String getStringMessage(final String key, final String arg1) {
		String s = null;
		try {
			s = this.textsMessages.getString(key);
		} catch (MissingResourceException e) {
			s = this.textsMessagesFW.getString(key);
		}
		final Object[] arguments = { arg1 };
		s = java.text.MessageFormat.format(s, arguments);
		return s;
	}

	/**
	 * return the localized string for a given resource key
	 * while inserting two strings.
	 * 
	 * @param key
	 *            the resource key
	 * @param arg1
	 *            the string to insert
	 * @param arg2
	 *            the string to insert
	 * 
	 * @return the localized string
	 */
	public final String getStringMessage(final String key, final String arg1,
			final String arg2) {
		String s = null;
		try {
			s = this.textsMessages.getString(key);
		} catch (MissingResourceException e) {
			s = this.textsMessagesFW.getString(key);
		}
		final Object[] arguments = { arg1, arg2 };
		s = java.text.MessageFormat.format(s, arguments);
		return s;
	}

	/**
	 * return the localized string for a given resource key
	 * while inserting three strings.
	 * 
	 * @param key
	 *            the resource key
	 * @param arg1
	 *            the string to insert
	 * @param arg2
	 *            the string to insert
	 * @param arg3
	 *            the string to insert
	 * 
	 * @return the localized string
	 */
	public final String getStringMessage(final String key, final String arg1,
			final String arg2, final String arg3) {
		String s = null;
		try {
			s = this.textsMessages.getString(key);
		} catch (MissingResourceException e) {
			s = this.textsMessagesFW.getString(key);
		}
		final Object[] arguments = { arg1, arg2, arg3 };
		s = java.text.MessageFormat.format(s, arguments);
		return s;
	}

	/**
	 * initializes the RapidBeansLocale instance.
	 * 
	 * @param client
	 *            the parent client
	 */
	public final void init(final Application client) {
		this.locale = new Locale(this.getName());
		this.textsGuiFW = this.initResourceBundle("org.rapidbeans", "gui");
		this.textsGui = this.initResourceBundle(client, "gui");
		this.textsMessagesFW = this.initResourceBundle("org.rapidbeans", "msg");
		this.textsMessages = this.initResourceBundle(client, "msg");
	}

	/**
	 * initializes the RapidBeansLocale instance.
	 * 
	 * @param rootPackage
	 *            the root package name
	 */
	public final void init(final String rootPackage) {
		this.locale = new Locale(this.getName());
		this.textsGuiFW = this.initResourceBundle("org.rapidbeans", "gui");
		this.textsGui = this.initResourceBundle(rootPackage, "gui");
		this.textsMessagesFW = this.initResourceBundle("org.rapidbeans", "msg");
		this.textsMessages = this.initResourceBundle(rootPackage, "msg");
	}

	/**
	 * initialization of one resource bundle.
	 * 
	 * @param s
	 *            one extension
	 * @param textsName
	 *            another extension
	 * 
	 * @return the resource bundle
	 */
	private ResourceBundle initResourceBundle(final String s, final String textsName) {
		String baseName = s + ".lang." + this.getName() + "." + textsName;
		ResourceBundle bundle = null;
		try {
			bundle = ResourceBundle.getBundle(baseName, this.locale);
		} catch (MissingResourceException e) {
			bundle = new ResourceBundle() {
				public Enumeration<String> getKeys() {
					return null;
				}

				protected Object handleGetObject(String key) {
					return null;
				}

				public String toString() {
					return null;
				}
			};
		}
		return bundle;
	}

	/**
	 * initialization of one resource bundle.
	 * 
	 * @param client
	 *            the parent client
	 * @param textsName
	 *            yyy
	 * 
	 * @return zzz
	 */
	private ResourceBundle initResourceBundle(final Application client, final String textsName) {
		return this.initResourceBundle(client.getRootpackage(), textsName);
	}

	// END manual code section

	/**
	 * property "name".
	 */
	private org.rapidbeans.core.basic.PropertyString name;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.name = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("name");
	}

	/**
	 * default constructor.
	 */
	public RapidBeansLocale() {
		super();
		// BEGIN manual code section
		// RapidBeansLocale.RapidBeansLocale()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public RapidBeansLocale(final String s) {
		super(s);
		// BEGIN manual code section
		// RapidBeansLocale.RapidBeansLocale(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public RapidBeansLocale(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// RapidBeansLocale.RapidBeansLocale(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(RapidBeansLocale.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}

	/**
	 * @return value of Property 'name'
	 */
	public String getName() {
		try {
			return (String) this.name.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("name");
		}
	}

	/**
	 * setter for Property 'name'.
	 * @param argValue
	 *            value of Property 'name' to set
	 */
	public void setName(
		final String argValue) {
		this.name.setValue(argValue);
	}
}
