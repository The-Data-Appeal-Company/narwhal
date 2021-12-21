package io.datappeal.core.analysis

import org.apache.iceberg.DataFile
import org.apache.iceberg.Table

data class AnalyzedTablePartition(
    val table: Table,
    val partition: List<Pair<String, Any?>>,
    val files: List<DataFile>
)
