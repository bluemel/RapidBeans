/*
 * Rapid Beans Framework: PropertyCollection.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/27/2005
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

package org.rapidbeans.core.basic;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.event.PropertyChangeEvent;
import org.rapidbeans.core.event.PropertyChangeEventType;
import org.rapidbeans.core.event.PropertyChangeListener;
import org.rapidbeans.core.exception.BeanDuplicateException;
import org.rapidbeans.core.exception.BeanNotFoundException;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.UnresolvedLinkException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.exception.ValidationInstanceAssocTwiceException;
import org.rapidbeans.core.exception.ValidationMandatoryException;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;

/**
 * A <b>Collection</b> bean property encapsulates a set of bean links belonging
 * to a certain associaton.
 * 
 * @author Martin Bluemel
 */
public class PropertyCollection extends PropertyAssociationend implements PropertyChangeListener {

	/**
	 * the collection of beans. !!! do not initialize here as this would overwrite
	 * the superclasses default constructor's behaviour.
	 */
	private Collection<Link> value;

	/**
	 * generic value getter. since collections are n o t immutable and we don't want
	 * to clone a collection that could be very large we just give out the iterator.
	 * 
	 * @return a collection of beans
	 */
	@SuppressWarnings("unchecked")
	public Collection<?> getValue() {
		Collection<Link> val = null;
		if (getBean() instanceof RapidBeanImplSimple) {
			// use getValueField here because the collection will be wrapped later on anyway
			final Object valobj = Property.getValueFieldByReflection(getBean(), getName());
			if (valobj != null) {
				if (valobj instanceof Link) {
					val = Arrays.asList(new Link[] { (Link) valobj });
				} else if (valobj instanceof Collection) {
					val = (Collection<Link>) valobj;
				} else {
					throw new RapidBeansRuntimeException(
							String.format("Unexpected value class \"%s\"", valobj.getClass().getName()));
				}
			}
		} else {
			val = this.value;
		}
		if (val == null) {
			return null;
		} else {
			return new ReadonlyListCollection<Link>(val, (TypePropertyCollection) this.getType());
		}
	}

	/**
	 * generic value setter.
	 * 
	 * @param newValue the new value for this property
	 */
	public void setValue(final Object newValue) {
		this.setValue(newValue, true, true, true);
	}

	/**
	 * generic value setter absolute experts with fine tuning possibilities.
	 * 
	 * @param newValue                             the new value for this property
	 * @param touchInverseLinks                    if an inverse link will be added
	 *                                             or not
	 * @param checkContainerLinksToExternalObjects determines if links from an
	 *                                             object living inside the
	 *                                             container should be allowed. Be
	 *                                             very careful to set this argument
	 *                                             to false.
	 */
	public void setValue(final Object newValue, final boolean touchInverseLinks,
			final boolean checkContainerLinksToExternalObjects) {
		this.setValue(newValue, touchInverseLinks, checkContainerLinksToExternalObjects, true);
	}

