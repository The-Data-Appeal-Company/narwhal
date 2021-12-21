package io.datappeal.narwhal.integration.aws.models

data class SqsMessage(
    val schema: String,
    val table: String,
    val partition: Map<String, Any?>,
)
