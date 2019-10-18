package co.revely.peertube.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import co.revely.peertube.R
import co.revely.peertube.helper.PreferencesHelper
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import timber.log.Timber

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
	private val navController : NavController by lazy { findNavController(R.id.main_nav_host_fragment) }
	private val sharedPreferences: SharedPreferences by inject()

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		val navHostFragment = main_nav_host_fragment as NavHostFragment
		val inflater = navHostFragment.navController.navInflater
		val defaultHost = PreferencesHelper.defaultHost.get()
		val graph = if (defaultHost.isEmpty())
			inflater.inflate(R.navigation.main_nav_graph)
		else
		{
			inflater.inflate(R.navigation.instance_nav_graph).apply {
				addArgument("host", NavArgument.Builder().setDefaultValue(defaultHost).build())
			}
		}

		navHostFragment.navController.graph = graph

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
