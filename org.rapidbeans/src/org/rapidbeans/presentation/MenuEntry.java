/*
 * Partially generated code file: MenuEntry.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans 
				bean
			 generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans 
				bean
			 type
 * org.rapidbeans.presentation.MenuEntry
 * 
 * model:    model/org/rapidbeans/presentation/MenuEntry.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation;



// BEGIN manual code section
// MenuEntry.import
import java.util.MissingResourceException;

import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.config.ConfigMenuEntry;
import org.rapidbeans.presentation.config.ConfigMenuHistoryOpenDocument;
import org.rapidbeans.presentation.config.ConfigMenuItem;
import org.rapidbeans.presentation.config.ConfigMenuSeparator;
import org.rapidbeans.presentation.config.ConfigMenuToolbars;
import org.rapidbeans.presentation.config.ConfigSubmenu;

// END manual code section

/**
 * Rapid Bean class: MenuEntry.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public abstract class MenuEntry extends RapidBeanImplStrict {
	// BEGIN manual code section
	// MenuEntry.classBody
	/**
	 * This implementation avoids some syntax errors that would occur using an
	 * abstract method here.
	 * 
	 * @return the widget.
	 */
	public Object getWidget() {
		return null;
	}

	/**
	 * create a MenuEntry of a special type out of a configuration which is the
	 * this object itself.
	 * 
	 * @param client
	 *            the parent client
	 * @param resourcePath
	 *            the resource path
	 * 
	 * @return the instance
	 */
	public static MenuEntry createInstance(final ConfigMenuEntry cfg, final Application client,
			final String resourcePath) {
		MenuEntry newEntry = null;
		if (cfg instanceof ConfigSubmenu) {
			newEntry = Submenu.createInstance((ConfigSubmenu) cfg, client, resourcePath);
		} else if (cfg instanceof ConfigMenuItem) {
			newEntry = MenuItem.createInstance((ConfigMenuItem) cfg, client, resourcePath);
		} else if (cfg instanceof ConfigMenuSeparator) {
			newEntry = MenuSeparator.createInstance((ConfigMenuSeparator) cfg, client, resourcePath);
		} else if (cfg instanceof ConfigMenuHistoryOpenDocument) {
			newEntry = MenuHistoryOpenDocument
					.createInstance((ConfigMenuHistoryOpenDocument) cfg, client, resourcePath);
		} else if (cfg instanceof ConfigMenuToolbars) {
			newEntry = MenuToolbars.createInstance((ConfigMenuToolbars) cfg, client, resourcePath);
		} else {
			throw new RapidBeansRuntimeException("unexpected config object class: " + cfg.getClass().getName());
		}
		return newEntry;
	}

	public String getMenuText(final Application app, final String resourcePath) {
		String menuText = null;
		final RapidBeansLocale locale = app.getCurrentLocale();
		if (locale != null) {
			try {
				menuText = locale.getStringGui(resourcePath + "." + this.getName() + ".label");
			} catch (MissingResourceException e) {
				menuText = null;
			}
		}
		if (menuText == null) {
			try {
				menuText = locale.getStringGui("commongui.text." + this.getName());
			} catch (MissingResourceException e) {
				menuText = null;
			}
		}
		if (menuText == null) {
			menuText = this.getName();
		}
		return menuText;
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
	public MenuEntry() {
		super();
		// BEGIN manual code section
		// MenuEntry.MenuEntry()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public MenuEntry(final String s) {
		super(s);
		// BEGIN manual code section
		// MenuEntry.MenuEntry(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public MenuEntry(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// MenuEntry.MenuEntry(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	@SuppressWarnings("unused")
	private static TypeRapidBean type = TypeRapidBean.createInstance(MenuEntry.class);

	/**
	 * @return the Biz Bean's type
	 */
	public abstract TypeRapidBean getType();

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
}
