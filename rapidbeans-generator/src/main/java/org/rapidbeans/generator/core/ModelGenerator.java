/*
 * Rapid Beans Framework, SDK, Maven Plugin: ModelGenerator.java
 *
 * Copyright (C) 2013 Martin Bluemel
 *
 * Creation Date: 01/21/2013
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
package org.rapidbeans.generator.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;
import org.rapidbeans.generator.utils.CodeGenMode;
import org.rapidbeans.generator.utils.TypeOfType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.google.common.base.Strings;

/**
 * the genclasses task.
 * 
 * @author Mischur.Alexander
 */
public class ModelGenerator extends AbstractGenerator {

	private static final String DEFAULT_STYLE_BEAN = "genBean.xsl";

	private static final String DEFAULT_STYLE_QUANTITY = "genQuantity.xsl";

	private static final String DEFAULT_STYLE_ENUM = "genEnum.xsl";

	private static final String DEFAULT_STYLE_DIR = "styles/";

	private final String defaultPkgName;

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

	public ModelGenerator(File srcdir, File destdirsimple, File destdirjoint, String defaultPkgName, Log log) {
		this(srcdir, destdirsimple, destdirjoint, false, defaultPkgName, log);
	}

	public ModelGenerator(File srcdir, File destdirsimple, File destdirjoint, boolean force, String defaultPkgName,
			Log log) {
		super(srcdir, destdirsimple, destdirjoint, force, log);
		this.defaultPkgName = defaultPkgName;
	}

	public File getStyledir() {
		if (styledir == null) {
			styledir = new File(DEFAULT_STYLE_DIR);
		}
		return styledir;
	}

	public void setStyledir(File styledir) {
		this.styledir = styledir;
	}

	public File getStyleEnum() {
		if (styleEnum == null) {
			styleEnum = new File(getStyledir(), DEFAULT_STYLE_ENUM);
		}
		return styleEnum;
	}

	public void setStyleEnum(File styleEnum) {
		this.styleEnum = styleEnum;
	}

	public File getStyleQuantity() {
		if (styleQuantity == null) {
			styleQuantity = new File(getStyledir(), DEFAULT_STYLE_QUANTITY);
		}
		return styleQuantity;
	}

	public void setStyleQuantity(File styleQuantity) {
		this.styleQuantity = styleQuantity;
	}

	public File getStyleBean() {
		if (styleBean == null) {
			styleBean = new File(getStyledir(), DEFAULT_STYLE_BEAN);
		}
		return styleBean;
	}

	public void setStyleBean(File styleBean) {
		this.styleBean = styleBean;
	}

	public String getDefaultPkgName() {
		return defaultPkgName;
	}

	public boolean hasDefaultPkgName() {
		return !Strings.isNullOrEmpty(this.defaultPkgName);
	}

	@Override
	public void execute() throws BuildException {
		this.processDir(getSrcdir(), getDestdirsimple(), getDestdirjoint(), "");
	}

	/**
	 * iterates recursively over a complete directory tree.
	 * 
	 * @param sdir       source directory
	 * @param ddirsimple directory where generated files are stored
	 * @param ddirjoint  directory where merged files are stored
	 * @param pkgname    the package name
	 * @throws BuildException
	 */
	private void processDir(File sdir, File ddirsimple, File ddirjoint, String pkgname) throws BuildException {
		if (log.isDebugEnabled()) {
			log.debug("process: source directory: " + sdir.getAbsolutePath());
			log.debug("process: simple destination directory: " + ddirsimple.getAbsolutePath());
			log.debug("process: joint destination directory: " + ddirjoint.getAbsolutePath());
			log.debug("style directory: " + getStyledir().getAbsolutePath());
		}

		File[] sfiles = sdir.listFiles();
		String sfilename;
		String subPkgname;
		for (int i = 0; i < sfiles.length; i++) {
			sfilename = sfiles[i].getName();
			if (sfiles[i].isDirectory()) {
				if (pkgname.equals("")) {
					subPkgname = sfilename;
				} else {
					subPkgname = pkgname + "." + sfilename;
				}
				this.processDir(sfiles[i], new File(ddirsimple, sfilename), new File(ddirjoint, sfilename), subPkgname);
			} else {
				if (!sfilename.endsWith(AbstractGenerator.FILE_ENDING_XML)) {
					log.debug("skipping non XML file \"" + sfilename + "\"");
					continue;
				}

				final CodeGenInfo cgen = this.check(sfiles[i]);

				final File stylesheet = chooseStylesheet(sfilename, cgen);

				File ddir = null;
				File tgtfile = null;
				final String classname = sfilename.substring(0,
						sfilename.length() - AbstractGenerator.FILE_ENDING_XML_LENGTH);
				final String pkgdirname = pkgname.replace('.', '/');
				switch (cgen.getMode()) {
				case simple:
					ddir = new File(ddirsimple, pkgdirname);
					tgtfile = new File(ddir, classname + ".java");
					log.debug("codegen mode: simple");
					break;
				case split:
					ddir = new File(ddirsimple, pkgdirname);
					tgtfile = new File(ddir, "RapidBeanBase" + classname + ".java");
					log.debug("codegen mode: split");
					break;
				case joint:
					ddir = new File(ddirjoint, pkgdirname);
					tgtfile = new File(ddirjoint, classname + ".java");
					log.debug("codegen mode: joint");
					break;
				case none:
					log.debug("codegen mode: none");
					continue;
				default:
					log.debug("Illegal codegen mode");
					continue;
				}
				if (!isForce() && tgtfile.exists() && sfiles[i].lastModified() <= tgtfile.lastModified()
						&& stylesheet.lastModified() <= tgtfile.lastModified()) {
					log.debug("up to date file \"" + sfilename + "\"");
					continue;
				}
				try (final InputStream srcIs = FileUtils.openInputStream(sfiles[i]);
						final OutputStream tgtOs = FileUtils.openOutputStream(tgtfile);
						final InputStream styleIs = openStyleIs(cgen, stylesheet)) {
					this.transform(srcIs, tgtOs, styleIs, styleIsIsDefault(stylesheet), pkgname, classname, cgen);
				} catch (TransformerException e) {
					throw new BuildException(e);
				} catch (IOException e) {
					throw new BuildException(e);
				} catch (NullPointerException e) {
					throw new BuildException(e);
				}
			}
		}
	}

