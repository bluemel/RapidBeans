package org.rapidbeans.core;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanImplParent;

/**
 * Test heap space needs and garbage collection for a bean array.
 * 
 * @author Martin Bluemel
 */
public final class BeanArrayCreateRuntimePerfTest extends TestCase {

	/**
	 * warm up flag.
	 */
	private boolean isWarmedUp = false;

	/**
	 * set up the unit test: warm up the first time.
	 * 
	 * @throws InterruptedException
	 *             because we use Thread.sleep().
	 */
	public void setUp() throws InterruptedException {
		// warm up
		if (!this.isWarmedUp) {
			System.out.println("[BeanArrayCreateRuntimePerfTest] warm up...");
			for (int i = 0; i < 100000; i++) {
				RapidBeanImplParent.createInstance("org.rapidbeans.presentation.MenuItem");
			}
			this.isWarmedUp = true;
		}
	}

	/**
	 * the test prooves: over 100 k MenuItem beans can be created per second
	 * (Pentium M 1,6 GHz).
	 * 
	 * @throws InterruptedException
	 *             because we use Thread.sleep().
	 */
	public void testBeanArrayCreateRuntime() throws InterruptedException {
		final int count = 100000;
		long timeExpectedMax = 3000;
		final long timeStart = System.currentTimeMillis();

		// set up a first array
		System.out.println("[BeanArrayCreateRuntimePerfTest]" + " setting up bean array...");
		// give message a chance to be printed on console before load starts
		Thread.sleep(100);
		RapidBean[] beans1 = new RapidBean[count];
		for (int i = 0; i < count; i++) {
			beans1[i] = RapidBeanImplParent.createInstance("org.rapidbeans.presentation.MenuItem");
		}

		// check runtime
		final long time = System.currentTimeMillis() - timeStart;
		assertTrue("creation of " + count + " MenuItem beans took longer than " + timeExpectedMax + " ms: " + time
				+ " ms", time <= timeExpectedMax);
		System.out.println("[BeanArrayCreateRuntimePerfTest]" + "   creation of " + count + " MenuItem beans took "
				+ time + " ms");
	}
}
