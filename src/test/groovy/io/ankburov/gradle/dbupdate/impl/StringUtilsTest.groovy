package io.ankburov.gradle.dbupdate.impl

import org.junit.Test

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

class StringUtilsTest {

    @Test
    void isBlank() {
        assertFalse(StringUtils.isBlank(" s "))
        assertTrue(StringUtils.isBlank("  "))
        assertTrue(StringUtils.isBlank(""))
        assertTrue(StringUtils.isBlank(null))
    }

    @Test
    void isNotBlank() {
        assertTrue(StringUtils.isNotBlank(" s "))
        assertFalse(StringUtils.isNotBlank("  "))
        assertFalse(StringUtils.isNotBlank(""))
        assertFalse(StringUtils.isNotBlank(null))
    }
}
