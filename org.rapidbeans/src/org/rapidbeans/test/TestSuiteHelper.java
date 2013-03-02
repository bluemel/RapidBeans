/*
 * Rapid Beans Framework: TestSuiteHelper.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 04/10/2006
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

package org.rapidbeans.test;

import java.io.File;
import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.rapidbeans.core.util.FileFilterRegExp;

/**
 * Helps building a TestSuite.
 * 
 * @author Martin Bluemel
 */
public final class TestSuiteHelper {

	/**
	 * utility classes must have a public constructor.
	 */
	private TestSuiteHelper() {
	}

	/**
	 * fills a test suite with all classes according to the given pattern.
	 * 
	 * @param suite
	 *            the test suite to fill
	 * @param dir
	 *            the directory to search for test classes
	 * @param classNamePattern
	 *            the pattern for test class names
	 * @param parentPackage
	 *            the parent package
	 * @param classnamesIn
	 *            the classnames to include
	 * @param classnamesEx
	 *            the classnames to exclude
	 */
	@SuppressWarnings("unchecked")
	public static void fill(final TestSuite suite, final File dir,
			final String classNamePattern, final String parentPackage,
			final ArrayList<String> classnamesIn,
			final ArrayList<String> classnamesEx) {
		String pckg;
		if (parentPackage == null) {
			pckg = "";
		} else if (parentPackage.equals("")) {
			pckg = dir.getName();
		} else {
			pckg = parentPackage + "." + dir.getName();
		}
		final File[] testClassFiles = dir.listFiles(new FileFilterRegExp("\\A"
				+ classNamePattern + ".java\\z"));
		int i;
		String filename, classname, pureClassname;
		for (i = 0; i < testClassFiles.length; i++) {
			filename = testClassFiles[i].getName();
			pureClassname = filename.substring(0, filename.length() - 5);
			classname = pckg + "." + pureClassname;
			try {
				if ((classnamesIn == null || classnamesIn.contains(classname))
						&& (classnamesEx == null || (!classnamesEx
								.contains(classname)))) {
					suite.addTestSuite((Class<TestCase>) Class
							.forName(classname));
				}
			} catch (ClassNotFoundException e) {
				Assert.fail("test class not found: " + classname);
			}
		}
		final File[] allFiles = dir.listFiles();
		for (i = 0; i < allFiles.length; i++) {
			if (allFiles[i].isDirectory()
					&& !(allFiles[i].getName().equals(".svn"))) {
				fill(suite, allFiles[i], classNamePattern, pckg, classnamesIn,
						classnamesEx);
			}
		}
	}
}
