package co.revely.peertube.api.peertube

import androidx.lifecycle.LiveData
import co.revely.peertube.api.ApiResponse
import co.revely.peertube.api.ArrayResponse
import co.revely.peertube.api.peertube.response.*
import co.revely.peertube.utils.Rate
import co.revely.peertube.viewmodel.OAuthViewModel
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
interface PeerTubeService
{
	class OAuthInterceptor(oAuthViewModel: OAuthViewModel) : Interceptor
	{
		private var token: OAuthToken? = null

		init
		{
			oAuthViewModel.token().observeForever { token = it?.data }
		}

		override fun intercept(chain: Interceptor.Chain): Response
		{
			val builder = chain.request().newBuilder()

			if (token != null)
				builder.header("Authorization", "${token!!.tokenType} ${token!!.accessToken}")

			return chain.proceed(builder.build())
		}
	}

	@GET("users/me")
	fun me(): LiveData<ApiResponse<User>>

	@GET("videos")
	fun videos(
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
	): Call<ArrayResponse<Video>>

	@GET("videos/{id}")
	fun video(
		@Path("id") id: String
	): LiveData<ApiResponse<Video>>

	@GET("config/about")
	fun configAbout(): LiveData<ApiResponse<AboutInstanceResponse>>

	@GET("users/me/videos/{id}/rating")
	fun getRate(
			@Path("id") id: String
	): LiveData<ApiResponse<RateResponse>>

	@FormUrlEncoded
	@PUT("videos/{id}/rate")
	fun rateVideo(
			@Path("id") id: String,
			@Field("rating") @Rate rating: String
	): Call<ResponseBody>
}
