package co.revely.peertube.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import co.revely.peertube.api.peertube.response.OAuthToken
import co.revely.peertube.helper.PreferencesHelper
import co.revely.peertube.repository.Resource
import co.revely.peertube.repository.Status
import co.revely.peertube.repository.peertube.oauth.OAuthRepository
import co.revely.peertube.utils.AbsentLiveData
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * Created at 2019-10-20
 *
 * @author rbenjami
 */
class OAuthViewModel : ViewModel(), KoinComponent
{
	private val oAuthRepository: OAuthRepository by inject()
	private val client = oAuthRepository.client()
	private val usernamePassword = MutableLiveData<Pair<String, String>>(null)

	private val _token: LiveData<Resource<OAuthToken>?> = client.switchMap { oAuthClient ->
		val c = oAuthClient.data
		if (c != null)
		{
			Transformations.switchMap(usernamePassword) { usernamePassword ->
				if (usernamePassword != null)
					oAuthRepository.token(c.id, c.secret, usernamePassword.first, usernamePassword.second)
				else
					oAuthRepository.token(c.id, c.secret)
			}
		}
		else
			AbsentLiveData.create()
	}

	val token = MediatorLiveData<Resource<OAuthToken>?>()

	fun isLogged() = token.value?.data != null

	init
	{
		token.addSource(_token) {
			token.value = it
			Timber.i("Token: ${it?.data?.accessToken}")
			if (it?.status == Status.SUCCESS && it.data?.accessToken != null)
			{
				val hostsLogged = PreferencesHelper.hostsLogged.get()
				hostsLogged.add(PreferencesHelper.defaultHost.get())
				PreferencesHelper.hostsLogged.set(hostsLogged)
			}
		}
	}

	fun refresh()
	{
		val client = client.value?.data
		val refreshToken = token.value?.data?.refreshToken
		if (client != null && refreshToken != null)
		{
			val newToken = oAuthRepository.refresh(client.id, client.secret, refreshToken).execute().body()
			if (newToken != null)
				Handler(Looper.getMainLooper()).post {
					token.value = Resource.success(newToken)
				}
		}
	}

	fun login(username: String, password: String)
	{
		usernamePassword.value = Pair(username, password)
	}
}