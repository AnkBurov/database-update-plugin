package io.ankburov.gradle.dbupdate

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.UnexpectedBuildFailure
import org.h2.tools.RunScript
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import java.nio.charset.Charset

import static io.ankburov.gradle.dbupdate.TestConst.APPLIED_SCRIPTS_SELECTION_SCRIPT
import static io.ankburov.gradle.dbupdate.TestConst.INITIAL_SCRIPT_FILE
import static io.ankburov.gradle.dbupdate.TestConst.LST_FILE
import static io.ankburov.gradle.dbupdate.TestConst.PASSWORD
import static io.ankburov.gradle.dbupdate.TestConst.SCRIPTS_PATH
import static io.ankburov.gradle.dbupdate.TestConst.USER
import static io.ankburov.gradle.dbupdate.TestConst.WRONG_BUILD_FILE_WITH_ILLEGAL_DRIVER

import static io.ankburov.gradle.dbupdate.TestUtils.writeFile
import static io.ankburov.gradle.dbupdate.TestUtils.writeFileToTempDirectory
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class IntegrationTest {

    @Rule
    public TemporaryFolder testProjectDir = new TemporaryFolder()
    private File buildFile
    def pluginClasspathResource = getClass().classLoader.findResource("plugin-classpath.txt")
    def pluginClasspath = pluginClasspathResource.readLines().collect { new File(it) }

    @Before
    void setUp() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle")
    }

    @Test
    void testCreateFilesTask() {
        writeFile(buildFile, { TestUtils.getCommonBuildFile(UUID.randomUUID()) })

        BuildResult result = buildProject("createFiles")

        assertEquals(result.task(":createFiles").getOutcome(), SUCCESS);
        def scriptsPathDir = new File(testProjectDir.getRoot().path, SCRIPTS_PATH.path)
        assertTrue(scriptsPathDir.exists())
        def lstPathFile = new File(scriptsPathDir.path, LST_FILE)
        assertTrue(lstPathFile.exists())
        def initialScriptPathFile = new File(scriptsPathDir.path, INITIAL_SCRIPT_FILE)
        assertTrue(initialScriptPathFile.exists())
        def dataFolderPathFile = new File(scriptsPathDir.path, "data")
        assertTrue(dataFolderPathFile.exists())
        def appliedScriptsFile = new File(scriptsPathDir.path, APPLIED_SCRIPTS_SELECTION_SCRIPT)
        assertTrue(appliedScriptsFile.exists())
    }

    @Test
    void applyInitialScript() throws Exception {
        applyInitialScriptInternal(UUID.randomUUID())
    }

    private void applyInitialScriptInternal(UUID dbUuid) {
        testCreateFilesTask()
        writeFileToTempDirectory(testProjectDir.getRoot(), INITIAL_SCRIPT_FILE)
        writeFile(buildFile, { TestUtils.getCommonBuildFile(dbUuid) })

        BuildResult result = buildProject("applyInitialScript")

        assertEquals(result.task(":applyInitialScript").getOutcome(), SUCCESS);
    }

    @Test(expected = UnexpectedBuildFailure)
    void applyInitialScriptWrongDriver() throws Exception {
        testCreateFilesTask()
        writeFileToTempDirectory(testProjectDir.getRoot(), INITIAL_SCRIPT_FILE)
        writeFile(buildFile, { WRONG_BUILD_FILE_WITH_ILLEGAL_DRIVER })

        buildProject("applyInitialScript")
    }

    @Test
    void testUpdateSchemaTask() throws Exception {
        def dbUuid = UUID.randomUUID()
        testCreateFilesTask()
        applyInitialScriptInternal(dbUuid)
        writeFileToTempDirectory(testProjectDir.getRoot(), INITIAL_SCRIPT_FILE)
        writeFileToTempDirectory(testProjectDir.getRoot(), LST_FILE)
        writeFileToTempDirectory(testProjectDir.getRoot(), APPLIED_SCRIPTS_SELECTION_SCRIPT)
        TestUtils.writeFileToTempDataDirectory(testProjectDir.getRoot(), TestConst.FIRST_SCRIPT_NAME)
        TestUtils.writeFileToTempDataDirectory(testProjectDir.getRoot(), TestConst.FIRST_SCRIPT_ROLLBACK_NAME)
        writeFile(buildFile, { TestUtils.getCommonBuildFile(dbUuid) })

        BuildResult result = buildProject("updateSchema")

        assertEquals(result.task(":updateSchema").getOutcome(), SUCCESS);
    }

    @After
    void tearDown() throws Exception {
        RunScript.execute(TestConst.TEST_URL, USER, PASSWORD,
                "classpath:shutdown.sql", Charset.forName("utf-8"), false);
    }

    private BuildResult buildProject(String... strings) {
        return GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withPluginClasspath(pluginClasspath)
                .withArguments(strings)
                .withDebug(true)
                .build()
    }
}
