/*
 * Rapid Beans Framework: LengthTest.java
*
 * Copyright Martin Bluemel, 2008
*
* Nov 14, 2005
*/
package org.rapidbeans.domain.math;

import java.math.BigDecimal;

import org.rapidbeans.core.type.TypeRapidQuantity;
import org.rapidbeans.domain.math.Length;
import org.rapidbeans.domain.math.UnitLength;

import junit.framework.TestCase;

/**
 * Unit tests for class Length.
 *
 * @author Martin Bluemel
 */
public final class LengthTest extends TestCase {

    /**
     * test constructor with string.
     */
    public void testLengthString() {
        Length len = new Length("11 m");
        assertSame(TypeRapidQuantity.forName("org.rapidbeans.domain.math.Length"),
                len.getType());
        assertSame(Length.class, len.getType().getImplementingClass());
        assertEquals(new BigDecimal("11"), len.getMagnitude());
        assertSame(UnitLength.m, len.getUnit());
    }

    /**
     * test constructor with decimal and unit.
     */
    public void testLengthMagUnit() {
        Length len = new Length(new BigDecimal("123"), UnitLength.pm);
        assertSame(TypeRapidQuantity.forName("org.rapidbeans.domain.math.Length"),
                len.getType());
        assertSame(Length.class, len.getType().getImplementingClass());
        assertEquals(new BigDecimal("123"), len.getMagnitude());
        assertSame(UnitLength.pm, len.getUnit());
    }

    /**
     * test conversion.
     */
    public void testLengthConvert() {
        Length len = new Length(new BigDecimal("123"), UnitLength.um);
        assertEquals(new Length("0.000000123 km"), len.convert(UnitLength.km));
        assertEquals(new Length("0.000123 m"), len.convert(UnitLength.m));
        assertEquals(new Length("123 um"), len.convert(UnitLength.um));
        assertEquals(new Length("1.23E+5 nm"), len.convert(UnitLength.nm));
        assertEquals(new Length("1.23E+8 pm"), len.convert(UnitLength.pm));
    }
}
