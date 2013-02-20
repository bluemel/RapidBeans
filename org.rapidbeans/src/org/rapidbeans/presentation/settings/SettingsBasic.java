/*
 * Partially generated code file: SettingsBasic.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.settings.SettingsBasic
 * 
 * model:    model/org/rapidbeans/presentation/settings/SettingsBasic.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.settings;



// BEGIN manual code section
// SettingsBasic.import
import java.io.File;

import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.LinkFrozen;
import org.rapidbeans.core.exception.UnresolvedLinkException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.FileHelper;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.config.ApplicationGuiType;
import org.rapidbeans.presentation.settings.swing.SettingsBasicGuiSwing;

// END manual code section

/**
 * Rapid Bean class: SettingsBasic.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class SettingsBasic extends org.rapidbeans.presentation.settings.Settings {
	// BEGIN manual code section
	// SettingsBasic.classBody
	/**
	 * init the basic settings for all constructors.
	 */
	private void init() {
		final File settingsFolder = new File(SettingsAll.getDirname());
		if (!settingsFolder.exists()) {
			FileHelper.mkdirs(settingsFolder);
		}
		final Application app = ApplicationManager.getApplication();
		if (app != null) {
			if (app.getConfiguration().getGuitype() == ApplicationGuiType.swing) {
				this.setGui(new SettingsBasicGuiSwing());
			} else {
				this.setGui(new SettingsBasicGui());
			}
		} else {
			this.setGui(new SettingsBasicGui());
		}
	}

	// END manual code section

	/**
	 * property "folderdoc".
	 */
	private org.rapidbeans.core.basic.PropertyFile folderdoc;

	/**
	 * property "folderfiles".
	 */
	private org.rapidbeans.core.basic.PropertyFile folderfiles;

	/**
	 * property "defaultencoding".
	 */
	private org.rapidbeans.core.basic.PropertyChoice defaultencoding;

	/**
	 * property "defaultencodingusage".
	 */
	private org.rapidbeans.core.basic.PropertyChoice defaultencodingusage;

	/**
	 * property "gui".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend gui;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		super.initProperties();
		this.folderdoc = (org.rapidbeans.core.basic.PropertyFile)
			this.getProperty("folderdoc");
		this.folderfiles = (org.rapidbeans.core.basic.PropertyFile)
			this.getProperty("folderfiles");
		this.defaultencoding = (org.rapidbeans.core.basic.PropertyChoice)
			this.getProperty("defaultencoding");
		this.defaultencodingusage = (org.rapidbeans.core.basic.PropertyChoice)
			this.getProperty("defaultencodingusage");
		this.gui = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("gui");
	}

	/**
	 * default constructor.
	 */
	public SettingsBasic() {
		super();
		// BEGIN manual code section
		// SettingsBasic.SettingsBasic()
		init();
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public SettingsBasic(final String s) {
		super(s);
		// BEGIN manual code section
		// SettingsBasic.SettingsBasic(String)
		init();
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public SettingsBasic(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// SettingsBasic.SettingsBasic(String[])
		init();
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(SettingsBasic.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}

	/**
	 * @return value of Property 'folderdoc'
	 */
	public java.io.File getFolderdoc() {
		try {
			return (java.io.File) this.folderdoc.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("folderdoc");
		}
	}

	/**
	 * setter for Property 'folderdoc'.
	 * @param argValue
	 *            value of Property 'folderdoc' to set
	 */
	public void setFolderdoc(
		final java.io.File argValue) {
		this.folderdoc.setValue(argValue);
	}

	/**
	 * @return value of Property 'folderfiles'
	 */
	public java.io.File getFolderfiles() {
		try {
			return (java.io.File) this.folderfiles.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("folderfiles");
		}
	}

	/**
	 * setter for Property 'folderfiles'.
	 * @param argValue
	 *            value of Property 'folderfiles' to set
	 */
	public void setFolderfiles(
		final java.io.File argValue) {
		this.folderfiles.setValue(argValue);
	}

	/**
	 * @return value of Property 'defaultencoding'
	 */
	public org.rapidbeans.datasource.CharsetsAvailable getDefaultencoding() {
		try {
			java.util.List<?> enumList = (java.util.List<?>) this.defaultencoding.getValue();
			if (enumList == null || enumList.size() == 0) {
				return null;
			} else {
				return (org.rapidbeans.datasource.CharsetsAvailable) enumList.get(0);
						}			
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("defaultencoding");
		}
	}

	/**
	 * setter for Property 'defaultencoding'.
	 * @param argValue
	 *            value of Property 'defaultencoding' to set
	 */
	public void setDefaultencoding(
		final org.rapidbeans.datasource.CharsetsAvailable argValue) {
		java.util.List<org.rapidbeans.datasource.CharsetsAvailable> list =
			new java.util.ArrayList<org.rapidbeans.datasource.CharsetsAvailable>();
		list.add(argValue);
		this.defaultencoding.setValue(list);
	}

	/**
	 * @return value of Property 'defaultencodingusage'
	 */
	public org.rapidbeans.presentation.DefaultEncodingUsage getDefaultencodingusage() {
		try {
			java.util.List<?> enumList = (java.util.List<?>) this.defaultencodingusage.getValue();
			if (enumList == null || enumList.size() == 0) {
				return null;
			} else {
				return (org.rapidbeans.presentation.DefaultEncodingUsage) enumList.get(0);
						}			
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("defaultencodingusage");
		}
	}

	/**
	 * setter for Property 'defaultencodingusage'.
	 * @param argValue
	 *            value of Property 'defaultencodingusage' to set
	 */
	public void setDefaultencodingusage(
		final org.rapidbeans.presentation.DefaultEncodingUsage argValue) {
		java.util.List<org.rapidbeans.presentation.DefaultEncodingUsage> list =
			new java.util.ArrayList<org.rapidbeans.presentation.DefaultEncodingUsage>();
		list.add(argValue);
		this.defaultencodingusage.setValue(list);
	}

	/**
	 * @return value of Property 'gui'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.presentation.settings.SettingsBasicGui getGui() {
		try {
			org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.settings.SettingsBasicGui> col
				= (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.settings.SettingsBasicGui>) this.gui.getValue();
			if (col == null || col.size() == 0) {
				return null;
			} else {
				Link link = (Link) col.iterator().next();
				if (link instanceof LinkFrozen) {
					throw new UnresolvedLinkException("unresolved link to \""
							+ "org.rapidbeans.presentation.settings.SettingsBasicGui"
							+ "\" \"" + link.getIdString() + "\"");
				} else {
					return (org.rapidbeans.presentation.settings.SettingsBasicGui) col.iterator().next();
				}
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("gui");
		}
	}

	/**
	 * setter for Property 'gui'.
	 * @param argValue
	 *            value of Property 'gui' to set
	 */
	public void setGui(
		final org.rapidbeans.presentation.settings.SettingsBasicGui argValue) {
		this.gui.setValue(argValue);
	}
}
