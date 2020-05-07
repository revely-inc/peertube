package co.revely.peertube

import android.app.Application
import co.revely.peertube.di.*
import com.bugsnag.android.Bugsnag
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
		else
			Bugsnag.start(this)

		startKoin {
			androidContext(applicationContext)
		}
	}
}
