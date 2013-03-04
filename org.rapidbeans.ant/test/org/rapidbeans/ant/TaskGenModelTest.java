/*
 * Rapid Beans Framework, SDK, Ant Tasks: TaskGenModelTest.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 08/11/2007
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

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.taskdefs.Mkdir;
import org.apache.tools.ant.taskdefs.Property;
import org.junit.Test;

/**
 * Unit TestCase (Unit Tests).
 * 
 * @author Martin Bluemel
 */
public final class TaskGenModelTest {

	@Test
	public void testExecute() {
		Project project = new Project();
		project.setKeepGoingMode(false);

		// load environment variables into the
		// project's properties (prefix "env.")
		Property propTask = new Property();
		propTask.setProject(project);
		propTask.setEnvironment("env");
		propTask.execute();

		Mkdir mkdirTask = new Mkdir();
		mkdirTask.setDir(new File("testdata/testtmp"));
		mkdirTask.execute();

		TaskGenModel task = new TaskGenModel();
		task.setProject(project);
		task.setSrcdir(new File("../org.rapidbeans/model"));
		task.setDestdirsimple(new File("testdata/testtmp"));
		task.setDestdirjoint(new File("testdata/testtmp"));
		task.setStyledir(new File("../org.rapidbeans/codegentemplates"));
		task.execute();

		Delete deleteTask = new Delete();
		deleteTask.setDir(new File("testdata/testtmp"));
		deleteTask.execute();
	}
}
