package io.datappeal.narwhal.job.config

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.datappeal.narwhal.job.factory.CatalogFactory
import io.datappeal.narwhal.job.models.TargetTable
import io.datappeal.narwhal.job.models.hive.CatalogConfig
import org.apache.iceberg.hive.HiveCatalog
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ConfigReaderTest {

    @Test
    fun `should throw Exception for missing path file`() {
        assertThrows<Exception> { ConfigReader.config("narwhal.yml") }
    }

    @Test
    fun `should read correct config file`() {
        val jobConfig = ConfigReader.config("./src/test/resources/config_test.yml")
        assertThat(jobConfig.catalog).isEqualTo(
            CatalogConfig(
                name = "name",
                uris = "uris",
                warehouse = "warehouse",
                access_key = "access_key",
                secret_key = "secret_key"
            )
        )
        assertThat(jobConfig.tables).isEqualTo(
            listOf(
                TargetTable(
                    name = "test_table",
                    schema = "test_schema",
                )
            )
        )
    }

    @Test
    fun `should create correct hiveConfig`() {
        val hiveCatalog = CatalogFactory.createCatalog(
            CatalogConfig(
                name = "name",
                uris = "uris",
                warehouse = "warehouse",
                access_key = "access_key",
                secret_key = "secret_key"
            )
        )
        assertThat(hiveCatalog)
            .isInstanceOf(HiveCatalog::class)
    }
}
