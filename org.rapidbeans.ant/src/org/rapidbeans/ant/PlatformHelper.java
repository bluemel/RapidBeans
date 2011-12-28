/*
 * Rapid Beans Framework, SDK, Ant Tasks: PlatformHelper.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 12/07/2008
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.apache.tools.ant.BuildException;

/**
 * Operating system recognition and
 * operating system specific services.
 * Could also contain some detection for
 * the virtual platform later on.
 *
 * @author Martin Bluemel
 */
public final class PlatformHelper {

    private static OperatingSystem os = null;

    private static Version osVersion = null;

    /**
     * @return the current operating system
     */
    public static OperatingSystem getOs() {
        return os;
    }

    /**
     * @return the current operating system's version
     */
    public static Version getOsVersion() {
        return osVersion;
    }

    /**
     * @return the operation system name
     */
    public static String getOsName(){
        return System.getProperty("os.name");
    }

    // initializer
    static {
        final String osName = System.getProperty("os.name");
        osVersion = new Version(System.getProperty("os.version"));
        if (osName.equals("Linux")) {
            os = OperatingSystem.linux;
        } else if (osName.startsWith("Windows")) {
            os = OperatingSystem.windows;
        } else if (osName.startsWith("Mac")) {
            os = OperatingSystem.mac;
        }
    }

    /**
     * @return the operation system specific line feed string.
     */
    public static String getLineFeed() {
        switch (os) {
        case windows: return "\r\n";
        default: return "\n";
        }
    }

    /**
     * Determines the host name via system command 'hostname'
     * available on every Unix, Windows and hopefully also Mac.
     *
     * @return the host name
     */
    public static String hostname() {
        LineNumberReader reader = null;
        try {
            final Process proc = Runtime.getRuntime().exec("hostname");
            proc.waitFor();
            reader = new LineNumberReader(new InputStreamReader(proc.getInputStream()));
            return reader.readLine().trim();
        } catch (InterruptedException e) {
            throw new BuildException(e);
        } catch (IOException e) {
            throw new BuildException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new BuildException(e);
                }
            }
        }
    }

    /**
     * @return the current user name from system property user.name
     */
    public static String username(){
        String str = System.getProperty("user.name");
        return str;
    }
}
