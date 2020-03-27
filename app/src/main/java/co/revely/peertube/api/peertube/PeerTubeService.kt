package co.revely.peertube.api.peertube

import androidx.lifecycle.LiveData
import co.revely.peertube.api.ApiResponse
import co.revely.peertube.api.peertube.response.*
import co.revely.peertube.utils.Rate
import co.revely.peertube.viewmodel.OAuthViewModel
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import timber.log.Timber
import java.util.*

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
interface PeerTubeService
{
	class OAuthInterceptor(val oAuthViewModel: OAuthViewModel) : Interceptor
	{
		private var token: OAuthToken? = null
		private var refreshInProgress = false

		init
		{
			oAuthViewModel.token.observeForever {
				token = it?.data
				refreshInProgress = false
			}
		}

		override fun intercept(chain: Interceptor.Chain): Response
		{
			val builder = chain.request().newBuilder()

			if (token != null)
			{
				if (Date().time >= token!!.expiresAt)
				{
					if (refreshInProgress)
					{
						while (refreshInProgress)
							Thread.sleep(100)
					}
					else
					{
						refreshInProgress = true
						oAuthViewModel.refresh()
					}
				}
				builder.header("Authorization", "${token!!.tokenType} ${token!!.accessToken}")
			}

			return chain.proceed(builder.build())
		}
	}

	@GET("users/me")
	fun me(): LiveData<ApiResponse<User>>

	@GET("users/me/videos/{id}/rating")
	fun getRate(
			@Path("id") id: String
	): LiveData<ApiResponse<RateResponse>>

	@GET("config/about")
	fun configAbout(): LiveData<ApiResponse<AboutInstanceResponse>>

	@GET("server/stats")
	fun serverStats(): LiveData<ApiResponse<Stats>>

	@GET("videos")
	fun getVideos(
		//category id of the video
		@Query("categoryOneOf") categoryOneOf: List<Int>? = null,
		//Number of items
		@Query("count") count: Int? = null,
		//Special filters (local for instance) which might require special rights:
		@Query("filter") filter: String? = null,
		//language id of the video
		@Query("languageOneOf") languageOneOf: List<String>? = null,
		//licence id of the video
		@Query("licenceOneOf") licenceOneOf: List<String>? = null,
		//whether to include nsfw videos, if any
		@Query("nsfw") nsfw: Boolean? = null,
		//Sort videos by criteria ("-name" "-duration" "-createdAt" "-publishedAt" "-views" "-likes" "-trending")
		@Query("sort") sort: String? = null,
		//Offset
		@Query("start") start: Int? = null,
		//tag(s) of the video, where all should be present in the video
		@Query("tagsAllOf") tagsAllOf: List<String>? = null,
		//tag(s) of the video
		@Query("tagsOneOf") tagsOneOf: List<String>? = null
	): Call<DataList<Video>>

	@GET("videos/{id}")
	fun video(
		@Path("id") id: String
	): LiveData<ApiResponse<Video>>

	@FormUrlEncoded
	@PUT("videos/{id}/rate")
	fun rateVideo(
			@Path("id") id: String,
			@Field("rating") @Rate rating: String
	): Call<ResponseBody>

	@GET("videos/{id}/comment-threads")
	fun getComments(
			@Path("id") id: String,
			//Sort videos by criteria ("-createdAt" "-updatedAt")
			@Query("sort") sort: String? = null,
			//Number of items
			@Query("count") count: Int? = null,
			//Offset
			@Query("start") start: Int? = null
	): Call<DataList<Comment>>
}
