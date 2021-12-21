package io.datappeal.narwhal.job.factory

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.datappeal.narwhal.integration.aws.SQSNotifier
import io.datappeal.narwhal.job.models.enumerations.IntegrationEnum

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class IntegrationFactoryTest {

    @Test
    fun `should generate correct instance of RewriteFilesResultConsumer with all parameter`() {
        val policy = IntegrationFactory.getPolicy(
            IntegrationEnum.SQS,
            mapOf(
                "region" to "eu-west-1",
                "queue" to "queue",
                "batch_size" to 10,
                "access_key" to "access",
                "secret_key" to "secret"
            )
        )
        assertThat(policy).isInstanceOf(SQSNotifier::class.java)
    }

    @Test
    fun `should throw exception for to small batch_size value`() {
        val assertThrows = assertThrows<IllegalArgumentException> {
            IntegrationFactory.getPolicy(
                IntegrationEnum.SQS,
                mapOf(
                    "region" to "eu-west-1",
                    "queue" to "queue",
                    "batch_size" to 0,
                    "access_key" to "access",
                    "secret_key" to "secret"
                )
            )
        }
        assertThat(assertThrows.message).isEqualTo("Invalid value for batch size: 0. Min: 0, Max: 10")
    }

    @Test
    fun `should throw exception for to big batch_size value`() {
        val assertThrows = assertThrows<IllegalArgumentException> {
            IntegrationFactory.getPolicy(
                IntegrationEnum.SQS,
                mapOf(
                    "region" to "eu-west-1",
                    "queue" to "queue",
                    "batch_size" to 11,
                    "access_key" to "access",
                    "secret_key" to "secret"
                )
            )
        }
        assertThat(assertThrows.message).isEqualTo("Invalid value for batch size: 11. Min: 0, Max: 10")
    }
}
