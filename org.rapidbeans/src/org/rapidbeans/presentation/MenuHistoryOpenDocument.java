/*
 * Partially generated code file: MenuHistoryOpenDocument.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans 
				bean
			 generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans 
				bean
			 type
 * org.rapidbeans.presentation.MenuHistoryOpenDocument
 * 
 * model:    model/org/rapidbeans/presentation/MenuHistoryOpenDocument.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation;



// BEGIN manual code section
// MenuHistoryOpenDocument.import
import org.rapidbeans.core.common.HistoryList;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.FileHelper;
import org.rapidbeans.core.util.PlatformHelper;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.config.ConfigMenuHistoryOpenDocument;
import org.rapidbeans.presentation.settings.SettingsBasicGuiOpenDocHistory;
import org.rapidbeans.presentation.swing.MenuHistoryOpenDocumentSwing;

// END manual code section

/**
 * Rapid Bean class: MenuHistoryOpenDocument.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class MenuHistoryOpenDocument extends org.rapidbeans.presentation.MenuEntry {
	// BEGIN manual code section
	// MenuHistoryOpenDocument.classBody

	private HistoryList<Document> histList = null;

	/**
	 * @return the histList
	 */
	protected HistoryList<Document> getHistList() {
		return histList;
	}

	/**
	 * We need this for lazy initialization.
	 */
	private ConfigMenuHistoryOpenDocument config = null;

	/**
	 * create a History menu out of a configuration.
	 * 
	 * @param client
	 *            the parent client
	 * @param resourcePath
	 *            the resource path
	 * 
	 * @return the instance
	 */
	public static final MenuHistoryOpenDocument createInstance(final ConfigMenuHistoryOpenDocument config,
			final Application client, final String resourcePath) {
		MenuHistoryOpenDocument menuHistory = null;
		switch (client.getConfiguration().getGuitype()) {
		case swing:
			menuHistory = new MenuHistoryOpenDocumentSwing(config, client, resourcePath);
			break;
		case eclipsercp:
			// mainWindow = new BBMainWindowEclispercp-swt();
			break;
		default:
			throw new RapidBeansRuntimeException("Unknown GUI type \"" + client.getConfiguration().getGuitype().name()
					+ "\"");
		}
		return menuHistory;
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
	public MenuHistoryOpenDocument(final Application app, final ConfigMenuHistoryOpenDocument config,
			final String resourcePath) {

		SettingsBasicGuiOpenDocHistory settings = null;
		if (app.getSettings() != null && app.getSettings().getBasic() != null
				&& app.getSettings().getBasic().getGui() != null
				&& app.getSettings().getBasic().getGui().getOpenDocumentHistory() != null) {
			settings = app.getSettings().getBasic().getGui().getOpenDocumentHistory();
		}
		if (settings != null) {
			setOn(settings.getOn());
			setEntryShortage(settings.getEntryShortage());
			setPresentation(settings.getPresentation());
			setProtocolFilter(settings.getProtocolFilter());
		} else {
			// it's on because we have always have configuration
			// at the point of time this code is run
			setOn(true);
			setEntryShortage(config.getEntryshortage());
			setPresentation(config.getPresentation());
			setProtocolFilter(config.getProtocolfilter());
		}
		this.config = config;
	}

	public void update() {
		final Application app = ApplicationManager.getApplication();
		if (app != null && this.histList == null) {
			this.histList = app.getHistoryDocumentOpen();
			SettingsBasicGuiOpenDocHistory settings = null;
			if (app.getSettings() != null && app.getSettings().getBasic() != null
					&& app.getSettings().getBasic().getGui() != null
					&& app.getSettings().getBasic().getGui().getOpenDocumentHistory() != null) {
				settings = app.getSettings().getBasic().getGui().getOpenDocumentHistory();
			}
			if (settings != null) {
				app.getHistoryDocumentOpen().setMax(settings.getMaxNumberOfEntries());
			} else {
				app.getHistoryDocumentOpen().setMax(this.config.getMaxnumberofentries());
			}
		}
	}

	/**
	 * Action handler for selected history menu item
	 * 
	 * @param selectedMenuItemIndex
	 *            the index of the history menu item that has been selected
	 */
	protected void histMenuItemSelected(final int selectedMenuItemIndex) {
		final Application app = ApplicationManager.getApplication();
		if (app != null && this.histList != null) {
			final Document doc = this.histList.get(selectedMenuItemIndex);
			app.openDocumentView(doc);
			app.addDocumentOpenedToHistory(doc);
		}
	}

	protected String getMenuText(final int index) {
		final StringBuffer buf = new StringBuffer();
		buf.append(Integer.toString(index + 1));
		buf.append(' ');
		final Document doc = this.histList.get(index);
		String url = doc.getUrl().toString();
		if (getProtocolFilter() != null) {
			if (url.startsWith(getProtocolFilter() + ":")) {
				url = url.substring(getProtocolFilter().length() + 1);
			}
			if (getProtocolFilter().equals("file") && PlatformHelper.getOsName().contains("Windows")
					&& url.startsWith("/")) {
				url = url.substring(1);
			}
		}
		if (getEntryShortage()) {
			url = FileHelper.shortenPathCenter(url, 10, "...", 15);
		}
		buf.append(url);
		return buf.toString();
	}

	/**
	 * Set the maximal number of entries.
	 * 
	 * @param maxNumberOfEntries
	 *            the new value
	 */
	public void setMaxNumberOfEntries(final int maxNumberOfEntries) {
		this.histList.setMax(maxNumberOfEntries);
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
	 * property references initialization.
	 */
	public void initProperties() {
		super.initProperties();
		this.on = (org.rapidbeans.core.basic.PropertyBoolean)
			this.getProperty("on");
		this.presentation = (org.rapidbeans.core.basic.PropertyChoice)
			this.getProperty("presentation");
		this.entryShortage = (org.rapidbeans.core.basic.PropertyBoolean)
			this.getProperty("entryShortage");
		this.protocolFilter = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("protocolFilter");
	}

	/**
	 * default constructor.
	 */
	public MenuHistoryOpenDocument() {
		super();
		// BEGIN manual code section
		// MenuHistoryOpenDocument.MenuHistoryOpenDocument()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public MenuHistoryOpenDocument(final String s) {
		super(s);
		// BEGIN manual code section
		// MenuHistoryOpenDocument.MenuHistoryOpenDocument(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public MenuHistoryOpenDocument(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// MenuHistoryOpenDocument.MenuHistoryOpenDocument(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(MenuHistoryOpenDocument.class);

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
			return ((org.rapidbeans.core.basic.PropertyBoolean) this.on).getValueBoolean();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("on");
		}
	}

	/**
	 * setter for Property 'on'.
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
			java.util.List<?> enumList = (java.util.List<?>) this.presentation.getValue();
			if (enumList == null || enumList.size() == 0) {
				return null;
			} else {
				return (org.rapidbeans.presentation.MenuHistoryOpenDocumentPresentationMode) enumList.get(0);
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("presentation");
		}
	}

	/**
	 * setter for Property 'presentation'.
	 * @param argValue
	 *            value of Property 'presentation' to set
	 */
	public void setPresentation(final org.rapidbeans.presentation.MenuHistoryOpenDocumentPresentationMode argValue) {
		java.util.List<org.rapidbeans.presentation.MenuHistoryOpenDocumentPresentationMode> list =
			new java.util.ArrayList<org.rapidbeans.presentation.MenuHistoryOpenDocumentPresentationMode>();
		list.add(argValue);
		this.presentation.setValue(list);
	}

	/**
	 * @return value of Property 'entryShortage'
	 */
	public boolean getEntryShortage() {
		try {
			return ((org.rapidbeans.core.basic.PropertyBoolean) this.entryShortage).getValueBoolean();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("entryShortage");
		}
	}

	/**
	 * setter for Property 'entryShortage'.
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
			throw new org.rapidbeans.core.exception.PropNotInitializedException("protocolFilter");
		}
	}

	/**
	 * setter for Property 'protocolFilter'.
	 * @param argValue
	 *            value of Property 'protocolFilter' to set
	 */
	public void setProtocolFilter(final String argValue) {
		this.protocolFilter.setValue(argValue);
	}
}
