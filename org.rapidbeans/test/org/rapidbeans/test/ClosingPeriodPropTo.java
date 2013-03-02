/*
 * Rapid Beans Application RapidClubAdmin: ClosingPeriodPropTo.java
 * 
 * Copyright Martin Bluemel, 2006
 * 
 * 19.10.2006
 */
package org.rapidbeans.test;

import java.util.Date;

import org.rapidbeans.core.basic.PropertyDate;
import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.exception.PropNotInitializedException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.type.TypeProperty;

/**
 * extension from date property ClosingPeriod.from.
 */
public class ClosingPeriodPropTo extends PropertyDate {

	/**
	 * constructor.
	 * 
	 * @param type
	 *            the property type
	 * @param parentBean
	 *            the parent bean
	 */
	public ClosingPeriodPropTo(final TypeProperty type,
			final RapidBean parentBean) {
		super(type, parentBean);
	}

	/**
	 * the special part of the validation.<br>
	 * implicitly also converts the given object.
	 * 
	 * @param newValue
	 *            the value object to validate
	 * 
	 * @return the converted value which is the internal representation or if a
	 *         primitive type the corresponding value object
	 */
	public Date validate(final Object newValue) {
		final Date date = (Date) super.validate(newValue);
		Date fromTime = null;
		try {
			fromTime = ((ClosingPeriod) this.getBean()).getFrom();
		} catch (PropNotInitializedException e) {
			fromTime = null;
		}
		if (fromTime != null) {
			if (date.getTime() < fromTime.getTime()) {
				throw new ValidationException(
						"invalid.prop.closingperiod.from.less.from", this,
						"invalid value \"" + date.toString()
								+ "\" for property \"to\""
								+ " less than property \"from\" = \""
								+ fromTime.toString() + "\"");
			}
		}
		return date;
	}
}

// // BEGIN manual code section
// // ClosingPeriod.toBeforeChange()
// // END manual code section
