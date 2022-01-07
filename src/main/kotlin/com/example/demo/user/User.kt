package com.example.demo.user

import com.example.demo.user.database.MySQLite
import java.sql.ResultSet
import java.sql.Statement
import kotlin.collections.HashMap

class User {
    private val res = HashMap<String, Any>()
    private val db = MySQLite()
    private val connection = db.connection()
    private val statement = connection.createStatement() as Statement

    private lateinit var query: String

    private fun isReadyToRegister(data: DataModel): String {
        var msg = "new user"

        var result: ResultSet

        if (data.password.isNotBlank() && data.confirm_password.isNotBlank() && data.password != data.confirm_password)
            msg = "Those passwords didn't matched"

        if (data.email.isNotBlank()) {
            query = "select email from user where email='${data.email}'"
            result = statement.executeQuery(query)
            if (result.next())
                msg = "This email is already registered here"
        }

        if (data.user_name.isNotBlank()) {
            query = "select user_name from user where user_name='${data.user_name}'"
            result = statement.executeQuery(query)
            if (result.next())
                msg = "username already taken"
        }
        return msg
    }

    fun register(data: DataModel): HashMap<String, Any> {
        try {
            val msg = isReadyToRegister(data)

            if (msg.equals("new user")) {
                val firstName = data.first_name?.trim()
                val middleName = data.middle_name?.trim()
                val lastName = data.last_name?.trim()
                val email = data.email.trim()
                val phoneNumber = data.phone_number?.trim()
                val userName = data.user_name.trim()
                val password = data.password.trim()
                query = "insert into user (first_name, middle_name, last_name, email, phone_number, user_name, password) " +
                        "values('$firstName','$middleName','$lastName'," +
                        "'$email','$phoneNumber','$userName','$password')"

                if (statement.execute(query)) {
                    res["status"] = true
                    res["message"] = "user have been added successfully."
                }else{
                    res["status"] = true
                    res["message"] = "user not added."
                }

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
        try {
            //val timeStamp= SimpleDateFormat("YYYY-MM-DD hh:mm:ss").format(Date())
            query = "update user set first_name='${data.first_name}',middle_name='${data.middle_name}'," +
                    "last_name='${data.last_name}',email='${data.email}',phone_number='${data.phone_number}'," +
                    "user_name='${data.user_name}',password='${data.password}'"+
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
        try {
            query = "select * from user where user_name='${login.userName}' and password='${login.password}'"
            val result=statement.executeQuery(query)
            if (result.next()) {
                val data=HashMap<String,Any>()
                data["id"]=result.getString(1)
                /*data["firstName"]=result.getString(2)
                data["middleName"]=result.getString(3)
                data["lastName"]=result.getString(4)*/
                val firstName=result.getString(2)
                val middleName=result.getString(3)
                val lastName=result.getString(4)
                data["Name"]="$firstName $middleName $lastName"
                data["email"]=result.getString(5)
                data["phone_number"]=result.getString(6)
                data["user_name"]=result.getString(7)
                data["password"]=result.getString(8)
                res["status"] = true
                res["message"] = "Successfully SignIn"
                res["data"]=data
            }else{
                res["status"] = false
                res["message"] = "Invalid Username or Password"
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
        var first_name: String?,
        var middle_name: String?,
        var last_name: String?,
        var email: String,
        var phone_number: String?,
        var user_name: String,
        var password: String,
        var confirm_password: String)