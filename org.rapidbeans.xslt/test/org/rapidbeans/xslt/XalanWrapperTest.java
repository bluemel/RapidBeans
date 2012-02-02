package org.rapidbeans.xslt;

import java.io.File;

import org.junit.Test;

public class XalanWrapperTest {

	@Test
	public void testXslt() {
		File in = new File("testdata/content.xml");
		File style = new File("testdata/style01.xsl");
		File out = new File("testdata/out.txt");
		XalanWrapper.xslt(in, style, out, null);
	}
}
