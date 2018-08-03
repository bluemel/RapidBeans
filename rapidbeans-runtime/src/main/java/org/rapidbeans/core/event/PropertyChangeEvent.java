/*
 * Rapid Beans Framework: PropertyChangeEvent.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 04/29/2006
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

package org.rapidbeans.core.event;

import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.RapidBean;

/**
 * A bean property change event.
 * 
 * @author Martin Bluemel
 */
public class PropertyChangeEvent {

	/**
	 * the Property that has been changed.
	 */
	private Property property;

	/**
	 * the value before the change.
	 */
	private Object oldValue;

	/**
	 * the value after the change.
	 */
	private Object newValue;

	/**
	 * the change type.
	 */
	private PropertyChangeEventType type;

	/**
	 * the link added or removed.
	 */
	private Link link;

	/**
	 * @return Returns the bean.
	 */
	public RapidBean getBean() {
		return this.property.getBean();
	}

	/**
	 * @return Returns the newValue.
	 */
	public Object getNewValue() {
		return this.newValue;
	}

	/**
	 * @return Returns the oldValue.
	 */
	public Object getOldValue() {
		return this.oldValue;
	}

	/**
	 * @return Returns the property.
	 */
	public Property getProperty() {
		return this.property;
	}

	/**
	 * constructor.
	 * 
	 * @param argProperty the Property than has been changed
	 * @param argOldValue the value before the change
	 * @param argNewValue the value after the change
	 * @param type        the event type
	 * @param link        the link that just has been added or removed
	 */
	public PropertyChangeEvent(final Property argProperty, final Object argOldValue, final Object argNewValue,
			final PropertyChangeEventType type, final Link link) {
		this.property = argProperty;
		this.oldValue = argOldValue;
		this.newValue = argNewValue;
		this.type = type;
		this.link = link;
	}

	/**
	 * @return the type of the event
	 */
	public PropertyChangeEventType getType() {
		return type;
	}

	/**
	 * @return the link of the event (only if type == CHANGE_TYPE_ADD/REMOVE)
	 */
	public Link getLink() {
		return link;
	}
}
