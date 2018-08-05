/*
 * Rapid Beans Framework, SDK, Ant Tasks: XXslt.java
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

package org.rapidbeans.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.XSLTProcess;
import org.apache.tools.ant.types.FileSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This Ant task supports XSLT based code generation of classes with protected
 * regions that allow them to be mixed with manually written parts.
 * 
 * @author Martin Bluemel
 */
public final class XXslt extends XSLTProcess {

	private enum PathMode {
		absolute, // absolute
		wd // relative to working directory
	}

	/**
	 * the path mode type.
	 */
	private PathMode pathmode = PathMode.wd;

	/**
	 * setter for the path mode.
	 * 
	 * @param argPathmode
	 *            the new value for this property.
	 */
	public void setPathmode(final String argPathmode) {
		this.pathmode = PathMode.valueOf(argPathmode);
	}

	/**
	 * the merge flag.
	 */
	private boolean merge = false;

	/**
	 * setter for merge flag.
	 * 
	 * @param argMerge
	 *            the new value for this property.
	 */
	public void setMerge(final boolean argMerge) {
		this.merge = argMerge;
	}

	/**
	 * the style file.
	 */
	private File style = null;

	/**
	 * setter for style file.
	 * 
	 * @param f
	 *            the new value for this property.
	 */
	public void setStyle(final File f) {
		super.setStyle(f.getAbsolutePath());
		this.style = f;
	}

	/**
	 * the input file.
	 */
	private File in = null;

	/**
	 * setter for input file.
	 * 
	 * @param f
	 *            the new value for this property.
	 */
	public void setIn(final File f) {
		super.setIn(f);
		this.in = f;
	}

	/**
	 * the output file.
	 */
	private File out = null;

	/**
	 * setter for output file.
	 * 
	 * @param f
	 *            the new value for this property.
	 */
	public void setOut(final File f) {
		super.setOut(f);
		this.out = f;
	}

	/**
	 * marks a one line comment.
	 */
	private String oneLineComment = "//";

	/**
	 * set the one line comment characters.
	 * 
	 * @param s
	 *            the one line comment characters
	 */
	public void setOneLineComment(final String s) {
		this.oneLineComment = s;
	}

	/**
	 * begin comment.
	 */
	private String sectionBegin = "BEGIN manual code section";

	/**
	 * set the string to mark the begin of a code section.
	 * 
	 * @param s
	 *            the string to mark the begin of a code section
	 */
	public void setSectionBegin(final String s) {
		this.sectionBegin = s;
	}

	/**
	 * end comment.
	 */
	private String sectionEnd = "END manual code section";

	/**
	 * set the string to mark the end of a code section.
	 * 
	 * @param s
	 *            the string to mark the end of a code section
	 */
	public void setSectionEnd(final String s) {
		this.sectionEnd = s;
	}

	/**
	 * unmatched section begin marker.
	 */
	private String sectionUnmatchedBegin = ">>> BEGIN unmatched code section";

	/**
	 * set the string to mark the begin of an unmatched code section.
	 * 
	 * @param s
	 *            the string to mark the begin of an unmatched code section. If
	 *            you take some characters that guarantee the compilation to
	 *            fail then you instantly are aware of unmatched sections.
	 */
	public void setSectionUnmatchedBegin(final String s) {
		this.sectionUnmatchedBegin = s;
	}

	/**
	 * unmatched section end marker.
	 */
	private String sectionUnmatchedEnd = ">>> END unmatched code section";

	/**
	 * set the string to mark the end of an unmatched code section.
	 * 
	 * @param s
	 *            the string to mark the end of an unmatched code section. If
	 *            you take some characters that guarantee the compilation to
	 *            fail then you instantly are aware of unmatched sections.
	 */
	public void setSectionUnmatchedEnd(final String s) {
		this.sectionUnmatchedEnd = s;
	}

	/**
	 * model file set.
	 */
	private Vector<FileSet> filesets = new Vector<FileSet>();

	/**
	 * Adds a set of files to be deleted.
	 * 
	 * @param set
	 *            the set of files to be deleted
	 */
	public void addFileset(final FileSet set) {
		this.filesets.addElement(set);
	}

	/**
	 * the force flag.
	 */
	private boolean force;

	/**
	 * setter for the force flag.
	 * 
	 * @param argForce
	 *            the force flag to set
	 */
	public void setForce(final boolean argForce) {
		super.setForce(argForce);
		this.force = argForce;
	}

