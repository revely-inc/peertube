package co.revely.peertube.repository.instances

import co.revely.peertube.api.ArrayResponse
import co.revely.peertube.api.instances.InstancesService
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
		object: NetworkBoundResource<List<Instance>, ArrayResponse<Instance>>(appExecutors) {
			override fun saveCallResult(item: ArrayResponse<Instance>) = instanceDao.insert(*item.data.toTypedArray())
			override fun shouldFetch(data: List<Instance>?) = data == null || data.isEmpty() || instancesRateLimit.shouldFetch("instances")
			override fun loadFromDb() = instanceDao.load()
			override fun createCall() = instancesService.instances()
		}.asLiveData()
}