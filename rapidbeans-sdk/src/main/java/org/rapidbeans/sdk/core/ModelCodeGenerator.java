/*
 * Rapid Beans Framework, SDK: ModelCodeGenerator.java
 * 
 * Copyright (C) 2018 Martin Bluemel
 * 
 * Creation Date: 07/31/2018
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

package org.rapidbeans.sdk.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.tools.ant.BuildException;

public class ModelCodeGenerator {

	private static final int BUF_LEN = 2048;

	public File loadResourceStyle(final String resourceFileName, final File targetFolder) {
		final File styleFile = new File(targetFolder, resourceFileName);
		if (!styleFile.getParentFile().exists()) {
			if (!styleFile.getParentFile().mkdirs()) {
				throw new BuildException("Could not create directory " + styleFile.getParentFile().getAbsolutePath());
			}
		}
		loadResourceFile("/stylesheets/" + resourceFileName, styleFile);
		return styleFile;
	}

	private void loadResourceFile(final String resourcePath, final File targetFile) {
		try (final InputStream is = getClass().getResourceAsStream(resourcePath);
				final OutputStream os = new FileOutputStream(targetFile)) {
			if (is == null) {
				throw new BuildException(
						String.format("Input resource file not found at relative path \"%s\".", resourcePath));
			}
			final byte[] buf = new byte[BUF_LEN];
			int bytesRead;
			while ((bytesRead = is.read(buf)) != -1) {
				os.write(buf, 0, bytesRead);
			}
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}
}
