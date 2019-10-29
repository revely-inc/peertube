package co.revely.peertube.ui.account

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.revely.peertube.R
import co.revely.peertube.api.ApiSuccessResponse
import co.revely.peertube.databinding.FragmentAccountBinding
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.ui.account.login.LoginFragmentArgs
import co.revely.peertube.utils.observe
import co.revely.peertube.viewmodel.OAuthViewModel
import co.revely.peertube.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created at 2019-10-21
 *
 * @author rbenjami
 */
class AccountFragment : LayoutFragment<FragmentAccountBinding>(R.layout.fragment_account)
{
	private val args: LoginFragmentArgs by navArgs()
	private val oAuthViewModel: OAuthViewModel by sharedViewModel(parameters = { parametersOf(args.host) })
	private val userViewModel: UserViewModel by sharedViewModel(parameters = { parametersOf(args.host, oAuthViewModel) })

	override fun title(context: Context): String = context.getString(R.string.account)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		observe(userViewModel.me()) {
			if (it is ApiSuccessResponse && isVisible)
				binding.user = it.body
			else
				findNavController().navigate(AccountFragmentDirections.actionAccountToLogin(args.host))
		}
	}
}