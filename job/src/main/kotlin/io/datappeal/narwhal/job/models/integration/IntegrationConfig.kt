package io.datappeal.narwhal.job.models.integration

data class IntegrationConfig(
    val type: String,
    val params: Map<String, Any>,
)
