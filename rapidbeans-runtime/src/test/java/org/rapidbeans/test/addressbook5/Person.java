/*
 * Partially generated code file: Person.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans 
				bean
			 generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans 
				bean
			 type
 * org.rapidbeans.test.addressbook5.Person
 * 
 * model:    testmodel/org/rapidbeans/test/addressbook5/Person.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.test.addressbook5;

// BEGIN manual code section
// Person.import
import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.LinkFrozen;
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.exception.UnresolvedLinkException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.domain.math.Length;
import org.rapidbeans.domain.org.Sex;
import org.rapidbeans.security.User;
import org.rapidbeans.test.addressbook.Group;

// END manual code section

/**
 * Rapid Bean class: Person. Partially generated Java class !!!Do only edit
 * manually in marked sections!!!
 **/
public class Person extends RapidBeanImplStrict {
	// BEGIN manual code section
	// Person.classBody
	// END manual code section

	/**
	 * property "lastname".
	 */
	private org.rapidbeans.core.basic.PropertyString lastname;

	/**
	 * property "firstname".
	 */
	private org.rapidbeans.core.basic.PropertyString firstname;

	/**
	 * property "dateofbirth".
	 */
	private org.rapidbeans.core.basic.PropertyDate dateofbirth;

	/**
	 * property "shoesize".
	 */
	private org.rapidbeans.core.basic.PropertyInteger shoesize;

	/**
	 * property "sex".
	 */
	private org.rapidbeans.core.basic.PropertyChoice sex;

	/**
	 * property "height".
	 */
	private org.rapidbeans.core.basic.PropertyQuantity height;

