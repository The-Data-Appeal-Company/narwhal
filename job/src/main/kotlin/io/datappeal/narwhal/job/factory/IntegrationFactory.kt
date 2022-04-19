package io.datappeal.narwhal.job.factory

import io.datappeal.core.analysis.result.RewriteFilesResultConsumer
import io.datappeal.narwhal.integration.aws.SQSNotifier
import io.datappeal.narwhal.job.models.enumerations.IntegrationEnum
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.internal.http.loader.DefaultSdkHttpClientBuilder
import software.amazon.awssdk.http.apache.ApacheHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient

class IntegrationFactory {
    companion object {
        fun getPolicy(
            integrationEnum: IntegrationEnum,
            integrationParams: Map<String, Any>
        ): RewriteFilesResultConsumer {
            return when (integrationEnum) {
                IntegrationEnum.SQS -> {
                    val batchSize = integrationParams["batch_size"] as Int
                    if (batchSize <= 0 || batchSize > 10) {
                        throw IllegalArgumentException("Invalid value for batch size: $batchSize. Min: 0, Max: 10")
                    }

                    SQSNotifier(
                        integrationParams["queue"] as String,
                        batchSize,
                        SqsClient.builder()
                            .httpClient(ApacheHttpClient.builder().build())
                            .credentialsProvider(
                                StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(
                                        integrationParams["access_key"] as String,
                                        integrationParams["secret_key"] as String
                                    )
                                )
                            )
                            .region(Region.of(integrationParams["region"] as String))
                            .build()
                    )
                }
            }
        }
    }
}
