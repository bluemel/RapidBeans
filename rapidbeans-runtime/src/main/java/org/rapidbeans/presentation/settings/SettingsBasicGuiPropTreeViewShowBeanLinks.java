/*
 * Rapid Beans Framework: SettingsBasicGuiPropTreeViewShowBeanLinks.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/14/2007
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

package org.rapidbeans.presentation.settings;

import org.rapidbeans.core.basic.PropertyBoolean;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.event.SettingsChangedEvent;

/**
 * extension from date property BillingPeriod.to.
 */
public class SettingsBasicGuiPropTreeViewShowBeanLinks extends PropertyBoolean {

	/**
	 * constructor.
	 * 
	 * @param type       the property type
	 * @param parentBean the parent bean
	 */
	public SettingsBasicGuiPropTreeViewShowBeanLinks(final TypeProperty type, final RapidBean parentBean) {
		super(type, parentBean);
	}

	/**
	 * @param newValue if links should be shown or not
	 */
	public void setValue(final Object newValue) {
		super.setValue(newValue);
		Application client = ApplicationManager.getApplication();
		if (client != null) {
			client.fireSettingsChanged(new SettingsChangedEvent(this));
		}
	}
}
