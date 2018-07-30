/*
 * Rapid Beans Framework, SDK, Maven Plugin: ModelGeneratorTest.java
 *
 * Copyright (C) 2013 Martin Bluemel
 *
 * Creation Date: 26.01.2013
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copies of the GNU Lesser General Public License and the
 * GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package org.rapidbeans.generator.core;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Assert;
import org.junit.Test;
import org.rapidbeans.generator.core.ModelGenerator;
import org.rapidbeans.generator.utils.FileHelper;

/**
 * @author Mischur.Alexander
 * 
 */
public class ModelGeneratorTest {

    @Test
    public void testGenModel() throws Exception {

        File currentDir = new File("./");
        File baseOutputDir = new File(currentDir, "target/testoutput/modelgenerator/");
        File destDirSimple = new File(baseOutputDir, "simple");
        File destDirJoint = new File(baseOutputDir, "merged");
        if (baseOutputDir.exists()) {
            FileUtils.forceDelete(baseOutputDir);
        }
        File model = new File(currentDir, "src/test/resources/modeladdressbook/");
        if (!model.exists() || !model.canRead()) {
            Assert.fail("Model '" + model.getAbsolutePath() + "' can't be read!");
        } else if (!model.isDirectory()) {
            Assert.fail("Model '" + model.getAbsolutePath() + "' is not a directory!");
        }
        System.out.println("**********modelURL=" + model.toURI().toURL());

        ModelGenerator generator = new ModelGenerator(model, destDirSimple, destDirJoint, "", new SystemStreamLog());

        generator.execute();

        Collection<File> filesToCompare = FileUtils.listFiles(new File(currentDir,
                "src/test/resources/result/adressbook"), new String[] { "java" }, true);
        Collection<File> generatedFiles = FileUtils.listFiles(destDirSimple, new String[] { "java" }, true);
        for (File fileToComp : filesToCompare) {
            boolean fileFound = false;

            for (File genFile : generatedFiles) {
                if (fileToComp.getName().equals(genFile.getName())) {
                    fileFound = true;
                    boolean filesEqual = FileHelper.filesEqual(fileToComp, genFile, false, true);
                    Assert.assertTrue("Generated file is not equals to expected file: " + fileToComp.getName(),
                            filesEqual);
                    break;
                }
            }
            Assert.assertTrue("File not found " + fileToComp.getName() + " so it was not generated", fileFound);
        }

    }
}
