package co.revely.peertube.api.peertube

import androidx.lifecycle.LiveData
import co.revely.peertube.api.ApiResponse
import co.revely.peertube.api.ArrayResponse
import co.revely.peertube.api.peertube.response.AboutInstanceResponse
import co.revely.peertube.api.peertube.response.ClientResponse
import co.revely.peertube.api.peertube.response.RateResponse
import co.revely.peertube.api.peertube.response.TokenResponse
import co.revely.peertube.db.peertube.entity.Video
import co.revely.peertube.utils.LiveDataCallAdapterFactory
import co.revely.peertube.utils.Rate
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import timber.log.Timber

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
interface PeerTubeService
{
	companion object
	{
		const val API_VERSION = "api/v1"

		private val services = HashMap<String, PeerTubeService>()

		fun instance(host: String): PeerTubeService
		{
			if (services.containsKey(host))
				return services[host]!!
			val service = Retrofit.Builder()
				.baseUrl("https://$host/$API_VERSION/")
				.client(OkHttpClient.Builder()
					.addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
						override fun log(message: String) {
							Timber.tag("OkHttp $host").d(message)
						}
					}).apply {
						level = HttpLoggingInterceptor.Level.BODY
					})
					.build())
				.addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()))
				.addCallAdapterFactory(LiveDataCallAdapterFactory())
				.build()
				.create(PeerTubeService::class.java)
			services[host] = service
			return service
		}
	}

	@GET("oauth-clients/local")
	fun getClient(): Call<ClientResponse>

	@POST("users/token")
	fun getToken(@Field("id_client") idClient: String): Call<TokenResponse>

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

	@PUT("videos/{id}/rate")
	fun rateVideo(
			@Path("id") id: String,
			@Field("rating") @Rate rating: String
	): ResponseBody
}
