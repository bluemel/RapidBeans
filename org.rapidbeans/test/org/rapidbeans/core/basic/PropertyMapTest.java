/*
 * RapidBeans Framework: PropertyMapTest.java
 * 
 * Copyright Martin Bluemel, 2007
 * 
 * 13.12.2007
 */
package org.rapidbeans.core.basic;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.exception.ModelValidationException;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TypePropertyMap;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.XmlNode;
import org.rapidbeans.test.TestHelper;
import org.rapidbeans.test.codegen.Person;
import org.rapidbeans.test.codegen.PersonMap;

/**
 * Unit Tests for property class PropertyMap.
 * 
 * @author Martin Bluemel
 */
public class PropertyMapTest extends TestCase {

	/**
	 * Test method for no default value.
	 */
	public void testDefaultNullAndGetValue() {
		this.createTestBean();
		PropertyMap prop = createMapProperty(
				"<property name=\"test\""
						+ " targettype=\"TestBean\""
						+ " />");
		assertNull(prop.getValue());
	}

	/**
	 * Test method for empty default value.
	 * An empty collection should be initialized.
	 */
	public void testDefaultEmpty() {
		this.createTestBean();
		PropertyMap prop = this.createMapProperty(
				"<property name=\"test\""
						+ " targettype=\"TestBean\""
						+ " default=\"\"/>");
		Map<String, Link> map = prop.getValue();
		assertEquals(0, map.size());
	}

	/**
	 * Creating a collection property with no target type
	 * is supposed to provoke a RapidBeansRuntimeException.
	 */
	public void testNoTargetType() {
		try {
			this.createMapProperty(
					"<property name=\"locales\"/>");
			fail("expected RapidBeansRuntimeException");
		} catch (ModelValidationException e) {
			assertTrue(e.getMessage().contains("no targettype specified"));
		}
	}

	/**
	 * Test method for setValue(Object).
	 */
	public void testSetValueSingleBean() {
		RapidBean bean = this.createTestBean();
		PropertyMap prop = this.createMapProperty(
				"<property name=\"test\""
						+ " targettype=\"TestBean\""
						+ " />");
		prop.setValue(new LinkWithKey("bean1", bean));
		Map<String, Link> map = prop.getValue();
		assertEquals(1, map.size());
		assertEquals("bean1", map.keySet().iterator().next());
		assertSame(bean, map.get("bean1"));
		assertSame(bean, map.values().iterator().next());
		ReadonlyListCollection<Link> links = prop.getValueCollection();
		assertEquals(1, links.size());
		assertSame(bean, links.iterator().next());
	}

	/**
	 * Test method for setValue(Object).
	 */
	public void testSetValueMultipleBeans() {
		RapidBean bean1 = this.createTestBean("Bl�mel", "Martin", "19641014");
		RapidBean bean2 = this.createTestBean("Bl�mel", "Ulrike", "19620802");
		PropertyMap prop = this.createMapProperty(
				"<property name=\"test\""
						+ " targettype=\"TestBean\""
						+ " />");
		Map<String, Link> newMap = new HashMap<String, Link>();
		newMap.put("bean1", bean1);
		newMap.put("bean2", bean2);
		prop.setValue(newMap);
		Map<String, Link> map = prop.getValue();
		assertEquals(2, map.size());
		assertSame(bean1, map.get("bean1"));
		assertSame(bean2, map.get("bean2"));
		Iterator<String> iter1 = map.keySet().iterator();
		assertEquals("bean1", iter1.next());
		assertEquals("bean2", iter1.next());
		Iterator<Link> iter2 = map.values().iterator();
		assertSame(bean1, iter2.next());
		assertSame(bean2, iter2.next());
		Collection<Link> links = (Collection<Link>) prop.getValueCollection();
		assertEquals(2, links.size());
		Iterator<Link> iter3 = links.iterator();
		assertSame(bean1, iter3.next());
		assertSame(bean2, iter3.next());
	}

