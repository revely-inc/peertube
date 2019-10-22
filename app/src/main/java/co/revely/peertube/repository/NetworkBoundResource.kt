
package co.revely.peertube.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import co.revely.peertube.api.ApiEmptyResponse
import co.revely.peertube.api.ApiErrorResponse
import co.revely.peertube.api.ApiResponse
import co.revely.peertube.api.ApiSuccessResponse
import co.revely.peertube.utils.AppExecutors

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors)
{
	private val result = MediatorLiveData<Resource<ResultType>>()

	init
	{
		result.value = Resource.loading(null)
		@Suppress("LeakingThis")
		val dbSource = loadFromDb()
		result.addSource(dbSource) { data ->
			result.removeSource(dbSource)
			if (shouldFetch(data))
				fetchFromNetwork(data, dbSource)
			else
			{
				result.addSource(dbSource) { newData ->
					setValue(Resource.success(newData))
				}
			}
		}
	}

	@MainThread
	private fun setValue(newValue: Resource<ResultType>)
	{
		if (result.value != newValue)
			result.value = newValue
	}

	private fun fetchFromNetwork(data: ResultType?, dbSource: LiveData<ResultType>)
	{
		val apiResponse = createCall(data)
		// we re-attach dbSource as a instance source, it will dispatch its latest value quickly
		result.addSource(dbSource) { newData ->
			setValue(Resource.loading(newData))
		}
		result.addSource(apiResponse) { response ->
			result.removeSource(apiResponse)
			result.removeSource(dbSource)
			when (response)
			{
				is ApiSuccessResponse ->
				{
					appExecutors.diskIO().execute {
						saveCallResult(processResponse(response))
						appExecutors.mainThread().execute {
							// we specially request a instance live data,
							// otherwise we will get immediately last cached value,
							// which may not be updated with latest results received from network.
							result.addSource(loadFromDb()) { newData ->
								setValue(Resource.success(newData))
							}
						}
					}
				}
				is ApiEmptyResponse ->
				{
					appExecutors.mainThread().execute {
						// reload from disk whatever we had
						result.addSource(loadFromDb()) { newData ->
							setValue(Resource.success(newData))
						}
					}
				}
				is ApiErrorResponse ->
				{
					onFetchFailed()
					result.addSource(dbSource) { newData ->
						setValue(Resource.error(response.errorMessage, newData))
					}
				}
			}
		}
	}

	protected open fun onFetchFailed()
	{
	}

	fun asLiveData() = result as LiveData<Resource<ResultType>>

	@WorkerThread
	protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

	@WorkerThread
	protected abstract fun saveCallResult(item: RequestType)

	@MainThread
	protected abstract fun shouldFetch(data: ResultType?): Boolean

	@MainThread
	protected abstract fun loadFromDb(): LiveData<ResultType>

	@MainThread
	protected abstract fun createCall(data: ResultType?): LiveData<ApiResponse<RequestType>>
}