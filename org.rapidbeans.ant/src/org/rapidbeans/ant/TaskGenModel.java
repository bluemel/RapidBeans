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
 * the genclasses task.
 *
 * @author Martin Bluemel
 */
public final class TaskGenModel extends Task {

    /**
     * force flag.
     */
    private boolean force = false;

    /**
     * set the force flag which determines if the generation should be performed
     * not regarding modification dates.
     * @param argForce determines if the generation should be performed
     *                 not regarding modification dates
     */
    public void setForce(final boolean argForce) {
        this.force = argForce;
    }

    /**
     * the model model description's directory.
     */
    private File srcdir = null;

    /**
     * set the model description's directory.
     * @param f the model description's directory
     */
    public void setSrcdir(final File f) {
        this.srcdir = f;
    }

    /**
     * the generated source's directory.
     */
    private File destdirsimple = null;

    /**
     * set the generated source's directory.
     * @param argDestdir the generated source's directory
     */
    public void setDestdirsimple(final File argDestdir) {
        this.destdirsimple = argDestdir;
    }

    /**
     * the generated source's directory.
     */
    private File destdirjoint = null;

    /**
     * set the generated source's directory.
     * @param argDestdir the generated source's directory
     */
    public void setDestdirjoint(final File argDestdir) {
        this.destdirjoint = argDestdir;
    }

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
     * set the the directory with the code generation templates.
     * @param argStyledir the directory with the code generation templates
     */
    public void setStyledir(final File argStyledir) {
        this.styledir = argStyledir;
        this.styleBean = new File(argStyledir, "genBean.xsl");
        this.styleEnum = new File(argStyledir, "genEnum.xsl");
        this.styleQuantity = new File(argStyledir, "genQuantity.xsl");
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
     * the classname parameter.
     */
    private XSLTProcess.Param xsltParameterCodegen = null;

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
            throw new BuildException("Invalid destination directory. File \"" + this.destdirsimple + " is not a directory");
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
            throw new BuildException("Invalid destination directory. File \"" + this.destdirjoint + " is not a directory");
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

                final CodeGenInfo cgen = this.check(sfiles[i]);

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
                switch (cgen.getMode()) {
                case simple:
                    ddir = new File (this.destdirsimple, pkgdirname);
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
     * check the XML model description file.
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
     * @param fsrc the model file
     * @param ftgt the target source file
     * @param fstyle the code gen template
     * @param pkgname the package name
     * @param classname the class name
     */
    private void transform(final File fsrc, final File ftgt,
            final File fstyle, final String pkgname, final String classname,
            final CodeGenInfo cgen) {
        // since Ant 1.7 we can't reuse the XSLT Task in the way we did before.
        // Maybe later on we could find out what we have to reset in order to reuse
        // the task again
        this.xsltTask = new XXslt();
        this.xsltTask.setProject(this.getProject());
        this.xsltTask.setTaskName("xxslt");
        this.xsltParameterPackage = this.xsltTask.createParam();
        this.xsltParameterPackage.setName("package");
        this.xsltParameterClassname = this.xsltTask.createParam();
        this.xsltParameterClassname.setName("classname");
        this.xsltParameterCodegen = this.xsltTask.createParam();
        this.xsltParameterCodegen.setName("codegen");

        this.log("IN File \"" + fsrc.getAbsolutePath() + "\"", Project.MSG_VERBOSE);
        this.xsltTask.setIn(fsrc);
        this.log("OUT File \"" + ftgt.getAbsolutePath() + "\"", Project.MSG_VERBOSE);
        this.xsltTask.setOut(ftgt);
        this.log("STYLE File \"" + fstyle.getAbsolutePath() + "\"", Project.MSG_VERBOSE);
        this.xsltTask.setStyle(fstyle);
        this.xsltTask.setForce(this.force);
        if (cgen.getMode() == CodeGenMode.joint) {
            this.xsltTask.setMerge(true);
        }
        this.log("performing RapidBeans code generation of class \"" + ftgt, Project.MSG_VERBOSE);
        this.xsltParameterPackage.setExpression(pkgname);
        this.xsltParameterClassname.setExpression(classname);
        this.xsltParameterCodegen.setExpression(cgen.getMode().name());

        this.xsltTask.execute();
    }

    private class CodeGenInfo {

        /**
         * the type of RapidBeans type.
         */
        private TypeOfType typeOfType = TypeOfType.undefined;

        /**
         * code generation mode.
         */
        private CodeGenMode mode = CodeGenMode.simple;

        /**
         * @return the mode
         */
        public CodeGenMode getMode() {
            return mode;
        }

        /**
         * @return the typeOfType
         */
        public TypeOfType getTypeOfType() {
            return typeOfType;
        }

        /**
         * @param mode the mode to set
         */
        public void setMode(CodeGenMode mode) {
            this.mode = mode;
        }

        /**
         * @param typeOfType the typeOfType to set
         */
        public void setTypeOfType(TypeOfType typeOfType) {
            this.typeOfType = typeOfType;
        }
    }
}
