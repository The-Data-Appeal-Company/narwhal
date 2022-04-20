package io.datappeal.core.analysis.rewritefiles

import io.datappeal.core.analysis.AnalyzedTablePartition
import kotlin.math.ceil

class FileSkewPolicy : RewritePartitionPolicy {

    override fun rewrite(partition: AnalyzedTablePartition, params: Map<String, Any>): Boolean {
        val fileSizeSkewThreshold: Double = params.getOrDefault("file_size_skew_threshold", 0.2) as Double

        val table = partition.table.properties().getOrDefault("write.target-file-size-bytes", "536870912").toLong()
        val tableTargetFileSizeBytes: Long = (params.getOrDefault("target_file_size_bytes", table) as Number).toLong()

        val partitionSizeBytes = partition.files.sumOf { it.fileSizeInBytes() }

        val targetFileCount: Long = ceil((partitionSizeBytes.toDouble() / tableTargetFileSizeBytes.toDouble())).toLong()
        val currentFilesCount = partition.files.size

        val tolerance: Double = currentFilesCount * fileSizeSkewThreshold

        val minFiles = targetFileCount - tolerance
        val maxFiles = targetFileCount + tolerance
        return currentFilesCount < minFiles || currentFilesCount > maxFiles
    }
}
