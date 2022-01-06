package com.example.demo.controller

import com.example.demo.user.*
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicLong

@RestController
class Controller{
    private val temp = "Hello, %s!"
    private val incr = AtomicLong()

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

    @PostMapping("/user/signIn")
    fun login(@RequestBody credentials: LoginModel): HashMap<String, Any> {
        return User().login(credentials)
    }

    @DeleteMapping("/user/remove")
    fun delete(@RequestParam(value = "uid", defaultValue = "")uid:Int): HashMap<String, Any> {
        return User().delete(uid)
    }

    @PostMapping("/user/create_profile")
    fun createProfile(@RequestBody image: String): String{
        return "uploaded"
    }

    @PostMapping("/user/update_profile")
    fun updateProfile(@RequestBody image: String): String{
        return "updated"
    }
}