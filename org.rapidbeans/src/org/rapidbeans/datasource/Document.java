/*
 * Rapid Beans Framework: Document.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/31/2006
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

package org.rapidbeans.datasource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.rapidbeans.core.basic.ContainerImpl;
import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.LinkFrozen;
import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanState;
import org.rapidbeans.core.common.RapidBeanDeserializer;
import org.rapidbeans.core.common.RapidBeanSerializer;
import org.rapidbeans.core.event.PropertyChangeEvent;
import org.rapidbeans.core.exception.BeanDuplicateException;
import org.rapidbeans.core.exception.BeanNotFoundException;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.exception.ValidationInstanceAssocTwiceException;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.ClassHelper;
import org.rapidbeans.datasource.event.AddedEvent;
import org.rapidbeans.datasource.event.ChangedEvent;
import org.rapidbeans.datasource.event.DocumentChangeListener;
import org.rapidbeans.datasource.event.RemovedEvent;
import org.rapidbeans.datasource.query.Query;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.config.ConfigDocument;

/**
 * A Document can be seen an in Memory DB of BizBEans persisted in an XML file.
 * 
 * @author Martin Bluemel
 */
public class Document extends ContainerImpl {

	/**
	 * The default character set if nothing is defined at all.
	 */
	public final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * the document name.
	 */
	private String name = null;

	/**
	 * @return the document's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * the read only flag.
	 */
	private boolean readonly = false;

	/**
	 * @return the read only flag
	 */
	public boolean getReadonly() {
		return this.readonly;
	}

	/**
	 * @param readonly
	 *            the read only to set
	 */
	public void setReadonly(final boolean readonly) {
		this.readonly = readonly;
	}

	/**
	 * the document configuration name.
	 */
	private String configName = ConfigDocument.NAME_NO_CONFIG;

	/**
	 * @return the document's configuration name
	 */
	public String getConfigName() {
		return this.configName;
	}

	/**
	 * @return the document's configuration name
	 */
	public String getConfigNameOrName() {
		if (this.configName.equals(ConfigDocument.NAME_NO_CONFIG)) {
			if (this.name.startsWith("file")) {
				return ConfigDocument.NAME_NO_CONFIG;
			} else {
				return this.name;
			}
		} else {
			return this.configName;
		}
	}

	/**
	 * the document URL.
	 */
	private URL url = null;

	/**
	 * @return Returns the url.
	 */
	public URL getUrl() {
		return this.url;
	}

	/**
	 * @param argFile
	 *            The file to set
	 */
	public void setUrl(final URL argUrl) {
		this.url = argUrl;
	}

	/**
	 * Equals method.
	 * 
	 * @param other
	 *            the other object
	 * 
	 * @return if the URL's equal
	 */
	@Override
	public boolean equals(final Object other) {
		return this.url.equals(((Document) other).url);
	}

	/**
	 * Hash code method.
	 * 
	 * @return the URL's hash code
	 */
	@Override
	public synchronized int hashCode() {
		return this.url.hashCode();
	}

	/**
	 * the document's root bean.
	 */
	private RapidBean root = null;

	/**
	 * the document's identity map.
	 */
	private IdMap idmap = null;

	/**
	 * the document's character encoding.
	 */
	private String encoding = null;

	/**
	 * @return the document's character encoding.
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(final String encoding) {
		this.encoding = encoding;
	}

	/**
	 * the change flag.
	 */
	private boolean changed;

	/**
	 * @return the change flag.
	 */
	public boolean getChanged() {
		return this.changed;
	}

	public void resetChanged() {
		this.changed = false;
	}

