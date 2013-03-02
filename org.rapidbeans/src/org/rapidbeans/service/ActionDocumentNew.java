/*
 * Rapid Beans Framework: ActionDocumentNew.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/23/2007
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

package org.rapidbeans.service;

import java.util.List;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.util.StringHelper;
import org.rapidbeans.presentation.ApplicationManager;

/**
 * Action to drive creating a new document.
 * 
 * @author Martin Bluemel
 */
public class ActionDocumentNew extends Action {

	/**
	 * load a bean document from file.
	 */
	public void execute() {
		final String rootClassName = this.getArgumentValue("rootclass");
		if (rootClassName == null) {
			throw new RapidBeansRuntimeException(
					"Action argument \"rootclass\" not defined");
		}
		final List<String> sa = StringHelper.split(rootClassName, ".");
		final String shortRootClassName = sa.get(sa.size() - 1).toLowerCase();
		final String docconfname = this.getArgumentValue("docconfname",
				shortRootClassName);
		final String viewconfname = this.getArgumentValue("viewconfname",
				"standard");
		ApplicationManager.getApplication().openNewDocumentView(
				shortRootClassName + "_new", rootClassName, docconfname,
				viewconfname);
	}
}
