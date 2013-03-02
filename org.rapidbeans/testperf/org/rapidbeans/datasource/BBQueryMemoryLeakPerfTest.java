package org.rapidbeans.datasource;

import junit.framework.TestCase;

import org.rapidbeans.datasource.query.Query;

/**
 * Test if BBQUeries produce a memory leak.
 * 
 * @author Martin Bluemel
 */
public final class BBQueryMemoryLeakPerfTest extends TestCase {

	/**
	 * the test that proove the memory overflow.
	 */
	public void testBBQueryMemoryLeak() {
		Query[] queries = new Query[1000000];
		try {
			for (int i = 0; i < 1000000; i++) {
				queries[i] = new Query(
						"xxx[yyy[zzz1 = '123' && zzzz2 = '123']]");
			}
			fail("expected out of memory error");
		} catch (OutOfMemoryError e) {
			assertTrue(true);
		}
	}

	/**
	 * the test.
	 */
	public void testBBQueryNoMemoryLeak() {
		for (int i = 0; i < 1000000; i++) {
			new Query("xxx[yyy[zzz1 = '123' && zzzz2 = '123']]");
		}
	}
}
