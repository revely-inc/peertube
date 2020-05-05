package co.revely.peertube.ui.instances

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import co.revely.peertube.db.instances.entity.Instance
import co.revely.peertube.repository.Resource
import co.revely.peertube.repository.instances.InstancesRepository

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class InstancesViewModel(private val instancesRepo: InstancesRepository) : ViewModel()
{
	private val loadTrigger = MutableLiveData(Unit)

	fun refresh() {
		loadTrigger.value = Unit
	}

	val instances: LiveData<Resource<List<Instance>>> = loadTrigger.switchMap { instancesRepo.getInstances() }
}