package org.rapidbeans.core;

import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanImplParent;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;

/**
 * Test heap space needs and garbage collection for a bean array.
 * 
 * @author Martin Bluemel
 */
public final class BeanArrayMemoryPerfTest extends TestCase {

	/**
	 * the test prooves: 1) 100 k MenuItem beans can be created per second
	 * (Pentium M 1,6 GHz) 2) beans and their properties are garbage collected.
	 * 
	 * @throws InterruptedException
	 *             because of Thread.sleep.
	 */
	public void testBeanArrayHeap() throws InterruptedException {
		final int count = 400000;

		// set up a first array
		System.out.println("[BeanArrayMemoryPerfTest] setting up 1st bean array...");
		// give message a chance to be printed on console before load starts
		Thread.sleep(100);
		RapidBean[] beans1 = new RapidBean[count];
		for (int i = 0; i < count; i++) {
			beans1[i] = RapidBeanImplParent.createInstance("org.rapidbeans.presentation.MenuItem");
		}

		// set up a second array and expect an InvocationTargetException
		System.out.println("[BeanArrayMemoryPerfTest] setting up 2nd bean array");
		System.out.println("[BeanArrayMemoryPerfTest]   without releasing the 1st one...");
		// give message a chance to be printed on console before load starts
		Thread.sleep(100);
		RapidBean[] beans2 = new RapidBean[count];
		try {
			for (int i = 0; i < count; i++) {
				beans2[i] = RapidBeanImplParent.createInstance("org.rapidbeans.presentation.MenuItem");
			}
			fail("expected RapidBeansRuntimeException");
		} catch (RapidBeansRuntimeException e) {
			final Throwable e1 = e.getCause();
			assertTrue("caught BBRuntimeException with cause different from InvocationTargetException"
					+ e1.getClass().getName() + ": " + e1.getMessage(), e1 instanceof InvocationTargetException);
			final Throwable e2 = e1.getCause();
			assertTrue("caught InvocationTargetException with nested exception different from OutOfMemoryError: "
					+ e2.getClass().getName() + ": " + e2.getMessage(), e2 instanceof OutOfMemoryError);
			System.out.println("[BeanArrayMemoryPerfTest] got an InvocationTargetException"
					+ " caused by an OutOfMemoryError as expected");
		} catch (OutOfMemoryError e) {
			System.out.println("[BeanArrayMemoryPerfTest] got an OutOfMemoryError as expected");
		}

		// release the first array
		System.out.println("[BeanArrayMemoryPerfTest] releasing reference to 1st BeanBean array...");
		beans1 = null;

		// set up the second array again
		System.out.println("[BeanArrayMemoryPerfTest] trying to set up 2nd bean array again...");
		// give message a chance to be printed on console before load starts
		Thread.sleep(100);
		beans2 = new RapidBean[count];
		for (int i = 0; i < count; i++) {
			beans2[i] = RapidBeanImplParent.createInstance("org.rapidbeans.presentation.MenuItem");
		}

		System.out.println("[BeanArrayMemoryPerfTest] finished: garbage collection succeded");
	}
}
