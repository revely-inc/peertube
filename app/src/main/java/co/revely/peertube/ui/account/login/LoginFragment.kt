package co.revely.peertube.ui.account.login

import android.os.Bundle
import android.view.View
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentLoginBinding
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.utils.observe
import co.revely.peertube.viewmodel.OAuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

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
		observe(oAuthViewModel.token) {
		}

		login.setOnClickListener {
			oAuthViewModel.login(username.text.toString(), password.text.toString())
		}
	}
}