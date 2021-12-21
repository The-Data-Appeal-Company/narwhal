package io.datappeal.core

import io.datappeal.core.analysis.AnalyzedTablePartition
import io.datappeal.core.analysis.rewritefiles.RewritePartitionPolicy
import org.apache.iceberg.DataFile
import org.apache.iceberg.Schema
import org.apache.iceberg.StructLike
import org.apache.iceberg.Table
import org.apache.iceberg.types.Types
import org.apache.iceberg.util.DateTimeUtil
import java.nio.ByteBuffer

class Analyzer(private val rewritePartitionPolicy: RewritePartitionPolicy) {

    fun analyzeRewriteFiles(table: Table): List<AnalyzedTablePartition> {
        val tablePartitions = listPartitions(table)
        return tablePartitions
            .map { AnalyzedTablePartition(table, it.key, it.value) }
            .filter { rewritePartitionPolicy.rewrite(it) }
    }

    private fun listPartitions(table: Table): Map<List<Pair<String, Any?>>, List<DataFile>> {
        val planFiles = table.newScan()
            .ignoreResiduals()
            .planFiles()

        val partitionsWithFiles: MutableMap<List<Pair<String, Any?>>, MutableList<DataFile>> = HashMap()

        for (it in planFiles.map { it.file() }) {
            val partition: MutableList<Pair<String, Any?>> = ArrayList()
            val structLike: StructLike = it.partition()
            val spec = table.specs()[it.specId()]
            for (i in 0 until structLike.size()) {
                val name = spec!!.fields()[i].name()
                partition.add(
                    Pair(name, getValue(table.schema(), name, i, structLike))
                )
            }

            val files = partitionsWithFiles[partition] ?: ArrayList()
            files.add(it)
            partitionsWithFiles[partition] = files
        }

        return partitionsWithFiles
    }

    private fun getValue(schema: Schema, name: String, index: Int, structLike: StructLike): Any? {
        return when (schema.findType(name)) {
            is Types.DateType -> {
                DateTimeUtil.dateFromDays(structLike.get(index, Any::class.java) as Int)
            }
            is Types.TimeType -> {
                DateTimeUtil.timeFromMicros(structLike.get(index, Long::class.java))
            }
            is Types.TimestampType -> {
                DateTimeUtil.timestampFromMicros(structLike.get(index, Long::class.java))
            }
            is Types.BinaryType -> {
                String(structLike.get(index, ByteBuffer::class.java).array())
            }
            else -> {
                structLike.get(index, Any::class.java)
            }
        }
    }
}
