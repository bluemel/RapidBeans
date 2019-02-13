/*
 * Rapid Beans Framework: Application.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 09/14/2005
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copies of the GNU Lesser General Public License and the
 * GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

package org.rapidbeans.presentation;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.rapidbeans.core.basic.IdType;
import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanImplParent;
import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.basic.ThreadLocalValidationSettings;
import org.rapidbeans.core.common.HistoryList;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.AuthorizationException;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TypePropertyChoice;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.type.TypeRapidEnum;
import org.rapidbeans.core.util.SoundHelper;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.datasource.CharsetsAvailable;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.datasource.Filter;
import org.rapidbeans.presentation.config.ApplicationGuiType;
import org.rapidbeans.presentation.config.ConfigApplication;
import org.rapidbeans.presentation.config.ConfigAuthorization;
import org.rapidbeans.presentation.config.ConfigDocument;
import org.rapidbeans.presentation.config.ConfigEditorBean;
import org.rapidbeans.presentation.config.ConfigFilterRuleRoleBased;
import org.rapidbeans.presentation.config.ConfigLocale;
import org.rapidbeans.presentation.config.ConfigMainWindow;
import org.rapidbeans.presentation.config.ConfigMenuHistoryOpenDocument;
import org.rapidbeans.presentation.config.ConfigMenuItem;
import org.rapidbeans.presentation.config.ConfigMenubar;
import org.rapidbeans.presentation.config.ConfigPropPersistencestrategy;
import org.rapidbeans.presentation.config.ConfigSubmenu;
import org.rapidbeans.presentation.config.ConfigView;
import org.rapidbeans.presentation.config.swing.ConfigApplicationSwing;
import org.rapidbeans.presentation.enabler.EnablerActiveViewClose;
import org.rapidbeans.presentation.enabler.EnablerDocumentSave;
import org.rapidbeans.presentation.enabler.EnablerDocumentSaveAs;
import org.rapidbeans.presentation.event.SettingsChangedEvent;
import org.rapidbeans.presentation.event.SettingsChangedListener;
import org.rapidbeans.presentation.guistate.BeanEditorState;
import org.rapidbeans.presentation.guistate.HistoryState;
import org.rapidbeans.presentation.guistate.MainWindowState;
import org.rapidbeans.presentation.guistate.PropState;
import org.rapidbeans.presentation.guistate.UiState;
import org.rapidbeans.presentation.guistate.ViewState;
import org.rapidbeans.presentation.settings.Settings;
import org.rapidbeans.presentation.settings.SettingsAll;
import org.rapidbeans.presentation.settings.SettingsBasic;
import org.rapidbeans.presentation.settings.SettingsBasicGui;
import org.rapidbeans.presentation.settings.SettingsBasicGuiOpenDocHistory;
import org.rapidbeans.presentation.settings.swing.ApplicationLnfTypeSwing;
import org.rapidbeans.presentation.settings.swing.SettingsBasicGuiSwing;
import org.rapidbeans.presentation.swing.DocumentViewSwing;
import org.rapidbeans.security.ChangePwdAfterNextlogonType;
import org.rapidbeans.security.LoginType;
import org.rapidbeans.security.User;
import org.rapidbeans.service.Action;
import org.rapidbeans.service.ActionActiveViewClose;
import org.rapidbeans.service.ActionArgument;
import org.rapidbeans.service.ActionDocumentNew;
import org.rapidbeans.service.ActionDocumentOpen;
import org.rapidbeans.service.ActionDocumentSave;
import org.rapidbeans.service.ActionDocumentSaveAs;
import org.rapidbeans.service.ActionQuit;
import org.rapidbeans.service.ActionSettings;

/**
 * Application is the RapidBeans application's root object (the root of all
 * evil) and additionally the one and only singleton of a RapidBeans
 * application.
 */
public class Application implements Appl {

	private static final Logger log = Logger.getLogger(Application.class.getName());

	private String name = null;

	private List<TypeRapidBean> docroottypes = new ArrayList<TypeRapidBean>();

	private String rootpackage = null;

	private File initiallyLoadDocument = null;

	private String[] initCreds = null;

	private ActionManager actionManager = new ActionManager();

	/**
	 * @return the actionManager
	 */
	public ActionManager getActionManager() {
		return actionManager;
	}

	private boolean pwdChanged = false;

	public boolean getPwdChanged() {
		return pwdChanged;
	}

	public void setInitiallyLoadDocument(final String initFilePath) {
		if (initFilePath != null) {
			this.initiallyLoadDocument = new File(initFilePath);
		} else {
			this.initiallyLoadDocument = null;
		}
	}

	public Application() {
		this(null, (String[]) null, null, null, null, null);
	}

	public Application(final String appname, final String docroottypename, final String approotpackage,
			final String initiallyLoadDocument) {
		this(appname, new String[] { docroottypename }, approotpackage, initiallyLoadDocument, null, null);
	}

	public Application(final String appname, final String[] docroottypenames, final String approotpackage,
			final String initiallyLoadDocument, final String user, final String pwd) {
		this.name = appname;
		if (docroottypenames != null) {
			for (final String typename : docroottypenames) {
				this.docroottypes.add(TypeRapidBean.forName(typename));
			}
		}
		this.rootpackage = approotpackage;
		this.setInitiallyLoadDocument(initiallyLoadDocument);
		if (RapidBeansTypeLoader.getInstance().getXmlRootElementBinding("applicationcfg") == null) {
			RapidBeansTypeLoader.getInstance().addXmlRootElementBinding("applicationcfg",
					"org.rapidbeans.presentation.config.swing.ConfigApplicationSwing");
		}
		if (user != null && pwd != null) {
			this.initCreds = new String[2];
			this.initCreds[0] = user;
			this.initCreds[1] = pwd;
		}
	}

	/**
	 * Here you can store the application's start options.
	 */
	private Properties options = new Properties();

	/**
	 * @return the application's start options.
	 */
	public Properties getOptions() {
		return this.options;
	}

	/**
	 * property "mainwindow".
	 */
	private MainWindow mainwindow;

	/**
	 * property "configuration".
	 */
	private ConfigApplication configuration;

	/**
	 * @return value of Property 'mainwindow'
	 */
	public final MainWindow getMainwindow() {
		return this.mainwindow;
	}

	/**
	 * setter for Property 'mainwindow'.
	 * 
	 * @param argValue value of Property 'mainwindow' to set
	 */
	public final void setMainwindow(final MainWindow argValue) {
		this.mainwindow = argValue;
	}

	/**
	 * @return value of Property 'configuration'
	 */
	public ConfigApplication getConfiguration() {
		return this.configuration;
	}

	/**
	 * setter for Property 'configuration'.
	 * 
	 * @param argValue value of Property 'configuration' to set
	 */
	public void setConfiguration(final ConfigApplication argValue) {
		this.configuration = argValue;
	}

	/**
	 * the Message Dialog controlling instance.
	 */
	private MessageDialog messageDialog = null;

	/**
	 * The applet is not null if the application runs as applet.
	 */
	private JApplet applet = null;

	/**
	 * the locales configured for this application (array list to preserve the
	 * order)
	 */
	private ArrayList<RapidBeansLocale> locales = new ArrayList<RapidBeansLocale>();

	/**
	 * the locales configured for this application
	 */
	private HashMap<String, RapidBeansLocale> localemap = new HashMap<String, RapidBeansLocale>();

	/**
	 * @return all the application's configured Locales
	 */
	public Collection<RapidBeansLocale> getLocales() {
		return locales;
	}

