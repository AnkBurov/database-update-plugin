package io.ankburov.gradle.dbupdate.impl

import groovy.transform.CompileStatic

@CompileStatic
interface ScriptListingFileService {

    /**
     * Get scripts map from lst file containing list of script names
     *
     * @param scriptsFile lst file
     * @return Map where key is script's identifier and value is script's name (for example data/1_create_user.sql)
     */
    Map<String, String> getScriptsMap(File scriptsFile)
}