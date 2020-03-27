package co.revely.peertube.api.peertube.response

/**
 * Created at 26/03/2020
 *
 * @author rbenjami
 */
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