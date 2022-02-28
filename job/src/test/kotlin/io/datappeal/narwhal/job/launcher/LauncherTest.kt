package io.datappeal.narwhal.job.launcher

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.size
import io.datappeal.core.Analyzer
import io.datappeal.core.analysis.AnalyzeTablePartitionResult
import io.datappeal.core.analysis.AnalyzedTablePartition
import io.datappeal.core.analysis.result.RewriteFilesResultConsumer
import io.datappeal.narwhal.job.models.TargetTable
import io.mockk.every
import io.mockk.mockk
import org.apache.iceberg.Table
import org.apache.iceberg.hive.HiveCatalog
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LauncherTest {

    @Test
    fun `should launch the analyzer and retrieve one partition 🥶`() {
        val tableName = "h.schema.test"
        val schema = "schema"
        val partition = listOf(
            Pair("pkey_0", tableName),
            Pair("pkey_1", 1)
        )

        val table = mockk<Table>()
        every { table.name() } returns tableName

        val hiveCatalog = mockk<HiveCatalog>()
        every { hiveCatalog.loadTable(any()) } returns table

        val analyzer = mockk<Analyzer>()
        every { analyzer.analyzeRewriteFiles(any()) } returns mutableListOf(
            AnalyzedTablePartition(
                table,
                partition,
                emptyList()
            )
        )

        val launcher = Launcher(
            hiveCatalog,
            analyzer,
            object : RewriteFilesResultConsumer {
                override fun accept(t: List<AnalyzeTablePartitionResult>) {
                    assertThat(t).isNotEmpty()
                    assertThat(t).size().isEqualTo(1)
                    assertEquals(
                        AnalyzeTablePartitionResult(
                            schema,
                            "test",
                            partition
                        ),
                        t[0]
                    )
                }
            }
        )

        launcher.launch(listOf(TargetTable(schema = "", name = tableName, params = emptyMap())))
    }
}
