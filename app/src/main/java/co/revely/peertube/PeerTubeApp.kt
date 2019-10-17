package co.revely.peertube

import android.app.Application
import co.revely.peertube.di.appModule
import co.revely.peertube.di.instancesModule
import co.revely.peertube.di.viewModelModule
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

		startKoin {
			androidContext(this@PeerTubeApp)
			modules(listOf(
					appModule,
					viewModelModule,
					instancesModule
			))
		}
	}
}