	/**
	 * generic value setter absolute experts with fine tuning possibilities.
	 * 
	 * @param col                                  the new value for this property
	 * @param touchInverseLinks                    if an inverse link will be added
	 *                                             or not
	 * @param checkContainerLinksToExternalObjects determines if links from an
	 *                                             object living inside the
	 *                                             container should be allowed. Be
	 *                                             very careful to set this argument
	 *                                             to false.
	 * @param validate                             turn validation off / on
	 */
	@SuppressWarnings("unchecked")
	public void setValue(final Object col, final boolean touchInverseLinks,
			final boolean checkContainerLinksToExternalObjects, final boolean validate) {

		Collection<Link> newCol = null;
		if (validate) {
			newCol = validate(col, ValidationMode.set);
		} else {
			newCol = convertValue(col);
		}

		final TypePropertyCollection proptype = (TypePropertyCollection) this.getType();
		final TypeProperty[] propsbefore = BeanSorter.get();
		prepareSorting(proptype);
		try {
			// remove all inverse links existing so far
			if (getValue() != null) {
				for (final Link curLink : (Collection<Link>) getValue()) {
					if (touchInverseLinks && curLink instanceof RapidBean) {
						removeInverseLink((RapidBean) curLink, true);
					}
				}
			}

			// Especially for x to 1 associations:
			// remove also inverse links pointing to the object
			// that is going to be linked
			if (touchInverseLinks && newCol != null && newCol.size() > 0 && proptype.getInverse() != null) {
				final TypePropertyCollection inverseProptype = (TypePropertyCollection) proptype.getTargetType()
						.getPropertyType(proptype.getInverse());
				if (inverseProptype == null) {
					throw new RapidBeansRuntimeException("could not find inverse property type: \""
							+ proptype.getTargetType().getName() + "\", property: \"" + proptype.getInverse() + "\"");
				}
				if (inverseProptype.getMaxmult() != TypePropertyCollection.INFINITE) {
					for (final Link newLink : newCol) {
						if (newLink instanceof RapidBean) {
							final RapidBean beanToLink = (RapidBean) newLink;
							final Collection<?> alreadyLinkedBeans = (Collection<?>) beanToLink
									.getPropValue(inverseProptype.getPropName());
							if (alreadyLinkedBeans != null
									&& alreadyLinkedBeans.size() >= inverseProptype.getMaxmult()) {
								// if
								// (alreadyLinkedBeans.contains(this.getBean()))
								// {
								// final PropertyCollection inverseColProp =
								// (PropertyCollection)
								// beanToLink.getProperty(inverseProptype.getPropName());
								// inverseColProp.removeLink(this.getBean());
								// } else {
								// throw new ValidationException("xxx", "yyy");
								// }
								switch (alreadyLinkedBeans.size()) {
								case 0: // do nothing
									break;
								case 1:
									final PropertyCollection colProp = (PropertyCollection) beanToLink
											.getProperty(inverseProptype.getPropName());
									colProp.setValue(new ArrayList<RapidBean>(), true, true, false);
									break;
								default:
									throw new RapidBeansRuntimeException("did not expect bean \"" + beanToLink.getType()
											+ "::" + beanToLink.getIdString()
											+ "\" to be already linked with more than one bean (" + "property \""
											+ inverseProptype.getPropName() + "\").");
								}
							}
						}
					}
				}
			}

			final Collection<Link> oldCol = (Collection<Link>) getValue();
			if ((oldCol == null && newCol != null) || (oldCol != null && newCol == null)
					|| ((oldCol != null && newCol != null) && (!oldCol.equals(newCol)))) {
				fireChangePre(this, PropertyChangeEventType.set, oldCol, newCol, null);
			}
			if (getBean() instanceof RapidBeanImplSimple) {
				if (proptype.getMaxmult() == 1) {
					if (newCol != null && newCol.iterator() != null) {
						Property.setValueByReflection(getBean(), getName(), newCol.iterator().next());
					} else {
						Property.setValueByReflection(getBean(), getName(), null);
					}
				} else {
					Property.setValueByReflection(getBean(), getName(), newCol);
				}
			} else {
				this.value = newCol;
			}
			if ((oldCol == null && newCol != null) || (oldCol != null && newCol == null)
					|| ((oldCol != null && newCol != null) && (!oldCol.equals(newCol)))) {
				fireChanged(this, PropertyChangeEventType.set, oldCol, newCol, null);
			}

			if (getValue() != null) {
				for (final Link curLink : (Collection<Link>) getValue()) {
					if (touchInverseLinks && curLink instanceof RapidBean) {
						addInverseLink((RapidBean) curLink, true, checkContainerLinksToExternalObjects, true);
					}
				}
			}

			removePropertyChangeListeners(oldCol);
		} finally {
			cleanupSorting(proptype, propsbefore);
		}
	}

	/**
	 * add a bean reference to the Collection Property.
	 * 
	 * @param link the bean to add
	 */
	public void addLink(final Link link) {
		this.addLink(link, true, true, true);
	}

