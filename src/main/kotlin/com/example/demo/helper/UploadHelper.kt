package com.example.demo.helper

import org.springframework.core.io.ClassPathResource
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class UploadHelper(private val str: String) {
    private lateinit var url: ClassPathResource

    fun save(file: MultipartFile): Boolean{
        try {
            url = ClassPathResource("static/${str}")
            if(!url.isFile) {
                val path = ClassPathResource("static")
                File(path.uri.path+"/${str}").mkdirs()
            }
            Files.copy(file.inputStream,
                    Paths.get(url.file.absolutePath + File.separator + file.originalFilename),
                    StandardCopyOption.REPLACE_EXISTING)
            return true

        }catch (e: Exception){
            println(e.message)
        }
        return false
    }
}