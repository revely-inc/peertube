package co.revely.peertube.api

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
data class ArrayResponse<T>(
	val total: Long,
	val data: List<T>
)
