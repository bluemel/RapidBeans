/*
 * Partially generated code file: ConfigApplication.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans 
				bean
			 generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans 
				bean
			 type
 * org.rapidbeans.presentation.config.ConfigApplication
 * 
 * model:    model/org/rapidbeans/presentation/config/ConfigApplication.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.config;



// BEGIN manual code section
// ConfigApplication.import
import java.util.Collection;

import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.LinkFrozen;
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.exception.UnresolvedLinkException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.datasource.Document;

// END manual code section

/**
 * Rapid Bean class: ConfigApplication.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class ConfigApplication extends RapidBeanImplStrict {
	// BEGIN manual code section
	// ConfigApplication.classBody
	/**
	 * as long as we do not have a map we have to iterate here
	 * 
	 * @param docconfname
	 *            the document configuration name
	 * @return the configuration of the document with the given name or null if
	 *         not found
	 */
	public final ConfigDocument getConfigDocument(final String docconfname) {
		final Collection<ConfigDocument> docconfs = this.getDocuments();
		if (docconfs == null) {
			return null;
		}
		for (ConfigDocument conf : docconfs) {
			if (conf.getName().equals(docconfname)) {
				return conf;
			}
		}
		return null;
	}

	/**
	 * searches the appropriate configuration for a given document.
	 * 
	 * @param doc
	 *            the document
	 * @return the document configuration which "rootclass" matches the given
	 *         document's root bean class or null if not found
	 */
	public final ConfigDocument getConfigDocument(final Document doc) {
		final Collection<ConfigDocument> docconfs = this.getDocuments();
		if (docconfs == null) {
			return null;
		}
		final TypeRapidBean rootBeanType = doc.getRoot().getType();
		for (ConfigDocument conf : docconfs) {
			if (TypeRapidBean.isSameOrSubtype(TypeRapidBean.forName(conf.getRootclass()), rootBeanType)) {
				return conf;
			}
		}
		return null;
	}

	/**
	 * @param docconfname
	 *            the document configuration name
	 * @param viewconfname
	 *            the view configuration name
	 * 
	 * @return the configuration of the document with the given name or null if
	 *         not found
	 */
	public final ConfigView getConfigView(final String docconfname, final String viewconfname) {
		final ConfigDocument docconf = this.getConfigDocument(docconfname);
		if (docconf == null) {
			return null;
		}
		return docconf.getConfigView(viewconfname);
	}

	// END manual code section

	/**
	 * property "name".
	 */
	private org.rapidbeans.core.basic.PropertyString name;

	/**
	 * property "rootpackage".
	 */
	private org.rapidbeans.core.basic.PropertyString rootpackage;

	/**
	 * property "applicationclass".
	 */
	private org.rapidbeans.core.basic.PropertyString applicationclass;

	/**
	 * property "guitype".
	 */
	private org.rapidbeans.core.basic.PropertyChoice guitype;

	/**
	 * property "defaultlocale".
	 */
	private org.rapidbeans.core.basic.PropertyString defaultlocale;

	/**
	 * property "defaultcharset".
	 */
	private org.rapidbeans.core.basic.PropertyString defaultcharset;

	/**
	 * property "locales".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend locales;

	/**
	 * property "branding".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend branding;

	/**
	 * property "beaneditors".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend beaneditors;

	/**
	 * property "documents".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend documents;

	/**
	 * property "actions".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend actions;

	/**
	 * property "mainwindow".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend mainwindow;

	/**
	 * property "authorization".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend authorization;

	/**
	 * property "currency".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend currency;

	/**
	 * property "xmlbinding".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend xmlbinding;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.name = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("name");
		this.rootpackage = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("rootpackage");
		this.applicationclass = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("applicationclass");
		this.guitype = (org.rapidbeans.core.basic.PropertyChoice)
			this.getProperty("guitype");
		this.defaultlocale = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("defaultlocale");
		this.defaultcharset = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("defaultcharset");
		this.locales = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("locales");
		this.branding = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("branding");
		this.beaneditors = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("beaneditors");
		this.documents = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("documents");
		this.actions = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("actions");
		this.mainwindow = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("mainwindow");
		this.authorization = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("authorization");
		this.currency = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("currency");
		this.xmlbinding = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("xmlbinding");
	}

	/**
	 * default constructor.
	 */
	public ConfigApplication() {
		super();
		// BEGIN manual code section
		// ConfigApplication.ConfigApplication()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public ConfigApplication(final String s) {
		super(s);
		// BEGIN manual code section
		// ConfigApplication.ConfigApplication(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public ConfigApplication(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// ConfigApplication.ConfigApplication(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(ConfigApplication.class);

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
	 * @return value of Property 'rootpackage'
	 */
	public String getRootpackage() {
		try {
			return (String) this.rootpackage.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("rootpackage");
		}
	}

	/**
	 * setter for Property 'rootpackage'.
	 * @param argValue
	 *            value of Property 'rootpackage' to set
	 */
	public void setRootpackage(final String argValue) {
		this.rootpackage.setValue(argValue);
	}

	/**
	 * @return value of Property 'applicationclass'
	 */
	public String getApplicationclass() {
		try {
			return (String) this.applicationclass.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("applicationclass");
		}
	}

	/**
	 * setter for Property 'applicationclass'.
	 * @param argValue
	 *            value of Property 'applicationclass' to set
	 */
	public void setApplicationclass(final String argValue) {
		this.applicationclass.setValue(argValue);
	}

	/**
	 * @return value of Property 'guitype'
	 */
	public org.rapidbeans.presentation.config.ApplicationGuiType getGuitype() {
		try {
			java.util.List<?> enumList = (java.util.List<?>) this.guitype.getValue();
			if (enumList == null || enumList.size() == 0) {
				return null;
			} else {
				return (org.rapidbeans.presentation.config.ApplicationGuiType) enumList.get(0);
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("guitype");
		}
	}

	/**
	 * setter for Property 'guitype'.
	 * @param argValue
	 *            value of Property 'guitype' to set
	 */
	public void setGuitype(final org.rapidbeans.presentation.config.ApplicationGuiType argValue) {
		java.util.List<org.rapidbeans.presentation.config.ApplicationGuiType> list =
			new java.util.ArrayList<org.rapidbeans.presentation.config.ApplicationGuiType>();
		list.add(argValue);
		this.guitype.setValue(list);
	}

	/**
	 * @return value of Property 'defaultlocale'
	 */
	public String getDefaultlocale() {
		try {
			return (String) this.defaultlocale.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("defaultlocale");
		}
	}

	/**
	 * setter for Property 'defaultlocale'.
	 * @param argValue
	 *            value of Property 'defaultlocale' to set
	 */
	public void setDefaultlocale(final String argValue) {
		this.defaultlocale.setValue(argValue);
	}

	/**
	 * @return value of Property 'defaultcharset'
	 */
	public String getDefaultcharset() {
		try {
			return (String) this.defaultcharset.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("defaultcharset");
		}
	}

	/**
	 * setter for Property 'defaultcharset'.
	 * @param argValue
	 *            value of Property 'defaultcharset' to set
	 */
	public void setDefaultcharset(final String argValue) {
		this.defaultcharset.setValue(argValue);
	}

	/**
	 * @return value of Property 'locales'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigLocale> getLocales() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigLocale>)
			this.locales.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("locales");
		}
	}

	/**
	 * setter for Property 'locales'.
	 * @param argValue
	 *            value of Property 'locales' to set
	 */
	public void setLocales(final java.util.Collection<org.rapidbeans.presentation.config.ConfigLocale> argValue) {
		this.locales.setValue(argValue);
	}
	/**
	 * add method for Property 'locales'.
	 * @param bean the bean to add
	 */
	public void addLocale(final org.rapidbeans.presentation.config.ConfigLocale bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.locales).addLink(bean);
	}
	/**
	 * remove method for Property 'locales'.
	 * @param bean the bean to add
	 */
	public void removeLocale(final org.rapidbeans.presentation.config.ConfigLocale bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.locales).removeLink(bean);
	}

	/**
	 * @return value of Property 'branding'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.presentation.config.ConfigBranding getBranding() {
		try {
			org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigBranding> col
				= (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigBranding>) this.branding.getValue();
			if (col == null || col.size() == 0) {
				return null;
			} else {
				Link link = (Link) col.iterator().next();
				if (link instanceof LinkFrozen) {
					throw new UnresolvedLinkException("unresolved link to \""
							+ "org.rapidbeans.presentation.config.ConfigBranding"
							+ "\" \"" + link.getIdString() + "\"");
				} else {
					return (org.rapidbeans.presentation.config.ConfigBranding) col.iterator().next();
				}
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("branding");
		}
	}

	/**
	 * setter for Property 'branding'.
	 * @param argValue
	 *            value of Property 'branding' to set
	 */
	public void setBranding(final org.rapidbeans.presentation.config.ConfigBranding argValue) {
		this.branding.setValue(argValue);
	}

	/**
	 * @return value of Property 'beaneditors'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigEditorBean> getBeaneditors() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigEditorBean>)
			this.beaneditors.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("beaneditors");
		}
	}

	/**
	 * setter for Property 'beaneditors'.
	 * @param argValue
	 *            value of Property 'beaneditors' to set
	 */
	public void setBeaneditors(final java.util.Collection<org.rapidbeans.presentation.config.ConfigEditorBean> argValue) {
		this.beaneditors.setValue(argValue);
	}
	/**
	 * add method for Property 'beaneditors'.
	 * @param bean the bean to add
	 */
	public void addBeaneditor(final org.rapidbeans.presentation.config.ConfigEditorBean bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.beaneditors).addLink(bean);
	}
	/**
	 * remove method for Property 'beaneditors'.
	 * @param bean the bean to add
	 */
	public void removeBeaneditor(final org.rapidbeans.presentation.config.ConfigEditorBean bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.beaneditors).removeLink(bean);
	}

	/**
	 * @return value of Property 'documents'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigDocument> getDocuments() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigDocument>)
			this.documents.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("documents");
		}
	}

	/**
	 * setter for Property 'documents'.
	 * @param argValue
	 *            value of Property 'documents' to set
	 */
	public void setDocuments(final java.util.Collection<org.rapidbeans.presentation.config.ConfigDocument> argValue) {
		this.documents.setValue(argValue);
	}
	/**
	 * add method for Property 'documents'.
	 * @param bean the bean to add
	 */
	public void addDocument(final org.rapidbeans.presentation.config.ConfigDocument bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.documents).addLink(bean);
	}
	/**
	 * remove method for Property 'documents'.
	 * @param bean the bean to add
	 */
	public void removeDocument(final org.rapidbeans.presentation.config.ConfigDocument bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.documents).removeLink(bean);
	}

	/**
	 * @return value of Property 'actions'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.service.Action> getActions() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.service.Action>)
			this.actions.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("actions");
		}
	}

	/**
	 * setter for Property 'actions'.
	 * @param argValue
	 *            value of Property 'actions' to set
	 */
	public void setActions(final java.util.Collection<org.rapidbeans.service.Action> argValue) {
		this.actions.setValue(argValue);
	}
	/**
	 * add method for Property 'actions'.
	 * @param bean the bean to add
	 */
	public void addAction(final org.rapidbeans.service.Action bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.actions).addLink(bean);
	}
	/**
	 * remove method for Property 'actions'.
	 * @param bean the bean to add
	 */
	public void removeAction(final org.rapidbeans.service.Action bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.actions).removeLink(bean);
	}

	/**
	 * @return value of Property 'mainwindow'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.presentation.config.ConfigMainWindow getMainwindow() {
		try {
			org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigMainWindow> col
				= (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigMainWindow>) this.mainwindow.getValue();
			if (col == null || col.size() == 0) {
				return null;
			} else {
				Link link = (Link) col.iterator().next();
				if (link instanceof LinkFrozen) {
					throw new UnresolvedLinkException("unresolved link to \""
							+ "org.rapidbeans.presentation.config.ConfigMainWindow"
							+ "\" \"" + link.getIdString() + "\"");
				} else {
					return (org.rapidbeans.presentation.config.ConfigMainWindow) col.iterator().next();
				}
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("mainwindow");
		}
	}

	/**
	 * setter for Property 'mainwindow'.
	 * @param argValue
	 *            value of Property 'mainwindow' to set
	 */
	public void setMainwindow(final org.rapidbeans.presentation.config.ConfigMainWindow argValue) {
		this.mainwindow.setValue(argValue);
	}

	/**
	 * @return value of Property 'authorization'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.presentation.config.ConfigAuthorization getAuthorization() {
		try {
			org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigAuthorization> col
				= (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigAuthorization>) this.authorization.getValue();
			if (col == null || col.size() == 0) {
				return null;
			} else {
				Link link = (Link) col.iterator().next();
				if (link instanceof LinkFrozen) {
					throw new UnresolvedLinkException("unresolved link to \""
							+ "org.rapidbeans.presentation.config.ConfigAuthorization"
							+ "\" \"" + link.getIdString() + "\"");
				} else {
					return (org.rapidbeans.presentation.config.ConfigAuthorization) col.iterator().next();
				}
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("authorization");
		}
	}

	/**
	 * setter for Property 'authorization'.
	 * @param argValue
	 *            value of Property 'authorization' to set
	 */
	public void setAuthorization(final org.rapidbeans.presentation.config.ConfigAuthorization argValue) {
		this.authorization.setValue(argValue);
	}

	/**
	 * @return value of Property 'currency'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.presentation.config.ConfigCurrency getCurrency() {
		try {
			org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigCurrency> col
				= (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigCurrency>) this.currency.getValue();
			if (col == null || col.size() == 0) {
				return null;
			} else {
				Link link = (Link) col.iterator().next();
				if (link instanceof LinkFrozen) {
					throw new UnresolvedLinkException("unresolved link to \""
							+ "org.rapidbeans.presentation.config.ConfigCurrency"
							+ "\" \"" + link.getIdString() + "\"");
				} else {
					return (org.rapidbeans.presentation.config.ConfigCurrency) col.iterator().next();
				}
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("currency");
		}
	}

	/**
	 * setter for Property 'currency'.
	 * @param argValue
	 *            value of Property 'currency' to set
	 */
	public void setCurrency(final org.rapidbeans.presentation.config.ConfigCurrency argValue) {
		this.currency.setValue(argValue);
	}

	/**
	 * @return value of Property 'xmlbinding'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.presentation.config.ConfigXmlBinding getXmlbinding() {
		try {
			org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigXmlBinding> col
				= (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigXmlBinding>) this.xmlbinding.getValue();
			if (col == null || col.size() == 0) {
				return null;
			} else {
				Link link = (Link) col.iterator().next();
				if (link instanceof LinkFrozen) {
					throw new UnresolvedLinkException("unresolved link to \""
							+ "org.rapidbeans.presentation.config.ConfigXmlBinding"
							+ "\" \"" + link.getIdString() + "\"");
				} else {
					return (org.rapidbeans.presentation.config.ConfigXmlBinding) col.iterator().next();
				}
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("xmlbinding");
		}
	}

	/**
	 * setter for Property 'xmlbinding'.
	 * @param argValue
	 *            value of Property 'xmlbinding' to set
	 */
	public void setXmlbinding(final org.rapidbeans.presentation.config.ConfigXmlBinding argValue) {
		this.xmlbinding.setValue(argValue);
	}
}
