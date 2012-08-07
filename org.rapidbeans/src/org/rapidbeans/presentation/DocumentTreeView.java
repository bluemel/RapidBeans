/*
 * Rapid Beans Framework: DocumentTreeView.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 02/11/2006
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

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.datasource.Filter;
import org.rapidbeans.datasource.event.DocumentChangeListener;
import org.rapidbeans.presentation.event.SettingsChangedEvent;
import org.rapidbeans.presentation.event.SettingsChangedListener;
import org.rapidbeans.presentation.settings.SettingsAll;
import org.rapidbeans.presentation.settings.SettingsBasic;
import org.rapidbeans.presentation.settings.SettingsBasicGui;
import org.rapidbeans.presentation.settings.SettingsBasicGuiPropTreeViewShowBeanLinks;
import org.rapidbeans.presentation.swing.DocumentTreeNodeBeanLink;
import org.rapidbeans.presentation.swing.DocumentTreeViewSwing;


/**
 * A tree view for a bean document.
 *
 * @author Martin Bluemel
 */
public abstract class DocumentTreeView
    implements View, DocumentChangeListener, SettingsChangedListener {

    /**
     * the document viewed.
     */
    private Document document = null;

    /**
     * @return the document
     */
    protected Document getDocument() {
        return this.document;
    }

    /**
     * @return the view's title.
     */
    public String getTitle() {
        return null;
    }

    /**
     * change the tree view selection.
     *
     * @param treePath the tree path identifying the tree object to select
     */
    protected abstract void setSelectedBean(Object treePath);

    /**
     * constructor.
     *
     * @param doc the document
     * @param filter the filter
     */
    protected DocumentTreeView(final Document doc, final Filter filter) {
        this(ApplicationManager.getApplication(), doc, filter);
    }

    /**
     * constructor.
     *
     * @param client the client
     * @param doc the document
     * @param filter the filter
     */
    protected DocumentTreeView(final Application client, final Document doc, final Filter filter) {
        this.document = doc;
        this.filter = filter;
        final SettingsAll settingsAll = client.getSettings();
        final SettingsBasic settingsBasic = settingsAll.getBasic();
        final SettingsBasicGui settingsBasicGui = settingsBasic.getGui();
        this.showBeanLinks = settingsBasicGui.getTreeViewShowBeanLinks();
        if (client != null) {
            client.addSettingsChangedListener(this);
        }
    }

    /**
     * @return if bean links are presented to the tree view.
     */
    public boolean getShowBeanLinks() {
        return this.showBeanLinks;
    }

    /**
     * setter.
     *
     * @param show  determines if bean links
     *              are presented to the tree view.
     */
    public void setShowBeanLinks(final boolean show) {
        if (show != this.showBeanLinks) {
            this.showBeanLinks = show;
            this.changeShowBeanLinks();
        }
    }

    /**
     * Change the show bean links behaviour.
     */
    protected abstract void changeShowBeanLinks();

    /**
     * Retrieve an object representing the bean in the TreeView.
     *
     * @param bean the bean
     *
     * @return the path
     */
    protected abstract Object getTreeKey(RapidBean bean);

    /**
     * if bean links shall be shown or not.
     */
    private boolean showBeanLinks = true;

    /**
     * if properties shall be shown or not.
     */
    private boolean showProperties = true;

    /**
     * initiates creating of a new bean.
     *
     * @return the bean editor used for creating the new bean
     */
    public abstract EditorBean createBean();

    /**
     * @return if properties are presented to the tree view.
     */
    protected boolean getShowProperties() {
        return showProperties;
    }

    /**
     * setter.
     *
     * @param show  determines if properties
     *              are presented to the tree view.
     */
    public void setShowProperties(final boolean show) {
        if (show != this.showProperties) {
            this.showProperties = show;
            this.changeShowProperties();
        }
    }

    /**
     * Change the show bean links behaviour.
     */
    protected abstract void changeShowProperties();

    /**
     * create a DocumentTreeView of a special type.
     *
     * @param client the parent client
     * @param document the document to show
     * @param filter the filter
     *
     * @return the instance
     */
    public static DocumentTreeView createInstance(final Application client,
            final Document document, final Filter filter) {
        DocumentTreeView treeView = null;
        switch (client.getConfiguration().getGuitype()) {
        case swing:
            treeView = new DocumentTreeViewSwing(client, document, filter);
            break;
        case eclipsercp:
            //mainWindow = new BBMainWindowEclispercp();
            break;
        default:
            throw new RapidBeansRuntimeException("Unknown GUI type \""
                + client.getConfiguration().getGuitype().name() + "\"");
        }

        return treeView;
    }

    /**
     * the collection of registered tree view listeners.
     */
    private DocumentTreeViewListener listener = null;

    /**
     * adds a tree view listener tha want to be notified be tree view
     * events.
     *
     * @param tlistener the tree view listener to add
     */
    public void setTreeViewListener(final DocumentTreeViewListener tlistener) {
        this.listener = tlistener;
    }

    /**
     * removes a tree view listener that does not want to be notified
     * anymore.
     */
    public void clearTreeViewListener() {
        this.listener = null;
    }

    /**
     * flag to supress reaction on selection changed event.
     */
    private int supressSelectionChangedHandling = 0;

    /**
     * @return the flag to supress reaction on selection changed event.
     */
    protected boolean getSupressSelectionChangedHandling() {
        return this.supressSelectionChangedHandling > 0;
    }

    /**
     * increment or decrement the selection change handling counter.
     *
     * @param suppress suppress or not.
     */
    protected synchronized void suppressSelectionChangeHandling(final boolean suppress) {
        if (suppress) {
            this.supressSelectionChangedHandling++;
        } else {
            this.supressSelectionChangedHandling--;
        }
    }

    /**
     * the filter.
     */
    private Filter filter = null;

    /**
     * create a bean of the given type.
     * @param key the bean's path
     * @param colProp the parent bean collection property of the new bean
     *
     * @return the bean editor created
     */
    public EditorBean createBean(final Object key, final PropertyCollection colProp) {
        if (getSupressSelectionChangedHandling()) {
            return null;
        }
        return this.listener.createBean(key, colProp);
    }

    /**
     * delete some beans.
     *
     * @param selObjs the selected objects
     */
    protected void deleteBeans(final Object[] selObjs) {
        // filter beans and the associated keys
        for (int i = 0; i < selObjs.length; i++) {
            if (selObjs[i] instanceof RapidBean) {
                ((RapidBean) selObjs[i]).delete();
            }
        }
    }

    /**
     * notifies all registered listenrs that a bean has bee selected.
     *
     * @param keys the tree path
     * @param selObjs the object (bean or Property) that recently has been selected.
     *
     * @return the bean editor of the last bean edited
     */
    public EditorBean editBeans(final Object[] keys, final Object[] selObjs) {
        EditorBean editor = null;
        if (getSupressSelectionChangedHandling()) {
            return editor;
        }

        // filter beans and the associated keys
        int j = 0;
        for (int i = 0; i < selObjs.length; i++) {
            if (selObjs[i] instanceof RapidBean
                    || selObjs[i] instanceof DocumentTreeNodeBeanLink) {
                j++;
            }
        }
        RapidBean[] ba = new RapidBean[j];
        Object[] bKeys = new Object[j];
        j = 0;
        for (int i = 0; i < selObjs.length; i++) {
            if (selObjs[i] instanceof RapidBean) {
                bKeys[j] = keys[j];
                ba[j++] = (RapidBean) selObjs[i];
            } else if (selObjs[i] instanceof DocumentTreeNodeBeanLink) {
                bKeys[j] = this.getOriginalForLink(keys[j]);
                ba[j++] = ((DocumentTreeNodeBeanLink) selObjs[i]).getLinkedBean();
            }
        }

        if (ba.length > 0) {
            editor = this.listener.editBeans(bKeys, ba);
        }
        return editor;
    }

    /**
     * retrieve the original object for the link object.
     *
     * @param link the link object
     *
     * @return the original object
     */
    protected abstract Object getOriginalForLink(Object link);

    /**
     * the document view's name is the document's name.
     *
     * @return the document view's name
     */
    public String getName() {
        return "documentview.treeview." + this.document.getConfigNameOrName();
    }

    /**
     * reset the document change mark.
     */
    public void documentSaved() {
    }

    /**
     * close the document tree view.
     *
     * @return if cancelling is desired
     */
    public boolean close() {
        Application client = ApplicationManager.getApplication();
        if (client != null) {
            client.removeSettingsChangedListener(this);
        }
        return false;
    }

    /**
     * implementation of the SettingsChangeListener interface.
     *
     * @param e the change event
     */
    public void settingsChanged(final SettingsChangedEvent e) {
        Property prop = e.getSettingsProp();
        if (prop.getClass() != SettingsBasicGuiPropTreeViewShowBeanLinks.class) {
            return;
        }
        boolean newShowLinks = ((SettingsBasicGuiPropTreeViewShowBeanLinks)
            prop).getValueBoolean();
        this.setShowBeanLinks(newShowLinks);
    }

    /**
     * @return the filter
     */
    protected Filter getFilter() {
        return filter;
    }
}
