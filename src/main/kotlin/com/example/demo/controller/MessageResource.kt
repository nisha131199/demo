package com.example.demo.controller

class MessageResource (private var id: Long, private var content: String) {
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