package org.rapidbeans.core;

import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.PropertyCollection;
import org.rapidbeans.core.basic.RapidBeanImplParent;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.presentation.MenuItem;
import org.rapidbeans.presentation.Submenu;

/**
 * Test heap space needs and garbage collection for a bean composite tree.
 * 
 * @author Martin Bluemel
 */
public final class BeanCompositionTreeMemoryPerfTest extends TestCase {

	/**
	 * the test prooves: bean composite trees are garbage collected.
	 * 
	 * @throws InterruptedException
	 *             because of THread.sleep.
	 */
	public void testBeanCompositeTreeHeap() throws InterruptedException {

		final int count = 400000;

		// set up a first composite tree
		System.out.println("[BeanCompositionTreeMemoryTest] setting up 1st composite tree...");
		Thread.sleep(100);
		Submenu menu1 = (Submenu) RapidBeanImplParent.createInstance("org.rapidbeans.presentation.Submenu");
		createSubmenus(menu1, count);

		// set up a second composite tree without releasing the first one
		// => out of memory
		System.out.println("[BeanCompositionTreeMemoryTest] setting up 2nd composite tree\n"
				+ "[BeanCompositionTreeMemoryTest]   without releasing the 1st one...");
		Thread.sleep(100);
		Submenu menu2 = (Submenu) RapidBeanImplParent.createInstance("org.rapidbeans.presentation.Submenu");
		try {
			createSubmenus(menu2, count);
			fail("expected OutOfMemoryError");
		} catch (RapidBeansRuntimeException e) {
			final Throwable e1 = e.getCause();
			assertTrue("caught BBRuntimeException with cause different from InvocationTargetException"
					+ e1.getClass().getName() + ": " + e1.getMessage(), e1 instanceof InvocationTargetException);
			final Throwable e2 = e1.getCause();
			assertTrue("caught InvocationTargetException with nested exception different from OutOfMemoryError: "
					+ e2.getClass().getName() + ": " + e2.getMessage(), e2 instanceof OutOfMemoryError);
			System.out.println("[BeanCompositionTreeMemoryTest] got an InvocationTargetException"
					+ " caused by an OutOfMemoryError as expected");
		} catch (Throwable t) {
			assertTrue(
					"caught throwable different from OutOfMemoryError: " + t.getClass().getName() + ": "
							+ t.getMessage(), t instanceof OutOfMemoryError);
			System.out.println("[BeanCompositionTreeMemoryTest] got an OutOfMemoryError as expected");
		}

		// release the first composite tree
		// a set up the secon one again => success
		System.out.println("[BeanCompositionTreeMemoryTest] releasing reference to 1st composite tree root");
		menu1 = null;

		System.out.println("[BeanCompositionTreeMemoryTest] setting up 2nd composite tree again...");
		Thread.sleep(100);
		menu2 = (Submenu) RapidBeanImplParent.createInstance("org.rapidbeans.presentation.Submenu");
		createSubmenus(menu2, count);

		System.out.println("[BeanCompositionTreeMemoryTest] finished: garbage collection succeded");
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
			item = (MenuItem) RapidBeanImplParent.createInstance("org.rapidbeans.presentation.MenuItem");
			propCol.addLink(item);
		}
	}
	//
	// /**
	// * constructor.
	// *
	// * @param manager the manager
	// */
	// public BeanCompositionTreeMemoryPerfTest(final PerformanceTestManager
	// manager) {
	// super(manager);
	// }
}
