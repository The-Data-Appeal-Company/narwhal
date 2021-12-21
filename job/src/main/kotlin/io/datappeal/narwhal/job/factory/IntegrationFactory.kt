package io.datappeal.narwhal.job.factory

import io.datappeal.core.analysis.result.RewriteFilesResultConsumer
import io.datappeal.narwhal.integration.aws.SQSNotifier
import io.datappeal.narwhal.job.models.enumerations.IntegrationEnum
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient

class IntegrationFactory {
    companion object {
        fun getPolicy(
            integrationEnum: IntegrationEnum,
            integrationConfig: Map<String, Any>
        ): RewriteFilesResultConsumer {
            return when (integrationEnum) {
                IntegrationEnum.SQS -> {
                    val batchSize = integrationConfig["batch_size"] as Int
                    if (batchSize <= 0 || batchSize > 10) {
                        throw IllegalArgumentException("Invalid value for batch size: $batchSize. Min: 0, Max: 10")
                    }

                    SQSNotifier(
                        integrationConfig["queue"] as String,
                        batchSize,
                        SqsClient.builder()
                            .credentialsProvider(
                                StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(
                                        integrationConfig["access_key"] as String,
                                        integrationConfig["secret_key"] as String
                                    )
                                )
                            )
                            .region(Region.of(integrationConfig["region"] as String))
                            .build()
                    )
                }
            }
        }
    }
}
