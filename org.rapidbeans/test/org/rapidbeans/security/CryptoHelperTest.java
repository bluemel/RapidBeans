/*
 * Rapid Beans Framework: CryptoHelperTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * June 07, 2008
 */
package org.rapidbeans.security;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.logging.Level;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rapidbeans.core.util.PlatformHelper;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.test.codegen.Person;

/**
 * Test symmetric encryption.
 * 
 * @author Martin Bluemel
 */
public class CryptoHelperTest {

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
	public void testDecryptString() {
		Assert.assertEquals("", CryptoHelper.decrypt("12345678911234567892123456789312345678941"));
	}

	@Test
	public void testEncryptDecryptString() {
		String senc = CryptoHelper.encrypt("ThisWorksFine4Me", "Flaschenbier");
		Assert.assertEquals("JG1DAcYIdn/v4D9Pwuwns0wRFZVvMv4Oz1O+lR+phIssA1WU8Tr5+A==", senc);
		Assert.assertEquals("ThisWorksFine4Me", CryptoHelper.decrypt(senc, "Flaschenbier"));
		Assert.assertEquals("1234", CryptoHelper.decrypt(CryptoHelper.encrypt("1234")));
		Assert.assertTrue(CryptoHelper.decrypt(CryptoHelper.encrypt("123")).equals("123") == false);
		Assert.assertTrue(CryptoHelper.decrypt(CryptoHelper.encrypt("")).equals("") == false);
	}

	@Test
	public void testEncryptDecryptStringWrongKeyPhrase() throws IOException, GeneralSecurityException {
		String s = "ThisWorksFine4Me";
		String senc = CryptoHelper.encrypt(s, "Flaschenbier");
		Assert.assertFalse(s.equals(CryptoHelper.decrypt(senc, "XXX")));
	}

	@Test
	public void testEncryptFile() throws IOException, GeneralSecurityException {
		String s = "ThisWorksFine4Me";
		String senc = CryptoHelper.encrypt(s, "Flaschenbier");
		File testfile = new File("testdata/cryptoTest.txt");
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(testfile), "UTF-8");
		out.write(senc);
		out.close();
		InputStreamReader in = new InputStreamReader(new FileInputStream(testfile), "UTF-8");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int c;
		while ((c = in.read()) != -1) {
			bos.write(c);
		}
		in.close();
		String sencRead = bos.toString();
		Assert.assertEquals(senc, sencRead);
		Assert.assertEquals(s, CryptoHelper.decrypt(sencRead, "Flaschenbier"));
		testfile.delete();
	}

	@Test
	public void testEncrypt() throws IOException, GeneralSecurityException {
		String s = "ThisWorksFine4Me";
		String senc = CryptoHelper.encrypt(s, "Flaschenbier");
		Assert.assertEquals(s, CryptoHelper.decrypt(senc, "Flaschenbier"));

		// write encrypted attribute value to a document read again and decrypt
		File testfile = new File("testdata/cryptoTest.xml");
		Person pw = new Person();
		pw.setSurname(senc);
		pw.setPrename("Hugo");
		pw.setDateofbirth(new Date(0));
		Document docw = new Document(pw);
		docw.setUrl(testfile.toURI().toURL());
		docw.save();
		Document docr = new Document(testfile);
		Person pr = (Person) docr.getRoot();
		String sencR = pr.getSurname();
		Assert.assertEquals(senc, sencR);
		Assert.assertEquals(s, CryptoHelper.decrypt(sencR, "Flaschenbier"));
		testfile.delete();
	}
}
