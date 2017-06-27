package io.ankburov.gradle.dbupdate.impl

import org.junit.Test

import static io.ankburov.gradle.dbupdate.TestConst.DRIVER_NAME
import static io.ankburov.gradle.dbupdate.TestConst.ENCODING
import static io.ankburov.gradle.dbupdate.TestConst.PASSWORD
import static io.ankburov.gradle.dbupdate.TestConst.QUERY_DELIMITER
import static io.ankburov.gradle.dbupdate.TestConst.SCRIPT_NAME_DELIMITER
import static io.ankburov.gradle.dbupdate.TestConst.TEST_URL
import static io.ankburov.gradle.dbupdate.TestConst.USER
import static io.ankburov.gradle.dbupdate.TestConst.SCRIPT_DIR_DELIMITER
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

class ServiceInstanceFactoryTest {

    @Test
    void getSqlExecutorService() throws Exception {
        def service = ServiceInstanceFactory.getSqlExecutorService(TEST_URL, USER, PASSWORD, DRIVER_NAME, QUERY_DELIMITER)
        assertNotNull service
        assertTrue service instanceof SqlExecutorService
    }

    @Test(expected = RuntimeException)
    void getSqlExecutorServiceWrongDriver() throws Exception {
        ServiceInstanceFactory.getSqlExecutorService(TEST_URL, USER, PASSWORD, DRIVER_NAME + 1, QUERY_DELIMITER)
    }

    @Test
    void getScriptListingFileService() throws Exception {
        def service = ServiceInstanceFactory.getScriptListingFileService(SCRIPT_DIR_DELIMITER, SCRIPT_NAME_DELIMITER, ENCODING)
        assertNotNull service
        assertTrue service instanceof ScriptListingFileService
    }
}
