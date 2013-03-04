/*
 * Rapid Beans Framework: FileHelper.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 11/09/2005
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

package org.rapidbeans.core.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.rapidbeans.core.basic.PropertyDate;
import org.rapidbeans.core.common.PrecisionDate;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.UtilException;

/**
 * A helper class for files handling.
 * 
 * @author Martin Bluemel
 */
public final class FileHelper {

	private static final Logger log = Logger.getLogger(FileHelper.class.getName());

	/**
	 * copies directory trees.
	 * 
	 * @param src
	 *            the source directory
	 * @param tgt
	 *            the target directory
	 */
	public static void copyDeep(final File src, final File tgt) {
		copyDeep(src, tgt, true, false);
	}

	/**
	 * copies directory trees.
	 * 
	 * @param src
	 *            the source directory
	 * @param tgt
	 *            the target directory
	 * @param force
	 *            the force flag
	 */
	public static void copyDeep(final File src, final File tgt, final boolean force) {
		copyDeep(src, tgt, force, false);
	}

	/**
	 * copies directory trees.
	 * 
	 * @param src
	 *            the source directory
	 * @param tgt
	 *            the target directory
	 * @param force
	 *            the force flag
	 * @param excludeSvnDirs
	 *            flag
	 */
	public static void copyDeep(final File src, final File tgt, final boolean force, final boolean excludeSvnDirs) {
		if (!src.exists()) {
			throw new UtilException("source folder \"" + src.getAbsolutePath() + "\" not found.");
		}
		if (!src.isDirectory()) {
			throw new UtilException("source file \"" + src.getAbsolutePath() + "\" isn't a folder.");
		}
		if (!tgt.exists()) {
			if (!tgt.mkdirs()) {
				throw new UtilException("Creation of target folder \"" + tgt.getAbsolutePath() + "\" failed.");
			}
		}
		if (!tgt.isDirectory()) {
			throw new UtilException("target file \"" + tgt.getAbsolutePath() + "\" isn't a folder.");
		}
		File[] subfiles = src.listFiles();
		File subtgt;
		for (int i = 0; i < subfiles.length; i++) {
			subtgt = new File(tgt, subfiles[i].getName());
			if (subfiles[i].isDirectory()) {
				if ((!excludeSvnDirs) || (!subfiles[i].getName().equals(".svn"))) {
					copyDeep(subfiles[i], subtgt, force, excludeSvnDirs);
				}
			} else {
				copyFile(subfiles[i], subtgt, force);
			}
		}
	}

	/**
	 * copies a source file over a target file.
	 * 
	 * @param src
	 *            the source file
	 * @param tgt
	 *            the target file
	 */
	public static void copyFile(final File src, final File tgt) {
		copyFile(src, tgt, false);
	}

	/**
	 * the buffer size for copy.
	 */
	private static final int BUF_SIZE = 4096;

	/**
	 * the time to sleep.
	 */
	private static final int SLEEP_TIME_100 = 100;

	/**
	 * copies a source file over a target file.
	 * 
	 * @param src
	 *            the source file
	 * @param tgt
	 *            the target file
	 * @param force
	 *            forces copy regardless of modification dates
	 */
	public static void copyFile(final File src, final File tgt, final boolean force) {
		if (!src.exists()) {
			throw new UtilException("source file \"" + src.getAbsolutePath() + "\" not found.");
		}
		if (tgt.exists()) {
			if (!tgt.canWrite()) {
				throw new UtilException("target file \"" + tgt.getAbsolutePath() + "\" is not writable.");
			}
		}

		if (force || src.lastModified() > tgt.lastModified()) {
			byte[] buf = new byte[BUF_SIZE];
			try {
				FileInputStream is = new FileInputStream(src);
				FileOutputStream os = new FileOutputStream(tgt);
				int bytesRead;
				while ((bytesRead = is.read(buf)) != -1) {
					if (bytesRead > 0) {
						os.write(buf, 0, bytesRead);
					} else {
						Thread.sleep(SLEEP_TIME_100);
					}
				}
				is.close();
				os.close();
			} catch (InterruptedException e) {
				throw new UtilException("InterruptedException: " + e.getMessage());
			} catch (FileNotFoundException e) {
				throw new UtilException("FileNotFound: " + e.getMessage());
			} catch (IOException e) {
				throw new UtilException("IOException: " + e.getMessage());
			}
		}
	}

	/**
	 * Deletes a whole directory tree.
	 * 
	 * @param del
	 *            the directory or file to delete
	 */
	public static void deleteDeep(final File del) {
		deleteDeep(del, false);
	}

