package io.ankburov.gradle.dbupdate

import io.ankburov.gradle.dbupdate.ext.CredentialsExtension
import io.ankburov.gradle.dbupdate.ext.DbUpdateExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyBasePlugin

class DbUpdatePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.apply(GroovyBasePlugin)

        createPluginExtension(project)
        createCreateFilesTask(project)
        createAddDependenciesToSqlTask(project)
        createApplyInitialScriptTask(project)
        createUpdateSchemaTask(project)
    }

    private void createPluginExtension(Project project) {
        project.extensions.create("dbUpdate", DbUpdateExtension.class)
        project.extensions.dbUpdate.extensions.create( 'credentials', CredentialsExtension )
    }

    private CreateFilesTask createCreateFilesTask(Project project) {
        project.tasks.create("createFiles", CreateFilesTask) {
            group = "DbUpdate"
            description = "Create upgrade.lst file and initial script"

            project.afterEvaluate {
                scriptsPath = project.dbUpdate.scriptsPath
                lstFileName = project.dbUpdate.lstFileName
                initialScriptFileName = project.dbUpdate.initialScriptFileName
                appliedScriptsSelectionScriptFileName = project.dbUpdate.appliedScriptsSelectionScriptFileName
            }
        }
    }

    private void createAddDependenciesToSqlTask(Project project) {
        def task = project.tasks.create("addDependenciesToSqlClassLoader", AddDependenciesToSqlClassLoaderTask)
        task.classpath = project.buildscript.configurations.classpath
        task.outputs.upToDateWhen {false}
    }

    private ApplyInitialScriptTask createApplyInitialScriptTask(Project project) {
        def task = project.tasks.create("applyInitialScript", ApplyInitialScriptTask) {
            it.dependsOn("addDependenciesToSqlClassLoader")
            group = "DbUpdate"
            description = "Apply initial script from the initial file in order to initialize database before incremental updating"

            project.afterEvaluate {
                scriptsPath = project.dbUpdate.scriptsPath
                initialScriptFileName = project.dbUpdate.initialScriptFileName
                queryDelimiter = project.dbUpdate.queryDelimiter
                url = project.dbUpdate.credentials.url
                user = project.dbUpdate.credentials.user
                password = project.dbUpdate.credentials.password
                driverName = project.dbUpdate.credentials.driverName
            }
        }

        return task
    }

    private UpdateSchemaTask createUpdateSchemaTask(Project project) {
        project.tasks.create("updateSchema", UpdateSchemaTask) {
            it.dependsOn("addDependenciesToSqlClassLoader")
            group = "DbUpdate"
            description = "Update specified schema with scripts defined in lst file"

            project.afterEvaluate {
                scriptsPath = project.dbUpdate.scriptsPath
                lstFileName = project.dbUpdate.lstFileName
                initialScriptFileName = project.dbUpdate.initialScriptFileName
                appliedScriptsSelectionScriptFileName = project.dbUpdate.appliedScriptsSelectionScriptFileName
                scriptDirDelimiter = project.dbUpdate.scriptDirDelimiter
                scriptNameDelimiter = project.dbUpdate.scriptNameDelimiter
                encoding = project.dbUpdate.encoding
                queryDelimiter = project.dbUpdate.queryDelimiter
                url = project.dbUpdate.credentials.url
                user = project.dbUpdate.credentials.user
                password = project.dbUpdate.credentials.password
                driverName = project.dbUpdate.credentials.driverName
            }
        }
    }
}
