/*
 * Partially generated code file: Settings.java
 * !!!Do only edit manually in marked sections!!!
 * 
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 * 
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.settings.Settings
 * 
 * model: model/org/rapidbeans/presentation/settings/Settings.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.settings;

// BEGIN manual code section
// Settings.import
import java.util.ArrayList;

import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypeRapidBean;

// END manual code section

/**
 * Rapid Bean class: Settings. Partially generated Java class !!!Do only edit
 * manually in marked sections!!!
 **/
public class Settings extends RapidBeanImplStrict {
	// BEGIN manual code section
	// Settings.classBody
	/**
	 * the signature.
	 */
	private String signature = null;

	/**
	 * @return the signature
	 */
	public String getSignature() {
		if (this.signature == null) {
			this.initSignature();
		}
		return this.signature;
	}

	/**
	 * initialize the settigs signature.
	 */
	private void initSignature() {
		ArrayList<String> parentSettingNamesBackward = new ArrayList<String>();
		PropertyCollection parentColProp = this.getParentProperty();
		if (parentColProp == null) {
			if (this.getClass() == SettingsAll.class) {
				this.signature = "//";
			} else {
				return;
			}
		}
		while (parentColProp != null) {
			parentSettingNamesBackward.add(parentColProp.getType().getPropName());
			parentColProp = parentColProp.getBean().getParentProperty();
		}
		ArrayList<String> parentSettingNames = new ArrayList<String>();
		int len = parentSettingNamesBackward.size();
		for (int i = len - 1; i >= 0; i--) {
			parentSettingNames.add(parentSettingNamesBackward.get(i));
		}
		StringBuffer sb = new StringBuffer();
		sb.append("/");
		for (int i = 0; i < len; i++) {
			sb.append('/');
			sb.append(parentSettingNames.get(i));
		}
		this.signature = sb.toString();
	}

	// END manual code section

	/**
	 * property references initialization.
	 */
	public void initProperties() {
	}

	/**
	 * default constructor.
	 */
	public Settings() {
		super();
		// BEGIN manual code section
		// Settings.Settings()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * 
	 * @param s
	 *            the string
	 */
	public Settings(final String s) {
		super(s);
		// BEGIN manual code section
		// Settings.Settings(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * 
	 * @param sa
	 *            the string array
	 */
	public Settings(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// Settings.Settings(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(Settings.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}
}
