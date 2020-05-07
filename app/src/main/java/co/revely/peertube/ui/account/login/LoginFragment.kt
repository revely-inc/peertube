package co.revely.peertube.ui.account.login

import android.os.Bundle
import android.view.View
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentLoginBinding
import co.revely.peertube.helper.PreferencesHelper
import co.revely.peertube.repository.Status
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.utils.observe
import co.revely.peertube.utils.progress
import co.revely.peertube.viewmodel.ErrorHelper
import co.revely.peertube.viewmodel.OAuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.lang.Error

/**
 * Created at 2019-10-18
 *
 * @author rbenjami
 */
class LoginFragment : LayoutFragment<FragmentLoginBinding>(R.layout.fragment_login)
{
	private val viewModel: LoginViewModel by sharedViewModel()
	private val oAuthViewModel: OAuthViewModel by sharedViewModel()

	override fun title() = getString(R.string.login)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		binding.host = PreferencesHelper.defaultHost.get()
		observe(oAuthViewModel.token) {
			if (it?.status == Status.ERROR && it.message != null) {
				ErrorHelper.setError(ErrorHelper.Retry {
					oAuthViewModel.login(username.text.toString(), password.text.toString())
				})
			}
			else if (it?.status == Status.SUCCESS && it.data != null)
				activity?.finish()
		}

		login.setOnClickListener {
			oAuthViewModel.login(username.text.toString(), password.text.toString())
//			progress_bar.progress(true)
		}
	}
}