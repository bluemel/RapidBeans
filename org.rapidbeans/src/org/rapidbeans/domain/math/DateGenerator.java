/*
 * Rapid Beans Framework: DateGenerator.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 01/14/2007
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.rapidbeans.core.basic.PropertyDate;
import org.rapidbeans.core.common.PrecisionDate;

/**
 * 
 * @author Martin Bluemel
 * 
 */
public class DateGenerator {

	/**
	 * constuctor.
	 */
	public DateGenerator() {
	}

	/**
	 * Generates dates per day.
	 * 
	 * @param from
	 *            start with day
	 * @param to
	 *            end with day
	 * @return the dates generated
	 */
	public ArrayList<Date> generateDays(final Date from, final Date to) {
		ArrayList<Date> dates = new ArrayList<Date>();

		if (from == null || to == null) {
			return dates;
		}
		if (from.getTime() > to.getTime()) {
			throw new IllegalArgumentException("from date greater than to date");
		}
		Date fromExact = new Date(PropertyDate.cutPrecisionLong(from.getTime(), PrecisionDate.day));
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(fromExact);
		while (calendar.getTime().getTime() < to.getTime()) {
			dates.add(calendar.getTime());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		dates.add(calendar.getTime());

		return dates;
	}

}
