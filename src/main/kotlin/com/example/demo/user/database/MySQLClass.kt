package com.example.demo.user.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.jvm.Throws

class MySQLClass {
    //database>data source>mysql>advance>enableTLSProtocol TLSv1,TLSv1.1,TLSv1.2,TLSv1.3
    @Throws(SQLException::class)
    fun getSQLConnection(): Connection {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance()
            ClassLoader.getSystemClassLoader()
        }catch (e: Exception){
            println(e.message.toString())
        } finally {
            return DriverManager.getConnection("jdbc:mysql://mysql-database-1.cf7tlvfbvdxr.us-east-2.rds.amazonaws.com:3306/user_data", "admin123", "admin123")
        }
    }
}