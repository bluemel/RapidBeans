package org.rapidbeans.sdk.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class FileTestHelper {

	/**
	 * Deletes a whole directory tree.
	 * 
	 * @param del the directory or file to delete
	 */
	public static void deleteDeep(final File del) {
		deleteDeep(del, true);
	}

	/**
	 * Deletes a whole directory tree.
	 * 
	 * @param del   the directory or file to delete
	 * @param force enforces deletion of read only files and directories
	 */
	public static void deleteDeep(final File del, final boolean force) {
		if (del.canWrite() || force) {
			if (del.isDirectory()) {
				File[] subfiles = del.listFiles();
				for (final File file : subfiles) {
					deleteDeep(file, force);
				}
				if (!del.delete()) {
					throw new UtilException("Could not delete directory \"" + del.getAbsolutePath() + "\"");
				}
			} else {
				if (!del.delete()) {
					throw new UtilException("Could not delete file \"" + del.getAbsolutePath() + "\"");
				}
			}
		}
	}

	public static void verifyFilesEqual(final File file1, final File file2, final boolean differentNamesAllowed,
			final boolean compareLineByLine) {
		if (!file1.exists()) {
			throw new UtilException("file \"" + file1.getAbsolutePath() + "\" not found.");
		}
		if (!file2.exists()) {
			throw new UtilException("file \"" + file2.getAbsolutePath() + "\" not found.");
		}
		if (!file1.isFile()) {
			throw new UtilException("file \"" + file1.getAbsolutePath() + "\" not a normal file.");
		}
		assertTrue("File to compare \"" + file2.getAbsolutePath() + "\" is no file.", file2.isFile());

		if (!differentNamesAllowed) {
			assertEquals("files have different names:\n" + "- file 1: " + file1.getAbsolutePath() + "\n" + "- file 2: "
					+ file2.getAbsolutePath() + "\n", file1.getName(), file2.getName());
		}

		FileInputStream is1 = null;
		FileInputStream is2 = null;
		LineNumberReader r1 = null;
		LineNumberReader r2 = null;
		try {
			if (compareLineByLine) {
				r1 = new LineNumberReader(new InputStreamReader(new FileInputStream(file1)));
				r2 = new LineNumberReader(new InputStreamReader(new FileInputStream(file2)));

				String l1 = r1.readLine();
				String l2 = r2.readLine();
				while (l1 != null) {
					assertNotNull("files have different number of lines:\n" + "- file 1: " + file1.getAbsolutePath()
							+ "\n" + "- file 2: " + file2.getAbsolutePath() + "\n" + "line " + r1.getLineNumber()
							+ " of file 1 not" + " found in file 2", l2);
					assertEquals("files differ:\n" + "- file 1: " + file1.getAbsolutePath() + "\n" + "  line "
							+ r1.getLineNumber() + ": \"" + l1 + "\"\n" + "- file 2: " + file2.getAbsolutePath() + "\n"
							+ "  line " + r2.getLineNumber() + ": \"" + l2 + "\"\n", l1, l2);
					l1 = r1.readLine();
					l2 = r2.readLine();
				}
				assertNull("files have different number of lines:\n" + "- file 1: " + file1.getAbsolutePath() + "\n"
						+ "- file 2: " + file2.getAbsolutePath(), l2);
			} else {
				is1 = new FileInputStream(file1);
				is2 = new FileInputStream(file2);

				int i1 = is1.read();
				int i2 = is2.read();
				while (i1 != -1) {
					assertTrue("files differ:\n" + "- file 1: " + file1.getAbsolutePath() + "\n" + "- file 2: "
							+ file2.getAbsolutePath(), i1 == i2);
					i1 = is1.read();
					i2 = is2.read();
				}
				assertTrue("files have different length:\n" + "- file 1: " + file1.getAbsolutePath() + "\n"
						+ "- file 2: " + file2.getAbsolutePath(), i2 != -1);
			}
		} catch (IOException e) {
			throw new UtilException(e);
		} finally {
			try {
				if (is1 != null) {
					is1.close();
				}
				if (is2 != null) {
					is2.close();
				}
				if (r1 != null) {
					r1.close();
				}
				if (r2 != null) {
					r2.close();
				}
			} catch (IOException e) {
				throw new UtilException(e);
			}
		}
	}
}
