/*
 * Rapid Beans Framework, SDK, Ant Tasks: CompareResourcePropertyFiles.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 02/05/2009
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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * This Ant task compares two or more resource property files
 * compares every file with the other ones and generates missing
 * expressions to the other ones optionally with the language of
 * your choice (if defined) and a big to do remark.
 * All properties in all files are written in alphabetical order.
 *
 * @author Martin Bluemel
 */
public final class CompareResourcePropertyFiles extends Task {

    /**
     * the common filename part of all resource property files.
     */
    private String file = null;

    /**
     * the parent folder.
     */
    private File dir = null;

    /**
     * Let the build fail if there is a todo.
     */
    private boolean failontodo = false;

//    /**
//     * the preferred language in which to write empty resource properties.
//     */
//    private String lang = null;

    /**
     * @param file the file to set
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * @param dir the dir to set
     */
    public void setDir(File dir) {
        this.dir = dir;
    }

//    /**
//     * @param lang the lang to set
//     */
//    public void setLang(String lang) {
//        this.lang = lang;
//    }

    /**
     * The execute method has to be implemented from every Ant task.
     */
    public void execute() {
        boolean todo = false;
        try {
            final List<File> filesList = findFiles(this.dir);
            final Properties[] propsArray = new Properties[filesList.size()];
            final boolean[] todosArray = new boolean[filesList.size()];
            int n = 0;
            for (File file : filesList) {
                final Properties props = new Properties();
                final FileInputStream fis = new FileInputStream(file);
                props.load(fis);
                propsArray[n] = props;
                todosArray[n] = false;
                fis.close();
                n++;
            }
            for (int i = 0; i < propsArray.length; i++) {
                final Properties props = propsArray[i];
                for (final Object o : props.keySet()) {
                    final String key = (String) o;
                    for (int j = 0; j < propsArray.length; j++) {
                        if (i != j) {
                            final Properties otherProps = propsArray[j];
                            if (otherProps.getProperty(key) == null) {
                                final String value =
                                    "TODO: TRANSLATE#" + props.getProperty(key);
                                otherProps.setProperty(key, value);
                                this.getProject().log("ERROR in File: " + filesList.get(j).getAbsolutePath() + ":\n"
                                        + "  property \"" + key + "\" is not defined.");
                                todo = true;
                                todosArray[j] = true;
                            }
                        }
                    }
                }
                for (final Object o : props.keySet()) {
                    if ((props.getProperty((String) o)).startsWith("TODO: TRANSLATE#")) {
                        this.getProject().log("ERROR in File: " + filesList.get(i).getAbsolutePath() + ":\n"
                                + "  Todo left for property \"" + o + "\".");
                        todo = true;
                        todosArray[i] = true;
                    }
                }
            }

            if (todo) {
                final String newline = PlatformHelper.getLineFeed();
                int i = 0;
                for (File file : filesList) {
                    if (!todosArray[i]) {
                        this.getProject().log("file OK: " + file.getAbsolutePath());
                        i++;
                        continue;
                    }
                    final List<String> keyList = new ArrayList<String>();
                    for (final Object key : propsArray[i].keySet()) {
                        keyList.add((String) key);
                    }
                    Collections.sort(keyList);
                    this.getProject().log("rewiriting file " + file.getAbsolutePath() + "...");
                    final FileWriter ow = new FileWriter(file);
                    for (final String key : keyList) {
                        ow.write(key);
                        ow.write("=");
                        ow.write(escape(propsArray[i].getProperty(key)));
                        ow.write(newline);
                    }
                    ow.close();
                    i++;
                }
            }

            if (this.failontodo && todo) {
                throw new BuildException("Please fix TODOs in resource properties.");
            }
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    private List<File> findFiles(final File folder) {
        final ArrayList<File> files = new ArrayList<File>();
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                files.addAll(findFiles(file));
            } else {
                if (file.getName().equals(this.file)) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    private String escape(final String s) {
        final StringBuffer sb = new StringBuffer();
        final int len = s.length();
        for (int i = 0; i < len; i++) {
            final char c = s.charAt(i);
            if (c == '\n') {
                sb.append("\\n");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public void setFailontodo(boolean failontodo) {
        this.failontodo = failontodo;
    }
}
