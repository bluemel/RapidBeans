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

package org.rapidbeans.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.XSLTProcess;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This Ant task conveniently drives the XSLT based RapidBeans code generation for a whole directory hierarchy
 * (similar to Ant task "javac").
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
	 * the code generation mode.
	 * Overwrites the mode particularly specified in the model.
	 */
	private CodeGenMode codeGenMode = null;

	private static final CodeGenMode DEFAULT_CODEGEN_MODE = CodeGenMode.simple;

	/**
	 * the code generation implementation: { 'simple' | 'strict' }.
	 * Overwrites the implementation particularly specified in the model.
	 */
	private CodeGenImpl codeGenImpl = null;

	private static final CodeGenImpl DEFAULT_CODEGEN_IMPL = CodeGenImpl.simple;

	private String indent = "\t";

	/**
	 * set the force flag which determines if the generation should be performed
	 * not regarding modification dates.
	 * 
	 * @param force
	 *            determines if the generation should be performed
	 *            not regarding modification dates
	 */
	public void setForce(final boolean force) {
		this.force = force;
	}

	/**
	 * set the model description's directory.
	 * 
	 * @param srcdir
	 *            the model description's directory
	 */
	public void setSrcdir(final File srcdir) {
		this.srcdir = srcdir;
	}

	/**
	 * set the generated source's directory.
	 * 
	 * @param destdirsimple
	 *            the generated source's directory
	 */
	public void setDestdirsimple(final File destdirsimple) {
		this.destdirsimple = destdirsimple;
	}

	/**
	 * set the generated source's directory.
	 * 
	 * @param destdirjoint
	 *            the generated source's directory
	 */
	public void setDestdirjoint(final File destdirjoint) {
		this.destdirjoint = destdirjoint;
	}

	/**
	 * set the the directory with the code generation templates.
	 * 
	 * @param styledir
	 *            the directory with the code generation templates
	 */
	public void setStyledir(final File styledir) {
		this.styledir = styledir;
		this.styleBean = new File(styledir, "genBean.xsl");
		this.styleEnum = new File(styledir, "genEnum.xsl");
		this.styleQuantity = new File(styledir, "genQuantity.xsl");
	}

	/**
	 * @param codeGenMode
	 *            the codeGenMode to set
	 */
	public void setCodeGenMode(CodeGenMode codeGenMode) {
		this.codeGenMode = codeGenMode;
	}

	/**
	 * @param codeGenImpl
	 *            the codeGenImpl to set
	 */
	public void setCodeGenImpl(CodeGenImpl codeGenImpl) {
		this.codeGenImpl = codeGenImpl;
	}

	/**
	 * @param indent
	 *            the indent to set
	 */
	public void setIndent(String indent) {
		this.indent = indent;
	}

	/**
	 * the extended XSLT task.
	 */
	private XXslt xsltTask = null;

	/**
	 * the package parameter.
	 */
	private XSLTProcess.Param xsltParameterPackage = null;

	/**
	 * the classname parameter.
	 */
	private XSLTProcess.Param xsltParameterClassname = null;

	/**
	 * the codegen parameter.
	 */
	private XSLTProcess.Param xsltParameterCodegen = null;

	/**
	 * the implementation parameter.
	 */
	private XSLTProcess.Param xsltParameterImplementation = null;

	/**
	 * the indent parameter.
	 */
	private XSLTProcess.Param xsltParameterIndent = null;

	/**
	 * the execute method.
	 */
	public void execute() {
		// plausi checks for attributes
		if (this.srcdir == null) {
			throw new BuildException("No Source directory. Please define value for attribute \"srcdir\".");
		}
		if (!this.srcdir.exists()) {
			throw new BuildException("Source directory \"" + this.srcdir + " not found");
		}
		if (!this.srcdir.isDirectory()) {
			throw new BuildException("Invalid source directory. File \"" + this.srcdir + " is not a directory");
		}
		if (this.destdirsimple == null) {
			throw new BuildException("No Destination directory. Please define value for attribute \"destdir\".");
		}
		if (!this.destdirsimple.exists()) {
			throw new BuildException("Destination directory \"" + this.destdirsimple + " not found");
		}
		if (!this.destdirsimple.isDirectory()) {
			throw new BuildException("Invalid destination directory. File \"" + this.destdirsimple
					+ " is not a directory");
		}
		if (!this.destdirsimple.exists()) {
			throw new BuildException("Destination directory \"" + this.destdirsimple + " not found");
		}
		if (this.destdirjoint == null) {
			throw new BuildException("No Destination directory. Please define value for attribute \"destdirjoint\".");
		}
		if (!this.destdirjoint.exists()) {
			throw new BuildException("Destination directory \"" + this.destdirjoint + " not found");
		}
		if (!this.destdirjoint.isDirectory()) {
			throw new BuildException("Invalid destination directory. File \"" + this.destdirjoint
					+ " is not a directory");
		}
		if (!this.destdirjoint.exists()) {
			throw new BuildException("Destination directory \"" + this.destdirjoint + " not found");
		}
		this.processDir(this.srcdir, this.destdirsimple, this.destdirjoint, "");
	}

	/**
	 * the lenght of extension ".xml".
	 */
	private static final int LEN_EXTENSION = 4;

	/**
	 * iterates recursively over a complete directory tree.
	 * 
	 * @param sdir
	 *            source directory
	 * @param ddir
	 *            target directory
	 * @param pkgname
	 *            the package name
	 * @throws BuildException
	 */
	public void processDir(final File sdir, final File ddirsimple,
			final File ddirjoint, final String pkgname) {
		this.log("process: source directory: " + sdir.getAbsolutePath(), Project.MSG_VERBOSE);
		this.log("process: simple destination directory: " + ddirsimple.getAbsolutePath(), Project.MSG_VERBOSE);
		this.log("process: joint destination directory: " + ddirjoint.getAbsolutePath(), Project.MSG_VERBOSE);
		this.log("style directory: " + this.styledir.getAbsolutePath(), Project.MSG_VERBOSE);

		File[] sfiles = sdir.listFiles();
		String sfilename;
		File stylesheet = null;
		String subPkgname;
		for (int i = 0; i < sfiles.length; i++) {
			sfilename = sfiles[i].getName();
			if (sfiles[i].isDirectory()) {
				if (pkgname.equals("")) {
					subPkgname = sfilename;
				} else {
					subPkgname = pkgname + "." + sfilename;
				}
				this.processDir(sfiles[i], new File(ddirsimple, sfilename),
						new File(ddirjoint, sfilename), subPkgname);
			} else {
				if (!sfilename.endsWith(".xml")) {
					this.log("skipping non XML file \"" + sfilename + "\"", Project.MSG_VERBOSE);
					continue;
				}

				final CodeGenParameters cgen = this.extractCodeGenParameters(sfiles[i]);

				switch (cgen.getTypeOfType()) {
				case undefined:
					this.log("no description of bean type in file \"" + sfilename + "\"", Project.MSG_VERBOSE);
					continue;
				case enumtype:
					stylesheet = this.styleEnum;
					break;
				case quantitytype:
					stylesheet = this.styleQuantity;
					break;
				case beantype:
					stylesheet = this.styleBean;
					break;
				}

				File ddir = null;
				File tgtfile = null;
				final String classname = sfilename.substring(0, sfilename.length() - LEN_EXTENSION);
				final String pkgdirname = pkgname.replace('.', '/');
				switch (cgen.getCodeGenMode()) {
				case simple:
					ddir = new File(this.destdirsimple, pkgdirname);
					tgtfile = new File(ddir, classname + ".java");
					this.log("codegen mode: simple", Project.MSG_VERBOSE);
					break;
				case split:
					ddir = new File(this.destdirsimple, pkgdirname);
					tgtfile = new File(ddir, "RapidBeanBase" + classname + ".java");
					this.log("codegen mode: split", Project.MSG_VERBOSE);
					break;
				case joint:
					ddir = new File(ddirjoint, pkgdirname);
					tgtfile = new File(ddirjoint, classname + ".java");
					this.log("codegen mode: joint", Project.MSG_VERBOSE);
					break;
				case none:
					this.log("codegen mode: none", Project.MSG_VERBOSE);
					continue;
				default:
					this.log("Illegal codegen mode",
							Project.MSG_VERBOSE);
					continue;
				}
				if (!this.force && tgtfile.exists()
						&& sfiles[i].lastModified() <= tgtfile.lastModified()
						&& stylesheet.lastModified() <= tgtfile.lastModified()) {
					this.log("up to date file \"" + sfilename + "\"", Project.MSG_VERBOSE);
					continue;
				}
				this.transform(sfiles[i], tgtfile, stylesheet, pkgname, classname, cgen);
			}
		}
	}

	/**
	 * Read the XML model description file and determine in advance:
	 * 
	 * - "type" of Rapid type: bean type, enum type, quantity type
	 * determines the template to use for code generation
	 * 
	 * @param xmlFile
	 *            the file to check
	 */
	private CodeGenParameters extractCodeGenParameters(final File xmlFile) {
		try {
			CodeGenParameters cgen = new CodeGenParameters();

			// read XML file as DOM
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// dbf.setNamespaceAware(true);
			// boolean validate = false;
			// if (this.xmlValidating)
			// validate = true;
			// dbf.setValidating(validate);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new FileInputStream(xmlFile));

			if (doc.getElementsByTagName("enumtype").item(0) != null) {
				cgen.setTypeOfType(TypeOfType.enumtype);
			} else if (doc.getElementsByTagName("quantitytype").item(0) != null) {
				cgen.setTypeOfType(TypeOfType.quantitytype);
			} else if (doc.getElementsByTagName("beantype").item(0) != null) {
				cgen.setTypeOfType(TypeOfType.beantype);
			} else {
				throw new BuildException("Could not determine type of Rapid type { 'bean' | 'enum' | 'quantity' }");
			}

			if (this.codeGenMode != null) {
				cgen.setCodeGenMode(this.codeGenMode);
			} else {
				final Node cgenNode = doc.getElementsByTagName("codegen").item(0);
				if (cgenNode == null) {
					cgen.setCodeGenMode(DEFAULT_CODEGEN_MODE);
				} else {
					final Node modeNode = cgenNode.getAttributes().getNamedItem("mode");
					if (modeNode == null) {
						cgen.setCodeGenMode(DEFAULT_CODEGEN_MODE);
					} else {
						final String mode = modeNode.getNodeValue();
						if (mode.equalsIgnoreCase("none")) {
							cgen.setCodeGenMode(CodeGenMode.none);
						} else if (mode.equalsIgnoreCase("simple")) {
							cgen.setCodeGenMode(CodeGenMode.simple);
						} else if (mode.equalsIgnoreCase("split")) {
							cgen.setCodeGenMode(CodeGenMode.split);
						} else if (mode.equalsIgnoreCase("joint")) {
							cgen.setCodeGenMode(CodeGenMode.joint);
						} else {
							throw new RuntimeException("Invalid codegen mode \"" + mode + "\"");
						}
					}
				}
			}

			if (this.codeGenImpl != null) {
				cgen.setImplementation(this.codeGenImpl);
			} else {
				final Node cgenNode = doc.getElementsByTagName("codegen").item(0);
				if (cgenNode == null) {
					cgen.setImplementation(DEFAULT_CODEGEN_IMPL);
				} else {
					final Node implNode = cgenNode.getAttributes().getNamedItem("implementation");
					if (implNode == null) {
						cgen.setImplementation(DEFAULT_CODEGEN_IMPL);
					} else {
						final String impl = implNode.getNodeValue();
						if (impl.equalsIgnoreCase("strict")) {
							cgen.setImplementation(CodeGenImpl.strict);
						} else if (impl.equalsIgnoreCase("simple")) {
							cgen.setImplementation(CodeGenImpl.simple);
						} else {
							throw new RuntimeException("Invalid codegen implementation \"" + impl + "\"");
						}
					}
				}
			}

			if (this.indent != null) {
				cgen.setIndent(this.indent);
			}

			return cgen;

		} catch (IOException e) {
			throw new BuildException(e);
		} catch (ParserConfigurationException e) {
			throw new BuildException(e);
		} catch (SAXException e) {
			throw new BuildException(e);
		}
	}

	/**
	 * transform.
	 * 
	 * @param fsrc
	 *            the model file
	 * @param ftgt
	 *            the target source file
	 * @param fstyle
	 *            the code gen template
	 * @param pkgname
	 *            the package name
	 * @param classname
	 *            the class name
	 */
	private void transform(final File fsrc, final File ftgt,
			final File fstyle, final String pkgname,
			final String classname, final CodeGenParameters cgen) {
		// since Ant 1.7 we can't reuse the XSLT Task in the way we did before.
		// Maybe later on we could find out what we have to reset in order to reuse
		// the task again
		this.xsltTask = new XXslt();
		this.xsltTask.setProject(this.getProject());
		this.xsltTask.setTaskName("xxslt");
		this.xsltTask.setIn(fsrc);
		this.xsltTask.setOut(ftgt);
		this.xsltTask.setStyle(fstyle);
		this.xsltTask.setForce(this.force);
		if (cgen.getCodeGenMode() == CodeGenMode.joint) {
			this.xsltTask.setMerge(true);
		}

		this.xsltParameterPackage = this.xsltTask.createParam();
		this.xsltParameterPackage.setName("package");
		this.xsltParameterPackage.setExpression(pkgname);

		this.xsltParameterClassname = this.xsltTask.createParam();
		this.xsltParameterClassname.setName("classname");
		this.xsltParameterClassname.setExpression(classname);

		this.xsltParameterCodegen = this.xsltTask.createParam();
		this.xsltParameterCodegen.setName("codegen");
		this.xsltParameterCodegen.setExpression(cgen.getCodeGenMode().name());

		this.xsltParameterImplementation = this.xsltTask.createParam();
		this.xsltParameterImplementation.setName("implementation");
		this.xsltParameterImplementation.setExpression(cgen.getImplementation().name());

		this.xsltParameterIndent = this.xsltTask.createParam();
		this.xsltParameterIndent.setName("indent");
		this.xsltParameterIndent.setExpression(cgen.getIndent());

		this.log("IN File \"" + fsrc.getAbsolutePath() + "\"", Project.MSG_VERBOSE);
		this.log("OUT File \"" + ftgt.getAbsolutePath() + "\"", Project.MSG_VERBOSE);
		this.log("STYLE File \"" + fstyle.getAbsolutePath() + "\"", Project.MSG_VERBOSE);
		this.log("performing RapidBeans code generation of class \"" + ftgt, Project.MSG_VERBOSE);
		this.xsltTask.execute();
	}

	private class CodeGenParameters {

		/**
		 * the type of RapidBeans type.
		 */
		private TypeOfType typeOfType = TypeOfType.undefined;

		/**
		 * code generation mode.
		 */
		private CodeGenMode codeGenMode = null;

		/**
		 * code generation implementation.
		 */
		private CodeGenImpl implementation = null;

		/**
		 * code generation indentation string.
		 */
		private String indent = null;

		/**
		 * @return the typeOfType
		 */
		protected TypeOfType getTypeOfType() {
			return typeOfType;
		}

		/**
		 * @param typeOfType
		 *            the typeOfType to set
		 */
		protected void setTypeOfType(TypeOfType typeOfType) {
			this.typeOfType = typeOfType;
		}

		/**
		 * @return the mode
		 */
		protected CodeGenMode getCodeGenMode() {
			return codeGenMode;
		}

		/**
		 * @param mode
		 *            the mode to set
		 */
		protected void setCodeGenMode(CodeGenMode codeGenMode) {
			this.codeGenMode = codeGenMode;
		}

		protected CodeGenImpl getImplementation() {
			return implementation;
		}

		/**
		 * @param implementation
		 *            the implementation to set
		 */
		protected void setImplementation(CodeGenImpl implementation) {
			this.implementation = implementation;
		}

		protected String getIndent() {
			return indent;
		}

		/**
		 * @param indent
		 *            the indent to set
		 */
		protected void setIndent(String indent) {
			this.indent = indent;
		}
	}
}
