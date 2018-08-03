package org.rapidbeans.sdk.core;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ModelCodeGeneratorTest {

	private ModelCodeGenerator generator = new ModelCodeGenerator();

	@Test
	public void loadResourceStyle() throws IOException {
		File targetFolder = new File("target/rapidbeans-sdk-stylesheets");
		if (targetFolder.exists()) {
			FileUtils.deleteDirectory(targetFolder);
		}
		assertEquals(false, targetFolder.exists());
		generator.loadResourceStyle("genBean.xsl", targetFolder);
		assertEquals(true, new File(targetFolder, "genBean.xsl").exists());
	}
}
