package co.revely.peertube.ui.videos.local

import android.content.Context
import android.os.Bundle
import android.view.View
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentLocalBinding
import co.revely.peertube.ui.videos.VideosFragment

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class LocalFragment : VideosFragment<FragmentLocalBinding>(R.layout.fragment_local)
{
	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)
	}

	override fun videos() = videosViewModel.videosLocal
	override fun title(context: Context): String = context.getString(R.string.title_local)
}
