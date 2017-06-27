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

