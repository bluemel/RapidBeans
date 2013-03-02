/*
 * Rapid Beans Framework: PlatformHelperTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Dec 7, 2008
 */

package org.rapidbeans.core.util;

import java.util.logging.Level;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PlatformHelperTest {

	private static Level platformHelperLogLevelBefore;

	@BeforeClass
	public static void setUpClass() {
		platformHelperLogLevelBefore = PlatformHelper.getLogger().getLevel();
		PlatformHelper.getLogger().setLevel(Level.WARNING);
	}

	@AfterClass
	public static void tearDownClass() {
		PlatformHelper.getLogger().setLevel(platformHelperLogLevelBefore);
	}

	@Test
	public void testGetOs() {
		Assert.assertNotNull(PlatformHelper.getOsfamily());
	}

	@Test
	public void testGetOsName() {
		Assert.assertTrue(PlatformHelper.getOsName().length() > 0);
	}

	@Test
	public void testGetOsVersion() {
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
		PlatformHelper.reset("Windows XP", "5.1");
		Assert.assertEquals(OperatingSystem.windows_xp, PlatformHelper.getOs());
		Assert.assertEquals(new Version("5.1"), PlatformHelper.getOsVersion());
		PlatformHelper.reset(null, null);
	}

	@Test
	public void testWin2k() {
		PlatformHelper.reset("Windows 2000", "5.0");
		Assert.assertEquals(OperatingSystem.windows_unknown_old, PlatformHelper.getOs());
		Assert.assertEquals(new Version("5.0"), PlatformHelper.getOsVersion());
		PlatformHelper.reset(null, null);
	}

	@Test
	public void testWinNt() {
		PlatformHelper.reset("Windows NT", "4.0");
		Assert.assertEquals(OperatingSystem.windows_unknown_old, PlatformHelper.getOs());
		Assert.assertEquals(new Version("4.0"), PlatformHelper.getOsVersion());
		PlatformHelper.reset(null, null);
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
