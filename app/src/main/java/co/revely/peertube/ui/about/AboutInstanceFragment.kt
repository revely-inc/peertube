package co.revely.peertube.ui.about

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import co.revely.peertube.R
import co.revely.peertube.api.ApiSuccessResponse
import co.revely.peertube.api.peertube.PeerTubeService
import co.revely.peertube.databinding.FragmentAboutInstanceBinding
import co.revely.peertube.ui.LayoutFragment

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
class AboutInstanceFragment : LayoutFragment<FragmentAboutInstanceBinding>(R.layout.fragment_about_instance)
{
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
		val host = arguments!!.getString("host")!!
		PeerTubeService.instance(host).configAbout().observe(this, Observer {
			if (it is ApiSuccessResponse)
				binding.instance = it.body.instance
		})
	}
}