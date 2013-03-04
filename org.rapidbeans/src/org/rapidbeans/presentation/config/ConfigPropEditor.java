/*
 * Partially generated code file: ConfigPropEditor.java
 * !!!Do only edit manually in marked sections!!!
 * 
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 * 
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.config.ConfigPropEditor
 * 
 * model: model/org/rapidbeans/presentation/config/ConfigPropEditor.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.config;

// BEGIN manual code section
// ConfigPropEditor.import
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypeRapidBean;

// END manual code section

/**
 * Rapid Bean class: ConfigPropEditor. Partially generated Java class !!!Do only
 * edit manually in marked sections!!!
 **/
public class ConfigPropEditor extends RapidBeanImplStrict {
	// BEGIN manual code section
	// ConfigPropEditor.classBody
	// END manual code section

	/**
	 * property "editorclass".
	 */
	private org.rapidbeans.core.basic.PropertyString editorclass;

	/**
	 * property "basepackage".
	 */
	private org.rapidbeans.core.basic.PropertyString basepackage;

	/**
	 * property "classnamepart".
	 */
	private org.rapidbeans.core.basic.PropertyString classnamepart;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.editorclass = (org.rapidbeans.core.basic.PropertyString) this.getProperty("editorclass");
		this.basepackage = (org.rapidbeans.core.basic.PropertyString) this.getProperty("basepackage");
		this.classnamepart = (org.rapidbeans.core.basic.PropertyString) this.getProperty("classnamepart");
	}

	/**
	 * default constructor.
	 */
	public ConfigPropEditor() {
		super();
		// BEGIN manual code section
		// ConfigPropEditor.ConfigPropEditor()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * 
	 * @param s
	 *            the string
	 */
	public ConfigPropEditor(final String s) {
		super(s);
		// BEGIN manual code section
		// ConfigPropEditor.ConfigPropEditor(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * 
	 * @param sa
	 *            the string array
	 */
	public ConfigPropEditor(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// ConfigPropEditor.ConfigPropEditor(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(ConfigPropEditor.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
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
	 * 
	 * @param argValue
	 *            value of Property 'editorclass' to set
	 */
	public void setEditorclass(final String argValue) {
		this.editorclass.setValue(argValue);
	}

	/**
	 * @return value of Property 'basepackage'
	 */
	public String getBasepackage() {
		try {
			return (String) this.basepackage.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("basepackage");
		}
	}

	/**
	 * setter for Property 'basepackage'.
	 * 
	 * @param argValue
	 *            value of Property 'basepackage' to set
	 */
	public void setBasepackage(final String argValue) {
		this.basepackage.setValue(argValue);
	}

	/**
	 * @return value of Property 'classnamepart'
	 */
	public String getClassnamepart() {
		try {
			return (String) this.classnamepart.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("classnamepart");
		}
	}

	/**
	 * setter for Property 'classnamepart'.
	 * 
	 * @param argValue
	 *            value of Property 'classnamepart' to set
	 */
	public void setClassnamepart(final String argValue) {
		this.classnamepart.setValue(argValue);
	}
}
