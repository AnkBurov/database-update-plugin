package io.ankburov.gradle.dbupdate.impl

import groovy.sql.Sql

class SqlExecutorAdapter {

    private Sql sql

    SqlExecutorAdapter(Map<String, String> dbHolder) {
        sql = Sql.newInstance(dbHolder.url, dbHolder.user, dbHolder.password, dbHolder.driverName)
    }

    void executeQuery(String sqlQuery) {
        sql.execute(sqlQuery)
    }

    List<String> selectFirstColumn(String sqlQuery) {
        List<String> result = []
        sql.eachRow(sqlQuery) {
            row -> result.add(String.valueOf(row[0]))
        }
        return result
    }
}
