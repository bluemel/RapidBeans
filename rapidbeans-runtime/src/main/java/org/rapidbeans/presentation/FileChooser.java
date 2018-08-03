/*
 * Rapid Beans Framework: FileChooser.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 12/12/2005
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

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.util.StringHelper;

/**
 * the class that drives the file chooser to load a document.
 * 
 * @author Martin Bluemel
 */
public abstract class FileChooser {

	/**
	 * the loading method.
	 * 
	 * @param title        the dialog title
	 * @param dialogType   open, save
	 * @param dir          the folder opended initially
	 * @param filterText   the filter text to limit file choosing
	 * @param filterSuffix the filter suffix to limit file choosing
	 * 
	 * @return the chosen file
	 */
	public static File chooseFile(final String title, final FileChooserType dialogType, final File dir,
			final String filterText, final String filterSuffix) {
		FileChooser loadDialog = createInstance();
		return loadDialog.chooseFileDialog(title, dialogType, null, dir, filterText, filterSuffix);
	}

	/**
	 * the loading method.
	 * 
	 * @param title             the dialog title
	 * @param dialogType        open, save, custom
	 * @param approveButtonText the tedt for the approve button my be null for
	 *                          dialogTye open, save
	 * @param dir               the folder opended initially
	 * @param filterText        the filter text to limit file choosing
	 * @param filterSuffix      the filter suffix to limit file choosing
	 * 
	 * @return the chosen file
	 */
	public static File chooseFile(final String title, final FileChooserType dialogType, final String approveButtonText,
			final File dir, final String filterText, final String filterSuffix) {
		FileChooser loadDialog = createInstance();
		return loadDialog.chooseFileDialog(title, dialogType, approveButtonText, dir, filterText, filterSuffix);
	}

	/**
	 * internally used for reflective instance creation.
	 */
	private static final Class<?>[] CONSTRUCTOR_PARAM_TYPES = new Class[0];

	/**
	 * internally used for reflective instance creation.
	 */
	private static final Object[] CONSTRUCTOR_PARAM_ARGS = new Object[0];

	/**
	 * Creates a FileChooser for the given Document class of the configured GUI type
	 * (Swing, Eclipse RCP).
	 * 
	 * @return the load dialog instance
	 */
	private static FileChooser createInstance() {
		FileChooser dialog = null;
		String guiclassname = null;
		try {
			final String guitype = ApplicationManager.getApplication().getConfiguration().getGuitype().toString();
			final String guitypeUpperFirstChar = StringHelper.upperFirstCharacter(guitype);
			guiclassname = "org.rapidbeans.presentation." + guitype + ".FileChooser" + guitypeUpperFirstChar;
			final Class<?> guiclass = Class.forName(guiclassname);
			Constructor<?> constructor = guiclass.getConstructor(CONSTRUCTOR_PARAM_TYPES);
			dialog = (FileChooser) constructor.newInstance(CONSTRUCTOR_PARAM_ARGS);
		} catch (NoSuchMethodException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		} catch (InstantiationException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new RapidBeansRuntimeException(
					"DocumentLaodDialog GUI specific sublclass not found" + ": " + guiclassname);
		}
		return dialog;
	}

	/**
	 * pop up the file chooser.
	 * 
	 * @param title             the dialog title
	 * @param dialogType        open, save, custom
	 * @param approveButtonText the tedt for the approve button my be null for
	 *                          dialogTye open, save
	 * @param dir               the folder opended initially
	 * @param filterText        the filter text to limit file choosing
	 * @param filterSuffix      the filter suffix to limit file choosing
	 * 
	 * @return the chosen file
	 */
	public abstract File chooseFileDialog(String title, FileChooserType dialogType, String approveButtonText, File dir,
			String filterText, String filterSuffix);
}
