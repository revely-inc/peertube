package co.revely.peertube.ui.videos.overview

import android.content.Context
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentOverviewBinding
import co.revely.peertube.ui.videos.VideosFragment

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class OverviewFragment : VideosFragment<FragmentOverviewBinding>(R.layout.fragment_overview)
{
	override fun videos() = videosViewModel.videos
	override fun title(): String = getString(R.string.title_overview)
}
