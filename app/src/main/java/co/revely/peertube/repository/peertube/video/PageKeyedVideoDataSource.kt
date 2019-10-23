package co.revely.peertube.repository.peertube.video

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import co.revely.peertube.api.peertube.PeerTubeService
import co.revely.peertube.api.peertube.query.VideoQuery
import co.revely.peertube.api.peertube.response.Video
import co.revely.peertube.repository.NetworkState
import co.revely.peertube.utils.enqueue
import java.io.IOException
import java.util.concurrent.Executor

/**
 * A data source that uses the before/after keys returned in page requests.
 * <p>
 * See ItemKeyedSubredditDataSource
 */
class PageKeyedVideoDataSource(
	private val peerTubeService: PeerTubeService,
	private val retryExecutor: Executor,
	private val videoQuery: VideoQuery?
) : PageKeyedDataSource<Int, Video>() {

	// keep a function reference for the retry event
	private var retry: (() -> Any)? = null

	/**
	 * There is no sync on the state because paging will always call loadInitial first then wait
	 * for it to return some success value before calling loadAfter.
	 */
	val networkState = MutableLiveData<NetworkState>()

	val initialLoad = MutableLiveData<NetworkState>()

	var loadedDataCount = 0

	fun retryAllFailed() {
		val prevRetry = retry
		retry = null
		prevRetry?.let {
			retryExecutor.execute {
				it.invoke()
			}
		}
	}

	override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Video>)
	{
		val request = peerTubeService.videos(
				videoQuery?.categoryOneOf,
				params.requestedLoadSize,
				videoQuery?.filter,
				videoQuery?.languageOneOf,
				videoQuery?.licenceOneOf,
				videoQuery?.nsfw,
				videoQuery?.sort,
				0,
				videoQuery?.tagsAllOf,
				videoQuery?.tagsOneOf
		)
		networkState.postValue(NetworkState.LOADING)
		initialLoad.postValue(NetworkState.LOADING)

		// triggered by a refresh, we better execute sync
		try {
			val response = request.execute()
			val data = response.body()?.data
			val items = data ?: emptyList()
			retry = null
			networkState.postValue(NetworkState.LOADED)
			initialLoad.postValue(NetworkState.LOADED)
			loadedDataCount += items.size
			callback.onResult(items, null, if (items.size == params.requestedLoadSize) loadedDataCount else null)
		} catch (ioException: IOException) {
			retry = { loadInitial(params, callback) }
			val error = NetworkState.error(ioException.message ?: "unknown error")
			networkState.postValue(error)
			initialLoad.postValue(error)
		}
	}

	override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Video>) {
		// ignored, since we only ever append to our initial load
	}

	override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Video>) {
		networkState.postValue(NetworkState.LOADING)
		peerTubeService.videos(
				videoQuery?.categoryOneOf,
				params.requestedLoadSize,
				videoQuery?.filter,
				videoQuery?.languageOneOf,
				videoQuery?.licenceOneOf,
				videoQuery?.nsfw,
				videoQuery?.sort,
				params.key,
				videoQuery?.tagsAllOf,
				videoQuery?.tagsOneOf
		).enqueue { _, response, t ->
			if (response == null)
			{
				retry = { loadAfter(params, callback) }
				networkState.postValue(NetworkState.error(t?.message ?: "unknown err"))
			}
			else
			{
				if (response.isSuccessful) {
					val data = response.body()?.data
					val items = data ?: emptyList()
					retry = null
					loadedDataCount += items.size
					callback.onResult(items, if (items.size == params.requestedLoadSize) loadedDataCount else null)
					networkState.postValue(NetworkState.LOADED)
				} else {
					retry = { loadAfter(params, callback) }
					networkState.postValue(NetworkState.error("error code: ${response.code()}"))
				}
			}
		}
	}
}
