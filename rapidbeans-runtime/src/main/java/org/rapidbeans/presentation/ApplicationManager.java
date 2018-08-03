/*
 * Rapid Beans Framework: ApplicationManager.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 09/14/2005
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

package org.rapidbeans.presentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.config.ConfigApplication;

/**
 * Controls one single application instance.
 * 
 * @author Martin Bluemel
 */
public abstract class ApplicationManager {

	/**
	 * the single application instance.
	 */
	private static Application application = null;

	/**
	 * @return the application instance
	 */
	public static Application getApplication() {
		return application;
	}

	/**
	 * setter, intended for internal use. Handle with care!
	 * 
	 * @param app the application to manage
	 */
	protected static void setApplication(final Application app) {
		application = app;
	}

	/**
	 * for test reasons.
	 */
	public static void resetApplication() {
		if (application != null) {
			if (application.getMainwindow() != null) {
				application.getMainwindow().close();
			}
			application = null;
		}
	}

	/**
	 * creates an Application and configures it with data from the file specified by
	 * appConfigFilePath.
	 * 
	 * @param appConfigFilePath specifies the application config file
	 */
	public static void start(final String appConfigFilePath) {
		start(null, appConfigFilePath, null);
	}

	/**
	 * creates a Application and configures it with data from the file specified by
	 * appConfigFilePath.
	 * 
	 * @param rootPackageClass  a class located in the app's root package. This
	 *                          class must be used for correct resource loading in
	 *                          case of a remote app (Applet or Java WebStart
	 *                          application).
	 * @param appConfigFilePath the path including the file name that specifies the
	 *                          config file relatively to the app's root package
	 * @param app               the applet or application
	 * @param options           the options
	 */
	public static void start(final Class<?> rootPackageClass, final String appConfigFilePath, final Appl appl) {
		Application newApp = null;
		InputStream is = null;
		URL url = null;

		// 1st try load a local file
		final File configFile = new File(appConfigFilePath);
		if (configFile.exists()) {
			try {
				is = new FileInputStream(configFile);
				try {
					url = configFile.toURI().toURL();
				} catch (MalformedURLException e) {
					throw new RapidBeansRuntimeException(e);
				}
			} catch (FileNotFoundException e) {
				throw new RapidBeansRuntimeException(e);
			}
		}

		// 2nd try load as resource from the jar either
		// - via class loader as system resource (local application) or
		// - from the same jar where the rootPackageClass comes from
		// relatively to this classes package (remote application)
		final ResourceLoader resourceLoader = new ResourceLoader();
		if (is == null) {
			resourceLoader.addRootPackageClass(rootPackageClass);
			is = resourceLoader.getResourceAsStream(appConfigFilePath);
			url = resourceLoader.getResource(appConfigFilePath);
		}

		if (is == null) {
			throw new RapidBeansRuntimeException("Application configuration file \"" + appConfigFilePath
					+ "\" not found as local file and in classpath");
		}

		Document appConfigDoc = null;
		if (RapidBeansTypeLoader.getInstance().getXmlRootElementBinding("applicationcfg") != null) {
			appConfigDoc = new Document("applicationcfg", null, url, is);
		} else {
			appConfigDoc = new Document("applicationcfg",
					TypeRapidBean.forName("org.rapidbeans.presentation.config.ConfigApplication"), url, is);
		}
		final ConfigApplication config = (ConfigApplication) appConfigDoc.getRoot();
		if (appl != null && appl instanceof Application) {
			newApp = (Application) appl;
		} else {
			try {
				newApp = (Application) determineApplicationClass(appConfigFilePath, config).newInstance();
			} catch (InstantiationException e) {
				throw new RapidBeansRuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RapidBeansRuntimeException(e);
			}
		}
		newApp.setConfiguration(config);
		newApp.setConfigFilePath(appConfigFilePath);
		newApp.setResourceLoader(resourceLoader);
		if (appl instanceof JApplet) {
			newApp.setApplet((JApplet) appl);
		}
		start(newApp);
	}

	/**
	 * starts the application.
	 * 
	 * @param app     the application instance
	 * @param options the options
	 */
	public static void start(final Application app) {
		application = app;
		if (app.getConfiguration() == null) {
			app.initSimpleDefaultConfiguration();
		}
		application.start();
	}

	/**
	 * finishes the application.
	 * 
	 * @param argClient the application instance
	 */
	public static void finish(final Application argClient) {
		application.end();
	}

	/**
	 * do not us this constructor.
	 */
	private ApplicationManager() {
	}

	/**
	 * determine the application class.
	 * 
	 * @param appConfigFilePath the configuration file path
	 * @param config            the configuration
	 * 
	 * @return the application class
	 */
	private static Class<?> determineApplicationClass(final String appConfigFilePath, final ConfigApplication config) {
		Class<?> appClass = null;
		final List<String> testclassnames = new ArrayList<String>();
		String classname = config.getApplicationclass();
		if (classname == null) {

			// try to find the application class at the
			// <appConfigFilePath>
			if (appClass == null) {
				try {
					classname = appConfigFilePath.replace('/', '.');
					classname = classname.substring(0, classname.length() - 4);
					appClass = Class.forName(classname);
				} catch (ClassNotFoundException e) {
					testclassnames.add(classname);
				}
			}

			// try to find
			// <root package>.presentation.<application name>Application
			try {
				classname = config.getRootpackage() + ".presentation." + config.getName() + "Application";
				appClass = Class.forName(classname);
			} catch (ClassNotFoundException e) {
				testclassnames.add(classname);
			}

			// try to find
			// <root package>.presentation.<application name>
			if (appClass == null) {
				try {
					classname = config.getRootpackage() + ".presentation." + config.getName();
					appClass = Class.forName(classname);
				} catch (ClassNotFoundException e) {
					testclassnames.add(classname);
				}
			}

			// try to find
			// <root package>.<application name>Application
			try {
				classname = config.getRootpackage() + "." + config.getName() + "Application";
				appClass = Class.forName(classname);
			} catch (ClassNotFoundException e) {
				testclassnames.add(classname);
			}

			// try to find
			// <root package>.<application name>
			if (appClass == null) {
				try {
					classname = config.getRootpackage() + "." + config.getName();
					appClass = Class.forName(classname);
				} catch (ClassNotFoundException e) {
					testclassnames.add(classname);
				}
			}
		} else {
			// try to find the configured application class directly
			try {
				appClass = Class.forName(classname);
			} catch (ClassNotFoundException e) {
				testclassnames.add(classname);
			}

			// try to find the configured application class under the root
			// package
			if (appClass == null) {
				try {
					classname = config.getRootpackage() + "." + config.getApplicationclass();
					appClass = Class.forName(classname);
				} catch (ClassNotFoundException e) {
					testclassnames.add(classname);
				}
			}

			// try to find the configured application class under
			// <root package>.presentation
			if (appClass == null) {
				classname = config.getRootpackage() + ".presentation." + config.getApplicationclass();
				try {
					appClass = Class.forName(classname);
				} catch (ClassNotFoundException e) {
					testclassnames.add(classname);
				}
			}
		}
		if (appClass == null) {
			String msg = "could not determine application class.\n" + "looked up at the following locations:";
			for (String clsn : testclassnames) {
				msg += "\n" + clsn;
			}
			throw new RapidBeansRuntimeException(msg);
		}
		return appClass;
	}
}
