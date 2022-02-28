package io.datappeal.narwhal.job.main

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import io.datappeal.core.Analyzer
import io.datappeal.narwhal.job.config.ConfigReader
import io.datappeal.narwhal.job.factory.CatalogFactory
import io.datappeal.narwhal.job.factory.IntegrationFactory
import io.datappeal.narwhal.job.factory.PolicyFactory
import io.datappeal.narwhal.job.launcher.Launcher
import io.datappeal.narwhal.job.models.TargetTable
import io.datappeal.narwhal.job.models.enumerations.IntegrationEnum
import io.datappeal.narwhal.job.models.enumerations.PolicyEnum

class JobCmd : CliktCommand() {
    private val config: String by argument(help = "configuration file path")

    override fun run() {
        val jobConfig = ConfigReader.config(config)
        val catalog = CatalogFactory.createCatalog(jobConfig.catalog)

        val rewritePartitionPolicy = PolicyFactory.getPolicy(
            PolicyEnum.valueOf(jobConfig.rewrite_files.policy.type),
        )

        val integration = IntegrationFactory.getPolicy(
            IntegrationEnum.valueOf(jobConfig.rewrite_files.integration.type),
            jobConfig.rewrite_files.integration.params
        )

        val tablesWithConfig = jobConfig.tables.map {
            TargetTable(it.name, it.schema, jobConfig.rewrite_files.policy.params + it.params.orEmpty())
        }

    val analyzer = Analyzer(rewritePartitionPolicy)
        Launcher(
            catalog = catalog,
            analyzer = analyzer,
            integration = integration
        ).launch(tablesWithConfig)
    }
}
