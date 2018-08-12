/*
 * Rapid Beans Framework, SDK, Ant Tasks: TaskGenModel.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 07/01/2005
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

package org.rapidbeans.sdk.ant;

import java.io.File;

import org.apache.tools.ant.Task;
import org.rapidbeans.sdk.core.CodeGenImpl;
import org.rapidbeans.sdk.core.CodeGenMode;
import org.rapidbeans.sdk.core.RapidBeansCodeGenerator;

/**
 * This Ant task conveniently drives the XSLT based RapidBeans code generation
 * for a whole directory hierarchy (similar to Ant task "javac").
 * 
 * @author Martin Bluemel
 */
public final class TaskGenModel extends Task {

	/**
	 * force flag.
	 */
	private boolean force = false;

	/**
	 * the model model description's directory.
	 */
	private File srcdir = null;

	/**
	 * the generated source's directory.
	 */
	private File destdirsimple = null;

	/**
	 * the generated source's directory.
	 */
	private File destdirjoint = null;

	/**
	 * the directory with the code generation templates.
	 */
	private File styledir = null;

	/**
	 * the code generation mode. Overwrites the mode particularly specified in the
	 * model.
	 */
	private CodeGenMode codeGenMode = null;

	/**
	 * the code generation implementation: { 'simple' | 'strict' }. Overwrites the
	 * implementation particularly specified in the model.
	 */
	private CodeGenImpl codeGenImpl = null;

	private String indent = "\t";

	/**
	 * set the force flag which determines if the generation should be performed not
	 * regarding modification dates.
	 * 
	 * @param force determines if the generation should be performed not regarding
	 *              modification dates
	 */
	public void setForce(final boolean force) {
		this.force = force;
	}

	/**
	 * set the model description's directory.
	 * 
	 * @param srcdir the model description's directory
	 */
	public void setSrcdir(final File srcdir) {
		this.srcdir = srcdir;
	}

	/**
	 * set the generated source's directory.
	 * 
	 * @param destdirsimple the generated source's directory
	 */
	public void setDestdirsimple(final File destdirsimple) {
		this.destdirsimple = destdirsimple;
	}

	/**
	 * set the generated source's directory.
	 * 
	 * @param destdirjoint the generated source's directory
	 */
	public void setDestdirjoint(final File destdirjoint) {
		this.destdirjoint = destdirjoint;
	}

	/**
	 * set the the directory with the code generation templates.
	 * 
	 * @param styledir the directory with the code generation templates
	 */
	public void setStyledir(final File styledir) {
		this.styledir = styledir;
	}

	/**
	 * @param codeGenMode the codeGenMode to set
	 */
	public void setCodeGenMode(CodeGenMode codeGenMode) {
		this.codeGenMode = codeGenMode;
	}

	/**
	 * @param codeGenImpl the codeGenImpl to set
	 */
	public void setCodeGenImpl(CodeGenImpl codeGenImpl) {
		this.codeGenImpl = codeGenImpl;
	}

	/**
	 * @param indent the indent to set
	 */
	public void setIndent(String indent) {
		this.indent = indent;
	}

	/**
	 * the execute method.
	 */
	public void execute() {
		final RapidBeansCodeGenerator generator = new RapidBeansCodeGenerator(new SdkLoggerAdapterAnt(getProject()));
		generator.setSrcdir(this.srcdir);
		generator.setStyledir(this.styledir);
		generator.setDestdirsimple(this.destdirsimple);
		generator.setDestdirjoint(this.destdirjoint);
		generator.setCodeGenImpl(this.codeGenImpl);
		generator.setCodeGenMode(this.codeGenMode);
		generator.setForce(this.force);
		generator.setIndent(this.indent);
		generator.execute();
	}
}
