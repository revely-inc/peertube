package co.revely.peertube.api.peertube.response

import androidx.annotation.Keep

/**
 * Created at 30/10/2019
 *
 * @author rbenjami
 */
@Keep
data class DataList<T>(
		val total: Int,
		val data: List<T>
)
