/*
 * Rapid Beans Framework: IdGeneratorUuid.java
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

import java.util.UUID;

/**
 * @author bluemel
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class IdGeneratorUuid  implements IdGenerator {

    /**
     * the generation method.
     *
     * @return the ID value object.
     */
    public UUID generateIdValue() {
        return UUID.randomUUID();
    }
}
