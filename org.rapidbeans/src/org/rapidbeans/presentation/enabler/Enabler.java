/*
 * Partially generated code file: Enabler.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.enabler.Enabler
 * 
 * model:    model/org/rapidbeans/presentation/enabler/Enabler.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.enabler;



// BEGIN manual code section
// Enabler.import
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.Application;

// END manual code section

/**
 * Rapid Bean class: Enabler.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class Enabler extends RapidBeanImplStrict {
	// BEGIN manual code section
	// Enabler.classBody
	/**
	 * the client.
	 */
	private Application client = null;

	/**
	 * @return the client
	 */
	protected Application getClient() {
		return this.client;
	}

	/**
	 * @param cl
	 *            the client
	 */
	protected void setClient(final Application cl) {
		this.client = cl;
	}

	/**
	 * the execute method of every Action.
	 * 
	 * @return if the menu is enable or not.
	 */
	public boolean getEnabled() {
		throw new RapidBeansRuntimeException("Enabler \""
				+ this.getClassname() + "\" not yet implemented");
	}

	/**
	 * default constructor parameter types.
	 */
	private static final Class<?>[] CONSTRUCTOR_PARAM_TYPES = {};

	/**
	 * the default constructor arguments.
	 */
	private static final Object[] CONSTRUCTOR_ARGS = {};

	/**
	 * creates an instance out of an enabler configuration.
	 * 
	 * @param cl
	 *            the client
	 * @return the new Enabler instance
	 */
	public final Enabler createInstance(final Application cl) {
		Enabler enabler = null;
		// construct enabler via reflection
		try {
			final Class<?> enablerClass = Class.forName(this.getClassname());
			Constructor<?> constructor =
					enablerClass.getConstructor(CONSTRUCTOR_PARAM_TYPES);
			enabler = (Enabler) constructor.newInstance(CONSTRUCTOR_ARGS);
			enabler.setClient(cl);
		} catch (ClassNotFoundException e) {
			throw new RapidBeansRuntimeException("Enabler class not found: " + this.getClassname(), e);
		} catch (NoSuchMethodException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		} catch (InstantiationException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		}
		enabler.setClassname(this.getClassname());
		return enabler;
	}

	// END manual code section

	/**
	 * property "classname".
	 */
	private org.rapidbeans.core.basic.PropertyString classname;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.classname = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("classname");
	}

	/**
	 * default constructor.
	 */
	public Enabler() {
		super();
		// BEGIN manual code section
		// Enabler.Enabler()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public Enabler(final String s) {
		super(s);
		// BEGIN manual code section
		// Enabler.Enabler(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public Enabler(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// Enabler.Enabler(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(Enabler.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}

	/**
	 * @return value of Property 'classname'
	 */
	public String getClassname() {
		try {
			return (String) this.classname.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("classname");
		}
	}

	/**
	 * setter for Property 'classname'.
	 * @param argValue
	 *            value of Property 'classname' to set
	 */
	public void setClassname(
		final String argValue) {
		this.classname.setValue(argValue);
	}
}
