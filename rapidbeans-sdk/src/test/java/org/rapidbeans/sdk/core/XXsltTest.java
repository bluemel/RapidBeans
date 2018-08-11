package org.rapidbeans.sdk.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.rapidbeans.sdk.utils.FileTestHelper;

import com.google.common.io.Files;

public class XXsltTest {

	private TestSdkLogger testLogger;

	@Before
	public void setUp() {
		testLogger = new TestSdkLogger();
		testLogger.setLevel(TestSdkLogger.Level.INFO);
	}

	@Test
	public void testXXsltWithoutParams() {
		xxslt(new File("src/test/resources/testxslt/testIn.xml"), new File("src/test/resources/testxslt/testStyle.xsl"),
				new File("target/testxslt/test.txt"), null, true, false);
		FileTestHelper.verifyFilesEqual(new File("src/test/resources/testxslt/testOutExpected.txt"),
				new File("target/testxslt/test.txt"), true, true);
	}

	@Test
	public void testXXsltWithSingleParam() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("testp1", "xxx");
		xxslt(new File("src/test/resources/testxslt/testIn.xml"), new File("src/test/resources/testxslt/testStyle.xsl"),
				new File("target/testxslt/testParSingle.txt"), params, true, false);
		FileTestHelper.verifyFilesEqual(new File("src/test/resources/testxslt/testOutExpectedParSingle.txt"),
				new File("target/testxslt/testParSingle.txt"), true, true);
	}

	@Test
	public void testXXsltWithMultpleParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("testp1", "xxx");
		params.put("testp2", "yyy");
		xxslt(new File("src/test/resources/testxslt/testIn.xml"), new File("src/test/resources/testxslt/testStyle.xsl"),
				new File("target/testxslt/testParMult.txt"), params, true, false);
		FileTestHelper.verifyFilesEqual(new File("src/test/resources/testxslt/testOutExpectedParMult.txt"),
				new File("target/testxslt/testParMult.txt"), true, true);
	}

	@Test
	public void testXXsltCreateOutdir() {
		if (new File("target/xslttest").exists()) {
			FileTestHelper.deleteDeep(new File("target/testxsl"));
		}
		xxslt(new File("src/test/resources/testxslt/testIn.xml"), new File("src/test/resources/testxslt/testStyle.xsl"),
				new File("target/testxslt/test.txt"), null, true, false);
		FileTestHelper.verifyFilesEqual(new File("src/test/resources/testxslt/testOutExpected.txt"),
				new File("target/testxslt/test.txt"), true, true);
	}

	@Test
	public void testNoForceOutputUpToDate() throws InterruptedException {
		// ensure generated file is there
		xxslt(new File("src/test/resources/testxslt/testIn.xml"), new File("src/test/resources/testxslt/testStyle.xsl"),
				new File("target/testxslt/test.txt"), null, true, false);
		long outLastModifiedBefore = new File("target/testxslt/test.txt").lastModified();
		// wait a blink of time
		Thread.sleep(10);
		// try again without force => should not execute
		xxslt(new File("src/test/resources/testxslt/testIn.xml"), new File("src/test/resources/testxslt/testStyle.xsl"),
				new File("target/testxslt/test.txt"), null, false, false);
		long outLastModifiedAfter = new File("target/testxslt/test.txt").lastModified();
		assertEquals(outLastModifiedBefore, outLastModifiedAfter);
	}

	@Test
	public void testNoForceOutputNotUpToDateIn() throws InterruptedException, IOException {
		// ensure generated file is there
		xxslt(new File("src/test/resources/testxslt/testIn.xml"), new File("src/test/resources/testxslt/testStyle.xsl"),
				new File("target/testxslt/test.txt"), null, true, false);
		long outLastModifiedBefore = new File("target/testxslt/test.txt").lastModified();
		// wait a blink of time
		Thread.sleep(10);
		Files.touch(new File("src/test/resources/testxslt/testIn.xml"));
		// try again without force: should execute
		xxslt(new File("src/test/resources/testxslt/testIn.xml"), new File("src/test/resources/testxslt/testStyle.xsl"),
				new File("target/testxslt/test.txt"), null, false, false);
		long outLastModifiedAfter = new File("target/testxslt/test.txt").lastModified();
		assertTrue(outLastModifiedAfter > outLastModifiedBefore);
	}

	@Test
	public void testNoForceOutputNotUpToDateStyle() throws InterruptedException, IOException {
		// ensure generated file is there
		xxslt(new File("src/test/resources/testxslt/testIn.xml"), new File("src/test/resources/testxslt/testStyle.xsl"),
				new File("target/testxslt/test.txt"), null, true, false);
		long outLastModifiedBefore = new File("target/testxslt/test.txt").lastModified();
		// wait a blink of time
		Thread.sleep(10);
		Files.touch(new File("src/test/resources/testxslt/testStyle.xsl"));
		// try again without force: should execute
		xxslt(new File("src/test/resources/testxslt/testIn.xml"), new File("src/test/resources/testxslt/testStyle.xsl"),
				new File("target/testxslt/test.txt"), null, false, false);
		long outLastModifiedAfter = new File("target/testxslt/test.txt").lastModified();
		assertTrue(outLastModifiedAfter > outLastModifiedBefore);
	}

	@Test
	public void testWithMergeConsecutive() throws IOException {
		if (!new File("target/testxslt").exists()) {
			assertTrue(new File("target/testxslt").mkdirs());
		}
		Files.copy(new File("src/test/resources/testxslt/testOutWithProtectedRegionsBefore.txt"),
				new File("target/testxslt/test.txt"));
		Map<String, String> params = new HashMap<String, String>();
		params.put("testp1", "a b c");
		params.put("testp2", "w x y\nzzz");
		xxslt(new File("src/test/resources/testxslt/testIn.xml"),
				new File("src/test/resources/testxslt/testStyleWithProtectedRegions.xsl"),
				new File("target/testxslt/test.txt"), params, true, true);
		FileTestHelper.verifyFilesEqual(
				new File("src/test/resources/testxslt/testOutExpectedWithProtectedRegionsMerged.txt"),
				new File("target/testxslt/test.txt"), true, true);
	}

	@Test
	public void testWithMergeFirst() throws IOException {
		if (!new File("target/testxslt").exists()) {
			assertTrue(new File("target/testxslt").mkdirs());
		}
		if (new File("target/testxslt/test.txt").exists()) {
			assertTrue(new File("target/testxslt/test.txt").delete());
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("testp1", "a b c");
		params.put("testp2", "w x y\nzzz");
		xxslt(new File("src/test/resources/testxslt/testIn.xml"),
				new File("src/test/resources/testxslt/testStyleWithProtectedRegions.xsl"),
				new File("target/testxslt/test.txt"), params, true, true);
		FileTestHelper.verifyFilesEqual(
				new File("src/test/resources/testxslt/testOutExpectedWithProtectedRegionsMergedFirst.txt"),
				new File("target/testxslt/test.txt"), true, true);
	}

	private void xxslt(File in, File style, File out, Map<String, String> parameters, boolean force, boolean merge) {
		XXslt xxslt = new XXslt(testLogger);
		xxslt.setIn(in);
		xxslt.setStyle(style);
		xxslt.setOut(out);
		xxslt.setParameters(parameters);
		xxslt.setForce(force);
		xxslt.setMerge(merge);
		xxslt.execute(new XslTransformer());
	}
}
