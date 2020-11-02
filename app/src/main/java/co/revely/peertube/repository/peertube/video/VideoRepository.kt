package co.revely.peertube.repository.peertube.video

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import co.revely.peertube.api.PeerTubeService
import co.revely.peertube.api.DataList
import co.revely.peertube.api.dao.VideoDao
import co.revely.peertube.utils.AppExecutors
import co.revely.peertube.utils.Listing
import co.revely.peertube.utils.Rate
import retrofit2.Call

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

	@MainThread
	fun videosListing(request: ((peerTubeService: PeerTubeService, size: Int, index: Int) -> Call<DataList<VideoDao>>)): Listing<VideoDao>
	{
		val sourceFactory = DataSourceFactory(peerTubeService, appExecutors.networkIO(), request)
		val livePagedList = sourceFactory.toLiveData(pageSize = PAGE_SIZE, fetchExecutor = appExecutors.networkIO())
		val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
			it.initialLoad
		}
		return Listing(
				pagedList = livePagedList,
				networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
					it.networkState
				},
				retry = {
					sourceFactory.sourceLiveData.value?.retryAllFailed()
				},
				refresh = {
					sourceFactory.sourceLiveData.value?.invalidate()
				},
				refreshState = refreshState
		)
	}

	fun getVideoById(id: String) = peerTubeService.video(id)
	fun getMyRating(id: String) = peerTubeService.getRate(id)

	fun rateVideo(id: String, @Rate rating: String) = peerTubeService.rateVideo(id, rating)
}