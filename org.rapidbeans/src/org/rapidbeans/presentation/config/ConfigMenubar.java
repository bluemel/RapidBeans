/*
 * Partially generated code file: ConfigMenubar.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans 
				bean
			 generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans 
				bean
			 type
 * org.rapidbeans.presentation.config.ConfigMenubar
 * 
 * model:    model/org/rapidbeans/presentation/config/ConfigMenubar.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.config;



// BEGIN manual code section
// ConfigMenubar.import
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypeRapidBean;

// END manual code section

/**
 * Rapid Bean class: ConfigMenubar.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class ConfigMenubar extends RapidBeanImplStrict {
	// BEGIN manual code section
	// ConfigMenubar.classBody
	public ConfigMenuHistoryOpenDocument findFirstMenuHistoryOpenDocument() {
		if (this.getMenus() != null) {
			for (final ConfigSubmenu menu : this.getMenus()) {
				if (menu.getMenuentrys() != null) {
					for (final ConfigMenuEntry entry : menu.getMenuentrys()) {
						if (entry instanceof ConfigMenuHistoryOpenDocument) {
							return (ConfigMenuHistoryOpenDocument) entry;
						}
					}
				}
			}
		}
		return null;
	}

	// END manual code section

	/**
	 * property "name".
	 */
	private org.rapidbeans.core.basic.PropertyString name;

	/**
	 * property "menus".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend menus;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.name = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("name");
		this.menus = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("menus");
	}

	/**
	 * default constructor.
	 */
	public ConfigMenubar() {
		super();
		// BEGIN manual code section
		// ConfigMenubar.ConfigMenubar()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public ConfigMenubar(final String s) {
		super(s);
		// BEGIN manual code section
		// ConfigMenubar.ConfigMenubar(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public ConfigMenubar(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// ConfigMenubar.ConfigMenubar(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(ConfigMenubar.class);

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
	 * @return value of Property 'menus'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigSubmenu> getMenus() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigSubmenu>)
			this.menus.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("menus");
		}
	}

	/**
	 * setter for Property 'menus'.
	 * @param argValue
	 *            value of Property 'menus' to set
	 */
	public void setMenus(final java.util.Collection<org.rapidbeans.presentation.config.ConfigSubmenu> argValue) {
		this.menus.setValue(argValue);
	}
	/**
	 * add method for Property 'menus'.
	 * @param bean the bean to add
	 */
	public void addMenu(final org.rapidbeans.presentation.config.ConfigSubmenu bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.menus).addLink(bean);
	}
	/**
	 * remove method for Property 'menus'.
	 * @param bean the bean to add
	 */
	public void removeMenu(final org.rapidbeans.presentation.config.ConfigSubmenu bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.menus).removeLink(bean);
	}
}
