/*
 * Rapid Beans Framework: SexTest.java
 *
 * created on 16.01.2007
 *
 * (c) Martin Bluemel, 2007
 */
package org.rapidbeans.domain.org;

import org.rapidbeans.domain.org.Sex;

import junit.framework.TestCase;

/**
 *
 * @author Martin Bluemel
 *
 */
public class SexTest extends TestCase {

    /**
     * Test method for {@link org.rapidbeans.core.basic.GenericEnum#name()}.
     */
    public void testGetName() {
        Sex s = Sex.male;
        assertEquals("male", s.name());
    }

    /**
     * Test method for {@link org.rapidbeans.core.basic.GenericEnum#ordinal()}.
     */
    public void testGetOrder() {
        Sex s = Sex.female;
        assertEquals(1, s.ordinal());
    }
}
