package org.rapidbeans.sdk.core;

public interface SdkLogger {

	/**
	 * Send a message to the user in the <b>debug</b> error level.
	 *
	 * @param content
	 */
	void debug(String content);

	/**
	 * Send a message (and accompanying exception) to the user in the <b>debug</b>
	 * error level.<br>
	 * The error's stacktrace will be output when this error level is enabled.
	 *
	 * @param content
	 * @param error
	 */
	void debug(String content, Throwable error);

	/**
	 * Send an exception to the user in the <b>debug</b> error level.<br>
	 * The stack trace for this exception will be output when this error level is
	 * enabled.
	 *
	 * @param error
	 */
	void debug(Throwable error);

	/**
	 * Send a message to the user in the <b>info</b> error level.
	 *
	 * @param content
	 */
	void info(String content);

	/**
	 * Send a message (and accompanying exception) to the user in the <b>info</b>
	 * error level.<br>
	 * The error's stacktrace will be output when this error level is enabled.
	 *
	 * @param content
	 * @param error
	 */
	void info(String content, Throwable error);

	/**
	 * Send an exception to the user in the <b>info</b> error level.<br>
	 * The stack trace for this exception will be output when this error level is
	 * enabled.
	 *
	 * @param error
	 */
	void info(Throwable error);

	/**
	 * Send a message to the user in the <b>warn</b> error level.
	 *
	 * @param content
	 */
	void warn(String content);

	/**
	 * Send a message (and accompanying exception) to the user in the <b>warn</b>
	 * error level.<br>
	 * The error's stacktrace will be output when this error level is enabled.
	 *
	 * @param content
	 * @param error
	 */
	void warn(String content, Throwable error);

	/**
	 * Send an exception to the user in the <b>warn</b> error level.<br>
	 * The stack trace for this exception will be output when this error level is
	 * enabled.
	 *
	 * @param error
	 */
	void warn(Throwable error);

	/**
	 * Send a message to the user in the <b>error</b> error level.
	 *
	 * @param content
	 */
	void error(String content);

	/**
	 * Send a message (and accompanying exception) to the user in the <b>error</b>
	 * error level.<br>
	 * The error's stacktrace will be output when this error level is enabled.
	 *
	 * @param content
	 * @param error
	 */
	void error(String content, Throwable error);

	/**
	 * Send an exception to the user in the <b>error</b> error level.<br>
	 * The stack trace for this exception will be output when this error level is
	 * enabled.
	 *
	 * @param error
	 */
	void error(Throwable error);
}
