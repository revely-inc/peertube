package co.revely.peertube.repository.peertube.video

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import co.revely.peertube.api.peertube.PeerTubeService
import co.revely.peertube.api.peertube.query.VideoQuery
import co.revely.peertube.api.peertube.response.Video
import co.revely.peertube.utils.AppExecutors
import co.revely.peertube.utils.Rate

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
class VideoRepository(
	private val peerTubeService: PeerTubeService,
	private val appExecutors: AppExecutors
)
{
	companion object
	{
		private const val PAGE_SIZE = 10
	}

	fun getVideos(videoQuery: VideoQuery?): LiveData<PagedList<Video>>
	{
		val sourceFactory = VideoDataSourceFactory(peerTubeService, appExecutors.networkIO(), videoQuery)
		return sourceFactory.toLiveData(pageSize = PAGE_SIZE, fetchExecutor = appExecutors.networkIO())
	}

	fun getVideoById(id: String) = peerTubeService.video(id)
	fun getMyRating(id: String) = peerTubeService.getRate(id)

	fun likeVideo(id: String) = peerTubeService.rateVideo(id, Rate.LIKE)
	fun dislikeVideo(id: String) = peerTubeService.rateVideo(id, Rate.DISLIKE)
	fun unrateVideo(id: String) = peerTubeService.rateVideo(id, Rate.NONE)
}