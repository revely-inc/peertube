package co.revely.peertube.api.peertube.response

import co.revely.peertube.utils.Rate

/**
 * Created at 2019-10-17
 *
 * @author rbenjami
 */
data class RateResponse(
		val id: String,
		@Rate val rating: String
)