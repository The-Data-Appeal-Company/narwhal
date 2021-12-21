package io.datappeal.narwhal.job.models.integration

data class IntegrationConfig(
    val type: String,
    val config: Map<String, Any>,
)
