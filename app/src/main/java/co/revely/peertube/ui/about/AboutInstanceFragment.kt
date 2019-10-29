package co.revely.peertube.ui.about

import android.content.Context
import android.os.Bundle
import android.view.View
import co.revely.peertube.R
import co.revely.peertube.api.ApiSuccessResponse
import co.revely.peertube.api.peertube.PeerTubeService
import co.revely.peertube.databinding.FragmentAboutInstanceBinding
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.utils.observe
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
class AboutInstanceFragment : LayoutFragment<FragmentAboutInstanceBinding>(R.layout.fragment_about_instance)
{
	private val peerTubeService: PeerTubeService by inject(parameters = { parametersOf(arguments!!.getString("host")!!)})

	override fun title(context: Context): String = context.getString(R.string.title_about)

	companion object
	{
		fun newInstance(host: String): AboutInstanceFragment
		{
			val f = AboutInstanceFragment()
			val args = Bundle()
			args.putString("host", host)
			f.arguments = args
			return f
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		observe(peerTubeService.configAbout()) {
			if (it is ApiSuccessResponse)
				binding.instance = it.body.instance
		}
	}
}