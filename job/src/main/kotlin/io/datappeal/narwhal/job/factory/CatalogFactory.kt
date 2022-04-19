package io.datappeal.narwhal.job.factory

import io.datappeal.narwhal.job.models.catalog.CatalogConfig
import org.apache.hadoop.conf.Configuration
import org.apache.iceberg.aws.glue.GlueCatalog
import org.apache.iceberg.catalog.Catalog
import org.apache.iceberg.hive.HiveCatalog

class CatalogFactory {
    companion object {
        fun createCatalog(catalogConf: CatalogConfig): Catalog {
            when (catalogConf.type) {
                "hive" -> {
                    val catalog = HiveCatalog()
                    val conf = Configuration()

                    for (entry in catalogConf.config) {
                        conf.set(entry.key, entry.value)
                    }

                    catalog.conf = conf
                    catalog.initialize(catalogConf.name, emptyMap())
                    return catalog
                }
                "glue" -> {
                    val catalog = GlueCatalog()
                    catalog.initialize(catalogConf.name, catalogConf.config)
                    return catalog
                }
                else -> {
                    throw IllegalArgumentException("invalid catalog type: " + catalogConf.type)
                }
            }
        }
    }


}
