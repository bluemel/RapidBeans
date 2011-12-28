/*
 * Partially generated code file: Toolbar.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.Toolbar
 * 
 * model:    model/org/rapidbeans/presentation/Toolbar.xml
 * template: codegentemplates/genBean.xsl
 */

package org.rapidbeans.presentation;



// BEGIN manual code section
// Toolbar.import
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.MissingResourceException;

import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.LinkFrozen;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.UnresolvedLinkException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.config.ConfigToolbar;
import org.rapidbeans.presentation.config.ConfigToolbarButton;
import org.rapidbeans.presentation.swing.ToolbarSwing;

// END manual code section


/**
 * Rapid Bean class: Toolbar.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class Toolbar extends RapidBean {

    // BEGIN manual code section
    // Toolbar.classBody
    private MainWindow mainWindow = null;

    /**
     * @return the parent main window
     */
    public MainWindow getMainWindow() {
        return this.mainWindow;
    }

    /**
     * @return the Java Swing JMenuBar instance
     */
    public Object getWidget() {
        throw new AssertionError("must be overriden");
    }

    /**
     * Update visibility of the tool bar and enabling of it's buttons
     */
    public void update() {
        throw new AssertionError("must be overriden");
    }

    /**
     * create a tool bar of a special type out of a configuration.
     *
     * @param client the parent client
     * @param mainWindow the main window
     * @param config the configuration
     * @param resourcePath the resource path
     *
     * @return the instance
     */
    public static Toolbar createInstance(
            final Application client,
            final MainWindow mainWindow,
            final ConfigToolbar config,
            final String resourcePath) {
        Toolbar toolBar = null;
        if (config.getClassname() != null) {
            Class<?> toolbarClass = null;
            try {
                toolbarClass = Class.forName(config.getClassname());
            } catch (ClassNotFoundException e) {
                throw new RapidBeansRuntimeException(e);
            }
            Constructor<?> constructor = null;
            try {
                constructor = toolbarClass.getConstructor(
                        Application.class, MainWindow.class,
                                ConfigToolbar.class, String.class);
            } catch (SecurityException e) {
                throw new RapidBeansRuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RapidBeansRuntimeException(e);
            }
            try {
                toolBar = (Toolbar) constructor.newInstance(
                        client, mainWindow, config, resourcePath);
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
        if (toolBar == null) {
            switch (client.getConfiguration().getGuitype()) {
            case swing:
                toolBar = new ToolbarSwing(client, mainWindow, config, resourcePath);
                break;
            case eclipsercp:
                break;
            default:
                throw new RapidBeansRuntimeException("Unknown GUI type \""
                        + client.getConfiguration().getGuitype().name() + "\"");
            }
        }
        return toolBar;
    }

    /**
     * construct a Toolbar.
     *
     * @param client the parent client
     * @param mainWindow the main window
     * @param menubarConfig the configuration
     * @param resourcePath the resource path
     */
    public Toolbar(final Application client,
            final MainWindow mainWindow,
            final ConfigToolbar toolbarConfig,
            final String resourcePath) {
        this.setName(toolbarConfig.getName());
        this.mainWindow = mainWindow;
        for (ConfigToolbarButton toolbarButtonConfig : toolbarConfig.getButtons()) {
            if (client.userIsAuthorized(toolbarButtonConfig.getRolesrequired())) {
                this.addButton(ToolbarButton.createInstance(toolbarButtonConfig, client,
                    mainWindow, resourcePath + "." + this.getName()));
            }
        }
    }

    /**
     * Get the localized text for the tool tip and the menu item.
     *
     * @param client the application
     * @param resourcePath the resource path so far
     *
     * @return the localized text for the tool tip and the menu item
     */
    public String getTextLocalized(
            final Application client, final String resourcePath) {
        String ttext = null;
        final RapidBeansLocale locale = client.getCurrentLocale();
        if (locale != null) {
            if (ttext == null) {
                try {
                    ttext = locale.getStringGui(resourcePath
                            + "." + this.getName() + ".label");
                } catch (MissingResourceException e) {
                    ttext = null;
                }
            }
            if (ttext == null) {
                try {
                    ttext = locale.getStringGui(
                            "mainwindow.toolbar." + this.getName() + ".label");
                } catch (MissingResourceException e) {
                    ttext = null;
                }
            }
        }
        if (ttext == null) {
            try {
                ttext = locale.getStringGui("commongui.text." + this.getName());
            } catch (MissingResourceException e) {
                ttext = null;
            }            
        }
        return ttext;
    }
    // END manual code section


    /**
     * property "name".
     */
    private org.rapidbeans.core.basic.PropertyString name;

    /**
     * property "on".
     */
    private org.rapidbeans.core.basic.PropertyBoolean on;

    /**
     * property "enabler".
     */
    private org.rapidbeans.core.basic.PropertyAssociationend enabler;

    /**
     * property "buttons".
     */
    private org.rapidbeans.core.basic.PropertyAssociationend buttons;

    /**
     * property references initialization.
     */
    protected void initProperties() {
        this.name = (org.rapidbeans.core.basic.PropertyString)
            this.getProperty("name");
        this.on = (org.rapidbeans.core.basic.PropertyBoolean)
            this.getProperty("on");
        this.enabler = (org.rapidbeans.core.basic.PropertyAssociationend)
            this.getProperty("enabler");
        this.buttons = (org.rapidbeans.core.basic.PropertyAssociationend)
            this.getProperty("buttons");
    }



    /**
     * default constructor.
     */
    public Toolbar() {
        super();
        // BEGIN manual code section
        // Toolbar.Toolbar()
        // END manual code section

    }

    /**
     * constructor out of a string.
     * @param s the string
     */
    public Toolbar(final String s) {
        super(s);
        // BEGIN manual code section
        // Toolbar.Toolbar(String)
        // END manual code section

    }

    /**
     * constructor out of a string array.
     * @param sa the string array
     */
    public Toolbar(final String[] sa) {
        super(sa);
        // BEGIN manual code section
        // Toolbar.Toolbar(String[])
        // END manual code section
    }

    /**
     * the bean's type (class variable).
     */
    private static TypeRapidBean type = TypeRapidBean.createInstance(Toolbar.class);
	

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
     * @param argValue value of Property 'name' to set
     */
    public void setName(
        final String argValue) {
        this.name.setValue(argValue);
    }

    /**
     * @return value of Property 'on'
     */
    public boolean getOn() {
        try {
            return ((org.rapidbeans.core.basic.PropertyBoolean) this.on).getValueBoolean();
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("on");
        }
    }

    /**
     * setter for Property 'on'.
     * @param argValue value of Property 'on' to set
     */
    public void setOn(
        final boolean argValue) {
        this.on.setValue(new Boolean(argValue));
    }

    /**
     * @return value of Property 'enabler'
     */
    @SuppressWarnings("unchecked")
    public org.rapidbeans.presentation.enabler.Enabler getEnabler() {
        try {
            org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.enabler.Enabler> col
                = (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.enabler.Enabler>) this.enabler.getValue();
            if (col == null || col.size() == 0) {
                return null;
            } else {
                Link link = (Link) col.iterator().next();
                if (link instanceof LinkFrozen) {
                    throw new UnresolvedLinkException("unresolved link to \""
                            + "org.rapidbeans.presentation.enabler.Enabler"
                            + "\" \"" + link.getIdString() + "\"");
                } else {
                    return (org.rapidbeans.presentation.enabler.Enabler) col.iterator().next();
                }
            }
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("enabler");
        }
    }

    /**
     * setter for Property 'enabler'.
     * @param argValue value of Property 'enabler' to set
     */
    public void setEnabler(
        final org.rapidbeans.presentation.enabler.Enabler argValue) {
        this.enabler.setValue(argValue);
    }

    /**
     * @return value of Property 'buttons'
     */
    @SuppressWarnings("unchecked")
    public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.ToolbarButton> getButtons() {
        try {
            return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.ToolbarButton>)
            this.buttons.getValue();
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("buttons");
        }
    }

    /**
     * setter for Property 'buttons'.
     * @param argValue value of Property 'buttons' to set
     */
    public void setButtons(
        final java.util.Collection<org.rapidbeans.presentation.ToolbarButton> argValue) {
        this.buttons.setValue(argValue);
    }

    /**
     * add method for Property 'buttons'.
     * @param bean the bean to add
     */
    public void addButton(final org.rapidbeans.presentation.ToolbarButton bean) {
        ((org.rapidbeans.core.basic.PropertyCollection) this.buttons).addLink(bean);
    }

    /**
     * remove method for Property 'buttons'.
     * @param bean the bean to add
     */
    public void removeButton(final org.rapidbeans.presentation.ToolbarButton bean) {
        ((org.rapidbeans.core.basic.PropertyCollection) this.buttons).removeLink(bean);
    }
}
