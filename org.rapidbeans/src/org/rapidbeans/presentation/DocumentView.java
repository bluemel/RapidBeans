/*
 * Rapid Beans Framework: DocumentView.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/14/2006
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
import java.util.MissingResourceException;

import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.datasource.Filter;
import org.rapidbeans.datasource.event.AddedEvent;
import org.rapidbeans.datasource.event.ChangedEvent;
import org.rapidbeans.datasource.event.DocumentChangeListener;
import org.rapidbeans.datasource.event.RemovedEvent;
import org.rapidbeans.presentation.config.ConfigApplication;
import org.rapidbeans.presentation.config.ConfigDocument;
import org.rapidbeans.presentation.config.ConfigPropPersistencestrategy;
import org.rapidbeans.presentation.config.ConfigView;
import org.rapidbeans.presentation.swing.DocumentViewSwing;

/**
 * A view for a bean document.
 * 
 * @author Martin Bluemel
 */
public abstract class DocumentView implements View, DocumentTreeViewListener, EditorBeanListener,
		DocumentChangeListener {

	/**
	 * @return the divider location
	 */
	public int getDividerLocation() {
		return -1;
	}

	/**
	 * @param the
	 *            divider location
	 */
	public void setDividerLocation(final int loc) {
	}

	/**
	 * the client.
	 */
	private Application client = null;

	protected MainWindow getMainwindow() {
		if (this.client == null) {
			return null;
		}
		return this.client.getMainwindow();
	}

	/**
	 * @return the view's title.
	 */
	public String getTitle() {
		String idstring = null;
		final RapidBeansLocale locale = this.getClient().getCurrentLocale();

		final RapidBean bean = this.document.getRoot();

		// gui.properties: view.<document config type name>.<config type
		// name>.title
		if (this.getConfigDocument() != null && this.getConfiguration() != null) {
			try {
				final String key = "view." + this.getConfigDocument().getName() + "."
						+ this.getConfiguration().getName() + ".title";
				final String pattern = locale.getStringGui(key);
				idstring = bean.expandPropertyValues(pattern, locale);
			} catch (MissingResourceException e) {
				idstring = null;
			}
		}

		// gui.properties: document.<document config type name>.title
		if (idstring == null && this.getConfigDocument() != null) {
			try {
				final String key = "document." + this.getConfigDocument().getName() + ".title";
				final String pattern = locale.getStringGui(key);
				idstring = bean.expandPropertyValues(pattern, locale);
			} catch (MissingResourceException e) {
				idstring = null;
			}
		}

		// gui.properties: document.<bean classname>.title
		if (idstring == null) {
			try {
				final String key = "document." + bean.getClass().getName().toLowerCase() + ".title";
				final String pattern = locale.getStringGui(key);
				idstring = bean.expandPropertyValues(pattern, locale);
			} catch (MissingResourceException e) {
				idstring = null;
			}
		}

		// gui.properties: bean.<bean classname>
		if (idstring == null) {
			try {
				final String key = "bean." + bean.getType().getName().toLowerCase();
				final String pattern = locale.getStringGui(key);
				idstring = bean.expandPropertyValues(pattern, locale);
			} catch (MissingResourceException e) {
				idstring = null;
			}
		}

		if (idstring == null) {
			idstring = bean.toStringGui(locale);
		}

		return idstring;
	}

	/**
	 * @return the client
	 */
	public Application getClient() {
		return this.client;
	}

	/**
	 * the tree view.
	 */
	private DocumentTreeView treeView = null;

	/**
	 * @return the tree view
	 */
	public DocumentTreeView getTreeView() {
		return this.treeView;
	}

	/**
	 * the document viewed.
	 */
	private Document document = null;

	/**
	 * @return the document viewed
	 */
	public Document getDocument() {
		return this.document;
	}

	/**
	 * the view's name.
	 */
	private String name = null;

	/**
	 * the document view's name is the document's name.
	 * 
	 * @return the document view's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the configured persistence strategy if any or 'ondemand' per
	 *         default
	 */
	public ConfigPropPersistencestrategy getPersistencestrategy() {
		return this.getConfiguration().getPersistencestrategy();
	}

	/**
	 * the editors.
	 */
	private HashMap<String, EditorBean> editors = new HashMap<String, EditorBean>();

	/**
	 * the tree paths.
	 */
	private HashMap<String, Object> treePaths = new HashMap<String, Object>();

	/**
	 * get an editor by a bean's ID.
	 * 
	 * @param bean
	 *            the edited bean
	 * @param newMode
	 *            if the editor to get is in new mode
	 * 
	 * @return the bean editor for that key
	 */
	public EditorBean getEditor(final RapidBean bean, final boolean newMode) {
		final String key = this.getEditorKey(newMode, bean);
		return this.editors.get(key);
	}

	/**
	 * get all bean editors.
	 * 
	 * @return all bean editors in the order first opened first
	 */
	public Collection<EditorBean> getEditors() {
		return this.editors.values();
	}

	/**
	 * @return the number of all open editors.
	 */
	public int getOpenEditorsNumber() {
		return this.editors.values().size();
	}

	/**
	 * get a tree path by a bean's ID.
	 * 
	 * @param bean
	 *            the edited bean
	 * 
	 * @return the tree path for that key
	 */
	protected Object getTreePath(final RapidBean bean) {
		final String key = bean.getType().getName() + "::" + bean.getIdString();
		return this.treePaths.get(key);
	}

	/**
	 * the view's document configuration.
	 */
	private ConfigDocument configDocument = null;

	/**
	 * @return the view's document configuration
	 */
	public ConfigDocument getConfigDocument() {
		return this.configDocument;
	}

	/**
	 * the view's configuration.
	 */
	private ConfigView configuration = null;

	/**
	 * @return the view's configuration
	 */
	public ConfigView getConfiguration() {
		return this.configuration;
	}

	/**
	 * constructor.
	 * 
	 * @param clnt
	 *            the client
	 * @param doc
	 *            the document
	 * @param docconfname
	 *            the view's document config name
	 * @param viewconfname
	 *            the view's config name
	 * @param filter
	 *            the filter to apply
	 */
	protected DocumentView(final Application clnt, final Document doc, final String docconfname,
			final String viewconfname, final Filter filter) {
		this.client = clnt;
		this.document = doc;
		if (filter != null) {
			this.beanFilter = filter;
			this.beanFilter.setDocument(doc);
		}
		if (clnt.getConfiguration() != null) {
			this.configDocument = clnt.getConfiguration().getConfigDocument(docconfname);
		}
		if (this.configDocument == null) {
			this.configDocument = new ConfigDocument();
			this.configDocument.setName(docconfname);
		}
		if (clnt.getConfiguration() != null) {
			this.configuration = clnt.getConfiguration()
					.getConfigView(this.getConfigDocument().getName(), viewconfname);
		}
		if (this.configuration == null) {
			this.configuration = new ConfigView();
		}
		this.name = doc.getName() + "." + viewconfname;
		this.treeView = DocumentTreeView.createInstance(this.client, doc, filter);

		this.document.addDocumentChangeListener(this);
		this.treeView.setTreeViewListener(this);
	}

	/**
	 * edit a bean.
	 * 
	 * @param bean
	 *            the bean to edit
	 * 
	 * @return the editor opened
	 */
	public EditorBean editBean(final RapidBean bean) {
		Object[] keys = { this.treeView.getTreeKey(bean) };
		Object[] selObjs = { bean };
		return this.treeView.editBeans(keys, selObjs);
	}

	/**
	 * handler for selected beans.
	 * 
	 * @param bean
	 *            the selected bean
	 * @param newBeanParent
	 *            a new Bean's parent collection property. Is not null if a new
	 *            Bean is to be created Is null if an existing bean is simply
	 *            edited
	 * @param treePath
	 *            the tree path
	 * @param newMode
	 *            if the editor is opened in new mode or not
	 * 
	 * @return the editor created.
	 */
	protected EditorBean addBeanEditor(final RapidBean bean, final PropertyCollection newBeanParent,
			final Object treePath, final boolean newMode) {
		final EditorBean editor = EditorBean.createInstance(this.client, this, bean, newBeanParent);
		if (newMode) {
			final Application app = ApplicationManager.getApplication();
			if (app != null) {
				final CreateNewBeansEditorApplyBehaviour mode = app.getSettings().getBasic().getGui()
						.getCreateNewBeansEditorApplyBehaviour();
				editor.setCreateApplyMode(mode);
			}
		}
		final String key = this.getEditorKey(newMode, bean);
		this.editors.put(key, editor);
		this.treePaths.put(key, treePath);
		editor.addEditorListener(this);
		return editor;
	}

	/**
	 * @return the key of the selected editor.
	 */
	protected abstract String getSelectedEditorKey();

	/**
	 * Update the document view's title.
	 */
	protected abstract void updateTitle();

	/**
	 * handler for closed bean editors.
	 * 
	 * @param editor
	 *            the editor to close
	 */
	public void closeBeanEditor(final EditorBean editor) {
		String key = this.getEditorKey(editor.isInNewMode(), editor.getBean());
		this.editors.remove(key);
		selectCurrentlySelectedEditorInTreeView();
	}

	/**
	 * @param isInNewMode
	 *            if the editor is in new modeor not
	 * @param bean
	 *            the bean to be edited
	 * @return the editor key
	 */
	private String getEditorKey(final boolean isInNewMode, final RapidBean bean) {
		String key;
		if (isInNewMode) {
			key = bean.getType().getName() + "::@@new@@";
		} else {
			key = bean.getType().getName() + "::" + bean.getIdString();
		}
		return key;
	}

	/**
	 * select the currently selected editor in the tree view.
	 */
	public void selectCurrentlySelectedEditorInTreeView() {
		this.treeView.setSelectedBean(this.treePaths.get(this.getSelectedEditorKey()));
	}

	/**
	 * select the currently selected editor in the tree view.
	 */
	public void selectBeanInTreeView(final RapidBean bean) {
		this.treeView.setSelectedBean(this.treePaths.get(bean));
	}

	/**
	 * constructor argument types.
	 */
	private static final Class<?>[] CONSTR_PARTYPES = { Application.class, Document.class, String.class, String.class,
			Filter.class };

	/**
	 * create a DocumentView of a special type.
	 * 
	 * @param client
	 *            the parent client
	 * @param document
	 *            the document to show
	 * @param docconfname
	 *            the view's document configuration name
	 * @param viewconfname
	 *            the view's configuration name
	 * @param filter
	 *            the filter
	 * 
	 * @return the instance
	 */
	public static DocumentView createInstance(final Application client, final Document document,
			final ConfigDocument docconf, final ConfigView viewconf, final Filter filter) {
		String docconfname = null;
		if (docconf != null) {
			docconfname = docconf.getName();
		}
		String viewconfname = null;
		if (viewconf != null) {
			viewconfname = viewconf.getName();
		}
		return createInstance(client, document, docconfname, viewconfname, filter);
	}

	/**
	 * the BiBeanFilter applied to this view.
	 */
	private Filter beanFilter = null;

	/**
	 * create a DocumentView of a special type.
	 * 
	 * @param client
	 *            the parent client
	 * @param document
	 *            the document to show
	 * @param docconfname
	 *            the view's document configuration name
	 * @param viewconfname
	 *            the view's configuration name
	 * @param filter
	 *            the filter
	 * 
	 * @return the instance
	 */
	public static DocumentView createInstance(final Application client, final Document document,
			final String doccfgname, final String viewcfgname, final Filter filter) {
		String docconfname = null;
		if (doccfgname == null) {
			docconfname = ConfigDocument.NAME_NO_CONFIG;
		} else {
			docconfname = doccfgname;
		}
		document.setConfigName(docconfname);
		String viewconfname = null;
		if (viewcfgname == null) {
			viewconfname = ConfigView.NAME_NO_CONFIG;
		} else {
			viewconfname = viewcfgname;
		}
		DocumentView documentView = null;
		final ConfigApplication clientCfg = client.getConfiguration();
		if (clientCfg != null) {
			final ConfigView viewconf = clientCfg.getConfigView(docconfname, viewconfname);
			if (viewconf != null && viewconf.getViewclass() != null) {
				Class<?> viewclass = null;
				try {
					viewclass = Class.forName(viewconf.getViewclass());
				} catch (ClassNotFoundException e) {
					viewclass = null;
				}
				if (viewclass != null) {
					try {
						Constructor<?> constr = viewclass.getConstructor(CONSTR_PARTYPES);
						Object[] oa = { client, document, docconfname, viewconfname, filter };
						documentView = (DocumentView) constr.newInstance(oa);
					} catch (SecurityException e) {
						throw new RapidBeansRuntimeException(e);
					} catch (NoSuchMethodException e) {
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
		if (documentView == null) {
			switch (client.getConfiguration().getGuitype()) {
			case swing:
				documentView = new DocumentViewSwing(client, document, docconfname, viewconfname, filter);
				break;
			case eclipsercp:
				// mainWindow = new BBMainWindowEclispercp();
				break;
			default:
				throw new RapidBeansRuntimeException("Unknown GUI type \""
						+ client.getConfiguration().getGuitype().name() + "\"");
			}
		}

		return documentView;
	}

	/**
	 * event handler for bean pre add event.
	 * 
	 * @param e
	 *            the added event
	 */
	public void beanAddPre(final AddedEvent e) {
		if (ThreadLocalEventLock.get()) {
			return;
		}
		this.treeView.beanAddPre(e);
		for (EditorBean bbEditor : this.editors.values()) {
			bbEditor.beanAddPre(e);
		}
	}

	/**
	 * event handler for bean added event.
	 * 
	 * @param e
	 *            the added event
	 */
	public void beanAdded(final AddedEvent e) {
		if (ThreadLocalEventLock.get()) {
			return;
		}
		this.treeView.beanAdded(e);
		for (EditorBean bbEditor : this.editors.values()) {
			bbEditor.beanAdded(e);
		}
		markAsChanged(true);
	}

	/**
	 * event handler for bean changed event.
	 * 
	 * @param e
	 *            the changed event
	 */
	public void beanChangePre(final ChangedEvent e) {
		this.treeView.beanChangePre(e);
		for (EditorBean bbEditor : this.editors.values()) {
			bbEditor.beanChangePre(e);
		}
	}

	/**
	 * event handler for bean changed event.
	 * 
	 * @param e
	 *            the changed event
	 */
	public void beanChanged(final ChangedEvent e) {
		markAsChanged(true);
		this.treeView.beanChanged(e);
		if (e.getBean() == this.document.getRoot()) {
			this.updateTitle();
		}
		for (EditorBean bbEditor : this.editors.values()) {
			bbEditor.beanChanged(e);
		}
	}

	/**
	 * event handler for bean pre remove event.
	 * 
	 * @param e
	 *            the removed event
	 */
	public void beanRemovePre(final RemovedEvent e) {
		if (ThreadLocalEventLock.get()) {
			return;
		}
		this.treeView.beanRemovePre(e);
		for (EditorBean bbEditor : this.editors.values()) {
			bbEditor.beanRemovePre(e);
		}
	}

	/**
	 * event handler for bean removed event.
	 * 
	 * @param event
	 *            the removed event
	 */
	public void beanRemoved(final RemovedEvent event) {
		if (ThreadLocalEventLock.get()) {
			return;
		}
		this.treeView.beanRemoved(event);
		ArrayList<EditorBean> bbEditors = new ArrayList<EditorBean>();
		for (EditorBean bbEditor : this.editors.values()) {
			bbEditors.add(bbEditor);
		}
		for (EditorBean bbEditor : bbEditors) {
			bbEditor.beanRemoved(event);
		}
		markAsChanged(true);
	}

	/**
	 * reset the document change mark.
	 */
	public void documentSaved() {
		markAsChanged(false);
	}

	/**
	 * close a document view.
	 * 
	 * @return if cancelling is desired
	 */
	public boolean close() {
		boolean cancel = false;
		ArrayList<EditorBean> clonedEditors = new ArrayList<EditorBean>();
		for (EditorBean editor : this.editors.values()) {
			clonedEditors.add(editor);
		}
		for (EditorBean editor : clonedEditors) {
			cancel = editor.close();
			if (cancel) {
				break;
			}
		}
		final Application app = ApplicationManager.getApplication();
		if (!cancel) {
			switch (this.getPersistencestrategy()) {
			case onclosedocumentview:
			case oncloseeditor:
				if (this.document.getChanged()) {
					if (app != null) {
						app.save(this.document);
					} else {
						this.document.save();
					}
				}
				break;
			default:
				break;
			}
		}
		if ((!cancel) && this.document.getChanged() && this.client.isLastOpenDocumentView(this)
				&& (app == null || (!app.getTestMode()))) {
			final RapidBeansLocale locale = this.client.getCurrentLocale();
			final String msg = locale.getStringMessage("messagedialog.documentview.close", this.getTitle());
			MessageDialogResponse response = this.client.messageYesNoCancel(msg,
					locale.getStringMessage("messagedialog.documentview.close.title"));
			switch (response) {
			case yes:
				if (this.document.getUrl() == null) {
					DocumentController.saveAs(this.document);
				}
				if (this.document.getUrl() != null) {
					if (app != null) {
						app.save(this.document);
					} else {
						this.document.save();
					}
				} else {
					cancel = true;
				}
				break;
			case no:
				break;
			default:
				cancel = true;
				break;
			}
		}
		if (!cancel) {
			this.document.removeDocumentChangeListener(this);
			if (this.client.getView(this.name) != null) {
				this.client.removeView(this);
			}
			if (this.client.getDocument(this.document.getName()) != null) {
				this.client.removeDocument(this.document);
			}
		}
		return cancel;
	}

	/**
	 * mark / unmark the document as changed.
	 * 
	 * @param changed
	 *            if changed or unchanged
	 */
	public abstract void markAsChanged(final boolean changed);

	/**
	 * @return the beanFilter
	 */
	protected Filter getBeanFilter() {
		return beanFilter;
	}
}
