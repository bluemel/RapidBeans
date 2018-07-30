/*
 * Completely generated code file: Addressbook.java
 * !!!Do not edit manually!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.test.addressbook.Addressbook
 * 
 * model:    
 * template: 
 */

package org.rapidbeans.test.addressbook;


import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypeRapidBean;


/**
 * Rapid Bean class: Addressbook.
 * Completely generated Java class
 * !!!Do not edit manually!!!
 **/
public class Addressbook extends RapidBeanImplStrict {


    /**
     * property "persons".
     */
    private org.rapidbeans.core.basic.PropertyAssociationend persons;

    /**
     * property "adresses".
     */
    private org.rapidbeans.core.basic.PropertyAssociationend adresses;

    /**
     * property "groups".
     */
    private org.rapidbeans.core.basic.PropertyAssociationend groups;

    /**
     * property references initialization.
     */
    public void initProperties() {
        this.persons = (org.rapidbeans.core.basic.PropertyAssociationend)
            this.getProperty("persons");
        this.adresses = (org.rapidbeans.core.basic.PropertyAssociationend)
            this.getProperty("adresses");
        this.groups = (org.rapidbeans.core.basic.PropertyAssociationend)
            this.getProperty("groups");
    }



    /**
     * default constructor.
     */
    public Addressbook() {
        super();

    }

    /**
     * constructor out of a string.
     * @param s the string
     */
    public Addressbook(final String s) {
        super(s);

    }

    /**
     * constructor out of a string array.
     * @param sa the string array
     */
    public Addressbook(final String[] sa) {
        super(sa);
    }

    /**
     * the bean's type (class variable).
     */
    private static TypeRapidBean type = TypeRapidBean.createInstance(Addressbook.class);
	

    /**
     * @return the Biz Bean's type
     */
    public TypeRapidBean getType() {
        return type;
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

    /**
     * @return value of Property 'adresses'
     */
    @SuppressWarnings("unchecked")
    public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.test.addressbook.Address> getAdresses() {
        try {
            return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.test.addressbook.Address>)
            this.adresses.getValue();
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("adresses");
        }
    }

    /**
     * setter for Property 'adresses'.
     * @param argValue value of Property 'adresses' to set
     */
    public void setAdresses(
        final java.util.Collection<org.rapidbeans.test.addressbook.Address> argValue) {
        this.adresses.setValue(argValue);
    }

    /**
     * add method for Property 'adresses'.
     * @param bean the bean to add
     */
    public void addAdresse(final org.rapidbeans.test.addressbook.Address bean) {
        ((org.rapidbeans.core.basic.PropertyCollection) this.adresses).addLink(bean);
    }

    /**
     * remove method for Property 'adresses'.
     * @param bean the bean to add
     */
    public void removeAdresse(final org.rapidbeans.test.addressbook.Address bean) {
        ((org.rapidbeans.core.basic.PropertyCollection) this.adresses).removeLink(bean);
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
