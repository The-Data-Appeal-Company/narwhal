package io.datappeal.core.analysis.rewritefiles

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import io.datappeal.core.analysis.AnalyzedTablePartition
import io.mockk.every
import io.mockk.mockk
import org.apache.iceberg.DataFile
import org.apache.iceberg.Table
import org.junit.jupiter.api.Test

class FileSkewPolicyTest {

    @Test
    fun `should not rewrite file if the partition size is below target size 😆`() {
        val policy = FileSkewPolicy()

        val table = mockk<Table>()
        every { table.properties() } returns HashMap<String, String>()

        val partitionFiles = listOf(
            icebergFile(sizeBytes = 1024 * 1024 * 100L)
        )

        val partitionKey = listOf(
            Pair("pkey_0", "test"), Pair("pkey_1", 1)
        )

        val partition = AnalyzedTablePartition(table, partitionKey, partitionFiles)
        assertThat(policy.rewrite(partition, emptyMap())).isFalse()
    }

    @Test
    fun `should rewrite files when the target file size for table is below actual file size`() {
        val policy = FileSkewPolicy()

        val table = mockk<Table>()
        every { table.properties() } returns HashMap<String, String>()

        val partitionFiles = listOf(
            icebergFile(sizeBytes = 1024 * 1024 * 100L)
        )

        val partitionKey = listOf(
            Pair("pkey_0", "test"), Pair("pkey_1", 1)
        )

        val partition = AnalyzedTablePartition(table, partitionKey, partitionFiles)
        assertThat(
            policy.rewrite(
                partition,
                mapOf(
                    "target_file_size_bytes" to 100
                )
            )
        ).isTrue()
    }

    private fun icebergFile(sizeBytes: Long = 1024 * 1024L): DataFile {
        val file = mockk<DataFile>()
        every { file.fileSizeInBytes() } returns sizeBytes
        return file
    }
}
