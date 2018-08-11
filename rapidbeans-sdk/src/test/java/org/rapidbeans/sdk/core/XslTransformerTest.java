package org.rapidbeans.sdk.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.rapidbeans.sdk.utils.FileTestHelper;

public class XslTransformerTest {

	private XslTransformer xslTransformer;

	@Before
	public void setUp() {
		this.xslTransformer = new XslTransformer();
	}

	@Test
	public void testXsltWithoutParams() {
		xslTransformer.transform(new File("src/test/resources/testxslt/testIn.xml"),
				new File("src/test/resources/testxslt/testStyle.xsl"), new File("target/testxslt/test.txt"), null);
		FileTestHelper.verifyFilesEqual(new File("src/test/resources/testxslt/testOutExpected.txt"),
				new File("target/testxslt/test.txt"), true, true);
	}

	@Test
	public void testXsltWithSingleParam() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("testp1", "xxx");
		xslTransformer.transform(new File("src/test/resources/testxslt/testIn.xml"),
				new File("src/test/resources/testxslt/testStyle.xsl"), new File("target/testxslt/testParSingle.txt"),
				params);
		FileTestHelper.verifyFilesEqual(new File("src/test/resources/testxslt/testOutExpectedParSingle.txt"),
				new File("target/testxslt/testParSingle.txt"), true, true);
	}

	@Test
	public void testXsltWithMultpleParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("testp1", "xxx");
		params.put("testp2", "yyy");
		xslTransformer.transform(new File("src/test/resources/testxslt/testIn.xml"),
				new File("src/test/resources/testxslt/testStyle.xsl"), new File("target/testxslt/testParMult.txt"),
				params);
		FileTestHelper.verifyFilesEqual(new File("src/test/resources/testxslt/testOutExpectedParMult.txt"),
				new File("target/testxslt/testParMult.txt"), true, true);
	}

	@Test
	public void testXsltCreateOutdir() {
		if (new File("target/xslttest").exists()) {
			FileTestHelper.deleteDeep(new File("target/testxsl"));
		}
		xslTransformer.transform(new File("src/test/resources/testxslt/testIn.xml"),
				new File("src/test/resources/testxslt/testStyle.xsl"), new File("target/testxslt/test.txt"), null);
		FileTestHelper.verifyFilesEqual(new File("src/test/resources/testxslt/testOutExpected.txt"),
				new File("target/testxslt/test.txt"), true, true);
	}
}
