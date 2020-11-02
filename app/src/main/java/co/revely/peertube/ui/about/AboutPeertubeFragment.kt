package co.revely.peertube.ui.about

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentAboutPeertubeBinding
import co.revely.peertube.ui.LayoutFragment


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
		fun newInstance(): AboutPeertubeFragment
		{
			return AboutPeertubeFragment()
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		binding.webView.webViewClient = WebViewClient()
		binding.webView.settings.javaScriptEnabled = true
		binding.webView.settings.domStorageEnabled = true
		binding.webView.loadUrl("https://joinpeertube.org/")
	}
}