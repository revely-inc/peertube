package co.revely.peertube.ui.account

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentAccountBinding
import co.revely.peertube.ui.LayoutFragment
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
	private val oAuthViewModel: OAuthViewModel by sharedViewModel()
	private val userViewModel: UserViewModel by sharedViewModel(parameters = { parametersOf(oAuthViewModel) })

	override fun title(): String = getString(R.string.account)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
	}
}