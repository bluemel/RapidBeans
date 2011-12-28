
package org.rapidbeans.test;

import java.io.File;
import java.util.ArrayList;

import org.rapidbeans.test.TestSuiteHelper;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for all (heap) memory tests.
 * These tests verify that the garbage collection works out
 * (memory leaks, cyclic references, ...).
 *
 * @author Martin Bluemel
 */
public final class TestSuiteMemoryPerf {

    /**
     * Utility class should not have a pulic default constructor.
     */
    private TestSuiteMemoryPerf() {
    }

    /**
     * the main function to run these tests in batch mode.
     *
     * @param args the arguments
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TestSuiteMemoryPerf.suite(args));
    }

    /**
     * the test suite.
     *
     * @return all memory tests
     */
    public static Test suite() {
        return suite(null);
    }

    /**
     * the test suite.
     *
     * @param args the test classes to run
     *
     * @return all memory tests
     */
    public static Test suite(final String[] args) {
        TestSuite suite = new TestSuite("Test for org.rapidbeans.test");
        ArrayList<String> classnames = null;
        if (args != null) {
            classnames = new ArrayList<String>();
            for (String classname : args) {
                classnames.add(classname);
            }
        }
        TestSuiteHelper.fill(suite, new File("testperf"), ".*MemoryPerfTest",
                null, classnames, null);
        return suite;
    }
}
