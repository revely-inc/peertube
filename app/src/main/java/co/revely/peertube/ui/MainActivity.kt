package co.revely.peertube.ui

import android.app.PictureInPictureParams
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import co.revely.peertube.R
import co.revely.peertube.api.ApiSuccessResponse
import co.revely.peertube.databinding.ActivityMainBinding
import co.revely.peertube.databinding.FragmentVideoBinding
import co.revely.peertube.databinding.NavHeaderMainBinding
import co.revely.peertube.ui.account.AccountActivity
import co.revely.peertube.ui.instances.InstancesActivity
import co.revely.peertube.ui.video.VideoFragment
import co.revely.peertube.ui.videos.VideosFragment
import co.revely.peertube.utils.globalVisibleRect
import co.revely.peertube.utils.invisible
import co.revely.peertube.utils.observe
import co.revely.peertube.utils.visible
import co.revely.peertube.viewmodel.ErrorHelper
import co.revely.peertube.viewmodel.InstanceViewModel
import co.revely.peertube.viewmodel.OAuthViewModel
import co.revely.peertube.viewmodel.UserViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.intentFor
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
{
	private val oAuthViewModel: OAuthViewModel by viewModel()
	private val userViewModel: UserViewModel by viewModel(parameters = { parametersOf(oAuthViewModel) })
	private val instanceViewModel: InstanceViewModel by viewModel()

	private val navController : NavController by lazy { findNavController(R.id.main_nav_host_fragment) }
	private lateinit var drawerHeaderBinding: NavHeaderMainBinding

	lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

		drawerHeaderBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
		drawerHeaderBinding.login.setOnClickListener { startActivity(intentFor<AccountActivity>()) }

		binding.navView.setNavigationItemSelectedListener { onNavigationItemSelected(it) }
		binding.swipeRefresh.apply {
			isEnabled = false
			setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark)
		}

		setSupportActionBar(binding.toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		navController.addOnDestinationChangedListener { _, d, a ->
			when (d.id) {
				R.id.navigation_overview,
				R.id.navigation_trending,
				R.id.navigation_local,
				R.id.navigation_recent,
				R.id.navigation_about,
				-> binding.navigation.visible()
				else -> binding.navigation.invisible()
			}
		}

		setupActionBarWithNavController(navController, AppBarConfiguration(setOf(
				R.id.navigation_overview, R.id.navigation_trending, R.id.navigation_recent, R.id.navigation_local, R.id.navigation_about
		), binding.drawer))
		binding.navigation.setupWithNavController(navController)

		observe(ErrorHelper.error) { error ->
			if (error.displayed == 0)
			{
				Snackbar.make(binding.mainMotionLayout, error.title, Snackbar.LENGTH_LONG)
						.setAction(error.actionText) { error.action(this) }
						.show()
				error.displayed++
			}
		}
		observe(instanceViewModel.configAbout) {
			if (it is ApiSuccessResponse)
				drawerHeaderBinding.instance = it.body.instance
		}
		initUser()
	}

	fun openVideoFragment(videoId: String)
	{
		val videoFragment = supportFragmentManager.findFragmentById(R.id.video_fragment)
		videoFragment?.also { fragment -> fragment.binding<FragmentVideoBinding>()?.videoMotionLayout?.transitionToEnd() }
		if ((videoFragment as? VideoFragment)?.arguments?.getString("video_id") != videoId)
		{
			supportFragmentManager.beginTransaction()
					.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down)
					.replace(R.id.video_fragment, VideoFragment.newInstance(videoId), "video_fragment")
					.commit()
		}
	}

	override fun onBackPressed()
	{
		supportFragmentManager.findFragmentById(R.id.video_fragment)?.also { fragment ->
			if (fragment.binding<FragmentVideoBinding>()?.videoMotionLayout?.currentState != fragment.binding<FragmentVideoBinding>()?.videoMotionLayout?.startState)
			{
				binding.mainMotionLayout.transitionToStart()
				fragment.binding<FragmentVideoBinding>()?.videoMotionLayout?.transitionToStart()
			}
			else
				super.onBackPressed()
		} ?: super.onBackPressed()
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		val mainNavHostFragment = supportFragmentManager.findFragmentByTag("main_nav_host_fragment") as? NavHostFragment
		if (mainNavHostFragment != null && mainNavHostFragment.childFragmentManager.fragments[0].onOptionsItemSelected(item))
			return true
		return when (item.itemId) {
			android.R.id.home -> {
				binding.drawer.open()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId) {
			R.id.instances -> {
				binding.drawer.close()
				startActivity(intentFor<InstancesActivity>())
			}
		}
		return true
	}

	@RequiresApi(Build.VERSION_CODES.O)
	override fun onUserLeaveHint()
	{
		super.onUserLeaveHint()
		val videoFragment = supportFragmentManager.findFragmentById(R.id.video_fragment)
		if (videoFragment != null)
		{
			enterPictureInPictureMode(PictureInPictureParams.Builder()
					.setSourceRectHint(videoFragment.binding<FragmentVideoBinding>()?.player?.globalVisibleRect)
					.setAspectRatio(Rational(16, 9))
					.build())
		}
	}

	private fun initUser()
	{
		observe(userViewModel.me()) { response -> when (response) {
			is ApiSuccessResponse -> {
				val user = response.body
				drawerHeaderBinding.user = user
			}
		} }
	}
}
