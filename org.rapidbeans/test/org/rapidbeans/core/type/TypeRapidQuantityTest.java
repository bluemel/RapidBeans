/*
 * Rapid Beans Framework: TypeRapidQuantityTest.java
 *
 * Copyright Martin Bluemel, 2008
 *
 * Nov 14, 2005
 */
package org.rapidbeans.core.type;

import org.rapidbeans.core.type.TypeRapidQuantity;
import org.rapidbeans.domain.math.Length;

import junit.framework.TestCase;

/**
 * UnitTests for class TypeRapidQuantity.
 *
 * @author Martin Bluemel
 */
public final class TypeRapidQuantityTest extends TestCase {

    /**
     * test the for name generated (type safe).
     */
    public void testForNameGenerated() {
        TypeRapidQuantity type = TypeRapidQuantity.forName("org.rapidbeans.domain.math.Length");
        assertNotNull(type);
        assertEquals("org.rapidbeans.domain.math.Length", type.getName());
        assertSame(Length.class, type.getImplementingClass());
        TypeRapidQuantity type1 = TypeRapidQuantity.forName("org.rapidbeans.domain.math.Length");
        assertSame(type, type1);
    }

    /**
     * Test creating a type instance of a generic quantity (with a
     * generic enum type).
     */
    public void testCreateInstanceDescrGeneric() {
        try {
            String descr = "<enumtype name=\"UnitVolt\">"
                + "<enum name=\"kV\"/>"
                + "<enum name=\"V\"/>"
                + "<enum name=\"mV\"/>"
                + "</enumtype>";
            TypeRapidEnum enumtype = TypeRapidEnum.createInstance(descr);
            assertEquals("UnitVolt", enumtype.getName());
            assertEquals("V", enumtype.elementOf("V").name());

            descr = "<quantitytype name=\"Voltage\""
                + " unitenum=\"UnitVolt\">"
                + "<unit name=\"kV\" factor=\"1E3\"/>"
                + "<unit name=\"V\" factor=\"1\"/>"
                + "<unit name=\"mV\" factor=\"1E-3\"/>"
                + "</quantitytype>";
            TypeRapidQuantity type = TypeRapidQuantity.createInstance(descr);
            assertEquals("Voltage", type.getName());
            assertEquals("Voltage", type.getNameShort());
            assertNull(type.getImplementingClass());
            assertSame(type, TypeRapidQuantity.forName("Voltage"));
            assertSame(enumtype, type.getUnitInfo());
        } finally {
            TestHelperTypeLoader.clearBeanTypesGeneric();
        }
    }

//    /**
//     * test the for name.
//     */
//    public void testForNameGeneric() {
//        TypeRapidQuantity type = TypeRapidQuantity.forName("org.rapidbeans.test.Lang");
//        assertNotNull(type);
//        assertEquals("org.rapidbeans.test.Lang", type.getName());
//        TypeRapidQuantity type1 = TypeRapidQuantity.forName("org.rapidbeans.test.Lang");
//        assertSame(type, type1);
//    }

//    /**
//     * test the constructor.
//     */
//    public void testBBTypeQuantity() {
//        final String conversionTableDescr =
//            "pm/1E12,nm/1E9,um*0.000001,mm/1000,cm/100,dm/10,m*1,km*1000";
//        TypeRapidEnum unittype = TypeRapidEnum.forName("org.rapidbeans.domain.math.UnitLength");
//        TypeRapidQuantity type = new TypeRapidQuantity(Length.class, unittype, conversionTableDescr);
//        assertNotNull(type);
//        assertEquals("org.rapidbeans.domain.math.Length", type.getName());
//    }

//    public void testGetMetainfoFromQuantityTypeName() {
//        // every concrete quantity class is loaded automatically when its
//        // metainfo
//        // is retrieved by quantity type name which is the class name
//        TypeRapidQuantity metainfLength = TypeRapidQuantity.forName("org.rapidbeans.domain.math.Length");
//        assertNotNull(metainfLength);
//
//        // assert that the quanity class was loaded properly
//        assertSame(Length.class, metainfLength.getQuantityClass());
//
//        TypeRapidEnum metainfLengthUnit = metainfLength.getUnitInfo();
//        RapidEnum[] elements = metainfLengthUnit.getElements();
//        assertSame(elements[0], UnitLength.KM);
//        assertSame(elements[1], UnitLength.M);
//        assertSame(elements[2], UnitLength.DM);
//        assertSame(elements[3], UnitLength.CM);
//        assertSame(elements[4], UnitLength.MM);
//        assertSame(elements[5], UnitLength.UM);
//        assertSame(elements[6], UnitLength.NM);
//        assertSame(elements[7], UnitLength.PM);
//    }
//
//    public void testGetMetainfoFromQuantityTypeNameGeneric() {
//        // if a concrete quantity class cannot be found
//        // a generic quantity object should be loaded
//        TypeRapidQuantity metainfLength = RapidQuantity.forName("Length");
//        assertNotNull(metainfLength);
//
//        // assert that the quanity class was loaded properly
//        assertSame(Length.class, metainfLength.getQuantityClass());
//
//        TypeRapidEnum metainfLengthUnit = metainfLength.getUnitInfo();
//        RapidEnum[] elements = metainfLengthUnit.getElements();
//        assertSame(UnitLength.class, elements[0].getClass());
//        TypeRapidEnum metainfUnitsLen = TypeRapidEnum.forName("UnitLength");
//        assertSame(metainfUnitsLen, metainfLengthUnit);
//
//        assertEquals(9, elements.length);
//        assertEquals("km", elements[0].getName());
//        assertEquals("m", elements[1].getName());
//        assertEquals("dm", elements[2].getName());
//        assertEquals("cm", elements[3].getName());
//        assertEquals("mm", elements[4].getName());
//        assertEquals("um", elements[5].getName());
//        assertEquals("nm", elements[6].getName());
//        assertEquals("pm", elements[7].getName());
//    }
//
//    public void testConcreteQuantityClass() {
//        Length length = new Length(1.234, UnitLength.M);
//        assertEquals(1.234, length.getMagnitudeDouble(), 0);
//        assertSame(UnitLength.M, length.getUnit());
//    }
//
//    public void testGenericQuantityType() {
//        Length len = (Length) RapidQuantity.createInstance("Length", "12.12 m");
//        assertEquals(12.12, len.getMagnitudeDouble(), 0);
//        UnitLength meter = UnitLength.M;
//        assertSame(meter, len.getUnit());
//        Length lenkm = (Length) len.convert(UnitLength.KM);
//        assertEquals(0.01212, lenkm.getMagnitudeDouble(), 0);
//    }
//
//    public void testEquals() {
//        Length length = new Length(new BigDecimal("1.234"), UnitLength.M);
//        assertEquals("1.234 m", length.toString());
//        length = new Length(1.234, UnitLength.M);
//        assertEquals("1.2339999999999999857891452847979962825775146484375 m", length.toString());
//    }
//
//    public void testConvert() {
//        Length l_nm = new Length(new BigDecimal("43.111"), UnitLength.NM);
//        Length l_pm = (Length) l_nm.convert(UnitLength.PM);
//        assertEquals(new BigDecimal("43111.000"), l_pm.getMagnitude());
//        Length l_um = (Length) l_nm.convert(UnitLength.UM);
//        assertEquals(new BigDecimal("0.043111"), l_um.getMagnitude());
//        Length l_m = (Length) l_nm.convert(UnitLength.M);
//        assertEquals(new BigDecimal("0.000000043111"), l_m.getMagnitude());
//        Length l_km = (Length) l_nm.convert(UnitLength.KM);
//        assertEquals(new BigDecimal("0.000000000043111"), l_km.getMagnitude());
//        try {
//            l_nm.convert(UnitLength.INCH);
//            assertTrue("expected to catch a "
//                + "beans.core.BBExQuantityConversionNotSupportedException here", false);
//        } catch (QuantityConversionNotSupportedException e) {
//        }
//
//        Money m_euro = new Money(BigDecimal.ONE, Currency.EURO);
//        assertEquals("euro", m_euro.getUnit().getName());
//        assertEquals("EUR", ((Currency) m_euro.getUnit()).getShort3());
//        Money m_dm = (Money) m_euro.convert(Currency.DEUTSCHMARK);
//        assertEquals(new BigDecimal("1.955383"), m_dm.getMagnitude());
//        m_dm = new Money(BigDecimal.ONE, Currency.DEUTSCHMARK);
//        m_euro = (Money) m_dm.convert(Currency.EURO);
//        assertEquals(new BigDecimal("0.511408762"), m_euro.getMagnitude());
//    }
}
