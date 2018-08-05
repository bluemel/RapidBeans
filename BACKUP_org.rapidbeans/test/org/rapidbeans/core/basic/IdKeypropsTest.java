package org.rapidbeans.core.basic;

import java.util.TreeSet;

import junit.framework.TestCase;

import org.rapidbeans.core.common.ReadonlyListCollection;
import org.rapidbeans.core.type.RapidBeansTypeLoader;
import org.rapidbeans.core.type.TypePropertyCollection;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.test.TestBean;
import org.rapidbeans.test.TestHelper;
import org.rapidbeans.test.codegen.AddressBook;
import org.rapidbeans.test.codegen.Person;

/**
 * UnitTests f�r IdKeyprops.
 * 
 * @author Martin Bluemel
 */
public class IdKeypropsTest extends TestCase {

	/**
	 * Test method for toString().
	 */
	public void testToString() {
		GenericBean bean = createTestBean("Bl�mel", "Martin", "19641014");
		IdKeyprops id = new IdKeyprops(bean, null);
		assertEquals("Bl�mel_Martin_19641014", id.toString());
	}

	/**
	 * Test method for equals().
	 */
	public void testEqualsEqual() {
		GenericBean bean1 = createTestBean("Bl�mel", "Martin", "19641014");
		IdKeyprops id1 = new IdKeyprops(bean1, null);
		GenericBean bean2 = createTestBean("Bl�mel", "Martin", "19641014");
		IdKeyprops id2 = new IdKeyprops(bean2, null);
		assertEquals(id1, id2);
	}

	/**
	 * Test method for equals().
	 */
	public void testEqualsDifferent() {
		GenericBean bean1 = createTestBean("Bl�mel", "Martin", "19641014");
		IdKeyprops id1 = new IdKeyprops(bean1, null);
		GenericBean bean2 = createTestBean("Bl�mel", "Johannes", "19641014");
		IdKeyprops id2 = new IdKeyprops(bean2, null);
		assertFalse(id1.equals(id2));
	}

	/**
	 * Test method for equals().
	 */
	public void testEqualsWrongType() {
		TestBean bean = new TestBean("\"Bluemel\" \"Martin\" \"19641014\"");
		bean.getType().setIdGenerator(new IdGeneratorUuid());
		IdKeyprops id1 = new IdKeyprops(bean, null);
		IdUuid id2 = new IdUuid(bean, null);
		assertFalse(id1.equals(id2));
	}

	public void testSortingSimple() {
		try {
			AddressBook book = new AddressBook();
			Person p1 = new Person("a");
			Person p2 = new Person("b");
			Person p3 = new Person("c");
			book.addPerson(p3);
			book.addPerson(p2);
			book.addPerson(p1);
			ReadonlyListCollection<Person> list = (ReadonlyListCollection<Person>) book.getPersons();
			assertSame(p3, list.get(0));
			assertSame(p2, list.get(1));
			assertSame(p1, list.get(2));
		} finally {
			RapidBeansTypeLoader.getInstance().unregisterType(new AddressBook().getType().getName());
			RapidBeansTypeLoader.getInstance().unregisterType(new Person().getType().getName());
		}
	}

	public void testSortingReal() {
		try {
			AddressBook book = new AddressBook();
			TypePropertyCollection colproptype = (TypePropertyCollection) book.getProperty("persons").getType();
			colproptype.setCollectionClass(TreeSet.class);
			Person p1 = new Person("a");
			Person p2 = new Person("b");
			Person p3 = new Person("c");
			book.addPerson(p3);
			book.addPerson(p1);
			book.addPerson(p2);
			ReadonlyListCollection<Person> list = (ReadonlyListCollection<Person>) book.getPersons();
			assertSame(p1, list.get(0));
			assertSame(p2, list.get(1));
			assertSame(p3, list.get(2));
		} finally {
			TypeRapidBean abType = new AddressBook().getType();
			TypeRapidBean pType = new Person().getType();
			if (RapidBeansTypeLoader.getInstance().lookupType(abType.getName()) != null) {
				RapidBeansTypeLoader.getInstance().unregisterType(abType.getName());
			}
			if (RapidBeansTypeLoader.getInstance().lookupType(abType.getName()) != null) {
				RapidBeansTypeLoader.getInstance().unregisterType(pType.getName());
			}
		}
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
		String descr = "<beantype name=\"TestBean\">" + "<property name=\"name\" key=\"true\"/>"
				+ "<property name=\"prename\" key=\"true\"/>"
				+ "<property name=\"dateofbirth\" type=\"date\" key=\"true\"/>" + "</beantype>";
		GenericBean bean = TestHelper.createGenericBeanInstance(descr);
		bean.setPropValue("name", name);
		bean.setPropValue("prename", prename);
		bean.setPropValue("dateofbirth", dateofbirth);
		return bean;
	}
}
