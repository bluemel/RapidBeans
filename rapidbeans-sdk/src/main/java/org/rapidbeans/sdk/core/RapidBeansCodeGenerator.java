/*
 * Rapid Beans Framework, SDK: RapidBeansCodeGenerator.java derived from old Ant task (TaskGenModel)
 * 
 * Copyright (C) 2018 Martin Bluemel
 * 
 * Creation Date: 08/09/2018
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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.rapidbeans.sdk.utils.BuildException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This class conveniently drives the XSLT based RapidBeans code generation for
 * a whole directory hierarchy (similar to Ant task "javac" or to Maven compiler
 * plugin).
 * 
 * @author Martin Bluemel
 */
public final class RapidBeansCodeGenerator {

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
	 * the code generation mode. Overwrites the mode particularly specified in the
	 * model.
	 */
	private CodeGenMode codeGenMode = null;

	private static final CodeGenMode DEFAULT_CODEGEN_MODE = CodeGenMode.simple;

	/**
	 * the code generation implementation: { 'simple' | 'strict' }. Overwrites the
	 * implementation particularly specified in the model.
	 */
	private CodeGenImpl codeGenImpl = null;

	private static final CodeGenImpl DEFAULT_CODEGEN_IMPL = CodeGenImpl.simple;

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
		if (styledir != null && this.styleBean == null) {
			this.styleBean = new File(styledir, "genBean.xsl");
		}
		if (styledir != null && this.styleEnum == null) {
			this.styleEnum = new File(styledir, "genEnum.xsl");
		}
		if (styledir != null && this.styleQuantity == null) {
			this.styleQuantity = new File(styledir, "genQuantity.xsl");
		}
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

	private final SdkLogger logger;

