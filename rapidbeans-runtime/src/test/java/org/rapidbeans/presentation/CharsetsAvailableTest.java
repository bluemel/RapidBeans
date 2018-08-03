/*
 * RapidBeans Framework: CharsetsAvailableTest.java
 * 
 * Copyright Martin Bluemel, 2007
 * 
 * 18.11.2007
 */
package org.rapidbeans.presentation;

import org.rapidbeans.datasource.CharsetsAvailable;

import junit.framework.TestCase;

/**
 * Test written after having problems with this generic enum after having re
 * desigend it.
 * 
 * @author Martin Bluemel
 */
public class CharsetsAvailableTest extends TestCase {

	/**
	 * test get a specific character set.
	 */
	public void testGetInstance() {
		CharsetsAvailable iso = (CharsetsAvailable) CharsetsAvailable.getInstance("ISO-8859-1");
		assertEquals("ISO-8859-1", iso.name());
		assertTrue(
				"unexpected ordinal " + Integer.toString(iso.ordinal()) + " for character set \"" + iso.name() + "\".",
				iso.ordinal() == 53 || iso.ordinal() == 54 || iso.ordinal() == 55 || iso.ordinal() == 56);
		// JDK 1.5: 53, JDK 1.6: 54, JDK 1.6 Linux: 55, JDK 1.10: 56
	}

	/**
	 * test character set constants.
	 */
	public void testConstants() {
		assertSame(CharsetsAvailable.ISO_8859_1, CharsetsAvailable.getInstance("ISO-8859-1"));
		assertSame(CharsetsAvailable.UTF_8, CharsetsAvailable.getInstance("UTF-8"));
		assertSame(CharsetsAvailable.UTF_16, CharsetsAvailable.getInstance("UTF-16"));
	}
}
