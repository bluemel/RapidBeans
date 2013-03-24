/*
 * Partially generated code file: ConfigToolbar.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans 
				bean
			 generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans 
				bean
			 type
 * org.rapidbeans.presentation.config.ConfigToolbar
 * 
 * model:    model/org/rapidbeans/presentation/config/ConfigToolbar.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.config;



// BEGIN manual code section
// ConfigToolbar.import
import org.rapidbeans.core.type.TypeRapidBean;

// END manual code section

/**
 * Rapid Bean class: ConfigToolbar.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class ConfigToolbar extends org.rapidbeans.presentation.config.ConfigMenuEntry {
	// BEGIN manual code section
	// ConfigToolbar.classBody
	// END manual code section

	/**
	 * property "name".
	 */
	private org.rapidbeans.core.basic.PropertyString name;

	/**
	 * property "classname".
	 */
	private org.rapidbeans.core.basic.PropertyString classname;

	/**
	 * property "buttons".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend buttons;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		super.initProperties();
		this.name = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("name");
		this.classname = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("classname");
		this.buttons = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("buttons");
	}

	/**
	 * default constructor.
	 */
	public ConfigToolbar() {
		super();
		// BEGIN manual code section
		// ConfigToolbar.ConfigToolbar()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public ConfigToolbar(final String s) {
		super(s);
		// BEGIN manual code section
		// ConfigToolbar.ConfigToolbar(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public ConfigToolbar(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// ConfigToolbar.ConfigToolbar(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(ConfigToolbar.class);

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
	public void setName(final String argValue) {
		this.name.setValue(argValue);
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
	public void setClassname(final String argValue) {
		this.classname.setValue(argValue);
	}

	/**
	 * @return value of Property 'buttons'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigToolbarButton> getButtons() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigToolbarButton>)
			this.buttons.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("buttons");
		}
	}

	/**
	 * setter for Property 'buttons'.
	 * @param argValue
	 *            value of Property 'buttons' to set
	 */
	public void setButtons(final java.util.Collection<org.rapidbeans.presentation.config.ConfigToolbarButton> argValue) {
		this.buttons.setValue(argValue);
	}
	/**
	 * add method for Property 'buttons'.
	 * @param bean
	 *            the bean to add
	 */
	public void addButton(final org.rapidbeans.presentation.config.ConfigToolbarButton bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.buttons).addLink(bean);
	}
	/**
	 * remove method for Property 'buttons'.
	 * @param bean
	 *            the bean to remove
	 */
	public void removeButton(final org.rapidbeans.presentation.config.ConfigToolbarButton bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.buttons).removeLink(bean);
	}
}
