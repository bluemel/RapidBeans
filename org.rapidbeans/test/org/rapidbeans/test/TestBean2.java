/*
 * test code file: TestBean2.java
 */

package org.rapidbeans.test;

import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypeRapidBean;

public class TestBean2 extends RapidBeanImplStrict {

	/**
	 * property "surname".
	 */
	private org.rapidbeans.core.basic.PropertyString surname;

	/**
	 * property "prename".
	 */
	private org.rapidbeans.core.basic.PropertyString prename;

	/**
	 * property "dateofbirth".
	 */
	private org.rapidbeans.core.basic.PropertyDate dateofbirth;

	/**
	 * property "city".
	 */
	private org.rapidbeans.core.basic.PropertyString city;

	/**
	 * property "zipcode".
	 */
	private org.rapidbeans.core.basic.PropertyInteger zipcode;

	/**
	 * property "email".
	 */
	private org.rapidbeans.core.basic.PropertyString email;

	/**
	 * property "sex".
	 */
	private org.rapidbeans.core.basic.PropertyChoice sex;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.surname = (org.rapidbeans.core.basic.PropertyString)
				this.getProperty("surname");
		this.prename = (org.rapidbeans.core.basic.PropertyString)
				this.getProperty("prename");
		this.dateofbirth = (org.rapidbeans.core.basic.PropertyDate)
				this.getProperty("dateofbirth");
		this.city = (org.rapidbeans.core.basic.PropertyString)
				this.getProperty("city");
		this.zipcode = (org.rapidbeans.core.basic.PropertyInteger)
				this.getProperty("zipcode");
		this.email = (org.rapidbeans.core.basic.PropertyString)
				this.getProperty("email");
		this.sex = (org.rapidbeans.core.basic.PropertyChoice)
				this.getProperty("sex");
	}

	/**
	 * default constructor.
	 */
	public TestBean2() {
		super();

	}

	/**
	 * constructor out of a string.
	 * 
	 * @param s
	 *            the string
	 */
	public TestBean2(final String s) {
		super(s);

	}

	/**
	 * constructor out of a string array.
	 * 
	 * @param sa
	 *            the string array
	 */
	public TestBean2(final String[] sa) {
		super(sa);
	}

	/**
	 * the Biz Bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(TestBean2.class);

	/**
	 * @return the Biz Bean's type
	 */
	public final TypeRapidBean getType() {
		return type;
	}

	/**
	 * @return value of Property 'surname'
	 */
	public final String getSurname() {
		try {
			return (String) this.surname.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("surname");
		}
	}

	/**
	 * setter for Property 'surname'.
	 * 
	 * @param argValue
	 *            value of Property 'surname' to set
	 */
	public final void setSurname(
			final String argValue) {
		this.surname.setValue(argValue);
	}

	/**
	 * @return value of Property 'prename'
	 */
	public final String getPrename() {
		try {
			return (String) this.prename.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("prename");
		}
	}

	/**
	 * setter for Property 'prename'.
	 * 
	 * @param argValue
	 *            value of Property 'prename' to set
	 */
	public final void setPrename(
			final String argValue) {
		this.prename.setValue(argValue);
	}

	/**
	 * @return value of Property 'dateofbirth'
	 */
	public final java.util.Date getDateofbirth() {
		try {
			return (java.util.Date) this.dateofbirth.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("dateofbirth");
		}
	}

	/**
	 * setter for Property 'dateofbirth'.
	 * 
	 * @param argValue
	 *            value of Property 'dateofbirth' to set
	 */
	public final void setDateofbirth(
			final java.util.Date argValue) {
		this.dateofbirth.setValue(argValue);
	}

	/**
	 * @return value of Property 'city'
	 */
	public final String getCity() {
		try {
			return (String) this.city.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("city");
		}
	}

	/**
	 * setter for Property 'city'.
	 * 
	 * @param argValue
	 *            value of Property 'city' to set
	 */
	public final void setCity(
			final String argValue) {
		this.city.setValue(argValue);
	}

	/**
	 * @return value of Property 'zipcode'
	 */
	public final int getZipcode() {
		try {
			return ((org.rapidbeans.core.basic.PropertyInteger) this.zipcode).getValueInt();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("zipcode");
		}
	}

	/**
	 * setter for Property 'zipcode'.
	 * 
	 * @param argValue
	 *            value of Property 'zipcode' to set
	 */
	public final void setZipcode(
			final int argValue) {
		this.zipcode.setValue(new Integer(argValue));
	}

	/**
	 * @return value of Property 'email'
	 */
	public final String getEmail() {
		try {
			return (String) this.email.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("email");
		}
	}

	/**
	 * setter for Property 'email'.
	 * 
	 * @param argValue
	 *            value of Property 'email' to set
	 */
	public final void setEmail(
			final String argValue) {
		this.email.setValue(argValue);
	}

	/**
	 * @return value of Property 'sex'
	 */
	public final org.rapidbeans.domain.org.Sex getSex() {
		try {
			java.util.List<?> enumList = (java.util.List<?>) this.sex.getValue();
			if (enumList == null || enumList.size() == 0) {
				return null;
			} else {
				return (org.rapidbeans.domain.org.Sex) enumList.get(0);
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("sex");
		}
	}

	/**
	 * setter for Property 'sex'.
	 * 
	 * @param argValue
	 *            value of Property 'sex' to set
	 */
	public final void setSex(
			final org.rapidbeans.domain.org.Sex argValue) {
		java.util.List<org.rapidbeans.domain.org.Sex> list =
				new java.util.ArrayList<org.rapidbeans.domain.org.Sex>();
		list.add(argValue);
		this.sex.setValue(list);
	}
}
