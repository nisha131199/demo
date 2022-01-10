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

    @PostMapping("/user/modify")
    fun update(@RequestBody data: DataModel): HashMap<String, Any> {
        return User().update(data)
    }

    @PostMapping("/user/login")
    fun login(@RequestBody credentials: LoginModel): HashMap<String, Any> {
        return User().login(credentials)
    }

    @DeleteMapping("/user/remove")
    fun delete(@RequestParam(value = "uid", defaultValue = "")uid:Int): HashMap<String, Any> {
        return User().delete(uid)
    }

    @PostMapping("/uploadFile")
    fun upload(@RequestParam("file") file: MultipartFile): HashMap<String,Any>{
        response = HashMap()
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
                        response[d[0]] = ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path(ClassPathResource("${d[0]}/").path)
                                .path(file.originalFilename.toString())
                                .toUriString()

                        response["status"] = true
                        response["message"] = "${d[0]} uploaded successfully!"

                    }
            }catch (e: Exception){
                response["status"] = false
                response["message"] = e.message.toString()
            }
        }

        return response
    }
}