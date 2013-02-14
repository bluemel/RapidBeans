/*
 * Rapid Beans Framework: PersonTest.java
 * 
 * Copyright Martin Bluemel, 2010
 * 
 * 08.10.2010
 */

package org.rapidbeans.test.addressbook5;

import junit.framework.TestCase;

/**
 * Tests for the Person.
 * 
 * @author Martin Bluemel
 */
public class PersonTest extends TestCase {

	/**
	 * Test how dependent property depname works.
	 */
	public void testDepname() {
		// TODO reactivate
		//        Person person = new Person();
		//        Property depname = person.getProperty("depname");
		//        List<TypeProperty> deps = depname.getType().getDependentFromProps();
		//        assertEquals(3, deps.size());
		//        assertEquals(person.getProperty("sex").getType(), deps.get(0));
		//        assertEquals(person.getProperty("firstname").getType(), deps.get(1));
		//        assertEquals(person.getProperty("lastname").getType(), deps.get(2));
		//        assertEquals("", person.getDepname());
		//        person.setFirstname("Martin");
		//        assertEquals("Martin", person.getDepname());
		//        person.setFirstname(null);
		//        person.setLastname("Bluemel");
		//        assertEquals("Bluemel", person.getDepname());
		//        person.setFirstname("Martin");
		//        assertEquals("Martin Bluemel", person.getDepname());
		//        List<Sex> sexes = new ArrayList<Sex>();
		//        sexes.add(Sex.male);
		//        person.setSex(sexes);
		//        assertEquals("Mr. Martin Bluemel", person.getDepname());
		//        assertEquals("Mr. Martin Bluemel", person.getProperty("depname").getValue());
		//        sexes.add(Sex.female);
		//        person.setSex(sexes);
		//        assertEquals("Mr./Mrs. Martin Bluemel", person.getDepname());
	}
}
