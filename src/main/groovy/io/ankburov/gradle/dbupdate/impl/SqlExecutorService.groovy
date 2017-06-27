package io.ankburov.gradle.dbupdate.impl

interface SqlExecutorService {

    /**
     * Execute script
     *
     * @param script
     */
    void executeScript(File script)

    /**
     * Get selection script and execute it to get already applied scripts (their ids)
     *
     * @param selectionScript
     * @return List of applied script ids
     */
    List<String> getAppliedScriptIds(File selectionScript)
}