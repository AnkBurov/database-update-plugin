package io.ankburov.gradle.dbupdate.impl

import groovy.transform.CompileStatic
import org.apache.commons.lang3.StringUtils

@CompileStatic
class ScriptListingFileAdapter {

    private static final String COMMENT_SYMBOL = "#"
    final String encoding

    ScriptListingFileAdapter(String encoding) {
        this.encoding = encoding
    }

    List<String> getScripts(File scriptsFile) {
        if (!scriptsFile.exists()) {
            throw new IllegalArgumentException("Script listing file $scriptsFile is not exists")
        }
        def result = new ArrayList()
        scriptsFile.eachLine(encoding, {
            line -> if (StringUtils.isNotBlank(line) && !line.startsWith(COMMENT_SYMBOL)) result.add(line)
        })
        result
    }
}
