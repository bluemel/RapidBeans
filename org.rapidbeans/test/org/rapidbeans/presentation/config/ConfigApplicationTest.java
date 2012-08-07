/*
 * Rapid Beans Framework: ConfigApplicationTest.java
 *
 * created on 23.03.2007
 *
 * (c) Martin Bluemel, 2007
 */
package org.rapidbeans.presentation.config;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.rapidbeans.datasource.Document;

/**
 *
 * @author Martin Bluemel
 *
 */
public class ConfigApplicationTest extends TestCase {

    /**
     * load a test configuration.
     */
    public void testLoadConfiguration() {
        Document doc = new Document(
                new File("../org.rapidbeans/testdata/rapidclubadmin/config/Client.xml"));
        ConfigApplication configRoot = (ConfigApplication) doc.getRoot();
        ConfigDocument doccfg = configRoot.getDocuments().iterator().next();
        assertEquals("billingperiod", doccfg.getName());
        ConfigView stdViewcfg = doccfg.getViews().iterator().next();
        assertEquals("trainings", stdViewcfg.getName());
        List<ConfigEditorBean> edCfgs = (List<ConfigEditorBean>) configRoot.getBeaneditors();
        ConfigEditorBean edTrainer = edCfgs.get(4);
        assertNull(edTrainer.getEditorclass());
        assertEquals("org.rapidbeans.clubadmin.domain.Trainer", edTrainer.getBeantype());
     }
}
