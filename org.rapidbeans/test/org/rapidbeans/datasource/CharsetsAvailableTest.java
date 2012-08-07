/**
 * 
 */
package org.rapidbeans.datasource;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * @author Bluemel1.Martin
 *
 */
public class CharsetsAvailableTest {

	@Test
	public void test() {
		CharsetsAvailable utf8 = CharsetsAvailable.UTF_8;
		assertSame(utf8, CharsetsAvailable.getInstance("UTF-8"));
	}
}