	/**
	 * property "groups".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend groups;

	/**
	 * property "user".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend user;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.lastname = (org.rapidbeans.core.basic.PropertyString) this.getProperty("lastname");
		this.firstname = (org.rapidbeans.core.basic.PropertyString) this.getProperty("firstname");
		this.dateofbirth = (org.rapidbeans.core.basic.PropertyDate) this.getProperty("dateofbirth");
		this.shoesize = (org.rapidbeans.core.basic.PropertyInteger) this.getProperty("shoesize");
		this.sex = (org.rapidbeans.core.basic.PropertyChoice) this.getProperty("sex");
		this.height = (org.rapidbeans.core.basic.PropertyQuantity) this.getProperty("height");
		this.groups = (org.rapidbeans.core.basic.PropertyAssociationend) this.getProperty("groups");
		this.user = (org.rapidbeans.core.basic.PropertyAssociationend) this.getProperty("user");
	}

	/**
	 * default constructor.
	 */
	public Person() {
		super();
		// BEGIN manual code section
		// Person.Person()
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * 
	 * @param s the string
	 */
	public Person(final String s) {
		super(s);
		// BEGIN manual code section
		// Person.Person(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * 
	 * @param sa the string array
	 */
	public Person(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// Person.Person(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(Person.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}

	/**
	 * @return value of Property 'lastname'
	 */
	public String getLastname() {
		try {
			return (String) this.lastname.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("lastname");
		}
	}

	/**
	 * setter for Property 'lastname'.
	 * 
	 * @param argValue value of Property 'lastname' to set
	 */
	public void setLastname(final String argValue) {
		this.lastname.setValue(argValue);
	}

	/**
	 * @return value of Property 'firstname'
	 */
	public String getFirstname() {
		try {
			return (String) this.firstname.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("firstname");
		}
	}

	/**
	 * setter for Property 'firstname'.
	 * 
	 * @param argValue value of Property 'firstname' to set
	 */
	public void setFirstname(final String argValue) {
		this.firstname.setValue(argValue);
	}

	/**
	 * @return value of Property 'dateofbirth'
	 */
	public java.util.Date getDateofbirth() {
		try {
			return (java.util.Date) this.dateofbirth.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("dateofbirth");
		}
	}

	/**
	 * setter for Property 'dateofbirth'.
	 * 
	 * @param argValue value of Property 'dateofbirth' to set
	 */
	public void setDateofbirth(final java.util.Date argValue) {
		this.dateofbirth.setValue(argValue);
	}

	/**
	 * @return value of Property 'shoesize'
	 */
	public int getShoesize() {
		try {
			return ((org.rapidbeans.core.basic.PropertyInteger) this.shoesize).getValueInt();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("shoesize");
		}
	}

	/**
	 * setter for Property 'shoesize'.
	 * 
	 * @param argValue value of Property 'shoesize' to set
	 */
	public void setShoesize(final int argValue) {
		this.shoesize.setValue(new Integer(argValue));
	}

	/**
	 * @return value of Property 'sex'
	 */
	@SuppressWarnings("unchecked")
	public java.util.List<Sex> getSex() {
		try {
			return (java.util.List<Sex>) this.sex.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("sex");
		}
	}

	/**
	 * setter for Property 'sex'.
	 * 
	 * @param argValue value of Property 'sex' to set
	 */
	public void setSex(final java.util.List<Sex> argValue) {
		this.sex.setValue(argValue);
	}

	/**
	 * @return value of Property 'height'
	 */
	public Length getHeight() {
		try {
			return (Length) this.height.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("height");
		}
	}

	/**
	 * setter for Property 'height'.
	 * 
	 * @param argValue value of Property 'height' to set
	 */
	public void setHeight(final Length argValue) {
		this.height.setValue(argValue);
	}

	/**
	 * @return value of Property 'groups'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.core.common.ReadonlyListCollection<Group> getGroups() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<Group>) this.groups.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("groups");
		}
	}

	/**
	 * setter for Property 'groups'.
	 * 
	 * @param argValue value of Property 'groups' to set
	 */
	public void setGroups(final java.util.Collection<Group> argValue) {
		this.groups.setValue(argValue);
	}

	/**
	 * add method for Property 'groups'.
	 * 
	 * @param bean the bean to add
	 */
	public void addGroup(final Group bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.groups).addLink(bean);
	}

	/**
	 * remove method for Property 'groups'.
	 * 
	 * @param bean the bean to remove
	 */
	public void removeGroup(final Group bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.groups).removeLink(bean);
	}

	/**
	 * @return value of Property 'user'
	 */
	@SuppressWarnings("unchecked")
	public User getUser() {
		try {
			org.rapidbeans.core.common.ReadonlyListCollection<User> col = (org.rapidbeans.core.common.ReadonlyListCollection<User>) this.user
					.getValue();
			if (col == null || col.size() == 0) {
				return null;
			} else {
				Link link = (Link) col.iterator().next();
				if (link instanceof LinkFrozen) {
					throw new UnresolvedLinkException(
							"unresolved link to \"" + "User" + "\" \"" + link.getIdString() + "\"");
				} else {
					return (User) col.iterator().next();
				}
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("user");
		}
	}

	/**
	 * setter for Property 'user'.
	 * 
	 * @param argValue value of Property 'user' to set
	 */
	public void setUser(final User argValue) {
		this.user.setValue(argValue);
	}

	/**
	 * @return value of Property 'depname'
	 */
	public String getDepname() {
		// BEGIN manual code section
		// Person. getDepname()
		final StringBuffer depname = new StringBuffer();
		if (getSex() != null && getSex().size() > 0) {
			int i = 0;
			for (final Sex sex : getSex()) {
				if (i > 0) {
					depname.append('/');
				}
				switch (sex) {
				case male:
					depname.append("Mr.");
					break;
				case female:
					depname.append("Mrs.");
					break;
				default:
					depname.append("??");
				}
				i++;
			}
		}
		if (getFirstname() != null) {
			if (depname.length() > 0) {
				depname.append(' ');
			}
			depname.append(getFirstname());
		}
		if (getLastname() != null) {
			if (depname.length() > 0) {
				depname.append(' ');
			}
			depname.append(getLastname());
		}
		return depname.toString();
		// END manual code section
	}
}
