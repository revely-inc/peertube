package co.revely.peertube.repository.peertube.comment

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import co.revely.peertube.api.PeerTubeService
import co.revely.peertube.api.dto.CommentDto
import co.revely.peertube.api.dao.CommentDao
import java.util.concurrent.Executor

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
class CommentDataSourceFactory(
    private val peerTubeService: PeerTubeService,
    private val commentQuery: CommentDto,
    private val retryExecutor: Executor
) : DataSource.Factory<Int, CommentDao>()
{
	val sourceLiveData = MutableLiveData<PageKeyedCommentDataSource>()

	override fun create(): DataSource<Int, CommentDao>
	{
		val source = PageKeyedCommentDataSource(peerTubeService, commentQuery, retryExecutor)
		sourceLiveData.postValue(source)
		return source
	}
}