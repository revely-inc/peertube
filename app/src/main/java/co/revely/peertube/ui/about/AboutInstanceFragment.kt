package co.revely.peertube.ui.about

import android.os.Bundle
import android.view.View
import co.revely.peertube.R
import co.revely.peertube.api.ApiSuccessResponse
import co.revely.peertube.api.peertube.PeerTubeService
import co.revely.peertube.databinding.FragmentAboutInstanceBinding
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.utils.observe
import co.revely.peertube.viewmodel.InstanceViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
class AboutInstanceFragment : LayoutFragment<FragmentAboutInstanceBinding>(R.layout.fragment_about_instance)
{
	private val instanceViewModel: InstanceViewModel by sharedViewModel()

	override fun title(): String = getString(R.string.title_about)

	companion object
	{
		fun newInstance(): AboutInstanceFragment
		{
			return AboutInstanceFragment()
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		observe(instanceViewModel.configAbout) {
			if (it is ApiSuccessResponse)
				binding.instance = it.body.instance
		}
		observe(instanceViewModel.serverStats) {
			if (it is ApiSuccessResponse)
				binding.stats = it.body
		}
	}
}