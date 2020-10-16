package co.revely.peertube.ui.videos.search

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuItemCompat
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import co.revely.peertube.R
import co.revely.peertube.api.peertube.response.Video
import co.revely.peertube.databinding.FragmentSearchBinding
import co.revely.peertube.ui.videos.VideosFragment
import co.revely.peertube.utils.Listing
import co.revely.peertube.utils.hideKeyboard
import co.revely.peertube.utils.progress
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.progress_bar
import kotlinx.android.synthetic.main.view_stat.view.*
import timber.log.Timber


/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class SearchFragment : VideosFragment<FragmentSearchBinding>(R.layout.fragment_search)
{
	private val handler = Handler()
	private var videosByQuery: Listing<Video>? = null

	override fun videos() = videosByQuery

	override fun title(): String? = null

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)
		progress_bar.progress(false)
	}

	override fun onDestroyView()
	{
		activity?.main_motion_layout?.hideKeyboard()
		super.onDestroyView()
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
	{
		inflater.inflate(R.menu.menu_search, menu)
		val searchItem = menu.findItem(R.id.search)
		val searchView = searchItem.actionView as SearchView
		searchItem.expandActionView()
		searchView.requestFocus()
		searchView.findViewById<View>(androidx.appcompat.R.id.search_plate).setBackgroundColor(Color.TRANSPARENT)

		searchItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
			override fun onMenuItemActionExpand(item: MenuItem?) = false
			override fun onMenuItemActionCollapse(item: MenuItem?) = findNavController().popBackStack()
		})

		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
		{
			override fun onQueryTextSubmit(query: String?) = false

			override fun onQueryTextChange(newText: String?): Boolean
			{
				newText?.let { query(it) }
				return false
			}
		})
	}

	private fun query(query: String)
	{
		handler.removeCallbacksAndMessages(null)
		videosByQuery = videosViewModel.videosByQuery(query)
		handler.postDelayed({
			updateVideos()
		}, 500)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId) {
			android.R.id.home ->
			{
//				activity?.supportFragmentManager?.popBackStackImmediate()
				return true
			}
		}
		return false
	}
}
