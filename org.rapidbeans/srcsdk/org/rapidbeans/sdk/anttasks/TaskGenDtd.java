/*
 * Rapid Beans Framework, SDK, Ant Tasks: TaskGenDtd.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 11/05/2010
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

package org.rapidbeans.sdk.anttasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.type.PropertyXmlBindingType;
import org.rapidbeans.core.type.TypeProperty;
import org.rapidbeans.core.type.TypePropertyBoolean;
import org.rapidbeans.core.type.TypePropertyChoice;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.PlatformHelper;

/**
 * the gen classes task.
 *
 * @author Martin Bluemel
 */
public final class TaskGenDtd extends Task {

    private static final String LF = PlatformHelper.getLineFeed();

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
     * the (root) bean type to analyze
     */
    private String type = null;

    /**
     * the (root) bean type to analyze.
     *
     * @param type the (root) directory to analyze
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * the output DTD file
     */
    private File dtd = null;
    
    /**
     * determine the DTD file to generate
     *
     * @param dtd the DTD file to generate
     */
    public void setDtd(final File dtd) {
        this.dtd = dtd;
    }

    /**
     * The model root directory (folder).
     */
    private String modelroot = null;
    
    /**
     * Determine the input model root directory (folder).
     * The model files are only used for modification date check.
     *
     * @param modelroot the model root directory (folder).
     */
    public void setModelroot(final String modelroot) {
        this.modelroot = modelroot;
    }

