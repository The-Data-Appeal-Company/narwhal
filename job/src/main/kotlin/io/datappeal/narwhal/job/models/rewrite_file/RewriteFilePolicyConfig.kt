package io.datappeal.narwhal.job.models.rewrite_file

data class RewriteFilePolicyConfig(
    val type: String,
    val config: Map<String, Any>
)
