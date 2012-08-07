/*
 * Rapid Beans Framework: EditorBean.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 02/13/2006
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copies of the GNU Lesser General Public License and the
 * GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

package org.rapidbeans.presentation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.MissingResourceException;

import org.rapidbeans.core.basic.IdKeyprops;
import org.rapidbeans.core.basic.IdType;
import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.basic.ThreadLocalValidationSettings;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.event.PropertyChangeEvent;
import org.rapidbeans.core.exception.BeanDuplicateException;
import org.rapidbeans.core.exception.BeanNotFoundException;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.exception.ValidationInstanceAssocTwiceException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.datasource.Filter;
import org.rapidbeans.datasource.event.AddedEvent;
import org.rapidbeans.datasource.event.ChangedEvent;
import org.rapidbeans.datasource.event.DocumentChangeListener;
import org.rapidbeans.datasource.event.RemovedEvent;
import org.rapidbeans.presentation.config.ConfigEditorBean;
import org.rapidbeans.presentation.config.ConfigPropEditorBean;
import org.rapidbeans.presentation.config.ConfigPropPersistencestrategy;
import org.rapidbeans.presentation.swing.EditorBeanSwing;


/**
 * the bean editor GUI.
 *
 * @author Martin Bluemel
 */
public abstract class EditorBean
   implements View, EditorPropertyListener, DocumentChangeListener  {

    /**
     * @return the view's widget
     */
    public abstract Object getWidget();

    /**
     * @return the view's title.
     */
    public String getTitle() {
        String idstring = null;
        if (bean.getContainer() == null) {
            idstring = this.locale.getStringGui("commongui.text.new")
                + " "
                + bean.toStringGuiType(this.locale);
        } else {
            try {
                final String key = "editor."
                    + bean.getType().getName().toLowerCase()
                    + ".title";
                final String pattern =
                    this.locale.getStringGui(key);
                idstring = bean.expandPropertyValues(pattern, this.getLocale());
                idstring = bean.toStringGuiType(this.locale) + ": " + idstring;
            } catch (MissingResourceException e) {
                idstring = null;
            }
            if (idstring == null) {
                idstring = bean.toStringGui(this.locale);
            }
        }
        return idstring;
    }

    /**
     * update the GUI's buttons.
     *
     * @param propEditor the property editor where an
     *        input field has been changed
     */
    public abstract void validateAndUpdateButtons(EditorProperty propEditor);

    /**
     * update all property editor UIs (input fields).
     */
    protected void updatePropedGUIs() {
        for (EditorProperty proped : this.propEditors) {
            proped.updateUI();
        }
    }

    /**
     * the bean to edit.
     */
    private RapidBean bean;

    /**
     * @return the bean currently edited
     */
    public RapidBean getBean() {
        return this.bean;
    }

    /**
     * the bean backup before editing.
     */
    private RapidBean bakbean;

    /**
     * @return the bean before
     */
    public RapidBean getBakbean() {
        return this.bakbean;
    }

    /**
     * the parent bean of a new bean to create.
     */
    private RapidBean parentBean;

    /**
     * @return the parent bean of a new bean to create.<BR>
     *         null if in "edit only" mode.<BR>
     *         not null if in create mode.
     */
    public RapidBean getParentBean() {
        return this.parentBean;
    }

    /**
     * the parent bean's collection property for
     * composition of a new bean to create.
     */
    private PropertyCollection parentBeanColProp;

    /**
     * the parent document view.
     */
    private DocumentView documentView = null;

    /**
     * @return the parent document view
     */
    public DocumentView getDocumentView() {
        return this.documentView;
    }

    /**
     * the list with all property editors.
     */
    private List<EditorProperty> propEditors = new ArrayList<EditorProperty>();

    /**
     * @return the list of property editors.
     */
    public List<EditorProperty> getPropEditors() {
        return this.propEditors;
    }

    private List<EditorProperty> getPropEditorsSorted() {
        final List<EditorProperty> propEditorsSorted = new ArrayList<EditorProperty>();
        for (final TypeProperty proptype : this.bean.getType().getPropertyTypes()) {
            final EditorProperty propEditor = this.getPropEditor(proptype.getPropName());
            if (propEditor != null) {
                propEditorsSorted.add(propEditor);
            }
        }
        return propEditorsSorted;
    }

    private List<Property> getPropEditorsKeyprops() {
        final List<Property> editorsKeyprops = new ArrayList<Property>();
        for (final Property prop :
            ((IdKeyprops) this.bean.getId()).getKeyprops()) {
            final EditorProperty propEditor = this.getPropEditor(prop.getName());
            if (propEditor != null) {
                editorsKeyprops.add(prop);
            }
        }
        return editorsKeyprops;
    }

    /**
     * Find a property editor specified by a property name.
     *
     * @param propname the property name
     *
     * @return the editor of the specified property or null if not found
     */
    public EditorProperty getPropEditor(final String propname) {
        return this.propEdMap.get(propname);
    }

    /**
     * the hashmap to access a prop editor quickly.
     */
    private HashMap<String, EditorProperty> propEdMap =
        new HashMap<String, EditorProperty>();

    /**
     * adds a new property editor to the bean editor.
     *
     * @param client the client
     * @param bbean the bean
     * @param property the property to edit
     * @param propBackup the backup property used instead of a transaction
     */
    private EditorProperty addPropertyEditor(final Application client,
            final RapidBean bbean, final Property property,
            final Property propBackup) {
        EditorProperty propEditor = EditorProperty.createInstance(
                client, this, property, propBackup);
        propEditor.addPropertyEditorListener(this);
        this.propEditors.add(propEditor);
        this.propEdMap.put(property.getType().getPropName(), propEditor);
        try {
            propEditor.validateInputField();
        } catch (ValidationException e) {
            // ignore validation exceptions
        }
        return propEditor;
    }

    /**
     * the collection of registered editor listeners.
     */
    private Collection<EditorBeanListener> listeners =
        new ArrayList<EditorBeanListener>();

    /**
     * adds a listener that wants to be notified by editor events.
     *
     * @param listener the listenr to add
     */
    public void addEditorListener(final EditorBeanListener listener) {
        this.listeners.add(listener);
    }

    /**
     * removes a listener.
     *
     * @param listener the listener to remove
     */
    public void removeEditorListener(final EditorBeanListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * notifies all listeners that the editor has been closed.
     */
    protected void fireEditorClosed() {
        for (EditorBeanListener listener : this.listeners) {
            listener.editorClosed(this);
        }
    }

    /**
     * flag that indicates if in new mode the bean is already added
     * to the parent collection property.
     */
    private boolean beanAdded = false;

    /**
     * @return if in new mode the bean is already added to the
     *         parent collection property
     */
    private boolean isBeanAdded() {
        // performance issue with big collections
        //return this.parentBeanColProp.getValue().contains(this.bean);
        return this.beanAdded;
    }

    /**
     * setter for this internal flag.
     * @param added if the biz bean has bean added
     */
    public void setBeanAdded(final boolean added) {
        this.beanAdded = added;
    }

    /**
     * the locale.
     */
    private RapidBeansLocale locale;

    /**
     * @return the locale
     */
    public RapidBeansLocale getLocale() {
        return this.locale;
    }

    /**
     * The configuration of this editor.
     */
    private ConfigEditorBean configuration = null;

    /**
     * @return the editor's configuration
     */
    public ConfigEditorBean getConfiguration() {
        return this.configuration;
    }

    /**
     * constructor.
     *
     * @param client the bean client
     * @param docView parentView the parent document view
     * @param bizBean the bean to edit
     * @param newBeanParentColProp a new Bean's parent collection property.
     *                      Is not null if a new Bean is to be created
     *                      Is null if an existing bean is simply edited
     */
    protected EditorBean(final Application client,
            final DocumentView docView, final RapidBean bizBean,
            final PropertyCollection newBeanParentColProp) {
        try {
            this.setEventLock();
            this.documentView = docView;
            this.bean = bizBean;
            this.configuration = client.getConfigBeanEditor(this.bean.getType());
            if (newBeanParentColProp != null) {
                this.parentBeanColProp = newBeanParentColProp;
                this.parentBean = newBeanParentColProp.getBean();
            }
            this.locale = client.getCurrentLocale();
            this.bakbean = bizBean.clone();
            if (this.configuration != null) {
                for (final ConfigPropEditorBean cfgprop : this.configuration.getPropertycfgs()) {
                    final Property prop = this.bean.getProperty(cfgprop.getName());
                    if (prop == null) {
                        String validTypes = "";
                        int i = 0;
                        for (final Property prop1 : bean.getPropertyList()) {
                            if (i > 0) {
                                validTypes += ", ";
                            }
                            validTypes += "\"" + prop1.getName() + "\"";
                            i++;
                        }
                    	throw new RapidBeansRuntimeException("Invalid property \"" + cfgprop.getName() + "\"" +
                    			" configured for class \""
                    	        + bean.getType().getName() + "\"\n"
                    	        + "Valid types are: " + validTypes);
                    }
                    addPropertyEditor(client, bizBean, prop);
                }
            } else {
                try {
                    ThreadLocalValidationSettings.readonlyOff();
                    for (final Property prop : this.bean.getPropertyList()) {
                        addPropertyEditor(client, bizBean, prop);
                    }
                } finally {
                    ThreadLocalValidationSettings.remove();
                }
            }
        } finally {
            this.releaseEventLock();
        }
    }

    /**
     * add a new property editor.
     *
     * @param client the client
     * @param bean the bean
     * @param prop the property edited
     */
    private EditorProperty addPropertyEditor(final Application client, final RapidBean bean, final Property prop) {
    	EditorProperty propEditor = null;
    	if (prop == null) {
    		throw new IllegalArgumentException("prop is unspecified (null)");
    	}
        if (prop instanceof PropertyCollection) {
            final TypePropertyCollection colPropType = (TypePropertyCollection) prop.getType();
            if (!colPropType.isComposition()) {
                final Filter filter = this.documentView.getBeanFilter();
                if (filter == null || filter.applies(colPropType.getTargetType())) {
                    propEditor = this.addPropertyEditor(client, bean, prop,
                        this.bakbean.getProperty(prop.getType().getPropName()));
                }
            }
        } else {
            propEditor = this.addPropertyEditor(client, bean, prop,
                    this.bakbean.getProperty(prop.getType().getPropName()));
        }
        return propEditor;
    }

    /**
     * constructor arguments.
     */
    private static final Class<?>[] BIZBEAN_EDITOR_CONSTR_ARGTYPES =
        {Application.class, DocumentView.class, RapidBean.class, PropertyCollection.class};

    /**
     * create a new editor.
     *
     * @param client the client
     * @param docView the parent document view
     * @param bizBean the bean to edit
     * @param newBeanParent a new Bean's parent collection property.
     *                      Is not null if a new Bean is to be created
     *                      Is null if an existing bean is simply edited
     *
     * @return the editor object
     */
    public static EditorBean createInstance(final Application client,
            final DocumentView docView, final RapidBean bizBean,
            final PropertyCollection newBeanParent) {

        EditorBean editor = null;

        final ConfigEditorBean cfg =
            client.getConfigBeanEditor(bizBean.getType());
        if (cfg != null) {
            final String editorclassname = cfg.getEditorclass();
            if (editorclassname != null) {
                Class<?> editorclass = null;
                try {
                    editorclass = Class.forName(editorclassname);
                } catch (ClassNotFoundException e) {
                    throw new RapidBeansRuntimeException(e);
                }

                Constructor<?> constr = null;
                try {
                    constr = editorclass.getConstructor(BIZBEAN_EDITOR_CONSTR_ARGTYPES);
                } catch (SecurityException e) {
                    throw new RapidBeansRuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RapidBeansRuntimeException("No constructor with aruments classes:\n"
                                + "Application, DocumentView, bean, PropertyCollection) found.", e);
                }

                if (constr != null) {
                    final Object[] oa = {client, docView, bizBean, newBeanParent};
                    try {
                        editor = (EditorBean) constr.newInstance(oa);
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
            }
        }

        if (editor == null) {
            switch (client.getConfiguration().getGuitype()) {
            case swing:
                editor = new EditorBeanSwing(client, docView, bizBean, newBeanParent);
                break;
            case eclipsercp:
                //mainWindow = new BBMainWindowEclispercp();
                break;
            default:
                throw new RapidBeansRuntimeException("Unknown GUI type \""
                        + client.getConfiguration().getGuitype().name() + "\"");
            }
        }

        return editor;
    }

    /**
     * action handler for OK button.
     */
    public void handleActionOk() {
        try {
            this.createOrUpdateBean(false);
            this.fireEditorClosed();
        } catch (ValidationException e) {
            if (this.getDocumentView().getClient().getTestMode()) {
                throw e;
            } else {
                // eat up that validation exception
                return;
            }
        }
    }

    /**
     * action handler for Apply button.
     */
    public void handleActionApply() {
        try {
            this.createOrUpdateBean(true);
        } catch (ValidationException e) {
            if (this.getDocumentView().getClient().getTestMode()) {
                throw e;
            } else {
                // eat up that validation exception
                return;
            }
        }
    }

    /**
     * determines the way how the editor behaves
     * after pressing the apply button.
     */
    private CreateNewBeansEditorApplyBehaviour createApplyMode = CreateNewBeansEditorApplyBehaviour.resetall;

    /**
     * Setter to determine the way how the editor behaves
     * after pressing the apply button.
     *
     * @param mode determines the way how the editor behaves
     * after pressing the apply button.<br/>
     * <code>CREATE_APPLY_RESET_ALL</code>: all property values are reset and have to be specified in order
     *                                      to create the next bean.<br/>
     * <code>CREATE_APPLY_RESET_KEY</code>: only key property values are reset.
     *                                      All other property values stay the
     *                                      same and can be reused for creating the next bean.<br/>
     * <code>CREATE_APPLY_RESET_NONE</code>: no property value is reset.
     *                                       Key properties have to be changed afterwards before<br/>
     *                                       creating the nex bean.
     */
    public void setCreateApplyMode(final CreateNewBeansEditorApplyBehaviour mode) {
        this.createApplyMode = mode;
    }

    /**
     * creates or updates a bean from the editor.
     */
    private void createOrUpdateBean(final boolean reset) {
        this.validateInputAndUpdateBean(true, true, true, true, null);
        this.addBeanIfNew(false);
        if (this.documentView.getPersistencestrategy()
                == ConfigPropPersistencestrategy.oncloseeditor) {
            if (ApplicationManager.getApplication() != null) {
                ApplicationManager.getApplication().save(this.documentView.getDocument());
            } else {
                this.documentView.getDocument().save();
            }
        }
        if (reset) {
            reset();
        }
    }

    /**
     * reset the editor.
     */
    private void reset() {
        if (isInNewMode()) {
            this.resetBean();
        }
        this.setBeanAdded(false);        
        this.resetBackupBean();
        updatePropEditors();
        updatePropedGUIs();
        validateAndUpdateButtons(null);
    }

    /**
     * action handler for Close (Cancel) button.
     */
    public void handleActionClose() {
        boolean anyInputFieldChanged;
        try {
            anyInputFieldChanged = this.isAnyInputFieldChanged();
        } catch (ValidationException e) {
            anyInputFieldChanged = true;
        }
        if (anyInputFieldChanged) {
            if (isInNewMode()) {
                if (this.isBeanAdded() && this.bean != null
                        && this.bean.getParentProperty() != null) {
                    this.bean.getParentProperty().removeLink(this.bean);
                    this.setBeanAdded(false);
                }
                this.resetBean();
            } else {
                // restore the bean (rollback the logical transaction)
                this.restoreBean();
            }
            // restore the bean (rollback the logical transaction
            this.updatePropedGUIs();
            this.validateAndUpdateButtons(null);
        }
        this.fireEditorClosed();
    }

    /**
     * resets the bean dependently of the mode.
     */
    private void resetBean() {

        // unregister all property editors as listeners for the old bean
        for (final Property prop : this.bean.getPropertyList()) {
            final EditorProperty ped = getPropEditor(prop.getName());
            if (ped != null) {
                this.bean.removePropertyChangeListener(ped);
            }
        }

        switch (this.createApplyMode) {
        case resetall:
            this.bean = RapidBeanImplStrict.createInstance(this.bean.getType().getName());
            break;
        case resetnothing:
        case resetkey:
            final RapidBean newBean = RapidBeanImplStrict.createInstance(this.bean.getType().getName());
            for (Property prop : newBean.getPropertyList()) {
                if (!prop.getType().isKeyCandidate() || this.createApplyMode == CreateNewBeansEditorApplyBehaviour.resetnothing) {
                    try {
                        ThreadLocalValidationSettings.validationOff();
                        if (prop instanceof PropertyCollection) {
                            ((PropertyCollection)prop).setValue(
                                    this.bean.getProperty(prop.getType().getPropName()).getValue(), false, true);
                        } else {
                            prop.setValue(this.bean.getProperty(prop.getType().getPropName()).getValue());
                        }
                    } finally {
                        ThreadLocalValidationSettings.remove();
                    }
                }
            }
            this.bean = newBean;
            break;
        default:
            throw new RapidBeansRuntimeException("Invalid createApplyMode " + this.createApplyMode
                    + " for bean editor.");
        }

        // register all property editors as listeners for the new bean
        for (final Property prop : this.bean.getPropertyList()) {
            final EditorProperty ped = getPropEditor(prop.getName());
            if (ped != null) {
                this.bean.addPropertyChangeListener(ped);
            }
        }
    }

    /**
     * reset the bean's properties
     * according to the edited input fields.
     */
    private void restoreBean() {
        List<Property> properties = this.bean.getPropertyList();
        List<Property> bakprops = this.bakbean.getPropertyList();
        int size = properties.size();
        try {
            this.setEventLock();
            ThreadLocalValidationSettings.validationOff();
            for (int i = 0; i < size; i++) {
                properties.get(i).setValue(bakprops.get(i).getValue());
            }
        } finally {
            ThreadLocalValidationSettings.remove();
            this.releaseEventLock();
        }
    }

    /**
     * @return if any of the property editor's input field is changed
     */
    public boolean isAnyInputFieldChanged() {
        boolean changedAny = false;
        for (EditorProperty propertyEditor : this.propEditors) {
            if (propertyEditor.isInputFieldChanged()) {
                changedAny = true;
                break;
            }
        }
        return changedAny;
    }

    /**
     * change the beans properties
     * according to the edited input fields.
     *
     * @param showDialog if dialogs should be shown
     * @param linkBack if the bean is completely linked
     *            (with forward and inverse linked) with another bean or not
     * @param updateUI determines if the UI will be updated or not e. g. for normalization
     */
    protected void validateInputAndUpdateBean(final boolean showDialog,
            final boolean linkBack, final boolean updateUI) {
    	for (EditorProperty ped : this.getPropEditors()) {
    		this.validateInputAndUpdateBean(
    				showDialog, linkBack, updateUI, true, ped);
    	}
    }

    /**
     * change the beans properties
     * according to the edited input fields.
     *
     * @param showDialog if dialogs should be shown
     * @param linkBack if the bean is completely linked
     *            (with forward and inverse linked) with another bean or not
     * @param updateUI determines if the UI will be updated or not e. g. for normalization
     * @param propEditorChanged the changed property editor. This is null if an
     *                          OK button has been pressed.
     */
    protected void validateInputAndUpdateBean(final boolean showDialog,
            final boolean linkBack, final boolean updateUI,
            final boolean checkDocAlreadyContainsBean,
            final EditorProperty propEditorChanged) {
        EditorProperty propEditor = null;
        final boolean docChangedBefore = this.getDocumentView().getDocument().getChanged();
        try {
            this.setEventLock();

            //List<Property> keyprops = null;
            int keypropsSize = -1;
            final boolean idtypeKeyprops =
                this.bean.getType().getIdtype() == IdType.keyprops
                || this.bean.getType().getIdtype() == IdType.keypropswithparentscope;
            if (idtypeKeyprops) {
                final List<Property> keyprops = this.getPropEditorsKeyprops();
                keypropsSize = keyprops.size();
                if (propEditorChanged != null && propEditorChanged.getProperty().getType().isKeyCandidate()) {
                    if (checkDocAlreadyContainsBean) {
                        if (this.getDocumentView().getDocument().contains(this.getBean())) {
                            throw new ValidationException("invalid.prop.key.already.in.document",
                            		this,
                                    "Changed key property \"" + propEditorChanged.getProperty().getName());
                        }
                    }
                }
            }

            int i = 0;
            String propName;
            Property prop;            
            for (EditorProperty currentPropEditor : this.getPropEditorsSorted()) {
                propEditor = currentPropEditor;
                if ((!idtypeKeyprops) || (i >= keypropsSize)) {
                    addBeanIfNew(true);
                }
                if (currentPropEditor == propEditorChanged) {
                    currentPropEditor.validateInputField();
                }
                propName = currentPropEditor.getProperty().getType().getPropName();
                prop = this.bean.getProperty(propName);

                if (prop instanceof PropertyCollection) {
                    if (linkBack) {
                        ((PropertyCollection) prop).setValue(currentPropEditor.getInputFieldValue(),
                            true, false);
                    } else {
                        ((PropertyCollection) prop).setValue(currentPropEditor.getInputFieldValue(),
                            false, false);
                    }
                } else {
                    if (!(prop.isDependent() || prop.getReadonly())) {
                        prop.setValue(currentPropEditor.getInputFieldValue());
                    }
                }

                // show the value again in case it has been normalized.
                if (updateUI) {
                    currentPropEditor.updateUI();
                }
                i++;
            }
            if (!idtypeKeyprops || i >= keypropsSize) {
                addBeanIfNew(true);
            }
        } catch (ValidationInstanceAssocTwiceException e) {
            throw e;
        } catch (BeanDuplicateException e) {
            if (showDialog && !this.getDocumentView().getClient().getTestMode()) {
                showCreateFailedMessage();
                propEditor.setFocus();
            }
            throw e;
        } catch (ValidationException e) {
            if (showDialog && !this.getDocumentView().getClient().getTestMode()) {
                this.documentView.getClient().messageError(
                    this.locale.getStringGui("messagedialog.input.field") + " \""
                    + propEditor.getProperty().getNameGui(this.locale)
                    + "\":\n"
                    + e.getLocalizedMessage(
                        this.locale),
                        this.locale.getStringGui("messagedialog.title.input.wrong"));
                propEditor.setFocus();
            }
            throw e;
        } finally {
            removeBeanIfNew(docChangedBefore);
            this.releaseEventLock();
        }
    }

    /**
     * if the editor is in new mode add the bean to the container.
     *
     * @param setAdded set bean added afterwards. Usually this
     *        is exactly what you want to do.
     */
    public void addBeanIfNew(final boolean setAdded) {
        if (this.isInNewMode() && (!this.isBeanAdded())) {
            if (this.getBean().getType().getIdtype() == IdType.keyprops
                    || this.getBean().getType().getIdtype() == IdType.keypropswithparentscope) {
                // clear the id in order to let the
                // "create bean" action work out
                this.bean.clearId();
            }
            this.parentBeanColProp.addLink(this.bean);
            if (setAdded) {
                this.setBeanAdded(true);
            }
        }
    }

    /**
     * if the editor is in new mode remove the bean from the container.
     *
     * @param docChangedBefore if false the document and it's view will be unmarked
     */
    public void removeBeanIfNew(final boolean docChangedBefore) {
        if (this.isInNewMode() && this.isBeanAdded()) {
            try {
                setEventLockPropEditors();
                for (int i = 0; i < 2; i++) {
                    try {
                        ThreadLocalValidationSettings.validationOff();
                        this.parentBeanColProp.removeLink(this.bean, true, true, false);
                        break;
                    } catch (BeanNotFoundException e) {
                        if (i == 0) {
                            this.parentBeanColProp.sort();
                        } else {
                            throw e;
                        }
                    } finally {
                        ThreadLocalValidationSettings.remove();
                    }

                }
                if (!docChangedBefore) {
                    this.documentView.getDocument().resetChanged();
                    this.documentView.markAsChanged(false);
                }
                this.setBeanAdded(false);
            } finally {
                releaseEventLockPropEditors();
            }
        }
    }

    /**
     * shows the "Create Failed" message.
     */
    private void showCreateFailedMessage() {
        if (!this.getDocumentView().getClient().getTestMode()) {
            this.getDocumentView().getClient().messageError(
                    this.getLocale().getStringMessage(
                            "messagedialog.create.duplicate",
                            this.getBean().toStringGui(this.getLocale())),
                            this.getLocale().getStringGui(
                            "messagedialog.title.create.duplicate"));
        }
    }

    /**
     * update the Property editors with the new bean's properties.
     */
    private void updatePropEditors() {
        for (EditorProperty propEditor : this.propEditors) {
            propEditor.setProperty(this.bean.getProperty(
                    propEditor.getProperty().getType().getPropName()));
        }
    }

    /**
     * for white box testing.
     * @return a HashMap with button wigets.
     *         The keys are the button names ok, apply and cancel
     */
    public abstract HashMap<String, Object> getButtonWidgets();

    /**
     * indicates if the editor is for creating a new bean
     * instead of just modifying an existing one.
     *
     * @return true if the editor is for creating a new bean<BR>
     *         false if the editor is for modifying an existing bean.
     */
    public boolean isInNewMode() {
        return this.parentBean != null;
    }

    /**
     * @param propEditor the editor that notified the change.
     */
    public void inputFieldChanged(final EditorProperty propEditor) {
        try {
            this.validateAndUpdateButtons(propEditor);
        } catch (ValidationException e) {
            throw e;
        }
    }

    /**
     * handler for added bean.
     * @param e the added event
     */
    public void beanAddPre(final AddedEvent e) {
    }

    /**
     * handler for added bean.
     * @param e the added event
     */
    public void beanAdded(final AddedEvent e) {
        if (this.getEventLock()) {
            return;
        }
        for (EditorProperty proped : this.propEditors) {
            if (proped.getProperty().getType() instanceof TypePropertyCollection) {
                proped.beanAdded(e);
            }
        }
    }


    /**
     * handler for change bean pre event.
     *
     * @param e the changed event
     */
    public void beanChangePre(final ChangedEvent e) {
    }

    /**
     * handler for changed bean.
     *
     * @param e the changed event
     */
    public void beanChanged(final ChangedEvent e) {

        if (this.getEventLock()) {
            return;
        }

        if (this.bean != e.getBean()) {
            return;
        }

        final EditorProperty sourcePropEditor = ThreadLocalEventLock.getSourcePropEditor();
        if (sourcePropEditor != null && sourcePropEditor.getBeanEditor() == this) {
            return;
        }

        try {
            this.setEventLock();
            for (final PropertyChangeEvent pce : e.getPropertyEvents()) {
                final Property prop = pce.getProperty();
                final String propname = prop.getName();
                final EditorProperty proped = this.propEdMap.get(propname);
                if (proped != null) {
                    try {
                        proped.setUIEventLock();
                        proped.beanChanged(e);
                    } finally {
                        proped.releaseUIEventLock();
                    }
                }
            }
        } finally {
            this.releaseEventLock();
        }

        if (e.getBean() == this.bean && (!this.modifies)) {
            this.resetBackupBean();
        }
    }

    /**
     * modifies flag.
     */
    private boolean modifies = false;

    /**
     * @return if the editor modifies.
     */
    public boolean getModifies() {
        return this.modifies;
    }

    /**
     * @param mod if modifies.
     */
    public void setModifies(final boolean mod) {
        this.modifies = mod;
    }

    /**
     * handler for bean pre remove event.
     *
     * @param e the added event
     */
    public void beanRemovePre(final RemovedEvent e) {
        if (this.getEventLock()) {
            return;
        }
    }

    /**
     * handler for added bean.
     * @param e the added event
     */
    public void beanRemoved(final RemovedEvent e) {
        if (this.getEventLock()) {
            return;
        }
        if (this.getBean() == e.getBean()) {
            boolean close;
            if (ApplicationManager.getApplication().getTestMode()) {
                close = true;
            } else {
                close = ApplicationManager.getApplication().messageYesNo(
                        "Close that Editor?", "bean removed");
            }
            if (close) {
                this.fireEditorClosed();
            } else {
                this.handleActionClose();
            }
        }
        for (EditorProperty proped : this.propEditors) {
           proped.beanRemoved(e);
        }
    }

    /**
     * the editor's event lock to avoid Change event feedback.
     */
    private int eventLock = 0;

    /**
     * @return the event lock
     */
    public boolean getEventLock() {
        return this.eventLock > 0;
    }

    /**
     * increase the event lock.
     */
    protected void setEventLock() {
        this.eventLock++;
    }

    /**
     * increase the event lock.
     */
    protected void releaseEventLock() {
        if (this.eventLock > 0) {
            this.eventLock--;
        }
    }

    /**
     * Lock all the property editors.
     */
    private void setEventLockPropEditors() {
        for (final EditorProperty proped : getPropEditors()) {
            proped.setUIEventLock();
        }
    }

    /**
     * Release all the property editor's locks
     */
    private void releaseEventLockPropEditors() {
        for (final EditorProperty proped : getPropEditors()) {
            proped.releaseUIEventLock();
        }
    }

    /**
     * the bean editor view's name is the document's name.
     *
     * @return the document view's name
     */
    public String getName() {
        return "documentview.beaneditor." + this.bean.getIdString();
    }

    /**
     * reset the document change mark.
     */
    public void documentSaved() {
    }

    /**
     * reset the backup bean to the current bean.
     */
    private void resetBackupBean() {
        this.bakbean = bean.clone();
        for (Property bakprop : this.bakbean.getPropertyList()) {
            if (!(bakprop instanceof PropertyCollection
                    && ((TypePropertyCollection) bakprop.getType()).isComposition())
                    && (this.propEdMap.get(bakprop.getType().getPropName()) != null)) {
                this.propEdMap.get(bakprop.getType().getPropName()).setPropertyBak(bakprop);
            }
        }
    }

    /**
     * close the bean editor.
     *
     * @return if canceling is desired
     */
    public boolean close() {
        boolean cancel = false;
        boolean changed = false;
        boolean valid = true;
        try {
            changed = this.isAnyInputFieldChanged();
        } catch (ValidationException e) {
            valid = false;
        }
        if (valid && changed) {
            switch (this.documentView.getPersistencestrategy()) {
            case oncloseeditor:
                if (changed) {
                    handleActionApply();
                }
                break;
            default:
                final String msg = this.locale.getStringMessage(
                        "messagedialog.beaneditor.close", this.getTitle());
                MessageDialogResponse response = null;
                if (ApplicationManager.getApplication().getTestMode()) {
                    response = MessageDialogResponse.yes;
                } else {
                    response = this.documentView.getClient().messageYesNoCancel(
                            msg, this.locale.getStringMessage(
                            "messagedialog.beaneditor.close.title"));
                }
                switch (response) {
                case yes:
                    this.handleActionOk();
                    break;
                case no:
                    this.handleActionClose();
                    break;
                default:
                    cancel = true;
                    break;
                }
                break;
            }
        } else {
            this.handleActionClose();
        }
        return cancel;
    }

    public static int DIRECTION_UP = 0;
    public static int DIRECTION_DOWN = 1;

    public abstract void rotateFocus(final Property property, final int direction);
    
    public EditorProperty getNextEditor(final Property property) {
        final EditorProperty currentPed = getPropEditor(property.getName());
        final int currentIndex = this.propEditors.indexOf(currentPed);
        EditorProperty nextEditor = null;
        if (currentIndex < this.propEditors.size() - 1) {
            nextEditor = this.propEditors.get(currentIndex + 1);
        } else {
            nextEditor = this.propEditors.get(0);
        }
        return nextEditor;
    }
    
    public EditorProperty getPreviousEditor(final Property property) {
        final EditorProperty currentPed = getPropEditor(property.getName());
        final int currentIndex = this.propEditors.indexOf(currentPed);
        EditorProperty prevEditor = null;
        if (currentIndex > 0) {
            prevEditor = this.propEditors.get(currentIndex - 1);
        } else {
            prevEditor = this.propEditors.get(this.propEditors.size() - 1);
        }
        return prevEditor;
    }
}
