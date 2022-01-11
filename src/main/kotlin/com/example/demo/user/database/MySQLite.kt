package com.example.demo.user.database

import java.sql.Connection
import java.sql.DriverManager

class MySQLite {
    private lateinit var conn: Connection

    fun connection(): Connection{
        Class.forName("org.sqlite.JDBC")
        conn = DriverManager.getConnection("jdbc:sqlite:F:/Database/dbms_testing.db")
        return conn
    }

    fun connectionFile(): Connection{
        Class.forName("org.sqlite.JDBC")
        conn = DriverManager.getConnection("jdbc:sqlite:F:/Database/file.db")
        return conn
    }
}