/*
 * Rapid Beans Framework, SDK, Maven Plugin: RapidBeansGeneratorMojo.java
 *
 * Copyright (C) 2018 Martin Bluemel
 *
 * Creation Date: 08/09/2018
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

package org.rapidbeans.sdk.maven;

import org.apache.maven.plugin.logging.Log;
import org.rapidbeans.sdk.core.SdkLogger;

public class SdkLoggerAdapterMaven implements SdkLogger {

	private final Log mavenLogger;

	public SdkLoggerAdapterMaven(final Log mavenLogger) {
		this.mavenLogger = mavenLogger;
	}

	@Override
	public void debug(String content) {
		this.mavenLogger.debug(content);
	}

	@Override
	public void debug(String content, Throwable error) {
		this.mavenLogger.debug(content, error);
	}

	@Override
	public void debug(Throwable error) {
		this.mavenLogger.debug(error);
	}

	@Override
	public void info(String content) {
		this.mavenLogger.info(content);
	}

	@Override
	public void info(String content, Throwable error) {
		this.mavenLogger.info(content, error);
	}

	@Override
	public void info(Throwable error) {
		this.mavenLogger.info(error);
	}

	@Override
	public void warn(String content) {
		this.mavenLogger.warn(content);
	}

	@Override
	public void warn(String content, Throwable error) {
		this.mavenLogger.warn(content, error);
	}

	@Override
	public void warn(Throwable error) {
		this.mavenLogger.warn(error);
	}

	@Override
	public void error(String content) {
		this.mavenLogger.error(content);
	}

	@Override
	public void error(String content, Throwable error) {
		this.mavenLogger.error(content, error);
	}

	@Override
	public void error(Throwable error) {
		this.mavenLogger.error(error);
	}
}
