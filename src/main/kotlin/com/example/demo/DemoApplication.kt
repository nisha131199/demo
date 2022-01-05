package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicLong

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@RestController
class MessageResource{
    /*@GetMapping
    fun message(): List<Message> =
            listOf(Message("Hey!"), Message("Nisha"), Message("GM"))*/

    private val template = "Hello, %s!"
    private val counter = AtomicLong()

    @GetMapping("/greeting")
    fun msgG(@RequestParam(value = "name", defaultValue = "World") name: String): Greeting {
        return Greeting(counter.incrementAndGet(), String.format(template, name))
    }

    @PostMapping("/greeting")
    fun msgP(@RequestBody name: Message): Greeting {
        return Greeting(counter.incrementAndGet(),name.str)
    }
}

class Greeting (private var id: Long, private var content: String) {
    @JvmName("getId")
    fun getId(): Long {
        return id
    }

    @JvmName("getContent")
    fun getContent(): String {
        return content
    }
}

data class Message(val str: String)