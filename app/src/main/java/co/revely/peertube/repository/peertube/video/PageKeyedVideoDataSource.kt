package co.revely.peertube.repository.peertube.video

import co.revely.peertube.api.peertube.PeerTubeService
import co.revely.peertube.api.peertube.query.VideoQuery
import co.revely.peertube.api.peertube.response.Video
import co.revely.peertube.repository.peertube.PageKeyedDataSource
import java.util.concurrent.Executor

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
class PageKeyedVideoDataSource(
	private val peerTubeService: PeerTubeService,
	private val videoQuery: VideoQuery?,
	retryExecutor: Executor
) : PageKeyedDataSource<Video>(retryExecutor) {

	override fun loadInitialRequest(params: LoadInitialParams<Int>) =
			peerTubeService.getVideos(
					videoQuery?.categoryOneOf,
					params.requestedLoadSize,
					videoQuery?.filter,
					videoQuery?.languageOneOf,
					videoQuery?.licenceOneOf,
					videoQuery?.nsfw,
					videoQuery?.sort,
					0,
					videoQuery?.tagsAllOf,
					videoQuery?.tagsOneOf
			)

	override fun loadAfterRequest(params: LoadParams<Int>) =
			peerTubeService.getVideos(
					videoQuery?.categoryOneOf,
					params.requestedLoadSize,
					videoQuery?.filter,
					videoQuery?.languageOneOf,
					videoQuery?.licenceOneOf,
					videoQuery?.nsfw,
					videoQuery?.sort,
					params.key,
					videoQuery?.tagsAllOf,
					videoQuery?.tagsOneOf
			)
}
