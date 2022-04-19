package io.datappeal.narwhal.job.models.catalog

data class CatalogConfig(
    val type: String,
    val name: String,
    val config: Map<String, String>,
)
