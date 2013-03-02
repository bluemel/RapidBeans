/*
 * Rapid Beans Framework: IdGeneratorNumeric.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/17/2006
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

package org.rapidbeans.core.basic;

import java.util.Stack;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;

/**
 * @author bluemel
 * 
 *         the UID generator
 */
public final class IdGeneratorNumeric implements IdGenerator {

	/**
	 * be fast do not reuse numbers.
	 */
	public static final int GENERATION_STRATEGY_FAST = 0;

	/**
	 * be compact use numbers.
	 */
	public static final int GENERATION_STRATEGY_COMPACT = 1;

	/**
	 * the maximal number already generated.
	 */
	private int maxGenNumber = 1;

	/**
	 * @return a new number
	 */
	public Integer generateIdValue() {
		switch (this.mode) {
		case GENERATION_STRATEGY_COMPACT:
			if (this.freeNumbers.size() > 0) {
				return this.freeNumbers.pop().intValue();
			}
			// fall trough
		default:
			if (this.maxGenNumber >= 1) {
				if (this.maxGenNumber < Integer.MAX_VALUE) {
					return this.maxGenNumber++;
				} else {
					this.maxGenNumber = Integer.MIN_VALUE;
					return Integer.MAX_VALUE;
				}
			} else /* this.maxGenNumber < 1 */{
				if (this.maxGenNumber < 0) {
					return this.maxGenNumber++;
				} else /* this.maxGenNUmber == 0 */{
					throw new RapidBeansRuntimeException(
							"maximal number reached");
				}
			}
		}
	}

	/**
	 * notify the number generator that an ID exists so that the max gen number
	 * will be set high enough.
	 * 
	 * @param existingId
	 *            existing numeric id as int
	 */
	public void notifiyIdExisists(final int existingId) {
		int newMaxGenNumber = existingId + 1;
		if (existingId == 0) {
			newMaxGenNumber = 0;
		}
		if (this.maxGenNumber == -1) {
			throw new RapidBeansRuntimeException("maximal number reached");
		} else if (this.maxGenNumber >= 0) {
			if (existingId >= 0) {
				if (newMaxGenNumber > this.maxGenNumber) {
					this.maxGenNumber = newMaxGenNumber;
				}
			} else /* argMaxGenNumber < 0 */{
				this.maxGenNumber = newMaxGenNumber;
			}
		} else /* this.maxGenNumber < -1 */{
			if (existingId < 0) {
				if (newMaxGenNumber > this.maxGenNumber) {
					this.maxGenNumber = newMaxGenNumber;
				}
			}
		}
	}

	/**
	 * release a number.
	 * 
	 * @param freeNumber
	 *            the number to release
	 */
	public void releaseNumber(final int freeNumber) {
		this.freeNumbers.push(freeNumber);
	}

	/**
	 * defines the generation strategy.
	 */
	private int mode = GENERATION_STRATEGY_FAST;

	/**
	 * collection numbers free for reuse.
	 */
	private Stack<Integer> freeNumbers = new Stack<Integer>();

	/**
	 * constructor.
	 */
	public IdGeneratorNumeric() {
	}

	/**
	 * set the number generation mode.<BR/>
	 * <B>GENERATION_STRATEGY_FAST: </B>just generate a new number<BR/>
	 * <B>GENERATION_STRATEGY_COMPACT: </B>reuse new numbers<BR/>
	 * 
	 * @param argMode
	 *            the new mode
	 */
	public void setMode(final int argMode) {
		this.mode = argMode;
	}
}
