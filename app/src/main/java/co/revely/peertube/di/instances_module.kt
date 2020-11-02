package co.revely.peertube.di

import co.revely.peertube.api.InstancesService
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
val instancesModule = module {
	single {
		Retrofit.Builder()
				.baseUrl(InstancesService.API_BASE_URL)
				.client(
						OkHttpClient.Builder()
								.addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
									override fun log(message: String)
									{
										Timber.tag("OkHttp InstancesService").d(message)
									}
								}).apply {
									level = HttpLoggingInterceptor.Level.BODY
								})
								.build())
				.addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()))
				.addCallAdapterFactory(LiveDataCallAdapterFactory())
				.build()
				.create(InstancesService::class.java)
	}
}