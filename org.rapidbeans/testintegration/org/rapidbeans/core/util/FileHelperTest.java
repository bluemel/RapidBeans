package org.rapidbeans.core.util;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.rapidbeans.core.exception.UtilException;

public class FileHelperTest {

    @Test
    public final void testCopyDeepFileFile() {
        if ((new File("copytest")).exists()) {
            FileHelper.deleteDeep(new File("copytest"), true);
        }
        try {
            FileHelper.copyDeep(new File("testdata/rapidclubadmin"), new File("copytest"));
            Assert.assertTrue(new File("copytest/config/subfolder/Application.xml").exists());
            Assert.assertTrue(new File("copytest/config/Client.xml").exists());
        } finally {
            if ((new File("copytest")).exists()) {
                FileHelper.deleteDeep(new File("copytest"), true);
            }
        }
    }

    @Test
    public final void testCopyDeepFileFileUnforced() {
        FileHelper.copyDeep(new File("testdata/rapidclubadmin"), new File("copytest"));
        long modifDate = new File("copytest/config/subfolder/Application.xml").lastModified();
        FileHelper.copyDeep(new File("testdata/rapidclubadmin"), new File("copytest"), false);
        Assert.assertEquals(new File("copytest/config/subfolder/Application.xml").lastModified(), modifDate);
        FileHelper.deleteDeep(new File("copytest"));
    }

    @Test
    public final void testCopyDeepFileFileForced() throws InterruptedException {
        if (new File("copytest").exists()) {
            FileHelper.deleteDeep(new File("copytest"));
        }
        FileHelper.copyDeep(new File("testdata/rapidclubadmin"), new File("copytest"));
        long modifDate = new File("copytest/config/subfolder/Application.xml").lastModified();
        // on my OpenSUSE on a VirtualBox the modification date seems to have
        // seconds precision only
        // That's why we have to spend one second of idle time here
        Thread.sleep(1100);
        FileHelper.copyDeep(new File("testdata/rapidclubadmin"), new File("copytest"), true);
        long newModifDate = new File("copytest/config/subfolder/Application.xml").lastModified();
        Assert.assertTrue(newModifDate > modifDate);
        FileHelper.deleteDeep(new File("copytest"));
    }

    @Test
    public final void testCopyDeepExcludeSvnDirs() {
        FileHelper.copyDeep(new File("testdata/rapidclubadmin"), new File("copytest"), false, true);
        Assert.assertTrue(new File("copytest/config/subfolder/Application.xml").exists());
        Assert.assertFalse(new File("copytest/config/subfolder/.svn/text-base/Application.xml.svn-base").exists());
        Assert.assertTrue(new File("copytest/config/Client.xml").exists());
        FileHelper.deleteDeep(new File("copytest"));
    }

    @Test
    public final void testCopyFileNormal() {
        File src = new File("testdata/rapidclubadmin/config/ApplicationAfterMoveComposite.xml");
        File tgt = new File("testdata/testfile.txt");
        if (tgt.exists()) {
            Assert.assertTrue(tgt.delete());
        }
        Assert.assertFalse(tgt.exists());
        FileHelper.copyFile(src, tgt);
        Assert.assertTrue(FileHelper.filesEqual(src, tgt, true, false));
        tgt.delete();
    }

    @Test(expected = UtilException.class)
    public final void testCopyFileNotFound() {
        File src = new File("testdata/rapidclubadmin/config/xxx");
        File tgt = new File("testdata/testfile.txt");
        FileHelper.copyFile(src, tgt);
    }

    @Test
    public final void testCopyFileFileFileBoolean() throws InterruptedException {
        File src = new File("testdata/rapidclubadmin/config/ApplicationAfterMoveComposite.xml");
        File tgt = new File("testdata/testfile.txt");
        FileHelper.copyFile(src, tgt);
        Assert.assertTrue(FileHelper.filesEqual(src, tgt, true, false));
        long mdate = tgt.lastModified();
        Assert.assertTrue(mdate > src.lastModified());
        Thread.sleep(2);
        FileHelper.copyFile(src, tgt, false);
        Assert.assertEquals(mdate, tgt.lastModified());
        // on my OpenSUSE on a VirtualBox the modification date seems to have
        // seconds precision only
        // That's why we have to spend one second of idle time here
        Thread.sleep(1100);
        FileHelper.copyFile(src, tgt, true);
        Assert.assertTrue(mdate < tgt.lastModified());
        Assert.assertTrue(FileHelper.filesEqual(src, tgt, true, false));
        tgt.delete();
    }

    @Test
    public final void testDeleteDeepFile() {
        FileHelper.copyDeep(new File("testdata/rapidclubadmin"), new File("copytest"));
        Assert.assertTrue(new File("copytest/config/subfolder/Application.xml").exists());
        Assert.assertTrue(new File("copytest/config/Client.xml").exists());
        FileHelper.deleteDeep(new File("copytest"));
        Assert.assertFalse(new File("copytest/config/subfolder/Application.xml").exists());
        Assert.assertFalse(new File("copytest/config/Client.xml").exists());
    }

