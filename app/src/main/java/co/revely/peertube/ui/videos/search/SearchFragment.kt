package co.revely.peertube.ui.videos.search

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import co.revely.peertube.R
import co.revely.peertube.api.dao.VideoDao
import co.revely.peertube.databinding.FragmentSearchBinding
import co.revely.peertube.ui.MainActivity
import co.revely.peertube.ui.videos.VideosFragment
import co.revely.peertube.utils.Listing
import co.revely.peertube.utils.hideKeyboard
import co.revely.peertube.utils.progress


/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class SearchFragment : VideosFragment<FragmentSearchBinding>(R.layout.fragment_search)
{
	private val handler = Handler()
	private var videosByQuery: Listing<VideoDao>? = null

	override fun videos() = videosByQuery

	override fun title(): String? = null

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)
		binding.progressBar.progress(false)
	}

	override fun onDestroyView()
	{
		(activity as MainActivity).binding.mainMotionLayout.hideKeyboard()
		super.onDestroyView()
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
	{
		inflater.inflate(R.menu.menu_search, menu)
		val searchItem = menu.findItem(R.id.search)
		val searchView = searchItem.actionView as SearchView
		searchItem.expandActionView()
		searchView.requestFocus()
		searchView.findViewById<View>(R.id.search_plate).setBackgroundColor(Color.TRANSPARENT)

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
