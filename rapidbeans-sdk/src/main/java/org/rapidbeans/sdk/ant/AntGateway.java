/*
 * Rapid Beans Framework, SDK, Ant Tasks: AntGateway.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 10/29/2005
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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Mkdir;
import org.apache.tools.ant.taskdefs.Move;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.taskdefs.Replace;
import org.apache.tools.ant.taskdefs.Touch;
import org.apache.tools.ant.taskdefs.XSLTProcess;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;
import org.rapidbeans.sdk.core.XXslt;
import org.rapidbeans.sdk.core.XslTransformer;
import org.rapidbeans.sdk.merge.KeyValuePair;
import org.rapidbeans.sdk.merge.MergeProperties;

/**
 * this class just provides a convenient interface to use this lots of pretty
 * ant components called tasks.
 * 
 * @author Martin Bluemel
 */
public final class AntGateway {
	/**
	 * the Ant project.
	 */
	private Project project = null;

	/**
	 * environment properties.
	 */
	private Properties envProps = null;

	/**
	 * task name potentially used.
	 */
	private String taskName = null;

	/**
	 * The normal constructor for the Ant gateway. The Ant Property task member is
	 * used for caching the environment variables are cached by the Ant Property
	 * task.
	 * 
	 * @param proj        the projct currently Ant works on.
	 * @param argTaskName the taskName to use
	 */
	public AntGateway(final Project proj, final String argTaskName) {
		this.project = proj;
		// load environment variables into the
		// project's properties (prefix "env.")
		Property propTask = new Property();
		propTask.setProject(this.project);
		propTask.setEnvironment("env");
		propTask.execute();
		this.taskName = argTaskName;
	}

	/**
	 * Constructor for tests without given project and environment. The Ant Property
	 * task member is used for caching the environment variables are cached by the
	 * Ant Property task.
	 */
	public AntGateway() {
		this.project = new Project();
		// load environment variables into the
		// project's properties (prefix "env.")
		Property propTask = new Property();
		propTask.setProject(this.project);
		propTask.setEnvironment("env");
		propTask.execute();
	}

	/**
	 * A constructor only for testing reasons. You can initialize the environment by
	 * means of a properties file.
	 * 
	 * @param proj        the project that ant currently works on.
	 * @param argEnvProps the file withe the environment varibles defined as
	 *                    properties.
	 */
	protected AntGateway(final Project proj, final File argEnvProps) {
		this();
		this.envProps = new Properties();
		try {
			this.envProps.load(new FileInputStream(argEnvProps));
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

	/**
	 * Applies XSL transformation and performs mergeBodies afterwards.
	 * 
	 * @param style      the XSL file (String)
	 * @param in         the input file
	 * @param out        the output file
	 * @param params     the parameter values
	 * @param mergeProps merge properties. If not null the a merge is performed
	 *                   after XSL transformation.
	 */
	public void xxslt(final File style, final File in, final File out, final KeyValuePair[] params,
			final MergeProperties mergeProps, final boolean force) {
		final XXslt task = new XXslt(new SdkLoggerAdapterAnt(this.project));
		task.setStyle(style);
		task.setIn(in);
		task.setOut(out);
		task.setForce(force);
		final Map<String, String> paramMap = new HashMap<String, String>();
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				paramMap.put(params[i].getKey(), params[i].getValue());
			}
		}
		task.setParameters(paramMap);
		if (mergeProps != null) {
			task.setMerge(true);
			if (mergeProps.getOneLineComment() != null) {
				task.setOneLineComment(mergeProps.getOneLineComment());
			}
			if (mergeProps.getSectionBegin() != null) {
				task.setSectionBegin(mergeProps.getSectionBegin());
			}
			if (mergeProps.getSectionEnd() != null) {
				task.setSectionEnd(mergeProps.getSectionEnd());
			}
			if (mergeProps.getSectionUnmatchedBegin() != null) {
				task.setSectionUnmatchedBegin(mergeProps.getSectionUnmatchedBegin());
			}
			if (mergeProps.getSectionUnmatchedEnd() != null) {
				task.setSectionUnmatchedEnd(mergeProps.getSectionUnmatchedEnd());
			}
		}
		task.execute(new XslTransformer());
	}

