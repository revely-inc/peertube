package co.revely.peertube.api.dao

import androidx.annotation.Keep

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
@Keep
data class AboutInstanceDao(
		val instance: Instance
)
{
	@Keep
	data class Instance(
		val name: String,
		val shortDescription: String,
		val description: String,
		val terms: String
	)
}
