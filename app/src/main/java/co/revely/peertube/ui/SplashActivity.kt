package co.revely.peertube.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.revely.peertube.R
import co.revely.peertube.di.*
import co.revely.peertube.helper.PreferencesHelper
import co.revely.peertube.ui.instances.InstancesActivity
import org.jetbrains.anko.intentFor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.context.unloadKoinModules


/**
 * Created at 2019-06-19
 *
 * @author rbenjami
 */
class SplashActivity : AppCompatActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		setTheme(R.style.AppTheme_Splash)
		super.onCreate(savedInstanceState)

		val modules = listOf(
				appModule,
				viewModelModule,
				instancesModule,
				peertubeModule,
				oauthModule,
				databaseModule
		)
		unloadKoinModules(modules)
		stopKoin()
		startKoin {
			androidContext(applicationContext)
			loadKoinModules(modules)
		}

		PreferencesHelper.currentHost.get().takeIf { it.isNotBlank() }?.also { host ->
			startActivity(
				intentFor<MainActivity>()
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
			)
		} ?: startActivity(intentFor<InstancesActivity>())
		finish()
	}
}