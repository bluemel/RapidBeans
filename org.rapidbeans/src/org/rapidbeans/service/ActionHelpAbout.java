/*
 * Rapid Beans Framework: ActionHelpAbout.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/01/2007
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

package org.rapidbeans.service;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.swing.JFrame;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.exception.UtilException;
import org.rapidbeans.core.util.ManifestReader;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;

/**
 * Pops up the usual "About" dialog-
 * This dialog usually shows the applcation's version,
 * the vendor, the developers, etc...
 * 
 * @author Martin Bluemel
 */
public class ActionHelpAbout extends Action {

	/**
	 * the execute method.
	 */
	public void execute() {
		Application client = ApplicationManager.getApplication();
		if (client != null) {
			client.messageInfo(getInfo(),
					client.getCurrentLocale().getStringGui("messagedialog.about.title"));
		}
	}

	/**
	 * Compute the version info out of the manifest
	 * in a localized manner.
	 * 
	 * @param locale
	 *            the locale
	 */
	public String getInfo() {
		final Application client = ApplicationManager.getApplication();
		final RapidBeansLocale locale = client.getCurrentLocale();
		String versionString = client.getName();
		try {
			versionString = locale.getStringMessage("info.about.application");
		} catch (MissingResourceException e) {
			final String title = ((JFrame) client.getMainwindow().getWidget()).getTitle();
			if (title != null && title.length() > 0) {
				versionString = title;
			}
		}
		Manifest manifest;
		try {
			manifest = ManifestReader.readManifestFromJarOfClass(client.getClass());
			versionString += "\n";
			versionString += locale.getStringMessage("info.about.authors");
			versionString += manifest.getMainAttributes().getValue("Created-By");
			versionString += "\n\n";
			versionString += locale.getStringMessage("info.about.version.program");
			versionString += manifest.getMainAttributes().getValue("Implementation-Version");
			versionString += "\n";
			versionString += locale.getStringMessage("info.about.version.program.build.number");
			versionString += manifest.getMainAttributes().getValue("Implementation-Build-Number");
			versionString += "\n";
			versionString += locale.getStringMessage("info.about.version.program.build.date");
			versionString += manifest.getMainAttributes().getValue("Implementation-Build-Date");
			versionString += "\n\n";
			versionString += locale.getStringMessage("info.about.version.components");
			ComponentDescr descr;
			String componentKey = null;
			Map<String, ComponentDescr> map = new HashMap<String, ComponentDescr>();
			for (Object o : manifest.getMainAttributes().keySet()) {
				final String attrKey = ((Attributes.Name) o).toString();
				if (attrKey.startsWith("Component-")) {
					if (attrKey.endsWith("-Name")) {
						componentKey = attrKey.substring(10, attrKey.length() - 5);
					} else if (attrKey.endsWith("-Package")) {
						componentKey = attrKey.substring(10, attrKey.length() - 8);
					} else if (attrKey.endsWith("-Version")) {
						componentKey = attrKey.substring(10, attrKey.length() - 8);
					}
					if (map.containsKey(componentKey)) {
						descr = map.get(componentKey);
					} else {
						descr = new ComponentDescr();
						map.put(componentKey, descr);
					}
					descr.setName(componentKey);
					if (attrKey.endsWith("-Name")) {
						descr.setDescription(manifest.getMainAttributes().getValue(attrKey));
					}
					if (attrKey.endsWith("-Package")) {
						descr.setPkg(manifest.getMainAttributes().getValue(attrKey));
					}
					if (attrKey.endsWith("-Version")) {
						descr.setVersion(manifest.getMainAttributes().getValue(attrKey));
					}
				}
			}
			for (ComponentDescr descr1 : map.values()) {
				versionString += "\n- " + descr1.getName() + ":";
				versionString += "\n    " + descr1.getDescription();
				versionString += "\n    " + locale.getStringMessage("info.about.version.version")
						+ descr1.getVersion();
			}
			versionString += "\n\n";
			versionString += locale.getStringMessage("info.about.version.java.build");
			versionString += manifest.getMainAttributes().getValue("Java-Version-Build");
			versionString += "\n";
			versionString += locale.getStringMessage("info.about.version.java.develop");
			versionString += manifest.getMainAttributes().getValue("Java-Version-Develop");
		} catch (UtilException e) {
			if (!(e.getCause() instanceof FileNotFoundException)) {
				throw e;
			}
		}
		versionString += "\n";
		versionString += locale.getStringMessage("info.about.version.java.current");
		versionString += System.getProperty("java.version");
		return versionString;
	}
}