	/**
	 * add a bean reference to the Collection Property.
	 * 
	 * @param link                                 the bean to add
	 * @param addInverse                           if an inverse link should be
	 *                                             added
	 * @param checkContainerLinksToExternalObjects determines if links from an
	 *                                             object living inside the
	 *                                             container should be allowed. Be
	 *                                             very careful to set this argument
	 *                                             to false.
	 * @param checkContainerAlreadyContains        determines if the bean already is
	 *                                             contained by the container
	 */
	@SuppressWarnings("unchecked")
	public void addLink(final Link link, final boolean addInverse, final boolean checkContainerLinksToExternalObjects,
			final boolean checkContainerAlreadyContains) {

		// Validation for add link checks null on mandatory, target type, ...
		// The check on the multiplicity with one single instance always
		// succeeds
		// but will be done separately for addLink afterwards.
		validate(link, ValidationMode.add);
		ContainerImpl doc = null;
		boolean parentSet = false;
		RapidBean prevParent = null;
		final TypePropertyCollection proptype = (TypePropertyCollection) this.getType();
		final TypeProperty[] propsbefore = BeanSorter.get();
		prepareSorting(proptype);
		try {
			if (link instanceof RapidBean && proptype.isComposition()) {
				doc = (ContainerImpl) this.getBean().getContainer();
				if (doc != null && checkContainerAlreadyContains) {
					final TypeRapidBean linkType = proptype.getTargetType();
					if ((linkType.getIdtype() == IdType.keyprops
							|| linkType.getIdtype() == IdType.keypropswithparentscope) && link instanceof RapidBean
							&& ((RapidBean) link).getParentBean() != this.getBean()) {
						prevParent = ((RapidBean) link).getParentBean();
						((RapidBean) link).setParentBean(this.getBean());
						parentSet = true;
					}
					if (this.getBean().getContainer().contains((RapidBean) link)) {
						if (parentSet) {
							((RapidBean) link).setParentBean(prevParent);
							parentSet = false;
						}
						final Application app = ApplicationManager.getApplication();
						String message;
						RapidBeansLocale locale = null;
						if (app != null) {
							locale = app.getCurrentLocale();
						}
						if (locale != null) {
							final String locGuiId = ((RapidBean) link).toStringGuiId(locale);
							message = "Bean \"" + locGuiId + "\" already exists in document \""
									+ getBean().getContainer().getName() + "\"";
							throw new BeanDuplicateException("messagedialog.create.duplicate", this.getBean(), message,
									new Object[] { locGuiId });
						} else {
							final String sid = ((RapidBean) link).getIdString();
							message = "Bean \"" + sid + "\" already exists in document \""
									+ getBean().getContainer().getName() + "\"";
							throw new BeanDuplicateException("messagedialog.create.duplicate", this.getBean(), message,
									new Object[] { sid });
						}
					}
				}
			}
			if (getValue() == null) {
				if (getBean() instanceof RapidBeanImplSimple) {
					if (proptype.getMaxmult() != 1) {
						Property.setValueByReflection(getBean(), getName(), createNewCollection());
					}
				} else {
					this.value = createNewCollection();
				}
			} else {
				final int maxmult = proptype.getMaxmult();
				if (maxmult != TypePropertyCollection.INFINITE && getValue().size() > maxmult - 1) {
					// special handling for inverse frozen links
					// do not throw an exception if value contains a frozen
					// link that equals the id string of the new link which
					// is a real bean.
					final Object[] oa = { ((RapidBean) link).getType().getName() + ": " + ((RapidBean) link).toString(),
							proptype.getMaxmult() };
					if (ThreadLocalValidationSettings.getValidation()) {
						if (!(link instanceof RapidBean) || (!containsFrozenLinkWithIdstring(link.getIdString()))) {
							throw new ValidationException("invalid.prop.collection.add.maxmult", link,
									"Bean \"" + this.getBean().getType().getName() + "::" + this.getBean().getIdString()
											+ "\":" + "Collection property \"" + this.getType().getPropName() + "\": "
											+ " failed to add bean " + link.getIdString() + ".\nMaximal multiplicity "
											+ proptype.getMaxmult() + " exceeded.",
									oa);
						}
					}
				}
			}
			fireChangePre(this, PropertyChangeEventType.addlink, null, null, link);
			boolean addSuccess = false;
			if (getBean() instanceof RapidBeanImplSimple) {
				Object valobj = Property.getValueFieldByReflection(getBean(), getName());
				if (valobj == null) {
					valobj = createNewCollection();
					Property.setValueByReflection(getBean(), getName(), createNewCollection());
				} else if (valobj instanceof Link) {
					throw new AssertionError("no collection value");
				}
				addSuccess = ((Collection<Link>) valobj).add(link);
			} else {
				addSuccess = this.value.add(link);
			}
			if (!addSuccess) {
				throw new ValidationInstanceAssocTwiceException("invalid.prop.collection.add.already", this, link,
						"Collection property \"" + this.getType().getPropName() + "\": " + " failed to add bean "
								+ link.getIdString()
								+ ".\nBean already is in this collection which does not permit duplicates.",
						new String[] { this.getType().getPropName(), link.getIdString() });
			}
			if (addInverse && link instanceof RapidBean) {
				try {
					addInverseLink((RapidBean) link, addInverse, checkContainerLinksToExternalObjects, false);
					if ((link instanceof RapidBean) && proptype.isComposition()) {
						if (doc != null) {
							insertComponentsIntoContainer((RapidBean) link, doc);
							resolveFrozenLinksInComponents((RapidBean) link, doc);
						}
					}
				} catch (BeanDuplicateException e) {
					if (getBean() instanceof RapidBeanImplSimple) {
						// use getValueField here because the collection must be modifiable
						final Object valobj = Property.getValueFieldByReflection(getBean(), getName());
						if (valobj == null) {
							throw new AssertionError("null value");
						}
						if (valobj instanceof Link) {
							throw new AssertionError("no collection value");
						}
						((Collection<Link>) valobj).remove(link);
					} else {
						this.value.remove(link);
					}
					throw e;
				}
			}
			fireChanged(this, PropertyChangeEventType.addlink, null, null, link);
			if (link instanceof RapidBean && (proptype.getSorting() == SortingType.byPropertyValues
					|| proptype.getSorting() == SortingType.byPropertyValuesAll)) {
				((RapidBean) link).addPropertyChangeListener(this);
			}
		} finally {
			cleanupSorting(proptype, propsbefore);
		}
	}