	/**
	 * Applies XSL transformation.
	 * 
	 * @param style  the XSL file (String)
	 * @param in     the input file
	 * @param out    the output file
	 * @param params the parameter values
	 */
	public void xslt(final String style, final File in, final File out, final KeyValuePair[] params) {
		XSLTProcess task = new XSLTProcess();
		this.setTaskName(task, "xslt");
		task.setProject(this.project);
		task.setStyle(style);
		task.setIn(in);
		task.setOut(out);
		XSLTProcess.Param param;
		for (int i = 0; i < params.length; i++) {
			param = task.createParam();
			param.setName(params[i].getKey());
			param.setExpression(params[i].getValue());
		}
		task.execute();
	}

	/**
	 * Applies XSL transformation.
	 * 
	 * @param style      the XSL file (File)
	 * @param in         the input file
	 * @param out        the output file
	 * @param params     the parameter values
	 * @param argProject the ant project
	 */
	public void xslt(final File style, final File in, final File out, final KeyValuePair[] params,
			final Project argProject) {
		XSLTProcess task = new XSLTProcess();
		if (argProject == null) {
			task.setProject(this.project);
		} else {
			task.setProject(argProject);
		}
		setTaskName(task, "xslt");
		task.setStyle(style.getAbsolutePath());
		task.setIn(in);
		task.setOut(out);
		XSLTProcess.Param param;
		this.project.log("  style = " + style.getAbsolutePath(), Project.MSG_VERBOSE);
		this.project.log("  in = " + in.getAbsolutePath(), Project.MSG_VERBOSE);
		this.project.log("  out = " + out.getAbsolutePath(), Project.MSG_VERBOSE);
		for (int i = 0; i < params.length; i++) {
			param = task.createParam();
			param.setName(params[i].getKey());
			param.setExpression(params[i].getValue());
			this.project.log("  param[" + i + "] = " + "(" + param.getName() + "|" + param.getExpression() + ")",
					Project.MSG_VERBOSE);
		}
		task.execute();
	}

	/**
	 * Merges one generated source code file with the manual parts of a second one.
	 * 
	 * @param srcfilegen the file with the (generated) source code
	 * @param srcfileman the file with the manually coded parts
	 * @param destfile   the file containing the merge result
	 */
	public void mergeSections(final File srcfilegen, final File srcfileman, final File destfile) {
		TaskMergeSections task = new TaskMergeSections();
		task.setProject(this.project);
		setTaskName(task, "TaskMergeSections");
		task.setSrcfilegen(srcfilegen);
		task.setSrcfileman(srcfileman);
		task.setDestfile(destfile);
		task.execute();
	}

	/**
	 * Merges one generated source code file with the manual parts of a second one.
	 * 
	 * @param srcfilegen the file with the (generated) source code
	 * @param srcfileman the file with the manually coded parts
	 * @param destfile   the file containing the merge result
	 * @param mergeProps the strings to configure the merge markers
	 */
	public void mergeSections(final File srcfilegen, final File srcfileman, final File destfile,
			final MergeProperties mergeProps) {
		TaskMergeSections task = new TaskMergeSections();
		task.setProject(this.project);
		setTaskName(task, "TaskMergeSections");
		task.setSrcfilegen(srcfilegen);
		task.setSrcfileman(srcfileman);
		task.setDestfile(destfile);
		if (mergeProps.getOneLineComment() != null) {
			task.setOneLineComment(mergeProps.getOneLineComment());
		}
		if (mergeProps.getSectionBegin() != null) {
			task.setSectionBegin(mergeProps.getSectionBegin());
		}
		if (mergeProps.getSectionEnd() != null) {
			task.setSectionEnd(mergeProps.getSectionEnd());
		}
		if (mergeProps.getSectionUnmatchedBegin() != null) {
			task.setSectionUnmatchedBegin(mergeProps.getSectionUnmatchedBegin());
		}
		if (mergeProps.getSectionUnmatchedEnd() != null) {
			task.setSectionUnmatchedEnd(mergeProps.getSectionUnmatchedEnd());
		}
		task.execute();
	}

	/**
	 * Creates a folder (directory) and all its parent folders if neccessary.
	 * 
	 * @param dir the folder (directory) to create
	 */
	public void mkdir(final File dir) {
		Mkdir task = new Mkdir();
		task.setProject(this.project);
		setTaskName(task, "Mkdir");
		task.setDir(dir);
		task.execute();
	}

	/**
	 * Determines values of enviromnment variables.
	 * 
	 * @param envvarname the name of an environment variable
	 * @return the value of the specified environment variable
	 */
	public String getEnv(final String envvarname) {
		if (this.envProps == null) {
			return this.project.getProperty("env." + envvarname);
		} else {
			return this.envProps.getProperty(envvarname);
		}
	}