	private File chooseStylesheet(String sfilename, final CodeGenInfo cgen) {
		File stylesheet = null;
		switch (cgen.getTypeOfType()) {
		case undefined:
			log.debug("no description of bean type in file \"" + sfilename + "\". Skipping it.");
			return stylesheet;
		case enumtype:
			stylesheet = getStyleEnum();
			break;
		case quantitytype:
			stylesheet = getStyleQuantity();
			break;
		case beantype:
			stylesheet = getStyleBean();
			break;
		}
		return stylesheet;
	}

	private InputStream openStyleIs(final CodeGenInfo cgen, final File stylesheet) throws IOException {
		InputStream styleIs;
		if (stylesheet == null || !stylesheet.exists() || !stylesheet.canRead() || !stylesheet.isFile()) {
			log.warn("Given Stylesheets could not be found! Reading default ones from classpath");
			switch (cgen.getTypeOfType()) {
			case enumtype:
				styleIs = readFileFromClasspath(DEFAULT_STYLE_DIR + DEFAULT_STYLE_ENUM).openStream();
				break;
			case quantitytype:
				styleIs = readFileFromClasspath(DEFAULT_STYLE_DIR + DEFAULT_STYLE_QUANTITY).openStream();
				break;
			case beantype:
				styleIs = readFileFromClasspath(DEFAULT_STYLE_DIR + DEFAULT_STYLE_BEAN).openStream();
				break;
			default:
				throw new BuildException("unknown stylesheet type!");
			}
		} else {
			styleIs = FileUtils.openInputStream(stylesheet);
		}
		return styleIs;
	}

	private boolean styleIsIsDefault(final File stylesheet) {
		boolean isDefault = false;
		if (stylesheet == null || !stylesheet.exists() || !stylesheet.canRead() || !stylesheet.isFile()) {
			isDefault = true;
		}
		return isDefault;
	}

	/**
	 * check the XML model description file.
	 * 
	 * @param xmlFile the file to check
	 */
	private CodeGenInfo check(final File xmlFile) {
		try {
			CodeGenInfo cgen = new CodeGenInfo();

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
				return cgen;
			}

			final Node cgenNode = doc.getElementsByTagName("codegen").item(0);
			if (cgenNode != null) {
				final String mode = cgenNode.getAttributes().getNamedItem("mode").getNodeValue();
				if (mode.equalsIgnoreCase("none")) {
					cgen.setMode(CodeGenMode.none);
				} else if (mode.equalsIgnoreCase("simple")) {
					cgen.setMode(CodeGenMode.simple);
				} else if (mode.equalsIgnoreCase("split")) {
					cgen.setMode(CodeGenMode.split);
				} else if (mode.equalsIgnoreCase("joint")) {
					cgen.setMode(CodeGenMode.joint);
				}
			}
			return cgen;
		} catch (ParserConfigurationException e) {
			throw new BuildException(e);
		} catch (FileNotFoundException e) {
			throw new BuildException(e);
		} catch (SAXException e) {
			throw new BuildException(e);
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

	/**
	 * transform.
	 * 
	 * @param fsrc      the model file
	 * @param ftgt      the target source file
	 * @param fstyle    the code gen template
	 * @param pkgname   the package name
	 * @param classname the class name
	 * @throws TransformerException
	 * @throws IOException
	 */
	private void transform(InputStream srcIs, OutputStream tgtOs, InputStream styleIs, boolean isDefault,
			String pkgname, String classname, CodeGenInfo cgen) throws TransformerException, IOException {

		Source xmlSource = new StreamSource(srcIs);
		Source xsltSource = new StreamSource(styleIs);
		if (isDefault) {
			xsltSource.setSystemId(readFileFromClasspath(DEFAULT_STYLE_DIR).toString());
		}
		Result result = new StreamResult(tgtOs);

		TransformerFactory transFact = TransformerFactory.newInstance();

		Transformer trans = transFact.newTransformer(xsltSource);
		trans.setParameter("package", pkgname);
		trans.setParameter("classname", classname);
		trans.setParameter("codegen", cgen.getMode().name());

		trans.transform(xmlSource, result);
	}

	private URL readFileFromClasspath(final String filePath) throws IOException {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL mapUrl = cl.getResource(filePath);
		if (null == mapUrl) {
			// fall back to own class loader if TCCL does not find the resource
			mapUrl = ModelGenerator.class.getResource(filePath);
		}
		return mapUrl;
	}
}
