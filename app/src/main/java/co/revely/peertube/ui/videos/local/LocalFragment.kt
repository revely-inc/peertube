package co.revely.peertube.ui.videos.local

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentLocalBinding
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.ui.videos.VideosAdapter
import co.revely.peertube.ui.videos.VideosViewModel
import co.revely.peertube.ui.videos.overview.OverviewFragmentArgs
import co.revely.peertube.utils.AppExecutors
import co.revely.peertube.utils.MarginItemDecoration
import co.revely.peertube.utils.autoCleared
import co.revely.peertube.utils.progress
import kotlinx.android.synthetic.main.fragment_overview.*
import org.jetbrains.anko.dip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class LocalFragment : LayoutFragment<FragmentLocalBinding>(R.layout.fragment_local)
{
	private val appExecutors: AppExecutors  by inject()
	private val args: OverviewFragmentArgs by navArgs()
	private val videosViewModel: VideosViewModel by viewModel(parameters = { parametersOf(args.host) })

	private var adapter: VideosAdapter by autoCleared()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		setHasOptionsMenu(true)
		adapter = VideosAdapter(args.host, appExecutors) {
			val direction =
				LocalFragmentDirections.actionLocalToVideo(
					args.host, it.id
				)
			findNavController().navigate(direction)
		}
		videos_list.adapter = adapter
		videos_list.layoutManager = LinearLayoutManager(context)
		videos_list.addItemDecoration(MarginItemDecoration(context!!.dip(8)))
		progress_bar.progress(true)
		initVideos()
	}

	private fun initVideos()
	{
		videosViewModel.videosLocal.observe(this, Observer {
			adapter.submitList(it)
			progress_bar.progress(false)
		})
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
		inflater.inflate(R.menu.menu_videos, menu)
}
