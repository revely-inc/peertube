package co.revely.peertube.api.peertube.response

/**
 * Created at 30/10/2019
 *
 * @author rbenjami
 */
data class DataList<T>(
		val total: Int,
		val data: List<T>
)
