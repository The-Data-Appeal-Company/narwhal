package io.datappeal.core.analysis

data class AnalyzeTablePartitionResult(
    val schema: String,
    val table: String,
    val partition: List<Pair<String, Any?>>
)
