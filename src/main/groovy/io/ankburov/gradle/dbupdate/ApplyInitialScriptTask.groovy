package io.ankburov.gradle.dbupdate

import groovy.transform.CompileStatic
import io.ankburov.gradle.dbupdate.impl.ServiceInstanceFactory
import io.ankburov.gradle.dbupdate.impl.SqlExecutorService
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Task that applies initial script to the database. For example creates DBUPDATE table
 */
@CompileStatic
class ApplyInitialScriptTask extends DefaultTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplyInitialScriptTask.class);

    @Input
    File scriptsPath

    @Input
    String initialScriptFileName

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

    SqlExecutorService sqlExecutorService

    @TaskAction
    void applyInitialScript() {
        sqlExecutorService = ServiceInstanceFactory.getSqlExecutorService(url, user, password, driverName, queryDelimiter)
        LOGGER.info("Trying to apply initial script")
        def initialScript = new File(scriptsPath.path, initialScriptFileName)
        sqlExecutorService.executeScript(initialScript)
    }
}
