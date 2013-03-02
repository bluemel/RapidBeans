/*
 * Rapid Beans Framework: OperatingSystemTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Dec 7, 2008
 */

package org.rapidbeans.core.util;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import junit.framework.TestCase;

public class OperatingSystemTest extends TestCase {

	public void testMBean() {
		OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
		assertNotNull(os);
		// System.out.println("OS name: " + os.getName());
		// System.out.println("OS version: " + os.getVersion());
		// System.out.println("OS architecture: " + os.getArch());
		// System.out.println("OS available processors: " +
		// os.getAvailableProcessors());
		// System.out.println("OS system load average: " +
		// os.getSystemLoadAverage());
	}
}
