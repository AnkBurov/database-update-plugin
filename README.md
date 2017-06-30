# Gradle database update plugin #

[![Build Status](https://travis-ci.org/AnkBurov/database-update-plugin.svg?branch=master)](https://travis-ci.org/AnkBurov/database-update-plugin) [![Coverage Status](https://coveralls.io/repos/github/AnkBurov/database-update-plugin/badge.svg?branch=master)](https://coveralls.io/github/AnkBurov/database-update-plugin?branch=master)

Simple alternative to Liquibase and Flyway projects for incrementing relational database updates. Just pure SQL scripts and some Groovy code.

Just import plugin, configure dbUpdate DSL extension, create initial files, apply initial script and run updateSchema. 
The plugin will find out which scripts are not applied to specified instance of database and apply them in the order script 
relative names specified in listing file.

## Tasks ##

* createFiles - creates listing file and other initial files and directories needed for plugin
* applyInitialScript - applies initial script to the database. For example creates DBUPDATE table, containing list of applied scripts
* updateSchema - incrementally updates database by executing not applied to database scripts

## Applying the plugin ##

**build.gradle**
```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "gradle.plugin.io.ankburov.gradle:dbupdate:1.0"
        classpath("mysql:mysql-connector-java:5.1.41") // JDBC driver of the database
    }
}
apply plugin: "io.ankburov.gradle.dbupdateplugin"

dbUpdate {
    scriptsPath = file('scripts')
    credentials {
        url = 'jdbc:mysql://localhost:3306/neww'
        user = 'root'
        password = 'root'
        driverName = 'com.mysql.jdbc.Driver'
    }
}
```
All available DSL extension properties can be found in DbUpdateExtension class.

## Example of usage ##

See in module example. 