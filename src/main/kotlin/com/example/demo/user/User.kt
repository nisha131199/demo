package com.example.demo.user

import com.example.demo.user.database.MySqlDb
import java.sql.ResultSet
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.collections.HashMap

class User {
    private val res = HashMap<String, Any>()
    private val db = MySqlDb()

    private lateinit var query: String

    private fun isReadyToRegister(data: DataModel): String {
        var msg = "new user"

        val connection = db.getConnection()
        val statement = connection.createStatement() as Statement
        var result: ResultSet

        if (data.password.isNotBlank() && data.confirmPassword.isNotBlank() && data.password != data.confirmPassword)
            msg = "Those passwords didn't matched"

        if (data.email.isNotBlank()) {
            query = "select email from user where email='${data.email}'"
            result = statement.executeQuery(query)
            if (result.next())
                msg = "This email is already registered here"
        }

        if (data.userName.isNotBlank()) {
            query = "select user_name from user where user_name='${data.userName}'"
            result = statement.executeQuery(query)
            if (result.next())
                msg = "username already taken"
        }
        return msg
    }

    fun register(data: DataModel): HashMap<String, Any> {
        val connection = db.getConnection()
        val statement = connection.createStatement() as Statement

        try {
            val msg = isReadyToRegister(data)

            if (msg.equals("new user")) {
                val firstName = data.firstName?.trim()
                val middleName = data.middleName?.trim()
                val lastName = data.lastName?.trim()
                val email = data.email.trim()
                val phoneNumber = data.phoneNumber?.trim()
                val userName = data.userName.trim()
                val password = data.password.trim()
                query = "insert into user (first_name,middle_name,last_name,email,phone_number,user_name,password) " +
                        "value('$firstName','$middleName','$lastName','$email','$phoneNumber','$userName','$password')"

                if (statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS) > 0) {
                    val d = HashMap<String, Any>()
                    val key=statement.generatedKeys
                    if (key.next())
                        d["id"] = key.getInt(1)

                    res["status"] = true
                    res["message"] = "user have been added successfully."
                    res["data"] = d
                }

                res["status"] = true
                res["message"] = "user have been added successfully."

            } else {
                res["status"] = false
                res["message"] = msg
            }
        } catch (e: Exception) {
            res["status"] = false
            res["message"] = e.message.toString()
        }
        return res
    }

    fun update(data: DataModel): HashMap<String, Any> {
        val connection = db.getConnection()
        val statement = connection.createStatement() as Statement

        try {
            val timeStamp= SimpleDateFormat("YYYY-MM-DD hh:mm:ss").format(Date())
            query = "update user set first_name='${data.firstName}',middle_name='${data.middleName}'," +
                    "last_name='${data.lastName}',email='${data.email}',phone_number='${data.phoneNumber}'," +
                    "user_name='${data.userName}',password='${data.password}',updated_at='$timeStamp' " +
                    "where id=${data.id}"

            if (statement.executeUpdate(query) > 0) {
                res["status"] = true
                res["message"] = "details updated."
            }
        } catch (e: Exception) {
            res["status"] = false
            res["message"] = "Error:${e.message}"
        }
        return res
    }

    fun delete(uid:Int): HashMap<String, Any> {
        val connection = db.getConnection()
        val statement = connection.createStatement() as Statement

        try {
            query = "delete from user where id=${uid}"
            if (statement.executeUpdate(query) > 0) {
                res["status"] = true
                res["message"] = "deleted Successfully."
            }
        } catch (e: Exception) {
            res["status"] = false
            res["message"] = "Error:${e.message}"
        }
        return res
    }

    fun login(login:LoginModel): HashMap<String, Any> {
        val connection = db.getConnection()
        val statement = connection.createStatement() as Statement

        try {
            query = "select * from user where user_name='${login.userName}' and password='${login.password}'"
            val result=statement.executeQuery(query)
            if (result.next()) {
                val data=HashMap<String,Any>()
                data["id"]=result.getString(1)
                data["firstName"]=result.getString(2)
                data["middleName"]=result.getString(3)
                data["lastName"]=result.getString(4)
                val firstName=result.getString(2)
                val middleName=result.getString(3)
                val lastName=result.getString(4)
                data["fullName"]="$firstName $middleName $lastName"
                data["email"]=result.getString(5)
                data["phoneNumber"]=result.getString(6)
                data["userName"]=result.getString(7)
                data["password"]=result.getString(8)
                res["status"] = true
                res["message"] = "Successfully SignIn"
                res["data"]=data
            }else{
                res["status"] = false
                res["message"] = "Invalid Username and Password"
            }
        } catch (e: Exception) {
            res["status"] = false
            res["message"] = "Error:${e.message}"
        }
        return res
    }
}

class LoginModel(var userName:String, var password:String)

data class DataModel(
        var id: Int?,
        var firstName: String?,
        var middleName: String?,
        var lastName: String?,
        var email: String,
        var phoneNumber: String?,
        var userName: String,
        var password: String,
        var confirmPassword: String)