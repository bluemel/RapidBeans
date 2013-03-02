package org.rapidbeans.ant;

import java.io.File;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TaskFileOverviewTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private static final String LF = PlatformHelper.getLineFeed();

	@Test
	public void testGenerateFolderAndFilesXMLUnlimited() {
		final File testdir = new File("testdata");
		final StringWriter writer = new StringWriter();
		TaskFileOverview.generateFolderAndFilesXML(testdir, -1, 0, writer,
				false, true, null);
		switch (PlatformHelper.getOs()) {
		case windows:
			Assert.assertTrue(writer
					.toString()
					.startsWith(
							"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
									+ LF
									+ "<folder path=\"testdata\">"
									+ LF
									+ "\t<folder path=\"testdata/test1\">"
									+ LF
									+ "\t\t<file name=\"testMergeResultExpected.txt\"/>"
									+ LF
									+ "\t\t<file name=\"testNewGen.txt\"/>"
									+ LF
									+ "\t\t<file name=\"testOldMan.txt\"/>"
									+ LF + "\t</folder>" + LF
									+ "\t<folder path=\"testdata/test2\">" + LF
									+ "\t\t<file name=\"testNewGen.txt\"/>"
									+ LF + "\t</folder>" + LF));
			break;
		default:
			System.out.println("@@@ " + writer.toString());
			Assert.assertTrue(writer
					.toString()
					.startsWith(
							"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
									+ LF
									+ "<folder path=\"testdata\">"
									+ LF
									+ "\t<folder path=\"testdata/test2\">"
									+ LF
									+ "\t\t<file name=\"testNewGen.txt\"/>"
									+ LF
									+ "\t</folder>"
									+ LF
									+ "\t<folder path=\"testdata/test5\">"
									+ LF
									+ "\t\t<file name=\"testXsltmergeResultExpected.txt\"/>"
									+ LF
									+ "\t\t<file name=\"testInput.xml\"/>"
									+ LF
									+ "\t\t<file name=\"testXsltmergeResultIOSrc.txt\"/>"
									+ LF + "\t\t<file name=\"testStyle.xsl\"/>"
									+ LF + "\t</folder>" + LF)
					|| writer
							.toString()
							.startsWith(
									"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
											+ LF
											+ "<folder path=\"testdata\">"
											+ LF
											+ "\t<folder path=\"testdata/test2\">"
											+ LF
											+ "\t\t<file name=\"testNewGen.txt\"/>"
											+ LF
											+ "\t</folder>"
											+ LF
											+ "\t<folder path=\"testdata/test4\">"
											+ LF
											+ "\t\t<file name=\"testXsltmergeResultExpected.txt\"/>"
											+ LF
											+ "\t\t<file name=\"testStyle.xsl\"/>"
											+ LF
											+ "\t\t<file name=\"testInput.xml\"/>"
											+ LF
											+ "\t</folder>"
											+ LF
											+ "\t<folder path=\"testdata/test5\">"
											+ LF
											+ "\t\t<file name=\"testXsltmergeResultExpected.txt\"/>"
											+ LF
											+ "\t\t<file name=\"testXsltmergeResultIOSrc.txt\"/>"
											+ LF
											+ "\t\t<file name=\"testStyle.xsl\"/>"
											+ LF
											+ "\t\t<file name=\"testInput.xml\"/>"
											+ LF + "\t</folder>" + LF));
			break;
		}
	}

	@Test
	public void testGenerateFolderAndFilesXML0() {
		final File testdir = new File("testdata");
		final StringWriter writer = new StringWriter();
		TaskFileOverview.generateFolderAndFilesXML(testdir, 0, 0, writer,
				false, true, null);
		Assert.assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ LF + "<folder path=\"testdata\">" + LF + "</folder>"
						+ LF, writer.toString());
	}
}
