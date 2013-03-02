/*
 * Rapid Beans Framework: StringHelperTest.java
 * 
 * Copyright Martin Bluemel, 2008
 * 
 * Nov 9, 2005
 */
package org.rapidbeans.core.util;

import java.util.List;

import junit.framework.TestCase;

/**
 * Tests for the StringHelper.
 * 
 * @author Martin Bluemel
 */
public final class StringHelperTest extends TestCase {

	public void testStripBothWhitespace() {
		assertEquals("hello world", StringHelper.strip(
				"\t\t \n  hello world \t \n", StringHelper.StripMode.both));
	}

	public void testStripTrailing() {
		assertEquals(
				"/\\ abc 789/ ",
				StringHelper.strip("/\\ abc 789/ /\\///\\", new char[] { '/',
						'\\' }, StringHelper.StripMode.trailing));
	}

	public void testStripLeading() {
		assertEquals("abc/789", StringHelper.strip("//abc/789", '/',
				StringHelper.StripMode.leading));
	}

	public void testFillUpLeft() {
		assertEquals("000123",
				StringHelper.fillUp("123", 6, '0', StringHelper.FillMode.left));
	}

	public void testFillUpRight() {
		assertEquals("hello world         ", StringHelper.fillUp("hello world",
				20, ' ', StringHelper.FillMode.right));
	}

	// // Core 2 Duo Notebook: 0,75 sec
	// public void testFillUpPerf() {
	// for (int i = 0; i < 1000000; i++) {
	// StringHelper.fillUp("hello world", 20, ' ', StringHelper.FillMode.left);
	// StringHelper.fillUp("hello world", 20, ' ', StringHelper.FillMode.right);
	// }
	// }

	public void testSplitWhitespace() {
		List<String> sp = StringHelper.split("\t1 22\n\t\t 333 \n4444\n");
		assertEquals(4, sp.size());
		assertEquals("1", sp.get(0));
		assertEquals("22", sp.get(1));
		assertEquals("333", sp.get(2));
		assertEquals("4444", sp.get(3));
	}

	public void testSplitPath() {
		List<String> sp = StringHelper.split(
				"//D:/Projects/RapidBeans\\org.rapidbeans\\src", "/\\");
		assertEquals(5, sp.size());
		assertEquals("D:", sp.get(0));
		assertEquals("Projects", sp.get(1));
		assertEquals("RapidBeans", sp.get(2));
		assertEquals("org.rapidbeans", sp.get(3));
		assertEquals("src", sp.get(4));
	}

	public void testSplitFirstWhitespace() {
		assertEquals("1", StringHelper.splitFirst("\t1 22\n\t\t 333 \n4444\n"));
	}

	public void testSplitFirstPath() {
		assertEquals("D:", StringHelper.splitFirst(
				"//D:/Projects/RapidBeans\\org.rapidbeans\\src", "/\\"));
	}

	public void testSplitFirstWhitespaceNo() {
		assertNull(StringHelper.splitFirst("\t \n\t\t  \n\n"));
	}

	public void testSplitFirstWhitespaceNull() {
		try {
			StringHelper.splitFirst(null);
			fail();
		} catch (NullPointerException e) {
			assertTrue(true);
		}
	}

	public void testSplitLastWhitespace() {
		assertEquals("4444",
				StringHelper.splitLast("\t1 22\n\t\t 333 \n4444\n"));
	}

	public void testSplitLastPath() {
		assertEquals("src", StringHelper.splitLast(
				"//D:/Projects/RapidBeans\\org.rapidbeans\\src", "/\\"));
	}

	public void testSplitBeforeLast() {
		assertEquals("http://www.martin-bluemel.de",
				StringHelper.splitBeforeLast(
						"http://www.martin-bluemel.de/software", "/.-"));
	}

	public void testSplitBeforeLastMultipleDelimSequence() {
		assertEquals("http://www.martin-bluemel.de",
				StringHelper.splitBeforeLast(
						"http://www.martin-bluemel.de/-.--..software-/./.--",
						"/.-"));
	}

	public void testSplitBeforeLastOnlyOneDelimsAround() {
		assertEquals("software",
				StringHelper.splitBeforeLast("-.--..software-/./.--", "/.-"));
	}

