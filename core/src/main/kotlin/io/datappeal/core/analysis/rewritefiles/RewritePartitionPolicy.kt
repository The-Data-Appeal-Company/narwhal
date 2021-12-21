package io.datappeal.core.analysis.rewritefiles

import io.datappeal.core.analysis.AnalyzedTablePartition

interface RewritePartitionPolicy {
    fun rewrite(partition: AnalyzedTablePartition): Boolean
}