	/**
	 * @return the locale with the given name
	 */
	public RapidBeansLocale getLocale(final String name) {
		return localemap.get(name);
	}

	/**
	 * add a locale internally.
	 * 
	 * @param locale the locale to add.
	 */
	protected void addLocale(final RapidBeansLocale locale) {
		this.locales.add(locale);
		this.localemap.put(locale.getName(), locale);
	}

	/**
	 * the current locale.
	 */
	private RapidBeansLocale currentLocale = null;

	/**
	 * @return the currentLocale
	 */
	public RapidBeansLocale getCurrentLocale() {
		return currentLocale;
	}

	/**
	 * @param currentLocale the currentLocale to set
	 */
	public void setCurrentLocale(RapidBeansLocale currentLocale) {
		this.currentLocale = currentLocale;
	}

	/**
	 * @param applet the applet to set
	 */
	public void setApplet(final JApplet applet) {
		this.applet = applet;
	}

	/**
	 * the authentication and authorization data document.
	 */
	private Document authnDoc = null;

	/**
	 * @return the authnDoc
	 */
	public Document getAuthnDoc() {
		if (this.authnDoc == null) {
			if (getConfiguration() == null) {
				log.warning("getConfiguration() == null");
			} else {
				if (getConfiguration().getAuthorization() == null) {
					log.warning("getConfiguration().getAuthorization() == null");
				} else {
					if (getConfiguration().getAuthorization().getRealm() != null) {
						try {
							this.authnDoc = new Document("realm",
									new URL(getConfiguration().getAuthorization().getRealm()));
						} catch (MalformedURLException e) {
							throw new RapidBeansRuntimeException(e);
						}
					}
				}
			}
		}
		return authnDoc;
	}

	/**
	 * @param authnDoc the authnDoc to set
	 */
	protected void setAuthnDoc(final Document authnDoc) {
		this.authnDoc = authnDoc;
	}

	/**
	 * the user authenticated to this application.
	 */
	private RapidBean authenticatedUser = null;

	/**
	 * @return the authenticated user
	 */
	public final RapidBean getAuthenticatedUser() {
		return this.authenticatedUser;
	}

	/**
	 * @return if authorization should be used or not
	 */
	public final boolean isUsingAuthorization() {
		return this.getConfiguration().getAuthorization() != null;
	}

	private UiProperties uiProps = null;

	/**
	 * @return the uiProps
	 */
	public UiProperties getUiProps() {
		return uiProps;
	}

	/**
	 * Save a certain document. COuld be overwritten by the application for special
	 * needs.
	 * 
	 * @param doc the document to save
	 */
	public void save(final Document doc) {
		doc.save();
	}

	/**
	 * Role check for the authenticated user.
	 * 
	 * @param reqdRoles the given roles required
	 * 
	 * @return if the authenticated user has one of the given required roles
	 */
	@SuppressWarnings("unchecked")
	public boolean userIsAuthorized(final Collection<RapidEnum> reqdRoles) {
		if (!this.isUsingAuthorization()) {
			return true;
		}
		boolean auth = false;
		if ((getAuthnDoc() != null) && (reqdRoles != null)) {
			if (this.authenticatedUser != null && this.authenticatedUser.getPropValue("roles") != null) {
				for (final RapidEnum reqdRole : reqdRoles) {
					for (RapidEnum userRole : (Collection<RapidEnum>) this.authenticatedUser.getPropValue("roles")) {
						if (userRole.name().equals(reqdRole.name())) {
							auth = true;
							break;
						}
					}
					if (auth == true) {
						break;
					}
				}
			}
		} else {
			auth = true;
		}
		return auth;
	}

	/**
	 * initializes the application.
	 */
	public void init() {
		initLocales();
		initMessageDialog();
		this.uiProps = new UiProperties(this);
		initGuiToolkit();
		if (this.getConfiguration().getXmlbinding() != null) {
			if (this.getConfiguration().getXmlbinding().getSeparator() != null
					&& this.getConfiguration().getXmlbinding().getSeparator().length() > 0) {
				TypePropertyCollection
						.setDefaultCharSeparator(this.getConfiguration().getXmlbinding().getSeparator().charAt(0));
			}
			if (this.getConfiguration().getXmlbinding().getEscape() != null) {
				TypePropertyCollection
						.setDefaultCharEscape(this.getConfiguration().getXmlbinding().getEscape().charAt(0));
			}
		}

		// load authorization info and drive the login
		if (this.isUsingAuthorization()) {
			final ConfigAuthorization authncfg = this.getConfiguration().getAuthorization();
			// initialize the authorization document
			if (authncfg.getAuthenticationmethod() != null
					&& authncfg.getAuthenticationmethod().contains(LoginType.userpassword)) {
				if (this.initCreds != null) {
					this.logon(this.initCreds[0], this.initCreds[1]);
					this.initCreds = null;
				}
				if (authncfg.getRoletype() != null && authncfg.getRoletype().length() > 0) {
					setAuthnRoleType(authncfg.getRoletype());
					// reload the application configuration
					if (this.configuration != null) {
						this.setConfiguration(null);
						initConfiguration();
					}
					// reload the realm document
					if (this.authnDoc != null) {
						this.setAuthnDoc(null);
						this.getAuthnDoc();
					}
					// reload the authenticated user if already logged in
					if (this.authenticatedUser != null) {
						final String userTypename = this.authenticatedUser.getType().getName();
						final String userId = this.authenticatedUser.getIdString();
						this.authenticatedUser = this.getAuthnDoc().findBean(userTypename, userId);
					}
				}
				if (this.initiallyLoadDocument != null && this.authnDoc == null) {
					this.setAuthnDoc(new Document(this.initiallyLoadDocument));
				}
				if (this.authenticatedUser == null) {
					if (this.getTestMode()) {
						this.authenticatedUser = (User) getAuthnDoc().findBean("org.rapidbeans.security.User",
								"testuser");
					} else {
						this.authenticatedUser = DialogLogin.login(authncfg.getLoginmaxtries());
						if (this.authenticatedUser == null) {
							this.end();
						}
					}
				}
			}
		}

		// drive password change if recommended or required
		if (this.isUsingAuthorization() && this.authenticatedUser != null && this.authenticatedUser instanceof User
				&& ((User) this.authenticatedUser).getChangePwdAfterNextLogon() != ChangePwdAfterNextlogonType.no) {
			final ChangePwdAfterNextlogonType changePwdAfterNextLogon = ((User) this.authenticatedUser)
					.getChangePwdAfterNextLogon();
			pwdChanged = DialogPwdChange.start(this.authenticatedUser);
			if (changePwdAfterNextLogon == ChangePwdAfterNextlogonType.mandatory && (!pwdChanged)) {
				messageInfo(getCurrentLocale().getStringMessage("login.cancelled.required.pwd.change.failed"));
				end();
			}
		}

		// open the app's main window
		if ((!this.isUsingAuthorization()) || this.authenticatedUser != null) {
			this.setMainwindow(MainWindow.createInstance(this, getConfiguration().getMainwindow()));
		}
	}

	public void logon(final String userAccountName, final String pwd) {
		final RapidBean user = this.getAuthnDoc().findBean("org.rapidbeans.security.User", userAccountName);
		if (user == null) {
			return;
		}
		final String userPwd = (String) user.getPropValue("pwd");
		if ((userPwd != null && userPwd.equals(pwd)) || (userPwd == null && (pwd == null || pwd.length() == 0))) {
			this.authenticatedUser = user;
		}
	}

