package io.ankburov.gradle.dbupdate.impl

import groovy.transform.CompileStatic

@CompileStatic
class ScriptListingFileServiceImpl implements ScriptListingFileService {

    final ScriptListingFileAdapter fileAdapter = new ScriptListingFileAdapter(encoding)
    final Character scriptDirDelimiter
    final String scriptNameDelimiter
    final String encoding

    ScriptListingFileServiceImpl(Character scriptDirDelimiter, String scriptNameDelimiter, String encoding) {
        this.scriptDirDelimiter = scriptDirDelimiter
        this.scriptNameDelimiter = scriptNameDelimiter
        this.encoding = encoding
    }

    @Override
    Map<String, String> getScriptsMap(File scriptsFile) {
        def result = new LinkedHashMap()

        fileAdapter.getScripts(scriptsFile).each { script ->
            def scriptId = script.tokenize(scriptDirDelimiter).last().tokenize(scriptNameDelimiter).first()
            if (result.containsKey(scriptId)) {
                throw new IllegalStateException("Multiple scripts with the same id $scriptId")
            }
            result.put(scriptId, script)
        }
        if (result.isEmpty()) {
            throw new IllegalStateException("Scripts File ${scriptsFile} does not contain any correct scripts")
        }
        result
    }
}
