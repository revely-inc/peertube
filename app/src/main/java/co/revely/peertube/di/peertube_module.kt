package co.revely.peertube.di

import co.revely.peertube.api.PeerTubeService
import co.revely.peertube.helper.PreferencesHelper
import co.revely.peertube.utils.LiveDataCallAdapterFactory
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

/**
 * Created at 2019-10-11
 *
 * @author rbenjami
 */
val peertubeModule = module {
	single {
		val API_VERSION = "api/v1"

		val host = PreferencesHelper.currentHost.get()
		Retrofit.Builder()
				.baseUrl("https://$host/${API_VERSION}/")
				.client(OkHttpClient.Builder()
						.addInterceptor(PeerTubeService.OAuthInterceptor(get()))
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
	}
}