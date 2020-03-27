package co.revely.peertube.api.instances

import androidx.lifecycle.LiveData
import co.revely.peertube.api.ApiResponse
import co.revely.peertube.api.peertube.response.DataList
import co.revely.peertube.db.instances.entity.Instance
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
interface InstancesService
{
	companion object
	{
		const val API_BASE_URL = "https://instances.joinpeertube.org/"
		const val API_VERSION = "api/v1"
	}

	@GET("$API_VERSION/instances")
	fun instances(
			//Number of instances
			@Query("count") count: Int = Int.MAX_VALUE,
			//Whether to show healthy instances only or non-healthy instances only (otherwise both are shown)
			@Query("healthy") healthy: Boolean? = null,
			//Whether to only show instances where registrations are open, or where they are closed
			@Query("signup") signup: Boolean? = null,
			//Sort order
			@Query("sort") sort: String? = null,
			//Offset
			@Query("start") start: Int? = null
	): LiveData<ApiResponse<DataList<Instance>>>
}
