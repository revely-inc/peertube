package co.revely.peertube.ui.instances

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentInstancesBinding
import co.revely.peertube.helper.PreferencesHelper
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_instances.*
import org.jetbrains.anko.dip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class InstancesFragment : LayoutFragment<FragmentInstancesBinding>(R.layout.fragment_instances)
{
	val appExecutors: AppExecutors by inject()

	private val instancesViewModel: InstancesViewModel by viewModel()

	private var adapter by autoCleared<InstancesAdapter>()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		activity?.setTitle(R.string.instances)
		setHasOptionsMenu(true)

		adapter = InstancesAdapter(appExecutors) { instance ->
			PreferencesHelper.defaultHost.set(instance.host)
			val directions = InstancesFragmentDirections.actionInstancesToInstance(instance.host)
			findNavController().navigate(directions)
		}
		instances_list.adapter = adapter
		instances_list.layoutManager = LinearLayoutManager(context)
		instances_list.addItemDecoration(MarginItemDecoration(context!!.dip(8)))
		progress_bar.progress(true)
		initInstances()
	}

	private fun initInstances()
	{
		instancesViewModel.instances.observe(this, Observer {
			adapter.submitList(it?.data)
			progress_bar.progress(false)
		})
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
	{
		inflater.inflate(R.menu.menu_instances, menu)
		val search = menu.findItem(R.id.search).actionView as SearchView
		search.setOnQueryTextListener(object : SearchView.OnQueryTextListener
		{
			override fun onQueryTextSubmit(query: String?) = false

			override fun onQueryTextChange(newText: String?): Boolean
			{
				instancesViewModel.instances.value?.data?.filter {
					it.name.contains(newText.toString(), ignoreCase = true) || it.host.contains(newText.toString(), ignoreCase = true)
				}?.also {
					adapter.submitList(it)
				}
				return false
			}
		})
	}

//	override fun onOptionsItemSelected(item: MenuItem): Boolean
//	{
//		when (item.itemId)
//		{
//			else -> return super.onOptionsItemSelected(item)
//		}
//	}
}