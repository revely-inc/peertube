package co.revely.peertube.ui.instances

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import co.revely.peertube.R
import co.revely.peertube.databinding.ActivityInstancesBinding
import co.revely.peertube.helper.PreferencesHelper
import co.revely.peertube.repository.Status
import co.revely.peertube.ui.SplashActivity
import co.revely.peertube.utils.AppExecutors
import co.revely.peertube.utils.MarginItemDecoration
import co.revely.peertube.utils.autoCleared
import co.revely.peertube.utils.observe
import org.jetbrains.anko.intentFor
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created at 06/05/2020
 *
 * @author rbenjami
 */
class InstancesActivity: AppCompatActivity()
{
	val appExecutors: AppExecutors by inject()

	private val instancesViewModel: InstancesViewModel by viewModel()
	private val hostsLogged = PreferencesHelper.hostsLogged.get()

	private var adapter by autoCleared<InstancesAdapter>()

	private lateinit var binding: ActivityInstancesBinding

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_instances)

		setSupportActionBar(binding.toolbar)
		supportActionBar?.setTitle(R.string.instances)

		adapter = InstancesAdapter(appExecutors) { instance ->
			if (PreferencesHelper.currentHost.get() == instance.host)
			{
				finish()
				return@InstancesAdapter
			}
			PreferencesHelper.currentHost.set(instance.host)
			startActivity(intentFor<SplashActivity>())
			finish()
		}
		binding.instancesList.adapter = adapter
		ContextCompat.getDrawable(this, R.drawable.line_divider)?.also {
			binding.instancesList.addItemDecoration(MarginItemDecoration(it))
		}
		binding.swipeRefresh.isRefreshing = true
		binding.swipeRefresh.setOnRefreshListener { instancesViewModel.refresh() }
		initInstances()
	}

	private fun initInstances()
	{
		observe(instancesViewModel.instances) {
			binding.swipeRefresh.isRefreshing = it.status == Status.LOADING
			adapter.submitList(it.data?.sortedByDescending { it.host in hostsLogged })
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean
	{
		menuInflater.inflate(R.menu.menu_instances, menu)
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
		return true
	}
}