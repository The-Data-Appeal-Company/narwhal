package io.datappeal.narwhal.job.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.datappeal.narwhal.job.models.JobConfig
import java.nio.file.Files
import java.nio.file.Paths

class ConfigReader {

    companion object {
        fun config(pathFile: String): JobConfig {
            val path = Paths.get(pathFile)
            val mapper = ObjectMapper(YAMLFactory()).apply {
                registerModule(KotlinModule.Builder().build())
            }
            try {
                return Files.newBufferedReader(path).use {
                    mapper.readValue(it, JobConfig::class.java)
                }
            } catch (exception: Exception) {
                throw Exception(exception.message)
            }
        }
    }
}
