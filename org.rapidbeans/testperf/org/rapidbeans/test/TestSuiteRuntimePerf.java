package org.rapidbeans.test;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for all runtime tests. These tests mesure how long things take.
 * 
 * @author Martin Bluemel
 */
public final class TestSuiteRuntimePerf {

	/**
	 * Utility class should not have a pulic default constructor.
	 */
	private TestSuiteRuntimePerf() {
	}

	/**
	 * the main function to run these tests in batch mode.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		junit.textui.TestRunner.run(TestSuiteRuntimePerf.suite());
	}

	/**
	 * the test suite.
	 * 
	 * @return all runtime tests
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.rapidbeans.test");
		TestSuiteHelper.fill(suite, new File("testperf"), ".*RuntimePerfTest", null, null, null);
		return suite;
	}
}
