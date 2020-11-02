package co.revely.peertube.repository.peertube.video

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import co.revely.peertube.api.PeerTubeService
import co.revely.peertube.api.DataList
import retrofit2.Call
import java.util.concurrent.Executor

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
class DataSourceFactory<T>(
    private val peerTubeService: PeerTubeService,
    private val retryExecutor: Executor,
    private val request: ((peerTubeService: PeerTubeService, size: Int, index: Int) -> Call<DataList<T>>)
) : DataSource.Factory<Int, T>()
{
	val sourceLiveData = MutableLiveData<PageKeyedResponseDataSource<T>>()

	override fun create(): DataSource<Int, T>
	{
		val source = PageKeyedResponseDataSource(peerTubeService, retryExecutor, request)
		sourceLiveData.postValue(source)
		return source
	}
}