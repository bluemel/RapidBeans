/*
 * Partially generated code file: SettingsBasicGuiOpenDocHistory.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.settings.SettingsBasicGuiOpenDocHistory
 * 
 * model:    model/org/rapidbeans/presentation/settings/SettingsBasicGuiOpenDocHistory.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.settings;

// BEGIN manual code section
// SettingsBasicGuiOpenDocHistory.import
import org.rapidbeans.core.event.PropertyChangeEvent;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.MenuHistoryOpenDocument;
import org.rapidbeans.presentation.ThreadLocalEventLock;
import org.rapidbeans.presentation.config.ConfigMenuHistoryOpenDocument;

// END manual code section

/**
 * Rapid Bean class: SettingsBasicGuiOpenDocHistory. Partially generated Java
 * class !!!Do only edit manually in marked sections!!!
 **/
public class SettingsBasicGuiOpenDocHistory extends
		org.rapidbeans.presentation.settings.Settings {
	// BEGIN manual code section
	// SettingsBasicGuiOpenDocHistory.classBody
	/**
	 * Data binding (one way) and factory settings. Intercepts any property
	 * changes.
	 * 
	 * @param event
	 *            the PropertyChangeEvent to fire
	 */
	public void propertyChanged(final PropertyChangeEvent event) {
		final boolean eventLoceBefore = ThreadLocalEventLock.get();
		try {
			if (eventLoceBefore) {
				ThreadLocalEventLock.release();
			}
			ConfigMenuHistoryOpenDocument config = null;
			MenuHistoryOpenDocument histMenu = null;
			final Application app = ApplicationManager.getApplication();
			if (app != null) {
				if (app.getConfiguration() != null) {
					if (app.getConfiguration().getMainwindow() != null) {
						if (app.getConfiguration().getMainwindow().getMenubar() != null) {
							config = app.getConfiguration().getMainwindow()
									.getMenubar()
									.findFirstMenuHistoryOpenDocument();
						}
					}
				}
				if (app.getMainwindow() != null) {
					if (app.getMainwindow().getMenubar() != null) {
						histMenu = app.getMainwindow().getMenubar()
								.findFirstMenuHistoryOpenDocument();
					}
				}
			}
			if (event.getProperty() == this.factorySettings) {
				if (((Boolean) event.getOldValue()) == false
						&& ((Boolean) event.getNewValue()) == true
						&& config != null) {
					setOn(true);
					setMaxNumberOfEntries(config.getMaxnumberofentries());
					setEntryShortage(config.getEntryshortage());
					setPresentation(config.getPresentation());
					setProtocolFilter(config.getProtocolfilter());
					setFactorySettings(true);
				}
			} else {
				if (event.getProperty() == this.entryShortage) {
					if (histMenu != null) {
						histMenu.setEntryShortage(getEntryShortage());
					}
				} else if (event.getProperty() == this.maxNumberOfEntries) {
					if (histMenu != null) {
						histMenu.setMaxNumberOfEntries(getMaxNumberOfEntries());
					}
				} else if (event.getProperty() == this.on) {
					if (histMenu != null) {
						histMenu.setOn(getOn());
					}
				} else if (event.getProperty() == this.presentation) {
					if (histMenu != null) {
						histMenu.setPresentation(getPresentation());
					}
				} else if (event.getProperty() == this.protocolFilter) {
					if (histMenu != null) {
						histMenu.setProtocolFilter(getProtocolFilter());
					}
				}
				if (this.factorySettings != null && this.getContainer() != null) {
					setFactorySettings(hasFactorySettings(config));
				}
				if (histMenu != null) {
					histMenu.update();
				}
			}
		} finally {
			if (eventLoceBefore) {
				ThreadLocalEventLock.set(null);
			}
		}
		super.propertyChanged(event);
	}

	/**
	 * Determines if the settings equal the original configuration.
	 * 
	 * @param config
	 *            the configuration
	 * 
	 * @return if the settings equal the original configuration
	 */
	private boolean hasFactorySettings(
			final ConfigMenuHistoryOpenDocument config) {
		if (config == null) {
			return true;
		}
		if (config.getEntryshortage() == this.getEntryShortage()
				&& config.getMaxnumberofentries() == this
						.getMaxNumberOfEntries()
				&& config.getPresentation() == this.getPresentation()
				&& config.getProtocolfilter().equals(this.getProtocolFilter())
				&& this.getOn()) {
			return true;
		} else {
			return false;
		}
	}

	// END manual code section

	/**
	 * property "on".
	 */
	private org.rapidbeans.core.basic.PropertyBoolean on;

	/**
	 * property "presentation".
	 */
	private org.rapidbeans.core.basic.PropertyChoice presentation;

	/**
	 * property "entryShortage".
	 */
	private org.rapidbeans.core.basic.PropertyBoolean entryShortage;

	/**
	 * property "protocolFilter".
	 */
	private org.rapidbeans.core.basic.PropertyString protocolFilter;

	/**
	 * property "maxNumberOfEntries".
	 */
	private org.rapidbeans.core.basic.PropertyInteger maxNumberOfEntries;

	/**
	 * property "factorySettings".
	 */
	private org.rapidbeans.core.basic.PropertyBoolean factorySettings;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		super.initProperties();
		this.on = (org.rapidbeans.core.basic.PropertyBoolean) this
				.getProperty("on");
		this.presentation = (org.rapidbeans.core.basic.PropertyChoice) this
				.getProperty("presentation");
		this.entryShortage = (org.rapidbeans.core.basic.PropertyBoolean) this
				.getProperty("entryShortage");
		this.protocolFilter = (org.rapidbeans.core.basic.PropertyString) this
				.getProperty("protocolFilter");
		this.maxNumberOfEntries = (org.rapidbeans.core.basic.PropertyInteger) this
				.getProperty("maxNumberOfEntries");
		this.factorySettings = (org.rapidbeans.core.basic.PropertyBoolean) this
				.getProperty("factorySettings");
	}

	/**
	 * default constructor.
	 */
	public SettingsBasicGuiOpenDocHistory() {
		super();
		// BEGIN manual code section
		// SettingsBasicGuiOpenDocHistory.SettingsBasicGuiOpenDocHistory()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * 
	 * @param s
	 *            the string
	 */
	public SettingsBasicGuiOpenDocHistory(final String s) {
		super(s);
		// BEGIN manual code section
		// SettingsBasicGuiOpenDocHistory.SettingsBasicGuiOpenDocHistory(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * 
	 * @param sa
	 *            the string array
	 */
	public SettingsBasicGuiOpenDocHistory(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// SettingsBasicGuiOpenDocHistory.SettingsBasicGuiOpenDocHistory(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean
			.createInstance(SettingsBasicGuiOpenDocHistory.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}

	/**
	 * @return value of Property 'on'
	 */
	public boolean getOn() {
		try {
			return ((org.rapidbeans.core.basic.PropertyBoolean) this.on)
					.getValueBoolean();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException(
					"on");
		}
	}

	/**
	 * setter for Property 'on'.
	 * 
	 * @param argValue
	 *            value of Property 'on' to set
	 */
	public void setOn(final boolean argValue) {
		this.on.setValue(new Boolean(argValue));
	}

	/**
	 * @return value of Property 'presentation'
	 */
	public org.rapidbeans.presentation.MenuHistoryOpenDocumentPresentationMode getPresentation() {
		try {
			java.util.List<?> enumList = (java.util.List<?>) this.presentation
					.getValue();
			if (enumList == null || enumList.size() == 0) {
				return null;
			} else {
				return (org.rapidbeans.presentation.MenuHistoryOpenDocumentPresentationMode) enumList
						.get(0);
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException(
					"presentation");
		}
	}

	/**
	 * setter for Property 'presentation'.
	 * 
	 * @param argValue
	 *            value of Property 'presentation' to set
	 */
	public void setPresentation(
			final org.rapidbeans.presentation.MenuHistoryOpenDocumentPresentationMode argValue) {
		java.util.List<org.rapidbeans.presentation.MenuHistoryOpenDocumentPresentationMode> list = new java.util.ArrayList<org.rapidbeans.presentation.MenuHistoryOpenDocumentPresentationMode>();
		list.add(argValue);
		this.presentation.setValue(list);
	}

	/**
	 * @return value of Property 'entryShortage'
	 */
	public boolean getEntryShortage() {
		try {
			return ((org.rapidbeans.core.basic.PropertyBoolean) this.entryShortage)
					.getValueBoolean();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException(
					"entryShortage");
		}
	}

	/**
	 * setter for Property 'entryShortage'.
	 * 
	 * @param argValue
	 *            value of Property 'entryShortage' to set
	 */
	public void setEntryShortage(final boolean argValue) {
		this.entryShortage.setValue(new Boolean(argValue));
	}

	/**
	 * @return value of Property 'protocolFilter'
	 */
	public String getProtocolFilter() {
		try {
			return (String) this.protocolFilter.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException(
					"protocolFilter");
		}
	}

	/**
	 * setter for Property 'protocolFilter'.
	 * 
	 * @param argValue
	 *            value of Property 'protocolFilter' to set
	 */
	public void setProtocolFilter(final String argValue) {
		this.protocolFilter.setValue(argValue);
	}

	/**
	 * @return value of Property 'maxNumberOfEntries'
	 */
	public int getMaxNumberOfEntries() {
		try {
			return ((org.rapidbeans.core.basic.PropertyInteger) this.maxNumberOfEntries)
					.getValueInt();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException(
					"maxNumberOfEntries");
		}
	}

	/**
	 * setter for Property 'maxNumberOfEntries'.
	 * 
	 * @param argValue
	 *            value of Property 'maxNumberOfEntries' to set
	 */
	public void setMaxNumberOfEntries(final int argValue) {
		this.maxNumberOfEntries.setValue(new Integer(argValue));
	}

	/**
	 * @return value of Property 'factorySettings'
	 */
	public boolean getFactorySettings() {
		try {
			return ((org.rapidbeans.core.basic.PropertyBoolean) this.factorySettings)
					.getValueBoolean();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException(
					"factorySettings");
		}
	}

	/**
	 * setter for Property 'factorySettings'.
	 * 
	 * @param argValue
	 *            value of Property 'factorySettings' to set
	 */
	public void setFactorySettings(final boolean argValue) {
		this.factorySettings.setValue(new Boolean(argValue));
	}
}
