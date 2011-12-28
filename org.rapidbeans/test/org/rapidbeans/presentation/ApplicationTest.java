/*
 * Rapid Beans Framework: ApplicationTest.java
*
 * Copyright Martin Bluemel, 2008
*
* Nov 27, 2005
*/
package org.rapidbeans.presentation;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import javax.swing.JMenuItem;

import junit.framework.TestCase;

import org.rapidbeans.core.common.RapidBeansLocale;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.config.ApplicationGuiType;
import org.rapidbeans.presentation.config.ConfigApplication;
import org.rapidbeans.presentation.config.swing.ConfigApplicationSwing;
import org.rapidbeans.presentation.settings.swing.ApplicationLnfTypeSwing;
import org.rapidbeans.presentation.swing.MenuItemSwing;
import org.rapidbeans.presentation.swing.PresentationSwingTestHelper;
import org.rapidbeans.service.Action;

/**
 * Unit tests for class Length.
 *
 * @author Martin Bluemel
 */
public final class ApplicationTest extends TestCase {

    /**
     * Test method for constructor Application()'.
     */
    public void testBBClient() {
        TestClient app = new TestClient();
        assertNotNull(app);
        assertEquals(0, app.getLocales().size());
    }

    /**
     * Test method for initialization of default values'.
     */
    public void testBBClientPropsDefault() {
        TestClient app = new TestClient();
        assertNotNull(app);
        ConfigApplication config = new ConfigApplication();
        app.setConfiguration(config);
        assertEquals("Application", app.getConfiguration().getName());
        assertEquals(ApplicationGuiType.swing, app.getConfiguration().getGuitype());
    }

    /**
     * Test load an Application configuration.
     */
    public void testLoadInstance() {
        ApplicationManager.start(null,
                "../org.rapidbeans/testdata/rapidclubadmin/config/Application.xml",
                new TestClient() {
                    @Override
                    public Properties getOptions() {
                        return new Properties();
                    }
                    public boolean getTestMode() {
                        return true;
                    }
        });
        TestClient app = (TestClient) ApplicationManager.getApplication();
        assertEquals("Test", app.getConfiguration().getName());
        assertSame(ApplicationGuiType.swing, app.getConfiguration().getGuitype());
        assertSame(ApplicationLnfTypeSwing.getInstance("metal"),
                ((ConfigApplicationSwing) app.getConfiguration()).getLookandfeel());

        Collection<RapidBeansLocale> locales = app.getLocales();
        assertEquals(1, locales.size());
        Iterator<RapidBeansLocale> iter1 = locales.iterator();
        RapidBeansLocale locale = iter1.next();
        assertEquals("en", locale.getName());
        assertEquals(Locale.ENGLISH, locale.getLocale());
        assertEquals("File", locale.getStringGui("mainwindow.menubar.file.label"));

        assertEquals("en", app.getCurrentLocale().getName());

        locale = app.getCurrentLocale();
        assertEquals("en", locale.getName());

        MainWindow mainWindow = app.getMainwindow();
        assertEquals(800, mainWindow.getWidth());
        assertEquals(500, mainWindow.getHeight());
        // does not work for the rapid beans framework test
        //assertEquals("Application Test Application", ((JFrame) mainWindow.getWidget()).getTitle());

        Menubar menubar = mainWindow.getMenubar();
        Collection<Submenu> menus = menubar.getMenus();
        assertEquals(3, menus.size());
        Iterator<Submenu> iter = menus.iterator();
        Submenu menu = iter.next();
        assertEquals("file", menu.getName());
        assertEquals("File", ((JMenuItem) menu.getWidget()).getText());
        Iterator<MenuEntry> iter2 = menu.getMenuentrys().iterator();
        iter2.next(); // load
        iter2.next(); // save
        MenuItem itemQuit = (MenuItem) iter2.next();
        assertEquals("quit", itemQuit.getName());
        Action action = itemQuit.getAction();
        assertEquals("org.rapidbeans.service.ActionQuit", action.getClassname());
        menu = iter.next();
        assertEquals("edit", menu.getName());
        menu = iter.next();
        assertEquals("test", menu.getName());

        Iterator<MenuEntry> iter3 = menu.getMenuentrys().iterator();
        iter3.next();
        Submenu submenu2 = (Submenu)iter3.next();
        assertEquals("test2", submenu2.getName());

        Iterator<MenuEntry> iter4 = submenu2.getMenuentrys().iterator();
        iter4.next();
        Submenu submenu22 = (Submenu)iter4.next();
        assertEquals("test22", submenu22.getName());

        Iterator<MenuEntry> iter5 = submenu22.getMenuentrys().iterator();
        MenuItem item221 = (MenuItem)iter5.next();
        assertTrue(item221 instanceof MenuItemSwing);
        assertEquals("test221", item221.getName());
        MenuItemSwing item222 = (MenuItemSwing)iter5.next();
        assertEquals("test222", item222.getName());
        // does not work for the rapid beans framework test
        //assertEquals("Test 222", ((JMenuItem) item222.getWidget()).getText());
    }

    /**
     * Test method for setType() normal.
     */
    public void testSetType() {
        TestClient app = new TestClient();
        ConfigApplication config = new ConfigApplication();
        app.setConfiguration(config);
        app.getConfiguration().setName("Hugo");
        assertEquals("Hugo", app.getConfiguration().getName());
    }

    /**
     * Test loading the same document twice
     */
    public void testViewDocumentSameTwice() {
        try {
            Application client = PresentationSwingTestHelper.getTestClient();
            assertNotNull(client);
            Document doc1 = new Document(TypeRapidBean.forName(
                    ConfigApplication.class.getName()),
                    new File("../org.rapidbeans/testdata/rapidclubadmin/config/Application.xml"));
            assertTrue(doc1.getName().endsWith("/org.rapidbeans/testdata/rapidclubadmin/config/Application.xml"));
            DocumentView view1 = client.openDocumentView(doc1, null, null);
            Document doc2 = new Document(TypeRapidBean.forName(
                    ConfigApplication.class.getName()),
                    new File("../org.rapidbeans/testdata/rapidclubadmin/config/Application.xml"));
            DocumentView view2 = client.openDocumentView(doc2, null, null);
            assertSame(view1, view2);
        } finally {
            PresentationSwingTestHelper.releaseTestClient();
        }
    }

    /**
     * Test loading two documents with same filenames from different folders
     */
    public void testViewDocumentFromFileTwoDifferentSameFilename() {
        try {
            Application client = PresentationSwingTestHelper.getTestClient();
            assertNotNull(client);
            Document doc1 = new Document(TypeRapidBean.forName(
                    ConfigApplication.class.getName()),
                    new File("../org.rapidbeans/testdata/rapidclubadmin/config/Application.xml"));
            assertTrue(doc1.getName().endsWith("/org.rapidbeans/testdata/rapidclubadmin/config/Application.xml"));
            client.openDocumentView(doc1, null, null);
            Document doc2 = new Document(TypeRapidBean.forName(
                    ConfigApplication.class.getName()),
                    new File("../org.rapidbeans/testdata/rapidclubadmin/config/subfolder/Application.xml"));
            assertTrue(doc2.getName().endsWith("/org.rapidbeans/testdata/rapidclubadmin/config/subfolder/Application.xml"));
            client.openDocumentView(doc2, null, null);
        } finally {
            PresentationSwingTestHelper.releaseTestClient();
        }
    }
}
