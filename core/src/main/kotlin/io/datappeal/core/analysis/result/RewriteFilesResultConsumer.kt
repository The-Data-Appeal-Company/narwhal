package io.datappeal.core.analysis.result

import io.datappeal.core.analysis.AnalyzeTablePartitionResult
import java.util.function.Consumer

interface RewriteFilesResultConsumer : Consumer<List<AnalyzeTablePartitionResult>>
