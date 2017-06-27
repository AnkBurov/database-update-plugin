package io.ankburov.gradle.dbupdate.impl

import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@CompileStatic
class SqlExecutorServiceImpl implements SqlExecutorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlExecutorServiceImpl.class);

    private SqlExecutorAdapter sqlAdapter
    private final Map<String, String> dbHolder
    private final String queryDelimiter

    SqlExecutorServiceImpl(Map<String, String> dbHolder, String queryDelimiter) {
        this.dbHolder = dbHolder
        this.queryDelimiter = queryDelimiter
        this.sqlAdapter = new SqlExecutorAdapter(this.dbHolder)
    }

    @Override
    void executeScript(File script) {
        SqlScriptParser.loadSqlQueriesFromFile(script, queryDelimiter).each {sqlQuery ->
            try {
                sqlAdapter.executeQuery(sqlQuery)
            } catch (Exception e) {
                LOGGER.error("Exception happened during executing query $sqlQuery in script $script", e)
                def rollbackScript = new File(script.getParent(), "_$script.name")
                executeRollbackScript(rollbackScript)
                throw e
            }
        }
        LOGGER.info("Script $script has been successfully applied")
    }

    private void executeRollbackScript(File rollbackScript) {
        LOGGER.info("Executing rollback script $rollbackScript")
        SqlScriptParser.loadSqlQueriesFromFile(rollbackScript, queryDelimiter).each {rollbackQuery ->
            try {
                sqlAdapter.executeQuery(rollbackQuery)
            } catch (Exception rollbackException) {
                LOGGER.error("Could not execute rollback query $rollbackQuery in rollback script $rollbackScript", rollbackException)
                throw rollbackException
            }
        }
    }

    @Override
    List<String> getAppliedScriptIds(File selectionScript) {
        LOGGER.info("Trying to load selection query from file $selectionScript")
        def selectionQueries = SqlScriptParser.loadSqlQueriesFromFile(selectionScript, queryDelimiter)
        if (selectionQueries.size() != 1) {
            LOGGER.error("Wrong number of selection queries")
            throw new IllegalStateException("Wrong number of selection queries. Expected: 1, actual: " + selectionQueries.size())
        }
        def selectionQuery = selectionQueries.get(0)
        try {
            def appliedScriptIds = sqlAdapter.selectFirstColumn(selectionQuery)
            return appliedScriptIds
        } catch (Exception e) {
            LOGGER.error("Exception of type $e happened during selecting applied script ids", e)
            throw e
        }
    }
}
