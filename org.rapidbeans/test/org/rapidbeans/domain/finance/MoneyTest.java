/*
 * Rapid Beans Framework: LengthTest.java
*
 * Copyright Martin Bluemel, 2008
*
* Nov 14, 2005
*/
package org.rapidbeans.domain.finance;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.rapidbeans.core.type.TypeRapidQuantity;
import org.rapidbeans.core.type.TypeRapidQuantityConversionTable;

/**
 * Unit tests for class Length.
 *
 * @author Martin Bluemel
 */
public final class MoneyTest extends TestCase {

    /**
     * test constructor with string.
     */
    public void testMoneyString() {
        Money money = new Money("11 euro");
        assertSame(TypeRapidQuantity.forName("org.rapidbeans.domain.finance.Money"),
                money.getType());
        assertSame(Money.class, money.getType().getImplementingClass());
        assertEquals(new BigDecimal("11"), money.getMagnitude());
        assertSame(Currency.euro, money.getUnit());
    }

    /**
     * test constructor with decimal and unit.
     */
    public void testMoneyMagUnit() {
        Money mon = new Money(new BigDecimal("100.00"), Currency.euro);
        assertSame(TypeRapidQuantity.forName("org.rapidbeans.domain.finance.Money"),
                mon.getType());
        assertSame(Money.class, mon.getType().getImplementingClass());
        assertEquals(new BigDecimal("100.00"), mon.getMagnitude());
        assertSame(Currency.euro, mon.getUnit());
    }

    /**
     * test conversion.
     */
    public void testMoneyConvert() {
        Money mon = new Money(new BigDecimal("1000.00000"), Currency.euro);
        TypeRapidQuantity type = mon.getType();
        TypeRapidQuantityConversionTable table = type.getConversionTable();
        table.setConversionFactor(Currency.euro, Currency.dollar, new BigDecimal(2));
        assertEquals(new BigDecimal("2000.00000"), mon.convert(Currency.dollar).getMagnitude());
        assertEquals(new BigDecimal("500.00000"), new Money(new BigDecimal("1000.00000"), Currency.dollar).convert(Currency.euro).getMagnitude());
    }
}
