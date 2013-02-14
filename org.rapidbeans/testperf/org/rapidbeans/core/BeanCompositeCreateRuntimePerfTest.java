package org.rapidbeans.core;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.presentation.MenuItem;
import org.rapidbeans.presentation.Submenu;

/**
 * Test rutime for creation of a composite tree.
 * 
 * @author Martin Bluemel
 */
public final class BeanCompositeCreateRuntimePerfTest extends TestCase {

	/**
	 * warm up flag.
	 */
	private boolean isWarmedUp = false;

	/**
	 * set up the unit test:
	 * warm up the first time.
	 * 
	 * @throws InterruptedException
	 *             because we use Thread.sleep().
	 */
	public void setUp()
			throws InterruptedException {
		// warm up
		if (!this.isWarmedUp) {
			System.out.println("[BeanCompositeCreateRuntimePerfTest] warm up...");
			Thread.sleep(100);
			Submenu menu0 = (Submenu) RapidBeanImplStrict.createInstance("org.rapidbeans.presentation.Submenu");
			createSubmenus(menu0, 100000);
			this.isWarmedUp = true;
		}
	}

	/**
	 * the test proves:
	 * 100 k MenuItem beans can be created per second
	 * as composite children of a Submenu bean (Pentium M 1,6 GHz)
	 * using the default collection class for collection properties
	 * (ArrayList directly constructed).
	 * 
	 * @throws InterruptedException
	 *             because we use Thread.sleep().
	 */
	public void testBeanCompositeTreeHeapDefaultClass() throws InterruptedException {
		final int count = 100000;
		long timeExpectedMax = 2500;
		System.out.println("[BeanCompositeCreateRuntimePerfTest] starting test with default class...");
		testBeanCompositeTreeHeap(count, timeExpectedMax);
	}

	/**
	 * the test prooves:
	 * 100 k MenuItem beans can be created per second
	 * as composite childs of a Submenu bean (Pentium M 1,6 GHz)
	 * using the default collection class for collection properties
	 * (ArrayList refectively constrcuted).
	 * 
	 * @throws InterruptedException
	 *             because we use Thread.sleep().
	 */
	public void testBeanCompositeTreeHeapArayList() throws InterruptedException {
		final int count = 100000;
		long timeExpectedMax = 2600;
		System.out.println("[BeanCompositeCreateRuntimePerfTest] starting test with ArrayList class...");
		testBeanCompositeTreeHeap(count, timeExpectedMax);
	}

	/**
	 * runtime test.
	 * 
	 * @param count
	 *            number of components to create in the composite
	 * @param timeExpectedMax
	 *            expected runtime
	 * 
	 * @throws InterruptedException
	 *             because we use Thread.sleep().
	 */
	private void testBeanCompositeTreeHeap(final int count, final long timeExpectedMax)
			throws InterruptedException {

		// set up a first composite tree
		System.out.println("[BeanCompositeCreateRuntimePerfTest]"
				+ " setting up the composite tree...");
		final long timeStart = System.currentTimeMillis();
		Thread.sleep(100);
		Submenu menu1 = (Submenu) RapidBeanImplStrict.createInstance("org.rapidbeans.presentation.Submenu");
		createSubmenus(menu1, count);

		// check runtime
		final long time = System.currentTimeMillis() - timeStart;
		assertTrue("[BeanCompositeCreateRuntimePerfTest] creation of "
				+ count + " Sumbmenu beans took longer than "
				+ timeExpectedMax + " ms: " + time + " ms",
				time <= timeExpectedMax);
		System.out.println("[BeanCompositeCreateRuntimePerfTest]"
				+ "   creation of " + count + " Sumbmenu beans (composite) took " + time + " ms");
	}

	/**
	 * test helper.
	 * 
	 * @param menu
	 *            the menu to create submenus for.
	 * @param count
	 *            the counter
	 */
	private void createSubmenus(final Submenu menu, final long count) {
		PropertyCollection propCol = (PropertyCollection) menu.getProperty("menuentrys");
		MenuItem item;
		for (int i = 1; i < count; i++) {
			item = (MenuItem) RapidBeanImplStrict.createInstance("org.rapidbeans.presentation.MenuItem");
			propCol.addLink(item);
		}
	}

	/**
	 * @param args
	 *            the args
	 */
	public static void main(final String[] args) {
		TestRunner.run(BeanCompositeCreateRuntimePerfTest.class);
	}
}