	/**
	 * Map a document filename to an appropriate document name.
	 * 
	 * @param docfile
	 *            the document file
	 * 
	 * @return the document name
	 */
	public static String mapToDocname(final File docfile) {
		String docname = null;
		try {
			docname = new File(docfile.getCanonicalPath()).toURI().toURL().toString();
		} catch (MalformedURLException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (IOException e) {
			throw new RapidBeansRuntimeException(e);
		}
		return docname;
	}

	/**
	 * Map a document filename to an appropriate document name.
	 * 
	 * @param docurl
	 *            the document URL
	 * 
	 * @return the document name
	 */
	public static String mapToDocname(final URL docurl) {
		return docurl.toString();
	}

	/**
	 * constructs a Document by reading the XML file. The specified file's URL
	 * will be new the document's name.
	 * 
	 * @param rbType
	 *            the root bean type
	 * @param docfile
	 *            the XML file
	 */
	public Document(final File docfile) {
		this(mapToDocname(docfile), null, docfile);
	}

	/**
	 * constructs a Document by reading the XML file. The specified file's URL
	 * will be new the document's name.
	 * 
	 * @param docfile
	 *            the XML file
	 */
	public Document(final TypeRapidBean rbType, final File docfile) {
		this(mapToDocname(docfile), rbType, docfile);
	}

	/**
	 * constructs a Document by reading the XML file.
	 * 
	 * @param docname
	 *            the document's name
	 * @param docfile
	 *            the XML file
	 */
	public Document(final String docname, final File docfile) {
		this(docname, null, docfile);
	}

	/**
	 * constructs a Document by reading the XML file.
	 * 
	 * @param docname
	 *            the document's name
	 * @param rbType
	 *            the root bean type
	 * @param docfile
	 *            the XML file
	 */
	public Document(final String docname, final TypeRapidBean rbType, final File docfile) {
		try {
			this.name = docname;
			this.url = RapidBeanDeserializer.urlFromFile(docfile);
			final RapidBeanDeserializer deser = new RapidBeanDeserializer();
			this.root = deser.loadBean(rbType, this.url);
			this.encoding = deser.getEncoding();
			this.init();
			this.validate();
		} catch (RapidBeansRuntimeException e) {
			throw new RapidBeansRuntimeException("Error while deserializing document from file \""
					+ docfile.getAbsolutePath() + "\"", e);
		}
	}

	/**
	 * validate the document.
	 */
	public void validate() {
		this.getRoot().validate();
	}

	/**
	 * constructs a Document by reading the XML file specified by the given URL.
	 * 
	 * @param docname
	 *            the document's name
	 * @param docfile
	 *            the XML file
	 */
	public Document(final String docname, final URL docurl) {
		this(docname, null, docurl);
	}

	/**
	 * constructs a Document by reading the XML file specified by the given URL.
	 * 
	 * @param rbType
	 *            the RapidBeanType of the new document's root bean
	 * @param docfile
	 *            the XML file
	 */
	public Document(final TypeRapidBean rbType, final URL docurl) {
		this(mapToDocname(docurl), rbType, docurl);
	}

	/**
	 * constructs a Document by reading the XML file specified by the given URL.
	 * 
	 * @param docname
	 *            the document's name
	 * @param rbType
	 *            the RapidBeanType of the new document's root bean
	 * @param docfile
	 *            the XML file
	 */
	public Document(final String docname, final TypeRapidBean rbType, final URL docurl) {
		try {
			this.name = docname;
			this.url = docurl;
			final RapidBeanDeserializer deser = new RapidBeanDeserializer();
			this.root = deser.loadBean(rbType, this.url);
			this.encoding = deser.getEncoding();
			init();
		} catch (RapidBeansRuntimeException e) {
			throw new RapidBeansRuntimeException("Error while deserializing document from file \"" + docurl.toString()
					+ "\"", e);
		}
	}

	/**
	 * constructs a Document by reading the XML file specified by the given URL.
	 * 
	 * @param docname
	 *            the document's name
	 * @param rbType
	 *            the RapidBeanType of the new document's root bean
	 * @param docurl
	 *            the URL
	 * @param is
	 *            input stream
	 */
	public Document(final String docname, final TypeRapidBean rbType, final URL docurl, final InputStream is) {
		try {
			this.name = docname;
			this.url = docurl;
			final RapidBeanDeserializer deser = new RapidBeanDeserializer();
			this.root = deser.loadBean(rbType, this.url, is);
			this.encoding = deser.getEncoding();
			init();
		} catch (RapidBeansRuntimeException e) {
			throw new RapidBeansRuntimeException("Error while deserializing document from file \"" + docurl.toString()
					+ "\"", e);
		}
	}

	/**
	 * constructs a Document out of an existing bean which will be the
	 * document's root bean.
	 * 
	 * @param rootBean
	 *            the document's root
	 */
	public Document(final RapidBean rootBean) {
		this.root = rootBean;
		this.name = "document";
		init();
	}

	/**
	 * constructs a Document out of an existing bean which will be the
	 * document's root bean.
	 * 
	 * @param docname
	 *            the document's name
	 * @param rootBean
	 *            the document's root
	 */
	public Document(final String docname, final RapidBean rootBean) {
		this.name = docname;
		this.root = rootBean;
		init();
	}

	/**
	 * common initialization steps.
	 */
	private void init() {
		this.idmap = new IdMap();
		final DocumentTreeVisitor visitor = new DocumentTreeVisitorInitIdMap(this, this.idmap);
		this.traverseDocumentTree(0, visitor, this.root);
		this.resolveFrozenLinks();
		this.changed = false;
	}

	/**
	 * Resolve all the document's frozen links.
	 */
	public void resolveFrozenLinks() {
		DocumentTreeVisitor visitor = new DocumentTreeVisitorResolveFrozenLinks(this.idmap);
		this.traverseDocumentTree(0, visitor, this.root);
	}

	/**
	 * @return the document's root bean
	 */
	public RapidBean getRoot() {
		return this.root;
	}

	/**
	 * setter.
	 * 
	 * @param root
	 *            the new root to set.
	 */
	protected void setRoot(RapidBean root) {
		this.root = root;
	}

	/**
	 * write a bean's data to the persistent store.
	 */
	public void save() {
		save(null, false, null);
	}

	/**
	 * write a bean's data to the persistent store.
	 * 
	 * @param useEncoding
	 *            prescribe an encoding to use for writing not matter what
	 *            encoding the document had until now. If you leave this value
	 *            empty the original encoding is preserved.
	 * 
	 * @param forceEncoding
	 * <br/>
	 *            <li>true: the given encoding will be preferred if not null</li> <li>false: the original encoding will be preferred if not null</li>
	 * @param useUrl
	 *            the URL to use for writing. If it is null the file's URL will
	 *            be automatically used.
	 */
	public void save(final String useEncoding, final boolean forceEncoding, final URL useUrl) {
		String writeEncoding = null;
		if (forceEncoding) {
			writeEncoding = useEncoding;
			if (writeEncoding == null) {
				writeEncoding = this.encoding;
			}
		} else {
			writeEncoding = this.encoding;
			if (writeEncoding == null) {
				writeEncoding = useEncoding;
			}
		}
		if (writeEncoding == null) {
			writeEncoding = DEFAULT_CHARSET;
		}
		if (useUrl == null) {
			(new RapidBeanSerializer()).saveBean(this.root, this.url, writeEncoding);
		} else {
			(new RapidBeanSerializer()).saveBean(this.root, useUrl, writeEncoding);
		}
		this.fireDocumentSaved();
	}

	/**
	 * returns a bean's data as an XML string.
	 * 
	 * @param useEncoding
	 *            prescribe an encoding to use for writing not matter what
	 *            encoding the document had until now. If you leave this value
	 *            empty the original encoding is preserved.
	 * 
	 * @param forceEncoding
	 * <br/>
	 *            <li>true: the given encoding will be preferred if not null</li> <li>false: the original encoding will be preferred if not null</li>
	 */
	public String toXmlString(final String useEncoding, final boolean forceEncoding) {
		String writeEncoding = null;
		if (forceEncoding) {
			writeEncoding = useEncoding;
			if (writeEncoding == null) {
				writeEncoding = this.encoding;
			}
		} else {
			writeEncoding = this.encoding;
			if (writeEncoding == null) {
				writeEncoding = useEncoding;
			}
		}
		if (writeEncoding == null) {
			writeEncoding = DEFAULT_CHARSET;
		}
		return (new RapidBeanSerializer()).toString(this.root, writeEncoding);
	}

	/**
	 * insert (create) a new bean in the DB.
	 * 
	 * @param bean
	 *            the bean to insert
	 */
	public void insert(final RapidBean bean) {
		this.insert(bean, false);
	}

	/**
	 * insert (create) a new bean in the DB.
	 * 
	 * @param bean
	 *            the bean to insert
	 * @param implicitly
	 *            special for documents. Usually you do not explicitly insert
	 *            beans into a document. Instead insert them implicitly by
	 *            adding the to a parent bean. If you anyway try to insert
	 *            explicitly the document tries to find an appropriate location
	 *            according to the following strategy. <li>Find all composition collection properties that have the type of the bean to insert as target type.</li> <li>If there is exactly one add the bean there. Otherwise throw an appropriate exception.</li>
	 */
	public void insert(final RapidBean bean, final boolean implicitly) {
		if (this.idmap.findBean(bean.getType().getName(), bean.getIdString()) != null) {
			throw new BeanDuplicateException("messagedialog.create.duplicate",
					bean,
					"Bean \"" + bean.toString()
							+ "\" already exists in document \""
							+ this.getName() + "\"", new Object[] {
							bean.toStringGui(ApplicationManager.getApplication().getCurrentLocale()) });
		}
		this.fireBeanAddPre(bean);
		this.idmap.insert(bean);
		bean.setContainer(this);
		this.fireBeanAdded(bean);
	}

	/**
	 * delete a bean reference from the document.
	 * 
	 * @param bean
	 *            the bean to insert
	 */
	public void delete(final RapidBean bean) {
		if (this.idmap.findBean(bean.getType().getName(), bean.getIdString()) == null) {
			throw new BeanNotFoundException(bean.getType().getName() + ":" + bean.getIdString());
		}
		if (bean.getBeanState() != RapidBeanState.deleting) {
			this.fireBeanRemovePre(bean);
		}
		this.idmap.delete(bean);
		bean.setContainer(null);
		if (bean.getBeanState() != RapidBeanState.deleting) {
			this.fireBeanRemoved(bean);
		}
	}

	/**
	 * the collection of listeners.
	 */
	private Collection<DocumentChangeListener> listeners = new ArrayList<DocumentChangeListener>();

	/**
	 * registers a new DocumentChangeListener to this document.
	 * 
	 * @param listener
	 *            the listener to register
	 */
	public void addDocumentChangeListener(final DocumentChangeListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * unregisters a DocumentChangeListener from this document.
	 * 
	 * @param listener
	 *            the listener to unregister
	 */
	public void removeDocumentChangeListener(final DocumentChangeListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * fire the bean pre add event.
	 * 
	 * @param bean
	 *            the bean that was added (has become element of this document).
	 */
	public void fireBeanAddPre(final RapidBean bean) {
		final AddedEvent event = new AddedEvent(bean);
		for (DocumentChangeListener listener : this.listeners) {
			listener.beanAddPre(event);
		}
	}

	/**
	 * fire the bean added event.
	 * 
	 * @param bean
	 *            the bean that was added (has become element of this document).
	 */
	public void fireBeanAdded(final RapidBean bean) {
		if (!this.changed) {
			this.changed = true;
		}
		final AddedEvent event = new AddedEvent(bean);
		for (DocumentChangeListener listener : this.listeners) {
			listener.beanAdded(event);
		}
	}

	/**
	 * fire the bean pre remove event.
	 * 
	 * @param bean
	 *            the bean that is going to be removed (has become element of
	 *            this document).
	 */
	public void fireBeanRemovePre(final RapidBean bean) {
		final RemovedEvent event = new RemovedEvent(bean);
		for (final DocumentChangeListener listener : this.listeners) {
			listener.beanRemovePre(event);
		}
	}

	/**
	 * fire the bean removed event.
	 * 
	 * @param bean
	 *            the bean that was removed (has become element of this
	 *            document).
	 */
	public void fireBeanRemoved(final RapidBean bean) {
		if (!this.changed) {
			this.changed = true;
		}
		final RemovedEvent event = new RemovedEvent(bean);
		for (final DocumentChangeListener listener : this.listeners) {
			listener.beanRemoved(event);
		}
	}

	/**
	 * fire the bean before change event.
	 * 
	 * @param propEvent
	 *            the property change event
	 */
	public void fireBeanChangePre(PropertyChangeEvent propEvent) {
		final PropertyChangeEvent[] props = { propEvent };
		final ChangedEvent ce = new ChangedEvent(propEvent.getBean(), props);
		for (DocumentChangeListener listener : this.listeners) {
			listener.beanChangePre(ce);
		}
	}

	/**
	 * fire the bean changed event.
	 * 
	 * @param propEvent
	 *            the property change event
	 */
	public void fireBeanChanged(final PropertyChangeEvent propEvent) {
		if (!this.changed) {
			this.changed = true;
		}
		final PropertyChangeEvent[] props = { propEvent };
		final ChangedEvent ce = new ChangedEvent(propEvent.getBean(), props);
		for (DocumentChangeListener listener : this.listeners) {
			listener.beanChanged(ce);
		}
	}

	/**
	 * fire the Document saved event.
	 */
	public void fireDocumentSaved() {
		this.changed = false;
		for (DocumentChangeListener listener : this.listeners) {
			listener.documentSaved();
		}
	}

	// finders (queries)

	/**
	 * general query for existence of a bean by type and ID.
	 * 
	 * @param typename
	 *            the bean's type
	 * @param id
	 *            the bean's ID
	 * 
	 * @return true if found and false if not found
	 */
	public boolean contains(final String typename, final String id) {
		return this.findBean(typename, id) != null;
	}

	/**
	 * general query for existence of a bean by type and ID.
	 * 
	 * @param bean
	 *            the bean
	 * 
	 * @return true if found and false if not found
	 */
	public boolean contains(final RapidBean bean) {
		return contains(bean.getType().getName(), bean.getIdString());
	}

	/**
	 * general query for a bean by type and ID.
	 * 
	 * @param typename
	 *            the bean's type
	 * @param id
	 *            the bean's ID
	 * 
	 * @return the bean's reference or null if not found
	 */
	public RapidBean findBean(final String typename, final String id) {
		return this.idmap.findBean(typename, id);
	}

	/**
	 * find types of all beans stored in this DB.
	 * 
	 * @return a list of strings with the typenames
	 */
	public Collection<String> findAllTypenames() {
		return this.idmap.findAllTypenames();
	}

	/**
	 * query for all beans of a type.
	 * 
	 * @param typename
	 *            the name of the bean type for which you want to find
	 *            instances.
	 * 
	 * @return a list with all found beans
	 */
	public List<RapidBean> findBeansByType(final String typename) {
		return this.idmap.findBeansByType(typename);
	}

	/**
	 * find a set of beans by query.
	 * 
	 * @param queryString
	 *            the query string.
	 * 
	 * @return a list with all found beans
	 */
	public List<RapidBean> findBeansByQuery(final String queryString) {
		return new Query(queryString).findBeans(this);
	}

	/**
	 * find a set of beans by query.
	 * 
	 * @param query
	 *            the query.
	 * 
	 * @return a list with all found beans
	 */
	public List<RapidBean> findBeansByQuery(final Query query) {
		return query.findBeans(this);
	}

	/**
	 * find a single bean by query. Convenience method that also parses the
	 * query.
	 * 
	 * @param squery
	 *            the query string.
	 * 
	 * @return the bean found or null
	 */
	public RapidBean findBeanByQuery(final String squery) {
		return new Query(squery).findBean(this);
	}

	/**
	 * find a single bean by query.
	 * 
	 * @param query
	 *            the query.
	 * 
	 * @return the bean found or null
	 */
	public RapidBean findBeanByQuery(final Query query) {
		return query.findBean(this);
	}

	/**
	 * Example Array for toArray.
	 */
	static final Object[] OA = new Object[0];

	/**
	 * find the path = concatenation of relationship names.
	 * 
	 * @param bean
	 *            the bean
	 * @param separator
	 *            the separator character
	 * @return the path of the bean
	 */
	public String getPath(final RapidBean bean, final char separator) {
		final StringBuffer sb = new StringBuffer(this.getConfigNameOrName());
		Object o = bean;
		ArrayList<Property> al = new ArrayList<Property>();
		while (o != null) {
			if (o instanceof RapidBean) {
				PropertyCollection parentProp = ((RapidBean) o).getParentProperty();
				if (parentProp == null) {
					o = null;
				} else {
					o = parentProp;
				}
			} else if (o instanceof PropertyCollection) {
				o = ((PropertyCollection) o).getBean();
			} else {
				throw new RapidBeansRuntimeException("Unexpected parent class \"" + o.getClass().getName()
						+ "\"for bean tree model");
			}
			if (o != null && o instanceof Property) {
				al.add((Property) o);
			}
		}

		// stick the objects found into an array in reverse order.
		final int alSize = al.size();
		for (int i = alSize - 1; i >= 0; i--) {
			sb.append(separator);
			final String propname = al.get(i).getType().getPropName();
			sb.append(propname);
		}

		return sb.toString();
	}

	/**
	 * visitor interface for traversing the document tree.
	 * 
	 * @author Martin Bluemel
	 */
	private interface DocumentTreeVisitor {

		/**
		 * the bean processing method.
		 * 
		 * @param bean
		 *            the bean to process
		 * @param depth
		 *            the depth in the object tree
		 */
		void processBean(int depth, RapidBean bean);
	}

	/**
	 * Initialize the documents identity map.
	 * 
	 * @author Martin Bluemel
	 */
	private final class DocumentTreeVisitorInitIdMap implements DocumentTreeVisitor {

		/**
		 * the document itself.
		 */
		private Document visitorDocument = null;

		/**
		 * the document's identity map.
		 */
		private IdMap vistorIdmap = null;

		/**
		 * constructor.
		 * 
		 * @param document
		 *            the document
		 * @param pool
		 *            the pool
		 */
		public DocumentTreeVisitorInitIdMap(final Document document, final IdMap pool) {
			this.visitorDocument = document;
			this.vistorIdmap = pool;
		}

		/**
		 * the bean processor.
		 * 
		 * @param depth
		 *            the depth
		 * @param bean
		 *            the bean
		 */
		public void processBean(final int depth, final RapidBean bean) {
			this.vistorIdmap.insert(bean);
			bean.setContainer(this.visitorDocument);
		}
	}

	/**
	 * resolve frozen links.
	 * 
	 * @author Martin Bluemel
	 */
	private final class DocumentTreeVisitorResolveFrozenLinks implements DocumentTreeVisitor {

		/**
		 * the document's identity map.
		 */
		private IdMap vistorIdmap = null;

		/**
		 * constructor.
		 * 
		 * @param pool
		 *            the pool
		 */
		public DocumentTreeVisitorResolveFrozenLinks(final IdMap pool) {
			this.vistorIdmap = pool;
		}

		/**
		 * the bean processor.
		 * 
		 * @param depth
		 *            the depth in the object tree
		 * @param bean
		 *            the bean
		 */
		@SuppressWarnings("unchecked")
		public void processBean(final int depth, final RapidBean bean) {
			PropertyCollection colProp;
			TypePropertyCollection colPropType;
			TypePropertyCollection inverseColPropType;
			Collection<Link> col;
			String colPropTargetTypename;
			boolean suppressMultipleLinksToSameInstance;
			boolean ignoreAssocTwiceException;
			Class<?> colclass, inverseColclass;
			for (Property prop : bean.getPropertyList()) {
				if (prop instanceof PropertyCollection) {
					colProp = (PropertyCollection) prop;
					colPropType = (TypePropertyCollection) prop.getType();
					suppressMultipleLinksToSameInstance = true;
					ignoreAssocTwiceException = false;
					inverseColPropType = null;
					colclass = colPropType.getCollectionClass();
					inverseColclass = null;
					if (ClassHelper.classOf(Set.class, colclass)) {
						ignoreAssocTwiceException = true;
					}
					if (colPropType.getInverse() != null) {
						inverseColPropType = (TypePropertyCollection) colPropType.getTargetType().getPropertyType(
								colPropType.getInverse());
						if (inverseColPropType != null) {
							if (colclass == null) {
								colclass = TypePropertyCollection.getDefaultCollectionClass();
							}
							inverseColclass = inverseColPropType.getCollectionClass();
							if (inverseColclass == null) {
								inverseColclass = TypePropertyCollection.getDefaultCollectionClass();
							}
							if (!ClassHelper.classOf(Set.class, inverseColclass)
									|| !ClassHelper.classOf(Set.class, colPropType.getCollectionClass())) {
								suppressMultipleLinksToSameInstance = true;
							}
						}
					}
					if (!colPropType.isComposition()) {
						colPropTargetTypename = colPropType.getTargetType().getName();
						col = (Collection<Link>) colProp.getValue();
						if (col != null) {
							Collection<Link> newCol = colProp.createNewCollection();
							RapidBean targetBean;
							for (Link link : col) {
								if (link instanceof LinkFrozen) {
									targetBean = this.vistorIdmap.findBean(colPropTargetTypename, link.getIdString());
									if (targetBean == null) {
										throw new ValidationException(
												"invalid.reference", bean,
												"could not resolve reference from bean:\n" + "\""
														+ bean.getType().getName() + "::" + bean.getIdString() + "\"\n"
														+ "property: \"" + prop.getName() + "\"\n" + "to bean \""
														+ colPropTargetTypename + "::" + link.getIdString() + "\"");
									}
								} else {
									targetBean = (RapidBean) link;
								}

								if (!suppressMultipleLinksToSameInstance || !newCol.contains(targetBean)) {
									newCol.add(targetBean);
								}
							}
							try {
								colProp.setValue(newCol);
							} catch (ValidationInstanceAssocTwiceException e) {
								if (!ignoreAssocTwiceException) {
									throw e;
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * recursive traversal of the document tree.
	 * 
	 * @param depth
	 *            the depth in the tree
	 * @param visitor
	 *            carries the bean processing template method
	 * @param bean
	 *            the current tree node
	 */
	@SuppressWarnings("unchecked")
	private void traverseDocumentTree(final int depth, final DocumentTreeVisitor visitor, final RapidBean bean) {
		visitor.processBean(depth, bean);
		PropertyCollection colProp;
		TypePropertyCollection colPropType;
		Collection<RapidBean> col;
		for (Property prop : bean.getPropertyList()) {
			if (prop instanceof PropertyCollection) {
				colProp = (PropertyCollection) prop;
				colPropType = (TypePropertyCollection) prop.getType();
				if (colPropType.isComposition()) {
					col = (Collection<RapidBean>) colProp.getValue();
					if (col != null) {
						for (RapidBean sonBean : col) {
							this.traverseDocumentTree(depth + 1, visitor, sonBean);
						}
					}
				}
			}
		}
	}

	/**
	 * @param configName
	 *            the configName to set
	 */
	public void setConfigName(String configName) {
		this.configName = configName;
	}
}
