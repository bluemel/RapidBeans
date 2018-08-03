/*
 * Rapid Beans Framework: RapidQuantityTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Nov 21, 2005
 */
package org.rapidbeans.core.basic;

import java.math.BigDecimal;

import org.rapidbeans.core.type.TypeRapidQuantity;
import org.rapidbeans.core.type.TypeRapidQuantityConversionTable;
import org.rapidbeans.domain.math.Length;
import org.rapidbeans.domain.math.Time;
import org.rapidbeans.domain.math.UnitLength;
import org.rapidbeans.domain.math.UnitTime;

import junit.framework.TestCase;

/**
 * test cases for class BBQUantity.
 * 
 * @author Martin Bluemel
 */
public final class RapidQuantityTest extends TestCase {

	/**
	 * test creating a generated instance (typesafe).
	 */
	public void testCreateInstanceClass() {
		RapidQuantity len = RapidQuantity.createInstance("org.rapidbeans.domain.math.Length", "2123456789 m");
		assertEquals(new BigDecimal(2123456789), len.getMagnitude());
		assertEquals(2123456789, len.getMagnitudeLong());
		assertEquals(2123456789.0, len.getMagnitudeDouble());
		assertEquals(2123456.789, len.convert(UnitLength.km).getMagnitudeDouble());
		assertSame(UnitLength.m, len.getUnit());
		assertSame(TypeRapidQuantity.forName("org.rapidbeans.domain.math.Length"), len.getType());
		assertSame(Length.class, len.getClass());
	}

	/**
	 * Test method for hashCode().
	 */
	public void testHashCode() {
		RapidQuantity len = RapidQuantity.createInstance("org.rapidbeans.domain.math.Length", "2123456789 m");
		assertEquals(new String("2123456789 m").hashCode(), len.hashCode());
	}

	/**
	 * Test method for 'org.rapidbeans.rename.BBQuantity.BBQuantity(BigDecimal,
	 * RapidEnum)'.
	 */
	public void testBBQuantity() {
		Length len = new Length(new BigDecimal("1234.567890123456789"), UnitLength.km);
		assertEquals(new Length("1234.567890123456789 km"), len);
	}

	/**
	 * Test method for 'org.rapidbeans.rename.BBQuantity.toString()'.
	 */
	public void testToString() {
		assertEquals("1234.567890123456789 km",
				new Length(new BigDecimal("1234.567890123456789"), UnitLength.km).toString());
	}

	/**
	 * Test method for 'org.rapidbeans.rename.BBQuantity.equals(Object)'.
	 */
	public void testEquals() {
		assertEquals(new Length("10 m"), new Length("10 m"));
		assertNotEquals(new Length("10 m"), new Length("20 cm"));
		assertNotEquals(new Length("1 m"), new Length("100 cm"));
		assertEquals(0, new Length("1 m").compareTo(new Length("100 cm")));
	}

	/**
	 * Test method for 'org.rapidbeans.rename.BBQuantity.compareTo(Object)'.
	 */
	public void testCompareTo() {
		assertEquals(-1, new Length("10 m").compareTo(new Length("11 m")));
		assertEquals(1, new Length("11 m").compareTo(new Length("10 m")));
		assertEquals(0, new Length("112 m").compareTo(new Length("112 m")));
		assertEquals(1, new Length("10 km").compareTo(new Length("11 pm")));
		assertEquals(-1, new Length("10 cm").compareTo(new Length("1 m")));
		assertEquals(0, new Length("1000000000000000000 pm").compareTo(new Length("1000 km")));
		assertEquals(1, new Length("1000000000000000001 pm").compareTo(new Length("1000 km")));
	}

	/**
	 * Test method for 'org.rapidbeans.rename.BBQuantity.convert(RapidEnum)'.
	 */
	public void testConvert() {
		TypeRapidQuantityConversionTable convTable = new Length("1 m").getType().getConversionTable();
		assertEquals(new BigDecimal("1E+3"), convTable.getConversionFactor(UnitLength.km, UnitLength.m));
		assertEquals(false, convTable.getConversionFactorReciprocalFlag(UnitLength.km, UnitLength.m));
		assertEquals(new BigDecimal("1E+3"), convTable.getConversionFactor(UnitLength.m, UnitLength.km));
		assertEquals(true, convTable.getConversionFactorReciprocalFlag(UnitLength.m, UnitLength.km));
		assertEquals(new BigDecimal("1E+2"), convTable.getConversionFactor(UnitLength.m, UnitLength.cm));
		assertEquals(false, convTable.getConversionFactorReciprocalFlag(UnitLength.m, UnitLength.cm));
		assertEquals(new BigDecimal("1E+2"), convTable.getConversionFactor(UnitLength.cm, UnitLength.m));
		assertEquals(true, convTable.getConversionFactorReciprocalFlag(UnitLength.cm, UnitLength.m));

		assertEquals("1001.123 m", new Length("100112.3 cm").convert(UnitLength.m).toString());
		assertEquals("1001.123 m", new Length("1001.123 m").convert(UnitLength.m).toString());
		assertEquals("100112.3 cm", new Length("1001.123 m").convert(UnitLength.cm).toString());

		convTable = new Time("1 h").getType().getConversionTable();
		assertEquals(new BigDecimal("60"), convTable.getConversionFactor(UnitTime.h, UnitTime.min));
		assertEquals(false, convTable.getConversionFactorReciprocalFlag(UnitTime.h, UnitTime.min));
		assertEquals(new BigDecimal("60"), convTable.getConversionFactor(UnitTime.min, UnitTime.h));
		assertEquals(true, convTable.getConversionFactorReciprocalFlag(UnitTime.min, UnitTime.h));

		assertEquals(new Time("11 h"), new Time("11 h").convert(UnitTime.h));
		assertEquals(new Time("1 h"), new Time("60 min").convert(UnitTime.h));
		assertEquals(new Time("1 h"), new Time("3600 s").convert(UnitTime.h));
	}

	/**
	 * convenience.
	 * 
	 * @param o1 Object 1
	 * @param o2 Object 2
	 */
	private void assertNotEquals(final Object o1, final Object o2) {
		assertFalse(o1.equals(o2));
	}
}
