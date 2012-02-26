/**
 * 
 */
package org.rapidbeans.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Exception that guarantees that you can always deserialize
 * it by changing all nested exceptions into instances of this
 * class while storing the original instance's class name
 * the original instances properties as string key value pairs
 * adds a time stamp information
 *
 * @author Martin Bluemel
 */
public class UniversalException extends Exception {

	/**
	 * For serialization issues
	 */
	private static final long serialVersionUID = -4563749306349098771L;

	private static final int GET_SEPARATION_INDEX = 3;

	private static final String TAB = "\t";

	private static final String COLON = ": ";

	private static final String AT = "at ";

	private Date timeStamp = null;

	private String originalClassname;

	private final Map<String, String> properties =
			new HashMap<String, String>();

	/**
	 * Constructor with a message only.
	 * 
	 * @param message
	 *            the exception message
	 */
	public UniversalException(final String message) {
		super(message);
		this.timeStamp = new Date();
	}

	/**
	 * Constructor with a cause.
	 * 
	 * @param cause
	 *            the root cause exception that will be saved as nested
	 *            exception.
	 */
	public UniversalException(final Throwable cause) {
		super("wrapped " + cause.getClass().getName() + COLON
				+ cause.getMessage(), convertCause(cause));
		this.timeStamp = new Date();
	}

