package co.revely.peertube.repository.peertube.video

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import co.revely.peertube.api.*
import co.revely.peertube.api.dao.VideoDao
import co.revely.peertube.utils.AppExecutors
import co.revely.peertube.utils.Rate
import retrofit2.Call

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
class VideoRepository(
		private val peerTubeService: PeerTubeService
)
{
	@MainThread
	fun videosDataList(
		request: ((peerTubeService: PeerTubeService, size: Int, index: Int) -> Call<DataList<VideoDao>>)
	) =
		apiDataList(peerTubeService, request)

	fun getVideoById(id: String) = peerTubeService.video(id).toApiData()
	fun getVideoDescriptionById(id: String) = peerTubeService.videoDescription(id).toApiData()
	fun getMyRating(id: String) = peerTubeService.getRate(id).toApiData()

	fun rateVideo(id: String, @Rate rating: String) = peerTubeService.rateVideo(id, rating)
}