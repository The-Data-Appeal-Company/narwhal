package io.datappeal.narwhal.job.config

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.datappeal.narwhal.job.factory.CatalogFactory
import io.datappeal.narwhal.job.models.TargetTable
import io.datappeal.narwhal.job.models.catalog.CatalogConfig
import org.apache.iceberg.aws.glue.GlueCatalog
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
                type = "hive",
                config = mapOf(
                    "warehouse" to "warehouse",
                    "access_key" to "access_key",
                    "secret_key" to "secret_key",
                ),
            )
        )
        assertThat(jobConfig.tables).isEqualTo(
            listOf(
                TargetTable(
                    name = "test_table",
                    schema = "test_schema",
                    params = mapOf(
                        "target_file_size_bytes" to 2,
                        "file_size_skew_threshold" to 1,
                    )
                )
            )
        )
    }

    @Test
    fun `should create correct hive catalog`() {
        val hiveCatalog = CatalogFactory.createCatalog(
            CatalogConfig(
                name = "name",
                type = "hive",
                config = mapOf(
                    "warehouse" to "file:///tmp",
                ),
            )
        )
        assertThat(hiveCatalog)
            .isInstanceOf(HiveCatalog::class)
    }


    @Test
    fun `should create correct glue catalog`() {
        val glueCatalog = CatalogFactory.createCatalog(
            CatalogConfig(
                name = "name",
                type = "glue",
                config = mapOf(
                    "warehouse" to "file:///tmp",
                ),
            )
        )
        assertThat(glueCatalog)
            .isInstanceOf(GlueCatalog::class)
    }

}
