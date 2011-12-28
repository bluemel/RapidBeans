/*
 * Rapid Beans Framework: TimeOfDay.java
 *
 * Copyright (C) 2009 Martin Bluemel
 *
 * Creation Date: 01/10/2006
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

package org.rapidbeans.domain.math;

import java.math.BigDecimal;

import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeRapidQuantity;


/**
 * Convenience quantity class.
 */
public final class TimeOfDay extends Time {

    /**
     * constructor out of a string.
     * @param s the string
     */
    public TimeOfDay(final String s) {
        super(s);
    }

    /**
     * constructor out of a BigDecimal and a UnitEnum.
     * @param magnitude the number
     * @param unit the unit
     */
    public TimeOfDay(final BigDecimal magnitude, final UnitTime unit) {
        super(magnitude, unit);
        switch (unit) {
        case h:
            break;
        case min:
            break;
        case s:
            break;
        default:
            throw new ValidationException("invalid.prop.timeofday.unit",
            		this,
                    "Invalid unit for TimeOfDay."
                    + "\nOnly hours, minutes or seconds are accepted");
        }
    }

    /**
     * the quantity's type (class variable).
     */
    private static TypeRapidQuantity type = TypeRapidQuantity.createInstance(TimeOfDay.class);

    /**
     * @return the quantity's type
     */
    public TypeRapidQuantity getType() {
        return type;
    }

    /**
     * toString() implementation for TimeOfDay.
     * @return the String representation for time of day. The unit is not printed.
     */
    public String toString() {
        String sHours, sMinutes, sSeconds;
        int seconds, minutes, hours, mins, secs, secsLeft;
        switch ((UnitTime) this.getUnit()) {
        case h:
            return this.getMagnitude().toString();
        case min:
            mins = (int) this.getMagnitude().toBigInteger().longValue();
            hours = mins / 60;
            minutes = mins % 60;
            sHours = Integer.toString(hours);
            sMinutes = Integer.toString(minutes);
            if (minutes < 10) {
                sMinutes = "0" + sMinutes;
            }
            return sHours + ":" + sMinutes;
        case s:
            secs = (int) this.getMagnitude().toBigInteger().longValue();
            hours = secs / 3600;
            secsLeft = secs % 3600;
            minutes = secsLeft / 60;
            seconds = secsLeft % 60;
            sHours = Integer.toString(hours);
            sMinutes = Integer.toString(minutes);
            sSeconds = Integer.toString(seconds);
            if (minutes < 10) {
                sMinutes = "0" + sMinutes;
            }
            if (seconds < 10) {
                sSeconds = "0" + sSeconds;
            }
            return sHours + ":" + sMinutes + ":" + sSeconds;
        default:
            throw new RapidBeansRuntimeException("Invalid Unit " + this.getUnit().toString()
                    + " for TimeOfDay");
        }
    }

    /**
     * parse the magnitude.
     * @param token the String
     * @return the magnitude
     */
    public BigDecimal parseMagnitudeOneToken(final String token) {
        BigDecimal magnitude;
        if (token.matches("[0-9]?[0-9]")) {
            magnitude = new BigDecimal(Integer.parseInt(token));
        } else if (token.matches("[0-9][0-9]:[0-9][0-9]")) {
            magnitude = new BigDecimal(Integer.parseInt(token.substring(0, 2)) * 60
                    + Integer.parseInt(token.substring(3, 5)));
        } else if (token.matches("[0-9]:[0-9][0-9]")) {
            magnitude = new BigDecimal(Integer.parseInt(token.substring(0, 1)) * 60
                    + Integer.parseInt(token.substring(2, 4)));
        } else if (token.matches("[0-9][0-9]:[0-9][0-9]:[0-9][0-9]")) {
            magnitude = new BigDecimal(Integer.parseInt(token.substring(0, 2)) * 3600
                    + Integer.parseInt(token.substring(3, 5)) * 60
                    + Integer.parseInt(token.substring(6, 8)));
        } else if (token.matches("[0-9]:[0-9][0-9]:[0-9][0-9]")) {
            magnitude = new BigDecimal(Integer.parseInt(token.substring(0, 1)) * 3600
                    + Integer.parseInt(token.substring(2, 4)) * 60
                    + Integer.parseInt(token.substring(5, 7)));
        } else {
            throw new ValidationException("invalid.prop.timeofday.magnitude",
            		this,
                    "Invalid magnitude string for TimeOfDay: \"" + token + "\"");
        }
        return magnitude;
    }

    /**
     * parse the unit.
     * @param token the String
     * @return the unit
     */
    public UnitTime parseUnitOneToken(final String token) {
        UnitTime unit;
        if (token.matches("[0-9]?[0-9]")) {
            unit = UnitTime.h;
        } else if (token.matches("[0-9]?[0-9]:[0-9][0-9]")) {
            unit = UnitTime.min;
        } else if (token.matches("[0-9]?[0-9]:[0-9][0-9]:[0-9][0-9]")) {
            unit = UnitTime.s;
        } else {
            throw new ValidationException("invalid.prop.timeofday.parse.unit",
            		this,
                    "Invalid unit for TimeOfDay."
                    + "\nOnly hours or minutes are accepted");
        }
        return unit;
    }

    /**
     * @return the hours as int.
     */
    public int getHours() {
        return this.getMagnitude().intValue() / 60;
    }

    /**
     * @return the minutest as int.
     */
    public int getMinutes() {
        return this.getMagnitude().intValue() % 60;
    }

    public int compareTo(final TimeOfDay o) {
        final int comp = super.compareTo(o);
        return comp;
    }
}
