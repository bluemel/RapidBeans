package org.rapidbeans.core.basic;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.exception.ImmutableCollectionException;
import org.rapidbeans.core.exception.ModelValidationException;
import org.rapidbeans.core.exception.ValidationException;
import org.rapidbeans.core.exception.ValidationInstanceAssocTwiceException;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.XmlNode;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.MenuEntry;
import org.rapidbeans.presentation.MenuItem;
import org.rapidbeans.presentation.Submenu;
import org.rapidbeans.test.ClosingPeriod;
import org.rapidbeans.test.Location;
import org.rapidbeans.test.TestBean;
import org.rapidbeans.test.TestHelper;
import org.rapidbeans.test.addressbook.Addressbook;
import org.rapidbeans.test.addressbook.Group;
import org.rapidbeans.test.codegen.Address;
import org.rapidbeans.test.codegen.AddressBook;
import org.rapidbeans.test.codegen.Person;
import org.rapidbeans.test.codegen.TestUser;

/**
 * Unit Tests for property class PropertyCollection.
 * 
 * @author Martin Bluemel
 */
public class PropertyCollectionTest {

	/**
	 * common test class tear down method.
	 */
	@AfterClass
	public static void tearDown() {
		if (RapidBeansTypeLoader.getInstance().lookupType("TestBean") != null) {
			RapidBeansTypeLoader.getInstance().unregisterType("TestBean");
		}
	}

	/**
	 * test adding a component bean to a composite bean and removing it
	 * afterwards using generated (production code) classes.
	 */
	@Test
	public void testAddAndRemoveLinkOkComponent() {
		Submenu root = new Submenu("root");
		MenuItem item = new MenuItem("item1");
		// at first property "menuentrys" is undefined.
		Assert.assertNull(root.getMenuentrys());
		Assert.assertNull(item.getParentBean());

		root.addMenuentry(item);
		Assert.assertEquals(1, root.getMenuentrys().size());
		Assert.assertSame(item, root.getMenuentrys().iterator().next());
		Assert.assertSame(root, item.getParentBean());

		root.removeMenuentry(item);
		// after removal "menuentrys" is defined but empty
		Assert.assertEquals(0, root.getMenuentrys().size());
		Assert.assertNull(item.getParentBean());
	}

	/**
	 * Prove that the association end collection is always sorted using a sorted
	 * collection class.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testAddAndRemoveLinkOkComponentTreeSetSorted() {
		TypeRapidBean rtypeParent = (TypeRapidBean) TypeRapidBean
				.forName("org.rapidbeans.test.addressbook5.Addressbook");
		TypePropertyCollection aetypeParentSons = (TypePropertyCollection) rtypeParent.getPropertyType("persons");
		Class<?> colClassBefore = aetypeParentSons.getCollectionClass();
		Assert.assertSame(TreeSet.class, colClassBefore);
		RapidBean adrbook = RapidBeanImplStrict.createInstance("org.rapidbeans.test.addressbook5.Addressbook");
		RapidBean person1 = RapidBeanImplStrict.createInstance("org.rapidbeans.test.addressbook5.Person");
		person1.setPropValue("lastname", "A");
		RapidBean person2 = RapidBeanImplStrict.createInstance("org.rapidbeans.test.addressbook5.Person");
		person2.setPropValue("lastname", "B");
		Assert.assertNull(((PropertyCollection) adrbook.getProperty("persons")).getValue());
		Assert.assertNull(person1.getParentBean());
		Assert.assertNull(person2.getParentBean());

		// add person 2 before person 1
		((PropertyCollection) adrbook.getProperty("persons")).addLink(person2);
		((PropertyCollection) adrbook.getProperty("persons")).addLink(person1);
		Assert.assertEquals(2,
				((Collection<Link>) ((PropertyCollection) adrbook.getProperty("persons")).getValue()).size());
		Iterator<Link> iter = ((Collection<Link>) ((PropertyCollection) adrbook.getProperty("persons")).getValue())
				.iterator();
		Assert.assertSame(person1, iter.next());
		Assert.assertSame(person2, iter.next());
		Assert.assertSame(adrbook, person1.getParentBean());
		Assert.assertSame(adrbook, person2.getParentBean());

		// reset persons and add person 1 before person 2
		((PropertyCollection) adrbook.getProperty("persons")).setValue(null);
		((PropertyCollection) adrbook.getProperty("persons")).addLink(person1);
		((PropertyCollection) adrbook.getProperty("persons")).addLink(person2);
		Assert.assertEquals(2,
				((Collection<Link>) ((PropertyCollection) adrbook.getProperty("persons")).getValue()).size());
		iter = ((Collection<Link>) ((PropertyCollection) adrbook.getProperty("persons")).getValue()).iterator();
		Assert.assertSame(person1, iter.next());
		Assert.assertSame(person2, iter.next());
		Assert.assertSame(adrbook, person1.getParentBean());
		Assert.assertSame(adrbook, person2.getParentBean());

		// remove the links again
		((PropertyCollection) adrbook.getProperty("persons")).removeLink(person2);
		((PropertyCollection) adrbook.getProperty("persons")).removeLink(person1);
		Assert.assertEquals(0,
				((Collection<Link>) ((PropertyCollection) adrbook.getProperty("persons")).getValue()).size());
		Assert.assertNull(person1.getParentBean());
		Assert.assertNull(person2.getParentBean());
	}

	/**
	 * If you work with a set sorted according to properties and you change a
	 * property you have a problem.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testAddAndRemoveLinkOkComponentTreeSetSortedChangeProperties() {
		// initialize one address book containing 4 entries
		TypeRapidBean rtypeParent = (TypeRapidBean) TypeRapidBean
				.forName("org.rapidbeans.test.addressbook5.Addressbook");
		TypePropertyCollection aetypeParentSons = (TypePropertyCollection) rtypeParent.getPropertyType("persons");
		Class<?> colClassBefore = aetypeParentSons.getCollectionClass();
		Assert.assertSame(TreeSet.class, colClassBefore);
		RapidBean adrbook = RapidBeanImplStrict.createInstance("org.rapidbeans.test.addressbook5.Addressbook");
		Assert.assertNull(((PropertyCollection) adrbook.getProperty("persons")).getValue());
		RapidBean person1 = RapidBeanImplStrict.createInstance("org.rapidbeans.test.addressbook5.Person");
		person1.setPropValue("lastname", "B");
		RapidBean person2 = RapidBeanImplStrict.createInstance("org.rapidbeans.test.addressbook5.Person");
		person2.setPropValue("lastname", "C");
		RapidBean person3 = RapidBeanImplStrict.createInstance("org.rapidbeans.test.addressbook5.Person");
		person3.setPropValue("lastname", "D");
		RapidBean person4 = RapidBeanImplStrict.createInstance("org.rapidbeans.test.addressbook5.Person");
		person4.setPropValue("lastname", "E");
		((PropertyCollection) adrbook.getProperty("persons")).addLink(person2);
		((PropertyCollection) adrbook.getProperty("persons")).addLink(person1);
		((PropertyCollection) adrbook.getProperty("persons")).addLink(person4);
		((PropertyCollection) adrbook.getProperty("persons")).addLink(person3);
		Assert.assertEquals(4,
				((Collection<Link>) ((PropertyCollection) adrbook.getProperty("persons")).getValue()).size());
		Iterator<Link> iter = ((Collection<Link>) ((PropertyCollection) adrbook.getProperty("persons")).getValue())
				.iterator();
		Assert.assertSame(person1, iter.next());
		Assert.assertSame(person2, iter.next());
		Assert.assertSame(person3, iter.next());
		Assert.assertSame(person4, iter.next());

		// change one single property and check correct sorting
		person2.setPropValue("lastname", "X");
		iter = ((Collection<Link>) ((PropertyCollection) adrbook.getProperty("persons")).getValue()).iterator();
		Assert.assertSame(person1, iter.next());
		Assert.assertSame(person3, iter.next());
		Assert.assertSame(person4, iter.next());
		Assert.assertSame(person2, iter.next());

		// remove the links agains
		((PropertyCollection) adrbook.getProperty("persons")).removeLink(person1);
		((PropertyCollection) adrbook.getProperty("persons")).removeLink(person2);
		((PropertyCollection) adrbook.getProperty("persons")).removeLink(person3);
		((PropertyCollection) adrbook.getProperty("persons")).removeLink(person4);
		Assert.assertEquals(0,
				((Collection<Link>) ((PropertyCollection) adrbook.getProperty("persons")).getValue()).size());
	}

	/**
	 * Test method for no default value.
	 */
	@Test
	public void testNewOkDefaultNullAndGetValue() {
		this.createTestBean();
		PropertyCollection prop = this.createCollectionProperty("<property name=\"test\"" + " targettype=\"TestBean\""
				+ " />");
		Assert.assertNull(prop.getValue());
	}

