package co.revely.peertube.ui.about

import android.content.Context
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
	override fun title(context: Context): String = context.getString(R.string.title_about)
}