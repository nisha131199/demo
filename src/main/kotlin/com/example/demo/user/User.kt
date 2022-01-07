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

    private fun isReadyToRegister(data: DataModel): String {
        val msg = "new user"

        if (data.password.isNotBlank() && data.confirm_password.isNotBlank() && data.password != data.confirm_password)
            return "Those passwords didn't matched"

        if (data.email.isNotBlank()) {
            if (statement.execute("select email from user where email='${data.email}'"))
                return "This email is already registered here"
        }

        if (data.user_name.isNotBlank()) {
            val b = statement.execute("select user_name from user where user_name='${data.user_name}'")
            if (b)
                return "username already taken"
        }
        return msg
    }

    fun register(data: DataModel): HashMap<String, Any> {
        try {
            val msg = isReadyToRegister(data)

            if (msg == "new user") {

                if (statement.execute
                        ("insert into user (first_name, middle_name, last_name, email, phone_number, user_name, password) " +
                                "values('${data.first_name?.trim()}','${data.middle_name?.trim()}','${data.last_name?.trim()}'," +
                                "'${data.email.trim()}','${data.phone_number?.trim()}','${data.user_name.trim()}'," +
                                "'${data.password.trim()}')")) {

                    res["status"] = true
                    res["message"] = "user have been added successfully."
                }else{
                    res["status"] = false
                    res["message"] = "error while adding new user."
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
            if (statement.executeUpdate("update user set first_name='${data.first_name}',middle_name='${data.middle_name}'," +
                            "last_name='${data.last_name}',email='${data.email}',phone_number='${data.phone_number}'," +
                            "user_name='${data.user_name}',password='${data.password}'"+
                            "where id=${data.id}")>0) {

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
            if (statement.executeUpdate("delete from user where id=${uid}")>0) {
                res["status"] = true
                res["message"] = "deleted Successfully."
            }else{
                res["status"] = false
                res["message"] = "no data found in this id"
            }
        } catch (e: Exception) {
            res["status"] = false
            res["message"] = "Error:${e.message}"
        }
        return res
    }

    fun login(login:LoginModel): HashMap<String, Any> {
        try {
            val result=statement.executeQuery("select * from user where user_name='${login.userName}' and password='${login.password}'")
            if (result.next()) {
                val data=HashMap<String,Any>()

                val firstName=result.getString(2)
                val middleName=result.getString(3)
                val lastName=result.getString(4)

                data["phone_number"]=result.getString(6)
                data["user_name"]=result.getString(7)
                data["email"]=result.getString(5)
                data["name"]="$firstName $middleName $lastName"
                data["id"]=result.getString(1)
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