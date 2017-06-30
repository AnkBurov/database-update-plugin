package io.ankburov.gradle.dbupdate

import groovy.transform.CompileStatic
import io.ankburov.gradle.dbupdate.impl.ScriptListingFileService
import io.ankburov.gradle.dbupdate.impl.ServiceInstanceFactory
import io.ankburov.gradle.dbupdate.impl.SqlExecutorService
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * Task that updates database by executing not applied to database scripts
 *
 * Each script file should also has insert to DBUPDATE (or other table defined in selectionScript.sql) table containing
 * list of already applied scripts
 */
@CompileStatic
class UpdateSchemaTask extends DefaultTask {

    @Input
    File scriptsPath
    @Input
    String lstFileName
    @Input
    String initialScriptFileName
    @Input
    String appliedScriptsSelectionScriptFileName
    @Input
    Character scriptDirDelimiter
    @Input
    String scriptNameDelimiter
    @Input
    String encoding
    @Input
    String queryDelimiter
    @Input
    String url
    @Input
    String user
    @Input
    String password
    @Input
    String driverName

    ScriptListingFileService listingFileService

    SqlExecutorService sqlExecutorService

    @TaskAction
    void updateSchema() {
        listingFileService = ServiceInstanceFactory.getScriptListingFileService(scriptDirDelimiter, scriptNameDelimiter, encoding)
        def lstFile = new File(scriptsPath.path, lstFileName)
        Map<String, String> scriptsMap = listingFileService.getScriptsMap(lstFile)

        sqlExecutorService = ServiceInstanceFactory.getSqlExecutorService(url, user, password, driverName, queryDelimiter)
        def selectionScript = new File(scriptsPath.path, appliedScriptsSelectionScriptFileName)
        List<String> appliedScriptIds = sqlExecutorService.getAppliedScriptIds(selectionScript)
        scriptsMap.keySet().removeAll(appliedScriptIds)
        println("Found ${scriptsMap.size()} not executed scripts")

        for (String value : scriptsMap.values()) {
            println("Executing $value script")
            def scriptFile = new File(scriptsPath.path, value)
            sqlExecutorService.executeScript(scriptFile)
        }
    }
}
