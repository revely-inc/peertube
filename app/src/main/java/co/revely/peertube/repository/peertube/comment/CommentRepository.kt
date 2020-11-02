package co.revely.peertube.repository.peertube.comment

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import co.revely.peertube.api.PeerTubeService
import co.revely.peertube.api.dto.CommentDto
import co.revely.peertube.api.dao.CommentDao
import co.revely.peertube.utils.AppExecutors
import co.revely.peertube.utils.Listing

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
class CommentRepository(
		private val peerTubeService: PeerTubeService,
		private val appExecutors: AppExecutors
)
{
	companion object
	{
		private const val PAGE_SIZE = 10
	}

	@MainThread
	fun getComments(commentQuery: CommentDto): Listing<CommentDao>
	{
		val sourceFactory = CommentDataSourceFactory(peerTubeService, commentQuery, appExecutors.networkIO())
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
}