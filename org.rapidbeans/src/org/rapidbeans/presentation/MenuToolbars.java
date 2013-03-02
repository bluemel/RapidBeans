/*
 * Partially generated code file: MenuToolbars.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.MenuToolbars
 * 
 * model:    model/org/rapidbeans/presentation/MenuToolbars.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation;

// BEGIN manual code section
// MenuToolbars.import
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.config.ConfigMenuToolbars;
import org.rapidbeans.presentation.swing.MenuToolbarsSwing;

// END manual code section

/**
 * Rapid Bean class: MenuToolbars. Partially generated Java class !!!Do only
 * edit manually in marked sections!!!
 **/
public class MenuToolbars extends org.rapidbeans.presentation.MenuEntry {
	// BEGIN manual code section
	// MenuToolbars.classBody

	// /**
	// * We need this for lazy initialization.
	// */
	// private ConfigMenuToolbars config = null;

	/**
	 * create a History menu out of a configuration.
	 * 
	 * @param config
	 *            the configuration
	 * @param client
	 *            the parent client
	 * @param resourcePath
	 *            the resource path
	 * 
	 * @return the instance
	 */
	public static final MenuToolbars createInstance(
			final ConfigMenuToolbars config, final Application client,
			final String resourcePath) {
		MenuToolbars menuToolbars = null;
		switch (client.getConfiguration().getGuitype()) {
		case swing:
			menuToolbars = new MenuToolbarsSwing(config, client, resourcePath);
			break;
		case eclipsercp:
			// mainWindow = new BBMainWindowEclispercp-swt();
			break;
		default:
			throw new RapidBeansRuntimeException("Unknown GUI type \""
					+ client.getConfiguration().getGuitype().name() + "\"");
		}
		return menuToolbars;
	}

	/**
	 * constructor.
	 * 
	 * @param app
	 *            the application
	 * @param config
	 *            the menu configuration
	 * @param resourcePath
	 *            the resource path
	 */
	public MenuToolbars(final Application app, final ConfigMenuToolbars config,
			final String resourcePath) {
		super();
		// this.config = config;
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
	public MenuToolbars() {
		super();
		// BEGIN manual code section
		// MenuToolbars.MenuToolbars()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * 
	 * @param s
	 *            the string
	 */
	public MenuToolbars(final String s) {
		super(s);
		// BEGIN manual code section
		// MenuToolbars.MenuToolbars(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * 
	 * @param sa
	 *            the string array
	 */
	public MenuToolbars(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// MenuToolbars.MenuToolbars(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean
			.createInstance(MenuToolbars.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}
}
