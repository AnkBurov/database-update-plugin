package io.ankburov.gradle.dbupdate.impl

import groovy.transform.CompileStatic
import io.ankburov.gradle.dbupdate.TestConst
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

@CompileStatic
class SqlExecutorAdapterTest {

    private SqlExecutorAdapter adapter

    @Before
    void setUp() throws Exception {
        this.adapter = new SqlExecutorAdapter([url     : TestConst.TEST_URL, user: TestConst.USER,
                                               password: TestConst.PASSWORD, driverName: TestConst.DRIVER_NAME])
    }

    @Test
    void executeQuery() throws Exception {
        createTestDatabase(1000)
    }

    @Test
    void selectFirstColumn() throws Exception {
        def expectedIds = ["1", "2", "3", "4", "5"]
        def tableName = createTestDatabase(expectedIds.size())

        def result = adapter.selectFirstColumn("SELECT ID FROM $tableName")

        assertEquals(expectedIds, result)
    }

    /**
     * Create test database
     * @param rowsToCreate how many rows have to be inserted in test table
     * @return name of the test table
     */
    private String createTestDatabase(int rowsToCreate) {
        def tableName = "TEST_TABLE"
        adapter.executeQuery("CREATE SCHEMA TEST_SCHEMA")
        adapter.executeQuery("CREATE TABLE $tableName(ID INT PRIMARY KEY, NAME VARCHAR(255))")
        for (int i = 1; i <= rowsToCreate; i++) {
            adapter.executeQuery("INSERT INTO TEST_TABLE (ID, NAME) VALUES ($i, 'test_name$i')")
        }
        return tableName
    }
}
