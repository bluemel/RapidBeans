/*
 * Rapid Beans Framework: BBTypeQuantityConversionTableTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Nov 14, 2005
 */
package org.rapidbeans.core.type;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.rapidbeans.domain.math.UnitLength;

/**
 * UnitTests for class BBTypeQuantityCoversionTable.
 * 
 * @author Martin Bluemel
 */
public final class TypeRapidQuantityCoversionTableTest extends TestCase {

	/**
	 * test constructor of conversion table.
	 */
	public void testBBQuantityCoversionTable() {
		TypeRapidEnum enumtype = TypeRapidEnum.forName("org.rapidbeans.domain.math.UnitLength");
		assertNotNull(enumtype);
		TypeRapidQuantityConversionTable ct = new TypeRapidQuantityConversionTable(enumtype,
				"pm/1E12,nm/1E9,um*0.000001,mm/1000,cm/100,dm/10,m*1,km*1000");
		assertEquals(new BigDecimal("1E+12"), ct.getConversionFactor(UnitLength.m, UnitLength.pm));
		assertEquals(false, ct.getConversionFactorReciprocalFlag(UnitLength.m, UnitLength.pm));
		assertEquals(new BigDecimal("1E+12"), ct.getConversionFactor(UnitLength.pm, UnitLength.m));
		assertEquals(true, ct.getConversionFactorReciprocalFlag(UnitLength.pm, UnitLength.m));
		assertEquals(new BigDecimal("1E+9"), ct.getConversionFactor(UnitLength.m, UnitLength.nm));
		assertEquals(false, ct.getConversionFactorReciprocalFlag(UnitLength.m, UnitLength.nm));
		assertEquals(new BigDecimal("1E+9"), ct.getConversionFactor(UnitLength.nm, UnitLength.m));
		assertEquals(true, ct.getConversionFactorReciprocalFlag(UnitLength.nm, UnitLength.m));
		assertEquals(new BigDecimal("1E+6"), ct.getConversionFactor(UnitLength.m, UnitLength.um));
		assertEquals(false, ct.getConversionFactorReciprocalFlag(UnitLength.m, UnitLength.um));
		assertEquals(new BigDecimal("1E+6"), ct.getConversionFactor(UnitLength.um, UnitLength.m));
		assertEquals(true, ct.getConversionFactorReciprocalFlag(UnitLength.um, UnitLength.m));
	}

	/**
	 * test get norm type.
	 */
	public void testGetNormType() {
		TypeRapidEnum enumtype = TypeRapidEnum.forName("org.rapidbeans.domain.math.UnitLength");
		assertNotNull(enumtype);
		TypeRapidQuantityConversionTable ct = new TypeRapidQuantityConversionTable(enumtype,
				"pm/1E12,nm/1E9,um*0.000001,mm/1000,cm/100,dm/10,m*1,km*1000");
		assertSame(UnitLength.m, ct.getNormUnit());
	}
}
