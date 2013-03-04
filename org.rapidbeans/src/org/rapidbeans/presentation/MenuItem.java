/*
 * Partially generated code file: MenuItem.java
 * !!!Do only edit manually in marked sections!!!
 * 
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 * 
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.MenuItem
 * 
 * model: model/org/rapidbeans/presentation/MenuItem.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation;

// BEGIN manual code section
// MenuItem.import
import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.LinkFrozen;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.UnresolvedLinkException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.config.ConfigApplication;
import org.rapidbeans.presentation.config.ConfigMenuItem;
import org.rapidbeans.presentation.enabler.Enabler;
import org.rapidbeans.presentation.swing.MenuItemSwing;
import org.rapidbeans.service.Action;

// END manual code section

/**
 * Rapid Bean class: MenuItem. Partially generated Java class !!!Do only edit
 * manually in marked sections!!!
 **/
public class MenuItem extends org.rapidbeans.presentation.MenuEntry {
	// BEGIN manual code section
	// MenuItem.classBody
	/**
	 * @return the widget.
	 */
	public Object getWidget() {
		return null;
	}

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
	public static final MenuItem createInstance(final ConfigMenuItem config, final Application client,
			final String resourcePath) {
		MenuItem menuItem = null;
		switch (client.getConfiguration().getGuitype()) {
		case swing:
			menuItem = new MenuItemSwing(config, client, resourcePath);
			break;
		case eclipsercp:
			// mainWindow = new BBMainWindowEclispercp-swt();
			break;
		default:
			throw new RapidBeansRuntimeException("Unknown GUI type \"" + client.getConfiguration().getGuitype().name()
					+ "\"");
		}
		return menuItem;
	}

	/**
	 * constructor.
	 * 
	 * @param menuItemConfig
	 *            the menu configuration
	 * @param resourcePath
	 *            the resource path
	 */
	public MenuItem(final Application client, final ConfigMenuItem menuItemConfig, final String resourcePath) {
		this.setName(menuItemConfig.getName());

		Action configAction = menuItemConfig.getAction();
		if (configAction != null && configAction.getParentBean() != null
				&& configAction.getParentBean() instanceof ConfigMenuItem) {
			((ConfigMenuItem) configAction.getParentBean()).setChildaction(null);
		}
		if (configAction != null && configAction.getParentBean() != null
				&& configAction.getParentBean() instanceof ConfigApplication) {
			final Action clonedAction = (Action) configAction.clone();
			clonedAction.setParentBean(null);
			clonedAction.setContainer(null);
			this.setAction(clonedAction);
		} else {
			this.setAction(configAction);
		}

		Enabler configEnabler = menuItemConfig.getEnabler();
		if (configEnabler != null && configEnabler.getParentBean() != null) {
			if (configEnabler.getParentBean() instanceof ConfigMenuItem) {
				((ConfigMenuItem) configEnabler.getParentBean()).setEnabler(null);
			}
		}
		if (configEnabler != null && configEnabler.getParentBean() != null
				&& configEnabler.getParentBean() instanceof Action) {
			final Enabler clonedEnabler = (Enabler) configEnabler.clone();
			clonedEnabler.setParentBean(null);
			clonedEnabler.setContainer(null);
			this.setEnabler(clonedEnabler);
		} else {
			this.setEnabler(configEnabler);
		}
	}

	// END manual code section

	/**
	 * property "action".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend action;

	/**
	 * property "enabler".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend enabler;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		super.initProperties();
		this.action = (org.rapidbeans.core.basic.PropertyAssociationend) this.getProperty("action");
		this.enabler = (org.rapidbeans.core.basic.PropertyAssociationend) this.getProperty("enabler");
	}

	/**
	 * default constructor.
	 */
	public MenuItem() {
		super();
		// BEGIN manual code section
		// MenuItem.MenuItem()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * 
	 * @param s
	 *            the string
	 */
	public MenuItem(final String s) {
		super(s);
		// BEGIN manual code section
		// MenuItem.MenuItem(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * 
	 * @param sa
	 *            the string array
	 */
	public MenuItem(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// MenuItem.MenuItem(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(MenuItem.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}

	/**
	 * @return value of Property 'action'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.service.Action getAction() {
		try {
			org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.service.Action> col = (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.service.Action>) this.action
					.getValue();
			if (col == null || col.size() == 0) {
				return null;
			} else {
				Link link = (Link) col.iterator().next();
				if (link instanceof LinkFrozen) {
					throw new UnresolvedLinkException("unresolved link to \"" + "org.rapidbeans.service.Action"
							+ "\" \"" + link.getIdString() + "\"");
				} else {
					return (org.rapidbeans.service.Action) col.iterator().next();
				}
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("action");
		}
	}

	/**
	 * setter for Property 'action'.
	 * 
	 * @param argValue
	 *            value of Property 'action' to set
	 */
	public void setAction(final org.rapidbeans.service.Action argValue) {
		this.action.setValue(argValue);
	}

	/**
	 * @return value of Property 'enabler'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.presentation.enabler.Enabler getEnabler() {
		try {
			org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.enabler.Enabler> col = (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.enabler.Enabler>) this.enabler
					.getValue();
			if (col == null || col.size() == 0) {
				return null;
			} else {
				Link link = (Link) col.iterator().next();
				if (link instanceof LinkFrozen) {
					throw new UnresolvedLinkException("unresolved link to \""
							+ "org.rapidbeans.presentation.enabler.Enabler" + "\" \"" + link.getIdString() + "\"");
				} else {
					return (org.rapidbeans.presentation.enabler.Enabler) col.iterator().next();
				}
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("enabler");
		}
	}

	/**
	 * setter for Property 'enabler'.
	 * 
	 * @param argValue
	 *            value of Property 'enabler' to set
	 */
	public void setEnabler(final org.rapidbeans.presentation.enabler.Enabler argValue) {
		this.enabler.setValue(argValue);
	}
}
