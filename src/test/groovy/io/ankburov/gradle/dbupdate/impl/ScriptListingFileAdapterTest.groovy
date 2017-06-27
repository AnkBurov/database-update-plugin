package io.ankburov.gradle.dbupdate.impl

import groovy.transform.CompileStatic
import io.ankburov.gradle.dbupdate.TestConst
import io.ankburov.gradle.dbupdate.TestUtils
import org.junit.Test

import static io.ankburov.gradle.dbupdate.TestConst.FIRST_SCRIPT
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

@CompileStatic
class ScriptListingFileAdapterTest {

    private ScriptListingFileAdapter adapter = new ScriptListingFileAdapter("utf-8")

    @Test
    void getScripts() throws Exception {
        File scriptsFile = TestUtils.getScript(TestConst.LST_FILE)
        List<String> scripts = adapter.getScripts(scriptsFile)
        assertTrue(!scripts.isEmpty())
        assertEquals(FIRST_SCRIPT, scripts.get(0))
    }

    @Test(expected = IllegalArgumentException)
    void getScriptsFromNotExistentFile() throws Exception {
        def script = new File("non-existent.lst")

        adapter.getScripts(script)
    }
}