	/**
	 * adds the inverse link.
	 * 
	 * @param linkedBean                           the bean linked
	 * @param addInverse                           the switch to prevent from
	 *                                             endless recursion
	 * @param checkContainerLinksToExternalObjects determines if links from an
	 *                                             object living inside the
	 *                                             container should be allowed. Be
	 *                                             very careful to set this argument
	 *                                             to false.
	 * @param setValue                             true if called from setValue
	 *                                             false if called from addValue
	 */
	private void addInverseLink(final RapidBean linkedBean, final boolean addInverse,
			final boolean checkContainerLinksToExternalObjects, final boolean setValue) {
		TypePropertyCollection colType = (TypePropertyCollection) this.getType();
		RapidBean bean = this.getBean();
		ContainerImpl container = null;
		if (bean != null) {
			container = (ContainerImpl) bean.getContainer();
		}
		Container linkedContainer = linkedBean.getContainer();
		if (colType.isComposition()) {
			if (linkedBean.getParentBean() != null && linkedBean.getParentBean() != this.getBean()) {
				throw new RapidBeansRuntimeException("tried to add a bean as component"
						+ " that already is component of another bean (has a parent)");
			}
			linkedBean.setParentBean(this.getBean());
			if (container == null) {
				if (linkedContainer != null) {
					throw new RapidBeansRuntimeException(
							"tried to compose a bean" + " living outside a container" + " with one inside a container");
				}
			} else {
				if (linkedContainer == null) {
					// insert a component implicitly into the container
					container.insert(linkedBean, true);
					linkedBean.setContainer(container);
				} else if (container != linkedContainer) {
					throw new RapidBeansRuntimeException("tried to compose a bean living outside a container"
							+ " with one inside a different container");
				}
			}
		} else if (colType.getInverse() != null && addInverse) {
			PropertyCollection inverseColProp = (PropertyCollection) linkedBean.getProperty(colType.getInverse());
			if (inverseColProp != null) {
				if (setValue) {
					final TypePropertyCollection inverseColPropType = (TypePropertyCollection) inverseColProp.getType();
					if (inverseColPropType.getMaxmult() == 1) {
						inverseColProp.setValue(this.getBean(), false, false);
					} else {
						inverseColProp.addLink(this.getBean(), false, checkContainerLinksToExternalObjects, true);
					}
				} else {
					inverseColProp.addLink(this.getBean(), false, checkContainerLinksToExternalObjects, true);
				}
				if (checkContainerLinksToExternalObjects) {
					if (container == null) {
						if (linkedContainer != null) {
							throw new RapidBeansRuntimeException("tried to associate"
									+ " a bean living outside a container" + " with one inside a container");
						}
					} else {
						if (linkedContainer == null) {
							throw new RapidBeansRuntimeException("tried to associate a"
									+ " bean living inside a container" + " with one outside a container");
						} else if (container != linkedContainer) {
							throw new RapidBeansRuntimeException("tried to associate a"
									+ " bean living outside a container" + " with one inside a different container");
						}
					}
				}
			}
		}
	}

	/**
	 * remove a bean reference from the Collection Property.
	 * 
	 * @param link the bean reference to remove
	 */
	public void removeLink(final Link link) {
		this.removeLink(link, true, true, true);
	}

