/*
 * Rapid Beans Framework: TimeTest.java
 *
 * Copyright Martin Bluemel, 2008
 *
 * Nov 14, 2005
 */
package org.rapidbeans.domain.math;

import org.rapidbeans.domain.math.Time;
import org.rapidbeans.domain.math.UnitTime;

import junit.framework.TestCase;

/**
 * Unit tests for class Time.
 *
 * @author Martin Bluemel
 */
public final class TimeTest extends TestCase {

    /**
     * test constructor with string.
     */
    public void testTimeConvert() {
        Time time = new Time("2 h");
        assertEquals(new Time("7200 s"), time.convert(UnitTime.s));
    }
}