	/**
	 * Copies src to or over target. - if target not exists - if src is newer than
	 * target
	 * 
	 * @param src    source file
	 * @param target target file
	 */
	public void copy(final File src, final File target) {
		copy(src, target, false);
	}

	/**
	 * Copies src to or over target. - if target not exists - if src is newer than
	 * target
	 * 
	 * @param src       source file
	 * @param target    target file
	 * @param overwrite true if the copy should be forced
	 */
	public void copy(final File src, final File target, final boolean overwrite) {
		Copy task = new Copy();
		task.setProject(this.project);
		setTaskName(task, "Copy");
		task.setFile(src);
		task.setTofile(target);
		task.setOverwrite(overwrite);
		task.execute();
	}

	/**
	 * Applies the Ant Copy task on one directory. Mainly all files of the source
	 * directory are copied to the target directory. If one target file is already
	 * present the copy task compares the modification date of source and target. If
	 * the source is newer than the target the source is copied over the target.
	 * 
	 * @param src    the source file
	 * @param target the target file
	 */
	public void copyDirs(final File src, final File target) {
		Copy task = new Copy();
		task.setProject(this.project);
		setTaskName(task, "CopyDirs");
		FileSet fileset = new FileSet();
		fileset.setDir(src);
		task.addFileset(fileset);
		task.setTodir(target);
		task.execute();
	}

	/**
	 * Deletes a single file or a whole directory if it exists. If not it does
	 * nothing.
	 * 
	 * @param file a single file or a directory.
	 */
	public void delete(final File file) {
		Delete task = new Delete();
		task.setProject(this.project);
		setTaskName(task, "Delete");
		if (file.isDirectory()) {
			task.setDir(file);
		} else {
			task.setFile(file);
		}
		task.execute();
	}

	/**
	 * Unpacks an archive file by mean of the Ant Expand task.
	 * 
	 * @param packedFile the source archive file.
	 * @param dest       the destination directory.
	 */
	public void expand(final File packedFile, final File dest) {
		Expand task = new Expand();
		task.setProject(this.project);
		setTaskName(task, "Expand");
		task.setSrc(packedFile);
		task.setDest(dest);
		task.execute();
	}

	/**
	 * Moves a single file or a whole direcory.
	 * 
	 * @param src  the source file or directory
	 * @param dest the target file or directory
	 */
	public void moveToDir(final File src, final File dest) {
		Move task = new Move();
		task.setProject(this.project);
		setTaskName(task, "Move");
		if (src.isDirectory()) {
			FileSet fileset = new FileSet();
			fileset.setDir(src);
			task.addFileset(fileset);
		} else {
			task.setFile(src);
		}
		task.setTodir(dest);
		task.execute();
	}

	/**
	 * Replaces all occurences of a string in a file with another string.
	 * 
	 * @param repFile the file with the strings to replace.
	 * @param from    the search string.
	 * @param to      the string which replaces the search string.
	 */
	public void replace(final File repFile, final String from, final String to) {
		Replace task = new Replace();
		task.setProject(this.project);
		setTaskName(task, "Replace");
		task.setFile(repFile);
		task.setToken(from);
		task.setValue(to);
		task.execute();
	}

	/**
	 * Excecutes a system command by means of the Ant Exec task. Consider rather to
	 * take this method for system command execution than the java.lang.Runtime
	 * class, because this class just stops the execution if the stdout buffer is
	 * full and not read.
	 * 
	 * @param executable the path to the executable file.
	 * @param args       the program arguments
	 */
	public void exec(final String executable, final String[] args) {
		final ExecTask task = new ExecTask();
		task.setProject(this.project);
		setTaskName(task, "Exec");
		final Commandline cmdl = new Commandline();
		cmdl.setExecutable(executable);
		cmdl.addArguments(args);
		task.setCommand(cmdl);
		task.setFailIfExecutionFails(true);
		task.setFailonerror(true);
		task.execute();
	}

	/**
	 * updates the modification time of the given file.
	 * 
	 * @param file the file to touch.
	 */
	public void touch(final File file) {
		Touch task = new Touch();
		task.setProject(this.project);
		setTaskName(task, "Touch");
		task.setFile(file);
		task.execute();
	}

	/**
	 * set a task name from this.taskName or default.
	 * 
	 * @param task        the task where to set the task name
	 * @param defaultName the default value
	 */
	private void setTaskName(final Task task, final String defaultName) {
		if (this.taskName != null) {
			task.setTaskName(this.taskName);
		} else {
			task.setTaskName(defaultName);
		}
	}
}
