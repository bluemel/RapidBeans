/*
 * Partially generated code file: ConfigDocument.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans 
				bean
			 generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans 
				bean
			 type
 * org.rapidbeans.presentation.config.ConfigDocument
 * 
 * model:    model/org/rapidbeans/presentation/config/ConfigDocument.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.config;



// BEGIN manual code section
// ConfigDocument.import
import java.util.Collection;

import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypeRapidBean;

// END manual code section

/**
 * Rapid Bean class: ConfigDocument.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class ConfigDocument extends RapidBeanImplStrict {
	// BEGIN manual code section
	// ConfigDocument.classBody
	public static final String NAME_NO_CONFIG = "no_conf";

	/**
	 * @param viewconfname
	 *            the view configuration name
	 * @return the configuration of the document view with the given name or
	 *         null if not found
	 */
	public final ConfigView getConfigView(final String viewconfname) {
		final Collection<ConfigView> viewconfs = this.getViews();
		if (viewconfs == null) {
			return null;
		}
		for (ConfigView conf : viewconfs) {
			if (conf.getName().equals(viewconfname)) {
				return conf;
			}
		}
		return null;
	}

	// END manual code section

	/**
	 * property "name".
	 */
	private org.rapidbeans.core.basic.PropertyString name;

	/**
	 * property "rootclass".
	 */
	private org.rapidbeans.core.basic.PropertyString rootclass;

	/**
	 * property "defaultview".
	 */
	private org.rapidbeans.core.basic.PropertyString defaultview;

	/**
	 * property "readaccessrolesrequired".
	 */
	private org.rapidbeans.core.basic.PropertyChoice readaccessrolesrequired;

	/**
	 * property "writeaccessrolesrequired".
	 */
	private org.rapidbeans.core.basic.PropertyChoice writeaccessrolesrequired;

	/**
	 * property "views".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend views;

	/**
	 * property "filterrules".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend filterrules;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.name = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("name");
		this.rootclass = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("rootclass");
		this.defaultview = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("defaultview");
		this.readaccessrolesrequired = (org.rapidbeans.core.basic.PropertyChoice)
			this.getProperty("readaccessrolesrequired");
		this.writeaccessrolesrequired = (org.rapidbeans.core.basic.PropertyChoice)
			this.getProperty("writeaccessrolesrequired");
		this.views = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("views");
		this.filterrules = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("filterrules");
	}

	/**
	 * default constructor.
	 */
	public ConfigDocument() {
		super();
		// BEGIN manual code section
		// ConfigDocument.ConfigDocument()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public ConfigDocument(final String s) {
		super(s);
		// BEGIN manual code section
		// ConfigDocument.ConfigDocument(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public ConfigDocument(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// ConfigDocument.ConfigDocument(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(ConfigDocument.class);

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
	 * @return value of Property 'rootclass'
	 */
	public String getRootclass() {
		try {
			return (String) this.rootclass.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("rootclass");
		}
	}

	/**
	 * setter for Property 'rootclass'.
	 * @param argValue
	 *            value of Property 'rootclass' to set
	 */
	public void setRootclass(final String argValue) {
		this.rootclass.setValue(argValue);
	}

	/**
	 * @return value of Property 'defaultview'
	 */
	public String getDefaultview() {
		try {
			return (String) this.defaultview.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("defaultview");
		}
	}

	/**
	 * setter for Property 'defaultview'.
	 * @param argValue
	 *            value of Property 'defaultview' to set
	 */
	public void setDefaultview(final String argValue) {
		this.defaultview.setValue(argValue);
	}

	/**
	 * @return value of Property 'readaccessrolesrequired'
	 */
	@SuppressWarnings("unchecked")
	public java.util.List<org.rapidbeans.core.basic.RapidEnum> getReadaccessrolesrequired() {
		try {
			return (java.util.List<org.rapidbeans.core.basic.RapidEnum>) this.readaccessrolesrequired.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("readaccessrolesrequired");
		}
	}

	/**
	 * setter for Property 'readaccessrolesrequired'.
	 * @param argValue
	 *            value of Property 'readaccessrolesrequired' to set
	 */
	public void setReadaccessrolesrequired(final java.util.List<org.rapidbeans.core.basic.RapidEnum> argValue) {
		this.readaccessrolesrequired.setValue(argValue);
	}

	/**
	 * @return value of Property 'writeaccessrolesrequired'
	 */
	@SuppressWarnings("unchecked")
	public java.util.List<org.rapidbeans.core.basic.RapidEnum> getWriteaccessrolesrequired() {
		try {
			return (java.util.List<org.rapidbeans.core.basic.RapidEnum>) this.writeaccessrolesrequired.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("writeaccessrolesrequired");
		}
	}

	/**
	 * setter for Property 'writeaccessrolesrequired'.
	 * @param argValue
	 *            value of Property 'writeaccessrolesrequired' to set
	 */
	public void setWriteaccessrolesrequired(final java.util.List<org.rapidbeans.core.basic.RapidEnum> argValue) {
		this.writeaccessrolesrequired.setValue(argValue);
	}

	/**
	 * @return value of Property 'views'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigView> getViews() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigView>)
			this.views.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("views");
		}
	}

	/**
	 * setter for Property 'views'.
	 * @param argValue
	 *            value of Property 'views' to set
	 */
	public void setViews(final java.util.Collection<org.rapidbeans.presentation.config.ConfigView> argValue) {
		this.views.setValue(argValue);
	}
	/**
	 * add method for Property 'views'.
	 * @param bean
	 *            the bean to add
	 */
	public void addView(final org.rapidbeans.presentation.config.ConfigView bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.views).addLink(bean);
	}
	/**
	 * remove method for Property 'views'.
	 * @param bean
	 *            the bean to remove
	 */
	public void removeView(final org.rapidbeans.presentation.config.ConfigView bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.views).removeLink(bean);
	}

	/**
	 * @return value of Property 'filterrules'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigFilterRuleRoleBased> getFilterrules() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigFilterRuleRoleBased>)
			this.filterrules.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("filterrules");
		}
	}

	/**
	 * setter for Property 'filterrules'.
	 * @param argValue
	 *            value of Property 'filterrules' to set
	 */
	public void setFilterrules(final java.util.Collection<org.rapidbeans.presentation.config.ConfigFilterRuleRoleBased> argValue) {
		this.filterrules.setValue(argValue);
	}
	/**
	 * add method for Property 'filterrules'.
	 * @param bean
	 *            the bean to add
	 */
	public void addFilterrule(final org.rapidbeans.presentation.config.ConfigFilterRuleRoleBased bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.filterrules).addLink(bean);
	}
	/**
	 * remove method for Property 'filterrules'.
	 * @param bean
	 *            the bean to remove
	 */
	public void removeFilterrule(final org.rapidbeans.presentation.config.ConfigFilterRuleRoleBased bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.filterrules).removeLink(bean);
	}
}