	/**
	 * Deletes a whole directory tree.
	 * 
	 * @param del
	 *            the directory or file to delete
	 * @param force
	 *            enforces deletion of read only files and directories
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

	/**
	 * Compares all files in a directory tree.
	 * 
	 * @param dir1
	 *            the first directory
	 * @param sdir2
	 *            the path to the second directory
	 * 
	 * @return true if all files equal, false if there are more ore less or
	 *         different files.
	 */
	public static boolean dirsEqual(final File dir1, final String sdir2) {
		return dirsEqual(dir1, new File(sdir2), -1, false);
	}

	/**
	 * Compares all files in a directory tree but exclude directories that start
	 * with dot.
	 * 
	 * @param dir1
	 *            the first directory
	 * @param sdir2
	 *            the path to the second directory
	 * 
	 * @return true if all files equal, false if there are more ore less or
	 *         different files.
	 */
	public static boolean dirsEqualExcludeDotDirs(final File dir1, final String sdir2) {
		return dirsEqual(dir1, new File(sdir2), -1, true);
	}

	/**
	 * Compares all files in a directory tree in a configurable manner. - you
	 * can configure the depth of the compare - you can configure if sub
	 * directories that start with . should be excluded from the comparison.
	 * This is useful for instance if the test data is version controlled with
	 * Subversion.
	 * 
	 * directories that start with dot.
	 * 
	 * @param dir1
	 *            the first directory
	 * @param dir2
	 *            the second directory
	 * @param depth
	 *            the depth of comparison in the directory tree. To specify
	 *            infinite depth just set this parameter to a negative number e.
	 *            g. -1.
	 * @param excludedotdirs
	 *            if true sub directories that start with . are excluded from
	 *            comparison.
	 * 
	 * @return true if all files equal, false if there are more ore less or
	 *         different files.
	 */
	public static boolean dirsEqual(final File dir1, final File dir2, final int depth, final boolean excludedotdirs) {
		boolean equals = true;

		if (!dir1.exists()) {
			throw new UtilException("folder \"" + dir1.getAbsolutePath() + "\" not found.");
		}
		if (!dir1.isDirectory()) {
			throw new UtilException("folder \"" + dir1.getAbsolutePath() + "\" is not a directory.");
		}
		if (!dir2.exists()) {
			throw new UtilException("folder \"" + dir2.getAbsolutePath() + "\" not found.");
		}
		if (!dir2.isDirectory()) {
			throw new UtilException("folder \"" + dir2.getAbsolutePath() + "\" is not a directory.");
		}

		if (!dir1.getName().equals(dir2.getName())) {
			log.fine("folders have different names:");
			log.fine("- folder 1: " + dir1.getAbsolutePath());
			log.fine("- folder 2: " + dir2.getAbsolutePath());
			equals = false;
		} else {
			File[] subfiles1 = null;
			File[] subfiles2 = null;
			if (excludedotdirs) {
				subfiles1 = listFilesExcludeFilter(dir1, ".*");
			} else {
				subfiles1 = dir1.listFiles();
			}
			if (excludedotdirs) {
				subfiles2 = listFilesExcludeFilter(dir2, ".*");
			} else {
				subfiles2 = dir2.listFiles();
			}

			if (subfiles1.length != subfiles2.length) {
				log.fine("folders have a different count of subfiles:");
				log.fine("- folder 1: " + dir1.getAbsolutePath());
				log.fine("- folder 2: " + dir2.getAbsolutePath());
				equals = false;
			}

			for (int i = 0; i < subfiles1.length && equals; i++) {
				if (subfiles1[i].isDirectory()) {
					if (depth < 0) {
						equals = dirsEqual(subfiles1[i], subfiles2[i], depth, excludedotdirs);
					} else if (depth > 1) {
						equals = dirsEqual(subfiles1[i], subfiles2[i], depth - 1, excludedotdirs);
					}
				} else {
					equals = filesEqual(subfiles1[i], subfiles2[i]);
				}
			}
		}

		return equals;
	}

	/**
	 * compares the contents of two files byte per byte.
	 * 
	 * @param file1
	 *            the first file
	 * @param file2
	 *            the second file
	 * 
	 * @return true if the files' content is equal,<br/>
	 *         false if there are differences
	 */
	public static boolean filesEqual(final File file1, final File file2) {
		return filesEqual(file1, file2, false, false);
	}

