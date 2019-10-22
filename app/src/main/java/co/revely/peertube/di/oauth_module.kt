package co.revely.peertube.di

import co.revely.peertube.api.oauth.OAuthService
import co.revely.peertube.api.peertube.response.OAuthClient
import co.revely.peertube.api.peertube.response.OAuthToken
import co.revely.peertube.utils.LiveDataCallAdapterFactory
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.lang.reflect.Type

/**
 * Created at 2019-10-11
 *
 * @author rbenjami
 */
val oauthModule = module {
	single { (host: String) ->
		val API_VERSION = "api/v1"

		Retrofit.Builder()
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
				.addConverterFactory(GsonConverterFactory.create(GsonBuilder()
						.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
						.registerTypeAdapter(OAuthClient::class.java, object : JsonDeserializer<OAuthClient>
						{
							override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): OAuthClient
							{
								val o = json.asJsonObject
								return OAuthClient(o.get("client_id").asString, o.get("client_secret").asString)
							}
						})
						.registerTypeAdapter(OAuthToken::class.java, object : JsonDeserializer<OAuthToken>
						{
							override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): OAuthToken
							{
								val o = json.asJsonObject
								return OAuthToken(
										o.get("access_token").asString,
										o.get("token_type").asString,
										o.get("expires_in").asLong,
										o.get("refresh_token").asString)
							}
						})
						.create()))
				.addCallAdapterFactory(LiveDataCallAdapterFactory())
				.build()
				.create(OAuthService::class.java)
	}
}