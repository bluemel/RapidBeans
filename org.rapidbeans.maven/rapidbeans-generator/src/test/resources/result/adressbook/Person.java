/*
 * Completely generated code file: Person.java
 * !!!Do not edit manually!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.test.addressbook.Person
 * 
 * model:    
 * template: 
 */

package org.rapidbeans.test.addressbook;


import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.LinkFrozen;
import org.rapidbeans.core.exception.UnresolvedLinkException;



/**
 * Rapid Bean class: Person.
 * Completely generated Java class
 * !!!Do not edit manually!!!
 **/
public class Person extends RapidBeanImplStrict {


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
     * property "email".
     */
    private org.rapidbeans.core.basic.PropertyString email;

    /**
     * property "sex".
     */
    private org.rapidbeans.core.basic.PropertyChoice sex;

    /**
     * property "length".
     */
    private org.rapidbeans.core.basic.PropertyQuantity length;

    /**
     * property "address".
     */
    private org.rapidbeans.core.basic.PropertyAssociationend address;

    /**
     * property "groups".
     */
    private org.rapidbeans.core.basic.PropertyAssociationend groups;

    /**
     * property references initialization.
     */
    public void initProperties() {
        this.lastname = (org.rapidbeans.core.basic.PropertyString)
            this.getProperty("lastname");
        this.firstname = (org.rapidbeans.core.basic.PropertyString)
            this.getProperty("firstname");
        this.dateofbirth = (org.rapidbeans.core.basic.PropertyDate)
            this.getProperty("dateofbirth");
        this.email = (org.rapidbeans.core.basic.PropertyString)
            this.getProperty("email");
        this.sex = (org.rapidbeans.core.basic.PropertyChoice)
            this.getProperty("sex");
        this.length = (org.rapidbeans.core.basic.PropertyQuantity)
            this.getProperty("length");
        this.address = (org.rapidbeans.core.basic.PropertyAssociationend)
            this.getProperty("address");
        this.groups = (org.rapidbeans.core.basic.PropertyAssociationend)
            this.getProperty("groups");
    }



    /**
     * default constructor.
     */
    public Person() {
        super();

    }

    /**
     * constructor out of a string.
     * @param s the string
     */
    public Person(final String s) {
        super(s);

    }

    /**
     * constructor out of a string array.
     * @param sa the string array
     */
    public Person(final String[] sa) {
        super(sa);
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
     * @param argValue value of Property 'lastname' to set
     */
    public void setLastname(
        final String argValue) {
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
     * @param argValue value of Property 'firstname' to set
     */
    public void setFirstname(
        final String argValue) {
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
     * @param argValue value of Property 'dateofbirth' to set
     */
    public void setDateofbirth(
        final java.util.Date argValue) {
        this.dateofbirth.setValue(argValue);
    }

    /**
     * @return value of Property 'email'
     */
    public String getEmail() {
        try {
            return (String) this.email.getValue();
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("email");
        }
    }

    /**
     * setter for Property 'email'.
     * @param argValue value of Property 'email' to set
     */
    public void setEmail(
        final String argValue) {
        this.email.setValue(argValue);
    }

    /**
     * @return value of Property 'sex'
     */
    public org.rapidbeans.domain.org.Sex getSex() {
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
     * @param argValue value of Property 'sex' to set
     */
    public void setSex(
        final org.rapidbeans.domain.org.Sex argValue) {
        java.util.List<org.rapidbeans.domain.org.Sex> list =
            new java.util.ArrayList<org.rapidbeans.domain.org.Sex>();
        list.add(argValue);
        this.sex.setValue(list);
    }

    /**
     * @return value of Property 'length'
     */
    public org.rapidbeans.domain.math.Length getLength() {
        try {
            return (org.rapidbeans.domain.math.Length) this.length.getValue();
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("length");
        }
    }

    /**
     * setter for Property 'length'.
     * @param argValue value of Property 'length' to set
     */
    public void setLength(
        final org.rapidbeans.domain.math.Length argValue) {
        this.length.setValue(argValue);
    }

    /**
     * @return value of Property 'address'
     */
    @SuppressWarnings("unchecked")
    public org.rapidbeans.test.addressbook.Address getAddress() {
        try {
            org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.test.addressbook.Address> col
                = (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.test.addressbook.Address>) this.address.getValue();
            if (col == null || col.size() == 0) {
                return null;
            } else {
                Link link = (Link) col.iterator().next();
                if (link instanceof LinkFrozen) {
                    throw new UnresolvedLinkException("unresolved link to \""
                            + "org.rapidbeans.test.addressbook.Address"
                            + "\" \"" + link.getIdString() + "\"");
                } else {
                    return (org.rapidbeans.test.addressbook.Address) col.iterator().next();
                }
            }
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("address");
        }
    }

    /**
     * setter for Property 'address'.
     * @param argValue value of Property 'address' to set
     */
    public void setAddress(
        final org.rapidbeans.test.addressbook.Address argValue) {
        this.address.setValue(argValue);
    }

    /**
     * @return value of Property 'groups'
     */
    @SuppressWarnings("unchecked")
    public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.test.addressbook.Group> getGroups() {
        try {
            return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.test.addressbook.Group>)
            this.groups.getValue();
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("groups");
        }
    }

    /**
     * setter for Property 'groups'.
     * @param argValue value of Property 'groups' to set
     */
    public void setGroups(
        final java.util.Collection<org.rapidbeans.test.addressbook.Group> argValue) {
        this.groups.setValue(argValue);
    }

    /**
     * add method for Property 'groups'.
     * @param bean the bean to add
     */
    public void addGroup(final org.rapidbeans.test.addressbook.Group bean) {
        ((org.rapidbeans.core.basic.PropertyCollection) this.groups).addLink(bean);
    }

    /**
     * remove method for Property 'groups'.
     * @param bean the bean to add
     */
    public void removeGroup(final org.rapidbeans.test.addressbook.Group bean) {
        ((org.rapidbeans.core.basic.PropertyCollection) this.groups).removeLink(bean);
    }
}
