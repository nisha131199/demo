package com.example.demo.user.database

import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties

class MySqlDb {
    private lateinit var conn: Connection

    fun getConnection(): Connection {
        val properties = Properties()
        properties["user"] = "u"
        properties["password"] = "p"

        val dbname = "user"
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance()

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/$dbname", properties)
        } catch (e: Exception) {
            println(e.message)
        }
        return conn
    }
}