	/**
	 * Constructor with message and cause.
	 * 
	 * @param message
	 *            the exception message
	 * @param cause
	 *            the root cause exception that will be saved as nested
	 *            exception.
	 */
	public UniversalException(final String message,
			final Throwable cause) {
		super(message, convertCause(cause));
		this.timeStamp = new Date();
	}

	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		return (Date) this.timeStamp.clone();
	}

	/**
	 * @return the originalClassname
	 */
	public String getOriginalClassname() {
		return originalClassname;
	}

	/**
	 * Returns the value of a property with the given name as string.
	 * 
	 * @param name
	 *            the property's name
	 * 
	 * @return the value of a property with the given name as string
	 */
	public String getProperty(final String name) {
		return this.properties.get(name);
	}

	/**
	 * Convert the complete cause chain into Exceptions if necessary.
	 *
	 * @param cause
	 *            the top level cause
	 *
	 * @return the top level cause converted into a ServiceException containing
	 *         only ServiceEception as nested exceptions (causes)
	 */
	private static UniversalException convertCause(
			final Throwable cause) {
		if (cause instanceof UniversalException) {
			return (UniversalException) cause;
		}
		final List<Throwable> causeChain = new ArrayList<Throwable>();
		Throwable causeOfCause = cause;
		while (causeOfCause != null) {
			causeChain.add(causeOfCause);
			causeOfCause = causeOfCause.getCause();
		}
		final int len = causeChain.size();
		Throwable lastCause = null;
		for (int i = (len - 1); i >= 0; i--) {
			final Throwable currentCause = causeChain.get(i);
			UniversalException universalException = null;
			if (currentCause instanceof UniversalException) {
				universalException = (UniversalException) currentCause;
			} else {
				if (lastCause == null) {
					universalException = new UniversalException(currentCause.getMessage());
				} else {
					universalException = new UniversalException(currentCause.getMessage(), lastCause);
				}
				universalException.originalClassname = currentCause.getClass().getName();
				universalException.setStackTrace(currentCause.getStackTrace());
				causeChain.set(i, universalException);
				// reflect over the Exception class' getters
				for (final Method method : currentCause.getClass().getMethods()) {
					if (method.getName().startsWith("get")
							&& method.getName().length() > GET_SEPARATION_INDEX
							&& method.getParameterTypes().length == 0
							&& Character.isUpperCase(method.getName().charAt(
									GET_SEPARATION_INDEX))) {
						final String key =
								lowerFirstCharacter(method.getName().substring(
										3));
						// exclude standard Object and Exception getters
						if (key.equals("class") || key.equals("cause")
								|| key.equals("message")
								|| key.equals("localizedMessage")
								|| key.equals("stackTrace")) {
							continue;
						}
						try {
							final Object value = method.invoke(currentCause);
							if (value == null) {
								universalException.properties.put(key, "<null>");
							} else {
								universalException.properties.put(key, value
										.toString());
							}
						// we can not really reach test coverage for the IllegalAccessException
						// so in my eyes the IllegalAccesException would have been better designed
						// as a RntimeException
						} catch (IllegalAccessException e) {
							putSeviceExceptionProperties(universalException, key);
						} catch (InvocationTargetException e) {
							putSeviceExceptionProperties(universalException, key);
						}
					}
				}
			}
			lastCause = universalException;
		}
		return (UniversalException) causeChain.get(0);
	}

	/**
	 * Write an error message into the properties of the given exception.
	 * 
	 * @param serviceException
	 *            the UniversalException to work on
	 * @param key
	 *            the property key where we had problems to determine a value
	 *            for.
	 */
	private static void putSeviceExceptionProperties(
			UniversalException serviceException,
			final String key) {
		serviceException.properties.put(key,
				"<problems to retrieve exception property " + key + ">");
	}

	/**
	 * Lowers the first character of the given string.
	 * 
	 * @param s
	 *            the input string
	 * 
	 * @return the string with lowered first character
	 */
	private static String lowerFirstCharacter(final String s) {
		final String firstCharLowered = s.substring(0, 1).toLowerCase();
		if (s.length() == 1) {
			return firstCharLowered;
		} else {
			return firstCharLowered + s.substring(1);
		}
	}

	/**
	 * Prints the ServiceException and its stack trace to the specified print
	 * stream.
	 * 
	 * @param s
	 *            PrintStream to used for output
	 */
	@Override
	public void printStackTrace(final PrintStream s) {
		printStackTrace(new OutputMediaAdapterPs(s));
	}

	/**
	 * Prints the ServiceException and its stack trace to the specified print
	 * writer.
	 * 
	 * @param s
	 *            PrintWriter used for output
	 */
	@Override
	public void printStackTrace(final PrintWriter s) {
		printStackTrace(new OutputMediaAdapterPw(s));
	}

	/**
	 * Prints the ServiceException and its stack trace to the specified print
	 * stream.
	 * 
	 * @param s
	 *            PrintStream to used for output
	 */
	private void printStackTrace(final OutputMediaAdapter s) {
		synchronized (s) {
			s.println(this);
			s.println(TAB + "time stamp: " + this.timeStamp.toString());
			for (final StackTraceElement trace : this.getStackTrace()) {
				s.println(TAB + AT + trace);
			}
			final UniversalException ourCause =
					(UniversalException) getCause();
			if (ourCause != null) {
				printStackTraceAsCause(ourCause, s, this.getStackTrace());
			}
		}
	}

	/**
	 * Print the stack trace as a cause for the specified stack trace.
	 * 
	 * @param t
	 *            the Throwable to print the stack trace for
	 * @param s
	 *            the stream or write to print to
	 * @param parentTrace
	 *            the stack trace of the parent exception
	 */
	private static void printStackTraceAsCause(
			final UniversalException t,
			final OutputMediaAdapter s, final StackTraceElement[] parentTrace) {

		// Compute number of frames in common between this and caused
		StackTraceElement[] trace = t.getStackTrace();
		int m = trace.length - 1;
		int n = parentTrace.length - 1;
		while (m >= 0 && n >= 0 && trace[m].equals(parentTrace[n])) {
			m--;
			n--;
		}
		int framesInCommon = trace.length - 1 - m;

		// Print the cause
		s.println("Caused by: " + t);

		// Print the properties
		if (t.properties != null) {
			for (final String key : t.properties.keySet()) {
				s.println(TAB + key + " = \"" + t.properties.get(key) + "\"");
			}
		}

		// Print the stack trace
		for (int i = 0; i <= m; i++) {
			s.println(TAB + AT + trace[i]);
		}
		if (framesInCommon != 0) {
			s.println(TAB + "... " + framesInCommon + " more");
		}

		// Recurse if we have a cause
		UniversalException ourCause =
				(UniversalException) t.getCause();
		if (ourCause != null) {
			printStackTraceAsCause(ourCause, s, trace);
		}
	}

	/**
	 * Returns a short description of this throwable. The result is the
	 * concatenation of:
	 * <ul>
	 * <li>the {@linkplain Class#getName() name} of the class of this object
	 * <li>": " (a colon and a space)
	 * <li>the result of invoking this object's {@link #getLocalizedMessage}
	 * method
	 * </ul>
	 * If <tt>getLocalizedMessage</tt> returns <tt>null</tt>, then just the
	 * class name is returned.
	 * 
	 * @return a string representation of this throwable.
	 */
	@Override
	public String toString() {
		String s = null;
		if (this.originalClassname != null) {
			s = this.originalClassname;
		} else {
			s = getClass().getName();
		}
		final String message = getLocalizedMessage();
		return (message != null) ? (s + COLON + message) : s;
	}

	/**
	 * The interface a stream or writer class used for stack trace printing has
	 * to implement.
	 * 
	 * @author Martin Bluemel
	 */
	private interface OutputMediaAdapter {
		/**
		 * Prints a line to this stream or writer.
		 * 
		 * @param o
		 *            the object to print
		 */
		void println(final Object o);
	}

	/**
	 * The print stream implementation of the OutputMediaAdapter.
	 * 
	 * @author Martin Bluemel
	 */
	private static class OutputMediaAdapterPs implements OutputMediaAdapter {

		private PrintStream ps = null;

		/**
		 * Constructor.
		 * 
		 * @param stream
		 *            the PrintStream instance to use
		 */
		public OutputMediaAdapterPs(final PrintStream stream) {
			ps = stream;
		}

		/**
		 * Delegates the print line request to the PrintStream instance.
		 * 
		 * @param o
		 *            the Object to print
		 */
		@Override
		public void println(final Object o) {
			ps.println(o);
		}
	}

	/**
	 * The print writer implementation of the OutputMediaAdapter.
	 * 
	 * @author Martin Bluemel
	 */
	private static class OutputMediaAdapterPw implements OutputMediaAdapter {

		private PrintWriter pw = null;

		/**
		 * Constructor.
		 * 
		 * @param writer
		 *            the PrintWriter instance to use
		 */
		public OutputMediaAdapterPw(final PrintWriter writer) {
			pw = writer;
		}

		/**
		 * Delegates the print line request to the PrintWriter instance.
		 * 
		 * @param o
		 *            the Object to print
		 */
		@Override
		public void println(final Object o) {
			pw.println(o);
		}
	}
}
