package co.revely.peertube.api.dao

import androidx.annotation.Keep
import java.util.*

@Keep
data class AccountDao(
		val id: String? = null,
		val uuid: String? = null,
		val url: String? = null,
		val name: String? = null,
		val host: String? = null,
		val hostRedundancyAllowed: Boolean? = null,
		val followingCount: Int? = null,
		val followersCount: Int? = null,
		val avatar: AvatarDao? = null,
		val createdAt: Date? = null,
		val updatedAt: Date? = null,
		val displayName: String? = null,
		val description: String? = null,
		val userId: Int? = null
): Dao()