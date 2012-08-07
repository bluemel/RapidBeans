/*
 * RapidBeans Framework: HistoryListTest.java
 *
 * Copyright Martin Bluemel, 2009
 *
 * 11.08.2009
 */
package org.rapidbeans.core.common;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import junit.framework.TestCase;

/**
 * Tests for the history list.
 *
 * @author Martin Bluemel
 */
public class HistoryListTest extends TestCase {

    public void testCreateNewHistoryList() {
        HistoryList<String> list = new HistoryList<String>(5);
        assertEquals(5, list.getMax());
        assertEquals(0, list.size());
    }

    public void testCreateNewHistoryListDefault() {
        HistoryList<URL> list = new HistoryList<URL>();
        assertEquals(10, list.getMax());
        assertEquals(0, list.size());
    }

    public void testCreateNewHistoryListIllegal() {
        try {
            new HistoryList<String>(-2);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    public void testAddFirst() throws MalformedURLException {
        HistoryList<URL> list = new HistoryList<URL>();
        list.add(new File("test1.xml").toURI().toURL());
        assertEquals(1, list.size());
        Iterator<URL> iter = list.iterator();
        assertEquals("test1.xml", new File(iter.next().getFile()).getName());
        assertFalse(iter.hasNext());
        assertEquals("test1.xml", new File(list.get(0).getFile()).getName());
        try {
            list.get(1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public void testAdd2() throws MalformedURLException {
        HistoryList<URL> list = new HistoryList<URL>();
        list.add(new File("test1.xml").toURI().toURL());
        list.add(new File("test2.xml").toURI().toURL());
        Iterator<URL> iter = list.iterator();
        assertEquals("test2.xml", new File(iter.next().getFile()).getName());
        assertEquals("test1.xml", new File(iter.next().getFile()).getName());
        assertEquals(2, list.size());
        assertFalse(iter.hasNext());
        assertEquals("test2.xml", new File(list.get(0).getFile()).getName());
        assertEquals("test1.xml", new File(list.get(1).getFile()).getName());
        try {
            list.get(2);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public void testAdd3() throws MalformedURLException {
        HistoryList<String> list = new HistoryList<String>();
        list.add("test1.xml");
        list.add("test2.xml");
        list.add("test3.xml");
        assertEquals(3, list.size());
        Iterator<String> iter = list.iterator();
        assertEquals("test3.xml", iter.next());
        assertEquals("test2.xml", iter.next());
        assertEquals("test1.xml", iter.next());
        assertFalse(iter.hasNext());
        assertEquals("test3.xml", list.get(0));
        assertEquals("test2.xml", list.get(1));
        assertEquals("test1.xml", list.get(2));
        try {
            list.get(3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public void testAdd3PartiallySame() throws MalformedURLException {
        HistoryList<String> list = new HistoryList<String>();
        list.add("test1.xml");
        list.add("test2.xml");
        list.add("test2.xml");
        list.add("test3.xml");
        list.add("test3.xml");
        list.add("test2.xml");
        assertEquals(3, list.size());
        Iterator<String> iter = list.iterator();
        assertEquals("test2.xml", iter.next());
        assertEquals("test3.xml", iter.next());
        assertEquals("test1.xml", iter.next());
        assertFalse(iter.hasNext());
        assertEquals("test2.xml", list.get(0));
        assertEquals("test3.xml", list.get(1));
        assertEquals("test1.xml", list.get(2));
        try {
            list.get(3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public void testAdd5() throws MalformedURLException {
        HistoryList<URL> historyList = new HistoryList<URL>();
        historyList.add(new File("test1.xml").toURI().toURL());
        historyList.add(new File("test2.xml").toURI().toURL());
        historyList.add(new File("test3.xml").toURI().toURL());
        historyList.add(new File("test4.xml").toURI().toURL());
        historyList.add(new File("test5.xml").toURI().toURL());
        assertEquals(5, historyList.size());
        Iterator<URL> iter = historyList.iterator();
        assertEquals("test5.xml", new File(iter.next().getFile()).getName());
        assertEquals("test4.xml", new File(iter.next().getFile()).getName());
        assertEquals("test3.xml", new File(iter.next().getFile()).getName());
        assertEquals("test2.xml", new File(iter.next().getFile()).getName());
        assertEquals("test1.xml", new File(iter.next().getFile()).getName());
        assertFalse(iter.hasNext());
        assertEquals("test5.xml", new File(historyList.get(0).getFile()).getName());
        assertEquals("test4.xml", new File(historyList.get(1).getFile()).getName());
        assertEquals("test3.xml", new File(historyList.get(2).getFile()).getName());
        assertEquals("test2.xml", new File(historyList.get(3).getFile()).getName());
        assertEquals("test1.xml", new File(historyList.get(4).getFile()).getName());
        try {
            historyList.get(5);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public void testAddMoreThanMax() throws MalformedURLException {
        HistoryList<URL> list = new HistoryList<URL>(3);
        list.add(new File("test1.xml").toURI().toURL());
        assertEquals(1, list.size());
        list.add(new File("test2.xml").toURI().toURL());
        assertEquals(2, list.size());
        list.add(new File("test3.xml").toURI().toURL());
        assertEquals(3, list.size());
        list.add(new File("test4.xml").toURI().toURL());
        assertEquals(3, list.size());
        list.add(new File("test5.xml").toURI().toURL());
        assertEquals(3, list.size());
        Iterator<URL> iter = list.iterator();
        assertEquals("test5.xml", new File(iter.next().getFile()).getName());
        assertEquals("test4.xml", new File(iter.next().getFile()).getName());
        assertEquals("test3.xml", new File(iter.next().getFile()).getName());
        assertFalse(iter.hasNext());
        assertEquals("test5.xml", new File(list.get(0).getFile()).getName());
        assertEquals("test4.xml", new File(list.get(1).getFile()).getName());
        assertEquals("test3.xml", new File(list.get(2).getFile()).getName());
        try {
            list.get(3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public void testAddMoreThanMaxPartiallySame() throws MalformedURLException {
        HistoryList<URL> list = new HistoryList<URL>(3);
        list.add(new File("test1.xml").toURI().toURL());
        assertEquals(1, list.size());
        list.add(new File("test2.xml").toURI().toURL());
        assertEquals(2, list.size());
        list.add(new File("test1.xml").toURI().toURL());
        assertEquals(2, list.size());
        list.add(new File("test3.xml").toURI().toURL());
        assertEquals(3, list.size());
        list.add(new File("test4.xml").toURI().toURL());
        assertEquals(3, list.size());
        list.add(new File("test3.xml").toURI().toURL());
        assertEquals(3, list.size());
        list.add(new File("test5.xml").toURI().toURL());
        assertEquals(3, list.size());
        list.add(new File("test4.xml").toURI().toURL());
        assertEquals(3, list.size());
        list.add(new File("test4.xml").toURI().toURL());
        assertEquals(3, list.size());
        Iterator<URL> iter = list.iterator();
        assertEquals("test4.xml", new File(iter.next().getFile()).getName());
        assertEquals("test5.xml", new File(iter.next().getFile()).getName());
        assertEquals("test3.xml", new File(iter.next().getFile()).getName());
        assertFalse(iter.hasNext());
        assertEquals("test4.xml", new File(list.get(0).getFile()).getName());
        assertEquals("test5.xml", new File(list.get(1).getFile()).getName());
        assertEquals("test3.xml", new File(list.get(2).getFile()).getName());
        try {
            list.get(3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public void testSetMaxGreater() throws MalformedURLException {
        HistoryList<URL> list = new HistoryList<URL>(3);
        list.add(new File("test1.xml").toURI().toURL());
        assertEquals(1, list.size());
        list.add(new File("test2.xml").toURI().toURL());
        assertEquals(2, list.size());
        list.add(new File("test3.xml").toURI().toURL());
        assertEquals(3, list.size());
        list.add(new File("test4.xml").toURI().toURL());
        assertEquals(3, list.size());
        list.add(new File("test5.xml").toURI().toURL());
        assertEquals(3, list.size());
        assertEquals("test5.xml", new File(list.get(0).getFile()).getName());
        assertEquals("test4.xml", new File(list.get(1).getFile()).getName());
        assertEquals("test3.xml", new File(list.get(2).getFile()).getName());
        try {
            list.get(3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
        list.setMax(5);
        assertEquals(3, list.size());
        list.add(new File("test6.xml").toURI().toURL());
        assertEquals(4, list.size());
        list.add(new File("test7.xml").toURI().toURL());
        assertEquals(5, list.size());
        list.add(new File("test8.xml").toURI().toURL());
        assertEquals(5, list.size());
        assertEquals("test8.xml", new File(list.get(0).getFile()).getName());
        assertEquals("test7.xml", new File(list.get(1).getFile()).getName());
        assertEquals("test6.xml", new File(list.get(2).getFile()).getName());
        assertEquals("test5.xml", new File(list.get(3).getFile()).getName());
        assertEquals("test4.xml", new File(list.get(4).getFile()).getName());
        try {
            list.get(5);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public void testSetMaxLessFilled() throws MalformedURLException {
        HistoryList<URL> list = new HistoryList<URL>(4);
        list.add(new File("test1.xml").toURI().toURL());
        assertEquals(1, list.size());
        list.add(new File("test2.xml").toURI().toURL());
        assertEquals(2, list.size());
        list.add(new File("test3.xml").toURI().toURL());
        assertEquals(3, list.size());
        list.add(new File("test4.xml").toURI().toURL());
        assertEquals(4, list.size());
        list.add(new File("test5.xml").toURI().toURL());
        assertEquals(4, list.size());
        assertEquals("test5.xml", new File(list.get(0).getFile()).getName());
        assertEquals("test4.xml", new File(list.get(1).getFile()).getName());
        assertEquals("test3.xml", new File(list.get(2).getFile()).getName());
        assertEquals("test2.xml", new File(list.get(3).getFile()).getName());
        try {
            list.get(4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
        list.setMax(2);
        assertEquals(2, list.size());        
        assertEquals("test5.xml", new File(list.get(0).getFile()).getName());
        assertEquals("test4.xml", new File(list.get(1).getFile()).getName());
        try {
            list.get(2);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public void testSetMaxLessNotFilled() throws MalformedURLException {
        HistoryList<URL> list = new HistoryList<URL>(4);
        list.add(new File("test1.xml").toURI().toURL());
        assertEquals(1, list.size());
        list.add(new File("test2.xml").toURI().toURL());
        assertEquals(2, list.size());
        list.add(new File("test3.xml").toURI().toURL());
        assertEquals(3, list.size());
        assertEquals("test3.xml", new File(list.get(0).getFile()).getName());
        assertEquals("test2.xml", new File(list.get(1).getFile()).getName());
        assertEquals("test1.xml", new File(list.get(2).getFile()).getName());
        try {
            list.get(3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
        list.setMax(2);
        assertEquals(2, list.size());        
        assertEquals("test3.xml", new File(list.get(0).getFile()).getName());
        assertEquals("test2.xml", new File(list.get(1).getFile()).getName());
        try {
            list.get(2);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public void testSetMaxEqual() throws MalformedURLException {
        HistoryList<URL> list = new HistoryList<URL>(4);
        list.add(new File("test1.xml").toURI().toURL());
        assertEquals(1, list.size());
        list.add(new File("test2.xml").toURI().toURL());
        assertEquals(2, list.size());
        list.add(new File("test3.xml").toURI().toURL());
        assertEquals(3, list.size());
        list.add(new File("test4.xml").toURI().toURL());
        assertEquals(4, list.size());
        list.add(new File("test5.xml").toURI().toURL());
        list.setMax(4);
        assertEquals(4, list.size());
        assertEquals("test5.xml", new File(list.get(0).getFile()).getName());
        assertEquals("test4.xml", new File(list.get(1).getFile()).getName());
        assertEquals("test3.xml", new File(list.get(2).getFile()).getName());
        assertEquals("test2.xml", new File(list.get(3).getFile()).getName());
        try {
            list.get(4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    public void testAddDoubleUrl() throws MalformedURLException {
        HistoryList<URL> list = new HistoryList<URL>(4);
        list.add(new File("test1.xml").toURI().toURL());
        assertEquals(1, list.size());
        list.add(new File("test2.xml").toURI().toURL());
        assertEquals(2, list.size());
        list.add(new File("test2.xml").toURI().toURL());
        assertEquals(2, list.size());
        list.add(new File("test3.xml").toURI().toURL());
        assertEquals(3, list.size());
        list.add(new File("test4.xml").toURI().toURL());
        assertEquals(4, list.size());
        list.add(new File("test5.xml").toURI().toURL());
        assertEquals("test5.xml", new File(list.get(0).getFile()).getName());
        assertEquals("test4.xml", new File(list.get(1).getFile()).getName());
        assertEquals("test3.xml", new File(list.get(2).getFile()).getName());
        assertEquals("test2.xml", new File(list.get(3).getFile()).getName());
        try {
            list.get(4);
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }
}
