/**
 * 
 */
package org.rapidbeans.core.math;

import java.math.BigInteger;

import junit.framework.TestCase;

/**
 * @author Martin Bluemel
 */
public class NumberTest extends TestCase {

	public void testBigIntegerDivision() {
		BigInteger i = new BigInteger("10");
		assertEquals(new BigInteger("3"), i.divide(new BigInteger("3")));
		assertEquals(9223372036854775807L, Long.MAX_VALUE);
		i = new BigInteger("1000000000000000000000000");
		assertEquals(new BigInteger("333333333333333333333333"),
				i.divide(new BigInteger("3")));
	}
}
