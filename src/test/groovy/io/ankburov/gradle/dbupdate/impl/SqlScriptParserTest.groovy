package io.ankburov.gradle.dbupdate.impl

import groovy.transform.CompileStatic
import org.junit.Test

import static io.ankburov.gradle.dbupdate.TestConst.EMPTY_SCRIPT
import static io.ankburov.gradle.dbupdate.TestConst.FIRST_SCRIPT
import static io.ankburov.gradle.dbupdate.TestConst.QUERY_DELIMITER
import static io.ankburov.gradle.dbupdate.TestUtils.getScript
import static org.junit.Assert.assertEquals

@CompileStatic
class SqlScriptParserTest {

    private static final String FIRST_QUERY = "CREATE TABLE TEST_TABLE (ID INT PRIMARY KEY, NAME VARCHAR(255))"
    private static final String SECOND_QUERY = "INSERT INTO TEST_TABLE (ID, NAME) VALUES (1, 'test_value')"

    @Test
    void loadSqlQueriesFromFile() throws Exception {
        def script = getScript(FIRST_SCRIPT)
        def sqlQueries = SqlScriptParser.loadSqlQueriesFromFile(script, QUERY_DELIMITER)
        assertEquals(2, sqlQueries.size())
        assertEquals(FIRST_QUERY, sqlQueries.get(0))
        assertEquals(SECOND_QUERY, sqlQueries.get(1))
    }

    @Test(expected = IllegalStateException)
    void loadSqlQueriesFromEmptyFile() throws Exception {
        def script = getScript(EMPTY_SCRIPT)

        SqlScriptParser.loadSqlQueriesFromFile(script, QUERY_DELIMITER)
    }

    @Test(expected = IllegalStateException)
    void loadSqlQueriesFromNotExistentFile() throws Exception {
        def script = new File("non-existent.sql")

        SqlScriptParser.loadSqlQueriesFromFile(script, QUERY_DELIMITER)
    }
}
