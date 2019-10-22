package co.revely.peertube.ui.videos

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import co.revely.peertube.InstanceNavGraphDirections
import co.revely.peertube.api.peertube.response.Video
import co.revely.peertube.ui.UserMenuFragment
import co.revely.peertube.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_overview.*
import org.jetbrains.anko.dip
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

	protected var adapter: VideosAdapter by autoCleared()

	protected abstract fun videos(): LiveData<PagedList<Video>>

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		activity?.navigation?.visible()
		adapter = VideosAdapter(host, appExecutors) {
			val direction = InstanceNavGraphDirections.actionGlobalNavigationVideo(
							host, it.id
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
		videos().observe(this, Observer {
			adapter.submitList(it)
			progress_bar.progress(false)
		})
	}
}