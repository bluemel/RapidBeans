package org.rapidbeans.datasource;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.test.codegen.Address;
import org.rapidbeans.test.codegen.AddressBook;
import org.rapidbeans.test.codegen.Person;

/**
 * Test heap space needs and garbage collection for a bean array.
 * 
 * @author Martin Bluemel
 */
public final class BBDocumentMemoryPerfTest extends TestCase {

	/**
	 * set the default collection class to array list to achive the desired
	 * perfomance.
	 */
	public void setUp() {
		TypePropertyCollection.setDefaultCollectionClass(ArrayList.class);
	}

	/**
	 * reset the default default collection class.
	 */
	public void tearDown() {
		try {
			return;
		} finally {
			TypePropertyCollection.setDefaultCollectionClass(TypePropertyCollection.DEFAULT_COLLECTION_CLASS_DEFAULT);
		}
	}

	/**
	 * this test prooves that deleting a bean out of a document releases the
	 * bean for garbage collection.
	 * 
	 * @throws InterruptedException
	 *             because of Thread.sleep.
	 */
	public void testDeleteBeans() throws InterruptedException {
		final int count = 100000;

		RapidBean[] array2 = new RapidBean[count / 2];
		for (int i = 0; i < count / 2; i++) {
			array2[i] = new Address();
		}

		// add the first set of beans to a document
		System.out.println("[BBDocumentMemoryPerfTest]" + " adding the first set of " + count
				+ " beans to a document...");
		Thread.sleep(100);
		AddressBook root = new AddressBook();
		Document doc = new Document("test", root);
		Person[] array = new Person[count];
		Person bean;
		int p = 0;
		int pCurrent;
		Runtime.getRuntime().gc();
		Thread.sleep(100);
		final long freeBytesBefore = Runtime.getRuntime().freeMemory();
		System.out.println("[BBDocumentMemoryPerfTest]" + " free Bytes: " + freeBytesBefore);
		for (int i = 0; i < count; i++) {
			bean = BBDocumentIORuntimePerfTest.generatePerson();
			root.addPerson(bean);
			assertSame(bean, doc.findBean(bean.getType().getName(), bean.getIdString()));
			// store the bean for deletion
			array[i] = bean;
			pCurrent = (int) ((((double) i / count)) * 100);
			if (pCurrent > (p + 1)) {
				System.out.print(".");
				Thread.sleep(100);
				p = pCurrent;
			}
		}
		System.out.println();
		Runtime.getRuntime().gc();
		Thread.sleep(100);
		final long consumedBytes = freeBytesBefore - Runtime.getRuntime().freeMemory();
		System.out.println("[BBDocumentMemoryPerfTest]" + " free Bytes: " + Runtime.getRuntime().freeMemory());
		System.out.println("[BBDocumentMemoryPerfTest]" + " consumed " + consumedBytes + " Bytes totally.");
		final long bytesPerBean = consumedBytes / count;
		System.out.println("[BBDocumentMemoryPerfTest]" + " consumed " + bytesPerBean + " Bytes per bean.");

		// add the second set of beans to a document
		// and expect an OutOfMemoryProblem
		System.out.println("[BBDocumentMemoryPerfTest]" + " adding a 2nd set of " + count
				+ " beans to the document without deleting a bean...");
		Thread.sleep(100);
		p = 0;
		try {
			for (int i = 0; i < count; i++) {
				bean = BBDocumentIORuntimePerfTest.generatePerson();
				root.addPerson(bean);
				assertSame(bean, doc.findBean(bean.getType().getName(), bean.getIdString()));
				pCurrent = (int) ((((double) i / count)) * 100);
				if (pCurrent > (p + 1)) {
					System.out.print(".");
					Thread.sleep(100);
					p = pCurrent;
				}
			}
			fail("expected RapidBeansRuntimeException or OutOfMemoryError");
		} catch (RapidBeansRuntimeException e) {
			final Throwable e1 = e.getCause();
			assertTrue("caught BBRuntimeException with cause different from InvocationTargetException"
					+ e1.getClass().getName() + ": " + e1.getMessage(), e1 instanceof InvocationTargetException);
			final Throwable e2 = e1.getCause();
			assertTrue("caught InvocationTargetException with nested exception different from OutOfMemoryError: "
					+ e2.getClass().getName() + ": " + e2.getMessage(), e2 instanceof OutOfMemoryError);
			System.out.println("[BBDocumentMemoryPerfTest] got an InvocationTargetException"
					+ " caused by an OutOfMemoryError as expected");
		} catch (OutOfMemoryError e) {
			System.out.println();
			System.out.println("[BBDocumentMemoryPerfTest] got an OutOfMemoryError as expected");
		}

		// delete count beans and try again
		array2 = null;
		System.out.println("[BBDocumentMemoryPerfTest] deleting the first set of beans from the document...");
		Thread.sleep(100);
		p = 0;
		for (int i = 0; i < count; i++) {
			bean = array[i];
			bean.delete();
			array[i] = null;
			pCurrent = (int) ((((double) i / count)) * 100);
			if (pCurrent > (p + 1)) {
				System.out.print(".");
				Thread.sleep(100);
				p = pCurrent;
			}
		}
		System.out.println();

		// add a third set of beans to a document
		System.out.println("[BBDocumentMemoryPerfTest]" + " adding again the 2nd set of " + count
				+ " beans to the document...");
		Thread.sleep(100);
		p = 0;
		for (int i = 0; i < count; i++) {
			bean = BBDocumentIORuntimePerfTest.generatePerson();
			root.addPerson(bean);
			assertSame(bean, doc.findBean(bean.getType().getName(), bean.getIdString()));
			pCurrent = (int) ((((double) i / count)) * 100);
			if (pCurrent > (p + 1)) {
				System.out.print(".");
				Thread.sleep(100);
				p = pCurrent;
			}
		}
		System.out.println();
	}
}
