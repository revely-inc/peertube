package co.revely.peertube.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import co.revely.peertube.R
import co.revely.peertube.helper.PreferencesHelper
import co.revely.peertube.ui.instances.InstancesFragmentDirections
import co.revely.peertube.utils.invisible
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
	private val navController : NavController by lazy { findNavController(R.id.main_nav_host_fragment) }

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		navController.addOnDestinationChangedListener { _, _, _ ->
			navigation?.invisible()
		}

		PreferencesHelper.defaultHost.get().takeIf { it.isNotBlank() }?.also { host ->
			val directions = InstancesFragmentDirections.actionInstancesToInstance(host)
			navController.navigate(directions)
		}

		navigation.setOnNavigationItemSelectedListener { menuItem ->
			val args = main_nav_host_fragment?.childFragmentManager?.fragments?.getOrNull(0)?.arguments
			args?.getString("host")?.also {
				navController.navigate(menuItem.itemId, Bundle().apply { putString("host", it) })
			} ?: Timber.e("Args 'host' is null")
			return@setOnNavigationItemSelectedListener true
		}
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId)
		{

		}
		return true
	}
}
