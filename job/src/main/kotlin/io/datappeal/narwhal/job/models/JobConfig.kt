package io.datappeal.narwhal.job.models

import io.datappeal.narwhal.job.models.hive.CatalogConfig
import io.datappeal.narwhal.job.models.rewrite_file.RewriteFilesConfig

data class TargetTable(
    val name: String,
    val schema: String,
)

data class JobConfig(
    val catalog: CatalogConfig,
    val rewrite_files: RewriteFilesConfig,
    val tables: List<TargetTable>,
)