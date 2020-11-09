package co.revely.peertube.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import androidx.paging.toLiveData
import co.revely.peertube.di.injectValue
import co.revely.peertube.repository.NetworkState
import co.revely.peertube.repository.peertube.DataSourceFactory
import co.revely.peertube.utils.ApiLiveData
import co.revely.peertube.utils.AppExecutors
import retrofit2.Call

/**
 * Created at 03/11/2020
 *
 * @author rbenjami
 */
open class ApiData<T>(
	// the LiveData of paged lists for the UI to observe
	val data: LiveData<T>,
	val networkState: LiveData<NetworkState>,
	// represents the refresh status to show to the user. Separate from networkState, this
	// value is importantly only when refresh is requested.
	val refreshState: LiveData<NetworkState>,
	// refreshes the whole data and fetches it from scratch.
	val refresh: () -> Unit,
	// retries any failed requests.
	val retry: () -> Unit
)

fun <T> apiData(request: Call<T>): ApiData<T>
{
	val liveData = ApiLiveData(request)
	return ApiData(
		data = liveData,
		networkState = liveData.networkState,
		retry = {
			liveData.refresh()
		},
		refresh = {
			liveData.refresh()
		},
		refreshState = liveData.networkState
	)
}

fun <T> Call<T>.toApiData() = apiData(this)