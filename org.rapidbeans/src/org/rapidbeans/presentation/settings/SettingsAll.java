/*
 * Partially generated code file: SettingsAll.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.settings.SettingsAll
 * 
 * model:    model/org/rapidbeans/presentation/settings/SettingsAll.xml
 * template: codegentemplates/genBean.xsl
 */

package org.rapidbeans.presentation.settings;



// BEGIN manual code section
// SettingsAll.import
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.LinkFrozen;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.UnresolvedLinkException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;

// END manual code section


/**
 * Rapid Bean class: SettingsAll.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class SettingsAll extends org.rapidbeans.presentation.settings.Settings {

    // BEGIN manual code section
    // SettingsAll.classBody
    /**
     * init the basic settings for all constructors.
     */
    private void init() {
        if (ApplicationManager.getApplication() != null
                && ApplicationManager.getApplication().getConfiguration().getAuthorization() != null) {
            this.setAuthn(new SettingsAuthn());
        }
    }

    /**
     * @return the program settings document file
     */
    public static File getFile() {
        File settingsFile = new File(getFilename());
        if (!settingsFile.exists()) {
            settingsFile = createFile(getFilename());
        }
        return settingsFile;
    }

    /**
     * @return the program settings document file name
     */
    protected static String getFilename() {
            return getDirname() + "/" + "settings.xml";
    }

    /**
     * @return the program settings document file name
     */
    public static String getDirname() {
        if (ApplicationManager.getApplication() != null) {
            return System.getProperty("user.home") + "/."
               + ApplicationManager.getApplication().getConfiguration().getName().toLowerCase();
        } else {
            return System.getProperty("user.home");
        }
    }

    /**
     * create an initial settings document and store it to the settings file.
     * @param filename the settings file name
     * @return the program settings document file
     */
    private static File createFile(final String filename) {
        final File settingsFile = new File(getFilename());
        if (!settingsFile.getParentFile().exists()) {
            if (!settingsFile.getParentFile().mkdir()) {
                throw new RapidBeansRuntimeException("problems to create settings folder \""
                        + settingsFile.getParentFile().getAbsolutePath() + "\" ");
            }
        }
        final SettingsAll settings = createSettingsInstance();
        final Document settingsDoc = new Document("programmsettings", settings);
        try {
            settingsDoc.setUrl(settingsFile.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RapidBeansRuntimeException(e);
        }
        settingsDoc.save();
        return settingsFile;
    }

    /**
     * factory method for potentially application specific settings.
     *
     * @return the created settings instance
     */
    public static SettingsAll createSettingsInstance() {
        SettingsAll settings = null;
        final Application app = ApplicationManager.getApplication();
        try {
            Class<?> clazz = null;
            if (app.getRootpackage() != null) {
                try {
                    clazz = Class.forName(app.getRootpackage()
                        + ".presentation." + "Settings");
                } catch (ClassNotFoundException e) {
                    clazz = null;
                }
            }
            if (clazz == null) {
                clazz = SettingsAll.class;
            }
            final Constructor<?> constr = clazz.getConstructor();
            settings = (SettingsAll) constr.newInstance(new Object[0]);
            return settings;
        } catch (NoSuchMethodException e) {
            throw new RapidBeansRuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RapidBeansRuntimeException(e);
        } catch (InstantiationException e) {
            throw new RapidBeansRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RapidBeansRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RapidBeansRuntimeException(e);
        }
    }
    // END manual code section


    /**
     * property "basic".
     */
    private org.rapidbeans.core.basic.PropertyAssociationend basic;

    /**
     * property "authn".
     */
    private org.rapidbeans.core.basic.PropertyAssociationend authn;

    /**
     * property references initialization.
     */
    protected void initProperties() {
        super.initProperties();
        this.basic = (org.rapidbeans.core.basic.PropertyAssociationend)
            this.getProperty("basic");
        this.authn = (org.rapidbeans.core.basic.PropertyAssociationend)
            this.getProperty("authn");
    }



    /**
     * default constructor.
     */
    public SettingsAll() {
        super();
        // BEGIN manual code section
        // SettingsAll.SettingsAll()
        init();
        // END manual code section

    }

    /**
     * constructor out of a string.
     * @param s the string
     */
    public SettingsAll(final String s) {
        super(s);
        // BEGIN manual code section
        // SettingsAll.SettingsAll(String)
        init();
        // END manual code section

    }

    /**
     * constructor out of a string array.
     * @param sa the string array
     */
    public SettingsAll(final String[] sa) {
        super(sa);
        // BEGIN manual code section
        // SettingsAll.SettingsAll(String[])
        init();
        // END manual code section
    }

    /**
     * the bean's type (class variable).
     */
    private static TypeRapidBean type = TypeRapidBean.createInstance(SettingsAll.class);
	

    /**
     * @return the Biz Bean's type
     */
    public TypeRapidBean getType() {
        return type;
    }

    /**
     * @return value of Property 'basic'
     */
    @SuppressWarnings("unchecked")
    public org.rapidbeans.presentation.settings.SettingsBasic getBasic() {
        try {
            org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.settings.SettingsBasic> col
                = (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.settings.SettingsBasic>) this.basic.getValue();
            if (col == null || col.size() == 0) {
                return null;
            } else {
                Link link = (Link) col.iterator().next();
                if (link instanceof LinkFrozen) {
                    throw new UnresolvedLinkException("unresolved link to \""
                            + "org.rapidbeans.presentation.settings.SettingsBasic"
                            + "\" \"" + link.getIdString() + "\"");
                } else {
                    return (org.rapidbeans.presentation.settings.SettingsBasic) col.iterator().next();
                }
            }
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("basic");
        }
    }

    /**
     * setter for Property 'basic'.
     * @param argValue value of Property 'basic' to set
     */
    public void setBasic(
        final org.rapidbeans.presentation.settings.SettingsBasic argValue) {
        this.basic.setValue(argValue);
    }

    /**
     * @return value of Property 'authn'
     */
    @SuppressWarnings("unchecked")
    public org.rapidbeans.presentation.settings.SettingsAuthn getAuthn() {
        try {
            org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.settings.SettingsAuthn> col
                = (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.settings.SettingsAuthn>) this.authn.getValue();
            if (col == null || col.size() == 0) {
                return null;
            } else {
                Link link = (Link) col.iterator().next();
                if (link instanceof LinkFrozen) {
                    throw new UnresolvedLinkException("unresolved link to \""
                            + "org.rapidbeans.presentation.settings.SettingsAuthn"
                            + "\" \"" + link.getIdString() + "\"");
                } else {
                    return (org.rapidbeans.presentation.settings.SettingsAuthn) col.iterator().next();
                }
            }
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("authn");
        }
    }

    /**
     * setter for Property 'authn'.
     * @param argValue value of Property 'authn' to set
     */
    public void setAuthn(
        final org.rapidbeans.presentation.settings.SettingsAuthn argValue) {
        this.authn.setValue(argValue);
    }
}
