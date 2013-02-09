/*
 * Rapid Beans Framework, SDK, Ant Tasks: TaskSimpleFtp.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 09/05/2007
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
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * This Ant task implements a very simple JDK base FTP upload.
 *
 * @author Martin.Bluemel
 */
public final class TaskSimpleFtp extends Task {

    /**
     * the server.
     */
    private String server = null;

    /**
     * the user id.
     */
    private String userid = null;

    /**
     * the password.
     */
    private String password = null;

    /**
     * the remote directory.
     */
    private String remotedir = null;

    /**
     * the local directory.
     */
    private File localdir = null;

    /**
     * the stamp directory.
     */
    private File stampdir = null;

    /**
     * The execute method has to be implemented from every Ant task.
     */
    public void execute() {
        if (this.stampdir != null && !this.stampdir.exists()) {
            throw new BuildException("stampdir \"" + this.stampdir.getAbsolutePath()
                    + "\" does not exist.");
        }
        ftpAllFiles(this.localdir, this.remotedir, this.stampdir);
    }

    private void ftpAllFiles(final File localDir, final String remoteDir, final File stampDir) {
        OutputStream os = null;
        FileInputStream is = null;
        int c;
        this.getProject().setName("simpleftp");
        try {
            if (stampDir != null && !stampDir.exists()) {
                if (!stampDir.getParentFile().exists()) {
                    throw new BuildException("Stamp dir \""
                            + stampDir.getParentFile() + "\" does not exist.");
                }
                if (stampDir != null && !stampDir.mkdir()) {
                    throw new BuildException("Could not create stamp dir \""
                            + stampDir.getAbsolutePath() + "\"");                    
                }
            }
            for (File file : localDir.listFiles()) {
                if (file.isDirectory()) {
                    File newStampDir = null;
                    if (stampDir != null) {
                        newStampDir = new File(stampDir, file.getName());
                    }
                    ftpAllFiles(new File(localDir, file.getName()), remoteDir + "/" + file.getName(), newStampDir);
                } else {
                    File stampFile = null;
                    if (stampDir != null) {
                        stampFile = new File(stampDir, file.getName() + ".stamp");
                        if (stampFile.exists() && stampFile.lastModified() > file.lastModified()) {
                            this.getProject().log("skipping up to date file: " + file.getAbsolutePath(),
                                    Project.MSG_VERBOSE);
                            continue;
                        }
                        if (stampFile.exists()) {
                            stampFile.setLastModified(System.currentTimeMillis());
                        } else {
                            if (!stampFile.createNewFile()) {
                                throw new BuildException("Could not create stam file: "
                                        + stampFile.getAbsolutePath());
                            }
                        }
                    }
                    final URL url = new URL("ftp://" + this.userid + ":"
                            + this.password + "@" + this.server + "/"
                            + remoteDir + "/"
                            + file.getName()
                            + ";type=i");
                    final URLConnection urlc = url.openConnection();
                    os = urlc.getOutputStream();
                    this.getProject().log("writing file: " + file.getAbsolutePath() + " to\n  "
                            + url.toString().replaceFirst("ftp://" + this.userid + ":"
                                    + this.password + "@", ""), Project.MSG_INFO);
                    is = new FileInputStream(file);
                    while ((c = is.read()) != -1) {
                        os.write(c);
                    }
                    is.close();
                    is = null;
                    os.close();
                    os = null;
                }
            }
        } catch (Exception e) {
            throw new BuildException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new BuildException(e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    throw new BuildException(e);
                }
            }
        }
    }

    /**
     * @param server the server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @param userid the user id to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param remotedir the remote directory to set
     */
    public void setRemotedir(String remotedir) {
        this.remotedir = remotedir;
    }

    /**
     * @param localdir the local directory to set
     */
    public void setLocaldir(File localdir) {
        this.localdir = localdir;
    }

    /**
     * @param stampdir the stamp directory to set
     */
    public void setStampdir(File stampdir) {
        this.stampdir = stampdir;
    }
}
