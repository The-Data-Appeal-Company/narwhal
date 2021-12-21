package io.datappeal.core.analysis.rewritefiles

import io.datappeal.core.analysis.AnalyzedTablePartition
import kotlin.math.ceil

class FileSkewPolicy(
    private val targetFileSizeBytes: Long? = null,
    private val fileSizeSkewThreshold: Double = 0.2
) : RewritePartitionPolicy {

    override fun rewrite(partition: AnalyzedTablePartition): Boolean {
        val targetFileSizeBytes = targetFileSize(partition)

        val partitionSizeBytes = partition.files.sumOf { it.fileSizeInBytes() }

        val targetFileCount: Long = ceil((partitionSizeBytes.toDouble() / targetFileSizeBytes.toDouble())).toLong()
        val currentFilesCount = partition.files.size

        val tolerance: Double = currentFilesCount * this.fileSizeSkewThreshold

        val minFiles = targetFileCount - tolerance
        val maxFiles = targetFileCount + tolerance
        return currentFilesCount < minFiles || currentFilesCount > maxFiles
    }

    private fun targetFileSize(partition: AnalyzedTablePartition): Long {
        if (this.targetFileSizeBytes != null) {
            return this.targetFileSizeBytes
        }

        // https://iceberg.apache.org/#configuration/
        return partition.table.properties()
            .getOrDefault("write.target-file-size-bytes", "536870912")
            .toLong()
    }
}
