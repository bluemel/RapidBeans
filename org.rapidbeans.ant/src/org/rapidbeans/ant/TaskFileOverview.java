/*
 * Rapid Beans Framework, SDK, Ant Tasks: TaskFileOverview.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 11/03/2010
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * Generates an XML file containing a list of folders and files that might be used
 * as an input for an XSLT transformation step.
 *
 * @author Martin Bluemel
 */
public final class TaskFileOverview extends Task {

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
     * absolute flag.
     */
    private boolean absolute = false;

    /**
     * set the absolute flag which determines if absolute paths should
     * be generated.
     * @param argForce determines if the generation should be performed
     *                 not regarding modification dates
     */
    public void setAbsolute(final boolean absolute) {
        this.absolute = absolute;
    }

    /**
     * exclude dot directories flag.
     */
    private boolean excludedotdirs = true;

    /**
     * set the exclude dot directories flag which determines
     * if the directories starting with '.' (e. g. .svn) character should
     * be excluded.
     *
     * @param exclude determines if the generation should be performed
     *                 not regarding modification dates
     */
    public void setExcludedotdirs(final boolean exclude) {
        this.excludedotdirs = exclude;
    }

    /**
     * the (root) directory to analyze
     */
    private String dir = null;
    
    /**
     * set the (root) directory to analyze.
     *
     * @param the (root) directory to analyze
     */
    public void setDir(final String dir) {
        this.dir = dir;
    }

    /**
     * the (root) directory to analyze
     */
    private File out = null;
    
    /**
     * set the file to generate
     *
     * @param the file to generate
     */
    public void setOut(final File out) {
        this.out = out;
    }

    /**
     * The depth to analyze the file system tree.
     */
    private int depth = -1;
    
    /**
     * set the depth.
     *
     * @param depth the depth to set
     */
    public void setDepth(final int depth) {
        this.depth = depth;
    }

    /**
     * the execute method.
     */
    public void execute() {

        final File rootDir = new File(this.dir);
        getProject().log("     [fileoverview]   dir: "
                + this.dir, Project.MSG_VERBOSE);
        getProject().log("     [fileoverview]   path: "
                + rootDir.getPath(), Project.MSG_VERBOSE);
        getProject().log("     [fileoverview]   absolute path: "
                + rootDir.getAbsolutePath(), Project.MSG_VERBOSE);

        // checks for attributes
        if (this.dir == null) {
            throw new BuildException("No directory defined. Please define value for attribute \"dir\".");
        }
        if (!rootDir.exists()) {
            throw new BuildException("Directory \"" + this.dir + "\" not found");
        }
        if (!rootDir.isDirectory()) {
            throw new BuildException("Invalid directory. File \"" + this.dir + " is not a directory");
        }
        if (this.out == null) {
            throw new BuildException("No out file defined. Please define value for attribute \"out\".");
        }
        if (!this.out.getParentFile().exists()) {
            if (!this.out.getParentFile().mkdirs())
            throw new BuildException("Problems to create directory \"" + this.out.getParentFile() + "\".");
        }
        long dirLatestModified = 0;
        if (!this.force) {
            dirLatestModified = getLatestModificationDirDate(rootDir, this.depth);
        }
        if (!this.out.exists()
                || this.force
                || dirLatestModified > this.out.lastModified()) {
            this.getProject().log(
                    "     [fileoverview] Processing directory "
                    + rootDir.getAbsolutePath()
                    + " to " + this.out.getAbsolutePath(),
                    Project.MSG_INFO);
            this.getProject().log("     [fileoverview]   depth = " + this.depth + "\n"
                    + "     [fileoverview]   absolute = " + this.absolute + "\n"
                    + "     [fileoverview]   excludedotdirs = " + this.excludedotdirs,
                    Project.MSG_VERBOSE);
            OutputStreamWriter writer = null;
            try {
                writer = new OutputStreamWriter(new FileOutputStream(this.out), "UTF-8");
                generateFolderAndFilesXML(rootDir, this.depth, 0, writer,
                        this.absolute, this.excludedotdirs, this.getProject());
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
     * Generate a folder and files XML list starting from the given directory / folder.
     * @param dir the starting point
     * @param maxdepth the maximal depth. Give -1 if depth is unlimited
     * @param depth the current depth for recursion. Give in zero here.
     * @param writer the write to write the list into
     * @param absolute
     * @param excludedotdirs
     */
    protected static void generateFolderAndFilesXML(final File dir, final int maxdepth, final int depth,
            final Writer writer, final boolean absolute, final boolean excludedotdirs,
            final Project project) {

        try {
            if (depth == 0) {
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + LF);
            }

            final StringBuffer indent = new StringBuffer();
            for (int i = 0; i < depth; i++) {
                indent.append('\t');
            }

            String foldername = dir.getPath();
            if (absolute) {
                foldername = dir.getAbsolutePath();
            }
            writer.write(indent + "<folder path=\""
                    + foldername.replace(File.separatorChar, '/')
                    + "\">" + LF);

            final int depth1 = depth + 1;
            final StringBuffer indent1 = new StringBuffer();
            for (int i = 0; i < depth1; i++) {
                indent1.append('\t');
            }

            for (final File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    if ((maxdepth > depth || maxdepth < 0)
                            && (!excludedotdirs || !file.getName().startsWith("."))) {
                        generateFolderAndFilesXML(file, maxdepth, depth1, writer, absolute, excludedotdirs, project);
                    }
                } else {
                    writer.write(indent1 + "<file name=\""
                            + file.getName()
                            + "\"/>" + LF);
                }
            }

            writer.write(indent + "</folder>" + LF);

        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    /**
     * iterates recursively over a complete directory tree
     * and determines the newest (latest) modification date
     * of all the directories contained in the file system
     * hierarchy.
     *
     * @param dir directory the directory / folder to analyze,
     * @param depth the depth
     *
     * @throws BuildException
     */
    protected static long getLatestModificationDirDate(final File dir, final int depth) {
        long lastModified = dir.lastModified();
        if (depth > 0 || depth < 0) {
            File[] files = dir.listFiles();
            for (final File file : files) {
                if (file.isDirectory()) {
                    final long lastModifiedTest =
                        getLatestModificationDirDate(file, depth - 1);
                    if (lastModifiedTest > lastModified) {
                        lastModified = lastModifiedTest;
                    }
                }
            }
        }
        return lastModified;
    }
}
