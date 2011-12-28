/*
 * Rapid Beans Framework: RapidBeanSerializerTest.java
 *
 * Copyright Martin Bluemel, 2008
 *
 * created 25.02.2008
 */
package org.rapidbeans.core.common;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.bind.JAXBException;

import junit.framework.TestCase;

import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.core.util.FileHelper;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.config.swing.ConfigApplicationSwing;
import org.rapidbeans.test.codegen.AddressBook;
import org.rapidbeans.test.codegen.Person;

/**
 * Unit test for class RapidBeanDeserializer.
 *
 * @author Martin Bluemel
 */
public class RapidBeanSerializerTest extends TestCase {

    public static boolean initialized = false;

    /**
     * Test the default serialization for multiline strings as attributes.
     */
    public void testSerializeStringMultilineAttribute() throws MalformedURLException {
        AddressBook book = new AddressBook();
        File testfile = new File("testdata/serialization/out1.xml");
        Person p1 = new Person(new String[]{"Bluemel", "Martin", "19641014"});
        p1.setComment("xxx\nyyy");
        book.addPerson(p1);
        Document doc = new Document(book);
        doc.setUrl(testfile.toURI().toURL());
        doc.save();
        File reference = new File("testdata/serialization/testSerMultiAttr.xml");
        assertTrue(reference.exists());
        assertTrue("File difference", FileHelper.filesEqual(testfile, reference, true, true));
        doc = new Document(testfile);
        p1 = (Person) doc.findBean("org.rapidbeans.test.codegen.Person", "Bluemel_Martin_19641014");
        assertEquals("xxx\nyyy", p1.getComment());
        assertTrue(testfile.delete());
    }

    /**
     * Test the default serialization for multiline strings as attributes.
     */
    public void testSerializeStringMultilineElement() throws MalformedURLException {
        AddressBook book = new AddressBook();
        File testfile = new File("testdata/serialization/out1.xml");
        Person p1 = new Person(new String[]{"Bluemel", "Martin", "19641014"});
        p1.setComment2("xxx\nyyy");
        book.addPerson(p1);
        Document doc = new Document(book);
        doc.setUrl(testfile.toURI().toURL());
        doc.save();
        File reference = new File("testdata/serialization/testSerMultiElem.xml");
        assertTrue(reference.exists());
        assertTrue("File difference", FileHelper.filesEqual(testfile, reference, true, true));
//        doc = new Document(testfile);
//        p1 = (Person) doc.findBean("org.rapidbeans.test.Person", "Bluemel_Martin_19641014");
//        assertEquals("xxx\nyyy", p1.getComment());
        assertTrue(testfile.delete());
    }


    /**
     * Test the default serialization for multiline strings as attributes.
     */
    public void testSerializeStringMultilineElementWithDefinedMapping() throws MalformedURLException {
        AddressBook book = new AddressBook();
        File testfile = new File("testdata/serialization/out1.xml");
        Person p1 = new Person(new String[]{"Bluemel", "Martin", "19641014"});
        p1.setComment3("xxx\nyyy");
        book.addPerson(p1);
        Document doc = new Document(book);
        doc.setUrl(testfile.toURI().toURL());
        doc.save();
        File reference = new File("testdata/serialization/testSerMultiElemDefinedMapping.xml");
        assertTrue(reference.exists());
        assertTrue("File difference", FileHelper.filesEqual(testfile, reference, true, true));
        assertTrue(testfile.delete());
    }

    /**
     * Test method for {@link org.rapidbeans.core.common.RapidBeanSerializer#saveBean(org.rapidbeans.core.basic.RapidBean, java.net.URL, java.lang.String)}.
     * @throws IOException 
     * @throws JAXBException 
     */
    public void testSaveBeanForJaxb() throws IOException {
        File file1 = new File("../org.rapidbeans/testdata/rapidclubadmin/config/Application.xml");
        File file2 = new File("../org.rapidbeans/testdata/rapidclubadmin/config/ApplicationTestTweakedBinding.xml");
        Document doc = new Document(TypeRapidBean.forName(
                ConfigApplicationSwing.class.getName()), file1);
        (new RapidBeanSerializer()).saveBean(doc.getRoot(),
                file2.toURI().toURL(), doc.getEncoding());
        assertTrue(FileHelper.filesEqual(file1, file2, true, true));
        file2.delete();
    }
}
