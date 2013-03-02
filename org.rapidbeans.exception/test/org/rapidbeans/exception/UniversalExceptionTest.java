package org.rapidbeans.exception;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit Test if the UniversalException
 * 
 * @author Martin Bluemel
 */
public class UniversalExceptionTest {

	private static final String BLA_BLA = "bla bla";

	private static final int NUM11 = 11;

	private static final String TEST_ERROR_MESSAGE = "test error message";

	private static final String TEST_MESSAGE = "test message";

	private static final String NL = getLineBreak();

	private static final String TAB = "\t";

	private static final int FIRST_LINE = 185;

	/**
	 * My tiny little test exception.
	 * 
	 * @author Martin Bluemel
	 */
	private static final class MyTinyLittleException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		private final int x;

		private final String detailedMessage;

		/**
		 * Constructor.
		 * 
		 * @param message
		 *            the message
		 * @param code
		 *            the code
		 * @param details
		 *            the details
		 */
		public MyTinyLittleException(String message, int code, String details) {
			super(message);
			this.x = code;
			this.detailedMessage = details;
		}

		/**
		 * @return the detailed message.
		 */
		// this method is used by means of reflection
		@SuppressWarnings("unused")
		public String getDetailedMessage() {
			return this.detailedMessage;
		}

		/**
		 * @return the error code.
		 */
		// this method is used by means of reflection
		@SuppressWarnings("unused")
		public int getX() {
			return this.x;
		}

		/**
		 * @return the null argument.
		 */
		// this method is used by means of reflection
		@SuppressWarnings("unused")
		public String getNullExArg() {
			return null;
		}

		/**
		 * Throws always a RuntimeException in order to provoke an
		 * InvocationTargetException
		 * 
		 * @return nothing
		 */
		// this method is used by means of reflection
		@SuppressWarnings("unused")
		public String getTestInvocationTargetException() {
			throw new RuntimeException("xxx");
		}

		/**
		 * @return nothing.
		 */
		// this method is used by means of reflection
		@SuppressWarnings("unused")
		public String get() {
			return null;
		}

		/**
		 * @return nothing.
		 */
		// this method is used by means of reflection
		@SuppressWarnings("unused")
		public String getY(final String x) {
			return null;
		}

