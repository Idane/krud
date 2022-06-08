package studio.crud.crudframework.web.ro

data class ManyCrudResult<SuccessfulResult, FailedResult>(
    val successful: Set<SuccessfulResult>,
    val failed: List<ManyFailedReason<FailedResult>>
)