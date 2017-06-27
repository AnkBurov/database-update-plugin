package io.ankburov.gradle.dbupdate.impl

import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@CompileStatic
class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    static void createInitialFiles(File scriptsPath, String lstFileName, String initialScriptFileName, String selectionScriptFileName) {
        createIfNotExists(scriptsPath, true, "ScriptPath $scriptsPath directory created")
        if (!scriptsPath.directory) {
            throw new IllegalArgumentException("scriptPath with value $scriptsPath is not a directory")
        }
        def lstFile = new File(scriptsPath, lstFileName)
        createIfNotExists(lstFile, false, "LstFile $lstFile created")

        def initialScriptFile = new File(scriptsPath, initialScriptFileName)
        createIfNotExists(initialScriptFile, false, "InitialScriptFile $initialScriptFile created")

        def dataDirectory = new File(scriptsPath, "data")
        createIfNotExists(dataDirectory, true, "Data directory $dataDirectory created")

        def selectionScript = new File(scriptsPath, selectionScriptFileName)
        createIfNotExists(selectionScript, false, "Applied scripts selection script $selectionScript created")
    }

    private static void createIfNotExists(File file, boolean isDirectory, GString logText) {
        if (!file.exists()) {
            isDirectory ? file.mkdir() : file.createNewFile()
            LOGGER.info(logText)
        }
    }
}
