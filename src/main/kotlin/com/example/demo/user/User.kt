package com.example.demo.user

import com.example.demo.helper.UploadHelper
import com.example.demo.user.database.MySQLClass
import com.example.demo.user.database.MySQLite
import org.springframework.core.io.ClassPathResource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.File
import java.sql.ResultSet
import java.sql.Statement
import kotlin.collections.HashMap

class User {
    private val res = HashMap<String, Any>()
    private val db = MySQLClass()

    private fun isReadyToRegister(data: DataModel): String {
        val connection = db.getSQLConnection()
        val statement = connection.createStatement() as Statement

        val msg = "new user"

        if (data.password.isNotBlank() && data.confirm_password.isNotBlank() && data.password != data.confirm_password)
            return "Those passwords didn't matched"

        if (data.email.isNotBlank()) {
            val e = statement.executeQuery("select email from user where email='${data.email}'")
            if (e.next())
                return "This email is already registered here"
        }

        if (data.user_name.isNotBlank()) {
            val e = statement.executeQuery("select user_name from user where user_name='${data.user_name}'")
            if (e.next())
                return "username already taken"
        }
        connection.close()
        return msg
    }

    fun register(data: DataModel): HashMap<String, Any> {
        val connection = db.getSQLConnection()
        val statement = connection.createStatement() as Statement

        try {
            val msg = isReadyToRegister(data)

            if (msg == "new user") {

                if (statement.executeUpdate("insert into user (first_name, middle_name, last_name, email, phone_number, user_name, password) values('${data.first_name?.trim()}','${data.middle_name?.trim()}','${data.last_name?.trim()}','${data.email.trim()}','${data.phone_number?.trim()}','${data.user_name.trim()}','${data.password.trim()}')")>0) {

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
        finally {
            connection.close()
        }
        return res
    }

    fun update(data: DataModel): HashMap<String, Any> {
        val connection = db.getSQLConnection()
        val statement = connection.createStatement() as Statement

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
        }finally {
            connection.close()
        }
        return res
    }

    fun delete(uid:Int): HashMap<String, Any> {
        val connection = db.getSQLConnection()
        val statement = connection.createStatement() as Statement

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
        }finally {
            connection.close()
        }
        return res
    }

    fun login(login:LoginModel): HashMap<String, Any> {
        val connection = db.getSQLConnection()
        val statement = connection.createStatement() as Statement

        try {
            val result=statement.executeQuery("select * from user where user_name='${login.user_name}' and password='${login.password}'")
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
        }finally {
            connection.close()
        }
        return res
    }

    fun getUsers(): HashMap<String, Any>{
        try {
            val connection = db.getSQLConnection()
            val statement = connection.createStatement() as Statement

            var firstName = ""
            var lastName = ""

            val result = statement.executeQuery("select * from user")
            var count = 1

            while (result.next()){
                firstName = result.getString(2)
                lastName = result.getString(4)

                res["user ${count++}"] = UserData(result.getInt(1), "$firstName $lastName",result.getString(7),result.getString(5))
            }
            connection.close()
        }catch (e: Exception){
            res["message"] = e.message.toString()
        }
        return res
    }

    fun uploadFile(file: MultipartFile): HashMap<String,Any>{
        val connection = db.getSQLConnection()
        val statement = connection.createStatement() as Statement

        val response: HashMap<String,Any> = HashMap()
        response["content-type"] = file.contentType.toString()

        if(file.isEmpty) {
            response["status"] = false
            response["message"] = "No file found!"
        }

        else {
            try {
                val d = file.contentType?.split("/")
                if(d != null)
                    if(UploadHelper(d[0]).save(file)){
                        val v = ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path(ClassPathResource("${d[0]}/").path)
                                .path(file.originalFilename.toString())
                                .toUriString()

                        val result = statement.executeQuery("select url from files where url='${v}'")
                        if(!result.next()){
                            statement.execute("insert into files values('${file.originalFilename}','${v}')")
                        }

                        response[d[0]] = v
                        response["status"] = true
                        response["message"] = "${d[0]} uploaded successfully!"

                    }
            }catch (e: Exception){
                response["status"] = false
                response["message"] = e.message.toString()
            }finally {
                connection.close()
            }
        }
        return response
    }

    fun getAllFiles(): HashMap<String,Any>{
        val connection = db.getSQLConnection()
        val statement = connection.createStatement() as Statement

        val response: HashMap<String,Any> = HashMap()
        val result = statement.executeQuery("select * from files")

        var count = 0

        while(result.next()){
            response["${++count}"] = FileData(result.getString(1), result.getString(2))
        }
        connection.close()
        return response
    }
}

class LoginModel(var user_name:String, var password:String)

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

data class UserData(
        val id: Int,
        val name: String,
        val user_name: String,
        val email: String
)

data class FileData(
        val name: String,
        val url: String
)