	/**
	 * remove a bean reference from the Collection Property.
	 * 
	 * @param link                    the bean reference to remove
	 * @param removeInverse           if the inverse link has to be removed
	 * @param checkNotFound           throw an exception if the link to remove is
	 *                                not in this property
	 * @param deleteOrpahnedComponent unbind orphaned component from all other
	 *                                links.
	 */
	@SuppressWarnings("unchecked")
	public void removeLink(final Link link, final boolean removeInverse, final boolean checkNotFound,
			final boolean deleteOrpahnedComponent) {

		if (!removeInverse && (getValue() == null || getValue().size() == 0)) {
			return;
		}

		final TypePropertyCollection proptype = (TypePropertyCollection) this.getType();
		final TypeProperty[] propsbefore = BeanSorter.get();
		prepareSorting(proptype);

		try {
			validate(link, ValidationMode.remove);
			if (ThreadLocalValidationSettings.getValidation()) {
				if (getValue() != null && getValue().size() <= proptype.getMinmult()
						&& (this.getBean().getBeanState() != RapidBeanState.deleting)) {
					throw new ValidationException("invalid.prop.collection.remove.minmult", this.getBean(),
							"Collection property \"" + this.getType().getPropName() + "\": " + " failed to remove bean "
									+ link.getIdString() + ".\nMinimal multiplicity " + proptype.getMinmult()
									+ " undergone.");
				}
			}
			fireChangePre(this, PropertyChangeEventType.removelink, null, null, link);
			if (getBean() instanceof RapidBeanImplSimple) {
				if (getValue() != null) {
					// use getValueField here because the collection must be modifiable
					final Object valobj = Property.getValueFieldByReflection(getBean(), getName());
					if (valobj == null) {
						throw new AssertionError("null value");
					}
					if (valobj instanceof Link) {
						throw new AssertionError("no collection value");
					}
					final boolean removeSuccess = ((Collection<Link>) valobj).remove(link);
					if (!removeSuccess) {
						throw new BeanNotFoundException("Collection property \"" + this.getType().getPropName() + "\": "
								+ " failed to remove bean " + link.getIdString() + ".\nBean is not in this collection."
								+ "Collction class: " + getValue().getClass().getName());
					}
				}
			} else {
				if (this.value != null && !this.value.remove(link)) {
					if (checkNotFound) {
						throw new BeanNotFoundException("Collection property \"" + this.getType().getPropName() + "\": "
								+ " failed to remove bean " + link.getIdString() + ".\nBean is not in this collection."
								+ "Collction class: " + getValue().getClass().getName());
					}
				}
			}
			if (removeInverse && link instanceof RapidBean) {
				try {
					removeInverseLink((RapidBean) link, removeInverse);
				} catch (BeanNotFoundException e) {
					return;
				}
			}
			if (deleteOrpahnedComponent && (link instanceof RapidBean) && proptype.isComposition()) {
				final RapidBean bean = (RapidBean) link;
				final ContainerImpl doc = (ContainerImpl) this.getBean().getContainer();
				if (doc != null) {
					bean.delete();
				}
			}
			if (link instanceof RapidBean && (proptype.getSorting() == SortingType.byPropertyValues
					|| proptype.getSorting() == SortingType.byPropertyValuesAll)) {
				((RapidBean) link).removePropertyChangeListener(this);
			}
			fireChanged(this, PropertyChangeEventType.removelink, null, null, link);
		} finally {
			cleanupSorting(proptype, propsbefore);
		}
	}

	/**
	 * adds the inverse link.
	 * 
	 * @param linkedBean    the bean linked
	 * @param removeInverse the switch to prevent from endless recursion
	 */
	private void removeInverseLink(final RapidBean linkedBean, final boolean removeInverse) {
		final TypePropertyCollection colType = (TypePropertyCollection) this.getType();
		if (colType.isComposition()) {
			linkedBean.setParentBean(null);
			if (linkedBean.getContainer() != null) {
				linkedBean.getContainer().delete(linkedBean);
				linkedBean.setContainer(null);
			}
		} else if (colType.getInverse() != null && removeInverse) {
			PropertyCollection inverseColProp = (PropertyCollection) linkedBean.getProperty(colType.getInverse());
			if (inverseColProp != null) {
				try {
					ThreadLocalValidationSettings.validationOff();
					inverseColProp.removeLink(this.getBean(), false, false, false);
				} finally {
					ThreadLocalValidationSettings.remove();
				}
			}
		}
	}

	/**
	 * add the document to the bean and all child beans.
	 * 
	 * @param bean the bean
	 * @param doc  the container
	 */
	private void insertComponentsIntoContainer(final RapidBean bean, final ContainerImpl doc) {
		if (bean.getContainer() == null) {
			doc.insert(bean, true);
		} else if (bean.getContainer() != doc) {
			throw new RapidBeansRuntimeException("wrong container");
		}
		for (PropertyCollection compColProp : bean.getColPropertiesComposition()) {
			if (compColProp.value != null) {
				for (Link childBeanLink : compColProp.value) {
					if (childBeanLink instanceof RapidBean) {
						insertComponentsIntoContainer((RapidBean) childBeanLink, doc);
					}
				}
			}
		}
	}

	/**
	 * add the document to the bean and all childs beans.
	 * 
	 * @param bean the bean
	 * @param doc  the container
	 */
	private void resolveFrozenLinksInComponents(final RapidBean bean, final Container doc) {
		final List<PropertyCollection> colProps = bean.getColProperties();
		final int size = colProps.size();
		for (int i = 0; i < size; i++) {
			final PropertyCollection colProp = colProps.get(i);
			final TypePropertyCollection colPropType = (TypePropertyCollection) colProp.getType();
			if (colProp.value != null) {
				if (colPropType.isComposition()) {
					for (Link childBeanLink : colProp.value) {
						if (childBeanLink instanceof RapidBean) {
							resolveFrozenLinksInComponents((RapidBean) childBeanLink, doc);
						}
					}
				} else {
					boolean resolvedAnyFrozen = false;
					final Collection<Link> col = colProp.createNewCollection();
					for (final Link childBeanLink : colProp.value) {
						if (childBeanLink instanceof LinkFrozen) {
							final RapidBean linkedBean = doc.findBean(colPropType.getTargetType().getName(),
									childBeanLink.getIdString());
							if (linkedBean == null) {
								throw new UnresolvedLinkException("from " + getBean().getType().getName() + "::"
										+ getBean().toString() + " to " + colPropType.getTargetType().getName() + "::"
										+ childBeanLink.getIdString());
							}
							col.add(linkedBean);
							resolvedAnyFrozen = true;
						} else {
							col.add(childBeanLink);
						}
					}
					if (resolvedAnyFrozen) {
						try {
							ThreadLocalValidationSettings.validationOff();
							colProp.setValue(col);
						} finally {
							ThreadLocalValidationSettings.remove();
						}
					}
				}
			}
		}
	}

