/*
 * Rapid Beans Framework: PlatformHelperTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Dec 7, 2008
 */

package org.rapidbeans.core.util;

import org.junit.Assert;
import org.junit.Test;

public class PlatformHelperTest {

	@Test
	public void testGetOs() {
		Assert.assertNotNull(PlatformHelper.getOsfamily());
	}

	@Test
	public void testGetOsName() {
		System.out.println(PlatformHelper.getOsfamily().toString());
		System.out.println(PlatformHelper.getOs().toString());
		Assert.assertTrue(PlatformHelper.getOsName().length() > 0);
	}

	@Test
	public void testGetOsVersion() {
		System.out.println(PlatformHelper.getOsVersion().toString());
		Assert.assertTrue(PlatformHelper.getOsVersion().toString().length() > 0);
	}

	@Test
	public void testGetLineFeed() {
		switch (PlatformHelper.getOsfamily()) {
		case windows:
			Assert.assertEquals("\r\n", PlatformHelper.getLineFeed());
			break;
		case linux:
			Assert.assertEquals("\n", PlatformHelper.getLineFeed());
			break;
		case mac:
			Assert.assertEquals("\n", PlatformHelper.getLineFeed());
			break;
		default:
			Assert.fail();
		}
	}

	@Test
	public void testWinXp() {
		String currentOsName = PlatformHelper.getOsName();
		Version currentVersion = PlatformHelper.getOsVersion();
		PlatformHelper.determineSystem("Windows XP", "5.1");
		Assert.assertEquals(OperatingSystem.windows_xp, PlatformHelper.getOs());
		Assert.assertEquals(new Version("5.1"), PlatformHelper.getOsVersion());
		PlatformHelper
				.determineSystem(currentOsName, currentVersion.toString());
	}

	@Test
	public void testWin2k() {
		String currentOsName = PlatformHelper.getOsName();
		Version currentVersion = PlatformHelper.getOsVersion();
		PlatformHelper.determineSystem("Windows 2000", "5.0");
		Assert.assertEquals(OperatingSystem.windows_unknown_old,
				PlatformHelper.getOs());
		Assert.assertEquals(new Version("5.0"), PlatformHelper.getOsVersion());
		PlatformHelper
				.determineSystem(currentOsName, currentVersion.toString());
	}

	@Test
	public void testWinNt() {
		String currentOsName = PlatformHelper.getOsName();
		Version currentVersion = PlatformHelper.getOsVersion();
		PlatformHelper.determineSystem("Windows NT", "4.0");
		Assert.assertEquals(OperatingSystem.windows_unknown_old,
				PlatformHelper.getOs());
		Assert.assertEquals(new Version("4.0"), PlatformHelper.getOsVersion());
		PlatformHelper
				.determineSystem(currentOsName, currentVersion.toString());
	}

	@Test
	public void testUsername() {
		Assert.assertTrue(PlatformHelper.username().length() > 0);
	}

	@Test
	public void testHostname() {
		Assert.assertTrue(PlatformHelper.hostname().length() > 0);
	}
}
