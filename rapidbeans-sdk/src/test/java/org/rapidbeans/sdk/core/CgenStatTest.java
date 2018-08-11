package org.rapidbeans.sdk.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CgenStatTest {

	@Test
	public void testNanosToReadable() {
		assertEquals("00:00.000", /*            */ CgenStat.nanosToReadable(0L));
		assertEquals("00:00.123", /*    */ CgenStat.nanosToReadable(123000000L));
		assertEquals("00:15.123", /*  */ CgenStat.nanosToReadable(15123000000L));
		assertEquals("01:31.123", /*  */ CgenStat.nanosToReadable(91123000000L));
		assertEquals("2:24:31.123", CgenStat.nanosToReadable(((2 * 3600 + 24 * 60 + 31) * 1000000000L) + 123000000L));
	}
}