	/**
	 * constructor for a new Collection Property.
	 * 
	 * @param type       the Property's type
	 * @param parentBean the parent bean
	 */
	public PropertyCollection(final TypeProperty type, final RapidBean parentBean) {
		super(type, parentBean);
	}

	/**
	 * initialize default value.
	 * 
	 * @param propType   the Property type
	 * @param parentBean the parent bean
	 */
	protected void initDefaultValue(final TypeProperty propType, final RapidBean parentBean) {
		try {
			this.setValue(propType.getDefaultValue());
		} catch (ValidationMandatoryException e) {
			if (getBean() instanceof RapidBeanImplSimple) {
				if (((TypePropertyCollection) propType).getMaxmult() != 1) {
					Property.setValueByReflection(getBean(), getName(), createNewCollection());
				}
			} else {
				this.value = createNewCollection();
			}
		}
	}

	/**
	 * implementation of toString().
	 * 
	 * @return the String representation of this collection
	 */
	@SuppressWarnings("unchecked")
	public String toString() {
		final Collection<?> value = getValue();
		if (value == null) {
			return null;
		}
		final char sep = ((TypePropertyCollection) this.getType()).getCharSeparator();
		final char esc = ((TypePropertyCollection) this.getType()).getCharEscape();
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (final Link link : (Collection<Link>) value) {
			if (i > 0) {
				sb.append(sep);
			}
			sb.append(StringHelper.escape(link.getIdString(), esc, sep));
			i++;
		}
		return sb.toString();
	}

	/**
	 * validation for Collection Properties. - delegate validation to all
	 * components.
	 * 
	 * @param newValue the new value to validate
	 * 
	 * @return the converted value which is the internal representation or if a
	 *         primitive type the corresponding value object
	 */
	@SuppressWarnings("unchecked")
	public Object validate(final Object newValue) {
		final Collection<Link> ret = validate(newValue, ValidationMode.set);
		if (((TypePropertyCollection) this.getType()).isComposition() && getValue() != null) {
			for (final Link link : (Collection<Link>) getValue()) {
				if (link instanceof RapidBean) {
					((RapidBean) link).validate();
				}
			}
		}
		return ret;
	}

	/**
	 * validation for Collection Properties.
	 * 
	 * @param newValue the new value
	 * @param mode     ( add | remove | set )
	 * 
	 * @return the converted value which is the internal representation or if a
	 *         primitive type the corresponding value object
	 */
	@SuppressWarnings("unchecked")
	public Collection<Link> validate(final Object newValue, final ValidationMode mode) {

		// general validation includes conversion
		final Collection<Link> collection = (Collection<Link>) super.validate(newValue);

		if (!ThreadLocalValidationSettings.getValidation()) {
			return collection;
		}
		if (collection == null) {
			return null;
		}

		final TypePropertyCollection type = (TypePropertyCollection) this.getType();
		final int size = collection.size();

		// check new collection size against maximal multiplicity
		final int maxmult = type.getMaxmult();
		if (maxmult != TypePropertyCollection.INFINITE && size > maxmult) {
			final Object[] oa = { this.getBean().getType().getName() + "::" + this.getBean().getIdString(), maxmult };
			throw new ValidationException("invalid.prop.collection.val.maxmult", this.getBean(),
					"Bean \"" + this.getBean().getType().getName() + "::" + this.getBean().getIdString() + "\""
							+ ".\nMaximal multiplicity " + maxmult + " exceeded.",
					oa);
		}

		// check new collection size against maximal multiplicity
		final int minmult = type.getMinmult();
		if (size < minmult) {
			final Object[] oa = { this.getBean().getType().getName() + "::" + this.getBean().getIdString(), minmult };
			throw new ValidationException("invalid.prop.collection.val.minmult", this.getBean(),
					"Bean \"" + this.getBean().getType().getName() + "::" + this.getBean().getIdString() + "\""
							+ ".\nMinimal multiplicity " + maxmult + " undergone.",
					oa);
		}

		// check target types of links of new collection
		for (Link link : collection) {
			if (link instanceof RapidBean) {
				final RapidBean bean = (RapidBean) link;
				if (!TypeRapidBean.isSameOrSubtype(type.getTargetType(), bean.getType())) {
					throw new ValidationException("invalid.prop.collection.targettype", this.getBean(),
							"Collection property \"" + this.getType().getPropName() + "\": " + " invalid bean type \""
									+ bean.getType().getName() + "\".\nType \"" + type.getTargetType().getName()
									+ "\" is required.");
				}
			}
		}

		return collection;
	}

