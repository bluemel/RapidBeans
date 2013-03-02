/*
 * Rapid Beans Framework: CryptoHelper.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 06/07/2008
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copies of the GNU Lesser General Public License and the
 * GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

package org.rapidbeans.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.presentation.ApplicationManager;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Simple encryption and decryption.
 * 
 * @author Martin Bluemel
 */
public class CryptoHelper {

	private static final BASE64Encoder BENC = new BASE64Encoder();

	private static final BASE64Decoder BDEC = new BASE64Decoder();

	private static final int BUFSIZE = 8192;

	private static final int HASH_LEN = 20;

	private static final String HASH_ALG = "SHA1";

	private static final String ENC_ALG = "TripleDES";

	/**
	 * Encrypt a single string.
	 * 
	 * @param sIn
	 *            the string to encrypt
	 * 
	 * @return the encrypted string
	 */
	public static String encrypt(final String sIn) {
		return encrypt(sIn, getDefaultKeyPhrase());
	}

	/**
	 * Encrypt a single string.
	 * 
	 * @param sIn
	 *            the string to encrypt
	 * @param keyPhrase
	 *            the keyPhrase
	 * 
	 * @return the encrypted string
	 */
	public static String encrypt(final String sIn, final String keyPhrase) {
		try {
			final MessageDigest md = MessageDigest.getInstance(HASH_ALG);
			final DigestInputStream digestIn = new DigestInputStream(
					new ByteArrayInputStream(sIn.getBytes()), md);
			final byte[] buffer = new byte[BUFSIZE];
			while (digestIn.read(buffer) != -1)
				;
			digestIn.close();
			final Cipher cipher = getCipher(true, keyPhrase);
			final ByteArrayInputStream srcStream = new ByteArrayInputStream(
					sIn.getBytes());
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final CipherOutputStream cipherStream = new CipherOutputStream(out,
					cipher);
			cipherStream.write(md.digest());
			int len;
			while ((len = srcStream.read(buffer)) != -1)
				cipherStream.write(buffer, 0, len);
			srcStream.close();
			cipherStream.close();
			return BENC.encode(out.toByteArray());
		} catch (IOException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (GeneralSecurityException e) {
			throw new RapidBeansRuntimeException(e);
		}
	}

	/**
	 * Decrypt a string.
	 * 
	 * @param keyPhrase
	 *            the key phrase
	 * 
	 * @return the decrypted string
	 */
	public static String decrypt(final String sIn) {
		return decrypt(sIn, getDefaultKeyPhrase());
	}

	/**
	 * Decrypt a string.
	 * 
	 * @param keyPhrase
	 *            the key phrase
	 * @param sIn
	 *            the string to decrypt
	 * 
	 * @return the decrypted string
	 */
	public static String decrypt(final String sIn, final String keyPhrase) {
		try {
			final byte[] originalHash = new byte[HASH_LEN];
			final Cipher cipher = getCipher(false, keyPhrase);
			final CipherInputStream cipherStream = new CipherInputStream(
					new ByteArrayInputStream(BDEC.decodeBuffer(sIn)), cipher);
			cipherStream.read(originalHash);
			final MessageDigest md = MessageDigest.getInstance(HASH_ALG);
			final DigestInputStream digestStream = new DigestInputStream(
					cipherStream, md);
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[BUFSIZE];
			int len;
			while ((len = digestStream.read(buffer)) != -1)
				out.write(buffer, 0, len);
			digestStream.close();
			// final byte[] computedHash = md.digest();
			// if (!(Arrays.equals(originalHash, computedHash))) {
			// throw new RapidBeansRuntimeException("hashs do not equal");
			// }
			return out.toString();
		} catch (IOException e) {
			throw new RapidBeansRuntimeException(e);
		} catch (GeneralSecurityException e) {
			throw new RapidBeansRuntimeException(e);
		}
	}

	private static String getDefaultKeyPhrase() {
		if (ApplicationManager.getApplication() != null) {
			return ApplicationManager.getApplication().getClass().getName();
		}
		return "1Q2w3E4r";
	}

	/**
	 * 
	 * @param keyPhrase
	 * @param srcFileName
	 * @param dstFileName
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static void encryptFile(final String keyPhrase,
			final String srcFileName, final String dstFileName)
			throws IOException, GeneralSecurityException {
		final MessageDigest md;
		final CipherOutputStream cipherStream;

		// MessageDigest-Exemplar erzeugen
		md = MessageDigest.getInstance(HASH_ALG);
		// Einen Stream mit diesem Exemplar erzeugen
		final DigestInputStream digestStream = new DigestInputStream(
				new FileInputStream(srcFileName), md);

		// Datei einmal einlesen, um den Hash zu berechnen
		byte[] buffer = new byte[BUFSIZE];
		while (digestStream.read(buffer) != -1)
			;
		digestStream.close();

		// Hash abrufen
		byte[] hash = md.digest();

		// Cipher-Objekt initialisieren
		Cipher cipher = getCipher(true, keyPhrase);

		final FileInputStream srcStream = new FileInputStream(srcFileName);

		// Cipher-Stream ueber einem FileOutputStream erzeugen
		cipherStream = new CipherOutputStream(
				new FileOutputStream(dstFileName), cipher);

		// Hash verschluesselt in die Datei schreiben
		cipherStream.write(hash);
		int len;
		// Daten aus der Quelldatei in den Cipher-Stream schreiben
		while ((len = srcStream.read(buffer)) != -1)
			cipherStream.write(buffer, 0, len);
		srcStream.close();
		cipherStream.close();
	}

	/**
	 * Decrypt a file.
	 * 
	 * @param keyPhrase
	 * @param srcFileName
	 * @param dstFileName
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static boolean decryptFile(final String keyPhrase,
			final String srcFileName, final String dstFileName)
			throws IOException, GeneralSecurityException {
		CipherInputStream cipherStream;
		FileOutputStream dstStream;
		MessageDigest md;
		DigestInputStream digestStream;
		byte[] originalHash = new byte[HASH_LEN];
		byte[] computedHash = new byte[HASH_LEN];

		Cipher cipher = getCipher(false, keyPhrase);

		cipherStream = new CipherInputStream(new FileInputStream(srcFileName),
				cipher);
		cipherStream.read(originalHash);
		md = MessageDigest.getInstance(HASH_ALG);
		digestStream = new DigestInputStream(cipherStream, md);

		dstStream = new FileOutputStream(dstFileName);
		byte[] buffer = new byte[BUFSIZE];
		int len;
		while ((len = digestStream.read(buffer)) != -1)
			dstStream.write(buffer, 0, len);
		digestStream.close();
		dstStream.close();
		computedHash = md.digest();
		return Arrays.equals(originalHash, computedHash);
	}

	/**
	 * create a cipher for symmetric encryption or decryption.
	 * 
	 * @param encrypt
	 *            determines if the cipher should be used for encryption or
	 *            decryption
	 * @param keyPhrase
	 *            the key phrase
	 * @return the cipher
	 * @throws IOException
	 *             if IO fails
	 * @throws GeneralSecurityException
	 *             if a general security problem occurs
	 */
	protected static Cipher getCipher(final boolean encrypt,
			final String keyPhrase) throws IOException,
			GeneralSecurityException {
		byte[] rawKey = getSymmetricKey(keyPhrase);
		final Key key = new SecretKeySpec(new DESedeKeySpec(rawKey).getKey(),
				ENC_ALG);
		final Cipher cipher = Cipher.getInstance(ENC_ALG);
		if (encrypt) {
			cipher.init(Cipher.ENCRYPT_MODE, key);
		} else {
			cipher.init(Cipher.DECRYPT_MODE, key);
		}
		return cipher;
	}

	/**
	 * Compute hash and prepare key.
	 * 
	 * @param keyPhrase
	 *            the key phrase
	 * @return a byte array that serves a symmetric key for encryption or
	 *         decryption
	 * @throws IOException
	 *             in case of IO problems
	 * @throws GeneralSecurityException
	 *             in case of problems in the security library
	 */
	protected static byte[] getSymmetricKey(final String keyPhrase)
			throws IOException, GeneralSecurityException {
		MessageDigest md = MessageDigest.getInstance(HASH_ALG);
		byte[] digest = md.digest(keyPhrase.getBytes());
		byte[] rawKey = new byte[24];
		System.arraycopy(digest, 0, rawKey, 0, 8);
		System.arraycopy(digest, 8, rawKey, 8, 8);
		System.arraycopy(digest, 0, rawKey, 16, 8);
		return rawKey;
	}
}