		/**
		 * @return nothing.
		 */
		// this method is used by means of reflection
		@SuppressWarnings("unused")
		public String geta() {
			return null;
		}
	}

	/**
	 * test method 1.
	 */
	private void f1() {
		f2();
	}

	/**
	 * test method 2.
	 */
	private void f2() {
		f3();
	}

	/**
	 * test method 3.
	 */
	private void f3() {
		f4();
	}

	/**
	 * test method 4.
	 */
	private void f4() {
		f5();
	}

	/**
	 * test method 5.
	 */
	private void f5() {
		f6();
	}

	/**
	 * test method 6.
	 */
	private void f6() {
		throw new RuntimeException(TEST_MESSAGE);
	}

	/**
	 * test method.
	 * 
	 * @throws UniversalException
	 *             for test reasons
	 */
	private void s01_1() throws UniversalException {
		try {
			s01_2();
		} catch (RuntimeException rex) {
			throw new UniversalException(rex);
		}
	}

	/**
	 * test method.
	 */
	private void s01_2() {
		s01_3();
	}

	/**
	 * test method.
	 */
	private void s01_3() {
		try {
			s01_4();
		} catch (RuntimeException e) {
			throw new RuntimeException(TEST_MESSAGE, e);
		}
	}

	/**
	 * test method.
	 */
	private void s01_4() {
		s01_5();
	}

	/**
	 * test method.
	 */
	private void s01_5() {
		s01_6();
	}

	/**
	 * test method.
	 */
	private void s01_6() {
		throw new MyTinyLittleException(TEST_ERROR_MESSAGE, NUM11, BLA_BLA);
	}

	/**
	 * test method.
	 */
	@Test
	public void testRuntimeException() {
		try {
			f1();
		} catch (RuntimeException e) {
			Assert.assertEquals(TEST_MESSAGE, e.getMessage());
			// Assert.assertEquals(25, e.getStackTrace().length);
			Assert.assertEquals("f6", e.getStackTrace()[0].getMethodName());
			Assert.assertEquals("f1", e.getStackTrace()[5].getMethodName());
			Assert.assertEquals("testRuntimeException",
					e.getStackTrace()[6].getMethodName());
		}
	}

	/**
	 * Test if the UniversalException has a correct time stamp.
	 */
	@Test
	public void testTimeStamp() {
		UniversalException sex = new UniversalException(TEST_MESSAGE);
		final Date currentTime = new Date();
		final Date ts = sex.getTimeStamp();
		// asserting the current time in milliseconds is a bit dangerous
		if (!(currentTime.toString().equals(ts.toString()))) {
			System.out.println("WARNING: exception time stamp: "
					+ currentTime.toString() + " differs from current time "
					+ ts.toString());
		}
		// assert immutability
		final long time = ts.getTime();
		ts.setTime(0);
		Assert.assertEquals(0, ts.getTime());
		Assert.assertEquals(time, sex.getTimeStamp().getTime());
	}

	/**
	 * test test correct message.
	 */
	@Test
	public void testMessage() {
		UniversalException uniEx = new UniversalException(TEST_MESSAGE);
		Assert.assertEquals(TEST_MESSAGE, uniEx.getMessage());
	}

	/**
	 * Test a wrapped exception message.
	 */
	@Test
	public void testMessageWrapped() {
		RuntimeException rex = new RuntimeException(TEST_MESSAGE);
		UniversalException uniEx = new UniversalException(rex);
		Assert.assertEquals("wrapped java.lang.RuntimeException: test message",
				uniEx.getMessage());
	}

	/**
	 * Test an explicitly wrapped message.
	 */
	@Test
	public void testMessageWrappedWithMessageExplicitely() {
		RuntimeException rex = new RuntimeException(TEST_MESSAGE);
		UniversalException uniEx = new UniversalException(BLA_BLA, rex);
		Assert.assertEquals(BLA_BLA, uniEx.getMessage());
	}

	/**
	 * Test the stack trace and cause chain.
	 * 
	 * @throws IOException
	 *             in case of IO problems
	 */
	@Test
	public void testPrintStackTraceAndCauseChain() throws IOException {
		try {
			s01_1();
		} catch (UniversalException uniEx) {
			UniversalException rex = (UniversalException) uniEx.getCause();
			Assert.assertEquals("java.lang.RuntimeException",
					rex.getOriginalClassname());
			Assert.assertEquals(TEST_MESSAGE, rex.getMessage());
			UniversalException mex = (UniversalException) rex.getCause();
			Assert.assertEquals(
					"org.rapidbeans.exception.UniversalExceptionTest$MyTinyLittleException",
					mex.getOriginalClassname());
			Assert.assertEquals(TEST_ERROR_MESSAGE, mex.getMessage());
			Assert.assertEquals(BLA_BLA, mex.getProperty("detailedMessage"));
			Assert.assertEquals("11", mex.getProperty("x"));
			Assert.assertNull(mex.getCause());
			Assert.assertNull(mex.getProperty("class"));
			Assert.assertNull(mex.getProperty("cause"));
			Assert.assertNull(mex.getProperty("message"));
			Assert.assertNull(mex.getProperty("localizedMessage"));
			Assert.assertNull(mex.getProperty("stackTrace"));
			Assert.assertEquals(getStackTraceExpectedCauseChain(uniEx
					.getTimeStamp().toString()),
					filterEnvironment(printStackTraceWriter(uniEx)));
		}
	}

	/**
	 * Test wrapping an UniversalException within an UniversalException.
	 */
	@Test
	public void testWrapUniversal() {
		UniversalException uniEx2 = new UniversalException(TEST_MESSAGE);
		RuntimeException rex = new RuntimeException(uniEx2);
		UniversalException uniEx = new UniversalException(rex);
		Assert.assertEquals("wrapped java.lang.RuntimeException: "
				+ "org.rapidbeans.exception.UniversalException: "
				+ "test message", uniEx.getMessage());
	}

	@Test
	public void testPrintStackTrace() {
		UniversalException uniEx = new UniversalException("asdf");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bos);
		uniEx.printStackTrace(ps);
	}

	/**
	 * Filter specific things from a stack trace to ease comparison.
	 * 
	 * @param s
	 *            the string to filter
	 * @return the filtered string
	 * @throws IOException
	 *             in case of IO problems
	 */
	private String filterEnvironment(String s) throws IOException {
		StringBuffer sb = new StringBuffer();
		LineNumberReader rd = new LineNumberReader(new StringReader(s));
		String line;
		while ((line = rd.readLine()) != null) {
			boolean append = true;
			if (line.startsWith("\t...")) {
				append = false;
			}
			if (append && line.startsWith("\tat ")
					&& (!line.startsWith("\tat org.rapidbeans"))) {
				append = false;
			}
			if (append) {
				sb.append(line + NL);
			}
		}
		return sb.toString();
	}

	/**
	 * Convert a stack trace into a string using a writer.
	 * 
	 * @param ex
	 *            the exception to print the stack trace from
	 * @return a string containing the stack trace
	 */
	private String printStackTraceWriter(final Exception ex) {
		CharArrayWriter caw = new CharArrayWriter();
		PrintWriter pw = new PrintWriter(caw);
		ex.printStackTrace(pw);
		pw.close();
		return caw.toString();
	}

	/**
	 * The test fixture for the stack trace string.
	 * 
	 * @param timeStamp
	 *            the time stamp
	 * @return the string
	 */
	private static String getStackTraceExpectedCauseChain(final String timeStamp) {
		return "org.rapidbeans.exception.UniversalException:"
				+ " wrapped java.lang.RuntimeException: test message"
				+ NL
				+ TAB
				+ "time stamp: "
				+ timeStamp
				+ NL
				+ TAB
				+ "at org.rapidbeans.exception.UniversalExceptionTest"
				+ ".s01_1(UniversalExceptionTest.java:"
				+ Integer.toString(FIRST_LINE)
				+ ")"
				+ NL
				+ TAB
				+ "at org.rapidbeans.exception.UniversalExceptionTest."
				+ "testPrintStackTraceAndCauseChain(UniversalExceptionTest.java:"
				+ Integer.toString(FIRST_LINE + 123)
				+ ")"
				+ NL
				+ "Caused by: java.lang.RuntimeException: test message"
				+ NL
				+ TAB
				+ "at org.rapidbeans.exception."
				+ "UniversalExceptionTest.s01_3("
				+ "UniversalExceptionTest.java:"
				+ Integer.toString(FIRST_LINE + 18)
				+ ")"
				+ NL
				+ TAB
				+ "at org.rapidbeans.exception."
				+ "UniversalExceptionTest.s01_2("
				+ "UniversalExceptionTest.java:"
				+ Integer.toString(FIRST_LINE + 8)
				+ ")"
				+ NL
				+ TAB
				+ "at org.rapidbeans.exception."
				+ "UniversalExceptionTest.s01_1("
				+ "UniversalExceptionTest.java:"
				+ Integer.toString(FIRST_LINE - 2)
				+ ")"
				+ NL
				+ "Caused by: org.rapidbeans.exception."
				+ "UniversalExceptionTest$MyTinyLittleException: test error message"
				+ NL
				+ TAB
				+ "testInvocationTargetException = \"<problems to retrieve exception property testInvocationTargetException>\""
				+ NL
				+ TAB
				+ "nullExArg = \"<null>\""
				+ NL
				+ TAB
				+ "detailedMessage = \"bla bla\""
				+ NL
				+ TAB
				+ "x = \"11\""
				+ NL
				+ TAB
				+ "at org.rapidbeans.exception."
				+ "UniversalExceptionTest.s01_6("
				+ "UniversalExceptionTest.java:"
				+ Integer.toString(FIRST_LINE + 40)
				+ ")"
				+ NL
				+ TAB
				+ "at org.rapidbeans.exception."
				+ "UniversalExceptionTest.s01_5("
				+ "UniversalExceptionTest.java:"
				+ Integer.toString(FIRST_LINE + 33)
				+ ")"
				+ NL
				+ TAB
				+ "at org.rapidbeans.exception."
				+ "UniversalExceptionTest.s01_4("
				+ "UniversalExceptionTest.java:"
				+ Integer.toString(FIRST_LINE + 26)
				+ ")"
				+ NL
				+ TAB
				+ "at org.rapidbeans.exception."
				+ "UniversalExceptionTest.s01_3("
				+ "UniversalExceptionTest.java:"
				+ Integer.toString(FIRST_LINE + 16) + ")" + NL;
	}

	/**
	 * @return the OS platform specific line break.
	 */
	private static String getLineBreak() {
		if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			return "\r\n";
		} else {
			return "\n";
		}
	}
}
