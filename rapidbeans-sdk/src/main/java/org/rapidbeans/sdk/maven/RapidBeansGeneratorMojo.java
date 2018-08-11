/*
 * Rapid Beans Framework, SDK, Maven Plugin: RapidBeansGeneratorMojo.java
 *
 * Copyright (C) 2013 Martin Bluemel
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

package org.rapidbeans.sdk.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.rapidbeans.sdk.core.CodeGenImpl;
import org.rapidbeans.sdk.core.CodeGenMode;
import org.rapidbeans.sdk.core.RapidBeansCodeGenerator;
import org.rapidbeans.sdk.utils.BuildException;

/**
 * Goal which touches a time stamp file.
 * 
 * @author Alexander Rolfes
 * @author Martin Bluemel
 */
@Mojo(name = "rapidbeans-generator")
public class RapidBeansGeneratorMojo extends AbstractMojo {

	/**
	 * the model model description's directory.
	 */
	@Parameter(required = true)
	private File srcdir;

	/**
	 * the generated source's directory before the merge.
	 */
	@Parameter(required = false, defaultValue = "${project.build.directory}/generated-sources/rapidbeans")
	private String destdirsimple;

	/**
	 * the generated source's directory where the merged files are placed, so it
	 * means the real generated sources dir.
	 */
	@Parameter(defaultValue = "${project.build.directory}/src/main/java")
	private String destdirjoint;

	/**
	 * the directory with the code generation templates.<br/>
	 * <br/>
	 * example:
	 * &lt;styledir&gt;${project.basedir}/src/rapidbeansstyles/&lt;/styledir&gt;<br/>
	 * <br/>
	 * If not specified the generator takes the default XSLT style sheets from the
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
	 */
	@Parameter(required = false)
	private File styledir;

	/**
	 * force flag.
	 */
	@Parameter(required = false, defaultValue = "false")
	private boolean force;

	/**
	 * codeGenMode the codeGenMode to set
	 */
	@Parameter(required = false, defaultValue = "simple")
	public CodeGenMode codeGenMode;

	/**
	 * @param codeGenImpl the codeGenImpl to set
	 */
	@Parameter(required = false, defaultValue = "simple")
	public CodeGenImpl codeGenImpl;

	/**
	 * the String used for indentation.
	 */
	@Parameter(required = false, defaultValue = "\t")
	public String indent;

	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	public void execute() throws MojoExecutionException {
		try {
			getLog().info("Running RapidBeans code generator...");
//			final TaskGenModel task = new TaskGenModel();
//			final Project project = new Project();
//			project.setBaseDir(project.getBaseDir());
//			task.setProject(project);
			final RapidBeansCodeGenerator task = new RapidBeansCodeGenerator(new SdkLoggerAdapterMaven(getLog()));
			task.setForce(this.force);
			task.setSrcdir(this.srcdir);
			task.setDestdirsimple(checkDestDir("destdirsimple", this.destdirsimple));
			task.setDestdirjoint(checkDestDir("destdirjoint", this.destdirjoint));
			task.setStyledir(this.styledir);
			task.setCodeGenMode(this.codeGenMode);
			task.setCodeGenImpl(this.codeGenImpl);
			task.setIndent(this.indent);
			task.execute();
			getLog().info("Finished RapidBEans code generator successfully.");
		} catch (RuntimeException e) {
			getLog().error("Finished RapidBEans code generator with Exception.", e);
			e.printStackTrace();
			throw e;
		}
	}

	private File checkDestDir(final String confPropName, final String destDirPath) {
		final File destDir = new File(destDirPath);
		if (destDir.exists()) {
			if (!destDir.isDirectory()) {
				throw new BuildException("Invalid configuration value");
			}
		} else {
			if (!destDir.mkdirs()) {
				throw new BuildException("Invalid configuration value");
			}
		}
		return destDir;
	}
}
