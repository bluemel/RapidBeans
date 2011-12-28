/*
 * Rapid Beans Framework: RapidBeansTypeLoaderTest.java
 *
 * Copyright Martin Bluemel, 2008
 *
 * Nov 4, 2005
 */
package org.rapidbeans.core.type;

import java.util.List;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.RapidEnum;
import org.rapidbeans.domain.org.Sex;

/**
 * Unit Tests for class RapidBeansTypeLoader.
 *
 * @author Martin Bluemel
 */
public final class RapidBeansTypeLoaderTest extends TestCase {

    /**
     * test get the type from it's name.
     */
    public void testGetMetainfoFromEnumTypeNameGenerated() {
        // every concrete enum class is loaded automatically when its metainfo
        // is retrieved by enum type name which is the class name
        TypeRapidEnum metainfSex = TypeRapidEnum.forName("org.rapidbeans.domain.org.Sex");
        assertNotNull(metainfSex);
        // assert that the right enum elements are loaded
        List<RapidEnum> enumSexElements = metainfSex.getElements();
        assertSame(Sex.male, enumSexElements.get(0));
        assertSame(Sex.female, enumSexElements.get(1));
    }

//    /**
//     * test get the type from it's name.
//     */
//    public void testGetMetainfoFromEnumTypeNameGeneric() {
//        // also a generic enum class can be loaded automatically
//        // when its metainfo is retrieved by enum type name
//        // if its XML description can be found
//        // (RapidBeansTypeLoader and ClassLoader)
//        TypeRapidEnum enumtypeLang = TypeRapidEnum.forName("org.rapidbeans.test.Lang");
//        assertNotNull(enumtypeLang);
//        assertEquals(INT_6, enumtypeLang.getElements().length);
//        assertEquals("english", enumtypeLang.elementOf(0).getName());
//        assertEquals("spanish", enumtypeLang.elementOf(1).getName());
//        assertEquals("portugese", enumtypeLang.elementOf(2).getName());
//        assertEquals("chinese", enumtypeLang.elementOf(INT_3).getName());
//        assertEquals("french", enumtypeLang.elementOf(INT_4).getName());
//        assertEquals("german", enumtypeLang.elementOf(INT_5).getName());
//    }
}
