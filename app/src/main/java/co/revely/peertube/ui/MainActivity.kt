package co.revely.peertube.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import co.revely.peertube.R
import co.revely.peertube.api.ApiSuccessResponse
import co.revely.peertube.databinding.ActivityMainBinding
import co.revely.peertube.databinding.NavHeaderMainBinding
import co.revely.peertube.db.instances.entity.Instance
import co.revely.peertube.ui.account.AccountActivity
import co.revely.peertube.ui.instances.InstancesActivity
import co.revely.peertube.ui.video.VideoFragment
import co.revely.peertube.utils.invisible
import co.revely.peertube.utils.observe
import co.revely.peertube.viewmodel.ErrorHelper
import co.revely.peertube.viewmodel.InstanceViewModel
import co.revely.peertube.viewmodel.OAuthViewModel
import co.revely.peertube.viewmodel.UserViewModel
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_video.*
import org.jetbrains.anko.intentFor
import org.koin.android.ext.android.inject
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
	private lateinit var binding: ActivityMainBinding
	private lateinit var drawerHeaderBinding: NavHeaderMainBinding

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)

		binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
		drawerHeaderBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))
		drawerHeaderBinding.login.setOnClickListener { startActivity(intentFor<AccountActivity>()) }
		nav_view.setNavigationItemSelectedListener { onNavigationItemSelected(it) }

		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		navController.addOnDestinationChangedListener { _, d, a ->
			navigation?.invisible()
		}

		setupActionBarWithNavController(navController, AppBarConfiguration(setOf(
				R.id.navigation_overview, R.id.navigation_trending, R.id.navigation_recent, R.id.navigation_local, R.id.navigation_about
		), drawer))
		navigation.setupWithNavController(navController)

		observe(ErrorHelper.error) { error ->
			if (error.displayed == 0)
			{
				Snackbar.make(main_motion_layout, error.title, Snackbar.LENGTH_LONG)
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
		main_motion_layout.transitionToEnd()
		supportFragmentManager.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down)
				.replace(R.id.video_fragment, VideoFragment.newInstance(videoId), "video_fragment")
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

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId) {
			android.R.id.home -> drawer.open()
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId) {
			R.id.instances -> {
				drawer.close()
				startActivity(intentFor<InstancesActivity>())
			}
		}
		return true
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