    @Test
    public final void testDeleteDeepFileUnforced() {
        try {
            FileHelper.copyDeep(new File("testdata/rapidclubadmin"), new File("copytest"));
            Assert.assertTrue(new File("copytest/config/subfolder/Application.xml").exists());
            Assert.assertTrue(new File("copytest/config/Client.xml").exists());
            Assert.assertTrue(new File("copytest/config/Client.xml").setReadOnly());
            try {
                FileHelper.deleteDeep(new File("copytest"), false);
                Assert.fail("expected UtilException");
            } catch (UtilException e) {
                Assert.assertTrue(true);
            }
            Assert.assertFalse(new File("copytest/config/subfolder/Application.xml").exists());
            Assert.assertTrue(new File("copytest/config/Client.xml").exists());
        } finally {
            if (new File("copytest").exists()) {
                FileHelper.deleteDeep(new File("copytest"), true);
            }
        }
    }

    @Test
    public final void testDeleteDeepFileForced() {
        try {
            FileHelper.copyDeep(new File("testdata/rapidclubadmin"), new File("copytest"));
            Assert.assertTrue(new File("copytest/config/subfolder/Application.xml").exists());
            Assert.assertTrue(new File("copytest/config/Client.xml").exists());
            Assert.assertTrue(new File("copytest/config/Client.xml").setReadOnly());
            FileHelper.deleteDeep(new File("copytest"), true);
            Assert.assertFalse(new File("copytest/config/subfolder/Application.xml").exists());
            Assert.assertFalse(new File("copytest/config/Client.xml").exists());
            Assert.assertFalse(new File("copytest").exists());
        } finally {
            if (new File("copytest").exists()) {
                FileHelper.deleteDeep(new File("copytest"), true);
            }
        }
    }

    @Test
    public final void testDirsEqualFileString() {
        FileHelper.copyDeep(new File("testdata/rapidclubadmin"), new File("copytest/rapidclubadmin"));
        Assert.assertTrue(FileHelper.dirsEqual(new File("testdata/rapidclubadmin"), "copytest/rapidclubadmin"));
        FileHelper.deleteDeep(new File("copytest"));
    }

    @Test
    public final void testDirsEqualFileStringFileMissing() {
        FileHelper.copyDeep(new File("testdata/rapidclubadmin"), new File("copytest/rapidclubadmin/"));
        Assert.assertTrue(new File("copytest/rapidclubadmin/config/subfolder/Application.xml").delete());
        Assert.assertFalse(FileHelper.dirsEqual(new File("testdata/rapidclubadmin"), "copytest/rapidclubadmin"));
        FileHelper.deleteDeep(new File("copytest"));
    }

    @Test
    public final void testDirsEqualFileStringFileDifferentLength() throws IOException {
        FileHelper.copyDeep(new File("testdata/rapidclubadmin"), new File("copytest/rapidclubadmin/"));
        FileHelper.append(new File("copytest/rapidclubadmin/config/subfolder/Application.xml"), "\nTest");
        Assert.assertFalse(FileHelper.dirsEqual(new File("testdata/rapidclubadmin"), "copytest/rapidclubadmin"));
        FileHelper.deleteDeep(new File("copytest"));
    }

    @Test
    public final void testDirsEqualFileStringFileDifferentChar() throws IOException {
        FileHelper.copyDeep(new File("testdata/rapidclubadmin"), new File("copytest/rapidclubadmin/"));
        File file = new File("copytest/rapidclubadmin/config/subfolder/Application.xml");
        FileHelper.changeCharAt(file, 10, '0');
        Assert.assertFalse(FileHelper.dirsEqual(new File("testdata/rapidclubadmin"), "copytest/rapidclubadmin"));
        FileHelper.deleteDeep(new File("copytest"));
    }

    @Test
    public final void testDirsEqualDepthLimited() {
        FileHelper.copyDeep(new File("testdata/rapidclubadmin"), new File("copytest/rapidclubadmin"));
        FileHelper.deleteDeep(new File("copytest/rapidclubadmin/config/subfolder"));
        Assert.assertFalse(FileHelper.dirsEqual(new File("testdata/rapidclubadmin"), "copytest/rapidclubadmin"));
        Assert.assertFalse(FileHelper.dirsEqual(new File("testdata/rapidclubadmin"),
                new File("copytest/rapidclubadmin"), 2, false));
        Assert.assertTrue(FileHelper.dirsEqual(new File("testdata/rapidclubadmin"),
                new File("copytest/rapidclubadmin"), 1, false));
        FileHelper.deleteDeep(new File("copytest"));
    }

    @Test
    public final void testFilesEqualFile() {
        File src = new File("testdata/rapidclubadmin/config/subfolder/Application.xml");
        File testdir = new File("testdata/test");
        try {
            Assert.assertTrue(testdir.mkdir());
            File tgt = new File("testdata/test/Application.xml");
            FileHelper.copyFile(src, tgt);
            Assert.assertTrue(FileHelper.filesEqual(src, tgt));
        } finally {
            if (testdir.exists()) {
                FileHelper.deleteDeep(testdir);
            }
        }
    }

