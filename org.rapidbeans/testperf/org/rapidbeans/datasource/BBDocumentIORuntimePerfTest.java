/*
 * Rapid Beans Framework: DocumentTest.java
 * 
 * Copyright Martin Bluemel, 2006
 * 
 * 31.01.2006
 */
package org.rapidbeans.datasource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.rapidbeans.core.basic.IdGeneratorNumeric;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.type.TypeRapidEnum;
import org.rapidbeans.domain.org.Sex;
import org.rapidbeans.test.codegen.AddressBook;
import org.rapidbeans.test.codegen.Person;

/**
 * Unit Tests for class Document.
 * 
 * @author Martin Bluemel
 */
public class BBDocumentIORuntimePerfTest extends TestCase {

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
		// set the default collection class to array list to achive the
		// desired perfomance.
		TypePropertyCollection.setDefaultCollectionClass(ArrayList.class);
		// warm up
		if (!this.isWarmedUp) {
			System.out.println("[BBDocumentIORuntimePerfTest] warm up...");
			for (int i = 0; i < 100000; i++) {
				RapidBeanImplStrict
						.createInstance("org.rapidbeans.test.Person");
			}
			this.isWarmedUp = true;
		}
	}

	/**
	 * reset the default default collection class.
	 */
	public void tearDown() {
		try {
			return;
		} finally {
			TypePropertyCollection
					.setDefaultCollectionClass(TypePropertyCollection.DEFAULT_COLLECTION_CLASS_DEFAULT);
		}
	}

	/**
	 * test write.
	 * 
	 * @throws IOException
	 *             if IO fails
	 * @throws InterruptedException
	 *             for Thread.sleep()
	 */
	public void testWrite() throws IOException, InterruptedException {
		IdGeneratorNumeric idGenerator = new IdGeneratorNumeric();
		idGenerator.setMode(IdGeneratorNumeric.GENERATION_STRATEGY_COMPACT);
		TypeRapidBean.forName("org.rapidbeans.test.Address").setIdGenerator(
				idGenerator);
		File file = new File("testperf/testdoc.xml");

		// generate 100k Persons
		final int number = 100000;

		// -----------------------------------------------------------------
		// test generating a bunch of Person beans
		// -----------------------------------------------------------------
		System.out.println("[BBDocumentIORuntimePerfTest]" + " generating "
				+ number + " Person beans...");
		Thread.sleep(100);
		long timeExpectedMax = 10000;
		long startTime = System.currentTimeMillis();
		AddressBook adrbook = new AddressBook();
		for (int i = 0; i < number; i++) {
			adrbook.addPerson(generatePerson());
		}
		long time = System.currentTimeMillis() - startTime;
		assertTrue("creation of " + number + " Person beans took longer than "
				+ timeExpectedMax + " ms: " + time + " ms",
				time <= timeExpectedMax);
		System.out.println("[BBDocumentIORuntimePerfTest]"
				+ "   generation took " + time + " ms");
		Thread.sleep(100);

		// -----------------------------------------------------------------
		// Test creating a document with that bunch of Person beans:
		// The beans a added to the "identity map" (pool).
		// -----------------------------------------------------------------
		System.out.println("[BBDocumentIORuntimePerfTest]"
				+ " creating document...");
		Thread.sleep(100);
		timeExpectedMax = 5000;
		startTime = System.currentTimeMillis();
		Document testdoc = new Document("testdoc", adrbook);
		testdoc.setUrl(file.toURI().toURL());
		time = System.currentTimeMillis() - startTime;
		assertTrue("creation of a document with " + number
				+ " Person beans took longer than " + timeExpectedMax + " ms: "
				+ time + " ms", time <= timeExpectedMax);
		System.out.println("[BBDocumentIORuntimePerfTest]"
				+ "   document creation took " + time + " ms");
		Thread.sleep(100);

		// -----------------------------------------------------------------
		// test executing a query over that bunch of Person beans
		// -----------------------------------------------------------------
		System.out.println("[BBDocumentIORuntimePerfTest]"
				+ " executing query...");
		Thread.sleep(100);
		timeExpectedMax = 500;
		startTime = System.currentTimeMillis();
		List<RapidBean> beans = testdoc
				.findBeansByQuery("org.rapidbeans.test.Person[surname $ 'A.*']");
		time = System.currentTimeMillis() - startTime;
		assertTrue("query over " + number + " Person beans took longer than "
				+ timeExpectedMax + " ms: " + time + " ms",
				time <= timeExpectedMax);
		System.out.println("[BBDocumentIORuntimePerfTest]   query took " + time
				+ " ms");
		System.out.println("[BBDocumentIORuntimePerfTest]   found "
				+ beans.size() + " Persons.");
		// for (int i = 0; i < 10; i++) {
		// if (beans.size() > i) {
		// System.out.println("   Person No " + (i + 1) + ": " + ((Person)
		// beans.get(i)).getSurname());
		// }
		// }

		// -----------------------------------------------------------------
		// test writing the document to a file
		// -----------------------------------------------------------------
		System.out
				.println("[BBDocumentIORuntimePerfTest]"
						+ " writing document to file " + file.getAbsolutePath()
						+ "...");
		Thread.sleep(100);
		timeExpectedMax = 5000;
		startTime = System.currentTimeMillis();
		testdoc.save();
		time = System.currentTimeMillis() - startTime;
		assertTrue("writing a document with " + number
				+ " Person beans took longer than " + timeExpectedMax + " ms: "
				+ time + " ms", time <= timeExpectedMax);
		System.out.println("[BBDocumentIORuntimePerfTest]" + "   writing took "
				+ time + " ms");
		System.out.println("[BBDocumentIORuntimePerfTest]" + "   file size = "
				+ file.length() + " byte");
		Thread.sleep(100);

		// -----------------------------------------------------------------
		// test reading the document from file
		// - reading the file, XML parsing
		// - creating the beans
		// - adding the to the document's identity map
		// -----------------------------------------------------------------
		Thread.sleep(100);
		timeExpectedMax = 25000;

		testdoc = null;
		System.out
				.println("[BBDocumentIORuntimePerfTest] waiting 2 seconds for"
						+ " garbage collection...");
		Thread.sleep(2000);

		System.out
				.println("[BBDocumentIORuntimePerfTest] reading document from file "
						+ file.getAbsolutePath() + "...");
		startTime = System.currentTimeMillis();
		testdoc = new Document("testdoc", file);
		time = System.currentTimeMillis() - startTime;
		assertTrue("reading a document with " + number
				+ " Person beans took longer than " + timeExpectedMax + " ms: "
				+ time + " ms", time <= timeExpectedMax);
		System.out.println("[BBDocumentIORuntimePerfTest]   reading took "
				+ time + " ms");
		Thread.sleep(100);

		file.delete();
	}

	/**
	 * @return a new person
	 */
	public static Person generatePerson() {
		Person person = new Person("\"" + generateName(3, 15) + "\" \""
				+ generateName(3, 20) + "\" \"19640101\"");
		person.setEmail(generateName(1, 15) + "@" + generateName(5, 15) + ".de");
		person.setSex((Sex) TypeRapidEnum.forName(
				"org.rapidbeans.domain.org.Sex").elementOf(random.nextInt(2)));
		person.setPhone(generateNumber(5, 16));
		return person;
	}

	/**
	 * the random generator.
	 */
	private static Random random = new Random(System.currentTimeMillis());

	/**
	 * @param min
	 *            minumum
	 * @param max
	 *            maximum
	 * @return a generatedName
	 */
	private static String generateName(final int min, final int max) {
		int len = min + random.nextInt(max);
		char[] ca = new char[len];
		ca[0] = generateUpperCaseChar();
		for (int i = 1; i < len; i++) {
			ca[i] = generateLowerCaseChar();
		}
		return new String(ca);
	}

	/**
	 * @param min
	 *            minimum
	 * @param max
	 *            maximum
	 * @return a generated number
	 */
	private static String generateNumber(final int min, final int max) {
		int len = min + random.nextInt(max);
		char[] ca = new char[len];
		for (int i = 0; i < len; i++) {
			ca[i] = generateNumberChar();
		}
		return new String(ca);
	}

	/**
	 * @return a randomly generated character between A and Z
	 */
	private static char generateUpperCaseChar() {
		return (char) (random.nextInt(26) + 'A');
	}

	/**
	 * @return a randomly generated character between A and Z
	 */
	private static char generateLowerCaseChar() {
		return (char) (random.nextInt(26) + 'a');
	}

	/**
	 * @return a randomly generated character between A and Z
	 */
	private static char generateNumberChar() {
		return (char) (random.nextInt(10) + '0');
	}

	// /**
	// * @return a new address
	// */
	// private Address generateAddress() {
	// Address address = new Address();
	// return address;
	// }

	/**
	 * @param args
	 *            the args
	 */
	public static void main(final String[] args) {
		TestRunner.run(BBDocumentIORuntimePerfTest.class);
	}
}
