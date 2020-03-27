package co.revely.peertube.ui

import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.Transition
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import co.revely.peertube.R
import co.revely.peertube.helper.PreferencesHelper
import co.revely.peertube.ui.instances.InstancesFragmentDirections
import co.revely.peertube.ui.video.VideoFragment
import co.revely.peertube.utils.invisible
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_video.*
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

		navController.addOnDestinationChangedListener { _, d, a ->
			navigation?.invisible()
		}

		if (savedInstanceState == null)
		{
			PreferencesHelper.defaultHost.get().takeIf { it.isNotBlank() }?.also { host ->
				val directions = InstancesFragmentDirections.actionInstancesToInstance(host)
				navController.navigate(directions)
			}
		}

		navigation.setupWithNavController(navController)
		navigation.setOnNavigationItemSelectedListener { menuItem ->
			if (navController.currentDestination?.id == menuItem.itemId)
				return@setOnNavigationItemSelectedListener false
			val args = main_nav_host_fragment?.childFragmentManager?.fragments?.getOrNull(0)?.arguments
			args?.getString("host")?.also {
				navController.navigate(menuItem.itemId, Bundle().apply { putString("host", it) }, navOptions { launchSingleTop = true })
			} ?: Timber.e("Args 'host' is null")
			return@setOnNavigationItemSelectedListener true
		}
	}

	fun openVideoFragment(host: String, videoId: String)
	{
		main_motion_layout.transitionToEnd()
		supportFragmentManager.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down)
				.replace(R.id.video_fragment, VideoFragment.newInstance(host, videoId), "video_fragment")
				.commit()
	}

	override fun onBackPressed()
	{
		supportFragmentManager.findFragmentById(R.id.video_fragment)?.also { fragment ->
			if (fragment.video_motion_layout.currentState != fragment.video_motion_layout.endState)
			{
				main_motion_layout.transitionToEnd()
				fragment.video_motion_layout.transitionToEnd()
			}
			else
				super.onBackPressed()
		} ?: super.onBackPressed()
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId)
		{

		}
		return true
	}
}
