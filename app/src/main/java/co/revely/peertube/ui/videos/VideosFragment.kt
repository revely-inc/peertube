package co.revely.peertube.ui.videos

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.revely.peertube.MainNavGraphDirections
import co.revely.peertube.R
import co.revely.peertube.api.dao.VideoDao
import co.revely.peertube.repository.NetworkState
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.ui.MainActivity
import co.revely.peertube.utils.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

/**
 * Created at 2019-10-18
 *
 * @author rbenjami
 */
abstract class VideosFragment<DB : ViewDataBinding>(@LayoutRes layoutId: Int): LayoutFragment<DB>(layoutId)
{
	private var adapter: VideosAdapter by autoCleared()
	private lateinit var videosList: RecyclerView
	private lateinit var progressBar: ImageView
	private var noResultFoundError: Group? = null

	protected val appExecutors: AppExecutors by inject()
	protected val videosViewModel: VideosViewModel by sharedViewModel()

	protected abstract fun videos(): Listing<VideoDao>?

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		videosList = view.findViewById(R.id.videos_list)
		progressBar = view.findViewById(R.id.progress_bar)
		noResultFoundError = view.findViewById(R.id.no_result_found_error)

		setHasOptionsMenu(true)
		initVideos()
	}

	private fun initVideos()
	{
		adapter = VideosAdapter(appExecutors) {
			(activity as MainActivity).openVideoFragment(it.id)
		}
		videosList.adapter = adapter
		videosList.layoutManager = LinearLayoutManager(context)
		ContextCompat.getDrawable(requireContext(), R.drawable.line_divider)?.also {
			videosList.addItemDecoration(MarginItemDecoration(it))
		}

		progressBar.progress(true)
		updateVideos()
	}

	fun updateVideos()
	{
		noResultFoundError?.invisible()
		videos()?.apply {
			observe(pagedList) {
				if (it.isEmpty())
					noResultFoundError?.visible()
				adapter.submitList(it)
				progressBar.progress(false)
			}
			observe(refreshState) {
				(activity as MainActivity).binding.swipeRefresh.isRefreshing = it == NetworkState.LOADING
			}
//			observe(networkState) {
//			}

//			(activity as MainActivity).binding.swipeRefresh.apply {
//				isEnabled = true
//				setOnRefreshListener { refresh() }
//			}
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