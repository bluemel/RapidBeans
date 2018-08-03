/*
 * Rapid Beans Framework: ChangedEvent.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 10/26/2006
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

package org.rapidbeans.datasource.event;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.event.PropertyChangeEvent;

/**
 * Bean changed event.
 * 
 * @author Martin Bluemel
 */
public class ChangedEvent {

	/**
	 * Bean.
	 */
	private RapidBean bean = null;

	/**
	 * @return changed bean
	 */
	public RapidBean getBean() {
		return this.bean;
	}

	/**
	 * changed properties.
	 */
	private PropertyChangeEvent[] propertyEvents = null;

	/**
	 * @return changed properties
	 */
	public PropertyChangeEvent[] getPropertyEvents() {
		return this.propertyEvents;
	}

	/**
	 * constructor.
	 * 
	 * @param bbean                 the bean
	 * @param changedPropertyEvents the properties changed
	 */
	public ChangedEvent(final RapidBean rbean, final PropertyChangeEvent[] changedPropertyEvents) {
		this.bean = rbean;
		this.propertyEvents = changedPropertyEvents;
	}
}
