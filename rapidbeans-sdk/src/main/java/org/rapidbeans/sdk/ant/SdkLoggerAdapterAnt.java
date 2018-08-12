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

package org.rapidbeans.sdk.ant;

import org.apache.tools.ant.Project;
import org.rapidbeans.sdk.core.SdkLogger;

public class SdkLoggerAdapterAnt implements SdkLogger {

	private final Project antProject;

	public SdkLoggerAdapterAnt(final Project antProject) {
		this.antProject = antProject;
	}

	@Override
	public void debug(String content) {
		this.antProject.log(content, Project.MSG_DEBUG);
	}

	@Override
	public void debug(String content, Throwable error) {
		this.antProject.log(content, error, Project.MSG_DEBUG);
	}

	@Override
	public void debug(Throwable error) {
		this.antProject.log(error.getClass().getName(), error, Project.MSG_DEBUG);
	}

	@Override
	public void info(String content) {
		this.antProject.log(content, Project.MSG_INFO);
	}

	@Override
	public void info(String content, Throwable error) {
		this.antProject.log(content, error, Project.MSG_INFO);
	}

	@Override
	public void info(Throwable error) {
		this.antProject.log(error.getClass().getName(), error, Project.MSG_INFO);
	}

	@Override
	public void warn(String content) {
		this.antProject.log(content, Project.MSG_WARN);
	}

	@Override
	public void warn(String content, Throwable error) {
		this.antProject.log(content, error, Project.MSG_WARN);
	}

	@Override
	public void warn(Throwable error) {
		this.antProject.log(error.getClass().getName(), error, Project.MSG_WARN);
	}

	@Override
	public void error(String content) {
		this.antProject.log(content, Project.MSG_ERR);
	}

	@Override
	public void error(String content, Throwable error) {
		this.antProject.log(content, error, Project.MSG_ERR);
	}

	@Override
	public void error(Throwable error) {
		this.antProject.log(error.getClass().getName(), error, Project.MSG_ERR);
	}
}
