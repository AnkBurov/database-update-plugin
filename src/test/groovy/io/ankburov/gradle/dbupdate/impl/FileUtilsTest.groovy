package io.ankburov.gradle.dbupdate.impl

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static io.ankburov.gradle.dbupdate.TestConst.APPLIED_SCRIPTS_SELECTION_SCRIPT
import static io.ankburov.gradle.dbupdate.TestConst.INITIAL_SCRIPT_FILE
import static io.ankburov.gradle.dbupdate.TestConst.LST_FILE
import static org.junit.Assert.assertTrue

class FileUtilsTest {

    @Rule
    public TemporaryFolder testProjectDir = new TemporaryFolder()
    private def scriptsPath

    @Before
    void setUp() throws Exception {
        scriptsPath = new File(testProjectDir.getRoot(), "testdirectory")
    }

    @Test
    void createInitialFiles() throws Exception {
        FileUtils.createInitialFiles(scriptsPath, LST_FILE, INITIAL_SCRIPT_FILE, APPLIED_SCRIPTS_SELECTION_SCRIPT)
        assertTrue(scriptsPath.exists())
        assertTrue(new File(scriptsPath.path + "/$LST_FILE").exists())
        assertTrue(new File(scriptsPath.path + "/$INITIAL_SCRIPT_FILE").exists())
        assertTrue(new File(scriptsPath.path + "/data").exists())
        assertTrue(new File(scriptsPath.path + "/$APPLIED_SCRIPTS_SELECTION_SCRIPT").exists())
    }

    @Test(expected = IllegalArgumentException)
    void createInitialFilesWithScriptsPathDirectory() throws Exception {
        def scriptsPath = new File(testProjectDir.getRoot(), "testfile.txt")
        scriptsPath.createNewFile()
        FileUtils.createInitialFiles(scriptsPath, LST_FILE, INITIAL_SCRIPT_FILE, APPLIED_SCRIPTS_SELECTION_SCRIPT)

    }
}
