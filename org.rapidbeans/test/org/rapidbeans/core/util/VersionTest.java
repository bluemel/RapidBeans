/*
 * Rapid Beans Framework: VersionTest.java
 *
 * Copyright Martin Bluemel, 2008
 *
 * Dec 7, 2008
 */
package org.rapidbeans.core.util;

import junit.framework.TestCase;

/**
 * Tests for the StringHelper.
 *
 * @author Martin Bluemel
 */
public final class VersionTest extends TestCase {

    public void testCompareToNumbersEqual() {
        assertEquals(0, new Version("1.2.3").compareTo(new Version("1.2.3")));
    }

    public void testCompareToNumbersGreaterEqualDim() {
        assertEquals(1, new Version("1.2.4").compareTo(new Version("1.2.3")));
    }

    public void testCompareToNumbersGreaterDifferentDim1() {
        assertEquals(1, new Version("1.3").compareTo(new Version("1.2.3")));
    }

    public void testCompareToNumbersGreaterDifferentDim2() {
        assertEquals(1, new Version("1.3.1").compareTo(new Version("1.2")));
    }

    public void testCompareToNumbersLessDifferentDim1() {
        assertEquals(-1, new Version("1.2.2.4").compareTo(new Version("1.3")));
    }

    public void testCompareToNumbersLessDifferentDim2() {
        assertEquals(-1, new Version("1.3").compareTo(new Version("1.4.3")));
    }

    public void testCompareToNumbersLess() {
        assertEquals(-1, new Version("1.2.2").compareTo(new Version("1.2.3")));
    }

    public void testCompareToMixedEqual() {
        assertEquals(0, new Version("1.2.b").compareTo(new Version("1.2.b")));
    }

    public void testCompareToMixedGreater() {
        assertEquals(1, new Version("1.2.b").compareTo(new Version("1.2.a")));
    }

    public void testCompareToMixedLess() {
        assertEquals(-1, new Version("1.2.b").compareTo(new Version("1.2.c")));
    }

    public void testCompareToNumbersGreaterDifferentLengthGreater() {
        assertEquals(1, new Version("1.2.a").compareTo(new Version("1.2")));
    }

    public void testCompareToNumbersGreaterDifferentLengthLess() {
        assertEquals(-1, new Version("1.2").compareTo(new Version("1.2.1")));
    }

    public void testCompareToMixedNumberVsNonNumberGreater() {
        assertEquals(1, new Version("1.2.1").compareTo(new Version("1.2.a")));
    }

    public void testCompareToMixedNumberVsNonNumberLess() {
        assertEquals(-1, new Version("1.2.a").compareTo(new Version("1.2.1")));
    }

    public void testWeightSimple() {
        assertEquals(1.234567, new Version("1.2.3.4.5.6.7").weight());
    }

    public void testWeightWithCaracters() {
        assertEquals(1.234065, new Version("1.2.3.4.A").weight());
        assertEquals(1.234122, new Version("1.2.3.4.z").weight());
        assertEquals(1.077767, new Version("a.a.a.a.a").weight());
        assertEquals(1.077777, new Version("a.a.a.b.a").weight());
        assertEquals(1.077778, new Version("a.a.a.b.b").weight());
        assertEquals(2.00979899, new Version("2.0.abc").weight());
        assertEquals(2.00989797, new Version("2.0.baa").weight());
    }

    public void testGetNearestVersion() {
        assertEquals("1.6.0_15", new Version("1.6.0_17").getNearest(
                new Version[] {
                    new Version("1.6.0"),
                    new Version("1.6.0_20"),
                    new Version("1.6.0_15"),
                    new Version("1.6.0_21"),
                    new Version("1.1")
                }).toString());
        assertEquals("1.6.0", new Version("1.6.0_17").getNearest(
                new Version[] {
                    new Version("1.5.9"),
                    new Version("1.6.0"),
                    new Version("1.6.1"),
                    new Version("1.7.0"),
                    new Version("1.8")
                }).toString());
        assertEquals("1.6.0_17", new Version("1.6.0_17").getNearest(
                new Version[] {
                    new Version("1.5.9"),
                    new Version("1.6.0_18"),
                    new Version("1.6.0_17"),
                    new Version("1.7.0_16"),
                    new Version("1.8")
                }).toString());
    }
}
