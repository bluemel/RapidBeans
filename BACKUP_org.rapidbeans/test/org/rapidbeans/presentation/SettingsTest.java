/*
 * Rapid Beans Framework: SettingsTest.java
 * 
 * Copyright Martin Bluemel, 2006
 * 
 * Dec 15, 2006
 */
package org.rapidbeans.presentation;

import junit.framework.TestCase;

import org.rapidbeans.presentation.settings.SettingsAll;
import org.rapidbeans.presentation.settings.SettingsBasic;
import org.rapidbeans.presentation.settings.SettingsBasicGui;

/**
 * Unit tests for class SettingsAll.
 * 
 * @author Martin Bluemel
 */
public final class SettingsTest extends TestCase {

	/**
	 * compensate missing test down of client.
	 */
	public void setUp() {
		if (ApplicationManager.getApplication() != null) {
			ApplicationManager.resetApplication();
		}
	}

	public void tearDown() {
		if (ApplicationManager.getApplication() != null) {
			ApplicationManager.resetApplication();
		}
	}

	/**
	 * Test method for constructor SettingsAll()'.
	 */
	public void testBBSettings() {
		SettingsAll settings = new SettingsAll();
		assertNotNull(settings);
		SettingsBasic basic = settings.getBasic();
		assertNull(basic.getFolderdoc());
		SettingsBasicGui gui = basic.getGui();
		assertSame(OpenWindowBehaviour.maximized, gui.getDocViewOpenWindowBehaviour());
	}
}
