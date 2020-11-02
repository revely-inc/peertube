package co.revely.peertube.api

import androidx.lifecycle.LiveData
import co.revely.peertube.api.ApiResponse
import co.revely.peertube.db.peertube.entity.OAuthClient
import co.revely.peertube.db.peertube.entity.OAuthToken
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created at 2019-10-20
 *
 * @author rbenjami
 */
interface OAuthService
{
	@GET("oauth-clients/local")
	fun getClient(): LiveData<ApiResponse<OAuthClient>>

	@FormUrlEncoded
	@POST("users/token")
	fun getToken(
			@Field("client_id") clientId: String,
			@Field("client_secret") clientSecret: String,
			@Field("grant_type") grantType: String,
			@Field("username") username: String? = null,
			@Field("password") password: String? = null,
			@Field("refresh_token") refreshToken: String? = null
	): LiveData<ApiResponse<OAuthToken>>

	@FormUrlEncoded
	@POST("users/token")
	fun refreshToken(
			@Field("client_id") clientId: String,
			@Field("client_secret") clientSecret: String,
			@Field("refresh_token") refreshToken: String,
			@Field("grant_type") grantType: String = "refresh_token"
	): Call<OAuthToken>
}