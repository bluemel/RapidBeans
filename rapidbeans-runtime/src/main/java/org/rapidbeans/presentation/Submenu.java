/*
 * Partially generated code file: Submenu.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans 
				bean
			 generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans 
				bean
			 type
 * org.rapidbeans.presentation.Submenu
 * 
 * model:    model/org/rapidbeans/presentation/Submenu.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation;

// BEGIN manual code section
// Submenu.import
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.config.ConfigMenuEntry;
import org.rapidbeans.presentation.config.ConfigSubmenu;
import org.rapidbeans.presentation.swing.SubmenuSwing;

// END manual code section

/**
 * Rapid Bean class: Submenu. Partially generated Java class !!!Do only edit
 * manually in marked sections!!!
 **/
public class Submenu extends org.rapidbeans.presentation.MenuEntry {
	// BEGIN manual code section
	// Submenu.classBody
	/**
	 * @return the widget.
	 */
	public Object getWidget() {
		return null;
	}

	/**
	 * create a Submenu of a special type out of a configuration.
	 * 
	 * @param client       the parent client
	 * @param config       the submenus configuration
	 * @param resourcePath the resource path
	 * 
	 * @return the created instance
	 */
	public static final Submenu createInstance(final ConfigSubmenu config, final Application client,
			final String resourcePath) {
		Submenu menu = null;
		switch (client.getConfiguration().getGuitype()) {
		case swing:
			menu = new SubmenuSwing(config, client, resourcePath);
			break;
		case eclipsercp:
			// mainWindow = new BBMainWindowEclispercp();
			break;
		default:
			throw new RapidBeansRuntimeException(
					"Unknown GUI type \"" + client.getConfiguration().getGuitype().name() + "\"");
		}

		return menu;
	}

	/**
	 * constructor.
	 * 
	 * @param client       the client
	 * @param menuConfig   the menu configuration
	 * @param resourcePath the resource path
	 */
	public Submenu(final Application client, final ConfigSubmenu menuConfig, final String resourcePath) {
		this.setName(menuConfig.getName());
		Collection<MenuEntry> menuEntries = new ArrayList<MenuEntry>();
		List<ConfigMenuEntry> menuEntryConfigs = (ReadonlyListCollection<ConfigMenuEntry>) menuConfig.getMenuentrys();
		if (menuEntryConfigs != null) {
			for (ConfigMenuEntry menuEntryConfig : menuEntryConfigs) {
				if (client.userIsAuthorized(menuEntryConfig.getRolesrequired())) {
					menuEntries.add(
							MenuEntry.createInstance(menuEntryConfig, client, resourcePath + "." + this.getName()));
				}
			}
		}
		this.setMenuentrys(menuEntries);
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
		this.menuentrys = (org.rapidbeans.core.basic.PropertyAssociationend) this.getProperty("menuentrys");
	}

	/**
	 * default constructor.
	 */
	public Submenu() {
		super();
		// BEGIN manual code section
		// Submenu.Submenu()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * 
	 * @param s the string
	 */
	public Submenu(final String s) {
		super(s);
		// BEGIN manual code section
		// Submenu.Submenu(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * 
	 * @param sa the string array
	 */
	public Submenu(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// Submenu.Submenu(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(Submenu.class);

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
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.MenuEntry> getMenuentrys() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.MenuEntry>) this.menuentrys
					.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("menuentrys");
		}
	}

	/**
	 * setter for Property 'menuentrys'.
	 * 
	 * @param argValue value of Property 'menuentrys' to set
	 */
	public void setMenuentrys(final java.util.Collection<org.rapidbeans.presentation.MenuEntry> argValue) {
		this.menuentrys.setValue(argValue);
	}

	/**
	 * add method for Property 'menuentrys'.
	 * 
	 * @param bean the bean to add
	 */
	public void addMenuentry(final org.rapidbeans.presentation.MenuEntry bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.menuentrys).addLink(bean);
	}

	/**
	 * remove method for Property 'menuentrys'.
	 * 
	 * @param bean the bean to remove
	 */
	public void removeMenuentry(final org.rapidbeans.presentation.MenuEntry bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.menuentrys).removeLink(bean);
	}
}
