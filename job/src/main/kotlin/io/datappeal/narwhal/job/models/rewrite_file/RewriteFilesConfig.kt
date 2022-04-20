package io.datappeal.narwhal.job.models.rewrite_file

import io.datappeal.narwhal.job.models.integration.IntegrationConfig

data class RewriteFilesConfig(
    val enabled: Boolean,
    val policy: RewriteFilePolicyConfig,
    val integration: IntegrationConfig,
)
