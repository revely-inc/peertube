package co.revely.peertube.ui.instances

import androidx.lifecycle.ViewModel
import co.revely.peertube.repository.instances.InstancesRepository

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class InstancesViewModel(instancesRepo: InstancesRepository) : ViewModel()
{
	val instances = instancesRepo.getInstances()
}