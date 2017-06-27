package io.ankburov.gradle.dbupdate.ext

import groovy.transform.CompileStatic

@CompileStatic
class DbUpdateExtension {

    /**
     * Relative path to directory with lst file
     */
    File scriptsPath

    /**
     * Name of script listing file containing script names.
     * Order of scripts in file affects the script execution order
     */
    String lstFileName = "upgrade.lst"

    /**
     * Name of initial script containing DDL with creation of DBUPDATE (for example) table
     */
    String initialScriptFileName = "initial.sql"

    /**
     * Name of selection script containing selection query to DBUPDATE
     * ResultSet of select must have first column as script id
     */
    String appliedScriptsSelectionScriptFileName = "appliedScriptsSelection.sql"

    /**
     * Query delimiter in script files demarcating several queries
     */
    String queryDelimiter = ";"

    /**
     * Symbol of directory delimiter of script names in lst file
     * data/1_create_user.sql
     */
    Character scriptDirDelimiter = '/'

    /**
     * Symbol of script name delimiter of script names in lst file.
     * First element in script name is its id
     * 1_create_user.sql
     */
    String scriptNameDelimiter = "_"

    /**
     * Encoding
     */
    String encoding = "utf-8"
}
