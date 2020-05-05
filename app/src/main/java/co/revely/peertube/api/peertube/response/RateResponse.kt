package co.revely.peertube.api.peertube.response

import androidx.annotation.Keep
import co.revely.peertube.utils.Rate

/**
 * Created at 2019-10-17
 *
 * @author rbenjami
 */
@Keep
data class RateResponse(
		val videoId: String,
		@Rate val rating: String
)