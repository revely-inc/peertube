package co.revely.peertube.api.peertube.response

/**
 * Created at 2019-10-21
 *
 * @author rbenjami
 */
data class User(
		val id: Long?,
		val username: String?,
		val email: String?,
		val displayNSFW: Boolean?,
		val autoPlayVideo: Boolean?,
		val role: Long?,
		val roleLabel: String?,
		val videoQuota: Long?,
		val videoQuotaDaily: Long?,
		val createdAt: String?,
		var account: Account?,
		var videoChannels: List<Channel>?
)