	/**
	 * Initialize the GUI toolkit.
	 */
	private void initGuiToolkit() {
		if (this.getConfiguration().getGuitype() == ApplicationGuiType.swing) {
			try {
				ApplicationLnfTypeSwing laf = null;
				final SettingsBasicGui guiSettings = this.getSettings().getBasic().getGui();
				if (guiSettings instanceof SettingsBasicGuiSwing) {
					laf = ((SettingsBasicGuiSwing) guiSettings).getLookAndFeel();
				}
				if (laf == null) {
					if (this.getConfiguration() instanceof ConfigApplicationSwing) {
						laf = ((ConfigApplicationSwing) this.getConfiguration()).getLookandfeel();
					} else {
						final ConfigApplicationSwing newConfig = new ConfigApplicationSwing();
						for (final Property prop : this.getConfiguration().getPropertyList()) {
							final Object value = prop.getValue();
							try {
								ThreadLocalValidationSettings.validationOff();
								prop.setValue(null);
								newConfig.getProperty(prop.getName()).setValue(value);
							} finally {
								ThreadLocalValidationSettings.remove();
							}
						}
						this.setConfiguration(newConfig);
						laf = ((ConfigApplicationSwing) this.getConfiguration()).getLookandfeel();
					}
				}
				String lafClassname = null;
				if (laf == null) {
					lafClassname = "javax.swing.plaf.metal.MetalLookAndFeel";
				} else if (laf == ApplicationLnfTypeSwing.getInstance("system")) {
					lafClassname = UIManager.getSystemLookAndFeelClassName();
				} else {
					lafClassname = laf.getLnfClass().getName();
				}
				UIManager.setLookAndFeel(lafClassname);
			} catch (ClassNotFoundException e) {
				throw new RapidBeansRuntimeException(e);
			} catch (InstantiationException e) {
				throw new RapidBeansRuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RapidBeansRuntimeException(e);
			} catch (UnsupportedLookAndFeelException e) {
				throw new RapidBeansRuntimeException(e);
			}
		}
	}

	protected void initLocales() {
		if (this.getConfiguration() != null && this.getConfiguration().getLocales() != null
				&& this.getConfiguration().getLocales().size() > 0) {
			// if any locale is defined
			RapidBeansLocale locale;
			for (ConfigLocale clocale : this.getConfiguration().getLocales()) {
				locale = new RapidBeansLocale(new String[] { clocale.getName() });
				locale.init(this);
				this.addLocale(locale);
			}
		} else {
			// if no locale is defined generate the english one
			final RapidBeansLocale defLocale = new RapidBeansLocale(new String[] { "en" });
			defLocale.init(this);
			this.currentLocale = defLocale;
		}
		if (this.locales.size() > 0) {
			if (this.getConfiguration().getDefaultlocale() != null) {
				final RapidBeansLocale defaultLocale = this.localemap.get(this.getConfiguration().getDefaultlocale());
				this.setCurrentLocale(defaultLocale);
			} else {
				this.setCurrentLocale(this.locales.get(0));
			}
		}
	}

	protected void initMessageDialog() {
		this.messageDialog = MessageDialog.createInstance(getConfiguration().getGuitype());
	}

	/**
	 * the document map.
	 */
	private HashMap<String, Document> documents = new HashMap<String, Document>();

	/**
	 * get the document with the given name.
	 * 
	 * @param docname the name of the document
	 * 
	 * @return the document
	 */
	public final Document getDocument(final String docname) {
		return this.documents.get(docname);
	}

	/**
	 * get all documents.
	 * 
	 * @return collection with all documents
	 */
	public final Collection<Document> getDocuments() {
		return this.documents.values();
	}

	/**
	 * register a document to the application.
	 * 
	 * @param doc the bean document
	 */
	public final void addDocument(final Document doc) {
		if (this.documents.get(doc.getName()) != null) {
			throw new RapidBeansRuntimeException("document file \"" + doc.getName() + "\" already loaded");
		}
		this.documents.put(doc.getName(), doc);
	}

	/**
	 * remove (close) a document.
	 * 
	 * @param doc the document to remove
	 */
	public final void removeDocument(final Document doc) {
		this.documents.remove(doc.getName());
	}

	/**
	 * the document map.
	 */
	private HashMap<String, View> views = new HashMap<String, View>();

	/**
	 * get the view with the given name.
	 * 
	 * @param viewname the name of the view
	 * 
	 * @return the view
	 */
	public final View getView(final String viewname) {
		return this.views.get(viewname);
	}

	/**
	 * get the view with the given widget.
	 * 
	 * @param widget the widget
	 * 
	 * @return the view
	 */
	public final View getViewByWidget(final Object widget) {
		View foundView = null;
		for (View v : this.views.values()) {
			if (v.getWidget() == widget) {
				foundView = v;
				break;
			}
		}
		return foundView;
	}

	/**
	 * register a view to the application.
	 * 
	 * @param view the bean view
	 */
	public final void addView(final View view) {
		final String viewname = view.getName();
		if (this.views.get(viewname) != null) {
			throw new RapidBeansRuntimeException("view \"" + viewname + "\" already loaded");
		}
		this.views.put(viewname, view);
		this.getMainwindow().addView(this, view);
	}

	/**
	 * open a document view on the given document.
	 * 
	 * @param doc          the document to view.
	 * @param docconfname  the view's document configuration name
	 * @param viewconfname the view's configuration name
	 * 
	 * @return the view opened
	 */
	public final DocumentView openDocumentView(final Document doc) {
		return openDocumentView(doc, null, null, null);
	}

	/**
	 * open a document view on the given document.
	 * 
	 * @param doc          the document to view.
	 * @param docconfname  the view's document configuration name
	 * @param viewconfname the view's configuration name
	 * 
	 * @return the view opened
	 */
	public final DocumentView openDocumentView(final Document doc, final String doccfgname, final String viewcfgname) {
		return openDocumentView(doc, doccfgname, viewcfgname, null);
	}

	/**
	 * open a document view on the given document.
	 * 
	 * @param doc          the document to view.
	 * @param docconfname  the view's document configuration name
	 * @param viewconfname the view's configuration name
	 * @param filter       the view's filter
	 * 
	 * @return the view opened
	 */
	public final DocumentView openDocumentView(final Document doc, final String doccfgname, final String viewcfgname,
			final Filter filter) {
		final String docconfname = getDocconfname(doccfgname);
		final String viewconfname = getViewconfname(viewcfgname);
		final ConfigDocument docconf = getDocconf(doc, docconfname);
		final ConfigView viewconf = getViewconf(docconf, viewconfname);
		return openDocumentView(docconf, doc, viewconfname, viewconf, filter);
	}

	/**
	 * open a document view with given configurations.
	 * 
	 * @param docconf      the document configuration
	 * @param doc          the document
	 * @param viewconfname the view configuration's name
	 * @param viewconf     the view configuration
	 * 
	 * @return the view
	 */
	public final DocumentView openDocumentView(final ConfigDocument docconf, final Document doc,
			final String viewconfname, final ConfigView viewconf) {
		return openDocumentView(docconf, doc, viewconfname, viewconf, null);
	}

