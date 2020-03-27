package co.revely.peertube.repository.peertube.comment

import co.revely.peertube.api.peertube.PeerTubeService
import co.revely.peertube.api.peertube.query.CommentQuery
import co.revely.peertube.api.peertube.response.Comment
import co.revely.peertube.repository.peertube.PageKeyedDataSource
import java.util.concurrent.Executor

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
class PageKeyedCommentDataSource(
		private val peerTubeService: PeerTubeService,
		private val commentQuery: CommentQuery,
		retryExecutor: Executor
) : PageKeyedDataSource<Comment>(retryExecutor) {

	override fun loadInitialRequest(params: LoadInitialParams<Int>) =
		peerTubeService.getComments(
				commentQuery.id,
				commentQuery.sort,
				params.requestedLoadSize,
				0
		)

	override fun loadAfterRequest(params: LoadParams<Int>) =
			peerTubeService.getComments(
					commentQuery.id,
					commentQuery.sort,
					params.requestedLoadSize,
					params.key
			)
}