	/**
	 * test creating association instances
	 * by adding two links successively.
	 * 
	 * 1 to n Association: Address - Person
	 * Collection Properties:
	 * - Address.inhabitants [*]
	 * - Person.address [1]
	 */
	public void testAddLinkWithInverseNormalCollection() {

		TypeRapidBean.forName("org.rapidbeans.test.codegen.Address").setIdGenerator(
				new IdGeneratorNumeric());
		// create 1 Address and 2 Persons
		PersonMap personMap = new PersonMap();
		Person martin = new Person("\"Martin\" \"Bl�mel\" \"19641014\"");
		Person jojo = new Person("\"Johannes\" \"Bl�mel\" \"19641014\"");
		assertNull(personMap.getPropValue("persons"));
		assertNull(personMap.getPersons());
		assertNull(martin.getPersonmap());
		assertNull(jojo.getPersonmap());

		//        // add the 2 Persons to the person map
		//        personMap.putPerson("martin", martin);
		//        personMap.putPerson("jojo", jojo);
		//
		//        // assert the Persons being linked to the Address
		//        assertEquals(2, personMap.getPersons().values().size());
		//        Iterator<?> iter = personMap.getPersons().values().iterator();
		//        assertSame(martin, iter.next());
		//        assertSame(jojo, iter.next());
		// assert the Address being linked to both Persons implicitly
		//        assertSame(personMap, martin.getPersonmap());
		//        assertSame(personMap, jojo.getPersonMap());
	}

