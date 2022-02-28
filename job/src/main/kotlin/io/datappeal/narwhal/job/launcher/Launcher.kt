package io.datappeal.narwhal.job.launcher

import io.datappeal.core.Analyzer
import io.datappeal.core.analysis.AnalyzeTablePartitionResult
import io.datappeal.core.analysis.result.RewriteFilesResultConsumer
import io.datappeal.narwhal.job.models.TargetTable
import org.apache.iceberg.catalog.Catalog
import org.apache.iceberg.catalog.TableIdentifier

class Launcher(
    private val catalog: Catalog,
    private val analyzer: Analyzer,
    private val integration: RewriteFilesResultConsumer
) {
    fun launch(tables: List<TargetTable>) {
        for (table in tables) {
            val analyzeRewriteFiles = this.analyzer.analyzeRewriteFiles(
                table = this.catalog.loadTable(TableIdentifier.of(table.schema, table.name)),
                params = table.params.orEmpty()
            )
            this.integration.accept(
                analyzeRewriteFiles
                    .map {
                        val tableDetails = it.table.name().split(".")
                        AnalyzeTablePartitionResult(
                            tableDetails[1],
                            tableDetails[2],
                            it.partition
                        )
                    }
            )
        }
    }
}
