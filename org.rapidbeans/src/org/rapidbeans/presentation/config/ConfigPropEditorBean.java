/*
 * Partially generated code file: ConfigPropEditorBean.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans 
				bean
			 generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans 
				bean
			 type
 * org.rapidbeans.presentation.config.ConfigPropEditorBean
 * 
 * model:    model/org/rapidbeans/presentation/config/ConfigPropEditorBean.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.config;



// BEGIN manual code section
// ConfigPropEditorBean.import
import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.LinkFrozen;
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.exception.UnresolvedLinkException;
import org.rapidbeans.core.type.TypeRapidBean;

// END manual code section

/**
 * Rapid Bean class: ConfigPropEditorBean.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class ConfigPropEditorBean extends RapidBeanImplStrict {
	// BEGIN manual code section
	// ConfigPropEditorBean.classBody
	/**
	 * Retrieve and return the argument value for a given name.
	 * 
	 * @param the
	 *            name of the argument to get the value for
	 * 
	 * @return the argument value for the given name
	 */
	public String getArgumentValue(final String name) {
		if (this.getArguments() != null) {
			for (ConfigPropEditorArgument arg : this.getArguments()) {
				if (arg.getName().equals(name)) {
					return arg.getValue();
				}
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
	 * property "enabled".
	 */
	private org.rapidbeans.core.basic.PropertyBoolean enabled;

	/**
	 * property "editor".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend editor;

	/**
	 * property "nullbehaviour".
	 */
	private org.rapidbeans.core.basic.PropertyChoice nullbehaviour;

	/**
	 * property "detail".
	 */
	private org.rapidbeans.core.basic.PropertyString detail;

	/**
	 * property "arguments".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend arguments;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.name = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("name");
		this.enabled = (org.rapidbeans.core.basic.PropertyBoolean)
			this.getProperty("enabled");
		this.editor = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("editor");
		this.nullbehaviour = (org.rapidbeans.core.basic.PropertyChoice)
			this.getProperty("nullbehaviour");
		this.detail = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("detail");
		this.arguments = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("arguments");
	}

	/**
	 * default constructor.
	 */
	public ConfigPropEditorBean() {
		super();
		// BEGIN manual code section
		// ConfigPropEditorBean.ConfigPropEditorBean()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public ConfigPropEditorBean(final String s) {
		super(s);
		// BEGIN manual code section
		// ConfigPropEditorBean.ConfigPropEditorBean(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public ConfigPropEditorBean(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// ConfigPropEditorBean.ConfigPropEditorBean(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(ConfigPropEditorBean.class);

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
	 * @return value of Property 'enabled'
	 */
	public boolean getEnabled() {
		try {
			return ((org.rapidbeans.core.basic.PropertyBoolean) this.enabled).getValueBoolean();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("enabled");
		}
	}

	/**
	 * setter for Property 'enabled'.
	 * @param argValue
	 *            value of Property 'enabled' to set
	 */
	public void setEnabled(final boolean argValue) {
		this.enabled.setValue(new Boolean(argValue));
	}

	/**
	 * @return value of Property 'editor'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.presentation.config.ConfigPropEditor getEditor() {
		try {
			org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigPropEditor> col
				= (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigPropEditor>) this.editor.getValue();
			if (col == null || col.size() == 0) {
				return null;
			} else {
				Link link = (Link) col.iterator().next();
				if (link instanceof LinkFrozen) {
					throw new UnresolvedLinkException("unresolved link to \""
							+ "org.rapidbeans.presentation.config.ConfigPropEditor"
							+ "\" \"" + link.getIdString() + "\"");
				} else {
					return (org.rapidbeans.presentation.config.ConfigPropEditor) col.iterator().next();
				}
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("editor");
		}
	}

	/**
	 * setter for Property 'editor'.
	 * @param argValue
	 *            value of Property 'editor' to set
	 */
	public void setEditor(final org.rapidbeans.presentation.config.ConfigPropEditor argValue) {
		this.editor.setValue(argValue);
	}

	/**
	 * @return value of Property 'nullbehaviour'
	 */
	public org.rapidbeans.presentation.config.EditorPropNullBehaviour getNullbehaviour() {
		try {
			java.util.List<?> enumList = (java.util.List<?>) this.nullbehaviour.getValue();
			if (enumList == null || enumList.size() == 0) {
				return null;
			} else {
				return (org.rapidbeans.presentation.config.EditorPropNullBehaviour) enumList.get(0);
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("nullbehaviour");
		}
	}

	/**
	 * setter for Property 'nullbehaviour'.
	 * @param argValue
	 *            value of Property 'nullbehaviour' to set
	 */
	public void setNullbehaviour(final org.rapidbeans.presentation.config.EditorPropNullBehaviour argValue) {
		java.util.List<org.rapidbeans.presentation.config.EditorPropNullBehaviour> list =
			new java.util.ArrayList<org.rapidbeans.presentation.config.EditorPropNullBehaviour>();
		list.add(argValue);
		this.nullbehaviour.setValue(list);
	}

	/**
	 * @return value of Property 'detail'
	 */
	public String getDetail() {
		try {
			return (String) this.detail.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("detail");
		}
	}

	/**
	 * setter for Property 'detail'.
	 * @param argValue
	 *            value of Property 'detail' to set
	 */
	public void setDetail(final String argValue) {
		this.detail.setValue(argValue);
	}

	/**
	 * @return value of Property 'arguments'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigPropEditorArgument> getArguments() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.config.ConfigPropEditorArgument>)
			this.arguments.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("arguments");
		}
	}

	/**
	 * setter for Property 'arguments'.
	 * @param argValue
	 *            value of Property 'arguments' to set
	 */
	public void setArguments(final java.util.Collection<org.rapidbeans.presentation.config.ConfigPropEditorArgument> argValue) {
		this.arguments.setValue(argValue);
	}
	/**
	 * add method for Property 'arguments'.
	 * @param bean
	 *            the bean to add
	 */
	public void addArgument(final org.rapidbeans.presentation.config.ConfigPropEditorArgument bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.arguments).addLink(bean);
	}
	/**
	 * remove method for Property 'arguments'.
	 * @param bean
	 *            the bean to remove
	 */
	public void removeArgument(final org.rapidbeans.presentation.config.ConfigPropEditorArgument bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.arguments).removeLink(bean);
	}
}
