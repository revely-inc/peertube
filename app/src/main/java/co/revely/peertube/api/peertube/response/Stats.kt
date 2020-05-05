package co.revely.peertube.api.peertube.response

import androidx.annotation.Keep

/**
 * Created at 26/03/2020
 *
 * @author rbenjami
 */
@Keep
data class Stats(
	val totalLocalVideos: Long,
	val totalLocalVideoViews: Long,
	val totalLocalVideoFilesSize: Long,
	val totalLocalVideoComments: Long,
	val totalVideos: Long,
	val totalVideoComments: Long,
	val totalUsers: Long,
	val totalInstanceFollowers: Long,
	val totalInstanceFollowing: Long,
	val videosRedundancy: List<Any>
)