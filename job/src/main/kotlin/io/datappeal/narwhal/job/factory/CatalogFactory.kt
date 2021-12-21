package io.datappeal.narwhal.job.factory

import io.datappeal.narwhal.job.models.hive.CatalogConfig
import org.apache.hadoop.conf.Configuration
import org.apache.iceberg.catalog.Catalog
import org.apache.iceberg.hive.HiveCatalog

class CatalogFactory {
    companion object {
        // todo create implementation agnostic configuration
        // at the moment only the hive catalog is supported
        fun createCatalog(catalogConf: CatalogConfig): Catalog {
            val catalog = HiveCatalog()
            val conf = Configuration()
            conf.set("fs.s3a.access.key", catalogConf.access_key)
            conf.set("fs.s3a.secret.key", catalogConf.secret_key)
            conf.set("hive.metastore.uris", catalogConf.uris)
            conf.set("hive.metastore.warehouse.dir", catalogConf.warehouse)

            catalog.conf = conf
            catalog.initialize(catalogConf.name, emptyMap())
            return catalog
        }
    }
}
