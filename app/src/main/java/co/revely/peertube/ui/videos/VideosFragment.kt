package co.revely.peertube.ui.videos

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.revely.peertube.MainNavGraphDirections
import co.revely.peertube.R
import co.revely.peertube.api.peertube.response.Video
import co.revely.peertube.repository.NetworkState
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.ui.MainActivity
import co.revely.peertube.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.fragment_overview.progress_bar
import kotlinx.android.synthetic.main.fragment_overview.videos_list
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Created at 2019-10-18
 *
 * @author rbenjami
 */
abstract class VideosFragment<DB : ViewDataBinding>(@LayoutRes layoutId: Int): LayoutFragment<DB>(layoutId)
{
	protected val appExecutors: AppExecutors by inject()
	protected val videosViewModel: VideosViewModel by sharedViewModel()

	private var adapter: VideosAdapter by autoCleared()

	protected abstract fun videos(): Listing<Video>?

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		activity?.navigation?.visible()
		setHasOptionsMenu(true)
		initVideos()
	}

	private fun initVideos()
	{
		adapter = VideosAdapter(appExecutors) {
//			val direction = InstanceNavGraphDirections.actionGlobalNavigationVideo(
//					host, it.id
//			)
//			findNavController().navigate(direction)
			(activity as MainActivity).openVideoFragment(it.id)
		}
		videos_list.adapter = adapter
		videos_list.layoutManager = LinearLayoutManager(context)
		ContextCompat.getDrawable(requireContext(), R.drawable.line_divider)?.also {
			videos_list.addItemDecoration(MarginItemDecoration(it))
		}

		progress_bar.progress(true)
		updateVideos()
	}

	fun updateVideos()
	{
		no_result_found_error?.invisible()
		videos()?.apply {
			observe(pagedList) {
				if (it.isEmpty())
					no_result_found_error?.visible()
				adapter.submitList(it)
				progress_bar.progress(false)
			}
			observe(refreshState) {
				activity?.swipe_refresh?.isRefreshing = it == NetworkState.LOADING
			}
//			observe(networkState) {
//			}

			activity?.swipe_refresh?.apply {
				isEnabled = true
				setOnRefreshListener { refresh() }
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
	{
		inflater.inflate(R.menu.menu_videos, menu)
		menu.findItem(R.id.search).setOnMenuItemClickListener {
			findNavController().navigate(MainNavGraphDirections.actionGlobalNavigationSearch())
			return@setOnMenuItemClickListener true
		}
	}
}