/*
 * Partially generated code file: SettingsBasicGui.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.settings.SettingsBasicGui
 * 
 * model:    model/org/rapidbeans/presentation/settings/SettingsBasicGui.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.presentation.settings;



// BEGIN manual code section
// SettingsBasicGui.import
import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.LinkFrozen;
import org.rapidbeans.core.exception.UnresolvedLinkException;
import org.rapidbeans.core.type.TypeRapidBean;

// END manual code section

/**
 * Rapid Bean class: SettingsBasicGui.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class SettingsBasicGui extends org.rapidbeans.presentation.settings.Settings {
	// BEGIN manual code section
	// SettingsBasicGui.classBody
	// END manual code section

	/**
	 * property "openDocumentHistory".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend openDocumentHistory;

	/**
	 * property "docViewOpenWindowBehaviour".
	 */
	private org.rapidbeans.core.basic.PropertyChoice docViewOpenWindowBehaviour;

	/**
	 * property "createNewBeansEditorApplyBehaviour".
	 */
	private org.rapidbeans.core.basic.PropertyChoice createNewBeansEditorApplyBehaviour;

	/**
	 * property "treeViewShowBeanLinks".
	 */
	private org.rapidbeans.core.basic.PropertyBoolean treeViewShowBeanLinks;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		super.initProperties();
		this.openDocumentHistory = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("openDocumentHistory");
		this.docViewOpenWindowBehaviour = (org.rapidbeans.core.basic.PropertyChoice)
			this.getProperty("docViewOpenWindowBehaviour");
		this.createNewBeansEditorApplyBehaviour = (org.rapidbeans.core.basic.PropertyChoice)
			this.getProperty("createNewBeansEditorApplyBehaviour");
		this.treeViewShowBeanLinks = (org.rapidbeans.core.basic.PropertyBoolean)
			this.getProperty("treeViewShowBeanLinks");
	}

	/**
	 * default constructor.
	 */
	public SettingsBasicGui() {
		super();
		// BEGIN manual code section
		// SettingsBasicGui.SettingsBasicGui()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public SettingsBasicGui(final String s) {
		super(s);
		// BEGIN manual code section
		// SettingsBasicGui.SettingsBasicGui(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public SettingsBasicGui(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// SettingsBasicGui.SettingsBasicGui(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(SettingsBasicGui.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}

	/**
	 * @return value of Property 'openDocumentHistory'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.presentation.settings.SettingsBasicGuiOpenDocHistory getOpenDocumentHistory() {
		try {
			org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.settings.SettingsBasicGuiOpenDocHistory> col
				= (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.settings.SettingsBasicGuiOpenDocHistory>) this.openDocumentHistory.getValue();
			if (col == null || col.size() == 0) {
				return null;
			} else {
				Link link = (Link) col.iterator().next();
				if (link instanceof LinkFrozen) {
					throw new UnresolvedLinkException("unresolved link to \""
							+ "org.rapidbeans.presentation.settings.SettingsBasicGuiOpenDocHistory"
							+ "\" \"" + link.getIdString() + "\"");
				} else {
					return (org.rapidbeans.presentation.settings.SettingsBasicGuiOpenDocHistory) col.iterator().next();
				}
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("openDocumentHistory");
		}
	}

	/**
	 * setter for Property 'openDocumentHistory'.
	 * @param argValue
	 *            value of Property 'openDocumentHistory' to set
	 */
	public void setOpenDocumentHistory(
		final org.rapidbeans.presentation.settings.SettingsBasicGuiOpenDocHistory argValue) {
		this.openDocumentHistory.setValue(argValue);
	}

	/**
	 * @return value of Property 'docViewOpenWindowBehaviour'
	 */
	public org.rapidbeans.presentation.OpenWindowBehaviour getDocViewOpenWindowBehaviour() {
		try {
			java.util.List<?> enumList = (java.util.List<?>) this.docViewOpenWindowBehaviour.getValue();
			if (enumList == null || enumList.size() == 0) {
				return null;
			} else {
				return (org.rapidbeans.presentation.OpenWindowBehaviour) enumList.get(0);
						}			
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("docViewOpenWindowBehaviour");
		}
	}

	/**
	 * setter for Property 'docViewOpenWindowBehaviour'.
	 * @param argValue
	 *            value of Property 'docViewOpenWindowBehaviour' to set
	 */
	public void setDocViewOpenWindowBehaviour(
		final org.rapidbeans.presentation.OpenWindowBehaviour argValue) {
		java.util.List<org.rapidbeans.presentation.OpenWindowBehaviour> list =
			new java.util.ArrayList<org.rapidbeans.presentation.OpenWindowBehaviour>();
		list.add(argValue);
		this.docViewOpenWindowBehaviour.setValue(list);
	}

	/**
	 * @return value of Property 'createNewBeansEditorApplyBehaviour'
	 */
	public org.rapidbeans.presentation.CreateNewBeansEditorApplyBehaviour getCreateNewBeansEditorApplyBehaviour() {
		try {
			java.util.List<?> enumList = (java.util.List<?>) this.createNewBeansEditorApplyBehaviour.getValue();
			if (enumList == null || enumList.size() == 0) {
				return null;
			} else {
				return (org.rapidbeans.presentation.CreateNewBeansEditorApplyBehaviour) enumList.get(0);
						}			
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("createNewBeansEditorApplyBehaviour");
		}
	}

	/**
	 * setter for Property 'createNewBeansEditorApplyBehaviour'.
	 * @param argValue
	 *            value of Property 'createNewBeansEditorApplyBehaviour' to set
	 */
	public void setCreateNewBeansEditorApplyBehaviour(
		final org.rapidbeans.presentation.CreateNewBeansEditorApplyBehaviour argValue) {
		java.util.List<org.rapidbeans.presentation.CreateNewBeansEditorApplyBehaviour> list =
			new java.util.ArrayList<org.rapidbeans.presentation.CreateNewBeansEditorApplyBehaviour>();
		list.add(argValue);
		this.createNewBeansEditorApplyBehaviour.setValue(list);
	}

	/**
	 * @return value of Property 'treeViewShowBeanLinks'
	 */
	public boolean getTreeViewShowBeanLinks() {
		try {
			return ((org.rapidbeans.core.basic.PropertyBoolean) this.treeViewShowBeanLinks).getValueBoolean();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("treeViewShowBeanLinks");
		}
	}

	/**
	 * setter for Property 'treeViewShowBeanLinks'.
	 * @param argValue
	 *            value of Property 'treeViewShowBeanLinks' to set
	 */
	public void setTreeViewShowBeanLinks(
		final boolean argValue) {
		this.treeViewShowBeanLinks.setValue(new Boolean(argValue));
	}
}
