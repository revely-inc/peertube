package co.revely.peertube.db.peertube.entity

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import java.util.*

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
@Entity
data class Video(
	@PrimaryKey
	val id: String,
	val uuid: String?,
	val name: String?,
	@Embedded(prefix = "category_") val category: Label?,
	@Embedded(prefix = "licence_") val licence: Label?,
	@Embedded(prefix = "language_") val language: Label?,
	@Embedded(prefix = "privacy_") val privacy: Label?,
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
	@Embedded(prefix = "account_") val account: Account?,
	@Embedded(prefix = "channel_") val channel: Channel?,
	val blacklisted: Boolean,
	val blacklistedReason: String?,
	val support: String?,
	val descriptionPath: String?,
	val tags: List<String>?,
	val commentsEnabled: Boolean?,
	val waitTranscoding: Boolean?,
	@Embedded(prefix = "state_") val state: Label?,
	val files: List<File>?
)
{
	data class File(
		@Embedded(prefix = "resolution_") val resolution: Label?,
		val magnetUri: String?,
		val size: Long?,
		val fps: Long?,
		val torrentUrl: String?,
		val torrentDownloadUrl: String?,
		val fileUrl: String?,
		val fileDownloadUrl: String?
	)

	data class Account(
		val id: String?,
		val uuid: String?,
		val url: String?,
		val name: String?,
		val host: String?,
		val hostRedundancyAllowed: Boolean?,
		val followingCount: Long?,
		val followersCount: Long?,
		@Embedded(prefix = "avatar_") val avatar: Avatar?,
		val createdAt: Date?,
		val updatedAt: Date?,
		val displayName: String?,
		val description: String?,
		val userId: Long?
	)

	data class Channel(
		val id: String?,
		val uuid: String?,
		val url: String?,
		val name: String?,
		val host: String?,
		val hostRedundancyAllowed: Boolean?,
		val followingCount: Long?,
		val followersCount: Long?,
		@Embedded(prefix = "avatar_") val avatar: Avatar?,
		val createdAt: Date?,
		val updatedAt: Date?,
		val displayName: String?,
		val description: String?,
		val support: String?,
		val isLocal: Boolean?,
		@Embedded(prefix = "ownerAccount_") val ownerAccount: Account?
	)

	data class Avatar(
		val path: String?,
		val createdAt: Date?,
		val updatedAt: Date?
	)

	data class Label(
		val id: String?,
		val label: String?
	)

	@androidx.room.Dao
	interface Dao
	{
		@Insert(onConflict = OnConflictStrategy.REPLACE)
		fun insert(vararg videos: Video)

		@Query("SELECT * FROM video ORDER by publishedAt DESC")
		fun load(): DataSource.Factory<Int, Video>

		@Query("SELECT * FROM video WHERE id=:id")
		fun loadById(id: String): LiveData<Video?>
	}
}