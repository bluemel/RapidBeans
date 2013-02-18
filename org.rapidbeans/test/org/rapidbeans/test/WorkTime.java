/*
 * Partially generated code file: WorkTime.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.test.WorkTime
 * 
 * model:    testmodel/org/rapidbeans/test/WorkTime.xml
 * template: codegentemplates/genBean.xsl
 */

package org.rapidbeans.test;

// BEGIN manual code section
// WorkTime.import
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Locale;

import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.domain.math.Time;
import org.rapidbeans.domain.math.UnitTime;

// END manual code section

/**
 * Rapid Bean class: WorkTime. Partially generated Java class !!!Do only edit
 * manually in marked sections!!!
 **/
public class WorkTime extends RapidBeanImplStrict {

	// BEGIN manual code section
	// WorkTime.classBody
	/**
	 * Date formatter.
	 */
	static final DateFormat DFDATE = DateFormat.getDateInstance(
			DateFormat.MEDIUM, Locale.GERMAN);

	/**
	 * Date formatter.
	 */
	static final DateFormat DFTIME = DateFormat.getDateTimeInstance(
			DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMAN);

	/**
	 * @return dependent attribute time
	 */
	public Time getTime() {
		if (this.from.getValue() == null || this.to.getValue() == null) {
			return null;
		}
		long diff = getTo().getTime() - getFrom().getTime();
		return new Time(new BigDecimal((int) (diff / 60000)), UnitTime.min);
	}

	// END manual code section

	/**
	 * property "from".
	 */
	private org.rapidbeans.core.basic.PropertyDate from;

	/**
	 * property "to".
	 */
	private org.rapidbeans.core.basic.PropertyDate to;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.from = (org.rapidbeans.core.basic.PropertyDate) this
				.getProperty("from");
		this.to = (org.rapidbeans.core.basic.PropertyDate) this
				.getProperty("to");
	}

	/**
	 * default constructor.
	 */
	public WorkTime() {
		super();
		// BEGIN manual code section
		// WorkTime.WorkTime()
		// END manual code section

	}

	/**
	 * constructor out of a string.
	 * 
	 * @param s
	 *            the string
	 */
	public WorkTime(final String s) {
		super(s);
		// BEGIN manual code section
		// WorkTime.WorkTime(String)
		// END manual code section

	}

	/**
	 * constructor out of a string array.
	 * 
	 * @param sa
	 *            the string array
	 */
	public WorkTime(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// WorkTime.WorkTime(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean
			.createInstance(WorkTime.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}

	/**
	 * @return value of Property 'from'
	 */
	public java.util.Date getFrom() {
		try {
			return (java.util.Date) this.from.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException(
					"from");
		}
	}

	/**
	 * setter for Property 'from'.
	 * 
	 * @param argValue
	 *            value of Property 'from' to set
	 */
	public void setFrom(final java.util.Date argValue) {
		this.from.setValue(argValue);
	}

	/**
	 * @return value of Property 'to'
	 */
	public java.util.Date getTo() {
		try {
			return (java.util.Date) this.to.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException(
					"to");
		}
	}

	/**
	 * setter for Property 'to'.
	 * 
	 * @param argValue
	 *            value of Property 'to' to set
	 */
	public void setTo(final java.util.Date argValue) {
		this.to.setValue(argValue);
	}
}