	/**
	 * open a document view with given configurations.
	 * 
	 * @param docconf      the document configuration
	 * @param doc          the document
	 * @param viewconfname the view configuration's name
	 * @param viewconf     the view configuration
	 * @param filter       the filter
	 * 
	 * @return the view
	 */
	public final DocumentView openDocumentView(final ConfigDocument docconf, final Document doc,
			final String viewconfname, final ConfigView viewconf, final Filter filter) {

		Filter localfilter = filter;

		if (this.isUsingAuthorization() && docconf != null) {
			if (docconf.getReadaccessrolesrequired() != null && docconf.getReadaccessrolesrequired().size() > 0
					&& (!this.userIsAuthorized(docconf.getReadaccessrolesrequired()))) {
				throw new AuthorizationException(
						this.getCurrentLocale().getStringMessage("authorization.denied.document",
								this.getAuthenticatedUser().getProperty("accountname").toString()));
			}
			if (docconf.getWriteaccessrolesrequired() != null && docconf.getWriteaccessrolesrequired().size() > 0
					&& (!this.userIsAuthorized(docconf.getWriteaccessrolesrequired()))) {
				doc.setReadonly(true);
			}

			if (docconf.getFilterrules() != null && docconf.getFilterrules().size() > 0) {
				for (final ConfigFilterRuleRoleBased rule : docconf.getFilterrules()) {
					for (final RapidEnum role : rule.getRoles()) {
						if (User.hasRoleGeneric(this.getAuthenticatedUser(), role.name())) {
							if (rule.getIncludesfilters() != null && rule.getIncludesfilters().length() > 0) {
								for (final String s : StringHelper.split(rule.getIncludesfilters(), ",")) {
									log.fine("adding include filter rule: " + s);
									if (localfilter == null) {
										localfilter = new Filter();
									}
									localfilter.addIncludes(s);
								}
							}
							if (rule.getExcludesfilters() != null && rule.getExcludesfilters().length() > 0) {
								for (final String s : StringHelper.split(rule.getExcludesfilters(), ",")) {
									log.fine("adding exclude filter rule: " + s);
									if (localfilter == null) {
										localfilter = new Filter();
									}
									localfilter.addExcludes(s);
								}
							}
							break;
						}
					}
				}
			}
		}

		if (this.documents.get(doc.getName()) == null) {
			this.addDocument(doc);
		}
		DocumentView view = null;
		final String viewname = this.getViewname(docconf, doc, viewconfname);
		try {
			view = (DocumentView) this.views.get(viewname);
		} catch (ClassCastException e) {
			view = null;
		}
		if (view == null) {
			view = DocumentView.createInstance(this, doc, docconf, viewconf, localfilter);
			addView(view);
		} else {
			this.getMainwindow().putToFront(view);
		}
		return view;
	}

	/**
	 * @param docconf      the document's configuration
	 * @param doc          the document
	 * @param viewconfname the view config's name
	 * 
	 * @return the view's name
	 */
	final String getViewname(final ConfigDocument docconf, final Document doc, final String viewconfname) {
		String viewnamepart = null;
		if (viewconfname.equals(ConfigView.NAME_NO_CONFIG)) {
			if (docconf != null && docconf.getDefaultview() != null && (!docconf.getDefaultview().equals(""))) {
				viewnamepart = docconf.getDefaultview();
			} else {
				viewnamepart = "standard";
			}
		} else {
			viewnamepart = viewconfname;
		}
		return doc.getName() + "." + viewnamepart;
	}

	/**
	 * open a document view on the given document.
	 * 
	 * @param docname      the document name
	 * @param typename     the type of the document's root.
	 * @param docconfname  the view's document configuration name
	 * @param viewconfname the view's configuration name
	 * 
	 * @return the document view opened
	 */
	public final DocumentView openNewDocumentView(final String docname, final String typename, final String docconfname,
			final String viewconfname) {
		RapidBean root = RapidBeanImplParent.createInstance(typename);
		Document doc = new Document(docname, root);
		return this.openDocumentView(doc, docconfname, viewconfname);
	}

	/**
	 * @param doccfgname a document config's name or null
	 * 
	 * @return the name or "no_conf"
	 */
	private String getDocconfname(final String doccfgname) {
		String docconfname = null;
		if (doccfgname == null) {
			docconfname = ConfigDocument.NAME_NO_CONFIG;
		} else {
			docconfname = doccfgname;
		}
		return docconfname;
	}

	/**
	 * @param doc         the document
	 * @param docconfname the document's configuration name
	 * 
	 * @return the document's configuration
	 */
	private ConfigDocument getDocconf(final Document doc, final String docconfname) {
		ConfigDocument docconf = null;
		if (docconfname.equals(ConfigDocument.NAME_NO_CONFIG)) {
			docconf = getConfigDocument(doc);
		} else {
			docconf = getConfigDocument(docconfname);
		}
		return docconf;
	}

	/**
	 * @param viewcfgname a view config's name or null
	 * 
	 * @return the name or "no_conf"
	 */
	private String getViewconfname(final String viewcfgname) {
		String viewconfname = null;
		if (viewcfgname == null || viewcfgname.equals(ConfigDocument.NAME_NO_CONFIG)) {
			viewconfname = ConfigView.NAME_NO_CONFIG;
		} else {
			viewconfname = viewcfgname;
		}
		return viewconfname;
	}

	/**
	 * @param docconf      the document's configuration
	 * @param viewconfname the view's configuration
	 * 
	 * @return the view's configuration
	 */
	private ConfigView getViewconf(final ConfigDocument docconf, final String viewconfname) {
		ConfigView viewconf = null;
		if ((viewconfname.equals(ConfigView.NAME_NO_CONFIG)) && docconf != null && docconf.getDefaultview() != null) {
			viewconf = docconf.getConfigView(docconf.getDefaultview());
		} else {
			if (docconf != null) {
				viewconf = getConfigView(docconf.getName(), viewconfname);
			}
			if (viewconf == null) {
				viewconf = new ConfigView(new String[] { "standard" });
				viewconf.setViewclass("org.rapidbeans.presentation.BBDocumentViewSwing");
				viewconf.setPersistencestrategy(ConfigPropPersistencestrategy.ondemand);
			}
		}
		return viewconf;
	}

	/**
	 * unregister a view.
	 * 
	 * @param view the view to remove
	 */
	public final void removeView(final View view) {
		if (this.views.get(view.getName()) == null) {
			throw new RapidBeansRuntimeException("view \"" + view.getName() + "\" already removed");
		}
		this.getMainwindow().removeView(view);
		this.views.remove(view.getName());
	}

	/**
	 * the program settings document.
	 */
	private Document settings = null;

	/**
	 * Supports mocking application instances with settings. Production use is
	 * discouraged.
	 * 
	 * @param settingsDoc the settings document
	 */
	public void setSettingsDoc(final Document settingsDoc) {
		this.settings = settingsDoc;
	}

	/**
	 * @return the program settings document
	 */
	public Document getSettingsDoc() {
		if (this.settings == null) {
			this.settings = new Document("programsettings", SettingsAll.getFile());
		}

		final SettingsBasic bsettings = ((SettingsAll) this.settings.getRoot()).getBasic();

		// initialize default encoding
		if (bsettings.getDefaultencoding() == null) {
			if (this.configuration != null) {
				bsettings.setDefaultencoding((CharsetsAvailable) CharsetsAvailable.getEnumType()
						.elementOf(this.configuration.getDefaultcharset()));
			} else {
				bsettings.setDefaultencoding(CharsetsAvailable.getInstance("UTF-8"));
			}
		}

		// create SettingsBasicGuiOpenDocHistory only if configured
		if (this.getConfiguration() != null && this.getConfiguration().getMainwindow() != null
				&& this.getConfiguration().getMainwindow().getMenubar() != null) {
			final ConfigMenuHistoryOpenDocument config = this.getConfiguration().getMainwindow().getMenubar()
					.findFirstMenuHistoryOpenDocument();
			if (config != null) {
				final SettingsBasicGui settingsBasicGui = bsettings.getGui();
				if (settingsBasicGui.getOpenDocumentHistory() == null) {
					final SettingsBasicGuiOpenDocHistory histSettings = new SettingsBasicGuiOpenDocHistory();
					histSettings.setOn(true);
					histSettings.setMaxNumberOfEntries(config.getMaxnumberofentries());
					histSettings.setEntryShortage(config.getEntryshortage());
					histSettings.setPresentation(config.getPresentation());
					histSettings.setProtocolFilter(config.getProtocolfilter());
					histSettings.setFactorySettings(true);
					settingsBasicGui.setOpenDocumentHistory(histSettings);
				}
			}
		}

		return this.settings;
	}