    @Test(expected = UtilException.class)
    public final void testFilesEqualDir() {
        File src = new File("testdata/rapidclubadmin/config/subfolder");
        File testdir = new File("testdata/test");
        try {
            Assert.assertTrue(testdir.mkdir());
            File tgt = new File("testdata/test/subfolder");
            FileHelper.copyDeep(src, tgt);
            FileHelper.filesEqual(src, tgt);
        } finally {
            if (testdir.exists()) {
                FileHelper.deleteDeep(testdir);
            }
        }
    }

    @Test
    public final void testFilesEqualFileFileBooleanBoolean() {
        File src = new File("testdata/rapidclubadmin/config/subfolder/Application.xml");
        File testdir = new File("testdata/test");
        try {
            Assert.assertTrue(testdir.mkdir());
            File tgt = new File("testdata/test/XXX.xml");
            FileHelper.copyFile(src, tgt);
            Assert.assertTrue(FileHelper.filesEqual(src, tgt, true, true));
            Assert.assertTrue(FileHelper.filesEqual(src, tgt, true, false));
            Assert.assertFalse(FileHelper.filesEqual(src, tgt, false, true));
            Assert.assertFalse(FileHelper.filesEqual(src, tgt, false, false));
        } finally {
            if (testdir.exists()) {
                FileHelper.deleteDeep(testdir);
            }
        }
    }

    @Test
    public final void testListFilesFilterSubdirs() {
        File testfolder = new File("testdata/rapidclubadmin/config");
        Assert.assertEquals(6, FileHelper.listFilesExcludeFilter(testfolder, "\\.*").length);
        Assert.assertEquals(1, FileHelper.listFilesExcludeFilter(testfolder, "*.xml").length);
        Assert.assertEquals(0, FileHelper.listFilesExcludeFilter(testfolder, "*").length);
        Assert.assertEquals(6, FileHelper.listFilesExcludeFilter(testfolder, "xxx").length);
    }

    @Test
    public final void testBasenameFile() {
        Assert.assertEquals("myfile", FileHelper.basename(new File("D:/xxx/yyy/myfile.txt")));
    }

    @Test
    public final void testBasenameFileWithoutExtension() {
        Assert.assertEquals("myfile", FileHelper.basename(new File("D:/xxx/yyy/myfile")));
    }

    @Test
    public final void testBasenameString() {
        Assert.assertEquals("myfile", FileHelper.basename("D:/xxx/yyy/myfile.txt"));
    }

    @Test
    public final void testBasenameStringWithoutExtension() {
        Assert.assertEquals("myfile", FileHelper.basename("D:/xxx/yyy/myfile"));
    }

    @Test
    public final void testExtensionFile() {
        Assert.assertEquals("txt", FileHelper.extension(new File("D:\\xxx\\yyy\\myfile.txt")));
    }

    @Test
    public final void testExtensionString() {
        Assert.assertEquals("txt", FileHelper.extension("D:\\xxx\\yyy\\myfile.txt"));
    }

    /**
     * Test backup.
     */
    @Test
    public void testBackupFile() {
        File file = new File("testdata/testsettings.xml");
        File backupFile = FileHelper.backup(file);
        Assert.assertTrue(backupFile.getName().matches("testsettings_.*.xml.bak"));
        Assert.assertTrue(backupFile.exists());
        backupFile.delete();
    }

    /**
     * Test method for mkdirs with one new directory.
     */
    @Test
    public void testMkdirsParentDirExists() {
        File newDir = new File("testdata/testmkdir");
        try {
            if (newDir.exists()) {
                FileHelper.deleteDeep(newDir);
            }
            Assert.assertFalse(newDir.exists());
            FileHelper.mkdirs(newDir);
            Assert.assertTrue(newDir.exists());
            Assert.assertTrue(newDir.isDirectory());
        } finally {
            if (newDir.exists()) {
                FileHelper.deleteDeep(newDir);
            }
        }
    }

    /**
     * Test method for mkdirs with one new directory.
     */
    @Test
    public void testMkdirsParentDirsDoNotExist() {
        File newDirRoot = new File("testdata/testmkdir");
        File newDir = new File("testdata/testmkdir/xxx/yyy/zzz");
        try {
            Assert.assertFalse(newDir.exists());
            Assert.assertFalse(newDir.getParentFile().exists());
            Assert.assertFalse(newDir.getParentFile().getParentFile().exists());
            Assert.assertFalse(newDirRoot.exists());
            FileHelper.mkdirs(newDir);
            Assert.assertTrue(newDir.exists());
            Assert.assertTrue(newDir.getParentFile().exists());
            Assert.assertTrue(newDir.getParentFile().getParentFile().exists());
            Assert.assertTrue(newDirRoot.exists());
            Assert.assertTrue(newDir.isDirectory());
            Assert.assertTrue(newDirRoot.isDirectory());
        } finally {
            if (newDir.exists()) {
                FileHelper.deleteDeep(newDirRoot);
            }
        }
    }
}
