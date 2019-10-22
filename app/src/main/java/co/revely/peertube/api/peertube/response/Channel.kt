package co.revely.peertube.api.peertube.response

import java.util.*

data class Channel(
		val id: String?,
		val uuid: String?,
		val url: String?,
		val name: String?,
		val host: String?,
		val hostRedundancyAllowed: Boolean?,
		val followingCount: Long?,
		val followersCount: Long?,
		val avatar: Avatar?,
		val createdAt: Date?,
		val updatedAt: Date?,
		val displayName: String?,
		val description: String?,
		val support: String?,
		val isLocal: Boolean?,
		val ownerAccount: User?
)