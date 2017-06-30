package io.ankburov.gradle.dbupdate.impl

class ServiceInstanceFactory {

    static SqlExecutorService getSqlExecutorService(String url, String user, String password, String driverName, String queryDelimiter) {
        try {
            return new SqlExecutorServiceImpl([url: url, user: user, password: password, driverName: driverName], queryDelimiter)
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize SqlExecutorService. Exception is $e and exception message is ${e.message}", e)
        }
    }

    static ScriptListingFileService getScriptListingFileService(Character scriptDirDelimiter, String scriptNameDelimiter, String encoding) {
        return new ScriptListingFileServiceImpl(scriptDirDelimiter, scriptNameDelimiter, encoding)
    }
}
