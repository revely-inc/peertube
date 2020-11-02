package co.revely.peertube.ui.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import co.revely.peertube.R
import co.revely.peertube.databinding.ActivityAccountBinding
import co.revely.peertube.utils.observe
import co.revely.peertube.viewmodel.ErrorHelper
import co.revely.peertube.viewmodel.OAuthViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created at 07/05/2020
 *
 * @author rbenjami
 */
class AccountActivity: AppCompatActivity()
{
	private val oAuthViewModel: OAuthViewModel by viewModel()
	private lateinit var binding: ActivityAccountBinding

	private val navController by lazy {
		findNavController(R.id.account_nav_host_fragment).apply {
			val g = navInflater.inflate(R.navigation.account_nav_graph)
			g.startDestination = if (oAuthViewModel.isLogged()) R.id.navigation_account else R.id.navigation_login
			graph = g
		}
	}

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_account)
		setSupportActionBar(binding.toolbar)

		setupActionBarWithNavController(navController, AppBarConfiguration(setOf(
				R.id.navigation_account, R.id.navigation_login
		)))

		navController.addOnDestinationChangedListener { controller, destination, arguments ->

		}

		observe(ErrorHelper.error) { error ->
			if (error.displayed == 0)
			{
				Snackbar.make(binding.coordinator, error.title, Snackbar.LENGTH_LONG)
						.setAction(error.actionText) { error.action(this) }
						.show()
				error.displayed++
			}
		}
	}
}