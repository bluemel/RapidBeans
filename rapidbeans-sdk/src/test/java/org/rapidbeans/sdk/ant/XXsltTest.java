/*
 * Rapid Beans Framework, SDK, Ant Tasks: XXsltTest.java
 * 
 * Copyright (C) 2010 Martin Bluemel
 * 
 * Creation Date: 10/29/2005
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

package org.rapidbeans.sdk.ant;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.rapidbeans.sdk.merge.KeyValuePair;
import org.rapidbeans.sdk.merge.MergeProperties;

/**
 * Unit TestCase (Unit Tests).
 * 
 * @author Martin Bluemel
 */
public final class XXsltTest {

	/**
	 * Happy day test with new file. This test proves. - task xxslt works - task
	 * mergesections works - parameters work
	 * 
	 * @throws IOException I/O Error
	 */
	@Test
	public void testXxsltNew() throws IOException {
		AntGateway ant = new AntGateway();
		KeyValuePair[] params = new KeyValuePair[1];
		params[0] = new KeyValuePair("package", "easybiz.test");
		File testDir = new File("src/test/resources/testdata/test4");
		File style = new File(testDir, "testStyle.xsl");
		File infile = new File(testDir, "testInput.xml");
		File outfile = new File(testDir, "testXsltmergeResult.txt");
		ant.xxslt(style, infile, outfile, params, new MergeProperties(), true);
		MergeSectionsTest.assertTextFilesEquals(outfile, new File(testDir, "testXsltmergeResultExpected.txt"));
		ant.delete(outfile);
	}

	/**
	 * Happy day test with existing file with body. This test proves the
	 * preservation of a body by xxslt -> mergesections.
	 * 
	 * @throws IOException I/O Error
	 */
	@Test
	public void testXxsltExisting() throws IOException {
		File inoutfile = null;
		try {
			AntGateway ant = new AntGateway();
			KeyValuePair[] params = new KeyValuePair[1];
			params[0] = new KeyValuePair("package", "easybiz.test");
			File testDir = new File("src/test/resources/testdata/test5");
			File style = new File(testDir, "testStyle.xsl");
			File infile = new File(testDir, "testInput.xml");
			File inoutfileSrc = new File(testDir, "testXsltmergeResultIOSrc.txt");
			inoutfile = new File(testDir, "testXsltmergeResultIO.txt");
			ant.copy(inoutfileSrc, inoutfile);
			ant.xxslt(style, infile, inoutfile, params, new MergeProperties(), false);
			MergeSectionsTest.assertTextFilesEquals(inoutfile, new File(testDir, "testXsltmergeResultExpected.txt"));
		} finally {
			if (inoutfile != null && inoutfile.exists()) {
				Assert.assertTrue(inoutfile.delete());
			}
		}
	}

	/**
	 * Happy day test with existing file with body. This test proves the
	 * preservation of a body by xxslt -> mergesections and in addition tests the
	 * properties of the xxslt task.
	 * 
	 * @throws IOException I/O Error
	 */
	@Test
	public void testXsltmergeExistingDifferentProps() throws IOException {
		File inoutfile = null;
		try {
			AntGateway ant = new AntGateway();
			KeyValuePair[] params = new KeyValuePair[1];
			params[0] = new KeyValuePair("package", "easybiz.test");
			File testDir = new File("src/test/resources/testdata/test6");
			File style = new File(testDir, "testStyle.xsl");
			File infile = new File(testDir, "testInput.xml");
			File inoutfileSrc = new File(testDir, "testXsltmergeResultIOSrc.txt");
			inoutfile = new File(testDir, "testXsltmergeResultIO.txt");
			ant.copy(inoutfileSrc, inoutfile);
			MergeProperties mergeProps = new MergeProperties("'", "BEGIN code section", "END code section", null, null);
			ant.xxslt(style, infile, inoutfile, params, mergeProps, false);
			MergeSectionsTest.assertTextFilesEquals(inoutfile, new File(testDir, "testXsltmergeResultExpected.txt"));
		} finally {
			if (inoutfile != null && inoutfile.exists()) {
				inoutfile.delete();
			}
		}
	}
}
