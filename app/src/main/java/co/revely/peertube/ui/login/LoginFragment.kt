package co.revely.peertube.ui.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentLoginBinding
import co.revely.peertube.ui.LayoutFragment

/**
 * Created at 2019-10-18
 *
 * @author rbenjami
 */
class LoginFragment : LayoutFragment<FragmentLoginBinding>(R.layout.fragment_login)
{
	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		(activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.login)
	}
}