/*
 * Partially generated code file: Menubar.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.Menubar
 * 
 * model:    model/org/rapidbeans/presentation/Menubar.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation;



// BEGIN manual code section
// Menubar.import
import java.util.ArrayList;
import java.util.Collection;

import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.config.ConfigMenubar;
import org.rapidbeans.presentation.config.ConfigSubmenu;
import org.rapidbeans.presentation.swing.MenubarSwing;

// END manual code section

/**
 * Rapid Bean class: Menubar.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class Menubar extends RapidBeanImplStrict {
	// BEGIN manual code section
	// Menubar.classBody
	/**
	 * create a Menubar of a special type out of a configuration.
	 * 
	 * @param client
	 *            the parent client
	 * @param menubarConfig
	 *            the configuration
	 * @param resourcePath
	 *            the resource path
	 * 
	 * @return the instance
	 */
	public static Menubar createInstance(final Application client,
			final ConfigMenubar menubarConfig, final String resourcePath) {
		Menubar menuBar = null;
		switch (client.getConfiguration().getGuitype()) {
		case swing:
			menuBar = new MenubarSwing(client, menubarConfig, resourcePath);
			break;
		case eclipsercp:
			//mainWindow = new BBMainWindowEclispercp();
			break;
		default:
			throw new RapidBeansRuntimeException("Unknown GUI type \""
					+ client.getConfiguration().getGuitype().name() + "\"");
		}

		return menuBar;
	}

	/**
	 * construct a Menubar.
	 * 
	 * @param client
	 *            the parent client
	 * @param menubarConfig
	 *            the configuration
	 * @param resourcePath
	 *            the resource path
	 */
	public Menubar(final Application client,
			final ConfigMenubar menubarConfig, final String resourcePath) {
		this.setName(menubarConfig.getName());
		Collection<Submenu> submenus = new ArrayList<Submenu>();
		for (ConfigSubmenu submenuConfig : menubarConfig.getMenus()) {
			if (client.userIsAuthorized(submenuConfig.getRolesrequired())) {
				submenus.add(Submenu.createInstance(submenuConfig, client,
						resourcePath + "." + this.getName()));
			}
		}
		this.setMenus(submenus);
	}

	/**
	 * Update any history views for the history given.
	 */
	public void updateHistoryViews() {
		for (Submenu menu : this.getMenus()) {
			for (final MenuEntry menuEntry : menu.getMenuentrys()) {
				if (menuEntry instanceof MenuHistoryOpenDocument) {
					((MenuHistoryOpenDocument) menuEntry).update();
				}
			}
		}
	}

	public MenuHistoryOpenDocument findFirstMenuHistoryOpenDocument() {
		for (Submenu menu : this.getMenus()) {
			for (final MenuEntry menuEntry : menu.getMenuentrys()) {
				if (menuEntry instanceof MenuHistoryOpenDocument) {
					return (MenuHistoryOpenDocument) menuEntry;
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
	public Menubar() {
		super();
		// BEGIN manual code section
		// Menubar.Menubar()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public Menubar(final String s) {
		super(s);
		// BEGIN manual code section
		// Menubar.Menubar(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public Menubar(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// Menubar.Menubar(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(Menubar.class);

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
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.Submenu> getMenus() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.Submenu>)
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
	public void setMenus(final java.util.Collection<org.rapidbeans.presentation.Submenu> argValue) {
		this.menus.setValue(argValue);
	}
	/**
	 * add method for Property 'menus'.
	 * @param bean the bean to add
	 */
	public void addMenu(final org.rapidbeans.presentation.Submenu bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.menus).addLink(bean);
	}
	/**
	 * remove method for Property 'menus'.
	 * @param bean the bean to add
	 */
	public void removeMenu(final org.rapidbeans.presentation.Submenu bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.menus).removeLink(bean);
	}
}
