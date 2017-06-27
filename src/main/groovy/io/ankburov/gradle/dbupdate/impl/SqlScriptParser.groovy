package io.ankburov.gradle.dbupdate.impl

import groovy.transform.CompileStatic
import org.gradle.internal.impldep.org.apache.commons.lang.StringUtils

@CompileStatic
class SqlScriptParser {

    static List<String> loadSqlQueriesFromFile(File script, String queryDelimiter) {
        if (!script.exists()) {
            throw new IllegalStateException("Script with path $script does not exist")
        }
        List<String> sqlQueries = []
        script.getText().split(queryDelimiter).each {String line ->
            if (StringUtils.isNotBlank(line)) {
                String newLine = line.replaceAll("[\r\n]", " ")
                if (org.apache.commons.lang3.StringUtils.isNotBlank(newLine)) {
                    sqlQueries.add(newLine.trim().replaceAll(" +", " "))
                }
            }
        }
        if (sqlQueries.isEmpty()) {
            throw new IllegalStateException("Script file $script does not contain any valid sql scripts")
        }
        sqlQueries
    }
}