/*
 * Rapid Beans Framework: PlatformHelper.java
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

package org.rapidbeans.core.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.logging.Logger;

import org.rapidbeans.core.exception.UtilException;

/**
 * Operating system recognition and operating system specific services. Could
 * also contain some detection for the virtual platform later on.
 * 
 * @author Martin Bluemel
 */
public final class PlatformHelper {

	private static OperatingSystem os = null;

	private static OperatingSystemFamily osFamily = null;

	private static Version osVersion = null;

	private static String hostname = null;

	private static final Logger log = Logger.getLogger(PlatformHelper.class.getName());

	public static Logger getLogger() {
		return log;
	}

	/**
	 * @return the current operating system family
	 */
	public static OperatingSystemFamily getOsfamily() {
		if (os == null) {
			determineSystem();
		}
		return osFamily;
	}

	/**
	 * @return the current operating system
	 */
	public static OperatingSystem getOs() {
		if (os == null) {
			determineSystem();
		}
		return os;
	}

	/**
	 * @return the current operating system's version
	 */
	public static Version getOsVersion() {
		if (os == null) {
			determineSystem();
		}
		return osVersion;
	}

	/**
	 * @return the operation system name
	 */
	public static String getOsName() {
		return System.getProperty("os.name");
	}

	/**
	 * @return the processor architecture
	 */
	public static String getArchName() {
		return System.getProperty("os.arch");
	}

	/**
	 * @return the operation system specific line feed string.
	 */
	public static String getLineFeed() {
		if (os == null) {
			determineSystem();
		}
		switch (osFamily) {
		case windows:
			return "\r\n";
		default:
			return "\n";
		}
	}

	/**
	 * Determines the host name via system command 'hostname' available on every
	 * Unix, Windows and hopefully also Mac.
	 * 
	 * @return the host name
	 */
	public static String hostname() {
		if (hostname == null) {
			determineHostname();
		}
		return hostname;
	}

	/**
	 * @return the current user name from Java system property user.name
	 */
	public static String username() {
		return System.getProperty("user.name");
	}

	/**
	 * @return the current user's home directory from Java system property user.home
	 */
	public static String userhome() {
		return System.getProperty("user.home");
	}

	/**
	 * @return the current working directory from Java system property user.dir
	 */
	public static String currentWorkingDirectory() {
		return System.getProperty("user.dir");
	}

	private static String syspropOsName = null;

	private static String syspropOsVersion = null;

	/**
	 * Package private test helper.
	 */
	static void reset(final String osName, final String osVersion) {
		syspropOsName = osName;
		syspropOsVersion = osVersion;
		os = null;
		hostname = null;
	}

	private static void determineSystem() throws AssertionError {
		if (syspropOsName == null) {
			syspropOsName = System.getProperty("os.name");
		}
		if (syspropOsVersion == null) {
			syspropOsVersion = System.getProperty("os.version");
		}
		log.info("OS name = \"" + syspropOsName + "\"");
		osVersion = new Version(syspropOsVersion);
		log.info("OS version = \"" + osVersion + "\"");
		if (syspropOsName.equals("Linux")) {
			osFamily = OperatingSystemFamily.linux;
			os = OperatingSystem.linux;
		} else if (syspropOsName.startsWith("Windows")) {
			osFamily = OperatingSystemFamily.windows;
			if (syspropOsName.endsWith("XP")) {
				os = OperatingSystem.windows_xp;
			} else if (syspropOsName.endsWith("Vista")) {
				os = OperatingSystem.windows_vista;
			} else if (syspropOsName.endsWith("7")) {
				os = OperatingSystem.windows_7;
			} else if (syspropOsName.endsWith("8")) {
				os = OperatingSystem.windows_8;
			} else if (syspropOsName.endsWith("10")) {
				os = OperatingSystem.windows_10;
			} else {
				// fallback for older Windows
				if (osVersion.compareTo(new Version("5.1")) < 0) {
					os = OperatingSystem.windows_unknown_old;
				} else if (osVersion.compareTo(new Version("8")) > 0) {
					os = OperatingSystem.windows_unknown_new;
				} else {
					throw new AssertionError("Unable to handle Windows operating system \"" + syspropOsName + "\"");
				}
			}
		} else if (syspropOsName.startsWith("Mac")) {
			osFamily = OperatingSystemFamily.mac;
			os = OperatingSystem.mac;
		} else {
			throw new AssertionError("Unknown OS \"" + syspropOsName + "\"");
		}
	}

	private static void determineHostname() {
		LineNumberReader reader = null;
		try {
			final Process proc = Runtime.getRuntime().exec("hostname");
			proc.waitFor();
			reader = new LineNumberReader(new InputStreamReader(proc.getInputStream()));
			hostname = reader.readLine().trim();
		} catch (InterruptedException e) {
			throw new UtilException(e);
		} catch (IOException e) {
			throw new UtilException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new UtilException(e);
				}
			}
		}
	}
}
