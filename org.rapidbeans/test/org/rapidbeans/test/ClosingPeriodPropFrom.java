/*
 * Rapid Beans Application RapidClubAdmin: ClosingPeriodPropFrom.java
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
public class ClosingPeriodPropFrom extends PropertyDate {

	/**
	 * constructor.
	 * 
	 * @param type
	 *            the property type
	 * @param parentBean
	 *            the parent bean
	 */
	public ClosingPeriodPropFrom(final TypeProperty type, final RapidBean parentBean) {
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
		Date toTime = null;
		try {
			toTime = ((ClosingPeriod) this.getBean()).getTo();
		} catch (PropNotInitializedException e) {
			toTime = null;
		}
		if (toTime != null) {
			if (date.getTime() > toTime.getTime()) {
				throw new ValidationException("invalid.prop.closingperiod.from.greater.to",
						this,
						"invalid value \"" + date.toString()
								+ "\" for property \"from\""
								+ " greater than property \"to\" = \""
								+ toTime.toString() + "\"");
			}
		}
		return date;
	}
}

//        // BEGIN manual code section
//        // ClosingPeriod.toBeforeChange()
//        final Date fromTime = this.getFrom();
//        if (fromTime != null) {
//            if (((Date) e.getNewValue()).getTime() < fromTime.getTime()) {
//                throw new ValidationException("invalid.prop.closingperiod.from.less.from",
//                        "invalid value \"" + e.getNewValue().toString()
//                        + "\"for property \"to\""
//                        + " smaller than property \"from\" = \""
//                        + this.getProperty("to").toString() + "\"");
//            }
//        }
//        // END manual code section
