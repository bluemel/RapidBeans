/*
 * Rapid Beans Framework: IdUuid.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/13/2006
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

import java.util.UUID;

/**
 * a UUID Identity.
 */
public final class IdUuid extends Id {

	/**
	 * the UUID.
	 */
	private UUID uuid = null;

	/**
	 * constructor for UUID IDs.
	 * 
	 * @param bean   not used for UID ids
	 * @param argUid the String with the number.
	 */
	public IdUuid(final RapidBean bean, final String argUid) {
		IdGeneratorUuid generator = null;
		if (bean != null) {
			generator = (IdGeneratorUuid) bean.getType().getIdGenerator();
		}
		if (generator == null) {
			generator = new IdGeneratorUuid();
			if (bean != null) {
				bean.getType().setIdGenerator(generator);
			}
		}
		if (argUid == null) {
			this.uuid = generator.generateIdValue();
		} else {
			this.uuid = UUID.fromString(argUid);
		}
	}

	/**
	 * String conversion.
	 * 
	 * @return String representation
	 */
	public String toString() {
		return this.uuid.toString();
	}

	/**
	 * @return the hash code
	 */
	public int hashCode() {
		return this.uuid.hashCode();
	}

	/**
	 * compare two numeric ids.
	 * 
	 * @param o the object to compare
	 * 
	 * @return if equals or not
	 */
	public boolean equals(final Object o) {
		if (!(o instanceof IdUuid)) {
			return false;
		}
		IdUuid id = (IdUuid) o;
		return (this.uuid.equals(id.uuid));
	}

	/**
	 * compareTo implementation.
	 * 
	 * @param id the object to compare with
	 * @return -1, 0, 1
	 */
	public int compareTo(final IdUuid id) {
		return this.uuid.compareTo(id.uuid);
	}
}
