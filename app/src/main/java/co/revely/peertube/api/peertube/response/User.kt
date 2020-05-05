package co.revely.peertube.api.peertube.response

import androidx.annotation.Keep

/**
 * Created at 2019-10-21
 *
 * @author rbenjami
 */
@Keep
data class User(
		val id: Int?,
		val username: String?,
		val email: String?,
		val displayNSFW: Boolean?,
		val autoPlayVideo: Boolean?,
		val role: Int?,
		val roleLabel: String?,
		val videoQuota: Int?,
		val videoQuotaDaily: Int?,
		val createdAt: String?,
		var account: Account?,
		var videoChannels: List<Channel>?
)