	/**
	 * compares the contents of two files byte per byte.
	 * 
	 * @param file1
	 *            the first file
	 * @param file2
	 *            the second file
	 * @param differentNamesAllowed
	 *            if different file names are allowed
	 * @param compareLineByLine
	 *            for text files only. You'll get the line where the first
	 *            difference occurs in the error message
	 * 
	 * @return true if the files' content is equal,<br/>
	 *         false if there are differences
	 */
	public static boolean filesEqual(final File file1, final File file2, final boolean differentNamesAllowed,
			final boolean compareLineByLine) {
		boolean equals = true;

		if (!file1.exists()) {
			throw new UtilException("file \"" + file1.getAbsolutePath() + "\" not found.");
		}
		if (!file2.exists()) {
			throw new UtilException("file \"" + file2.getAbsolutePath() + "\" not found.");
		}
		if (!file1.isFile()) {
			throw new UtilException("file \"" + file1.getAbsolutePath() + "\" not a normal file.");
		}
		if (!file2.isFile()) {
			return false;
		}

		if (!differentNamesAllowed) {
			if (!file1.getName().equals(file2.getName())) {
				log.fine("files have different names:");
				log.fine("- file 1: " + file1.getAbsolutePath());
				log.fine("- file 2: " + file2.getAbsolutePath());
				equals = false;
			}
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
					if (l2 == null) {
						log.fine("files have different number of lines:");
						log.fine("- file 1: " + file1.getAbsolutePath());
						log.fine("- file 2: " + file2.getAbsolutePath());
						log.fine(" line " + r1.getLineNumber() + " of file 1 not" + " found in file 2");
						equals = false;
						break;
					}
					if (!l1.equals(l2)) {
						log.fine("files differ:");
						log.fine("- file 1: " + file1.getAbsolutePath());
						log.fine("  line " + r1.getLineNumber() + ": \"" + l1 + "\"");
						log.fine("- file 2: " + file2.getAbsolutePath());
						log.fine("  line " + r2.getLineNumber() + ": \"" + l2 + "\"");
						equals = false;
						break;
					}
					l1 = r1.readLine();
					l2 = r2.readLine();
				}

				if (equals && l2 != null) {
					log.fine("files have different number of lines:");
					log.fine("- file 1: " + file1.getAbsolutePath());
					log.fine("- file 2: " + file2.getAbsolutePath());
					equals = false;
				}
			} else {
				is1 = new FileInputStream(file1);
				is2 = new FileInputStream(file2);

				int i1 = is1.read();
				int i2 = is2.read();
				while (i1 != -1) {
					if (i1 != i2) {
						log.fine("files differ:");
						log.fine("- file 1: " + file1.getAbsolutePath());
						log.fine("- file 2: " + file2.getAbsolutePath());
						equals = false;
						break;
					}
					i1 = is1.read();
					i2 = is2.read();
				}

				if (equals && i2 != -1) {
					log.fine("files have different length:");
					log.fine("- file 1: " + file1.getAbsolutePath());
					log.fine("- file 2: " + file2.getAbsolutePath());
					equals = false;
				}
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
		return equals;
	}

	/**
	 * List all files in a directory except the files with names matching the
	 * given pattern.
	 * 
	 * @param dir
	 *            the directory to list
	 * @param filter
	 *            the filter pattern for filenames to exclude
	 * 
	 * @return the found files and sub directories except the ones matching the
	 *         pattern
	 */
	public static File[] listFilesExcludeFilter(final File dir, final String filter) {
		ArrayList<File> l = new ArrayList<File>();
		File[] files = dir.listFiles();
		File[] excludedFiles = dir.listFiles(new FileFilterRegExp(filter));
		for (File file : files) {
			if (!containsFileWithName(excludedFiles, file.getName())) {
				l.add(file);
			}
		}
		return (File[]) l.toArray(FILE_ARRAY);
	}

