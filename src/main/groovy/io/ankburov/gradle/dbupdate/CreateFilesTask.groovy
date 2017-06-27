package io.ankburov.gradle.dbupdate

import groovy.transform.CompileStatic
import io.ankburov.gradle.dbupdate.impl.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * Task that creates lst file and other initial files and directories needed for plugin
 */
@CompileStatic
class CreateFilesTask extends DefaultTask {

    @Input
    File scriptsPath

    @Input
    String lstFileName

    @Input
    String initialScriptFileName

    @Input
    String appliedScriptsSelectionScriptFileName

    @TaskAction
    void createFiles() {
        FileUtils.createInitialFiles(scriptsPath, lstFileName, initialScriptFileName, appliedScriptsSelectionScriptFileName)
    }
}
