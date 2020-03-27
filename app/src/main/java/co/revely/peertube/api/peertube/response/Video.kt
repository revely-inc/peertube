package co.revely.peertube.api.peertube.response

import co.revely.peertube.utils.Rate
import java.util.*

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
data class Video(
		val id: String,
		val uuid: String? = null,
		val name: String? = null,
		val category: Label? = null,
		val licence: Label? = null,
		val language: Label? = null,
		val privacy: Label? = null,
		val state: Label? = null,
		val nsfw: Boolean? = null,
		val description: String? = null,
		val isLocal: Boolean? = null,
		val views: Int? = null,
		val duration: Int? = null,
		val likes: Int? = null,
		val dislikes: Int? = null,
		val thumbnailPath: String? = null,
		val previewPath: String? = null,
		val embedPath: String? = null,
		val createdAt: Date? = null,
		val publishedAt: Date? = null,
		val updatedAt: Date? = null,
		val blacklisted: Boolean = false,
		val blacklistedReason: String? = null,
		val support: String? = null,
		val descriptionPath: String? = null,
		val tags: List<String>? = null,
		val commentsEnabled: Boolean? = null,
		val waitTranscoding: Boolean? = null,
		val account: Account? = null,
		val channel: Channel? = null,
		val files: List<File>? = null,

		@Rate
		var rating: String? = null
)