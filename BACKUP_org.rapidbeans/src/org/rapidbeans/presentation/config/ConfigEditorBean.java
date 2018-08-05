/*
 * Partially generated code file: ConfigEditorBean.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans 
				bean
			 generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans 
				bean
			 type
 * org.rapidbeans.presentation.config.ConfigEditorBean
 * 
 * model:    model/org/rapidbeans/presentation/config/ConfigEditorBean.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.config;



// BEGIN manual code section
// ConfigEditorBean.import
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypeRapidBean;

// END manual code section

/**
 * Rapid Bean class: ConfigEditorBean.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class ConfigEditorBean extends RapidBeanImplStrict {
	// BEGIN manual code section
	// ConfigEditorBean.classBody
	/**
	 * Since we do not have a HashMap.
	 * 
	 * @param s
	 *            the String key
	 * 
	 * @return the editor configuration
	 */
	public ConfigPropEditorBean getPropertycfg(final String s) {
		ConfigPropEditorBean propcfg = null;
		for (ConfigPropEditorBean cfg : this.getPropertycfgs()) {
			if (cfg.getName().equals(s)) {
				propcfg = cfg;
				break;
			}
		}
		return propcfg;
	}

	// END manual code section

	/**
	 * property "beantype".
	 */
	private org.rapidbeans.core.basic.PropertyString beantype;

	/**
	 * property "editorclass".
	 */
	private org.rapidbeans.core.basic.PropertyString editorclass;

	/**
	 * property "propertycfgs".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend propertycfgs;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.beantype = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("beantype");
		this.editorclass = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("editorclass");
		this.propertycfgs = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("propertycfgs");
	}

	/**
	 * default constructor.
	 */
	public ConfigEditorBean() {
		super();
		// BEGIN manual code section
		// ConfigEditorBean.ConfigEditorBean()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public ConfigEditorBean(final String s) {
		super(s);
		// BEGIN manual code section
		// ConfigEditorBean.ConfigEditorBean(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public ConfigEditorBean(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// ConfigEditorBean.ConfigEditorBean(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(ConfigEditorBean.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}

	/**
	 * @return value of Property 'beantype'
	 */
	public String getBeantype() {
		try {
			return (String) this.beantype.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("beantype");
		}
	}

	/**
	 * setter for Property 'beantype'.
	 * @param argValue
	 *            value of Property 'beantype' to set
	 */
	public void setBeantype(final String argValue) {
		this.beantype.setValue(argValue);
	}

	/**
	 * @return value of Property 'editorclass'
	 */
	public String getEditorclass() {
		try {
			return (String) this.editorclass.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("editorclass");
		}
	}

	/**
	 * setter for Property 'editorclass'.
	 * @param argValue
	 *            value of Property 'editorclass' to set
	 */
	public void setEditorclass(final String argValue) {
		this.editorclass.setValue(argValue);
	}

	/**
	 * @return value of Property 'propertycfgs'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigPropEditorBean> getPropertycfgs() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigPropEditorBean>)
			this.propertycfgs.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("propertycfgs");
		}
	}

	/**
	 * setter for Property 'propertycfgs'.
	 * @param argValue
	 *            value of Property 'propertycfgs' to set
	 */
	public void setPropertycfgs(final java.util.Collection<org.rapidbeans.presentation.config.ConfigPropEditorBean> argValue) {
		this.propertycfgs.setValue(argValue);
	}
	/**
	 * add method for Property 'propertycfgs'.
	 * @param bean
	 *            the bean to add
	 */
	public void addPropertycfg(final org.rapidbeans.presentation.config.ConfigPropEditorBean bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.propertycfgs).addLink(bean);
	}
	/**
	 * remove method for Property 'propertycfgs'.
	 * @param bean
	 *            the bean to remove
	 */
	public void removePropertycfg(final org.rapidbeans.presentation.config.ConfigPropEditorBean bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.propertycfgs).removeLink(bean);
	}
}