	//    /**
	//     * test creating association instances
	//     * by adding multiple links successively.
	//     *
	//     * n to n Association: ClosingPeriod - Location
	//     */
	//    public void testAddLinkWithInverseNN() {
	//
	//        // create 2 ClosingPeriods and 2 Locations
	//        String[] sa1 = {"20051225", "XMas Holidays", "20060101"};
	//        ClosingPeriod cp1 = new ClosingPeriod(sa1);
	//        String[] sa2 = {"20060704", "IndependenceDay", "20060704"};
	//        ClosingPeriod cp2 = new ClosingPeriod(sa2);
	//        Location loc1 = new Location("\"Location A\"");
	//        assertEquals("Location A", loc1.getIdString());
	//        Location loc2 = new Location("\"Location B\"");
	//        cp1.addLocation(loc1);
	//        cp1.addLocation(loc2);
	//        assertEquals(2, cp1.getLocations().size());
	//        assertEquals(1, loc1.getClosedons().size());
	//        ClosingPeriod cp11Inverse = loc1.getClosedons().iterator().next();
	//        assertSame(cp1, cp11Inverse);
	//        assertEquals(1, loc1.getClosedons().size());
	//        ClosingPeriod cp12Inverse = loc2.getClosedons().iterator().next();
	//        assertSame(cp1, cp12Inverse);
	//        assertEquals(1, loc2.getClosedons().size());
	//        cp2.addLocation(loc1);
	//        cp2.addLocation(loc2);
	//        assertEquals(2, cp1.getLocations().size());
	//        assertEquals(2, cp2.getLocations().size());
	//        assertEquals(2, loc1.getClosedons().size());
	//        assertEquals(2, loc2.getClosedons().size());
	//        Iterator<ClosingPeriod> it = loc1.getClosedons().iterator();
	//        assertSame(cp1, it.next());
	//        assertSame(cp2, it.next());
	//        it = loc2.getClosedons().iterator();
	//        assertSame(cp1, it.next());
	//        assertSame(cp2, it.next());
	//
	//        cp2.removeLocation(loc1);
	//        assertEquals(2, cp1.getLocations().size());
	//        assertEquals(1, cp2.getLocations().size());
	//        assertEquals(1, loc1.getClosedons().size());
	//        assertEquals(2, loc2.getClosedons().size());
	//     }
	//
	//    /**
	//     * test creating association instances
	//     * by setting two links all at once.
	//     *
	//     * 1 to n Association: Address - Person
	//     * Collection Properties:
	//     * - Address.inhabitants [*]
	//     * - Person.address [1]
	//     */
	//    public void testSetLinkWithInverse() {
	//
	//        // create 1 Address and 2 Persons
	//        Address adr = new Address();
	//        Person martin = new Person("\"Martin\" \"Bl�mel\" \"19641014\"");
	//        Person jojo = new Person("\"Johannes\" \"Bl�mel\" \"19641014\"");
	//        assertNull(adr.getInhabitants());
	//        assertNull(martin.getAddress());
	//        assertNull(jojo.getAddress());
	//
	//        // create a new collection and use the collection
	//        // property's setter
	//        Collection<Person> persons = new ArrayList<Person>();
	//        persons.add(martin);
	//        persons.add(jojo);
	//        adr.setInhabitants(persons);
	//
	//        // assert the Persons being linked to the Address
	//        assertEquals(2, adr.getInhabitants().size());
	//        Iterator iter = adr.getInhabitants().iterator();
	//        assertSame(martin, iter.next());
	//        assertSame(jojo, iter.next());
	//        // assert the Address being linked to both Persons implicitely
	//        assertSame(adr, martin.getAddress());
	//        assertSame(adr, jojo.getAddress());
	//    }
	//
	//    /**
	//     * test deleting association instances
	//     * by removing two links successively.
	//     *
	//     * 1 to n Association: Address - Person
	//     * Collection Properties:
	//     * - Address.inhabitants [*]
	//     * - Person.address [1]
	//     */
	//    public void testRemoveLinkWithInverse() {
	//        Address adr = new Address();
	//        adr.getType().setIdGenerator(new IdGeneratorNumeric());
	//        assertEquals(1, ((IdNumeric) adr.getId()).getNumber());
	//        Person martin = new Person("\"Martin\" \"Bl�mel\" \"19641014\"");
	//        Person jojo = new Person("\"Johannes\" \"Bl�mel\" \"19641014\"");
	//        adr.addInhabitant(martin);
	//        adr.addInhabitant(jojo);
	//
	//        adr.removeInhabitant(jojo);
	//
	//        assertEquals(1, adr.getInhabitants().size());
	//        Iterator iter = adr.getInhabitants().iterator();
	//        assertSame(martin, iter.next());
	//        assertSame(adr, martin.getAddress());
	//        assertNull(jojo.getAddress());
	//
	//        // removing last link via remove link produces
	//        // an empty collection but no null value
	//        adr.removeInhabitant(martin);
	//
	//        assertEquals(0, adr.getInhabitants().size());
	//        assertNull(jojo.getAddress());
	//        assertNull(martin.getAddress());
	//    }
	//
	//    /**
	//     * test deleting association instances
	//     * by setting new values for the collection property.
	//     *
	//     * 1 to n Association: Address - Person
	//     * Collection Properties:
	//     * - Address.inhabitants [*]
	//     * - Person.address [1]
	//     */
	//    public void testRemoveViaSetLinkWithInverse() {
	//        Address adr = new Address();
	//        adr.getType().setIdGenerator(new IdGeneratorNumeric());
	//        assertEquals(1, ((IdNumeric) adr.getId()).getNumber());
	//        Person martin = new Person("\"Martin\" \"Bl�mel\" \"19641014\"");
	//        Person jojo = new Person("\"Johannes\" \"Bl�mel\" \"19641014\"");
	//        adr.addInhabitant(martin);
	//        adr.addInhabitant(jojo);
	//
	//        Collection<Person> persons = new ArrayList<Person>();
	//        persons.add(martin);
	//        adr.setInhabitants(persons);
	//
	//        assertEquals(1, adr.getInhabitants().size());
	//        Iterator iter = adr.getInhabitants().iterator();
	//        assertSame(martin, iter.next());
	//        assertSame(adr, martin.getAddress());
	//        assertNull(jojo.getAddress());
	//
	//        adr.setInhabitants(new ArrayList<Person>());
	//
	//        assertEquals(0, adr.getInhabitants().size());
	//        assertNull(martin.getAddress());
	//        assertNull(jojo.getAddress());
	//
	//        adr.setInhabitants(null);
	//
	//        assertNull(adr.getInhabitants());
	//        assertNull(martin.getAddress());
	//        assertNull(jojo.getAddress());
	//    }
	//
	//    /**
	//     * test add a component bean to a composite bean.
	//     */
	//    public void testAddLinkComponent() {
	//        Submenu root = new Submenu("root");
	//        assertNull(root.getParentBean());
	//        assertEquals("root", root.getName());
	//        assertNull(root.getMenuentrys());
	//
	//        MenuItem item1 = new MenuItem("item1");
	//        assertNull(item1.getParentBean());
	//        root.addMenuentry(item1);
	//        assertEquals(1, root.getMenuentrys().size());
	//        assertSame(item1, root.getMenuentrys().iterator().next());
	//        assertSame(root, item1.getParentBean());
	//
	//        Submenu submenu1 = new Submenu("submenu1");
	//        assertNull(submenu1.getParentBean());
	//        root.addMenuentry(submenu1);
	//
	//        Iterator iter = root.getMenuentrys().iterator();
	//        assertEquals(2, root.getMenuentrys().size());
	//        assertSame(item1, iter.next());
	//        assertSame(submenu1, iter.next());
	//        assertSame(root, item1.getParentBean());
	//        assertSame(root, submenu1.getParentBean());
	//    }
	//
	//    /**
	//     * test add a component bean to a composite bean.
	//     */
	//    public void testAddLinkComponentViaSet() {
	//        Submenu root = new Submenu("root");
	//        assertNull(root.getParentBean());
	//        assertEquals("root", root.getName());
	//        assertNull(root.getMenuentrys());
	//
	//        MenuItem item1 = new MenuItem("item1");
	//        assertNull(item1.getParentBean());
	//        Collection<MenuEntry> col = new ArrayList<MenuEntry>();
	//        col.add(item1);
	//        root.setMenuentrys(col);
	//
	//        assertEquals(1, root.getMenuentrys().size());
	//        assertSame(item1, root.getMenuentrys().iterator().next());
	//        assertSame(root, item1.getParentBean());
	//
	//        Submenu submenu1 = new Submenu("submenu1");
	//        col = new ArrayList<MenuEntry>();
	//        col.add(item1);
	//        col.add(submenu1);
	//        assertNull(submenu1.getParentBean());
	//        root.setMenuentrys(col);
	//
	//        Iterator iter = root.getMenuentrys().iterator();
	//        assertEquals(2, root.getMenuentrys().size());
	//        assertSame(item1, iter.next());
	//        assertSame(submenu1, iter.next());
	//        assertSame(root, item1.getParentBean());
	//        assertSame(root, submenu1.getParentBean());
	//    }
	//
	//    /**
	//     * test remove a component bean from a composite.
	//     */
	//    public void testRemoveLinkComponent() {
	//        Submenu root = new Submenu("root");
	//        MenuItem item1 = new MenuItem("item1");
	//        root.addMenuentry(item1);
	//        Submenu submenu1 = new Submenu("submenu1");
	//        root.addMenuentry(submenu1);
	//
	//        root.removeMenuentry(submenu1);
	//        assertEquals(1, root.getMenuentrys().size());
	//        assertSame(item1, root.getMenuentrys().iterator().next());
	//        assertNull(submenu1.getParentBean());
	//        root.removeMenuentry(item1);
	//        assertEquals(0, root.getMenuentrys().size());
	//        assertNull(item1.getParentBean());
	//    }
	//
	//    /**
	//     * test remove a component bean from a composite.
	//     */
	//    public void testRemoveLinkComponentViaSet() {
	//        Submenu root = new Submenu("root");
	//        MenuItem item1 = new MenuItem("item1");
	//        root.addMenuentry(item1);
	//        Submenu submenu1 = new Submenu("submenu1");
	//        root.addMenuentry(submenu1);
	//
	//        Collection<MenuEntry> col = new ArrayList<MenuEntry>();
	//        col.add(item1);
	//        root.setMenuentrys(col);
	//        assertEquals(1, root.getMenuentrys().size());
	//        assertSame(item1, root.getMenuentrys().iterator().next());
	//        assertNull(submenu1.getParentBean());
	//
	//        root.setMenuentrys(new ArrayList<MenuEntry>());
	//        assertEquals(0, root.getMenuentrys().size());
	//        assertNull(item1.getParentBean());
	//    }
	//
	//    /**
	//     * Test method for toString() of a single bean.
	//     */
	//    @SuppressWarnings("unchecked")
	//    public void testToStringSingleBean() {
	//        RapidBean bean = this.createTestBean();
	//        PropertyCollection prop = this.createCollectionProperty(
	//                "<property name=\"test\""
	//                + " targettype=\"TestBean\""
	//                + " />");
	//        prop.setValue(bean);
	//        assertEquals("Bl�mel_Martin_19641014", prop.toString());
	//    }
	//
	//    /**
	//     * Test method for toString() of a collection with three beans.
	//     */
	//    @SuppressWarnings("unchecked")
	//    public void testToStringMultipleBeans() {
	//        RapidBean bean1 = this.createTestBean("Bl�mel", "Martin", "19641014");
	//        RapidBean bean2 = this.createTestBean("Bl�mel", "Ulrike", "19620802");
	//        RapidBean bean3 = this.createTestBean("Keinki", "Katharina", "19901119");
	//        PropertyCollection prop = this.createCollectionProperty(
	//                "<property name=\"test\""
	//                + " targettype=\"TestBean\""
	//                + " />");
	//        Collection<RapidBean> col = new ArrayList<RapidBean>();
	//        col.add(bean1);
	//        col.add(bean2);
	//        col.add(bean3);
	//        prop.setValue(col);
	//        assertEquals("Bl�mel_Martin_19641014,Bl�mel_Ulrike_19620802,Keinki_Katharina_19901119", prop.toString());
	//    }
	//
	//    /**
	//     * Happy day test for validation.
	//     */
	//    public void testValidateOk() {
	//        RapidBean bean = createTestBean("Bl�mel", "Martin", "19641014");
	//        PropertyCollection prop = this.createCollectionProperty(
	//                "<property name=\"test\""
	//                + " targettype=\"TestBean\""
	//                + " />");
	//        prop.validate(bean);
	//    }
	//
	//    /**
	//     * Test for validation if the bean has not the specified target type.
	//     */
	//    public void testValidateWrongType() {
	//        RapidBean bean = createTestBean("Bl�mel", "Martin", "19641014");
	//        PropertyCollection prop = this.createCollectionProperty(
	//                "<property name=\"test\""
	//                + " targettype=\"org.rapidbeans.test.TestBean\""
	//                + " />");
	//        try {
	//            prop.validate(bean);
	//            fail("expected ValidationException");
	//        } catch (ValidationException e) {
	//            assertTrue(true);
	//        }
	//    }
	//
	//    /**
	//     * test immutability.
	//     * proove that our PropertyCollection is immutable after getValue
	//     *
	//     * @throws java.text.ParseException if parsing fails
	//     */
	//    @SuppressWarnings("unchecked")
	//    public void testImmutabilityGet() throws java.text.ParseException {
	//
	//        PropertyCollection prop = this.createCollectionProperty(
	//                "<property name=\"test\""
	//                + " targettype=\"org.rapidbeans.test.TestBean\""
	//                + " />");
	//
	//        // originally prop's value is null (undefined)
	//        assertNull(prop.getValue());
	//
	//        TestBean bean1 = new TestBean("\"Bluemel\" \"Martin\" \"19641014\"");
	//        TestBean bean2 = new TestBean("\"Bluemel\" \"Johannes\" \"19641014\"");
	//        Collection<Link> col = new ArrayList();
	//        col.add(bean1);
	//        col.add(bean2);
	//        prop.setValue(col);
	//
	//        // then I try to remove an object of the collection via the iterator
	//        // returned by the getter
	//        Collection<Link> col1 = prop.getValue();
	//        try {
	//            col1.clear();
	//        } catch (ImmutableCollectionException e) {
	//            assertTrue(true);
	//        }
	//
	//        // of course our prop stays the same
	//        assertEquals(2, ((Collection<RapidBean>) prop.getValue()).size());
	//    }
	//
	//    /**
	//     * test a tree set.
	//     */
	//    public void testNMAssociationInverseArrayList() {
	//        // configure collection properties of Location and ClosingPeriod
	//        // to use TreeSet as collection implementing class
	//        ((TypePropertyCollection) (new Location()).getProperty("closedons")
	//                .getType()).setCollectionClass(ArrayList.class);
	//        ((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations")
	//                .getType()).setCollectionClass(ArrayList.class);
	//        tstNMAssociationInverse();
	//    }
	//
	//    /**
	//     * test a tree set.
	//     */
	//    public void testNMAssociationInverseLinkedHashSet() {
	//        // configure collection properties of Location and ClosingPeriod
	//        // to use TreeSet as collection implementing class
	//        ((TypePropertyCollection) (new Location()).getProperty("closedons")
	//                .getType()).setCollectionClass(LinkedHashSet.class);
	//        ((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations")
	//                .getType()).setCollectionClass(LinkedHashSet.class);
	//        tstNMAssociationInverse();
	//    }
	//
	//    /**
	//     * test a tree set.
	//     */
	//    public void testNMAssociationInverseTreeSet() {
	//        // configure collection properties of Location and ClosingPeriod
	//        // to use TreeSet as collection implementing class
	//        ((TypePropertyCollection) (new Location()).getProperty("closedons")
	//                .getType()).setCollectionClass(TreeSet.class);
	//        ((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations")
	//                .getType()).setCollectionClass(TreeSet.class);
	//        tstNMAssociationInverse();
	//    }
	//
	//    /**
	//     * test colection properties used in conjunction with an N:M association.
	//     */
	//    private void tstNMAssociationInverse() {
	//        // configure collection properties of Location and ClosingPeriod
	//        // to use TreeSet as collection implementing class
	//        ((TypePropertyCollection) (new Location()).getProperty("closedons")
	//                .getType()).setCollectionClass(TreeSet.class);
	//        ((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations")
	//                .getType()).setCollectionClass(TreeSet.class);
	//        // set up two locations and closing periods
	//        ClosingPeriod cp1 = new ClosingPeriod("\"20050101\" \"1\"");
	//        ClosingPeriod cp2 = new ClosingPeriod("\"20050201\" \"2\"");
	//        Location locA = new Location("\"A\"");
	//        Location locB = new Location("\"B\"");
	//
	//        // at the beginning all four colection properties are
	//        // undefinded (null)
	//        assertNull(locA.getClosedons());
	//        assertNull(locB.getClosedons());
	//        assertNull(cp1.getLocations());
	//        assertNull(cp2.getLocations());
	//
	//        // link locA with cp1
	//        locA.addClosedon(cp1);
	//        assertEquals(1, locA.getClosedons().size());
	//        ReadonlyListCollection cps = (ReadonlyListCollection) locA.getClosedons();
	//        assertSame(cp1, cps.get(0));
	//        assertNull(locB.getClosedons());
	//        ReadonlyListCollection locs = (ReadonlyListCollection) cp1.getLocations();
	//        assertSame(locA, locs.get(0));
	//        assertEquals(1, cp1.getLocations().size());
	//        assertNull(cp2.getLocations());
	//
	//        // link locA with cp2
	//        locA.addClosedon(cp2);
	//        assertEquals(2, locA.getClosedons().size());
	//        cps = (ReadonlyListCollection) locA.getClosedons();
	//        assertSame(cp1, cps.get(0));
	//        assertSame(cp2, cps.get(1));
	//        assertNull(locB.getClosedons());
	//        assertEquals(1, cp1.getLocations().size());
	//        locs = (ReadonlyListCollection) cp1.getLocations();
	//        assertSame(locA, locs.get(0));
	//        assertEquals(1, cp2.getLocations().size());
	//        locs = (ReadonlyListCollection) cp2.getLocations();
	//        assertSame(locA, locs.get(0));
	//
	//        // link locB with cp1
	//        locB.addClosedon(cp1);
	//        assertEquals(2, locA.getClosedons().size());
	//        cps = (ReadonlyListCollection) locA.getClosedons();
	//        assertSame(cp1, cps.get(0));
	//        assertSame(cp2, cps.get(1));
	//        assertEquals(1, locB.getClosedons().size());
	//        cps = (ReadonlyListCollection) locB.getClosedons();
	//        assertSame(cp1, cps.get(0));
	//        assertEquals(2, cp1.getLocations().size());
	//        locs = (ReadonlyListCollection) cp1.getLocations();
	//        assertSame(locA, locs.get(0));
	//        assertSame(locB, locs.get(1));
	//        assertEquals(1, cp2.getLocations().size());
	//        locs = (ReadonlyListCollection) cp2.getLocations();
	//        assertSame(locA, locs.get(0));
	//
	//        // link locB with cp2
	//        locB.addClosedon(cp2);
	//        assertEquals(2, locA.getClosedons().size());
	//        cps = (ReadonlyListCollection) locA.getClosedons();
	//        assertSame(cp1, cps.get(0));
	//        assertSame(cp2, cps.get(1));
	//        assertEquals(2, locB.getClosedons().size());
	//        cps = (ReadonlyListCollection) locB.getClosedons();
	//        assertSame(cp1, cps.get(0));
	//        assertSame(cp2, cps.get(1));
	//        assertEquals(2, cp1.getLocations().size());
	//        locs = (ReadonlyListCollection) cp1.getLocations();
	//        assertSame(locA, locs.get(0));
	//        assertSame(locB, locs.get(1));
	//        assertEquals(2, cp2.getLocations().size());
	//        locs = (ReadonlyListCollection) cp2.getLocations();
	//        assertSame(locA, locs.get(0));
	//        assertSame(locB, locs.get(1));
	//
	//        assertEquals(2, cp1.getLocations().size());
	//        try {
	//            cp1.addLocation(locA);
	//            fail("expected ValidationInstanceAssocTwiceException");
	//        } catch (ValidationInstanceAssocTwiceException e) {
	//            assertTrue(true);
	//        }
	//        assertEquals(2, cp1.getLocations().size());
	//        try {
	//            cp1.addLocation(locB);
	//            fail("expected ValidationInstanceAssocTwiceException");
	//        } catch (ValidationInstanceAssocTwiceException e) {
	//            assertTrue(true);
	//        }
	//        assertEquals(2, cp1.getLocations().size());
	//        assertEquals(2, locB.getClosedons().size());
	//        try {
	//            locB.addClosedon(cp1);
	//            fail("expected ValidationInstanceAssocTwiceException");
	//        } catch (ValidationInstanceAssocTwiceException e) {
	//            assertTrue(true);
	//        }
	//        assertEquals(2, locB.getClosedons().size());
	//        try {
	//            locB.addClosedon(cp2);
	//            fail("expected ValidationInstanceAssocTwiceException");
	//        } catch (ValidationInstanceAssocTwiceException e) {
	//            assertTrue(true);
	//        }
	//        assertEquals(2, locB.getClosedons().size());
	//    }

