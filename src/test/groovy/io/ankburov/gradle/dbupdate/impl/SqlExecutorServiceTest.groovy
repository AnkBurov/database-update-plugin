package io.ankburov.gradle.dbupdate.impl

import groovy.transform.CompileStatic
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

import static io.ankburov.gradle.dbupdate.TestConst.APPLIED_SCRIPTS_SELECTION_SCRIPT
import static io.ankburov.gradle.dbupdate.TestConst.APPLIED_WRONG_SCRIPTS
import static io.ankburov.gradle.dbupdate.TestConst.FIRST_SCRIPT
import static io.ankburov.gradle.dbupdate.TestConst.QUERY_DELIMITER
import static io.ankburov.gradle.dbupdate.TestConst.SECOND_SCRIPT
import static io.ankburov.gradle.dbupdate.TestConst.THIRD_ROLLBACK_SCRIPT
import static io.ankburov.gradle.dbupdate.TestConst.THIRD_SCRIPT
import static io.ankburov.gradle.dbupdate.TestUtils.getScript
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue
import static org.mockito.Mockito.doThrow
import static org.mockito.Mockito.verify

@CompileStatic
@RunWith(MockitoJUnitRunner)
class SqlExecutorServiceTest {

    @Mock
    private SqlExecutorAdapter sqlExecutorAdapter

    @InjectMocks
    private SqlExecutorService sqlExecutorService = new SqlExecutorServiceImpl([url: "jdbc:h2:mem:", user: "sa", password: "",
                                                                                driverName: "org.h2.Driver"], QUERY_DELIMITER)

    @Test
    void executeScript() throws Exception {
        def script = getScript(FIRST_SCRIPT)

        sqlExecutorService.executeScript(script)

        verify(sqlExecutorAdapter, Mockito.times(2)).executeQuery(Mockito.anyString())
    }

    @Test
    void testRollbackScript() throws Exception {
        def script = getScript(SECOND_SCRIPT)
        doThrow(new RuntimeException())
                .when(sqlExecutorAdapter).executeQuery(script.readLines().get(0).replace(";", ""))

        try {
            sqlExecutorService.executeScript(script)
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException)
        }

        verify(sqlExecutorAdapter, Mockito.times(2)).executeQuery(Mockito.anyString())
    }

    @Test
    void testRollbackScriptError() throws Exception {
        def script = getScript(THIRD_SCRIPT)
        def rollbackScript = getScript(THIRD_ROLLBACK_SCRIPT)
        doThrow(new RuntimeException())
                .when(sqlExecutorAdapter).executeQuery(script.readLines().get(0).replace(";", ""))
        doThrow(new IllegalArgumentException())
                .when(sqlExecutorAdapter).executeQuery(rollbackScript.readLines().get(0).replace(";", ""))

        try {
            sqlExecutorService.executeScript(script)
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException)
        }

        verify(sqlExecutorAdapter, Mockito.times(2)).executeQuery(Mockito.anyString())
    }

    @Test
    void getAppliedScriptIds() throws Exception {
        def script = getScript(APPLIED_SCRIPTS_SELECTION_SCRIPT)
        def expectedIds = ["1"]
        Mockito.doReturn(expectedIds).when(sqlExecutorAdapter).selectFirstColumn(script.readLines().get(0).replace(";", ""))

        def appliedScriptIds = sqlExecutorService.getAppliedScriptIds(script)

        verify(sqlExecutorAdapter, Mockito.times(1)).selectFirstColumn(Mockito.anyString())
        assertEquals(expectedIds, appliedScriptIds)
    }

    @Test(expected = IllegalStateException)
    void getAppliedScriptIdsTooManyQueries() throws Exception {
        def script = getScript(APPLIED_WRONG_SCRIPTS)

        try {
            sqlExecutorService.getAppliedScriptIds(script)
        } finally {
            verify(sqlExecutorAdapter, Mockito.times(0)).selectFirstColumn(Mockito.anyString())
        }
    }

    @Test(expected = RuntimeException)
    void getAppliedScriptIdsSelectionException() throws Exception {
        def script = getScript(APPLIED_SCRIPTS_SELECTION_SCRIPT)
        doThrow(new RuntimeException())
                .when(sqlExecutorAdapter).selectFirstColumn(script.readLines().get(0).replace(";", ""))

        try {
            sqlExecutorService.getAppliedScriptIds(script)
        } finally {
            verify(sqlExecutorAdapter, Mockito.times(1)).selectFirstColumn(Mockito.anyString())
        }
    }
}
