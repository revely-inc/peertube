package co.revely.peertube.api.dao

import androidx.annotation.Keep
import co.revely.peertube.utils.Rate

/**
 * Created at 2019-10-17
 *
 * @author rbenjami
 */
@Keep
data class RateDao(
		val videoId: String? = null,
		@Rate val rating: String? = null
): Dao()