	/**
	 * Test method for empty default value. An empty collection should be
	 * initialized.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testNewOkDefaultEmpty() {
		this.createTestBean();
		PropertyCollection prop = this.createCollectionProperty("<property name=\"test\"" + " targettype=\"TestBean\""
				+ " default=\"\"/>");
		Collection<Link> col = (Collection<Link>) prop.getValue();
		Assert.assertEquals(0, col.size());
	}

	/**
	 * Creating a collection property with no target type is supposed to provoke
	 * a RapidBeansRuntimeException.
	 */
	@Test
	public void testNewInvalidNoTargetType() {
		try {
			this.createCollectionProperty("<property name=\"locales\"/>");
			Assert.fail("expected ModelValidationException");
		} catch (ModelValidationException e) {
			Assert.assertTrue(e.getMessage().contains("no targettype specified"));
		}
	}

	/**
	 * Test method for setValue(Object).
	 */
	@Test
	public void testSetValueOkMultNtoN() {
		// create 2 ClosingPeriods and 2 Locations
		String[] sa1 = { "20051225", "XMas Holidays", "20060101" };
		ClosingPeriod cp1 = new ClosingPeriod(sa1);
		String[] sa2 = { "20060704", "IndependenceDay", "20060704" };
		ClosingPeriod cp2 = new ClosingPeriod(sa2);
		Location loc1 = new Location("\"Location A\"");
		Assert.assertEquals("Location A", loc1.getIdString());
		Location loc2 = new Location("\"Location B\"");
		List<Location> locs = new ArrayList<Location>();
		locs.add(loc1);
		locs.add(loc2);
		cp1.setLocations(locs);
		Assert.assertEquals(2, cp1.getLocations().size());
		Assert.assertEquals(1, loc1.getClosedons().size());
		ClosingPeriod cp11Inverse = loc1.getClosedons().iterator().next();
		Assert.assertSame(cp1, cp11Inverse);
		Assert.assertEquals(1, loc1.getClosedons().size());
		ClosingPeriod cp12Inverse = loc2.getClosedons().iterator().next();
		Assert.assertSame(cp1, cp12Inverse);
		Assert.assertEquals(1, loc2.getClosedons().size());
		cp2.setLocations(locs);
		Assert.assertEquals(2, cp1.getLocations().size());
		Assert.assertEquals(2, cp2.getLocations().size());
		Assert.assertEquals(2, loc1.getClosedons().size());
		Assert.assertEquals(2, loc2.getClosedons().size());
		Iterator<ClosingPeriod> it = loc1.getClosedons().iterator();
		Assert.assertSame(cp1, it.next());
		Assert.assertSame(cp2, it.next());
		it = loc2.getClosedons().iterator();
		Assert.assertSame(cp1, it.next());
		Assert.assertSame(cp2, it.next());
	}

	/**
	 * test 1 to 1 Association: User - Person. Simply set the value of a
	 * collection property that was null before.
	 * 
	 * Collection Properties: - TestUser.person [0..1] - Person.user [0..1]
	 */
	@Test
	public void testSetValueMult1to1Ok() {

		// set up
		Person martin = new Person("\"Bl�mel\" \"Martin\" \"19641014\"");
		Person jojo = new Person("\"Bl�mel\" \"Johannes\" \"19641014\"");
		TestUser user = new TestUser("admin");
		Assert.assertNull(martin.getUser());
		Assert.assertNull(jojo.getUser());
		Assert.assertNull(user.getPerson());

		// test
		user.setPerson(martin);
		Assert.assertSame(martin, user.getPerson());
		Assert.assertSame(user, martin.getUser());
		Assert.assertNull(jojo.getUser());
	}

	/**
	 * test 1 to 1 Association: User - Person. Simply set the value of a
	 * collection property that was null before.
	 * 
	 * 1 to 1 Association: User Person
	 * 
	 * Collection Properties: - TestUser.person [0..1] - Person.user [0..1]
	 */
	@Test
	public void testSetValueMult1to1Overwrite() {

		// set up
		Person martin = new Person("\"Bl�mel\" \"Martin\" \"19641014\"");
		Person jojo = new Person("\"Bl�mel\" \"Johannes\" \"19641014\"");
		TestUser user = new TestUser("admin");
		user.setPerson(jojo);
		Assert.assertSame(jojo, user.getPerson());
		Assert.assertSame(user, jojo.getUser());
		Assert.assertNull(martin.getUser());

		// test: relink user to martin
		user.setPerson(martin);
		Assert.assertSame(martin, user.getPerson());
		Assert.assertSame(user, martin.getUser());
		Assert.assertNull(jojo.getUser());
	}

