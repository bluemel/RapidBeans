/*
 * Rapid Beans Framework: TypeRapidBeanTest.java
 * 
 * Copyright Martin Bluemel, 2006
 * 
 * 30.08.2006
 */

package org.rapidbeans.core.type;

import java.util.Collection;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.basic.RapidBeanImplParent;

/**
 * Tests for class TypeRapidBean.
 * 
 * @author Martin Bluemel
 */
public class TypeRapidBeanTest extends TestCase {

	/**
	 * as the name says.
	 */
	public void testInheritanceWithGenericBeans() {
		RapidBean bean1 = RapidBeanImplParent.createInstance("org.rapidbeans.test.TestBeanGen");
		Collection<TypeProperty> proptypes1 = bean1.getType().getPropertyTypes();
		assertEquals(17, proptypes1.size());

		RapidBean bean2 = RapidBeanImplParent.createInstance("org.rapidbeans.test.TestBeanExtended1Gen");
		Collection<TypeProperty> proptypes2 = bean2.getType().getPropertyTypes();
		assertEquals(18, proptypes2.size());

		RapidBean bean3 = RapidBeanImplParent.createInstance("org.rapidbeans.test.TestBeanExtended2Gen");
		Collection<TypeProperty> proptypes3 = bean3.getType().getPropertyTypes();
		assertEquals(19, proptypes3.size());
	}
}
