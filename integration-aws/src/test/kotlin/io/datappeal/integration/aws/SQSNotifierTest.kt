package io.datappeal.integration.aws

import io.datappeal.core.analysis.AnalyzeTablePartitionResult
import io.datappeal.narwhal.integration.aws.SQSNotifier
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequest
import software.amazon.awssdk.services.sqs.model.SendMessageBatchResponse

class SQSNotifierTest {

    @Test
    fun `should send messages correctly without throwing an exception`() {
        val amazonClient = mockk<SqsClient>()
        val response = mockk<SendMessageBatchResponse>()
        every { amazonClient.sendMessageBatch(any<SendMessageBatchRequest>()) } returns response

        val sqsNotifier = SQSNotifier(
            "queue",
            10,
            amazonClient
        )

        assertDoesNotThrow {
            sqsNotifier.accept(
                listOf(
                    AnalyzeTablePartitionResult(
                        "schema",
                        "test",
                        listOf(
                            Pair("pkey_0", "test"),
                            Pair("pkey_1", 1)
                        )

                    )
                )
            )
        }
    }
}
