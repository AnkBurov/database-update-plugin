package io.ankburov.gradle.dbupdate

import groovy.transform.Canonical

/**
 * Holder for shared constants used in tests
 */
@Canonical
class TestConst {
    public static final File SCRIPTS_PATH = new File("testdirectory")
    public static final String DIRECTORY = "/testdirectory"

    public static final String INITIAL_SCRIPT_FILE = "initial.sql"
    public static final String APPLIED_SCRIPTS_SELECTION_SCRIPT = "appliedScriptsSelection.sql"
    public static final String LST_FILE = "upgrade.lst"
    public static final String BLANK_LST_FILE = "blankupgrade.lst"
    public static final String LST_FILE_SAME_IDS = "same_ids_upgrade.lst"
    public static final String APPLIED_WRONG_SCRIPTS = "applied_wrong_script.sql"

    public static final String FIRST_SCRIPT = "data/1_create_db_update.sql"
    public static final String FIRST_SCRIPT_NAME = "1_create_db_update.sql"
    public static final String FIRST_SCRIPT_ROLLBACK_NAME = "_1_create_db_update.sql"
    public static final String SECOND_SCRIPT = "data/2_invalid_script_valid_rollback.sql"
    public static final String THIRD_SCRIPT = "data/3_invalid_script_invalid_rollback.sql"
    public static final String THIRD_ROLLBACK_SCRIPT = "data/_3_invalid_script_invalid_rollback.sql"
    public static final String EMPTY_SCRIPT = 'data/4_empty_script.sql'

    public static final String QUERY_DELIMITER = ";"
    public static final Character SCRIPT_DIR_DELIMITER = '/'
    public static final String SCRIPT_NAME_DELIMITER = "_"
    public static final String ENCODING = "utf-8"

    public static final String TEST_URL = "jdbc:h2:mem:"
    public static final String USER = "sa"
    public static final String PASSWORD = ""
    public static final String DRIVER_NAME = "org.h2.Driver"

    public static final String WRONG_BUILD_FILE_WITH_ILLEGAL_DRIVER = """
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.h2database:h2:1.4.196")
    }
}

plugins {
    id "dbupdateplugin"
}

dbUpdate {
    scriptsPath = file('$SCRIPTS_PATH')
    lstFileName = '$LST_FILE'
    initialScriptFileName = '$INITIAL_SCRIPT_FILE'
    appliedScriptsSelectionScriptFileName = '$APPLIED_SCRIPTS_SELECTION_SCRIPT'
    credentials {
        url = '$TEST_URL'
        user = '$USER'
        password = '$PASSWORD'
        driverName = 'wrongValue'
    }
}
"""
}
