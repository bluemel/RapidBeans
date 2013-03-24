/*
 * Partially generated code file: ConfigSubmenu.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans 
				bean
			 generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans 
				bean
			 type
 * org.rapidbeans.presentation.config.ConfigSubmenu
 * 
 * model:    model/org/rapidbeans/presentation/config/ConfigSubmenu.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.config;



// BEGIN manual code section
// ConfigSubmenu.import
import org.rapidbeans.core.type.TypeRapidBean;

// END manual code section

/**
 * Rapid Bean class: ConfigSubmenu.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class ConfigSubmenu extends org.rapidbeans.presentation.config.ConfigMenuEntry {
	// BEGIN manual code section
	// ConfigSubmenu.classBody
	/**
	 * Convenience to add a menu separator configuration
	 */
	public void addSeparator() {
		this.addMenuentry(new ConfigMenuSeparator());
	}

	// END manual code section

	/**
	 * property "menuentrys".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend menuentrys;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		super.initProperties();
		this.menuentrys = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("menuentrys");
	}

	/**
	 * default constructor.
	 */
	public ConfigSubmenu() {
		super();
		// BEGIN manual code section
		// ConfigSubmenu.ConfigSubmenu()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public ConfigSubmenu(final String s) {
		super(s);
		// BEGIN manual code section
		// ConfigSubmenu.ConfigSubmenu(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public ConfigSubmenu(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// ConfigSubmenu.ConfigSubmenu(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(ConfigSubmenu.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}

	/**
	 * @return value of Property 'menuentrys'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigMenuEntry> getMenuentrys() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigMenuEntry>)
			this.menuentrys.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("menuentrys");
		}
	}

	/**
	 * setter for Property 'menuentrys'.
	 * @param argValue
	 *            value of Property 'menuentrys' to set
	 */
	public void setMenuentrys(final java.util.Collection<org.rapidbeans.presentation.config.ConfigMenuEntry> argValue) {
		this.menuentrys.setValue(argValue);
	}
	/**
	 * add method for Property 'menuentrys'.
	 * @param bean
	 *            the bean to add
	 */
	public void addMenuentry(final org.rapidbeans.presentation.config.ConfigMenuEntry bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.menuentrys).addLink(bean);
	}
	/**
	 * remove method for Property 'menuentrys'.
	 * @param bean
	 *            the bean to remove
	 */
	public void removeMenuentry(final org.rapidbeans.presentation.config.ConfigMenuEntry bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.menuentrys).removeLink(bean);
	}
}
