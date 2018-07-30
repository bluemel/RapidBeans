/*
 * Rapid Beans Framework, SDK, Maven Plugin: RapidBeansGeneratorMojo.java
 *
 * Copyright (C) 2013 Martin Bluemel
 *
 * Creation Date: 01/18/2013
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
package org.rapidbeans.generator.maven;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal generate
 * @phase generate-sources
 */
public class RapidBeansGeneratorMojo extends AbstractMojo {

	/**
	 * force flag.
	 * 
	 * @parameter default-value="false" property="rapidBeansForce"
	 */
	private final boolean force = false;

	/**
	 * the model description's directory.
	 * 
	 * @parameter default-value="${project.basedir}/src/model/"
	 */
	private final File modeldir = null;

	/**
	 * the generated source's directory before the merge.
	 * 
	 * @parameter default-value=
	 *            "${project.build.directory}/rapidbeans/generated-sources/simplesources/"
	 */
	private final File destdirsimple = null;

	/**
	 * the generated source's directory where the merged files are placed, so it
	 * means the real generated sources dir.
	 * 
	 * @parameter default-value=
	 *            "${project.build.directory}/rapidbeans/generated-sources/java/"
	 */
	private final File destdirjoint = null;

	/**
	 * the directory with the code generation templates.<br/>
	 * <br/>
	 * example:
	 * &lt;styledir&gt;${project.basedir}/src/rapidbeansstyles/&lt;/styledir&gt;<br/>
	 * <br/>
	 * If not specified the generater takes the default xlst stylesheets from the
	 * plugin.<br/>
	 * <br/>
	 * if specified the files in the directory must be named:
	 * <ul>
	 * <li><code>genBean.xsl</code></li>
	 * <li><code>genEnum.xsl</code></li>
	 * <li><code>genQuantity.xsl</code></li>
	 * </ul>
	 * You can overrule the the filenames with paramters:
	 * <ul>
	 * <li><code>styleEnum</code></li>
	 * <li><code>styleQuantity</code></li>
	 * <li><code>styleBean</code></li>
	 * </ul>
	 * 
	 * @paramter
	 */
	private final File styledir = null;

	/**
	 * the code generation template for Enums.
	 */
	private File styleEnum;

	/**
	 * the code generation template for Quantities.
	 */
	private File styleQuantity;

	/**
	 * the code generation template for Rapid Beans.
	 */
	private File styleBean;

	/**
	 * Location of the file.
	 * 
	 * @parameter property="${project.build.directory}"
	 * @required
	 */
	private File outputDirectory;

	public void execute() throws MojoExecutionException {
		File f = outputDirectory;

		if (!f.exists()) {
			f.mkdirs();
		}

		File touch = new File(f, "touch.txt");

		FileWriter w = null;
		try {
			w = new FileWriter(touch);

			w.write("touch.txt");
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating file " + touch, e);
		} finally {
			if (w != null) {
				try {
					w.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	public File getStyleEnum() {
		return styleEnum;
	}

	public void setStyleEnum(File styleEnum) {
		this.styleEnum = styleEnum;
	}

	public File getStyleQuantity() {
		return styleQuantity;
	}

	public void setStyleQuantity(File styleQuantity) {
		this.styleQuantity = styleQuantity;
	}

	public File getStyleBean() {
		return styleBean;
	}

	public void setStyleBean(File styleBean) {
		this.styleBean = styleBean;
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public boolean isForce() {
		return force;
	}

	public File getModeldir() {
		return modeldir;
	}

	public File getDestdirsimple() {
		return destdirsimple;
	}

	public File getDestdirjoint() {
		return destdirjoint;
	}

	public File getStyledir() {
		return styledir;
	}
}
