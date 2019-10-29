package co.revely.peertube

import android.app.Application
import co.revely.peertube.di.*
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class PeerTubeApp : Application()
{
	override fun onCreate()
	{
		super.onCreate()
		if (BuildConfig.DEBUG)
			Timber.plant(Timber.DebugTree())

		Sentry.init("https://3ef84b44b9d241948419ab216cfa6b78@sentry.io/1801039",  AndroidSentryClientFactory(this))

		startKoin {
			androidContext(this@PeerTubeApp)
			modules(listOf(
					appModule,
					viewModelModule,
					instancesModule,
					peertubeModule,
					oauthModule,
					databaseModule
			))
		}
	}
}
