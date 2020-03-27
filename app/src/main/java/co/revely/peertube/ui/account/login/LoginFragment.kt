package co.revely.peertube.ui.account.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentLoginBinding
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.utils.observe
import co.revely.peertube.viewmodel.OAuthViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created at 2019-10-18
 *
 * @author rbenjami
 */
class LoginFragment : LayoutFragment<FragmentLoginBinding>(R.layout.fragment_login)
{
	private val args: LoginFragmentArgs by navArgs()
	private val viewModel: LoginViewModel by sharedViewModel(parameters = { parametersOf(args.host)})
	private val oAuthViewModel: OAuthViewModel by sharedViewModel(parameters = { parametersOf(args.host)})

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