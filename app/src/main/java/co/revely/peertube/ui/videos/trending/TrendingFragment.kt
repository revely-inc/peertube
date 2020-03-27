package co.revely.peertube.ui.videos.trending

import android.content.Context
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentTrendingBinding
import co.revely.peertube.ui.videos.VideosFragment

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class TrendingFragment : VideosFragment<FragmentTrendingBinding>(R.layout.fragment_trending)
{
	override fun videos() = videosViewModel.videosTrending
	override fun title(): String = getString(R.string.title_trending)
}
