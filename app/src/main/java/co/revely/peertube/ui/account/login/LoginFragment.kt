package co.revely.peertube.ui.account.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentLoginBinding
import co.revely.peertube.ui.LayoutFragment
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

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		(activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.login)

		oAuthViewModel.token().observe(this) {
		}

		login.setOnClickListener {
			oAuthViewModel.login(username.text.toString(), password.text.toString())
		}
	}
}