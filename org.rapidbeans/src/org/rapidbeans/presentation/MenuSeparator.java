/*
 * Partially generated code file: MenuSeparator.java
 * !!!Do only edit manually in marked sections!!!
 * 
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 * 
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.MenuSeparator
 * 
 * model: model/org/rapidbeans/presentation/MenuSeparator.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation;

// BEGIN manual code section
// MenuSeparator.import
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.config.ConfigMenuSeparator;
import org.rapidbeans.presentation.swing.MenuSeparatorSwing;

// END manual code section

/**
 * Rapid Bean class: MenuSeparator. Partially generated Java class !!!Do only
 * edit manually in marked sections!!!
 **/
public class MenuSeparator extends org.rapidbeans.presentation.MenuEntry {
	// BEGIN manual code section
	// MenuSeparator.classBody
	/**
	 * create a Submenu of a special type out of a configuration.
	 * 
	 * @param client
	 *            the parent client
	 * @param resourcePath
	 *            the resource path
	 * 
	 * @return the instaance
	 */
	public static final MenuSeparator createInstance(final ConfigMenuSeparator config, final Application client,
			final String resourcePath) {
		MenuSeparator menuSeparator = null;
		switch (client.getConfiguration().getGuitype()) {
		case swing:
			menuSeparator = new MenuSeparatorSwing(config, client, resourcePath);
			break;
		case eclipsercp:
			// mainWindow = new BBMainWindowEclispercp-swt();
			break;
		default:
			throw new RapidBeansRuntimeException("Unknown GUI type \"" + client.getConfiguration().getGuitype().name()
					+ "\"");
		}
		return menuSeparator;
	}

	/**
	 * constructor.
	 * 
	 * @param menuSeparatorConfig
	 *            the menu configuration
	 * @param resourcePath
	 *            the resource path
	 */
	public MenuSeparator(final Application client, final ConfigMenuSeparator menuSeparatorConfig,
			final String resourcePath) {
	}

	// END manual code section

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		super.initProperties();
	}

	/**
	 * default constructor.
	 */
	public MenuSeparator() {
		super();
		// BEGIN manual code section
		// MenuSeparator.MenuSeparator()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * 
	 * @param s
	 *            the string
	 */
	public MenuSeparator(final String s) {
		super(s);
		// BEGIN manual code section
		// MenuSeparator.MenuSeparator(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * 
	 * @param sa
	 *            the string array
	 */
	public MenuSeparator(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// MenuSeparator.MenuSeparator(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(MenuSeparator.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}
}
