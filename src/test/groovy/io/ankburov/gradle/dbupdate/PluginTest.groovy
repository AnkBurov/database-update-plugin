package io.ankburov.gradle.dbupdate

import io.ankburov.gradle.dbupdate.ext.DbUpdateExtension
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static io.ankburov.gradle.dbupdate.TestConst.APPLIED_SCRIPTS_SELECTION_SCRIPT
import static io.ankburov.gradle.dbupdate.TestConst.ENCODING
import static io.ankburov.gradle.dbupdate.TestConst.INITIAL_SCRIPT_FILE
import static io.ankburov.gradle.dbupdate.TestConst.LST_FILE
import static io.ankburov.gradle.dbupdate.TestConst.SCRIPT_DIR_DELIMITER
import static io.ankburov.gradle.dbupdate.TestConst.SCRIPT_NAME_DELIMITER
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue

class PluginTest {

    @Test
    void testHasExtensions() throws Exception {
        def project = ProjectBuilder.builder().withName("test-project").build()
        project.pluginManager.apply DbUpdatePlugin
        assertTrue(project.extensions.dbUpdate instanceof DbUpdateExtension)
        assertEquals(LST_FILE, project.extensions.dbUpdate.lstFileName)
        assertEquals(INITIAL_SCRIPT_FILE, project.extensions.dbUpdate.initialScriptFileName)
        assertEquals(APPLIED_SCRIPTS_SELECTION_SCRIPT, project.extensions.dbUpdate.appliedScriptsSelectionScriptFileName)
        assertEquals(SCRIPT_DIR_DELIMITER, project.extensions.dbUpdate.scriptDirDelimiter)
        assertEquals(SCRIPT_NAME_DELIMITER, project.extensions.dbUpdate.scriptNameDelimiter)
        assertEquals(ENCODING, project.extensions.dbUpdate.encoding)
        assertNull(project.extensions.dbUpdate.scriptsPath)
        assertNull(project.extensions.dbUpdate.credentials.url)
        assertNull(project.extensions.dbUpdate.credentials.user)
        assertNull(project.extensions.dbUpdate.credentials.password)
        assertNull(project.extensions.dbUpdate.credentials.driverName)
    }

    @Test
    void testCreateFilesTask() {
        def project = ProjectBuilder.builder().withName("test-project").build()
        project.pluginManager.apply DbUpdatePlugin
        assertNotNull(project.tasks.createFiles)
    }

    @Test
    void testAddDependenciesToSqlClassLoaderTask() throws Exception {
        def project = ProjectBuilder.builder().withName("test-project").build()
        project.pluginManager.apply DbUpdatePlugin
        assertNotNull(project.tasks.addDependenciesToSqlClassLoader)
    }

    @Test
    void testApplyInitialScriptTask() throws Exception {
        def project = ProjectBuilder.builder().withName("test-project").build()
        project.pluginManager.apply DbUpdatePlugin
        assertNotNull(project.tasks.applyInitialScript)
        assertTrue(project.tasks.applyInitialScript.getDependsOn().contains("addDependenciesToSqlClassLoader"))
    }

    @Test
    void testUpdateSchemaTask() throws Exception {
        def project = ProjectBuilder.builder().withName("test-project").build()
        project.pluginManager.apply DbUpdatePlugin
        assertNotNull(project.tasks.updateSchema)
        assertTrue(project.tasks.updateSchema.getDependsOn().contains("addDependenciesToSqlClassLoader"))
    }
}
