package io.ankburov.gradle.dbupdate.impl

import groovy.transform.CompileStatic
import org.junit.Test

import static io.ankburov.gradle.dbupdate.TestConst.BLANK_LST_FILE
import static io.ankburov.gradle.dbupdate.TestConst.FIRST_SCRIPT
import static io.ankburov.gradle.dbupdate.TestConst.LST_FILE
import static io.ankburov.gradle.dbupdate.TestConst.LST_FILE_SAME_IDS
import static io.ankburov.gradle.dbupdate.TestUtils.getScript
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

@CompileStatic
class ScriptListingFileServiceTest {

    private ScriptListingFileService service = new ScriptListingFileServiceImpl('/' as Character, "_", "utf-8")

    @Test
    void getScriptsMap() throws Exception {
        File scriptsFile = getScript(LST_FILE)
        def scriptsMap = service.getScriptsMap(scriptsFile)

        assertTrue(!scriptsMap.isEmpty())
        assertEquals(FIRST_SCRIPT, scriptsMap.get("1"))
    }

    @Test(expected = IllegalStateException)
    void testBlankFile() throws Exception {
        File scriptsFile = getScript(BLANK_LST_FILE)

        service.getScriptsMap(scriptsFile)
    }

    @Test(expected = IllegalStateException)
    void getScriptsMapFromFileWithSameIds() throws Exception {
        File scriptsFile = getScript(LST_FILE_SAME_IDS)

        service.getScriptsMap(scriptsFile)
    }
}
