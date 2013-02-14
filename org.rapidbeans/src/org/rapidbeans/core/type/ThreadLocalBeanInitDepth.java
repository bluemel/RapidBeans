/*
 * Rapid Beans Framework: ThreadLocalBeanInitDepth.java
 * 
 * Copyright (C) 2011 Martin Bluemel
 * 
 * Creation Date: 03/01/2011
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

package org.rapidbeans.core.type;

import java.util.ArrayList;
import java.util.List;

/**
 * thread local RapidBean init depth.
 * 
 * @author Martin Bluemel
 */
public final class ThreadLocalBeanInitDepth {

	/**
	 * Thread local property sorter.
	 */
	private static ThreadLocal<Integer> initDepth =
			new ThreadLocal<Integer>() {
				protected synchronized Integer initialValue() {
					return new Integer(0);
				}
			};

	/**
	 * Thread local property sorter.
	 */
	private static ThreadLocal<List<TypeRapidBean>> typesInitialized =
			new ThreadLocal<List<TypeRapidBean>>() {
				protected synchronized List<TypeRapidBean> initialValue() {
					return new ArrayList<TypeRapidBean>();
				}
			};

	/**
	 * ask the depth.
	 * 
	 * @return the depth
	 */
	public static int getDepth() {
		return initDepth.get().intValue();
	}

	public static List<TypeRapidBean> getTypesInitialilized() {
		return typesInitialized.get();
	}

	/**
	 * increase.
	 */
	public static void increment(final TypeRapidBean type) {
		int i = initDepth.get().intValue();
		initDepth.set(new Integer(i + 1));
		typesInitialized.get().add(type);
	}

	/**
	 * decrease.
	 */
	public static void decrement() {
		int i = initDepth.get().intValue();
		if (i > 0) {
			final int newDepth = i - 1;
			if (newDepth == 0) {
				typesInitialized.get().clear();
			}
			initDepth.set(new Integer(newDepth));
		} else {
			throw new AssertionError("Init depth cannot be decreased under 0.");
		}
	}
}
