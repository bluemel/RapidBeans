/*
 * Rapid Beans Framework: BeanSorter.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 02/22/2009
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

/**
 * a thread local fine tuning for property validation.
 * 
 * @author Martin Bluemel
 */
public class ThreadLocalValidationSettings {

	/**
	 * Thread local property sorter.
	 */
	private static ThreadLocal<ThreadLocalValidationSettings> settings =
			new ThreadLocal<ThreadLocalValidationSettings>() {
				protected synchronized ThreadLocalValidationSettings initialValue() {
					return null;
				}
			};

	/**
	 * @return the thread local property validation settings
	 */
	protected static ThreadLocalValidationSettings get() {
		return settings.get();
	}

	/**
	 * remove the validation settings from the current thread.
	 */
	public static void remove() {
		settings.remove();
	}

	/**
	 * Switch off mandatory check.
	 */
	public static void mandatoryOff() {
		ThreadLocalValidationSettings currentSettings = settings.get();
		if (currentSettings == null) {
			currentSettings = new ThreadLocalValidationSettings();
			settings.set(currentSettings);
		}
		currentSettings.mandatoryCheck = false;
	}

	/**
	 * determines if any validation should be performed.
	 */
	public static boolean getValidation() {
		ThreadLocalValidationSettings currentSettings = settings.get();
		if (currentSettings == null) {
			return true;
		}
		return currentSettings.validation;
	}

	private boolean validation = true;

	/**
	 * Switch off validation.
	 * Be careful. Use only if you are really sure that you
	 * have to write an invalid object
	 */
	public static void validationOff() {
		ThreadLocalValidationSettings currentSettings = settings.get();
		if (currentSettings == null) {
			currentSettings = new ThreadLocalValidationSettings();
			settings.set(currentSettings);
		}
		currentSettings.validation = false;
	}

	/**
	 * determines if a mandatory check should be performed.
	 */
	protected static boolean getMandatoryCheck() {
		ThreadLocalValidationSettings currentSettings = settings.get();
		if (currentSettings == null) {
			return true;
		}
		return currentSettings.mandatoryCheck;
	}

	private boolean mandatoryCheck = true;

	/**
	 * Switch off read only check.
	 */
	public static void readonlyOff() {
		ThreadLocalValidationSettings currentSettings = settings.get();
		if (currentSettings == null) {
			currentSettings = new ThreadLocalValidationSettings();
			settings.set(currentSettings);
		}
		currentSettings.readonlyCheck = false;
	}

	/**
	 * Switch on read only check.
	 */
	public static void readonlyOn() {
		ThreadLocalValidationSettings currentSettings = settings.get();
		if (currentSettings == null) {
			currentSettings = new ThreadLocalValidationSettings();
			settings.set(currentSettings);
		}
		currentSettings.readonlyCheck = false;
	}

	/**
	 * determines if a read only check should be performed.
	 */
	protected static boolean getReadonlyCheck() {
		ThreadLocalValidationSettings currentSettings = settings.get();
		if (currentSettings == null) {
			return true;
		}
		return currentSettings.readonlyCheck;
	}

	private boolean readonlyCheck = true;
}
