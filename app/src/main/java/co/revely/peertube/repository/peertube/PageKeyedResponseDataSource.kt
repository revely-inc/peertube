package co.revely.peertube.repository.peertube

import co.revely.peertube.api.PeerTubeService
import co.revely.peertube.api.DataList
import retrofit2.Call
import java.util.concurrent.Executor

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
class PageKeyedResponseDataSource<T>(
    private val peerTubeService: PeerTubeService,
    retryExecutor: Executor,
    private val request: ((peerTubeService: PeerTubeService, size: Int, index: Int) -> Call<DataList<T>>)
) : PageKeyedDataSource<T>(retryExecutor) {

	override fun loadInitialRequest(params: LoadInitialParams<Int>) =
			request(peerTubeService, params.requestedLoadSize, 0)

	override fun loadAfterRequest(params: LoadParams<Int>) =
			request(peerTubeService, params.requestedLoadSize, params.key)
}
