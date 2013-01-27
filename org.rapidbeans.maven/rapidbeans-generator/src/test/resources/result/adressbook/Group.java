/*
 * Completely generated code file: Group.java
 * !!!Do not edit manually!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.test.addressbook.Group
 * 
 * model:    
 * template: 
 */

package org.rapidbeans.test.addressbook;


import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypeRapidBean;


/**
 * Rapid Bean class: Group.
 * Completely generated Java class
 * !!!Do not edit manually!!!
 **/
public class Group extends RapidBeanImplStrict {


    /**
     * property "name".
     */
    private org.rapidbeans.core.basic.PropertyString name;

    /**
     * property "childGroups".
     */
    private org.rapidbeans.core.basic.PropertyAssociationend childGroups;

    /**
     * property "persons".
     */
    private org.rapidbeans.core.basic.PropertyAssociationend persons;

    /**
     * property references initialization.
     */
    public void initProperties() {
        this.name = (org.rapidbeans.core.basic.PropertyString)
            this.getProperty("name");
        this.childGroups = (org.rapidbeans.core.basic.PropertyAssociationend)
            this.getProperty("childGroups");
        this.persons = (org.rapidbeans.core.basic.PropertyAssociationend)
            this.getProperty("persons");
    }



    /**
     * default constructor.
     */
    public Group() {
        super();

    }

    /**
     * constructor out of a string.
     * @param s the string
     */
    public Group(final String s) {
        super(s);

    }

    /**
     * constructor out of a string array.
     * @param sa the string array
     */
    public Group(final String[] sa) {
        super(sa);
    }

    /**
     * the bean's type (class variable).
     */
    private static TypeRapidBean type = TypeRapidBean.createInstance(Group.class);
	

    /**
     * @return the Biz Bean's type
     */
    public TypeRapidBean getType() {
        return type;
    }

    /**
     * @return value of Property 'name'
     */
    public String getName() {
        try {
            return (String) this.name.getValue();
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("name");
        }
    }

    /**
     * setter for Property 'name'.
     * @param argValue value of Property 'name' to set
     */
    public void setName(
        final String argValue) {
        this.name.setValue(argValue);
    }

    /**
     * @return value of Property 'childGroups'
     */
    @SuppressWarnings("unchecked")
    public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.test.addressbook.Group> getChildGroups() {
        try {
            return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.test.addressbook.Group>)
            this.childGroups.getValue();
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("childGroups");
        }
    }

    /**
     * setter for Property 'childGroups'.
     * @param argValue value of Property 'childGroups' to set
     */
    public void setChildGroups(
        final java.util.Collection<org.rapidbeans.test.addressbook.Group> argValue) {
        this.childGroups.setValue(argValue);
    }

    /**
     * add method for Property 'childGroups'.
     * @param bean the bean to add
     */
    public void addChildGroup(final org.rapidbeans.test.addressbook.Group bean) {
        ((org.rapidbeans.core.basic.PropertyCollection) this.childGroups).addLink(bean);
    }

    /**
     * remove method for Property 'childGroups'.
     * @param bean the bean to add
     */
    public void removeChildGroup(final org.rapidbeans.test.addressbook.Group bean) {
        ((org.rapidbeans.core.basic.PropertyCollection) this.childGroups).removeLink(bean);
    }

    /**
     * @return value of Property 'persons'
     */
    @SuppressWarnings("unchecked")
    public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.test.addressbook.Person> getPersons() {
        try {
            return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.test.addressbook.Person>)
            this.persons.getValue();
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("persons");
        }
    }

    /**
     * setter for Property 'persons'.
     * @param argValue value of Property 'persons' to set
     */
    public void setPersons(
        final java.util.Collection<org.rapidbeans.test.addressbook.Person> argValue) {
        this.persons.setValue(argValue);
    }

    /**
     * add method for Property 'persons'.
     * @param bean the bean to add
     */
    public void addPerson(final org.rapidbeans.test.addressbook.Person bean) {
        ((org.rapidbeans.core.basic.PropertyCollection) this.persons).addLink(bean);
    }

    /**
     * remove method for Property 'persons'.
     * @param bean the bean to add
     */
    public void removePerson(final org.rapidbeans.test.addressbook.Person bean) {
        ((org.rapidbeans.core.basic.PropertyCollection) this.persons).removeLink(bean);
    }
}
