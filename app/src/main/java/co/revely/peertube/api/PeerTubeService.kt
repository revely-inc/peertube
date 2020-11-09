package co.revely.peertube.api

import androidx.lifecycle.LiveData
import co.revely.peertube.api.dao.*
import co.revely.peertube.db.peertube.entity.OAuthToken
import co.revely.peertube.utils.Rate
import co.revely.peertube.viewmodel.OAuthViewModel
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
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
					if (!refreshInProgress)
						oAuthViewModel.refresh()
					refreshInProgress = true
					while (refreshInProgress)
						Thread.sleep(100)
				}
				builder.header("Authorization", "${token!!.tokenType} ${token!!.accessToken}")
			}

			return chain.proceed(builder.build())
		}
	}

	@GET("users/me")
	fun me(): LiveData<ApiResponse<UserDao>>

	@GET("users/me/videos/{id}/rating")
	fun getRate(
			@Path("id") id: String
	): Call<RateDao>

	@GET("config/about")
	fun configAbout(): LiveData<ApiResponse<AboutInstanceDao>>

	@GET("server/stats")
	fun serverStats(): LiveData<ApiResponse<StatsDao>>

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
			//If you don't need the total in the response
			@Query("skipCount") skipCount: Boolean? = null,
			//Sort videos by criteria ("-name" "-duration" "-createdAt" "-publishedAt" "-views" "-likes" "-trending")
			@Query("sort") sort: String? = null,
			//Offset
			@Query("start") start: Int? = null,
			//tag(s) of the video, where all should be present in the video
			@Query("tagsAllOf") tagsAllOf: List<String>? = null,
			//tag(s) of the video
			@Query("tagsOneOf") tagsOneOf: List<String>? = null
	): Call<DataList<VideoDao>>

	@GET("search/videos")
	fun searchVideos(
			@Query("search") search: String,
			//category id of the video
			@Query("categoryOneOf") categoryOneOf: List<Int>? = null,
			//Number of items
			@Query("count") count: Int? = null,
			//Get videos that have this maximum duration
			@Query("durationMax") durationMax: Int? = null,
			//Get videos that have this minimum duration
			@Query("durationMin") durationMin: Int? = null,
			//Special filters (local for instance) which might require special rights:
			@Query("filter") filter: String? = null,
			//language id of the video
			@Query("languageOneOf") languageOneOf: List<String>? = null,
			//licence id of the video
			@Query("licenceOneOf") licenceOneOf: List<String>? = null,
			//whether to include nsfw videos, if any
			@Query("nsfw") nsfw: Boolean? = null,
			//Get videos that are originally published before this date
			@Query("originallyPublishedEndDate") originallyPublishedEndDate: String? = null,
			//Get videos that are originally published after this date
			@Query("originallyPublishedStartDate") originallyPublishedStartDate: String? = null,
			//If the administrator enabled search index support, you can override the default search target. ("local" "search-index")
			@Query("searchTarget") searchTarget: String? = null,
			//If you don't need the total in the response
			@Query("skipCount") skipCount: Boolean? = null,
			//Sort videos by criteria ("name" "-duration" "-createdAt" "-publishedAt" "-views" "-likes" "-match")
			@Query("sort") sort: String? = null,
			//Offset used to paginate results
			@Query("start") start: Int? = null,
			//Get videos that are published after this date
			@Query("startDate") startDate: String? = null,
			//Get videos that are published before this date
			@Query("endDate") endDate: String? = null,
			//tag(s) of the video, where all should be present in the video
			@Query("tagsAllOf") tagsAllOf: List<String>? = null,
			//tag(s) of the video
			@Query("tagsOneOf") tagsOneOf: List<String>? = null
	): Call<DataList<VideoDao>>

	@GET("videos/{id}")
	fun video(
		@Path("id") id: String
	): Call<VideoDao>

	@GET("videos/{id}/description")
	fun videoDescription(
		@Path("id") id: String
	): Call<DescriptionDao>

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
	): Call<DataList<CommentDao>>
}
