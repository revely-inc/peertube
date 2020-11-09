package co.revely.peertube.api
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.toLiveData
import co.revely.peertube.di.injectValue
import co.revely.peertube.repository.NetworkState
import co.revely.peertube.repository.peertube.DataSourceFactory
import co.revely.peertube.utils.AppExecutors
import retrofit2.Call

/**
 * Data class that is necessary for a UI to show a listing and interact w/ the rest of the system
 */
class ApiDataList<T>(
	pagedList: LiveData<PagedList<T>>,
	networkState: LiveData<NetworkState>,
	refreshState: LiveData<NetworkState>,
	refresh: () -> Unit,
	retry: () -> Unit
): ApiData<PagedList<T>>(pagedList, networkState, refreshState, refresh, retry)

private const val PAGE_SIZE = 10
private val appExecutors by injectValue<AppExecutors>()

fun <T> apiDataList(peerTubeService: PeerTubeService, request: ((peerTubeService: PeerTubeService, size: Int, index: Int) -> Call<DataList<T>>)): ApiDataList<T>
{
	val sourceFactory = DataSourceFactory(peerTubeService, appExecutors.networkIO(), request)
	val livePagedList = sourceFactory.toLiveData(pageSize = PAGE_SIZE, fetchExecutor = appExecutors.networkIO())
	val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
		it.initialLoad
	}
	return ApiDataList(
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