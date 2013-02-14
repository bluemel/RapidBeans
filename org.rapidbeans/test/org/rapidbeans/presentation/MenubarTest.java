/*
 * Rapid Beans Framework: MenubarTest.java
 * 
 * Copyright Martin Bluemel, 2006
 * 
 * Dec 15, 2006
 */
package org.rapidbeans.presentation;

import junit.framework.TestCase;

/**
 * Unit tests for class Menubar.
 * 
 * @author Martin Bluemel
 */
public final class MenubarTest extends TestCase {

	/**
	 * Test method for constructor SettingsAll()'.
	 */
	public void testBBSettings() {
		Menubar bean = new Menubar();
		assertNotNull(bean);
		assertNull(bean.getName());
		bean.setName("asdf");
		assertEquals("asdf", bean.getName());
	}
}
