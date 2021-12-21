package io.datappeal.narwhal.integration.aws

import com.google.gson.Gson
import io.datappeal.core.analysis.AnalyzeTablePartitionResult
import io.datappeal.core.analysis.result.RewriteFilesResultConsumer
import io.datappeal.narwhal.integration.aws.models.SqsMessage
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequest
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequestEntry
import java.util.*

class SQSNotifier(
    private val queue: String,
    private val batchSize: Int,
    private val sqsClient: SqsClient,
    private val gson: Gson = Gson(),
) : RewriteFilesResultConsumer {

    override fun accept(tablePartitions: List<AnalyzeTablePartitionResult>) {
        val chunked = tablePartitions.chunked(this.batchSize)
        for (chunkTablePartition in chunked) {
            val queueEntries: MutableList<SendMessageBatchRequestEntry> = mutableListOf()
            for (tablePartition in chunkTablePartition) {
                queueEntries.add(
                    SendMessageBatchRequestEntry.builder().messageBody(
                        this.gson.toJson(
                            SqsMessage(
                                tablePartition.schema,
                                tablePartition.table,
                                tablePartition.partition.associate { it.first to it.second }
                            )
                        )
                    ).id(UUID.randomUUID().toString()).build()
                )
            }

            val batchRequest: SendMessageBatchRequest =
                SendMessageBatchRequest.builder()
                    .queueUrl(this.queue)
                    .entries(queueEntries)
                    .build()

            this.sqsClient.sendMessageBatch(batchRequest)
        }
    }
}
