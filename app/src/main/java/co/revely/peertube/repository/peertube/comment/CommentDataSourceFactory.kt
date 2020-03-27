package co.revely.peertube.repository.peertube.comment

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import co.revely.peertube.api.peertube.PeerTubeService
import co.revely.peertube.api.peertube.query.CommentQuery
import co.revely.peertube.api.peertube.response.Comment
import java.util.concurrent.Executor

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
class CommentDataSourceFactory(
		private val peerTubeService: PeerTubeService,
		private val commentQuery: CommentQuery,
		private val retryExecutor: Executor
) : DataSource.Factory<Int, Comment>()
{
	val sourceLiveData = MutableLiveData<PageKeyedCommentDataSource>()

	override fun create(): DataSource<Int, Comment>
	{
		val source = PageKeyedCommentDataSource(peerTubeService, commentQuery, retryExecutor)
		sourceLiveData.postValue(source)
		return source
	}
}