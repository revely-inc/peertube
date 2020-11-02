package co.revely.peertube.api.dao

import androidx.annotation.Keep

/**
 * Created at 30/10/2020
 *
 * @author rbenjami
 */
@Keep
data class RedundancyDao(
		val baseUrl: String? = null
): Dao()