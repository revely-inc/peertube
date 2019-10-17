package co.revely.peertube.api.peertube.response

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
data class AboutInstanceResponse(
		val instance: Instance
)
{
	data class Instance(
		val name: String,
		val shortDescription: String,
		val description: String,
		val terms: String
	)
}
