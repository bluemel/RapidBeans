/*
 * Rapid Beans Framework, SDK, Ant Tasks: StringHelper.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 11/01/2005
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

package org.rapidbeans.ant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.tools.ant.BuildException;

/**
 * Helper class for string processing.
 *
 * @author Martin Bluemel
 */
public final class StringHelper {

    /**
     * Convert the first character of the given string to upper cases.
     *
     * @param string the string to convert
     *
     * @return the converted string
     */
    public static String upperFirstCharacter(final String string) {
        final char[] ca = string.toCharArray();
        ca[0] = Character.toUpperCase(ca[0]);
        return new String(ca);
    }

    public enum StripMode {

        /**
         * strip leading and trailing characters.
         */
        both,

        /**
         * strip only leading characters
         */
        leading,

        /**
         * strip only trailing characters
         */
        trailing,
    }

    private static final char[] WHITESPACE_CHARACTERS = {' ', '\n', '\t'};

    private static final String WHITESPACE_CHARACTER_STRING = new String(WHITESPACE_CHARACTERS);

    /**
     * Check if the given string contains only digits or not.
     *
     * @param string the string that will be checked
     *
     * @return true if the given string contains only digits,<br/>
     *         otherwise false.
     */
    public static boolean isDigitsOnly(final String string) {
        final int length = string.length();
        for (int i = 0; i < length; i++) {
            final char c = string.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * Implements a more powerful mechanism to remove or filter
     * leading and trailing characters than String.trim().<br/>
     * Instead of simply removing whitespace characters you can
     * specify an arbitrary character to be removed.<br/>
     * This variant removes the specified character from
     * both sides.
     *
     * @param string the string to strip
     * @param stripCharacter specifies the character to remove or filter.
     *
     * @return the filtered string
     */
    public static String strip(final String string, final char stripCharacter) {
        return strip(string, new char[]{stripCharacter}, StripMode.both);
    }

    /**
     * Implements a more powerful mechanism to remove or filter
     * leading and trailing characters than String.trim().<br/>
     * Instead of simply removing whitespace characters you can
     * specify arbitrary characters to be removed.<br/>
     * This variant removes the specified characters from
     * both sides.
     *
     * @param string the string to strip
     * @param stripCharacters specifies a list of characters to remove or filter.
     *
     * @return the filtered string
     */
    public static String strip(final String string, final char[] stripCharacters) {
        return strip(string, stripCharacters, StripMode.both);
    }
 
    /**
     * Implements a more powerful mechanism to remove or filter
     * leading and trailing whitespace characters than String.trim().<br/>
     * Additionally to simply removing whitespace on both string ends
     * you can specify if only leading or trailing characters are
     * removed.
     *
     * @param string the string to strip
     * @param stripMode specifies if only leading or trailing characters are
     *                  removed or if removal happens on both sides.
     *
     * @return the filtered string
     */
    public static String strip(final String string, final StripMode mode) {
        return strip(string, WHITESPACE_CHARACTERS, mode);
    }

    /**
     * Implements a more powerful mechanism to remove or filter
     * leading and trailing characters than String.trim().<br/>
     * Instead of simply removing whitespace can specify a character
     * to be removed.<br/>
     * Additionally you can specify the characters are removed from
     * both sides or if only leading or trailing characters are
     * removed.
     *
     * @param string the string to strip
     * @param stripCharacters specifies a list of characters to remove or filter.
     * @param stripMode specifies if only leading or trailing characters are
     *                  removed or if removal happens on both sides.
     *
     * @return the filtered string
     */
    public static String strip(final String string,
            final char trimChar, final StripMode mode) {
        return strip(string, new char[]{trimChar}, mode);
    }

    /**
     * Implements a more powerful mechanism to remove or filter
     * leading and trailing characters than String.trim().<br/>
     * Instead of simply removing whitespace can specify the characters
     * to be removed.<br/>
     * Additionally you can specify the characters are removed from
     * both sides or if only leading or trailing characters are
     * removed.
     *
     * @param string the string to strip
     * @param stripCharacters specifies a list of characters to remove or filter.
     * @param stripMode specifies if only leading or trailing characters are
     *                  removed or if removal happens on both sides.
     *
     * @return the filtered string
     */
    public static String strip(final String string,
            final char[] trimChars, final StripMode mode) {
        final int len = string.length();
        int firstNonStrippedIndex = 0;
        int lastNonStrippedIndex = len;
        if (mode == StripMode.both || mode == StripMode.leading) {
            firstNonStrippedIndex = -1;
            while (firstNonStrippedIndex < len
                    && charMatches(trimChars, string.charAt(++firstNonStrippedIndex)));
        }
        if (mode == StripMode.both || mode == StripMode.trailing) {
            lastNonStrippedIndex = len;
            while (lastNonStrippedIndex >= firstNonStrippedIndex
                    && charMatches(trimChars, string.charAt(--lastNonStrippedIndex)));
            lastNonStrippedIndex++;
        }
        String s = string;
        if (firstNonStrippedIndex > 0 || lastNonStrippedIndex < len) {
            s = string.substring(firstNonStrippedIndex, lastNonStrippedIndex);
        }
        return s;
    }

    public enum FillMode {

        /**
         * fill up the left side.
         */
        left,

        /**
         * fill up the right side
         */
        right,
    }

    /**
     * A primitive helper for formatting strings by simply
     * filling it up on the left or on the right side.
     *
     * @param string the string
     * @param length the length to fill the string up to
     * @param fillChar the fill character
     * @param mode FILL_MODE_LEFT or FILL_MODE_RIGHT
     *
     * @return the string filled up to the specified length
     */
    public static String fillUp(final String string, final int length,
            final char fillChar, final FillMode mode) {
        final int charsToFillCount = length - string.length();
        final StringBuffer buf = new StringBuffer(length);
        if (mode == FillMode.left) {
            buf.append(newCharArray(charsToFillCount, fillChar));
        }
        buf.append(string);
        if (mode == FillMode.right) {
            buf.append(newCharArray(charsToFillCount, fillChar));
        }
        return buf.toString();
    }

    private static char[] newCharArray(final int charsToFillCount, final char fillChar) {
        final char[] ca = new char[charsToFillCount];
        Arrays.fill(ca, fillChar);
        return ca;
    }

    /**
     * the pattern to build String arrays.
     */
    private static final String[] EMPTY_STRING_ARRAY = {};

    /**
     * Splits a string into tokens using any whitespace character
     * a delimiter.
     *
     * @param string the string to split.
     *
     * @return a list containing all tokens
     */
    public static List<String> split(final String string) {
        return split(string, WHITESPACE_CHARACTER_STRING);
    }

    /**
     * Splits a string into tokens using one or multiple delimiter characters.
     *
     * @param string the string to spit.
     * @param delimChars a string containing all delimiter characters
     *
     * @return a list containing all tokens
     */
    public static List<String> split(final String string, final String delimChars) {
        final ArrayList<String> list = new ArrayList<String>();
        final StringTokenizer tokenizer = new StringTokenizer(string, delimChars);
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }

    /**
     * Split the first token from a give string using any
     * whitespace character as delimiter.
     *
     * @param string the string to split
     *
     * @return the first token or null if no token has been found
     */
    public static String splitFirst(final String string) {
        return splitFirst(string, WHITESPACE_CHARACTER_STRING);
    }

    /**
     * Split the first token from a give string using one or
     * multiple delimiter characters given in a string. 
     *
     * @param string the string to split
     * @param delimiterChars the delimiter characters
     *
     * @return the first token or null if no token has been found
     */
    public static String splitFirst(final String string, final String delimChars) {
        final StringTokenizer tokenizer = new StringTokenizer(string, delimChars);
        if (tokenizer.hasMoreTokens()) {
            return tokenizer.nextToken();
        } else {
            return null;
        }
    }

    /**
     * Split the last token from a give string using any
     * whitespace character as delimiter. 
     *
     * @param string the string to split
     *
     * @return the last token or null if no token has been found
     */
    public static String splitLast(final String string) {
        return splitLast(string, WHITESPACE_CHARACTER_STRING);
    }

    /**
     * Split the last token from a give string using one or
     * multiple delimiter characters given in a string. 
     *
     * @param string the string to split
     * @param delimiterChars the delimiter characters
     *
     * @return the last token or null if no token has been found
     */
    public static String splitLast(final String string, final String delimChars) {
        String lastToken = null;
        final StringTokenizer tokenizer = new StringTokenizer(string, delimChars);
        while (tokenizer.hasMoreTokens()) {
            lastToken = tokenizer.nextToken(delimChars);
        }
        return lastToken;
    }

    /**
     * Split all before the last token from a given string using one
     * delimiter character in a string. The delimiter tokens in between
     * will be simply returned.
     *
     * @param string the string to split
     * @param delimiterChars the delimiter characters
     *
     * @return all tokens besides the last or null if no token has been found
     */
    public static String splitBeforeLast(final String string, final String delimChars) {
        final char[] delimCa = delimChars.toCharArray();
        final int len = string.length();
        int state = 0;
        int pos1 = -1;
        int pos2 = -1;
        int pos3 = -1;
        for (int i = len - 1; pos3 == -1 && i >= 0; i--) {
            final char c = string.charAt(i);
            switch (state) {
            case 0:
                if (!charMatches(delimCa, c)) {
                    pos1 = i + 1;
                    state = 1;
                }
                break;
            case 1:
                if (charMatches(delimCa, c)) {
                    pos2 = i + 1;
                    state = 2;
                }
                break;
            case 2:
                if (!charMatches(delimCa, c)) {
                    pos3 = i + 1;
                }
                break;
            }
        }
        switch (state) {
        case 0:
            return "";
        case 1:
            return string.substring(0, pos1);
        default:
            if (pos3 > -1) {
                return string.substring(0, pos3);
            } else {
                return string.substring(pos2, pos1);
            }
        }
    }

    /**
     * A convenient method to split a string by any whitespace
     * character but leave quoted substring together.
     *
     * @param string the string that will be split
     *
     * @return an array with substring split
     */
    public static String[] splitQuoted(final String string) {
        final List<String> list = new ArrayList<String>();
        final StringBuffer buffer = new StringBuffer();
        int state = 0;
        final int len = string.length();
        for (int i = 0; i < len; i++) {
            final char c = string.charAt(i);
            switch (state) {
            case 0:
                switch (c) {
                case ' ':
                case '\t':
                case '\n':
                    // state stays 0
                    break;
                case '"':
                    state = 2;
                    break;
                default:
                    buffer.append(c);
                    state = 1;
                }
                break;

            case 1:
                switch (c) {
                case ' ':
                case '\t':
                case '\n':
                    list.add(buffer.toString());
                    buffer.setLength(0);
                    state = 0;
                    break;
                case '"':
                    list.add(buffer.toString());
                    buffer.setLength(0);
                    state = 2;
                    break;
                default:
                    buffer.append(c);
                    state = 1;
                }
                break;

            case 2:
                switch (c) {
                case '"':
                    list.add(buffer.toString());
                    buffer.setLength(0);
                    state = 0;
                    break;
                default:
                    buffer.append(c);
                break;
                }
                break;

            default:
                throw new BuildException("wrong state " + state);
            }
        }

        switch (state) {
        case 0:
            break;
        case 1:
            list.add(buffer.toString());
            break;
        case 2:
            throw new BuildException("Missing qouote at the end of string\""
                    + string + "\"");
        }

        return list.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * determines if a character is in a character array.
     *
     * @param chars the character array
     * @param c the character to test
     * @return if the character matches or not
     */
    private static boolean charMatches(final char[] chars, final char c) {
        boolean matches = false;
        for (int i = 0; !matches && i < chars.length; i++) {
            if (chars[i] == c) {
                matches = true;
            }
        }
        return matches;
    }

    /**
     * The default constructor may not be used.
     */
    private StringHelper() {
    }
}
