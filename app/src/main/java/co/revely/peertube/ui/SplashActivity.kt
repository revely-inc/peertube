package co.revely.peertube.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.revely.peertube.R
import co.revely.peertube.helper.PreferencesHelper
import co.revely.peertube.ui.instances.InstancesActivity
import org.jetbrains.anko.intentFor

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
		PreferencesHelper.defaultHost.get().takeIf { it.isNotBlank() }?.also { host ->
			startActivity(intentFor<MainActivity>())
		} ?: startActivity(intentFor<InstancesActivity>())
		finish()
	}
}