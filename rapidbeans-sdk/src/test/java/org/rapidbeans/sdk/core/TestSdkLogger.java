package org.rapidbeans.sdk.core;

public class TestSdkLogger implements SdkLogger {

	public enum Level {
		ERROR, WARN, INFO, DEBUG
	}

	private Level level = Level.INFO;

	@Override
	public void warn(Throwable error) {
		if (this.isWarnEnabled()) {
			System.out.println("WARNING: " + error.getMessage());
			error.printStackTrace(System.out);
		}
	}

	@Override
	public void warn(String content, Throwable error) {
		if (this.isWarnEnabled()) {
			System.out.println("WARNING: " + content);
			error.printStackTrace(System.out);
		}
	}

	@Override
	public void warn(String content) {
		if (this.isWarnEnabled()) {
			System.out.println("WARNING: " + content);
		}
	}

	@Override
	public void info(Throwable error) {
		if (this.isInfoEnabled()) {
			System.out.println("INFO: " + error.getMessage());
			error.printStackTrace(System.out);
		}
	}

	@Override
	public void info(String content, Throwable error) {
		if (this.isInfoEnabled()) {
			System.out.println("INFO: " + content);
			error.printStackTrace(System.out);
		}
	}

	@Override
	public void info(String content) {
		if (this.isInfoEnabled()) {
			System.out.println("INFO: " + content);
		}
	}

	@Override
	public void error(Throwable error) {
		if (this.isErrorEnabled()) {
			System.err.println("ERROR: " + error.getMessage());
			error.printStackTrace();
		}
	}

	@Override
	public void error(String content, Throwable error) {
		if (this.isErrorEnabled()) {
			System.err.println("ERROR: " + content);
			error.printStackTrace();
		}
	}

	@Override
	public void error(String content) {
		if (this.isErrorEnabled()) {
			System.err.println("ERROR: " + content);
		}
	}

	@Override
	public void debug(Throwable error) {
		if (this.isDebugEnabled()) {
			System.out.println("DEBUG: " + error.getMessage());
			error.printStackTrace(System.out);
		}
	}

	@Override
	public void debug(String content, Throwable error) {
		if (this.isDebugEnabled()) {
			System.out.println("DEBUG: " + content);
			error.printStackTrace(System.out);
		}
	}

	@Override
	public void debug(String content) {
		if (this.isDebugEnabled()) {
			System.out.println("DEBUG: " + content);
		}
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	private boolean isWarnEnabled() {
		return this.level.ordinal() >= Level.WARN.ordinal();
	}

	private boolean isInfoEnabled() {
		return this.level.ordinal() >= Level.INFO.ordinal();
	}

	private boolean isErrorEnabled() {
		return this.level.ordinal() >= Level.ERROR.ordinal();
	}

	private boolean isDebugEnabled() {
		return this.level.ordinal() >= Level.DEBUG.ordinal();
	}
}
