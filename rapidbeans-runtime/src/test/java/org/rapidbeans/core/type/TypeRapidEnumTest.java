/*
 * Rapid Beans Framework: TypeRapidEnumTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Nov 4, 2005
 */
package org.rapidbeans.core.type;

import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.core.exception.EnumException;
import org.rapidbeans.core.exception.TypeNotFoundException;
import org.rapidbeans.domain.finance.Currency;
import org.rapidbeans.domain.org.Sex;

import junit.framework.TestCase;

/**
 * Unit Tests for class TypeRapidEnum.
 * 
 * @author Martin Bluemel
 */
public final class TypeRapidEnumTest extends TestCase {

	/**
	 * Test method for getName().
	 */
	public void testGetName() {
		TypeRapidEnum sextype = Sex.male.getType();
		assertEquals("org.rapidbeans.domain.org.Sex", sextype.getName());
	}

	/**
	 * Test method for getElements().
	 */
	public void testGetElements() {
		TypeRapidEnum sextype = Sex.male.getType();
		assertEquals(2, sextype.getElements().size());
		assertSame(Sex.female, sextype.getElements().get(1));
	}

	/**
	 * Test method getImplementingClass() concrete.
	 */
	public void testGetImplementingClassConcrete() {
		TypeRapidEnum sextype = Sex.male.getType();
		assertSame(Sex.class, sextype.getImplementingClass());
	}

	/**
	 * Test method for indexOf(String).
	 */
	public void testIndexOf() {
		assertEquals(1, Sex.male.getType().indexOf("female"));
	}

	/**
	 * Test find an RapidEnum type by its name.
	 */
	public void testForName() {
		TypeRapidEnum currencyType = TypeRapidEnum.forName("org.rapidbeans.domain.finance.Currency");
		assertNotNull(currencyType);
		assertEquals("org.rapidbeans.domain.finance.Currency", currencyType.getName());
		assertSame(Currency.euro, currencyType.elementOf("euro"));
	}

	/**
	 * negative Test.
	 */
	public void testForNameNotFound() {
		try {
			TypeRapidEnum.forName("org.rapidbeans.domain.finance.XXX");
		} catch (TypeNotFoundException e) {
			// all is cool
			assertTrue(true);
		}
	}

	/**
	 * retrieve an RapidEnum element by it's index.
	 */
	public void testElementOfInt() {
		TypeRapidEnum currencyType = TypeRapidEnum.forName("org.rapidbeans.domain.finance.Currency");
		RapidEnum currencyDollar = currencyType.elementOf(0);
		assertEquals("dollar", currencyDollar.name());
	}

	/**
	 * Magic number.
	 */
	private static final int INT_10 = 10;

	/**
	 * retrieve an RapidEnum element by it's index out of bounds.
	 */
	public void testElementOfIntOutOfBounds() {
		TypeRapidEnum currencyType = TypeRapidEnum.forName("org.rapidbeans.domain.finance.Currency");
		try {
			currencyType.elementOf(INT_10);
		} catch (IndexOutOfBoundsException e) {
			// all is cool
			assertTrue(true);
		}
	}

	/**
	 * retrieve an RapidEnum element by it's name.
	 */
	public void testElementOfString() {
		TypeRapidEnum currencyType = TypeRapidEnum.forName("org.rapidbeans.domain.finance.Currency");
		RapidEnum currencyEuro = currencyType.elementOf("euro");
		assertEquals("euro", currencyEuro.name());
	}

	/**
	 * retrieve an RapidEnum element by an undefined name.
	 */
	public void testElementOfStringUnknown() {
		TypeRapidEnum currencyType = TypeRapidEnum.forName("org.rapidbeans.domain.finance.Currency");
		try {
			currencyType.elementOf("xxx");
		} catch (EnumException e) {
			// all is cool
			assertTrue(true);
		}
	}

	/**
	 * Test method getImplementingClass() generic.
	 */
	public void testGetImplementingClassGeneric() {
		TypeRapidEnum type = TypeRapidEnum.forName("org.rapidbeans.test.Lang");
		assertNotNull(type.getImplementingClass());
	}
}