	public RapidBeansCodeGenerator(final SdkLogger logger) {
		this.logger = logger;
	}

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
			throw new BuildException(
					"Invalid destination directory. File \"" + this.destdirsimple + " is not a directory");
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
			throw new BuildException(
					"Invalid destination directory. File \"" + this.destdirjoint + " is not a directory");
		}
		if (!this.destdirjoint.exists()) {
			throw new BuildException("Destination directory \"" + this.destdirjoint + " not found");
		}
		if (this.styledir == null) {
			final ModelCodeGenerator generator = new ModelCodeGenerator();
			final File targetFolder = new File("target/rapidbeans-sdk-stylesheets");
			this.styleBean = generator.loadResourceStyle("genBean.xsl", targetFolder);
			this.styleEnum = generator.loadResourceStyle("genEnum.xsl", targetFolder);
			this.styleQuantity = generator.loadResourceStyle("genQuantity.xsl", targetFolder);
			generator.loadResourceStyle("java.xsl", targetFolder);
			generator.loadResourceStyle("string.xsl", targetFolder);
		}
		final CgenStat stat = new CgenStat();
		stat.startTime();
		this.processDir(this.srcdir, this.destdirsimple, this.destdirjoint, "", stat, new XslTransformer());
		stat.stopTime();
		this.logger.info(String.format("Generated %d code files, skipped %d code file already up to date in %s",
				stat.getGenerated(), stat.getUptodate(), stat.getExecTimeReadable()));
	}

	/**
	 * the lenght of extension ".xml".
	 */
	private static final int LEN_EXTENSION = 4;

	/**
	 * iterates recursively over a complete directory tree.
	 * 
	 * @param sdir    source directory.
	 * @param ddir    target directory.
	 * @param pkgname the package name.
	 * @param stat    code generation statistics.
	 */
	public void processDir(final File sdir, final File ddirsimple, final File ddirjoint, final String pkgname,
			final CgenStat stat, final XslTransformer xslTransformer) {
		this.logger.debug("process: source directory: " + sdir.getAbsolutePath());
		this.logger.debug("process: simple destination directory: " + ddirsimple.getAbsolutePath());
		this.logger.debug("process: joint destination directory: " + ddirjoint.getAbsolutePath());
		this.logger.debug("style directory: " + (this.styledir == null ? "null" : this.styledir.getAbsolutePath()));

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
				this.processDir(sfiles[i], new File(ddirsimple, sfilename), new File(ddirjoint, sfilename), subPkgname,
						stat, xslTransformer);
			} else {
				if (!sfilename.endsWith(".xml")) {
					this.logger.info("skipping non XML file \"" + sfilename + "\"");
					continue;
				}

				final CodeGenParameters cgen = this.extractCodeGenParameters(sfiles[i]);

				switch (cgen.getTypeOfType()) {
				case undefined:
					this.logger.info("no description of bean type in file \"" + sfilename + "\"");
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
					this.logger.debug("codegen mode: simple");
					break;
				case split:
					ddir = new File(this.destdirsimple, pkgdirname);
					tgtfile = new File(ddir, "RapidBeanBase" + classname + ".java");
					this.logger.debug("codegen mode: split");
					break;
				case joint:
					ddir = new File(ddirjoint, pkgdirname);
					tgtfile = new File(ddirjoint, classname + ".java");
					this.logger.debug("codegen mode: joint");
					break;
				case none:
					this.logger.debug("codegen mode: none");
					continue;
				default:
					this.logger.debug("Illegal codegen mode");
					continue;
				}
				this.transform(sfiles[i], tgtfile, stylesheet, pkgname, classname, cgen, stat, xslTransformer);
			}
		}
	}

	/**
	 * Read the XML model description file and determine in advance:
	 * 
	 * - "type" of Rapid type: bean type, enum type, quantity type determines the
	 * template to use for code generation
	 * 
	 * @param xmlFile the file to check
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

			cgen.setCodeGenMode(determineCodeGenMode(doc));

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
			throw new BuildException(
					"IOException: while trying to parse XML file \"" + xmlFile.getAbsolutePath() + "\"", e);
		} catch (ParserConfigurationException e) {
			throw new BuildException("ParserConfigurationException: while trying to parse XML file \""
					+ xmlFile.getAbsolutePath() + "\"", e);
		} catch (SAXException e) {
			throw new BuildException("SAXException: while parsing XML file \"" + xmlFile.getAbsolutePath() + "\"", e);
		}
	}

	private CodeGenMode determineCodeGenMode(Document doc) {
		CodeGenMode codeGenMode = DEFAULT_CODEGEN_MODE;
		if (this.codeGenMode == CodeGenMode.flexible) {
			final Node cgenNode = doc.getElementsByTagName("codegen").item(0);
			if (cgenNode != null) {
				final Node modeNode = cgenNode.getAttributes().getNamedItem("mode");
				if (modeNode != null) {
					final String mode = modeNode.getNodeValue();
					if (mode.equalsIgnoreCase("none")) {
						codeGenMode = CodeGenMode.none;
					} else if (mode.equalsIgnoreCase("simple")) {
						codeGenMode = CodeGenMode.simple;
					} else if (mode.equalsIgnoreCase("split")) {
						codeGenMode = CodeGenMode.split;
					} else if (mode.equalsIgnoreCase("joint")) {
						codeGenMode = CodeGenMode.joint;
					} else {
						throw new RuntimeException("Invalid codegen mode \"" + mode + "\"");
					}
				}
			}
		} else if (this.codeGenMode != null) {
			codeGenMode = this.codeGenMode;
		}
		return codeGenMode;
	}

	/**
	 * transform.
	 * 
	 * @param fsrc      the model file
	 * @param ftgt      the target source file
	 * @param fstyle    the code gen template
	 * @param pkgname   the package name
	 * @param classname the class name
	 */
	private void transform(final File fsrc, final File ftgt, final File fstyle, final String pkgname,
			final String classname, final CodeGenParameters cgen, final CgenStat stat,
			final XslTransformer xslTransformer) {

		final XXslt xsltTask = new XXslt(this.logger);
		xsltTask.setIn(fsrc);
		xsltTask.setOut(ftgt);
		xsltTask.setStyle(fstyle);
		xsltTask.setForce(this.force);
		if (cgen.getCodeGenMode() == CodeGenMode.joint) {
			xsltTask.setMerge(true);
		}

		final Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("package", pkgname);
		parameters.put("classname", classname);
		parameters.put("codegen", cgen.getCodeGenMode().name());
		parameters.put("implementation", cgen.getImplementation().name());
		parameters.put("indent", cgen.getIndent());
		xsltTask.setParameters(parameters);

		if (XXslt.notForceAndOutUpToDate(this.force, fsrc.lastModified(), 0L, ftgt.lastModified())) {
			this.logger.debug(String.format("Code up to date: %s", ftgt.getAbsolutePath()));
			stat.incUptodate();
			return;
		}
		this.logger.debug(String.format("Generating code:\n  model %s\n  code: %s", fsrc.getAbsolutePath(),
				ftgt.getAbsolutePath()));
		this.logger.debug("IN File \"" + fsrc.getAbsolutePath() + "\"");
		this.logger.debug("OUT File \"" + ftgt.getAbsolutePath() + "\"");
		this.logger.debug("STYLE File \"" + fstyle.getAbsolutePath() + "\"");
		this.logger.debug("performing RapidBeans code generation of class \"" + ftgt);
		stat.incGenerated();
		xsltTask.execute(xslTransformer);
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
		 * @param typeOfType the typeOfType to set
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
		 * @param mode the mode to set
		 */
		protected void setCodeGenMode(CodeGenMode codeGenMode) {
			this.codeGenMode = codeGenMode;
		}

		/**
		 * @param implementation the implementation to set
		 */
		protected void setImplementation(CodeGenImpl implementation) {
			this.implementation = implementation;
		}

		/**
		 * @param indent the indent to set
		 */
		protected void setIndent(String indent) {
			this.indent = indent;
		}

		public CodeGenImpl getImplementation() {
			return implementation;
		}

		public String getIndent() {
			return indent;
		}
	}
}
