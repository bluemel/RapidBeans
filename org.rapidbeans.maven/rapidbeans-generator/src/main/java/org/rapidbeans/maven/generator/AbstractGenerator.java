/*
 * Rapid Beans Framework, SDK, Maven Plugin: AbstractGeneratorWorker.java
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
package org.rapidbeans.maven.generator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;
import org.rapidbeans.maven.exceptions.BuildException;

/**
 * Abstract Class for all generators that provides common access to same
 * properties.<br/>
 * <br/>
 * 
 * @author Mischur.Alexander
 */
public abstract class AbstractGenerator {

    public static final String FILE_ENDING_XML = ".xml";

    public static final String FILE_ENDING_JAVA = ".java";

    public static final int FILE_ENDING_XML_LENGTH = FILE_ENDING_XML.length();

    public static final int FILE_ENDING_JAVA_LENGTH = FILE_ENDING_JAVA.length();

    protected final Log log;

    /**
     * force flag.
     */
    private final boolean force;

    /**
     * the model model description's directory.
     */
    private final File srcdir;

    /**
     * the generated source's directory.
     */
    private final File destdirsimple;

    /**
     * the generated source's directory.
     */
    private final File destdirjoint;

    public AbstractGenerator(File srcdir, File destdirsimple, File destdirjoint, Log log) {
        this(srcdir, destdirsimple, destdirjoint, false, log);
    }

    public AbstractGenerator(File srcdir, File destdirsimple, File destdirjoint, boolean force, Log log) {

        // set srcdir when valid
        if (srcdir == null) {
            throw new BuildException("No Source directory. Please define value for attribute \"srcdir\".");
        }
        if (!srcdir.exists()) {
            throw new BuildException("Source directory \"" + srcdir + " not found");
        }
        if (!srcdir.isDirectory()) {
            throw new BuildException("Invalid source directory. File \"" + srcdir + " is not a directory");
        }
        this.srcdir = srcdir;

        // set destdirsimple when valid
        if (destdirsimple == null) {
            throw new BuildException("No Destination directory. Please define value for attribute \"destdir\".");
        }
        if (!destdirsimple.exists()) {
            try {
                FileUtils.forceMkdir(destdirsimple);
            } catch (IOException e) {
                throw new BuildException("Destination directory \"" + destdirsimple
                        + " does not exists and could not be created!", e);
            }
        }
        if (!destdirsimple.isDirectory()) {
            throw new BuildException("Invalid destination directory. File \"" + destdirsimple
                    + " is not a directory");
        }
        if (!destdirsimple.exists()) {
            throw new BuildException("Destination directory \"" + destdirsimple + " not found");
        }
        this.destdirsimple = destdirsimple;

        // set destdirjoint when valid
        if (destdirjoint == null) {
            throw new BuildException("No Destination directory. Please define value for attribute \"destdirjoint\".");
        }
        if (!destdirjoint.exists()) {
            try {
                FileUtils.forceMkdir(destdirjoint);
            } catch (IOException e) {
                throw new BuildException("Destination directory \"" + destdirjoint
                        + " does not exists and could not be created!", e);
            }
        }
        if (!destdirjoint.isDirectory()) {
            throw new BuildException("Invalid destination directory. File \"" + destdirjoint
                    + " is not a directory");
        }
        if (!destdirjoint.exists()) {
            throw new BuildException("Destination directory \"" + destdirjoint + " not found");
        }
        this.destdirjoint = destdirjoint;

        this.force = force;
        this.log = log;
    }

    abstract public void execute() throws BuildException;

    protected boolean isForce() {
        return this.force;
    }

    protected File getSrcdir() {
        return this.srcdir;
    }

    protected File getDestdirsimple() {
        return this.destdirsimple;
    }

    protected File getDestdirjoint() {
        return this.destdirjoint;
    }
}
