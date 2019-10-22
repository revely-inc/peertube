package co.revely.peertube.api.peertube.response

import java.util.*

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
data class Video(
		val id: String,
		val uuid: String?,
		val name: String?,
		val category: Label?,
		val licence: Label?,
		val language: Label?,
		val privacy: Label?,
		val state: Label?,
		val nsfw: Boolean?,
		val description: String?,
		val isLocal: Boolean?,
		val views: Long?,
		val duration: Long?,
		val likes: Long?,
		val dislikes: Long?,
		val thumbnailPath: String?,
		val previewPath: String?,
		val embedPath: String?,
		val createdAt: Date?,
		val publishedAt: Date?,
		val updatedAt: Date?,
		val blacklisted: Boolean,
		val blacklistedReason: String?,
		val support: String?,
		val descriptionPath: String?,
		val tags: List<String>?,
		val commentsEnabled: Boolean?,
		val waitTranscoding: Boolean?,
		val account: Account?,
		val channel: Channel?,
		val files: List<File>?
)