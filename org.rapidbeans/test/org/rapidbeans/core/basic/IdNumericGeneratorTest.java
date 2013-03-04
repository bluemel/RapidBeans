package org.rapidbeans.core.basic;

import junit.framework.TestCase;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;

/**
 * JUnit tests.
 * 
 * @author Martin Bluemel
 */
public class IdNumericGeneratorTest extends TestCase {

	/**
	 * a new generator begins to generate id 1, 2, 3, and so on.
	 */
	public void testGenerateNumber() {
		IdGeneratorNumeric generator = new IdGeneratorNumeric();
		assertEquals(1, generator.generateIdValue().intValue());
		assertEquals(2, generator.generateIdValue().intValue());
		assertEquals(3, generator.generateIdValue().intValue());
	}

	/**
	 * After having generated ID 2,147,483,647 = 2^31-1 = Long.MAX_VALUE the
	 * generator continues with negative numbers from -2,147,483,648 = 2^31-1 =
	 * Long.MIN_VALUE up to -1.
	 */
	public void testGenerateNumberMaxInt() {
		IdGeneratorNumeric generator = new IdGeneratorNumeric();
		generator.notifiyIdExisists(2147483645);
		assertEquals(2147483646, generator.generateIdValue().intValue());
		assertEquals(Integer.MAX_VALUE, generator.generateIdValue().intValue());
		assertEquals(Integer.MIN_VALUE, generator.generateIdValue().intValue());
		assertEquals(-2147483647, generator.generateIdValue().intValue());
		assertEquals(-2147483646, generator.generateIdValue().intValue());
	}

	/**
	 * test the boundary condition. The generator should generate a -1 as last
	 * number and afterwards switch to 0, 0 is not given out but is the boundary
	 */
	public void testGenerateNumberBoundary1() {
		IdGeneratorNumeric generator = new IdGeneratorNumeric();
		generator.notifiyIdExisists(-3);
		assertEquals(-2, generator.generateIdValue().intValue());
		assertEquals(-1, generator.generateIdValue().intValue());
		try {
			generator.generateIdValue().intValue();
			fail("expected RapidBeansRuntimeException");
		} catch (RapidBeansRuntimeException e) {
			assertTrue(true);
		}
	}

	/**
	 * test case method.
	 */
	public void testSetMaxGenNumberPositive() {
		IdGeneratorNumeric generator = new IdGeneratorNumeric();
		generator.notifiyIdExisists(247110815);
		assertEquals(247110816, generator.generateIdValue().intValue());
		assertEquals(247110817, generator.generateIdValue().intValue());
		assertEquals(247110818, generator.generateIdValue().intValue());
	}

	/**
	 * test case method.
	 */
	public void testSetMaxGenNumberNegative() {
		IdGeneratorNumeric generator = new IdGeneratorNumeric();
		generator.notifiyIdExisists(-247110815);
		assertEquals(-247110814L, generator.generateIdValue().intValue());
		assertEquals(-247110813L, generator.generateIdValue().intValue());
		assertEquals(-247110812L, generator.generateIdValue().intValue());
	}

	/**
	 * test the boundary condition.
	 */
	public void testSetMaxGenNumberMinus1() {
		IdGeneratorNumeric generator = new IdGeneratorNumeric();
		generator.notifiyIdExisists(-1);
		try {
			generator.generateIdValue().intValue();
			fail("expected RapidBeansRuntimeException");
		} catch (RapidBeansRuntimeException e) {
			assertTrue(true);
		}
	}

	/**
	 * test the boundary condition.
	 */
	public void testSetMaxGenNumberZero() {
		IdGeneratorNumeric generator = new IdGeneratorNumeric();
		generator.notifiyIdExisists(0);
		assertEquals(1, generator.generateIdValue().intValue());
		assertEquals(2, generator.generateIdValue().intValue());
		assertEquals(3, generator.generateIdValue().intValue());
	}

	/**
	 * test COMPACT mode and realeasing a number.
	 */
	public void testReleaseNumber() {
		IdGeneratorNumeric generator = new IdGeneratorNumeric();
		generator.setMode(IdGeneratorNumeric.GENERATION_STRATEGY_COMPACT);
		assertEquals(1, generator.generateIdValue().intValue());
		assertEquals(2, generator.generateIdValue().intValue());
		assertEquals(3, generator.generateIdValue().intValue());
		assertEquals(4, generator.generateIdValue().intValue());
		generator.releaseNumber(2);
		generator.releaseNumber(3);
		assertEquals(3, generator.generateIdValue().intValue());
		assertEquals(2, generator.generateIdValue().intValue());
		assertEquals(5, generator.generateIdValue().intValue());
		assertEquals(6, generator.generateIdValue().intValue());
	}
}
