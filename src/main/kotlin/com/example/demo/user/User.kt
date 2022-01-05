package com.example.demo.user

import com.example.demo.user.database.MySqlDb
import java.sql.ResultSet
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.collections.HashMap

class User() {
    private val res = HashMap<String, Any>()
    private val db = MySqlDb()

    private lateinit var query: String
    private val connection = db.getConnection()
    private val statement = connection.createStatement() as Statement

    fun register(user: UserModel): HashMap<String, Any> {
        try {
            val msg = isReadyToRegister(user)
            if (msg == "new user") {
                val firstName = user.firstName.trim()
                val middleName = user.middleName.trim()
                val lastName = user.lastName.trim()
                val email = user.email.trim()
                val phoneNumber = user.phoneNumber.trim()
                val userName = user.userName.trim()
                val password = user.password.trim()
                query = "insert into user (first_name,middle_name,last_name,email,phone_number,user_name,password) " +
                        "value('$firstName','$middleName','$lastName','$email','$phoneNumber','$userName','$password')"

                if (statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS) > 0) {
                    val data = HashMap<String, Any>()
                    val key=statement.generatedKeys
                    if (key.next())
                        data["id"] = key.getInt(1)

                    res["status"] = true
                    res["message"] = "user have been added successfully."
                    res["data"] = data
                }
            } else {
                res["status"] = false
                res["message"] = msg
            }
        } catch (e: Exception) {
            res["status"] = false
            res["message"] = "ERROR:${e.message}"
        }
        return res
    }

    fun update(user: UserModel): HashMap<String, Any> {
        try {
            val timeStamp= SimpleDateFormat("YYYY-MM-DD hh:mm:ss").format(Date())
            query = "update user set first_name='${user.firstName}',middle_name='${user.middleName}'," +
                    "last_name='${user.lastName}',email='${user.email}',phone_number='${user.phoneNumber}'," +
                    "user_name='${user.userName}',password='${user.password}',updated_at='$timeStamp' " +
                    "where id=${user.id}"

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

    fun login(user:LoginModel): HashMap<String, Any> {
        try {
            query = "select * from user where user_name='${user.userName}' and password='${user.password}'"
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

    private fun isReadyToRegister(user: UserModel): String {
        var msg = "new user"
        val mysql = MySqlDb()
        val conn = mysql.getConnection()
        val stmt = conn.createStatement() as Statement
        var result: ResultSet

        if (user.password.isNotBlank() && user.confirmPassword.isNotBlank() && user.password != user.confirmPassword)
            msg = "Those passwords didn't matched"

        if (user.email.isNotBlank()) {
            query = "select email from user where email='${user.email}'"
            result = stmt.executeQuery(query)
            if (result.next())
                msg = "This email is already registered here"
        }
        if (user.phoneNumber.isNotBlank()) {
            query = "select phone_number from user where phone_number='${user.phoneNumber}'"
            result = stmt.executeQuery(query)
            if (result.next())
                msg = "This number is already taken"
        }
        if (user.userName.isNotBlank()) {
            query = "select user_name from user where user_name='${user.userName}'"
            result = stmt.executeQuery(query)
            if (result.next())
                msg = "username already taken"
        }
        return msg
    }
}

class LoginModel(var userName:String, var password:String)

class UserModel(
        var id: Int,
        var firstName: String,
        var middleName: String,
        var lastName: String,
        var email: String,
        var phoneNumber: String,
        var userName: String,
        var password: String,
        var confirmPassword: String)