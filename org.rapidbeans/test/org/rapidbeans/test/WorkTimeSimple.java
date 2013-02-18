/*
 * Partially generated code file: WorkTimeSimple.java
 * !!!Do only edit manually in marked sections!!!
 * 
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 * 
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.test.WorkTimeSimple
 * 
 * model: testmodel/org/rapidbeans/test/WorkTimeSimple.xml
 * template: codegentemplates/genBean.xsl
 */

package org.rapidbeans.test;

// BEGIN manual code section
// WorkTimeSimple.import
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.rapidbeans.core.basic.Property;
import org.rapidbeans.core.basic.RapidBeanImplSimple;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.domain.math.Time;
import org.rapidbeans.domain.math.UnitTime;

// END manual code section

/**
 * Rapid Bean class: WorkTime. Partially generated Java class !!!Do only edit
 * manually in marked sections!!!
 **/
public class WorkTimeSimple extends RapidBeanImplSimple {

	// BEGIN manual code section
	// WorkTimeSimple.classBody
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
		if (this.from == null || this.to == null) {
			return null;
		}
		long diff = getTo().getTime() - getFrom().getTime();
		return new Time(new BigDecimal((int) (diff / 60000)), UnitTime.min);
	}

	// END manual code section

	/**
	 * property "from".
	 */
	private Date from;

	/**
	 * property "to".
	 */
	private Date to;

	/**
	 * property default values initialization.
	 */
	public void initProperties() {
		this.from = (Date) getType().getPropertyType("from").getDefaultValue();
		this.to = (Date) getType().getPropertyType("to").getDefaultValue();
	}

	/**
	 * default constructor.
	 */
	public WorkTimeSimple() {
		super();
		// BEGIN manual code section
		// WorkTimeSimple.WorkTimeSimple()
		// END manual code section

	}

	/**
	 * constructor out of a string.
	 * 
	 * @param s
	 *            the string
	 */
	public WorkTimeSimple(final String s) {
		super(s);
		// BEGIN manual code section
		// WorkTimeSimple.WorkTimeSimple(String)
		// END manual code section

	}

	/**
	 * constructor out of a string array.
	 * 
	 * @param sa
	 *            the string array
	 */
	public WorkTimeSimple(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// WorkTimeSimple.WorkTimeSimple(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean
			.createInstance(WorkTimeSimple.class);

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
		if (this.from == null) {
			return null;
		} else {
			return new Date(this.from.getTime());
		}
	}

	/**
	 * setter for Property 'from'.
	 * 
	 * @param argValue
	 *            value of Property 'from' to set
	 */
	public void setFrom(final Date from) {
		Property.createInstance(getType().getPropertyType("from"), this)
				.setValue(from);
	}

	/**
	 * inner setter for Property 'from'.
	 * 
	 * @param argValue
	 *            value of Property 'from' to set
	 */
	public void _setFrom(final Date from) {
		this.from = from;
	}

	/**
	 * @return value of Property 'to'
	 */
	public java.util.Date getTo() {
		if (this.to == null) {
			return null;
		} else {
			return new Date(this.to.getTime());
		}
	}

	/**
	 * setter for Property 'to'.
	 * 
	 * @param to
	 *            value of Property 'to' to set
	 */
	public void setTo(final Date to) {
		Property.createInstance(getType().getPropertyType("to"), this)
				.setValue(to);
	}

	/**
	 * inner setter for Property 'to'.
	 * 
	 * @param argValue
	 *            value of Property 'to' to set
	 */
	public void _setTo(final java.util.Date to) {
		this.to = to;
	}
}
