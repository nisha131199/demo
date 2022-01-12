package com.example.demo.controller

import com.example.demo.helper.UploadHelper
import com.example.demo.user.*
import org.springframework.core.io.ClassPathResource
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartRequest
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.concurrent.atomic.AtomicLong
import javax.servlet.annotation.MultipartConfig

@RestController
class Controller{
    private val temp = "Hello, %s!"
    private val incr = AtomicLong()
    private lateinit var response: HashMap<String,Any>

    @GetMapping("/greeting")
    fun msgG(@RequestParam(value = "name", defaultValue = "champions") name: String): MessageResource {
        return MessageResource(incr.incrementAndGet(), String.format(temp, name))
    }

    @PostMapping("/greeting")
    fun msgP(@RequestBody name: Message): MessageResource {
        return MessageResource(incr.incrementAndGet(),name.str)
    }

    @PostMapping("/user/register")
    fun insert(@RequestBody data: DataModel): HashMap<String, Any> {
        return User().register(data)
    }

    @PostMapping("/user/update")
    fun update(@RequestBody data: DataModel): HashMap<String, Any> {
        return User().update(data)
    }

    @PostMapping("/user/login")
    fun login(@RequestBody credentials: LoginModel): HashMap<String, Any> {
        return User().login(credentials)
    }

    @GetMapping("/user/allUsers")
    fun getUsers(): HashMap<String, Any>{
        return User().getUsers()
    }

    @DeleteMapping("/user/delete")
    fun delete(@RequestParam(value = "uid", defaultValue = "")uid:Int): HashMap<String, Any> {
        return User().delete(uid)
    }

    @PostMapping("/uploadFile")
    fun upload(@RequestParam("file") file: MultipartFile): HashMap<String,Any>{
        return User().uploadFile(file)
    }

    @GetMapping("/getAllFile")
    fun get(): HashMap<String, Any>{
        return User().getAllFiles()
    }
}