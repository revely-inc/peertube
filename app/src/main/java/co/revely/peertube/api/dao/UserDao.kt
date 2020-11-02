package co.revely.peertube.api.dao

import androidx.annotation.Keep

/**
 * Created at 2019-10-21
 *
 * @author rbenjami
 */
@Keep
data class UserDao(
		val id: Int? = null,
		val username: String? = null,
		val email: String? = null,
		val displayNSFW: Boolean? = null,
		val autoPlayVideo: Boolean? = null,
		val role: Int? = null,
		val roleLabel: String? = null,
		val videoQuota: Int? = null,
		val videoQuotaDaily: Int? = null,
		val createdAt: String? = null,
		var account: AccountDao? = null,
		var videoChannels: List<ChannelDao>? = null
): Dao()
