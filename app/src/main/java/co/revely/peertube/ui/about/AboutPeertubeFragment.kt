package co.revely.peertube.ui.about

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentAboutPeertubeBinding
import co.revely.peertube.ui.LayoutFragment
import kotlinx.android.synthetic.main.fragment_about_peertube.*


/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
class AboutPeertubeFragment : LayoutFragment<FragmentAboutPeertubeBinding>(R.layout.fragment_about_peertube)
{
	override fun title(): String = getString(R.string.title_about)

	companion object
	{
		fun newInstance(host: String): AboutPeertubeFragment
		{
			val f = AboutPeertubeFragment()
			val args = Bundle()
			args.putString("host", host)
			f.arguments = args
			return f
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		web_view.webViewClient = WebViewClient()
		web_view.settings.javaScriptEnabled = true
		web_view.loadUrl("http://${arguments!!.getString("host")!!}/about/peertube")
	}
}