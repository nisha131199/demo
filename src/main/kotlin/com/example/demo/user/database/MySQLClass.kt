package com.example.demo.user.database

import java.sql.Connection
import java.sql.DriverManager

class MySQLClass {
    //database>data source>mysql>advance>enableTLSProtocol TLSv1,TLSv1.1,TLSv1.2,TLSv1.3
    fun getSQLConnection(): Connection {
        Class.forName("com.mysql.cj.jdbc.Driver")
        return DriverManager.getConnection("jdbc:mysql://mysql-database-1.cf7tlvfbvdxr.us-east-2.rds.amazonaws.com:3306/user_data", "admin123", "admin123")
    }
}