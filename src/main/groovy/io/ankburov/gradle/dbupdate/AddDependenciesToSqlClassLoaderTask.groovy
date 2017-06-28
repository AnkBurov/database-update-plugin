package io.ankburov.gradle.dbupdate

import groovy.sql.Sql
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction

/**
 * Internal task that modifies Sql class classloader and adds dependencies from buildscript classpath dependencies to Sql.class
 * classloader.
 *
 * Other way Sql (and DriverManager) class will not see buildscript classpath dependencies and will fail with message
 * that so suitable drivers found for the url
 */
class AddDependenciesToSqlClassLoaderTask extends DefaultTask {

    @Classpath
    @InputFiles
    FileCollection classpath

    @TaskAction
    void addBuildScriptDependencies() {
        def sqlClassLoader = (URLClassLoader) Sql.classLoader
        for (File file : classpath.getFiles()) {
            if (!sqlClassLoader.getURLs().contains(file.toURI().toURL())) {
                sqlClassLoader.addURL(file.toURI().toURL())
            }
        }

    }
}
