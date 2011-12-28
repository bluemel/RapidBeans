/*
 * Rapid Beans Framework: TypeRapidBeanTest.java
 *
 * Copyright Martin Bluemel, 2006
 *
 * 30.08.2006
 */

package org.rapidbeans.core.type;

import java.util.Collection;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.type.TypeProperty;

import junit.framework.TestCase;

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
        RapidBean bean1 = RapidBean.createInstance("org.rapidbeans.test.TestBeanGen");
        Collection<TypeProperty> proptypes1 = bean1.getType().getPropertyTypes();
        assertEquals(9, proptypes1.size());

        RapidBean bean2 = RapidBean.createInstance("org.rapidbeans.test.TestBeanExtended1Gen");
        Collection<TypeProperty> proptypes2 = bean2.getType().getPropertyTypes();
        assertEquals(10, proptypes2.size());

        RapidBean bean3 = RapidBean.createInstance("org.rapidbeans.test.TestBeanExtended2Gen");
        Collection<TypeProperty> proptypes3 = bean3.getType().getPropertyTypes();
        assertEquals(11, proptypes3.size());
    }
}
