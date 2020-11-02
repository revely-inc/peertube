package co.revely.peertube.api.dao

import androidx.annotation.Keep

/**
 * Created at 30/10/2020
 *
 * @author rbenjami
 */
@Keep
data class StreamingPlaylistDao(
		val id: Int? = null,
		val type: Int? = null,
		val playlistUrl: String? = null,
		val segmentsSha256Url: String? = null,
		val files: List<FileDao>? = null,
		val redundancies: List<RedundancyDao>? = null
): Dao()