	/**
	 * test 1 to 1 Association: User - Person. Simply set the value of a
	 * collection property that was null before.
	 * 
	 * 1 to 1 Association: User Person
	 * 
	 * Collection Properties: - TestUser.person [0..1] - Person.user [0..1]
	 */
	@Test
	public void testSetValueMult1to1OverwriteInverse() {

		// set up
		Person martin = new Person("\"Bl�mel\" \"Martin\" \"19641014\"");
		Person jojo = new Person("\"Bl�mel\" \"Johannes\" \"19641014\"");
		TestUser user = new TestUser("admin");
		user.setPerson(jojo);
		Assert.assertSame(jojo, user.getPerson());
		Assert.assertSame(user, jojo.getUser());
		Assert.assertNull(martin.getUser());

		// test: relink martin to user (other way round)
		martin.setUser(user);
		Assert.assertSame(martin, user.getPerson());
		Assert.assertSame(user, martin.getUser());
		Assert.assertNull(jojo.getUser());
	}

	@Test
	public void testAddLinkInvalidSameLinkTwiceArrayList() {
		// create 1 Address and 1 Person
		Address adr = new Address();
		Person martin = new Person("\"Martin\" \"Bl�mel\" \"19641014\"");
		TypePropertyCollection proptype = (TypePropertyCollection) adr.getProperty("inhabitants").getType();
		Class<?> colClassBefore = proptype.getCollectionClass();
		try {
			proptype.setCollectionClass(ArrayList.class);
			Assert.assertSame(ArrayList.class, proptype.getCollectionClass());
			Assert.assertNull(adr.getInhabitants());
			Assert.assertNull(martin.getAddress());

			// add the same Person to the Address as inhabitant twice
			adr.addInhabitant(martin);
			try {
				// should Assert.fail because of the association end with
				// multiplicity
				// 1
				adr.addInhabitant(martin);
				Assert.fail("expected ValidationException exception");
			} catch (ValidationException e) {
				Assert.assertTrue(true);
			}
		} finally {
			proptype.setCollectionClass(colClassBefore);
		}
	}

