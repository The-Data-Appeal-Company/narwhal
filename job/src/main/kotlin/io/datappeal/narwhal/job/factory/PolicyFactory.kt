package io.datappeal.narwhal.job.factory

import io.datappeal.core.analysis.rewritefiles.FileSkewPolicy
import io.datappeal.core.analysis.rewritefiles.RewritePartitionPolicy
import io.datappeal.narwhal.job.models.enumerations.PolicyEnum

class PolicyFactory {
    companion object {
        fun getPolicy(policyEnum: PolicyEnum): RewritePartitionPolicy {
            return when (policyEnum) {
                PolicyEnum.FILE_SKEW -> {
                    FileSkewPolicy()
                }
            }
        }
    }
}
