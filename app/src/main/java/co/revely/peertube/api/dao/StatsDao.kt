package co.revely.peertube.api.dao

import androidx.annotation.Keep

/**
 * Created at 26/03/2020
 *
 * @author rbenjami
 */
@Keep
data class StatsDao(
	val totalLocalVideos: Long? = null,
	val totalLocalVideoViews: Long? = null,
	val totalLocalVideoFilesSize: Long? = null,
	val totalLocalVideoComments: Long? = null,
	val totalVideos: Long? = null,
	val totalVideoComments: Long? = null,
	val totalUsers: Long? = null,
	val totalInstanceFollowers: Long? = null,
	val totalInstanceFollowing: Long? = null,
	val videosRedundancy: List<Any>? = null
): Dao()