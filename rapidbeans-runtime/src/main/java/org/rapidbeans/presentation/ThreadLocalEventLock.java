/*
 * Rapid Beans Framework: ThreadLocalEventLock.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/25/2006
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

package org.rapidbeans.presentation;

/**
 * thread local event lock.
 * 
 * @author Martin Bluemel
 */
public final class ThreadLocalEventLock {

	/**
	 * Thread local property sorter.
	 */
	private static ThreadLocal<Integer> lockLevel = new ThreadLocal<Integer>() {
		protected synchronized Integer initialValue() {
			return new Integer(0);
		}
	};

	/**
	 * Thread local property sorter.
	 */
	private static ThreadLocal<EditorProperty> sourcePropertyEditor = new ThreadLocal<EditorProperty>() {
		protected synchronized EditorProperty initialValue() {
			return null;
		}
	};

	/**
	 * set the lock.
	 */
	public static void set(final EditorProperty propEditor) {
		int i = lockLevel.get().intValue();
		lockLevel.set(new Integer(i + 1));
		if (propEditor != null) {
			sourcePropertyEditor.set(propEditor);
		}
	}

	/**
	 * release the lock.
	 */
	public static void release() {
		int i = lockLevel.get().intValue();
		if (i > 0) {
			lockLevel.set(new Integer(i - 1));
		}
		sourcePropertyEditor.remove();
	}

	/**
	 * ask the lock.
	 * 
	 * @return if locked or not
	 */
	public static boolean get() {
		return lockLevel.get().intValue() > 0;
	}

	/**
	 * @return the source property editor
	 */
	public static EditorProperty getSourcePropEditor() {
		if (sourcePropertyEditor == null) {
			return null;
		} else {
			return sourcePropertyEditor.get();
		}
	}

	/**
	 * prevent from being constructed.
	 */
	private ThreadLocalEventLock() {
	}
}
