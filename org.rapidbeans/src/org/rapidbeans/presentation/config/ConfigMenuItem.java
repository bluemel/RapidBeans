/*
 * Rapid Beans Framework: ConfigMenuItem.java
 * 
 * Copyright (C) 2010 Martin Bluemel
 * 
 * Creation Date: 03/28/2010
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

package org.rapidbeans.presentation.config;

import java.util.List;

import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.enabler.Enabler;
import org.rapidbeans.service.Action;

/**
 * Rapid Bean class: ConfigMenuItem
 **/
public class ConfigMenuItem extends RapidBeanBaseConfigMenuItem {

	/**
	 * @return the action configuration either as linked or embedded one.
	 */
	@Override
	public Action getAction() {
		if (super.getChildaction() != null && super.getAction() != null) {
			throw new RapidBeansRuntimeException("Invalid configuration for menu item" + this.getName()
					+ "either use a" + " contained action or a linked action not both");
		}
		if (super.getAction() != null) {
			return super.getAction();
		}
		return super.getChildaction();
	}

	/**
	 * @return the roles required for this menu item either from this
	 *         configuration itself (prioritized way) or from the (central)
	 *         action definition.
	 */
	@Override
	public List<RapidEnum> getRolesrequired() {
		if (super.getRolesrequired() != null) {
			return super.getRolesrequired();
		}
		final Action action = this.getAction();
		if (action != null) {
			return action.getRolesrequired();
		}
		return null;
	}

	/**
	 * @return the enabler defined for this menu item either from this
	 *         configuration itself (prioritized way) or from the (central)
	 *         action definition.
	 */
	@Override
	public Enabler getEnabler() {
		if (super.getEnabler() != null) {
			return super.getEnabler();
		}
		final Action action = this.getAction();
		if (action != null) {
			return action.getEnabler();
		}
		return null;
	}

	/**
	 * default constructor.
	 */
	public ConfigMenuItem() {
		super();
	}

	/**
	 * constructor out of a string.
	 * 
	 * @param s
	 *            the string
	 */
	public ConfigMenuItem(final String s) {
		super(s);
	}

	/**
	 * constructor out of a string array.
	 * 
	 * @param sa
	 *            the string array
	 */
	public ConfigMenuItem(final String[] sa) {
		super(sa);
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(ConfigMenuItem.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}
}