    /**
     * the execute method.
     */
    public void execute() {

        // checks for attributes
        if (this.type == null) {
            throw new BuildException("No bean type to analyze defined."
                    + " Please define value for attribute \"type\".");
        }

        final TypeRapidBean rootType = TypeRapidBean.forName(this.type);

        if (this.dtd == null) {
            throw new BuildException("No DTD file defined. Please define value for attribute \"dtd\".");
        }
        if (!this.dtd.getParentFile().exists()) {
            if (!this.dtd.getParentFile().mkdirs()) {
                throw new BuildException("Problems to create directory \""
                        + this.dtd.getParentFile() + "\".");
            }
        }

        if (this.modelroot == null) {
            getProject().log("     [gendtd]   no modelroot defined, force = true",
                    Project.MSG_VERBOSE);
            force = true;
        }

        final File modelrootDir = new File(this.modelroot);
        getProject().log("     [gendtd]   modelroot: "
                + this.modelroot, Project.MSG_DEBUG);
        getProject().log("     [gendtd]   modelroot path: "
                + modelrootDir.getPath(), Project.MSG_DEBUG);
        getProject().log("     [gendtd]   modelroot absolute path: "
                + modelrootDir.getAbsolutePath(), Project.MSG_DEBUG);

        if (!modelrootDir.exists()) {
            throw new BuildException("Model root directory \""
                    + this.modelroot + "\" not found");
        }
        if (!modelrootDir.isDirectory()) {
            throw new BuildException("Invalid model root directory. File \""
                    + this.modelroot + " is not a directory");
        }

        long dirLatestModified = 0;
        if (!this.force) {
            dirLatestModified = getLatestModificationDirDate(
                    modelrootDir, rootType, 0);
        }

        if (!this.dtd.exists()
                || this.force
                || dirLatestModified > this.dtd.lastModified()
                ) {
            this.getProject().log(
                    "     [gendtd] Processing bean type "
                    + this.type
                    + " to " + this.dtd.getAbsolutePath(),
                    Project.MSG_INFO);
            OutputStreamWriter writer = null;
            try {
                writer = new OutputStreamWriter(new FileOutputStream(this.dtd), "UTF-8");
                generateDTD(rootType, writer, this.getProject());
            } catch (IOException e) {
                throw new BuildException(e);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        throw new BuildException(e);
                    }
                }
            }
        }
    }

    /**
     * Generate the DTD recursively
     * @param typename the type to process
     * @param writer the writer
     * @param project the ant project
     */
    protected static void generateDTD(final TypeRapidBean type,
            final Writer writer, final Project project) {
        generateDTD(type, null, new HashMap<String, Object>(), writer, project);
    }

    /**
     * Generate the DTD recursively
     * @param typename the type to process
     * @param xmlname the XML name
     * @param alreadydefined the map storing XML element / type combinations
     *                               already described so far.
     * @param writer the writer
     * @param project the ant project
     */
    private static void generateDTD(final TypeRapidBean type,
            final String xmlname,
            final Map<String, Object> alreadydefined,
            final Writer writer,
            final Project project) {

        try {
            // write element name
            String elementname = xmlname;
            if (elementname == null) {
                elementname = type.getXmlRootElement();
                if (elementname == null) {
                    throw new BuildException("No XML root element definition for bean type \""
                            + type.getName() + "\"");
                }
            }

            writer.write("<!ELEMENT " + elementname + " ");
            alreadydefined.put(elementname, type);

            // write sub element list
            final List<TypePropertyCollection> colProptypes =
                type.getColPropertyTypes();
            int proptypesWithElementXmlBindingCount = 0;
            for (final TypeProperty proptype : type.getPropertyTypes()) {
                if (proptype.getXmlBindingType() == PropertyXmlBindingType.element) {
                    proptypesWithElementXmlBindingCount++;
                }
            }
            if (colProptypes.size() == 0
                    && proptypesWithElementXmlBindingCount == 0) {
                writer.write("EMPTY>");
            } else {
                writer.write("(#PCDATA");
                for (final TypeProperty proptype : type.getPropertyTypes()) {
                    if (proptype.getXmlBindingType() == PropertyXmlBindingType.element) {
                        writer.write(" | ");
                        writer.write(proptype.getPropName());                   
                    }
                }
                for (final TypePropertyCollection colPropType : colProptypes) {
                    if (colPropType.isComposition() && (!colPropType.isTransient())) {
                        for (final String subelementname : getSubelementNames(colPropType, type)) {
                            writer.write(" | ");
                            writer.write(subelementname);
                        }
                    }
                }
                writer.write(")*>");
            }
            writer.write(LF);

            // count attributes
            int attrcount = 0;
            if (type.getPropertyTypes().size() > colProptypes.size()) {
                for (final TypeProperty proptype : type.getPropertyTypes()) {
                    if (proptype.isTransient()
                            || proptype.getXmlBindingType() == PropertyXmlBindingType.element) {
                        continue;
                    }
                    if ((!(proptype instanceof TypePropertyCollection))
                            || (!((TypePropertyCollection) proptype).isComposition())) {
                        attrcount++;
                    }
                }
            }

            // write attribute list
            if (attrcount > 0) {
                writer.write("<!ATTLIST " + elementname + LF);
                for (final TypeProperty proptype : type.getPropertyTypes()) {
                    if (proptype.isTransient()
                            || proptype.getXmlBindingType() == PropertyXmlBindingType.element) {
                        continue;
                    }
                    if (proptype instanceof TypePropertyBoolean) {
                        writer.write("\t" + proptype.getPropName() + " (false | true)");
                        if (proptype.getMandatory()) {
                            writer.write(" #REQUIRED");
                        } else {
                            writer.write(" #IMPLIED");
                        }
                        writer.write(LF);
                    } else if ((!(proptype instanceof TypePropertyCollection))
                            || (!((TypePropertyCollection) proptype).isComposition())) {
                        writer.write("\t" + proptype.getPropName() + " ");
                        if (proptype instanceof TypePropertyChoice) {
                            writer.write("(");
                            int i = 0;
                            for (final RapidEnum emumElement :
                                ((TypePropertyChoice) proptype).getEnumType().getElements()) {
                                if (i > 0) {
                                    writer.write(" | ");
                                }
                                writer.write(emumElement.name());
                                i++;
                            }
                            writer.write(")");
                        } else {
                            writer.write("CDATA");
                        }
                        if (proptype.getMandatory()) {
                            writer.write(" #REQUIRED");
                        } else {
                            writer.write(" #IMPLIED");
                        }
                        writer.write(LF);
                    }
                }
                writer.write(">" + LF);
            }

            writer.write(LF);

            // iterate over attributes serialized as XML element
            for (final TypeProperty proptype : type.getPropertyTypes()) {
                if (proptype.getXmlBindingType() == PropertyXmlBindingType.element) {
                    if (alreadydefined.get(proptype.getPropName()) == null) {
                        writer.write("<!ELEMENT ");
                        writer.write(proptype.getPropName());
                        alreadydefined.put(proptype.getPropName(), proptype);
                        writer.write(" (#PCDATA)*>");
                        writer.write(LF);
                        writer.write(LF);
                    }
//                    } else if (alreadydefined.get(proptype.getPropName()) != proptype) {
//                        if (alreadydefined.get(proptype.getPropName()) instanceof TypeRapidBean) {
//                            throw new BuildException("XML Element \"" + proptype.getPropName()
//                                    + "\" already used for bean type \"" + proptype.getName() + "\"");
//                        } else {
//                            throw new BuildException("XML Element \"" + proptype.getPropName()
//                                    + "\" already used for property type \"" + proptype.getPropName() + "\"");
//                        }
//                    }
                }
            }

            // recurse over composition associated types
            for (final TypePropertyCollection colPropType : colProptypes) {
                if (!colPropType.isComposition()
                        || colPropType.isTransient()) {
                    continue;
                }
                for (final String subelementname : getSubelementNames(colPropType, type)) {
                    TypeRapidBean targetType = colPropType.getTargetType();
                    if (type.getXmlElementsTypeMap() != null
                            && type.getXmlElementsTypeMap().get(subelementname) != null) {
                        targetType = type.getXmlElementsTypeMap().get(subelementname);
                    }
                    if (alreadydefined.get(subelementname) == null) {
                        generateDTD(targetType, subelementname, alreadydefined, writer, project);
                    } else if (alreadydefined.get(subelementname) != targetType) {
                        if (alreadydefined.get(subelementname) instanceof TypeRapidBean) {
                            throw new BuildException("XML Element \"" + subelementname
                                    + "\" already used for bean type \""
                                    + ((TypeRapidBean) alreadydefined.get(subelementname) ).getName()
                                    + "\"");
                        } else {
                            throw new BuildException("XML Element \"" + subelementname
                                    + "\" already used for property type \""
                                    + ((TypeProperty) alreadydefined.get(subelementname) ).getPropName()
                                    + "\"");
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    /**
     * @param colPropType
     * @return
     */
    private static List<String> getSubelementNames(final TypePropertyCollection colPropType,
            final TypeRapidBean type) {
        final List<String> subelementNames = new ArrayList<String>();
        if (type.getXmlElements() != null && type.getXmlElements().size() > 0) {
            for (final Entry<String, TypeProperty> entry : type.getXmlElements().entrySet()) {
                if (colPropType == entry.getValue()) {
                    subelementNames.add(entry.getKey());
                }
            }
        }
        if (subelementNames.size() == 0) {
            String subelementName = colPropType.getPropName();
            if (colPropType.getMaxmult() != 1
                    || subelementName.endsWith("s")) {
                subelementName = subelementName.substring(0, subelementName.length() - 1);
            }
            subelementNames.add(subelementName);
        }
        return subelementNames;
    }

    /**
     * iterates recursively over a complete directory tree
     * and determines the newest (latest) modification date
     * of all the directories contained in the file system
     * hierarchy.
     */
    protected static long getLatestModificationDirDate(
            final File dir, final TypeRapidBean type, final long latest) {
        long lastModified = latest;
        final File typefile = new File(dir, type.getName().replace('.', '/'));
        if (typefile.lastModified() > lastModified) {
            lastModified = typefile.lastModified();
        }

        // recurse over composition associated types
        for (final TypePropertyCollection colPropType : type.getColPropertyTypes()) {
            if (!colPropType.isComposition()
                    || colPropType.isTransient()) {
                continue;
            }
            for (final String subelementname : getSubelementNames(colPropType, type)) {
                if (type.getXmlElementsTypeMap() != null
                        && type.getXmlElementsTypeMap().get(subelementname) != null) {
                    lastModified = getLatestModificationDirDate(dir,
                            type.getXmlElementsTypeMap().get(subelementname), lastModified);
                } else if (!(colPropType.getTargetType().equals(type))) {
                    lastModified = getLatestModificationDirDate(dir,
                            colPropType.getTargetType(), lastModified);
                }
            }
        }

        return lastModified;
    }
}