	/**
	 * @return the program settings document
	 */
	public SettingsAll getSettings() {
		Document settingsDoc = getSettingsDoc();
		return (SettingsAll) settingsDoc.getRoot();
	}

	/**
	 * the listeners for settings changed events.
	 */
	private Collection<SettingsChangedListener> settingsChangedListeners = new ArrayList<SettingsChangedListener>();

	/**
	 * adds a settings changed listener.
	 * 
	 * @param listener the listener to add
	 */
	public synchronized void addSettingsChangedListener(final SettingsChangedListener listener) {
		this.settingsChangedListeners.add(listener);
	}

	/**
	 * removes a settings changed listener.
	 * 
	 * @param listener the listener to add
	 */
	public synchronized void removeSettingsChangedListener(final SettingsChangedListener listener) {
		this.settingsChangedListeners.remove(listener);
	}

	/**
	 * Fire a settings changed event to all interested objects.
	 * 
	 * @param e the settings changed event.
	 */
	public void fireSettingsChanged(final SettingsChangedEvent e) {
		for (SettingsChangedListener listener : this.settingsChangedListeners) {
			listener.settingsChanged(e);
		}
	}

	/**
	 * @return the testMode
	 */
	public boolean getTestMode() {
		return false;
	}

	/**
	 * show a Dialog on with an informational message.
	 * 
	 * @param message the message to show
	 */
	public final void messageInfo(final String message) {
		this.messageDialog.messageInfo(message, null);
	}

	/**
	 * show a Dialog on with an informational message.
	 * 
	 * @param message the message to show
	 * @param title   the window title
	 */
	public void messageInfo(final String message, final String title) {
		this.messageDialog.messageInfo(message, title);
	}

	/**
	 * show a Dialog on with an error message.
	 * 
	 * @param message the message to show
	 */
	public void messageError(final String message) {
		this.messageDialog.messageError(message, null);
	}

	/**
	 * show a Dialog on with an informational message.
	 * 
	 * @param message the message to show
	 * @param title   the window title
	 */
	public void messageError(final String message, final String title) {
		this.messageDialog.messageError(message, title);
	}

	/**
	 * Present an unforeseen exception.
	 * 
	 * @param throwable the Throwable instance
	 * @param title     the dialog title
	 */
	public void messageException(final Throwable throwable, final String title) {
		ExceptionMessageDialog.createInstance(this, throwable, title).show();
	}

	/**
	 * show a Dialog on with an yes no question message.
	 * 
	 * @param message the message to show
	 * @param title   the window title
	 * @return true if yes, false if no
	 */
	public boolean messageYesNo(final String message, final String title) {
		return this.messageDialog.messageYesNo(message, title);
	}

	/**
	 * show a Dialog on with an yes no cancel question message.
	 * 
	 * @param message the message to show
	 * @param title   the window title
	 * @return yes, no, cancel
	 */
	public final MessageDialogResponse messageYesNoCancel(final String message, final String title) {
		return this.messageDialog.messageYesNoCancel(message, title);
	}

	/**
	 * starts the application.
	 * 
	 * @param options the start options
	 */
	public void start() {
		if (ApplicationManager.getApplication() == null) {
			ApplicationManager.setApplication(this);
		}
		initConfiguration();
		init();
		if (this.mainwindow != null && !getTestMode()) {
			this.mainwindow.show();
		}
		if (this.initiallyLoadDocument != null) {
			final Document doc = new Document(this.initiallyLoadDocument);
			this.openDocumentView(doc);
			this.addDocumentOpenedToHistory(doc);
			if (this.getSettings().getBasic().getFolderdoc() == null) {
				final File parent = this.initiallyLoadDocument.getParentFile().getParentFile();
				this.getSettings().getBasic().setFolderdoc(parent);
				if (this.getSettings().getBasic().getFolderfiles() == null) {
					this.getSettings().getBasic().setFolderfiles(parent);
				}
			}
		} else {
			this.restoreUiState();
		}
	}

	/**
	 * initialize the application configuration.
	 */
	private void initConfiguration() {

		if (this.getConfiguration() != null) {
			log.fine("configuration already initialized");
			return;
		}

		URL url = null;

		if (this.configFilePath != null && this.configFilePath.length() > 0) {
			log.fine("Trying configFilePath \"" + this.configFilePath + "...");
			url = this.getResourceLoader().getResource(this.configFilePath);
		}

		if (url == null && this.rootpackage != null && this.rootpackage.length() > 0) {
			if (this.name != null && this.name.length() != 0) {
				final String configFilePath1 = this.rootpackage.replace('.', '/') + "/" + this.name;
				log.fine("Trying path \"" + configFilePath1 + "...");
				url = this.getResourceLoader().getResource(configFilePath1);
			}
		}

		if (url == null && this.rootpackage != null && this.rootpackage.length() > 0) {
			final String configFilePath2 = this.rootpackage.replace('.', '/') + "/Application.xml";
			log.fine("Trying path \"" + configFilePath2 + "...");
			url = this.getResourceLoader().getResource(configFilePath2);
		}

		if (url != null) {
			final Document appConfigDoc = new Document("appconfig", url);
			this.setConfiguration((ConfigApplication) appConfigDoc.getRoot());
		}

		if (this.getConfiguration() == null) {
			log.info("initializing simple default configuration...");
			this.initSimpleDefaultConfiguration();
		} else {
			if (Locale.getDefault() != null && Locale.getDefault().getLanguage() != null
					&& Locale.getDefault().getLanguage().length() > 0) {
				final String systemLang = Locale.getDefault().getLanguage();
				boolean foundSystemLang = false;
				if (this.getConfiguration().getLocales() != null) {
					for (final ConfigLocale cl : this.configuration.getLocales()) {
						if (cl.getName().equals(systemLang)) {
							foundSystemLang = true;
							break;
						}
					}
				}
				if (foundSystemLang) {
					this.configuration.setDefaultlocale(systemLang);
				}
			}
		}
	}

