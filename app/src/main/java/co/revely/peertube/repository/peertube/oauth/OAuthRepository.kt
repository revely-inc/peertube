package co.revely.peertube.repository.peertube.oauth

import co.revely.peertube.api.oauth.OAuthService
import co.revely.peertube.api.peertube.response.OAuthClient
import co.revely.peertube.api.peertube.response.OAuthToken
import co.revely.peertube.repository.NetworkBoundResource
import co.revely.peertube.utils.AppExecutors
import java.util.*

/**
 * Created at 2019-10-20
 *
 * @author rbenjami
 */
class OAuthRepository(
		private val oAuthService: OAuthService,
		private val appExecutors: AppExecutors,
		private val oAuthClientDao: OAuthClient.Dao,
		private val oAuthTokenDao: OAuthToken.Dao
)
{
	fun token(clientId: String, clientSecret: String, username: String? = null, password: String? = null) =
			object : NetworkBoundResource<OAuthToken, OAuthToken>(appExecutors)
			{
				override fun saveCallResult(item: OAuthToken) = oAuthTokenDao.insert(item)
				override fun shouldFetch(data: OAuthToken?) =
						(username != null && password != null) || (data != null && Date().time >= data.expiresAt)
				override fun loadFromDb() = oAuthTokenDao.load()
				override fun createCall(data: OAuthToken?) = oAuthService.getToken(
						clientId,
						clientSecret,
						if (username == null) "refresh_token" else "password",
						username,
						password,
						data?.refreshToken
				)
			}.asLiveData()

	fun refresh(clientId: String, clientSecret: String, refreshToken: String) =
			oAuthService.refreshToken(clientId, clientSecret, refreshToken)

	fun client() =
			object : NetworkBoundResource<OAuthClient, OAuthClient>(appExecutors) {
				override fun saveCallResult(item: OAuthClient) = oAuthClientDao.insert(item)
				override fun shouldFetch(data: OAuthClient?) = data == null
				override fun loadFromDb() = oAuthClientDao.load()
				override fun createCall(data: OAuthClient?) = oAuthService.getClient()
			}.asLiveData()
}
