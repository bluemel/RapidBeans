/*
 * Rapid Beans Framework: EditorProperty.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/15/2006
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
import java.util.List;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.PropertyString;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.ThreadLocalValidationSettings;
import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.event.PropertyChangeEvent;
import org.rapidbeans.core.event.PropertyChangeListener;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.PropertyType;
import org.rapidbeans.core.type.TypePropertyChoice;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypePropertyQuantity;
import org.rapidbeans.core.type.TypePropertyString;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.datasource.event.AddedEvent;
import org.rapidbeans.datasource.event.ChangedEvent;
import org.rapidbeans.datasource.event.DocumentChangeListener;
import org.rapidbeans.datasource.event.RemovedEvent;
import org.rapidbeans.domain.finance.Currency;
import org.rapidbeans.presentation.config.ConfigEditorBean;
import org.rapidbeans.presentation.config.ConfigPropEditor;
import org.rapidbeans.presentation.config.ConfigPropEditorBean;
import org.rapidbeans.presentation.config.EditorPropNullBehaviour;
import org.rapidbeans.security.User;

/**
 * the bean property editor GUI.
 * 
 * @author Martin Bluemel
 */
public abstract class EditorProperty implements View, DocumentChangeListener,
		PropertyChangeListener {

	/**
	 * The null behavior.
	 */
	private EditorPropNullBehaviour nullBehaviour = EditorPropNullBehaviour.always_empty;

	/**
	 * @return the null behavior
	 */
	public EditorPropNullBehaviour getNullBehaviour() {
		return nullBehaviour;
	}

	/**
	 * @return the property editors label widget
	 */
	public abstract Object getLabelWidget();

	/**
	 * set the focus to the input field's widget.
	 */
	public abstract void setFocus();

	/**
	 * the bean property to edit.
	 */
	private Property property;

	/**
	 * @return the property currently edited
	 */
	public Property getProperty() {
		return this.property;
	}

	/**
	 * setter.
	 * 
	 * @param prop
	 *            the property to set.
	 */
	protected void setProperty(final Property prop) {
		this.property = prop;
	}

	/**
	 * the bean property with it's original values.
	 */
	private Property propertyBak;

	/**
	 * @return the backup property.
	 */
	protected Property getPropertyBak() {
		return this.propertyBak;
	}

	/**
	 * @param bakprop
	 *            the propertyBak to set
	 */
	protected void setPropertyBak(final Property bakprop) {
		this.propertyBak = bakprop;
	}

	/**
	 * the locale.
	 */
	private RapidBeansLocale locale = null;

	/**
	 * @return the locale
	 */
	protected RapidBeansLocale getLocale() {
		return this.locale;
	}

	/**
	 * the editor's UI event lock to avoid input event feedback during updating
	 * the UI accoring to a bean's contents.
	 */
	private int uiEventLock = 0;

	/**
	 * @return the UI event lock
	 */
	public boolean getUIEventLock() {
		return this.uiEventLock > 0;
	}

	/**
	 * increase the UI event lock.
	 */
	public void setUIEventLock() {
		this.uiEventLock++;
	}

	/**
	 * decrease the UI event lock.
	 */
	public void releaseUIEventLock() {
		if (this.uiEventLock > 0) {
			this.uiEventLock--;
		}
	}

	/**
	 * the editor's specific UI update lock to avoid unwanted presentation of
	 * normalized forms before having completed the input (usually used text
	 * editors).
	 */
	private int uiUpdateLock = 0;

	/**
	 * @return the UI update lock
	 */
	public boolean getUIUpdateLock() {
		return this.uiUpdateLock > 0;
	}

	/**
	 * increase the UI update lock.
	 */
	public void setUIUpdateLock() {
		this.uiUpdateLock++;
	}

	/**
	 * decrease the UI update lock.
	 */
	public void releaseUIUpdateLock() {
		if (this.uiUpdateLock > 0) {
			this.uiUpdateLock--;
		}
	}

	/**
	 * the parent bean editor.
	 */
	private EditorBean beanEditor = null;

	/**
	 * @return the parent bean editor
	 */
	public EditorBean getBeanEditor() {
		return this.beanEditor;
	}

	public ConfigPropEditorBean getConfig() {
		ConfigPropEditorBean cfg = null;
		if (this.beanEditor != null) {
			final ConfigEditorBean beanCfg = this.beanEditor.getConfiguration();
			if (beanCfg != null) {
				cfg = beanCfg.getPropertycfg(this.property.getName());
			}
		}
		return cfg;
	}

	/**
	 * the listeners.
	 */
	private List<EditorPropertyListener> listeners = new ArrayList<EditorPropertyListener>();

	/**
	 * @param listener
	 *            the listener to add.
	 */
	public void addPropertyEditorListener(final EditorPropertyListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * @param listener
	 *            the listener to remove.
	 */
	public void removePropertyEditorListener(
			final EditorPropertyListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * fire an input file changed event.
	 */
	public void fireInputFieldChanged() {
		if (this.getUIEventLock()) {
			return;
		}

		// write the value into the bean
		// before performing any further checks
		boolean modifiesBefore = false;
		Object oldValue = null;
		try {
			ThreadLocalEventLock.set(this);
			modifiesBefore = this.beanEditor.getModifies();
			this.beanEditor.setModifies(true);
			oldValue = this.property.getValue();
			if (this.property instanceof PropertyCollection) {
				((PropertyCollection) this.property).setValue(
						this.getInputFieldValue(), true, false);
			} else {
				this.property.setValue(this.getInputFieldValue());
			}
			if (this.property.getType().isKeyCandidate()) {
				this.property.getBean().clearId();
			}
		} catch (ValidationException e) {
			// Do not validate here at that point of time.
			// Simply rewrite the old value.
			try {
				ThreadLocalValidationSettings.validationOff();
				this.property.setValue(oldValue);
			} finally {
				ThreadLocalValidationSettings.remove();
			}
		} finally {
			ThreadLocalEventLock.release();
			this.beanEditor.setModifies(modifiesBefore);
		}

		for (EditorPropertyListener listener : this.listeners) {
			listener.inputFieldChanged(this);
		}
	}

	/**
	 * handler for added bean.
	 * 
	 * @param e
	 *            the removed event
	 */
	public void beanRemoved(final RemovedEvent e) {
	}

	/**
	 * @return the view's title.
	 */
	public String getTitle() {
		return null;
	}

	/**
	 * display a value on the GUI.
	 */
	public abstract void updateUI();

	/**
	 * check if the input field has changed.
	 * 
	 * @return if the input field has a different value than the property.
	 */
	public boolean isInputFieldChanged() {
		boolean equals = true;
		Object newValue = null;
		try {
			final Object val = this.getInputFieldValue();
			newValue = this.getProperty().convertValue(val);
		} catch (ValidationException e) {
			equals = false;
		}
		if (equals) {
			equals = false;
			Object oldValue = this.propertyBak.getValue();
			if (newValue == null && oldValue == null) {
				equals = true;
			} else if (newValue != null && oldValue != null) {
				if (oldValue instanceof ReadonlyListCollection<?>
						&& newValue instanceof Collection<?>) {
					equals = true;
					final Collection<?> newValueCol = (Collection<?>) newValue;
					final ReadonlyListCollection<?> oldValueCol = (ReadonlyListCollection<?>) oldValue;
					if (newValueCol.size() != oldValueCol.size()) {
						equals = false;
					} else {
						for (Object o1 : newValueCol) {
							if (!oldValueCol.contains(o1)) {
								equals = false;
								break;
							} else {
								final Object o2 = oldValueCol.get(oldValueCol
										.indexOf(o1));
								if (o1 == null && o2 == null) {
									// equals stays true, do nothing
								} else if (o1 == null || o2 == null
										|| (!(o1.equals(o2)))) {
									equals = false;
									break;
								}
							}
						}
					}
				} else if (oldValue instanceof RapidBean
						&& newValue instanceof Collection<?>) {
					final Collection<?> newValueCol = (Collection<?>) newValue;
					if (newValueCol.size() != 1) {
						throw new RapidBeansRuntimeException(
								"Can't compare a bean with a"
										+ " Collection with size != 1 (size == "
										+ newValueCol.size() + ")");
					}
					final RapidBean newBean = (RapidBean) newValueCol
							.iterator().next();
					equals = oldValue.equals(newBean);
				} else if (oldValue instanceof Collection<?>
						&& newValue instanceof RapidBean) {
					final Collection<?> oldValueCol = (Collection<?>) oldValue;
					if (oldValueCol.size() != 1) {
						throw new RapidBeansRuntimeException("Can't compare a"
								+ " Collection with size != 1 (size == "
								+ oldValueCol.size() + ") with a bean");
					}
					final RapidBean oldBean = (RapidBean) oldValueCol
							.iterator().next();
					equals = oldBean.equals(newValue);
				} else {
					equals = newValue.equals(oldValue);
				}
			}
		}
		return !equals;
	}

	/**
	 * @return the input field value
	 */
	public abstract Object getInputFieldValue();

	/**
	 * @return the input field value as text.
	 */
	public abstract Object getInputFieldValueString();

	/**
	 * validate an input field.
	 * 
	 * @return the value object if the value is valid
	 */
	public abstract Object validateInputField();

	/**
	 * validate an input field.
	 * 
	 * @return if the string in the input field is valid or at least could at
	 *         least get after appending additional characters.
	 * 
	 * @param ex
	 *            the validation exception
	 */
	protected boolean hasPotentiallyValidInputField(final ValidationException ex) {
		return false;
	}

	/**
	 * create a new editor.
	 * 
	 * @param client
	 *            the client
	 * @param bizBeanEditor
	 *            the parent bean editor
	 * @param prop
	 *            the bean property to edit
	 * @param bakProp
	 *            the bean backup property used instead of a transaction
	 * 
	 * @return the editor object
	 */
	public static EditorProperty createInstance(final Application client,
			final EditorBean bizBeanEditor, final Property prop,
			final Property bakProp) {
		EditorProperty editor = null;
		final PropertyType proptype = prop.getType().getProptype();
		// String proptypename = proptype.name();
		String widgetname = null;
		String classname = null;

		// try to find the property editor class out ouf the UI configuration
		final ConfigEditorBean editorConfig = bizBeanEditor.getConfiguration();

		if (editorConfig != null) {

			final ConfigPropEditorBean propEditorConfig = editorConfig
					.getPropertycfg(prop.getType().getPropName());
			if (propEditorConfig != null) {
				final ConfigPropEditor propEditorConfigEditor = propEditorConfig
						.getEditor();
				if (propEditorConfigEditor != null) {
					// 1st try: editorclass
					if (propEditorConfigEditor.getEditorclass() != null) {
						classname = propEditorConfigEditor.getEditorclass();
					} else if (propEditorConfigEditor.getBasepackage() != null
							&& propEditorConfigEditor.getClassnamepart() != null) {
						classname = propEditorConfigEditor.getBasepackage()
								+ "."
								+ client.getConfiguration().getGuitype().name()
								+ ".EditorProperty"
								+ propEditorConfigEditor.getClassnamepart()
								+ StringHelper
										.upperFirstCharacter(client
												.getConfiguration()
												.getGuitype().name());
					}
				}
			}
		}

		if (classname == null) {
			if ((prop.getBean() instanceof User || TypeRapidBean
					.isSameOrSubtype(TypeRapidBean
							.forName("org.rapidbeans.security.User"), prop
							.getBean().getType()))
					&& prop instanceof PropertyString
					&& prop.getType().getPropName().equals("pwd")) {
				classname = "org.rapidbeans.presentation.swing.EditorPropertyPwd";
			}
		}

		// if a property editor is not configured use the default editor
		if (classname == null) {
			switch (proptype) {
			case bool:
				widgetname = "Checkbox";
				break;
			case choice:
				if (((TypePropertyChoice) prop.getType()).getMultiple()) {
					widgetname = "List";
				} else {
					widgetname = "Combobox";
				}
				break;
			case date:
				widgetname = "Date";
				break;
			case collection:
				final TypePropertyCollection colProptype = (TypePropertyCollection) prop
						.getType();
				if (colProptype.getMaxmult() == 1) {
					widgetname = "Combobox";
				} else {
					widgetname = "List";
				}
				break;
			case quantity:
				final TypePropertyQuantity qProptype = (TypePropertyQuantity) prop
						.getType();
				if ((Currency.dollar).getType() == qProptype.getQuantitytype()
						.getUnitInfo()) {
					widgetname = "Money";
				} else {
					widgetname = "Quantity";
				}
				break;
			case file:
				widgetname = "File";
				break;
			case string:
				final TypePropertyString sProptype = (TypePropertyString) prop
						.getType();
				if (sProptype.getMultiline()) {
					widgetname = "TextArea";
				} else {
					widgetname = "Text";
				}
				break;
			default:
				widgetname = "Text";
				break;
			}
			classname = "org.rapidbeans.presentation."
					+ client.getConfiguration().getGuitype().name()
					+ ".EditorProperty"
					+ widgetname
					+ StringHelper.upperFirstCharacter(client
							.getConfiguration().getGuitype().name());
		}

		// instantiate the property editor
		Class<?> clazz;
		try {
			clazz = Class.forName(classname);
		} catch (ClassNotFoundException e) {
			throw new RapidBeansRuntimeException("class " + classname
					+ " not found", e);
		}
		try {
			Constructor<?> constr = clazz
					.getConstructor(BBEDITORPROP_CONSTRUCTOR_TYPES);
			Object[] oa = { client, bizBeanEditor, prop, bakProp };
			editor = (EditorProperty) constr.newInstance(oa);
		} catch (NoSuchMethodException e) {
			throw new RapidBeansRuntimeException(
					"failed to initialize BBProp of Class \"" + clazz.getName()
							+ "\".\n"
							+ "Constructor (Application, Property) not found.");
		} catch (IllegalAccessException e) {
			throw new RapidBeansRuntimeException(
					"failed to initialize BBProp of Class \""
							+ clazz.getName()
							+ "\".\n"
							+ "IllegalAccessException while calling the constructor.");
		} catch (InstantiationException e) {
			throw new RapidBeansRuntimeException(
					"failed to initialize BBProp of Class \""
							+ clazz.getName()
							+ "\".\n"
							+ "InstatiationException while calling the constructor.");
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			if (t instanceof ExceptionInInitializerError) {
				t = ((ExceptionInInitializerError) t).getException();
			}
			if (t instanceof RuntimeException) {
				throw ((RuntimeException) t);
			}
			throw new RapidBeansRuntimeException(
					"failed to initialize EditorProperty of Class \""
							+ clazz.getName() + "\".\n"
							+ "InvocationTargetException caused by "
							+ t.getClass().getName() + " \"" + t.getMessage()
							+ "\"" + " while calling the constructor.");
		}

		return editor;
	}

	/**
	 * constructor.
	 * 
	 * @param prop
	 *            the bean property to edit
	 * @param bakProp
	 *            the backup property used instead of a transaction
	 * @param beanEditor
	 *            the parent bean editor
	 * @param client
	 *            the client
	 */
	protected EditorProperty(final Application client,
			final EditorBean beanEditor, final Property prop,
			final Property bakProp) {
		this.beanEditor = beanEditor;
		this.property = prop;
		this.propertyBak = bakProp;
		this.locale = client.getCurrentLocale();
		if (this.beanEditor != null) {
			final ConfigEditorBean editorConfig = this.beanEditor
					.getConfiguration();
			if (editorConfig != null) {
				final ConfigPropEditorBean propEditorConfig = editorConfig
						.getPropertycfg(prop.getType().getPropName());
				if (propEditorConfig != null) {
					if (propEditorConfig.getNullbehaviour() != null) {
						this.nullBehaviour = propEditorConfig
								.getNullbehaviour();
					}
				}
			}
		}
		this.getProperty().getBean().addPropertyChangeListener(this);
	}

	/**
	 * the document view's name is the document's name.
	 * 
	 * @return the document view's name
	 */
	public String getName() {
		return "documentview.beaneditor.propertyeditor."
				+ this.property.getBean().getIdString() + "."
				+ this.property.getType().getPropName();
	}

	/**
	 * validate the input field and mark wrong fields.
	 * 
	 * @return the validated value
	 */
	protected Object validateInputFieldInternal() {
		Object value = null;
		try {
			ThreadLocalValidationSettings.readonlyOff();
			value = this.getProperty().validate(this.getInputFieldValue());
		} catch (ValidationException e) {
			throw e;
		} catch (RuntimeException e) {
			Throwable t = e.getCause();
			while (t != null) {
				if (t instanceof ValidationException) {
					throw (ValidationException) t;
				}
				t = t.getCause();
			}
			throw e;
		} finally {
			ThreadLocalValidationSettings.remove();
		}
		return value;
	}

	/**
	 * constant for constructor arguments of class TypeProperty.
	 */
	private static final Class<?>[] BBEDITORPROP_CONSTRUCTOR_TYPES = {
			Application.class, EditorBean.class, Property.class, Property.class };

	/**
	 * close the bean property editor.
	 * 
	 * @return if cancelling is desired
	 */
	public boolean close() {
		getProperty().getBean().removePropertyChangeListener(this);
		return false;
	}

	/**
	 * bean created event.
	 * 
	 * @param e
	 *            added event
	 */
	public void beanAddPre(final AddedEvent e) {
	}

	/**
	 * bean created event.
	 * 
	 * @param e
	 *            added event
	 */
	public void beanAdded(final AddedEvent e) {
	}

	/**
	 * bean deleted event.
	 * 
	 * @param e
	 *            the deleted event
	 */
	public void beanRemovePre(final RemovedEvent e) {
	}

	/**
	 * bean changed event.
	 * 
	 * @param e
	 *            changed event
	 */
	public void beanChangePre(final ChangedEvent e) {
	}

	/**
	 * bean changed event.
	 * 
	 * @param e
	 *            changed event
	 */
	public void beanChanged(final ChangedEvent e) {
		boolean interestedForEvent = false;
		for (PropertyChangeEvent propEv : e.getPropertyEvents()) {
			final Property prop = propEv.getProperty();
			if (prop == this.getProperty()) {
				interestedForEvent = true;
				break;
			}
		}
		if (!interestedForEvent) {
			return;
		}
		this.updateUI();
	}

	@Override
	public void propertyChangePre(PropertyChangeEvent e) {
	}

	@Override
	public void propertyChanged(PropertyChangeEvent e) {
		if (getUIEventLock()) {
			return;
		}
		if (e.getProperty() == this.getProperty()) {
			this.updateUI();
		}
	}

	/**
	 * document save event.
	 */
	public void documentSaved() {
	}
}
