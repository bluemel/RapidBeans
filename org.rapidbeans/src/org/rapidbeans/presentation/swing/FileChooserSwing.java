/*
 * Rapid Beans Framework: FileChooserSwing.java
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

package org.rapidbeans.presentation.swing;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.FileChooser;
import org.rapidbeans.presentation.FileChooserType;

/**
 * the class that drives the file chooser to load a document.
 * 
 * @author Martin Bluemel
 */
public class FileChooserSwing extends FileChooser {

	/**
	 * pop up the file chooser.
	 * 
	 * @param title
	 *            the dialog title
	 * @param dialogType
	 *            open, save, custom
	 * @param approveButtonText
	 *            the tedt for the approve button my be null for dialogTye open,
	 *            save
	 * @param dir
	 *            the folder opended initially
	 * @param filterText
	 *            the filter text to limit file choosing
	 * @param filterSuffix
	 *            the filter suffix to limit file choosing
	 * 
	 * @return the chosen file
	 */
	public File chooseFileDialog(final String title,
			final FileChooserType dialogType, final String approveButtonText,
			final File dir, final String filterText, final String filterSuffix) {
		JFileChooser chooser = new JFileChooser(dir);
		chooser.setDialogTitle(title);
		ExampleFileFilter filter = new ExampleFileFilter();
		filter.setDescription(filterText);
		filter.addExtension(filterSuffix);
		chooser.setFileFilter(filter);
		int returnVal = -1;
		switch (dialogType) {
		case open:
			returnVal = chooser.showOpenDialog((JFrame) ApplicationManager
					.getApplication().getMainwindow().getWidget());
			break;
		case save:
			returnVal = chooser.showSaveDialog((JFrame) ApplicationManager
					.getApplication().getMainwindow().getWidget());
			break;
		default:
			returnVal = chooser.showDialog((JFrame) ApplicationManager
					.getApplication().getMainwindow().getWidget(),
					approveButtonText);
			break;
		}
		File file = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
		}
		return file;
	}
}
