package co.revely.peertube.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
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

	private val token: LiveData<Resource<OAuthToken>?> = Transformations
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

	fun token() = token

	fun login(username: String, password: String)
	{
		this.usernamePassword.value = Pair(username, password)
	}
}