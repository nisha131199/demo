package com.example.demo.user.database

import java.sql.Connection
import java.sql.DriverManager

class MySQLite {
    fun connection(): Connection{
        Class.forName("org.sqlite.JDBC")
        return DriverManager.getConnection("jdbc:sqlite:F:/Database/dbms_testing.db")
    }

    fun connectionFile(): Connection{
        Class.forName("org.sqlite.JDBC")
        return DriverManager.getConnection("jdbc:sqlite:F:/Database/file.db")
    }
}