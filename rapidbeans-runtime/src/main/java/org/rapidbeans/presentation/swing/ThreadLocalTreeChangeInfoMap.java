/*
 * Rapid Beans Framework: ThreadLocalTreeChangeInfoMap.java
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

package org.rapidbeans.presentation.swing;

import java.util.HashMap;
import java.util.Stack;

import org.rapidbeans.core.basic.Property;

/**
 * thread local tree change info.
 * 
 * @author Martin Bluemel
 */
final class ThreadLocalTreeChangeInfoMap {

	/**
	 * Thread local tree change info.
	 */
	private static ThreadLocal<Stack<HashMap<Property, TreeChangeInfo>>> tcInfoMapStack = new ThreadLocal<Stack<HashMap<Property, TreeChangeInfo>>>() {
		protected synchronized Stack<HashMap<Property, TreeChangeInfo>> initialValue() {
			return new Stack<HashMap<Property, TreeChangeInfo>>();
		}
	};

	/**
	 * set the thread local tree change info.
	 * 
	 * @param tciMap the thread local tree change info to set
	 */
	public static void push(final HashMap<Property, TreeChangeInfo> tciMap) {
		tcInfoMapStack.get().push(tciMap);
	}

	/**
	 * get the thread local tree change info.
	 * 
	 * @return the thread local tree change info.
	 */
	public static HashMap<Property, TreeChangeInfo> pop() {
		return tcInfoMapStack.get().pop();
	}

	/**
	 * get the thread local tree change info.
	 * 
	 * @return the thread local tree change info.
	 */
	public static HashMap<Property, TreeChangeInfo> peek() {
		return tcInfoMapStack.get().peek();
	}

	/**
	 * prevent from being constructed.
	 */
	private ThreadLocalTreeChangeInfoMap() {
	}
}
