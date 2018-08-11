package org.rapidbeans.sdk.core;

public class CgenStat {

	private int generated = 0;

	private int uptodate = 0;

	private long startTimeNanos = 0;

	private long execTimeNanos = 0;

	public void incGenerated() {
		this.generated++;
	}

	public void incUptodate() {
		this.uptodate++;
	}

	public int getGenerated() {
		return generated;
	}

	public int getUptodate() {
		return uptodate;
	}

	public void startTime() {
		this.startTimeNanos = System.nanoTime();
	}

	public void stopTime() {
		this.execTimeNanos = System.nanoTime() - this.startTimeNanos;
	}

	public String getExecTimeReadable() {
		return nanosToReadable(this.execTimeNanos);
	}

	public static String nanosToReadable(final long allnanos) {
		long allmillis = allnanos / 1000000;
		int millis = (int) allmillis % 1000;
		long allseconds = allmillis / 1000;
		int seconds = (int) (allseconds % 60);
		long allminutes = allseconds / 60;
		int minutes = (int) allminutes % 60;
		int hours = (int) allminutes / 60;
		if (hours > 0) {
			return String.format("%d:%02d:%02d.%03d", hours, minutes, seconds, millis);
		} else {
			return String.format("%02d:%02d.%03d", minutes, seconds, millis);
		}
	}
}
