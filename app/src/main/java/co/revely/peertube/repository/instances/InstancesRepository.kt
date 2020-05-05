package co.revely.peertube.repository.instances

import co.revely.peertube.api.instances.InstancesService
import co.revely.peertube.api.peertube.response.DataList
import co.revely.peertube.db.instances.entity.Instance
import co.revely.peertube.repository.NetworkBoundResource
import co.revely.peertube.utils.AppExecutors
import co.revely.peertube.utils.RateLimiter
import java.util.concurrent.TimeUnit

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class InstancesRepository(
	private val appExecutors: AppExecutors,
	private val instanceDao: Instance.Dao,
	private val instancesService: InstancesService)
{
	private val instancesRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

	fun getInstanceByHost(host: String) = instanceDao.loadByHost(host)

	fun getInstances() =
		object: NetworkBoundResource<List<Instance>, DataList<Instance>>(appExecutors) {
			override fun saveCallResult(item: DataList<Instance>) = item.data?.toTypedArray()?.let { instanceDao.insert(*it) } ?: Unit
			override fun shouldFetch(data: List<Instance>?) = data == null || data.isEmpty() || instancesRateLimit.shouldFetch("instances")
			override fun loadFromDb() = instanceDao.load()
			override fun createCall(data: List<Instance>?) = instancesService.instances()
		}.asLiveData()
}