package io.datappeal.narwhal.job.factory

import io.datappeal.core.analysis.rewritefiles.FileSkewPolicy
import io.datappeal.core.analysis.rewritefiles.RewritePartitionPolicy
import io.datappeal.narwhal.job.models.enumerations.PolicyEnum

class PolicyFactory {
    companion object {
        fun getPolicy(policyEnum: PolicyEnum, policyConfig: Map<String, Any>): RewritePartitionPolicy {
            return when (policyEnum) {
                PolicyEnum.FILE_SKEW -> {
                    val targetFileSizeBytes = policyConfig.getOrElse("target_file_size_bytes") { null }

                    FileSkewPolicy(
                        if (targetFileSizeBytes != null) (targetFileSizeBytes as Int).toLong() else null,
                        policyConfig["file_size_skew_threshold"] as Double
                    )
                }
            }
        }
    }
}