	/**
	 * converts a variety of types to Collection.
	 * 
	 * @param collectionValue the value to convert
	 * 
	 * @return a List of beans
	 */
	@SuppressWarnings("unchecked")
	public Collection<Link> convertValue(final Object collectionValue) {

		if (collectionValue == null) {
			return null;
		}

		final Collection<Link> collection = createNewCollection();
		if (collectionValue instanceof Collection) {
			final Collection<Link> argValList = (Collection<Link>) collectionValue;
			for (Object obj : argValList) {
				checkBean(collection, obj);
			}
		} else if (collectionValue instanceof Object[]) {
			final Object[] argValList = (Object[]) collectionValue;
			for (Object obj : argValList) {
				checkBean(collection, obj);
			}
		} else if (collectionValue instanceof RapidBean) {
			collection.add((RapidBean) collectionValue);
		} else if (collectionValue instanceof LinkFrozen) {
			collection.add((LinkFrozen) collectionValue);
		} else if (collectionValue instanceof String) {
			for (String frozenLinkDescription : StringHelper.splitEscaped((String) collectionValue,
					((TypePropertyCollection) this.getType()).getCharSeparator(),
					((TypePropertyCollection) this.getType()).getCharEscape())) {
				collection.add(new LinkFrozen(frozenLinkDescription));
			}
		} else if (collectionValue instanceof String[]) {
			String[] sa = (String[]) collectionValue;
			for (int i = 0; i < sa.length; i++) {
				collection.add(new LinkFrozen(sa[i]));
			}
		} else {
			throw new ValidationException("invalid.prop.collection.type", this,
					"Collection property \"" + this.getType().getPropName() + "\": " + " invalid data type "
							+ collectionValue.getClass().getName()
							+ ".\nOnly \"bean\", \"Collection<RapidBean>\", \"RapidBean[]\""
							+ " \"String[]\", and \"String\" are valid types.");
		}
		return collection;
	}

	/**
	 * Check a single bean.
	 * 
	 * @param collection add the instance to this collection if it is ok.
	 * @param obj        the instance to check
	 */
	private void checkBean(final Collection<Link> collection, Object obj) {
		if (obj == null) {
			throw new ValidationException("invalid.prop.collection.contentnull", this.getBean(), "invalid null object");
		} else if (obj instanceof RapidBean || obj instanceof LinkFrozen) {
			collection.add((Link) obj);
		} else if (obj instanceof String) {
			collection.add(new LinkFrozen((String) obj));
		} else {
			throw new ValidationException("invalid.prop.collection.contenttype", this.getBean(),
					"Collection property \"" + this.getType().getPropName() + "\": " + " invalid content data type "
							+ obj.getClass().getName()
							+ ".\nOnly \"bean\", \"LinkFrozen\", and \"String\" are valid types.");
		}
	}

	/**
	 * just an empty object array.
	 */
	private static final Object[] COLLECTION_CONSTR_ARGS = new Object[0];

	/**
	 * internal creation of new collections.
	 * 
	 * @return the collection
	 */
	@SuppressWarnings("unchecked")
	public Collection<Link> createNewCollection() {
		final Class<?> collectionClass = ((TypePropertyCollection) this.getType()).getCollectionClass();

		// construct standard collection
		if (collectionClass == LinkedHashSet.class) {
			return new LinkedHashSet<Link>();
		} else if (collectionClass == HashSet.class) {
			return new HashSet<Link>();
		} else if (collectionClass == TreeSet.class) {
			return new TreeSet<Link>();
		} else if (collectionClass == ArrayList.class) {
			return new ArrayList<Link>();
		} else if (collectionClass == LinkedList.class) {
			return new LinkedList<Link>();
		} else if (collectionClass == Vector.class) {
			return new Vector<Link>();
		}

		// use reflective creation for special collection classes
		Collection<Link> newCol = null;
		try {
			newCol = (Collection<Link>) ((TypePropertyCollection) this.getType()).getCollectionClassConstructor()
					.newInstance(COLLECTION_CONSTR_ARGS);
		} catch (IllegalAccessException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		} catch (InstantiationException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		}
		return newCol;
	}