	/**
	 * The execute method has to be implemented from every Ant task.
	 */
	public void execute() {
		File out1 = null;
		File out2 = null;
		ArrayList<File> infiles = null;
		final long lastModifiedOut = this.out.lastModified();
		// work on files in the file sets
		if (this.filesets.size() > 0) {
			try {
				this.in = File.createTempFile("xxsltIn", ".xml");
				super.setIn(this.in);
				infiles = evalFilesets();
				long lastModifiedIn = -1L;
				for (File file : infiles) {
					final long lm = file.lastModified();
					if (lm > lastModifiedIn) {
						lastModifiedIn = lm;
					}
				}
				if (!this.force && (lastModifiedIn <= lastModifiedOut && this.style.lastModified() <= lastModifiedOut)) {
					this.log("nothing to do (multiple input)...", Project.MSG_INFO);
					StringBuffer modelFiles = new StringBuffer();
					boolean firstRun = true;
					for (File file : infiles) {
						if (!firstRun) {
							modelFiles.append(", ");
						}
						modelFiles.append(file.getAbsolutePath());
						firstRun = false;
					}
					this.log("neither one of the model files: " + modelFiles.toString() + " nor style sheet "
							+ this.style.getAbsolutePath() + " is newer than output file" + this.out.getAbsolutePath()
							+ ".", Project.MSG_VERBOSE);
					return;
				}
				concatXmls(infiles, this.in);
			} catch (IOException e) {
				throw new BuildException(e);
			}
		} else {
			if (!this.force
					&& (this.in.lastModified() <= lastModifiedOut && this.style.lastModified() <= lastModifiedOut)) {
				this.log("nothing to do (single input)...", Project.MSG_INFO);
				this.log(
						"neither model file " + this.in.getAbsolutePath() + " nor style sheet "
								+ this.style.getAbsolutePath() + " is newer than output file"
								+ this.out.getAbsolutePath() + ".", Project.MSG_VERBOSE);
				return;
			}
		}
		AntGateway antGateway = new AntGateway(this.getProject(), this.getTaskName());
		try {
			if (this.merge) {
				out1 = new File(this.out.getAbsolutePath() + ".tmp1");
				out2 = new File(this.out.getAbsolutePath() + ".tmp2");
				super.setOut(out1);
				this.log("calling sub task \"xslt\"...", Project.MSG_DEBUG);
				this.log("  style = " + this.style.getAbsolutePath(), Project.MSG_DEBUG);
				this.log("  in = " + this.in, Project.MSG_DEBUG);
				this.log("  out = " + out1.getAbsolutePath(), Project.MSG_DEBUG);
				initParams();
				super.execute();

				this.log("merging file " + this.out.getAbsolutePath() + " with result of XSL generation",
						Project.MSG_INFO);
				MergeProperties mergeProps = new MergeProperties(this.oneLineComment, this.sectionBegin,
						this.sectionEnd, this.sectionUnmatchedBegin, this.sectionUnmatchedEnd);
				antGateway.mergeSections(out1, this.out, out2, mergeProps);
				this.log("calling sub task \"copy\"...", Project.MSG_VERBOSE);
				antGateway.copy(out2, this.out);
			} else {
				initParams();
				super.execute();
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			if (out1 != null) {
				out1.delete();
			}
			if (out2 != null) {
				out2.delete();
			}
			if (infiles != null) {
				this.in.delete();
			}
		}
	}

	/**
	 * set the params in out style merge.
	 */
	private void initParams() {
		Param param = super.createParam();
		param.setName("in");
		param.setExpression(this.in.getAbsolutePath());
		param = super.createParam();
		param.setName("out");
		param.setExpression(this.out.getAbsolutePath());
		param = super.createParam();
		param.setName("style");
		param.setExpression(this.style.getAbsolutePath());
		param = super.createParam();
		param.setName("merge");
		param.setExpression(new Boolean(this.merge).toString());
		param = super.createParam();
		param.setName("force");
		param.setExpression(new Boolean(this.force).toString());
		param = super.createParam();
		param.setName("force");
		param.setExpression(new Boolean(this.force).toString());
		switch (this.pathmode) {
		case wd:
			param = super.createParam();
			param.setName("root");
			param.setExpression(System.getProperty("user.dir"));
			break;
		default:
			break;
		}
	}

	/**
	 * convert the task's filesets to a Collection of Files.
	 * 
	 * @return an AraayList with the Files
	 */
	private ArrayList<File> evalFilesets() {
		final ArrayList<File> infiles = new ArrayList<File>();
		File file;
		for (FileSet fs : this.filesets) {
			try {
				DirectoryScanner ds = fs.getDirectoryScanner(getProject());
				String[] files = ds.getIncludedFiles();
				// String[] dirs = ds.getIncludedDirectories();
				for (String filename : files) {
					file = new File(fs.getDir(this.getProject()), filename);
					infiles.add(file);
				}
			} catch (BuildException be) {
				// directory doesn't exist or is not readable
				log(be.getMessage(), Project.MSG_WARN);
			}
		}
		return infiles;
	}

	/**
	 * concatenates the given XML file collection to one single XML file.
	 * 
	 * @param files
	 *            the XML files to concatenate
	 * @param catfile
	 *            the XML file with the concatenated content.
	 */
	private void concatXmls(final Collection<File> files, final File catfile) {
		try {
			final DocumentBuilderFactory dbfwr = DocumentBuilderFactory.newInstance();
			final DocumentBuilder dbwr = dbfwr.newDocumentBuilder();
			final XxsltXmlErrorHandler errorHandler = new XxsltXmlErrorHandler();
			errorHandler.setFile(catfile);
			dbwr.setErrorHandler(errorHandler);

			// create an XML DOM document
			// DOMImplementation impl = db.getDOMImplementation();
			final Document catdoc = dbwr.newDocument();

			// create and append the root node "packae" to the document
			Element rootModelNode = catdoc.createElement("model");
			catdoc.appendChild(rootModelNode);
			for (final File file : files) {
				final Element currentPackage = retrieveCurrentPackage(catdoc, rootModelNode, file);
				this.log("parsing XML file: " + file.getAbsolutePath() + "...", Project.MSG_VERBOSE);
				final Document doc = parseXmlModelFile(file);
				currentPackage.appendChild(cloneNode(catdoc, doc.getDocumentElement()));
			}

			// write the XML DOM document to catfile
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			Properties oformat = new Properties();
			oformat.setProperty(OutputKeys.INDENT, "yes");
			trans.setOutputProperties(oformat);

			Source src = new DOMSource(catdoc);
			Result dest = new StreamResult(catfile);
			trans.transform(src, dest);
		} catch (TransformerException e) {
			throw new BuildException(e);
		} catch (ParserConfigurationException e) {
			throw new BuildException(e);
		}
	}

	/**
	 * White space charcters.
	 */
	private static final char[] WSCHARS = { ' ', '\n', '\t' };

	/**
	 * parse a single XMLModelFile.
	 * 
	 * @param file
	 *            the XML model file to parse
	 * @return the XML Document parsed
	 */
	private Document parseXmlModelFile(final File file) {
		LineNumberReader lnr = null;
		try {
			lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(file)));
			final String firstLine = lnr.readLine();
			boolean validate = false;
			if ((firstLine != null)
					&& StringHelper.strip(firstLine, WSCHARS, StringHelper.StripMode.leading).startsWith("<?xml")) {
				final String secondLine = lnr.readLine();
				if ((secondLine != null)
						&& StringHelper.strip(secondLine, WSCHARS, StringHelper.StripMode.leading).startsWith(
								"<!DOCTYPE")) {
					validate = true;
				}
			}
			final XxsltXmlErrorHandler errorHandler = new XxsltXmlErrorHandler();
			errorHandler.setFile(file);
			final DocumentBuilderFactory dbfrd = DocumentBuilderFactory.newInstance();
			dbfrd.setNamespaceAware(true);
			dbfrd.setValidating(validate);
			final DocumentBuilder dbrd = dbfrd.newDocumentBuilder();
			dbrd.setErrorHandler(errorHandler);
			final Document doc = dbrd.parse(new FileInputStream(file));
			return doc;
		} catch (SAXException e) {
			throw new BuildException(e);
		} catch (IOException e) {
			throw new BuildException(e);
		} catch (ParserConfigurationException e) {
			throw new BuildException(e);
		} finally {
			if (lnr != null) {
				try {
					lnr.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * copy the given node and it's subnodes into the given XML DOM document.
	 * 
	 * @param doc
	 *            the document
	 * @param node
	 *            the node to copy
	 * @return the node in the document
	 */
	private Node cloneNode(final Document doc, final Node node) {
		Node clone = null;
		switch (node.getNodeType()) {
		case Node.ELEMENT_NODE:
			clone = doc.createElement(node.getNodeName());
			Element el = (Element) clone;

			NamedNodeMap attributes = node.getAttributes();
			Node attrNode;
			final int l1 = attributes.getLength();
			for (int i = 0; i < l1; i++) {
				attrNode = attributes.item(i);
				el.setAttribute(attrNode.getNodeName(), attrNode.getNodeValue());
			}
			break;
		case Node.ENTITY_NODE:
			clone = doc.createEntityReference(node.getNodeName());
			break;
		case Node.TEXT_NODE:
			clone = doc.createTextNode(node.getTextContent());
			break;
		default:
			break;
		}

		final NodeList childNodes = node.getChildNodes();
		final int l2 = childNodes.getLength();
		Node childNode;
		for (int i = 0; i < l2; i++) {
			childNode = childNodes.item(i);
			clone.appendChild(this.cloneNode(doc, childNode));
		}
		return clone;
	}

	/**
	 * retrieve the current package element in the XML DOM tree.
	 * 
	 * @param doc
	 *            the XML document tree
	 * @param rootNode
	 *            the root node
	 * @param file
	 *            the file
	 * @return the current package element in the XML DOM tree
	 */
	private Element retrieveCurrentPackage(final Document doc, final Element rootNode, final File file) {
		Element currentPackage = rootNode;
		String filePath = file.getPath();
		final String currentDir = System.getProperty("user.dir");
		if (filePath.startsWith(currentDir)) {
			filePath = filePath.substring(currentDir.length());
		}
		final StringTokenizer st = new StringTokenizer(filePath, File.separator);
		while (st.hasMoreTokens()) {
			final String pkgname = st.nextToken();
			if (!pkgname.equals(file.getName())) {
				final NodeList subPackages = rootNode.getElementsByTagName("package");
				final int len = subPackages.getLength();
				Element newSubPackage = null;
				for (int i = 0; i < len; i++) {
					final Element subPackage = (Element) subPackages.item(i);
					if (subPackage.getAttribute("name").equals(pkgname)) {
						newSubPackage = subPackage;
						break;
					}
				}
				if (newSubPackage == null) {
					newSubPackage = doc.createElement("package");
					newSubPackage.setAttribute("name", pkgname);
					currentPackage.appendChild(newSubPackage);
				}
				currentPackage = newSubPackage;
			}
		}
		if (currentPackage == rootNode) {
			currentPackage = null;
		}
		return currentPackage;
	}

	/**
	 * the XML error handler.
	 * 
	 * @author Martin Bluemel
	 */
	class XxsltXmlErrorHandler implements ErrorHandler {

		/**
		 * the default constructor.
		 */
		public XxsltXmlErrorHandler() {
		}

		/**
		 * the XML file currently parsed.
		 */
		private File file = null;

		/**
		 * warning.
		 * 
		 * @param e
		 *            the exception
		 * @throws SAXException
		 *             the exception
		 */
		public void warning(final SAXParseException e) throws SAXException {
			throw new BuildException("XML Parser Warning in file \"" + this.file.getAbsolutePath() + "\", line "
					+ e.getLineNumber() + ":\n" + e.getMessage(), e);
		}

		/**
		 * error.
		 * 
		 * @param e
		 *            the exception
		 * @throws SAXException
		 *             the exception
		 */
		public void error(final SAXParseException e) throws SAXException {
			throw new BuildException("XML Parser Error in file\n    " + this.file.getAbsolutePath() + "\", line "
					+ e.getLineNumber() + ":\n    " + e.getMessage(), e);
		}

		/**
		 * fatal error.
		 * 
		 * @param e
		 *            the exception
		 * @throws SAXException
		 *             the exception
		 */
		public void fatalError(final SAXParseException e) throws SAXException {
			throw new BuildException("XML Parser Fatal Error in file \"" + this.file.getAbsolutePath() + "\", line "
					+ e.getLineNumber() + ":\n" + e.getMessage(), e);
		}

		// /**
		// * @return Returns the file.
		// */
		// public File getFile() {
		// return this.file;
		// }

		/**
		 * @param argFile
		 *            The file to set.
		 */
		public void setFile(final File argFile) {
			this.file = argFile;
		}
	}
}