	/**
	 * create a generic TestBean.
	 * 
	 * @return the test bean
	 */
	private GenericBean createTestBean() {
		return this.createTestBean("Bl�mel", "Martin", "19641014");
	}

	/**
	 * create a generic TestBean.
	 * 
	 * @param name
	 *            the last name
	 * @param prename
	 *            the first name
	 * @param dateofbirth
	 *            the date of birth
	 * @return the test bean
	 */
	private GenericBean createTestBean(final String name,
			final String prename, final String dateofbirth) {
		String descr = "<beantype name=\"TestBean\" idtype=\"keyprops\">"
				+ "<property name=\"name\" key=\"true\"/>"
				+ "<property name=\"prename\" key=\"true\"/>"
				+ "<property name=\"dateofbirth\" type=\"date\" key=\"true\"/>"
				+ "</beantype>";
		GenericBean bean = null;
		TypeRapidBean testBeanType = (TypeRapidBean) RapidBeansTypeLoader.getInstance().lookupType("TestBean");
		if (testBeanType == null) {
			bean = TestHelper.createGenericBeanInstance(descr);
			RapidBeansTypeLoader.getInstance().registerType(bean.getType());
		} else {
			bean = new GenericBean(testBeanType);
		}
		bean.setPropValue("name", name);
		bean.setPropValue("prename", prename);
		bean.setPropValue("dateofbirth", dateofbirth);
		return bean;
	}

	/**
	 * set up a Collection Property.
	 * 
	 * @param descr
	 *            the XML property type description
	 * 
	 * @return a new Collection property.
	 */
	private PropertyMap createMapProperty(final String descr) {
		XmlNode propertyNode = XmlNode.getDocumentTopLevel(
				new ByteArrayInputStream(descr.getBytes()));
		TypePropertyMap type = new TypePropertyMap(new XmlNode[] { propertyNode }, null);
		return new PropertyMap(type, null);
	}

	/**
	 * common tear down method.
	 */
	public void tearDown() {
		if (RapidBeansTypeLoader.getInstance().lookupType("TestBean") != null) {
			RapidBeansTypeLoader.getInstance().unregisterType("TestBean");
		}
	}
}
