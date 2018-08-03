/*
 * Rapid Beans Framework: MergeSectionsTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Oct 29, 2005
 */

package org.rapidbeans.sdk.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.apache.tools.ant.BuildException;
import org.junit.Assert;
import org.junit.Test;
import org.rapidbeans.sdk.merge.MergeProperties;

/**
 * Unit TestCase (Unit Tests).
 * 
 * @author Martin Bluemel
 */
public final class MergeSectionsTest {

	/**
	 * test1. - normal Merge - merge of a newly genrated body in srcfilegen that has
	 * no correspnding counterpart in srcfileman - merge of a body with a changed
	 * signature
	 * 
	 * @throws IOException I/O Error
	 */
	@Test
	public void testMergeBodiesNormalAndSignatureChange() throws IOException {
		AntGateway ant = new AntGateway();
		File testDir = new File("src/test/resources/testdata/test1");
		File srcfilegen = new File(testDir, "testNewGen.txt");
		File srcfileman = new File(testDir, "testOldMan.txt");
		File destfile = new File(testDir, "testMergeResult.txt");
		File destfileExpected = new File(testDir, "testMergeResultExpected.txt");
		ant.mergeSections(srcfilegen, srcfileman, destfile);
		assertTextFilesEquals(destfileExpected, destfile);
		ant.delete(destfile);
	}

	/**
	 * test2: simpliest case, only one file => no merge.
	 * 
	 * @throws IOException I/O Error
	 */
	@Test
	public void testMergeMethodsNoSrcfilemanGiven() throws IOException {
		AntGateway ant = new AntGateway();
		File testDir = new File("src/test/resources/testdata/test2");
		File srcfilegen = new File(testDir, "testNewGen.txt");
		File destfile = new File(testDir, "testMergeResult.txt");
		ant.mergeSections(srcfilegen, null, destfile);
		assertTextFilesEquals(srcfilegen, destfile);
		ant.delete(destfile);
	}

	/**
	 * test2: srcfileman given but not existent => no merge.
	 * 
	 * @throws IOException I/O Error
	 */
	@Test
	public void testMergeMethodsSrcfilemanNotExistent() throws IOException {
		AntGateway ant = new AntGateway();
		File testDir = new File("src/test/resources/testdata/test2");
		File srcfilegen = new File(testDir, "testNewGen.txt");
		File srcfileman = new File(testDir, "testOldMan.txt");
		File destfile = new File(testDir, "testMergeResult.txt");
		ant.mergeSections(srcfilegen, srcfileman, destfile);
		assertTextFilesEquals(srcfilegen, destfile);
		ant.delete(destfile);
	}

	/**
	 * test3. - normal Merge with different properties - merge of a newly genrated
	 * body in srcfilegen that has no correspnding counterpart in srcfileman - merge
	 * of a body with a changed signature
	 * 
	 * @throws IOException I/O Error
	 */
	@Test
	public void testMergeBodiesNormalAndSignatureChangeDifferentProps() throws IOException {
		AntGateway ant = new AntGateway();
		File testDir = new File("src/test/resources/testdata/test3");
		File srcfilegen = new File(testDir, "testNewGen.txt");
		File srcfileman = new File(testDir, "testOldMan.txt");
		File destfile = new File(testDir, "testMergeResult.txt");
		File destfileExpected = new File(testDir, "testMergeResultExpected.txt");
		final MergeProperties mergeProps = new MergeProperties("#", "BEGIN hand written code", "END hand written code",
				"!!!! BEGIN unmatched hand written code", "!!!! END unmatched hand written code");
		ant.mergeSections(srcfilegen, srcfileman, destfile, mergeProps);
		assertTextFilesEquals(destfileExpected, destfile);
		ant.delete(destfile);
	}

	/**
	 * all properties are ok.
	 */
	@Test
	public void testValidatePropertiesHappy() {
		TaskMergeSections task = new TaskMergeSections();
		File testDir = new File("src/test/resources/testdata/test1");
		task.setSrcfilegen(new File(testDir, "testNewGen.txt"));
		task.setSrcfileman(new File(testDir, "testOldMan.txt"));
		task.setDestfile(new File(testDir, "testMergeResult.txt"));
		task.validateProperties();
	}

	/**
	 * srcfilegen may not be null.
	 */
	@Test
	public void testValidatePropertiesSrcfilegenNull() {
		TaskMergeSections task = new TaskMergeSections();
		File testDir = new File("src/test/resources/testdata/test1");
		task.setSrcfileman(new File(testDir, "testOldMan.txt"));
		task.setDestfile(new File(testDir, "testMergeResult.txt"));
		try {
			task.validateProperties();
			Assert.fail("expected BuildException");
		} catch (BuildException e) {
			doNothingJustAvoidWarnings();
		}
	}

	/**
	 * srcfileman may be null. In this case srcfile gen simply is copied to destfile
	 */
	public void testValidatePropertiesSrcfilemanNull() {
		TaskMergeSections task = new TaskMergeSections();
		File testDir = new File("src/test/resources/testdata/test1");
		task.setSrcfilegen(new File(testDir, "testNewGen.txt"));
		task.setDestfile(new File(testDir, "testMergeResult.txt"));
		task.validateProperties();
	}

	/**
	 * asserts that two text files have exactly the same content.
	 * 
	 * @param f1 first file
	 * @param f2 second file
	 * @throws IOException error with IO
	 */
	public static void assertTextFilesEquals(final File f1, final File f2) throws IOException {
		LineNumberReader rd1 = null;
		LineNumberReader rd2 = null;
		try {
			rd1 = new LineNumberReader(new InputStreamReader(new FileInputStream(f1)));
			rd2 = new LineNumberReader(new InputStreamReader(new FileInputStream(f2)));
			int lineno = 1;
			String line1 = rd1.readLine();
			String line2;
			while (line1 != null) {
				line2 = rd2.readLine();
				if (line2 == null) {
					Assert.fail("file " + f2.getAbsolutePath() + " is shorter than file " + f1.getAbsolutePath() + "."
							+ " file comparison failed in line " + lineno);
				}
				if (!line1.equals(line2)) {
					Assert.fail(" file comparison failed in line " + lineno + "\nfile " + f1.getAbsolutePath() + ": \""
							+ line1 + "\"" + "\nfile " + f2.getAbsolutePath() + ": \"" + line2 + "\"");
				}
				lineno++;
				line1 = rd1.readLine();
			}
			line2 = rd2.readLine();
			if (line2 != null) {
				Assert.fail("file " + f2.getAbsolutePath() + " is longer than file " + f1.getAbsolutePath() + "."
						+ " file comparison failed in line " + lineno);
			}
		} finally {
			if (rd1 != null) {
				rd1.close();
			}
			if (rd1 != null) {
				rd2.close();
			}
		}
	}

	/**
	 * do nothing just avoid warnings.
	 */
	private void doNothingJustAvoidWarnings() {
	}
}