	@Test
	public void testAddLinkInvalidSameLinkTwiceHashSet() {
		// create 1 Address and 1 Person
		Address adr = new Address();
		TypePropertyCollection proptype = (TypePropertyCollection) adr.getProperty("inhabitants").getType();
		Assert.assertSame(LinkedHashSet.class, proptype.getCollectionClass());
		Person martin = new Person("\"Martin\" \"Bl�mel\" \"19641014\"");
		Assert.assertNull(adr.getInhabitants());
		Assert.assertNull(martin.getAddress());

		// add the same Person to the Address as inhabitant twice
		adr.addInhabitant(martin);
		try {
			adr.addInhabitant(martin);
			Assert.fail("expected exception");
		} catch (ValidationInstanceAssocTwiceException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * Test creating bag associations using an array list a collection
	 * implementation. At least for two way navigable (binary) associations we
	 * have to postulate, that adding the same reference twice to a collection
	 * implies a multiplicity > 1 and bags on both association ends.
	 */
	@Test
	public void testAddLinkOkMultNtoNArrayListSameLinkTwice() {
		// create 2 ClosingPeriods and 2 Locations
		TypePropertyCollection proptypeCpLocations = (TypePropertyCollection) TypeRapidBean.forName(
				ClosingPeriod.class.getName()).getPropertyType("locations");
		TypePropertyCollection proptypeLocClosedons = (TypePropertyCollection) TypeRapidBean.forName(
				Location.class.getName()).getPropertyType("closedons");
		Class<?> colClassCpLocationsBefore = proptypeCpLocations.getCollectionClass();
		Class<?> colClassLocClosedonsBefore = proptypeLocClosedons.getCollectionClass();
		proptypeCpLocations.setCollectionClass(ArrayList.class);
		proptypeLocClosedons.setCollectionClass(ArrayList.class);
		ClosingPeriod cp1 = new ClosingPeriod(new String[] { "20051225", "XMas Holidays", "20060101" });
		Location loc1 = new Location(new String[] { "Location A" });
		try {
			cp1.addLocation(loc1);
			cp1.addLocation(loc1);
			Assert.assertEquals(2, cp1.getLocations().size());
			ClosingPeriod cp11Inverse = loc1.getClosedons().iterator().next();
			Assert.assertSame(cp1, cp11Inverse);
			Assert.assertEquals(2, loc1.getClosedons().size());
			cp1.removeLocation(loc1);
			Assert.assertEquals(1, cp1.getLocations().size());
			cp1.removeLocation(loc1);
			Assert.assertEquals(0, cp1.getLocations().size());
		} finally {
			proptypeCpLocations.setCollectionClass(colClassCpLocationsBefore);
			proptypeLocClosedons.setCollectionClass(colClassLocClosedonsBefore);
		}
	}

	/**
	 * test creating association instances by adding two links successively.
	 * 
	 * 1 to n Association: Address - Person Collection Properties: -
	 * Address.inhabitants [*] - Person.address [1]
	 */
	@Test
	public void testAddLinkOkMult1toN() {

		// create 1 Address and 2 Persons
		Address adr = new Address();
		Person martin = new Person("\"Martin\" \"Bl�mel\" \"19641014\"");
		Person jojo = new Person("\"Johannes\" \"Bl�mel\" \"19641014\"");
		Assert.assertNull(adr.getInhabitants());
		Assert.assertNull(martin.getAddress());
		Assert.assertNull(jojo.getAddress());

		// add the 2 Persons to the Address as inhabitants
		adr.addInhabitant(martin);
		adr.addInhabitant(jojo);

		// assert the Persons being linked to the Address
		Assert.assertEquals(2, adr.getInhabitants().size());
		Iterator<?> iter = adr.getInhabitants().iterator();
		Assert.assertSame(martin, iter.next());
		Assert.assertSame(jojo, iter.next());
		// assert the Address being linked to both Persons implicitely
		Assert.assertSame(adr, martin.getAddress());
		Assert.assertSame(adr, jojo.getAddress());
	}

	/**
	 * test adding too many instances.
	 * 
	 * 1 to n Association: Address - Person Collection Properties: -
	 * Address.inhabitants [5] - Person.address [1]
	 */
	@Test
	public void testAddLinkInvalidMultExceeded() {

		// create 1 Address and some Persons
		Address adr = new Address();
		Person jojo1 = new Person("\"Johannes\" \"Bl�mel\" \"19641014\"");
		Person jojo2 = new Person("\"Johannes\" \"Bl�mel\" \"19641015\"");
		Person jojo3 = new Person("\"Johannes\" \"Bl�mel\" \"19641016\"");
		Person jojo4 = new Person("\"Johannes\" \"Bl�mel\" \"19641017\"");
		Person jojo5 = new Person("\"Johannes\" \"Bl�mel\" \"19641018\"");
		Person jojo6 = new Person("\"Johannes\" \"Bl�mel\" \"19641019\"");

		// add more persons than allowed
		adr.addInhabitant(jojo1);
		adr.addInhabitant(jojo2);
		adr.addInhabitant(jojo3);
		adr.addInhabitant(jojo4);
		adr.addInhabitant(jojo5);
		try {
			adr.addInhabitant(jojo6);
			Assert.fail();
		} catch (ValidationException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * test 1 to 1 Association: User - Person simply add one link to a
	 * collection property that was null before.
	 * 
	 * Collection Properties: - TestUser.person [0..1] - Person.user [0..1]
	 */
	@Test
	public void testAddLinkOkMult1to1() {

		// set up
		Person martin = new Person("\"Bl�mel\" \"Martin\" \"19641014\"");
		TestUser umartin = new TestUser("martin");
		Assert.assertNull(martin.getUser());
		Assert.assertNull(umartin.getPerson());

		// test
		((PropertyCollection) umartin.getProperty("person")).addLink(martin);
		Assert.assertSame(martin, umartin.getPerson());
		Assert.assertSame(umartin, martin.getUser());
	}

	/**
	 * test 1 to 1 Association: User - Person Add a second link to a collection
	 * property with maxmult = 1. This should throw a validation exception
	 * because the maximal multiplicity would be exceeded.
	 * 
	 * 1 to 1 Association: User Person
	 * 
	 * Collection Properties: - TestUser.person [0..1] - Person.user [0..1]
	 */
	@Test
	public void testAddLinkOkMult1to1Second() {

		// set up
		Person martin = new Person("\"Bl�mel\" \"Martin\" \"19641014\"");
		Person jojo = new Person("\"Bl�mel\" \"Johannes\" \"19641014\"");
		TestUser umartin = new TestUser("martin");
		Assert.assertNull(martin.getUser());
		Assert.assertNull(umartin.getPerson());
		((PropertyCollection) umartin.getProperty("person")).addLink(jojo);
		Assert.assertSame(jojo, umartin.getPerson());
		Assert.assertSame(umartin, jojo.getUser());

		// test
		try {
			((PropertyCollection) umartin.getProperty("person")).addLink(martin);
			Assert.fail("expected a ValidationException");
		} catch (ValidationException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testSetValueInvalidNullOnMandatory() {
		Person p = new Person();
		try {
			p.setAddress(null);
			Assert.fail();
		} catch (ValidationException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testAddLinkInvalidTargetType() {
		Address adr = new Address();
		try {
			((PropertyCollection) adr.getProperty("inhabitants")).addLink(new Location());
			Assert.fail("expected \"ValidationException\"");
		} catch (ValidationException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * test creating association instances by adding multiple links
	 * successively.
	 * 
	 * n to n Association: ClosingPeriod - Location
	 */
	@Test
	public void testAddLinkOkMultNtoN() {

		// create 2 ClosingPeriods and 2 Locations
		String[] sa1 = { "20051225", "XMas Holidays", "20060101" };
		ClosingPeriod cp1 = new ClosingPeriod(sa1);
		String[] sa2 = { "20060704", "IndependenceDay", "20060704" };
		ClosingPeriod cp2 = new ClosingPeriod(sa2);
		Location loc1 = new Location("\"Location A\"");
		Assert.assertEquals("Location A", loc1.getIdString());
		Location loc2 = new Location("\"Location B\"");
		cp1.addLocation(loc1);
		cp1.addLocation(loc2);
		Assert.assertEquals(2, cp1.getLocations().size());
		Assert.assertEquals(1, loc1.getClosedons().size());
		ClosingPeriod cp11Inverse = loc1.getClosedons().iterator().next();
		Assert.assertSame(cp1, cp11Inverse);
		Assert.assertEquals(1, loc1.getClosedons().size());
		ClosingPeriod cp12Inverse = loc2.getClosedons().iterator().next();
		Assert.assertSame(cp1, cp12Inverse);
		Assert.assertEquals(1, loc2.getClosedons().size());
		cp2.addLocation(loc1);
		cp2.addLocation(loc2);
		Assert.assertEquals(2, cp1.getLocations().size());
		Assert.assertEquals(2, cp2.getLocations().size());
		Assert.assertEquals(2, loc1.getClosedons().size());
		Assert.assertEquals(2, loc2.getClosedons().size());
		Iterator<ClosingPeriod> it = loc1.getClosedons().iterator();
		Assert.assertSame(cp1, it.next());
		Assert.assertSame(cp2, it.next());
		it = loc2.getClosedons().iterator();
		Assert.assertSame(cp1, it.next());
		Assert.assertSame(cp2, it.next());

		cp2.removeLocation(loc1);
		Assert.assertEquals(2, cp1.getLocations().size());
		Assert.assertEquals(1, cp2.getLocations().size());
		Assert.assertEquals(1, loc1.getClosedons().size());
		Assert.assertEquals(2, loc2.getClosedons().size());
	}

	/**
	 * test creating association instances by setting two links all at once.
	 * 
	 * 1 to n Association: Address - Person Collection Properties: -
	 * Address.inhabitants [*] - Person.address [1]
	 */
	@Test
	public void testSetValueOkMult1toN() {

		// create 1 Address and 2 Persons
		Address adr = new Address();
		Person martin = new Person("\"Martin\" \"Bl�mel\" \"19641014\"");
		Person jojo = new Person("\"Johannes\" \"Bl�mel\" \"19641014\"");
		Assert.assertNull(adr.getInhabitants());
		Assert.assertNull(martin.getAddress());
		Assert.assertNull(jojo.getAddress());

		// create a new collection and use the collection
		// property's setter
		Collection<Person> persons = new ArrayList<Person>();
		persons.add(martin);
		persons.add(jojo);
		adr.setInhabitants(persons);

		// assert the Persons being linked to the Address
		Assert.assertEquals(2, adr.getInhabitants().size());
		Iterator<?> iter = adr.getInhabitants().iterator();
		Assert.assertSame(martin, iter.next());
		Assert.assertSame(jojo, iter.next());
		// assert the Address being linked to both Persons implicitely
		Assert.assertSame(adr, martin.getAddress());
		Assert.assertSame(adr, jojo.getAddress());
	}

	/**
	 * test deleting association instances by removing two links successively.
	 * 
	 * 1 to n Association: Address - Person Collection Properties: -
	 * Address.inhabitants [*] - Person.address [1]
	 */
	@Test
	public void testRemoveLinkWithInverse() {
		Address adr = new Address();
		adr.getType().setIdGenerator(new IdGeneratorNumeric());
		Assert.assertEquals(1, ((IdNumeric) adr.getId()).getNumber());
		Person martin = new Person("\"Martin\" \"Bl�mel\" \"19641014\"");
		Person jojo = new Person("\"Johannes\" \"Bl�mel\" \"19641014\"");
		adr.addInhabitant(martin);
		adr.addInhabitant(jojo);

		adr.removeInhabitant(jojo);

		Assert.assertEquals(1, adr.getInhabitants().size());
		Iterator<?> iter = adr.getInhabitants().iterator();
		Assert.assertSame(martin, iter.next());
		Assert.assertSame(adr, martin.getAddress());
		Assert.assertNull(jojo.getAddress());

		// removing last link via remove link produces
		// an empty collection but no null value
		adr.removeInhabitant(martin);

		Assert.assertEquals(0, adr.getInhabitants().size());
		Assert.assertNull(jojo.getAddress());
		Assert.assertNull(martin.getAddress());
	}

	/**
	 * test deleting association instances by setting new values for the
	 * collection property.
	 * 
	 * 1 to n Association: Address - Person Collection Properties: -
	 * Address.inhabitants [*] - Person.address [1]
	 */
	@Test
	public void testRemoveViaSetLinkWithInverse() {
		Address adr = new Address();
		adr.getType().setIdGenerator(new IdGeneratorNumeric());
		Assert.assertEquals(1, ((IdNumeric) adr.getId()).getNumber());
		Person martin = new Person("\"Martin\" \"Bl�mel\" \"19641014\"");
		Person jojo = new Person("\"Johannes\" \"Bl�mel\" \"19641014\"");
		adr.addInhabitant(martin);
		adr.addInhabitant(jojo);

		Collection<Person> persons = new ArrayList<Person>();
		persons.add(martin);
		adr.setInhabitants(persons);

		Assert.assertEquals(1, adr.getInhabitants().size());
		Iterator<?> iter = adr.getInhabitants().iterator();
		Assert.assertSame(martin, iter.next());
		Assert.assertSame(adr, martin.getAddress());
		Assert.assertNull(jojo.getAddress());

		adr.setInhabitants(new ArrayList<Person>());

		Assert.assertEquals(0, adr.getInhabitants().size());
		Assert.assertNull(martin.getAddress());
		Assert.assertNull(jojo.getAddress());

		adr.setInhabitants(null);

		Assert.assertNull(adr.getInhabitants());
		Assert.assertNull(martin.getAddress());
		Assert.assertNull(jojo.getAddress());
	}

	/**
	 * test breaking a one to one link.
	 * 
	 * 1 to 1 Association: User Person
	 * 
	 * Collection Properties: - TestUser.person [0..1] - Person.user [0..1]
	 */
	@Test
	public void testRemoveLinkMult1to1() {
		// create 1 TestUser and 2 Person
		Person martin = new Person("\"Bl�mel\" \"Martin\" \"19641014\"");
		TestUser umartin = new TestUser("martin");

		umartin.setPerson(martin);
		Assert.assertSame(martin, umartin.getPerson());
		Assert.assertSame(umartin, martin.getUser());
		umartin.setPerson(null);
		Assert.assertNull(martin.getUser());
		Assert.assertNull(umartin.getPerson());

		martin.setUser(umartin);
		Assert.assertSame(martin, umartin.getPerson());
		Assert.assertSame(umartin, martin.getUser());
		martin.setUser(null);
		Assert.assertNull(martin.getUser());
		Assert.assertNull(umartin.getPerson());
	}

	/**
	 * test add a component bean to a composite bean.
	 */
	@Test
	public void testAddLinkOkComponent() {
		Submenu root = new Submenu("root");
		Assert.assertNull(root.getParentBean());
		Assert.assertEquals("root", root.getName());
		Assert.assertNull(root.getMenuentrys());

		MenuItem item1 = new MenuItem("item1");
		Assert.assertNull(item1.getParentBean());
		root.addMenuentry(item1);
		Assert.assertEquals(1, root.getMenuentrys().size());
		Assert.assertSame(item1, root.getMenuentrys().iterator().next());
		Assert.assertSame(root, item1.getParentBean());

		Submenu submenu1 = new Submenu("submenu1");
		Assert.assertNull(submenu1.getParentBean());
		root.addMenuentry(submenu1);

		Iterator<?> iter = root.getMenuentrys().iterator();
		Assert.assertEquals(2, root.getMenuentrys().size());
		Assert.assertSame(item1, iter.next());
		Assert.assertSame(submenu1, iter.next());
		Assert.assertSame(root, item1.getParentBean());
		Assert.assertSame(root, submenu1.getParentBean());
	}

	/**
	 * test add a component bean to a composite bean.
	 */
	@Test
	public void testAddLinkOkComponentViaSet() {
		Submenu root = new Submenu("root");
		Assert.assertNull(root.getParentBean());
		Assert.assertEquals("root", root.getName());
		Assert.assertNull(root.getMenuentrys());

		MenuItem item1 = new MenuItem("item1");
		Assert.assertNull(item1.getParentBean());
		Collection<MenuEntry> col = new ArrayList<MenuEntry>();
		col.add(item1);
		root.setMenuentrys(col);

		Assert.assertEquals(1, root.getMenuentrys().size());
		Assert.assertSame(item1, root.getMenuentrys().iterator().next());
		Assert.assertSame(root, item1.getParentBean());

		Submenu submenu1 = new Submenu("submenu1");
		col = new ArrayList<MenuEntry>();
		col.add(item1);
		col.add(submenu1);
		Assert.assertNull(submenu1.getParentBean());
		root.setMenuentrys(col);

		Iterator<?> iter = root.getMenuentrys().iterator();
		Assert.assertEquals(2, root.getMenuentrys().size());
		Assert.assertSame(item1, iter.next());
		Assert.assertSame(submenu1, iter.next());
		Assert.assertSame(root, item1.getParentBean());
		Assert.assertSame(root, submenu1.getParentBean());
	}

	/**
	 * test remove a component bean from a composite.
	 */
	@Test
	public void testRemoveLinkComponent() {
		Submenu root = new Submenu("root");
		MenuItem item1 = new MenuItem("item1");
		root.addMenuentry(item1);
		Submenu submenu1 = new Submenu("submenu1");
		root.addMenuentry(submenu1);

		root.removeMenuentry(submenu1);
		Assert.assertEquals(1, root.getMenuentrys().size());
		Assert.assertSame(item1, root.getMenuentrys().iterator().next());
		Assert.assertNull(submenu1.getParentBean());
		root.removeMenuentry(item1);
		Assert.assertEquals(0, root.getMenuentrys().size());
		Assert.assertNull(item1.getParentBean());
	}

	/**
	 * test remove a component bean from a composite.
	 */
	@Test
	public void testRemoveLinkComponentViaSet() {
		Submenu root = new Submenu("root");
		MenuItem item1 = new MenuItem("item1");
		root.addMenuentry(item1);
		Submenu submenu1 = new Submenu("submenu1");
		root.addMenuentry(submenu1);

		Collection<MenuEntry> col = new ArrayList<MenuEntry>();
		col.add(item1);
		root.setMenuentrys(col);
		Assert.assertEquals(1, root.getMenuentrys().size());
		Assert.assertSame(item1, root.getMenuentrys().iterator().next());
		Assert.assertNull(submenu1.getParentBean());

		root.setMenuentrys(new ArrayList<MenuEntry>());
		Assert.assertEquals(0, root.getMenuentrys().size());
		Assert.assertNull(item1.getParentBean());
	}

	/**
	 * test remove a component bean from a composite. Because the component is
	 * part of a document it is automatically deleted there
	 */
	@Test
	public void testRemoveLinkComponentDeleteOrphanedFromDoc() {
		AddressBook adrbook = new AddressBook();
		Document doc = new Document(adrbook);
		Assert.assertSame(doc.findBeansByType("org.rapidbeans.test.codegen.AddressBook").get(0), doc.getRoot());
		Person martin = new Person(new String[] { "Bl�mel", "Martin", "19641014" });
		adrbook.addPerson(martin);
		Assert.assertSame(martin, ((ReadonlyListCollection<?>) adrbook.getPersons()).get(0));
		Assert.assertSame(martin, doc.findBeansByType("org.rapidbeans.test.codegen.Person").get(0));
		Address fasanstreet = new Address();
		fasanstreet.setStreet("Fasanenstra�e");
		adrbook.addAddress(fasanstreet);
		Assert.assertNull(fasanstreet.getInhabitants());
		fasanstreet.addInhabitant(martin);
		Assert.assertEquals(1, fasanstreet.getInhabitants().size());
		Assert.assertSame(fasanstreet, martin.getAddress());
		adrbook.removeAddress(fasanstreet);
		Assert.assertEquals(0, fasanstreet.getInhabitants().size());
	}

	/**
	 * Test method for toString() of a single bean.
	 */
	@Test
	public void testToStringSingleBean() {
		RapidBean bean = this.createTestBean();
		PropertyCollection prop = this.createCollectionProperty("<property name=\"test\"" + " targettype=\"TestBean\""
				+ " />");
		prop.setValue(bean);
		Assert.assertEquals("Bl�mel_Martin_19641014", prop.toString());
	}

	/**
	 * Test method for toString() of a collection with three beans.
	 */
	@Test
	public void testToStringMultipleBeans() {
		RapidBean bean1 = this.createTestBean("Bl�mel", "Martin", "19641014");
		RapidBean bean2 = this.createTestBean("Bl�mel", "Ulrike", "19620802");
		RapidBean bean3 = this.createTestBean("Keinki", "Katharina", "19901119");
		PropertyCollection prop = this.createCollectionProperty("<property name=\"test\"" + " targettype=\"TestBean\""
				+ "/>", ",", "\\");
		Collection<RapidBean> col = new ArrayList<RapidBean>();
		col.add(bean1);
		col.add(bean2);
		col.add(bean3);
		prop.setValue(col);
		Assert.assertEquals("Bl�mel_Martin_19641014,Bl�mel_Ulrike_19620802,Keinki_Katharina_19901119", prop.toString());
	}

	/**
	 * Happy day test for validation.
	 */
	@Test
	public void testValidateOk() {
		RapidBean bean = createTestBean("Bl�mel", "Martin", "19641014");
		PropertyCollection prop = this.createCollectionProperty("<property name=\"test\"" + " targettype=\"TestBean\""
				+ " />");
		prop.validate(bean);
	}

	/**
	 * Test for validation if the bean has not the specified target type.
	 */
	@Test
	public void testValidateInvalidWrongType() {
		RapidBean bean = createTestBean("Bl�mel", "Martin", "19641014");
		PropertyCollection prop = this.createCollectionProperty("<property name=\"test\""
				+ " targettype=\"org.rapidbeans.test.TestBean\"" + " />");
		try {
			prop.validate(bean);
			Assert.fail("expected ValidationException");
		} catch (ValidationException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * test immutability. prove that our PropertyCollection is immutable after
	 * getValue
	 * 
	 * @throws java.text.ParseException
	 *             if parsing Assert.fails
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetValueImmutability() throws java.text.ParseException {

		PropertyCollection prop = this.createCollectionProperty("<property name=\"test\""
				+ " targettype=\"org.rapidbeans.test.TestBean\"" + " />", ",", "\\");

		// originally prop's value is null (undefined)
		Assert.assertNull(prop.getValue());

		TestBean bean1 = new TestBean("\"Bluemel\" \"Martin\" \"19641014\"");
		TestBean bean2 = new TestBean("\"Bluemel\" \"Johannes\" \"19641014\"");
		Collection<Link> col = new ArrayList<Link>();
		col.add(bean1);
		col.add(bean2);
		prop.setValue(col);

		// then I try to remove an object of the collection via the iterator
		// returned by the getter
		Collection<Link> col1 = (Collection<Link>) prop.getValue();
		try {
			col1.clear();
		} catch (ImmutableCollectionException e) {
			Assert.assertTrue(true);
		}

		// of course our prop stays the same
		Assert.assertEquals(2, ((Collection<Link>) prop.getValue()).size());
	}

	/**
	 * test a tree set.
	 */
	@Test
	public void testAddLinkOkMultNtoNArrayList() {
		// configure collection properties of Location and ClosingPeriod
		// to use TreeSet as collection implementing class
		((TypePropertyCollection) (new Location()).getProperty("closedons").getType())
				.setCollectionClass(ArrayList.class);
		((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations").getType())
				.setCollectionClass(ArrayList.class);
		tstNMAssociationInverse();
	}

	/**
	 * test a tree set.
	 */
	@Test
	public void testAddLinkOkMultNtoNLinkedHashSet() {
		// configure collection properties of Location and ClosingPeriod
		// to use TreeSet as collection implementing class
		((TypePropertyCollection) (new Location()).getProperty("closedons").getType())
				.setCollectionClass(LinkedHashSet.class);
		((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations").getType())
				.setCollectionClass(LinkedHashSet.class);
		tstNMAssociationInverse();
	}

	/**
	 * test a tree set.
	 */
	@Test
	public void testAddLinkOkMultNtoNTreeSet() {
		// configure collection properties of Location and ClosingPeriod
		// to use TreeSet as collection implementing class
		((TypePropertyCollection) (new Location()).getProperty("closedons").getType())
				.setCollectionClass(TreeSet.class);
		((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations").getType())
				.setCollectionClass(TreeSet.class);
		tstNMAssociationInverse();
	}

	/**
	 * test collection properties used in conjunction with an N:M association.
	 */
	private void tstNMAssociationInverse() {
		// configure collection properties of Location and ClosingPeriod
		// to use TreeSet as collection implementing class
		((TypePropertyCollection) (new Location()).getProperty("closedons").getType())
				.setCollectionClass(TreeSet.class);
		((TypePropertyCollection) (new ClosingPeriod()).getProperty("locations").getType())
				.setCollectionClass(TreeSet.class);
		// set up two locations and closing periods
		ClosingPeriod cp1 = new ClosingPeriod("\"20050101\" \"1\"");
		ClosingPeriod cp2 = new ClosingPeriod("\"20050201\" \"2\"");
		Location locA = new Location("\"A\"");
		Location locB = new Location("\"B\"");

		// at the beginning all four collection properties are
		// undefined (null)
		Assert.assertNull(locA.getClosedons());
		Assert.assertNull(locB.getClosedons());
		Assert.assertNull(cp1.getLocations());
		Assert.assertNull(cp2.getLocations());

		// link locA with cp1
		locA.addClosedon(cp1);
		Assert.assertEquals(1, locA.getClosedons().size());
		ReadonlyListCollection<ClosingPeriod> cps = (ReadonlyListCollection<ClosingPeriod>) locA.getClosedons();
		Assert.assertSame(cp1, cps.get(0));
		Assert.assertNull(locB.getClosedons());
		ReadonlyListCollection<Location> locs = (ReadonlyListCollection<Location>) cp1.getLocations();
		Assert.assertSame(locA, locs.get(0));
		Assert.assertEquals(1, cp1.getLocations().size());
		Assert.assertNull(cp2.getLocations());

		// link locA with cp2
		locA.addClosedon(cp2);
		Assert.assertEquals(2, locA.getClosedons().size());
		cps = (ReadonlyListCollection<ClosingPeriod>) locA.getClosedons();
		Assert.assertSame(cp1, cps.get(0));
		Assert.assertSame(cp2, cps.get(1));
		Assert.assertNull(locB.getClosedons());
		Assert.assertEquals(1, cp1.getLocations().size());
		locs = (ReadonlyListCollection<Location>) cp1.getLocations();
		Assert.assertSame(locA, locs.get(0));
		Assert.assertEquals(1, cp2.getLocations().size());
		locs = (ReadonlyListCollection<Location>) cp2.getLocations();
		Assert.assertSame(locA, locs.get(0));

		// link locB with cp1
		locB.addClosedon(cp1);
		Assert.assertEquals(2, locA.getClosedons().size());
		cps = (ReadonlyListCollection<ClosingPeriod>) locA.getClosedons();
		Assert.assertSame(cp1, cps.get(0));
		Assert.assertSame(cp2, cps.get(1));
		Assert.assertEquals(1, locB.getClosedons().size());
		cps = (ReadonlyListCollection<ClosingPeriod>) locB.getClosedons();
		Assert.assertSame(cp1, cps.get(0));
		Assert.assertEquals(2, cp1.getLocations().size());
		locs = (ReadonlyListCollection<Location>) cp1.getLocations();
		Assert.assertSame(locA, locs.get(0));
		Assert.assertSame(locB, locs.get(1));
		Assert.assertEquals(1, cp2.getLocations().size());
		locs = (ReadonlyListCollection<Location>) cp2.getLocations();
		Assert.assertSame(locA, locs.get(0));

		// link locB with cp2
		locB.addClosedon(cp2);
		Assert.assertEquals(2, locA.getClosedons().size());
		cps = (ReadonlyListCollection<ClosingPeriod>) locA.getClosedons();
		Assert.assertSame(cp1, cps.get(0));
		Assert.assertSame(cp2, cps.get(1));
		Assert.assertEquals(2, locB.getClosedons().size());
		cps = (ReadonlyListCollection<ClosingPeriod>) locB.getClosedons();
		Assert.assertSame(cp1, cps.get(0));
		Assert.assertSame(cp2, cps.get(1));
		Assert.assertEquals(2, cp1.getLocations().size());
		locs = (ReadonlyListCollection<Location>) cp1.getLocations();
		Assert.assertSame(locA, locs.get(0));
		Assert.assertSame(locB, locs.get(1));
		Assert.assertEquals(2, cp2.getLocations().size());
		locs = (ReadonlyListCollection<Location>) cp2.getLocations();
		Assert.assertSame(locA, locs.get(0));
		Assert.assertSame(locB, locs.get(1));

		Assert.assertEquals(2, cp1.getLocations().size());
		try {
			cp1.addLocation(locA);
			Assert.fail("expected ValidationInstanceAssocTwiceException");
		} catch (ValidationInstanceAssocTwiceException e) {
			Assert.assertTrue(true);
		}
		Assert.assertEquals(2, cp1.getLocations().size());
		try {
			cp1.addLocation(locB);
			Assert.fail("expected ValidationInstanceAssocTwiceException");
		} catch (ValidationInstanceAssocTwiceException e) {
			Assert.assertTrue(true);
		}
		Assert.assertEquals(2, cp1.getLocations().size());
		Assert.assertEquals(2, locB.getClosedons().size());
		try {
			locB.addClosedon(cp1);
			Assert.fail("expected ValidationInstanceAssocTwiceException");
		} catch (ValidationInstanceAssocTwiceException e) {
			Assert.assertTrue(true);
		}
		Assert.assertEquals(2, locB.getClosedons().size());
		try {
			locB.addClosedon(cp2);
			Assert.fail("expected ValidationInstanceAssocTwiceException");
		} catch (ValidationInstanceAssocTwiceException e) {
			Assert.assertTrue(true);
		}
		Assert.assertEquals(2, locB.getClosedons().size());
	}

	@Test
	public void testDeletePersonFromGroup() {
		org.rapidbeans.test.addressbook.Addressbook adrbook = new Addressbook();
		// we need the context of a document in order have
		// the group associations cleaned up properly
		Document doc = new Document(adrbook);
		org.rapidbeans.test.addressbook.Person fru = new org.rapidbeans.test.addressbook.Person();
		fru.setLastname("Fru");
		org.rapidbeans.test.addressbook.Person blu = new org.rapidbeans.test.addressbook.Person();
		blu.setLastname("Bluemel");
		adrbook.addPerson(fru);
		adrbook.addPerson(blu);
		org.rapidbeans.test.addressbook.Group group = new Group();
		group.setName("Sports");
		adrbook.addGroup(group);
		group.addPerson(fru);
		group.addPerson(blu);
		Assert.assertEquals(2, group.getPersons().size());
		Assert.assertEquals(fru, group.getPersons().get(0));
		Assert.assertEquals(blu, group.getPersons().get(1));
		Assert.assertEquals(1, fru.getGroups().size());
		Assert.assertEquals(group, fru.getGroups().get(0));
		Assert.assertEquals(1, blu.getGroups().size());
		Assert.assertEquals(group, blu.getGroups().get(0));
		Assert.assertEquals(2, doc.findBeansByType("org.rapidbeans.test.addressbook.Person").size());

		adrbook.removePerson(blu);

		Assert.assertEquals(1, group.getPersons().size());
		Assert.assertEquals(fru, group.getPersons().get(0));
		Assert.assertEquals(1, doc.findBeansByType("org.rapidbeans.test.addressbook.Person").size());
		Assert.assertEquals(1, fru.getGroups().size());
		Assert.assertEquals(group, fru.getGroups().get(0));
		Assert.assertEquals(0, blu.getGroups().size());
		Assert.assertEquals(1, doc.findBeansByType("org.rapidbeans.test.addressbook.Person").size());
	}

	@Test
	public void testDeletePersonFromDocument() {
		org.rapidbeans.test.addressbook.Addressbook adrbook = new Addressbook();
		Document doc = new Document(adrbook);
		org.rapidbeans.test.addressbook.Person fru = new org.rapidbeans.test.addressbook.Person();
		fru.setLastname("Fru");
		org.rapidbeans.test.addressbook.Person blu = new org.rapidbeans.test.addressbook.Person();
		blu.setLastname("Bluemel");
		adrbook.addPerson(fru);
		adrbook.addPerson(blu);
		org.rapidbeans.test.addressbook.Group group = new Group();
		group.setName("Sports");
		adrbook.addGroup(group);
		group.addPerson(fru);
		group.addPerson(blu);
		Assert.assertEquals(2, group.getPersons().size());
		Assert.assertEquals(fru, group.getPersons().get(0));
		Assert.assertEquals(blu, group.getPersons().get(1));
		Assert.assertEquals(1, fru.getGroups().size());
		Assert.assertEquals(group, fru.getGroups().get(0));
		Assert.assertEquals(1, blu.getGroups().size());
		Assert.assertEquals(group, blu.getGroups().get(0));
		Assert.assertEquals(2, doc.findBeansByType("org.rapidbeans.test.addressbook.Person").size());

		blu.delete(); // = doc.delete(blu)

		Assert.assertEquals(1, adrbook.getPersons().size());
		Assert.assertTrue(adrbook.getPersons().contains(fru));
		Assert.assertFalse(adrbook.getPersons().contains(blu));
		Assert.assertEquals(1, group.getPersons().size());
		Assert.assertTrue(group.getPersons().contains(fru));
		Assert.assertFalse(group.getPersons().contains(blu));
		Assert.assertEquals(1, doc.findBeansByType("org.rapidbeans.test.addressbook.Person").size());
		Assert.assertEquals(1, doc.findBeansByType("org.rapidbeans.test.addressbook.Person").size());

		adrbook = new Addressbook();
		doc = new Document(adrbook);
		fru = new org.rapidbeans.test.addressbook.Person();
		fru.setLastname("Fru");
		blu = new org.rapidbeans.test.addressbook.Person();
		blu.setLastname("Bluemel");
		adrbook.addPerson(fru);
		adrbook.addPerson(blu);
		group = new Group();
		group.setName("Sports");
		adrbook.addGroup(group);
		group.addPerson(fru);
		group.addPerson(blu);
		Assert.assertEquals(2, group.getPersons().size());
		Assert.assertEquals(fru, group.getPersons().get(0));
		Assert.assertEquals(blu, group.getPersons().get(1));
		Assert.assertEquals(1, fru.getGroups().size());
		Assert.assertEquals(group, fru.getGroups().get(0));
		Assert.assertEquals(1, blu.getGroups().size());
		Assert.assertEquals(group, blu.getGroups().get(0));
		Assert.assertEquals(2, doc.findBeansByType("org.rapidbeans.test.addressbook.Person").size());

		fru.delete(); // = doc.delete(blu)

		Assert.assertEquals(1, adrbook.getPersons().size());
		Assert.assertTrue(adrbook.getPersons().contains(blu));
		Assert.assertFalse(adrbook.getPersons().contains(fru));
		Assert.assertEquals(1, group.getPersons().size());
		Assert.assertTrue(group.getPersons().contains(blu));
		Assert.assertFalse(group.getPersons().contains(fru));
		Assert.assertEquals(1, doc.findBeansByType("org.rapidbeans.test.addressbook.Person").size());
		Assert.assertEquals(1, doc.findBeansByType("org.rapidbeans.test.addressbook.Person").size());
	}

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
	private GenericBean createTestBean(final String name, final String prename, final String dateofbirth) {
		String descr = "<beantype name=\"TestBean\" idtype=\"keyprops\">" + "<property name=\"name\" key=\"true\"/>"
				+ "<property name=\"prename\" key=\"true\"/>"
				+ "<property name=\"dateofbirth\" type=\"date\" key=\"true\"/>" + "</beantype>";
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
	 * @return a new Collection property.
	 */
	private PropertyCollection createCollectionProperty(final String descr) {
		return createCollectionProperty(descr, null, null);
	}

	/**
	 * set up a Collection Property.
	 * 
	 * @param descr
	 *            the XML property type description
	 * @param sep
	 *            the separator char
	 * @param esc
	 *            the escaping char
	 * @return a new Collection property.
	 */
	private PropertyCollection createCollectionProperty(final String descr, final String sep, final String esc) {
		XmlNode propertyNode = XmlNode.getDocumentTopLevel(new ByteArrayInputStream(descr.getBytes()));
		TypePropertyCollection type = new TypePropertyCollection(new XmlNode[] { propertyNode }, null, "Collection",
				sep, esc);
		return new PropertyCollection(type, null);
	}
}
