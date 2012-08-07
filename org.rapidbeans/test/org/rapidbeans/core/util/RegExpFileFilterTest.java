/*
 * Rapid Beans Framework: FileFilterRegExp.java
 *
 * Copyright Martin Bluemel, 2008
 *
 * Nov 9, 2005
 */
package org.rapidbeans.core.util;

import java.io.File;

import junit.framework.TestCase;

/**
 * The Unit Tests for the filter with regular expressions.
 *
 * @author Martin Bluemel
 */
public final class RegExpFileFilterTest extends TestCase {

    /**
     * test the accept method with some regular expressions.
     * The constructor implicitly is tested with that
     */
    public void testAccept() {
        FileFilterRegExp filter = new FileFilterRegExp("*.xml");
        assertTrue(filter.accept(new File("test.xml")));
        assertTrue(filter.accept(new File(".xml")));
        assertTrue(filter.accept(new File("§$%.xml")));
        assertFalse(filter.accept(new File("test.xmlx")));
        assertFalse(filter.accept(new File("xml.java")));
    }
}

