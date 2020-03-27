package co.revely.peertube.ui.videos.recent

import android.content.Context
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentRecentBinding
import co.revely.peertube.ui.videos.VideosFragment

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class RecentFragment : VideosFragment<FragmentRecentBinding>(R.layout.fragment_recent)
{
	override fun videos() = videosViewModel.videosRecent
	override fun title(): String = getString(R.string.title_recently_added)
}