	/**
	 * initialize a simple configuration if the configuration is not given.
	 */
	protected void initSimpleDefaultConfiguration() {
		ConfigApplication cfg = null;
		if (RapidBeansTypeLoader.getInstance().getXmlRootElementBinding("applicationcfg") != null) {
			final TypeRapidBean beantype = RapidBeansTypeLoader.getInstance()
					.getXmlRootElementBinding("applicationcfg");
			cfg = (ConfigApplication) RapidBeanImplParent.createInstance(beantype);
		} else {
			cfg = new ConfigApplication();
		}
		this.setConfiguration(cfg);
		String appName = null;
		final String appClassName = this.getClass().getName();
		final String appClassNameShort = StringHelper.splitLast(appClassName, ".");
		if (this.name == null) {
			appName = appClassNameShort;
			if ((appName.endsWith("Application") || appName.endsWith("application")) && appName.length() > 11) {
				appName = appName.substring(0, appName.length() - 11);
			}
		} else {
			appName = this.name;
		}
		cfg.setName(appName);
		cfg.setRootpackage(appClassName.substring(0, appClassName.length() - appClassNameShort.length() - 1));
		cfg.setApplicationclass(this.getClass().getName());

		// configure locale
		final ConfigLocale locale = new ConfigLocale(Locale.getDefault().toString());
		cfg.addLocale(locale);
		cfg.setDefaultlocale(locale.getName());

		// configure settings to be stored immediately
		final ConfigDocument confdoc = new ConfigDocument("settings");
		confdoc.setRootclass(SettingsAll.class.getName());
		final ConfigView confview = new ConfigView("standard");
		confview.setViewclass(DocumentViewSwing.class.getName());
		confview.setPersistencestrategy(ConfigPropPersistencestrategy.oncloseeditor);
		confdoc.addView(confview);
		cfg.addDocument(confdoc);

		// configure the main window
		final ConfigMainWindow mainwin = new ConfigMainWindow("mainwindow");
		cfg.setMainwindow(mainwin);
		final ConfigMenubar menubar = new ConfigMenubar("menubar");
		mainwin.setMenubar(menubar);
		final ConfigSubmenu fileMenu = new ConfigSubmenu("file");
		menubar.addMenu(fileMenu);
		switch (this.docroottypes.size()) {
		case 0:
			break;
		case 1: // size = 1 => create one single "New" menu entry
			final ConfigMenuItem menuItemNew1 = new ConfigMenuItem("new");
			menuItemNew1.setName("new");
			final Action newAction1 = new ActionDocumentNew();
			newAction1
					.addArgument(new ActionArgument(new String[] { "rootclass", this.docroottypes.get(0).getName() }));
			menuItemNew1.setChildaction(newAction1);
			fileMenu.addMenuentry(menuItemNew1);
			break;
		default: // size > 1 create a "New" submenu with menu entries
			final ConfigSubmenu submenuNew = new ConfigSubmenu("new");
			submenuNew.setName("newsubmenu");
			if (this.getCurrentLocale() == null) {
				this.initLocales();
			}
			fileMenu.addMenuentry(submenuNew);
			for (final TypeRapidBean docroottype : this.docroottypes) {
				final ConfigMenuItem menuItemNew = new ConfigMenuItem("new");
				try {
					this.getCurrentLocale().getStringGui("mainwindow.menubar.file.newsubmenu."
							+ docroottype.getNameShort().toLowerCase() + ".label");
					menuItemNew.setName(docroottype.getNameShort().toLowerCase());
				} catch (MissingResourceException e1) {
					try {
						this.getCurrentLocale().getStringGui("bean." + docroottype.getName().toLowerCase());
						menuItemNew.setName(
								this.getCurrentLocale().getStringGui("bean." + docroottype.getName().toLowerCase()));
					} catch (MissingResourceException e2) {
						menuItemNew.setName(docroottype.getNameShort());
					}
				}
				final Action newAction = new ActionDocumentNew();
				newAction.addArgument(new ActionArgument(new String[] { "rootclass", docroottype.getName() }));
				menuItemNew.setChildaction(newAction);
				submenuNew.addMenuentry(menuItemNew);
			}
			break;
		}
		final ConfigMenuItem menuItemOpen = new ConfigMenuItem("open");
		menuItemOpen.setChildaction(new ActionDocumentOpen());
		fileMenu.addMenuentry(menuItemOpen);
		final ConfigMenuItem menuItemClose = new ConfigMenuItem("close");
		menuItemClose.setChildaction(new ActionActiveViewClose());
		menuItemClose.setEnabler(new EnablerActiveViewClose());
		fileMenu.addMenuentry(menuItemClose);
		fileMenu.addSeparator();
		final ConfigMenuItem menuItemSave = new ConfigMenuItem("save");
		menuItemSave.setChildaction(new ActionDocumentSave());
		menuItemSave.setEnabler(new EnablerDocumentSave());
		fileMenu.addMenuentry(menuItemSave);
		final ConfigMenuItem menuItemSaveAs = new ConfigMenuItem("saveas");
		menuItemSaveAs.setChildaction(new ActionDocumentSaveAs());
		menuItemSaveAs.setEnabler(new EnablerDocumentSaveAs());
		fileMenu.addMenuentry(menuItemSaveAs);
		fileMenu.addSeparator();
		final ConfigMenuHistoryOpenDocument history = new ConfigMenuHistoryOpenDocument("history");
		fileMenu.addMenuentry(history);
		fileMenu.addSeparator();
		final ConfigMenuItem menuItemQuit = new ConfigMenuItem("quit");
		menuItemQuit.setChildaction(new ActionQuit());
		fileMenu.addMenuentry(menuItemQuit);
		final ConfigSubmenu adminMenu = new ConfigSubmenu("admin");
		menubar.addMenu(adminMenu);
		final ConfigMenuItem menuItemSettings = new ConfigMenuItem("settings");
		menuItemSettings.setChildaction(new ActionSettings());
		adminMenu.addMenuentry(menuItemSettings);
	}

	/**
	 * ends the application.
	 * 
	 * @return if ending the application has been canceled.
	 */
	public boolean end() {
		saveUiState();
		Collection<View> cViews = new ArrayList<View>();
		for (View view : this.getViews()) {
			cViews.add(view);
		}
		boolean cancel = false;
		for (View view : cViews) {
			cancel = view.close();
			if (cancel) {
				break;
			}
		}
		if (!cancel) {
			if (this.getMainwindow() != null) {
				this.getMainwindow().close();
			}
			if (this.applet != null) {
				this.applet.stop();
				this.applet.destroy();
			} else {
				System.exit(0);
			}
		}
		return cancel;
	}

	/**
	 * @return the application's views.
	 */
	public Collection<View> getViews() {
		return this.views.values();
	}

	private final class ViewOrderHelper implements Comparable<ViewOrderHelper> {
		private View view = null;

		public View getView() {
			return this.view;
		}

		private Integer zOrder = Integer.valueOf(-1);

		public int compareTo(final ViewOrderHelper other) {
			return this.zOrder.compareTo(other.zOrder);
		}

		public ViewOrderHelper(final View view, final int zOrder) {
			this.view = view;
			this.zOrder = zOrder;
		}
	}

	/**
	 * @return the application's views in Z-Order
	 */
	public View[] getViewsInZOrder() {
		final ViewOrderHelper[] views = new ViewOrderHelper[this.views.size()];
		int i = 0;
		for (final View view : this.views.values()) {
			views[i++] = new ViewOrderHelper(view, this.mainwindow.getViewZOrder(view));
		}
		Arrays.sort(views);
		final View[] viewsSorted = new View[this.views.size()];
		i = 0;
		for (final ViewOrderHelper view : views) {
			viewsSorted[i++] = view.getView();
		}
		return viewsSorted;
	}

	/**
	 * retrieve the document currently on top and active.
	 * 
	 * @return the active document
	 */
	public Document getActiveDocument() {
		return getMainwindow().getActiveDocument();
	}

	/**
	 * retrieve the view currently on top and active.
	 * 
	 * @return the active document
	 */
	public View getActiveView() {
		return getMainwindow().getActiveDocumentView();
	}

	/**
	 * @param doc the document
	 * 
	 * @return the document's title to present in the GUI
	 */
	public String getDocumentTitle(final Document doc) {
		String title = this.currentLocale.getStringGui("document." + doc.getName() + ".title");
		return title;
	}

