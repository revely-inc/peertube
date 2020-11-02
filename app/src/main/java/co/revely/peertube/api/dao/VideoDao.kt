package co.revely.peertube.api.dao

import androidx.annotation.Keep
import co.revely.peertube.utils.Rate
import java.util.*

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
@Keep
data class VideoDao(
		val id: String,
		val uuid: String? = null,
		val createdAt: Date? = null,
		val publishedAt: Date? = null,
		val updatedAt: Date? = null,
		val originallyPublishedAt: Date? = null,
		val category: LabelDao? = null,
		val licence: LabelDao? = null,
		val language: LabelDao? = null,
		val privacy: LabelDao? = null,
		val description: String? = null,
		val duration: Int? = null,
		val isLocal: Boolean? = null,
		val name: String? = null,
		val thumbnailPath: String? = null,
		val previewPath: String? = null,
		val embedPath: String? = null,
		val views: Long? = null,
		val likes: Long? = null,
		val dislikes: Long? = null,
		val nsfw: Boolean? = null,
		val waitTranscoding: Boolean? = null,
		val state: LabelDao? = null,
		val scheduledUpdate: ScheduledUpdateDao? = null,
		val blacklisted: Boolean = false,
		val blacklistedReason: String? = null,
		val account: AccountDao? = null,
		val channel: ChannelDao? = null,
		val userHistory: UserHistoryDao? = null,
		val descriptionPath: String? = null,
		val support: String? = null,
		val tags: List<String>? = null,
		val files: List<FileDao>? = null,
		val commentsEnabled: Boolean? = null,
		val downloadEnabled: Boolean? = null,
		val trackerUrls: List<String>? = null,
		val streamingPlaylists: List<StreamingPlaylistDao>? = null,

		//TODO: Remove rating in VideoDao
		@Rate
		var rating: String? = null
): Dao()