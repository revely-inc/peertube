package co.revely.peertube.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import co.revely.peertube.api.peertube.response.OAuthToken
import co.revely.peertube.repository.Resource
import co.revely.peertube.repository.peertube.oauth.OAuthRepository
import co.revely.peertube.utils.AbsentLiveData

/**
 * Created at 2019-10-20
 *
 * @author rbenjami
 */
class OAuthViewModel(private val oAuthRepository: OAuthRepository) : ViewModel()
{
	private val usernamePassword = MutableLiveData<Pair<String, String>>(null)
	private val client = oAuthRepository.client()

	private val _token: LiveData<Resource<OAuthToken>?> = Transformations
			.switchMap(client) { oAuthClient ->
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

	init
	{
		token.addSource(_token) { token.value = it }
	}

	fun token() = token

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
		this.usernamePassword.value = Pair(username, password)
	}
}