	/**
	 * Search trough an array of File objects for a file with the given name.
	 * 
	 * @param array
	 *            the File array to search through
	 * @param filename
	 *            the file name to search for
	 * 
	 * @return true if the file has been found or null if no file has been found
	 */
	public static boolean containsFileWithName(final File[] array, final String filename) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].getName().equals(filename)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates a directory / folder according to the given file handle. Parent
	 * directories / folders will also be created if necessary.
	 * 
	 * @param dir
	 *            specifies the directory / folder to be created.
	 */
	public static void mkdirs(final File dir) {
		final ArrayList<File> parentDirsToCreate = new ArrayList<File>();
		File parentDir = dir.getParentFile();
		while (parentDir != null && (!parentDir.exists())) {
			parentDirsToCreate.add(parentDir);
			parentDir = parentDir.getParentFile();
		}
		final int len = parentDirsToCreate.size();
		int i = len - 1;
		for (; i >= 0; i--) {
			if (!parentDirsToCreate.get(i).mkdir()) {
				if (i < (len - 1)) {
					parentDirsToCreate.get(len - 1).delete();
				}
				throw new RapidBeansRuntimeException("Creation of directory \"" + dir.getAbsolutePath() + "\" failed");
			}
		}
		if (!dir.mkdir()) {
			if (i < (len - 1)) {
				parentDirsToCreate.get(len - 1).delete();
			}
			throw new RapidBeansRuntimeException("Creation of directory \"" + dir.getAbsolutePath() + "\" failed");
		}
	}

	public static File backup(final File file) {
		return backup(file, "bak");
	}

	public static File backup(final File file, final String extension) {
		if (file.isDirectory()) {
			throw new RapidBeansRuntimeException("Can not backup directory \"" + file.getAbsolutePath()
					+ "\". Only flat files can be backed up.");
		}
		if (!file.exists()) {
			throw new RapidBeansRuntimeException("Can not backup non existing file \"" + file.getAbsolutePath() + "\".");
		}
		String backupFileName = tmpFilename(file);
		if (extension != null) {
			backupFileName += "." + extension;
		}
		final File backupFile = new File(file.getParentFile(), backupFileName);
		copyFile(file, backupFile);
		return backupFile;
	}

	public static String tmpFilename(final File file) {
		return tmpFilename(file.getName());
	}

	public static String tmpFilename(final String filename) {
		return basename(filename) + "_" + backupDateString() + "." + extension(filename);
	}

	public static String backupDateString() {
		return PropertyDate.format(new Date(), PrecisionDate.second);
	}

	public static String basename(final File file) {
		return basename(file.getName());
	}

	public static String basename(final String filename) {
		return StringHelper.splitBeforeLast(StringHelper.splitLast(filename, "/\\"), ".");
	}

	public static String extension(final File file) {
		return extension(file.getName());
	}

	public static String extension(final String filename) {
		return StringHelper.splitLast(StringHelper.splitLast(filename, "/\\"), ".");
	}

	/**
	 * example instance for array creation.
	 */
	private static final File[] FILE_ARRAY = new File[0];

	/**
	 * prevent default constructor from being used.
	 */
	private FileHelper() {
	}

	public static void append(File file, String string) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, true);
			final int len = string.length();
			for (int i = 0; i < len; i++) {
				fos.write(string.charAt(i));
			}
		} catch (IOException e) {
			throw new UtilException(e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					throw new UtilException(e);
				}
			}
		}
	}

	public static void changeCharAt(final File file, final int index, final char newChar) {
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			is = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
			int c;
			while ((c = is.read()) != -1) {
				bos.write(c);
			}
			is.close();
			is = null;
			byte[] ba = bos.toByteArray();
			ba[index] = (byte) newChar;
			os = new FileOutputStream(file);
			os.write(ba);
			os.close();
		} catch (IOException e) {
			throw new UtilException(e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				throw new UtilException(e);
			}
		}
	}

	/**
	 * Present a shortened platform specific shortend path
	 * 
	 * @param path
	 *            the path to shorten
	 * @param maxStart
	 *            the maximal start string length (except we have one single
	 *            start path component;
	 * @param shortcutSign
	 *            the shortcut sign
	 * @param maxEnd
	 *            the maximal end string length (except we have one single start
	 *            path component;
	 * 
	 * @return the shortened string
	 */
	public static String shortenPathCenter(final String string, final int maxStart, String shortcutSign, int maxEnd) {
		if (string.length() <= maxStart + maxEnd) {
			return string;
		}
		String separator;
		if (string.contains("/")) {
			separator = "/";
		} else if (string.contains("\\")) {
			separator = "\\";
		} else {
			return string;
		}
		final List<String> pathComponents = StringHelper.split(string, separator);
		final StringBuffer buf = new StringBuffer();
		int index = 0;
		while (buf.length() < maxStart && index < pathComponents.size()) {
			buf.append(pathComponents.get(index));
			buf.append(File.separatorChar);
			index++;
		}
		index--;
		final StringBuffer bufEnd = new StringBuffer();
		int indexEnd = pathComponents.size() - 1;
		while (bufEnd.length() < maxEnd && indexEnd > index) {
			bufEnd.insert(0, pathComponents.get(indexEnd));
			if (indexEnd - index > 1) {
				bufEnd.insert(0, File.separatorChar);
			}
			indexEnd--;
		}
		if (indexEnd - index > 1) {
			buf.append(shortcutSign);
		}
		buf.append(bufEnd);
		return buf.toString();
	}
}
