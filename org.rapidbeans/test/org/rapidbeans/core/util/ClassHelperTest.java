/*
 * Rapid Beans Framework: UtilsClassTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Nov 9, 2005
 */
package org.rapidbeans.core.util;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JTextField;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.RapidBean;

/**
 * The Unit Tests.
 * 
 * @author Martin Bluemel
 */
public final class ClassHelperTest extends TestCase {

	public void testInheritanceSimple() {
		// Object -> Component -> Container
		assertTrue(ClassHelper.classOf(Component.class, Container.class));
	}

	public void testInheritanceMultiStep() {
		// Object -> Component -> Container
		// -> JComponent -> JTextComponent -> JTextField
		assertTrue(ClassHelper.classOf(Component.class, JTextField.class));
	}

	public void testInheritanceNegative() {
		// Object -> Component -> Container
		assertFalse(ClassHelper.classOf(Container.class, Component.class));
	}

	public void testInheritanceSame() {
		assertTrue(ClassHelper.classOf(Container.class, Container.class));
	}

	public void testImplementationSimple() {
		assertTrue(ClassHelper.classOf(Link.class, RapidBean.class));
	}
}
