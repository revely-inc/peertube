package co.revely.peertube.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.revely.peertube.repository.NetworkState
import retrofit2.Call

class ApiLiveData<T>(private val request: Call<T>) : LiveData<T>()
{
	private var retry: (() -> Any)? = null
	val networkState = MutableLiveData<NetworkState>()

	override fun onActive()
	{
		super.onActive()
		refresh()
	}

	fun refresh()
	{
		networkState.postValue(NetworkState.LOADING)
		(request.takeIf { !it.isExecuted } ?: request.clone()).enqueue { _, response, t ->
			if (response != null)
			{
				val data = response.body()
				retry = null
				networkState.postValue(NetworkState.LOADED)
				postValue(data)
			}
			else
			{
				retry = { refresh() }
				val error = NetworkState.error(t?.message ?: "unknown error")
				networkState.postValue(error)
			}
		}
	}
}