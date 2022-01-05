package com.example.demo.user.database

import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties

class MySqlDb {

    fun getConnection(): Connection {
        lateinit var conn: Connection
        val properties = Properties()
        properties["user"] = "root"
        properties["password"] = "xyz"

        val dbname = "userData"
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance()

            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306$dbname",
                    properties)
        } catch (e: Exception) {
            println(e.message)
        }
        return conn
    }
}