package co.revely.peertube.ui.videos

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import co.revely.peertube.R
import co.revely.peertube.api.peertube.response.Video
import co.revely.peertube.repository.NetworkState
import co.revely.peertube.ui.MainActivity
import co.revely.peertube.ui.UserMenuFragment
import co.revely.peertube.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_overview.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created at 2019-10-18
 *
 * @author rbenjami
 */
abstract class VideosFragment<DB : ViewDataBinding>(@LayoutRes layoutId: Int): UserMenuFragment<DB>(layoutId)
{
	protected val appExecutors: AppExecutors by inject()
	protected val videosViewModel: VideosViewModel by sharedViewModel(parameters = { parametersOf(host) })

	private var adapter: VideosAdapter by autoCleared()

	protected abstract fun videos(): Listing<Video>

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		activity?.navigation?.visible()
		initVideos()
	}

	private fun initVideos()
	{
		adapter = VideosAdapter(host, appExecutors) {
//			val direction = InstanceNavGraphDirections.actionGlobalNavigationVideo(
//					host, it.id
//			)
//			findNavController().navigate(direction)
			(activity as MainActivity).openVideoFragment(host, it.id)
		}
		videos_list.adapter = adapter
		videos_list.layoutManager = LinearLayoutManager(context)
		ContextCompat.getDrawable(context!!, R.drawable.line_divider)?.also {
			videos_list.addItemDecoration(MarginItemDecoration(it))
		}

		progress_bar.progress(true)
		videos().apply {
			observe(pagedList) {
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
}