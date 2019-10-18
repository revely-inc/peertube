package co.revely.peertube.ui.videos

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import co.revely.peertube.InstanceNavGraphDirections
import co.revely.peertube.R
import co.revely.peertube.db.peertube.entity.Video
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_overview.*
import org.jetbrains.anko.dip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Created at 2019-10-18
 *
 * @author rbenjami
 */
abstract class VideosFragment <DB : ViewDataBinding>(@LayoutRes layoutId: Int): LayoutFragment<DB>(layoutId)
{
	protected val appExecutors: AppExecutors by inject()
	protected val videosViewModel: VideosViewModel by viewModel(parameters = { parametersOf(host) })
	protected val host by lazy { arguments?.getString("host")!! }

	protected var adapter: VideosAdapter by autoCleared()

	protected abstract fun videos(): LiveData<PagedList<Video>>
	protected abstract fun title(context: Context): String

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		activity?.navigation?.visible()
		(activity as? AppCompatActivity)?.supportActionBar?.title = title(view.context)
		setHasOptionsMenu(true)
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

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
			inflater.inflate(R.menu.menu_videos, menu)

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId)
		{
			R.id.account -> {
				val direction = InstanceNavGraphDirections.actionGlobalNavigationAccount(host)
				findNavController().navigate(direction)
			}
			else -> return false
		}
		return true
	}
}