	public void testSplitBeforeLastOnlyOneDelimsRight() {
		assertEquals("software",
				StringHelper.splitBeforeLast("software-/./.--", "/.-"));
	}

	public void testSplitBeforeLastOnlyOneDelimsLeft() {
		assertEquals("software",
				StringHelper.splitBeforeLast("-.--..software", "/.-"));
	}

	public void testSplitBeforeLastOnlyOneDelimsNo() {
		assertEquals("software",
				StringHelper.splitBeforeLast("software", "/.-"));
	}

	public void testSplitEscaped() {
		assertEquals(
				"X,Y,Z",
				StringHelper.splitEscaped("X\\,Y\\,Z,Bluemel\\,Martin,ABC",
						',', '\\').get(0));
		assertEquals(
				"Bluemel,Martin",
				StringHelper.splitEscaped("X\\,Y\\,Z,Bluemel\\,Martin,ABC",
						',', '\\').get(1));
		assertEquals(
				"ABC",
				StringHelper.splitEscaped("X\\,Y\\,Z,Bluemel\\,Martin,ABC",
						',', '\\').get(2));
	}

	public void testSplitQuotedMixed() {
		String[] sa = StringHelper
				.splitQuoted(" \t Hier  \n\t \"kommt die\" \t\n"
						+ "\t \"kleine, schlaue\"  \n \t\tMaus\n \t ");
		assertEquals(4, sa.length);
		assertEquals("Hier", sa[0]);
		assertEquals("kommt die", sa[1]);
		assertEquals("kleine, schlaue", sa[2]);
		assertEquals("Maus", sa[3]);
	}

	public void testSplitQoutedSingle() {
		String[] sa = StringHelper.splitQuoted("Hier");
		assertEquals(1, sa.length);
		assertEquals("Hier", sa[0]);
	}

	public void testSplitQoutedQuotesEscaped() {
		String[] sa = StringHelper
				.splitQuoted("/C \"echo Hello \\\"My Folks\\\"!& pause\"");
		assertEquals(2, sa.length);
		assertEquals("/C", sa[0]);
		assertEquals("echo Hello \"My Folks\"!& pause", sa[1]);
	}

	public void testSplitQuotedToken() {
		final List<StringHelper.SplitToken> sl = StringHelper
				.splitQuotedIsQuoted("/C \"echo Hello \\\"My Folks\\\"!& pause\"");
		assertEquals(2, sl.size());
		assertEquals("/C", sl.get(0).getToken());
		assertEquals(false, sl.get(0).isQuoted());
		assertEquals("echo Hello \"My Folks\"!& pause", sl.get(1).getToken());
		assertEquals(true, sl.get(1).isQuoted());
	}

	public void testUpperFistCharacter() {
		assertEquals("Martin", StringHelper.upperFirstCharacter("martin"));
	}

	public void testIsNumberOk() {
		assertTrue(StringHelper.isDigitsOnly("1234455622233"));
	}

	public void testIsNumberFalse() {
		assertFalse(StringHelper.isDigitsOnly("1234455A622233"));
	}

	public void testEscapeMapSimple() {
		EscapeMap map = new EscapeMap(new String[] { "\b", "\\b", "\n", "\\n",
				"\r", "\\r", "\t", "\\t", });
		assertEquals("12\\t3\\n\\xx\\bx\\r",
				StringHelper.escape("12\t3\n\\xx\bx\r", map));
		assertEquals("12\t3\n\\xx\bx\r",
				StringHelper.unescape("12\\t3\\n\\xx\\bx\\r", map));
	}

	public void testEscapeMapBackslash() {
		EscapeMap map = new EscapeMap(new String[] { "\b", "\\b", "\n", "\\n",
				"\r", "\\r", "\t", "\\t", "\\", "\\\\" });
		assertEquals("\\\\t", StringHelper.escape("\\t", map));
		assertEquals("\\t", StringHelper.unescape("\\\\t", map));
	}

	public void testUnescapeLf() {
		assertEquals("\n", StringHelper.unescape("\\n"));
	}

	public void testUnescapeEscLf() {
		assertEquals("\\n", StringHelper.unescape("\\\\n"));
	}
}
