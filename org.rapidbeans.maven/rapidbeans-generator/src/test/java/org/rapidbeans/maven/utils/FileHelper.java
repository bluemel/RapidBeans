/*
 * Rapid Beans Framework, SDK, Maven Plugin: FileHelper.java
 *
 * Copyright (C) 2013 Martin Bluemel
 *
 * Creation Date: 26.01.2013
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
package org.rapidbeans.maven.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * A helper class for files handling.
 * 
 * @author Martin Bluemel
 */
public final class FileHelper
{

  /**
   * compares the contents of two files byte per byte.
   * 
   * @param file1
   *          the first file
   * @param file2
   *          the second file
   * 
   * @return true if the files' content is equal,<br/>
   *         false if there are differences
   */
  public static boolean filesEqual(final File file1, final File file2)
  {
    return filesEqual(file1, file2, false, false);
  }

  /**
   * compares the contents of two files byte per byte.
   * 
   * @param file1
   *          the first file
   * @param file2
   *          the second file
   * @param differentNamesAllowed
   *          if different file names are allowed
   * @param compareLineByLine
   *          for text files only. You'll get the
   *          line where the first difference occurs in the error message
   * 
   * @return true if the files' content is equal,<br/>
   *         false if there are differences
   */
  public static boolean filesEqual(final File file1, final File file2,
      final boolean differentNamesAllowed,
      final boolean compareLineByLine)
  {
    boolean equals = true;

    if (!file1.exists())
    {
      throw new UtilException("file \"" + file1.getAbsolutePath() + "\" not found.");
    }
    if (!file2.exists())
    {
      throw new UtilException("file \"" + file2.getAbsolutePath() + "\" not found.");
    }
    if (!file1.isFile())
    {
      throw new UtilException("file \"" + file1.getAbsolutePath() + "\" not a normal file.");
    }
    if (!file2.isFile())
    {
      return false;
    }

    if (!differentNamesAllowed)
    {
      if (!file1.getName().equals(file2.getName()))
      {
        System.out.println("files have different names:");
        System.out.println("- file 1: " + file1.getAbsolutePath());
        System.out.println("- file 2: " + file2.getAbsolutePath());
        equals = false;
      }
    }

    FileInputStream is1 = null;
    FileInputStream is2 = null;
    LineNumberReader r1 = null;
    LineNumberReader r2 = null;
    try
    {
      if (compareLineByLine)
      {
        r1 = new LineNumberReader(new InputStreamReader(new FileInputStream(file1)));
        r2 = new LineNumberReader(new InputStreamReader(new FileInputStream(file2)));

        String l1 = r1.readLine();
        String l2 = r2.readLine();
        while (l1 != null)
        {
          if (l2 == null)
          {
            System.out.println("files have different number of lines:");
            System.out.println("- file 1: " + file1.getAbsolutePath());
            System.out.println("- file 2: " + file2.getAbsolutePath());
            System.out.println(" line " + r1.getLineNumber() + " of file 1 not" + " found in file 2");
            equals = false;
            break;
          }
          if (!l1.equals(l2))
          {
            System.out.println("files differ:");
            System.out.println("- file 1: " + file1.getAbsolutePath());
            System.out.println("  line " + r1.getLineNumber() + ": \"" + l1 + "\"");
            System.out.println("- file 2: " + file2.getAbsolutePath());
            System.out.println("  line " + r2.getLineNumber() + ": \"" + l2 + "\"");
            equals = false;
            break;
          }
          l1 = r1.readLine();
          l2 = r2.readLine();
        }

        if (equals && l2 != null)
        {
          System.out.println("files have different number of lines:");
          System.out.println("- file 1: " + file1.getAbsolutePath());
          System.out.println("- file 2: " + file2.getAbsolutePath());
          equals = false;
        }
      } else
      {
        is1 = new FileInputStream(file1);
        is2 = new FileInputStream(file2);

        int i1 = is1.read();
        int i2 = is2.read();
        while (i1 != -1)
        {
          if (i1 != i2)
          {
            System.out.println("files differ:");
            System.out.println("- file 1: " + file1.getAbsolutePath());
            System.out.println("- file 2: " + file2.getAbsolutePath());
            equals = false;
            break;
          }
          i1 = is1.read();
          i2 = is2.read();
        }

        if (equals && i2 != -1)
        {
          System.out.println("files have different length:");
          System.out.println("- file 1: " + file1.getAbsolutePath());
          System.out.println("- file 2: " + file2.getAbsolutePath());
          equals = false;
        }
      }
    } catch (IOException e)
    {
      throw new UtilException(e);
    } finally
    {
      try
      {
        if (is1 != null)
        {
          is1.close();
        }
        if (is2 != null)
        {
          is2.close();
        }
        if (r1 != null)
        {
          r1.close();
        }
        if (r2 != null)
        {
          r2.close();
        }
      } catch (IOException e)
      {
        throw new UtilException(e);
      }
    }
    return equals;
  }

}
