/*
 * Rapid Beans Framework: RapidEnumTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Nov 4, 2005
 */
package org.rapidbeans.core.basic;

import junit.framework.TestCase;

import org.rapidbeans.core.common.PrecisionDate;
import org.rapidbeans.core.exception.TypeNotFoundException;
import org.rapidbeans.core.type.TypeRapidEnum;
import org.rapidbeans.domain.finance.Currency;
import org.rapidbeans.domain.math.UnitLength;
import org.rapidbeans.domain.org.Sex;

/**
 * Unit Tests for class RapidEnum.
 * 
 * @author Martin Bluemel
 */
public final class RapidEnumTest extends TestCase {

	/**
	 * test getInstance happy day.
	 */
	public void testValueOf() {
		assertSame(Sex.male, Sex.valueOf("male"));
		assertSame(Sex.female, Sex.valueOf("female"));
		assertSame(PrecisionDate.day, PrecisionDate.valueOf("day"));
	}

	/**
	 * test getInstance not existent.
	 */
	public void testValueOfNotExistent() {
		try {
			Sex.valueOf("xxx");
		} catch (IllegalArgumentException e) {
			// O. K.
			assertTrue(true);
		}
	}

	/**
	 * test getInstance type not existent.
	 */
	public void testValueOfNotExistentType() {
		try {
			GenericEnum.valueOf("org.rapidbeans.domain.org.Xxx", "male");
		} catch (TypeNotFoundException e) {
			// O. K.
			assertTrue(true);
		}
	}

	/**
	 * Test method for getType().
	 */
	public void testGetType() {
		assertEquals("org.rapidbeans.domain.org.Sex", Sex.male.getType().getName());
	}

	/**
	 * Test method for toString().
	 */
	public void testToString() {
		assertEquals("male", Sex.male.toString());
	}

	/**
	 * Test method for compareTo().
	 */
	public void testCompareTo() {
		assertEquals(0, Sex.male.compareTo(Sex.male));
		assertEquals(-1, Sex.male.compareTo(Sex.female));
		assertEquals(1, Sex.female.compareTo(Sex.male));
	}

	/**
	 * Test method for RapidEnum.format(RapidEnum[], char).
	 */
	public void testFormat() {
		assertEquals("male,female", TypeRapidEnum.format(Sex.male.getType().getElements()));
	}

	/**
	 * test a concrete (generated) RapidEnum class.
	 */
	public void testConcreteEnumClass() {
		// an enum is automatically loaded while using one
		// of its elements
		Sex sex = Sex.male;
		assertNotNull(sex);
		assertSame(Sex.male, sex);
		assertEquals(Sex.male, sex);
		assertEquals("male", sex.toString());
		assertEquals(Sex.male.ordinal(), sex.ordinal());

		sex = Sex.female;
		assertTrue(sex == Sex.female);
		assertEquals(Sex.female, sex);
		assertEquals("female", sex.toString());
		assertEquals(Sex.female.ordinal(), sex.ordinal());

		switch (sex) {
		case female:
			break;
		default:
			assertTrue("sex was supposed to be Sex.female", false);
			break;
		}
	}

	/**
	 * test the value table.
	 */
	public void testEnumValueTable() {
		Currency eur = Currency.euro;
		assertEquals("euro", eur.name());
		assertEquals(1, eur.ordinal());
		assertEquals("EU", eur.getShort2());
		assertEquals("EUR", eur.getShort3());
	}

	/**
	 * test enum Sex.
	 */
	public void testEnumSex() {
		Sex sex = Sex.female;
		assertSame(Sex.female, sex);
		assertEquals(Sex.female.ordinal(), sex.ordinal());
	}

	/**
	 * test enum UnitLength.
	 */
	public void testEnumUnitLength() {
		assertSame(2, UnitLength.dm.ordinal());
		assertSame("dm", UnitLength.dm.name());
	}
}
