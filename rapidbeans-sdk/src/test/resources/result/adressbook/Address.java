/*
 * Completely generated code file: Address.java
 * !!!Do not edit manually!!!
 *
 * Rapid Beans bean generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans bean type
 * org.rapidbeans.test.addressbook.Address
 * 
 * model:    
 * template: 
 */

package org.rapidbeans.test.addressbook;


import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.type.TypeRapidBean;


/**
 * Rapid Bean class: Address.
 * Completely generated Java class
 * !!!Do not edit manually!!!
 **/
public class Address extends RapidBeanImplStrict {


    /**
     * property "street".
     */
    private org.rapidbeans.core.basic.PropertyString street;

    /**
     * property "zipcode".
     */
    private org.rapidbeans.core.basic.PropertyString zipcode;

    /**
     * property "city".
     */
    private org.rapidbeans.core.basic.PropertyString city;

    /**
     * property "country".
     */
    private org.rapidbeans.core.basic.PropertyString country;

    /**
     * property "inhabitants".
     */
    private org.rapidbeans.core.basic.PropertyAssociationend inhabitants;

    /**
     * property references initialization.
     */
    public void initProperties() {
        this.street = (org.rapidbeans.core.basic.PropertyString)
            this.getProperty("street");
        this.zipcode = (org.rapidbeans.core.basic.PropertyString)
            this.getProperty("zipcode");
        this.city = (org.rapidbeans.core.basic.PropertyString)
            this.getProperty("city");
        this.country = (org.rapidbeans.core.basic.PropertyString)
            this.getProperty("country");
        this.inhabitants = (org.rapidbeans.core.basic.PropertyAssociationend)
            this.getProperty("inhabitants");
    }



    /**
     * default constructor.
     */
    public Address() {
        super();

    }

    /**
     * constructor out of a string.
     * @param s the string
     */
    public Address(final String s) {
        super(s);

    }

    /**
     * constructor out of a string array.
     * @param sa the string array
     */
    public Address(final String[] sa) {
        super(sa);
    }

    /**
     * the bean's type (class variable).
     */
    private static TypeRapidBean type = TypeRapidBean.createInstance(Address.class);
	

    /**
     * @return the Biz Bean's type
     */
    public TypeRapidBean getType() {
        return type;
    }

    /**
     * @return value of Property 'street'
     */
    public String getStreet() {
        try {
            return (String) this.street.getValue();
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("street");
        }
    }

    /**
     * setter for Property 'street'.
     * @param argValue value of Property 'street' to set
     */
    public void setStreet(
        final String argValue) {
        this.street.setValue(argValue);
    }

    /**
     * @return value of Property 'zipcode'
     */
    public String getZipcode() {
        try {
            return (String) this.zipcode.getValue();
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("zipcode");
        }
    }

    /**
     * setter for Property 'zipcode'.
     * @param argValue value of Property 'zipcode' to set
     */
    public void setZipcode(
        final String argValue) {
        this.zipcode.setValue(argValue);
    }

    /**
     * @return value of Property 'city'
     */
    public String getCity() {
        try {
            return (String) this.city.getValue();
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("city");
        }
    }

    /**
     * setter for Property 'city'.
     * @param argValue value of Property 'city' to set
     */
    public void setCity(
        final String argValue) {
        this.city.setValue(argValue);
    }

    /**
     * @return value of Property 'country'
     */
    public String getCountry() {
        try {
            return (String) this.country.getValue();
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("country");
        }
    }

    /**
     * setter for Property 'country'.
     * @param argValue value of Property 'country' to set
     */
    public void setCountry(
        final String argValue) {
        this.country.setValue(argValue);
    }

    /**
     * @return value of Property 'inhabitants'
     */
    @SuppressWarnings("unchecked")
    public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.test.addressbook.Person> getInhabitants() {
        try {
            return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.test.addressbook.Person>)
            this.inhabitants.getValue();
        } catch (NullPointerException e) {
            throw new org.rapidbeans.core.exception.PropNotInitializedException("inhabitants");
        }
    }

    /**
     * setter for Property 'inhabitants'.
     * @param argValue value of Property 'inhabitants' to set
     */
    public void setInhabitants(
        final java.util.Collection<org.rapidbeans.test.addressbook.Person> argValue) {
        this.inhabitants.setValue(argValue);
    }

    /**
     * add method for Property 'inhabitants'.
     * @param bean the bean to add
     */
    public void addInhabitant(final org.rapidbeans.test.addressbook.Person bean) {
        ((org.rapidbeans.core.basic.PropertyCollection) this.inhabitants).addLink(bean);
    }

    /**
     * remove method for Property 'inhabitants'.
     * @param bean the bean to add
     */
    public void removeInhabitant(final org.rapidbeans.test.addressbook.Person bean) {
        ((org.rapidbeans.core.basic.PropertyCollection) this.inhabitants).removeLink(bean);
    }
}
