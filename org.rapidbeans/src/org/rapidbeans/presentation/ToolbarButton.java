/*
 * Partially generated code file: ToolbarButton.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.presentation.ToolbarButton
 * 
 * model:    model/org/rapidbeans/presentation/ToolbarButton.xml
 * template: codegentemplates/genBean.xsl
 */

package org.rapidbeans.presentation;



// BEGIN manual code section
// ToolbarButton.import
import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.LinkFrozen;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.UnresolvedLinkException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.config.ConfigToolbarButton;
import org.rapidbeans.presentation.enabler.Enabler;
import org.rapidbeans.presentation.swing.ToolbarButtonSwing;
import org.rapidbeans.service.Action;

// END manual code section


/**
 * Rapid Bean class: ToolbarButton.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class ToolbarButton extends RapidBean {

    // BEGIN manual code section
    // ToolbarButton.classBody
    /**
     * @return the widget.
     */
    public Object getWidget() {
        throw new AssertionError("override this method");
    }

    /**
     * Update visibility of the tool bar and enabling of it's buttons
     */
    public void update() {
        throw new AssertionError("must be overriden");
    }

    /**
     * create a ToolbarButton of a special type out of a configuration
     * which is the this object itself.
     *
     * @param client the parent client
     * @param mainWindow the main window
     * @param resourcePath the resource path
     *
     * @return the instance
     */
    public static ToolbarButton createInstance(
            final ConfigToolbarButton cfg,
            final Application client,
            final MainWindow mainWindow,
            final String resourcePath) {
        ToolbarButton toolbarButton = null;
        switch (client.getConfiguration().getGuitype()) {
        case swing:
            toolbarButton = new ToolbarButtonSwing(cfg, client, mainWindow, resourcePath);
            break;
        case eclipsercp:
            throw new AssertionError("Eclipse RCP not yet supported");
        default:
            throw new RapidBeansRuntimeException("Unknown GUI type \""
                + client.getConfiguration().getGuitype().name() + "\"");
        }
        return toolbarButton;
    }


    /**
     * constructor.
     *
     * @param config the toolbar button configuration
     * @param resourcePath the resource path
     */
    public ToolbarButton(final Application client,
            final ConfigToolbarButton config,
            final String resourcePath) {
        this.setName(config.getName());

        Action configAction = config.getAction();
        if (configAction != null && configAction.getParentBean() != null
                && configAction.getParentBean() instanceof ConfigToolbarButton) {
            ((ConfigToolbarButton) configAction.getParentBean()).setAction(null);
        }
        if (configAction != null && configAction.getParentBean() != null) {
            final Action clonedAction = (Action) configAction.clone();
            clonedAction.setParentBean(null);
            clonedAction.setContainer(null);
            this.setAction(clonedAction);
        } else {
            this.setAction(configAction);
        }

        final Enabler enablerConfig = config.getEnabler();
        if (enablerConfig != null) {
            this.setEnabler(enablerConfig.createInstance(client));
        }
    }
    // END manual code section


    /**
     * property "name".
     */
    private org.rapidbeans.core.basic.PropertyString name;

    /**
     * property "action".
     */
    private org.rapidbeans.core.basic.PropertyAssociationend action;

    /**
     * property "enabler".
     */
    private org.rapidbeans.core.basic.PropertyAssociationend enabler;

    /**
     * property references initialization.
     */
    protected void initProperties() {
        this.name = (org.rapidbeans.core.basic.PropertyString)
            this.getProperty("name");
        this.action = (org.rapidbeans.core.basic.PropertyAssociationend)
            this.getProperty("action");
        this.enabler = (org.rapidbeans.core.basic.PropertyAssociationend)
            this.getProperty("enabler");
    }



    /**
     * default constructor.
     */
    public ToolbarButton() {
        super();
        // BEGIN manual code section
        // ToolbarButton.ToolbarButton()
        // END manual code section

    }

    /**
     * constructor out of a string.
     * @param s the string
     */
    public ToolbarButton(final String s) {
        super(s);
        // BEGIN manual code section
        // ToolbarButton.ToolbarButton(String)
        // END manual code section

    }

    /**
     * constructor out of a string array.
     * @param sa the string array
     */
    public ToolbarButton(final String[] sa) {
        super(sa);
        // BEGIN manual code section
        // ToolbarButton.ToolbarButton(String[])
        // END manual code section
    }

    /**
     * the bean's type (class variable).
     */
    private static TypeRapidBean type = TypeRapidBean.createInstance(ToolbarButton.class);
	

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
     * @return value of Property 'action'
     */
    @SuppressWarnings("unchecked")
    public org.rapidbeans.service.Action getAction() {
        try {
            org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.service.Action> col
                = (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.service.Action>) this.action.getValue();
            if (col == null || col.size() == 0) {
                return null;
            } else {
                Link link = (Link) col.iterator().next();
                if (link instanceof LinkFrozen) {
                    throw new UnresolvedLinkException("unresolved link to \""
                            + "org.rapidbeans.service.Action"
                            + "\" \"" + link.getIdString() + "\"");
                } else {
                    return (org.rapidbeans.service.Action) col.iterator().next();
                }
            }
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("action");
        }
    }

    /**
     * setter for Property 'action'.
     * @param argValue value of Property 'action' to set
     */
    public void setAction(
        final org.rapidbeans.service.Action argValue) {
        this.action.setValue(argValue);
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
}
