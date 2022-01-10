package com.example.demo.helper

import org.springframework.core.io.ClassPathResource
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class UploadHelper(private val str: String) {   //resources/static
    private lateinit var url: ClassPathResource

    fun save(file: MultipartFile): Boolean{
        try {
            url = ClassPathResource("static/${str}")
            if(url.exists()) {
                Files.copy(file.inputStream,
                        Paths.get(url.file.absolutePath + File.separator + file.originalFilename),
                        StandardCopyOption.REPLACE_EXISTING)
            }
            return true

        }catch (e: Exception){
            println(e.message)
        }
        return false
    }
}