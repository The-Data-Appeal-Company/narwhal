package io.datappeal.narwhal.job.models.rewrite_file

data class RewriteFilePolicyConfig(
    val type: String,
    val params: Map<String, Any>
)
