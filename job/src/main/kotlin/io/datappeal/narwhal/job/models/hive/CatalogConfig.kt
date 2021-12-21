package io.datappeal.narwhal.job.models.hive

data class CatalogConfig(
    val name: String,
    val uris: String,
    val warehouse: String,
    val access_key: String,
    val secret_key: String
)
