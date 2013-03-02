/*
 * Rapid Beans Framework, SDK, Ant Tasks: ModifyProperty.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 09/05/2007
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

package org.rapidbeans.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * this Ant task implements a very simple JDK base FTP upload.
 * 
 * @author Martin.Bluemel
 */
public final class ModifyProperty extends Task {

	/**
	 * the property file.
	 */
	private File file = null;

	/**
	 * the property name.
	 */
	private String name = null;

	/**
	 * the mode.
	 */
	private String mode = null;

	/**
	 * The execute method has to be implemented from every Ant task.
	 */
	public void execute() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(this.file));
			if (mode.equals("increment")) {
				final String oldVal = props.getProperty(this.name);
				if (oldVal == null) {
					throw new BuildException("property \"" + this.name
							+ "\" not found");
				}
				props.setProperty(this.name,
						Integer.toString((Integer.parseInt(oldVal) + 1)));
			} else {
				throw new BuildException("unsupported \"" + this.mode + "\"");
			}
			props.store(new FileOutputStream(this.file), null);
		} catch (FileNotFoundException e) {
			throw new BuildException(e);
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
}