	/**
	 * Helper function.
	 * 
	 * @param idstr the id string to search for
	 * 
	 * @return if the collection already contains a link with the given idstring.
	 */
	@SuppressWarnings("unchecked")
	private boolean containsFrozenLinkWithIdstring(final String idstr) {
		boolean contains = false;
		for (Link link : (Collection<Link>) getValue()) {
			if (link instanceof LinkFrozen && link.getIdString().equals(idstr)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	/**
	 * The before property change event handle method to implement by every
	 * listener.
	 * 
	 * @param e the property change event
	 */
	public void propertyChangePre(final PropertyChangeEvent e) {
		// do nothing
	}

	/**
	 * The after property change event handle method to implement by every listener.
	 * 
	 * @param e the property change event
	 */
	public void propertyChanged(final PropertyChangeEvent e) {
		final TypePropertyCollection proptype = (TypePropertyCollection) this.getType();
		if (proptype.getSorting() == SortingType.byPropertyValues
				|| proptype.getSorting() == SortingType.byPropertyValuesAll) {
			sort();
		}
	}

	@SuppressWarnings("unchecked")
	public void sort() {
		final TypePropertyCollection proptype = (TypePropertyCollection) this.getType();
		if (proptype.getSorting() == null) {
			return;
		}
		final TypeProperty[] propsbefore = BeanSorter.get();
		prepareSorting(proptype);
		try {
			final Collection<Link> newCol = createNewCollection();
			if (isSorted(newCol)) {
				for (final Link link : (Collection<Link>) getValue()) {
					newCol.add(link);
				}
			} else {
				final Link[] links = ((Collection<Link>) getValue()).toArray(new Link[0]);
				Arrays.sort(links);
				for (final Link link : links) {
					newCol.add(link);
				}
			}
			final Collection<Link> oldCol = (Collection<Link>) getValue();
			final boolean changed = !collectionsContainSameLinks(oldCol, newCol);
			if (changed) {
				fireChangePre(this, PropertyChangeEventType.set, oldCol, newCol, null);
			}
			if (getBean() instanceof RapidBeanImplSimple) {
				if (proptype.getMaxmult() == 1) {
					Property.setValueByReflection(getBean(), getName(), newCol.iterator().next());
				} else {
					Property.setValueByReflection(getBean(), getName(), newCol);
				}
			} else {
				this.value = newCol;
			}
			if (changed) {
				fireChanged(this, PropertyChangeEventType.set, oldCol, newCol, null);
			}
		} finally {
			cleanupSorting(proptype, propsbefore);
		}
	}

	/**
	 * Compare two Link collections for containing the same links.
	 * 
	 * @param col1 the first collection of Links
	 * @param col2 the second collection of Links
	 * @return true if both collections contain exactly the same Links in the same
	 *         order or if both collections are null<br/>
	 *         false otherwise
	 */
	public static boolean collectionsContainSameLinks(final Collection<Link> col1, final Collection<Link> col2) {
		boolean colsEqual = true;
		if (col1 == null && col2 == null) {
			colsEqual = true;
		} else if (col1 != null && col2 == null || col1 == null && col2 != null) {
			colsEqual = false;
		} else if (col1.size() != col2.size()) {
			colsEqual = false;
		} else {
			Iterator<Link> it = col2.iterator();
			for (final Link link : col1) {
				final Link oldLink = it.next();
				if (!link.equals(oldLink)) {
					colsEqual = false;
					break;
				}
			}
		}
		return colsEqual;
	}

	/**
	 * prepare sorting.
	 * 
	 * @param proptype the association end property's type
	 */
	public static void prepareSorting(final TypeProperty proptype) {
		if (!(proptype instanceof TypePropertyCollection)) {
			return;
		}
		final TypePropertyCollection cproptype = (TypePropertyCollection) proptype;
		if (cproptype.getSorting() != null) {
			if (cproptype.getSorting() == SortingType.byPropertyValues) {
				BeanSorter.set(cproptype.getSortingProptypes());
			}
		}
	}

	/**
	 * prepare sorting.
	 * 
	 * @param proptype the association end property's type
	 */
	public static void cleanupSorting(final TypeProperty proptype, final TypeProperty[] propsbefore) {
		if (!(proptype instanceof TypePropertyCollection)) {
			return;
		}
		final TypePropertyCollection aproptype = (TypePropertyCollection) proptype;
		if (aproptype.getSorting() != null) {
			if (aproptype.getSorting() == SortingType.byPropertyValues) {
				BeanSorter.set(propsbefore);
			}
		}
	}

	/**
	 * Remove all property change listeners from the collection given.
	 * 
	 * @param col a collection of links
	 */
	private void removePropertyChangeListeners(final Collection<Link> col) {
		final TypePropertyCollection proptype = (TypePropertyCollection) this.getType();
		if (col != null && (proptype.getSorting() == SortingType.byPropertyValues
				|| proptype.getSorting() == SortingType.byPropertyValuesAll)) {
			for (Link link : col) {
				if (link instanceof RapidBean) {
					((RapidBean) link).removePropertyChangeListener(this);
				}
			}
		}
	}

	private boolean isSorted(final Collection<?> newCol) {
		if (newCol instanceof TreeSet<?>) {
			return true;
		}
		if (newCol instanceof LinkedHashSet<?>) {
			return true;
		}
		return false;
	}

	public enum ValidationMode {
		add, remove, set
	}
}
