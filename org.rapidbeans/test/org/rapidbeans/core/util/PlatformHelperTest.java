/*
 * Rapid Beans Framework: PlatformHelperTest.java
 *
 * Copyright Martin Bluemel, 2008
 *
 * Dec 7, 2008
 */

package org.rapidbeans.core.util;

import junit.framework.TestCase;

public class PlatformHelperTest extends TestCase {

    public void testGetOs() {
        assertNotNull(PlatformHelper.getOs());
    }

    public void testGetOsName() {
        assertTrue(PlatformHelper.getOsName().length() > 0);
    }

    public void testGetOsVersion() {
        assertTrue(PlatformHelper.getOsVersion().toString().length() > 0);
    }

    public void testGetLineFeed() {
        switch (PlatformHelper.getOs()) {
        case windows:
            assertEquals("\r\n", PlatformHelper.getLineFeed());
            break;
        case linux:
            assertEquals("\n", PlatformHelper.getLineFeed());
            break;
        case mac:
            assertEquals("\n", PlatformHelper.getLineFeed());
            break;
        default:
            fail();
        }
    }

    public void testUsername() {
        assertTrue(PlatformHelper.username().length() > 0);
    }

    public void testHostname() {
        assertTrue(PlatformHelper.hostname().length() > 0);
    }
}
