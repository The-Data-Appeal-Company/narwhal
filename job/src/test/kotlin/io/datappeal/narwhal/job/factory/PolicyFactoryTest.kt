package io.datappeal.narwhal.job.factory

import assertk.assertThat
import assertk.assertions.isInstanceOf
import io.datappeal.core.analysis.rewritefiles.FileSkewPolicy
import io.datappeal.narwhal.job.models.enumerations.PolicyEnum
import org.junit.jupiter.api.Test

class PolicyFactoryTest {

    @Test
    fun `should generate correct instance of RewritePartitionPolicy with all parameter`() {
        val policy = PolicyFactory.getPolicy(
            PolicyEnum.FILE_SKEW,
        )
        assertThat(policy).isInstanceOf(FileSkewPolicy::class.java)
    }

    @Test
    fun `should generate correct instance of RewritePartitionPolicy with required parameter`() {
        val policy = PolicyFactory.getPolicy(
            PolicyEnum.FILE_SKEW,
        )

        assertThat(policy).isInstanceOf(FileSkewPolicy::class.java)
    }
}
