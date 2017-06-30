package io.ankburov.gradle.dbupdate

import static io.ankburov.gradle.dbupdate.TestConst.DIRECTORY

import static io.ankburov.gradle.dbupdate.TestConst.APPLIED_SCRIPTS_SELECTION_SCRIPT
import static io.ankburov.gradle.dbupdate.TestConst.INITIAL_SCRIPT_FILE
import static io.ankburov.gradle.dbupdate.TestConst.LST_FILE
import static io.ankburov.gradle.dbupdate.TestConst.PASSWORD
import static io.ankburov.gradle.dbupdate.TestConst.SCRIPTS_PATH
import static io.ankburov.gradle.dbupdate.TestConst.USER
import static io.ankburov.gradle.dbupdate.TestConst.TEST_URL
import static io.ankburov.gradle.dbupdate.TestConst.DRIVER_NAME

class TestUtils {

    static File getScript(String relativeScriptName) {
        URL scriptsFilePath = this.getResource("$DIRECTORY/$relativeScriptName")
        return new File(scriptsFilePath.path)
    }

    static void writeFile(File destination, Closure<String> closure) {
        destination.write(closure.call())
    }

    static void writeFileToTempDirectory(File tempDir, String resourceFile) {
        def script = getScript(resourceFile)
        writeFile(new File("${tempDir.path}/${SCRIPTS_PATH.path}", script.name)) { script.text }
    }

    static void writeFileToTempDataDirectory(File tempDir, String resourceFile) {
        def script = getScript("data/$resourceFile")
        writeFile(new File("${tempDir.path}/${SCRIPTS_PATH.path}/data", script.name)) { script.text }
    }

    /**
     * Get common build file for a project in integration tests
     *
     * Passing UUID is needed to ensure that each test has its own instance of database (with multiple connections support, so
     * just mem: in url doesn't fit)
     * @param uuid UUID of database instance identifier
     * @return build.gradle content
     */
    static GString getCommonBuildFile(UUID uuid) {
        """
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.h2database:h2:1.4.196")
    }
}

plugins {
    id "io.ankburov.gradle.dbupdateplugin"
}

dbUpdate {
    scriptsPath = file('$SCRIPTS_PATH')
    lstFileName = '$LST_FILE'
    initialScriptFileName = '$INITIAL_SCRIPT_FILE'
    appliedScriptsSelectionScriptFileName = '$APPLIED_SCRIPTS_SELECTION_SCRIPT'
    credentials {
        url = '$TEST_URL/$uuid'
        user = '$USER'
        password = '$PASSWORD'
        driverName = '$DRIVER_NAME'
    }
}
"""
    }
}
