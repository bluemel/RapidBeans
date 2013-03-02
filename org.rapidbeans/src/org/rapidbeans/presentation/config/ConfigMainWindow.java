/*
 * Partially generated code file: ConfigMainWindow.java
 * !!!Do only edit manually in marked sections!!!
 * 
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 * 
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.config.ConfigMainWindow
 * 
 * model: model/org/rapidbeans/presentation/config/ConfigMainWindow.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.config;

// BEGIN manual code section
// ConfigMainWindow.import
import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.LinkFrozen;
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.exception.UnresolvedLinkException;
import org.rapidbeans.core.type.TypeRapidBean;

// END manual code section

/**
 * Rapid Bean class: ConfigMainWindow. Partially generated Java class !!!Do only
 * edit manually in marked sections!!!
 **/
public class ConfigMainWindow extends RapidBeanImplStrict {
	// BEGIN manual code section
	// ConfigMainWindow.classBody
	// END manual code section

	/**
	 * property "name".
	 */
	private org.rapidbeans.core.basic.PropertyString name;

	/**
	 * property "width".
	 */
	private org.rapidbeans.core.basic.PropertyInteger width;

	/**
	 * property "height".
	 */
	private org.rapidbeans.core.basic.PropertyInteger height;

	/**
	 * property "toolbars".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend toolbars;

	/**
	 * property "menubar".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend menubar;

	/**
	 * property "footer".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend footer;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.name = (org.rapidbeans.core.basic.PropertyString) this.getProperty("name");
		this.width = (org.rapidbeans.core.basic.PropertyInteger) this.getProperty("width");
		this.height = (org.rapidbeans.core.basic.PropertyInteger) this.getProperty("height");
		this.toolbars = (org.rapidbeans.core.basic.PropertyAssociationend) this.getProperty("toolbars");
		this.menubar = (org.rapidbeans.core.basic.PropertyAssociationend) this.getProperty("menubar");
		this.footer = (org.rapidbeans.core.basic.PropertyAssociationend) this.getProperty("footer");
	}

	/**
	 * default constructor.
	 */
	public ConfigMainWindow() {
		super();
		// BEGIN manual code section
		// ConfigMainWindow.ConfigMainWindow()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * 
	 * @param s
	 *            the string
	 */
	public ConfigMainWindow(final String s) {
		super(s);
		// BEGIN manual code section
		// ConfigMainWindow.ConfigMainWindow(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * 
	 * @param sa
	 *            the string array
	 */
	public ConfigMainWindow(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// ConfigMainWindow.ConfigMainWindow(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(ConfigMainWindow.class);

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
	 * 
	 * @param argValue
	 *            value of Property 'name' to set
	 */
	public void setName(final String argValue) {
		this.name.setValue(argValue);
	}

	/**
	 * @return value of Property 'width'
	 */
	public int getWidth() {
		try {
			return ((org.rapidbeans.core.basic.PropertyInteger) this.width).getValueInt();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("width");
		}
	}

	/**
	 * setter for Property 'width'.
	 * 
	 * @param argValue
	 *            value of Property 'width' to set
	 */
	public void setWidth(final int argValue) {
		this.width.setValue(new Integer(argValue));
	}

	/**
	 * @return value of Property 'height'
	 */
	public int getHeight() {
		try {
			return ((org.rapidbeans.core.basic.PropertyInteger) this.height).getValueInt();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("height");
		}
	}

	/**
	 * setter for Property 'height'.
	 * 
	 * @param argValue
	 *            value of Property 'height' to set
	 */
	public void setHeight(final int argValue) {
		this.height.setValue(new Integer(argValue));
	}

	/**
	 * @return value of Property 'toolbars'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigToolbar> getToolbars() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigToolbar>) this.toolbars
					.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("toolbars");
		}
	}

	/**
	 * setter for Property 'toolbars'.
	 * 
	 * @param argValue
	 *            value of Property 'toolbars' to set
	 */
	public void setToolbars(final java.util.Collection<org.rapidbeans.presentation.config.ConfigToolbar> argValue) {
		this.toolbars.setValue(argValue);
	}

	/**
	 * add method for Property 'toolbars'.
	 * 
	 * @param bean
	 *            the bean to add
	 */
	public void addToolbar(final org.rapidbeans.presentation.config.ConfigToolbar bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.toolbars).addLink(bean);
	}

	/**
	 * remove method for Property 'toolbars'.
	 * 
	 * @param bean
	 *            the bean to add
	 */
	public void removeToolbar(final org.rapidbeans.presentation.config.ConfigToolbar bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.toolbars).removeLink(bean);
	}

	/**
	 * @return value of Property 'menubar'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.presentation.config.ConfigMenubar getMenubar() {
		try {
			org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigMenubar> col = (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigMenubar>) this.menubar
					.getValue();
			if (col == null || col.size() == 0) {
				return null;
			} else {
				Link link = (Link) col.iterator().next();
				if (link instanceof LinkFrozen) {
					throw new UnresolvedLinkException("unresolved link to \""
							+ "org.rapidbeans.presentation.config.ConfigMenubar" + "\" \"" + link.getIdString() + "\"");
				} else {
					return (org.rapidbeans.presentation.config.ConfigMenubar) col.iterator().next();
				}
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("menubar");
		}
	}

	/**
	 * setter for Property 'menubar'.
	 * 
	 * @param argValue
	 *            value of Property 'menubar' to set
	 */
	public void setMenubar(final org.rapidbeans.presentation.config.ConfigMenubar argValue) {
		this.menubar.setValue(argValue);
	}

	/**
	 * @return value of Property 'footer'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.presentation.config.ConfigFooter getFooter() {
		try {
			org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigFooter> col = (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigFooter>) this.footer
					.getValue();
			if (col == null || col.size() == 0) {
				return null;
			} else {
				Link link = (Link) col.iterator().next();
				if (link instanceof LinkFrozen) {
					throw new UnresolvedLinkException("unresolved link to \""
							+ "org.rapidbeans.presentation.config.ConfigFooter" + "\" \"" + link.getIdString() + "\"");
				} else {
					return (org.rapidbeans.presentation.config.ConfigFooter) col.iterator().next();
				}
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("footer");
		}
	}

	/**
	 * setter for Property 'footer'.
	 * 
	 * @param argValue
	 *            value of Property 'footer' to set
	 */
	public void setFooter(final org.rapidbeans.presentation.config.ConfigFooter argValue) {
		this.footer.setValue(argValue);
	}
}