	/**
	 * Since we currently have no map property type...
	 * 
	 * @param bizBeanType the bean type
	 * 
	 * @return the biz bean editor configuration associated to this class or null if
	 *         not existent.
	 */
	public final ConfigEditorBean getConfigBeanEditor(final TypeRapidBean bizBeanType) {
		ConfigEditorBean conf = null;
		if (this.getConfiguration() == null) {
			return null;
		}
		Collection<ConfigEditorBean> confs = this.getConfiguration().getBeaneditors();
		if (confs == null) {
			return null;
		}
		for (ConfigEditorBean cfg : confs) {
			if (cfg.getBeantype().equals(bizBeanType.getName())) {
				conf = cfg;
				break;
			}
		}
		return conf;
	}

	/**
	 * Getter by name.
	 * 
	 * @param docconfname the document configuration name
	 * 
	 * @return the document view configuration associated to this name or null if
	 *         not existent.
	 */
	public final ConfigDocument getConfigDocument(final String docconfname) {
		return this.getConfiguration().getConfigDocument(docconfname);
	}

	/**
	 * Getter by document (searches by type of root bean).
	 * 
	 * @param doc the document of which you want to search the configuration
	 * 
	 * @return the document view configuration associated to this name or null if
	 *         not existent.
	 */
	public final ConfigDocument getConfigDocument(final Document doc) {
		return this.getConfiguration().getConfigDocument(doc);
	}

	/**
	 * Getter by name.
	 * 
	 * @param docconfname  the document configuration name
	 * @param viewconfname the view configuration name
	 * 
	 * @return the document view configuration associated to these names or null if
	 *         not existent.
	 */
	public final ConfigView getConfigView(final String docconfname, final String viewconfname) {
		return this.getConfiguration().getConfigView(docconfname, viewconfname);
	}

	/**
	 * play the error sound.
	 */
	public void playSoundError() {
		if (!this.getTestMode()) {
			SoundHelper.play(Application.class.getResourceAsStream("sounds/error.wav"));
		}
	}

	/**
	 * determines if the given document view is the last open document view to its
	 * document.
	 * 
	 * @param view the document to investigate
	 * @return if the given document view is the last open document view to its
	 *         document
	 */
	public boolean isLastOpenDocumentView(final DocumentView view) {
		boolean isLast = true;
		DocumentView vwd;
		for (View vw : this.getViews()) {
			if (vw instanceof DocumentView && vw != view) {
				vwd = (DocumentView) vw;
				if (vwd.getDocument() == view.getDocument()) {
					isLast = false;
					break;
				}
			}
		}
		return isLast;
	}

	/**
	 * @return the applet
	 */
	protected JApplet getApplet() {
		return applet;
	}

	private static final String[] SA = new String[0];

	public static void setAuthnRoleType(final String typename) {
		final TypeRapidEnum enumType = TypeRapidEnum.forName(typename);

		final TypeRapidBean cfgMenuEntryType = TypeRapidBean
				.forName("org.rapidbeans.presentation.config.ConfigMenuEntry");
		final TypePropertyChoice cfgMenuEntryRolesProptype = (TypePropertyChoice) cfgMenuEntryType
				.getPropertyType("rolesrequired");
		cfgMenuEntryRolesProptype.setEnumType(enumType);

		final TypeRapidBean cfgToolbarButtonType = TypeRapidBean
				.forName("org.rapidbeans.presentation.config.ConfigToolbarButton");
		final TypePropertyChoice cfgToolbarButtonRolesProptype = (TypePropertyChoice) cfgToolbarButtonType
				.getPropertyType("rolesrequired");
		cfgToolbarButtonRolesProptype.setEnumType(enumType);

		final TypeRapidBean cfgDocumentType = TypeRapidBean
				.forName("org.rapidbeans.presentation.config.ConfigDocument");
		final TypePropertyChoice cfgDocumentRolesReadProptype = (TypePropertyChoice) cfgDocumentType
				.getPropertyType("readaccessrolesrequired");
		cfgDocumentRolesReadProptype.setEnumType(enumType);
		final TypePropertyChoice cfgDocumentRolesWriteProptype = (TypePropertyChoice) cfgDocumentType
				.getPropertyType("writeaccessrolesrequired");
		cfgDocumentRolesWriteProptype.setEnumType(enumType);

		final TypeRapidBean cfgFilterRuleRoleBasedType = TypeRapidBean
				.forName("org.rapidbeans.presentation.config.ConfigFilterRuleRoleBased");
		final TypePropertyChoice cfgFilterRuleRoleBasedProptype = (TypePropertyChoice) cfgFilterRuleRoleBasedType
				.getPropertyType("roles");
		cfgFilterRuleRoleBasedProptype.setEnumType(enumType);

		final TypeRapidBean userType = TypeRapidBean.forName("org.rapidbeans.security.User");
		final TypePropertyChoice userRolesProptype = (TypePropertyChoice) userType.getPropertyType("roles");
		userRolesProptype.setEnumType(enumType);

		final TypeRapidBean actionType = TypeRapidBean.forName("org.rapidbeans.service.Action");
		final TypePropertyChoice actionRolesProptype = (TypePropertyChoice) actionType.getPropertyType("rolesrequired");
		actionRolesProptype.setEnumType(enumType);
	}

	/**
	 * Generic main method.
	 * 
	 * @param args the arguments
	 * @throws ClassNotFoundException
	 */
	public static void main(final String[] args) {
		try {
			String appname = null;
			String approotpackage = null;
			String initdocfile = null;
			String user = null;
			String pwd = null;
			final List<String> docRootTypenames = new ArrayList<String>();
			for (int i = 0; i < args.length; i++) {
				if (args[i].startsWith("-")) {
					if (args[i].equals("-appname")) {
						appname = args[++i];
					} else if (args[i].equals("-approotpackage")) {
						approotpackage = args[++i];
					} else if (args[i].equals("-loaddoc")) {
						initdocfile = args[++i];
					} else if (args[i].equals("-docroottype")) {
						docRootTypenames.add(args[++i]);
					} else if (args[i].equals("-user")) {
						user = args[++i];
					} else if (args[i].equals("-pwd")) {
						pwd = args[++i];
					} else {
						throw new IllegalArgumentException("Illegal option \"" + args[i] + "\"");
					}
				} else {
					throw new IllegalArgumentException("Illegal argument \"" + args[i] + "\"");
				}
			}
			new Application(appname, docRootTypenames.toArray(SA), approotpackage, initdocfile, user, pwd).start();
		} catch (IllegalArgumentException e) {
			printUsage();
			throw e;
		}
	}

