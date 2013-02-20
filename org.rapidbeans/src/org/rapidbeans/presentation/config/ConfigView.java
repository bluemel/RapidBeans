/*
 * Partially generated code file: ConfigView.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.config.ConfigView
 * 
 * model:    model/org/rapidbeans/presentation/config/ConfigView.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.config;



// BEGIN manual code section
// ConfigView.import
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypeRapidBean;

// END manual code section

/**
 * Rapid Bean class: ConfigView.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class ConfigView extends RapidBeanImplStrict {
	// BEGIN manual code section
	// ConfigView.classBody
	public static final String NAME_NO_CONFIG = "no_conf";

	/**
	 * @return the complete name <b>&lt;parent doc name>.&lt;view name></b>
	 */
	public String getNameComplete() {
		return ((ConfigDocument) this.getParentBean()).getName()
				+ "." + this.getName();
	}

	// END manual code section

	/**
	 * property "name".
	 */
	private org.rapidbeans.core.basic.PropertyString name;

	/**
	 * property "persistencestrategy".
	 */
	private org.rapidbeans.core.basic.PropertyChoice persistencestrategy;

	/**
	 * property "viewclass".
	 */
	private org.rapidbeans.core.basic.PropertyString viewclass;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.name = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("name");
		this.persistencestrategy = (org.rapidbeans.core.basic.PropertyChoice)
			this.getProperty("persistencestrategy");
		this.viewclass = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("viewclass");
	}

	/**
	 * default constructor.
	 */
	public ConfigView() {
		super();
		// BEGIN manual code section
		// ConfigView.ConfigView()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public ConfigView(final String s) {
		super(s);
		// BEGIN manual code section
		// ConfigView.ConfigView(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public ConfigView(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// ConfigView.ConfigView(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(ConfigView.class);

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
	public void setName(
		final String argValue) {
		this.name.setValue(argValue);
	}

	/**
	 * @return value of Property 'persistencestrategy'
	 */
	public org.rapidbeans.presentation.config.ConfigPropPersistencestrategy getPersistencestrategy() {
		try {
			java.util.List<?> enumList = (java.util.List<?>) this.persistencestrategy.getValue();
			if (enumList == null || enumList.size() == 0) {
				return null;
			} else {
				return (org.rapidbeans.presentation.config.ConfigPropPersistencestrategy) enumList.get(0);
						}			
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("persistencestrategy");
		}
	}

	/**
	 * setter for Property 'persistencestrategy'.
	 * @param argValue
	 *            value of Property 'persistencestrategy' to set
	 */
	public void setPersistencestrategy(
		final org.rapidbeans.presentation.config.ConfigPropPersistencestrategy argValue) {
		java.util.List<org.rapidbeans.presentation.config.ConfigPropPersistencestrategy> list =
			new java.util.ArrayList<org.rapidbeans.presentation.config.ConfigPropPersistencestrategy>();
		list.add(argValue);
		this.persistencestrategy.setValue(list);
	}

	/**
	 * @return value of Property 'viewclass'
	 */
	public String getViewclass() {
		try {
			return (String) this.viewclass.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("viewclass");
		}
	}

	/**
	 * setter for Property 'viewclass'.
	 * @param argValue
	 *            value of Property 'viewclass' to set
	 */
	public void setViewclass(
		final String argValue) {
		this.viewclass.setValue(argValue);
	}
}
