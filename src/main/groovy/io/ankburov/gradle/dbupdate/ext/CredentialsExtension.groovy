package io.ankburov.gradle.dbupdate.ext

import groovy.transform.CompileStatic

@CompileStatic
class CredentialsExtension {

    /**
     * JDBC url
     */
    String url

    /**
     * User login
     */
    String user

    /**
     * User password
     */
    String password

    /**
     * JDBC driver name of the using database
     * Example: org.h2.Driver
     */
    String driverName
}