	private static void printUsage() {
		System.out.println("Valid options:");
		System.out.println("  -appname .......... (single)   the application's name");
		System.out.println("  -approotpackage ... (single)   the application's root package");
		System.out.println("  -docroottype ...... (multiple) the application's document root type(s)");
		System.out.println("                                 every document root type:");
		System.out.println(
				"                                     - will be pre loeaded (including XML root element binding)");
		System.out.println("                                     - will have its own new document entry");
		System.out.println("  -loaddoc .......... (single)   a document to load initially\n");
		System.out.println("                                 Later on replace by file history");
		System.out.println("                                 and view storage and recovery");
		System.out.println("  -user ............. (single)   user");
		System.out.println("  -pwd .............. (single)   pwd");
		System.out.println("  ");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the rootpackage
	 */
	public String getRootpackage() {
		if (this.rootpackage == null) {
			if (this.getConfiguration() != null && this.getConfiguration().getRootpackage() != null) {
				this.rootpackage = this.getConfiguration().getRootpackage();
			}
		}
		return rootpackage;
	}

	public void dumpViews() {
		for (String view : this.views.keySet()) {
			System.out.println("- view: " + view);
		}
	}

	/**
	 * Save the UI state locally in a window.
	 */
	public void saveUiState() {
		final UiState uiState = new UiState();
		if (this.getMainwindow() == null) {
			return;
		}
		final MainWindowState mainWinState = this.getMainwindow().saveUiState(uiState);
		uiState.setMainWindow(mainWinState);
		for (final View view : getViewsInZOrder()) {
			if (!(view instanceof DocumentView)) {
				continue;
			}
			final DocumentView dview = (DocumentView) view;
			if (dview.getDocument().getUrl() == null) {
				continue;
			}
			final ViewState viewState = new ViewState();
			viewState.setName(dview.getName());
			if (dview.getDocument().getUrl() != null) {
				viewState.setUrl(dview.getDocument().getUrl().toString());
			}
			viewState.setConfnameDoc(dview.getConfigDocument().getName());
			viewState.setConfnameView(dview.getConfiguration().getName());
			viewState.setDividerLocation(dview.getDividerLocation());
			for (final EditorBean beanEditor : dview.getEditors()) {
				final BeanEditorState bedState = new BeanEditorState();
				bedState.setBeantype(beanEditor.getBean().getType().getName());
				if (beanEditor.getBean().getType().getIdtype() == IdType.transientid) {
					for (final Property prop : beanEditor.getBean().getPropertyList()) {
						if (prop.getValue() == null) {
							continue;
						}
						if (prop.getType().isTransient()) {
							continue;
						}
						final PropState propState = new PropState();
						propState.setName(prop.getName());
						propState.setValue(prop.toString());
						bedState.addProperty(propState);
					}
				} else {
					bedState.setBeanid(beanEditor.getBean().getIdString());
				}
				viewState.addBeaneditor(bedState);
			}
			mainWinState.addView(viewState);
		}
		if (this.historyDocumentOpen != null && this.historyDocumentOpen.size() > 0) {
			for (int i1 = 0; i1 < this.historyDocumentOpen.size(); i1++) {
				final Document histDoc = this.historyDocumentOpen.get(i1);
				final HistoryState histState = new HistoryState();
				histState.setUrl(histDoc.getUrl().toString());
				uiState.addHistory(histState);
			}
		}
		final Document uiStateDoc = new Document(uiState);
		uiStateDoc.setUrl(getUiStateURL());
		uiStateDoc.save();
	}

	/**
	 * Restore UI state from local UI state settings file if present.
	 */
	public void restoreUiState() {
		final File uiStateFile = new File(getUiStateURL().getFile().replace("%20", " "));
		if (!uiStateFile.exists()) {
			return;
		}
		final Document doc = new Document(uiStateFile);
		final UiState uiState = (UiState) doc.getRoot();
		if (uiState == null || uiState.getMainWindow() == null || uiState.getMainWindow().getViews() == null) {
			return;
		}
		this.mainwindow.restoreUiState(uiState);
		final ViewState[] vss = new ViewState[uiState.getMainWindow().getViews().size()];
		int i = vss.length - 1;
		for (final ViewState vs : ((UiState) doc.getRoot()).getMainWindow().getViews()) {
			vss[i--] = vs;
		}
		for (final ViewState vs : vss) {
			Document viewdoc = null;
			try {
				if (vs.getUrl() != null && vs.getUrl().length() > 0) {
					viewdoc = new Document(new File((new URL(vs.getUrl()).getFile())));
				}
			} catch (MalformedURLException e) {
				throw new RapidBeansRuntimeException(e);
			}
			final DocumentView view = openDocumentView(viewdoc, vs.getConfnameDoc(), vs.getConfnameView());
			if (view == null) {
				continue;
			}
			if (vs.getDividerLocation() > -1) {
				view.setDividerLocation(vs.getDividerLocation());
			}
			if (vs.getBeaneditors() != null) {
				for (final BeanEditorState bedState : vs.getBeaneditors()) {
					RapidBean bean = null;
					if (bedState.getBeanid() != null) {
						bean = viewdoc.findBean(bedState.getBeantype(), bedState.getBeanid());
					} else {
						final StringBuffer queryBuffer = new StringBuffer(bedState.getBeantype());
						queryBuffer.append("[");
						if (bedState.getPropertys() != null && bedState.getPropertys().size() > 0) {
							boolean notFirstRun = false;
							for (final PropState propState : bedState.getPropertys()) {
								if (notFirstRun) {
									queryBuffer.append(" & ");
								}
								notFirstRun = true;
								queryBuffer.append(propState.getName());
								queryBuffer.append(" = ");
								queryBuffer.append("\'");
								queryBuffer.append(propState.getValue());
								queryBuffer.append("\'");
							}
						}
						queryBuffer.append("]");
						bean = viewdoc.findBeanByQuery(queryBuffer.toString());
					}
					if (bean != null) {
						boolean show = true;
						if (view.getBeanFilter() != null && (!view.getBeanFilter().applies(bean))) {
							show = false;
						}
						if (show) {
							view.editBean(bean);
						}
					}
				}
			}
		}
		if (uiState.getHistorys() != null && uiState.getHistorys().size() > 0) {
			final HistoryState[] histStates = new HistoryState[uiState.getHistorys().size()];
			int i2 = uiState.getHistorys().size() - 1;
			for (final HistoryState histState : uiState.getHistorys()) {
				histStates[i2--] = histState;
			}
			if (this.historyDocumentOpen == null) {
				this.historyDocumentOpen = new HistoryList<Document>();
			}
			for (final HistoryState histState : histStates) {
				File histFile = null;
				try {
					histFile = new File((new URL(histState.getUrl()).getFile().replace("%20", " ")));
				} catch (MalformedURLException e) {
					throw new RapidBeansRuntimeException(e);
				}
				if (!histFile.exists()) {
					continue;
				}
				final Document histDoc = new Document(histFile);
				this.historyDocumentOpen.add(histDoc);
			}
			getMainwindow().getMenubar().updateHistoryViews();
		}
	}

	/**
	 * @return the UI state settings document URL
	 */
	private URL getUiStateURL() {
		URL url = null;
		try {
			url = new File(SettingsAll.getDirname(), "uistate.xml").toURI().toURL();
		} catch (MalformedURLException e) {
			throw new RapidBeansRuntimeException(e);
		}
		return url;
	}

	/**
	 * Tracks the history of opened documents
	 */
	private HistoryList<Document> historyDocumentOpen = null;

	private String configFilePath;

	private ResourceLoader resourceLoader;

	/**
	 * @return the history list of documents opened so far
	 */
	public HistoryList<Document> getHistoryDocumentOpen() {
		return this.historyDocumentOpen;
	}

	/**
	 * Add an URL of a document just opened to the history
	 * 
	 * @param doc the document to add to the history
	 */
	public void addDocumentOpenedToHistory(final Document doc) {
		// lazy initialization of the history list
		if (this.historyDocumentOpen == null) {
			this.historyDocumentOpen = new HistoryList<Document>(4);
		}
		if (doc.getRoot() instanceof Settings) {
			return;
		}
		this.historyDocumentOpen.add(doc);
		this.mainwindow.updateHistoryViews();
	}

	/**
	 * @return the configFilePath
	 */
	public String getConfigFilePath() {
		return configFilePath;
	}

	/**
	 * @param configFilePath the configFilePath to set
	 */
	protected void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	/**
	 * @return the resourceLoader
	 */
	public synchronized ResourceLoader getResourceLoader() {
		if (this.resourceLoader == null) {
			this.resourceLoader = new ResourceLoader();
		}
		return this.resourceLoader;
	}

	/**
	 * @param resourceLoader the resourceLoader to set
	 */
	protected void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
