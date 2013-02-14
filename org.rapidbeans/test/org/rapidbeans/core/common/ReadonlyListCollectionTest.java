/*
 * Rapid Beans Framework: ReadonlyListCollectionTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * created 13.02.2008
 */
package org.rapidbeans.core.common;

import java.util.HashSet;
import java.util.LinkedHashSet;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.test.codegen.Address;
import org.rapidbeans.test.codegen.Person;

/**
 * @author Martin Bluemel
 */
public class ReadonlyListCollectionTest extends TestCase {

	/**
	 * Test method for {@link org.rapidbeans.core.common.ReadonlyListCollection#toArray()}.
	 */
	public void testToArray() {
		ReadonlyListCollection<Person> persons = createPersonROCollection();
		Object[] pa = persons.toArray();
		assertEquals(4, pa.length);
		assertEquals("Bluemel_Martin_19641014", pa[0].toString());
		assertEquals("Bluemel_Melanie_20020831", pa[3].toString());
	}

	/**
	 * Test method for {@link org.rapidbeans.core.common.ReadonlyListCollection#toArray(java.lang.Object[])}.
	 */
	public void testToArrayObjectArray() {
		ReadonlyListCollection<Person> persons = createPersonROCollection();
		Person[] pa = persons.toArray(new Person[4]);
		assertEquals(4, pa.length);
		assertEquals("Martin", pa[0].getPrename());
		assertEquals("Melanie", pa[3].getPrename());
	}

	/**
	 * Test method for {@link org.rapidbeans.core.common.ReadonlyListCollection#toArray(java.lang.Object[])}.
	 */
	public void testToArrayObjectArrayWrongType() {
		ReadonlyListCollection<Person> persons = createPersonROCollection();
		try {
			persons.toArray(new String[4]);
		} catch (ArrayStoreException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for {@link org.rapidbeans.core.common.ReadonlyListCollection#get(int)}.
	 */
	public void testGetList() {
		ReadonlyListCollection<Person> persons = createPersonROCollection();
		assertEquals("Daniela", persons.get(2).getPrename());
	}

	/**
	 * Test method for {@link org.rapidbeans.core.common.ReadonlyListCollection#get(int)}.
	 */
	public void testGetListSet() {
		ReadonlyListCollection<Person> persons = createPersonROCollectionSet();
		assertEquals("Ulrike", persons.get(2).getPrename());
	}

	/**
	 * Test method for {@link org.rapidbeans.core.common.ReadonlyListCollection#indexOf(java.lang.Object)}.
	 */
	public void testIndexOf() {
		ReadonlyListCollection<Person> persons = createPersonROCollection();
		assertEquals(0, persons.indexOf(persons.get(0)));
		assertEquals(1, persons.indexOf(persons.get(1)));
		assertEquals(3, persons.indexOf(persons.get(3)));
	}

	/**
	 * Test method for {@link org.rapidbeans.core.common.ReadonlyListCollection#indexOf(java.lang.Object)}.
	 */
	public void testIndexOfSet() {
		ReadonlyListCollection<Person> persons = createPersonROCollectionSet();
		assertEquals(0, persons.indexOf(persons.get(0)));
		assertEquals(1, persons.indexOf(persons.get(1)));
		assertEquals(3, persons.indexOf(persons.get(3)));
	}

	/**
	 * Test method for {@link org.rapidbeans.core.common.ReadonlyListCollection#lastIndexOf(java.lang.Object)}.
	 */
	public void testLastIndexOf() {
		ReadonlyListCollection<Person> persons = createPersonROCollection();
		assertEquals(0, persons.lastIndexOf(persons.get(0)));
		assertEquals(1, persons.lastIndexOf(persons.get(1)));
		assertEquals(3, persons.lastIndexOf(persons.get(3)));
	}

	/**
	 * Test method for {@link org.rapidbeans.core.common.ReadonlyListCollection#lastIndexOf(java.lang.Object)}.
	 */
	public void testLastIndexOfSet() {
		ReadonlyListCollection<Person> persons = createPersonROCollectionSet();
		assertEquals(0, persons.lastIndexOf(persons.get(0)));
		assertEquals(1, persons.lastIndexOf(persons.get(1)));
		assertEquals(3, persons.lastIndexOf(persons.get(3)));
	}

	private ReadonlyListCollection<Person> createPersonROCollection() {
		Address adr = new Address();
		adr.addInhabitant(new Person(new String[] { "Bluemel", "Martin", "19641014" }));
		adr.addInhabitant(new Person(new String[] { "Bluemel", "Ulrike", "19620802" }));
		adr.addInhabitant(new Person(new String[] { "Bluemel", "Daniela", "19991212" }));
		adr.addInhabitant(new Person(new String[] { "Bluemel", "Melanie", "20020831" }));
		return (ReadonlyListCollection<Person>) adr.getInhabitants();
	}

	private ReadonlyListCollection<Person> createPersonROCollectionSet() {
		Address adr = new Address();
		Property prop = adr.getProperty("inhabitants");
		TypePropertyCollection proptype = (TypePropertyCollection) prop.getType();
		Class<?> prevClass = proptype.getCollectionClass();
		assertSame(LinkedHashSet.class, prevClass);
		try {
			proptype.setCollectionClass(HashSet.class);
			adr.addInhabitant(new Person(new String[] { "Bluemel", "Martin", "19641014" }));
			adr.addInhabitant(new Person(new String[] { "Bluemel", "Ulrike", "19620802" }));
			adr.addInhabitant(new Person(new String[] { "Bluemel", "Daniela", "19991212" }));
			adr.addInhabitant(new Person(new String[] { "Bluemel", "Melanie", "20020831" }));
			return (ReadonlyListCollection<Person>) adr.getInhabitants();
		} finally {
			proptype.setCollectionClass(prevClass);
		}
	}
}
